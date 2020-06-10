package com.google.sps.data;

/**
 * Class that stores data for /auth request
 */
public class AuthenticationData {
  /** Boolean whether anyone is logged in */
  boolean loggedIn;
  /** The email of the user logged in null if not logged in */
  String userEmail;
  /** Either the login url or the logout url */
  String url;
  /** Booelean whether the user is admin */
  boolean isAdmin;

  public AuthenticationData(boolean loggedIn, String userEmail, String url, boolean isAdmin) {
    this.loggedIn = loggedIn;
    this.userEmail = userEmail;
    this.url = url;
    this.isAdmin = isAdmin;
  }

}
