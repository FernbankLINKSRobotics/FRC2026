package frc.robot.subsystems.scoring;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.hardware.TalonFX;

public class IntakeSubsystem extends SubsystemBase{
    private final SparkMax intakeMovementMotor;
    private final TalonFX intakeMotor;
    private final CANBus kCANBus = CANBus.roboRIO();


    public IntakeSubsystem() {
        intakeMovementMotor = new SparkMax(15, MotorType.kBrushless);
        intakeMotor = new TalonFX(10, kCANBus);
    }

    
}