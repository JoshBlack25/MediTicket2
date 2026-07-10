package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.repository.NotificationRepository;
import za.ac.cput.service.impl.INotificationService;
import za.ac.cput.domain.Notification;
import za.ac.cput.domain.enums.NotificationStatus;
import za.ac.cput.domain.enums.NotificationType;
import za.ac.cput.domain.user.Patient;
import java.util.List;

@Service
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification create(Notification notification){
        return notificationRepository.save(notification);
    }

    @Override
    public Notification read(Integer id){
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public Notification update(Notification notification){
        return notificationRepository.save(notification);
    }

    @Override
    public void delete(Integer id){
        notificationRepository.deleteById(id);
    }

    @Override
    public List<Notification> getAll(){
        return notificationRepository.findAll();
    }

    @Override
    public  List<Notification> findByPatient(Patient patient){
        return notificationRepository.findByPatient(patient);
    }

    @Override
    public  List<Notification> findByNotificationType(NotificationType notificationType){
        return notificationRepository.findByNotificationType(notificationType);
    }

    @Override
    public  List<Notification> findByNotificationStatus(NotificationStatus notificationStatus){
        return notificationRepository.findByNotificationStatus(notificationStatus);
    }

}