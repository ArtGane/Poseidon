package com.poseidon.api.service;

import com.poseidon.api.custom.exceptions.bid.BidAlreadyExistsException;
import com.poseidon.api.custom.exceptions.bid.BidNotFoundException;
import com.poseidon.api.custom.exceptions.bid.InvalidBidException;
import com.poseidon.api.model.Bid;
import com.poseidon.api.repository.BidRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BidService {

    @Autowired
    BidRepository bidRepository;

    /**
     * Crée un nouvel objet Bid en base de données.
     *
     * @param bid l'objet Bid à créer
     * @return true si la création a réussi, false sinon
     * @throws IllegalArgumentException si l'objet Bid est null ou s'il existe déjà une entrée avec le même id
     */
    public void createBid(Bid bid) throws InvalidBidException, BidAlreadyExistsException {
        if (bid == null) {
            throw new IllegalArgumentException("L'objet Bid ne peut pas être nul");
        }
        if (bidRepository.findById(bid.getId()).isPresent()) {
            throw new BidAlreadyExistsException("Un objet Bid avec le même ID existe déjà");
        }
        if (bid.getAccount() == null || bid.getType() == null || bid.getBidQuantity() == null) {
            throw new InvalidBidException("L'objet Bid doit avoir tous les champs requis");
        }
        if (bid.getBidQuantity() < 0) {
            throw new InvalidBidException("La valeur de bidQuantity doit être positive");
        }
        bidRepository.save(bid);
        log.info("[BidConfiguration] Nouveau Bid créé pour le compte : " + bid.getAccount() + " quantité : " + bid.getBidQuantity());
    }

    /**
     * Met à jour une soumission d'offre existante avec les nouvelles données fournies.
     *
     * @param id               l'identifiant de la Bid à mettre à jour
     * @param bidEntityUpdated les nouvelles données à utiliser pour la mise à jour de la Bid
     * @throws InvalidBidException  si la Bid mise à jour n'est pas valide
     * @throws BidNotFoundException si la Bid avec l'identifiant donné n'existe pas
     */
    public boolean updateBid(Long id, Bid bidEntityUpdated) throws InvalidBidException, BidNotFoundException, ChangeSetPersister.NotFoundException {
        if (bidEntityUpdated == null || bidEntityUpdated.getBidQuantity() == null || bidEntityUpdated.getBidQuantity() <= 0
                || bidEntityUpdated.getAccount() == null || bidEntityUpdated.getAccount().isEmpty()) {
            throw new InvalidBidException("L'offre mise à jour est invalide");
        }

        Bid existingBid = bidRepository.findById(id).orElseThrow(() -> new BidNotFoundException("Impossible de trouver l'offre avec l'identifiant : " + id));

        existingBid.setAccount(bidEntityUpdated.getAccount());
        existingBid.setBidQuantity(bidEntityUpdated.getBidQuantity());

        bidRepository.save(existingBid);
        log.info("[BidConfiguration] Offre pour le compte " + existingBid.getAccount() + " avec l'identifiant " + id + " mise à jour");
        return true;
    }

    /**
     * Supprime une Bid avec l'ID donné de la base de données.
     *
     * @param id du Bid à supprimer
     * @return true si la Bid est supprimée avec succès, false sinon
     * @throws BidNotFoundException si aucune Bid n'est trouvée avec l'ID donné
     */
    public boolean deleteBid(Long id) throws BidNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("L'ID de la bid ne peut pas être nul");
        }

        Optional<Bid> bidOptional = bidRepository.findById(id);
        if (!bidOptional.isPresent()) {
            throw new BidNotFoundException("Aucune bid trouvée avec l'ID : " + id);
        }

        try {
            bidRepository.delete(bidOptional.get());
            log.info("[BidConfiguration] Bid supprimée avec succès avec l'ID " + id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la bid avec l'ID : " + id, e);
        }
    }
}
