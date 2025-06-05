package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Room;
import bt.edu.gcit.usermicroservice.entity.Room.RoomAvailabilityStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelAdmin_Id(Long hotelAdminId);
    // List<Room> findByHotelAdmin_IdAndAvailableTrue(Long hotelAdminId);
    List<Room> findByHotelAdmin_IdAndAvailabilityStatus(Long hotelAdminId, RoomAvailabilityStatus status);



}