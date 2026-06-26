package za.ac.cput.factory;

import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;

public class NotificationFactory {

    public static Notification createNotification(int notificationId,
                                                  NotificationType notificationType,
                                                  NotificationStatus notificationStatus,
                                                  String notificationMessage,
                                                  Patient patient,
                                                  PatientTicket ticket,
                                                  Appointment appointment,
                                                  LocalDateTime notificationDate) {

        if (!Helper.isValidInt(notificationId)) return null;
        if (!Helper.isValidObject(notificationType)) return null;
        if (!Helper.isValidObject(notificationStatus)) return null;
        if (Helper.isNullOrEmpty(notificationMessage)) return null;
        if (!Helper.isValidObject(patient)) return null;
        if (!Helper.isValidObject(ticket)) return null;
        if (!Helper.isValidObject(appointment)) return null;
        if (!Helper.isValidObject(notificationDate)) return null;

        return new Notification.Builder()
                .setNotificationId(notificationId)
                .setNotificationType(notificationType)
                .setNotificationStatus(notificationStatus)
                .setNotificationMessage(notificationMessage)
                .setPatient(patient)
                .setTicket(ticket)
                .setAppointment(appointment)
                .setNotificationDate(notificationDate)
                .build();
    }
}