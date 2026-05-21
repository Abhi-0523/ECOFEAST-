package com.ecofeast.service;

import com.ecofeast.dao.*;
import com.ecofeast.model.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DonationService - Business Logic Layer for food donation operations.
 *
 * Acts as an intermediary between Servlet Controllers and DAO classes.
 * Contains validation logic and business rules.
 *
 * MVC Role: This is the MODEL layer's service component.
 */
public class DonationService {

    private final FoodDonationDao donationDao;
    private final DonationRequestDao requestDao;
    private final FoodCategoryDao categoryDao;
    private final NotificationDao notificationDao;
    private final VolunteerTaskDao taskDao;

    public DonationService() {
        this.donationDao = new FoodDonationDao();
        this.requestDao = new DonationRequestDao();
        this.categoryDao = new FoodCategoryDao();
        this.notificationDao = new NotificationDao();
        this.taskDao = new VolunteerTaskDao();
    }

    public DonationService(FoodDonationDao donationDao, DonationRequestDao requestDao,
                           FoodCategoryDao categoryDao, NotificationDao notificationDao,
                           VolunteerTaskDao taskDao) {
        this.donationDao = donationDao;
        this.requestDao = requestDao;
        this.categoryDao = categoryDao;
        this.notificationDao = notificationDao;
        this.taskDao = taskDao;
    }

    /* -------------------------------------------------------
     * DONATION LISTING (DONOR)
     * ----------------------------------------------------- */

