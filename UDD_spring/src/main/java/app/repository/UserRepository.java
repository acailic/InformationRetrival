package app.repository;



import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
	
	 public User findByEmail(String email);
	 
	 @Query("SELECT p FROM User p WHERE LOWER(p.role) = LOWER(:role)")
	 public Collection<User> findAllByRole(@Param("role") String role);


}
