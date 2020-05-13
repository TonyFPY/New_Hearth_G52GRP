package org.group4;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.group4.Controller.GameController;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

/**
 * This class aims to create a combat system. It simulates a combat situation, where user can command his/her spaceship
 * to fight against the enemy.
 *
 * @author Team 4
 */
public class CombatSystem {

    private Player mainChar;
    private Player enemyPlayer;

    private Random EnemyRandom = new Random(System.currentTimeMillis());
    private Random PlayerRandom = new Random(System.currentTimeMillis()*20);
    private Random randomVoice = new Random(System.currentTimeMillis());

    private static int begin = 1;

    private static long duration;
    public CombatSystem(){
        mainChar = new Player(20);
        enemyPlayer = new Player(10);
    }

    /**
     * Start the combat
     * @return the result of the combat win or lose
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception
     */
    public String combat() throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {

        GameController.switchToCombatMusic();
        if (begin == 1){
            mediaPlay("combatBegin1.wav");
            begin ++;
        } else if (begin == 2){
            mediaPlay("combatBegin2.wav");
            begin ++;
        } else if (begin == 3){
            mediaPlay("combatBegin3.wav");
            begin ++;
        } else {
            mediaPlay("combatBegin4.wav");
        }

        System.out.println("Enemy life: "+enemyPlayer.getHealth());
        System.out.println("Main char life: " + mainChar.getHealth());

        String s = null;
        int eStatus1 = 1, eStatus2 = 1, eStatus3 = 1, cStatus = 1;

        while(true){

            // Initialisation
            int playerNum = PlayerRandom.nextInt(6);
            int enemyNum = EnemyRandom.nextInt(4);
            double enemyStatus = EnemyRandom.nextDouble();
            System.out.println("enemy status:" + enemyStatus);
            if(enemyNum > 6 && enemyStatus < 0.5){
                mediaPlay("enemyPower.wav");
            }
            System.out.print("your action:");
            s = record();
            GameController.setFlag("Stop");
            System.out.println("s: " + s);

            // Initialise the play and enemy attack and defend status
            if(s.contains("attack")){
                System.out.println("attack");
                attackEnemy(playerNum, enemyStatus, enemyNum, randomVoice.nextDouble());
            }else if(s.contains("defend")){
                System.out.println("defend");
                defendEnemy(playerNum, enemyStatus, enemyNum);
            }else if(s.contains("ene")){
                System.out.println("enemy");
                checkEnemyStatus();
                mediaPlay("nextRound.wav");
                continue;
            }else if(s.contains("my")){
                System.out.println("my");
                checkMyStatus();
                mediaPlay("nextRound.wav");
                continue;
            }
            else{
                mediaPlay("error.wav");
                continue;
            }

            // check enemy's status
            if(enemyPlayer.getHealth() <= 3 && eStatus1 == 1){
                mediaPlay("30%.wav");
                //playVoice("30%.mp3");
                eStatus1 = 0;
            }else if(enemyPlayer.getHealth() <= 5 && eStatus2 == 1) {
                mediaPlay("50%.wav");
                //playVoice("50%.mp3");
                eStatus2 = 0;
            }else if(enemyPlayer.getHealth() <= 8 && eStatus3 == 1){
                mediaPlay("80%.wav");
               // playVoice("80%.mp3");
                eStatus3 = 0;
            }


            // check character's status
            if(mainChar.getHealth() <= 10 && cStatus == 1){
                mediaPlay("healthWarning.wav");
                //playVoice("healthWarning.mp3");
                cStatus = 0;
            }

            // Check whether the enemy or the player is dead or not
            if(enemyPlayer.getHealth() <= 0){
                return "win";
            }
            if(mainChar.getHealth() <= 0){
                return "lose";
            }

            mainChar.setCritical(false);
            enemyPlayer.setCritical(false);
            mediaPlay(("nextRound.wav"));
            //playVoice("nextRound.mp3");

        }
    }

