package frc.robot.subsystems.swervedrive;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import swervelib.SwerveDrive;
import swervelib.SwerveModule;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.Timer;

public class PerModuleFFSwerveController
{
  private final SwerveDrive swerveDrive;
  private final moduleFeedForwards feedforwards;

  public PerModuleFFSwerveController(SwerveDrive swerveDrive, moduleFeedForwards feedforwards)
  {
    this.swerveDrive = swerveDrive;
    this.feedforwards = feedforwards;
  }

  public void drive(Translation2d translation, double rotationRadiansPerSecond, boolean fieldRelative) {
    ChassisSpeeds robotRelativeSpeeds =
        fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(
                translation.getX(),
                translation.getY(),
                rotationRadiansPerSecond,
                swerveDrive.getYaw())
            : new ChassisSpeeds(
                translation.getX(),
                translation.getY(),
                rotationRadiansPerSecond);

    driveRobotRelative(robotRelativeSpeeds);
  }

  public void driveRobotRelative(ChassisSpeeds robotRelativeSpeeds)
  {
    // Let YAGSL do the kinematics/optimization work.
    SwerveModuleState[] desiredStates =
        swerveDrive.toServeModuleStates(robotRelativeSpeeds, true);

    SwerveModule[] modules = swerveDrive.swerveDriveConfiguration.modules;

    for (int i = 0; i < modules.length; i++)
    {
      double targetSpeed = desiredStates[i].speedMetersPerSecond;

      // Basic FF using desired velocity.
      double ffVolts = feedforwards.get(i).calculate(targetSpeed);

      modules[i].setDesiredState(desiredStates[i], false, ffVolts);
    }
  }
}