import org.group4.CombatSystem;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import static org.junit.Assert.assertEquals;

/** 
* CombatSystem Tester. 
* 
* @author team 4
* @since <pre>Apr 24, 2020</pre> 
* @version 1.0 
*/ 
public class CombatSystemTest { 

    CombatSystem combat = new CombatSystem();

/**
* 
* Method: setMainCharAttack(int attackNum, double rate)
* 
*/ 
@Test
public void testSetMainCharAttack() throws Exception {
    combat.setMainCharAttack(3, 0.6);
    int attackValue1 = combat.getMainChar().getAttack();
    boolean isCritical1 = combat.getMainChar().getCritical();
    assertEquals(3, attackValue1);
    assertEquals(false, isCritical1);
    combat.setMainCharAttack(1, 0.3);
    int attackValue2 = combat.getMainChar().getAttack();
    boolean isCritical2 = combat.getMainChar().getCritical();
    assertEquals(2, attackValue2);
    assertEquals(true, isCritical2);

}

/**
 *
 * Method: setMainCharDefend(int defendNum)
 *
 */
@Test
public void testSetMainCharDefend() throws Exception{
    combat.setMainCharDefend(5);
    int defendValue = combat.getMainChar().getDefend();
    assertEquals(5, defendValue);
}


/**
*
* Method: setEnemyValue(double status, int enemyNum)
*
*/
@Test
public void testSetEnemyValue() throws Exception {
    combat.setEnemyValue(1, 5, 0);
    int defendValue = combat.getEnemyPlayer().getDefend();
    assertEquals(5, defendValue);
    combat.setEnemyValue(0, 4, 0.8);
    int attackValue1 = combat.getEnemyPlayer().getAttack();
    assertEquals(4, attackValue1);
    combat.setEnemyValue(0, 4, 0.1);
    int attackValue2 = combat.getEnemyPlayer().getAttack();
    assertEquals(8, attackValue2);

}



} 
