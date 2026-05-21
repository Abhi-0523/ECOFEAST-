package com.ecofeast.controllers;

import com.ecofeast.model.User;
import com.ecofeast.model.VolunteerTask;
import com.ecofeast.service.VolunteerService;
import com.ecofeast.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * VolunteerController - Servlet handling all volunteer operations.
 *
 * URL Pattern: /volunteer
 * Actions via ?action= parameter:
 *   GET:  dashboard, pickupTasks, myTasks, deliveryHistory
 *   POST: acceptTask, startTask, completeTask
 *
 * MVC Role: Controller — orchestrates volunteer features between Service and JSP.
 */
public class VolunteerController extends HttpServlet {

    private final VolunteerService volunteerService = new VolunteerService();

    /* -------------------------------------------------------
     * HTTP GET
     * ----------------------------------------------------- */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = SessionUtil.getLoggedInUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "dashboard";

        try {
            switch (action) {

                case "dashboard":
                    // Load task counts for dashboard overview cards
                    List<VolunteerTask> myAllTasks = volunteerService.getMyTasks(user.getId());
                    List<VolunteerTask> openTasks  = volunteerService.getOpenTasks();

                    long myActive    = myAllTasks.stream().filter(t -> "ACCEPTED".equals(t.getStatus()) || "IN_PROGRESS".equals(t.getStatus())).count();
                    long myCompleted = myAllTasks.stream().filter(t -> "COMPLETED".equals(t.getStatus())).count();

                    request.setAttribute("openTaskCount",      openTasks.size());
                    request.setAttribute("myActiveTaskCount",  myActive);
                    request.setAttribute("myCompletedCount",   myCompleted);
                    request.setAttribute("recentTasks",
                        myAllTasks.subList(0, Math.min(5, myAllTasks.size())));
                    forward(request, response, "/WEB-INF/views/volunteer/volunteer-dashboard.jsp");
                    break;

                case "pickupTasks":
                    // Show all OPEN tasks available to accept
                    request.setAttribute("tasks", volunteerService.getOpenTasks());
                    forward(request, response, "/WEB-INF/views/volunteer/pickup-tasks.jsp");
                    break;

                case "myTasks":
                    // Tasks already accepted/in-progress
                    request.setAttribute("tasks", volunteerService.getMyTasks(user.getId()));
                    forward(request, response, "/WEB-INF/views/volunteer/pickup-tasks.jsp");
                    break;

                case "deliveryHistory":
                    // Completed deliveries for this volunteer
                    List<VolunteerTask> allTasks = volunteerService.getMyTasks(user.getId());
                    request.setAttribute("tasks", allTasks);
                    forward(request, response, "/WEB-INF/views/volunteer/delivery-history.jsp");
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/volunteer?action=dashboard");
            }
        } catch (Exception e) {
            handleError(request, response, e, "/WEB-INF/views/volunteer/volunteer-dashboard.jsp");
        }
    }

    /* -------------------------------------------------------
     * HTTP POST
     * ----------------------------------------------------- */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = SessionUtil.getLoggedInUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        int taskId    = parseIntOrZero(request.getParameter("taskId"));

        try {
            boolean result;
            switch (action != null ? action : "") {

                case "acceptTask":
                    result = volunteerService.acceptTask(taskId, user.getId());
                    setFlashMessage(request, result,
                        "Task accepted! Check your active tasks.",
                        "Could not accept task — it may have been taken.");
                    response.sendRedirect(request.getContextPath() + "/volunteer?action=myTasks");
                    break;

                case "startTask":
                    result = volunteerService.startTask(taskId, user.getId());
                    setFlashMessage(request, result,
                        "Task started! Mark complete when done.",
                        "Failed to update task status.");
                    response.sendRedirect(request.getContextPath() + "/volunteer?action=myTasks");
                    break;

                case "completeTask":
                    result = volunteerService.completeTask(taskId, user.getId());
                    setFlashMessage(request, result,
                        "Task completed! Great work!",
                        "Failed to mark task as complete.");
                    response.sendRedirect(request.getContextPath() + "/volunteer?action=deliveryHistory");
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/volunteer?action=dashboard");
            }
        } catch (Exception e) {
            handleError(request, response, e, "/WEB-INF/views/volunteer/volunteer-dashboard.jsp");
        }
    }

    /* -------------------------------------------------------
     * PRIVATE HELPERS
     * ----------------------------------------------------- */

    private void forward(HttpServletRequest req, HttpServletResponse res, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, res);
    }

    /** Stores a flash message in the session for use after redirect. */
    private void setFlashMessage(HttpServletRequest req, boolean success, String ok, String fail) {
        HttpSession session = req.getSession();
        if (success) session.setAttribute("successMsg", ok);
        else         session.setAttribute("errorMsg",   fail);
    }

    private void handleError(HttpServletRequest req, HttpServletResponse res, Exception e, String fallback)
            throws ServletException, IOException {
        System.err.println("[VolunteerController] Error: " + e.getMessage());
        req.setAttribute("error", "An error occurred: " + e.getMessage());
        req.getRequestDispatcher(fallback).forward(req, res);
    }

    private int parseIntOrZero(String val) {
        try { return Integer.parseInt(val); } catch (Exception e) { return 0; }
    }
}