    /**
     * Main character attack
     * @param attackNum the attack value
     * @param enemyStatus enemy status defend or status
     * @param enemyNum enemy attack or defend value
     * @param rate probability
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception
     */
    public void attackEnemy(int attackNum, double enemyStatus, int enemyNum, double rate)
            throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        setMainCharAttack(attackNum, rate);
        setEnemyValue(enemyStatus, enemyNum, rate);
        if(enemyStatus < 0.5){
            bothAttack(mainChar.getAttack(), enemyPlayer.getAttack());
        }else{
            enemyDefend(mainChar.getAttack(), enemyPlayer.getDefend());
        }
    }

    /**
     * Set main character attack number and check whether critical attack happens
     * @param attackNum the attack value
     * @param rate probability of critical attack
     */
    public void setMainCharAttack(int attackNum, double rate){
        if(rate < 0.5){
            System.out.println("critical hit");
            mainChar.setAttack(attackNum * 2);
            mainChar.setCritical(true);
        }else{
            mainChar.setAttack(attackNum);
        }
    }

    /**
     * Main character defend
     * @param defendNum defend number
     * @param enemyStatus enemy status
     * @param enemyNum enemy defend or attack value
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception
     */
    public void defendEnemy(int defendNum, double enemyStatus, int enemyNum) throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        setMainCharDefend(defendNum);
        setEnemyValue(enemyStatus, enemyNum, randomVoice.nextDouble());
        if(enemyStatus < 0.5){
            charDefend(defendNum, enemyNum);
        }else{
            nothingHappens();
        }
    }

    /**
     * Set the main character defend number
     * @param defendNum main character defend number
     */
    public void setMainCharDefend(int defendNum){
        mainChar.setDefend(defendNum);
    }

    /**
     * Set the enemy attack or defend value and check whether critical attack happens
     * @param status the status of enemy (attack or defend)
     * @param enemyNum the enemy attack or defend number
     *  @param rate probability of critical attack
     */
    public void setEnemyValue(double status, int enemyNum, double rate){
        if(status < 0.5){
            if(rate < 0.2){
                enemyPlayer.setAttack(enemyNum * 2);
                enemyPlayer.setCritical(true);
            }else{
                enemyPlayer.setAttack(enemyNum);
            }
        }else{
            enemyPlayer.setDefend(enemyNum);
        }
    }

    /**
     * Both player and enemy attack
     * @param playerNum player attack number
     * @param enemyNum enemy attack number
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception
     */
    public void bothAttack(int playerNum, int enemyNum) throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        if(mainChar.getAttack() == 0) {
            mediaPlay("miss.wav");
            return;
        }else{
            GameController.combatPlayer.setVolume(0.05);
            if(randomVoice.nextDouble() < 0.33){
                System.out.println("attack1");
                playVoice("attack1.mp3");
            }else if(randomVoice.nextDouble() < 0.66){
                System.out.println("attack2");
                playVoice("attack2.mp3");
            }else {
                System.out.println("attack3");
                playVoice("attack3.mp3");
            }


            if(mainChar.getCritical()){
                System.out.println("good job");
                mediaPlay("critical.wav");
                //playVoice("critical.mp3");
            }else{
                mediaPlay("success.wav");
            }

        }

        //enemy miss the target, critical
        if(enemyPlayer.getAttack() == 0){
            mediaPlay("enemyMissTarget.wav");
        }else if(enemyPlayer.getCritical()) {
            mediaPlay("criticalAttackByEnemy.wav");
        }else{
            mediaPlay("enemyNormalAttack.wav");
        }
        System.out.println(playerNum);
        System.out.println(mainChar.getAttack());

        mainChar.setHealth(mainChar.getHealth()-enemyNum);
        enemyPlayer.setHealth(enemyPlayer.getHealth()-playerNum);
        System.out.println("Enemy attack:" + enemyNum + " Player attack: "+playerNum);
        System.out.println("Enemy life: "+enemyPlayer.getHealth());
        System.out.println("Main char life: " + mainChar.getHealth());
        System.out.println("----------------------------------");
    }

    /**
     * Enemy defend, player attack
     * @param playerNum player attack value
     * @param enemyNum enemy defend value
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception
     */
    public void enemyDefend(int playerNum, int enemyNum) throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        if(mainChar.getAttack() == 0){
            mediaPlay("miss.wav");
            return;
        }else if(enemyNum < playerNum) {
            GameController.combatPlayer.setVolume(0.05);
            if(randomVoice.nextDouble() < 0.33){
                System.out.println("attack1");
                playVoice("attack1.mp3");
            }else if(randomVoice.nextDouble() < 0.66){
                System.out.println("attack2");
                playVoice("attack2.mp3");
            }else {
                System.out.println("attack3");
                playVoice("attack3.mp3");
            }
            enemyPlayer.setHealth(enemyPlayer.getHealth() - playerNum + enemyNum);
            if(mainChar.getCritical()){
                System.out.println("good job");
                mediaPlay("critical.wav");
                //playVoice("critical.mp3");
            }else if(randomVoice.nextDouble() < 0.33){
                mediaPlay("success.wav");
            }else if(randomVoice.nextDouble() < 0.66){
                mediaPlay("success2.wav");
            }else {
                mediaPlay("success3.wav");
            }
            GameController.combatPlayer.setVolume(0.2);

        } else {
            if(randomVoice.nextDouble() < 0.5){
                mediaPlay("defend.wav");
            }else{
                mediaPlay("defend2.wav");
            }

            System.out.println("Enemy defend successfully");

        }
        System.out.println("Enemy defend:" + enemyNum + " Player attack: "+playerNum);
        System.out.println("Enemy life: "+enemyPlayer.getHealth());
        System.out.println("Main char life: " + mainChar.getHealth());
        System.out.println("----------------------------------");

    }

    /**
     * Enemy attack, player defend
     * @param playerNum main character defend value
     * @param enemyNum enemy attack value
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception

     */
    public void charDefend(int playerNum, int enemyNum) throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        if(enemyNum <= playerNum){
            mediaPlay("charDefend.wav");
            System.out.println("Main character defend successfully");

        }else{
            mainChar.setHealth(mainChar.getHealth()+ playerNum - enemyNum);
        }
        System.out.println("Enemy attack:" + enemyNum + " Player defend: "+playerNum);
        System.out.println("Enemy life: "+enemyPlayer.getHealth());
        System.out.println("Main char life: " + mainChar.getHealth());
        System.out.println("----------------------------------");
    }

    /**
     * Both player and enemy defends, nothing happens
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception

     */
    public void nothingHappens() throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        System.out.println("hello defend");
        mediaPlay("NothingHappens.wav");
        //playVoice("NothingHappens.mp3");
        System.out.println("nothing happens");
    }

    /**
     * Check the enemy status
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception
     */
    public void checkEnemyStatus() throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        if(enemyPlayer.getHealth() < 5){
            if(enemyPlayer.getHealth() < 2.5){
                mediaPlay("dying.wav");
                System.out.println("Your enemy is almost dying, keep fighting");
            }else {
                mediaPlay("lessHealth.wav");
                System.out.println("Your enemy is less than half health");
            }
        }else{
            mediaPlay("goodHealth.wav");
            System.out.println("Your enemy is still in good health");
        }
    }

    /**
     * Check player status
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception
     */
    public void checkMyStatus() throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        if(mainChar.getHealth() < 10){
            if(mainChar.getHealth() < 5){
                mediaPlay("severeDamage.wav");
                System.out.println("Your spaceship is in severe damage, it's losing power.");
            }else {
                mediaPlay("meHalfHealth.wav");
                System.out.println("You still got half health, try to protect yourself.");
            }
        }else{
            mediaPlay("meGoodHealth.wav");
            System.out.println("You are still in good health, keep fighting.");
        }
    }

    /**
     * Record the user voice and transfer it to the text
     * @return the text that the user speak
     * @throws FileNotFoundException if file cannot be found, program will throw an exception
     * @throws InterruptedException  if current thread is block or interrupt, program will throw an exception
     */
    public String record() throws FileNotFoundException, InterruptedException {
        while(true){
            String flag = GameController.getFlag();
            Thread.currentThread().sleep(1);
            if(flag.equals("Start")){
                break;
            }
        }
        Speech2Text.getInstance().startTranslating();
        String userNextInput = Speech2Text.getInstance().translatedResult();
        System.out.println(userNextInput);
        return userNextInput;
    }

    /**
     * Pay the music of format wav
     * @param filename the file path of the music
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception
     */
    public void mediaPlay(String filename) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        GameController.keyEventStatus = false;
        String voiceFile = "src/main/resources/org/group4/voice/combat/" + filename;
        File audioFile = new File(voiceFile);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip audioClip = (Clip) AudioSystem.getLine(info);
        audioClip.open(audioStream);
        audioClip.start();
        duration = audioClip.getMicrosecondLength() / 1000;
        Thread.sleep(duration);

        audioClip.close();
        audioStream.close();
        GameController.keyEventStatus = true;
    }

    /**
     * Play the music of format mp3
     * @param fileName the file path of the music
     * @throws InterruptedException if current thread is block or interrupt, program will throw an exception
     * @throws UnsupportedAudioFileException if the voice can not play, program will throw an exception
     * @throws LineUnavailableException if the current line is unavailable, it will throw an exception
     * @throws IOException if there exist input and output problem, program will throw an exception
     */
    public void playVoice(String fileName) throws InterruptedException, IOException, UnsupportedAudioFileException, LineUnavailableException {
        GameController.keyEventStatus = false;
        String voiceFile = "src/main/resources/org/group4/voice/combat/" + fileName;
        Media hit = new Media(new File(voiceFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
        mediaPlayer.setVolume(0.4);
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                double duration = hit.getDuration().toMillis();
                System.out.println(duration);
                //System.out.println(currentThread.getName());

                long l = Math.round(duration);
                try{
                    Thread.currentThread().sleep(l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        GameController.keyEventStatus = true;
    }

    /**
     * Get an enemy object
     * @return the enemy player object
     */
    public Player getEnemyPlayer(){
        return enemyPlayer;
    }

    /**
     * Get an main character object
     * @return the main character object
     */
    public Player getMainChar(){
        return mainChar;
    }
}
