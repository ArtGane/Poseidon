package com.poseidon.api.controllers;

import com.poseidon.api.model.User;
import com.poseidon.api.model.dto.UserDto;
import com.poseidon.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.Optional;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(UserDto userDto) {
        return "user/add";

    }

    @PostMapping("/user/validate")
    public String validate(@Valid UserDto userDto, BindingResult result, Model model,
                           RedirectAttributes redirectAttributes) {

        if (!result.hasErrors()) {
            Optional<User> newUserOptional = userService.convertDtoToEntity(userDto);

            if (newUserOptional.isPresent()) {
                User newUser = newUserOptional.get();
                try {
                    userService.createUser(Optional.of(newUser));
                } catch (UsernameNotFoundException error) {
                    result.rejectValue("username", "", "Username is already taken");
                    return "user/add";
                }
                redirectAttributes.addFlashAttribute("message",
                        String.format("User with username '%s' and role '%s' was successfully created",
                                newUser.getUsername(), newUser.getRole()));
                model.addAttribute("users", userService.findAllUsers());
                return "redirect:/user/list";
            }
        }
        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User userToUpdate = userService.findUserById(id);
            Optional<UserDto> userOptional = userService.convertEntityToDto(userToUpdate);

            if (userOptional.isPresent()) {
                UserDto user = userOptional.get();
                user.setPassword("");
                model.addAttribute("userDto", user);
            } else {
                throw new UsernameNotFoundException("User not found");
            }

        } catch (UsernameNotFoundException error) {
            redirectAttributes.addFlashAttribute("message", error.getMessage());
            return "redirect:/user/list";
        }

        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @Valid UserDto userDto, BindingResult result, Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/update";
        }
        Optional<User> newUserOptional = userService.convertDtoToEntity(userDto);
        if (newUserOptional.isPresent()) {
            User newUser = newUserOptional.get();
            userService.updateUser(id, newUser);
            redirectAttributes.addFlashAttribute("message",
                    String.format("User '%s' was successfully updated", newUser.getUsername()));
//      redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.PRIMARY);
            model.addAttribute("users", userService.findAllUsers());

            return "redirect:/user/list";
        } else {
            throw new UsernameNotFoundException("Could not update user. Invalid user data.");
        }
    }

    /**
     * Delete a user by its ID
     *
     * @param id
     * @return									The users list if the deletion was successful
     *
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        String username = "";
        try {
            username = userService.findUserById(id).getUsername();
            userService.deleteUser(id);
        } catch (UsernameNotFoundException error) {
            redirectAttributes.addFlashAttribute("message", error.getMessage());
            return "redirect:/user/list";
        }
        redirectAttributes.addFlashAttribute("message", String.format("User '%s' was successfully deleted", username));
        model.addAttribute("users", userService.findAllUsers());
        return "redirect:/user/list";
    }


}
