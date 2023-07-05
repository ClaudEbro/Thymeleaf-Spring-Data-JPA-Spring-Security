package ca.ebrottie.hospitalmgmt.security.repo;

import ca.ebrottie.hospitalmgmt.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole,String> {
}
