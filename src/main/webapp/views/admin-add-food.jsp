<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ecofeast.models.User" %>
<%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Food Item - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <div class="logo">EcoFeast - Admin</div>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
            </ul>
        </div>
    </nav>

    <div class="container" style="max-width: 640px; margin: 2rem auto;">
        <h1>Add Food Item</h1>
        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
            <div class="alert alert-error"><strong>Error:</strong> <%= error %></div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/admin/dashboard" class="card" style="padding: 1.5rem;">
            <input type="hidden" name="action" value="addFoodItem">

            <div class="form-group">
                <label for="itemName">Item name</label>
                <input type="text" id="itemName" name="itemName" required>
            </div>
            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="3"></textarea>
            </div>
            <div class="form-group">
                <label for="category">Category</label>
                <input type="text" id="category" name="category" required placeholder="e.g. Fruits, Bakery">
            </div>
            <div class="form-group">
                <label for="quantity">Quantity</label>
                <input type="number" id="quantity" name="quantity" min="1" required>
            </div>
            <div class="form-group">
                <label for="expiryDate">Expiry date</label>
                <input type="date" id="expiryDate" name="expiryDate" required>
            </div>
            <div class="form-group">
                <label for="location">Pickup location</label>
                <input type="text" id="location" name="location" required>
            </div>

            <button type="submit" class="btn btn-primary">Save item</button>
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn" style="margin-left: 0.5rem;">Cancel</a>
        </form>
    </div>
</body>
</html>
