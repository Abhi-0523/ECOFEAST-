<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volunteer Dashboard - EcoFeast</title>
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
            <!-- Volunteer Sidebar -->
            <aside class="sidebar">
                <div class="sidebar-menu">
                    <a href="${pageContext.request.contextPath}/volunteer?action=dashboard" class="active">Dashboard Overview</a>
                    <a href="${pageContext.request.contextPath}/volunteer?action=pickupTasks">Find Pickup Tasks</a>
                    <a href="${pageContext.request.contextPath}/volunteer?action=myTasks">My Active Tasks</a>
                    <a href="${pageContext.request.contextPath}/volunteer?action=deliveryHistory">Delivery History</a>
                    <a href="${pageContext.request.contextPath}/profile">Profile</a>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <div class="card">
                    <h2>Welcome, ${sessionScope.loggedInUser.fullName} (Volunteer)</h2>
                    
                    <div class="stats-grid">
                        <div class="stat-card">
                            <h3>Available Tasks</h3>
                            <div class="number">${openTaskCount}</div>
                        </div>
                        <div class="stat-card">
                            <h3>My Active Tasks</h3>
                            <div class="number">${myActiveTaskCount}</div>
                        </div>
                        <div class="stat-card">
                            <h3>Completed Deliveries</h3>
                            <div class="number">${myCompletedCount}</div>
                        </div>
                    </div>

                    <h3>My Recent Tasks</h3>
                    <div class="table-container">
                        <c:choose>
                            <c:when test="${empty recentTasks}">
                                <p>You haven't accepted any tasks yet. <a href="${pageContext.request.contextPath}/volunteer?action=pickupTasks">Find tasks here</a>.</p>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Task Type</th>
                                            <th>Food Item</th>
                                            <th>NGO</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="task" items="${recentTasks}">
                                            <tr>
                                                <td>${task.taskType}</td>
                                                <td>${task.foodName}</td>
                                                <td>${task.ngoName}</td>
                                                <td>
                                                    <span style="padding: 4px 8px; border-radius: 4px; font-size: 0.85em;
                                                        background-color: ${task.status == 'COMPLETED' ? '#e8f8f5' : (task.status == 'IN_PROGRESS' ? '#d6eaf8' : '#fcf3cf')};
                                                        color: ${task.status == 'COMPLETED' ? '#117a65' : (task.status == 'IN_PROGRESS' ? '#2980b9' : '#b7950b')};">
                                                        ${task.status}
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
