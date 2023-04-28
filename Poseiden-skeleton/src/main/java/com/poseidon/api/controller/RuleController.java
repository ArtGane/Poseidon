package com.poseidon.api.controller;

import com.poseidon.api.model.Rule;
import com.poseidon.api.repository.RuleRepository;
import com.poseidon.api.service.RuleService;
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
import java.util.Optional;

@Controller
public class RuleController {

    @Autowired
    RuleService ruleService;
    @Autowired
    private RuleRepository ruleRepository;

    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        model.addAttribute("rules", ruleRepository.findAll());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(Rule rule) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid Rule rule, BindingResult result, Model model,
                           RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            ruleService.createRule(rule);
            redirectAttributes.addFlashAttribute("message",
                    String.format("Rule with id '%d' was successfully created", rule.getId()));
            model.addAttribute("rules", ruleRepository.findAll());
            return "redirect:/ruleName/list";
        }
        return "ruleName/add";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

        try {
            Optional<Rule> ruleToUpdate = ruleRepository.findById(id);
            model.addAttribute("rule", ruleToUpdate);
        } catch (Exception error) {
            redirectAttributes.addFlashAttribute("message", error.getMessage());
            return "redirect:/ruleName/list";
        }
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRule(@PathVariable("id") Long id, @Valid Rule rule, BindingResult result,
                             Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "ruleName/update";
        }

        ruleService.updateRule(id, rule);

        redirectAttributes.addFlashAttribute("message",
                String.format("Rule with id '%d' was successfully updated", id));

        model.addAttribute("rules", ruleRepository.findAll());

        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ruleService.deleteRule(id);
        } catch (Exception error) {
            redirectAttributes.addFlashAttribute("message", error.getMessage());
            return "redirect:/ruleName/list";
        }

        redirectAttributes.addFlashAttribute("message",
                String.format("Rule with id '%d' was successfully deleted", id));

        model.addAttribute("rules", ruleRepository.findAll());

        return "redirect:/ruleName/list";
    }
}