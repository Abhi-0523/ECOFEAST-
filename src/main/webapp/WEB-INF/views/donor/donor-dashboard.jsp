<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Donor Dashboard - EcoFeast</title>
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
            <a href="${pageContext.request.contextPath}/donor?action=dashboard" class="db-menu-item active">
                <span>Overview</span>
            </a>
            <a href="${pageContext.request.contextPath}/donor?action=addDonation" class="db-menu-item">
                <span>List New Food</span>
            </a>
            <a href="${pageContext.request.contextPath}/donor?action=manageDonations" class="db-menu-item">
                <span>Manage Inventory</span>
            </a>
            <a href="${pageContext.request.contextPath}/donor?action=requests" class="db-menu-item">
                <span>Requests</span>
            </a>
        </nav>

        <nav class="db-menu-section">
            <div class="db-menu-label">General</div>
            <a href="${pageContext.request.contextPath}/donor?action=donationHistory" class="db-menu-item">History</a>
            <a href="${pageContext.request.contextPath}/profile" class="db-menu-item">Profile</a>
            <a href="${pageContext.request.contextPath}/logout" class="db-menu-item">Logout</a>
        </nav>

        <div class="db-sidebar-footer">
            <div class="db-user-profile">
                <div class="db-user-avatar" style="background: #e67e22;">
                    ${sessionScope.loggedInUser.fullName.substring(0,1)}
                </div>
                <div class="db-user-info">
                    <h4>${sessionScope.loggedInUser.fullName}</h4>
                    <p>Food Donor</p>
                </div>
            </div>
        </div>
    </aside>

    <!-- Main Content -->
    <main class="db-main">
        <header class="db-header">
            <div class="db-header-title">
                <p>Helping communities thrive</p>
                <h1>Donor Portal</h1>
            </div>
            <div class="db-header-actions">
                <button class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/donor?action=addDonation'">+ List Surplus Food</button>
            </div>
        </header>

        <div class="db-grid">
            <div class="db-stat-card">
                <span class="label">Total Contributions</span>
                <div class="value">${totalDonations}</div>
                <span class="trend">Items donated</span>
            </div>
            <div class="db-stat-card">
                <span class="label">Currently Available</span>
                <div class="value">${availableDonations}</div>
                <span class="trend" style="color: #e67e22;">Waiting for pickup</span>
            </div>
            <div class="db-stat-card">
                <span class="label">Success Rate</span>
                <div class="value">${distributedDonations}</div>
                <span class="trend">Items distributed</span>
            </div>
        </div>

        <div class="db-card-large">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                <h3 style="font-weight: 800; font-size: 1.2rem;">Recent Donations</h3>
                <a href="${pageContext.request.contextPath}/donor?action=manageDonations" style="color: var(--accent-color); font-size: 0.9rem; font-weight: 600; text-decoration: none;">View All</a>
            </div>
            
            <c:choose>
                <c:when test="${empty donations}">
                    <div style="text-align: center; padding: 3rem; color: #888;">
                        <p>No active donations found. Start by listing your surplus food.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="db-table">
                        <thead>
                            <tr>
                                <th>Food Item</th>
                                <th>Category</th>
                                <th>Quantity</th>
                                <th>Expiry</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${donations}" end="4">
                                <tr>
                                    <td style="font-weight: 600;">${item.foodName}</td>
                                    <td>${item.categoryName}</td>
                                    <td>${item.quantity} ${item.quantityUnit}</td>
                                    <td>${item.expiryTime.toLocalDate()}</td>
                                    <td>
                                        <span class="status-badge ${item.status == 'AVAILABLE' ? 'status-pending' : 'status-completed'}">
                                            ${item.status}
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</body>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
