<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ecofeast.models.User" %>
<%@ page import="com.ecofeast.models.FoodItem" %>
<%@ page import="java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<FoodItem> wishlist = (List<FoodItem>) session.getAttribute("wishlist");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Wishlist - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <div class="logo">🌱 EcoFeast</div>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/user/dashboard">Dashboard</a></li>
                <li><span style="color: white;">Welcome, <%= currentUser.getFirstName() %></span></li>
                <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
            </ul>
        </div>
    </nav>

    <div class="container" style="margin-top: 2rem;">
        <div class="card">
            <h2>My Food Cart (Wishlist)</h2>
            <p>Review the items you have saved. These are temporarily stored for your session.</p>
            
            <% 
                String success = (String) request.getAttribute("success");
                if (success != null) { 
            %>
                <div class="alert alert-success" style="margin-top:1rem; margin-bottom:1rem;">
                    <strong>Success:</strong> <%= success %>
                </div>
            <% } %>

            <div class="table-container" style="margin-top: 1.5rem;">
                <table>
                    <thead>
                        <tr>
                            <th>Item Name</th>
                            <th>Category</th>
                            <th>Available Quantity</th>
                            <th>Expiry Date</th>
                            <th>Location</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (wishlist != null && !wishlist.isEmpty()) { 
                            for (FoodItem item : wishlist) { %>
                        <tr>
                            <td><%= item.getItemName() %></td>
                            <td><%= item.getCategory() %></td>
                            <td><%= item.getQuantity() %></td>
                            <td><%= item.getExpiryDate() %></td>
                            <td><%= item.getLocation() %></td>
                            <td>
                                <form method="POST" action="${pageContext.request.contextPath}/user/dashboard" style="display:inline;">
                                    <input type="hidden" name="action" value="removeFromWishlist">
                                    <input type="hidden" name="itemId" value="<%= item.getItemId() %>">
                                    <button type="submit" class="btn btn-secondary" style="padding:0.25rem 0.5rem; background-color: #e74c3c; color: white; border: none; cursor: pointer; border-radius: 4px;">Remove</button>
                                </form>
                            </td>
                        </tr>
                        <%  } 
                           } else { %>
                        <tr>
                            <td colspan="6" style="text-align: center; padding: 2rem;">
                                Your wishlist is empty. 
                                <br><br>
                                <a href="${pageContext.request.contextPath}/user/dashboard?action=availableItems" class="btn btn-primary">Browse Available Food</a>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            
            <% if (wishlist != null && !wishlist.isEmpty()) { %>
                <div style="margin-top: 1.5rem; text-align: right;">
                    <a href="${pageContext.request.contextPath}/user/dashboard?action=availableItems" class="btn btn-secondary">Continue Browsing</a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
