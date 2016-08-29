package app.service.user;

import java.util.Collection;

import app.model.User;

public interface UserService {

	   	User getUserById(long id);

	    User getUserByEmail(String email);

	    Collection<User> getAllUsers();

	    Collection<User> getAllUsersByRole(String role); 
	    
	    User create(String email, String ime, String lozinka);
	    
	    void edit(Long id, String name);
	    
	    boolean changePassword(Long id, String oldPassword, String newPassword, String confirmNewPassword);
	
}
