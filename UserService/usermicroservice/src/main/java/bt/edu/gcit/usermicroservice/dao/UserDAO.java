package bt.edu.gcit.usermicroservice.dao;
import java.util.List;

import bt.edu.gcit.usermicroservice.entity.User;

public interface UserDAO{
    User save(User user);  
    List<User> getAllUsers(); 
    // User getUserById(int id);  
    // User updateUser(User user);

    User findByEmail(String email);
    User findByID(int theId);
    void deleteById(int theId);
    void updateUserEnabledStatus(int id, boolean enabled);

    List<User> findHotelAdminsByEnabledStatus(boolean enabled);
    List<User> findByRejectedTrue();
    List<User> getUsersByRoleName(String roleName);

    int countUsersByRole(String roleName);
    int countHotelAdminsByStatus(boolean enabled, boolean rejected);

    


 
}
