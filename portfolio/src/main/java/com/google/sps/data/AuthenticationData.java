package com.google.sps.data;

public class AuthenticationData {
  /** Boolean whether anyone is logged in */
  boolean loggedIn;
  /** The email of the user logged in null if not logged in */
  String userEmail;
  /** Either the login url or the logout url */
  String url;

  public AuthenticationData(boolean loggedIn, String userEmail, String url) {
    this.loggedIn = loggedIn;
    this.userEmail = userEmail;
    this.url = url;
  }

}
