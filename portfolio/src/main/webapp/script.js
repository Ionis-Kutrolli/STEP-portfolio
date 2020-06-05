// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Changes the image that is displaying corresponds to the id specified.
 * @param {string} id The id of the image to display.
 */
function displayImage(id) {
  var elements = document.getElementsByClassName("project_imgs");
  for (let element of elements) {
    if (element.id === id){
      element.style.display = "block";
    } else {
      element.style.display = "none";
    }
  }
}

/**
 * Displays the videos that are labled rock_climbing.
 */
function displayRockClimbingVideos() {
  var elements = document.getElementsByClassName("rock_climbing");
  for (let element of elements) {
    element.style.display = "block"
  }
}

/**
 * Initialies a Google Translate element for translating albanian segment
 */
function googleTranslateElementInit() {
  new google.translate.TranslateElement(
    { pageLanguage: 'sq', 
    layout: google.translate.TranslateElement.InlineLayout.SIMPLE }, 
    'google_translate_element');
}

/** submits the user comment to servlet  */
function submitComment() {
  const commentElement = document.getElementById('textarea-comment');
  const usernameElement = document.getElementById('textarea-user');
  const params = new URLSearchParams();

  var username = usernameElement.innerText;
  if (username === ''){
    username = 'Anonymous';
  }
  params.append('user', username);
  params.append('comment', commentElement.innerText);

  fetch('/new-comment', {method: 'POST', body: params})
    .then(removeCommentsFromDOM)
    .then(loadComments);
}

function changeMaxComments(maxNum) {
  const params = new URLSearchParams();
  params.append('max-comments', maxNum);

  fetch('/comment', {method: 'POST', body: params})
    .then(removeCommentsFromDOM)
    .then(loadComments);
}

/** Fetches the comments from the servlet */
function loadComments() {
  fetch('/comment').then(response => response.json()).then(addCommentsToDOM);
}

/** Adds comments with user name to DOM. */
function addCommentsToDOM(comments) {
    const commentListElement = document.getElementById('comment-container');
    comments.forEach((comment) => {
      var time = new Date(comment.timestamp);
      commentListElement.appendChild(createCommentElementList(comment));
    });
}

/** Sends request to delete comments from database */
function deleteComments() {
  fetch('/delete-comments', {method: 'POST'}).then(removeCommentsFromDOM);
}

/** Deletes the elements from the DOM */
function removeCommentsFromDOM(){
  const commentListElement = document.getElementById('comment-container');
  while (commentListElement.lastElementChild) {
    commentListElement.removeChild(commentListElement.lastElementChild);
  }
}

/** Deletes the specified comment */
function deleteComment(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete-comment', {method: 'POST', body: params});
}

/**
 * Creates an list element with prespecified text
 * @param {string} text Text to be put in the list element
 */
function createCommentElementList(comment) {
  const liElement = document.createElement('li');
  const innerDiv = document.createElement('div');
  const userElement = document.createElement('p');
  const timeElement = document.createElement('p');
  const commentTextElement = document.createElement('p');
  const deleteCommentButton = document.createElement('button');

  liElement.classList.add("comments");
  innerDiv.classList.add("comment-div");
  userElement.classList.add('user-text');
  timeElement.classList.add('time-text');
  commentTextElement.classList.add('comment-text')

  userElement.innerText = comment.user + ':';
  var timezone = new Date(comment.timestamp);
  timeElement.innerText = timezone.toLocaleString();
  commentTextElement.innerText = comment.comment;
  deleteCommentButton.innerText = 'Delete';
  deleteCommentButton.addEventListener('click', () => {
    deleteComment(comment);

    //Remove the element holding the comment
    liElement.remove();
  })

  innerDiv.appendChild(deleteCommentButton);
  innerDiv.appendChild(timeElement);
  innerDiv.appendChild(userElement);
  innerDiv.appendChild(commentTextElement);
  liElement.appendChild(innerDiv);
  return liElement;
}
