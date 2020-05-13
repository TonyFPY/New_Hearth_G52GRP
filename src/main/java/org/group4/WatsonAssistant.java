package org.group4;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.*;
import javafx.concurrent.Task;
import org.group4.Controller.GameController;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;
import java.util.logging.LogManager;

/**
 * This class contains the main structure and main logic of the whole game. It can create a Watson Assistant object to
 * connect its corresponding API. Also, methods can be called for user interaction, which handles the cases including user
 * audio input, keyboard input, etc.
 *
 * @author Team4
 */
public class WatsonAssistant extends Task<Void> {

	/**
	 *  configures the Speech-to-Text API parameters
	 */
	private String WatsonAssistant_APIKEY = "XWK-4CKHz-hPGD8N9l3L4tQce_G9Ytr9mTjlp4dO6Oow";
	private String WatsonAssistant_serviceURL = "https://gateway-lon.watsonplatform.net/assistant/api";
	private String assistantVersion = "2019-10-17";
	private String assistantID = "e3f86d5f-9d56-43d9-a0f7-160056fbc5ae";
	private String sessionID;
	private boolean flag = true;

	private Assistant assistant;
	private static WatsonAssistant watsonAssistant = null;

	/**
	 * Defines constructor to initialise the Watson assistanct API parameters
	 */
	public WatsonAssistant(){
		// set up Assistant service with IAM authentication

		IamAuthenticator authenticator = new IamAuthenticator(WatsonAssistant_APIKEY);
		assistant = new Assistant(assistantVersion, authenticator);
		assistant.setServiceUrl(WatsonAssistant_serviceURL);

		// create a session
		CreateSessionOptions s_options = new CreateSessionOptions.Builder(assistantID).build();
		SessionResponse s_response = assistant.createSession(s_options).execute().getResult();
		sessionID = s_response.getSessionId(); // get the session ID for message transmission
		//System.out.println(s_response + "\n"); // testing code, which ensures the session is created successfully
	}

	/**
	 * Gets the instance of watson assistant using the singleton design pattern to avoid duplicate
	 * @return the instance of watson assistant
	 */
	public static WatsonAssistant getInstance(){
		if (watsonAssistant == null)
			watsonAssistant = new WatsonAssistant();
		return watsonAssistant;
	}

	/**
	 * Create a new thread to initialize the interaction between the user and the game using watson assistant, text-to-speech, speech-to-text
	 * @return null
	 * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
	 * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
	 * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
	 * @throws IOException if there exist input and output problem, program will throw an exception
	 */
	public Void call() throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {
		// suppress log messages in stdout.
		LogManager.getLogManager().reset();

		// send an empty message to start conversation
		MessageInput userInput = new MessageInput.Builder().messageType("text").text("").build();

		boolean isLeft = false;
		boolean isRight = false;
		do{
			// send an message to Watson Assistant
			MessageOptions m_options = new MessageOptions.Builder(assistantID, sessionID).input(userInput).build();
			MessageResponse msgResponse = assistant.message(m_options).execute().getResult();

			// If an intent was detected, print it to the console.
			List<RuntimeIntent> responseIntents = msgResponse.getOutput().getIntents();
			boolean isError = false;
			if(responseIntents.size() > 0) {
				System.out.println("Detected intent: #" + responseIntents.get(0).intent());
				if(responseIntents.get(0).intent().equals("introRight")) {
					isRight = true;
				}else if(responseIntents.get(0).intent().equals("introLeft")){
					isLeft = true;
				}
				if(responseIntents.get(0).intent().equals("combat")) {
					CombatSystem combat = new CombatSystem();
					String input = combat.combat();
					userInput = new MessageInput.Builder().messageType("text").text(input).build();
					GameController.switchToBackgroundMusic();
					continue;
				}
				//GameMusic.getInstance().playMusic(responseIntents.get(0).intent(), Thread.currentThread());
			}
			// Print the output from dialog, if any. Assumes a single text response.
			//System.out.println(msgResponse);
			List<RuntimeResponseGeneric> responseGeneric = msgResponse.getOutput().getGeneric();
			int num = 0;
			String text = "";
			System.out.println(responseGeneric.size());
			while(num < responseGeneric.size() && responseGeneric.get(num).responseType().equals("text")) {
				String currentResponse = responseGeneric.get(num).text();
				System.out.println(currentResponse);
				if(currentResponse.contains("Sorry")){
					System.out.println("Error message");
					isError = true;
				}
				text += currentResponse;
				//System.out.println(num+": "+currentResponse);
//				if(responseIntents.size() == 0){
//					break;
//				}
				num++;
			}
			//System.out.println(num);
			//System.out.println(responseGeneric.get(num).responseType());

			System.out.println("Starts text to speech");
			Text2Speech.getText(text);
			if(responseIntents.size() > 0 && isError == false){
				Text2Speech.getInstance().startSpeaking(responseIntents.get(0).intent(), Thread.currentThread(), isLeft, isRight);
			}else{
				Text2Speech.getInstance().startSpeaking(" ", Thread.currentThread(), isLeft, isRight);
			}

			System.out.println("Finish text to speech");

			if(responseIntents.size() > 0 && responseIntents.get(0).intent().equals("win")){
				System.out.println("Finish demo");
				System.exit(0);
			}
			// Waits for keyboard events
			while(true){
				String flag = GameController.getFlag();
				Thread.currentThread().sleep(1);
				if(flag.equals("Start")){
					break;
				}
			}

			// speech to text
			Speech2Text.getInstance().startTranslating();
			String userNextInput = Speech2Text.getInstance().translatedResult();
			System.out.println(userNextInput);
			userInput = new MessageInput.Builder().messageType("text").text(userNextInput).build();
			GameController.setFlag("Stop");
		} while (true); // !

	}
}
