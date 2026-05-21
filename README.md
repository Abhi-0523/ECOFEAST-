# EcoFeast - Sustainable Food Redistribution System

## Project Overview
EcoFeast is a Java EE MVC web application designed to facilitate sustainable food redistribution. It allows businesses to list surplus food items and enables users in need to request and collect these items in a controlled, ethical manner.

## Architecture & Technology Stack
- **Language:** Java (J2EE/Jakarta EE)
- **Architecture:** Strict Model-View-Controller (MVC)
- **Database:** MySQL with JDBC
- **Frontend:** JSP and CSS (Flexbox & Media Queries - NO Bootstrap)
- **JavaScript:** Minimal, for UI enhancements only
- **Build Tool:** Maven (Optional; can compile with javac)
- **Server:** Apache Tomcat 9+

## Project Structure
```
EcoFeast/
├── pom.xml                          # Maven configuration
├── database/
│   └── schema.sql                   # MySQL database schema
├── src/
│   └── main/
│       ├── java/com/ecofeast/
│       │   ├── controllers/         # Servlet Controllers
│       │   │   ├── LoginServlet.java
│       │   │   ├── RegisterServlet.java
│       │   │   ├── LogoutServlet.java
│       │   │   ├── AdminController.java
│       │   │   └── UserController.java
│       │   ├── models/              # Data Models
│       │   │   ├── User.java
│       │   │   ├── FoodItem.java
│       │   │   └── Request.java
│       │   ├── filters/             # Servlet Filters
│       │   │   └── RBACFilter.java   # Role-Based Access Control
│       │   └── util/                # Utility Classes
│       │       ├── DatabaseUtil.java
│       │       ├── PasswordUtil.java
│       │       └── ValidationUtil.java
│       └── webapp/
│           ├── WEB-INF/
│           │   └── web.xml          # Deployment descriptor
│           ├── index.jsp            # Home page
│           ├── css/
│           │   └── style.css        # Responsive CSS styles
│           └── views/               # JSP pages
│               ├── login.jsp
│               ├── register.jsp
│               ├── admin-dashboard.jsp
│               ├── user-portal.jsp
│               └── error.jsp
```

## Prerequisites
Before you begin, ensure you have the following installed:
- **Java Development Kit (JDK):** Java 11 or higher
- **MySQL Server:** Version 5.7 or higher
- **Apache Tomcat:** Version 9.x
- **Maven:** Version 3.6+ (Optional, for build automation)

### Check Installations
```bash
java -version
mysql --version
tomcat/bin/version.sh      # or bin/version.bat on Windows
mvn --version
```

## Setup Instructions

### 1. Database Setup

#### Step 1.1: Create Database and Tables
```bash
# Login to MySQL
mysql -u root -p

# Run the schema script
source database/schema.sql;

# Verify tables created
USE ecofeast;
SHOW TABLES;
```

### 2. Build the Project

#### Option A: Using Maven
```bash
cd EcoFeast
mvn clean package
```

This creates `target/ecofeast.war`

#### Option B: Manual Compilation with javac
```bash
# Compile with classpath including servlet API and MySQL JDBC
javac -cp "path/to/servlet-api.jar:path/to/mysql-connector.jar" \
      -d target/classes \
      src/main/java/com/ecofeast/**/*.java
```

### 3. Configure Database Connection

Edit `src/main/java/com/ecofeast/util/DatabaseUtil.java`:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/ecofeast";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password"; // Change if needed
```

### 4. Deploy to Tomcat

#### Step 4.1: Copy WAR file
```bash
cp target/ecofeast.war $TOMCAT_HOME/webapps/
```

#### Step 4.2: Start Tomcat
```bash
# Unix/Linux/Mac
$TOMCAT_HOME/bin/startup.sh

