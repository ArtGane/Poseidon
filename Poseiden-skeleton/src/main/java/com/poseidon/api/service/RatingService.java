package com.poseidon.api.service;

import com.poseidon.api.customexceptions.RatingServiceException;
import com.poseidon.api.model.Rating;
import com.poseidon.api.model.dto.RatingDto;
import com.poseidon.api.repositories.RatingRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RatingService {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<Rating> findAllRatings() {
        return ratingRepository.findAll();
    }

    public Rating findRatingById(Integer id) throws RatingServiceException {
        Optional<Rating> rating = ratingRepository.findRatingById(id);
        if (id != null && rating.isPresent()) {
            return rating.get();
        }
        throw new RatingServiceException("Could not find rating with id : " + id);
    }

    public boolean createRating(Rating ratingEntity) throws RatingServiceException {
        if (ratingEntity != null && !ratingRepository.findRatingById(ratingEntity.getId()).isPresent()) {
            ratingRepository.save(ratingEntity);
            log.info("[RATING] Created a new rating with id " + ratingEntity.getId() + " for order number " + ratingEntity.getOrderNumber());
            return true;
        }
        throw new RatingServiceException("There was an error while creating the rating");
    }

    public boolean updateRating(Integer id, Rating ratingEntityUpdated) throws RatingServiceException {
        Optional<Rating> rating = ratingRepository.findRatingById(id);
        if (id != null && rating.isPresent()) {
            ratingEntityUpdated.setId(id);
            ratingRepository.save(ratingEntityUpdated);

            log.info("[RATING] Updated rating's id " + ratingEntityUpdated.getId() + " for order number " + ratingEntityUpdated.getOrderNumber());
            return true;
        }
        throw new RatingServiceException("Could not find rating with id : " + id);
    }

    public boolean deleteRating(Integer id) throws RatingServiceException {
        Optional<Rating> rating = ratingRepository.findRatingById(id);
        if (id != null && rating.isPresent()) {
            ratingRepository.delete(rating.get());
            log.info("[RATING] Deleted rating's id " + id + " for order number " + rating.get().getOrderNumber());
            return true;
        }
        throw new RatingServiceException("Could not find rating with id : " + id);
    }

    public Rating convertDtoToEntity(RatingDto ratingDto) {
        return modelMapper.map(ratingDto, Rating.class);
    }

    public RatingDto convertEntityToDto(Rating ratingEntity) {
        return modelMapper.map(ratingEntity, RatingDto.class);
    }
}
