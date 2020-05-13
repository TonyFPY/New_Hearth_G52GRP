package org.group4;

/**
 * This class can be used to create a character object in the story, which is mainly used in the combat system.
 *
 * @author Team 4
 */

public class Player {
    private int health;
    private boolean critical = false;
    private int attack = 0;
    private int defend = 0;

    public Player(int health){
        this.health = health;
    }

    public int getDefend() {
        return defend;
    }

    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setDefend(int defend) {
        this.defend = defend;
    }

    public void setCritical(boolean isCritical){this.critical = isCritical; }

    public boolean getCritical(){return critical;}
}
