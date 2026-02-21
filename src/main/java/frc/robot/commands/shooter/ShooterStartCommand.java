package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.scoring.ShooterSubsystem;

public class ShooterStartCommand extends Command{

    public ShooterStartCommand() {
        this.addRequirements(RobotContainer.shooterSubsystem);
    }

    public void execute() {
        RobotContainer.shooterSubsystem.startShooter();
    }
}
