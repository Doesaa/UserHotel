package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.Room;
import bt.edu.gcit.usermicroservice.entity.Room.RoomAvailabilityStatus;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import bt.edu.gcit.usermicroservice.entity.Role;
import bt.edu.gcit.usermicroservice.dao.RoomRepository;
import bt.edu.gcit.usermicroservice.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final String uploadDir = "src/main/resources/static/images/rooms";


    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserService userService;
    
    @Override
    public Room addRoom(Room room, Long hotelAdminId) {
        User hotelAdmin = userService.getUserById(hotelAdminId.intValue());

        if (hotelAdmin == null) {
            throw new RuntimeException("Hotel admin not found");
        }

        boolean isHotelAdmin = hotelAdmin.getRoles().stream()
                .map(Role::getName)
                .anyMatch(role -> role.equalsIgnoreCase("HotelAdmin"));

        if (!isHotelAdmin) {
            throw new RuntimeException("User is not a hotel admin");
        }

        room.setHotelAdmin(hotelAdmin);
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }

    @Override
    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    @Override
    public List<Room> getAllRoomsByHotelAdmin(Long hotelAdminId) {
        return roomRepository.findByHotelAdmin_Id(hotelAdminId);
    }
    
    @Override
    public List<Room> getAvailableRoomsByHotelAdmin(Long hotelAdminId) {
        return roomRepository.findByHotelAdmin_IdAndAvailabilityStatus(hotelAdminId, RoomAvailabilityStatus.AVAILABLE);
    }

   
    @Override
    public List<Room> getUnavailableRoomsByHotelAdmin(Long hotelAdminId) {
        return roomRepository.findByHotelAdmin_IdAndAvailabilityStatus(hotelAdminId, RoomAvailabilityStatus.UNAVAILABLE);
    }




    @Transactional
    @Override
    public void uploadRoomPhoto(int roomId, MultipartFile[] photos) throws IOException {
        Room room = getRoomById((long) roomId);
        if (room == null) {
            throw new RuntimeException("Room not found with id " + roomId);
        }

        List<String> savedFilenames = new ArrayList<>();

        for (MultipartFile photo : photos) {
            if (photo == null || photo.isEmpty()) continue;
            if (photo.getSize() > 1024 * 1024) {
                throw new FileSizeException("Each file must be < 1MB");
            }


    
        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
    
        String filename = baseName + "_" + timestamp + "." + extension;
    
        Path uploadPathDir = Paths.get(uploadDir);
        if (!Files.exists(uploadPathDir)) {
            Files.createDirectories(uploadPathDir);
        }
    
        Path uploadPath = uploadPathDir.resolve(filename);
        photo.transferTo(uploadPath);
        savedFilenames.add(filename);
    }
    
        // ✅ Save photo name in DB
        List<String> existingPhotos = room.getPhotos();
        if (existingPhotos == null) {
            existingPhotos = new ArrayList<>();
        }
        existingPhotos.addAll(savedFilenames);
        room.setPhotos(existingPhotos);

        roomRepository.save(room); 
    }

    @Override
    public void markRoomAsUnavailable(Long roomId) {
        Room room = getRoomById(roomId);
        if (room == null) {
            throw new RuntimeException("Room not found");
        }
        room.setAvailabilityStatus(Room.RoomAvailabilityStatus.UNAVAILABLE);
        roomRepository.save(room);
    }

   

    
}
