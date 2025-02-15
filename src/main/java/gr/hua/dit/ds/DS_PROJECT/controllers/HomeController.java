package gr.hua.dit.ds.DS_PROJECT.controllers;

import gr.hua.dit.ds.DS_PROJECT.entities.Status;
import gr.hua.dit.ds.DS_PROJECT.entities.User;
import gr.hua.dit.ds.DS_PROJECT.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("email: " + email);
        if (!email.equals("anonymousUser")) {
            User user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
            if (user.getStatus().equals(Status.PENDING)) {
                return "auth/pending_register";
            } else {
                return "index";
            }
        } else {
            return "index";
        }
    }
}
