//              Constants              //
/** HTML element ID for comment text area */
const ELEMENT_TEXTAREA_COMMENT = 'textarea-comment';
/** HTML element ID for user text area */
const ELEMENT_TEXTAREA_USER = 'textarea-user';
/** HTML element ID for comment container */
const ELEMENT_COMMENT_CONTAINER = 'comment-container';
const ELEMENT_COMMENTS = 'comments';
const ELEMENT_COMMENT_DIV = 'comment-div';
const ELEMENT_USER_TEXT = 'user-text';
const ELEMENT_TIME_TEXT = 'time-text';
const ELEMENT_COMMENT_TEXT = 'comment-text';
const ELEMENT_DELETE_BUTTON = 'delete-button';
const ELEMENT_INDIV_DELETE = 'indiv-delete-button';
/** Parameter id for user */
const PARAM_USER = 'user';
/** Parameter id for comment */
const PARAM_COMMENT = 'comment';
/** Parameter id for comment id */
const PARAM_ID = 'id';
/** Parameter id for Max Comment */
const PARAM_MAX_COMMENT = 'max-comments';
/** Parameter id for page number */
const PARAM_PAGE = 'page';
/** Fetch new comment servlet */
const FETCH_NEW_COMMENT = '/new-comment';
/** Fetch comment servlet */
const FETCH_COMMENT = '/comment';
/** Fetch delete comments servlet */
const FETCH_DELETE_COMMENTS = '/delete-comments';
/** Fetch delete comment servlet */
const FETCH_DELETE_COMMENT = '/delete-comment';
/** Servlet method type */
const SERVLET_METHOD_POST = 'POST';

/** HTML elements */
const HTML_ELEMENT_LI = 'li';
const HTML_ELEMENT_DIV = 'div';
const HTML_ELEMENT_P = 'p';
const HTML_ELEMENT_BUTTON = 'button';

/** Current Page Number */
let pageNumber = 0;
let maximumPages;

/** submits the user comment to servlet  */
function submitComment() {
  const commentElement = document.getElementById(ELEMENT_TEXTAREA_COMMENT);
  const usernameElement = document.getElementById(ELEMENT_TEXTAREA_USER);
  const languageElement = document.getElementById('language');
  const params = new URLSearchParams();

  var username = usernameElement.innerText;
  if (username === '') {
    username = 'Anonymous';
  }
  params.append(PARAM_USER, username);
  params.append(PARAM_COMMENT, commentElement.innerText);
  params.append('language', languageElement.value);

  commentElement.innerText = '';

  fetch(FETCH_NEW_COMMENT, { method: SERVLET_METHOD_POST, body: params })
    .then(removeCommentsFromDOM)
    .then(loadComments);
}

/** Sends new maximum number of comments to display 
 *  @param {number} maximumNumber the new number of maximum comments on a page
 */
function changeMaxComments(maximumNumber) {
  const params = new URLSearchParams();
  params.append(PARAM_MAX_COMMENT, maximumNumber);

  fetch(FETCH_COMMENT, { method: SERVLET_METHOD_POST, body: params })
    .then(removeCommentsFromDOM)
    .then(loadComments);
}

/** Fetches the comments from the servlet */
function loadComments() {
  fetch(FETCH_COMMENT).then(response => response.clone().json())
    .then(data => {
      addCommentsToDOM(data.comments);
      maximumPages = data.maximumPages;
    });
}

/** Increment the page by 1 to next page or -1 to previous page and 
 *  fetches new comments 
 *  @param {number} increment number to increment the page number by
 */
function incrementPage(increment) {
  pageNumber += increment;
  if (pageNumber > maximumPages) {
    pageNumber = maximumPages;
  } else if (pageNumber < 0) {
    pageNumber = 0;
  } else {
    const params = new URLSearchParams();
    params.append(PARAM_PAGE, pageNumber);
    fetch(FETCH_COMMENT, { method: SERVLET_METHOD_POST, body: params })
      .then(removeCommentsFromDOM)
      .then(loadComments);
  }
}

