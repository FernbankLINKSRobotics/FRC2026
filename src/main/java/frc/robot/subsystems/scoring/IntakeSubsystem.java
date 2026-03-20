package frc.robot.subsystems.scoring;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.TalonFX;

/**
 * Subsystem that manages the robot's intake assembly.
 */
public class IntakeSubsystem extends SubsystemBase{
    private final SparkMax intakeMovementMotor;
    private final TalonFX intakeMotor;
    private final CANBus kCANBus = CANBus.roboRIO();
    public static boolean intakePower = false;

    public IntakeSubsystem() {
        intakeMovementMotor = new SparkMax(7, MotorType.kBrushed);
        intakeMotor = new TalonFX(10, kCANBus);
    }

    /**
     * Toggle the intake motor between on and off states.
     */
    public Command toggleIntake() {
        return runOnce(() -> {
            if (!intakePower) {
                intakeMotor.set(-0.7);
                intakePower = true;
            } else {
                intakeMotor.set(0.0);
                intakePower = false;
            }
        });
    }

    /**
     * Disables the intake
     */
    public Command disableIntake() {
        return runOnce(() -> {
            intakeMotor.set(0.0);
            intakePower = false;
        });
    }

    /**
     * Enables the intake
     */
    public Command enableIntake() {
        return runOnce(() -> {
            intakeMotor.set(-0.7);
            intakePower = true;
        });
    }

    /**
     * Lowers the intake by driving the intake movement motor at full positive speed while the command is active and stops the motor when the command ends.
     */
    public Command lowerIntake() {
        return runEnd(
            () -> intakeMovementMotor.set(-0.25),
            () -> intakeMovementMotor.set(0.0));
    }

    /**
     * Raises the intake by driving the intake movement motor at full negative speed while the command is active and stops the motor when the command ends.
     */
    public Command raiseIntake() {
        return runEnd(
            () -> intakeMovementMotor.set(0.5),
            () -> intakeMovementMotor.set(0.0));
    }

    /**
     * Command for the intake to run during the basic auto
     */
    public Command intakeAuto() {
        return runOnce(() -> {
            intakeMovementMotor.set(-0.2);
            Timer.delay(0.5);
            intakeMovementMotor.set(0.0);
        });
    }

    public Command powerIntake() {
        return startEnd(
            () -> intakeMotor.set(-0.7),
            () -> intakeMotor.set(0.0));
    }
}