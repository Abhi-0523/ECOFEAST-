<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<nav class="navbar">
    <div class="container">
        <a href="${pageContext.request.contextPath}/" class="logo">🌱 EcoFeast</a>
        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/views/about.jsp">About</a></li>
            <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
            <li><a href="${pageContext.request.contextPath}/views/faq.jsp">FAQ</a></li>
            
            <c:choose>
                <c:when test="${not empty sessionScope.loggedInUser}">
                    <c:set var="role" value="${empty sessionScope.loggedInUser.role.roleName ? '' : fn:toUpperCase(sessionScope.loggedInUser.role.roleName)}" />
                    <c:if test="${role == 'ADMIN'}">
                        <li><a href="${pageContext.request.contextPath}/admin?action=dashboard" class="btn-nav">Dashboard</a></li>
                    </c:if>
                    <c:if test="${role == 'DONOR'}">
                        <li><a href="${pageContext.request.contextPath}/donor?action=dashboard" class="btn-nav">Dashboard</a></li>
                    </c:if>
                    <c:if test="${role == 'NGO'}">
                        <li><a href="${pageContext.request.contextPath}/ngo?action=dashboard" class="btn-nav">Dashboard</a></li>
                    </c:if>
                    <c:if test="${role == 'VOLUNTEER'}">
                        <li><a href="${pageContext.request.contextPath}/volunteer?action=dashboard" class="btn-nav">Dashboard</a></li>
                    </c:if>
                    <li><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/register" class="btn-nav">Register</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>
