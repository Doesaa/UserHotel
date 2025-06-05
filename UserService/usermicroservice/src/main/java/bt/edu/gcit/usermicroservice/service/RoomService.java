package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.Room;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface RoomService {
    Room addRoom(Room room, Long hotelAdminId);
    Room updateRoom(Room room);
    void deleteRoom(Long roomId);
    Room getRoomById(Long roomId);
    List<Room> getAllRoomsByHotelAdmin(Long hotelAdminId);
    List<Room> getAvailableRoomsByHotelAdmin(Long hotelAdminId);
    void uploadRoomPhoto(int roomId, MultipartFile[] photos) throws IOException;
    void markRoomAsUnavailable(Long roomId);
    List<Room> getUnavailableRoomsByHotelAdmin(Long hotelAdminId);



}
