package gr.hua.dit.ds.DS_PROJECT.controllers;

import gr.hua.dit.ds.DS_PROJECT.entities.Role;
import gr.hua.dit.ds.DS_PROJECT.entities.Status;
import gr.hua.dit.ds.DS_PROJECT.entities.User;
import gr.hua.dit.ds.DS_PROJECT.repositories.RoleRepository;
import gr.hua.dit.ds.DS_PROJECT.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {


    RoleRepository roleRepository;
    private UserService userService;

    public AuthController(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        }

    @PostConstruct
    public void setup() {
      Role role_tenant = new Role("ROLE_TENANT");
      Role role_landlord = new Role("ROLE_LANDLORD");
      Role role_admin = new Role("ROLE_ADMIN");

      roleRepository.updateOrInsert(role_tenant);
      roleRepository.updateOrInsert(role_landlord);
      roleRepository.updateOrInsert(role_admin);
    }

    @GetMapping("/login")
    public String login() { return "auth/login"; }

}
