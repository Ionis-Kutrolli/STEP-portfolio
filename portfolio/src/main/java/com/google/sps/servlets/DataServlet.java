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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.common.base.*;
import java.lang.Math;

/** Servlet that stores comments so they persist and displays retreives them to be displayed */
@WebServlet("/comment")
public class DataServlet extends HttpServlet {

  private int numCommentsMax = 5;
  private int maxPageNum;
  private int pageNum = 0;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Retreive comments stored by the database
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    int numComments = results.countEntities(FetchOptions.Builder.withDefaults());
    int maxPageNum = (int)Math.ceil((double)numComments/(double)numCommentsMax) - 1;
    int commentDisplayOffset = pageNum * numCommentsMax;

    // Goes through the comments and adds the ones that should be displayed to comments list
    List<Comment> comments = new ArrayList<>();
    Iterator<Entity> resultsIterator = results.asIterator();
    for (int i = 0; (i < numCommentsMax * (pageNum+1) && resultsIterator.hasNext()); i++){
      Entity entity = resultsIterator.next();
      if (i >= commentDisplayOffset) {
        Comment comment = Comment.fromEntity(entity);
        comments.add(comment);
      }
    }

    GetRequestData data = new GetRequestData(comments, maxPageNum);

    //Json conversion
    Gson gson = new Gson();
    String json = gson.toJson(data);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (request.getParameter("max-comments") != null){
      numCommentsMax = getMaxComments(request);
    } else {
      pageNum = getPage(request);
    }

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

    Preconditions.checkArgument(maxComments == 5 || maxComments == 10, "%s: not a valid value.");

    return maxComments;
  }

  /** Retreives the Page parameter and converts it to int */
  public int getPage(HttpServletRequest request) {
    String pageString = request.getParameter("page");

    int page;
    try {
      page = Integer.parseInt(pageString);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + pageString);
      return -1;
    }

    Preconditions.checkArgument(page >=0 || page <= maxPageNum, "%s: not a valid value.");

    return page;
  }

}

/** Class that stores information requested in get requests */
class GetRequestData {
  List<Comment> comments;
  int maxPages;

  public GetRequestData(List<Comment> comments, int maxPages) {
    this.comments = comments;
    this.maxPages = maxPages;
  }
}