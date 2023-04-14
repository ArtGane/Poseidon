package com.poseidon.api.service;

import com.poseidon.api.custom.exceptions.curve.CurvePointAlreadyExistsException;
import com.poseidon.api.custom.exceptions.curve.CurvePointNotFoundException;
import com.poseidon.api.custom.exceptions.curve.InvalidCurvePointException;
import com.poseidon.api.model.CurvePoint;
import com.poseidon.api.repositories.CurvePointRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CurvePointServiceTest {

    @InjectMocks
    private CurvePointService curvePointService;

    @Mock
    private CurvePointRepository curvePointRepository;

    @Test
    public void testCreateCurvePoint() {
        CurvePoint curvePoint = new CurvePoint(2L, 1L, 2.0, 3.0, LocalDateTime.now(), LocalDateTime.now());

        assertTrue(curvePointService.createCurve(curvePoint));
        verify(curvePointRepository, times(1)).findById(anyLong());
        verify(curvePointRepository, times(1)).save(any(CurvePoint.class));
    }

    @Test
    public void testCreateCurvePointWithExistingId() {
        CurvePoint curvePoint = new CurvePoint(1L, 1L, 2.0, 3.0, LocalDateTime.now(), LocalDateTime.now());

        when(curvePointRepository.findById(anyLong())).thenReturn(Optional.of(curvePoint));

        assertThrows(CurvePointAlreadyExistsException.class, () -> curvePointService.createCurve(curvePoint));
        verify(curvePointRepository, times(1)).findById(anyLong());
        verify(curvePointRepository, never()).save(any(CurvePoint.class));
    }

    @Test
    public void testCreateCurvePointWithNullCurvePoint() {
        CurvePoint curvePoint = null;

        assertThrows(IllegalArgumentException.class, () -> curvePointService.createCurve(curvePoint));
        verify(curvePointRepository, never()).findById(anyLong());
        verify(curvePointRepository, never()).save(any(CurvePoint.class));
    }

    @Test
    public void testCreateCurvePointWithNullFields() {
        CurvePoint curvePoint = new CurvePoint(1L, null, 2.0, 3.0, LocalDateTime.now(), LocalDateTime.now());

        assertThrows(InvalidCurvePointException.class, () -> curvePointService.createCurve(curvePoint));
        verify(curvePointRepository, never()).findById(anyLong());
        verify(curvePointRepository, never()).save(any(CurvePoint.class));
    }

    @Test
    public void testCreateCurvePointWithNegativeValues() {
        CurvePoint curvePoint = new CurvePoint(1L, 1L, -2.0, -3.0, LocalDateTime.now(), LocalDateTime.now());

        assertThrows(InvalidCurvePointException.class, () -> curvePointService.createCurve(curvePoint));
        verify(curvePointRepository, never()).findById(anyLong());
        verify(curvePointRepository, never()).save(any(CurvePoint.class));
    }

    @Test
    public void testUpdateCurveNullId() {
        assertThrows(IllegalArgumentException.class, () -> curvePointService.updateCurve(null, new CurvePoint()));
    }

    @Test
    public void testUpdateCurveNullCurvePoint() {
        assertThrows(IllegalArgumentException.class, () -> curvePointService.updateCurve(1L, null));
    }

    @Test
    public void testUpdateCurveInvalidCurvePoint() {
        CurvePoint curvePoint = new CurvePoint(1L, null, null, null, null, null);
        assertThrows(InvalidCurvePointException.class, () -> curvePointService.updateCurve(1L, curvePoint));
    }

    @Test
    public void testUpdateCurveNotFound() {
        CurvePoint curvePoint = new CurvePoint(1L, 1L, 1.0, 2.0, LocalDateTime.now(), LocalDateTime.now());
        assertThrows(CurvePointNotFoundException.class, () -> curvePointService.updateCurve(1L, curvePoint));
    }

    @Test
    public void testUpdateCurveSuccess() {
        CurvePoint curvePoint = new CurvePoint(1L, 1L, 1.0, 2.0, LocalDateTime.now(), LocalDateTime.now());
        when(curvePointRepository.findById(anyLong())).thenReturn(Optional.of(curvePoint));
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);

        CurvePoint curvePointUpdated = new CurvePoint(1L, 2L, 2.0, 3.0, LocalDateTime.now(), LocalDateTime.now());
        boolean result = curvePointService.updateCurve(1L, curvePointUpdated);
        assertTrue(result);

        verify(curvePointRepository, times(1)).findById(anyLong());
        verify(curvePointRepository, times(1)).save(any(CurvePoint.class));

        CurvePoint updatedCurvePoint = curvePointRepository.findById(1L).orElse(null);
        assertNotNull(updatedCurvePoint);
        assertEquals(curvePointUpdated.getCurveId(), updatedCurvePoint.getCurveId());
        assertEquals(curvePointUpdated.getTerm(), updatedCurvePoint.getTerm());
        assertEquals(curvePointUpdated.getValue(), updatedCurvePoint.getValue());
        assertEquals(curvePointUpdated.getAsOfDate(), updatedCurvePoint.getAsOfDate());
        assertEquals(curvePointUpdated.getCreationDate(), updatedCurvePoint.getCreationDate());
    }

    @Test
    public void testDeleteCurveNullId() {
        assertThrows(IllegalArgumentException.class, () -> curvePointService.deleteCurve(null));
    }

    @Test
    public void testDeleteCurveInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> curvePointService.deleteCurve(-1L));
    }

    @Test
    public void testDeleteCurveNotFound() {
        assertThrows(CurvePointNotFoundException.class, () -> curvePointService.deleteCurve(1L));
    }
}