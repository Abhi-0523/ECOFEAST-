<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Donations - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body style="display: flex; flex-direction: column; min-height: 100vh;">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container">
        <div class="dashboard">
            <!-- Admin Sidebar -->
            <aside class="sidebar">
                <div class="sidebar-menu">
                    <a href="${pageContext.request.contextPath}/admin?action=dashboard">Dashboard Overview</a>
                    <a href="${pageContext.request.contextPath}/admin?action=manageUsers">Manage Users</a>
                    <a href="${pageContext.request.contextPath}/admin?action=manageDonations" class="active">All Donations</a>
                    <a href="${pageContext.request.contextPath}/admin?action=manageTasks">All Tasks</a>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <div class="card">
                    <h2>All Food Donations</h2>

                    <div class="table-container">
                        <c:choose>
                            <c:when test="${empty donations}">
                                <p>No donations found.</p>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Item Name</th>
                                            <th>Donor</th>
                                            <th>Qty</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="d" items="${donations}">
                                            <tr>
                                                <td>${d.donationId}</td>
                                                <td>${d.foodName}</td>
                                                <td>${d.donorName}</td>
                                                <td>${d.quantity} ${d.quantityUnit}</td>
                                                <td>
                                                    <span style="padding: 4px 8px; border-radius: 4px; font-size: 0.85em;
                                                        background-color: ${d.status == 'AVAILABLE' ? '#d5f4e6' : (d.status == 'DISTRIBUTED' ? '#e8f8f5' : '#fcf3cf')};
                                                        color: ${d.status == 'AVAILABLE' ? '#186a3b' : (d.status == 'DISTRIBUTED' ? '#117a65' : '#b7950b')};">
                                                        ${d.status}
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
