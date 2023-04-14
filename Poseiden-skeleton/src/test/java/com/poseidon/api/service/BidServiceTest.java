package com.poseidon.api.service;

import com.poseidon.api.custom.exceptions.bid.BidAlreadyExistsException;
import com.poseidon.api.custom.exceptions.bid.BidNotFoundException;
import com.poseidon.api.custom.exceptions.bid.InvalidBidException;
import com.poseidon.api.model.Bid;
import com.poseidon.api.repositories.BidRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class BidServiceTest {

    @InjectMocks
    private BidService bidService;

    @Mock
    private BidRepository bidRepository;


    @Test
    void createBid_whenBidIsNull_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bidService.createBid(null));
    }

    @Test
    void createBid_whenBidAlreadyExists_thenThrowBidAlreadyExistsException() {
        Bid existingBid = new Bid(1L, "account1", "type1", 100.0);
        when(bidRepository.findById(existingBid.getId())).thenReturn(Optional.of(existingBid));

        Bid newBid = new Bid(1L, "account2", "type2", 200.0);
        assertThrows(BidAlreadyExistsException.class, () -> bidService.createBid(newBid));

        verify(bidRepository, never()).save(any(Bid.class));
    }

    @Test
    void createBid_whenBidIsInvalid_thenThrowInvalidBidException() {
        Bid invalidBid = new Bid(1L, null, null, -100.0);
        assertThrows(InvalidBidException.class, () -> bidService.createBid(invalidBid));

        verify(bidRepository, never()).save(any(Bid.class));
    }

    @Test
    void createBid_whenBidIsValid_thenSaveBid() throws InvalidBidException, BidAlreadyExistsException {
        Bid validBid = new Bid(1L, "account1", "type1", 100.0);
        when(bidRepository.findById(validBid.getId())).thenReturn(Optional.empty());

        bidService.createBid(validBid);

        verify(bidRepository, times(1)).save(validBid);
    }

    @Test
    public void testUpdateBidWithValidInput() throws Exception {
        Long id = 1L;
        Bid existingBid = new Bid(id, "account1", "type1", 100.0);
        Bid updatedBid = new Bid(id, "account2", "type2", 200.0);
        when(bidRepository.findById(id)).thenReturn(Optional.of(existingBid));

        boolean result = bidService.updateBid(id, updatedBid);

        assertTrue(result);
        assertEquals(existingBid.getAccount(), updatedBid.getAccount());
        assertEquals(existingBid.getBidQuantity(), updatedBid.getBidQuantity());
        verify(bidRepository, times(1)).findById(id);
        verify(bidRepository, times(1)).save(existingBid);
    }

    @Test
    public void testUpdateBidWithInvalidBid() {
        Long id = 1L;
        Bid existingBid = new Bid(id, "account1", "type1", 100.0);
        Bid updatedBid = new Bid(id, null, null, null);
        when(bidRepository.findById(id)).thenReturn(Optional.of(existingBid));

        assertThrows(InvalidBidException.class, () -> bidService.updateBid(id, updatedBid));
        verify(bidRepository, never()).findById(id);
        verify(bidRepository, never()).save(existingBid);
    }

    @Test
    public void testUpdateBidWithNegativeQuantity() {
        Long id = 1L;
        Bid existingBid = new Bid(id, "account1", "type1", 100.0);
        Bid updatedBid = new Bid(id, "account2", "type2", -10.0);
        when(bidRepository.findById(id)).thenReturn(Optional.of(existingBid));

        assertThrows(InvalidBidException.class, () -> bidService.updateBid(id, updatedBid));
        verify(bidRepository, never()).findById(id);
        verify(bidRepository, never()).save(existingBid);
    }

    @Test
    public void testUpdateBidWithNonExistingBid() {
        Long id = 1L;
        Bid updatedBid = new Bid(id, "account2", "type2", 200.0);
        when(bidRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BidNotFoundException.class, () -> bidService.updateBid(id, updatedBid));
        verify(bidRepository, times(1)).findById(id);
        verify(bidRepository, never()).save(any(Bid.class));
    }

    @Test
    void testDeleteBid_shouldDeleteBid() throws BidNotFoundException {
        Bid bid = new Bid();
        bid.setId(1L);
        bid.setAccount("account1");
        bid.setType("type1");
        bid.setBidQuantity(10.0);

        when(bidRepository.findById(bid.getId())).thenReturn(Optional.of(bid));
        Long id = bid.getId();

        boolean result = bidService.deleteBid(id);

        assertTrue(result);
    }

    @Test
    void testDeleteBid_shouldThrowIllegalArgumentException() {
        Long id = null;

        assertThrows(IllegalArgumentException.class, () -> bidService.deleteBid(id));
    }

    @Test
    void testDeleteBid_shouldThrowBidNotFoundException() {
        Long id = Long.MAX_VALUE;

        assertThrows(BidNotFoundException.class, () -> bidService.deleteBid(id));
    }

    @Test
    void testDeleteBid_shouldThrowRuntimeException() {
        Bid bid = new Bid();
        bid.setId(1L);
        bid.setAccount("account1");
        bid.setType("type1");
        bid.setBidQuantity(10.0);

        doThrow(new RuntimeException()).when(bidRepository).delete(bid);

        assertThrows(RuntimeException.class, () -> bidService.deleteBid(bid.getId()));
    }
}