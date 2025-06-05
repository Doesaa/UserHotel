package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.Room;
import bt.edu.gcit.usermicroservice.entity.Room.RoomAvailabilityStatus;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.RoomService;
import bt.edu.gcit.usermicroservice.service.UserService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomRestController {

    private final RoomService roomService;
    private final UserService userService;

    public RoomRestController(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    @PostMapping(value = "/add/{hotelAdminId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Room> createRoom(
            @PathVariable Long hotelAdminId,
            @RequestParam String roomType,
            @RequestParam String roomSize,
            @RequestParam String bedSize,
            @RequestParam BigDecimal pricePerNight,
            @RequestParam String roomFacilities,
            @RequestParam String roomDescription,
            @RequestParam(value = "photos", required = false) MultipartFile[] photos) throws IOException
            
            {

        User hotelAdmin = userService.getUserById(hotelAdminId.intValue());

        Room room = new Room(roomType, roomSize, bedSize, pricePerNight,
                roomFacilities, roomDescription, hotelAdmin);


        Room savedRoom = roomService.addRoom(room, hotelAdminId);
        
        if (photos != null) {
            roomService.uploadRoomPhoto(savedRoom.getId().intValue(), photos);
        }
        return ResponseEntity.ok(savedRoom);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long id,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) String roomSize,
            @RequestParam(required = false) String bedSize,
            @RequestParam(required = false) BigDecimal pricePerNight,
            @RequestParam(required = false) String roomFacilities,
            @RequestParam(required = false) String roomDescription,
            @RequestParam(value = "photos", required = false) MultipartFile[] photos,
            @RequestParam(required = false) RoomAvailabilityStatus availabilityStatus) throws IOException {

        Room existingRoom = roomService.getRoomById(id);

        if (roomType != null) existingRoom.setRoomType(roomType);
        if (roomSize != null) existingRoom.setRoomSize(roomSize);
        if (bedSize != null) existingRoom.setBedSize(bedSize);
        if (pricePerNight != null) existingRoom.setPricePerNight(pricePerNight);
        if (roomFacilities != null) existingRoom.setRoomFacilities(roomFacilities);
        if (roomDescription != null) existingRoom.setRoomDescription(roomDescription);
        if (availabilityStatus != null) existingRoom.setAvailabilityStatus(availabilityStatus);

        Room updatedRoom = roomService.updateRoom(existingRoom);

        if (photos != null) {
            roomService.uploadRoomPhoto(updatedRoom.getId().intValue(), photos);
        }

        return ResponseEntity.ok(updatedRoom);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/hotel/{hotelAdminId}")
    public ResponseEntity<List<Room>> getRoomsByHotelAdmin(@PathVariable Long hotelAdminId) {
        List<Room> rooms = roomService.getAllRoomsByHotelAdmin(hotelAdminId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/hotel/{hotelAdminId}/available")
    public ResponseEntity<List<Room>> getAvailableRoomsByHotelAdmin(@PathVariable Long hotelAdminId) {
        List<Room> rooms = roomService.getAvailableRoomsByHotelAdmin(hotelAdminId);
        return ResponseEntity.ok(rooms);
    }

   
    @GetMapping("/hotel/{hotelAdminId}/unavailable")
    public ResponseEntity<List<Room>> getUnavailableRoomsByHotelAdmin(@PathVariable Long hotelAdminId) {
        List<Room> rooms = roomService.getUnavailableRoomsByHotelAdmin(hotelAdminId);
        return ResponseEntity.ok(rooms);
    }

    
    @PutMapping("/{id}/availability")
    public ResponseEntity<String> updateRoomAvailability(
            @PathVariable Long id,
            @RequestParam RoomAvailabilityStatus availabilityStatus) {
        Room room = roomService.getRoomById(id);
        room.setAvailabilityStatus(availabilityStatus);
        roomService.updateRoom(room);
        return ResponseEntity.ok("Room availability updated to: " + availabilityStatus);
    }

    @PutMapping("/{roomId}/unavailable")
    public ResponseEntity<String> markRoomUnavailable(@PathVariable Long roomId) {
        roomService.markRoomAsUnavailable(roomId);
        return ResponseEntity.ok("Room marked as UNAVAILABLE");
    }


}
