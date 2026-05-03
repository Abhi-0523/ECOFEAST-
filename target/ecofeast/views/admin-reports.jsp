<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ecofeast.models.User" %>
<%@ page import="java.util.Map" %>
<%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    Map<String, Object> reports = (Map<String, Object>) request.getAttribute("reports");
    Map<String, Integer> popularItems = reports != null ? (Map<String, Integer>) reports.get("popularItems") : null;
    Map<String, Integer> availabilityStatus = reports != null ? (Map<String, Integer>) reports.get("availabilityStatus") : null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reports - Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .report-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 2rem;
            margin-top: 1.5rem;
        }
        .report-card {
            background: #fff;
            padding: 1.5rem;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
            border: 1px solid #eee;
        }
        .report-item {
            display: flex;
            justify-content: space-between;
            padding: 0.75rem 0;
            border-bottom: 1px solid #f0f0f0;
        }
        .report-item:last-child {
            border-bottom: none;
        }
        .badge {
            background: #eaf2f8;
            color: #2980b9;
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            font-weight: bold;
        }
    </style>
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
                    <a href="${pageContext.request.contextPath}/admin/dashboard?action=pendingUsers" class="nav-item">👥 Pending Users</a>
                    <a href="${pageContext.request.contextPath}/admin/dashboard?action=reports" class="nav-item active">📈 Reports</a>
                    <a href="${pageContext.request.contextPath}/admin/dashboard?action=backupDatabase" class="nav-item" style="color: #d35400;">💾 Backup DB</a>
                </nav>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <section class="card">
                    <h2>System Analytics & Reports</h2>
                    <p>Analysis of food availability and popular requested items.</p>
                    
                    <div class="report-grid">
                        <div class="report-card">
                            <h3>Most Requested Items</h3>
                            <div style="margin-top: 1rem;">
                                <% if (popularItems != null && !popularItems.isEmpty()) { 
                                    for(Map.Entry<String, Integer> entry : popularItems.entrySet()) { %>
                                    <div class="report-item">
                                        <span><%= entry.getKey() %></span>
                                        <span class="badge"><%= entry.getValue() %> requests</span>
                                    </div>
                                <% } } else { %>
                                    <p>No request data available yet.</p>
                                <% } %>
                            </div>
                        </div>

                        <div class="report-card">
                            <h3>Inventory Status</h3>
                            <div style="margin-top: 1rem;">
                                <% if (availabilityStatus != null && !availabilityStatus.isEmpty()) { 
                                    for(Map.Entry<String, Integer> entry : availabilityStatus.entrySet()) { %>
                                    <div class="report-item">
                                        <span><%= entry.getKey() %></span>
                                        <span class="badge" style="background: #e8f8f5; color: #117a65;"><%= entry.getValue() %> items</span>
                                    </div>
                                <% } } else { %>
                                    <p>No inventory data available yet.</p>
                                <% } %>
                            </div>
                        </div>
                    </div>
                </section>
            </main>
        </div>
    </div>
</body>
</html>
