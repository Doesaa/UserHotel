package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Review;
import bt.edu.gcit.usermicroservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewDAO extends JpaRepository<Review, Long> {
    List<Review> findByHotelAdmin(User hotelAdmin);
    List<Review> findByUser(User user);
    List<Review> findByHotelAdminId(Long hotelAdminId);
    List<Review> findByUserId(Long userId);
    List<Review> findByVisibilityTrue();
    List<Review> findByHotelAdminIdAndVisibilityTrue(Long hotelAdminId);


}