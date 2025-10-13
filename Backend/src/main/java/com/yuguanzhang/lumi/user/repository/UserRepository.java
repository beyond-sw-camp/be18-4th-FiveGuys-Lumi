package com.yuguanzhang.lumi.user.repository;

import com.yuguanzhang.lumi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    void deleteAllByIsDeleted(String isDeleted);

}
