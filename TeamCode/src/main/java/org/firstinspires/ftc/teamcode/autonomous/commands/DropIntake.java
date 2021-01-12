package org.firstinspires.ftc.teamcode.autonomous.commands;

public class DropIntake extends Command {

    double startingY;
    double startingTime;
    boolean isDone = false;
    boolean firstForward = true;
    double forwardTime;

    @Override
    public void start() {
        startingY = robot.odometry.getYAvgPos();
        startingTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        if(System.currentTimeMillis() < startingTime + 100)
            robot.drivetrain.update(-1, 0, 0);
        else {
            if(firstForward) {
                forwardTime = System.currentTimeMillis();
                firstForward = false;
            }
            if(System.currentTimeMillis() < forwardTime + 350)
                robot.drivetrain.update(1, 0, 0);
            else isDone = true;
        }
    }

    @Override
    public boolean isFinished() {
        return isDone;
    }
}
