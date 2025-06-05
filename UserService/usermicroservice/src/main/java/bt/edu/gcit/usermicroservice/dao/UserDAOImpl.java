package bt.edu.gcit.usermicroservice.dao;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class UserDAOImpl implements UserDAO {
    private EntityManager entityManager;
    @Autowired
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public User save(User user) {
        return entityManager.merge(user);
    }

    @Override
    public List<User> getAllUsers() {
        TypedQuery<User> query = entityManager.createQuery("FROM User", User.class);
        return query.getResultList();
    }



    @Override
    public User findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("from User where email = :email", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        System.out.println(users.size());
        if (users.isEmpty()) {
            return null;
        } else {
        System.out.println(users.get(0)+" "+users.get(0).getEmail());
            return users.get(0);
        }
    }

    @Override
    public User findByID(int theId) {
    // Implement the logic to find a user by their ID in the database
    // and return the user object
        User user = entityManager.find(User.class, theId);
        return user;
    }

    @Override
    public void deleteById(int theId) {
    // Implement the logic to delete a user by their ID from the database
    //find user by id
        User user = findByID(theId);
        //remove user
        entityManager.remove(user);
    }

    @Override
    public void updateUserEnabledStatus(int id, boolean enabled) {
        User user = entityManager.find(User.class, id);
        System.out.println(user);
        if (user == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }
        user.setEnabled(enabled);
        entityManager.persist(user);
    }
    @Override
    public List<User> findHotelAdminsByEnabledStatus(boolean enabled) {
        String jpql = "SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName AND u.enabled = :enabled";
        return entityManager.createQuery(jpql, User.class)
                            .setParameter("roleName", "HotelAdmin")
                            .setParameter("enabled", enabled)
                            .getResultList();
    }

    @Override
    public List<User> findByRejectedTrue() {
        String jpql = "SELECT u FROM User u WHERE u.rejected = true";
        return entityManager.createQuery(jpql, User.class).getResultList();
    }


    
  

    @Override
    public List<User> getUsersByRoleName(String roleName) {
        return entityManager.createQuery(
            "SELECT u FROM User u JOIN u.roles r WHERE UPPER(r.name) = :role", User.class)
            .setParameter("role", roleName.toUpperCase())
            .getResultList();
    }

    @Override
    public int countUsersByRole(String roleName) {
        try {
            // Use proper JPQL parameter syntax without quotes
            String jpql = "SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName";
            
            Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("roleName", roleName)
                .getSingleResult();
                
            return count != null ? count.intValue() : 0;
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public int countHotelAdminsByStatus(boolean enabled, boolean rejected) {
        String jpql = "SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = 'HotelAdmin' " +
                    "AND u.enabled = :enabled AND u.rejected = :rejected";
        
        Long count = entityManager.createQuery(jpql, Long.class)
            .setParameter("enabled", enabled)
            .setParameter("rejected", rejected)
            .getSingleResult();
        return count != null ? count.intValue() : 0;
    }
}

    
    
