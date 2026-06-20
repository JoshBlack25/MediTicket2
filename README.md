# MediTicket2

## Project Overview

MediTicket2 is a healthcare management system developed using Java, Spring Boot, Maven, and Domain-Driven Design (DDD) principles.

The system manages:

* Patients
* Doctors
* Clinic Staff
* Appointments
* Patient Tickets
* Ticket Status Tracking
* Payments
* Notifications

The project follows a layered architecture consisting of:

* Domain Layer
* Factory Layer
* Repository Layer
* Service Layer
* Controller Layer
* Test Layer

---

## UML Class Diagram

Insert the approved UML diagram below.

```markdown
![MediTicket2 UML](MediTicket2.png)
```

---

## Technology Stack

* Java 21
* Spring Boot
* Maven
* JPA
* JUnit 5
* Mockito
* GitHub
* IntelliJ IDEA

---

## Project Structure

```text
src
└── main
    └── java
        └── za.ac.cput
            ├── domain
            ├── factory
            ├── repository
            ├── service
            ├── controller
            └── util
```

---

## Team Members and Entity Allocation

| Team Member            | Student Number | Entity                            |
| ---------------------- | -------------- | --------------------------------- |
| Abdullahi Raage Farah  | 230971091      | Payment                           |
| Aidan Barends          | 230155639      | Patient                           |
| Jaden Clayton Abrahams | 222206721      | Doctor                            |
| Joshua Peter Bonzet    | 221312536      | Appointment                       |
| Joshua Reid Adams      | 230317693      | PatientTicket, TicketStatus, User |
| Matthew Barron         | 230398863      | ClinicStaff                       |
| Raul Jaaim Everts      | 230270564      | Notification                      |

---

## Branch Naming Convention

Each team member will work on their own branch.

**Format**

```text
INITIALS-StudentNumber
```

**Branches**

```text
ARF-230971091
AB-230155639
JCA-222206721
JPB-221312536
JRA-230317693
MB-230398863
RJE-230270564
```

---

## Development Workflow

### 1. Pull the Latest Code

```bash
git checkout main
git pull origin main
```

### 2. Switch to Your Branch

```bash
git checkout <branch-name>
```

### 3. Complete Your Assigned Tasks

Implement the required repository, service, controller and tests.

### 4. Commit Changes

```bash
git add .
git commit -m "Completed assigned implementation"
```

### 5. Push Changes

```bash
git push origin <branch-name>
```

### 6. Create a Pull Request

Submit a Pull Request from your branch to `main`.

### 7. Await Review

The Team Lead will review and merge approved pull requests.

---

## Deliverables Per Entity

Each member is responsible for:

### Repository Layer

* Repository Interface

### Service Layer

* Service Interface
* Service Implementation

### Controller Layer

* REST Controller

### Testing

* Factory Test
* Service Test
* Controller Test

---

## Testing Frameworks

The project uses:

* JUnit 5
* Mockito
* Spring Boot Test

All implementations must include appropriate test classes.

---

## GitHub Milestone

### Domain Milestone

**Due Date:** 21 June 2026

All assigned tasks and pull requests must be completed and merged before the milestone deadline.

---

## Team Lead Responsibilities

* Maintain repository structure
* Review pull requests
* Merge approved code
* Create and manage milestones
* Create and assign issues
* Ensure code quality standards are followed

---

## Contributors

* Joshua Reid Adams (Team Lead)
* Abdullahi Raage Farah
* Aidan Barends
* Jaden Clayton Abrahams
* Joshua Peter Bonzet
* Matthew Barron
* Raul Jaaim Everts
