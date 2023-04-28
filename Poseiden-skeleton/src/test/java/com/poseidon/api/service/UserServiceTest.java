package com.poseidon.api.service;

import com.poseidon.api.model.Role;
import com.poseidon.api.model.User;
import com.poseidon.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    public void setUpForUpdateTests() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setPassword("TestPassword123");
    }

    void setUpForDeleteTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_Success() {
        User user = new User();
        user.setUsername("john.doe");
        user.setPassword("Password1");
        user.setRole(String.valueOf(Role.USER));
        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        boolean result = userService.createUser(optionalUser);

        assertTrue(result);
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_NullUser_ThrowsNullPointerException() {
        Optional<User> optionalUser = Optional.empty();

        assertThrows(UsernameNotFoundException.class, () -> userService.createUser(optionalUser));

        verify(userRepository, never()).findByUsername(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ExistingUsername_ThrowsUsernameNotFoundException() {
        User user = new User();
        user.setUsername("john.doe");
        user.setPassword("Password1");
        user.setRole(String.valueOf(Role.USER));
        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        assertThrows(UsernameNotFoundException.class, () -> userService.createUser(optionalUser));

        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_WeakPassword_ThrowsIllegalArgumentException() {
        User user = new User();
        user.setUsername("john.doe");
        user.setPassword("password"); // Mot de passe faible ne respectant pas les contraintes de validation
        user.setRole(String.valueOf(Role.USER));
        Optional<User> optionalUser = Optional.of(user);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(optionalUser));

        verify(userRepository, never()).save(any(User.class));
    }

//    @Test
//    public void testUpdateUser_Success() {
//        setUpForUpdateTests();
//        Long userId = 1L;
//        String newPassword = "NewTestPassword456";
//        User updatedUser = new User();
//        updatedUser.setUsername("updatedUser");
//        updatedUser.setPassword(newPassword);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
//        when(userRepository.findByUsername(updatedUser.getUsername())).thenReturn(Optional.empty());
//        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
//
//        assertTrue(userService.updateUser(userId, updatedUser));
//        assertEquals(userId, updatedUser.getId());
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository, times(1)).findByUsername(updatedUser.getUsername());
//        verify(userRepository, times(1)).save(updatedUser);
//    }

    @Test
    public void testUpdateUserFailUserIdNull() {
        setUpForUpdateTests();
        Long userId = null;
        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword("TestPassword123");

        assertThrows(NullPointerException.class, () -> userService.updateUser(userId, Optional.of(updatedUser)));
        verifyNoInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }

//    @Test
//    public void testUpdateUser_FailUserEntityUpdatedNull() {
//        setUpForUpdateTests();
//        Long userId = 1L;
//        User updatedUser = null;
//
//        assertThrows(NullPointerException.class, () -> userService.updateUser(userId, Optional.ofNullable(updatedUser)));
//        verifyNoInteractions(userRepository);
//        verifyNoInteractions(passwordEncoder);
//    }

    @Test
    public void testUpdateUser_FailUsernameNull() {
        setUpForUpdateTests();
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setUsername(null);
        updatedUser.setPassword("TestPassword123");

        assertThrows(NullPointerException.class, () -> userService.updateUser(userId, Optional.of(updatedUser)));
        verifyNoInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void testUpdateUser_FailPasswordNull() {
        setUpForUpdateTests();
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword(null);

        assertThrows(NullPointerException.class, () -> userService.updateUser(userId, Optional.of(updatedUser)));
        verifyNoInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void testUpdateUser_FailPasswordConstraints() {
        setUpForUpdateTests();
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword("weakpassword");

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userId, Optional.of(updatedUser)));
        verifyNoInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void testDeleteUser_existingUser() {
        setUpForDeleteTests();
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        boolean result = userService.deleteUser(userId);

        assertTrue(result);
        verify(userRepository).delete(userCaptor.capture());
        assertEquals(userId, userCaptor.getValue().getId());
        assertEquals("testUser", userCaptor.getValue().getUsername());
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testDeleteUser_nonExistingUser() {
        setUpForDeleteTests();
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testDeleteUser_nullId() {
        setUpForDeleteTests();
        assertThrows(NullPointerException.class, () -> userService.deleteUser(null));
        verifyNoInteractions(userRepository);
    }

    @Test
    void testPasswordConstraints_validPassword() {
        String password = "MyPassword1";
        boolean result = userService.passwordConstraints(password);
        assertTrue(result);
    }

    @Test
    void testPasswordConstraints_shortPassword() {
        String password = "Pwd1";
        boolean result = userService.passwordConstraints(password);
        assertFalse(result);
    }

    @Test
    void testPasswordConstraints_noUppercaseLetter() {
        String password = "mypassword1";
        boolean result = userService.passwordConstraints(password);
        assertFalse(result);
    }

    @Test
    void testPasswordConstraints_noDigit() {
        String password = "MyPassword";
        boolean result = userService.passwordConstraints(password);
        assertFalse(result);
    }


    @Test
    void testIsCurrentUserAdmin_adminUser() {
        String username = "admin";
        User user = new User();
        user.setUsername(username);
        user.setRole(Role.ADMIN.name());
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "password");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.isCurrentUserAdmin(authentication);

        assertTrue(result);
    }

    @Test
    void testIsCurrentUserAdmin_nonAdminUser() {
        String username = "user";
        User user = new User();
        user.setUsername(username);
        user.setRole(Role.USER.name());
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "password");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.isCurrentUserAdmin(authentication);

        assertFalse(result);
    }

    @Test
    void testIsCurrentUserAdmin_userNotFound() {
        String username = "unknown";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "password");

        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.isCurrentUserAdmin(authentication);
        });
    }
}