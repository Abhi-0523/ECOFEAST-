<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .stats-grid { display: flex; gap: 1rem; margin-bottom: 2rem; flex-wrap: wrap;}
        .stat-card { flex: 1; min-width: 200px; background: #fff; padding: 1.5rem; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); text-align: center;}
        .stat-card h3 { color: var(--text-color); font-size: 1rem; margin-bottom: 0.5rem;}
        .stat-card .number { font-size: 2rem; font-weight: bold; color: var(--primary-color);}
    </style>
</head>
<body style="display: flex; flex-direction: column; min-height: 100vh;">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container">
        <div class="dashboard">
            <!-- NGO Sidebar -->
            <aside class="sidebar">
                <div class="sidebar-menu">
                    <a href="${pageContext.request.contextPath}/ngo?action=dashboard" class="active">Dashboard Overview</a>
                    <a href="${pageContext.request.contextPath}/ngo?action=browse">Browse Food</a>
                    <a href="${pageContext.request.contextPath}/ngo?action=requests">My Requests</a>
                    <a href="${pageContext.request.contextPath}/ngo?action=requestHistory">History</a>
                    <a href="${pageContext.request.contextPath}/profile">Profile</a>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <div class="card">
                    <h2>Welcome, ${sessionScope.loggedInUser.fullName} (NGO)</h2>
                    
                    <div class="stats-grid">
                        <div class="stat-card">
                            <h3>Total Requests</h3>
                            <div class="number">${totalRequests}</div>
                        </div>
                        <div class="stat-card">
                            <h3>Pending</h3>
                            <div class="number">${pendingRequests}</div>
                        </div>
                        <div class="stat-card">
                            <h3>Approved</h3>
                            <div class="number">${approvedRequests}</div>
                        </div>
                        <div class="stat-card">
                            <h3>Collected</h3>
                            <div class="number">${collectedCount}</div>
                        </div>
                    </div>

                    <h3>Recent Requests</h3>
                    <div class="table-container">
                        <c:choose>
                            <c:when test="${empty recentRequests}">
                                <p>You haven't made any requests yet. <a href="${pageContext.request.contextPath}/ngo?action=browse">Browse available food</a>.</p>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Food Item</th>
                                            <th>Donor</th>
                                            <th>Qty Requested</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="req" items="${recentRequests}">
                                            <tr>
                                                <td>${req.foodName}</td>
                                                <td>${req.donorName}</td>
                                                <td>${req.quantityRequested}</td>
                                                <td>
                                                    <span style="padding: 4px 8px; border-radius: 4px; font-size: 0.85em;
                                                        background-color: ${req.status == 'APPROVED' ? '#d5f4e6' : (req.status == 'REJECTED' ? '#fadbd8' : '#fcf3cf')};
                                                        color: ${req.status == 'APPROVED' ? '#186a3b' : (req.status == 'REJECTED' ? '#a93226' : '#b7950b')};">
                                                        ${req.status}
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
