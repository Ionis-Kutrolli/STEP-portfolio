package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.Entity;

/**
 * Class to package a comment to be sent from the database to the javascript
 */
public class Comment {
  private final String user;
  private final long id;
  private final String comment;
  // Timestamp in millis
  private final long timestamp;

  private Comment(long id, String user, String comment, long timestamp) {
    this.id = id;
    this.user = user;
    this.comment = comment;
    this.timestamp = timestamp;
  }

  /**
   * Static factory for converting entity to Comment
   * 
   * @param entity the entity to be converted into comment type
   * @return the converted entity as a Comment
   */
  public static Comment fromEntity(Entity entity) {
    long id = entity.getKey().getId();
    long timestamp = (long) entity.getProperty("timestamp");
    String userComment = (String) entity.getProperty("comment");
    String user = (String) entity.getProperty("user"); // Maybe null if no user

    return new Comment(id, user, userComment, timestamp);
  }
  
}
