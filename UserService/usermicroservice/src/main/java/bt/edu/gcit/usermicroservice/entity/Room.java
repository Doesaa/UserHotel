package bt.edu.gcit.usermicroservice.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = true)
    private List<String> photos = new ArrayList<>();

    @Column(nullable = false, length = 50)
    private String roomType;

    @Column(nullable = false, length = 20)
    private String roomSize;

    @Column(nullable = false, length = 20)
    private String bedSize;

    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(name = "room_facilities", columnDefinition = "TEXT")
    private String roomFacilities;

    @Column(name = "room_description", columnDefinition = "TEXT")
    private String roomDescription;


    public enum RoomAvailabilityStatus {
    AVAILABLE,
    PENDING,
    BOOKED,
    UNAVAILABLE
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomAvailabilityStatus availabilityStatus = RoomAvailabilityStatus.AVAILABLE;

    // private boolean available = true;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User hotelAdmin;

    // Constructors, Getters, and Setters
    public Room() {
    }

    public Room(String roomType, String roomSize, String bedSize, BigDecimal pricePerNight, 
               String roomFacilities, String roomDescription, User hotelAdmin) {
        this.roomType = roomType;
        this.roomSize = roomSize;
        this.bedSize = bedSize;
        this.pricePerNight = pricePerNight;
        this.roomFacilities = roomFacilities;
        this.roomDescription = roomDescription;
        this.hotelAdmin = hotelAdmin;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }

    public String getBedSize() {
        return bedSize;
    }

    public void setBedSize(String bedSize) {
        this.bedSize = bedSize;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getRoomFacilities() {
        return roomFacilities;
    }

    public void setRoomFacilities(String roomFacilities) {
        this.roomFacilities = roomFacilities;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    // public boolean isAvailable() {
    //     return available;
    // }

    // public void setAvailable(boolean available) {
    //     this.available = available;
    // }

    public RoomAvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(RoomAvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }


    public User getHotelAdmin() {
        return hotelAdmin;
    }

    public void setHotelAdmin(User hotelAdmin) {
        this.hotelAdmin = hotelAdmin;
    }
}