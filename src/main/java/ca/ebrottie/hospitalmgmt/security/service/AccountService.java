package ca.ebrottie.hospitalmgmt.security.service;

import ca.ebrottie.hospitalmgmt.security.entities.AppRole;
import ca.ebrottie.hospitalmgmt.security.entities.AppUser;

public interface AccountService {
    AppUser addNewUser(String username, String password, String email, String confirmPassword);
    AppRole addNewRole(String role);
    void addRoleToUser(String username, String role);
    void removeRoleFromToUser(String username, String role);

    AppUser loadUserByUsername(String username);
}
