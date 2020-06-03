// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import com.google.sps.data.Comment;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Servlet that stores comments so they persist and displays retreives them to be displayed */
@WebServlet("/comment")
public class DataServlet extends HttpServlet {

  private int numCommentsMax;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Retreive comments stored by the database
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Comment> comments = new ArrayList<>();
    // for(Entity entity : results.asIterable()) {
    //   long id = entity.getKey().getId();
    //   long timestamp = (long) entity.getProperty("timestamp");
    //   String userComment = (String) entity.getProperty("comment");
    //   String user = (String) entity.getProperty("user"); // Maybe null if no user

    //   if (user == null) {
    //     user = "Anonymous";
    //   }

    //   Comment comment = new Comment(id, user, userComment, timestamp);
    //   comments.add(comment);
    // }
    
    Iterator<Entity> resultsIterator = results.asIterator();

    for (int i = 0; i < numCommentsMax && results.asIterator().hasNext(); i++){
      Entity entity = resultsIterator.next();

      long id = entity.getKey().getId();
      long timestamp = (long) entity.getProperty("timestamp");
      String userComment = (String) entity.getProperty("comment");
      String user = (String) entity.getProperty("user"); // Maybe null if no user

      if (user == null) {
        user = "Anonymous";
      }
      
      Comment comment = new Comment(id, user, userComment, timestamp);
      comments.add(comment);
    }

    //Json conversion
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int maxComments = getMaxComments(request);

    if (maxComments == -1) {
      response.setContentType("text/html");
      response.getWriter().println("Please enter integer between 5 and 10.");
      return;
    }

    numCommentsMax = maxComments;

    response.sendRedirect("/index.html");
  }

  /** Retreives the max comments parameter and converts it to int */
  public int getMaxComments(HttpServletRequest request) {
    String maxCommentsString = request.getParameter("max-comments");

    int maxComments;
    try {
      maxComments = Integer.parseInt(maxCommentsString);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + maxCommentsString);
      return -1;
    }

    return maxComments;
  }

}
