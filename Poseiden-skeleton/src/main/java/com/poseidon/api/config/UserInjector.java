package com.poseidon.api.config;

import com.poseidon.api.custom.constantes.UserConstantes;
import com.poseidon.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;

@Slf4j
public class UserInjector {

    @Autowired
    UserService userService;

    PasswordEncryptor encryptor = new StrongPasswordEncryptor();
    /**
     * Établit une connexion à la base de données, vérifie si les utilisateurs ADMIN et USER existent déjà dans la base de données,
     * et les ajoute à la base de données si nécessaire.
     */
    public void init() {
        try {
            Environment env = ApplicationContextProvider.getApplicationContext().getEnvironment();
            Connection connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"), env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

            if (connection != null) {
                log.info("Connexion à la base de données établie");

                boolean adminExist = checkUserExists(connection, env.getProperty("poseidon.admin.username"));
                boolean userExist = checkUserExists(connection, env.getProperty("poseidon.user.username"));

                if (adminExist && userExist) {
                    log.info("Les utilisateurs ADMIN et USER sont déjà présents dans la base de données.");
                } else {
                    if (!adminExist) {
                        insertUser(connection, env.getProperty("poseidon.admin.username"), env.getProperty("poseidon.admin.password"), env.getProperty("poseidon.admin.fullname"), "ADMIN");
                        log.info("L'utilisateur ADMIN a été créé dans la base de données.");
                    }

                    if (!userExist) {
                        insertUser(connection, env.getProperty("poseidon.user.username"), env.getProperty("poseidon.user.password"), env.getProperty("poseidon.user.fullname"), "USER");
                        log.info("L'utilisateur USER a été créé dans la base de données.");
                    }
                }
                connection.close();
            }
        } catch (SQLException e) {
            log.error("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }

    /**
     * Vérifie si un utilisateur avec le nom d'utilisateur spécifié existe déjà dans la base de données.
     *
     * @param connection la connexion à la base de données
     * @param username le nom d'utilisateur à vérifier
     * @return true si l'utilisateur existe, false sinon
     * @throws SQLException si une erreur se produit lors de l'exécution de la requête SQL
     */
    private static boolean checkUserExists(Connection connection, String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username=?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    /**
     * Ajoute un utilisateur à la base de données avec le nom d'utilisateur, le mot de passe et le rôle spécifiés.
     *
     * @param connection la connexion à la base de données
     * @param username le nom d'utilisateur à ajouter
     * @param password le mot de passe à utiliser pour l'utilisateur ajouté chiffré pour la base de données par la méthode SCrypt
     * @param role le rôle de l'utilisateur ajouté
     * @throws SQLException si une erreur se produit lors de l'exécution de la requête SQL
     */

    public void insertUser(Connection connection, String username, String password, String fullname, String role) throws SQLException {
        String query = "INSERT INTO users (username, password, fullname, role) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, encryptor.encryptPassword(password));
        statement.setString(3, fullname);
        statement.setString(4, role);
        statement.executeUpdate();
        log.info(String.format(UserConstantes.USER_CREATED_LOG_MESSAGE, username, role));
    }
}
