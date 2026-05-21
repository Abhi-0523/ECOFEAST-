-- ============================================================
-- EcoFeast - Sustainable Food Redistribution System
-- Full Normalized Database Schema v2.0
-- Java EE MVC Coursework | MySQL 8+
-- ============================================================

-- Create and select database
DROP DATABASE IF EXISTS ecofeast;
CREATE DATABASE ecofeast
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE ecofeast;

-- ============================================================
-- TABLE 1: roles
-- ============================================================
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS volunteer_tasks;
DROP TABLE IF EXISTS donation_requests;
DROP TABLE IF EXISTS food_donations;
DROP TABLE IF EXISTS food_categories;
DROP TABLE IF EXISTS announcements;
DROP TABLE IF EXISTS contact_messages;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

CREATE TABLE roles (
                       role_id   INT          AUTO_INCREMENT PRIMARY KEY,
                       role_name VARCHAR(30)  NOT NULL UNIQUE  COMMENT 'ADMIN | DONOR | NGO | VOLUNTEER',
                       description TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE 2: users
-- Core user entity shared by all roles
-- ============================================================
CREATE TABLE users (
                       user_id        INT           AUTO_INCREMENT PRIMARY KEY,
                       role_id        INT           NOT NULL,
                       full_name      VARCHAR(100)  NOT NULL,
                       email          VARCHAR(150)  NOT NULL UNIQUE,
                       phone          VARCHAR(20)   NOT NULL UNIQUE,
                       password_hash  VARCHAR(255)  NOT NULL,
                       organization   VARCHAR(150),
                       address        VARCHAR(255),
                       city           VARCHAR(80),
                       state          VARCHAR(80),
                       zip_code       VARCHAR(20),
                       profile_image  VARCHAR(255),
                       account_status ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
                       is_active      BOOLEAN       DEFAULT TRUE,
                       created_at     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                       updated_at     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(role_id) ON UPDATE CASCADE,
                       INDEX idx_email  (email),
                       INDEX idx_role   (role_id),
                       INDEX idx_status (account_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE 3: food_categories
-- Lookup table for food classification
-- ============================================================
CREATE TABLE food_categories (
                                 category_id   INT          AUTO_INCREMENT PRIMARY KEY,
                                 category_name VARCHAR(80)  NOT NULL UNIQUE,
                                 description   TEXT,
                                 icon_class    VARCHAR(50)  COMMENT 'CSS icon class for UI',
                                 is_active     BOOLEAN      DEFAULT TRUE,
                                 created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE 4: food_donations
-- Food items listed by donors
-- ============================================================
CREATE TABLE food_donations (
                                donation_id     INT          AUTO_INCREMENT PRIMARY KEY,
                                donor_id        INT          NOT NULL,
                                category_id     INT          NOT NULL,
                                food_name       VARCHAR(150) NOT NULL,
                                description     TEXT,
                                quantity        INT          NOT NULL CHECK (quantity > 0),
                                quantity_unit   VARCHAR(30)  DEFAULT 'kg' COMMENT 'kg, pieces, litres, boxes',
                                expiry_time     DATETIME     NOT NULL,
                                pickup_location VARCHAR(255) NOT NULL,
                                pickup_city     VARCHAR(80),
                                image_url       VARCHAR(255),
                                status          ENUM('AVAILABLE','REQUESTED','APPROVED','DISTRIBUTED','EXPIRED','CANCELLED')
                    DEFAULT 'AVAILABLE',
                                created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                                updated_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                CONSTRAINT fk_donation_donor    FOREIGN KEY (donor_id)    REFERENCES users(user_id)            ON DELETE CASCADE,
                                CONSTRAINT fk_donation_category FOREIGN KEY (category_id) REFERENCES food_categories(category_id) ON UPDATE CASCADE,
                                INDEX idx_donor    (donor_id),
                                INDEX idx_category (category_id),
                                INDEX idx_status   (status),
                                INDEX idx_expiry   (expiry_time),
                                INDEX idx_city     (pickup_city)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE 5: donation_requests
-- Requests placed by NGOs on food donations
-- ============================================================
CREATE TABLE donation_requests (
                                   request_id         INT    AUTO_INCREMENT PRIMARY KEY,
                                   donation_id        INT    NOT NULL,
                                   ngo_id             INT    NOT NULL,
                                   quantity_requested INT    NOT NULL CHECK (quantity_requested > 0),
                                   request_message    TEXT,
                                   status             ENUM('PENDING','APPROVED','REJECTED','COLLECTED','CANCELLED')
                       DEFAULT 'PENDING',
                                   rejection_reason   VARCHAR(255),
                                   requested_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   responded_at       TIMESTAMP NULL,
                                   collected_at       TIMESTAMP NULL,
                                   CONSTRAINT fk_req_donation FOREIGN KEY (donation_id) REFERENCES food_donations(donation_id) ON DELETE CASCADE,
                                   CONSTRAINT fk_req_ngo      FOREIGN KEY (ngo_id)      REFERENCES users(user_id)              ON DELETE CASCADE,
                                   UNIQUE KEY uq_ngo_donation (ngo_id, donation_id),
                                   INDEX idx_donation (donation_id),
                                   INDEX idx_ngo      (ngo_id),
                                   INDEX idx_status   (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE 6: volunteer_tasks
-- Pickup/delivery tasks assigned to volunteers
-- ============================================================
CREATE TABLE volunteer_tasks (
                                 task_id       INT    AUTO_INCREMENT PRIMARY KEY,
                                 request_id    INT    NOT NULL,
                                 volunteer_id  INT,
                                 task_type     ENUM('PICKUP','DELIVERY') DEFAULT 'PICKUP',
                                 pickup_address    VARCHAR(255),
                                 delivery_address  VARCHAR(255),
                                 scheduled_time    DATETIME,
                                 accepted_at       TIMESTAMP NULL,
                                 completed_at      TIMESTAMP NULL,
                                 status        ENUM('OPEN','ACCEPTED','IN_PROGRESS','COMPLETED','CANCELLED')
                  DEFAULT 'OPEN',
                                 notes         TEXT,
                                 created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_task_request   FOREIGN KEY (request_id)   REFERENCES donation_requests(request_id) ON DELETE CASCADE,
                                 CONSTRAINT fk_task_volunteer FOREIGN KEY (volunteer_id) REFERENCES users(user_id)                ON DELETE SET NULL,
                                 INDEX idx_volunteer (volunteer_id),
                                 INDEX idx_status    (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE 7: deliveries
-- Delivery completion records
-- ============================================================
CREATE TABLE deliveries (
                            delivery_id   INT    AUTO_INCREMENT PRIMARY KEY,
                            task_id       INT    NOT NULL UNIQUE,
                            volunteer_id  INT    NOT NULL,
                            delivered_at  TIMESTAMP NULL,
                            proof_image   VARCHAR(255),
                            recipient_name  VARCHAR(100),
                            recipient_sign  VARCHAR(255),
                            delivery_notes  TEXT,
                            status        ENUM('PENDING','DELIVERED','FAILED') DEFAULT 'PENDING',
                            created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_delivery_task      FOREIGN KEY (task_id)      REFERENCES volunteer_tasks(task_id) ON DELETE CASCADE,
                            CONSTRAINT fk_delivery_volunteer FOREIGN KEY (volunteer_id) REFERENCES users(user_id)           ON DELETE CASCADE,
                            INDEX idx_volunteer (volunteer_id),
                            INDEX idx_status    (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE 8: wishlist
-- NGO-saved / favorited food donations
-- ============================================================
CREATE TABLE wishlist (
                          wishlist_id  INT    AUTO_INCREMENT PRIMARY KEY,
                          user_id      INT    NOT NULL,
                          donation_id  INT    NOT NULL,
                          added_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_wl_user     FOREIGN KEY (user_id)     REFERENCES users(user_id)         ON DELETE CASCADE,
                          CONSTRAINT fk_wl_donation FOREIGN KEY (donation_id) REFERENCES food_donations(donation_id) ON DELETE CASCADE,
                          UNIQUE KEY uq_user_donation (user_id, donation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE 9: notifications
-- In-app notification system for all roles
-- ============================================================
CREATE TABLE notifications (
                               notification_id INT    AUTO_INCREMENT PRIMARY KEY,
                               user_id         INT    NOT NULL,
                               title           VARCHAR(200) NOT NULL,
                               message         TEXT   NOT NULL,
                               type            ENUM('INFO','SUCCESS','WARNING','ERROR') DEFAULT 'INFO',
                               is_read         BOOLEAN DEFAULT FALSE,
                               link_url        VARCHAR(255),
                               created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                               INDEX idx_user_read (user_id, is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE 10: contact_messages
-- Contact form submissions from public/users
-- ============================================================
CREATE TABLE contact_messages (
                                  message_id  INT    AUTO_INCREMENT PRIMARY KEY,
                                  sender_name  VARCHAR(100) NOT NULL,
                                  sender_email VARCHAR(150) NOT NULL,
                                  subject      VARCHAR(200),
                                  message      TEXT  NOT NULL,
                                  is_replied   BOOLEAN DEFAULT FALSE,
                                  admin_reply  TEXT,
                                  replied_at   TIMESTAMP NULL,
                                  created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  INDEX idx_replied (is_replied)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE 11: feedback
-- User feedback / ratings on donations / system
-- ============================================================
CREATE TABLE feedback (
                          feedback_id  INT    AUTO_INCREMENT PRIMARY KEY,
                          user_id      INT    NOT NULL,
                          donation_id  INT,
                          rating       TINYINT CHECK (rating BETWEEN 1 AND 5),
                          comment      TEXT,
                          is_approved  BOOLEAN DEFAULT FALSE,
                          created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_fb_user     FOREIGN KEY (user_id)     REFERENCES users(user_id)             ON DELETE CASCADE,
                          CONSTRAINT fk_fb_donation FOREIGN KEY (donation_id) REFERENCES food_donations(donation_id) ON DELETE SET NULL,
                          INDEX idx_user     (user_id),
                          INDEX idx_donation (donation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE 12: announcements
-- System-wide announcements posted by admin
-- ============================================================
CREATE TABLE announcements (
                               announcement_id INT    AUTO_INCREMENT PRIMARY KEY,
                               admin_id        INT    NOT NULL,
                               title           VARCHAR(200) NOT NULL,
                               content         TEXT   NOT NULL,
                               target_role     VARCHAR(30) DEFAULT 'ALL' COMMENT 'ALL | DONOR | NGO | VOLUNTEER',
                               is_active       BOOLEAN DEFAULT TRUE,
                               published_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               expires_at      TIMESTAMP NULL,
                               CONSTRAINT fk_ann_admin FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE CASCADE,
                               INDEX idx_role   (target_role),
                               INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- SEED DATA
-- ============================================================

-- Insert roles
INSERT INTO roles (role_name, description) VALUES
                                               ('ADMIN',     'System administrator with full access'),
                                               ('DONOR',     'Food donor: restaurant, hotel, supermarket'),
                                               ('NGO',       'Non-government organization requesting food'),
                                               ('VOLUNTEER', 'Volunteer handling pickup and delivery');

-- Insert food categories
INSERT INTO food_categories (category_name, description, icon_class) VALUES
                                                                         ('Cooked Food',    'Prepared meals and cooked dishes',       'icon-cooked'),
                                                                         ('Raw Vegetables', 'Fresh vegetables and produce',           'icon-veg'),
                                                                         ('Bakery Items',   'Bread, pastries, and baked goods',       'icon-bakery'),
                                                                         ('Dairy Products', 'Milk, cheese, yogurt, and dairy',        'icon-dairy'),
                                                                         ('Fruits',         'Fresh and seasonal fruits',              'icon-fruit'),
                                                                         ('Packaged Food',  'Sealed and packaged food products',      'icon-package'),
                                                                         ('Beverages',      'Juices, drinks, and liquid foods',       'icon-beverage'),
                                                                         ('Grains & Rice',  'Rice, wheat, lentils, and dry grains',  'icon-grain');

-- Insert default admin user (password: admin123)
-- SHA-256 hash: use BCrypt in production; this is MD5 for legacy support
INSERT INTO users (role_id, full_name, email, phone, password_hash, account_status, is_active)
VALUES (1, 'System Administrator', 'admin@ecofeast.com', '9800000001',
        '0192023a7bbd73250516f069df18b500', 'APPROVED', TRUE);

-- Insert sample donor (password: Donor@123)
INSERT INTO users (role_id, full_name, email, phone, password_hash, organization, city, account_status, is_active)
VALUES (2, 'Green Garden Restaurant', 'donor@ecofeast.com', '9800000002',
        '599e465dfac898ad52a7f0d1f234ac93', 'Green Garden Pvt. Ltd.', 'Kathmandu', 'APPROVED', TRUE);

-- Insert sample NGO (password: Ngo@1234)
INSERT INTO users (role_id, full_name, email, phone, password_hash, organization, city, account_status, is_active)
VALUES (3, 'Hope Foundation', 'ngo@ecofeast.com', '9800000003',
        '40af2b9a850cf5528a12606fd463ae7a', 'Hope Foundation Nepal', 'Lalitpur', 'APPROVED', TRUE);

-- Insert sample volunteer (password: Vol@1234)
INSERT INTO users (role_id, full_name, email, phone, password_hash, city, account_status, is_active)
VALUES (4, 'Ram Volunteer', 'volunteer@ecofeast.com', '9800000004',
        '0b509c8c771d5e0e06deb8b76ab6c1f7', 'Bhaktapur', 'APPROVED', TRUE);
