package org.usfirst.frc.team1014.robot.subsystems;

import org.usfirst.frc.team1014.robot.controls.ControlsManager;
import org.usfirst.frc.team1014.robot.sensors.IMU;
import org.usfirst.frc.team1014.robot.sensors.LIDAR;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

/**
 * This class defines the drive train subsystem and the abilities to do things like drive.
 * 
 * @author Manu S.
 * 
 */
public class DriveTrain extends BadSubsystem
{
	private RobotDrive train;
	
	private static DriveTrain instance;
	private SpeedController backLeft, frontLeft, backRight, frontRight;

	private LIDAR lidar;
	private Ultrasonic ultrasonic;
	
	private IMU mxp;
	private SerialPort serialPort;

	public DriveTrain()
	{

	}

	/**
	 * returns the current instance of drive train. If none exists, then it creates a new instance.
	 * 
	 * @return instance of the DriveTrain
	 */
	public static DriveTrain getInstance()
	{
		if(instance == null)
			instance = new DriveTrain();
		return instance;
	}

	@Override
	protected void initialize()
	{
		backLeft = new Talon(ControlsManager.BACK_LEFT_SPEED_CONTROLLER);
		frontLeft = new Talon(ControlsManager.FRONT_LEFT_SPEED_CONTROLLER);
		backRight = new Talon(ControlsManager.BACK_RIGHT_SPEED_CONTROLLER);
		frontRight = new Talon(ControlsManager.FRONT_RIGHT_SPEED_CONTROLLER);
		
		lidar = new LIDAR(Port.kMXP);
		
//		ultrasonic = new Ultrasonic(RobotMap.ultraPing, RobotMap.ultraEcho);
//		ultrasonic.setEnabled(true); ultrasonic.setAutomaticMode(true);
		
		//mxp stuff
    	serialPort = new SerialPort(57600,SerialPort.Port.kMXP);

		byte update_rate_hz = 127;
		mxp = new IMU(serialPort,update_rate_hz);
		Timer.delay(0.3);
        mxp.zeroYaw();

		train = new RobotDrive(backLeft, frontLeft, backRight, frontRight);
	}
	public void tankDrive(double leftStickY, double rightStickY) 
	{
		train.tankDrive(leftStickY, rightStickY);
	}

	public double getLIDARDistance()
	{
		lidar.updateDistance();
		return lidar.getDistance();
	}
	
	public double getUltraDistance(boolean inInches)
	{
		if(inInches)
			return ultrasonic.getRangeInches();
		else
			return ultrasonic.getRangeMM();
	}
	
	public double getAngle()// return -180 - 180
 	{
 		return (double)mxp.getYaw();
 	}
 	
 	public double getAngle360() // returns 0 -360
 	{
 		if(mxp.getYaw() < 0)
 			return mxp.getYaw() + 360;
 		else
 			return mxp.getYaw();
 	}
 	
 	public void resetMXPAngle()
 	{
 		mxp.zeroYaw();
 	}
 	
 	public IMU getMXP()
 	{
 		return mxp;
 	}
	
	@Override
	public String getConsoleIdentity()
	{
		return "DriveTrain";
	}

	@Override
	protected void initDefaultCommand()
	{

	}
}
