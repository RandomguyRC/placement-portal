package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    // ✅ Fetch by student's enrollment number (recipient.enrollmentNumber)
    @Query("SELECT n FROM Notification n WHERE n.recipient.enrollmentNumber = :enrollmentNumber ORDER BY n.createdAt DESC")
    List<Notification> findByStudentEnrollmentNumber(@Param("enrollmentNumber") String enrollmentNumber);
}
