package org.group4;
import java.io.File;
import java.io.FileNotFoundException;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;

/**
 * This class is used when building an object to connect Speech-to-Text API. It receives a audio which is recorded and
 * translates the audio into text for Watson Assistant to recognise.
 *
 * @author Team 4
 */
public class Speech2Text {

    // configures the Speech-to-Text API parameters
    private static String SpeechToText_APIKEY = "OGeLjunDgu-kr80tSfPaK15dG7EjmWI2ND-2vTQ9PKAN";
    private static String SpeechToText_serviceURL = "https://gateway-lon.watsonplatform.net/speech-to-text/api";
    private static  Speech2Text voiceInput;
    private SpeechToText speechToText;
    private String inputStr;

    /**
     * Defines constructor to initialise the Speech-to-Text API parameters
     */
    public Speech2Text(){
        // set up Text-to-Speech service with IAM Authentication
        Authenticator authenticator = new IamAuthenticator(SpeechToText_APIKEY);
        speechToText = new SpeechToText(authenticator);
        speechToText.setServiceUrl(SpeechToText_serviceURL);
    }

    /**
     * Gets the instance of speech-to-text using the singleton design pattern to avoid duplicate
     * @return the instance of Speech-to-text
     */
    public static Speech2Text getInstance(){
        if (voiceInput == null)
            voiceInput = new Speech2Text();
        return voiceInput;
    }

    /**
     * Reads the audio file and translates it into text
     * @throws FileNotFoundException file cannot be found
     */
    public void startTranslating() throws FileNotFoundException {
        System.out.println("start translate");
        File audio = new File("src/main/resources/org/group4/voice/RecordAudio.wav");
        RecognizeOptions options = new RecognizeOptions.Builder().audio(audio).contentType(HttpMediaType.AUDIO_WAV).build();
        SpeechRecognitionResults transcript = speechToText.recognize(options).execute().getResult();
        if(transcript.getResults().size() == 0){
            System.out.println("unrecognized");
            inputStr = "";
            return;
        }
        inputStr = transcript.getResults().get(0).getAlternatives().get(0).getTranscript();
        System.out.println("Finish translate");
    }

    /**
     * Gets the translated result to be used in Watson assistant
     * @return the audio translated result
     */
    public String translatedResult(){
        return inputStr;
    }
}
