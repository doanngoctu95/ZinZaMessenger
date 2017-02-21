package chotot.prect.aptech.zinzamessenger.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * Created by ASUS on 02/13/2017.
 */

public class Message implements Serializable{
    private int mId;
    private int mIdSender;
    private int mIdRecipient;
    private int mType;
    private String mContent;
    private String mTime;
    private int mRecipientOrSenderStatus;
    public Message(int mId, int mIdSender, int mIdRecipient, int mType, String mContent, String mTime) {
        this.mId = mId;
        this.mIdSender = mIdSender;
        this.mIdRecipient = mIdRecipient;
        this.mType = mType;
        this.mContent = mContent;
        this.mTime = mTime;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmIdSender() {
        return mIdSender;
    }

    public void setmIdSender(int mIdSender) {
        this.mIdSender = mIdSender;
    }

    public int getmIdRecipient() {
        return mIdRecipient;
    }

    public void setmIdRecipient(int mIdRecipient) {
        this.mIdRecipient = mIdRecipient;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }
    @Exclude
    public int getRecipientOrSenderStatus() {
        return mRecipientOrSenderStatus;
    }
    public void setRecipientOrSenderStatus(int recipientOrSenderStatus) {
        this.mRecipientOrSenderStatus = recipientOrSenderStatus;
    }
}
