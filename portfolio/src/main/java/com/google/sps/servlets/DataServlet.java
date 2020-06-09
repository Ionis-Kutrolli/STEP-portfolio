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
import com.google.sps.data.CommentRetriever;
import com.google.gson.Gson;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.common.base.*;

/**
 * Servlet that stores comments so they persist and displays retreives them to
 * be displayed
 */
@WebServlet("/comment")
public class DataServlet extends HttpServlet {

  private CommentRetriever commentRetriever;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (commentRetriever == null) {
      commentRetriever = new CommentRetriever(5, 0, 0);
    }

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    List<Comment> comments = commentRetriever.retreiveCurrentPageComments(datastore);

    GetRequestedData data = new GetRequestedData(comments, commentRetriever.getMaximumPageNumber());

    // Json conversion
    Gson gson = new Gson();
    String json = gson.toJson(data);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (request.getParameter("max-comments") != null) {
      commentRetriever.setMaximumCommentsPerPage(getMaxComments(request));
    } else {
      System.out.println(commentRetriever);
      commentRetriever.setPageNumber(getPageParameter(request));
      System.out.println(commentRetriever.getPageNumber());
    }

    response.sendRedirect("/index.html");
  }

  /** Retreives the max comments parameter and converts it to int */
  public int getMaxComments(HttpServletRequest request) {
    String maxCommentsString = request.getParameter("max-comments");

    int maximumComments;
    try {
      maximumComments = Integer.parseInt(maxCommentsString);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException();
    }

    Preconditions.checkArgument(maximumComments == 5 || maximumComments == 10,
        "%s: not a valid value. Value can only be 5 or 10.", maximumComments);

    return maximumComments;
  }

  /** Retreives the Page parameter and converts it to int */
  public int getPageParameter(HttpServletRequest request) {
    String pageString = request.getParameter("page");

    int page;
    try {
      page = Integer.parseInt(pageString);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException();
    }

    Preconditions.checkArgument(page >= 0 || page <= commentRetriever.getMaximumPageNumber(),
        "%d: not a valid value. Page number cannot be less than zero or more than maximum.", page);

    return page;
  }

}

/** Class that stores information requested in get requests */
class GetRequestedData {
  List<Comment> comments;
  int maximumPages;

  public GetRequestedData(List<Comment> comments, int maximumPages) {
    this.comments = comments;
    this.maximumPages = maximumPages;
  }
}
