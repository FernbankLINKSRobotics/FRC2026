package frc.robot.subsystems.scoring;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
//import com.revrobotics.RelativeEncoder;
//import com.revrobotics.spark.SparkAbsoluteEncoder;

public class ShooterSubsystem extends SubsystemBase{
    private final SparkMax leftShooterMotor;
    //private final RelativeEncoder shooterHallSensor;
    //right shooter motor is set up as follower within SparkMax configs (REV Hardware Client)
    //private final SparkMax indexerMotor;
    //private final SparkMax hoodMotor;

    public ShooterSubsystem() {
        leftShooterMotor = new SparkMax(2, MotorType.kBrushless);
        //shooterHallSensor = leftShooterMotor.getEncoder();
        //right shooter motor is follower
        //indexerMotor = new SparkMax(14, MotorType.kBrushed);
        //hoodMotor = new SparkMax(14, MotorType.kBrushless);
    }

    public void startShooter() {
        leftShooterMotor.set(0.6);
        //right shooter motor is follower
        //startIndexer();
    }

    public void stopShooter() {
        leftShooterMotor.set(0.0);
        //right shooter motor is follower
        //stopIndexer();
    }

    public void startIndexer() {
        //indexerMotor.set(1.0);
    }

    public void stopIndexer() {
        //indexerMotor.set(0.0);
    }

    /*
    public void adjustHoodUp() {
        hoodMotor.set(1.0);
    }

    public void adjustHoodDown() {
        hoodMotor.set(0.0);
    }
    */
}