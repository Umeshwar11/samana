package com.samana.repository;

import com.samana.entity.HostProfile;
import com.samana.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HostProfileRepository extends JpaRepository<HostProfile, Long> {

    Optional<HostProfile> findByUser(User user);

    Optional<HostProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}