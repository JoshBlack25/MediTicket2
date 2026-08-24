package za.ac.cput.service.impl;

import za.ac.cput.domain.Notification;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.user.Patient;

import java.util.List;

public interface INotificationService extends IService<Notification, Integer> {

    List<Notification> findByPatient(Patient patient);

    List<Notification> findByDoctor(Doctor doctor);

    List<Notification> findByClinicStaff(ClinicStaff clinicStaff);

    List<Notification> findByNotificationStatus(NotificationStatus notificationStatus);

    List<Notification> findByNotificationType(NotificationType notificationType);

}