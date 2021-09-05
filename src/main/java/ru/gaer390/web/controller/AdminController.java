package ru.gaer390.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gaer390.web.user.User;
import ru.gaer390.web.user.UserServiceImpl;

import java.util.stream.Collectors;

@Controller @RequestMapping(path = "/admin") @RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admins/admin";
    }

    @GetMapping(path = "/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "admins/new";
    }

    @PostMapping
    public String addNewUser(@ModelAttribute User user,
                             @RequestParam(required = false) String[] role) {
        userService.addNewUser(user);
        if (role != null) {
            for (String rl: role) {
                userService.addRoleToUser(user.getEmail(), rl);
            }
        }
        return "redirect:/admin";
    }

    @GetMapping(path = "/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", user.getRoles().stream().map(x -> x.getNameRole()).collect(Collectors.toSet()));
        return "admins/edit";
    }

    @PutMapping
    public String updateUser(@ModelAttribute User user,
                             @RequestParam(required = false) String[] role) {
        userService.updateUser(user);
        if (role != null) {
            for (String rl: role) {
                userService.addRoleToUser(user.getEmail(), rl);
            }
        }
        return "redirect:/admin";
    }

    @DeleteMapping(path = "/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
