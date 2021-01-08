package org.firstinspires.ftc.teamcode.autonomous.commands;

public class IntakeFeed extends Command {

    double startingTime;
    double elapsedTime;
    double elapsedTimeMilli;

    @Override
    public void start() {
        startingTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        robot.intake.update(true, false);
    }

    @Override
    public boolean isFinished() {
        if (robot.odometry.sensorValues().get("Red") > 300 &&
                robot.odometry.sensorValues().get("Green") > 300) {
            robot.intake.update(false, false);
            return true;
        }
        else return false;
    }
}
