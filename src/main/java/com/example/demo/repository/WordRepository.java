package com.example.demo.repository;

import com.example.demo.dto.GroupInfoProjection;
import com.example.demo.entity.WordEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<WordEntity, Long> {

    Optional<WordEntity> findByNameAndGroupEntity_Id(String name, Long groupId);
    boolean existsByNameAndGroupEntity_Id(String name, Long groupId);
    List<WordEntity> findByGroupEntity_Id(Long groupId);

    @Query(nativeQuery = true, value = "SELECT * from words where " +
            "(correct*1.0/NULLIF(attempt, 0))*100 >= :rate OR attempt = 0 order by rand() limit :count")
    List<WordEntity> findByRateGreaterThanEqual(@Param("rate") Double rate, @Param("count") int count);
    @Query(nativeQuery = true, value = "SELECT * from words where " +
            "((correct*1.0/NULLIF(attempt, 0))*100 >= :rate OR attempt = 0) AND group_id = :groupId " +
            "order by rand() limit :count")
    List<WordEntity> findByRateGreaterThanEqualAndGroupId(@Param("rate") Double rate, @Param("count") int count, @Param("groupId") Long groupId);
    @Query(nativeQuery = true, value = "Select " +
            "COALESCE(CAST(SUM(w.correct) AS DOUBLE) / NULLIF(SUM(w.attempt), 0) * 100, 0) AS successRate, " +
            "COUNT(*) AS count " +
            "from words w WHERE w.group_id = :groupId")
    GroupInfoProjection findAttemptToCountRatioByGroupId(@Param("groupId") Long groupId);
}