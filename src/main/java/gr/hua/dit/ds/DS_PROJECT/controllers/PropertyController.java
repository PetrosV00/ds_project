package gr.hua.dit.ds.DS_PROJECT.controllers;

import gr.hua.dit.ds.DS_PROJECT.entities.Property;
import gr.hua.dit.ds.DS_PROJECT.entities.User;
import gr.hua.dit.ds.DS_PROJECT.services.BookingService;
import gr.hua.dit.ds.DS_PROJECT.services.PropertyService;
import gr.hua.dit.ds.DS_PROJECT.services.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final BookingService bookingService;
    private final UserService userService;

    public PropertyController(PropertyService propertyService, BookingService bookingService, UserService userService) {
        this.propertyService = propertyService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    // All available accommodations for all users
    @GetMapping("")
    public String showProperties(Model model) {
        model.addAttribute("properties", propertyService.getProperties());
        return "property/accomodations"; // Separate HTML template
    }

    // Landlord-specific properties
    @Secured("ROLE_LANDLORD")
    @GetMapping("my_properties")
    public String showMyProperties(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("properties", user.getProperties());
        return "property/properties"; // Separate HTML template
    }

    // Add a new property form
    @Secured({"ROLE_LANDLORD"})
    @GetMapping("/new")
    public String addProperty(Model model) {
        model.addAttribute("property", new Property());
        return "property/property";
    }

    // Save a new property
    @Secured({"ROLE_LANDLORD"})
    @PostMapping("/new")
    public String saveProperty(@ModelAttribute("property") Property property, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
        propertyService.assignLandlordToProperty(user.getId(), property);
        model.addAttribute("properties", user.getProperties());
        return "property/properties"; // Redirect back to "My Properties"
    }

    @GetMapping("/pending_properties")
    public String showPendingProperties(Model model) {
        model.addAttribute("properties", propertyService.getPendingProperties());
        return "property/pending_properties";
    }

    @GetMapping("/{id}")
    public String PropertyDetails(Model model, @PathVariable int id) {
        Property property = propertyService.getProperty(id);
        model.addAttribute("properties", property);
        return "property/properties";
    }

    @GetMapping("/pending_property/{id}")
    public String ShowPendingProperty(Model model, @PathVariable int id) {
        Property property = propertyService.getProperty(id);
        model.addAttribute("property", property);
        return "property/pending_property";
    }

    @PostMapping("/pending_property/{id}")
    public String updatePendingProperty(@ModelAttribute("property") Property property, @PathVariable int id, Model model) {
        Property updatedProperty = propertyService.getProperty(id);
        updatedProperty.setStatus(property.getStatus());
        propertyService.save(updatedProperty);
        model.addAttribute("properties", propertyService.getApprovedProperties());
        return "property/properties";
    }
}

