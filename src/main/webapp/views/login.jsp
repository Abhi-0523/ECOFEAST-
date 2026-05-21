<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:if test="${empty demoAdminEmail}">
    <c:set var="demoAdminEmail" value="admin@ecofeast.com" />
    <c:set var="demoAdminPassword" value="admin123" />
</c:if>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .form-container {
            max-width: 400px;
            margin: 4rem auto;
            background: #fff;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body style="padding-top: 8rem; background-color: var(--bg-soft);">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container" style="display: flex; justify-content: center;">
        <div class="food-card" style="max-width: 500px; width: 100%; padding: 3rem;">
            <div style="text-align: center; margin-bottom: 2.5rem;">
                <h2 style="font-size: 2.5rem; font-weight: 800; margin-bottom: 0.5rem;">Welcome Back</h2>
                <p style="color: var(--text-gray);">Sign in to continue your sustainable journey</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error" style="margin-bottom: 1.5rem;">
                    ${error}
                </div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/login" method="POST">
                <div class="form-group" style="margin-bottom: 1.5rem;">
                    <label style="font-weight: 600; font-size: 0.9rem; margin-bottom: 0.5rem; display: block;">Email Address</label>
                    <input type="email" name="email" required placeholder="hello@ecofeast.com" 
                           style="width: 100%; padding: 1rem; border-radius: 15px; border: 1px solid #eee; background: #fff; font-family: inherit;">
                </div>

                <div class="form-group" style="margin-bottom: 2rem;">
                    <label style="font-weight: 600; font-size: 0.9rem; margin-bottom: 0.5rem; display: block;">Password</label>
                    <input type="password" name="password" required placeholder="&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;" 
                           style="width: 100%; padding: 1rem; border-radius: 15px; border: 1px solid #eee; background: #fff; font-family: inherit;">
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%;">Sign In</button>
            </form>

            <p style="text-align: center; margin-top: 2rem; font-size: 0.95rem; color: var(--text-gray);">
                New to EcoFeast? <a href="${pageContext.request.contextPath}/register" style="color: var(--primary-color); text-decoration: none; font-weight: 700;">Join Community</a>
            </p>

            <div style="margin-top: 2.5rem; padding-top: 2rem; border-top: 1px dotted #eee; font-size: 0.8rem; color: var(--text-gray); line-height: 1.6;">
                <strong>Demo Admin:</strong> ${demoAdminEmail} / ${demoAdminPassword}<br>
                <span style="opacity: 0.7;">Use these credentials after importing <code>schema_full.sql</code>.</span>
            </div>
        </div>
    </div>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
