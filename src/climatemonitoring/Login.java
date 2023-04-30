package src.climatemonitoring;

import java.util.LinkedList;

/**
 * Login Class used to access special function in the program
 */
public class Login {
	static boolean autenticato = false;
	static String userid;
	static String password;

	/**
	 * Empty Constructor to invoke later the function login
	 */
	public Login() {
	}

	/**
	 * Constructor for Login Class
	 *
	 * @param userid String
	 * @param password String
	 */
	public Login(String userid, String password) {
		Login.userid = userid;
		Login.password = password;
	}

	/**
	 * Get Global variable autenticato
	 *
	 * @return autenticato
	 */
	public static boolean isAutenticato() {
		return autenticato;
	}

	/**
	 * Function to read through the file and match the password and userid
	 *
	 * @return if the login is successful or not
	 */
	public boolean login() {
		LinkedList<Registration> list = Registration.readRegistrati();
		assert list != null;
		for (Registration r : list) {
			if (r.userid.equals(userid) && r.password.equals(password)) {
				autenticato = true;
				return true;
			}
		}
		return false;
	}


}
