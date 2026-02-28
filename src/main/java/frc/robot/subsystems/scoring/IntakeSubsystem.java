package frc.robot.subsystems.scoring;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.TalonFX;

public class IntakeSubsystem extends SubsystemBase{
    private final SparkMax intakeMovementMotor;
    private final TalonFX intakeMotor;
    private final CANBus kCANBus = CANBus.roboRIO();
    private boolean intakePower = false;


    public IntakeSubsystem() {
        intakeMovementMotor = new SparkMax(7, MotorType.kBrushed);
        intakeMotor = new TalonFX(9, kCANBus);
    }

    public void StartIntake() {
        if (intakePower) {
            intakeMotor.set(1.0);
        } else {
            intakeMotor.set(0.0);
        }
        intakePower = !intakePower;
    }

    public void StopIntake() {
        intakeMotor.set(0.0);
    }

    public void LowerIntake() {
        intakeMovementMotor.set(0.1);
        
        intakeMovementMotor.set(0.0);
    }

    public void RaiseIntake() {
        intakeMovementMotor.set(-0.1);
        
        intakeMovementMotor.set(0.0);
    }
}