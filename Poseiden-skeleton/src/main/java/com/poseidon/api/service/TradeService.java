package com.poseidon.api.service;

import com.poseidon.api.custom.exceptions.rating.TradeAlreadyExistsException;
import com.poseidon.api.custom.exceptions.rating.TradeNotFoundException;
import com.poseidon.api.custom.exceptions.trade.InvalidTradeException;
import com.poseidon.api.model.Trade;
import com.poseidon.api.repositories.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TradeService {

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Crée un nouvel objet Trade et l'ajoute à la base de données.
     *
     * @param trade l'objet Trade à ajouter
     * @return true si l'ajout s'est déroulé avec succès
     * @throws IllegalArgumentException si le paramètre trade est nul ou si l'un de ses champs requis est manquant
     * @throws InvalidTradeException si l'un des champs de trade est invalide
     * @throws TradeAlreadyExistsException si un Trade avec le même ID existe déjà dans la base de données
     * @throws RuntimeException si une erreur inattendue se produit lors de l'ajout de l'objet Trade à la base de données
     */
    public boolean createTrade(Trade trade) {
        if (trade == null) {
            throw new IllegalArgumentException("Le Trade ne peut pas être nul");
        }

        if (trade.getAccount() == null || trade.getType() == null || trade.getBuyQuantity() == null || trade.getAction() == null) {
            throw new InvalidTradeException("Le Trade doit avoir tous les champs requis");
        }

        if (trade.getBuyQuantity() < 0) {
            throw new InvalidTradeException("La quantité d'achat doit être positive");
        }

        if (!tradeRepository.findById(trade.getId()).isPresent()) {
            tradeRepository.save(trade);
            log.info("[TradeConfiguration] Création d'un nouveau Trade avec ID : " + trade.getId() + ", compte : " + trade.getAccount() + ", type : " + trade.getType() + ", quantité d'achat : " + trade.getBuyQuantity() + ", action : " + trade.getAction());
            return true;
        }
        throw new TradeAlreadyExistsException("Un Trade avec le même ID existe déjà");
    }

    /**
     * Modifie un objet Trade existant dans la base de données.
     *
     * @param trade l'objet Trade à modifier
     * @return l'objet Trade modifié
     * @throws TradeNotFoundException si l'objet Trade n'est pas trouvé dans la base de données
     * @throws IllegalArgumentException si l'objet Trade est null
     */
    public Trade updateTrade(Trade trade) {
        if (trade == null) {
            throw new IllegalArgumentException("Le Trade ne peut pas être null");
        }

        Optional<Trade> tradeOptional = tradeRepository.findById(trade.getId());
        if (!tradeOptional.isPresent()) {
            throw new TradeNotFoundException("Impossible de trouver le Trade avec l'ID: " + trade.getId());
        }

        Trade existingTrade = tradeOptional.get();
        existingTrade.setAccount(trade.getAccount());
        existingTrade.setType(trade.getType());
        existingTrade.setBuyQuantity(trade.getBuyQuantity());
        existingTrade.setAction(trade.getAction());

        try {
            Trade updatedTrade = tradeRepository.save(existingTrade);
            log.info("[TradeConfiguration] Trade mis à jour avec l'ID : {}", updatedTrade.getId());
            return updatedTrade;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du Trade avec l'ID : " + existingTrade.getId(), e);
        }
    }

    /**
     * Supprime un objet Trade de la base de données.
     *
     * @param id l'identifiant de l'objet Trade à supprimer
     * @throws IllegalArgumentException si l'ID est null
     * @throws TradeNotFoundException si l'objet Trade avec cet ID n'existe pas dans la base de données
     */
    public void deleteTrade(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
        if (!tradeRepository.existsById(id)) {
            throw new TradeNotFoundException("Impossible de trouver l'objet Trade avec l'ID : " + id);
        }
        tradeRepository.deleteById(id);
        log.info("[TradeConfiguration] Trade avec ID '{}' supprimé avec succès", id);
    }

}
