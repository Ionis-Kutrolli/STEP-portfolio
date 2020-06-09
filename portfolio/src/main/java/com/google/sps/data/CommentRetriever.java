package com.google.sps.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import java.lang.Math;

public class CommentRetriever {

  private int maximumCommentsPerPage;
  private int maximumPageNumber;
  private int pageNumber;

  public CommentRetriever(int maximumCommentsPerPage, int maximumPageNumber, int pageNumber) {
    this.maximumCommentsPerPage = maximumCommentsPerPage;
    this.maximumPageNumber = maximumPageNumber;
    this.pageNumber = pageNumber;
  }

  /**
   * Retrieves the comments stored in the database that are on the specified page
   * @param pageNumber The current page of comments to be retrieved
   * @param maximumCommentsPerPage The maximum number of comments on a page
   * @return a List of comments that are on the current page
   */
  public List<Comment> retreiveCurrentPageComments(DatastoreService datastore) {
    // Retreive comments stored by the database
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    PreparedQuery results = datastore.prepare(query);

    int numberComments = results.countEntities(FetchOptions.Builder.withDefaults());
    maximumPageNumber = (int) Math.ceil((double) numberComments / maximumCommentsPerPage) - 1;

    int commentDisplayOffset = pageNumber * maximumCommentsPerPage;
    int lastCommentDisplayableIndex = maximumCommentsPerPage * (pageNumber + 1);

    // Goes through the comments and adds the ones that should be displayed to
    // comments list
    List<Comment> comments = new ArrayList<>();
    Iterator<Entity> resultsIterator = results.asIterator();
    for (int i = 0; (i < lastCommentDisplayableIndex && resultsIterator.hasNext()); i++) {
      Entity entity = resultsIterator.next();
      if (i >= commentDisplayOffset) {
        Comment comment = Comment.fromEntity(entity);
        comments.add(comment);
      }
    }

    return comments;
  }

  /**
   * Gets the maximum page Number for the comments
   * @return maximum page number
   */
  public int getMaximumPageNumber() {
    return maximumPageNumber;
  }

  /**
   * Set the page number field to the given page number
   * @param pageNumber The page Number to change to the class field to
   */
  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  /**
   * Set the maximum comments per page to the given maximumComments per page
   * @param maximumCommentsPerPage The value to change the class field to
   */
  public void setMaximumCommentsPerPage(int maximumCommentsPerPage) {
    this.maximumCommentsPerPage = maximumCommentsPerPage;
  }

}
