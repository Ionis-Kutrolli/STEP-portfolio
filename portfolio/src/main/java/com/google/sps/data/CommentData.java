package com.google.sps.data;

import java.util.List;

/**
 * Class that stores information requested in get requests for /comment
 */
public class CommentData {
  List<Comment> comments;
  int maximumPages;

  public CommentData(List<Comment> comments, int maximumPages) {
    this.comments = comments;
    this.maximumPages = maximumPages;
  }
}
