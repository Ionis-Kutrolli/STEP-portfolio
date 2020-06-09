package com.google.sps.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@WebServlet("/auth")
public class HomeServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.setContentType("text/html");

      UserService userService = UserServiceFactory.getUserService();
      if (userService.isUserLoggedIn()) {
        String userEmail = userService.getCurrentUser().getEmail();
          
        response.getWriter().println("<p>Hello " + userEmail + "!</p>" );
      } else {
        String loginUrl = userService.createLoginURL(request.getHeader("referer"));
        response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
      }
    }
}