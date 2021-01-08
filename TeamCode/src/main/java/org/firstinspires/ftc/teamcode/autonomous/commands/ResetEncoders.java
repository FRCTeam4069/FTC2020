package org.firstinspires.ftc.teamcode.autonomous.commands;

public class ResetEncoders extends Command{

    @Override
    public void start() {
        robot.drivetrain.resetEncoders();
    }

    @Override
    public void loop() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
