package com.poseidon.api.service;

import com.poseidon.api.model.Rating;
import com.poseidon.api.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @InjectMocks
    private RatingService ratingService;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private Logger log;

    @Test
    public void testCreateRatingWithNullRating() {
        boolean result = ratingService.createRating(null);
        assertFalse(result);
    }

    @Test
    public void testCreateRatingWithExistingId() {
        Rating rating = new Rating();
        rating.setId(1L);
        rating.setOrderNumber(123456);
        rating.setMoodysRating("A+");
        rating.setSandPRating("AAA");
        rating.setFitchRating("AA+");

        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating));

        boolean result = ratingService.createRating(new Rating(1L, "", "", "", 123));
        assertFalse(result);
    }

    @Test
    public void testCreateRatingWithNewRating() {
        Rating rating = new Rating();
        rating.setId(2L);
        rating.setOrderNumber(Integer.valueOf("123456"));
        rating.setMoodysRating("A+");
        rating.setSandPRating("AAA");
        rating.setFitchRating("AA+");
        boolean result = ratingService.createRating(rating);
        assertTrue(result);
    }


    @Test
    public void testUpdateRatingWithValidId() {
        Rating existingRating = new Rating();
        existingRating.setId(1L);
        existingRating.setOrderNumber(Integer.valueOf("12345"));

        Rating updatedRating = new Rating();
        updatedRating.setOrderNumber(Integer.valueOf("12345"));

        when(ratingRepository.findById(1L)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(updatedRating)).thenReturn(updatedRating);

        assertTrue(ratingService.updateRating(1L, updatedRating));
    }

    @Test
    public void testUpdateRatingWithNullId() {
        Rating updatedRating = new Rating();
        updatedRating.setOrderNumber(Integer.valueOf("12345"));

        assertFalse(ratingService.updateRating(null, updatedRating));
    }

    @Test
    public void testUpdateRatingWithNonexistentId() {
        Rating updatedRating = new Rating();
        updatedRating.setOrderNumber(Integer.valueOf("12345"));

        when(ratingRepository.findById(2L)).thenReturn(Optional.empty());

        assertFalse(ratingService.updateRating(2L, updatedRating));
    }

    @Test
    public void testUpdateRatingWithNullRating() {
        assertFalse(ratingService.updateRating(1L, null));
    }


    @Test
    public void testDeleteRatingSuccessfully() {
        Long ratingId = 1L;
        Rating rating = new Rating();
        rating.setId(ratingId);
        rating.setOrderNumber(Integer.valueOf("123"));
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        boolean result = ratingService.deleteRating(ratingId);

        assertTrue(result);
        verify(ratingRepository, times(1)).delete(rating);
    }

    @Test
    public void testDeleteRatingNotFound() {
        Long ratingId = 1L;
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.empty());

        boolean result = ratingService.deleteRating(ratingId);

        assertFalse(result);
        verify(ratingRepository, never()).delete(any());
        verify(log, never()).info(anyString());
    }

    @Test
    public void testDeleteRatingThrowsException() {
        Long ratingId = 1L;
        Rating rating = new Rating();
        rating.setId(ratingId);
        rating.setOrderNumber(Integer.valueOf("123"));
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));
        doThrow(DataIntegrityViolationException.class).when(ratingRepository).delete(rating);

        assertThrows(DataIntegrityViolationException.class, () -> ratingService.deleteRating(ratingId));
    }

    @Test
    public void testDeleteRatingNullId() {
        assertFalse(ratingService.deleteRating(null));
        verify(ratingRepository, times(1)).findById(any());
        verify(ratingRepository, never()).delete(any());
    }
}