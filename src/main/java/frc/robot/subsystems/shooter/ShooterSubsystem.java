package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class ShooterSubsystem extends SubsystemBase{
    private final SparkMax leftShooterMotor;
    private final SparkMax rightShooterMotor;

    public ShooterSubsystem() {
        leftShooterMotor = new SparkMax(11, MotorType.kBrushless);
        rightShooterMotor = new SparkMax(12, MotorType.kBrushless);
    }

    public void startShooter() {
        leftShooterMotor.set(1.0);
        rightShooterMotor.set(1.0);
    }

    public void stopShooter() {
        leftShooterMotor.set(0.0);
        rightShooterMotor.set(0.0);
    }
}