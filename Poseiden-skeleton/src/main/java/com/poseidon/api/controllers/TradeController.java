package com.poseidon.api.controllers;

import com.poseidon.api.custom.exceptions.rating.TradeAlreadyExistsException;
import com.poseidon.api.custom.exceptions.trade.TradeValidationException;
import com.poseidon.api.model.Trade;
import com.poseidon.api.model.dto.TradeDto;
import com.poseidon.api.service.TradeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TradeController {

    @Autowired
    TradeService tradeService;

    @Autowired
    ModelMapper modelMapper;

    @RequestMapping("/trade/list")
    public String listTrades(Model model) {
        List<TradeDto> trades = tradeService.findAllTrades().stream()
                .map(trade -> modelMapper.map(trade, TradeDto.class))
                .collect(Collectors.toList());
        model.addAttribute("trades", trades);
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(Trade trade) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        try {
            boolean isCreated = tradeService.createTrade(trade);
            if (isCreated) {
                model.addAttribute("successMessage", "Le trade a été créé avec succès !");
            } else {
                model.addAttribute("errorMessage", "Le trade existe déjà !");
            }
            List<Trade> trades = tradeService.findAllTrades();
            model.addAttribute("trades", trades);
            return "trade/list";
        } catch (TradeAlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            List<Trade> trades = tradeService.findAllTrades();
            model.addAttribute("trades", trades);
            return "trade/list";
        } catch (TradeValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            List<Trade> trades = tradeService.findAllTrades();
            model.addAttribute("trades", trades);
            return "trade/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur est survenue lors de la création du trade !");
            List<Trade> trades = tradeService.findAllTrades();
            model.addAttribute("trades", trades);
            return "trade/list";
        }
    }


    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Trade by Id and to model then show to the form
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                              BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Trade and return Trade list
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Trade by Id and delete the Trade, return to Trade list
        return "redirect:/trade/list";
    }
}
