package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling the deletion of one selected comment
 */
@WebServlet("/delete-comment")
public class DeleteCommentServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      long id = Long.parseLong(request.getParameter("id"));

      Key commentEntityKey = KeyFactory.createKey("Comment", id);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      Entity commentEntity;
      try {
        commentEntity = datastore.get(commentEntityKey);
      } catch (EntityNotFoundException e) {
        System.out.println("Cannot find comment to delete.");
        return;
      }
      if (userService.getCurrentUser().getUserId().equals(commentEntity.getProperty("userId")) 
          || userService.isUserAdmin()) {
        datastore.delete(commentEntityKey);
      }
    }
  }
}
