package src.climatemonitoring;

import java.util.LinkedList;
import java.util.Objects;

public class Login {
    String userid;
    String password;

    static boolean autenticato = false;

    public Login() {
    }

    public Login(String userid, String password) {
        this.userid = userid;
        this.password = password;
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

    public void setAutenticato(boolean autenticato) {
        Login.autenticato = autenticato;
    }

    public static boolean isAutenticato() {
        return autenticato;
    }


public boolean login() {
        LinkedList<Registration> list = Registration.readRegistrati();
        for (Registration r : list) {
            if (r.userid.equals(this.getUserid()) && r.password.equals(this.getPassword())){
                Login var = new Login(r.userid, r.password);
                var.setAutenticato(true);
                return true;
            };
        }
        return false;
    }

}
