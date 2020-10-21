package org.firstinspires.ftc.teamcode.autonomous.commands;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

public class DriveForward extends Command {

    double desiredPosition;

    double error;

    public DriveForward(double desiredPosition) {
        this.desiredPosition = desiredPosition;
    }

    @Override
    public void start() {
        drivetrain.resetEncoders();
    }

    @Override
    public void loop() {
        double currentPos = drivetrain.getCurrentPos();
        error = desiredPosition - currentPos;
        double p = 0.1;
        double output = error * p;

        drivetrain.update(output, 0, 0);
    }

    @Override
    public boolean isFinished() {
        return error <= 5;
    }
}
