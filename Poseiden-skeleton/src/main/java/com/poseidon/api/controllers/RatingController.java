package com.poseidon.api.controllers;

import com.poseidon.api.model.Rating;
import com.poseidon.api.model.User;
import com.poseidon.api.model.dto.RatingDto;
import com.poseidon.api.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class RatingController {

    @Autowired
    RatingService ratingService;
    @RequestMapping("/rating/list")
    public String home(Model model) {
        model.addAttribute("ratings", ratingService.findAllRatings());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(RatingDto ratingDto) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid RatingDto ratingDto, BindingResult result, Model model,
                           RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            Rating newRating = ratingService.convertDtoToEntity(ratingDto);
            ratingService.createRating(newRating);

            redirectAttributes.addFlashAttribute("message",
                    String.format("Rating with id " + newRating.getId() + " was successfully created"));

            model.addAttribute("ratings", ratingService.findAllRatings());

            return "redirect:/rating/list";
        }
        return "rating/add";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Rating ratingToUpdate = ratingService.findRatingById(id);
            RatingDto ratingDto = ratingService.convertEntityToDto(ratingToUpdate);
            ratingDto.setId(id);
            model.addAttribute("ratingDto", ratingDto);
        } catch (Exception error) {
            redirectAttributes.addFlashAttribute("message", error.getMessage());
            return "redirect:/rating/list";
        }
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid RatingDto ratingDto, BindingResult result,
                               Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "rating/update";
        }
        Rating updatedRating = ratingService.convertDtoToEntity(ratingDto);
        ratingService.updateRating(id, updatedRating);

        redirectAttributes.addFlashAttribute("message",
                String.format("Rating with id '%d' was successfully updated", id));

        model.addAttribute("ratings", ratingService.findAllRatings());

        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ratingService.deleteRating(id);
        } catch (Exception error) {
            redirectAttributes.addFlashAttribute("message", error.getMessage());
            return "redirect:/rating/list";
        }

        redirectAttributes.addFlashAttribute("message",
                String.format("Rating with id '%d' was successfully deleted", id));

        model.addAttribute("ratings", ratingService.findAllRatings());
        return "redirect:/rating/list";
    }
}