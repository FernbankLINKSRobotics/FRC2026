package frc.robot.subsystems.scoring;

import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
//import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.RobotContainer;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;

import frc.robot.Constants;


public class ShooterSubsystem extends SubsystemBase{
    private SparkMax leftShooterMotor;
    private SparkClosedLoopController leftShooterController;
    private SparkMaxConfig leftShooterConfigs;

    private SparkMax rightShooterMotor;
    private SparkClosedLoopController rightShooterController;
    private SparkMaxConfig rightShooterConfigs;

    private SparkMax indexerMotor;
    public Boolean indexerPower = false;
    public Command indexCMD = new SequentialCommandGroup(
        new WaitCommand(1),
        new InstantCommand(() -> indexerMotor.set(0.45))
    );

    public Boolean shooterPower = false;

    //private Vision vision = new Vision();

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
     */
    public Command AutoShoot() {
        return runOnce(() -> {
            leftShooterMotor.set(0.6);
            rightShooterMotor.set(0.6);
            Timer.delay(4);
            indexerMotor.set(0.45);
            CommandScheduler.getInstance().schedule(RobotContainer.intakeSubsystem.enableIntake());
            CommandScheduler.getInstance().schedule(RobotContainer.intakeSubsystem.intakeAuto());
            Timer.delay(10.0);
            leftShooterMotor.set(0);
            rightShooterMotor.set(0);
            indexerMotor.set(0);
            CommandScheduler.getInstance().schedule(RobotContainer.intakeSubsystem.disableIntake());
        });
    }

    /**
     * Initialize and apply PID and output-range settings for the left shooter closed-loop controller.
     */
    public void setPIDConfigs() {
        leftShooterConfigs.closedLoop.pid(0.5, 0, 0.25).outputRange(0.5, 1.0).feedForward.kS(0).kV(10).kA(0);
        leftShooterMotor.configure(leftShooterConfigs, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

        rightShooterConfigs.closedLoop.pid(0.5, 0, 0.25).outputRange(0.5, 1.0).feedForward.kS(0).kV(10).kA(0);
        rightShooterMotor.configure(rightShooterConfigs, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }

    /**
     * Command to disable the shooter motors.
     */
    public Command disableShooter() {
        return runOnce(() -> {
            leftShooterMotor.set(0.0);
            rightShooterMotor.set(0.0);
            indexerMotor.set(0.0);
            shooterPower = false;
        });
    }

    public void setVoltage(double volts) {
        leftShooterMotor.setVoltage(volts);
        rightShooterMotor.setVoltage(volts);
    }

    /**
     * Calculates the required RPMs for the shooter based on the distance to the target.
     * @return RPMs needed to fire fuel into the hub.
     */
    public int getShooterRPMs() {
        double distance = RobotContainer.vision.targetRange;
        double RPMs;
        double exitVelocity = distance;
        RPMs = (exitVelocity/Constants.ShooterConstants.WHEEL_RADIUS)*120*Math.PI;
        return (int) RPMs; // Return the calculated RPMs
    }

    public void initializeShooter() {
        int targetRPMs = getShooterRPMs();
        leftShooterController.setSetpoint(targetRPMs, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
        rightShooterController.setSetpoint(targetRPMs, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
        shooterPower = true;
    }

    public Command visionShot() {
        return runOnce(() -> {
            if (!indexerPower) {
                int targetRPMs = getShooterRPMs();
                leftShooterController.setSetpoint(targetRPMs, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                rightShooterController.setSetpoint(targetRPMs, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                indexerPower = true;
                indexerMotor.set(0.45);
            } else {
                leftShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                rightShooterController.setSetpoint(0, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                indexerPower = false;
                CommandScheduler.getInstance().cancel(indexCMD);
                indexerMotor.set(0);
            }
        });
    }

    public Command fixedShot(double power) {
        return runOnce(() -> {
            if (!shooterPower) {
                //leftShooterController.setSetpoint(power*4000, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                //rightShooterController.setSetpoint(power*4000, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
                leftShooterMotor.set(power);
                rightShooterMotor.set(power);
                shooterPower = true;
                CommandScheduler.getInstance().schedule(indexCMD);
            } else {
                leftShooterMotor.set(0.4);
                rightShooterMotor.set(0.4);
                shooterPower = false;
                indexerMotor.set(0);
                CommandScheduler.getInstance().cancel(indexCMD);
            }
        });
    }

    public Command reverseShooter() {
        return startEnd(
            () -> {
                leftShooterMotor.set(-1.0);
                rightShooterMotor.set(-1.0);
            },
            () -> {
                leftShooterMotor.set(0.0);
                rightShooterMotor.set(0.0);
            }
        );
    }

    // Create the SysId routine
    private final SysIdRoutine sysIdShooterMotorRoutine =
        new SysIdRoutine(
            new SysIdRoutine.Config(),
            new SysIdRoutine.Mechanism(
                (voltage) -> setVoltage(voltage.in(Volts)),
                null,
                this
            )
        );

    public Command sysIdTestAll = new SequentialCommandGroup(
        sysIdShooterMotorRoutine.quasistatic(Direction.kForward),
        new WaitCommand(5),
        sysIdShooterMotorRoutine.quasistatic(Direction.kReverse),
        new WaitCommand(5),
        sysIdShooterMotorRoutine.dynamic(Direction.kForward),
        new WaitCommand(5),
        sysIdShooterMotorRoutine.dynamic(Direction.kReverse)
    );
}