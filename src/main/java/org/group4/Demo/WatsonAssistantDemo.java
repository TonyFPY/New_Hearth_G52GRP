package org.group4.Demo;

import com.ibm.cloud.sdk.core.http.Headers;
import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.cloud.sdk.core.service.exception.RequestTooLargeException;
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.*;

import java.util.List;
import java.util.Scanner;
import java.util.logging.LogManager;

import static java.lang.System.console;

public class WatsonAssistantDemo {

	private static String WatsonAssistant_APIKEY = "XWK-4CKHz-hPGD8N9l3L4tQce_G9Ytr9mTjlp4dO6Oow";
	private static String WatsonAssistant_serviceURL = "https://api.eu-gb.assistant.watson.cloud.ibm.com";
	private static String assistantVersion = "2019-10-17";
	private static String assistantID = "e3f86d5f-9d56-43d9-a0f7-160056fbc5ae";
	private static String sessionID;

	public static void main(String[] args) {
		// suppress log messages in stdout.
		LogManager.getLogManager().reset();

		// set up Assistant service with IAM authentication
		IamAuthenticator authenticator = new IamAuthenticator(WatsonAssistant_APIKEY);
		Assistant assistant = new Assistant(assistantVersion, authenticator);
		assistant.setServiceUrl(WatsonAssistant_serviceURL);

		// create a session
		CreateSessionOptions s_options = new CreateSessionOptions.Builder(assistantID).build();
		SessionResponse s_response = assistant.createSession(s_options).execute().getResult();
		sessionID = s_response.getSessionId(); // get the session ID for message transmission
		System.out.println(s_response + "\n"); // testing code, which ensures the session is created successfully

		// send an empty message to start conversation
		MessageInput userInput = new MessageInput.Builder().messageType("text").text("").build();

		do{
			// send an message to Watson Assistant
			MessageOptions m_options = new MessageOptions.Builder(assistantID, sessionID).input(userInput).build();
			MessageResponse msgResponse = assistant.message(m_options).execute().getResult();

			// If an intent was detected, print it to the console.
			List<RuntimeIntent> responseIntents = msgResponse.getOutput().getIntents();
			if(responseIntents.size() > 0) {
				System.out.println("Detected intent: #" + responseIntents.get(0).intent());
			}

			// Print the output from dialog, if any. Assumes a single text response.
			List<RuntimeResponseGeneric> responseGeneric = msgResponse.getOutput().getGeneric();
			int num = 0;
			while(num < responseGeneric.size() && responseGeneric.get(num).responseType().equals("text")) {
				String currentResponse = responseGeneric.get(num).text();
				System.out.println(currentResponse);
				if(currentResponse.equals("I don't understand, could you repeat please?")){
					break;
				}
				num++;
			}

			// Prompt for next round of input.
			Scanner sc = new Scanner(System.in);
			System.out.print(">> ");
			String userNextInput = sc.nextLine();
			userInput = new MessageInput.Builder().messageType("text").text(userNextInput).build();

		} while (!userInput.text().equals("quit"));
	}
}
