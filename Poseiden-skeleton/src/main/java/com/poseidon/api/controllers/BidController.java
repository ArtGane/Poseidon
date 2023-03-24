package com.poseidon.api.controllers;

import com.poseidon.api.custom.constantes.BidConstantes;
import com.poseidon.api.custom.exceptions.bid.BidConversionException;
import com.poseidon.api.custom.exceptions.bid.BidCreationException;
import com.poseidon.api.custom.exceptions.bid.BidNotFoundException;
import com.poseidon.api.model.Bid;
import com.poseidon.api.model.dto.BidDto;
import com.poseidon.api.service.BidService;
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
public class BidController {

    @Autowired
    BidService bidService;

    @RequestMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("bids", bidService.findAllBids());

        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidDto bidDto) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String createBid(@Valid BidDto bidDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "bidList/add";
        }
        try {
            Bid newBid = bidService.convertDtoToEntity(bidDto);
            bidService.createBid(newBid);
            redirectAttributes.addFlashAttribute("message", String.format(BidConstantes.CREATED_BID_MESSAGE_TEMPLATE, newBid.getId()));
            return "redirect:/bidList/list";
        } catch (BidConversionException | BidCreationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/bidList/add";
        }
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Bid bidToUpdate = bidService.findBidById(id);
            BidDto bidDto = bidService.convertEntityToDto(bidToUpdate);
            model.addAttribute("bidDto", bidDto);
            return "bidList/update";
        } catch (BidNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/bidList/list";
        } catch (BidConversionException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error converting Bid to BidDto");
            return "redirect:/bidList/list";
        }
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Long id, @Valid BidDto bidDto,
                            BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "bidList/update";
        }

        try {
            Bid updatedBid = bidService.convertDtoToEntity(bidDto);
            if (bidService.updateBid(id, updatedBid)) {
                redirectAttributes.addFlashAttribute("message", String.format("Bid with id " + id + " was successfully updated"));
                return "redirect:/bidList/list";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", String.format("Failed to update bid with id " + id));
                return "redirect:/bidList/update/" + id;
            }
        } catch (BidNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/bidList/list";
        }
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            bidService.deleteBid(id);
            redirectAttributes.addFlashAttribute("message", String.format("Bid with id " + id + " was successfully deleted"));
            model.addAttribute("bids", bidService.findAllBids());
        } catch (BidNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/bidList/list";
        }

        return "redirect:/bidList/list";
    }
}
