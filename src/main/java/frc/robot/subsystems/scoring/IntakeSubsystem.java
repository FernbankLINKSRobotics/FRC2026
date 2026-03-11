package frc.robot.subsystems.scoring;

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
        intakeMotor = new TalonFX(9, kCANBus);
    }

    /**
     * Toggle the intake motor between on and off states.
     */
    public void toggleIntake() {
        if (intakePower) {
            intakeMotor.set(1.0);
        } else {
            intakeMotor.set(0.0);
        }
        intakePower = !intakePower;
    }

    /**
     * Lowers the intake by driving the intake movement motor at full positive speed while the command is active and stops the motor when the command ends.
     */
    public Command lowerIntake() {
        return runEnd(
            () -> intakeMovementMotor.set(1.0),
            () -> intakeMovementMotor.set(0.0));
    }

    /**
     * Raises the intake by driving the intake movement motor at full negative speed while the command is active and stops the motor when the command ends.
     */
    public Command raiseIntake() {
        return runEnd(
            () -> intakeMovementMotor.set(-1.0),
            () -> intakeMovementMotor.set(0.0));
    }
}