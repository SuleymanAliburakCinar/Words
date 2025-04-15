package com.example.demo.repository;

import com.example.demo.entity.GroupEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    @Transactional
    Optional<Long> deleteByName(String name);
    Optional<GroupEntity> findByName(String name);
    boolean existsByName(String name);

    @Query(nativeQuery = true, value = "SELECT g.id FROM groups g WHERE g.name = :name")
    Optional<Long> findIdByName(@Param("name") String name);
}