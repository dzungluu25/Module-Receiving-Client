-- use cnpm;
--
-- create table if not exists tblUser(
-- 	id int primary key,
--     name varchar(20) not null,
--     username varchar(20),
-- 	password varchar(20),
--     position varchar(50)
-- );
-- create table if not exists tblClient(
-- 	id int auto_increment primary key,
--     name varchar(20) not null,
--     phone varchar(20),
--     address varchar(255)
-- );
-- create table if not exists tblStaff(
-- 	id int auto_increment primary key,
--     name varchar(50) not null,
--     phone varchar(20),
--     email varchar(20),
--     status varchar(10)
-- );
-- create table if not exists tblService(
-- 	id int auto_increment primary key,
--     name varchar(100) not null,
--     unitPrice DOUBLE not null,
--     category varchar(50),
--     description TEXT
-- );
-- create table if not exists tblServiceStaff(
-- 	id int auto_increment primary key,
--     staff_id int not null,
--     service_id int not null,
--     specialization varchar(100),
--     foreign key (staff_id) references tblService(id)
-- );

-- create table if not exists tblMaterial(
-- 	id int auto_increment primary key,
--     name varchar(100) not null,
--     unitPrice DOUBLE not null,
--     category varchar(50),
--     description TEXT
-- );
-- create table if not exists tblAppointment(
-- 	id int auto_increment primary key,
--     status boolean default false,
--     appointmentTime DATETIME not null,
--     user_id int,
--     client_id int,
--     foreign key (client_id) references tblClient(id),
--     foreign key (user_id) references tblUser(id)
-- );

-- -- Table Appointment - Service
-- create table if not exists tblAppointmentService(
-- 	id int auto_increment primary key,
--     appointment_id int not null,
--     service_id int not null,
--     staff_id int not null,
--     quantity int not null,
--     price double not null,
--     foreign key (appointment_id) references tblAppointment(id),
--     foreign key (service_id) references tblService(id),
--     foreign key (staff_id) references tblStaff(id)
-- );
-- Table Appointment - Material
-- create table if not exists tblAppointmentMaterial(
-- 	id int auto_increment primary key,
--     appointment_id int not null,
--     material_id int not null,
--     quantity int not null,
--     price double not null,
--     foreign key (appointment_id) references tblAppointment(id),
--     foreign key (material_id) references tblMaterial(id)
-- );

-- =========================================================================
-- INSERT SEED DATA FOR ALL TABLES
-- =========================================================================

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