package org.firstinspires.ftc.teamcode.autonomous.commands;

public class IntakeFeed extends Command {

    double startingTime;
    double elapsedTime;
    double elapsedTimeMilli;
    boolean index;

    public IntakeFeed(boolean index) {
        this.index = index;
    }

    @Override
    public void start() {
        startingTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        if (index) {
            if (robot.odometry.colorSensor().red() > 300 &&
                    robot.odometry.colorSensor().green() > 300) {
                robot.intake.updatePassthrough(false, false);
                robot.intake.updateIntake(true, false);
            } else {
                robot.intake.update(true, false);
            }
        }
        else robot.intake.update(true, false);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
