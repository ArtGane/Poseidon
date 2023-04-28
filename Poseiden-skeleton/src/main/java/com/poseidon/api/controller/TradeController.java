package com.poseidon.api.controller;

import com.poseidon.api.custom.exceptions.rating.TradeAlreadyExistsException;
import com.poseidon.api.custom.exceptions.trade.TradeValidationException;
import com.poseidon.api.model.Trade;
import com.poseidon.api.repository.TradeRepository;
import com.poseidon.api.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class TradeController {

    @Autowired
    TradeService tradeService;

    @Autowired
    private TradeRepository tradeRepository;

    @RequestMapping("/trade/list")
    public String listTrades(Model model) {
        List<Trade> trades = tradeRepository.findAll();
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
            List<Trade> trades = tradeRepository.findAll();
            model.addAttribute("trades", trades);
            return "trade/list";
        } catch (TradeAlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            List<Trade> trades = tradeRepository.findAll();
            model.addAttribute("trades", trades);
            return "trade/list";
        } catch (TradeValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            List<Trade> trades = tradeRepository.findAll();
            model.addAttribute("trades", trades);
            return "trade/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur est survenue lors de la création du trade !");
            List<Trade> trades = tradeRepository.findAll();
            model.addAttribute("trades", trades);
            return "trade/list";
        }
    }


    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws ChangeSetPersister.NotFoundException {
        Optional<Trade> trade = tradeRepository.findById(id);
        if (trade == null) {
            model.addAttribute("errorMessage", "Le trade avec l'ID " + id + " n'existe pas !");
            return "trade/list";
        }
        model.addAttribute("trade", trade);
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Long id, @Valid Trade trade, BindingResult result, Model model) throws ChangeSetPersister.NotFoundException {
        if (result.hasErrors()) {
            model.addAttribute("trade", trade);
            return "trade/edit";
        }

        Optional<Trade> existingTrade = tradeRepository.findById(id);
        if (existingTrade == null) {
            model.addAttribute("errorMessage", "Le trade avec l'ID " + id + " n'existe pas !");
            List<Trade> trades = tradeRepository.findAll();
            model.addAttribute("trades", trades);
            return "trade/list";
        }

        try {
            trade.setId(id);
            tradeService.updateTrade(trade);
            model.addAttribute("successMessage", "Le trade a été mis à jour avec succès !");
            List<Trade> trades = tradeRepository.findAll();
            model.addAttribute("trades", trades);
            return "trade/list";
        } catch (TradeValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("trade", trade);
            return "trade/edit";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur est survenue lors de la mise à jour du trade !");
            List<Trade> trades = tradeRepository.findAll();
            model.addAttribute("trades", trades);
            return "trade/list";
        }
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Long id, Model model) throws ChangeSetPersister.NotFoundException {
        Optional<Trade> trade = tradeRepository.findById(id);
        if (trade == null) {
            model.addAttribute("errorMessage", "Le trade avec l'ID " + id + " n'existe pas !");
        } else {
            tradeService.deleteTrade(id);
            model.addAttribute("successMessage", "Le trade a été supprimé avec succès !");
        }
        List<Trade> trades = tradeRepository.findAll();
        model.addAttribute("trades", trades);
        return "trade/list";
    }
}
