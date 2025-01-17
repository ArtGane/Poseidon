package com.poseidon.api.service;

import com.poseidon.api.custom.exceptions.curve.CurvePointAlreadyExistsException;
import com.poseidon.api.custom.exceptions.curve.CurvePointNotFoundException;
import com.poseidon.api.custom.exceptions.curve.InvalidCurvePointException;
import com.poseidon.api.model.CurvePoint;
import com.poseidon.api.repository.CurvePointRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CurvePointService {

    @Autowired
    CurvePointRepository curvePointRepository;

    /**
     * Crée un nouveau CurvePoint.
     *
     * @param curvePoint le point de courbe à créer
     * @return true si le point de courbe a été créé avec succès
     * @throws IllegalArgumentException         si le point de courbe est nul
     * @throws InvalidCurvePointException       si le point de courbe n'a pas tous les champs requis ou si les valeurs de term et de valeur sont négatives
     * @throws CurvePointAlreadyExistsException si un point de courbe avec le même ID existe déjà
     */
    public boolean createCurve(CurvePoint curvePoint) {
        if (curvePoint == null) {
            throw new IllegalArgumentException("Le CurvePoint ne peut pas être nul");
        }

        if (curvePoint.getCurveId() == null || curvePoint.getTerm() == null || curvePoint.getValue() == null) {
            throw new InvalidCurvePointException("Le CurvePoint doit avoir tous les champs requis");
        }

        if (curvePoint.getTerm() < 0 || curvePoint.getValue() < 0) {
            throw new InvalidCurvePointException("Les valeurs de term et de valeur doivent être positives");
        }

        if (!curvePointRepository.findAll().contains(curvePoint)) {
            curvePointRepository.save(curvePoint);
            log.info("[CurveConfiguration] Création d'un nouveau CurvePoint avec ID : " + curvePoint.getCurveId() + ", terme : " + curvePoint.getTerm() + " et valeur : " + curvePoint.getValue());
            return true;
        }
        throw new CurvePointAlreadyExistsException("Un CurvePoint avec le même ID existe déjà");
    }

    /**
     * Met à jour le CurvePoint avec l'identifiant donné en utilisant les données du point de courbe mis à jour.
     *
     * @param id                      L'identifiant du point de courbe à mettre à jour.
     * @param curvePointEntityUpdated L'entité CurvePoint mise à jour à utiliser pour mettre à jour le point de courbe existant.
     * @return true si le point de courbe a été mis à jour avec succès, false sinon.
     * @throws IllegalArgumentException    Si l'ID ou le CurvePoint mis à jour est null.
     * @throws InvalidCurvePointException  Si le point de courbe mis à jour ne possède pas tous les champs requis.
     * @throws CurvePointNotFoundException Si le point de courbe avec l'ID donné n'existe pas.
     */
    public boolean updateCurve(Long id, CurvePoint curvePointEntityUpdated) {
        if (id == null || curvePointEntityUpdated == null) {
            throw new IllegalArgumentException("L'ID et le CurvePoint ne peuvent pas être null");
        }
        if (curvePointEntityUpdated.getCurveId() == null || curvePointEntityUpdated.getTerm() == null || curvePointEntityUpdated.getValue() == null) {
            throw new InvalidCurvePointException("Le CurvePoint doit avoir tous les champs requis");
        }
        Optional<CurvePoint> curvePointOptional = curvePointRepository.findById(id);
        if (!curvePointOptional.isEmpty()) {
            CurvePoint curvePoint = curvePointOptional.get();
            curvePoint.setCurveId(curvePointEntityUpdated.getCurveId());
            curvePoint.setTerm(curvePointEntityUpdated.getTerm());
            curvePoint.setValue(curvePointEntityUpdated.getValue());
            curvePointRepository.save(curvePoint);
            log.info("[CurveConfiguration] CurvePoint mis à jour " + curvePointEntityUpdated.getCurveId() + " avec la maturité " + curvePointEntityUpdated.getTerm() + " et la valeur " + curvePointEntityUpdated.getValue());

            return true;
        } else {
            throw new CurvePointNotFoundException("Le CurvePoint avec l'ID " + id + " n'a pas été trouvé");
        }
    }

    /**
     * Supprime un objet CurvePoint de la base de données en utilisant l'ID spécifié.
     *
     * @param id l'ID de l'objet CurvePoint à supprimer.
     * @return true si l'objet CurvePoint a été supprimé avec succès, false sinon.
     * @throws IllegalArgumentException    si l'ID donné est null ou invalide.
     * @throws CurvePointNotFoundException si l'objet CurvePoint correspondant à l'ID donné ne peut pas être trouvé dans la base de données.
     * @throws RuntimeException            si une erreur se produit lors de la suppression de l'objet CurvePoint.
     */
    public boolean deleteCurve(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalide: " + id);
        }

        Optional<CurvePoint> curvePoint = curvePointRepository.findById(id);
        if (curvePoint.isPresent() && curvePoint.get().getId().equals(id)) {
            try {
                CurvePoint curvePoint1 = curvePoint.get();
                curvePointRepository.delete(curvePoint1);
                log.info("[CurveConfiguration] CurvePoint avec l'ID: " + id + " bien supprimé");
                return true;
            } catch (Exception ex) {
                log.error("[Configuration de la courbe] Impossible de supprimer le CurvePoint avec l'ID : " + id, ex);
                throw new RuntimeException("Impossible de trouver le CurvePoint avec l'ID: " + id, ex);
            }
        }

        throw new CurvePointNotFoundException("Impossible de supprimer le CurvePoint avec l'ID: " + id);
    }
}
