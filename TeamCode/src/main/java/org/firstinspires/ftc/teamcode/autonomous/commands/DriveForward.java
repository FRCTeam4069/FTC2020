package org.firstinspires.ftc.teamcode.autonomous.commands;

public class DriveForward extends Command {

    //Values needed to PID accurate driving positions
    double desiredPosition;
    double error;

    //Initializer ensures command has a desired position
    public DriveForward(double desiredPosition) {
        this.desiredPosition = desiredPosition;
    }

    //Reset encoders for consistency
    @Override
    public void start() {
        robot.drivetrain.resetEncoders();
    }

    //PID output to stop accurately at desired position
    @Override
    public void loop() {
        double currentPos = robot.drivetrain.getCurrentPos();
        error = desiredPosition - currentPos;
        double p = 0.09;
        double output = error * p;

        robot.drivetrain.update(output, 0, 0);
    }

    //Command is complete if robot is within 5 ticks or less of accurate position
    @Override
    public boolean isFinished() {
        if(desiredPosition < 0) {
            return error >= -5;
        }
        return error <= 5;
    }
}
