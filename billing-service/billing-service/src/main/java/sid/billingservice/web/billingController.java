package sid.billingservice.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sid.billingservice.dao.BillRepository;
import sid.billingservice.dao.ProductItemRepository;
import sid.billingservice.entities.Bill;
import sid.billingservice.feign.CustomerRestClient;
import sid.billingservice.feign.ProductRestClient;

@RestController
public class billingController {
    @Autowired
    private BillRepository billRepository;
    @Autowired private ProductItemRepository productItemRepository;
    @Autowired private CustomerRestClient customerRestClient;
    @Autowired private ProductRestClient productRestClient;
    @GetMapping("/bills/full/{id}")
    Bill getBill(@PathVariable(name="id") Long id){
        Bill bill=billRepository.findById(id).get();
        bill.setCustomer(customerRestClient.findCustomerById(bill.getCustomerID()));
        bill.setProductItems(productItemRepository.findProductItemByBillId(id));
        bill.getProductItems().forEach(pi->{
            pi.setProduct(productRestClient.getProductById(pi.getProductId()));
        });
        return bill; }
}
