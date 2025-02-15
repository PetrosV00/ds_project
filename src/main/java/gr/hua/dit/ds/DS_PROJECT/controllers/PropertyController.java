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
    public String showFilteredProperties( @RequestParam String location, @RequestParam String price,@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, Model model) throws ParseException {
        List<Property> filteredProperties = new ArrayList<>();
        List<Property> AvailableProperties = propertyService.getProperties();


        for (Property property : AvailableProperties) {
            if (property.getCity().equals(location) && price.isEmpty() ) {
                if (startDate.isEmpty() || endDate.isEmpty()) {
                    filteredProperties.add(property);
                } else {
                    Date sdate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                    Date edate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

                    boolean flag = true;
                    for(Booking booking : property.getBookings()) {
                        if(property.getBookings().isEmpty()) {
                            filteredProperties.add(property);
                        } else {

                            Date b_sdate = new SimpleDateFormat("yyyy-MM-dd").parse(booking.getStartDate());
                            Date b_edate = new SimpleDateFormat("yyyy-MM-dd").parse(booking.getEndDate());

                            if (!(sdate.after(b_edate) || edate.before(b_sdate))) {
                                flag = false;
                                break;
                            }
                        }
                    }

                    if (flag == true) {
                        filteredProperties.add(property);
                    }
                }
            } else if ( !price.isEmpty() && property.getPrice() <= Integer.parseInt(price) && location.isEmpty()) {
                if (startDate.isEmpty() || endDate.isEmpty()) {
                    filteredProperties.add(property);
                } else {
                    Date sdate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                    Date edate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

                    boolean flag = true;
                    for(Booking booking : property.getBookings()) {
                        if(property.getBookings().isEmpty()) {
                            filteredProperties.add(property);
                        } else {

                            Date b_sdate = new SimpleDateFormat("yyyy-MM-dd").parse(booking.getStartDate());
                            Date b_edate = new SimpleDateFormat("yyyy-MM-dd").parse(booking.getEndDate());

                            if (!(sdate.after(b_edate) || edate.before(b_sdate))) {
                                flag = false;
                                break;
                            }
                        }
                    }

                    if (flag == true) {
                        filteredProperties.add(property);
                    }
                }
            } else if (!price.isEmpty() && property.getPrice() <= Integer.parseInt(price) && property.getCity().equals(location)) {
                if (startDate.isEmpty() || endDate.isEmpty()) {
                    filteredProperties.add(property);
                } else {
                    Date sdate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                    Date edate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

                    boolean flag = true;
                    for(Booking booking : property.getBookings()) {
                        if(property.getBookings().isEmpty()) {
                            filteredProperties.add(property);
                        } else {

                            Date b_sdate = new SimpleDateFormat("yyyy-MM-dd").parse(booking.getStartDate());
                            Date b_edate = new SimpleDateFormat("yyyy-MM-dd").parse(booking.getEndDate());

                            if (!(sdate.after(b_edate) || edate.before(b_sdate))) {
                                flag = false;
                                break;
                            }
                        }
                    }

                    if (flag == true) {
                        filteredProperties.add(property);
                    }
                }
            } else if (price.isEmpty() && location.isEmpty()) {
                if (startDate.isEmpty() || endDate.isEmpty()) {
                    filteredProperties.add(property);
                } else {
                    Date sdate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                    Date edate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

                    boolean flag = true;
                    for(Booking booking : property.getBookings()) {
                        if(property.getBookings().isEmpty()) {
                            filteredProperties.add(property);
                        } else {

                            Date b_sdate = new SimpleDateFormat("yyyy-MM-dd").parse(booking.getStartDate());
                            Date b_edate = new SimpleDateFormat("yyyy-MM-dd").parse(booking.getEndDate());

                            if (!(sdate.after(b_edate) || edate.before(b_sdate))) {
                                flag = false;
                                break;
                            }
                        }
                    }

                    if (flag == true) {
                        filteredProperties.add(property);
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

    @Secured("ROLE_ADMIN")
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

    @Secured("ROLE_LANDLORD")
    @GetMapping("/edit/{id}")
    public String editProperty(@PathVariable int id, Model model) {
        Property property = propertyService.getProperty(id);
        model.addAttribute("property", property);
        return "property/editProperty";
    }

    @Secured("ROLE_LANDLORD")
    @PostMapping("/update/{id}")
    public String updateProperty(@PathVariable int id, @ModelAttribute("property") Property updatedProperty, Model model) {
        Property property = propertyService.getProperty(id);
        property.setTitle(updatedProperty.getTitle());
        property.setDescription(updatedProperty.getDescription());
        property.setAddress(updatedProperty.getAddress());
        property.setPrice(updatedProperty.getPrice());
        property.setCity(updatedProperty.getCity());
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
        propertyService.save(property);
        model.addAttribute("properties", user.getProperties());
        return "/property/myProperties";
    }

    @Secured("ROLE_LANDLORD")
    @GetMapping("/delete/{id}")
    public String deleteProperty(@PathVariable int id, Model model) {
        Property property = propertyService.getProperty(id);
        if(property.getBookings().isEmpty()) {
            propertyService.deleteProperty(id);
            model.addAttribute("Message", "Your Property has been deleted successfully.");
        } else {
            model.addAttribute("Message", "Cannot delete property because there are still upcoming booking registered.");
        }
        return "redirect:/properties/my_properties";
    }


}

