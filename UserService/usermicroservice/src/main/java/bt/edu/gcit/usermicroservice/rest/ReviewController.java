package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.Review;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import bt.edu.gcit.usermicroservice.service.ReviewServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewServiceImpl reviewService;

    public ReviewController(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Review> createReview(
            @RequestParam int userId,
            @RequestParam int hotelAdminId,
            @RequestParam String comment,
            @RequestParam int rating) {
        
        Review review = reviewService.createReview(userId, hotelAdminId, comment, rating);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/hotel/{hotelAdminId}")
    public ResponseEntity<List<Review>> getReviewsForHotelAdmin(@PathVariable Long hotelAdminId) {
        List<Review> reviews = reviewService.getReviewsByHotelAdmin(hotelAdminId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable Long userId) {
        List<Review> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/visibility")
    public ResponseEntity<String> updateReviewVisibility(@PathVariable Long id, @RequestParam boolean visible) {
        try {
            reviewService.updateReviewVisibility(id, visible);
            return ResponseEntity.ok("Review visibility updated successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating review visibility.");
        }
    }

   @GetMapping("/{hotelAdminId}/visible")
    public ResponseEntity<List<Review>> getVisibleReviewsForHotelAdmin(@PathVariable Long hotelAdminId) {
        List<Review> reviews = reviewService.getVisibleReviewsByHotelAdmin(hotelAdminId);
        return ResponseEntity.ok(reviews);
    }



}