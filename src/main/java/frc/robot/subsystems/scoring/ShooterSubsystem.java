package frc.robot.subsystems.scoring;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;
//import com.revrobotics.RelativeEncoder;
//import com.revrobotics.spark.SparkAbsoluteEncoder;

public class ShooterSubsystem extends SubsystemBase{
    private SparkMax leftShooterMotor;
    //private final RelativeEncoder shooterHallSensor;
    //right shooter motor is set up as follower within SparkMax configs (REV Hardware Client)
    private SparkMax indexerMotor;
    private SparkClosedLoopController leftShooterController;
    private SparkFlexConfig leftShooterConfigs;
    //private final SparkMax hoodMotor;

    public ShooterSubsystem() {
        leftShooterMotor = new SparkMax(2, MotorType.kBrushless);
        //shooterHallSensor = leftShooterMotor.getEncoder();
        //right shooter motor is follower
        indexerMotor = new SparkMax(14, MotorType.kBrushed);
        leftShooterController = leftShooterMotor.getClosedLoopController();
        leftShooterConfigs = new SparkFlexConfig();
        setPIDConfigs();
        //hoodMotor = new SparkMax(14, MotorType.kBrushless);
    }

    public void setPIDConfigs() {
        leftShooterConfigs.closedLoop
            .p(0.1)
            .i(0.0)
            .d(0.1)
            .outputRange(0.0, 1.0);
        leftShooterMotor.configure(leftShooterConfigs, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }

    public void startShooter() {
        leftShooterController.setSetpoint(1000, ControlType.kPosition, ClosedLoopSlot.kSlot0);
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

    private int getShooterRPMs() {
        int RPM = 0;
        
        return RPM;
    }

    private double distanceToHub() {
        double distance = 0.0;
        
        return distance;
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