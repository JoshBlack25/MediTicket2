package za.ac.cput.domain.enums;
//Raul Everts 230270565
public enum NotificationStatus {
    SENT,
    FAILED,
    PENDING,
    READ

    //sent = to customer(both sides)
    //failed = notif hasnt been sent (both sides)
    //pending = sent but not read/opened (both sides)
    //read = after notif is opened
}
