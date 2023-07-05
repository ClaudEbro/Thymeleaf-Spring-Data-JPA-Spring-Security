package ca.ebrottie.hospitalmgmt.security.service;

import ca.ebrottie.hospitalmgmt.security.entities.AppRole;
import ca.ebrottie.hospitalmgmt.security.entities.AppUser;
import ca.ebrottie.hospitalmgmt.security.repo.AppRoleRepository;
import ca.ebrottie.hospitalmgmt.security.repo.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser addNewUser(String username, String password, String email, String confirmPassword) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if(appUser != appUserRepository.findByUsername(username)) throw new RuntimeException("This user already exists");
        if(!password.equals(confirmPassword)) throw new RuntimeException("Password not match");
        appUser = AppUser.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();
        AppUser savedAppUser = appUserRepository.save(appUser);
        return savedAppUser;
    }

    @Override
    public AppRole addNewRole(String role) {
        AppRole appRole = appRoleRepository.findById(role).orElse(null);
                if(appRole.equals("USER")) {
                    throw new RuntimeException("This role already exists");
                }
                if(appRole.equals("ADMIN")) {
                    throw new RuntimeException("This role already exists");
                }
                appRole = AppRole.builder()
                        .role(role)
                        .build();
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String role) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).get();
        appUser.getRoles().add(appRole);

    }


    @Override
    public void removeRoleFromToUser(String username, String role) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).get();
        appUser.getRoles().remove(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
}
