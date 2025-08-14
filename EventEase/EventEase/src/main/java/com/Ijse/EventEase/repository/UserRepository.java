package com.Ijse.EventEase.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.Ijse.EventEase.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String username);
}
