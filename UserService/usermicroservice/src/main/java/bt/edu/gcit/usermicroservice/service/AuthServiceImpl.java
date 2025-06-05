package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.exception.UserNotEnabledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(@Lazy AuthenticationManager authenticationManager,
                           @Lazy UserDetailsService userDetailsService,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails login(String email, String password) {
        System.out.println("Authenticating user: " + email);

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (!userDetails.isEnabled()) {
                throw new UserNotEnabledException("User is not enabled");
            }

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            // Authentication is successful
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return userDetails;

        } catch (BadCredentialsException e) {
            System.out.println("Password does not match stored value.");
            throw e;

        } catch (UserNotEnabledException e) {
            System.out.println("User is not enabled.");
            throw e;

        } catch (Exception e) {
            System.out.println("General authentication failure: " + e.getMessage());
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}

// package bt.edu.gcit.usermicroservice.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Lazy;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// @Service
// public class AuthServiceImpl implements AuthService {

//     private final AuthenticationManager authenticationManager;
//     private final UserDetailsService userDetailsService;
//     private final PasswordEncoder passwordEncoder;

//     @Autowired
//     public AuthServiceImpl(@Lazy AuthenticationManager authenticationManager,
//                            @Lazy UserDetailsService userDetailsService,
//                            PasswordEncoder passwordEncoder) {
//         this.authenticationManager = authenticationManager;
//         this.userDetailsService = userDetailsService;
//         this.passwordEncoder = passwordEncoder;
//     }

//     @Override
//     public UserDetails login(String email, String password) {
//         System.out.println("Authenticating user: " + email);

//         try {
//             UserDetails userDetails = userDetailsService.loadUserByUsername(email);

//             String storedPassword = userDetails.getPassword();

//             System.out.println("RAW password from login request: " + password);
//             System.out.println("STORED password from DB (encoded): " + storedPassword);
//             System.out.println("Password match result: " + passwordEncoder.matches(password, storedPassword));

//             if (passwordEncoder.matches(password, storedPassword)) {
//                 // Passwords match, authenticate user
//                 System.out.println("Password matches. Authentication successful.");
//                 authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
//                 return userDetails;
//             } else {
//                 System.out.println("Password does not match stored value.");
//                 throw new RuntimeException("Invalid email or password");
//             }

//         } catch (Exception e) {
//             System.out.println("Authentication failed: " + e.getMessage());
//             throw new RuntimeException("Not enable");
//         }
//     }
// }
