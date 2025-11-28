package com.EzyCoding.user.repository;

import com.EzyCoding.user.model.Coder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CoderRepository extends JpaRepository<Coder, UUID> {
    Coder findByHandle(String handle);
}
