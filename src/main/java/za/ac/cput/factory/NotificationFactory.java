package za.ac.cput.factory;

import za.ac.cput.domain.Appointment;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.domain.user.Doctor;
import za.ac.cput.domain.user.Patient;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;

public class NotificationFactory {

    public static Notification createNotification(int notificationId,
                                                  NotificationType notificationType,
                                                  NotificationStatus notificationStatus,
                                                  String notificationMessage,
                                                  Patient patient,
                                                  Doctor doctor,
                                                  ClinicStaff clinicStaff,
                                                  PatientTicket ticket,
                                                  Appointment appointment,
                                                  LocalDateTime notificationDate) {

        // notificationId is DB-generated (@GeneratedValue IDENTITY) — it's
        // always 0 on creation, so it can't be validated here. Removed check.
        if (!Helper.isValidObject(notificationType)) return null;
        if (!Helper.isValidObject(notificationStatus)) return null;
        if (Helper.isNullOrEmpty(notificationMessage)) return null;
        if (!Helper.isValidObject(ticket)) return null;
        if (!Helper.isValidObject(appointment)) return null;
        if (!Helper.isValidObject(notificationDate)) return null;

        int recipientCount = 0;
        if (patient != null) recipientCount++;
        if (doctor != null) recipientCount++;
        if (clinicStaff != null) recipientCount++;
        if (recipientCount != 1) return null;

        return new Notification.Builder()
                .setNotificationId(notificationId)
                .setNotificationType(notificationType)
                .setNotificationStatus(notificationStatus)
                .setNotificationMessage(notificationMessage)
                .setPatient(patient)
                .setDoctor(doctor)
                .setClinicStaff(clinicStaff)
                .setTicket(ticket)
                .setAppointment(appointment)
                .setNotificationDate(notificationDate)
                .build();
    }
}