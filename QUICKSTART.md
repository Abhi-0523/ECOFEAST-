# EcoFeast Quick Start Guide

## Prerequisites ⚠️ MISSING

Your system is missing:
- ❌ MySQL Server (database)
- ❌ Apache Tomcat 9+ (web server)  
- ❌ Maven 3.8+ (build tool)
- ✅ Java 17 (already installed)

## Quick Installation (15 minutes)

### Windows Users:

#### 1. Install MySQL (5 minutes)
```
Go to: https://dev.mysql.com/downloads/mysql/
Click: "Download" for MySQL Community Server (Windows)
Run installer and remember your password
```

#### 2. Install Tomcat (3 minutes)
```
Go to: https://tomcat.apache.org/download-90.cgi
Download: apache-tomcat-9.x.x-windows-x64.zip
Extract to: C:\tomcat
```

#### 3. Install Maven (3 minutes)
```
Go to: https://maven.apache.org/download.cgi
Download: apache-maven-3.8.x-bin.zip
Extract to: C:\maven
```

#### 4. Set Environment Variables (Windows)
```
1. Press: Win + X, then choose "System"
2. Click: "Advanced system settings"
3. Click: "Environment Variables"
4. Add new variables:
   - JAVA_HOME = C:\Program Files\Java\jdk-17.x
   - CATALINA_HOME = C:\tomcat
   - M2_HOME = C:\maven
5. Edit PATH and add:
   - %JAVA_HOME%\bin
   - %CATALINA_HOME%\bin
   - %M2_HOME%\bin
6. Click OK and RESTART PowerShell
```

### OR Run Setup Script:
```powershell
# PowerShell
. .\SETUP.ps1
```

---

## Build & Run Steps

### Step 1: Create Database
```bash
mysql -u root -p
Enter password: [your MySQL password]

source database/schema.sql;
```

### Step 2: Build Project
```bash
cd "C:\Users\ACER\OneDrive\Desktop\Advance Programming\EcoFeast"
mvn clean package
```

Expected output:
```
[INFO] BUILD SUCCESS
[INFO] Total time: ~30 seconds
[INFO] Created: target/ecofeast.war
```

### Step 3: Deploy to Tomcat
```bash
# Copy WAR file
Copy-Item target/ecofeast.war $env:CATALINA_HOME/webapps/

# Or manually: Copy target/ecofeast.war to C:\tomcat\webapps\
```

### Step 4: Start Tomcat
```bash
# Windows
$env:CATALINA_HOME\bin\startup.bat

# Or manually double-click: C:\tomcat\bin\startup.bat
```

Wait for message: "Tomcat started successfully"

### Step 5: Access Application
```
Open: http://localhost:8080/ecofeast/
```

---

## Test Credentials

### Admin Account
```
Email: admin@ecofeast.com
Password: admin123
```

### Donor Account
```
Email: donor@ecofeast.com
Password: Donor@123
```

### NGO Account
```
Email: ngo@ecofeast.com
Password: Ngo@1234
```

### Volunteer Account
```
Email: volunteer@ecofeast.com
Password: Vol@1234
```

---

## Troubleshooting

### "Maven not found"
→ Maven not installed or PATH not set. Run SETUP.ps1

### "MySQL connection refused"
→ MySQL not running. Start MySQL service:
```powershell
Start-Service MySQL80  # or your MySQL service name
```

### "Tomcat won't start"
→ Check if port 8080 is in use:
```powershell
netstat -ano | findstr :8080
# Kill process if needed: taskkill /PID [PID] /F
```

### "Database not found"
→ Run the schema.sql file:
```bash
mysql -u root -p < database/schema.sql
```

---

## Project Structure

```
EcoFeast/
├── database/
│   └── schema.sql              ← Database setup
├── src/main/java/com/ecofeast/
│   ├── controllers/            ← Servlets
│   ├── models/                 ← Data classes
│   ├── filters/                ← Security filter
│   └── util/                   ← Utilities
├── src/main/webapp/
│   ├── WEB-INF/web.xml        ← Config
│   ├── views/                  ← JSP pages
│   ├── css/style.css           ← Responsive design
│   └── index.jsp               ← Home page
├── pom.xml                     ← Maven config
├── target/
│   └── ecofeast.war            ← Deployable (after build)
└── README.md                   ← Full documentation
```

---

## Key Technologies

- **Java 17** ✅
- **JSP & Servlets** (MVC)
- **MySQL** Database
- **Apache Tomcat 9**
- **Pure CSS** (Flexbox, NO Bootstrap)
- **JDBC** for database access

---

## Architecture

```
Users Access Application
        ↓
    Web Server (Tomcat)
        ↓
    Servlet [Controller]
    - LoginServlet
    - AdminController
    - UserController
        ↓
    Models [Data Classes]
    - User
    - FoodItem
    - Request
        ↓
    Database (MySQL)
        ↓
    Views [JSP Pages]
    - HTML/CSS Response
```

---

## Support

For detailed information, see: **README.md**

For specific setup issues, refer to vendor documentation:
- MySQL: https://dev.mysql.com/doc/
- Tomcat: https://tomcat.apache.org/tomcat-9.0-doc/
- Maven: https://maven.apache.org/guides/
