package frc.robot.subsystems.scoring;

import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
//import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;

import com.revrobotics.spark.SparkMax;
//import com.revrobotics.spark.ClosedLoopSlot;
//import com.revrobotics.spark.SparkBase.ControlType;
//import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
//import com.revrobotics.spark.config.SparkMaxConfig;
//import com.revrobotics.ResetMode;
//import com.revrobotics.PersistMode;
//import frc.robot.subsystems.swervedrive.Vision;
//import frc.robot.Constants;

public class ShooterSubsystem extends SubsystemBase{
    private SparkMax leftShooterMotor;
    private SparkMax rightShooterMotor;
    private SparkMax indexerMotor;
    /*private SparkClosedLoopController leftShooterController;
    private SparkMaxConfig leftShooterConfigs;
    private SparkClosedLoopController rightShooterController;
    private SparkMaxConfig rightShooterConfigs;*/
    public int RPMs = 1000;
    public Boolean shooterPower = false;
    public Boolean indexerPower = false;
    public Double shooterLevel = 0.6;
    public Command indexCMD = new SequentialCommandGroup(new WaitCommand(2), new InstantCommand(() -> indexerMotor.set(0.45)));
    //private Vision vision = new Vision();

    public ShooterSubsystem() {
        leftShooterMotor = new SparkMax(2, MotorType.kBrushless);
        rightShooterMotor = new SparkMax(13, MotorType.kBrushless);
        indexerMotor = new SparkMax(14, MotorType.kBrushed);
        //leftShooterController = leftShooterMotor.getClosedLoopController();
        //leftShooterConfigs = new SparkMaxConfig();
        //rightShooterController = rightShooterMotor.getClosedLoopController();
        //rightShooterConfigs = new SparkMaxConfig();
        //setPIDConfigs();
    }

    /**
     * Autonomous shooting sequence:
     * runs the shooter motors at 60% power for 2 seconds to reach the target speed,
     * then runs the indexer at full power for 5 seconds to feed balls into the shooter,
     * and finally stops all motors.
     */
    public Command AutoShoot() {
        return runOnce(() -> {
            leftShooterMotor.set(0.6);
            rightShooterMotor.set(0.6);
            Timer.delay(4);
            indexerMotor.set(0.45);
            CommandScheduler.getInstance().schedule(RobotContainer.intakeSubsystem.enableIntake());
            Timer.delay(10.0);
            leftShooterMotor.set(0);
            rightShooterMotor.set(0);
            indexerMotor.set(0);
            CommandScheduler.getInstance().schedule(RobotContainer.intakeSubsystem.disableIntake());
        });
    }

    /**
     * Initialize and apply PID and output-range settings for the left shooter closed-loop controller.
     *
    public void setPIDConfigs() {
        leftShooterConfigs.closedLoop
            .p(0.5)
            .i(0.0)
            .d(0.1)
            .outputRange(0.0, 1.0);
        leftShooterMotor.configure(leftShooterConfigs, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        rightShooterConfigs.closedLoop
            .p(0.5)
            .i(0.0)
            .d(0.1)
            .outputRange(0.0, 1.0);
        rightShooterMotor.configure(rightShooterConfigs, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        DriverStation.reportWarning(("PID configs initialized in subsystem with name: " + getName()), false);
    }*/

    /**
     * Command to disable the shooter motors.
     */
    public Command disableShooter() {
        return runOnce(() -> {
            //leftShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
            //rightShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
            leftShooterMotor.set(0.0);
            rightShooterMotor.set(0.0);
            indexerMotor.set(0.0);
            shooterPower = false;
        });
    }

    /**
     * Toggle the shooter's power state and updates the left shooter controller setpoint.
     *
    public Command toggleShooter(double RPMs) {
        return runOnce(() -> {
            if (!shooterPower) {
                leftShooterController.setSetpoint(RPMs, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                rightShooterController.setSetpoint(RPMs, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                //leftShooterMotor.set(0.6);
                //rightShooterMotor.set(0.6);
                shooterPower = true;
            } else {
                leftShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                rightShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                //leftShooterMotor.set(0);
                //rightShooterMotor.set(0);
                shooterPower = false;
            }
        });
    }*/

    /**
     * Calculates the required RPMs for the shooter based on the distance to the target.
     * @return RPMs needed to fire fuel into the hub.
     *
    public int getShooterRPMs() {
        double distance = vision.getDistanceToHub();
        double RPMs;
        double exitVelocity = distance; //TODO: Implement exit velocity calculation based on distance
        RPMs = (exitVelocity/Constants.ShooterConstants.WHEEL_RADIUS)*120*Math.PI;
        return (int) RPMs; // Return the calculated RPMs
    }*/

    public Command fixedShot(double power) {
        return runOnce(() -> {
            if (!shooterPower) {
                //leftShooterController.setSetpoint(1000, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                //rightShooterController.setSetpoint(1000, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                leftShooterMotor.set(power);
                rightShooterMotor.set(power);
                shooterPower = true;
                CommandScheduler.getInstance().schedule(indexCMD);
            } else {
                //leftShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                //rightShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                leftShooterMotor.set(0);
                rightShooterMotor.set(0);
                shooterPower = false;
                indexerMotor.set(0);
            }
        });
    }

    public Command reverseShooter() {
        return startEnd(
            () -> {
                leftShooterMotor.set(-1.0);
                rightShooterMotor.set(-1.0);
            },
            () -> {
                leftShooterMotor.set(0.0);
                rightShooterMotor.set(0.0);
            }
        );
    }

    /**
     * Toggles the indexer between running and stopped.
     */
    public Command toggleIndexer() {
        return runOnce(() -> {
            if (!indexerPower) {
                indexerMotor.set(1.0);
                indexerPower = true;
            } else {
                indexerMotor.set(0.0);
                indexerPower = false;
            }
        });
    }

    public Command runIndexer() {
        return startEnd(
            () -> indexerMotor.set(1.0),
            () -> indexerMotor.set(0.0)
        );
    }

    /**
     * No-operation placeholder method.
     */
    public void nothing() {}

}