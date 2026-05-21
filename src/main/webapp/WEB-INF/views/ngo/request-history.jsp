<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Requests - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body style="display: flex; flex-direction: column; min-height: 100vh;">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container">
        <div class="dashboard">
            <!-- NGO Sidebar -->
            <aside class="sidebar">
                <div class="sidebar-menu">
                    <a href="${pageContext.request.contextPath}/ngo?action=dashboard">Dashboard Overview</a>
                    <a href="${pageContext.request.contextPath}/ngo?action=browse">Browse Food</a>
                    <a href="${pageContext.request.contextPath}/ngo?action=requests" class="${param.action == 'requests' ? 'active' : ''}">My Requests</a>
                    <a href="${pageContext.request.contextPath}/ngo?action=requestHistory" class="${param.action == 'requestHistory' ? 'active' : ''}">History</a>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <div class="card">
                    <h2>${param.action == 'requestHistory' ? 'Request History' : 'My Active Requests'}</h2>
                    
                    <c:if test="${not empty sessionScope.successMsg}">
                        <div class="alert alert-success">${sessionScope.successMsg}</div>
                        <c:remove var="successMsg" scope="session"/>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-error">${error}</div>
                    </c:if>

                    <div class="table-container">
                        <c:choose>
                            <c:when test="${empty requests}">
                                <p>No requests found.</p>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Date</th>
                                            <th>Food Item</th>
                                            <th>Donor</th>
                                            <th>Qty</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="req" items="${requests}">
                                            <!-- Filter based on action (active vs history) if needed, for now showing all -->
                                            <tr>
                                                <td>${req.requestedAt.toLocalDate()}</td>
                                                <td>${req.foodName}</td>
                                                <td>${req.donorName}</td>
                                                <td>${req.quantityRequested}</td>
                                                <td>
                                                    <span style="padding: 4px 8px; border-radius: 4px; font-size: 0.85em;
                                                        background-color: ${req.status == 'APPROVED' ? '#d5f4e6' : (req.status == 'REJECTED' or req.status == 'CANCELLED' ? '#fadbd8' : (req.status == 'COLLECTED' ? '#e8f8f5' : '#fcf3cf'))};
                                                        color: ${req.status == 'APPROVED' ? '#186a3b' : (req.status == 'REJECTED' or req.status == 'CANCELLED' ? '#a93226' : (req.status == 'COLLECTED' ? '#117a65' : '#b7950b'))};">
                                                        ${req.status}
                                                    </span>
                                                </td>
                                                <td>
                                                    <c:if test="${req.status == 'PENDING'}">
                                                        <form action="${pageContext.request.contextPath}/ngo" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to cancel this request?');">
                                                            <input type="hidden" name="action" value="cancelRequest">
                                                            <input type="hidden" name="requestId" value="${req.requestId}">
                                                            <button type="submit" class="btn" style="padding: 4px 8px; font-size: 0.8rem; background: var(--error-color); color: white;">Cancel</button>
                                                        </form>
                                                    </c:if>
                                                    <c:if test="${req.status == 'REJECTED'}">
                                                        <small title="Reason: ${req.rejectionReason}">ℹ️ Info</small>
                                                    </c:if>
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
