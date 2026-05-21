package com.ecofeast.controllers;

import com.ecofeast.model.User;
import com.ecofeast.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class RegisterServletTest {

    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher dispatcher;

    private RegisterServlet registerServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registerServlet = new RegisterServlet(userService);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void testDoGet() throws Exception {
        registerServlet.doGet(request, response);
        verify(request).getRequestDispatcher("/views/register.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostSuccessPending() throws Exception {
        // Setup mock form parameters
        when(request.getParameter("role")).thenReturn("DONOR");
        when(request.getParameter("fullName")).thenReturn("Alice Green");
        when(request.getParameter("email")).thenReturn("alice@green.com");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("organization")).thenReturn("Green Corp");
        when(request.getParameter("city")).thenReturn("Austin");

        // Stub registration service call
        when(userService.register(any(User.class), eq("DONOR"))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setAccountStatus("PENDING"); // sets status as expected from a success
            return true;
        });

        registerServlet.doPost(request, response);

        verify(request).setAttribute(eq("success"), contains("successful"));
        verify(request).getRequestDispatcher("/views/login.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostMissingFields() throws Exception {
        when(request.getParameter("role")).thenReturn("DONOR");
        when(request.getParameter("fullName")).thenReturn(""); // Missing required full name
        when(request.getParameter("email")).thenReturn("alice@green.com");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getParameter("password")).thenReturn("Pass1234");

        registerServlet.doPost(request, response);

        verify(request).setAttribute("error", "Please fill in all required fields.");
        verify(request).getRequestDispatcher("/views/register.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostInvalidRole() throws Exception {
        when(request.getParameter("role")).thenReturn("ADMIN"); // Not in allowed registration roles
        when(request.getParameter("fullName")).thenReturn("Admin Spy");
        when(request.getParameter("email")).thenReturn("spy@admin.com");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getParameter("password")).thenReturn("Pass1234");

        registerServlet.doPost(request, response);

        verify(request).setAttribute("error", "Please select a valid registration role.");
        verify(request).getRequestDispatcher("/views/register.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostInvalidEmail() throws Exception {
        when(request.getParameter("role")).thenReturn("DONOR");
        when(request.getParameter("fullName")).thenReturn("Alice Green");
        when(request.getParameter("email")).thenReturn("invalidemail"); // Invalid email format
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getParameter("password")).thenReturn("Pass1234");

        registerServlet.doPost(request, response);

        verify(request).setAttribute("error", "Please enter a valid email address.");
        verify(request).getRequestDispatcher("/views/register.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostInvalidPhone() throws Exception {
        when(request.getParameter("role")).thenReturn("DONOR");
        when(request.getParameter("fullName")).thenReturn("Alice Green");
        when(request.getParameter("email")).thenReturn("alice@green.com");
        when(request.getParameter("phone")).thenReturn("123"); // Too short
        when(request.getParameter("password")).thenReturn("Pass1234");

        registerServlet.doPost(request, response);

        verify(request).setAttribute("error", "Please enter a valid phone number.");
        verify(request).getRequestDispatcher("/views/register.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostDuplicateEmailException() throws Exception {
        when(request.getParameter("role")).thenReturn("DONOR");
        when(request.getParameter("fullName")).thenReturn("Alice Green");
        when(request.getParameter("email")).thenReturn("alice@green.com");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getParameter("password")).thenReturn("Pass1234");

        when(userService.register(any(User.class), eq("DONOR")))
            .thenThrow(new IllegalArgumentException("Email already exists."));

        registerServlet.doPost(request, response);

        verify(request).setAttribute("error", "Email already exists.");
        verify(request).getRequestDispatcher("/views/register.jsp");
        verify(dispatcher).forward(request, response);
    }
}
