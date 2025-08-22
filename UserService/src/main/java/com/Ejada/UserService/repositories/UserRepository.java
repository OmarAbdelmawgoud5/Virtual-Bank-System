package com.Ejada.UserService.repositories;

import com.Ejada.UserService.domain.entities.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** userRepository */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByUsername(String userName);
}
