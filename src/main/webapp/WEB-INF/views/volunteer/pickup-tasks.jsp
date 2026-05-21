<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.action == 'myTasks' ? 'My Active Tasks' : 'Find Pickup Tasks'} - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .task-card {
            background: #fff;
            border-radius: 8px;
            padding: 1.5rem;
            margin-bottom: 1.5rem;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            border-left: 5px solid var(--primary-color);
        }
        .task-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; border-bottom: 1px solid #eee; padding-bottom: 0.5rem;}
        .task-header h3 { color: var(--text-color); margin: 0;}
        .task-body { display: flex; gap: 2rem; flex-wrap: wrap;}
        .task-details { flex: 1; min-width: 250px;}
        .task-actions { display: flex; flex-direction: column; gap: 0.5rem; justify-content: center;}
    </style>
</head>
<body style="display: flex; flex-direction: column; min-height: 100vh;">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container">
        <div class="dashboard">
            <!-- Volunteer Sidebar -->
            <aside class="sidebar">
                <div class="sidebar-menu">
                    <a href="${pageContext.request.contextPath}/volunteer?action=dashboard">Dashboard Overview</a>
                    <a href="${pageContext.request.contextPath}/volunteer?action=pickupTasks" class="${param.action == 'pickupTasks' ? 'active' : ''}">Find Pickup Tasks</a>
                    <a href="${pageContext.request.contextPath}/volunteer?action=myTasks" class="${param.action == 'myTasks' ? 'active' : ''}">My Active Tasks</a>
                    <a href="${pageContext.request.contextPath}/volunteer?action=deliveryHistory">Delivery History</a>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <div class="card">
                    <h2>${param.action == 'myTasks' ? 'My Active Tasks' : 'Available Tasks'}</h2>
                    
                    <c:if test="${not empty sessionScope.successMsg}">
                        <div class="alert alert-success">${sessionScope.successMsg}</div>
                        <c:remove var="successMsg" scope="session"/>
                    </c:if>
                    <c:if test="${not empty sessionScope.errorMsg}">
                        <div class="alert alert-error">${sessionScope.errorMsg}</div>
                        <c:remove var="errorMsg" scope="session"/>
                    </c:if>

                    <div>
                        <c:choose>
                            <c:when test="${empty tasks}">
                                <p>No tasks found.</p>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="task" items="${tasks}">
                                    <div class="task-card">
                                        <div class="task-header">
                                            <h3>${task.foodName}</h3>
                                            <span style="font-weight: bold; color: ${task.status == 'OPEN' ? '#b7950b' : '#2980b9'};">${task.status}</span>
                                        </div>
                                        <div class="task-body">
                                            <div class="task-details">
                                                <p><strong>Type:</strong> ${task.taskType}</p>
                                                <p><strong>NGO Dest:</strong> ${task.ngoName}</p>
                                                <p><strong>Pickup At:</strong> ${task.pickupAddress}</p>
                                                <p><strong>Deliver To:</strong> ${task.deliveryAddress}</p>
                                                <p><strong>Notes:</strong> ${task.notes}</p>
                                            </div>
                                            <div class="task-actions">
                                                <c:if test="${task.status == 'OPEN'}">
                                                    <form action="${pageContext.request.contextPath}/volunteer" method="post">
                                                        <input type="hidden" name="action" value="acceptTask">
                                                        <input type="hidden" name="taskId" value="${task.taskId}">
                                                        <button type="submit" class="btn btn-primary" style="width: 100%;">Accept Task</button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${task.status == 'ACCEPTED'}">
                                                    <form action="${pageContext.request.contextPath}/volunteer" method="post">
                                                        <input type="hidden" name="action" value="startTask">
                                                        <input type="hidden" name="taskId" value="${task.taskId}">
                                                        <button type="submit" class="btn btn-secondary" style="width: 100%;">Start Pickup</button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${task.status == 'IN_PROGRESS'}">
                                                    <form action="${pageContext.request.contextPath}/volunteer" method="post" onsubmit="return confirm('Confirm delivery completed?');">
                                                        <input type="hidden" name="action" value="completeTask">
                                                        <input type="hidden" name="taskId" value="${task.taskId}">
                                                        <button type="submit" class="btn" style="width: 100%; background: var(--success-color); color: white;">Mark Completed</button>
                                                    </form>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
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
