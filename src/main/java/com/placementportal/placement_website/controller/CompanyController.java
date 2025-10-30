package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Company;
import com.placementportal.placement_website.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/companies")
    public String showCompanies(Model model) {
        List<Company> companies = companyRepository.findAll();
        System.out.println("✅ Companies fetched: " + companies.size());
        model.addAttribute("companies", companies);
        return "company-listing";
    }
}
