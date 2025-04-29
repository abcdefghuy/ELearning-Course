package com.example.Learning_Course_App.repository;

import com.example.Learning_Course_App.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookmarkRepository extends JpaRepository<Bookmark, Long> {
}
