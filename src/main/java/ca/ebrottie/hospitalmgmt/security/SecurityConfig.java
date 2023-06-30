package ca.ebrottie.hospitalmgmt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // To protect access by methods
public class SecurityConfig {
    @Autowired
    private PasswordEncoder passwordEncoder; // I inject PasswordEncoder to hash the password


    //Personalize httpSecurity.formLogin() by InMemory Authentication
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("12345")).roles("USER").build(),
                User.withUsername("user2").password(passwordEncoder.encode("12345")).roles("USER").build(),
                User.withUsername("admin").password(passwordEncoder.encode("12345")).roles("USER","ADMIN").build()
        );
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests().requestMatchers("/webjars/**").permitAll(); // To authorized bootstrap
        httpSecurity.formLogin().loginPage("/login").permitAll(); // To personalize login page
        httpSecurity.rememberMe();

        //Define permissions to access or use PreAuthorize annotation with @EnableMethodSecurity
        //httpSecurity.authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER");  All request which contains in the path "/user" needs to have USER role.
        //httpSecurity.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN");

        httpSecurity.authorizeHttpRequests().anyRequest().authenticated(); // All requests need an authentication.

        httpSecurity.exceptionHandling().accessDeniedPage("/notAuthorized"); // To redirect users on this page when they don't have access to some resources.

        return httpSecurity.build();
    }


}
