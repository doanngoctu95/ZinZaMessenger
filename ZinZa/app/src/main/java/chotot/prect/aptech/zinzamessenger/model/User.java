package chotot.prect.aptech.zinzamessenger.model;

/**
 * Created by ASUS on 02/10/2017.
 */

public class User {
    private int mId;
    private String mUsername;
    private String mEmail;
    private String mPassword;
    private String mAvatar;
    private long mDateOfBirth;
    private int mStatus;
    private String mToken;
    private long mCreatedAt;

    public User(int mId, String mUsername, String mEmail, String mPassword, String mAvatar, long mDateOfBirth, int mStatus, String mToken, long mCreatedAt) {
        this.mId = mId;
        this.mUsername = mUsername;
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mAvatar = mAvatar;
        this.mDateOfBirth = mDateOfBirth;
        this.mStatus = mStatus;
        this.mToken = mToken;
        this.mCreatedAt = mCreatedAt;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmAvatar() {
        return mAvatar;
    }

    public void setmAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }

    public long getmDateOfBirth() {
        return mDateOfBirth;
    }

    public void setmDateOfBirth(long mDateOfBirth) {
        this.mDateOfBirth = mDateOfBirth;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }

    public long getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(long mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }
}
