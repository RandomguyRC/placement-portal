package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Attendance;
import com.placementportal.placement_website.model.AttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {
}
