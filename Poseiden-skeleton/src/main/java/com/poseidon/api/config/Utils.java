package com.poseidon.api.config;

import com.poseidon.api.repositories.customconfig.Identifiable;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class Utils {

    /**
     * Vérifie si un objet est présent dans le référentiel correspondant en utilisant son ID.
     *
     * @param object     l'objet Identifiable présent dans le package model.
     * @param repository le référentiel JpaRepository présent dans le package repositories.
     * @param <T>        le type générique de l'objet Identifiable.
     * @return true si l'objet est présent dans le référentiel, false sinon.
     */
    public static <T extends Identifiable> boolean isPresent(T object, JpaRepository<T, Long> repository) {
        Optional<T> optionalObject = repository.findById((Long) object.getId());
        return optionalObject.isPresent();
    }

    /**
     * Récupère tous les objets d'un référentiel donné.
     *
     * @param repository le référentiel JpaRepository présent dans le package repositories.
     * @param <T>        le type générique de l'objet Identifiable.
     * @return une liste contenant tous les objets du repository.
     */
    public static <T extends Identifiable> List<T> findAll(JpaRepository<T, Long> repository) {
        return repository.findAll();
    }

    /**
     * Récupère un objet à partir de son ID dans un référentiel donné.
     *
     * @param id         l'ID de l'objet à récupérer.
     * @param repository le référentiel JpaRepository présent dans le package repositories.
     * @param <T>        le type générique de l'objet Identifiable.
     * @return l'objet correspondant à l'ID spécifié.
     * @throws ChangeSetPersister.NotFoundException si aucun objet correspondant à l'ID spécifié n'a été trouvé dans le référentiel.
     */
    public static <T extends Identifiable> T findById(Long id, JpaRepository<T, Long> repository) throws ChangeSetPersister.NotFoundException {
        Optional<T> object = repository.findById(id);
        return object.orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }
}
