package spacecadets2016;

import robocode.*;

/**
 * AdvancedEnemyRobot - A class by Seco Vlad
 * Another helper class which extends the previously written EnemyRobot.
 */
public class AdvancedEnemyRobot extends EnemyRobot {

	private double x;
	private double y;
	
	public AdvancedEnemyRobot(){
		reset();
	}
	
	public void update(ScannedRobotEvent e, Robot robot){
		super.update(e);
		double absBearing = (robot.getHeading() + e.getBearing());
		if (absBearing < 0) absBearing += 360;
		x = robot.getX() + Math.sin(Math.toRadians(absBearing)) * e.getDistance();
		y = robot.getY() + Math.cos(Math.toRadians(absBearing)) * e.getDistance();
	}
	
	@Override
	public void reset(){
		super.reset();
		x = 0.0;
		y = 0.0;
	}
	
	/* Accesor methods */

	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getFutureX(long when){
		return x + Math.sin(Math.toRadians(getHeading())) * getVelocity() * when;
	}
	
	public double getFutureY(long when){
		return y + Math.cos(Math.toRadians(getHeading())) * getVelocity() * when;
	}
}
