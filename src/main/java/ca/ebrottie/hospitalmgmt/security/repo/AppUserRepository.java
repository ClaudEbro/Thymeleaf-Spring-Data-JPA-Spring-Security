package ca.ebrottie.hospitalmgmt.security.repo;

import ca.ebrottie.hospitalmgmt.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String>{
    AppUser findByUsername(String username);
}
