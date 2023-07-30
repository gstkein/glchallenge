package com.gsteren.glchallenge.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gsteren.glchallenge.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<com.gsteren.glchallenge.entities.User> findByEmail(String email);
    // Additional custom query methods can be defined here if needed
}
