package com.ecofeast.service;

import com.ecofeast.dao.*;
import com.ecofeast.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DonationServiceTest {

    @Mock
    private FoodDonationDao donationDao;
    @Mock
    private DonationRequestDao requestDao;
    @Mock
    private FoodCategoryDao categoryDao;
    @Mock
    private NotificationDao notificationDao;
    @Mock
    private VolunteerTaskDao taskDao;

    private DonationService donationService;

    private FoodDonation sampleDonation;
    private DonationRequest sampleRequest;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        donationService = new DonationService(donationDao, requestDao, categoryDao, notificationDao, taskDao);

        sampleDonation = new FoodDonation();
        sampleDonation.setDonationId(100);
        sampleDonation.setDonorId(10);
        sampleDonation.setCategoryId(1);
        sampleDonation.setFoodName("Canned Soup");
        sampleDonation.setQuantity(50);
        sampleDonation.setExpiryTime(LocalDateTime.now().plusDays(30));
        sampleDonation.setPickupLocation("Main St 123");
        sampleDonation.setPickupCity("Dallas");

        sampleRequest = new DonationRequest();
        sampleRequest.setRequestId(200);
        sampleRequest.setDonationId(100);
        sampleRequest.setNgoId(20);
        sampleRequest.setQuantityRequested(10);
        sampleRequest.setRequestMessage("Need for shelter");
        sampleRequest.setNgoName("Shelter NGO");
    }

    @Test
    public void testAddDonationSuccess() throws SQLException {
        when(donationDao.addDonation(any(FoodDonation.class))).thenReturn(100);

        int result = donationService.addDonation(sampleDonation);

        assertEquals(100, result);
        verify(donationDao).addDonation(sampleDonation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDonationEmptyName() throws SQLException {
        sampleDonation.setFoodName("");
        donationService.addDonation(sampleDonation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDonationInvalidQuantity() throws SQLException {
        sampleDonation.setQuantity(0);
        donationService.addDonation(sampleDonation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDonationNullExpiry() throws SQLException {
        sampleDonation.setExpiryTime(null);
        donationService.addDonation(sampleDonation);
    }

    @Test
    public void testUpdateDonationSuccess() throws SQLException {
        when(donationDao.updateDonation(any(FoodDonation.class))).thenReturn(true);

        boolean success = donationService.updateDonation(sampleDonation);

        assertTrue(success);
        verify(donationDao).updateDonation(sampleDonation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateDonationInvalidIds() throws SQLException {
        sampleDonation.setDonorId(0);
        donationService.updateDonation(sampleDonation);
    }

    @Test
    public void testDeleteDonation() throws SQLException {
        when(donationDao.deleteDonation(100, 10)).thenReturn(true);

        boolean result = donationService.deleteDonation(100, 10);

        assertTrue(result);
        verify(donationDao).deleteDonation(100, 10);
    }

    @Test
    public void testGetDonorDonations() throws SQLException {
        List<FoodDonation> list = new ArrayList<>();
        list.add(sampleDonation);
        when(donationDao.getDonationsByDonor(10)).thenReturn(list);

        List<FoodDonation> result = donationService.getDonorDonations(10);

        assertEquals(1, result.size());
        assertEquals("Canned Soup", result.get(0).getFoodName());
    }

    @Test
    public void testSubmitRequestSuccess() throws SQLException {
        when(requestDao.hasAlreadyRequested(20, 100)).thenReturn(false);
        when(requestDao.addRequest(any(DonationRequest.class))).thenReturn(200);
        when(donationDao.getDonationById(100)).thenReturn(sampleDonation);

        int result = donationService.submitRequest(sampleRequest);

        assertEquals(200, result);
        verify(requestDao).addRequest(sampleRequest);
        // Verify notification is created for donor
        verify(notificationDao).createNotification(any(Notification.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testSubmitRequestDuplicate() throws SQLException {
        when(requestDao.hasAlreadyRequested(20, 100)).thenReturn(true);

        donationService.submitRequest(sampleRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitRequestInvalidQuantity() throws SQLException {
        sampleRequest.setQuantityRequested(0);
        donationService.submitRequest(sampleRequest);
    }

    @Test
    public void testApproveRequestSuccess() throws SQLException {
        when(requestDao.approveRequest(200, 10)).thenReturn(true);
        when(requestDao.getRequestById(200)).thenReturn(sampleRequest);
        when(donationDao.getDonationById(100)).thenReturn(sampleDonation);

        boolean success = donationService.approveRequest(200, 10);

        assertTrue(success);
        verify(requestDao).approveRequest(200, 10);
        verify(notificationDao).createNotification(any(Notification.class)); // NGO notified
        verify(donationDao).updateStatus(100, "APPROVED"); // Donation status updated
        verify(taskDao).createTask(any(VolunteerTask.class)); // Volunteer pickup task created
    }

    @Test
    public void testRejectRequestSuccess() throws SQLException {
        when(requestDao.rejectRequest(200, 10, "Out of stock")).thenReturn(true);
        when(requestDao.getRequestById(200)).thenReturn(sampleRequest);

        boolean success = donationService.rejectRequest(200, 10, "Out of stock");

        assertTrue(success);
        verify(requestDao).rejectRequest(200, 10, "Out of stock");
        verify(notificationDao).createNotification(any(Notification.class)); // NGO notified with reason
    }

    @Test
    public void testCancelRequest() throws SQLException {
        when(requestDao.cancelRequest(200, 20)).thenReturn(true);

        boolean success = donationService.cancelRequest(200, 20);

        assertTrue(success);
        verify(requestDao).cancelRequest(200, 20);
    }

    @Test
    public void testGetDashboardStats() throws SQLException {
        when(donationDao.countAll()).thenReturn(10);
        when(donationDao.countByStatus("AVAILABLE")).thenReturn(7);
        when(donationDao.countByStatus("DISTRIBUTED")).thenReturn(3);
        when(requestDao.countByStatus("PENDING")).thenReturn(4);
        when(requestDao.countByStatus("APPROVED")).thenReturn(2);
        when(requestDao.countByStatus("COLLECTED")).thenReturn(1);

        Map<String, Integer> stats = donationService.getDashboardStats();

        assertEquals(Integer.valueOf(10), stats.get("totalDonations"));
        assertEquals(Integer.valueOf(7), stats.get("availableDonations"));
        assertEquals(Integer.valueOf(3), stats.get("distributedDonations"));
        assertEquals(Integer.valueOf(4), stats.get("pendingRequests"));
        assertEquals(Integer.valueOf(2), stats.get("approvedRequests"));
        assertEquals(Integer.valueOf(1), stats.get("collectedRequests"));
    }
}
