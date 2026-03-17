// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.swervedrive;

import org.photonvision.PhotonCamera;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;

import org.photonvision.PhotonUtils;

public class Vision extends SubsystemBase {

  public PhotonCamera centerCamera = new PhotonCamera("center");

  public double targetYaw = 0.0;
  public double targetRange = 0.0;
  public int targetID;

  /** Creates a new Vision. */
  public Vision() {
    if (DriverStation.getAlliance().toString() == "Red") {
      targetID = 10; // Red alliance hub AprilTag
    } else {
      targetID = 26; // Blue alliance hub AprilTag
    }
  }

  public double getDistanceToHub() {
    var result = centerCamera.getLatestResult();
    if(result.hasTargets()) {
      for (var target : result.getTargets()) {
        if (target.getFiducialId() == targetID) {
          targetYaw = target.getYaw();
          targetRange = PhotonUtils.calculateDistanceToTargetMeters(Constants.CAMERA_HEIGHT, Constants.HUB_TAG_HEIGHT, Constants.CAMERA_PITCH, Units.degreesToRadians(target.getPitch()));
          targetID = target.getFiducialId();
        }
      }
    }
    return targetRange;
  }
}