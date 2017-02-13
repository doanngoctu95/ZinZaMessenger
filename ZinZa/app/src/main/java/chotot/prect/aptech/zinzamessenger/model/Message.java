package chotot.prect.aptech.zinzamessenger.model;

import java.io.Serializable;

/**
 * Created by ASUS on 02/13/2017.
 */

public class Message implements Serializable{
    private int id;
    private int idSender;
    private int idRecipient;
    private int type;
    private String content;
    private long time;

    public Message(int id, int idSender, int idRecipient, int type, String content, long time) {
        this.id = id;
        this.idSender = idSender;
        this.idRecipient = idRecipient;
        this.type = type;
        this.content = content;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public int getIdRecipient() {
        return idRecipient;
    }

    public void setIdRecipient(int idRecipient) {
        this.idRecipient = idRecipient;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
