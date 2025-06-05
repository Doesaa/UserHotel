package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.dao.CustomerDAO;
import bt.edu.gcit.usermicroservice.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Customer saveCustomer(Customer customer) {
        entityManager.persist(customer);
        return customer;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return entityManager.merge(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = entityManager.find(Customer.class, id);
        if (customer != null) {
            entityManager.remove(customer);
        }
    }

    @Override
    public Customer findCustomerById(Long id) {
        return entityManager.find(Customer.class, id);
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        TypedQuery<Customer> query = entityManager.createQuery(
            "SELECT c FROM Customer c WHERE c.email = :email", Customer.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }

    @Override
    public List<Customer> findAllCustomers() {
        TypedQuery<Customer> query = entityManager.createQuery(
            "SELECT c FROM Customer c", Customer.class);
        return query.getResultList();
    }

    @Override
    public List<Customer> findCustomersByName(String name) {
        TypedQuery<Customer> query = entityManager.createQuery(
            "SELECT c FROM Customer c WHERE c.name LIKE :name", Customer.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    @Override
    public long countCustomers() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(c) FROM Customer c", Long.class);
        return query.getSingleResult();
    }

    @Override
    public boolean existsByEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(c) FROM Customer c WHERE c.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }
}