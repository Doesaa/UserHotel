// package bt.edu.gcit.usermicroservice.rest;

// import bt.edu.gcit.usermicroservice.entity.User;
// import bt.edu.gcit.usermicroservice.service.AuthService;
// import bt.edu.gcit.usermicroservice.service.JWTUtil;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.web.bind.annotation.*;

// import jakarta.servlet.http.Cookie;
// import jakarta.servlet.http.HttpServletResponse;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.logging.Level;
// import java.util.logging.Logger;

// @RestController
// @RequestMapping("/api/auth")
// public class AuthController {

//     private static final Logger logger = Logger.getLogger(AuthController.class.getName());

//     @Autowired
//     private AuthService authService;

//     @Autowired
//     private JWTUtil jwtUtil;

//     @PostMapping("/login")
//     public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
//         try {
//             UserDetails userDetails = authService.login(user.getEmail(), user.getPassword());
//             String jwt = jwtUtil.generateToken(userDetails);

//             // Set JWT as a cookie
//             Cookie jwtCookie = new Cookie("JWT-TOKEN", jwt);
//             jwtCookie.setHttpOnly(true);
//             response.addCookie(jwtCookie);

//             Map<String, Object> responseBody = new HashMap<>();
//             responseBody.put("user", userDetails);

//             logger.info("Login successful for user: " + user.getEmail());
//             return ResponseEntity.ok(responseBody);

//         } catch (BadCredentialsException e) {
//             logger.warning("Login failed for user: " + user.getEmail() + " - Invalid credentials.");
//             return ResponseEntity.status(401).body("Invalid email or password");

//         } catch (Exception e) {
//             logger.log(Level.SEVERE, "Unexpected error during login for user: " + user.getEmail(), e);
//             return ResponseEntity.status(500).body("Internal Server Error");
//         }
//     }

//     @PostMapping("/logout")
//     public ResponseEntity<?> logout(HttpServletResponse response) {
//         // Clear JWT cookie
//         Cookie jwtCookie = new Cookie("JWT-TOKEN", null);
//         jwtCookie.setMaxAge(0);
//         response.addCookie(jwtCookie);
//         logger.info("User logged out successfully.");
//         return ResponseEntity.ok().build();
//     }
// }


package bt.edu.gcit.usermicroservice.rest;
import bt.edu.gcit.usermicroservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import
org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import bt.edu.gcit.usermicroservice.service.AuthService;
import bt.edu.gcit.usermicroservice.service.JWTUtil;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")


   public ResponseEntity<Map<String, Object>> login(@RequestBody User user, HttpServletResponse response) {

    UserDetails userDetails = authService.login(user.getEmail(), user.getPassword());

    String jwt = jwtUtil.generateToken(userDetails);
    // Set JWT as a cookie
    Cookie jwtCookie = new Cookie("JWT-TOKEN", jwt);
    jwtCookie.setHttpOnly(true);
    response.addCookie(jwtCookie);

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("user", userDetails);
    
    return ResponseEntity.ok(responseBody);
   
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
    // Clear JWT cookie
        Cookie jwtCookie = new Cookie("JWT-TOKEN", null);
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
        return ResponseEntity.ok().build();
        }
    }
