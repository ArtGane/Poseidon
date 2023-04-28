package com.poseidon.api.controller;

import com.poseidon.api.model.User;
import com.poseidon.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RolesAllowed({"ADMIN"})
    @RequestMapping("/user/list")
    public String userList(Model model, Authentication authentication) {
        if (!userService.isCurrentUserAdmin(authentication)) {
            return "redirect:/403";
        }
        model.addAttribute("users", userService.findAllUsers());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(User user) {
        return "user/add";

    }

    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                userService.createUser(Optional.of(user));
                model.addAttribute("message",
                        String.format("User with username '%s' and role '%s' was successfully created",
                                user.getUsername(), user.getRole()));
                model.addAttribute("users", userService.findAllUsers());
                return "redirect:/user/list";
            } catch (UsernameNotFoundException error) {
                result.rejectValue("username", "", "Username is already taken");
            }
        }
        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        try {
            User userToUpdate = userService.findUserById(id);
            if (userToUpdate != null) {
                userToUpdate.setPassword("");
                model.addAttribute("user", userToUpdate);
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        } catch (UsernameNotFoundException error) {
            model.addAttribute("errorMessage", error.getMessage());
            model.addAttribute("users", userService.findAllUsers());
            return "user/list";
        }

        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        try {
            userService.updateUser(id, Optional.of(user));
            model.addAttribute("message",
                    String.format("User '%s' was successfully updated", user.getUsername()));
            model.addAttribute("users", userService.findAllUsers());
            return "user/list";
        } catch (UsernameNotFoundException error) {
            throw new UsernameNotFoundException("Could not update user. Invalid user data.");
        }
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        try {
            String username = userService.findUserById(id).getUsername();
            userService.deleteUser(id);
            model.addAttribute("message", String.format("User '%s' was successfully deleted", username));
        } catch (UsernameNotFoundException error) {
            model.addAttribute("errorMessage", error.getMessage());
        }
        model.addAttribute("users", userService.findAllUsers());
        return "user/list";
    }

}
