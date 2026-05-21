<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .stats-grid { display: flex; gap: 1rem; margin-bottom: 2rem; flex-wrap: wrap;}
        .stat-card { flex: 1; min-width: 200px; background: #fff; padding: 1.5rem; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); text-align: center;}
        .stat-card h3 { color: var(--text-color); font-size: 1rem; margin-bottom: 0.5rem;}
        .stat-card .number { font-size: 2rem; font-weight: bold; color: var(--primary-color);}
    </style>
</head>
<body class="db-container">
    <!-- Sidebar -->
    <aside class="db-sidebar">
        <div class="db-sidebar-brand">
            🌱 ECOFEAST
        </div>

        <nav class="db-menu-section">
            <div class="db-menu-label">Menu</div>
            <a href="${pageContext.request.contextPath}/admin?action=dashboard" class="db-menu-item active">
                <span>Overview</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin?action=manageUsers" class="db-menu-item">
                <span>Manage Users</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin?action=manageDonations" class="db-menu-item">
                <span>Transactions</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin?action=manageTasks" class="db-menu-item">
                <span>Tasks</span>
            </a>
        </nav>

        <nav class="db-menu-section">
            <div class="db-menu-label">General</div>
            <a href="${pageContext.request.contextPath}/profile" class="db-menu-item">Profile</a>
            <a href="${pageContext.request.contextPath}/logout" class="db-menu-item">Logout</a>
        </nav>

        <div class="db-sidebar-footer">
            <div class="db-user-profile">
                <div class="db-user-avatar">
                    ${sessionScope.loggedInUser.fullName.substring(0,1)}
                </div>
                <div class="db-user-info">
                    <h4>${sessionScope.loggedInUser.fullName}</h4>
                    <p>System Admin</p>
                </div>
            </div>
        </div>
    </aside>

    <!-- Main Content -->
    <main class="db-main">
        <header class="db-header">
            <div class="db-header-title">
                <p>Welcome back, Admin</p>
                <h1>Dashboard Overview</h1>
            </div>
            <div class="db-header-actions">
                <button class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/admin?action=manageUsers'">View Pending Users</button>
            </div>
        </header>

        <c:if test="${not empty error}">
            <div class="alert alert-error" style="margin-bottom: 2rem;">${error}</div>
        </c:if>

        <div class="db-grid">
            <div class="db-stat-card">
                <span class="label">Total Donations</span>
                <div class="value">${stats.totalDonations != null ? stats.totalDonations : 0}</div>
                <span class="trend">↑ 12% from last month</span>
            </div>
            <div class="db-stat-card">
                <span class="label">Active Items</span>
                <div class="value">${stats.availableDonations != null ? stats.availableDonations : 0}</div>
                <span class="trend">↑ 8% growth</span>
            </div>
            <div class="db-stat-card" style="background: #0d1b11; color: white;">
                <span class="label" style="color: rgba(255,255,255,0.6)">Pending Requests</span>
                <div class="value" style="color: white;">${stats.pendingRequests != null ? stats.pendingRequests : 0}</div>
                <span class="trend" style="color: var(--accent-color)">Action required</span>
            </div>
            <div class="db-stat-card">
                <span class="label">Active Tasks</span>
                <div class="value">${taskStats.acceptedTasks != null ? taskStats.acceptedTasks : 0}</div>
                <span class="trend">In progress</span>
            </div>
        </div>

        <div class="db-card-large">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                <h3 style="font-weight: 800; font-size: 1.2rem;">Quick Summary</h3>
                <a href="${pageContext.request.contextPath}/admin?action=manageDonations" style="color: var(--accent-color); font-size: 0.9rem; font-weight: 600; text-decoration: none;">See all transactions ></a>
            </div>
            <p style="color: #666; font-size: 0.95rem;">
                System performance is stable. <strong>${stats.pendingRequests}</strong> requests are waiting for manual verification. 
                Average response time is down by 15% this week.
            </p>
        </div>
    </main>
</body>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