# Windows
%TOMCAT_HOME%\bin\startup.bat
```

#### Step 4.3: Access Application
Open browser and navigate to:
```
http://localhost:8080/ecofeast/
```

## Default Test Credentials

### Admin User
- **Email:** admin@ecofeast.com
- **Password:** admin123
- **Role:** ADMIN (Full system access)

### Donor User
- **Email:** donor@ecofeast.com
- **Password:** Donor@123
- **Role:** DONOR

### NGO User
- **Email:** ngo@ecofeast.com
- **Password:** Ngo@1234
- **Role:** NGO

### Volunteer User
- **Email:** volunteer@ecofeast.com
- **Password:** Vol@1234
- **Role:** VOLUNTEER

## Key Features Implemented

### 1. Authentication & Authorization (MVC Pattern)
- **LoginServlet:** User authentication with email/password validation
- **RegisterServlet:** New user registration with duplicate checks
- **RBACFilter:** Role-Based Access Control for URL redirection
- **LogoutServlet:** Session invalidation and logout

### 2. Admin Dashboard
- Manage food items (CRUD operations)
- Approve/Reject user requests
- View all users and distribution requests
- Track food distribution statistics

### 3. User Portal
- Browse available food items with search functionality
- Submit requests for food items
- Track request status (Pending, Approved, Rejected, Collected)
- Update profile information

### 4. Security Features
- Password hashing using MD5 (MD5 for teaching; use bcrypt in production)
- Session management with HTTP-only cookies
- Input validation to prevent SQL injection
- Duplicate email/phone number prevention

### 5. Responsive Design
- Flexbox-based responsive layout
- Media queries for mobile, tablet, and desktop
- NO Bootstrap or external CSS frameworks
- Pure CSS with valid accessibility standards

## Database Schema Overview

### Users Table
- Stores admin and regular user accounts
- Tracks user roles and approval status
- Maintains contact information

### Food Items Table
- Lists available food items for redistribution
- Tracks quantity, expiry date, and location
- Maintains item status (AVAILABLE, RESERVED, DISTRIBUTED)

### Requests Table
- Records user requests for food items
- Tracks request status and collection information
- Links users to food items with quantity tracking

## MVC Architecture Implementation

### Models (com.ecofeast.models)
- **User.java:** Represents users with role-based properties
- **FoodItem.java:** Represents food products with availability status
- **Request.java:** Represents food distribution requests

### Views (JSP Pages)
- HTML/JSP templates with embedded Java logic
- Responsive CSS styling using Flexbox
- Form handling and data display

### Controllers (Servlets)
- **LoginServlet:** Handles authentication
- **RegisterServlet:** Processes user registration
- **AdminController:** Manages admin operations
- **UserController:** Manages user portal operations
- **Filters:** RBACFilter enforces authorization rules

## Common Troubleshooting

### Issue: Database Connection Failed
**Solution:** 
1. Verify MySQL is running
2. Check credentials in DatabaseUtil.java
3. Ensure ecofeast database exists

### Issue: 404 Error on pages
**Solution:**
1. Verify Tomcat is running: `http://localhost:8080/`
2. Check that WAR file is deployed correctly
3. Review Tomcat logs: `$TOMCAT_HOME/logs/catalina.out`

### Issue: JSP pages not rendering
**Solution:**
1. Verify web.xml is in WEB-INF directory
2. Check JSP syntax for unclosed tags
3. Review application logs in Tomcat

### Issue: CSS not loading
**Solution:**
1. Check file path in JSP: `/css/style.css`
2. Verify CSS file exists in webapp/css directory
3. Clear browser cache (Ctrl+Shift+Delete)

## Coursework Requirements Compliance

### ✓ Tech Stack
- [x] Java (J2EE/Jakarta EE)
- [x] MySQL with JDBC
- [x] JSP for views
- [x] Pure CSS (NO Bootstrap)
- [x] Minimal JavaScript

### ✓ Architecture
- [x] Strict MVC Pattern
- [x] Model classes with getters/setters
- [x] Servlet Controllers
- [x] JSP Views with proper separation

### ✓ Features
- [x] User authentication & registration
- [x] Role-based access control (RBAC)
- [x] Admin dashboard with CRUD
- [x] User portal for food requests
- [x] Session management
- [x] Input validation & error handling
- [x] Responsive design with Flexbox

### ✓ Security
- [x] Passwords hashed (MD5 for learning)
- [x] Servlet Filter for authorization
- [x] Duplicate email/phone checks
- [x] User-friendly error messages

## Ethical Aspect: Sustainable Food Redistribution
EcoFeast addresses the global issue of food waste and food insecurity by:
1. **Reducing Food Waste:** Enables businesses to redistribute surplus food instead of discarding it
2. **Community Support:** Provides food access to those in need
3. **Ethical Sourcing:** Ensures food is from verified, legitimate sources
4. **Transparency:** Admin oversight ensures fair distribution and food safety

## Development Notes

### Production Recommendations
1. Replace MD5 with bcrypt/PBKDF2 for passwords
2. Implement HTTPS/SSL for secure communication
3. Add parameterized queries throughout (already using PreparedStatement)
4. Implement connection pooling (HikariCP or C3P0)
5. Add logging framework (SLF4J with Logback)
6. Implement input sanitization for XSS prevention

### Future Enhancements
1. Email notifications for request status changes
2. Admin reports and analytics dashboard
3. Geolocation for food item search
4. File upload for food item images
5. QR code generation for item tracking
6. Mobile app version

## Support & Contact
For issues or questions about this implementation, refer to:
- Java Servlet API Documentation
- JSP Specification
- MySQL JDBC Documentation
- Tomcat Administration Guide

---
**Project Status:** Complete implementation for academic coursework  
**Last Updated:** March 2024  
**Version:** 1.0
