package com.ecofeast.service;

import com.ecofeast.dao.*;
import com.ecofeast.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VolunteerServiceTest {

    @Mock
    private VolunteerTaskDao taskDao;
    @Mock
    private NotificationDao notificationDao;
    @Mock
    private DonationRequestDao requestDao;
    @Mock
    private FoodDonationDao donationDao;

    private VolunteerService volunteerService;

    private VolunteerTask sampleTask;
    private DonationRequest sampleRequest;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        volunteerService = new VolunteerService(taskDao, notificationDao, requestDao, donationDao);

        sampleTask = new VolunteerTask();
        sampleTask.setTaskId(500);
        sampleTask.setRequestId(200);
        sampleTask.setVolunteerId(30);
        sampleTask.setStatus("OPEN");
        sampleTask.setTaskType("PICKUP");

        sampleRequest = new DonationRequest();
        sampleRequest.setRequestId(200);
        sampleRequest.setDonationId(100);
    }

    @Test
    public void testGetOpenTasks() throws SQLException {
        List<VolunteerTask> list = new ArrayList<>();
        list.add(sampleTask);
        when(taskDao.getOpenTasks()).thenReturn(list);

        List<VolunteerTask> result = volunteerService.getOpenTasks();

        assertEquals(1, result.size());
        assertEquals(500, result.get(0).getTaskId());
    }

    @Test
    public void testGetMyTasks() throws SQLException {
        List<VolunteerTask> list = new ArrayList<>();
        list.add(sampleTask);
        when(taskDao.getTasksByVolunteer(30)).thenReturn(list);

        List<VolunteerTask> result = volunteerService.getMyTasks(30);

        assertEquals(1, result.size());
    }

    @Test
    public void testAcceptTaskSuccess() throws SQLException {
        when(taskDao.acceptTask(500, 30)).thenReturn(true);

        boolean success = volunteerService.acceptTask(500, 30);

        assertTrue(success);
        verify(taskDao).acceptTask(500, 30);
        verify(notificationDao).createNotification(any(Notification.class)); // Volunteer notified
    }

    @Test
    public void testStartTask() throws SQLException {
        when(taskDao.startTask(500, 30)).thenReturn(true);

        boolean success = volunteerService.startTask(500, 30);

        assertTrue(success);
        verify(taskDao).startTask(500, 30);
    }

    @Test
    public void testCompleteTaskSuccess() throws SQLException {
        when(taskDao.getTaskById(500)).thenReturn(sampleTask);
        when(taskDao.completeTask(500, 30)).thenReturn(true);
        when(requestDao.getRequestById(200)).thenReturn(sampleRequest);

        boolean success = volunteerService.completeTask(500, 30);

        assertTrue(success);
        verify(taskDao).completeTask(500, 30);
        verify(requestDao).markCollected(200); // Request marked collected
        verify(donationDao).updateStatus(100, "DISTRIBUTED"); // Donation marked distributed
        verify(notificationDao).createNotification(any(Notification.class)); // Volunteer thanked
    }

    @Test
    public void testGetAllTasks() throws SQLException {
        List<VolunteerTask> list = new ArrayList<>();
        list.add(sampleTask);
        when(taskDao.getAllTasks()).thenReturn(list);

        List<VolunteerTask> result = volunteerService.getAllTasks();

        assertEquals(1, result.size());
    }

    @Test
    public void testCreateTask() throws SQLException {
        when(taskDao.createTask(any(VolunteerTask.class))).thenReturn(500);

        int id = volunteerService.createTask(sampleTask);

        assertEquals(500, id);
        verify(taskDao).createTask(sampleTask);
    }

    @Test
    public void testGetTaskStats() throws SQLException {
        when(taskDao.countByStatus("OPEN")).thenReturn(5);
        when(taskDao.countByStatus("ACCEPTED")).thenReturn(3);
        when(taskDao.countByStatus("COMPLETED")).thenReturn(8);

        Map<String, Integer> stats = volunteerService.getTaskStats();

        assertEquals(Integer.valueOf(5), stats.get("openTasks"));
        assertEquals(Integer.valueOf(3), stats.get("acceptedTasks"));
        assertEquals(Integer.valueOf(8), stats.get("completedTasks"));
    }
}
