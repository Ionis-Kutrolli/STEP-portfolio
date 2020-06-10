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
  /** The id for the authenticated user */
  String userId;

  public AuthenticationData(boolean loggedIn, String userEmail, String url, boolean isAdmin, String userId) {
    this.loggedIn = loggedIn;
    this.userEmail = userEmail;
    this.url = url;
    this.isAdmin = isAdmin;
    this.userId = userId;
  }

}
