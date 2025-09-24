package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

    public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

        @Query("""
            SELECT t FROM Todo t
            LEFT JOIN FETCH t.user u
            WHERE :weather IS NULL OR t.weather = :weather
            AND (:startDateTime IS NULL OR t.modifiedAt >= :startDateTime)
            AND (:endDateTime IS NULL OR t.modifiedAt <= :endDateTime)
            ORDER BY t.modifiedAt DESC
            """)
    Page<Todo> findAllByWeatherAndModifiedAtBetween(String weather, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);
}
