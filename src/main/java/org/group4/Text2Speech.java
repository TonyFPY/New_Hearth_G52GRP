package org.group4;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;
import org.group4.Controller.GameController;

import javax.sound.sampled.*;
import java.io.*;
import java.util.logging.LogManager;

/**
 * This class connect the Text-to-Speech API and play the voice that it generates
 * @author Team 4
 */
public class Text2Speech {

    // configures the Text-to-Speech API parameters
    private static String TextToSpeech_APIKEY = "rTEk-qjNh0GQR6J0b5uN3ANo8U5B6Qnbyqfyo1lEdBcc";
    private static String TextToSpeech_serviceURL = "https://api.us-south.text-to-speech.watson.cloud.ibm.com/instances/2410b812-554e-4802-bb20-babd62da6edf";
    private TextToSpeech textToSpeech;
    private static String textOutput;
    private static Text2Speech voiceOutput = null;
    private static long duration;
    private String intent;
    private Thread currentThread;
    private boolean left;
    private boolean right;

    /**
     * Defines constructor to initialise the Text-to-Speech API parameters
     */
    public Text2Speech(){
        // set up Text-to-Speech service with IAM Authentication
        IamAuthenticator authenticator = new IamAuthenticator(TextToSpeech_APIKEY);
        textToSpeech = new TextToSpeech(authenticator);
        textToSpeech.setServiceUrl(TextToSpeech_serviceURL);
    }

    /**
     * Gets the instance of text-to-speech using the singleton design pattern to avoid duplicate
     * @return the instance of text-to-speech
     */
    public static Text2Speech getInstance(){
        if (voiceOutput == null)
            voiceOutput = new Text2Speech();
        return voiceOutput;
    }

    /**
     * Stores the responding text from Watson Assistant into the file
     * @param intent The intend that Watson Assistant detected and used to decide the response text
     * @param currentThread The current thread
     * @param left its value is based on the story line, which is used to decide which music to display
     * @param right its value is based on the story line, which is used to decide which music to display
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception
     */
    public void startSpeaking(String intent, Thread currentThread, boolean left, boolean right) throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {

        this.intent = intent;
        this.currentThread = currentThread;
        this.left = left;
        this.right = right;
        System.out.println("Intent: " + intent);
        // suppress log messages in stdout.
        LogManager.getLogManager().reset();

        try {
            // set up the string to be recognized
            SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder().text(textOutput).accept("audio/wav").voice("en-US_MichaelVoice").build();

            // receive the result
            InputStream inputStream = textToSpeech.synthesize(synthesizeOptions).execute().getResult();
            InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

            // save the voice as voice.wav
            OutputStream out = new FileOutputStream("src/main/resources/org/group4/voice/voice.wav");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            out.close();
            in.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playVoice();

        System.out.println("----------------\nPlayer stops");

    }

    /**
     * Plays the voice from the file
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception
     */
    public void playVoice() throws InterruptedException, IOException, UnsupportedAudioFileException, LineUnavailableException {
        GameController.keyEventStatus = false;
        String voiceFile = "src/main/resources/org/group4/voice/voice.wav";
        File audioFile = new File(voiceFile);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip audioClip = (Clip) AudioSystem.getLine(info);
        audioClip.open(audioStream);
        audioClip.start();
        if(!intent.equals(" ")){
            GameMusic.getInstance().playMusic(intent, currentThread, left, right);
        }

        duration = audioClip.getMicrosecondLength() / 1000;
        Thread.sleep(duration);

        audioClip.close();
        audioStream.close();
        GameController.keyEventStatus = true;
    }


    /**
     *
     * @param text the response text of Watson Assistant
     */
    // get the text from Watson Assistant
    public static void getText(String text){
        textOutput = text;
    }

}
