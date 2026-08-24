package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.user.Patient;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByPatient(Patient patient);

    List<Notification> findByDoctor(Doctor doctor);

    List<Notification> findByClinicStaff(ClinicStaff clinicStaff);

    List<Notification> findByNotificationStatus(NotificationStatus notificationStatus);

    List<Notification> findByNotificationType(NotificationType notificationType);

}