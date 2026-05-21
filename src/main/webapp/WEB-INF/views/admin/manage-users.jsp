<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users - EcoFeast</title>
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
                    <a href="${pageContext.request.contextPath}/admin?action=manageUsers" class="active">Manage Users</a>
                    <a href="${pageContext.request.contextPath}/admin?action=manageDonations">All Donations</a>
                    <a href="${pageContext.request.contextPath}/admin?action=manageTasks">All Tasks</a>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <div class="card">
                    <h2>Pending User Approvals</h2>
                    
                    <c:if test="${not empty sessionScope.successMsg}">
                        <div class="alert alert-success">${sessionScope.successMsg}</div>
                        <c:remove var="successMsg" scope="session"/>
                    </c:if>
                    <c:if test="${not empty sessionScope.errorMsg}">
                        <div class="alert alert-error">${sessionScope.errorMsg}</div>
                        <c:remove var="errorMsg" scope="session"/>
                    </c:if>

                    <div class="table-container">
                        <c:choose>
                            <c:when test="${empty pendingUsers}">
                                <p>No pending users at the moment.</p>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Name</th>
                                            <th>Email</th>
                                            <th>Role</th>
                                            <th>Organization</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="u" items="${pendingUsers}">
                                            <tr>
                                                <td>${u.fullName}</td>
                                                <td>${u.email}</td>
                                                <td>${u.role.roleName}</td>
                                                <td>${not empty u.organization ? u.organization : '-'}</td>
                                                <td>
                                                    <div style="display: flex; gap: 8px; align-items: center;">
                                                        <form action="${pageContext.request.contextPath}/admin" method="post">
                                                            <input type="hidden" name="action" value="approveUser">
                                                            <input type="hidden" name="userId" value="${u.id}">
                                                            <button type="submit" style="padding: 6px 14px; font-size: 0.82rem; font-weight: 700; background: #2e7d32; color: #ffffff; border: none; border-radius: 8px; cursor: pointer;">&#10003; Approve</button>
                                                        </form>
                                                        <form action="${pageContext.request.contextPath}/admin" method="post" onsubmit="return confirm('Are you sure you want to reject this user?');">
                                                            <input type="hidden" name="action" value="rejectUser">
                                                            <input type="hidden" name="userId" value="${u.id}">
                                                            <button type="submit" style="padding: 6px 14px; font-size: 0.82rem; font-weight: 700; background: #d32f2f; color: #ffffff; border: none; border-radius: 8px; cursor: pointer;">&#10007; Reject</button>
                                                        </form>
                                                    </div>
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
