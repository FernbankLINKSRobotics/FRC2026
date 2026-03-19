// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.swervedrive;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonUtils;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.geometry.Rotation3d;

public class Vision extends SubsystemBase {

  public PhotonCamera centerCamera;

  public static final AprilTagFieldLayout rebuiltTagLayout = AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);

  public static final Transform3d cameraPosition = new Transform3d(new Translation3d(0,0,0), new Rotation3d());

  PhotonPoseEstimator photonEstimator = new PhotonPoseEstimator(rebuiltTagLayout, cameraPosition);

  public double targetYaw = 0.0;
  public double targetRange = 0.0;
  public int targetID;

  

  /** Creates a new Vision. */
  public Vision() {
    centerCamera = new PhotonCamera("center");
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