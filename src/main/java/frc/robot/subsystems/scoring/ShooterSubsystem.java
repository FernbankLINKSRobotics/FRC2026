package frc.robot.subsystems.scoring;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;

public class ShooterSubsystem extends SubsystemBase{
    private SparkMax leftShooterMotor;
    private SparkMax rightShooterMotor;
    private SparkMax indexerMotor;
    private SparkClosedLoopController leftShooterController;
    private SparkMaxConfig leftShooterConfigs;
    private SparkClosedLoopController rightShooterController;
    private SparkMaxConfig rightShooterConfigs;
    public int RPMs = 1000;
    public Boolean shooterPower = false;
    public Boolean indexerPower = false;
    public Double shooterLevel = 0.6;

    public ShooterSubsystem() {
        leftShooterMotor = new SparkMax(2, MotorType.kBrushless);
        rightShooterMotor = new SparkMax(13, MotorType.kBrushless);
        indexerMotor = new SparkMax(14, MotorType.kBrushed);
        leftShooterController = leftShooterMotor.getClosedLoopController();
        leftShooterConfigs = new SparkMaxConfig();
        rightShooterController = rightShooterMotor.getClosedLoopController();
        rightShooterConfigs = new SparkMaxConfig();
        setPIDConfigs();
    }

    /**
     * Autonomous shooting sequence:
     * runs the shooter motors at 60% power for 2 seconds to reach the target speed,
     * then runs the indexer at full power for 5 seconds to feed balls into the shooter,
     * and finally stops all motors.
     * @deprecated This method uses blocking delays which interfere with WPIlib's commands architecture.
     */
    public void AutoShoot() {
        leftShooterMotor.set(0.6);
        rightShooterMotor.set(0.6);
        Timer.delay(2.0); // Alternative to Thread.sleep for better compatibility with WPILib's command-based framework
        indexerMotor.set(1.0);
        Timer.delay(5.0); // Alternative to Thread.sleep for better compatibility with WPILib's command-based framework
        leftShooterMotor.set(0);
        rightShooterMotor.set(0);
        indexerMotor.set(0);
    }

    /**
     * Initialize and apply PID and output-range settings for the left shooter closed-loop controller.
     */
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
    }

    /**
     * Command to disable the shooter motors.
     */
    public Command disableShooter() {
        return runOnce(() -> {
            leftShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
            rightShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
            //leftShooterMotor.set(0);
            //rightShooterMotor.set(0);
            shooterPower = false;
        });
    }

    /**
     * Toggle the shooter's power state and updates the left shooter controller setpoint.
     */
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
    }

    public Command fixedShot(double power) {
        return runOnce(() -> {
            if (!shooterPower) { try {
                leftShooterController.setSetpoint(1000, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                rightShooterController.setSetpoint(1000, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                //.set(power);
                //rightShooterMotor.set(power);
                shooterPower = true;
                Thread.sleep((long) Math.pow(power*1.7, 2.0)*1000);
                indexerMotor.set(1.0);
                } catch (InterruptedException e) {}
            } else {
                leftShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                rightShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                //leftShooterMotor.set(0);
                //rightShooterMotor.set(0);
                shooterPower = false;
                indexerMotor.set(0);
            }
        });
    }

    /**
     * Toggles the indexer between running and stopped.
     */
    public Command toggleIndexer() {
        return runOnce(() -> {
            if (!indexerPower) {
                indexerMotor.set(1.0);
                indexerPower = !indexerPower;
            } else {
                indexerMotor.set(0.0);
                indexerPower = !indexerPower;
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