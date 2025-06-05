package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.Review;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.dao.ReviewDAO;
import bt.edu.gcit.usermicroservice.dao.ReviewDAO;
import bt.edu.gcit.usermicroservice.dao.UserDAOImpl;
import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;

import java.util.List;

@Service
public class ReviewServiceImpl {
    private final ReviewDAO reviewRepository;
    private final UserDAO userRepository;

    public ReviewServiceImpl(ReviewDAO reviewRepository, UserDAO userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Review createReview(int userId, int hotelAdminId, String comment, int rating) {
        // int intUserId = userId.intValue();
        // int intHotelAdminId = hotelAdminId.intValue();
        User user = userRepository.findByID(userId);

         if (user == null) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        
         User hotelAdmin = userRepository.findByID(hotelAdminId);
        if (hotelAdmin == null) {
            throw new UserNotFoundException("Hotel admin not found with id: " + hotelAdminId);
        }
        // Check if user is regular user and hotelAdmin is actually a hotel admin
        // (You'll need to implement this check based on your role system)
        
        Review review = new Review();
        review.setUser(user);
        review.setHotelAdmin(hotelAdmin);
        review.setComment(comment);
        review.setRating(rating);
        
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByHotelAdmin(Long hotelAdminId) {
        return reviewRepository.findByHotelAdminId(hotelAdminId);
    }

    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public void updateReviewVisibility(Long reviewId, boolean visibility) {
        // Review review = reviewRepository.findById(reviewId)
        //     if (review == null) {
        //         throw new UserNotFoundException("review not found with id: " + reviewId);
        //     }

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new UserNotFoundException("Review not found with id: " + reviewId));

        
        review.setVisibility(visibility);
        reviewRepository.save(review);
    }

    public List<Review> getVisibleReviewsByHotelAdmin(Long hotelAdminId) {
        return reviewRepository.findByHotelAdminIdAndVisibilityTrue(hotelAdminId);
    }



}