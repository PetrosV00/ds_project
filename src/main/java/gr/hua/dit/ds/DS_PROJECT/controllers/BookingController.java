package gr.hua.dit.ds.DS_PROJECT.controllers;

import gr.hua.dit.ds.DS_PROJECT.entities.Booking;
import gr.hua.dit.ds.DS_PROJECT.entities.Status;
import gr.hua.dit.ds.DS_PROJECT.entities.User;
import gr.hua.dit.ds.DS_PROJECT.services.BookingService;
import gr.hua.dit.ds.DS_PROJECT.services.PropertyService;
import gr.hua.dit.ds.DS_PROJECT.services.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Secured("APPROVED")
@Controller
@RequestMapping("/booking")
public class BookingController {


    private BookingService bookingService;
    private PropertyService propertyService;
    private UserService userService;

    public BookingController(BookingService bookingService, PropertyService propertyService, UserService userService) {
        this.bookingService = bookingService;
        this.propertyService = propertyService;
        this.userService = userService;
    }
    @GetMapping("")
    public String showProperties(Model model) {
        model.addAttribute("properties", propertyService.getProperties());
        return "property/properties";
    }

    @GetMapping("/view")
    public String ShowMyBookings(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("bookings", user.getBookings());
        return "booking/bookings";
    }

    @GetMapping("/{id}/new")
    public String addBooking(@PathVariable int id, Model model ) {
        Booking newBooking = new Booking();
        model.addAttribute("booking", newBooking);
        return "booking/booking";
    }

    @PostMapping("/{id}/new")
    public String saveBooking(@ModelAttribute("booking") Booking booking, @PathVariable int id, Model model) {
        booking.setId(0);
        booking.setStatus(Status.PENDING);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
        bookingService.assignPropertyAndTenantToBooking(id, user.getId(), booking);
        model.addAttribute("bookings", booking);
        return "booking/bookingConfirmation";
    }

    @GetMapping("/{booking_id}/cancel")
    public String cancelBooking(@PathVariable int booking_id, Model model) {
        Booking booking = bookingService.getBookingById(booking_id);
        bookingService.removeBooking(booking_id);
        model.addAttribute("bookings", booking);
        return "booking/bookingCancelation";
    }

    @GetMapping("/pending_booking_requests")
    public String showPendingBookings(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("bookings", bookingService.getMyPropertyBookings(user.getId()));
        return "booking/bookings";
    }

    @GetMapping("/pending_booking_requests/{id}")
    public String showPendingBookingStatus(@PathVariable int id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        return "booking/bookingStatus";
    }

    @PostMapping("/pending_booking_requests/{id}")
    public String approveBooking(@ModelAttribute("booking") Booking booking, @PathVariable int id, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
        Booking updatedBooking = bookingService.getBookingById(id);
        updatedBooking.setStatus(booking.getStatus());
        bookingService.save(updatedBooking);
        model.addAttribute("bookings", bookingService.getMyPropertyBookings(user.getId()));
        return "booking/bookings";
    }

}
