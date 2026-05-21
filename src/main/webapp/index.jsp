<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EcoFeast - Sustainable Food Redistribution</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/components/navbar.jsp" />

    <!-- Hero Section -->
    <section class="hero">
        <div class="container">
            <div class="hero-grid">
                <div class="hero-content">
                    <h1>Sustainable Food<br>Redistributed<br>For You</h1>
                    <p>EcoFeast bridges the gap between food surplus and community needs. Join us in making a world where nothing goes to waste.</p>
                    <div class="buttons" style="justify-content: flex-start;">
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Join Community</a>
                    </div>
                </div>
                <div class="hero-image">
                    <img src="https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800" alt="Healthy Food Bowl">
                </div>
            </div>
        </div>
    </section>

    <!-- Info Bar -->
    <div class="container info-bar">
        <div class="info-grid">
            <div class="info-item">
                <h3>24/7 Operations</h3>
                <p>Redistributing food anytime</p>
            </div>
            <div class="info-item">
                <h3>Global Reach</h3>
                <p>Connecting cities nationwide</p>
            </div>
            <div class="info-item">
                <h3>Verified Partners</h3>
                <p>Secure and trusted network</p>
            </div>
        </div>
    </div>

    <!-- Feature Section -->
    <section class="feature-section">
        <div class="container">
            <div class="feature-grid">
                <div class="feature-image">
                    <img src="https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=800" alt="Balanced Diet">
                </div>
                <div class="feature-content">
                    <h2>Food Sustainability Is Part Of A Better Future</h2>
                    <p>We provide a resilient platform to ensure surplus food from restaurants and hotels reach those who need it most, ensuring quality nutrition for everyone.</p>
                    <a href="${pageContext.request.contextPath}/views/about.jsp" class="btn btn-primary">Learn More</a>
                </div>
            </div>
        </div>
    </section>

    <!-- How It Works -->
    <section class="how-it-works" style="padding: 8rem 0;">
        <div class="container">
            <div class="section-header">
                <span>Process</span>
                <h2>How It Works</h2>
            </div>
            <div class="steps-grid">
                <div class="step-card">
                    <div class="step-icon">📦</div>
                    <h3>List Surplus</h3>
                    <p>Donors list surplus food items with details and expiry times.</p>
                </div>
                <div class="step-card">
                    <div class="step-icon">🤝</div>
                    <h3>Match & Verify</h3>
                    <p>NGOs and Charities request items based on community needs.</p>
                </div>
                <div class="step-card">
                    <div class="step-icon">🚚</div>
                    <h3>Redistribute</h3>
                    <p>Volunteers and NGOs handle the logistics of redistribution.</p>
                </div>
            </div>
        </div>
    </section>

    <!-- Donations Menu -->
    <section class="menu-section">
        <div class="container">
            <div class="section-header">
                <span>Explore</span>
                <h2>Recent Opportunities</h2>
            </div>
            <div class="menu-grid">
                <!-- Product 1 -->
                <div class="food-card">
                    <div class="food-img"><img src="https://images.unsplash.com/photo-1550258987-190a2d41a8ba?w=500&h=350&fit=crop" alt="Fresh Pineapples"></div>
                    <div class="food-info">
                        <div class="food-header"><h3>Surplus Pineapples</h3><div class="food-price">FREE</div></div>
                        <p class="food-desc">A fresh batch of ripe pineapples donated by local farmers, perfect for community distribution.</p>
                        <div class="food-footer">
                            <div style="display:flex; flex-direction:column; gap:2px;">
                                <span style="color: var(--text-gray); font-size: 0.8rem;">Available: 20 kg</span>
                                <span style="color: #d32f2f; font-size: 0.8rem; font-weight: bold;">Expires in: 5 Days</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/login" class="btn-add">+</a>
                        </div>
                    </div>
                </div>

                <!-- Product 2 -->
                <div class="food-card">
                    <div class="food-img"><img src="https://images.unsplash.com/photo-1568702846914-96b305d2aaeb?w=500&h=350&fit=crop" alt="Organic Red Apples"></div>
                    <div class="food-info">
                        <div class="food-header"><h3>Organic Red Apples</h3><div class="food-price">FREE</div></div>
                        <p class="food-desc">Crisp, organic red apples saved from local grocery surplus. Nutritious and ready to eat.</p>
                        <div class="food-footer">
                            <div style="display:flex; flex-direction:column; gap:2px;">
                                <span style="color: var(--text-gray); font-size: 0.8rem;">Available: 15 Boxes</span>
                                <span style="color: #d32f2f; font-size: 0.8rem; font-weight: bold;">Expires in: 7 Days</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/login" class="btn-add">+</a>
                        </div>
                    </div>
                </div>

                <!-- Product 3 -->
                <div class="food-card">
                    <div class="food-img"><img src="https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=500&h=350&fit=crop" alt="Yellow Bananas"></div>
                    <div class="food-info">
                        <div class="food-header"><h3>Yellow Bananas</h3><div class="food-price">FREE</div></div>
                        <p class="food-desc">Bunches of ripe yellow bananas donated by a local supermarket chain.</p>
                        <div class="food-footer">
                            <div style="display:flex; flex-direction:column; gap:2px;">
                                <span style="color: var(--text-gray); font-size: 0.8rem;">Available: 30 Bunches</span>
                                <span style="color: #d32f2f; font-size: 0.8rem; font-weight: bold;">Expires in: 3 Days</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/login" class="btn-add">+</a>
                        </div>
                    </div>
                </div>

                <!-- Product 4 -->
                <div class="food-card">
                    <div class="food-img"><img src="https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=500&h=350&fit=crop" alt="Fresh Tomatoes"></div>
                    <div class="food-info">
                        <div class="food-header"><h3>Fresh Tomatoes</h3><div class="food-price">FREE</div></div>
                        <p class="food-desc">A large crate of fresh, juicy tomatoes directly from a local greenhouse.</p>
                        <div class="food-footer">
                            <div style="display:flex; flex-direction:column; gap:2px;">
                                <span style="color: var(--text-gray); font-size: 0.8rem;">Available: 50 kg</span>
                                <span style="color: #d32f2f; font-size: 0.8rem; font-weight: bold;">Expires in: 4 Days</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/login" class="btn-add">+</a>
                        </div>
                    </div>
                </div>

                <!-- Product 5 -->
                <div class="food-card">
                    <div class="food-img"><img src="https://images.unsplash.com/photo-1598170845058-32b9d6a5da37?w=500&h=350&fit=crop" alt="Farm Carrots"></div>
                    <div class="food-info">
                        <div class="food-header"><h3>Farm Carrots</h3><div class="food-price">FREE</div></div>
                        <p class="food-desc">Freshly harvested orange carrots ideal for stews and community kitchen meals.</p>
                        <div class="food-footer">
                            <div style="display:flex; flex-direction:column; gap:2px;">
                                <span style="color: var(--text-gray); font-size: 0.8rem;">Available: 25 kg</span>
                                <span style="color: #d32f2f; font-size: 0.8rem; font-weight: bold;">Expires in: 6 Days</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/login" class="btn-add">+</a>
                        </div>
                    </div>
                </div>

                <!-- Product 6 -->
                <div class="food-card">
                    <div class="food-img"><img src="https://images.unsplash.com/photo-1582284540020-8acbe03f4924?w=500&h=350&fit=crop" alt="Red Radishes"></div>
                    <div class="food-info">
                        <div class="food-header"><h3>Red Radishes</h3><div class="food-price">FREE</div></div>
                        <p class="food-desc">Bunches of crisp red radishes with green leaves, perfect for salads.</p>
                        <div class="food-footer">
                            <div style="display:flex; flex-direction:column; gap:2px;">
                                <span style="color: var(--text-gray); font-size: 0.8rem;">Available: 40 Bunches</span>
                                <span style="color: #d32f2f; font-size: 0.8rem; font-weight: bold;">Expires in: 5 Days</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/login" class="btn-add">+</a>
                        </div>
                    </div>
                </div>

                <!-- Product 7 -->
                <div class="food-card">
                    <div class="food-img"><img src="https://images.unsplash.com/photo-1587593810167-a84920ea0781?w=500&h=350&fit=crop" alt="Raw Chicken Pieces"></div>
                    <div class="food-info">
                        <div class="food-header"><h3>Surplus Poultry</h3><div class="food-price">FREE</div></div>
                        <p class="food-desc">Fresh raw chicken drumsticks safely packaged and frozen, donated by a local supplier.</p>
                        <div class="food-footer">
                            <div style="display:flex; flex-direction:column; gap:2px;">
                                <span style="color: var(--text-gray); font-size: 0.8rem;">Available: 10 kg</span>
                                <span style="color: #d32f2f; font-size: 0.8rem; font-weight: bold;">Expires in: 2 Days</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/login" class="btn-add">+</a>
                        </div>
                    </div>
                </div>

                <!-- Product 8 -->
                <div class="food-card">
                    <div class="food-img"><img src="https://images.unsplash.com/photo-1569288052389-dac9b01c9c05?w=500&h=350&fit=crop" alt="Farm Fresh Eggs"></div>
                    <div class="food-info">
                        <div class="food-header"><h3>Farm Fresh Eggs</h3><div class="food-price">FREE</div></div>
                        <p class="food-desc">A large lot of native farm eggs, carefully packed to prevent breakage during transport.</p>
                        <div class="food-footer">
                            <div style="display:flex; flex-direction:column; gap:2px;">
                                <span style="color: var(--text-gray); font-size: 0.8rem;">Available: 15 Trays</span>
                                <span style="color: #d32f2f; font-size: 0.8rem; font-weight: bold;">Expires in: 7 Days</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/login" class="btn-add">+</a>
                        </div>
                    </div>
                </div>

                <!-- Product 9 -->
                <div class="food-card">
                    <div class="food-img"><img src="https://images.unsplash.com/photo-1509440159596-0249088772ff?w=500&h=350&fit=crop" alt="Artisan Brown Bread"></div>
                    <div class="food-info">
                        <div class="food-header"><h3>Artisan Brown Bread</h3><div class="food-price">FREE</div></div>
                        <p class="food-desc">Freshly baked brown bread donated by a local bakery at the end of the day.</p>
                        <div class="food-footer">
                            <div style="display:flex; flex-direction:column; gap:2px;">
                                <span style="color: var(--text-gray); font-size: 0.8rem;">Available: 25 Loaves</span>
                                <span style="color: #d32f2f; font-size: 0.8rem; font-weight: bold;">Expires in: 3 Days</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/login" class="btn-add">+</a>
                        </div>
                    </div>
                </div>

                <!-- Product 10 -->
                <div class="food-card">
                    <div class="food-img"><img src="https://images.unsplash.com/photo-1607532941433-304659e8198a?w=500&h=350&fit=crop" alt="Fresh Bagels"></div>
                    <div class="food-info">
                        <div class="food-header"><h3>Fresh Bagels</h3><div class="food-price">FREE</div></div>
                        <p class="food-desc">A large batch of surplus fresh bagels on a cooling rack from a local cafe.</p>
                        <div class="food-footer">
                            <div style="display:flex; flex-direction:column; gap:2px;">
                                <span style="color: var(--text-gray); font-size: 0.8rem;">Available: 50 Bagels</span>
                                <span style="color: #d32f2f; font-size: 0.8rem; font-weight: bold;">Expires in: 2 Days</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/login" class="btn-add">+</a>
                        </div>
                    </div>
                </div>

                <!-- Product 11 -->
                <div class="food-card">
                    <div class="food-img"><img src="https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=500&h=350&fit=crop" alt="Artisan Cheese"></div>
                    <div class="food-info">
                        <div class="food-header"><h3>Artisan Cheese</h3><div class="food-price">FREE</div></div>
                        <p class="food-desc">Pieces of fresh artisan cheese on a wooden plate donated by a dairy farm.</p>
                        <div class="food-footer">
                            <div style="display:flex; flex-direction:column; gap:2px;">
                                <span style="color: var(--text-gray); font-size: 0.8rem;">Available: 10 Blocks</span>
                                <span style="color: #d32f2f; font-size: 0.8rem; font-weight: bold;">Expires in: 7 Days</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/login" class="btn-add">+</a>
                        </div>
                    </div>
                </div>

                <!-- Product 12 -->
                <div class="food-card">
                    <div class="food-img"><img src="https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=500&h=350&fit=crop" alt="Fresh Mixed Berries"></div>
                    <div class="food-info">
                        <div class="food-header"><h3>Fresh Mixed Berries</h3><div class="food-price">FREE</div></div>
                        <p class="food-desc">A beautiful display of fresh strawberries and blackberries ready to be shared with the community.</p>
                        <div class="food-footer">
                            <div style="display:flex; flex-direction:column; gap:2px;">
                                <span style="color: var(--text-gray); font-size: 0.8rem;">Available: 20 Boxes</span>
                                <span style="color: #d32f2f; font-size: 0.8rem; font-weight: bold;">Expires in: 4 Days</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/login" class="btn-add">+</a>
                        </div>
                    </div>
                </div>

                <!-- Product 13 -->
                <div class="food-card">
                    <div class="food-img"><img src="https://images.unsplash.com/photo-1551024601-bec78aea704b?w=500&h=350&fit=crop" alt="Bakery Doughnuts"></div>
                    <div class="food-info">
                        <div class="food-header"><h3>Bakery Doughnuts</h3><div class="food-price">FREE</div></div>
                        <p class="food-desc">A large pile of sweet, glazed doughnuts donated at the end of the day by a local cafe.</p>
                        <div class="food-footer">
                            <div style="display:flex; flex-direction:column; gap:2px;">
                                <span style="color: var(--text-gray); font-size: 0.8rem;">Available: 40 Pieces</span>
                                <span style="color: #d32f2f; font-size: 0.8rem; font-weight: bold;">Expires in: 1 Day</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/login" class="btn-add">+</a>
                        </div>
                    </div>
                </div>
            </div>
            <div style="text-align: center; margin-top: 4rem;">
                <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Join To See More</a>
            </div>
        </div>
    </section>

    <!-- Testimonials -->
    <section class="testimonials">
        <div class="container">
            <div class="section-header">
                <span>Testimonials</span>
                <h2>What They Are Saying</h2>
            </div>
            <div class="testimonial-card">
                <img src="https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150" alt="User" class="testimonial-avatar">
                <p class="testimonial-text">"EcoFeast has revolutionized how we handle surplus. We've seen a 40% reduction in waste and a huge impact on our local community."</p>
                <h4 style="font-weight: 800;">Sarah Johnson</h4>
                <p style="color: var(--text-gray); font-size: 0.9rem;">Donor Coordinator, Green Valley</p>
            </div>
        </div>
    </section>

    <!-- Newsletter -->
    <section class="container" style="padding: 4rem 0;">
        <div class="newsletter">
            <h2>Have a question in mind?<br>Let us help you</h2>
            <form class="newsletter-form">
                <input type="email" placeholder="yourmail@gmail.com" required>
                <button type="submit">Send</button>
            </form>
        </div>
    </section>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
