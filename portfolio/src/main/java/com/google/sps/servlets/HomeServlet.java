package com.google.sps.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@WebServlet("/auth")
public class HomeServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();
    boolean loggedIn = false;
    String userEmail = null;
    String url;
    if (userService.isUserLoggedIn()) {
      loggedIn = true;
      userEmail = userService.getCurrentUser().getEmail();
      url = userService.createLogoutURL(request.getHeader("referer"));
    } else {
      url = userService.createLoginURL(request.getHeader("referer"));
    }

    GetAuthenticationData data = new GetAuthenticationData(loggedIn, userEmail, url);
    Gson gson = new Gson();
    String json = gson.toJson(data);
    response.setContentType("application/json");
    response.getWriter().println(json);
  }
}

class GetAuthenticationData {
  /** Boolean whether anyone is logged in */
  boolean loggedIn;
  /** The email of the user logged in null if not logged in */
  String userEmail;
  /** Either the login url or the logout url */
  String url;

  public GetAuthenticationData(boolean loggedIn, String userEmail, String url) {
    this.loggedIn = loggedIn;
    this.userEmail = userEmail;
    this.url = url;
  }
}
