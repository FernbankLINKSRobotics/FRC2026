package frc.robot.subsystems.swervedrive;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;


public final class moduleFeedForwards {
    public SimpleMotorFeedforward frontLeftFeedForwards;
    public SimpleMotorFeedforward frontRightFeedForwards;
    public SimpleMotorFeedforward backLeftFeedForwards;
    public SimpleMotorFeedforward backRightFeedForwards;

    public moduleFeedForwards() {
        frontLeftFeedForwards = new SimpleMotorFeedforward(0.029133, 0.42307, 0.039994);
        frontRightFeedForwards = new SimpleMotorFeedforward(0.161, 2.9057, 0.25805);
        backLeftFeedForwards = new SimpleMotorFeedforward(0.19866, 2.8182, 0.25166);
        backRightFeedForwards = new SimpleMotorFeedforward(0.21627, 2.7812, 0.28464);
    }

    public SimpleMotorFeedforward get(int moduleIndex) {
        return switch(moduleIndex) {
            case 0 -> frontLeftFeedForwards;
            case 1 -> frontLeftFeedForwards;
            case 2 -> frontLeftFeedForwards;
            case 3 -> frontLeftFeedForwards;
            default -> throw new IllegalArgumentException("Please call a module 0-3");
        };
    }
}
