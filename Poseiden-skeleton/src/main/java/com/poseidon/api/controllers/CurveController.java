package com.poseidon.api.controllers;

import com.poseidon.api.config.Utils;
import com.poseidon.api.custom.exceptions.curve.CurvePointNotFoundException;
import com.poseidon.api.model.CurvePoint;
import com.poseidon.api.repositories.CurvePointRepository;
import com.poseidon.api.service.CurvePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
public class CurveController {

    @Autowired
    CurvePointService curvePointService;
    @Autowired
    private CurvePointRepository curvePointRepository;

    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        model.addAttribute("curvePoints", Utils.findAll(curvePointRepository));
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(CurvePoint curvePoint) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@ModelAttribute("curvePoint") @Valid CurvePoint curvePoint, BindingResult result, Model model,
                           RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            curvePointService.createCurve(curvePoint);
            model.addAttribute("curvePoints", Utils.findAll(curvePointRepository));
            redirectAttributes.addFlashAttribute("message",
                    String.format("Curve Point with id " + curvePoint.getId() + " was successfully created"));
            return "redirect:/curvePoint/list";
        }
        return "curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) throws CurvePointNotFoundException, ChangeSetPersister.NotFoundException {
        CurvePoint curvePointToUpdate = Utils.findById(id, curvePointRepository);
        model.addAttribute("curvePoint", curvePointToUpdate);

        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Long id, @Valid CurvePoint updatedCurvePoint,
                            BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "curvePoint/update";
        }
        curvePointService.updateCurve(id, updatedCurvePoint);
        model.addAttribute("curvePoints", Utils.findAll(curvePointRepository));
        redirectAttributes.addFlashAttribute("message",
                String.format("Curve Point with id '%d' was successfully updated", id));
        redirectAttributes.addFlashAttribute("message_type", "alert-primary");

        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Long id, Model model) {
        curvePointService.deleteCurve(id);
        model.addAttribute("message",
                String.format("Curve Point with id '%d' was successfully deleted", id));
        model.addAttribute("curvePoints", Utils.findAll(curvePointRepository));
        return "curvePoint/list";
    }
}
