package com.example.demo.repository;

import com.example.demo.entity.WordEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<WordEntity, Long> {

    @Transactional
    Optional<Long> deleteByName(String name);
    Optional<WordEntity> findByName(String name);
    Optional<WordEntity> findByNameAndGroupEntity_Id(String name, Long groupId);
    boolean existsByNameAndId(String name, Long groupId);

    @Query(nativeQuery = true, value = "SELECT * from words where " +
            "(correct*1.0/NULLIF(attempt, 0))*100 >= :rate OR attempt = 0 order by rand() limit :count")
    List<WordEntity> findByRateGreaterThanEqual(@Param("rate") Double rate, @Param("count") int count);
    @Query(nativeQuery = true, value = "SELECT * from words where " +
            "((correct*1.0/NULLIF(attempt, 0))*100 >= :rate OR attempt = 0) AND group_id = :groupId " +
            "order by rand() limit :count")
    List<WordEntity> findByRateGreaterThanEqualAndGroupId(@Param("rate") Double rate, @Param("count") int count, @Param("groupId") Long groupId);
    List<WordEntity> findByGroupEntity_Id(Long groupId);
}