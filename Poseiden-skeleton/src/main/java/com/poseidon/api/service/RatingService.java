package com.poseidon.api.service;

import com.poseidon.api.model.Rating;
import com.poseidon.api.repositories.RatingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class RatingService {

    @Autowired
    RatingRepository ratingRepository;

    /**
     * Crée un objet Rating dans la base de données.
     * @param rating L'objet Rating à créer.
     * @return true si la création a réussi, false sinon.
     * @throws DataAccessException si une erreur survient lors de l'accès aux données.
     */
    public boolean createRating(Rating rating) {
        try {
            if (rating != null && !ratingRepository.findById(rating.getId()).isPresent()) {
                ratingRepository.save(rating);
                log.info("[RatingConfiguration] Création d'un nouveau rating avec l'ID " + rating.getId() + " pour le numéro de commande " + rating.getOrderNumber());
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            log.error("Erreur lors de la sauvegarde de l'objet Rating : " + e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour un objet Rating dans la base de données.
     * @param id L'identifiant de l'objet Rating à mettre à jour.
     * @param ratingUpdated L'objet Rating avec les nouvelles données.
     * @return true si la mise à jour a réussi, false sinon.
     * @throws DataAccessException si une erreur survient lors de l'accès aux données.
     */
    public boolean updateRating(Long id, Rating ratingUpdated) throws DataAccessException {
        try {
            Optional<Rating> rating = ratingRepository.findById(id);
            if (id != null && rating.isPresent()) {
                ratingUpdated.setId(id);
                ratingRepository.save(ratingUpdated);

                log.info("[RatingConfiguration] Mise à jour de l'objet Rating avec l'ID {} pour le numéro de commande {}", ratingUpdated.getId(), ratingUpdated.getOrderNumber());

                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Erreur lors de la mise à jour de l'objet Rating : " + e.getMessage());
        }
    }

    /**
     * Supprime un objet Rating de la base de données.
     *
     * @param id l'identifiant de l'objet Rating à supprimer
     * @return true si la suppression a été effectuée avec succès, false sinon
     * @throws DataAccessException si une exception est levée lors de l'accès aux données
     *         ou si l'objet Rating à supprimer n'est pas trouvé dans la base de données
     */
    public boolean deleteRating(Long id) throws DataAccessException {
        Optional<Rating> rating = ratingRepository.findById(id);
        if (id == null || rating.isEmpty()) {
            return false;
        }

        try {
            ratingRepository.delete(rating.get());
            log.info("[RatingConfiguration] Suppression de l'objet Rating d'ID " + id + " pour le numéro de commande " + rating.get().getOrderNumber());
            return true;
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Erreur lors de la suppression de l'objet Rating d'ID " + id + " : " + e.getMessage());
        }
    }

}
