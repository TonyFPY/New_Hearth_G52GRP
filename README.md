# <img src="/src/main/resources/org/group4/readme/robot.jpg" alt="logo" width="40"/> Interactive Audio Adventure for those with Physical Challenges
> This is a Software Engineering Group Project (G52GRP/COMP2002) starting from September 2019 to May 2020. The *[project]()* is about developing a story-based interactive audio game for people with vision loss. Our supervisor is *[Steve Benford](https://www.nottingham.ac.uk/computerscience/people/steve.benford)* and industry sponsor is *[IBM](https://www.ibm.com/uk-en)*.

### Catalogue
- [Development Environment & Running Requirement](#Development-Environment-&-Running-Requirement)
- [Screenshots](#screenshots)
- [Source File Structure](#source-file-structure)
- [High Level Program Structure](#high-level-program-structure)
- [Highlight](#highlight)
- [Manuals](#manuals)
- [Video](#video)
- [Credits](#credits)

### Development Environment & Running Requirement
- Development Environment
    - IDE: IntelliJ
    - Build Script: Maven Framework
    - Programming Language: Java with SDK 10
    - GUI: JavaFX 11
    - Windows 10 OS

- Running Requirement:
    - Running environment: Windows OS with Java 10
    - Hardware requirement: Accessible built-in microphone
    
    > Note: 
    You don't have to install the software since we have already packed the game into excutable files. To launch the game, all you need is just clicking the .exe file with game icon.

### Screenshots
<div align=center>
     <img src="/src/main/resources/org/group4/readme/robot.jpg" alt="game interface" width=60% />
</div>  

### Source File Structure

```
src
└── main
    ├── java
    |   └── org
    |       └── group4
    |           ├── Main.java
    |           ├── Controller
    |           |   ├── GameController.java
    |           |   └── WindowController.java
    |           └── Model
    |               ├── CombatSystem.java
    |               ├── WatsonAssistant.java
    |               ├── Text2Speech.java
    |               ├── Speech2Text.java
    |               ├── GameMuisic.java
    |               └── JavaSoundRecorder.java
    └── resources
        └── org
            └── group4
                ├── image
                ├── music
                ├── FXML
                └── HighScoreList
```

### High Level Program Structure
![](/src/main/resources/org/group4/readme/class_diagram.png)

### Highlight

- **Collaboration between IBM Technologies**
    - In the project, we use IBM Watson Assistant (WA), Text-to-Speech (T2S) and Speech-To-Text (S2T) to build a chatbot-like framework.
    - The collaboration bwtween those tools are achieved by multi-threaded programming.
    <br/>

- **User Interaction Experience**
    - The game is mostly voice-controlled, which provides blind people with unique experience.
    - No other hardware requirements. All the user interaction events can be done by an accessible build-in microphone and keyboards.
    <br/>

- **Combat System**
    - Inside the game, we build a combat system to simulate 'war' in the story.
    - In this virtual environment, the player can use his/her voice to manipulate the spaceship.
    - The status of NPCs (enemies) is created randomly with different health value, attack value and defence value so that player can experience different combat situations.
    <br/>

- **SSML**
    - We add SSML to fine-tune the pitch, pronunciation, speaking rate, volume, etc. to make it more like a human voice.
    <br/>

- **Sound Effect**
    - We add various sound effects matching different game situations
    <br/>

- **MVC pattern**
    - The development is based on MVC design pattern to make the project maintainable and extendable.
        - The controller accepts the inputs (button, keyboards, mouse) from the players and decides what to do with it.
        - The view displays GUI on the screen with the help of `.fxml` files.
        - Model deals with the data.  

        |     Model      |        View      |            Controller     |
        | :------------: | :--------------: | :------------------------:|
        |  WA / T2S / S2T|  FXML files      |   GameController          |
        |  CombatSystem  |                  |   WindowController        |
        |  GameMusic     |                  |                           |

<br>

### Manuals
- **[Software Manual](/src/main/resources/org/group4/readme/Software_Manual.pdf)**
    <br>
- **[User Manual](/src/main/resources/org/group4/readme/User_Manual.pdf)**
    <br>

### Video
**[Please Click Here](https://www.youtube.com/watch?v=6Guy5WK35xY)**
<br>

### Credits
All media assets (images & music) are derived from the internet, and are ensured to be free to use for non-commercial use.

This project adopts MIT license.