    /**
     * Adds a new food donation listing by a donor.
     * Business Rule: Validates non-empty food name and positive quantity.
     *
     * @param donation the FoodDonation object populated from form
     * @return generated donation_id or -1 on failure
     * @throws SQLException on database error
     */
    public int addDonation(FoodDonation donation) throws SQLException {
        // Business validation
        if (donation.getFoodName() == null || donation.getFoodName().isBlank()) {
            throw new IllegalArgumentException("Food name is required.");
        }
        if (donation.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (donation.getExpiryTime() == null) {
            throw new IllegalArgumentException("Expiry time is required.");
        }
        return donationDao.addDonation(donation);
    }

    /**
     * Updates an existing donation (donor's edit form).
     * Only the owning donor can update their donation.
     *
     * @param donation updated FoodDonation data
     * @return true if update succeeded
     * @throws SQLException on database error
     */
    public boolean updateDonation(FoodDonation donation) throws SQLException {
        if (donation.getDonationId() <= 0 || donation.getDonorId() <= 0) {
            throw new IllegalArgumentException("Invalid donation or donor ID.");
        }
        return donationDao.updateDonation(donation);
    }

    /**
     * Deletes a donation owned by the given donor.
     *
     * @param donationId the donation ID to delete
     * @param donorId    ownership check
     * @return true if deletion succeeded
     * @throws SQLException on database error
     */
    public boolean deleteDonation(int donationId, int donorId) throws SQLException {
        return donationDao.deleteDonation(donationId, donorId);
    }

    /**
     * Retrieves all donations by a specific donor (for dashboard).
     *
     * @param donorId the donor's user_id
     * @return list of FoodDonation objects
     * @throws SQLException on database error
     */
    public List<FoodDonation> getDonorDonations(int donorId) throws SQLException {
        return donationDao.getDonationsByDonor(donorId);
    }

    /* -------------------------------------------------------
     * FOOD BROWSING (NGO)
     * ----------------------------------------------------- */

    /**
     * Returns all available donations for NGO browsing.
     *
     * @return list of available FoodDonation objects
     * @throws SQLException on database error
     */
    public List<FoodDonation> getAvailableDonations() throws SQLException {
        return donationDao.getAvailableDonations();
    }

    /**
     * Searches donations by keyword, category, and city.
     *
     * @param keyword    search keyword
     * @param categoryId category filter (0 = all)
     * @param city       city filter
     * @return filtered list of FoodDonation objects
     * @throws SQLException on database error
     */
    public List<FoodDonation> searchDonations(String keyword, int categoryId, String city) throws SQLException {
        return donationDao.searchDonations(keyword, categoryId, city);
    }

    /**
     * Retrieves a single donation by ID.
     *
     * @param donationId the donation ID
     * @return FoodDonation or null
     * @throws SQLException on database error
     */
    public FoodDonation getDonationById(int donationId) throws SQLException {
        return donationDao.getDonationById(donationId);
    }

    /* -------------------------------------------------------
     * DONATION REQUESTS (NGO → DONOR)
     * ----------------------------------------------------- */

    /**
     * Submits a food request by an NGO.
     * Business Rule: An NGO cannot request the same donation twice.
     *
     * @param request the DonationRequest to submit
     * @return generated request_id or -1 on failure
     * @throws SQLException          on database error
     * @throws IllegalStateException if duplicate request detected
     */
    public int submitRequest(DonationRequest request) throws SQLException {
        // Prevent duplicate requests
        if (requestDao.hasAlreadyRequested(request.getNgoId(), request.getDonationId())) {
            throw new IllegalStateException("You have already submitted a request for this donation.");
        }
        if (request.getQuantityRequested() <= 0) {
            throw new IllegalArgumentException("Requested quantity must be greater than zero.");
        }
        int id = requestDao.addRequest(request);
        if (id > 0) {
            // Notify the donor about the new request
            FoodDonation donation = donationDao.getDonationById(request.getDonationId());
            if (donation != null) {
                Notification notif = new Notification(
                    donation.getDonorId(),
                    "New Food Request",
                    "An NGO has requested your donation: " + donation.getFoodName(),
                    "INFO",
                    "/donor?action=requests"
                );
                notificationDao.createNotification(notif);
            }
        }
        return id;
    }

    /**
     * Donor approves an NGO's food request.
     * Also creates a volunteer task for pickup.
     *
     * @param requestId  the request to approve
     * @param donorId    ownership check (must be the donor's request)
     * @return true if approval succeeded
     * @throws SQLException on database error
     */
    public boolean approveRequest(int requestId, int donorId) throws SQLException {
        boolean result = requestDao.approveRequest(requestId, donorId);
        if (result) {
            // Notify the NGO
            DonationRequest req = requestDao.getRequestById(requestId);
            if (req != null) {
                FoodDonation donation = donationDao.getDonationById(req.getDonationId());

                Notification notif = new Notification(
                    req.getNgoId(),
                    "Request Approved!",
                    "Your food request has been approved. Please arrange pickup.",
                    "SUCCESS",
                    "/ngo?action=requests"
                );
                notificationDao.createNotification(notif);

                // Update donation status to APPROVED
                donationDao.updateStatus(req.getDonationId(), "APPROVED");

                if (donation != null) {
                    VolunteerTask task = new VolunteerTask();
                    task.setRequestId(req.getRequestId());
                    task.setTaskType("PICKUP");
                    task.setPickupAddress(formatAddress(donation.getPickupLocation(), donation.getPickupCity()));
                    task.setDeliveryAddress(formatDeliveryAddress(req));
                    taskDao.createTask(task);
                }
            }
        }
        return result;
    }

    /**
     * Donor rejects an NGO's food request with a reason.
     *
     * @param requestId the request to reject
     * @param reason    rejection reason
     * @return true if rejection succeeded
     * @throws SQLException on database error
     */
    public boolean rejectRequest(int requestId, int donorId, String reason) throws SQLException {
        boolean result = requestDao.rejectRequest(requestId, donorId, reason);
        if (result) {
            DonationRequest req = requestDao.getRequestById(requestId);
            if (req != null) {
                Notification notif = new Notification(
                    req.getNgoId(),
                    "Request Rejected",
                    "Your food request was rejected. Reason: " + (reason != null ? reason : "N/A"),
                    "WARNING",
                    "/ngo?action=requests"
                );
                notificationDao.createNotification(notif);
            }
        }
        return result;
    }

    private String formatAddress(String address, String city) {
        String cleanAddress = address == null ? "" : address.trim();
        String cleanCity = city == null ? "" : city.trim();

        if (cleanAddress.isEmpty()) {
            return cleanCity;
        }
        if (cleanCity.isEmpty() || cleanAddress.toLowerCase().contains(cleanCity.toLowerCase())) {
            return cleanAddress;
        }
        return cleanAddress + ", " + cleanCity;
    }

    private String formatDeliveryAddress(DonationRequest req) {
        String ngoName = req.getNgoName();
        if (ngoName == null || ngoName.isBlank()) {
            return "NGO delivery location to be coordinated";
        }
        return ngoName + " delivery location to be coordinated";
    }

    /**
     * NGO cancels a pending request.
     *
     * @param requestId the request to cancel
     * @param ngoId     ownership check
     * @return true if cancellation succeeded
     * @throws SQLException on database error
     */
    public boolean cancelRequest(int requestId, int ngoId) throws SQLException {
        return requestDao.cancelRequest(requestId, ngoId);
    }

    /**
     * Retrieves all requests made by a specific NGO.
     *
     * @param ngoId the NGO's user_id
     * @return list of DonationRequest objects
     * @throws SQLException on database error
     */
    public List<DonationRequest> getNgoRequests(int ngoId) throws SQLException {
        return requestDao.getRequestsByNgo(ngoId);
    }

    /**
     * Retrieves all requests on a donor's donations.
     *
     * @param donorId the donor's user_id
     * @return list of DonationRequest objects
     * @throws SQLException on database error
     */
    public List<DonationRequest> getDonorRequests(int donorId) throws SQLException {
        return requestDao.getRequestsByDonor(donorId);
    }

    /* -------------------------------------------------------
     * CATEGORIES
     * ----------------------------------------------------- */

    /**
     * Returns all active food categories.
     *
     * @return list of FoodCategory objects
     * @throws SQLException on database error
     */
    public List<FoodCategory> getAllCategories() throws SQLException {
        return categoryDao.getAllCategories();
    }

    /* -------------------------------------------------------
     * ADMIN — STATISTICS
     * ----------------------------------------------------- */

    /**
     * Compiles a statistics map for the admin dashboard.
     * Returns counts of users, donations, requests, etc.
     *
     * @return Map of stat label → count
     * @throws SQLException on database error
     */
    public Map<String, Integer> getDashboardStats() throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalDonations",     donationDao.countAll());
        stats.put("availableDonations", donationDao.countByStatus("AVAILABLE"));
        stats.put("distributedDonations", donationDao.countByStatus("DISTRIBUTED"));
        stats.put("pendingRequests",    requestDao.countByStatus("PENDING"));
        stats.put("approvedRequests",   requestDao.countByStatus("APPROVED"));
        stats.put("collectedRequests",  requestDao.countByStatus("COLLECTED"));
        return stats;
    }

    /** Returns all donations — admin view. */
    public List<FoodDonation> getAllDonations() throws SQLException {
        return donationDao.getAllDonations();
    }

    /** Returns all requests — admin view. */
    public List<DonationRequest> getAllRequests() throws SQLException {
        return requestDao.getAllRequests();
    }
}
