package src.climatemonitoring;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class Login {
    static boolean autenticato = false;
    String userid;
    String password;

    public Login() {
    }

    public Login(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }

    public static boolean isAutenticato() {
        return autenticato;
    }

    public void setAutenticato(boolean autenticato) {
        Login.autenticato = autenticato;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean login() {
        LinkedList<Registration> list = Registration.readRegistrati();
        for (Registration r : list) {
            if (r.userid.equals(this.getUserid()) && r.password.equals(this.getPassword())) {
                Login var = new Login(r.userid, r.password);
                var.setAutenticato(true);
                return true;
            }
        }
        return false;
    }



}
