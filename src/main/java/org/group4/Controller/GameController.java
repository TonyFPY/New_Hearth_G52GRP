package org.group4.Controller;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.group4.JavaSoundRecorder;
import org.group4.Main;

import java.io.*;

import javax.sound.sampled.*;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.group4.WatsonAssistant;

/**
 * This class is a controller to handle the user events. Multi-thread programming is applied to integrate watson
 * assistant, text-to-speech, speech-to-text together and make the collaboration effective.
 *
 * @author Team 4
 */
public class GameController {

	Thread t1;
	public static MediaPlayer backgroundPlayer;
	public static MediaPlayer combatPlayer;
	public static boolean keyEventStatus = true;
	public static boolean mediaPlayerStatus = true; // use to indicate which player is currently used
	@FXML
	public void initialize() {
//		String voiceFile = "src/main/resources/org/group4/voice/background.mp3";
//		Media hit = new Media(new File(voiceFile).toURI().toString());
//		MediaPlayer mediaPlayer = new MediaPlayer(hit);
//
//		mediaPlayer.setOnReady(new Runnable() {
//			@Override
//			public void run() {
//				mediaPlayer.play();
//				mediaPlayer.setVolume(0.3);
//				double duration = hit.getDuration().toMillis();
//				long l = Math.round(duration);
//				try {
//					Thread.currentThread().sleep(l);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//
//			}
//		});

		String voiceFile = "src/main/resources/org/group4/voice/background.mp3";
		Media sound = new Media(new File(voiceFile).toURI().toString());
		backgroundPlayer = new MediaPlayer(sound);
		backgroundPlayer.setVolume(0.07);
		backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		backgroundPlayer.play();
		String combatFile = "src/main/resources/org/group4/voice/combat/combatMusic.mp3";
		Media combatSound = new Media(new File(combatFile).toURI().toString());
		combatPlayer = new MediaPlayer(combatSound);
		combatPlayer.setVolume(0.2);
		combatPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		t1 = new Thread(WatsonAssistant.getInstance());
		t1.start();
	}
	
	JavaSoundRecorder recorder;
	Thread thread;
	private static String flag = "stop";
    
    int count = 0;

	/**
	 * Handles the user keyboard events. User can press "F" to start recording and press "J" to finish recording.
	 * @param event keyboard event
	 * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
	 * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
	 * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
	 * @throws IOException if there exist input and output problem, program will throw an exception
	 */
    @FXML
    public void handleOnKeyReleased(KeyEvent event) throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
    	if(keyEventStatus){
			if(event.getCode() == KeyCode.F) {
				count++;
				if(count == 1){
					if(mediaPlayerStatus){
						backgroundPlayer.setVolume(0.05);
					}else{
						combatPlayer.setVolume(0.05);
					}
					System.out.println("hello F");
					playVoice("beep.mp3");
					System.out.println("----------------\nRecording begins");
					this.recorder = new JavaSoundRecorder();
					Thread thread = new Thread(this.recorder);
					thread.start();
				}

			} else if (event.getCode() == KeyCode.J) {
				if (mediaPlayerStatus) {
					backgroundPlayer.setVolume(0.07);
				} else {
					combatPlayer.setVolume(0.2);
				}
				System.out.println("hello J");
				playVoice("beep.mp3");
				count = 0;
				System.out.println("----------------\nRecording stops");
				this.recorder.finish();
				this.recorder.cancel();
				flag = "Start";
			}
			else {
				System.out.println("hello error");
				playVoice("error.mp3");
			}


		}

		if(event.getCode() == KeyCode.ESCAPE){
			Main.getStage().close();
			System.exit(0);
		}
    }
    
 	public static String getFlag(){
    	return flag;
	}

	public static void setFlag(String bool){
    	flag = bool;
	}

	/**
	 * Plays the audio from a specific file location
	 * @param file the file path of the music
	 * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
	 * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
	 * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
	 * @throws IOException if there exist input and output problem, program will throw an exception
	 */
	public void playVoice(String file) throws InterruptedException, IOException, UnsupportedAudioFileException, LineUnavailableException {
		String voiceFile = "src/main/resources/org/group4/voice/"+file;
		Media hit = new Media(new File(voiceFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.play();
	}

	public static void switchToCombatMusic(){
		mediaPlayerStatus = false;
    	backgroundPlayer.stop();
		combatPlayer.play();
	}

	public static void switchToBackgroundMusic(){
		mediaPlayerStatus = true;
    	System.out.println("stop combat music");
    	combatPlayer.stop();
    	backgroundPlayer.play();
	}
}
