package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle comments of the pages 
 */
public class Comments {
  private ArrayList<String> comments;

  /**
   * Initializes the arraylist holding the comments
   */
  public Comments() {
    comments = new ArrayList<>();
  }

  /**
   * Adds a comment to comments arraylist
   */
  public void addComment(String comment) {
    comments.add(comment);
  }

  /**
   * Getter for the comments arraylist
   */
  public ArrayList<String> getComments() {
    return (ArrayList<String>)comments.clone();
  }
}