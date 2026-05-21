package com.ecofeast.controllers;

import com.ecofeast.model.Role;
import com.ecofeast.model.User;
import com.ecofeast.service.UserService;
import com.ecofeast.service.UserService.LoginFailure;
import com.ecofeast.service.UserService.LoginResult;
import com.ecofeast.util.SessionUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Constructor;

import static org.mockito.Mockito.*;

public class LoginServletTest {

    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher dispatcher;

    private LoginServlet loginServlet;

    private User sampleUser;
    private Role donorRole;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        loginServlet = new LoginServlet(userService);

        donorRole = new Role(2, "DONOR");

        sampleUser = new User();
        sampleUser.setId(10);
        sampleUser.setEmail("donor@ecofeast.com");
        sampleUser.setRole(donorRole);
        sampleUser.setRoleId(2);
        sampleUser.setAccountStatus("APPROVED");

        when(request.getSession(true)).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getContextPath()).thenReturn("/ecofeast");
    }

    // Helper to generate a LoginResult using reflection because the constructor is private
    private LoginResult createLoginResult(User user, LoginFailure failure) throws Exception {
        Constructor<LoginResult> constructor = LoginResult.class.getDeclaredConstructor(User.class, LoginFailure.class);
        constructor.setAccessible(true);
        return constructor.newInstance(user, failure);
    }

    @Test
    public void testDoGetNotLoggedIn() throws Exception {
        when(session.getAttribute(SessionUtil.SESSION_USER)).thenReturn(null);

        loginServlet.doGet(request, response);

        verify(request).getRequestDispatcher("/views/login.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetAlreadyLoggedIn() throws Exception {
        when(session.getAttribute(SessionUtil.SESSION_USER)).thenReturn(sampleUser);

        loginServlet.doGet(request, response);

        verify(response).sendRedirect("/ecofeast/donor/?action=dashboard");
    }

    @Test
    public void testDoPostSuccess() throws Exception {
        when(request.getParameter("email")).thenReturn("donor@ecofeast.com");
        when(request.getParameter("password")).thenReturn("Donor@123");
        when(session.getAttribute(SessionUtil.SESSION_USER)).thenReturn(sampleUser);
        
        LoginResult successResult = createLoginResult(sampleUser, LoginFailure.SUCCESS);
        when(userService.tryLogin("donor@ecofeast.com", "Donor@123")).thenReturn(successResult);

        loginServlet.doPost(request, response);

        verify(session).setAttribute(SessionUtil.SESSION_USER, sampleUser);
        verify(response).sendRedirect("/ecofeast/donor/?action=dashboard");
    }

    @Test
    public void testDoPostPendingStatus() throws Exception {
        sampleUser.setAccountStatus("PENDING");
        when(request.getParameter("email")).thenReturn("donor@ecofeast.com");
        when(request.getParameter("password")).thenReturn("Donor@123");
        when(response.encodeRedirectURL(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        LoginResult successResult = createLoginResult(sampleUser, LoginFailure.SUCCESS);
        when(userService.tryLogin("donor@ecofeast.com", "Donor@123")).thenReturn(successResult);

        loginServlet.doPost(request, response);

        verify(session).setAttribute(SessionUtil.SESSION_USER, sampleUser);
        verify(response).sendRedirect("/ecofeast/error/account-pending.jsp?status=PENDING");
    }

    @Test
    public void testDoPostRejectedStatus() throws Exception {
        sampleUser.setAccountStatus("REJECTED");
        when(request.getParameter("email")).thenReturn("donor@ecofeast.com");
        when(request.getParameter("password")).thenReturn("Donor@123");

        LoginResult successResult = createLoginResult(sampleUser, LoginFailure.SUCCESS);
        when(userService.tryLogin("donor@ecofeast.com", "Donor@123")).thenReturn(successResult);

        loginServlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(request).getRequestDispatcher("/views/login.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostUnknownEmail() throws Exception {
        when(request.getParameter("email")).thenReturn("bad@ecofeast.com");
        when(request.getParameter("password")).thenReturn("pwd");

        LoginResult unknownResult = createLoginResult(null, LoginFailure.UNKNOWN_EMAIL);
        when(userService.tryLogin("bad@ecofeast.com", "pwd")).thenReturn(unknownResult);

        loginServlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(request).getRequestDispatcher("/views/login.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWrongPassword() throws Exception {
        when(request.getParameter("email")).thenReturn("donor@ecofeast.com");
        when(request.getParameter("password")).thenReturn("wrong");

        LoginResult wrongPassResult = createLoginResult(null, LoginFailure.WRONG_PASSWORD);
        when(userService.tryLogin("donor@ecofeast.com", "wrong")).thenReturn(wrongPassResult);

        loginServlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(request).getRequestDispatcher("/views/login.jsp");
        verify(dispatcher).forward(request, response);
    }
}
