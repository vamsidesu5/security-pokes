package vamsidesu5.com.spokesv2.Model;


import java.util.LinkedList;

public class NotificationManager {
    private static NotificationManager notificationManager = new NotificationManager();
    private static LinkedList<Notification> notifications = new LinkedList<Notification>();

    public static NotificationManager getInstance() {
        return notificationManager;
    }

    public static void updateNotification(Notification notification) {
        notifications.addFirst(notification);
    }

    public static LinkedList<Notification> getNotifications() {
        return notifications;
    }
}
