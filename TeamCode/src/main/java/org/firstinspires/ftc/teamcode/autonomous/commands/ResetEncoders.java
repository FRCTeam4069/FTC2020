package org.firstinspires.ftc.teamcode.autonomous.commands;

public class ResetEncoders extends Command{

    @Override
    public void start() {
        robot.drivetrain.resetEncoders();
        robot.odometry.x.reset();
        robot.odometry.yRight.reset();
    }

    @Override
    public void loop() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
