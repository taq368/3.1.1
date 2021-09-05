package ru.gaer390.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gaer390.web.user.UserServiceImpl;

@Controller @RequestMapping(path = "/user")  @RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping
    public String getUser(Model model, Authentication authentication) {
        model.addAttribute("users", userService.getUserByEmail(authentication.getName()));
        return "users/user";
    }
}
