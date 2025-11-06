package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Venue;
import com.placementportal.placement_website.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/venues")
public class AdminVenueController {

    @Autowired
    private VenueRepository venueRepository;

    // List all venues
    @GetMapping
    public String listVenues(Model model) {
        List<Venue> venues = venueRepository.findAll();
        model.addAttribute("venues", venues);
        return "admin/venues-list";
    }

    // Show Add Venue form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("venue", new Venue());
        return "admin/venues-add";
    }

    // Handle Add Venue submission
    @PostMapping("/add")
    public String addVenue(@ModelAttribute Venue venue) {
        if (venue.getVenueId() == null || venue.getVenueId().isEmpty()) {
            venue.setVenueId(UUID.randomUUID().toString());
        }
        venueRepository.save(venue);
        return "redirect:/admin/venues";
    }

    // Delete a venue
    @GetMapping("/delete/{id}")
    public String deleteVenue(@PathVariable("id") String id) {
        venueRepository.deleteById(id);
        return "redirect:/admin/venues";
    }
}
