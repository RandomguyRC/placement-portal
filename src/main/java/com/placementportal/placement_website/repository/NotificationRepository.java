package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    /**
     * Returns notifications for the student with the given enrollment number,
     * ordered by createdAt descending.
     *
     * Note: your Notification entity stores the Student in a field (e.g. 'recipient').
     * The JPQL below queries n.recipient.enrollmentNumber to match your existing model.
     */
    @Query("SELECT n FROM Notification n WHERE n.recipient.enrollmentNumber = :enrollmentNumber ORDER BY n.createdAt DESC")
    List<Notification> findByStudentEnrollmentNumber(@Param("enrollmentNumber") String enrollmentNumber);

}
