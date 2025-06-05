package bt.edu.gcit.usermicroservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class HotelSecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() {
        return new ProviderManager(Arrays.asList(authProvider()));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(configurer ->
                configurer
                    .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/users/{id}").permitAll()



                    .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                    .requestMatchers("/api/hotels/**").authenticated()

                    .requestMatchers(HttpMethod.GET,"/api/users/checkDuplicateEmail").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/users/{id}").permitAll()
                    .requestMatchers(HttpMethod.DELETE,"/api/users/{id}").permitAll()
                    
                    .requestMatchers(HttpMethod.PUT,"/api/users/{id}/enabled").permitAll()
                    

                    .anyRequest().permitAll()
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


// package bt.edu.gcit.usermicroservice.security;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.context.annotation.Bean;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.authentication.ProviderManager;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import java.util.Arrays;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import bt.edu.gcit.usermicroservice.security.JwtRequestFilter;
// import bt.edu.gcit.usermicroservice.security.oauth.CustomerOAuth2UserService;
// import bt.edu.gcit.usermicroservice.security.oauth.OAuth2LoginSuccessHandler;




// //to secure the authorixation and authentication

// @Configuration
// @EnableWebSecurity
// public class HotelSecurityConfig {

//     public HotelSecurityConfig() {

//         System.out.println("HotelSecurityConfig created");
//     }
//     @Autowired
//     private JwtRequestFilter jwtRequestFilter;

//     // @Bean
//     // public PasswordEncoder passwordEncoder(){
//     //     return new BCryptPasswordEncoder();
//     // }   


//     @Autowired
//     private UserDetailsService userDetailsService;

//     @Autowired
//     private CustomerOAuth2UserService oAuth2UserService;
//     @Autowired
//     private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

//     @Bean
//     public AuthenticationManager customAuthenticationManager() throws Exception {
//     System.out.println("H2i ");
//         return new ProviderManager(Arrays.asList(authProvider()));
//     }

    
//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }



//     @Bean
//     public AuthenticationProvider authProvider() {
//         System.out.println("Hi ");

//         DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//         System.out.println("UserDetailsService: " + userDetailsService.getClass().getName());
        
//         authProvider.setUserDetailsService(userDetailsService);
//         authProvider.setPasswordEncoder(passwordEncoder());
//         System.out.println("AuthProvider: " + authProvider.getClass().getName());
//         return authProvider;
//  }

    

//     @Bean
//     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http.authorizeHttpRequests(configurer ->
//             configurer
//                 .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
//                 .requestMatchers(HttpMethod.POST, "/api/roles").permitAll()


//                 .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
//                 .requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()
                

//                 // .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("Admin")
//                 .requestMatchers(HttpMethod.GET,"/api/users/checkDuplicateEmail").hasAuthority("Admin")
//                 .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAuthority("Admin")
//                 .requestMatchers(HttpMethod.DELETE,"/api/users/{id}").hasAuthority("Admin")
                
//                 .requestMatchers(HttpMethod.PUT,"/api/users/{id}/enabled").permitAll()
                
//                 .requestMatchers(HttpMethod.PUT, "/api/users/{id}/enabled").permitAll()
//                 .requestMatchers(HttpMethod.GET, "/api/countries/**").permitAll()
//                 .requestMatchers(HttpMethod.POST, "/api/countries").permitAll()
//                 .requestMatchers(HttpMethod.PUT, "/api/countries").permitAll()
//                 .requestMatchers(HttpMethod.DELETE, "/api/countries").permitAll()
//                 .requestMatchers(HttpMethod.GET, "/api/states/**").permitAll()
//                 .requestMatchers(HttpMethod.POST,"/api/states/{country_id}").permitAll()
                
//                 .requestMatchers(HttpMethod.PUT, "/api/states").permitAll()
//                 .requestMatchers(HttpMethod.DELETE, "/api/states").permitAll()
//                 .requestMatchers(HttpMethod.POST, "/api/customer/*").permitAll()

//         )
//         .addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class);

//         http.oauth2Login()
//         .userInfoEndpoint()
//         .userService(oAuth2UserService)
//         .and()
//         .successHandler(oAuth2LoginSuccessHandler);


//         //disable CSRF
//         http.csrf().disable();

//         return http.build();
//     }

   
    
// }
