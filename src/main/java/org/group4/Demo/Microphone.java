package org.group4.Demo;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

public class Microphone {
    public static void main(String[] args) {
        //Enumerates all available microphones
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : mixerInfos) {
            Mixer m = AudioSystem.getMixer(info);
            Line.Info[] lineInfos = m.getTargetLineInfo();
            if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(TargetDataLine.class)) {//Only prints out info if it is a Microphone
                System.out.println("Line Name: " + info.getName());//The name of the AudioDevice
                System.out.println("Line Description: " + info.getDescription());//The type of audio device
                for (Line.Info lineInfo : lineInfos) {
                    System.out.println("\t" + "---" + lineInfo);
                    Line line;
                    try {
                        line = m.getLine(lineInfo);
                    } catch (LineUnavailableException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return;
                    }
                    System.out.println("\t-----" + line);
                }
            }
        }
    }
}