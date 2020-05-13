package org.group4.Demo;

import javax.sound.sampled.*;
import java.io.*;

/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 */
public class AudioRecorder {
    // record duration, in milliseconds
    static final long RECORD_TIME = 3000;  // 1 minute

    // path of the wav file
    File wavFile = new File("src/main/resources/org/group4/voice/RecordAudio.wav");

    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    // the line from which audio data is captured
    TargetDataLine line;

    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 8000f;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    /**
     * Captures the sound and record into a WAV file
     */
    void start() {
        try {
            Mixer.Info[] mixerInfo;
            mixerInfo = AudioSystem.getMixerInfo();
            Line.Info targetDLInfo = new Line.Info(TargetDataLine.class);
            Mixer currentMixer=AudioSystem.getMixer(mixerInfo[0]);

            for(int cnt = 0; cnt < mixerInfo.length; cnt++) {
                currentMixer = AudioSystem.getMixer(mixerInfo[cnt]);

                if( currentMixer.isLineSupported(targetDLInfo) && mixerInfo[cnt].getName().equals("External Microphone")) {
                    System.out.println(mixerInfo[cnt].getName());
                    break;
                }
            }

            //(AudioFormat.Encoding.PCM_SIGNED,48000,8,1,1,48000,false);//
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // checks if system supports the data line
//            if (!AudioSystem.isLineSupported(info)) {
//                System.out.println("Line not supported");
//                System.exit(0);
//            }
            line = (TargetDataLine) currentMixer.getLine(info);
            line.open(format);
            line.start();   // start capturing

            System.out.println("Start capturing...");

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("Start recording...");

            // start recording
            AudioSystem.write(ais, fileType, wavFile);

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Closes the target data line to finish capturing and recording
     */
    void finish() {
        line.stop();
        line.close();
        System.out.println("Finished");
    }

    /**
     * Entry to run the program
     */
    public static void main(String[] args) {
        final AudioRecorder recorder = new AudioRecorder();

        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.finish();
            }
        });

        stopper.start();

        // start recording
        recorder.start();
    }
}
