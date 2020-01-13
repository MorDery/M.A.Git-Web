package dataFromServlet;

import logic.notifications.Notification;

import java.util.LinkedList;
import java.util.List;

public class NotificationListInformation {

    private List<String> m_Notifications = new LinkedList<>();

    public NotificationListInformation(List<Notification> i_Notifications,boolean i_PageSelector){
        //true means secondPage Notifications
        if(i_PageSelector) {
            for (Notification notification : i_Notifications) {
                if (!notification.getIsShownOnSecondPage()) {
                    notification.setIsShownOnSecondPage(true);
                    m_Notifications.add(notification.toString());
                }
            }
        }
        else{
            for (Notification notification : i_Notifications) {
                if (!notification.getIsShownOnThirdPage()) {
                    notification.setIsShownOnThirdPage(true);
                    m_Notifications.add(notification.toString());
                }
            }
        }
    }
}
