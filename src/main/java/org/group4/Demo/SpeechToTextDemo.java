package org.group4.Demo;

import java.io.File;
import java.io.FileNotFoundException;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import org.group4.Text2Speech;

/**
 * Recognize a sample wav file and print the transcript into the console output. Make sure you are using UTF-8 to print
 * messages; otherwise, you will see question marks.
 */
public class SpeechToTextDemo {

    public static void main(String[] args) throws FileNotFoundException {
        Authenticator authenticator = new IamAuthenticator("OGeLjunDgu-kr80tSfPaK15dG7EjmWI2ND-2vTQ9PKAN");
        SpeechToText service = new SpeechToText(authenticator);
        service.setServiceUrl("https://gateway-lon.watsonplatform.net/speech-to-text/api");
        File audio = new File("src/main/resources/org/group4/voice/voice.wav");
        RecognizeOptions options = new RecognizeOptions.Builder().audio(audio).contentType(HttpMediaType.AUDIO_WAV).build();
        SpeechRecognitionResults transcript = service.recognize(options).execute().getResult();
        String inputStr = transcript.getResults().get(0).getAlternatives().get(0).getTranscript();
        System.out.println(inputStr);
    }
}
