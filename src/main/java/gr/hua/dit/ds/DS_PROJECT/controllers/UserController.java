package gr.hua.dit.ds.DS_PROJECT.controllers;

import gr.hua.dit.ds.DS_PROJECT.entities.Role;
import gr.hua.dit.ds.DS_PROJECT.entities.Status;
import gr.hua.dit.ds.DS_PROJECT.entities.User;
import gr.hua.dit.ds.DS_PROJECT.repositories.RoleRepository;
import gr.hua.dit.ds.DS_PROJECT.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    public String saveUser(@ModelAttribute("user") User user, Model model, @RequestParam String role) {
        user.setStatus(Status.PENDING);
        Role role1 = roleRepository.findByName(role).get();
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        user.setRoles(roles);
        userService.saveUser(user);
        model.addAttribute("users", userService.getUsers());
        return "/auth/pending_register";
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
        return "auth/pending_users";
    }

    @GetMapping ("/user/role/delete/{user_id}/{role_id}")
    public String deteteRole(@PathVariable Long user_id, @PathVariable int role_id, Model model) {
        User user = (User) userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().remove(role);
        userService.updateUser(user);
        model.addAttribute("users", userService.getUsersByStatus(Status.APPROVED));
        model.addAttribute("roles", roleRepository.findAll());
        return "redirect:/users";
    }

    @GetMapping("/user/role/add/{user_id}/{role_id}")
    public String addRole(@PathVariable Long user_id, @PathVariable Integer role_id, Model model){
        User user = (User) userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().add(role);
        System.out.println("Roles: "+user.getRoles());
        userService.updateUser(user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "redirect:/users";
    }


}
