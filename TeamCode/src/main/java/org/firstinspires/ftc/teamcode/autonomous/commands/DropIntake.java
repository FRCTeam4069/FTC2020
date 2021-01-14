package org.firstinspires.ftc.teamcode.autonomous.commands;

public class DropIntake extends Command {

    double startingY;
    double startingTime;
    boolean isDone = false;
    boolean backDone = false;

    @Override
    public void start() {
        startingY = robot.odometry.getYAvgPos();
        startingTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        if(!backDone) {
            if (robot.odometry.getYAvgPos() > startingY - 2500)
                robot.drivetrain.update(-1, 0, 0);
            else {
                backDone = true;
            }
        }
        else {
            if(robot.odometry.getYAvgPos() < startingY)
                robot.drivetrain.update(1, 0,0);
            else isDone = true;
        }
    }

    @Override
    public boolean isFinished() {
        return isDone;
    }
}
