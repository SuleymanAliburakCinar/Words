package com.example.demo.repository;

import com.example.demo.entity.GroupEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    @Transactional
    Optional<Long> deleteByName(String name);
    Optional<GroupEntity> findByName(String name);
}