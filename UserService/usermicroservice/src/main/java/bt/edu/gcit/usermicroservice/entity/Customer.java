package bt.edu.gcit.usermicroservice.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Common user fields from User entity
    @Column(nullable = false, unique = true, length = 128)
    private String email;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(name = "name", nullable = false, length = 90)
    private String name;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(length = 200)
    private String address;

    @Column(name = "photo", length = 64)
    private String photo;

    @Column(nullable = false)
    private boolean enabled = true;

    // Customer-specific fields for hotel system
    @Column(name = "loyalty_id", length = 50)
    private String loyaltyId;

    @Column(name = "preferred_payment", length = 50)
    private String preferredPayment;

    @Column(name = "total_bookings")
    private Integer totalBookings = 0;

    @Column(name = "last_booking_date")
    private Date lastBookingDate;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    // Getters and Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLoyaltyId() {
        return loyaltyId;
    }

    public void setLoyaltyId(String loyaltyId) {
        this.loyaltyId = loyaltyId;
    }

    public String getPreferredPayment() {
        return preferredPayment;
    }

    public void setPreferredPayment(String preferredPayment) {
        this.preferredPayment = preferredPayment;
    }

    public Integer getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(Integer totalBookings) {
        this.totalBookings = totalBookings;
    }

    public Date getLastBookingDate() {
        return lastBookingDate;
    }

    public void setLastBookingDate(Date lastBookingDate) {
        this.lastBookingDate = lastBookingDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}