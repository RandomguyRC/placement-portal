package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Duty;
import com.placementportal.placement_website.model.Tpr;
import com.placementportal.placement_website.model.Venue;
import com.placementportal.placement_website.model.JobListing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.HashMap;
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
                duty.getStatus().name() // Enum name stored as string
        );
    }

    public List<Map<String, Object>> findDutyDetailsByTprId(String tprId) {
    String sql = """
        SELECT 
            d.duty_id,
            v.venue_location,
            jl.job_role,
            c.company_name,
            a.start_time,
            a.end_time
        FROM duties d
        JOIN venue v ON d.venue_id = v.venue_id
        JOIN job_listings jl ON d.related_listing_id = jl.listing_id
        JOIN companies c ON jl.company_id = c.company_id
        JOIN assessment a ON a.listing_id = jl.listing_id
        WHERE d.tpr_id = ?
    """;

    return jdbcTemplate.query(sql, new Object[]{tprId}, (rs, rowNum) -> {
        Map<String, Object> map = new HashMap<>();
        map.put("dutyId", rs.getString("duty_id"));
        map.put("venueLocation", rs.getString("venue_location"));
        map.put("jobRole", rs.getString("job_role"));
        map.put("companyName", rs.getString("company_name"));
        map.put("timing", rs.getTimestamp("start_time") + " - " + rs.getTimestamp("end_time"));
        return map;
    });
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