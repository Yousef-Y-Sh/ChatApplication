package com.yousef.sh.chatapplication.moudle;


import android.os.Parcel;
import android.os.Parcelable;

public class UserM implements Parcelable {
    String id;
    String name;
    String email;
    String phone;
    String password;
    String imgUri;
    String Token;

    public UserM(String id, String name, String email, String phone, String password, String imgUri) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.imgUri = imgUri;
    }

    public UserM() {
    }

    protected UserM(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        password = in.readString();
        imgUri = in.readString();
        Token = in.readString();
    }

    public static final Creator<UserM> CREATOR = new Creator<UserM>() {
        @Override
        public UserM createFromParcel(Parcel in) {
            return new UserM(in);
        }

        @Override
        public UserM[] newArray(int size) {
            return new UserM[size];
        }
    };

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(password);
        parcel.writeString(imgUri);
        parcel.writeString(Token);
    }
}
