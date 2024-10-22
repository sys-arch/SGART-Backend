package com.team1.sgart.backend.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.team1.sgart.backend.model.User;

import jakarta.persistence.EntityNotFoundException;

@Repository
public interface UserDao extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    default User updateUser(String email, User updatedUser) {
        Optional<User> existingUser = findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // Si el json trae los datos completados y verificados, se actualiza en la base de datos
			if (updatedUser.getName() != null && !updatedUser.getName().isEmpty())
				user.setName(updatedUser.getName());
			if(updatedUser.getLastName() != null && !updatedUser.getLastName().isEmpty())
				user.setName(updatedUser.getName());
			if(updatedUser.getDepartment() != null && !updatedUser.getDepartment().isEmpty())
				user.setLastName(updatedUser.getLastName());
			if (updatedUser.getCenter() != null && !updatedUser.getCenter().isEmpty())
				user.setCenter(updatedUser.getCenter());
			if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty())
				user.setDepartment(updatedUser.getDepartment());
			if (updatedUser.getHiringDate() != null && !updatedUser.getHiringDate().isEmpty())
				user.setHiringDate(updatedUser.getHiringDate());
			if (updatedUser.getProfile() != null && !updatedUser.getProfile().isEmpty())
				user.setEmail(updatedUser.getEmail());
			if(updatedUser.getProfile() != null && !updatedUser.getProfile().isEmpty())
				user.setProfile(updatedUser.getProfile());
			

            // Guardar el usuario actualizado
            return save(user);
        } else {
            throw new EntityNotFoundException("Usuario con email " + email +  " no encontrado");
        }
    }
}
