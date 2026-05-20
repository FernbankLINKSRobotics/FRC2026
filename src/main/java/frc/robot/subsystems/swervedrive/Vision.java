// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.swervedrive;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonUtils;
//import org.photonvision.targeting.PhotonTrackedTarget;
//import org.photonvision.EstimatedRobotPose;
//import java.util.Optional;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.geometry.Rotation3d;

public class Vision extends SubsystemBase {
  public static final AprilTagFieldLayout rebuiltField = AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);


  /* public PhotonCamera leftCamera;

  public PhotonCamera rightCamera;

  public PhotonPoseEstimator leftEstimator = new PhotonPoseEstimator(rebuiltField, new Transform3d(new Translation3d(0,0,0), new Rotation3d()));
  public PhotonPoseEstimator rightEstimator = new PhotonPoseEstimator(rebuiltField, new Transform3d(new Translation3d(0,0,0), new Rotation3d()));

  public Vision() {
    leftCamera = new PhotonCamera("left");
    rightCamera = new PhotonCamera("right");

  }*/
  public PhotonCamera centerCamera;

  public static final AprilTagFieldLayout rebuiltTagLayout = AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);

  public static final Transform3d cameraPosition = new Transform3d(new Translation3d(0,0,0), new Rotation3d());

  PhotonPoseEstimator photonEstimator = new PhotonPoseEstimator(rebuiltField, cameraPosition);

  public double targetYaw = 0.0;
  public double targetRange = 6.0;
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

  public void periodic() {
    var result = centerCamera.getLatestResult();
    if(result.hasTargets()) {
      for (var target : result.getTargets()) {
        if (target.getFiducialId() == targetID) {
          targetYaw = target.getYaw();
          targetRange = PhotonUtils.calculateDistanceToTargetMeters(Constants.CAMERA_HEIGHT, Constants.HUB_TAG_HEIGHT, Constants.CAMERA_PITCH, Units.degreesToRadians(target.getPitch()));
          targetID = target.getFiducialId();
        }
      }
    } /* else {
      targetYaw = 0.0;
      targetRange = 6.0;
    } */
    SmartDashboard.putNumber("Hub tag distance", targetRange);
    SmartDashboard.putNumber("Hub tag yaw", targetYaw);
  }
}