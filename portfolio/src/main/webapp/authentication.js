/** Fetches the link to allow users to login */
function getUserAuthentication() {
  fetch('/auth').then(response => response.json())
    .then(data => {
      if (data.loggedIn) {
        commentButton = document.getElementById("comment-submit");
      }
    })
}
