package chotot.prect.aptech.zinzamessenger.model;

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
    private long mTime;

    public Message(int mId, int mIdSender, int mIdRecipient, int mType, String mContent, long mTime) {
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

    public long getmTime() {
        return mTime;
    }

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }
}
