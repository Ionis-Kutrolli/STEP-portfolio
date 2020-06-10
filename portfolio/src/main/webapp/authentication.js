/** The id of the user if they are logged in */
var userId;
/** If the user is and Admin */
var admin = false;

/** Fetches the link to allow users to login */
function getUserAuthentication() {
  fetch('/auth').then(response => response.json())
    .then(data => {
      enableCommentSubmition(data.loggedIn, data.userEmail, data.isAdmin);
      displayAuthenticationBanner(data);
      userId = data.userId;
      admin = data.isAdmin;
    });
}

/** Enables or disables comment submittion 
 *  based on whether the user is logge in or not 
 *  @param {boolean} loggedIn wether the user is logged in or not
 */
function enableCommentSubmition(loggedIn, userEmail, isAdmin) {
  var commentButton = document.getElementById("comment-submit");
  var textAreaUser = document.getElementById("textarea-user");
  var textAreaComment = document.getElementById("textarea-comment");
  var deleteAll = document.getElementById("delete-all");
  if (loggedIn) {
    commentButton.className = "submit-button-enabled";
    commentButton.addEventListener('click', submitComment);
    textAreaUser.className = "textarea-user";
    textAreaComment.className = "textarea-comment";
    textAreaUser.innerText = userEmail;
    textAreaComment.contentEditable = true;
  } else {
    commentButton.className = "submit-button-disabled";
    commentButton.removeEventListener('click', submitComment);
    textAreaUser.className = "disabled";
    textAreaComment.className = "textarea-disabled";
    textAreaComment.contentEditable = false;
  }
  if (isAdmin) {
    deleteAll.style.display = 'block';
  } else {
    deleteAll.style.display = 'none';
  }
}

/** Displays the user authentication information for logging in or out
 * 
 * @param {object} data the data retrieved from authentication
 */
function displayAuthenticationBanner(data) {
  var authenticationBar = document.getElementById("authentication");
  var authenticationLink = document.getElementById("logging-link");
  if (data.loggedIn) {
    authenticationBar.innerText = "Hello, " + data.userEmail; 
    authenticationLink.innerText = "Logout"
  } else {
    authenticationBar.innerText = "Welcome,";
    authenticationLink.innerText = "Login"
  }
  authenticationLink.href = data.url;
}

/**
 * Return the id of the user if logged in if not logged in null
 */
function getUserId() {
  return userId;
}

/** Returns whether the user is admin or not */
function isAdmin() {
  return admin;
}
