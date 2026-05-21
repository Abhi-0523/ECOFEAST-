package com.ecofeast.service;

import com.ecofeast.dao.DonationRequestDao;
import com.ecofeast.dao.FoodDonationDao;
import com.ecofeast.dao.NotificationDao;
import com.ecofeast.dao.VolunteerTaskDao;
import com.ecofeast.model.DonationRequest;
import com.ecofeast.model.Notification;
import com.ecofeast.model.VolunteerTask;

import java.sql.SQLException;
import java.util.List;

/**
 * VolunteerService - Business Logic Layer for volunteer task management.
 *
 * Handles task discovery, acceptance, progress tracking, and completion.
 * Sends notifications upon key task events.
 */
public class VolunteerService {

    private final VolunteerTaskDao taskDao;
    private final NotificationDao notificationDao;
    private final DonationRequestDao requestDao;
    private final FoodDonationDao donationDao;

    public VolunteerService() {
        this.taskDao = new VolunteerTaskDao();
        this.notificationDao = new NotificationDao();
        this.requestDao = new DonationRequestDao();
        this.donationDao = new FoodDonationDao();
    }

    public VolunteerService(VolunteerTaskDao taskDao, NotificationDao notificationDao,
                            DonationRequestDao requestDao, FoodDonationDao donationDao) {
        this.taskDao = taskDao;
        this.notificationDao = notificationDao;
        this.requestDao = requestDao;
        this.donationDao = donationDao;
    }

    /**
     * Returns all open tasks that a volunteer can accept.
     *
     * @return list of open VolunteerTask objects
     * @throws SQLException on database error
     */
    public List<VolunteerTask> getOpenTasks() throws SQLException {
        return taskDao.getOpenTasks();
    }

    /**
     * Returns all tasks for a specific volunteer (active + completed).
     *
     * @param volunteerId the volunteer's user_id
     * @return list of VolunteerTask objects
     * @throws SQLException on database error
     */
    public List<VolunteerTask> getMyTasks(int volunteerId) throws SQLException {
        return taskDao.getTasksByVolunteer(volunteerId);
    }

    /**
     * Volunteer accepts an open task.
     * Business Rule: Task must be in OPEN status.
     *
     * @param taskId      the task ID to accept
     * @param volunteerId the volunteer accepting the task
     * @return true if acceptance succeeded
     * @throws SQLException on database error
     */
    public boolean acceptTask(int taskId, int volunteerId) throws SQLException {
        boolean result = taskDao.acceptTask(taskId, volunteerId);
        if (result) {
            // Notify volunteer of their accepted task
            Notification notif = new Notification(
                volunteerId,
                "Task Accepted",
                "You have successfully accepted a pickup/delivery task.",
                "SUCCESS",
                "/volunteer?action=myTasks"
            );
            notificationDao.createNotification(notif);
        }
        return result;
    }

    /**
     * Volunteer starts a task (marks as IN_PROGRESS).
     *
     * @param taskId      the task ID
     * @param volunteerId ownership check
     * @return true if update succeeded
     * @throws SQLException on database error
     */
    public boolean startTask(int taskId, int volunteerId) throws SQLException {
        return taskDao.startTask(taskId, volunteerId);
    }

    /**
     * Volunteer marks a task as completed.
     * Business Rule: Task must be IN_PROGRESS and owned by this volunteer.
     *
     * @param taskId      the task ID
     * @param volunteerId ownership check
     * @return true if completion recorded
     * @throws SQLException on database error
     */
    public boolean completeTask(int taskId, int volunteerId) throws SQLException {
        VolunteerTask task = taskDao.getTaskById(taskId);
        boolean result = taskDao.completeTask(taskId, volunteerId);
        if (result) {
            if (task != null) {
                requestDao.markCollected(task.getRequestId());
                DonationRequest request = requestDao.getRequestById(task.getRequestId());
                if (request != null) {
                    donationDao.updateStatus(request.getDonationId(), "DISTRIBUTED");
                }
            }

            Notification notif = new Notification(
                volunteerId,
                "Task Completed!",
                "Your delivery task has been marked as completed. Thank you!",
                "SUCCESS",
                "/volunteer?action=deliveryHistory"
            );
            notificationDao.createNotification(notif);
        }
        return result;
    }

    /**
     * Returns all tasks — admin monitoring view.
     *
     * @return list of all VolunteerTask objects
     * @throws SQLException on database error
     */
    public List<VolunteerTask> getAllTasks() throws SQLException {
        return taskDao.getAllTasks();
    }

    /**
     * Creates a new volunteer task linked to an approved donation request.
     * Called automatically when a donor approves an NGO request.
     *
     * @param task the VolunteerTask to create
     * @return generated task_id or -1 on failure
     * @throws SQLException on database error
     */
    public int createTask(VolunteerTask task) throws SQLException {
        return taskDao.createTask(task);
    }

    /**
     * Gets task completion stats for admin dashboard.
     *
     * @return Map of status → count
     * @throws SQLException on database error
     */
    public java.util.Map<String, Integer> getTaskStats() throws SQLException {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        stats.put("openTasks",      taskDao.countByStatus("OPEN"));
        stats.put("acceptedTasks",  taskDao.countByStatus("ACCEPTED"));
        stats.put("completedTasks", taskDao.countByStatus("COMPLETED"));
        return stats;
    }
}
