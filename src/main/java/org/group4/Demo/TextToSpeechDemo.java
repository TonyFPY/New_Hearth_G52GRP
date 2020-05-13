package org.group4.Demo;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.GetVoiceOptions;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.model.Voice;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;
import java.io.*;
import javax.sound.sampled.*;


public class TextToSpeechDemo {

    private String TextToSpeech_APIKEY = "6NJRvP-figKfH1WdWXY4xBCjBFcwVa-v-YvQlqhgRmdW";
    private String TextToSpeech_serviceURL = "https://api.eu-gb.text-to-speech.watson.cloud.ibm.com";
    public TextToSpeech textToSpeech;

    TextToSpeechDemo(){
        IamAuthenticator authenticator = new IamAuthenticator(TextToSpeech_APIKEY);
        textToSpeech = new TextToSpeech(authenticator);
        textToSpeech.setServiceUrl(TextToSpeech_serviceURL);

        GetVoiceOptions getVoiceOptions = new GetVoiceOptions.Builder().voice("en-US_AllisonVoice").build();

        Voice voice = textToSpeech.getVoice(getVoiceOptions).execute().getResult();
        System.out.println(voice);
    }

    public void speaker(){
        try {
            String textOutput = "Hello World, this is interactive audio adventure!";
            SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder().text(textOutput).accept("audio/wav").voice("en-US_AllisonVoice").build();

            InputStream inputStream = textToSpeech.synthesize(synthesizeOptions).execute().getResult();
            InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

            OutputStream out = new FileOutputStream("src/main/resources/org/group4/voice/voice.wav");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            out.close();
            in.close();
            inputStream.close();

            playVoice();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }


    }

    public void playVoice() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        String voiceFile = "src/main/resources/org/group4/voice/voice.wav";
        File audioFile = new File(voiceFile);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip audioClip = (Clip) AudioSystem.getLine(info);
        audioClip.open(audioStream);
        audioClip.start();
        long duration = audioClip.getMicrosecondLength()/1000;
        Thread.sleep(duration);
        audioClip.close();
        audioStream.close();
        System.exit(0);
    }

    public static void main(String[] args){
        TextToSpeechDemo test = new TextToSpeechDemo();
        test.speaker();
    }
}
