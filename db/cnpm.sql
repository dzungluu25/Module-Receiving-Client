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

-- 3. Insert seed data into tblStaff (Service Staff, e.g., Doctors, Technicians, Nurses)
INSERT INTO tblStaff (id, name, phone, email, status) VALUES
(1, 'Dr. Nguyen Anh Tu', '0944445555', 'tuna@clinic.com', 'active'),
(2, 'Dr. Pham Thanh Ha', '0955556666', 'hapt@clinic.com', 'active'),
(3, 'Nurse Le Minh', '0966667777', 'minhl@clinic.com', 'active');

-- 4. Insert seed data into tblService
INSERT INTO tblService (id, name, unitPrice, category, description) VALUES
(1, 'General Health Checkup', 150000, 'Consultation', 'Standard comprehensive general physical examination'),
(2, 'Dental Scaling & Polishing', 300000, 'Dentistry', 'Deep dental cleaning, scaling, and polishing'),
(3, 'Blood Test Analysis', 200000, 'Laboratory', 'Complete Blood Count (CBC) and basic biochemical check'),
(4, 'Chest X-Ray Scanning', 250000, 'Radiology', 'Diagnostic chest or limb X-ray imaging scan');

-- 5. Insert seed data into tblServiceStaff (Mapping Staff to their Services and specializations)
INSERT INTO tblServiceStaff (id, staff_id, service_id, specialization) VALUES
(1, 1, 1, 'General Physician'),
(2, 2, 2, 'Dental Surgeon'),
(3, 3, 3, 'Laboratory Nurse');

-- 6. Insert seed data into tblMaterial (Medical Consumables)
INSERT INTO tblMaterial (id, name, unitPrice, category, description) VALUES
(1, 'Sterile Disposable Gloves', 2000, 'Consumables', 'Box of latex single-use examination gloves'),
(2, 'Disposable Syringe 5ml', 5000, 'Consumables', 'Single-use 5ml sterile syringe with needle'),
(3, '3-Ply Medical Face Mask', 1000, 'Consumables', 'High filtration protective surgical face mask'),
(4, 'Sterile Cotton Swab', 500, 'Consumables', 'Pack of double-ended medical cotton buds');

-- 7. Insert seed data into tblAppointment
INSERT INTO tblAppointment (id, status, appointmentTime, user_id, client_id) VALUES
(1, 'checked in', '2026-10-10 10:30:20', 1, 1),
(2, 'checked in', '2026-10-10 10:30:20', 1, 1),
(3, 'pending', '2026-10-12 14:00:00', 2, 2),
(4, 'pending', '2026-10-13 09:15:00', 1, 3);

-- 8. Insert seed data into tblAppointmentService
INSERT INTO tblAppointmentService (id, appointment_id, service_id, staff_id, quantity, price) VALUES
(1, 1, 1, 1, 1, 150000),
(2, 1, 3, 3, 1, 200000),
(3, 2, 2, 2, 1, 300000);

-- 9. Insert seed data into tblAppointmentMaterial
INSERT INTO tblAppointmentMaterial (id, appointment_id, material_id, quantity, price) VALUES
(1, 1, 1, 2, 2000),
(2, 1, 2, 1, 5000),
(3, 2, 1, 4, 2000);