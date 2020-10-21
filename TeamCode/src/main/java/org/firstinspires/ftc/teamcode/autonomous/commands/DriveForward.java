package org.firstinspires.ftc.teamcode.autonomous.commands;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

public class DriveForward extends Command {

    double desiredPosition;

    public DriveForward(double desiredPosition) {
        this.desiredPosition = desiredPosition;
    }
    
    @Override
    public void start() {
        drivetrain.resetEncoders();
    }

    @Override
    public void loop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
