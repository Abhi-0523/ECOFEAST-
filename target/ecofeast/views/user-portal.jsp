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
                    <a href="${pageContext.request.contextPath}/user/dashboard?action=viewWishlist" class="nav-item" style="color: #8e44ad;">🛒 My Wishlist</a>
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
                        <%@ page import="java.sql.ResultSet" %>
                        <% 
                           ResultSet foodItems = (ResultSet) request.getAttribute("foodItems");
                           if (foodItems != null) {
                               while(foodItems.next()) {
                        %>
                        <div style="border: 1px solid #bdc3c7; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);">
                            <div style="background: #d5f4e6; height: 150px; display: flex; align-items: center; justify-content: center; font-size: 3rem;">🍎</div>
                            <div style="padding: 1.5rem;">
                                <h3 style="color: #2ecc71; margin-bottom: 0.5rem;"><%= foodItems.getString("item_name") %></h3>
                                <p style="margin-bottom: 0.5rem;"><strong>Category:</strong> <%= foodItems.getString("category") %></p>
                                <p style="margin-bottom: 0.5rem;"><strong>Available:</strong> <%= foodItems.getInt("quantity") %></p>
                                <p style="margin-bottom: 0.5rem;"><strong>Expiry:</strong> <%= foodItems.getString("expiry_date") %></p>
                                <p style="margin-bottom: 1rem; color: #7f8c8d; font-size: 0.9rem;"><%= foodItems.getString("description") %></p>
                                <form method="post" action="${pageContext.request.contextPath}/user/dashboard" style="display:inline-block; width: 48%;">
                                    <input type="hidden" name="action" value="requestFood">
                                    <input type="hidden" name="itemId" value="<%= foodItems.getInt("item_id") %>">
                                    <button type="submit" class="btn btn-primary" style="width: 100%; padding: 0.5rem;">Request</button>
                                </form>
                                <form method="post" action="${pageContext.request.contextPath}/user/dashboard" style="display:inline-block; width: 48%;">
                                    <input type="hidden" name="action" value="addToWishlist">
                                    <input type="hidden" name="itemId" value="<%= foodItems.getInt("item_id") %>">
                                    <button type="submit" class="btn btn-secondary" style="width: 100%; padding: 0.5rem;">Wishlist</button>
                                </form>
                            </div>
                        </div>
                        <% } } else { %>
                            <p>No food items available at the moment. Check back later!</p>
                        <% } %>
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
                            <h3 style="color: #2ecc71; margin-bottom: 1rem;">Update Profile Information</h3>
                            <form method="post" action="${pageContext.request.contextPath}/user/dashboard" style="max-width: 500px;">
                                <input type="hidden" name="action" value="updateProfile">
                                <div class="form-group">
                                    <label for="firstName">First Name:</label>
                                    <input type="text" id="firstName" name="firstName" value="<%= currentUser.getFirstName() %>" required>
                                </div>
                                <div class="form-group">
                                    <label for="lastName">Last Name:</label>
                                    <input type="text" id="lastName" name="lastName" value="<%= currentUser.getLastName() %>" required>
                                </div>
                                <div class="form-group">
                                    <label for="phoneNumber">Phone Number:</label>
                                    <input type="text" id="phoneNumber" name="phoneNumber" value="<%= currentUser.getPhoneNumber() %>">
                                </div>
                                <button type="submit" class="btn btn-primary">Update Profile</button>
                            </form>
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
