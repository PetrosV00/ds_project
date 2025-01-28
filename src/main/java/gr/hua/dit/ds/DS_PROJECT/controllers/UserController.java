package gr.hua.dit.ds.DS_PROJECT.controllers;

import gr.hua.dit.ds.DS_PROJECT.entities.Status;
import gr.hua.dit.ds.DS_PROJECT.entities.User;
import gr.hua.dit.ds.DS_PROJECT.repositories.RoleRepository;
import gr.hua.dit.ds.DS_PROJECT.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private UserService userService;
    private RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "auth/register";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        user.setStatus(Status.PENDING);
        userService.saveUser(user);
        model.addAttribute("users", userService.getUsers());
        return "index";
    }

    @GetMapping("/users")
    public String ShowUsers(Model model) {
        model.addAttribute("users", userService.getUsersByStatus(Status.APPROVED));
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

    @GetMapping("/user/{user_id}")
    public String showUser(@PathVariable Long user_id, Model model){
        model.addAttribute("user", userService.getUser(user_id));
        return "auth/user";
    }

    @PostMapping("/user/{user_id}")
    public String updateUser(@PathVariable Long user_id, @ModelAttribute("user") User user, Model model) {
        User updatedUser = (User) userService.getUser(user_id);
        updatedUser.setFirstName(user.getUsername());
        updatedUser.setLastName(user.getEmail());
        updatedUser.setStatus(user.getStatus());
        userService.updateUser(updatedUser);
        model.addAttribute("users", userService.getUsersByStatus(Status.APPROVED));
        return "auth/users";
    }

    @GetMapping("/pending_users")
    public String showPendingUsers(Model model) {
        model.addAttribute("users", userService.getUsersByStatus(Status.PENDING));
        return "auth/pending_users";
    }

    @GetMapping("/pending_users/{user_id}")
    public String showPendingUser(@PathVariable Long user_id, Model model) {
        model.addAttribute("user", userService.getUser(user_id));
        return "auth/pending_user";
    }

    @PostMapping("/pending_users/{user_id}")
    public String updatePendingUser(@PathVariable Long user_id, @ModelAttribute("user") User user, Model model) {
        User updatedUser = (User) userService.getUser(user_id);
        updatedUser.setStatus(user.getStatus());
        System.out.println(updatedUser);
        userService.updateUser(updatedUser);
        model.addAttribute("users", userService.getUsersByStatus(Status.PENDING));
        return "auth/users";
    }


}
