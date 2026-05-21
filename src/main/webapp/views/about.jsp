<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .about-section {
            padding: 4rem 0;
            text-align: center;
        }
        .about-text {
            max-width: 800px;
            margin: 0 auto;
            font-size: 1.1rem;
            line-height: 1.8;
            color: #555;
        }
        .mission-vision {
            display: flex;
            gap: 2rem;
            margin-top: 3rem;
            justify-content: center;
            flex-wrap: wrap;
        }
        .mv-card {
            background: #fff;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            flex: 1;
            min-width: 300px;
            max-width: 400px;
        }
        .mv-card h3 {
            color: var(--primary-color);
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <jsp:include page="/components/navbar.jsp" />

    <!-- Hero Section -->
    <section class="hero">
        <div class="container">
            <div class="hero-grid">
                <div class="hero-content">
                    <h1>About<br>EcoFeast</h1>
                    <p>Bridging the gap between surplus food and communities in need. Learn how we are building a zero-waste society.</p>
                    <div class="buttons" style="justify-content: flex-start;">
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Join Our Cause</a>
                    </div>
                </div>
                <div class="hero-image">
                    <img src="https://images.unsplash.com/photo-1593113598332-cd288d649433?w=800" alt="Food Charity Volunteers">
                </div>
            </div>
        </div>
    </section>

    <section class="about-section">
        <div class="container">
            <h2>Who We Are</h2>
            <p class="about-text">
                EcoFeast is a sustainable food redistribution platform designed to combat food waste and hunger. 
                Every day, tons of perfectly edible food are discarded by restaurants, hotels, and supermarkets, 
                while countless individuals go hungry. We provide a seamless digital bridge connecting food donors 
                with NGOs and volunteers to ensure surplus food reaches those who need it most.
            </p>

            <div class="mission-vision">
                <div class="mv-card">
                    <h3>Our Mission</h3>
                    <p>To eliminate food waste by creating an efficient, reliable, and transparent ecosystem for food donation and redistribution.</p>
                </div>
                <div class="mv-card">
                    <h3>Our Vision</h3>
                    <p>A world where no good food goes to waste, and every community has access to nutritious meals.</p>
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
