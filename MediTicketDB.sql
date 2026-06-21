CREATE DATABASE IF NOT EXISTS mediticket_db;
USE mediticket_db;

-- 1. Patient Table (Inherits User fields + flattens @Embedded Name object)
CREATE TABLE patient (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    cell_phone VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    dob DATE,
    account_status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL,
    date_registered DATE,
    emergency_contact VARCHAR(100)
);

-- 2. Doctor Table (Inherits User fields + flattens @Embedded Name object)
CREATE TABLE doctor (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    cell_phone VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    dob DATE,
    account_status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL,
    specialty VARCHAR(100),
    license_number VARCHAR(100) UNIQUE NOT NULL
);

-- 3. Clinic Staff Table (Inherits User fields + flattens @Embedded Name object)
CREATE TABLE clinic_staff (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    cell_phone VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    dob DATE,
    account_status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL,
    staff_role VARCHAR(100),
    department VARCHAR(100)
);

-- 4. Appointment Table
CREATE TABLE appointment (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_date DATE,
    appointment_time TIME,
    confirmation_status ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'RESCHEDULED') NOT NULL,
    doctor_id INT,
    staff_id INT,
    FOREIGN KEY (doctor_id) REFERENCES doctor(user_id) ON DELETE SET NULL,
    FOREIGN KEY (staff_id) REFERENCES clinic_staff(user_id) ON DELETE SET NULL
);

-- 5. Patient Ticket Table
CREATE TABLE patient_ticket (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_description TEXT,
    ticket_created_date DATETIME,
    patient_id INT,
    appointment_id INT,
    FOREIGN KEY (patient_id) REFERENCES patient(user_id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id) ON DELETE SET NULL
);

-- 6. Ticket Status Table (History Tracking)
CREATE TABLE ticket_status (
    status_id INT AUTO_INCREMENT PRIMARY KEY,
    status_type ENUM('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED', 'ESCALATED') NOT NULL,
    status_date DATETIME,
    ticket_id INT,
    FOREIGN KEY (ticket_id) REFERENCES patient_ticket(ticket_id) ON DELETE CASCADE
);

-- 7. Notification Table
CREATE TABLE notification (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    notification_type ENUM('SMS', 'EMAIL') NOT NULL,
    notification_status ENUM('SENT', 'FAILED', 'PENDING') NOT NULL,
    notification_message TEXT,
    patient_id INT,
    ticket_id INT,
    appointment_id INT,
    notification_date DATETIME,
    FOREIGN KEY (patient_id) REFERENCES patient(user_id) ON DELETE CASCADE,
    FOREIGN KEY (ticket_id) REFERENCES patient_ticket(ticket_id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id) ON DELETE CASCADE
);

-- 8. Payment Table
CREATE TABLE payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    payment_amount DECIMAL(10, 2) NOT NULL,
    payment_date DATETIME,
    payment_method ENUM('CASH', 'CARD', 'EFT', 'MEDICAL_AID') NOT NULL,
    payment_status ENUM('PAID', 'PENDING', 'FAILED', 'REFUNDED') NOT NULL,
    appointment_id INT,
    FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id) ON DELETE CASCADE
);
