package com.poseidon.api.service;

import com.poseidon.api.custom.exceptions.bid.BidNotFoundException;
import com.poseidon.api.custom.exceptions.bid.InvalidBidException;
import com.poseidon.api.model.Bid;
import com.poseidon.api.model.dto.BidDto;
import com.poseidon.api.repositories.BidRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BidService {

    @Autowired
    BidRepository bidRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<Bid> findAllBids() {
        return bidRepository.findAll();
    }

    /**
     * Recherche un objet Bid par son identifiant.
     *
     * @param id L'identifiant de l'objet Bid à rechercher
     * @return L'objet Bid correspondant à l'identifiant donné
     * @throws BidNotFoundException si aucun objet Bid correspondant n'est trouvé pour l'identifiant donné
     * @throws IllegalArgumentException si l'identifiant est null
     */
    public Bid findBidById(Long id) throws BidNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("L'identifiant ne peut pas être null");
        }
        Optional<Bid> bid = bidRepository.findBidById(id);
        if (bid.isPresent()) {
            return bid.get();
        } else {
            throw new BidNotFoundException("Impossible de trouver un objet Bid avec l'identifiant : " + id);
        }
    }

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
     * @param id L'ID de la Bid à supprimer
     * @throws BidNotFoundException si aucune Bid n'est trouvée avec l'ID donné
     * @return true si la Bid est supprimée avec succès, false sinon
     */
    public boolean deleteBid(Long id) throws BidNotFoundException {
        Optional<Bid> bid = bidRepository.findBidById(id);
        if (id != null && bid.isPresent()) {
            bidRepository.delete(bid.get());
            log.info("[BidConfiguration] Bid supprimée avec succès avec l'ID " + id);
            return true;
        }
        throw new BidNotFoundException("Aucune Bid trouvée avec l'ID : " + id);
    }
    /**
     * Convertit un objet BidDto en entité Bid à l'aide de ModelMapper.
     *
     * @param bidDto l'objet BidDto à convertir
     * @return l'entité Bid convertie
     * @throws IllegalArgumentException si bidDto est null
     */
    public Bid convertDtoToEntity(BidDto bidDto) {
        if (bidDto == null) {
            throw new IllegalArgumentException("Le BidDto ne peut pas être null");
        }
        return modelMapper.map(bidDto, Bid.class);
    }

    /**
     * Convertit une entité Bid en objet BidDto à l'aide de ModelMapper.
     *
     * @param bidEntity l'entité Bid à convertir
     * @return l'objet BidDto converti
     * @throws IllegalArgumentException si bidEntity est null
     */
    public BidDto convertEntityToDto(Bid bidEntity) {
        if (bidEntity == null) {
            throw new IllegalArgumentException("Le Bid ne peut pas être null");
        }
        return modelMapper.map(bidEntity, BidDto.class);
    }
}
