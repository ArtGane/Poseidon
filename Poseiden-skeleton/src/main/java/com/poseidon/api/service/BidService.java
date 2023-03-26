package com.poseidon.api.service;

import com.poseidon.api.config.Utils;
import com.poseidon.api.custom.exceptions.bid.BidNotFoundException;
import com.poseidon.api.custom.exceptions.bid.InvalidBidException;
import com.poseidon.api.model.Bid;
import com.poseidon.api.repositories.BidRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BidService {

    @Autowired
    BidRepository bidRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Crée un nouvel objet Bid en base de données.
     *
     * @param bid l'objet Bid à créer
     * @return true si la création a réussi, false sinon
     * @throws IllegalArgumentException si l'objet Bid est null ou s'il existe déjà une entrée avec le même id
     */
    public boolean createBid(Bid bid) throws InvalidBidException {
        if (bid == null) {
            throw new IllegalArgumentException("L'objet Bid ne peut pas être nul");
        }
        if (bidRepository.findBidById(bid.getId()).isPresent()) {
            throw new IllegalArgumentException("Un objet Bid avec le même ID existe déjà");
        }
        if (bid.getAccount() == null || bid.getType() == null || bid.getBidQuantity() == null) {
            throw new InvalidBidException("L'objet Bid doit avoir tous les champs requis");
        }
        if (bid.getBidQuantity() < 0) {
            throw new InvalidBidException("La valeura de bidQuantity doit être positive");
        }
        bidRepository.save(bid);
        log.info("[BidConfiguration] Nouveau Bid créé pour le compte : " + bid.getAccount() + " quantité : " + bid.getBidQuantity());

        return true;
    }

    /**
     * Met à jour une soumission d'offre existante avec les nouvelles données fournies.
     *
     * @param id              l'identifiant de la Bid à mettre à jour
     * @param bidEntityUpdated les nouvelles données à utiliser pour la mise à jour de la Bid
     * @throws InvalidBidException si la Bid mise à jour n'est pas valide
     * @throws BidNotFoundException si la Bid avec l'identifiant donné n'existe pas
     */
    public boolean updateBid(Long id, Bid bidEntityUpdated) throws InvalidBidException, BidNotFoundException {
        Optional<Bid> existingBid = bidRepository.findBidById(id);

        if (id == null || !existingBid.isPresent()) {
            throw new BidNotFoundException("Impossible de trouver l'offre avec l'identifiant : " + id);
        }

        Bid bidToUpdate = existingBid.get();

        if (bidEntityUpdated == null || bidEntityUpdated.getBidQuantity() == null || bidEntityUpdated.getBidQuantity() <= 0
                || bidEntityUpdated.getAccount() == null || bidEntityUpdated.getAccount().isEmpty()) {
            throw new InvalidBidException("L'offre mise à jour est invalide");
        }

        bidToUpdate.setAccount(bidEntityUpdated.getAccount());
        bidToUpdate.setBidQuantity(bidEntityUpdated.getBidQuantity());

        bidRepository.save(bidToUpdate);
        log.info("[BidConfiguration] Offre pour le compte " + bidToUpdate.getAccount() + " avec l'identifiant " + id + " mise à jour");
        return true;
    }

    /**
     * Supprime une Bid avec l'ID donné de la base de données.
     *
     * @param bid Bid à supprimer
     * @throws BidNotFoundException si aucune Bid n'est trouvée avec l'ID donné
     * @return true si la Bid est supprimée avec succès, false sinon
     */
    public boolean deleteBid(Bid bid) throws BidNotFoundException {
        if (Utils.isPresent(bid, bidRepository)) {
            bidRepository.delete(bid);
            log.info("[BidConfiguration] Bid supprimée avec succès avec l'ID " + bid.getId());
            return true;
        }
        throw new BidNotFoundException("Aucune Bid trouvée avec l'ID : " + bid.getId());
    }

}
