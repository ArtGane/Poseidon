package com.poseidon.api.service;

import com.poseidon.api.model.Rule;
import com.poseidon.api.repository.RuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RuleServiceTest {

    @Mock
    private RuleRepository ruleNameRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private RuleService ruleService;

    private Rule testRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public void setupForUpdateRules() {
        testRule = new Rule();
        testRule.setId(1L);
        testRule.setName("Test Rule");
        testRule.setDescription("This is a test rule.");
        testRule.setJson("{}");
        testRule.setTemplate("Template");
        testRule.setSqlStr("SqlStr");
        testRule.setSqlPart("SqlPart");
    }

    @Test
    public void testCreateRuleWithNullEntity() {
        String expectedMessage = messageSource.getMessage("create.rule.entity.null", null, Locale.getDefault());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ruleService.createRule(null));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testCreateRuleWithEmptyName() {
        Rule rule = new Rule();
        rule.setDescription("Description");
        rule.setJson("Json");
        rule.setTemplate("Template");
        rule.setSqlStr("SqlStr");
        rule.setSqlPart("SqlPart");
        String expectedMessage = messageSource.getMessage("create.rule.name.empty", null, Locale.getDefault());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ruleService.createRule(rule));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testCreateRuleWithEmptyDescription() {
        Rule rule = new Rule();
        rule.setName("Name");
        rule.setJson("Json");
        rule.setTemplate("Template");
        rule.setSqlStr("SqlStr");
        rule.setSqlPart("SqlPart");
        String expectedMessage = messageSource.getMessage("create.rule.description.empty", null, Locale.getDefault());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ruleService.createRule(rule));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testUpdateRuleSuccess() {
        setupForUpdateRules();

        Rule updatedRule = new Rule();
        updatedRule.setName("Updated Test Rule");
        updatedRule.setDescription("This is an updated test rule.");
        updatedRule.setJson("{}");
        updatedRule.setTemplate("Template");
        updatedRule.setSqlStr("SqlStr");
        updatedRule.setSqlPart("SqlPart");

        assertTrue(ruleService.updateRule(1L, updatedRule));

        assertNotEquals(updatedRule.getName(), testRule.getName());
        assertNotEquals(updatedRule.getDescription(), testRule.getDescription());

        assertEquals(updatedRule.getJson(), testRule.getJson());
        assertEquals(updatedRule.getTemplate(), testRule.getTemplate());
        assertEquals(updatedRule.getSqlStr(), testRule.getSqlStr());
        assertEquals(updatedRule.getSqlPart(), testRule.getSqlPart());
    }

    @Test
    public void testUpdateRuleNullId() {
        setupForUpdateRules();
        Rule updatedRule = new Rule();
        updatedRule.setDescription("This is an updated test rule.");
        updatedRule.setJson("{}");
        updatedRule.setTemplate("Template");
        updatedRule.setSqlStr("SqlStr");
        updatedRule.setSqlPart("SqlPart");

        String expectedMessage = messageSource.getMessage("update.rule.name.null", null, Locale.getDefault());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ruleService.updateRule(1L, updatedRule);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testUpdateRuleNullName() {
        setupForUpdateRules();
        Rule updatedRule = new Rule();
        updatedRule.setDescription("This is an updated test rule.");
        updatedRule.setJson("{}");
        updatedRule.setTemplate("Template");
        updatedRule.setSqlStr("SqlStr");
        updatedRule.setSqlPart("SqlPart");

        when(messageSource.getMessage("update.rule.name.null", null, Locale.getDefault()))
                .thenReturn("Expected message");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ruleService.updateRule(1L, updatedRule);
        });

        assertTrue(exception.getMessage().contains("Expected message"));
    }

    @Test
    void testDeleteRuleWithValidId() {
        Long id = 1L;
        Rule ruleToDelete = new Rule();
        ruleToDelete.setId(id);

        when(ruleNameRepository.findById(id)).thenReturn(Optional.of(ruleToDelete));

        boolean isDeleted = ruleService.deleteRule(id);

        assertThat(isDeleted).isTrue();
        verify(ruleNameRepository, times(1)).delete(ruleToDelete);
        verify(messageSource, never()).getMessage(anyString(), any(), any());
    }

    @Test
    void testDeleteRuleWithNonExistingIdShouldThrowUsernameNotFoundException() {
        Long id = 1L;

        when(ruleNameRepository.findById(id)).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("delete.rule.notfound"), eq(new Object[] {id}), any())).thenReturn("Règle non trouvée");

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> ruleService.deleteRule(id));
        assertThat(exception.getMessage()).isEqualTo("Règle non trouvée");
        verify(ruleNameRepository, times(1)).findById(id);
        verify(ruleNameRepository, never()).delete(any());
        verify(messageSource, times(1)).getMessage(eq("delete.rule.notfound"), eq(new Object[] {id}), any());
    }
}