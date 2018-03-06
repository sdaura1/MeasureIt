package com.example.shaheed.mtesting;

import com.google.firebase.firestore.DocumentReference;

/**
 * Created by shaheed on 2/10/18.
 */

public class Users {

    String name;
    int shirt, trouser, hand, shoulder;
    long phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShirt() {
        return shirt;
    }

    public void setShirt(int shirt) {
        this.shirt = shirt;
    }

    public int getTrouser() {
        return trouser;
    }

    public void setTrouser(int trouser) {
        this.trouser = trouser;
    }

    public int getHand() {
        return hand;
    }

    public void setHand(int hand) {
        this.hand = hand;
    }

    public int getShoulder() {
        return shoulder;
    }

    public void setShoulder(int shoulder) {
        this.shoulder = shoulder;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
}
