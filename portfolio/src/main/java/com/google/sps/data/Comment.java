package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

/**
 * Class to package a comment to be sent from the database to the javascript
 */
public class Comment {
  private final String user;
  private final String userId;
  private final long id;
  private final String comment;
  // Timestamp in millis
  private final long timestamp;
  private final String languageId;
  private final float sentiment;

  private Comment(long id, String userId, String user, String comment, long timestamp, 
      String languageId, float sentiment) {
    this.id = id;
    this.userId = userId;
    this.user = user;
    this.comment = comment;
    this.timestamp = timestamp;
    this.languageId = languageId;
    this.sentiment = sentiment;
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
    String userId = (String) entity.getProperty("userId");
    String languageId = (String) entity.getProperty("language");
    float sentiment = (float) entity.getProperty("sentiment");

    return new Comment(id, userId, user, userComment, timestamp, languageId, sentiment);
  }
  
}
