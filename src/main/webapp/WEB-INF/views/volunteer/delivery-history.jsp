<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Delivery History - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body style="display: flex; flex-direction: column; min-height: 100vh;">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container">
        <div class="dashboard">
            <!-- Volunteer Sidebar -->
            <aside class="sidebar">
                <div class="sidebar-menu">
                    <a href="${pageContext.request.contextPath}/volunteer?action=dashboard">Dashboard Overview</a>
                    <a href="${pageContext.request.contextPath}/volunteer?action=pickupTasks">Find Pickup Tasks</a>
                    <a href="${pageContext.request.contextPath}/volunteer?action=myTasks">My Active Tasks</a>
                    <a href="${pageContext.request.contextPath}/volunteer?action=deliveryHistory" class="active">Delivery History</a>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <div class="card">
                    <h2>Delivery History</h2>
                    
                    <p style="margin-bottom: 1.5rem; color: #555;">Record of your completed deliveries.</p>

                    <c:if test="${not empty sessionScope.successMsg}">
                        <div class="alert alert-success">${sessionScope.successMsg}</div>
                        <c:remove var="successMsg" scope="session"/>
                    </c:if>

                    <div class="table-container">
                        <c:choose>
                            <c:when test="${empty tasks}">
                                <p>No completed deliveries found.</p>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Date Completed</th>
                                            <th>Food Item</th>
                                            <th>Delivered To</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="task" items="${tasks}">
                                            <c:if test="${task.status == 'COMPLETED'}">
                                                <tr>
                                                    <td>${not empty task.completedAt ? task.completedAt.toLocalDate() : ''}</td>
                                                    <td>${task.foodName}</td>
                                                    <td>${task.ngoName}</td>
                                                    <td>
                                                        <span style="padding: 4px 8px; border-radius: 4px; font-size: 0.85em; background-color: #e8f8f5; color: #117a65;">
                                                            ${task.status}
                                                        </span>
                                                    </td>
                                                </tr>
                                            </c:if>
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
