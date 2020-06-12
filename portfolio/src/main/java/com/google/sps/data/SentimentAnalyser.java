package com.google.sps.data;

import java.io.IOException;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

/**
 * Class that handles the analysing of the sentiment in the comments inputted
 */
public class SentimentAnalyser {
  
  /**
   * Takes a string input of text and provides a sentiment value ranging form -1 to 1
   * -1 being negative 1 beign positive.
   * @param text The text to be analyzed
   * @return The sentiment score of the text as a float
   */
  public float analyseSentiment(String text) throws IOException {
    Document doc =
        Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();
    return score;
  }
}
