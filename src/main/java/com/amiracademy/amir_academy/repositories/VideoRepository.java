package com.amiracademy.amir_academy.repositories;

import com.amiracademy.amir_academy.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("from Video where title like concat('%', :search, '%') or description like concat('%', :search, '%') ")
    List<Video> findFirstByTitleContainsOrDescriptionContains(String search);
}
