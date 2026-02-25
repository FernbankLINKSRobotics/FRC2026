package frc.robot.subsystems.scoring;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;

public class IntakeSubsystem extends SubsystemBase{
    private final SparkMax intakeMovementMotor;
    private final TalonFX intakeMotor;
    private final CANBus kCANBus = CANBus.roboRIO();
    private final DutyCycleOut intakeMotorOn;
    private final DutyCycleOut intakeMotorOff;


    public IntakeSubsystem() {
        intakeMovementMotor = new SparkMax(15, MotorType.kBrushless);
        intakeMotorOn = new DutyCycleOut(1.0);
        intakeMotorOff = new DutyCycleOut(0.0);
        intakeMotor = new TalonFX(10, kCANBus);
    }

    public void StartIntake() {
        intakeMotor.set(1.0);
    }
}