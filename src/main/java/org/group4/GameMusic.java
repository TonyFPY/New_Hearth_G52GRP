package org.group4;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * This class is used to deal with all the background music in the game
 *
 * @author Team 4
 */

public class GameMusic{
    private static GameMusic GM = null;

    public static GameMusic getInstance(){
        if (GM == null) {
            GM = new GameMusic();
        }
        return GM;
    }

    /**
     * Plays the sound effect in the game
     * @param intent the intent detected by Watson Assistant and used to decide which music to display
     * @param currentThread the currentThread
     * @param left its value is based on the story line, which is used to decide which music to display
     * @param right its value is based on the story line, which is used to decide which music to display
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception
     */
    public void playMusic (String intent, Thread currentThread, boolean left, boolean right) throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        switch (intent){
            case "getup":
                playVoice("getUp.mp3", currentThread);
                break;
            case "yesGun":
                playVoice("yesGun.mp3", currentThread);
                break;
            case "yesKnife":
                playVoice("yesKnife.mp3", currentThread);
                break;
            case "supplies":
                playVoice("echo_walk.mp3", currentThread);
                break;
            case "oxygen":
                playVoice("mask.mp3", currentThread);
                break;
            case "launch":
                playVoice("engine.mp3", currentThread);
                break;
            case "getout":
                System.out.println("getout");
                playVoice("getout.mp3", currentThread);
                break;
            case "start_story":
                System.out.println("inside context");
                playVoice("context.mp3", currentThread);
                break;
            case"introLeft":
                System.out.println("Intro left");
                playVoice("introLeft.mp3", currentThread);
                break;
            case"introRight":
                System.out.println("Intro Right");
                playVoice("introRight.mp3", currentThread);
                break;
            case "turnback":
                if(left == true){
                    System.out.println("turnbackleft");
                    playVoice("turnbackLeft.mp3", currentThread);
                    break;
                }else if(right == true){
                    System.out.println("turnbackright");
                    playVoice("turnbackRight.mp3", currentThread);
                    break;
                }
            case "investigate":
                if(left == true){
                    System.out.println("invertigateleft");
                    playVoice("investigateLeft.mp3", currentThread);
                    break;
                }else if(right == true){
                    System.out.println("investigateright");
                    playVoice("investigateRight.mp3", currentThread);
                    break;
                }
            case "hide":
                if(left == true){
                    System.out.println("hideleft");
                    playVoice("hideLeft.mp3", currentThread);
                    break;
                }else if(right == true){
                    System.out.println("hideRight");
                    playVoice("hideRight.mp3", currentThread);
                    break;
                }
            case "win":
                System.out.println("Win");
                playVoice("win.mp3", currentThread);
                break;
            case "lose":
                System.out.println("Lose");
                playVoice("lose.mp3", currentThread);
                break;
            case"booth":
                System.out.println("booth");
                playVoice("booth.mp3", currentThread);
                break;
            case "help":
                System.out.println("help");
                playVoice("help.mp3", currentThread);
                break;
            case "run":
                if(left == true){
                    System.out.println("runLeft");
                    playVoice("runLeft.mp3", currentThread);
                    break;
                }else if(right == true){
                    System.out.println("runRight");
                    playVoice("runRight.mp3", currentThread);
                    break;
                }
            default:
               break;
        }
    }

    /**
     * Plays the music from a specific file location
     * @param fileName the file path of music that need to be displayed
     * @param currentThread the currentThread
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception
     */
    public void playVoice(String fileName, Thread currentThread) throws InterruptedException, IOException, UnsupportedAudioFileException, LineUnavailableException {
        String voiceFile = "src/main/resources/org/group4/voice/" + fileName;
        Media hit = new Media(new File(voiceFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                double duration = hit.getDuration().toMillis();
                System.out.println(duration);
                System.out.println(currentThread.getName());

                long l = Math.round(duration);
                try {
                    currentThread.sleep(l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
