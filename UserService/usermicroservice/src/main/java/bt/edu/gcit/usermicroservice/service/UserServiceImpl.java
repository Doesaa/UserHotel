package bt.edu.gcit.usermicroservice.service;
import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.util.StringUtils;
import jakarta.persistence.PersistenceContext;

import java.nio.file.Files;
import java.nio.file.Path;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {
    private UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String uploadDir = "src/main/resources/static/images";


    @Autowired
    private EmailService emailService;

  

    @Autowired
    @Lazy
    public UserServiceImpl(UserDAO userDAO, BCryptPasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User save(User user) {
        String password = user.getPassword();
        if (!password.startsWith("$2a$") && !password.startsWith("$2b$") && !password.startsWith("$2y$")) {
            user.setPassword(passwordEncoder.encode(password));
        }
    
        return userDAO.save(user);
    }

    


    // @Override
    // @Transactional
    // public User save(User user) {
    //     user.setPassword(passwordEncoder.encode(user.getPassword()));
    //     return userDAO.save(user); 
    // }



    
    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public User getUserById(int id) {
        return userDAO.findByID(id);
    }

    // @Override
    // public User updateUser(User user) {
    //     return userDAO.updateUser(user);
    // }

    @Override
    public boolean isEmailDuplicate(String email){
        User user = userDAO.findByEmail(email);
        return user !=null;
    }

    @Override
    public User findByID(int theId) {
        return userDAO.findByID(theId);
    }

    
    @Transactional
    @Override
    public User updateUser(int id, User updatedUser) {
        // First, find the user by ID
        User existingUser = userDAO.findByID(id);

        // If the user doesn't exist, throw UserNotFoundException
        if (existingUser == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }   

        // Update the basic user information
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        
        // Update password if changed
        if (updatedUser.getPassword() != null && 
            !updatedUser.getPassword().isEmpty() && 
            !existingUser.getPassword().equals(updatedUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        // Update hotel-specific fields if provided
        if (updatedUser.getHotelName() != null) {
            existingUser.setHotelName(updatedUser.getHotelName());
        }
        if (updatedUser.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        }
        if (updatedUser.getAddress() != null) {
            existingUser.setAddress(updatedUser.getAddress());
        }
        if (updatedUser.getLicenseNumber() != null) {
            existingUser.setLicenseNumber(updatedUser.getLicenseNumber());
        }

        if (updatedUser.getHotelDescription() != null) {
            existingUser.setHotelDescription(updatedUser.getHotelDescription());
        }

        if (updatedUser.getCheckOutTime() != null) {
            existingUser.setCheckOutTime(updatedUser.getCheckOutTime());
        }
        if (updatedUser.getCheckInTime() != null) {
            existingUser.setCheckInTime(updatedUser.getCheckInTime());
        }

        if (updatedUser.getHotelFacilities() != null) {
            existingUser.setHotelFacilities(updatedUser.getHotelFacilities());
        }

        if (updatedUser.getRoomTypes() != null) {
            existingUser.setRoomTypes(updatedUser.getRoomTypes());
        }

        // Update photo if provided
        if (updatedUser.getPhoto() != null) {
            existingUser.setPhoto(updatedUser.getPhoto());
        }
        

        // Update roles if provided
        if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
            existingUser.setRoles(updatedUser.getRoles());
        }

        // Save the updated user and return it
        return userDAO.save(existingUser);
    }


   

    @Transactional
    @Override
    public void uploadUserPhoto(int id, MultipartFile photo) throws IOException {
        User user = findByID(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }
        if (photo == null || photo.isEmpty()) {
            return; // Skip if no photo
        }
        if (photo.getSize() > 1024 * 1024) {
            throw new FileSizeException("File size must be < 1MB"); 
        }
    
        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
        System.out.println(originalFilename);


        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        System.out.println(filenameExtension);


        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        System.out.println(filenameWithoutExtension);


        String timestamp = String.valueOf(System.currentTimeMillis());
        System.out.println(timestamp);

        // Append the timestamp to the filename
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        System.out.println(filename);


        // 👇 Make sure directory exists
        Path uploadPathDir = Paths.get(uploadDir);
        if (!Files.exists(uploadPathDir)) {
            Files.createDirectories(uploadPathDir);  // 🔥 THIS IS THE FIX
        }

        Path uploadPath = uploadPathDir.resolve(filename);
        photo.transferTo(uploadPath);
        System.out.println("File saved to: " + uploadPath.toAbsolutePath());
        System.out.println("File exists: " + Files.exists(uploadPath));

        user.setPhoto(filename);
        // save(user);
        userDAO.save(user);
    }

    @Transactional
    public void uploadUserPhoto(int userId, MultipartFile[] photos) throws IOException {
        User user = findByID(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found with id " + userId);
        }
        // if (photos == null) {
        //     return; // Skip if no photo
        // }

        List<String> savedFilenames = new ArrayList<>();

        for (MultipartFile photo : photos) {
            if (photo == null || photo.isEmpty()) continue;
            if (photo.getSize() > 1024 * 1024) {
                throw new FileSizeException("Each file must be < 1MB");
            }

            String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String nameWithoutExt = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            String timestamp = String.valueOf(System.currentTimeMillis());
            String finalName = nameWithoutExt + "_" + timestamp + "." + extension;

            Path uploadPathDir = Paths.get(uploadDir);
            if (!Files.exists(uploadPathDir)) {
                Files.createDirectories(uploadPathDir);
            }

            Path uploadPath = uploadPathDir.resolve(finalName);
            photo.transferTo(uploadPath);
            savedFilenames.add(finalName);
            // List<String> savedFilenames = new ArrayList<>();

        }

        List<String> existingPhotos = user.getPhotos();
        if (existingPhotos == null) {
            existingPhotos = new ArrayList<>();
        }
        existingPhotos.addAll(savedFilenames);
        user.setPhotos(existingPhotos);

        userDAO.save(user);
    }



    @Transactional
    @Override
    public void deleteById(int theId) {
        userDAO.deleteById(theId);
    }

  
    @Transactional
    @Override
    public void updateUserEnabledStatus(int id, boolean enabled) {
        // Fetch the user from the database
        User user = userDAO.findByID(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    
        // Update the user's enabled status
        user.setEnabled(enabled);
    
        // Save the user back to the database
        userDAO.save(user);
    
        // If the user is a HotelAdmin and has been enabled, send the email
        if (enabled && user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("HotelAdmin"))) {
            String subject = "Your Hotel Admin Account is Now Enabled";
            String body = "Hello " + user.getName() + ",\n\n" +
                          "Your Hotel Admin account has been enabled. You can now add rooms to your hotel.\n\n" +
                          "Best regards,\n" +
                          "Your Support Team";
    
            // Send email to the user
            emailService.sendEmail(user.getEmail(), subject, body);
        }
    }



    @Override
    @Transactional
    public void rejectHotelAdmin(int id, String reason) {
        User user = userDAO.findByID(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        user.setRejected(true);
        user.setRejectionReason(reason);

        user.setEnabled(false);
        userDAO.save(user);

        if (user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("HotelAdmin"))) {
            String subject = "Your Hotel Admin Account Status Update";
            String body = "Hello " + user.getName() + ",\n\n" +
                        "We regret to inform you that you cannot access as hotel admin." +
                        (reason != null ? "\n\nReason: " + reason : "") +
                        "\n\nBest regards,\nYour Support Team";

            emailService.sendEmail(user.getEmail(), subject, body);
        }
    }


   @Override
    public List<User> findByRejectedTrue() {
        return userDAO.findByRejectedTrue();
    }



    @Override
    public List<User> getHotelAdminsByEnabledStatus(boolean enabled) {
        return userDAO.findHotelAdminsByEnabledStatus(enabled);
    }

    @Transactional
    @Override
    public void resetPassword(String email) {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }

        // 🔐 Generate random temporary password
        String tempPassword = UUID.randomUUID().toString().substring(0, 8); // 8-char temp password
        String encodedPassword = passwordEncoder.encode(tempPassword);

        // 🔄 Set and save
        user.setPassword(encodedPassword);
        userDAO.save(user);

        // 📧 Send email with the temp password
        String subject = "Password Reset Request";
        String body = "Hello " + user.getName() + ",\n\n" +
                    "Your new temporary password is: " + tempPassword + "\n" +
                    "You can login and change your password.\n\n" +
                    "Regards,\nYour Support Team";

        emailService.sendEmail(user.getEmail(), subject, body);
    }

    @Transactional
    @Override
    public void changePassword(int userId, String currentPassword, String newPassword) {
        User user = userDAO.findByID(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        // 🔒 Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // 🔐 Encode and update new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userDAO.save(user);
    }


    @Transactional
    @Override
    public List<User> getUsersByRoleName(String roleName) {
        try {
            // Validate roleName is not empty/null
            if (roleName == null || roleName.trim().isEmpty()) {
                throw new IllegalArgumentException("Role name cannot be empty");
            }
            
            // Call the DAO method
            List<User> users = userDAO.getUsersByRoleName(roleName);
            
            // Optionally, you can perform additional business logic here
            // For example, filtering or transforming the results
            
            return users;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error fetching users by role: " + e.getMessage());
            throw e; // Re-throw the exception for proper handling
        }
    }



    @Override
    public int countUsersByRole(String roleName) {
        return userDAO.countUsersByRole(roleName);
    }

    @Override
    public int countHotelAdminsByStatus(boolean enabled, boolean rejected) {
        return userDAO.countHotelAdminsByStatus(enabled, rejected);
    }

    

}
