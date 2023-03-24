package com.poseidon.api.controllers;

import com.poseidon.api.custom.exceptions.rating.RatingDeletionException;
import com.poseidon.api.model.Rating;
import com.poseidon.api.model.dto.RatingDto;
import com.poseidon.api.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

        if (ratingDto == null) {
            throw new IllegalArgumentException("RatingDto ne peut pas être null");
        }

        if (result.hasErrors()) {
            return "rating/add";
        }

        Rating newRating = ratingService.convertDtoToEntity(ratingDto);
        boolean created = ratingService.createRating(newRating);

        if (created) {
            redirectAttributes.addFlashAttribute("message",
                    String.format("Le rating avec l'id %d a été créé avec succès", newRating.getId()));

            model.addAttribute("ratings", ratingService.findAllRatings());

            return "redirect:/rating/list";
        } else {
            model.addAttribute("error", "Échec de la création du rating");
            return "rating/add";
        }
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

        if (id == null) {
            redirectAttributes.addFlashAttribute("message", "L'ID ne peut pas être nul");
            return "redirect:/rating/list";
        }

        Rating ratingToUpdate = ratingService.findRatingById(id);
        if (ratingToUpdate == null) {
            redirectAttributes.addFlashAttribute("message", "Aucun rating trouvé avec l'ID " + id);
            return "redirect:/rating/list";
        }

        RatingDto ratingDto = ratingService.convertEntityToDto(ratingToUpdate);
        ratingDto.setId(id);
        model.addAttribute("ratingDto", ratingDto);

        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Long id, @Valid RatingDto ratingDto, BindingResult result,
                               Model model, RedirectAttributes redirectAttributes) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("L'ID ne peut pas être nul");
            }

            if (result.hasErrors()) {
                return "rating/update";
            }

            Rating updatedRating = ratingService.convertDtoToEntity(ratingDto);
            ratingService.updateRating(id, updatedRating);

            redirectAttributes.addFlashAttribute("message",
                    String.format("La notation avec l'ID '%d' a été mise à jour avec succès", id));

            model.addAttribute("ratings", ratingService.findAllRatings());

            return "redirect:/rating/list";
        } catch (IllegalArgumentException | DataAccessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/rating/list";
        }
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            ratingService.deleteRating(id);
            redirectAttributes.addFlashAttribute("message",
                    String.format("Le rating avec l'identifiant '%d' a été supprimé avec succès", id));
        } catch (RatingDeletionException error) {
            redirectAttributes.addFlashAttribute("message", error.getMessage());
        }

        return "redirect:/rating/list";
    }
}