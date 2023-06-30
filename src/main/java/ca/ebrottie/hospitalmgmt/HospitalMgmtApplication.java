package ca.ebrottie.hospitalmgmt;

import ca.ebrottie.hospitalmgmt.entities.Patient;
import ca.ebrottie.hospitalmgmt.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

        // Add patient by Spring JPA
        patientRepository.save(new Patient(null,"Mark", new Date(), false,300));
        patientRepository.save(new Patient(null,"Hannah", new Date(), false,4345));
        patientRepository.save(new Patient(null,"Ivanoe", new Date(), true,500));
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
