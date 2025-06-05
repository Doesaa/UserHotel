package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import bt.edu.gcit.usermicroservice.dao.ChangePasswordRequest;
import bt.edu.gcit.usermicroservice.entity.Role;
import bt.edu.gcit.usermicroservice.service.UserService;
import bt.edu.gcit.usermicroservice.service.UserServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public UserRestController(UserService userService, ObjectMapper objectMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;

    }

    @PostMapping(value = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> createUser(
            @RequestPart("name") @Valid @NotNull String name,
            @RequestPart("email") @Valid @NotNull String email,
            @RequestPart("password") @Valid String password,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "photos", required = false) MultipartFile[] photos,

            @RequestPart("roles") @Valid @NotNull String rolesJson,
            @RequestPart(value = "hotelName", required = false) String hotelName,
            @RequestPart(value = "phoneNumber", required = false) String phoneNumber,
            @RequestPart(value = "rating", required = false) String rating,

            @RequestPart(value = "address", required = false) String address,
            @RequestPart(value = "licenseNumber", required = false) String licenseNumber,
            @RequestPart(value = "checkInTime", required = false) String checkInTime,
            @RequestPart(value = "checkOutTime", required = false) String checkOutTime,
            @RequestPart(value = "hotelDescription", required = false) String hotelDescription,
            @RequestPart(value = "hotelFacilities", required = false) String hotelFacilities,
            @RequestPart(value = "roomTypes", required = false) String roomTypes) {

        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setPhoneNumber(phoneNumber);


            // Set hotel information if this is a hotel admin
            if (hotelName != null && !hotelName.isEmpty()) {
                user.setHotelName(hotelName);
                user.setAddress(address);
                user.setLicenseNumber(licenseNumber);
                user.setCheckInTime(normalizeTimeFormat(checkInTime));
                user.setCheckOutTime(normalizeTimeFormat(checkOutTime));
                user.setHotelDescription(hotelDescription);
                user.setHotelFacilities(hotelFacilities);
                user.setRating(rating);

                user.setRoomTypes(roomTypes);
            }

            Set<Role> roles = objectMapper.readValue(rolesJson, new TypeReference<Set<Role>>(){});
            user.setRoles(roles);

            User savedUser = userService.save(user);

           

            if (photos != null && photos.length > 0) {

                userService.uploadUserPhoto(savedUser.getId().intValue(), photos);
            }

            if (photo != null && !photo.isEmpty()) {
                userService.uploadUserPhoto(savedUser.getId().intValue(), photo);
            }

            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error processing request: " + e.getMessage(), e);
        }
    }

    @PutMapping(value = "/users/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateUser(
            @PathVariable int id,
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart(value = "password", required = false) String password,
            @RequestPart("roles") String rolesJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "photos", required = false) MultipartFile[] photos,

            @RequestPart(value = "phoneNumber", required = false) String phoneNumber,
            @RequestPart(value = "rating", required = false) String rating,

            @RequestPart(value = "address", required = false) String address,
            @RequestPart(value = "licenseNumber", required = false) String licenseNumber,
            @RequestPart(value = "hotelName", required = false) String hotelName,
            @RequestPart(value = "checkInTime", required = false) String checkInTime,
            @RequestPart(value = "checkOutTime", required = false) String checkOutTime,
            @RequestPart(value = "hotelDescription", required = false) String hotelDescription,
            @RequestPart(value = "hotelFacilities", required = false) String hotelFacilities,
            @RequestPart(value = "roomTypes", required = false) String roomTypes) {

        try {
            User existingUser = userService.getUserById(id);
            existingUser.setName(name);
            existingUser.setEmail(email);
            existingUser.setPhoneNumber(phoneNumber);

            
           if (password != null && !password.isEmpty()) {
                if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                    existingUser.setPassword(passwordEncoder.encode(password));
                }
            }


            // Update hotel information if provided
            if (hotelName != null) {
                existingUser.setHotelName(hotelName);
                existingUser.setAddress(address);
                existingUser.setLicenseNumber(licenseNumber);
                existingUser.setCheckInTime(normalizeTimeFormat(checkInTime));
                existingUser.setCheckOutTime(normalizeTimeFormat(checkOutTime));
                existingUser.setHotelDescription(hotelDescription);
                existingUser.setHotelFacilities(hotelFacilities);
                existingUser.setRating(rating);

                existingUser.setRoomTypes(roomTypes);
            }

            Set<Role> roles = objectMapper.readValue(rolesJson, new TypeReference<Set<Role>>(){});
            existingUser.setRoles(roles);

            User updatedUser = userService.updateUser(id, existingUser);

            if (photo != null && !photo.isEmpty()) {
                userService.uploadUserPhoto(id, photo);
            }
            if (photos != null && photos.length > 0) {
                userService.uploadUserPhoto(id, photos);
            }


            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error processing request: " + e.getMessage(), e);
        }
    }

    private String normalizeTimeFormat(String time) {
        if (time == null || time.trim().isEmpty()) {
            return null;
        }
        
        time = time.trim().toUpperCase();
        
        try {
            // Handle various input formats
            if (time.matches("^\\d{1,2}:\\d{2}$")) {
                // Format like "2:00" - assume PM if not specified
                String[] parts = time.split(":");
                int hour = Integer.parseInt(parts[0]);
                String minute = parts[1];
                
                String period = hour < 12 ? "AM" : "PM";
                if (hour > 12) hour -= 12;
                if (hour == 0) hour = 12; // midnight
                
                return String.format("%d:%s %s", hour, minute, period);
            }
            else if (time.matches("^\\d{1,2}:\\d{2} [AP]M$")) {
                // Already in correct format, just standardize
                String[] parts = time.split(" ");
                String[] timeParts = parts[0].split(":");
                return String.format("%d:%s %s", 
                    Integer.parseInt(timeParts[0]), 
                    timeParts[1], 
                    parts[1]);
            }
            else if (time.matches("^\\d{1,2} \\d{2} [AP]M$")) {
                // Format like "2 00 PM" - replace space with colon
                return time.replaceFirst(" ", ":");
            }
            throw new IllegalArgumentException("Invalid time format");
        } catch (Exception e) {
            throw new RuntimeException("Invalid time format: " + time + 
                   ". Please use formats like '2:00 PM' or '11:00 AM'");
        }
    }

    
    // Standard CRUD endpoints remain the same...
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/users/checkDuplicateEmail")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.isEmailDuplicate(email));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}/enabled")
    public ResponseEntity<Void> updateUserEnabledStatus(
            @PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
        userService.updateUserEnabledStatus(id, requestBody.get("enabled"));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}/reject")
    public ResponseEntity<?> rejectHotelAdmin(@PathVariable int id, @RequestBody(required = false) Map<String, String> payload) {
        String reason = (payload != null) ? payload.get("reason") : null;

        try {
            userService.rejectHotelAdmin(id, reason);
            return ResponseEntity.ok("Hotel Admin rejected" + (reason != null ? " with reason." : "."));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error rejecting Hotel Admin.");
        }
    }


    @GetMapping("/hotelAdmin/true")
    public List<User> getEnabledHotelAdmins() {
        return userService.getHotelAdminsByEnabledStatus(true);
    }

    @GetMapping("/hotelAdmin/false")
    public List<User> getPendingHotelAdmins() {
        return userService.getHotelAdminsByEnabledStatus(false);
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        userService.resetPassword(email);
        return ResponseEntity.ok("Password reset email sent successfully.");
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<String> changePassword(
            @PathVariable int id,
            @RequestBody ChangePasswordRequest request) {
        
        userService.changePassword(id, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");
    }


    @GetMapping("/hotelAdmin/rejected")
    public ResponseEntity<List<User>> getRejectedHotelAdmins() {
        List<User> rejectedAdmins = userService.findByRejectedTrue();
        return ResponseEntity.ok(rejectedAdmins);
    }

  
    @GetMapping("/users/role/superadmin")
    public ResponseEntity<List<User>> getAllSuperAdmins() {
        List<User> users = userService.getUsersByRoleName("SuperAdmin");
        return ResponseEntity.ok(users);
    }


    @GetMapping("/stats")
    public ResponseEntity<Map<String, Integer>> getUserStatistics() {
        Map<String, Integer> stats = new HashMap<>();
            
        // Total regular users (role = "User")
        stats.put("totalUsers", userService.countUsersByRole("Users"));
        
        // Total registered hotels (HotelAdmin with enabled=true)
        stats.put("totalRegisteredHotels", userService.countHotelAdminsByStatus(true, false));
        
        // Total rejected hotels (HotelAdmin with rejected=true)
        stats.put("totalRejectedHotels", userService.countHotelAdminsByStatus(false, true));
        
        return ResponseEntity.ok(stats);
    }
    
}

