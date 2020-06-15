package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.SentimentAnalyser;

/**
 * Servlet used to handle the submittion of a new comment to the database.
 */
@WebServlet("/new-comment")
public class NewCommentServlet extends HttpServlet {

  SentimentAnalyser sentimentAnalyser = new SentimentAnalyser();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      // Get input from form
      String comment = request.getParameter("comment");
      String user = request.getParameter("user");
      String languageId = request.getParameter("language");
      String userId = userService.getCurrentUser().getUserId();
      long timestamp = System.currentTimeMillis();
      float sentiment;
      // Try statement when sentiment is not enabled. 0 is neutral sentiment.
      try {
        sentiment = sentimentAnalyser.analyseSentiment(comment); 
      } catch (IOException e) {
        sentiment = 0.0f;
      }

      Entity commentEntity = new Entity("Comment");
      commentEntity.setProperty("comment", comment);
      commentEntity.setProperty("timestamp", timestamp);
      commentEntity.setProperty("user", user);
      commentEntity.setProperty("userId", userId);
      commentEntity.setProperty("language", languageId);
      commentEntity.setProperty("sentiment", sentiment);

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(commentEntity);
    }

    response.sendRedirect(request.getHeader("referer"));
  }
}
