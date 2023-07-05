package ca.ebrottie.hospitalmgmt.security;

import ca.ebrottie.hospitalmgmt.security.service.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // To protect access by methods
@AllArgsConstructor
public class SecurityConfig {

    private PasswordEncoder passwordEncoder; // I inject PasswordEncoder to hash the password
    private UserDetailServiceImpl userDetailServiceImpl; // I inject UserDetailService

    //@Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){  // JdbcUserDetailsManager Authentication Strategy

        return new JdbcUserDetailsManager(dataSource);
    }

    //Personalize httpSecurity.formLogin() by InMemory Authentication
    //@Bean
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
        httpSecurity.formLogin().loginPage("/login").defaultSuccessUrl("/").permitAll(); // To personalize login page
        httpSecurity.rememberMe();

        //Define permissions to access or use PreAuthorize annotation with @EnableMethodSecurity
        //httpSecurity.authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER");  All request which contains in the path "/user" needs to have USER role.
        //httpSecurity.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN");

        httpSecurity.authorizeHttpRequests().anyRequest().authenticated(); // All requests need an authentication.

        httpSecurity.exceptionHandling().accessDeniedPage("/notAuthorized"); // To redirect users on this page when they don't have access to some resources.

        httpSecurity.userDetailsService(userDetailServiceImpl);

        return httpSecurity.build();
    }


}
