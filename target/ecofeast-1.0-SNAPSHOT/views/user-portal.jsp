<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ecofeast.models.User" %>
<%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Portal - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <div class="logo">🌱 EcoFeast - User Portal</div>
            <ul class="nav-links">
                <li><span style="color: white;">Welcome, <%= currentUser.getFirstName() %></span></li>
                <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
            </ul>
        </div>
    </nav>

    <div class="container">
        <div class="dashboard">
            <!-- Sidebar Navigation -->
            <aside class="sidebar">
                <nav class="sidebar-menu">
                    <a href="#available-items" class="nav-item active">🍎 Available Items</a>
                    <a href="#my-requests" class="nav-item">📨 My Requests</a>
                    <a href="#profile" class="nav-item">👤 My Profile</a>
                </nav>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <!-- Error/Success Messages -->
                <% 
                    String error = (String) request.getAttribute("error");
                    String success = (String) request.getAttribute("success");
                %>
                <% if (error != null) { %>
                    <div class="alert alert-error">
                        <strong>Error:</strong> <%= error %>
                    </div>
                <% } %>
                <% if (success != null) { %>
                    <div class="alert alert-success">
                        <strong>Success:</strong> <%= success %>
                    </div>
                <% } %>

                <!-- Available Items Section -->
                <section id="available-items" class="card">
                    <h2>Browse Available Food Items</h2>
                    <div style="margin-bottom: 1rem;">
                        <input type="text" placeholder="Search food items..." style="width: 100%; padding: 0.75rem; border: 1px solid #bdc3c7; border-radius: 4px; margin-bottom: 1rem;">
                    </div>

                    <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 2rem;">
                        <!-- Food Item Card 1 -->
                        <div style="border: 1px solid #bdc3c7; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);">
                            <div style="background: #d5f4e6; height: 150px; display: flex; align-items: center; justify-content: center; font-size: 3rem;">🍎</div>
                            <div style="padding: 1.5rem;">
                                <h3 style="color: #2ecc71; margin-bottom: 0.5rem;">Fresh Apples</h3>
                                <p style="margin-bottom: 0.5rem;"><strong>Category:</strong> Fruits</p>
                                <p style="margin-bottom: 0.5rem;"><strong>Available:</strong> 50 kg</p>
                                <p style="margin-bottom: 0.5rem;"><strong>Expiry:</strong> 2024-04-15</p>
                                <p style="margin-bottom: 1rem; color: #7f8c8d; font-size: 0.9rem;">Fresh, ripe apples from local orchard. Perfect for daily consumption.</p>
                                <button class="btn btn-primary" style="width: 100%; padding: 0.5rem;">Request This Item</button>
                            </div>
                        </div>

                        <!-- Food Item Card 2 -->
                        <div style="border: 1px solid #bdc3c7; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);">
                            <div style="background: #fef5e7; height: 150px; display: flex; align-items: center; justify-content: center; font-size: 3rem;">🍞</div>
                            <div style="padding: 1.5rem;">
                                <h3 style="color: #2ecc71; margin-bottom: 0.5rem;">Bread Loaves</h3>
                                <p style="margin-bottom: 0.5rem;"><strong>Category:</strong> Bakery</p>
                                <p style="margin-bottom: 0.5rem;"><strong>Available:</strong> 30 units</p>
                                <p style="margin-bottom: 0.5rem;"><strong>Expiry:</strong> 2024-03-25</p>
                                <p style="margin-bottom: 1rem; color: #7f8c8d; font-size: 0.9rem;">Freshly baked whole wheat bread loaves. Nutritious and delicious.</p>
                                <button class="btn btn-primary" style="width: 100%; padding: 0.5rem;">Request This Item</button>
                            </div>
                        </div>

                        <!-- Food Item Card 3 -->
                        <div style="border: 1px solid #bdc3c7; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);">
                            <div style="background: #d6eaf8; height: 150px; display: flex; align-items: center; justify-content: center; font-size: 3rem;">🥬</div>
                            <div style="padding: 1.5rem;">
                                <h3 style="color: #2ecc71; margin-bottom: 0.5rem;">Fresh Vegetables</h3>
                                <p style="margin-bottom: 0.5rem;"><strong>Category:</strong> Vegetables</p>
                                <p style="margin-bottom: 0.5rem;"><strong>Available:</strong> 25 kg</p>
                                <p style="margin-bottom: 0.5rem;"><strong>Expiry:</strong> 2024-03-28</p>
                                <p style="margin-bottom: 1rem; color: #7f8c8d; font-size: 0.9rem;">Assorted fresh vegetables including lettuce, tomatoes, and carrots.</p>
                                <button class="btn btn-primary" style="width: 100%; padding: 0.5rem;">Request This Item</button>
                            </div>
                        </div>
                    </div>
                </section>

                <!-- My Requests Section -->
                <section id="my-requests" class="card">
                    <h2>My Food Requests</h2>
                    
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Request ID</th>
                                    <th>Food Item</th>
                                    <th>Quantity Requested</th>
                                    <th>Request Date</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>#3001</td>
                                    <td>Fresh Apples</td>
                                    <td>10 kg</td>
                                    <td>2024-03-20</td>
                                    <td><span style="background: #fef5e7; padding: 0.25rem 0.75rem; border-radius: 4px; color: #7d6608;">Pending</span></td>
                                    <td><a href="#" style="color: #3498db; text-decoration: none;">View Details</a> | <a href="#" style="color: #e74c3c; text-decoration: none;">Cancel</a></td>
                                </tr>
                                <tr>
                                    <td>#3000</td>
                                    <td>Bread Loaves</td>
                                    <td>5 units</td>
                                    <td>2024-03-18</td>
                                    <td><span style="background: #d5f4e6; padding: 0.25rem 0.75rem; border-radius: 4px; color: #186a3b;">Approved</span></td>
                                    <td><a href="#" style="color: #3498db; text-decoration: none;">View Details</a> | <a href="#" style="color: #2ecc71; text-decoration: none;">Mark as Collected</a></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </section>

                <!-- Profile Section -->
                <section id="profile" class="card">
                    <h2>My Profile</h2>
                    
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; margin-top: 1.5rem;">
                        <div>
                            <h3 style="color: #2ecc71; margin-bottom: 1rem;">Personal Information</h3>
                            <p><strong>Name:</strong> <%= currentUser.getFullName() %></p>
                            <p><strong>Email:</strong> <%= currentUser.getEmail() %></p>
                            <p><strong>Phone:</strong> <%= currentUser.getPhoneNumber() %></p>
                            <p><strong>Role:</strong> <%= currentUser.getRole() %></p>
                        </div>
                        <div>
                            <h3 style="color: #2ecc71; margin-bottom: 1rem;">Account Statistics</h3>
                            <p><strong>Total Requests:</strong> 2</p>
                            <p><strong>Approved Requests:</strong> 1</p>
                            <p><strong>Items Collected:</strong> 8 units</p>
                            <p><strong>Member Since:</strong> 2024-01-15</p>
                        </div>
                    </div>

                    <div style="margin-top: 2rem; border-top: 1px solid #bdc3c7; padding-top: 1.5rem;">
                        <h3 style="color: #2ecc71; margin-bottom: 1rem;">Update Password</h3>
                        <form method="post" action="${pageContext.request.contextPath}/user/dashboard" style="max-width: 500px;">
                            <input type="hidden" name="action" value="changePassword">
                            <div class="form-group">
                                <label for="oldPassword">Current Password:</label>
                                <input type="password" id="oldPassword" name="oldPassword" required>
                            </div>
                            <div class="form-group">
                                <label for="newPassword">New Password:</label>
                                <input type="password" id="newPassword" name="newPassword" required minlength="8">
                            </div>
                            <div class="form-group">
                                <label for="confirmPassword">Confirm New Password:</label>
                                <input type="password" id="confirmPassword" name="confirmPassword" required minlength="8">
                            </div>
                            <button type="submit" class="btn btn-primary">Update Password</button>
                        </form>
                    </div>
                </section>
            </main>
        </div>
    </div>

    <footer>
        <p>&copy; 2024 EcoFeast - Sustainable Food Redistribution System. All rights reserved.</p>
    </footer>
</body>
</html>
