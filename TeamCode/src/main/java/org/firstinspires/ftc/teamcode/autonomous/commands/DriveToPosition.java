package org.firstinspires.ftc.teamcode.autonomous.commands;

public class DriveToPosition extends Command {

    double xSetPoint;
    double ySetPoint;
    double startingTurn;

    public DriveToPosition(double xSetPoint, double ySetPoint) {
        this.xSetPoint = xSetPoint;
        this.ySetPoint = ySetPoint;
    }

    @Override
    public void start() {
        startingTurn = robot.odometry.getCurrentHeading();
    }

    @Override
    public void loop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
