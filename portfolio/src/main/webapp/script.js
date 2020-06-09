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


//              Constants              //
/** HTML class name for project images */
const CLASS_PROJECT_IMGS = 'project_imgs';
/** HTML class name for rock climning elements */
const CLASS_ROCK_CLIMBING = 'rock_climbing';
/** HTML element ID for google translate */
const ELEMENT_GOOGLE_TRANSLATE = 'google_translate_element';
/** Display style value of block*/
const STYLE_BLOCK = 'block';
/** Display style value of none */
const STYLE_NONE = 'none';
/** Language id for Albanian */
const LANGUAGE_ALBANIAN = 'sq';

/** Fetches the link to allow users to login */
function getUserAuthentication() {
  fetch('/auth').then(response => response.text())
    .then(html => {
      document.body.insertAdjacentHTML('beforebegin', html);
    })
}

/**
 * Changes the image that is displaying corresponds to the id specified.
 * @param {string} id The id of the image to display.
 */
function displayImage(id) {
  var elements = document.getElementsByClassName(CLASS_PROJECT_IMGS);
  for (let element of elements) {
    if (element.id === id){
      element.style.display = STYLE_BLOCK;
    } else {
      element.style.display = STYLE_NONE;
    }
  }
}

/**
 * Displays the videos that are labled rock_climbing.
 */
function displayRockClimbingVideos() {
  var elements = document.getElementsByClassName(CLASS_ROCK_CLIMBING);
  for (let element of elements) {
    element.style.display = STYLE_BLOCK
  }
}

/**
 * Initialies a Google Translate element for translating albanian segment
 */
function googleTranslateElementInit() {
  new google.translate.TranslateElement(
    { pageLanguage: LANGUAGE_ALBANIAN, 
    layout: google.translate.TranslateElement.InlineLayout.SIMPLE }, 
    ELEMENT_GOOGLE_TRANSLATE);
}
