<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Donation History - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body style="display: flex; flex-direction: column; min-height: 100vh;">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container">
        <div class="dashboard">
            <!-- Donor Sidebar -->
            <aside class="sidebar">
                <div class="sidebar-menu">
                    <a href="${pageContext.request.contextPath}/donor?action=dashboard">Dashboard Overview</a>
                    <a href="${pageContext.request.contextPath}/donor?action=addDonation">List New Food</a>
                    <a href="${pageContext.request.contextPath}/donor?action=manageDonations">Manage Donations</a>
                    <a href="${pageContext.request.contextPath}/donor?action=requests">Incoming Requests</a>
                    <a href="${pageContext.request.contextPath}/donor?action=donationHistory" class="active">History</a>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <div class="card">
                    <h2>Donation History</h2>
                    
                    <p style="margin-bottom: 1.5rem; color: #555;">A record of all your past and current food donations.</p>

                    <div class="table-container">
                        <c:choose>
                            <c:when test="${empty donations}">
                                <p>No history found.</p>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Date Listed</th>
                                            <th>Food Item</th>
                                            <th>Category</th>
                                            <th>Qty</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${donations}">
                                            <tr>
                                                <td>${item.createdAt.toLocalDate()}</td>
                                                <td>${item.foodName}</td>
                                                <td>${item.categoryName}</td>
                                                <td>${item.quantity} ${item.quantityUnit}</td>
                                                <td>
                                                    <span style="padding: 4px 8px; border-radius: 4px; font-size: 0.85em;
                                                        background-color: ${item.status == 'AVAILABLE' ? '#d5f4e6' : (item.status == 'DISTRIBUTED' ? '#e8f8f5' : '#fcf3cf')};
                                                        color: ${item.status == 'AVAILABLE' ? '#186a3b' : (item.status == 'DISTRIBUTED' ? '#117a65' : '#b7950b')};">
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
                </div>
            </main>
        </div>
    </div>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
