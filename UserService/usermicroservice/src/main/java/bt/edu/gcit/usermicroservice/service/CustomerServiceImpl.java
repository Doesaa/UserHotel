package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dao.CustomerDAO;
import bt.edu.gcit.usermicroservice.entity.Customer;
import bt.edu.gcit.usermicroservice.service.CustomerService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerServiceImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerDAO.saveCustomer(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerDAO.deleteCustomer(id);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerDAO.findCustomerById(id);
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerDAO.findCustomerByEmail(email);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.findAllCustomers();
    }

    @Override
    public List<Customer> searchCustomersByName(String name) {
        return customerDAO.findCustomersByName(name);
    }

    @Override
    public long getCustomerCount() {
        return customerDAO.countCustomers();
    }

    @Override
    public boolean emailExists(String email) {
        return customerDAO.existsByEmail(email);
    }
}