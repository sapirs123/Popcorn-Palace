package com.att.tdp.popcorn_palace.repository;
import com.att.tdp.popcorn_palace.entity.Theater;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    Optional<Theater> findByName(String name);
    boolean existsByName(String name);
}