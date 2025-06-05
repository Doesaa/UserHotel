package bt.edu.gcit.usermicroservice.service;
import bt.edu.gcit.usermicroservice.entity.User;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public interface UserService {
    User save(User user);
    List<User> getAllUsers();
    User getUserById(int id);
    // User updateUser(User user);

    boolean isEmailDuplicate(String email);
    User updateUser(int id, User updatedUser);
    void deleteById(int theId);

    void uploadUserPhoto(int id, MultipartFile photo) throws IOException;
    void uploadUserPhoto(int id, MultipartFile[] photos) throws IOException;

    User findByID(int theId);
    void updateUserEnabledStatus(int id, boolean enabled);
    List<User> getHotelAdminsByEnabledStatus(boolean enabled);
    void resetPassword(String email);
    void changePassword(int userId, String currentPassword, String newPassword);
    void rejectHotelAdmin(int id, String reason);

    List<User> findByRejectedTrue();

    List<User> getUsersByRoleName(String roleName);


    // In UserService interface
    int countUsersByRole(String roleName);
    int countHotelAdminsByStatus(boolean enabled, boolean rejected);


}
