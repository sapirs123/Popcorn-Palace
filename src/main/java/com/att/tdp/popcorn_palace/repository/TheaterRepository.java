package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {}