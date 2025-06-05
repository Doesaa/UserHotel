package bt.edu.gcit.usermicroservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "name", length = 90, nullable = false)
    private String name;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(length = 64, nullable = true)
    private String photo;

    @Column(length = 64, nullable = true)
    @ElementCollection
    private List<String> photos = new ArrayList<>();

    // Hotel information fields
    @Column(name = "hotel_name", length = 100)
    private String hotelName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "rating", length = 5)
    private String rating;
    


    @Column(length = 200)
    private String address;

    @Column(name = "license_number", length = 50)
    private String licenseNumber;

     @Column(name = "check_in_time", length = 20)
    @Pattern(regexp = "^(1[0-2]|0?[1-9]):[0-5][0-9] (AM|PM)$", 
             message = "Check-in time must be in h:mm AM/PM format (e.g., 2:00 PM)")
    private String checkInTime;

    @Column(name = "check_out_time", length = 20)
    @Pattern(regexp = "^(1[0-2]|0?[1-9]):[0-5][0-9] (AM|PM)$", 
             message = "Check-out time must be in h:mm AM/PM format (e.g., 11:00 AM)")
    private String checkOutTime;

    @Column(name = "hotel_description", columnDefinition = "TEXT")
    private String hotelDescription;

    @Column(name = "hotel_facilities", columnDefinition = "TEXT")
    private String hotelFacilities;

    @Column(name = "room_types", columnDefinition = "TEXT")
    private String roomTypes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "hotelAdmin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Room> rooms = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviewsGiven = new HashSet<>();

    @OneToMany(mappedBy = "hotelAdmin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviewsReceived = new HashSet<>();


    // Constructors
    public User() {
    }

    // Constructor for regular users
    public User(String email, String password, String name, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // Constructor for hotel admins
    public User(String email, String password, String name,
               String hotelName, String address,
               String licenseNumber, String checkInTime, String checkOutTime,
               String hotelDescription, String hotelFacilities, String roomTypes) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.hotelName = hotelName;
        this.address = address;
        this.licenseNumber = licenseNumber;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.hotelDescription = hotelDescription;
        this.hotelFacilities = hotelFacilities;
        this.roomTypes = roomTypes;
    }

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

  
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public String getCheckInTime24Hour() {
        return convertTo24HourFormat(checkInTime);
    }

    // Helper method to convert to 24-hour format for calculations
    public String getCheckOutTime24Hour() {
        return convertTo24HourFormat(checkOutTime);
    }

    private String convertTo24HourFormat(String time12Hour) {
        if (time12Hour == null || time12Hour.isEmpty()) {
            return null;
        }
        try {
            String[] parts = time12Hour.split(" ");
            String[] timeParts = parts[0].split(":");
            int hour = Integer.parseInt(timeParts[0]);
            String minute = timeParts[1];
            String period = parts[1].toUpperCase();

            if (period.equals("PM") && hour != 12) {
                hour += 12;
            } else if (period.equals("AM") && hour == 12) {
                hour = 0;
            }
            return String.format("%02d:%s", hour, minute);
        } catch (Exception e) {
            return time12Hour; // fallback to original if conversion fails
        }
    }

    private boolean rejected = false;

    private String rejectionReason;




    // Getters and Setters

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        // Check if the user is a HotelAdmin, only then we care if enabled is true
        return roles.stream()
            .anyMatch(role -> role.getName().equalsIgnoreCase("HotelAdmin")) ? enabled : true;
    }
    

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String filename) {
        this.photo = filename;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }


    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getHotelDescription() {
        return hotelDescription;
    }

    public void setHotelDescription(String hotelDescription) {
        this.hotelDescription = hotelDescription;
    }

    public String getHotelFacilities() {
        return hotelFacilities;
    }

    public void setHotelFacilities(String hotelFacilities) {
        this.hotelFacilities = hotelFacilities;
    }

    
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public String getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(String roomTypes) {
        this.roomTypes = roomTypes;
    }


    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
        room.setHotelAdmin(this);
    }

    public void removeRoom(Room room) {
        this.rooms.remove(room);
        room.setHotelAdmin(null);
    }

    
    
}