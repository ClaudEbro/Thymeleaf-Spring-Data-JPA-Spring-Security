package ca.ebrottie.hospitalmgmt;

import ca.ebrottie.hospitalmgmt.entities.Patient;
import ca.ebrottie.hospitalmgmt.repository.PatientRepository;
import ca.ebrottie.hospitalmgmt.security.entities.AppRole;
import ca.ebrottie.hospitalmgmt.security.entities.AppUser;
import ca.ebrottie.hospitalmgmt.security.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;

@SpringBootApplication
public class HospitalMgmtApplication implements CommandLineRunner {

    @Autowired
    private PatientRepository patientRepository;

    public static void main(String[] args) {
        SpringApplication.run(HospitalMgmtApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        /* Add a patient
        Patient patient = new Patient();
        patient.setId(null);
        patient.setName("John");
        patient.setBirthDate(new Date());
        patient.setSick(false);
        patient.setScore(25);

        // or you can add a patient like that
        Patient patient2 = new Patient();
        patient2 = new Patient(null, "Maximilian", new Date(), false, 205);

        // or you can also use Builder
        Patient patient3 = Patient.builder()
                .birthDate(new Date())
                .name("Franck")
                .sick(true)
                .score(57)
                .build();*/

        // Add patient by Spring JPA with InUserDetailsManager Strategy
        patientRepository.save(new Patient(null,"Mark", new Date(), false,300));
        patientRepository.save(new Patient(null,"Hannah", new Date(), false,4345));
        patientRepository.save(new Patient(null,"Ivanoe", new Date(), true,500));
    }

    //Creating Users by JDBC Authentication Strategy with JdbcUserDetailsManager
    //@Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager){
        PasswordEncoder passwordEncoder = passwordEncoder();

        return args -> {
            UserDetails u1 = jdbcUserDetailsManager.loadUserByUsername("user11");
            if (u1==null)
            jdbcUserDetailsManager.createUser(
                    User.withUsername("user11").password(passwordEncoder.encode("12345")).roles("USER").build()
            );

            UserDetails u2 = jdbcUserDetailsManager.loadUserByUsername("user22");
            if (u2==null)
            jdbcUserDetailsManager.createUser(
                    User.withUsername("user22").password(passwordEncoder.encode("12345")).roles("USER").build()
            );

            UserDetails u3 = jdbcUserDetailsManager.loadUserByUsername("admin2");
            if (u3==null)
            jdbcUserDetailsManager.createUser(
                    User.withUsername("admin2").password(passwordEncoder.encode("12345")).roles("USER","ADMIN").build()
            );
        };
    }

    //Creating Users with UserDetailsService Strategy
    @Bean
    CommandLineRunner commandLineRunnerUserDetails(AccountService accountService) {
        return args -> {

            accountService.addNewRole("USER");
            accountService.addNewRole("ADMIN");

            accountService.addNewUser("user2", "1234", "user2@gmail.com", "1234");
            accountService.addNewUser("user3", "1234", "user3@gmail.com", "1234");
            accountService.addNewUser("admin5", "1234", "admin5@gmail.com", "1234");

            accountService.addRoleToUser("user2", "USER");
            accountService.addRoleToUser("user3", "USER");
            accountService.addRoleToUser("admin5", "USER");
            accountService.addRoleToUser("admin5", "ADMIN");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
