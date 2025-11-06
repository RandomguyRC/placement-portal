package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Company;
import com.placementportal.placement_website.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/companies")
public class AdminCompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    // Show all companies
    @GetMapping
    public String listCompanies(Model model) {
        model.addAttribute("companies", companyRepository.findAll());
        return "admin/companies-list";
    }

    // Show Add Company form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("company", new Company());
        return "admin/companies-add";
    }

    // Handle Add Company form submission
    @PostMapping("/add")
    public String addCompany(@ModelAttribute Company company) {
        companyRepository.save(company);
        return "redirect:/admin/companies";
    }

    // Show Edit Company form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        Company company = companyRepository.findById(id).orElse(null);
        model.addAttribute("company", company);
        return "admin/companies-edit";
    }

    // Handle Edit Company form submission
    @PostMapping("/update")
    public String updateCompany(@ModelAttribute Company company) {
        companyRepository.save(company);
        return "redirect:/admin/companies";
    }

    // Delete company
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable("id") String id) {
        companyRepository.deleteById(id);
        return "redirect:/admin/companies";
    }
}
