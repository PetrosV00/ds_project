package gr.hua.dit.ds.DS_PROJECT.controllers;

import gr.hua.dit.ds.DS_PROJECT.entities.Booking;
import gr.hua.dit.ds.DS_PROJECT.entities.Role;
import gr.hua.dit.ds.DS_PROJECT.entities.Status;
import gr.hua.dit.ds.DS_PROJECT.entities.User;
import gr.hua.dit.ds.DS_PROJECT.repositories.RoleRepository;
import gr.hua.dit.ds.DS_PROJECT.services.BookingService;
import gr.hua.dit.ds.DS_PROJECT.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class AuthController {


    RoleRepository roleRepository;
    private UserService userService;
    private BookingService bookingService;

    public AuthController(RoleRepository roleRepository, UserService userService, BookingService bookingService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.bookingService = bookingService;
        }

    @PostConstruct
    public void setup() throws ParseException {
      Role role_tenant = new Role("ROLE_TENANT");
      Role role_landlord = new Role("ROLE_LANDLORD");
      Role role_admin = new Role("ROLE_ADMIN");

      roleRepository.updateOrInsert(role_tenant);
      roleRepository.updateOrInsert(role_landlord);
      roleRepository.updateOrInsert(role_admin);

      List<Booking> bookings = bookingService.getAllBookings();
      String currentDate = java.time.LocalDate.now().toString();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      Date currdare = formatter.parse(currentDate);
      for (Booking booking : bookings) {
          Date eDate = formatter.parse(booking.getEndDate());
          if (eDate.before(currdare)) {
              bookingService.removeBooking(booking.getId());
          }
      }
    }

    @GetMapping("/login")
    public String login() { return "auth/login"; }

}
