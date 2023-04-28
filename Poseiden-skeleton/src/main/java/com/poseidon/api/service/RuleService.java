package com.poseidon.api.service;

import com.poseidon.api.model.Rule;
import com.poseidon.api.repository.RuleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
public class RuleService {

    @Autowired
    RuleRepository ruleNameRepository;

    @Autowired
    private MessageSource messageSource;


    /**
     * Crée une nouvelle règle dans la base de données à partir d'une entité de règle donnée.
     *
     * @param ruleEntity l'entité de règle à enregistrer dans la base de données
     * @return true si la règle a été créée avec succès, false sinon
     * @throws UsernameNotFoundException si une règle avec le même identifiant existe déjà dans la base de données
     * @throws IllegalArgumentException si l'entité de règle est null ou si l'un des champs requis est null ou vide
     */
    public boolean createRule(Rule ruleEntity) throws UsernameNotFoundException, IllegalArgumentException {if (ruleEntity == null) {
        throw new IllegalArgumentException(messageSource.getMessage("create.rule.null", null, Locale.getDefault()));
    }
        if (StringUtils.isEmpty(ruleEntity.getName())) {
            throw new IllegalArgumentException(messageSource.getMessage("create.rule.name.null", null, Locale.getDefault()));
        }
        if (StringUtils.isEmpty(ruleEntity.getDescription())) {
            throw new IllegalArgumentException(messageSource.getMessage("create.rule.description.null", null, Locale.getDefault()));
        }
        if (StringUtils.isEmpty(ruleEntity.getJson())) {
            throw new IllegalArgumentException(messageSource.getMessage("create.rule.json.null", null, Locale.getDefault()));
        }
        if (StringUtils.isEmpty(ruleEntity.getTemplate())) {
            throw new IllegalArgumentException(messageSource.getMessage("create.rule.template.null", null, Locale.getDefault()));
        }
        if (StringUtils.isEmpty(ruleEntity.getSqlStr())) {
            throw new IllegalArgumentException(messageSource.getMessage("create.rule.sqlstr.null", null, Locale.getDefault()));
        }
        if (StringUtils.isEmpty(ruleEntity.getSqlPart())) {
            throw new IllegalArgumentException(messageSource.getMessage("create.rule.sqlpart.null", null, Locale.getDefault()));
        }
        if (!ruleNameRepository.findById(ruleEntity.getId()).isPresent()) {
            ruleNameRepository.save(ruleEntity);
            log.info(messageSource.getMessage("create.rule.success", new Object[] {ruleEntity.getId(), ruleEntity.getName()}, Locale.getDefault()));
            return true;
        }
        throw new UsernameNotFoundException(messageSource.getMessage("create.rule.id.exist", null, Locale.getDefault()));

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
            throw new IllegalArgumentException(messageSource.getMessage("update.rule.id.null", null, Locale.getDefault()));
        }

        if (ruleEntityUpdated == null) {
            throw new IllegalArgumentException(messageSource.getMessage("update.rule.updatedInfo.null", null, Locale.getDefault()));
        }

        if (StringUtils.isEmpty(ruleEntityUpdated.getName())) {
            throw new IllegalArgumentException(messageSource.getMessage("update.rule.name.null", null, Locale.getDefault()));
        }

        if (StringUtils.isEmpty(ruleEntityUpdated.getDescription())) {
            throw new IllegalArgumentException(messageSource.getMessage("update.rule.description.null", null, Locale.getDefault()));
        }

        if (StringUtils.isEmpty(ruleEntityUpdated.getJson())) {
            throw new IllegalArgumentException(messageSource.getMessage("update.rule.json.null", null, Locale.getDefault()));
        }

        if (StringUtils.isEmpty(ruleEntityUpdated.getTemplate())) {
            throw new IllegalArgumentException(messageSource.getMessage("update.rule.template.null", null, Locale.getDefault()));
        }

        if (StringUtils.isEmpty(ruleEntityUpdated.getSqlStr())) {
            throw new IllegalArgumentException(messageSource.getMessage("update.rule.sqlstr.null", null, Locale.getDefault()));
        }

        if (StringUtils.isEmpty(ruleEntityUpdated.getSqlPart())) {
            throw new IllegalArgumentException(messageSource.getMessage("update.rule.sqlpart.null", null, Locale.getDefault()));
        }

        ruleEntityUpdated.setId(id);
        ruleNameRepository.save(ruleEntityUpdated);

        log.info(messageSource.getMessage("update.rule.success", new Object[] {ruleEntityUpdated.getId(), ruleEntityUpdated.getName()}, Locale.getDefault()));
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

        Optional<Rule> ruleOptional = ruleNameRepository.findById(id);
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
