package com.google.sps.data;

import java.util.List;
import com.google.sps.data.Comment;

/**
 * Class that stores information requested in get requests for /comment
 */
public class GetCommentData {
  List<Comment> comments;
  int maximumPages;

  public GetCommentData(List<Comment> comments, int maximumPages) {
    this.comments = comments;
    this.maximumPages = maximumPages;
  }
}
