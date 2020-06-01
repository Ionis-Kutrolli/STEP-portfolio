package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to package a comment to be sent from the database to the javascript
 */
public class Comment {
  private final String user;
  private final long id;
  private final String comment;
  // Timestamp in millis
  private final long timestamp;

  public Comment(long id, String user, String comment, long timestamp) {
    this.id = id;
    this.user = user;
    this.comment = comment;
    this.timestamp = timestamp;
  }
}
