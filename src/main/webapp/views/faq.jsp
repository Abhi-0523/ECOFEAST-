<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Frequently Asked Questions - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .faq-section { max-width: 800px; margin: 3rem auto; padding: 0 20px; }
        .faq-item {
            background: #fff;
            margin-bottom: 1rem;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
            padding: 1.5rem;
        }
        .faq-question {
            font-weight: bold;
            color: var(--primary-color);
            margin-bottom: 0.5rem;
            font-size: 1.1rem;
        }
        .faq-answer { color: #555; line-height: 1.6; }
    </style>
</head>
<body>
    <jsp:include page="/components/navbar.jsp" />

    <!-- Hero Section -->
    <section class="hero" style="padding-bottom: 4rem;">
        <div class="container">
            <div class="hero-grid">
                <div class="hero-content">
                    <h1>Frequently<br>Asked Questions</h1>
                    <p>Find answers to common questions about EcoFeast, how the donations work, and how you can participate.</p>
                    <div class="buttons" style="justify-content: flex-start;">
                        <a href="${pageContext.request.contextPath}/views/contact.jsp" class="btn btn-primary">Have More Questions?</a>
                    </div>
                </div>
                <div class="hero-image">
                    <img src="https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=800" alt="EcoFeast Support FAQ">
                </div>
            </div>
        </div>
    </section>

    <div class="faq-section">
        <div class="faq-item">
            <div class="faq-question">Who can donate food?</div>
            <div class="faq-answer">Any registered business, including restaurants, hotels, supermarkets, and event organizers, can sign up as a Donor to list surplus food.</div>
        </div>
        <div class="faq-item">
            <div class="faq-question">How do NGOs request food?</div>
            <div class="faq-answer">Registered NGOs can browse available donations on their dashboard and submit a request. Once the donor approves, a volunteer will be assigned for delivery or the NGO can pick it up.</div>
        </div>
        <div class="faq-item">
            <div class="faq-question">Can I volunteer to deliver food?</div>
            <div class="faq-answer">Yes! You can register as a Volunteer. Once approved, you can view open delivery tasks and accept them to help transport food from donors to NGOs.</div>
        </div>
        <div class="faq-item">
            <div class="faq-question">Is the food safe to consume?</div>
            <div class="faq-answer">Donors are required to list food that is safe and within its expiry date. We encourage NGOs to inspect the food upon collection.</div>
        </div>
    </div>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
