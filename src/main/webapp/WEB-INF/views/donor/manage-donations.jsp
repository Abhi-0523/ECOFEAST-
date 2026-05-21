<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Donations - EcoFeast</title>
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
                    <a href="${pageContext.request.contextPath}/donor?action=manageDonations" class="${param.action == 'manageDonations' ? 'active' : ''}">Manage Donations</a>
                    <a href="${pageContext.request.contextPath}/donor?action=requests" class="${param.action == 'requests' ? 'active' : ''}">Incoming Requests</a>
                    <a href="${pageContext.request.contextPath}/donor?action=donationHistory">History</a>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <div class="card">
                    
                    <c:choose>
                        <c:when test="${param.action == 'requests'}">
                            <h2>Incoming Food Requests</h2>
                            
                            <c:if test="${not empty success}">
                                <div class="alert alert-success">${success}</div>
                            </c:if>
                            <c:if test="${not empty error}">
                                <div class="alert alert-error">${error}</div>
                            </c:if>

                            <div class="table-container">
                                <c:choose>
                                    <c:when test="${empty requests}">
                                        <p>No incoming requests at the moment.</p>
                                    </c:when>
                                    <c:otherwise>
                                        <table>
                                            <thead>
                                                <tr>
                                                    <th>Date</th>
                                                    <th>Food Item</th>
                                                    <th>NGO</th>
                                                    <th>Qty</th>
                                                    <th>Message</th>
                                                    <th>Status</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="req" items="${requests}">
                                                    <tr>
                                                        <td>${req.requestedAt.toLocalDate()}</td>
                                                        <td>${req.foodName}</td>
                                                        <td>${req.ngoName}</td>
                                                        <td>${req.quantityRequested}</td>
                                                        <td title="${req.requestMessage}">${not empty req.requestMessage ? 'Yes' : 'No'}</td>
                                                        <td>
                                                            <span style="padding: 4px 8px; border-radius: 4px; font-size: 0.85em;
                                                                background-color: ${req.status == 'APPROVED' ? '#d5f4e6' : (req.status == 'REJECTED' ? '#fadbd8' : '#fcf3cf')};
                                                                color: ${req.status == 'APPROVED' ? '#186a3b' : (req.status == 'REJECTED' ? '#a93226' : '#b7950b')};">
                                                                ${req.status}
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <c:if test="${req.status == 'PENDING'}">
                                                                <div style="display: flex; gap: 5px;">
                                                                    <form action="${pageContext.request.contextPath}/donor" method="post">
                                                                        <input type="hidden" name="action" value="approveRequest">
                                                                        <input type="hidden" name="requestId" value="${req.requestId}">
                                                                        <button type="submit" class="btn" style="padding: 4px 8px; font-size: 0.8rem; background: var(--success-color); color: white;">Approve</button>
                                                                    </form>
                                                                    <form action="${pageContext.request.contextPath}/donor" method="post" onsubmit="return prompt('Reason for rejection:') != null;">
                                                                        <input type="hidden" name="action" value="rejectRequest">
                                                                        <input type="hidden" name="requestId" value="${req.requestId}">
                                                                        <!-- simplified prompt handling for ui, usually better handled with a modal -->
                                                                        <input type="hidden" name="reason" value="Item unavailable or issue with request" id="reason_${req.requestId}">
                                                                        <button type="submit" class="btn" style="padding: 4px 8px; font-size: 0.8rem; background: var(--error-color); color: white;" onclick="let r = prompt('Reason?'); if(r) { document.getElementById('reason_${req.requestId}').value = r; return true; } return false;">Reject</button>
                                                                    </form>
                                                                </div>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:when>
                        
                        <c:otherwise>
                            <!-- Manage Donations View -->
                            <h2>Manage Your Donations</h2>
                            
                            <c:if test="${not empty sessionScope.successMsg}">
                                <div class="alert alert-success">${sessionScope.successMsg}</div>
                                <c:remove var="successMsg" scope="session"/>
                            </c:if>
                            <c:if test="${not empty success}">
                                <div class="alert alert-success">${success}</div>
                            </c:if>
                            <c:if test="${not empty error}">
                                <div class="alert alert-error">${error}</div>
                            </c:if>

                            <div class="table-container">
                                <c:choose>
                                    <c:when test="${empty donations}">
                                        <p>No donations found.</p>
                                    </c:when>
                                    <c:otherwise>
                                        <table>
                                            <thead>
                                                <tr>
                                                    <th>Item</th>
                                                    <th>Category</th>
                                                    <th>Qty</th>
                                                    <th>Expires</th>
                                                    <th>Status</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="item" items="${donations}">
                                                    <tr>
                                                        <td>${item.foodName}</td>
                                                        <td>${item.categoryName}</td>
                                                        <td>${item.quantity} ${item.quantityUnit}</td>
                                                        <td>${item.expiryTime.toLocalDate()}</td>
                                                        <td>
                                                            <span style="padding: 4px 8px; border-radius: 4px; font-size: 0.85em;
                                                                background-color: ${item.status == 'AVAILABLE' ? '#d5f4e6' : (item.status == 'DISTRIBUTED' ? '#e8f8f5' : '#fcf3cf')};
                                                                color: ${item.status == 'AVAILABLE' ? '#186a3b' : (item.status == 'DISTRIBUTED' ? '#117a65' : '#b7950b')};">
                                                                ${item.status}
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <c:if test="${item.status == 'AVAILABLE'}">
                                                                <a href="${pageContext.request.contextPath}/donor?action=editDonation&id=${item.donationId}" style="margin-right: 10px; color: #3498db; text-decoration: none;">Edit</a>
                                                                <form action="${pageContext.request.contextPath}/donor" method="post" style="display:inline;" onsubmit="return confirm('Delete this donation?');">
                                                                    <input type="hidden" name="action" value="deleteDonation">
                                                                    <input type="hidden" name="donationId" value="${item.donationId}">
                                                                    <button type="submit" style="background: none; border: none; color: var(--error-color); cursor: pointer; padding: 0;">Delete</button>
                                                                </form>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:otherwise>
                    </c:choose>

                </div>
            </main>
        </div>
    </div>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
