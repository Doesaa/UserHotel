package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.Customer;
import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    void deleteCustomer(Long id);
    Customer getCustomerById(Long id);
    Customer getCustomerByEmail(String email);
    List<Customer> getAllCustomers();
    List<Customer> searchCustomersByName(String name);
    long getCustomerCount();
    boolean emailExists(String email);
}