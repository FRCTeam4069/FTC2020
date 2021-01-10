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
        if(robot.odometry.sensorValues().get("Red") > 300 &&
                robot.odometry.sensorValues().get("Green") > 300) {
            robot.intake.updatePassthrough(false, false);
            robot.intake.updateIntake(true, false);
        }
        else {
            robot.intake.update(true, false);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
