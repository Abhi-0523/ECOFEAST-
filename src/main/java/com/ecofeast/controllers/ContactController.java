package com.ecofeast.controllers;

import com.ecofeast.dao.ContactMessageDao;
import com.ecofeast.model.ContactMessage;
import com.ecofeast.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import java.io.IOException;

@MultipartConfig(
        maxFileSize = 1048576,
        maxRequestSize = 5242880,
        fileSizeThreshold = 0
)
public class ContactController extends HttpServlet {

    private final ContactMessageDao contactDao = new ContactMessageDao();

    /* -------------------------------------------------------
     * HTTP GET — Show the contact form page
     * ----------------------------------------------------- */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/contact.jsp").forward(request, response);
    }

    /* -------------------------------------------------------
     * HTTP POST — Process contact form submission
     * ----------------------------------------------------- */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name    = request.getParameter("name");
        String email   = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        // --- Validation ---
        if (!ValidationUtil.isNotEmpty(name) || !ValidationUtil.isNotEmpty(email)
                || !ValidationUtil.isNotEmpty(message)) {
            request.setAttribute("error", "Name, email, and message are required.");
            request.setAttribute("prevName",    name);
            request.setAttribute("prevEmail",   email);
            request.setAttribute("prevSubject", subject);
            request.setAttribute("prevMessage", message);
            request.getRequestDispatcher("/views/contact.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Please enter a valid email address.");
            request.setAttribute("prevName",    name);
            request.setAttribute("prevEmail",   email);
            request.setAttribute("prevSubject", subject);
            request.setAttribute("prevMessage", message);
            request.getRequestDispatcher("/views/contact.jsp").forward(request, response);
            return;
        }

        // --- Save to DB ---
        try {
            ContactMessage msg = new ContactMessage(name.trim(), email.trim(), subject, message.trim());
            boolean saved = contactDao.saveMessage(msg);

            if (saved) {
                request.setAttribute("success",
                    "Thank you! Your message has been received. We'll respond within 24 hours.");
            } else {
                request.setAttribute("error", "Failed to send your message. Please try again.");
            }
        } catch (Exception e) {
            System.err.println("[ContactController] Error: " + e.getMessage());
            request.setAttribute("error", "A system error occurred. Please try again later.");
        }

        request.getRequestDispatcher("/views/contact.jsp").forward(request, response);
    }
}
