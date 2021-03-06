package org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain;

import org.firstinspires.ftc.teamcode.autonomous.commands.Command;

public class DriveForward extends Command {

    //Values needed to PID accurate driving positions
    double desiredPosition;
    double error;
    boolean started = false;
    double currentPos;

    double errorSum = 0;

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
        currentPos = robot.odometry.getYAvgPos();
        error = desiredPosition - currentPos;
        errorSum += error;
        double lastError = 0;
        double changeInError;
        if(!started) {
            changeInError = 0;
            started = true;
        }
        else {
            changeInError = error - lastError;
        }
        lastError = error;

        double kP = 0.00003;
        double kI = 0.00000607;
        double kD = 0.00000078;
        double output = error * kP + errorSum * kI + changeInError * kD;
        if(output > 0.8) output = 0.8;
        else if(output < -0.8) output = -0.8;
        robot.drivetrain.update(output, 0, 0);
    }

    //Command is complete if robot is within 5 ticks or less of accurate position
    @Override
    public boolean isFinished() {
        if(currentPos < desiredPosition + 1500 && currentPos > desiredPosition - 1500) {
            return true;
        }
        else return false;
    }
}
