package com.poseidon.api.service;

import com.poseidon.api.model.Rule;
import com.poseidon.api.repositories.RuleRepository;
import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
public class RuleService {

    @Autowired
    RuleRepository ruleNameRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Crée une nouvelle règle dans la base de données à partir d'une entité de règle donnée.
     *
     * @param ruleEntity l'entité de règle à enregistrer dans la base de données
     * @return true si la règle a été créée avec succès, false sinon
     * @throws UsernameNotFoundException si une règle avec le même identifiant existe déjà dans la base de données
     * @throws IllegalArgumentException si l'entité de règle est null ou si l'un des champs requis est null ou vide
     */
    public boolean createRule(Rule ruleEntity) throws UsernameNotFoundException, IllegalArgumentException {
        if (ruleEntity == null) {
            throw new IllegalArgumentException("Impossible de créer la règle : l'entité de règle est null.");
        }
        if (StringUtils.isEmpty(ruleEntity.getName())) {
            throw new IllegalArgumentException("Impossible de créer la règle : le nom de la règle est null ou vide.");
        }
        if (StringUtils.isEmpty(ruleEntity.getDescription())) {
            throw new IllegalArgumentException("Impossible de créer la règle : la description de la règle est null ou vide.");
        }
        if (StringUtils.isEmpty(ruleEntity.getJson())) {
            throw new IllegalArgumentException("Impossible de créer la règle : le JSON de la règle est null ou vide.");
        }
        if (StringUtils.isEmpty(ruleEntity.getTemplate())) {
            throw new IllegalArgumentException("Impossible de créer la règle : le modèle de la règle est null ou vide.");
        }
        if (StringUtils.isEmpty(ruleEntity.getSqlStr())) {
            throw new IllegalArgumentException("Impossible de créer la règle : la chaîne SQL de la règle est null ou vide.");
        }
        if (StringUtils.isEmpty(ruleEntity.getSqlPart())) {
            throw new IllegalArgumentException("Impossible de créer la règle : la partie SQL de la règle est null ou vide.");
        }
        if (!ruleNameRepository.findRuleById(ruleEntity.getId()).isPresent()) {
            ruleNameRepository.save(ruleEntity);
            log.info("[RuleConfiguration] Une nouvelle règle a été créée avec l'identifiant '{}' et le nom '{}'", ruleEntity.getId(),
                    ruleEntity.getName());
            return true;
        }
        throw new UsernameNotFoundException("Impossible de créer la règle : une règle avec le même identifiant existe déjà.");
    }

    /**
     * Met à jour la règle identifiée par l'ID avec les informations de la règle mise à jour.
     *
     * @param id l'ID de la règle à mettre à jour
     * @param ruleEntityUpdated les informations mises à jour de la règle
     * @return true si la mise à jour a été effectuée avec succès, false sinon
     * @throws UsernameNotFoundException si la règle à mettre à jour n'a pas été trouvée dans la base de données
     * @throws IllegalArgumentException si l'ID de la règle ou l'une des informations mises à jour est null ou vide
     */
    public boolean updateRule(Long id, Rule ruleEntityUpdated) throws UsernameNotFoundException, IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Impossible de mettre à jour la règle : l'ID de la règle est null.");
        }

        Optional<Rule> ruleName = ruleNameRepository.findRuleById(id);
        if (!ruleName.isPresent()) {
            throw new UsernameNotFoundException("Impossible de trouver la règle avec l'ID : " + id);
        }

        if (ruleEntityUpdated == null) {
            throw new IllegalArgumentException("Impossible de mettre à jour la règle : les informations mises à jour sont null.");
        }

        if (StringUtils.isEmpty(ruleEntityUpdated.getName())) {
            throw new IllegalArgumentException("Impossible de mettre à jour la règle : le nom de la règle est null ou vide.");
        }

        if (StringUtils.isEmpty(ruleEntityUpdated.getDescription())) {
            throw new IllegalArgumentException("Impossible de mettre à jour la règle : la description de la règle est null ou vide.");
        }

        if (StringUtils.isEmpty(ruleEntityUpdated.getJson())) {
            throw new IllegalArgumentException("Impossible de mettre à jour la règle : la structure JSON de la règle est null ou vide.");
        }

        if (StringUtils.isEmpty(ruleEntityUpdated.getTemplate())) {
            throw new IllegalArgumentException("Impossible de mettre à jour la règle : le modèle de la règle est null ou vide.");
        }

        if (StringUtils.isEmpty(ruleEntityUpdated.getSqlStr())) {
            throw new IllegalArgumentException("Impossible de mettre à jour la règle : la requête SQL de la règle est null ou vide.");
        }

        if (StringUtils.isEmpty(ruleEntityUpdated.getSqlPart())) {
            throw new IllegalArgumentException("Impossible de mettre à jour la règle : la partie SQL de la règle est null ou vide.");
        }

        ruleEntityUpdated.setId(id);
        ruleNameRepository.save(ruleEntityUpdated);

        log.info("[RuleConfiguration] Règle mise à jour avec succès - ID : '{}' Nom : '{}'", ruleEntityUpdated.getId(),
                ruleEntityUpdated.getName());
        return true;
    }

    /**
     * Supprime la règle correspondant à l'identifiant spécifié.
     *
     * @param id l'identifiant de la règle à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws IllegalArgumentException si l'identifiant est null
     * @throws UsernameNotFoundException si la règle correspondant à l'identifiant n'a pas été trouvée
     * @throws RuntimeException si une erreur survient lors de la suppression de la règle
     */
    public boolean deleteRule(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(messageSource.getMessage("delete.rule.id.null", null, Locale.getDefault()));
        }

        Optional<Rule> ruleOptional = ruleNameRepository.findRuleById(id);
        if (!ruleOptional.isPresent()) {
            throw new UsernameNotFoundException(messageSource.getMessage("delete.rule.notfound", new Object[] {id}, Locale.getDefault()));
        }

        try {
            ruleNameRepository.delete(ruleOptional.get());
            log.info("[RuleConfiguration] Deleted rule id '{}'", id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(messageSource.getMessage("delete.rule.error", null, Locale.getDefault()), e);
        }
    }

}
