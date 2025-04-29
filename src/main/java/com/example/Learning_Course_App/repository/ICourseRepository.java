package com.example.Learning_Course_App.repository;

import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.enumeration.LessonStatus;
import com.example.Learning_Course_App.enumeration.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ICourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c JOIN c.categories cat WHERE cat.category.categoryName like %:categoryName%")
    Page<Course> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);
    @Query("SELECT c FROM Course c WHERE c.details.createdAt >= :sevenDaysAgo ORDER BY c.details.createdAt DESC")
    List<Course> findTop10ByCreatedAt(LocalDate sevenDaysAgo, Pageable pageable);
    @Query("SELECT c FROM Course c where c.details.isBestSeller = true")
    Page<Course> findTop10BestSellers(Pageable pageable);
    // Repository
    @Query("SELECT c FROM Course c " +
            "LEFT JOIN c.enrollments e " +
            "LEFT JOIN c.lessons l " +
            "LEFT JOIN l.progresses p " +
            "WHERE e.user.id = :userId " +
            "AND e.courseStatus = :courseStatus " +
            "AND p.status = :lessonStatus " +
            "ORDER BY p.updateAt DESC")
    Page<Course> findContinueCoursesCommon(@Param("userId") Long userId,
                                           @Param("courseStatus") Status courseStatus,
                                           @Param("lessonStatus") LessonStatus lessonStatus,
                                           Pageable pageable);
    // Sử dụng
    default Course findLastContinueCourse(Long userId, Status status, LessonStatus lessonStatus) {
        return findContinueCoursesCommon(userId, status, lessonStatus, PageRequest.of(0, 1))
                .stream().findFirst().orElse(null);
    }
    @Query("SELECT c FROM Course c " +
            "JOIN c.enrollments e " +
            "WHERE e.user.id = :userId " +
            "AND e.courseStatus = :status")
    Page<Course> findAllCompletedCourses(@Param("userId") Long userId,
                                         @Param("status") Status status,
                                         Pageable pageable);

    @Query(" SELECT CAST(COUNT(CASE WHEN p.status = :completedStatus THEN 1 END) * 100.0 / COUNT(l) AS int)" +
            " FROM Enrollment e" +
            " JOIN e.course c" +
            " JOIN c.lessons l" +
            " JOIN l.progresses p" +
            " WHERE e.user.id = :studentId" +
            " AND c.id = :courseId" +
            " AND p.student.id = :studentId" +
            " AND e.courseStatus = :courseStatus")
    Integer getCourseProgressPercent(@Param("courseId") Long courseId,
                                     @Param("studentId") Long studentId,
                                     @Param("completedStatus") LessonStatus completedStatus,
                                     @Param("courseStatus") Status courseStatus);

    @Query("SELECT c FROM Course c " +
            "WHERE c.courseName LIKE %:keyword%")
    Page<Course> findCourseByKeyWord(String keyword, Pageable pageable);
}
