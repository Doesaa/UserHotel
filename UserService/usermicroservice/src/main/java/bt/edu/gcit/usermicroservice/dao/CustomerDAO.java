package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Customer;
import java.util.List;

public interface CustomerDAO {
    Customer saveCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    void deleteCustomer(Long id);
    Customer findCustomerById(Long id);
    Customer findCustomerByEmail(String email);
    List<Customer> findAllCustomers();
    List<Customer> findCustomersByName(String name);
    long countCustomers();
    boolean existsByEmail(String email);
}