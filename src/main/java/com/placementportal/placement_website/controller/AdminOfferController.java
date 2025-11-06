package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Offer;
import com.placementportal.placement_website.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/offers")
public class AdminOfferController {

    @Autowired
    private OfferRepository offerRepository;

    // List all offers
    @GetMapping
    public String listOffers(Model model) {
        List<Offer> offers = offerRepository.findAll();
        model.addAttribute("offers", offers);
        return "admin/offers-list";
    }

    // Show Add Offer form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("offer", new Offer());
        model.addAttribute("statuses", Offer.OfferStatus.values());
        return "admin/offers-add";
    }

    // Handle Add Offer submission
    @PostMapping("/add")
    public String addOffer(@ModelAttribute Offer offer) {
        if (offer.getApplicationId() == null || offer.getApplicationId().isEmpty()) {
            offer.setApplicationId(UUID.randomUUID().toString());
        }
        offerRepository.save(offer);
        return "redirect:/admin/offers";
    }

    // Show Edit Offer form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        Offer offer = offerRepository.findById(id).orElse(null);
        model.addAttribute("offer", offer);
        model.addAttribute("statuses", Offer.OfferStatus.values());
        return "admin/offers-edit";
    }

    // Handle Update Offer submission
    @PostMapping("/update")
    public String updateOffer(@ModelAttribute Offer offer) {
        offerRepository.save(offer);
        return "redirect:/admin/offers";
    }

    // Delete Offer
    @GetMapping("/delete/{id}")
    public String deleteOffer(@PathVariable("id") String id) {
        offerRepository.deleteById(id);
        return "redirect:/admin/offers";
    }
}
