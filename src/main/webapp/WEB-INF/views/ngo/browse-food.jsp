<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Browse Food - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .filter-bar {
            background: var(--light-bg);
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 2rem;
            display: flex;
            gap: 1rem;
            flex-wrap: wrap;
            align-items: center;
        }
        .filter-bar input, .filter-bar select {
            padding: 0.5rem;
            border: 1px solid var(--border-color);
            border-radius: 4px;
        }
        .food-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 1.5rem;
        }
        .food-card {
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            overflow: hidden;
            display: flex;
            flex-direction: column;
        }
        .food-card-img {
            height: 150px;
            background-color: #ddd;
            background-size: cover;
            background-position: center;
        }
        .food-card-content {
            padding: 1.5rem;
            flex: 1;
            display: flex;
            flex-direction: column;
        }
        .food-card-content h3 { color: var(--primary-color); margin-bottom: 0.5rem; }
        .food-card-content p { margin-bottom: 0.5rem; font-size: 0.9rem; color: #555; }
        .food-card-actions { margin-top: auto; padding-top: 1rem; border-top: 1px solid var(--light-bg); }
    </style>
</head>
<body style="display: flex; flex-direction: column; min-height: 100vh;">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container">
        <div class="dashboard">
            <!-- NGO Sidebar -->
            <aside class="sidebar">
                <div class="sidebar-menu">
                    <a href="${pageContext.request.contextPath}/ngo?action=dashboard">Dashboard Overview</a>
                    <a href="${pageContext.request.contextPath}/ngo?action=browse" class="active">Browse Food</a>
                    <a href="${pageContext.request.contextPath}/ngo?action=requests">My Requests</a>
                    <a href="${pageContext.request.contextPath}/ngo?action=requestHistory">History</a>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <div class="card">
                    <h2>Available Food Donations</h2>
                    
                    <!-- Filter Bar -->
                    <form action="${pageContext.request.contextPath}/ngo" method="get" class="filter-bar">
                        <input type="hidden" name="action" value="browse">
                        
                        <input type="text" name="keyword" placeholder="Search food..." value="${keyword}">
                        
                        <select name="categoryId">
                            <option value="0">All Categories</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.categoryId}" ${selectedCat == cat.categoryId ? 'selected' : ''}>${cat.categoryName}</option>
                            </c:forEach>
                        </select>
                        
                        <input type="text" name="city" placeholder="City..." value="${city}">
                        
                        <button type="submit" class="btn btn-primary" style="padding: 0.5rem 1rem;">Filter</button>
                    </form>

                    <c:if test="${not empty error}">
                        <div class="alert alert-error">${error}</div>
                    </c:if>

                    <div class="food-grid">
                        <c:choose>
                            <c:when test="${empty donations}">
                                <p style="grid-column: 1 / -1; text-align: center; padding: 2rem; color: #777;">No food donations found matching your criteria.</p>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="food" items="${donations}">
                                    <div class="food-card">
                                        <div class="food-card-img" style="background-image: url('${not empty food.imageUrl ? food.imageUrl : 'https://images.unsplash.com/photo-1498837167922-ddd27525d352?w=500'}');"></div>
                                        <div class="food-card-content">
                                            <h3>${food.foodName}</h3>
                                            <p><strong>Donor:</strong> ${food.donorName}</p>
                                            <p><strong>Category:</strong> ${food.categoryName}</p>
                                            <p><strong>Quantity:</strong> ${food.quantity} ${food.quantityUnit}</p>
                                            <p><strong>Expires:</strong> ${food.expiryTime}</p>
                                            <p><strong>Location:</strong> ${food.pickupLocation}, ${food.pickupCity}</p>
                                            <div class="food-card-actions">
                                                <a href="${pageContext.request.contextPath}/ngo?action=requestFood&donationId=${food.donationId}" class="btn btn-primary" style="width: 100%; text-align: center; display: block;">Request Food</a>
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
