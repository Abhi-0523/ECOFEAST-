<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Us - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .contact-container {
            display: flex;
            gap: 2rem;
            max-width: 1000px;
            margin: 3rem auto;
            padding: 0 20px;
            flex-wrap: wrap;
        }
        .contact-info, .contact-form-wrap {
            flex: 1;
            min-width: 300px;
        }
        .contact-info {
            background: var(--primary-color);
            color: #fff;
            padding: 2rem;
            border-radius: 8px;
        }
        .contact-info h3 { margin-bottom: 1rem; color: #fff;}
        .contact-info p { margin-bottom: 1rem; display: flex; align-items: center; gap: 10px;}
        .contact-form-wrap {
            background: #fff;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <jsp:include page="/components/navbar.jsp" />

    <!-- Hero Section -->
    <section class="hero" style="padding-bottom: 4rem;">
        <div class="container">
            <div class="hero-grid">
                <div class="hero-content">
                    <h1>Contact<br>Our Team</h1>
                    <p>We'd love to hear from you. Get in touch with the EcoFeast team for inquiries, support, or partnership opportunities.</p>
                    <div class="buttons" style="justify-content: flex-start;">
                        <a href="#contact-details" class="btn btn-primary">Send a Message</a>
                    </div>
                </div>
                <div class="hero-image">
                    <img src="https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?w=800" alt="Contact EcoFeast">
                </div>
            </div>
        </div>
    </section>
 
     <div class="contact-container" id="contact-details">
        <div class="contact-info">
            <h3>Contact Information</h3>
            <p><strong>Location:</strong> Damak, Nepal</p>
            <p><strong>Phone:</strong> +977 9800000000</p>
            <p><strong>Email:</strong> support@ecofeast.com</p>
            <p><strong>Hours:</strong> Mon - Fri, 9:00 AM - 5:00 PM</p>
        </div>

        <div class="contact-form-wrap">
            <h3>Send a Message</h3>
            
            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/contact" method="post">
                <div class="form-group">
                    <label>Your Name</label>
                    <input type="text" name="name" value="${prevName}" required>
                </div>
                <div class="form-group">
                    <label>Email Address</label>
                    <input type="email" name="email" value="${prevEmail}" required>
                </div>
                <div class="form-group">
                    <label>Subject (Optional)</label>
                    <input type="text" name="subject" value="${prevSubject}">
                </div>
                <div class="form-group">
                    <label>Message</label>
                    <textarea name="message" required>${prevMessage}</textarea>
                </div>
                <button type="submit" class="btn btn-primary" style="width:100%">Send Message</button>
            </form>
        </div>
    </div>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
