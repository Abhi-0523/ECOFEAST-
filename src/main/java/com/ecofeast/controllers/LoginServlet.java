package com.ecofeast.controllers;

import com.ecofeast.config.AppConfig;
import com.ecofeast.model.User;
import com.ecofeast.service.UserService;
import com.ecofeast.service.UserService.LoginFailure;
import com.ecofeast.service.UserService.LoginResult;
import com.ecofeast.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private final UserService userService;

    public LoginServlet() {
        this.userService = new UserService();
    }

    public LoginServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        putDemoAdminHint(req);
        if (SessionUtil.isLoggedIn(req)) {
            redirectByRole(req, resp);
            return;
        }
        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        putDemoAdminHint(req);
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            LoginResult outcome = userService.tryLogin(email, password);
            if (outcome.isSuccess()) {
                User user = outcome.user;
                String accountStatus = normalize(user.getAccountStatus());
                if ("PENDING".equals(accountStatus)) {
                    // Allow sign-in: pending users get a session and see the waiting page (and can log out).
                    SessionUtil.setLoggedInUser(req, user);
                    resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/error/account-pending.jsp?status=PENDING"));
                    return;
                }
                if ("REJECTED".equals(accountStatus)) {
                    req.setAttribute("error",
                            "Your registration was not approved. Please contact support if you have questions.");
                    req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
                    return;
                }
                SessionUtil.setLoggedInUser(req, user);
                String redirect = (String) req.getSession().getAttribute("redirectAfterLogin");
                if (redirect != null) {
                    req.getSession().removeAttribute("redirectAfterLogin");
                    resp.sendRedirect(redirect);
                } else {
                    redirectByRole(req, resp);
                }
            } else if (outcome.failure == LoginFailure.UNKNOWN_EMAIL) {
                req.setAttribute("error",
                        "No account exists for this email. The built-in administrator is not your personal Gmail — "
                                + "use email \"" + AppConfig.DEMO_ADMIN_EMAIL + "\" with password \""
                                + AppConfig.DEMO_ADMIN_PASSWORD + "\" after importing database/schema_full.sql. "
                                + "Otherwise register first, then wait for admin approval.");
                req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            } else if (outcome.failure == LoginFailure.WRONG_PASSWORD) {
                req.setAttribute("error", "Wrong password for this email. If you are signing in as the demo admin, the password is \"" + AppConfig.DEMO_ADMIN_PASSWORD + "\".");
                req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "System Error: " + e.getMessage());
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
        }
    }

    private void putDemoAdminHint(HttpServletRequest req) {
        req.setAttribute("demoAdminEmail", AppConfig.DEMO_ADMIN_EMAIL);
        req.setAttribute("demoAdminPassword", AppConfig.DEMO_ADMIN_PASSWORD);
    }

    private void redirectByRole(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String role = SessionUtil.getUserRole(req);
        if ("ADMIN".equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/admin?action=dashboard");
        } else if ("DONOR".equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/donor/?action=dashboard");
        } else if ("NGO".equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/ngo/?action=dashboard");
        } else if ("VOLUNTEER".equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/volunteer/?action=dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }
}
