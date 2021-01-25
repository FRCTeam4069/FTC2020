package org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain;

import org.firstinspires.ftc.teamcode.autonomous.commands.Command;

public class ResetEncoders extends Command {

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
