package com.google.sps.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.sps.configuration.Flags;
import com.google.sps.data.Comment;
import com.google.sps.data.CommentData;

/**
 * Data servlet for relaying hard coded information for testing front end
 */
@WebServlet(Flags.isReal ? "/disabled-comment" : "/comment")
public class FakeDataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<Comment> comments = new ArrayList<>();

    for(int i = 0; i < 3; i++) {
      Entity commentEntity = new Entity("Comment");
      commentEntity.setProperty("comment", "This is comment " + i);
      commentEntity.setProperty("timestamp", 0L);
      commentEntity.setProperty("user", "Anonymous");
      commentEntity.setProperty("userId", "0");
      commentEntity.setProperty("language", "en");
      commentEntity.setProperty("sentiment", 0.0);

      comments.add(Comment.fromEntity(commentEntity));
    }
    CommentData data = new CommentData(comments, 0);

     // Json conversion
     Gson gson = new Gson();
     String json = gson.toJson(data);
     response.setContentType("application/json;");
     response.getWriter().println(json);
  }

  // @Override
  // public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
  
  // }

}
