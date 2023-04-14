package com.poseidon.api.service;

import com.poseidon.api.custom.exceptions.rating.TradeAlreadyExistsException;
import com.poseidon.api.custom.exceptions.rating.TradeNotFoundException;
import com.poseidon.api.custom.exceptions.trade.InvalidTradeException;
import com.poseidon.api.model.Trade;
import com.poseidon.api.repositories.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TradeServiceTest {
    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTradeWithValidTrade() {
        Trade trade = new Trade();
        trade.setId(2L);
        trade.setAccount("A001");
        trade.setType("Stock");
        trade.setBuyQuantity(100.0);
        trade.setAction("Buy");

        when(tradeRepository.findById(1L)).thenReturn(Optional.of(trade));
        boolean created = tradeService.createTrade(trade);

        assertTrue(created);
    }

    @Test
    public void testCreateTradeWithExistingTrade() {
        Trade trade = new Trade();
        trade.setId(1L);
        trade.setAccount("A001");
        trade.setType("Stock");
        trade.setBuyQuantity(100.0);
        trade.setAction("Buy");

        when(tradeRepository.findById(1L)).thenReturn(Optional.of(trade));

        assertThrows(TradeAlreadyExistsException.class, () -> {
            tradeService.createTrade(trade);
        });
    }

    @Test
    public void testCreateTradeWithInvalidTrade() {
        Trade trade = new Trade();
        trade.setId(1L);

        when(tradeRepository.findById(1L)).thenReturn(null);

        assertThrows(InvalidTradeException.class, () -> {
            tradeService.createTrade(trade);
        });
    }

    @Test
    public void testCreateTradeWithNullTrade() {
        assertThrows(IllegalArgumentException.class, () -> {
            tradeService.createTrade(null);
        });
    }

    @Test
    public void testUpdateExistingTrade() {
        Trade existingTrade = new Trade();
        existingTrade.setId(1L);
        existingTrade.setAccount("A001");
        existingTrade.setType("Stock");
        existingTrade.setBuyQuantity(100.0);
        existingTrade.setAction("Buy");

        when(tradeRepository.findById(1L)).thenReturn(Optional.of(existingTrade));

        Trade updatedTrade = new Trade();
        updatedTrade.setId(1L);
        updatedTrade.setAccount("A002");
        updatedTrade.setType("Bond");
        updatedTrade.setBuyQuantity(200.0);
        updatedTrade.setAction("Sell");

        when(tradeRepository.save(existingTrade)).thenReturn(updatedTrade);

        Trade result = tradeService.updateTrade(updatedTrade);

        assertEquals(updatedTrade, result);

        assertEquals(updatedTrade.getAccount(), existingTrade.getAccount());
        assertEquals(updatedTrade.getType(), existingTrade.getType());
        assertEquals(updatedTrade.getBuyQuantity(), existingTrade.getBuyQuantity());
        assertEquals(updatedTrade.getAction(), existingTrade.getAction());

        verify(tradeRepository).save(existingTrade);
    }

    @Test
    public void testUpdateNonExistingTrade() {
        Trade trade = new Trade();
        trade.setId(1L);
        trade.setAccount("A001");
        trade.setType("Stock");
        trade.setBuyQuantity(100.0);
        trade.setAction("Buy");

        when(tradeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TradeNotFoundException.class, () -> tradeService.updateTrade(trade));

        verify(tradeRepository, never()).save(any());
    }

    @Test
    public void testUpdateNullTrade() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.updateTrade(null));

        verify(tradeRepository, never()).save(any());
    }

    @Test
    public void testDeleteTradeWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.deleteTrade(null));
    }

    @Test
    public void testDeleteTradeWithNonexistentTrade() {
        Long id = 1L;
        when(tradeRepository.existsById(id)).thenReturn(false);
        assertThrows(TradeNotFoundException.class, () -> tradeService.deleteTrade(id));
    }

    @Test
    public void testDeleteTradeWithValidId() {
        Long id = 1L;
        when(tradeRepository.existsById(id)).thenReturn(true);
        tradeService.deleteTrade(id);
        verify(tradeRepository).deleteById(id);
    }
}