package frc.robot.subsystems.scoring;

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
            shooterPower = false;
        });
    }

    /**
     * Toggle the shooter's power state and updates the left shooter controller setpoint.
     */
    public Command toggleShooter() {
        return runOnce(() -> {
            if (!shooterPower) {
                leftShooterController.setSetpoint(1000, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                rightShooterController.setSetpoint(1000, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                shooterPower = true;
            } else {
                leftShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                rightShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                shooterPower = false;
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

    /**
     * No-operation placeholder method.
     */
    public void nothing() {}

}