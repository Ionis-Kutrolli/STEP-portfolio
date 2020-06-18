// package com.google.sps;

// import org.junit.Before;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.junit.runners.JUnit4;
// import org.mockito.Mockito;
// import java.io.IOException;
// import org.junit.Assert;

// import com.google.cloud.language.v1.Document;
// import com.google.cloud.language.v1.LanguageServiceClient;
// import com.google.cloud.language.v1.Sentiment;
// import com.google.sps.data.SentimentAnalyser;

// @RunWith(JUnit4.class)
// public final class SentimentAnalyserTest extends Mockito{

//   private SentimentAnalyser sentimentAnalyser;
//   private LanguageServiceClient languageService;
//   private Sentiment sentiment;

//   @Before
//   public void setUp() {
//     languageService = mock(LanguageServiceClient.class);
//     sentiment = mock(Sentiment.class);
//     sentimentAnalyser = new SentimentAnalyser();
//   }

//   @Test
//   public void happySentimentAnalysisTest() throws IOException {
//     String analysisString = "Happy";
//     when(LanguageServiceClient.create()).thenReturn(languageService);
//     when(languageService.analyzeSentiment(any(Document.class)).getDocumentSentiment()).thenReturn(sentiment);
//     when(sentiment.getScore()).thenReturn(1.0f);
    
//     float actual = sentimentAnalyser.analyseSentiment(analysisString);
//     float expected = 1.0f;

//     Assert.assertEquals(expected, actual, .05);
//   }

// }