/** Adds comments with user name to DOM. */
function addCommentsToDOM(comments) {
  const languageId = document.getElementById('language').value;
  const commentListElement = document
    .getElementById(ELEMENT_COMMENT_CONTAINER);

  comments.forEach((comment) => {
    if (comment.languageId != languageId) {
      comment.comment = translateComment(comment.comment, languageId);
    }
    var time = new Date(comment.timestamp);
    commentListElement.appendChild(createCommentElementList(comment));
  });
}

/**
 * Takes the text of a comments and translates it to the langauge selected
 * @param {String} commentText Text to be translated
 * @param {String} languageId Language to translate to
 */
function translateComment(commentText, languageId) {
  const params = new URLSearchParams();
  var translatedText = null;
  params.append('text', commentText);
  params.append('language', languageId);
  fetch('/translate', { method: SERVLET_METHOD_POST, body: params })
    .then(response => response.text()).then(translation =>{
      translatedText = translation;
    });
    return translatedText;
}

/** Reloads the comments when the languages is changed. */
function languageChanged() {
  removeCommentsFromDOM();
  loadComments();
}

/** Sends request to delete comments from database. */
function deleteComments() {
  fetch(FETCH_DELETE_COMMENTS, { method: SERVLET_METHOD_POST })
    .then(removeCommentsFromDOM);
}

/** Deletes the elements from the DOM */
function removeCommentsFromDOM() {
  const commentListElement = document
    .getElementById(ELEMENT_COMMENT_CONTAINER);
  while (commentListElement.lastElementChild) {
    commentListElement.removeChild(commentListElement.lastElementChild);
  }
}

/** Deletes the specified comment */
function deleteComment(comment) {
  const params = new URLSearchParams();
  params.append(PARAM_ID, comment.id);
  fetch(FETCH_DELETE_COMMENT, { method: SERVLET_METHOD_POST, body: params });
}

/**
 * Creates an list element with prespecified text
 * @param {object} comment Comment object holding username, comment text 
 *   and timestamp
 */
function createCommentElementList(comment) {
  const liElement = document.createElement(HTML_ELEMENT_LI);
  const innerDiv = document.createElement(HTML_ELEMENT_DIV);
  const userElement = document.createElement(HTML_ELEMENT_P);
  const timeElement = document.createElement(HTML_ELEMENT_P);
  const commentTextElement = document.createElement(HTML_ELEMENT_P);

  liElement.classList.add(ELEMENT_COMMENTS);
  innerDiv.classList.add(ELEMENT_COMMENT_DIV);
  userElement.classList.add(ELEMENT_USER_TEXT);
  timeElement.classList.add(ELEMENT_TIME_TEXT);
  commentTextElement.classList.add(ELEMENT_COMMENT_TEXT);

  if (comment.userId == this.getUserId() || this.isAdmin()) {
    console.log(this.userId);
    const deleteCommentButton = document.createElement(HTML_ELEMENT_BUTTON);
    deleteCommentButton.classList.add(ELEMENT_DELETE_BUTTON);
    deleteCommentButton.id = ELEMENT_INDIV_DELETE;
    deleteCommentButton.innerText = 'x';
    deleteCommentButton.addEventListener('click', () => {
      deleteComment(comment);

      //Remove the element holding the comment
      removeCommentsFromDOM();
      loadComments();
    })
    innerDiv.appendChild(deleteCommentButton);
  }

  userElement.innerText = comment.user + ':';
  var timezone = new Date(comment.timestamp);
  timeElement.innerText = timezone.toLocaleString();
  commentTextElement.innerText = comment.comment;
  
  innerDiv.appendChild(timeElement);
  innerDiv.appendChild(userElement);
  innerDiv.appendChild(commentTextElement);
  liElement.appendChild(innerDiv);
  return liElement;
}
