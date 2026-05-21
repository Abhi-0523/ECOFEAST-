package com.ecofeast.service;

import com.ecofeast.dao.UserDao;
import com.ecofeast.model.Role;
import com.ecofeast.model.User;
import com.ecofeast.util.PasswordUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserDao userDao;

    private UserService userService;

    private User existingUser;
    private Role donorRole;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userDao);

        donorRole = new Role(2, "DONOR");

        existingUser = new User();
        existingUser.setId(10);
        existingUser.setEmail("donor@ecofeast.com");
        // Hash for "Donor@123" is "2303038676231362e5585b9b1e9c4021" or similar. We can set it to a plain hash and stub checkPassword or just let it hash.
        existingUser.setPasswordHash(PasswordUtil.hashPassword("Donor@123"));
        existingUser.setRole(donorRole);
        existingUser.setRoleId(2);
    }

    @Test
    public void testTryLoginSuccess() throws SQLException {
        when(userDao.findByEmail("donor@ecofeast.com")).thenReturn(existingUser);

        UserService.LoginResult result = userService.tryLogin("donor@ecofeast.com", "Donor@123");

        assertTrue(result.isSuccess());
        assertEquals(UserService.LoginFailure.SUCCESS, result.failure);
        assertEquals(existingUser.getId(), result.user.getId());
    }

    @Test
    public void testTryLoginUnknownEmail() throws SQLException {
        when(userDao.findByEmail("unknown@ecofeast.com")).thenReturn(null);

        UserService.LoginResult result = userService.tryLogin("unknown@ecofeast.com", "Password");

        assertFalse(result.isSuccess());
        assertEquals(UserService.LoginFailure.UNKNOWN_EMAIL, result.failure);
        assertNull(result.user);
    }

    @Test
    public void testTryLoginWrongPassword() throws SQLException {
        when(userDao.findByEmail("donor@ecofeast.com")).thenReturn(existingUser);

        UserService.LoginResult result = userService.tryLogin("donor@ecofeast.com", "WrongPassword");

        assertFalse(result.isSuccess());
        assertEquals(UserService.LoginFailure.WRONG_PASSWORD, result.failure);
        assertNull(result.user);
    }

    @Test
    public void testTryLoginNullOrEmptyInputs() throws SQLException {
        // Null inputs
        assertFalse(userService.tryLogin(null, "Password").isSuccess());
        assertFalse(userService.tryLogin("donor@ecofeast.com", null).isSuccess());

        // Empty inputs
        assertFalse(userService.tryLogin("  ", "Password").isSuccess());
        assertFalse(userService.tryLogin("donor@ecofeast.com", "  ").isSuccess());
    }

    @Test
    public void testLoginAlias() throws SQLException {
        when(userDao.findByEmail("donor@ecofeast.com")).thenReturn(existingUser);

        User user = userService.login("donor@ecofeast.com", "Donor@123");
        assertNotNull(user);
        assertEquals(existingUser.getId(), user.getId());

        User badUser = userService.login("donor@ecofeast.com", "wrong");
        assertNull(badUser);
    }

    @Test
    public void testRegisterSuccess() throws SQLException {
        User newUser = new User();
        newUser.setEmail("newuser@ecofeast.com");
        newUser.setPasswordHash("plaintextPass123");
        newUser.setFullName("New User");

        when(userDao.findByEmail("newuser@ecofeast.com")).thenReturn(null);
        when(userDao.getRoleIdByName("DONOR")).thenReturn(2);
        when(userDao.createUser(any(User.class))).thenReturn(1);

        boolean success = userService.register(newUser, "DONOR");

        assertTrue(success);
        assertEquals(2, newUser.getRoleId());
        assertEquals("PENDING", newUser.getAccountStatus());
        assertNotEquals("plaintextPass123", newUser.getPasswordHash()); // Hashed during registration
        verify(userDao).createUser(newUser);
    }

    @Test
    public void testRegisterAdminSuccess() throws SQLException {
        User newUser = new User();
        newUser.setEmail("admin2@ecofeast.com");
        newUser.setPasswordHash("adminPass123");
        newUser.setFullName("Admin User");

        when(userDao.findByEmail("admin2@ecofeast.com")).thenReturn(null);
        when(userDao.getRoleIdByName("ADMIN")).thenReturn(1);
        when(userDao.createUser(any(User.class))).thenReturn(1);

        boolean success = userService.register(newUser, "ADMIN");

        assertTrue(success);
        assertEquals(1, newUser.getRoleId());
        assertEquals("APPROVED", newUser.getAccountStatus()); // Admins are automatically approved
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterDuplicateEmail() throws SQLException {
        User newUser = new User();
        newUser.setEmail("donor@ecofeast.com"); // already exists
        newUser.setPasswordHash("Password123");

        when(userDao.findByEmail("donor@ecofeast.com")).thenReturn(existingUser);

        userService.register(newUser, "DONOR");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterInvalidRole() throws SQLException {
        User newUser = new User();
        newUser.setEmail("newuser@ecofeast.com");
        newUser.setPasswordHash("Password123");

        when(userDao.findByEmail("newuser@ecofeast.com")).thenReturn(null);
        when(userDao.getRoleIdByName("INVALID_ROLE")).thenReturn(-1);

        userService.register(newUser, "INVALID_ROLE");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateProfileEmptyName() throws SQLException {
        userService.updateProfile(10, "  ", "9800000002", null, null, null, null, null);
    }

    @Test
    public void testUpdateProfileSuccess() throws SQLException {
        when(userDao.findById(10)).thenReturn(existingUser);
        when(userDao.updateProfile(any(User.class))).thenReturn(true);
        when(userDao.findById(10)).thenReturn(existingUser);

        User updated = userService.updateProfile(
                10, "Updated Donor", "9800000099", "Org", "Addr", "City", "State", "44600");

        assertNotNull(updated);
        verify(userDao).updateProfile(argThat(u ->
                "Updated Donor".equals(u.getFullName()) && "9800000099".equals(u.getPhone())));
    }
}
