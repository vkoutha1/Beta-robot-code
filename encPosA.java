package org.usfirst.frc.team193.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class encPosA {

	
	String gameData;
	// Right Talon Motor- Drive
	TalonSRX rightTalonMotor;
	// Left Talon Motor- Drive
	TalonSRX leftTalonMotor;
	//Talon Arm Motor
	TalonSRX TalonArmMotor;
	//Talon Intake Motor
	TalonSRX TalonIntakeMotor;
	//Right Victor Motor- Drive
	VictorSPX rightVictorMotor;
	//Right Victor Motor- Drive
	VictorSPX rightVictor1Motor;
	//Left Victor Motor- Drive
	VictorSPX leftVictorMotor;
	//Left Victor Motor- Drive
	VictorSPX leftVictor1Motor;
	//Victor Arm Motor
	VictorSPX VictorArmMotor;
	//Victor Intake Motor
	VictorSPX VictorIntakeMotor;
	//timer
	Timer time;
	
	DigitalInput upSwitch;
	
	boolean intakeStop = false;
	boolean armRaising = false;
	
	public encPosA(TalonSRX rTal, TalonSRX lTal, TalonSRX armTal, VictorSPX armVic, TalonSRX intakeTal,
			VictorSPX intakeVic, VictorSPX rVic1, VictorSPX rVic2, VictorSPX lVic1, VictorSPX lVic2, Timer timer, DigitalInput upSwitch){
		
		rightTalonMotor = rTal;
		leftTalonMotor = lTal;
		TalonArmMotor = armTal;
		TalonIntakeMotor = intakeTal;
		rightVictorMotor = rVic1;
		rightVictor1Motor = rVic2;
		leftVictorMotor = lVic1;
		leftVictor1Motor = lVic2;
		VictorArmMotor = armVic;
		VictorIntakeMotor = intakeVic;
		time = timer;
		this.upSwitch = upSwitch;
		
	}
	
	public void start(){		
		
		//If the switch is on the left side (go to the switch)
		if(gameData.charAt(0) == 'L' && gameData.charAt(1)=='R'){
			
			//Monitor left talon motor for ease of turning
			switch (leftTalonMotor.getSelectedSensorPosition(0)){
			
			case 0:
				
				//Go all the way forward to side of the switch
				leftTalonMotor.set(ControlMode.PercentOutput, .5);
				rightTalonMotor.set(ControlMode.PercentOutput, -.5);
				
				break;
			
			//Go forward for 9216 counts
			case 9216:
				
				//Begin raising the arm; Turn right so arm is facing the switch
				leftTalonMotor.set(ControlMode.PercentOutput, .15);
				rightTalonMotor.set(ControlMode.PercentOutput, .15);
				TalonArmMotor.set(ControlMode.Velocity, -6.5);
				
				break;

			//Turn for about 800 counts
			case 10000:
				
				//Go forward a small amount so robot hits switch
				leftTalonMotor.set(ControlMode.PercentOutput, .3);
				rightTalonMotor.set(ControlMode.PercentOutput, -.3);
				TalonArmMotor.set(ControlMode.Velocity, -3);
				
				break;
			
			//Go forward for about 1000 counts
			case 11000:
				
				//Stop wheels and start intake
				leftTalonMotor.set(ControlMode.PercentOutput, 0);
				rightTalonMotor.set(ControlMode.PercentOutput, 0);
				TalonIntakeMotor.set(ControlMode.PercentOutput, -.5);
				
				break;
				
				default: 
					
					break;
					
					
					}//End of switch statement
			
			//Starting timer for intake to run
			if (TalonIntakeMotor.getMotorOutputPercent() == -.5 && intakeStop == false){
				time.reset();
				intakeStop = true;
			}  
			
			//Stopping the intake 
			if (time.get() > 1.5 && intakeStop == true){
				TalonIntakeMotor.set(ControlMode.PercentOutput, 0);
			}
//      ------------------------------------------------------------------------------
				
			//If we own the scale on our side
			}else if (gameData.charAt(1) == 'L'){
				
				switch(rightTalonMotor.getSelectedSensorPosition(0)){
				
				case 0:
					
					//Go forward all the way to the side of the scale
					leftTalonMotor.set(ControlMode.PercentOutput, .5);
					rightTalonMotor.set(ControlMode.PercentOutput, -.5);
					
					break;
				
				//Go forward for 17152 counts
				case 17152:
					
					//Turn so that back of the robot is facing scale; Begin raising arm
					leftTalonMotor.set(ControlMode.PercentOutput, -.2);
					rightTalonMotor.set(ControlMode.PercentOutput, -.2);
					armRaising = true;
					
					break;
				
				//Turn for about 850 counts
				case 18000:
					
					//Stop moving, stop the arm, and start the intake
					leftTalonMotor.set(ControlMode.PercentOutput, 0);
					rightTalonMotor.set(ControlMode.PercentOutput, 0);
					
					break;
					
					default:
						
						break;
						
				}//End of switch statement
				
				//Raising the arm for the scale
				if(upSwitch.get()==true && armRaising == true){
					
					TalonArmMotor.set(ControlMode.Velocity, -10);
					
					}else if (upSwitch.get()==false && armRaising == true){
						
						TalonArmMotor.set(ControlMode.Velocity, 0);
						TalonArmMotor.setIntegralAccumulator(0, 0, 0);
						armRaising = false;
						
					}
				
				//Starting the intake once arm raise is complete
				
				if (armRaising == false){
					TalonIntakeMotor.set(ControlMode.PercentOutput, -.65);
				}
				//Starting timer for intake to run
				if (TalonIntakeMotor.getMotorOutputPercent() == -.65 && intakeStop == false){
					time.reset();
					intakeStop = true;
				}
				
				//Stopping the intake 
				if (time.get() > 1.5 && intakeStop == true){
					TalonIntakeMotor.set(ControlMode.PercentOutput, 0);
				
			}
			
//	 ----------------------------------------------------------------------------
		
		//If neither the switch nor the scale is on our left side (Just cross the autoline)
		}else if (gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R'){
			
			
			//Monitor left talon motor (motor to monitor doesn't matter)
			switch (leftTalonMotor.getSelectedSensorPosition(0)){
			
			case 0:
				
				leftTalonMotor.set(ControlMode.PercentOutput, .5);
				rightTalonMotor.set(ControlMode.PercentOutput, -.5);
				
				break;
				
			case 6656:
				
				leftTalonMotor.set(ControlMode.PercentOutput, 0);
				rightTalonMotor.set(ControlMode.PercentOutput, 0);
				
				break;
				
				default:
					
					break;
			
			}
			
		} // End of gameData if statements
		
	}
	
}