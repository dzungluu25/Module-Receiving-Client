CREATE DATABASE IF NOT EXISTS cnpm;
USE cnpm;

CREATE TABLE IF NOT EXISTS tblUser(
    id INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    username VARCHAR(20),
    password VARCHAR(20),
    position VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS tblClient(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tblStaff(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(20),
    status VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS tblService(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unitPrice DOUBLE NOT NULL,
    category VARCHAR(50),
    description TEXT
);

CREATE TABLE IF NOT EXISTS tblServiceStaff(
    id INT AUTO_INCREMENT PRIMARY KEY,
    staff_id INT NOT NULL,
    service_id INT NOT NULL,
    specialization VARCHAR(100),
    FOREIGN KEY (staff_id) REFERENCES tblStaff(id),
    FOREIGN KEY (service_id) REFERENCES tblService(id)
);

CREATE TABLE IF NOT EXISTS tblMaterial(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unitPrice DOUBLE NOT NULL,
    category VARCHAR(50),
    description TEXT
);

CREATE TABLE IF NOT EXISTS tblAppointment(
    id INT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(10) DEFAULT 'pending',
    appointmentTime DATETIME NOT NULL,
    user_id INT,
    client_id INT,
    FOREIGN KEY (client_id) REFERENCES tblClient(id),
    FOREIGN KEY (user_id) REFERENCES tblUser(id)
);

-- Table Appointment - Service
CREATE TABLE IF NOT EXISTS tblAppointmentService(
    id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    service_id INT NOT NULL,
    staff_id INT NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    FOREIGN KEY (appointment_id) REFERENCES tblAppointment(id),
    FOREIGN KEY (service_id) REFERENCES tblService(id),
    FOREIGN KEY (staff_id) REFERENCES tblStaff(id)
);

-- Table Appointment - Material
CREATE TABLE IF NOT EXISTS tblAppointmentMaterial(
    id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    material_id int not null,
    quantity int not null,
    price double not null,
    foreign key (appointment_id) references tblAppointment(id),
    foreign key (material_id) references tblMaterial(id)
);

=========================================================================
INSERT SEED DATA FOR ALL TABLES
=========================================================================

-- 1. Insert seed data into tblUser (Receptionists and Managers)
INSERT INTO tblUser (id, name, username, password, position) VALUES
(1, 'Luu Anh Dung', 'dungluuanh', '123456', 'receptionist'),
(2, 'Nguyen Van B', 'receptionist2', '123456', 'receptionist'),
(3, 'Tran Thi C', 'manager1', '123456', 'manager');

-- 2. Insert seed data into tblClient (Patients)
INSERT INTO tblClient (id, name, phone, address) VALUES
(1, 'Nguyen Van A', '0987654321', 'Ha Noi'),
(2, 'Tran Thi B', '0912345678', 'TP Ho Chi Minh'),
(3, 'Le Van C', '0903334445', 'Da Nang'),
(4, 'Pham Minh D', '0977888999', 'Hai Phong');

-- 3. Insert seed data into tblStaff (Spa Therapists / Estheticians)
INSERT INTO tblStaff (id, name, phone, email, status) VALUES
(1, 'Therapist Nguyen Anh Tu', '0944445555', 'tuan@spaluxury.com', 'free'),
(2, 'Esthetician Pham Thanh Ha', '0955556666', 'haha@spaluxury.com', 'free'),
(3, 'Therapist Le Minh', '0966667777', 'minhl@spaluxury.com', 'free');

-- 4. Insert seed data into tblService (Spa Treatments & Therapies)
INSERT INTO tblService (id, name, unitPrice, category, description) VALUES
(1, 'Swedish Full Body Massage', 450000, 'Massage', 'Relaxing full body massage with warm therapeutic essential oils'),
(2, 'Organic Hydrating Facial', 350000, 'Facial', 'Deep facial cleansing, gentle organic exfoliation and herbal mask sheet'),
(3, 'Hot Stone Healing Therapy', 600000, 'Therapy', 'Muscle relief and circulation boost using heated natural basalt stones'),
(4, 'Aromatherapy Lavender Scrub', 300000, 'Body Care', 'Exfoliating body scrub with pure sea salt and lavender essential oils');

-- 5. Insert seed data into tblServiceStaff (Mapping Therapists to their specialties)
INSERT INTO tblServiceStaff (id, staff_id, service_id, specialization) VALUES
(1, 1, 1, 'Swedish Massage Specialist'),
(2, 2, 2, 'Organic Skincare Esthetician'),
(3, 3, 3, 'Hot Stone Therapy Practitioner'),
(4, 1, 4, 'Aromatherapy Body Care Practitioner'),
(5, 2, 4, 'Skincare & Exfoliation Expert'),
(6, 3, 1, 'Swedish Massage Practitioner');


-- 6. Insert seed data into tblMaterial (Spa Consumables / Oils / Skincare Products)
INSERT INTO tblMaterial (id, name, unitPrice, category, description) VALUES
(1, 'Aromatherapy Lavender Oil', 15000, 'Oils', 'Premium lavender therapeutic essential oil (10ml portion)'),
(2, 'Organic Facial Mask Pack', 25000, 'Skincare', 'Single-use herbal soothing and hydrating face sheet mask'),
(3, 'Disposable Spa Bed Sheet', 5000, 'Linen', 'Sanitary single-use non-woven disposable spa massage bed sheet'),
(4, 'Exfoliating Mineral Sea Salt', 12000, 'Body Care', 'Natural dead sea mineral exfoliating salt scrub (50g portion)');

-- 7. Insert seed data into tblAppointment (Spa Bookings)
INSERT INTO tblAppointment (id, status, appointmentTime, user_id, client_id) VALUES
(1, 'checked in', '2026-10-10 10:30:20', 1, 1),
(2, 'checked in', '2026-10-10 10:30:20', 1, 1),
(3, 'pending', '2026-10-12 14:00:00', 2, 2),
(4, 'pending', '2026-10-13 09:15:00', 1, 3);

-- 8. Insert seed data into tblAppointmentService
INSERT INTO tblAppointmentService (id, appointment_id, service_id, staff_id, quantity, price) VALUES
(1, 1, 1, 1, 1, 450000),
(2, 1, 3, 3, 1, 600000),
(3, 2, 2, 2, 1, 350000);

-- 9. Insert seed data into tblAppointmentMaterial
INSERT INTO tblAppointmentMaterial (id, appointment_id, material_id, quantity, price) VALUES
(1, 1, 1, 2, 15000),
(2, 1, 2, 1, 25000),
(3, 2, 1, 4, 15000);