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
    <title>Admin Dashboard - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <div class="logo">🌱 EcoFeast - Admin</div>
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
                    <a href="#dashboard" class="nav-item active">📊 Dashboard</a>
                    <a href="#food-items" class="nav-item">🍎 Food Items</a>
                    <a href="#users" class="nav-item">👥 Users</a>
                    <a href="#requests" class="nav-item">📨 Requests</a>
                    <a href="#reports" class="nav-item">📈 Reports</a>
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

                <!-- Dashboard Section -->
                <section id="dashboard" class="card">
                    <h2>Dashboard Overview</h2>
                    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem; margin-top: 1.5rem;">
                        <div style="background: #d5f4e6; padding: 1.5rem; border-radius: 8px; text-align: center;">
                            <p style="font-size: 2rem; font-weight: bold; color: #186a3b;">42</p>
                            <p style="color: #186a3b;">Active Food Items</p>
                        </div>
                        <div style="background: #d6eaf8; padding: 1.5rem; border-radius: 8px; text-align: center;">
                            <p style="font-size: 2rem; font-weight: bold; color: #1a5276;">156</p>
                            <p style="color: #1a5276;">Total Users</p>
                        </div>
                        <div style="background: #fef5e7; padding: 1.5rem; border-radius: 8px; text-align: center;">
                            <p style="font-size: 2rem; font-weight: bold; color: #7d6608;">28</p>
                            <p style="color: #7d6608;">Pending Requests</p>
                        </div>
                        <div style="background: #fadbd8; padding: 1.5rem; border-radius: 8px; text-align: center;">
                            <p style="font-size: 2rem; font-weight: bold; color: #a93226;">312</p>
                            <p style="color: #a93226;">Total Items Distributed</p>
                        </div>
                    </div>
                </section>

                <!-- Food Items Section -->
                <section id="food-items" class="card">
                    <h2>Manage Food Items</h2>
                    <a class="btn btn-primary" style="margin-bottom: 1rem; margin-top: 1rem; display: inline-block;" href="${pageContext.request.contextPath}/admin/dashboard?action=addFood">Add New Food Item</a>
                    
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Item ID</th>
                                    <th>Item Name</th>
                                    <th>Category</th>
                                    <th>Quantity</th>
                                    <th>Expiry Date</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>#1001</td>
                                    <td>Fresh Apples</td>
                                    <td>Fruits</td>
                                    <td>50 kg</td>
                                    <td>2024-04-15</td>
                                    <td><span style="background: #d5f4e6; padding: 0.25rem 0.75rem; border-radius: 4px; color: #186a3b;">Available</span></td>
                                    <td><a href="#" style="color: #2ecc71; text-decoration: none;">Edit</a> | <a href="#" style="color: #e74c3c; text-decoration: none;">Delete</a></td>
                                </tr>
                                <tr>
                                    <td>#1002</td>
                                    <td>Bread Loaves</td>
                                    <td>Bakery</td>
                                    <td>30 units</td>
                                    <td>2024-03-25</td>
                                    <td><span style="background: #d5f4e6; padding: 0.25rem 0.75rem; border-radius: 4px; color: #186a3b;">Available</span></td>
                                    <td><a href="#" style="color: #2ecc71; text-decoration: none;">Edit</a> | <a href="#" style="color: #e74c3c; text-decoration: none;">Delete</a></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </section>

                <!-- Users Section -->
                <section id="users" class="card">
                    <h2>Manage Users</h2>
                    
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>User ID</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>#2001</td>
                                    <td>John Doe</td>
                                    <td>john@example.com</td>
                                    <td>USER</td>
                                    <td><span style="background: #d5f4e6; padding: 0.25rem 0.75rem; border-radius: 4px; color: #186a3b;">Active</span></td>
                                    <td><a href="#" style="color: #2ecc71; text-decoration: none;">Edit</a> | <a href="#" style="color: #e74c3c; text-decoration: none;">Deactivate</a></td>
                                </tr>
                                <tr>
                                    <td>#2002</td>
                                    <td>Jane Smith</td>
                                    <td>jane@example.com</td>
                                    <td>USER</td>
                                    <td><span style="background: #d5f4e6; padding: 0.25rem 0.75rem; border-radius: 4px; color: #186a3b;">Active</span></td>
                                    <td><a href="#" style="color: #2ecc71; text-decoration: none;">Edit</a> | <a href="#" style="color: #e74c3c; text-decoration: none;">Deactivate</a></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </section>

                <!-- Requests Section -->
                <section id="requests" class="card">
                    <h2>Food Distribution Requests</h2>
                    
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Request ID</th>
                                    <th>User</th>
                                    <th>Food Item</th>
                                    <th>Quantity</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>#3001</td>
                                    <td>John Doe</td>
                                    <td>Fresh Apples</td>
                                    <td>10 kg</td>
                                    <td><span style="background: #fef5e7; padding: 0.25rem 0.75rem; border-radius: 4px; color: #7d6608;">Pending</span></td>
                                    <td><a href="#" style="color: #2ecc71; text-decoration: none;">Approve</a> | <a href="#" style="color: #e74c3c; text-decoration: none;">Reject</a></td>
                                </tr>
                                <tr>
                                    <td>#3002</td>
                                    <td>Jane Smith</td>
                                    <td>Bread Loaves</td>
                                    <td>5 units</td>
                                    <td><span style="background: #d5f4e6; padding: 0.25rem 0.75rem; border-radius: 4px; color: #186a3b;">Approved</span></td>
                                    <td><a href="#" style="color: #3498db; text-decoration: none;">View</a></td>
                                </tr>
                            </tbody>
                        </table>
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
