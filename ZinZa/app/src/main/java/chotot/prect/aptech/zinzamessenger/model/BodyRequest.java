package chotot.prect.aptech.zinzamessenger.model;

/**
 * Created by ASUS on 02/28/2017.
 */

public class BodyRequest {
    private String to;
    private Notification notification;

    public BodyRequest(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
