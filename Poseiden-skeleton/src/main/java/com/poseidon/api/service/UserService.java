package com.poseidon.api.service;

import com.poseidon.api.custom.constantes.UserConstantes;
import com.poseidon.api.model.Role;
import com.poseidon.api.model.User;
import com.poseidon.api.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    SCryptPasswordEncoder passwordEncoder = new SCryptPasswordEncoder();


    /**
     * Charge un utilisateur à partir de son nom d'utilisateur et retourne une instance UserDetails représentant cet utilisateur.
     *
     * @param username le nom d'utilisateur à rechercher
     * @return une instance UserDetails représentant l'utilisateur trouvé
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                return new User(user.get());
            }
            throw new UsernameNotFoundException("User not found : " + username);
        } catch (NullPointerException ex) {
            throw new UsernameNotFoundException("User not found : " + username);
        }
    }

    /**
     * Récupère une liste de tous les utilisateurs enregistrés.
     *
     * @return la liste de tous les utilisateurs enregistrés
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param userId l'identifiant de l'utilisateur à rechercher
     * @return l'utilisateur trouvé
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé
     */
    public User findUserById(Long userId) {
        Optional<User> user;
        if (userId == null) {
            throw new UsernameNotFoundException("Could not find user with id : " + userId);
        } else {
            user = userRepository.findById(userId);
        }
        return user.get();
    }

    /**
     * Cette méthode permet de créer un utilisateur à partir d'un objet Optional<User>.
     *
     * @param userEntity un objet Optional contenant les informations de l'utilisateur à créer.
     * @return true si l'utilisateur a été créé avec succès, false sinon.
     * @throws IllegalArgumentException  si le mot de passe ne respecte pas les contraintes définies.
     * @throws UsernameNotFoundException si le nom d'utilisateur est déjà pris.
     * @throws NullPointerException      si l'utilisateur ou certaines de ses propriétés sont nuls.
     */
    public boolean createUser(Optional<User> userEntity) {

        Objects.requireNonNull(userEntity, "L'utilisateur ne peut pas être null");
        User user = userEntity.orElseThrow(() -> new UsernameNotFoundException("L'utilisateur ne peut pas être null"));

        String username = user.getUsername();
        Objects.requireNonNull(username, "Le nom d'utilisateur ne peut pas être null");

        String password = user.getPassword();
        Objects.requireNonNull(password, "Le mot de passe ne peut pas être null");

        if (!passwordConstraints(password)) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins une lettre majuscule, un chiffre et 8 caractères");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameNotFoundException(UserConstantes.USERNAME_ALREADY_TAKEN_EXCEPTION_MESSAGE);
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        log.info(String.format(UserConstantes.USER_CREATED_LOG_MESSAGE, username, user.getRole()));

        return true;
    }

    /**
     * Met à jour un utilisateur dans la base de données.
     *
     * @param userId            l'identifiant de l'utilisateur à mettre à jour
     * @param userEntityUpdated l'objet User mis à jour
     * @return true si la mise à jour a été effectuée avec succès, false sinon
     * @throws IllegalArgumentException  si le mot de passe ne respecte pas les contraintes de sécurité
     * @throws UsernameNotFoundException si l'utilisateur à mettre à jour n'a pas été trouvé dans la base de données ou si le nom d'utilisateur est déjà pris par un autre utilisateur
     * @throws NullPointerException      si l'identifiant de l'utilisateur ou l'objet User mis à jour est null
     */
    public boolean updateUser(Long userId, User userEntityUpdated) {

        Objects.requireNonNull(userId, "L'identifiant de l'utilisateur ne peut pas être null");
        Objects.requireNonNull(userEntityUpdated, "L'utilisateur mis à jour ne peut pas être null");

        String username = userEntityUpdated.getUsername();
        Objects.requireNonNull(username, "Le nom d'utilisateur ne peut pas être null");

        String password = userEntityUpdated.getPassword();
        Objects.requireNonNull(password, "Le mot de passe ne peut pas être null");

        if (!passwordConstraints(password)) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins une lettre majuscule, un chiffre et 8 caractères");
        }

        Optional<User> existingUser = userRepository.findById(userId);
        if (!existingUser.isPresent()) {
            throw new UsernameNotFoundException(String.format(UserConstantes.USER_NOT_FOUND_EXCEPTION_MESSAGE, userId));
        }

        User user = existingUser.get();

        if (!user.getUsername().equals(username) && userRepository.findByUsername(username).isPresent()) {
            throw new UsernameNotFoundException(UserConstantes.USERNAME_ALREADY_TAKEN_EXCEPTION_MESSAGE);
        }

        userEntityUpdated.setId(userId);
        userEntityUpdated.setPassword(passwordEncoder.encode(password));
        userRepository.save(userEntityUpdated);
        log.info(String.format(UserConstantes.USER_UPDATED_LOG_MESSAGE, username));

        return true;
    }

    /**
     * Cette méthode supprime l'utilisateur avec l'identifiant spécifié de la base de données.
     *
     * @param userId l'ID de l'utilisateur à supprimer
     * @return true si l'utilisateur est supprimé avec succès, false sinon
     * @throws UsernameNotFoundException si l'utilisateur avec l'ID donné n'est pas trouvé dans la base de données
     * @throws NullPointerException      si l'ID de l'utilisateur est null
     */
    public boolean deleteUser(Long userId) {

        Objects.requireNonNull(userId, "L'identifiant de l'utilisateur ne peut pas être null");

        Optional<User> existingUser = userRepository.findById(userId);
        if (!existingUser.isPresent()) {
            throw new UsernameNotFoundException(String.format(UserConstantes.USER_NOT_FOUND_EXCEPTION_MESSAGE, userId));
        }

        User user = existingUser.get();
        userRepository.delete(user);
        log.info(String.format(UserConstantes.USER_DELETED_LOG_MESSAGE, user.getUsername()));

        return true;
    }

    /**
     * Vérifie que le mot de passe respecte les contraintes suivantes:
     * au moins une lettre majuscule
     * au moins un chiffre
     * une longueur minimale de 8 caractères
     *
     * @param password le mot de passe à vérifier
     * @return true si le mot de passe respecte les contraintes, false sinon
     */
    public boolean passwordConstraints(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        if (!password.matches(passwordPattern)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Vérifie si l'utilisateur actuel est un administrateur en fonction de son nom d'utilisateur et de son rôle.
     *
     * @param authentication l'objet Authentication représentant l'utilisateur authentifié
     * @return true si l'utilisateur actuel est un administrateur, false sinon
     */
    public boolean isCurrentUserAdmin(Authentication authentication) {
        String username = authentication.getName();
        User currentUser = (User) loadUserByUsername(username);
        return currentUser != null && Role.ADMIN.equals(Role.valueOf(currentUser.getRole()));
    }
}