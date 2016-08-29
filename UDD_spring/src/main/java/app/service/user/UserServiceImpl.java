package app.service.user;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import app.model.User;
import app.repository.UserRepository;
import app.util.Role;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User getUserById(long id) {
		return userRepository.findOne(id);
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Collection<User> getAllUsers() {
		return (Collection<User>) userRepository.findAll();
	}

	@Override
	public User create(String email, String ime, String lozinka) {
		User newUser = new User(email, ime, null,
				new BCryptPasswordEncoder().encode(lozinka), Role.JOURNALIST);

		return userRepository.save(newUser);
	}

	@Override
	public Collection<User> getAllUsersByRole(String role) {
		// TODO Auto-generated method stub
		Collection<User> users = new ArrayList<User>();
		users = userRepository.findAllByRole(role);
		return users;
	}

	@Override
	public void edit(Long id, String name) {
		// TODO Auto-generated method stub
		User user = getUserById(id);
		user.setName(name);
		userRepository.save(user);
	}

	@Override
	public boolean changePassword(Long id, String oldPassword, String newPassword,
			String confirmNewPassword) {
		System.out.println("id: " + id);
		User user = getUserById(id);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(!encoder.matches(oldPassword, user.getPasswordHash()))
				return false;
		System.out.println("Prosao sifru");
		if(!(newPassword.equals(confirmNewPassword)))
			return false;
		user.setPasswordHash(encoder.encode(newPassword));
		userRepository.save(user);
		return true;
	}
	
	

}