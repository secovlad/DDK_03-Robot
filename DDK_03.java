package spacecadets2016;
import robocode.*;
import java.awt.Color;
import java.awt.geom.Point2D;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 * DDK_03 - a robot by Seco Vlad
 * My third try at making a decent robot - this time implementing some
 * methods found in sample bots / online.
 * The main feature of DDK_03 is that it can smartly track enemies and it
 * accomplishes that with the EnemyRobot helper class.
 * 
 * NOTE: Before implementing the EnemyRobot class, I just used a 
 * simple string to keep track of the enemy. I did not remove that,
 * out of fear of making some pieces of code not work (I know, bad
 * practice, but I'm kind of rushing this poor bot for tomorrow's
 * competition (actually today's, it's 1am)
 */
public class DDK_03 extends AdvancedRobot {

	// the robot we are tracking - it's an AdvancedEnemyRobot!
	// this class has the special purpose of implementing predictive 
	// aiming, to get those pesky fast moving robots!
	private AdvancedEnemyRobot enemy = new AdvancedEnemyRobot();
	
	String trackedRobot; // name of the unlucky robot (redundant but eh)
	int turns = 0; // how many turns we have been tracking for
	double gunTurn; // how much to turn the gun when searching

	/**
	 * run: DDK_03's default behavior
	 */
	public void run() {
	
		// set the robot's colors - rocking the team colors, 
		// same as the other DDK robots before it
		setBodyColor(new Color(20, 170, 38)); 
		setGunColor(new Color(130, 16, 13));
		setRadarColor(new Color(9, 47, 142));
		setBulletColor(new Color(0, 220, 65));
		setScanColor(new Color(0, 130, 147));
		
		// prepare the gun
		trackedRobot = null;
		setAdjustGunForRobotTurn(true);
		gunTurn = -10;
		
		// reset the tracked enemy to avoid any weird behaviour
		enemy.reset();
		
		// the main behavioural loop
		while(true) {
			// turn the gun to look for enemies
			setTurnGunRight(gunTurn);
			//increment the number of turns we've been searching
			turns++;
			// look right for our target
			if (turns > 2)
				gunTurn = 10;
			// then left
			if (turns > 5)
				gunTurn = -10;
			// but stop looking if we can't find them after 10 turns
			if (turns > 11)
				trackedRobot = null;
			setTurnGunRight(gunTurn);
			execute();
		}
	}

	/**
	 * onScannedRobot: We attempt to track a robot when we see it
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		
		// return out of the function if we have a target which is not
		// the scanned one
		if (trackedRobot != null && !e.getName().equals(trackedRobot)) {
			return;
		}
		
		// if we didn't have a target, register the scanned robot!
		if (trackedRobot == null){
			trackedRobot = e.getName();
			System.out.println("Currently tracking "+trackedRobot);
		}
		
		// update the enemy we are tracking
		// we start tracking the robot we spotted if:
		// we were not tracking anything before, or
		// the one we just spotted is closer, or
		// we've found the one we're tracking
		if (enemy.none() || e.getName().equals(enemy.getName()) ||
			e.getDistance() < enemy.getDistance() - 70){
			enemy.update(e, this);
		}
		
		// reset the number of turns since we've scanned a robot
		turns = 0;
		
		// if our target is too far away, turn and move toward it
		if (e.getDistance() > 150){
			gunTurn = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
			setTurnGunRight(gunTurn);
			setTurnRight(e.getBearing());
			setAhead(e.getDistance() - 140);
			execute();
			return;
		}
		
		// keep gunTurn updated to where the enemy will be heading
		gunTurn = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		// calculate firepower based on distance
		double firepower = Math.min(500 / e.getDistance(), 3);
		// turn the gun to the predicted x, y location
		setTurnGunRight(gunTurn);
		
		// this is to prevent premature firing
		if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10)
			fire(firepower);
		
		// the target is too close! retreat
		if (e.getDistance() < 100){
			if (e.getBearing() > -90 && e.getBearing() <= 90){
				back(40);
			}
			else {
				ahead(40);
			}
		}
		execute();
		scan();
	}

	/**
	 * onHitByBullet: If the bullet came from closer than the robot
	 * we're currently tracking, switch targets and destroy the
	 * other dude.
	 * 
	 * NOTE: We can't properly update the enemy robot from here so
	 * I'm just resetting it (that's not good now, is it)
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		trackedRobot = e.getName();
		enemy.reset();
	}
	
	/**
	 * onHitWall: Back up when you hit a wall. Check the bearing so
	 * you can tell which way to back up.
	 */
	public void onHitWall(HitWallEvent e) {
		if (e.getBearing() > -90 && e.getBearing() <= 90){
			setBack(40);
			setTurnRight(90);
		}
		else {
			setAhead(40);
		}
		execute();
	}	
	
	/**
	 * onHitRobot: Get pissed off at him and make him our new target!
	 */
	public void onHitRobot(HitRobotEvent e){
		// only print if it wasn't already our target
		if (trackedRobot != null && !trackedRobot.equals(e.getName())){
			System.out.println("Now tracking "+e.getName()+" because he bumped into us.");
		}
		// set the target
		trackedRobot = e.getName();
		// back up a bit
		gunTurn = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		setTurnGunRight(gunTurn);
		setFire(3);
		setBack(50);
		execute();
	}
	
	/**
	 * onRobotDeath: Reset the enemy we were tracking after murdering it.
	 */
	public void onRobotDeath(RobotDeathEvent e){
		if (e.getName().equals(enemy.getName())){
			enemy.reset();
		}
	}
	
	/**
	 * onWin: Gotta have a nonsensical victory dance!
	 */
	public void onWin(WinEvent e){
		for (int i=1; i<=50; i++){
			setAhead(20);
			turnRight(45);
			setBack(20);
			turnLeft(45);
		}
	}	
}
