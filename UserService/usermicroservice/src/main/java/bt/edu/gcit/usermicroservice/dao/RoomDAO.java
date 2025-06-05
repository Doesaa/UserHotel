package bt.edu.gcit.usermicroservice.dao;

import java.util.List;
import bt.edu.gcit.usermicroservice.entity.Room;

public interface RoomDAO {
    Room findById(Long id);
    List<Room> findAll();
    List<Room> findByHotelId(Long hotelId);
    List<Room> findAvailableRooms(Long hotelId);
    void save(Room room);
    void update(Room room);
    void delete(Long id);
}
