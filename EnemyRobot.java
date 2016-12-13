package spacecadets2016;

import robocode.*;

/**
 * EnemyRobot - a class by Seco Vlad
 * Helper class used to hold data on an enemy robot
 */
public class EnemyRobot {

	/* Private instance variables holding the stats of the robot */
	private double bearing;
	private double distance;
	private double energy;
	private double heading;
	private double velocity;
	private String name;
	
	/**
	 * Constructor which just resets the values.
	 */
	public EnemyRobot(){
		reset();
	}
	
	/**
	 * This is the method which updates the instance variables.
	 */
	public void update(ScannedRobotEvent e){
		bearing = e.getBearing();
		distance = e.getDistance();
		energy = e.getEnergy();
		heading = e.getHeading();
		velocity = e.getVelocity();
		name = e.getName();
	}
	
	/**
	 * This is the method which resets the instance variables.
	 */
	public void reset(){
		bearing = 0.0;
		distance = 0.0;
		energy = 0.0;
		heading = 0.0;
		velocity = 0.0;
		name = "";
	}
	
	/**
	 * This method returns true if the data has just been reset.
	 */
	public boolean none(){
		boolean returnVal = false;
		if (name.equals("")) returnVal = true;
		return returnVal;
	}
	
	/* Accessor methods */

	public double getBearing(){
		return bearing;
	}
	
	public double getDistance(){
		return distance;
	}
	
	public double getEnergy(){
		return energy;
	}
	
	public double getHeading(){
		return heading;
	}
	
	public double getVelocity(){
		return velocity;
	}
	
	public String getName(){
		return name;
	}
}
