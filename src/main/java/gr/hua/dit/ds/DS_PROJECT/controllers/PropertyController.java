package gr.hua.dit.ds.DS_PROJECT.controllers;

import gr.hua.dit.ds.DS_PROJECT.entities.Booking;
import gr.hua.dit.ds.DS_PROJECT.entities.Property;
import gr.hua.dit.ds.DS_PROJECT.entities.User;
import gr.hua.dit.ds.DS_PROJECT.services.BookingService;
import gr.hua.dit.ds.DS_PROJECT.services.PropertyService;
import gr.hua.dit.ds.DS_PROJECT.services.UserService;
import gr.hua.dit.ds.DS_PROJECT.entities.Status;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        model.addAttribute("properties", propertyService.getApprovedProperties());
        return "property/accomodations"; // Separate HTML template
    }
    @GetMapping("/filtered")
    public String showFilteredProperties( @RequestParam String location, @RequestParam String price,@RequestParam String startDate, @RequestParam String endDate, Model model) throws ParseException {
        List<Property> filteredProperties = new ArrayList<>();
        List<Property> AvailableProperties = propertyService.getProperties();
        for (Property property : AvailableProperties) {
            if(property.getCity().equals(location) && property.getPrice() <= Integer.parseInt(price)
               && startDate != null && endDate != null) {
                if (property.getBookings().isEmpty()) {
                    filteredProperties.add(property);
                } else {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date sdate = formatter.parse(startDate);
                    Date edate = formatter.parse(endDate);
                    for (Booking booking : property.getBookings()) {
                        Date b_sdate = formatter.parse(booking.getStartDate());
                        Date b_edate = formatter.parse(booking.getEndDate());
                        if (sdate.after(b_edate) || edate.before(b_sdate)) {
                            filteredProperties.add(property);
                        }
                    }
                }
            }
        }
        model.addAttribute("properties", filteredProperties);
        return "property/accomodations";
    }

    // Landlord-specific properties
    @Secured("ROLE_LANDLORD")
    @GetMapping("my_properties")
    public String showMyProperties(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("properties", user.getProperties());
        return "property/myProperties"; // Separate HTML template
    }

    // Add a new property form
    @Secured({"ROLE_LANDLORD"})
    @GetMapping("/new")
    public String addProperty(Model model) {
        model.addAttribute("property", new Property());
        return "/property/newProperty";
    }

    // Save a new property
    @Secured({"ROLE_LANDLORD"})
    @PostMapping("/new")
    public String saveProperty(@ModelAttribute("property") Property property, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
        property.setStatus(Status.PENDING);
        propertyService.assignLandlordToProperty(user.getId(), property);
        model.addAttribute("properties", user.getProperties());
        return "property/myProperties"; // Redirect back to "My Properties"
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
        return "property/myProperties";
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
        return "property/pending_properties";
    }


}

