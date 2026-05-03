<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ecofeast.models.User" %>
<%@ page import="java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<User> pendingUsers = (List<User>) request.getAttribute("pendingUsers");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pending Users - Admin Dashboard</title>
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
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-item">📊 Dashboard</a>
                    <a href="${pageContext.request.contextPath}/admin/dashboard?action=pendingUsers" class="nav-item active">👥 Pending Users</a>
                    <a href="${pageContext.request.contextPath}/admin/dashboard?action=reports" class="nav-item">📈 Reports</a>
                </nav>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
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

                <section class="card">
                    <h2>Pending User Approvals</h2>
                    <p>Approve users to grant them access to the user portal.</p>
                    
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>User ID</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Phone</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (pendingUsers != null && !pendingUsers.isEmpty()) { 
                                    for(User u : pendingUsers) { %>
                                <tr>
                                    <td>#<%= u.getUserId() %></td>
                                    <td><%= u.getFullName() %></td>
                                    <td><%= u.getEmail() %></td>
                                    <td><%= u.getPhoneNumber() %></td>
                                    <td>
                                        <form method="POST" action="${pageContext.request.contextPath}/admin/dashboard" style="display:inline;">
                                            <input type="hidden" name="action" value="approveUser">
                                            <input type="hidden" name="userId" value="<%= u.getUserId() %>">
                                            <button type="submit" class="btn btn-primary" style="padding:0.25rem 0.5rem; font-size:0.9rem;">Approve</button>
                                        </form>
                                    </td>
                                </tr>
                                <% } } else { %>
                                <tr>
                                    <td colspan="5" style="text-align: center;">No pending users to approve.</td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                </section>
            </main>
        </div>
    </div>
</body>
</html>
