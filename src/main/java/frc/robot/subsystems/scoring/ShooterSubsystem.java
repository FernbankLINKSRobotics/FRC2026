package frc.robot.subsystems.scoring;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class ShooterSubsystem extends SubsystemBase{
    private final SparkMax leftShooterMotor;
    private final SparkMax rightShooterMotor;
    private final SparkMax indexerMotor;
    private final SparkMax hoodMotor;

    public ShooterSubsystem() {
        leftShooterMotor = new SparkMax(11, MotorType.kBrushless);
        rightShooterMotor = new SparkMax(12, MotorType.kBrushless);
        indexerMotor = new SparkMax(11, MotorType.kBrushless);
        hoodMotor = new SparkMax(11, MotorType.kBrushless);
    }

    public void startShooter() {
        leftShooterMotor.set(1.0);
        rightShooterMotor.set(1.0);
    }

    public void stopShooter() {
        leftShooterMotor.set(0.0);
        rightShooterMotor.set(0.0);
    }

    public void startIndexer() {
        indexerMotor.set(1.0);
    }

    public void stopIndexer() {
        indexerMotor.set(0.0);
    }

    public void adjustHoodUp() {
        hoodMotor.set(1.0);
    }

    public void adjustHoodDown() {
        hoodMotor.set(0.0);
    }
}