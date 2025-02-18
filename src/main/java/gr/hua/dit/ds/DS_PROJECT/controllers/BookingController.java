package gr.hua.dit.ds.DS_PROJECT.controllers;

import gr.hua.dit.ds.DS_PROJECT.entities.Booking;
import gr.hua.dit.ds.DS_PROJECT.entities.Property;
import gr.hua.dit.ds.DS_PROJECT.entities.Status;
import gr.hua.dit.ds.DS_PROJECT.entities.User;
import gr.hua.dit.ds.DS_PROJECT.services.BookingService;
import gr.hua.dit.ds.DS_PROJECT.services.PropertyService;
import gr.hua.dit.ds.DS_PROJECT.services.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    @GetMapping("/view")
    public String ShowMyBookings(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("bookings", user.getBookings());
        return "booking/bookings";
    }
    @Secured({"ROLE_TENANT"})
    @GetMapping("/{id}/new")
    public String addBooking(@PathVariable int id, Model model ) {
        Booking newBooking = new Booking();
        model.addAttribute("booking", newBooking);
        return "booking/booking";
    }

    @PostMapping("/{id}/new")
    public String saveBooking(@ModelAttribute("booking") Booking booking, @PathVariable int id, Model model) throws ParseException {
        Property property = propertyService.getProperty(id);
        for (Booking booking1 : property.getBookings()) {
            Date sdate = new SimpleDateFormat("yyyy-MM-dd").parse(booking.getStartDate());
            Date edate = new SimpleDateFormat("yyyy-MM-dd").parse(booking.getEndDate());
            Date b1_sdate = new SimpleDateFormat("yyyy-MM-dd").parse(booking1.getStartDate());
            Date b1_edate = new SimpleDateFormat("yyyy-MM-dd").parse(booking1.getEndDate());
            if(!(sdate.after(b1_edate) || edate.before(b1_sdate))) {
                model.addAttribute("booking", new Booking());
                return "booking/new_booking";
            }
        }
        booking.setId(0);
        booking.setStatus(Status.PENDING);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);

        bookingService.assignPropertyAndTenantToBooking(id, user.getId(), booking);

        model.addAttribute("bookings", List.of(booking));

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
