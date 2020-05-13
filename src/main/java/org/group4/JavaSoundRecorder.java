package org.group4;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import javafx.concurrent.Task;

/**
 * This class is used to records the user voice in a new thread.
 *
 * @author Team 4
 */
public class JavaSoundRecorder extends Task<Void>{

    /**
     * path of the wav file
     */
    private File wavFile = new File("src/main/resources/org/group4/voice/RecordAudio.wav");

    /**
     * format of audio file
     */
    private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    /**
     * the line from which audio data is captured
     */
    private TargetDataLine line;

    /**
     * Creates a new threads specified for audio recording and records the user voice
     * @return null
     * @throws Exception Audio line doesn't support the configuration
     */
    @Override
    protected Void call() throws Exception
    {
        try {
            //creates a data line with an audio format
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   // start capturing
            AudioInputStream ais = new AudioInputStream(line);

            // start recording
            AudioSystem.write(ais, fileType, wavFile);
        }
        catch (LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
    
    /**
     * Defines an audio format
     * @return audio format
     */
    public AudioFormat getAudioFormat()
    {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    /**
     * Closes the target data line to finish capturing and recording
     */
    public void finish()
    {
        line.stop();
        line.close();
        System.out.println("Finished");
    }
}

