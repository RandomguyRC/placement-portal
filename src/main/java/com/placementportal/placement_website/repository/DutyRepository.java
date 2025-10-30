package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Duty;
import com.placementportal.placement_website.model.Tpr;
import com.placementportal.placement_website.model.Venue;
import com.placementportal.placement_website.model.JobListing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DutyRepository {

    private final JdbcTemplate jdbcTemplate;

    public DutyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ---------------- SAVE DUTY ----------------
    public int save(Duty duty) {
        String sql = "INSERT INTO duties (duty_id, tpr_id, venue_id, related_listing_id, status) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                duty.getDutyId(),
                duty.getTpr().getTprId(),
                duty.getVenue().getVenueId(),
                duty.getRelatedListing().getListingId(),
                duty.getStatus().name() // ✅ Enum name stored as string
        );
    }

    // ---------------- FIND DUTIES BY TPR ----------------
    public List<Duty> findByTprId(String tprId) {
        String sql = "SELECT * FROM duties WHERE tpr_id = ?";
        return jdbcTemplate.query(sql, new Object[]{tprId}, (rs, rowNum) -> {
            Duty duty = new Duty();
            duty.setDutyId(rs.getString("duty_id"));

            Tpr tpr = new Tpr();
            tpr.setTprId(rs.getString("tpr_id"));
            duty.setTpr(tpr);

            Venue venue = new Venue();
            venue.setVenueId(rs.getString("venue_id"));
            duty.setVenue(venue);

            JobListing listing = new JobListing();
            listing.setListingId(rs.getString("related_listing_id"));
            duty.setRelatedListing(listing);

            duty.setStatus(Duty.DutyStatus.valueOf(rs.getString("status")));
            return duty;
        });
    }
}
