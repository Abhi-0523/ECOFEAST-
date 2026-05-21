<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Tasks - EcoFeast</title>
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
                    <a href="${pageContext.request.contextPath}/admin?action=manageDonations">All Donations</a>
                    <a href="${pageContext.request.contextPath}/admin?action=manageTasks" class="active">All Tasks</a>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <div class="card">
                    <h2>All Volunteer Tasks</h2>

                    <div class="table-container">
                        <c:choose>
                            <c:when test="${empty tasks}">
                                <p>No tasks found.</p>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Task ID</th>
                                            <th>Food Item</th>
                                            <th>NGO</th>
                                            <th>Volunteer</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="t" items="${tasks}">
                                            <tr>
                                                <td>${t.taskId}</td>
                                                <td>${t.foodName}</td>
                                                <td>${t.ngoName}</td>
                                                <td>${not empty t.volunteerName ? t.volunteerName : 'Unassigned'}</td>
                                                <td>
                                                    <span style="padding: 4px 8px; border-radius: 4px; font-size: 0.85em;
                                                        background-color: ${t.status == 'COMPLETED' ? '#e8f8f5' : (t.status == 'IN_PROGRESS' ? '#d6eaf8' : '#fcf3cf')};
                                                        color: ${t.status == 'COMPLETED' ? '#117a65' : (t.status == 'IN_PROGRESS' ? '#2980b9' : '#b7950b')};">
                                                        ${t.status}
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
