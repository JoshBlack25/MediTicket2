package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import za.ac.cput.domain.user.Patient;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByPatient(Patient patient);

    List<Notification> findByNotificationStatus(NotificationStatus notificationStatus);

    List<Notification> findByNotificationType(NotificationType notificationtype);


}