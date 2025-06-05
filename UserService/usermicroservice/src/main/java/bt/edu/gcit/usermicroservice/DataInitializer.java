package bt.edu.gcit.usermicroservice;

import bt.edu.gcit.usermicroservice.entity.Role;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.dao.RoleDAO;
import bt.edu.gcit.usermicroservice.dao.UserDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserDAO userRepository;

    @Autowired
    private RoleDAO roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    @Override
    public void run(String... args) {
        String defaultEmail = "superadmin@example.com";
        String defaultPassword = "admin123";

        // Get or create role
        Role superAdminRole = getRoleByName("SuperAdmin");
        if (superAdminRole == null) {
            superAdminRole = new Role();
            superAdminRole.setName("SuperAdmin");
            superAdminRole.setDescription("System administrator");
            roleRepository.addRole(superAdminRole);
            System.out.println("✅ Role 'SuperAdmin' created.");
        }

        // Check if user exists by email using EntityManager
        User existingUser = getUserByEmail(defaultEmail);
        if (existingUser == null) {
            User superAdmin = new User();
            superAdmin.setEmail(defaultEmail);
            superAdmin.setPassword(passwordEncoder.encode(defaultPassword));
            superAdmin.setName("Default Super Admin");
            superAdmin.setEnabled(true);
            superAdmin.setRejected(false);
            superAdmin.setRoles(new HashSet<>());
            superAdmin.getRoles().add(superAdminRole);

            userRepository.save(superAdmin);
            System.out.println("✅ Default Super Admin created.");
        } else {
            System.out.println("ℹ️ Super Admin already exists.");
        }
    }

    private Role getRoleByName(String name) {
        try {
            TypedQuery<Role> query = entityManager.createQuery(
                    "SELECT r FROM Role r WHERE UPPER(r.name) = :name", Role.class);
            query.setParameter("name", name.toUpperCase());
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private User getUserByEmail(String email) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
