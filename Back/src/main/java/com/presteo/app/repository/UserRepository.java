package com.presteo.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.presteo.app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Méthodes générées automatiquement par JpaRepository:
    // save(), findById(), findAll(), delete(), etc.
    
    // Méthodes personnalisées basées sur le nom (Spring Data JPA les implémente automatiquement)
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    // Requête JPQL personnalisée
    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    List<User> findByEmailDomain(String domain);
}