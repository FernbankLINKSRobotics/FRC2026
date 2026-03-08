package frc.robot.subsystems.scoring;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.TalonFX;

public class IntakeSubsystem extends SubsystemBase{
    private final SparkMax intakeMovementMotor;
    private final TalonFX intakeMotor;
    private final CANBus kCANBus = CANBus.roboRIO();
    public static boolean intakePower = false;


    public IntakeSubsystem() {
        intakeMovementMotor = new SparkMax(7, MotorType.kBrushed);
        intakeMotor = new TalonFX(9, kCANBus);
    }

    public void toggleIntake() {
        if (intakePower) {
            intakeMotor.set(1.0);
        } else {
            intakeMotor.set(0.0);
        }
        intakePower = !intakePower;
    }

    public Command lowerIntake() {
        return runEnd(
            () -> intakeMovementMotor.set(1.0),
            () -> intakeMovementMotor.set(0.0));
    }

    public Command raiseIntake() {
        return runEnd(
            () -> intakeMovementMotor.set(-1.0),
            () -> intakeMovementMotor.set(0.0));
    }
}