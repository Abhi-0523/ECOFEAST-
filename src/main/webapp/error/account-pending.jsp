<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Pending - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .pending-container {
            text-align: center;
            padding: 5rem 20px;
            max-width: 600px;
            margin: 0 auto;
        }
        .pending-icon { font-size: 4rem; margin-bottom: 1rem; }
        .pending-container p { font-size: 1.1rem; margin-bottom: 2rem; color: #555; line-height: 1.6; }
    </style>
</head>
<body style="display: flex; flex-direction: column; min-height: 100vh;">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container pending-container">
        <div class="pending-icon">⏳</div>
        <h2>Account status: ${not empty accountStatus ? accountStatus : (not empty param.status ? param.status : 'PENDING')}</h2>
        <c:choose>
            <c:when test="${(not empty accountStatus ? accountStatus : param.status) == 'REJECTED'}">
                <p>Your account was not approved. If you think this is a mistake, please contact support.</p>
            </c:when>
            <c:otherwise>
                <p>Your account is currently under review by our administration team. You will be able to access all features once your account is approved.</p>
                <p>If you believe this is an error or have been waiting a long time, please contact support.</p>
            </c:otherwise>
        </c:choose>
        
        <div style="margin-top: 2rem; display: flex; gap: 1rem; justify-content: center;">
            <a href="${pageContext.request.contextPath}/contact" class="btn btn-primary">Contact Support</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary">Logout</a>
        </div>
    </div>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
