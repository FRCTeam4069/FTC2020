package org.firstinspires.ftc.teamcode.autonomous.commands;

public class IntakeFeed extends Command {

    double startingTime;
    double elapsedTime;
    double elapsedTimeMilli;

    public IntakeFeed(double elapsedTimeMilli) {
        this.elapsedTimeMilli = elapsedTimeMilli;
    }

    @Override
    public void start() {
        startingTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        elapsedTime = System.currentTimeMillis();
        robot.intake.update(true, false);
    }

    @Override
    public boolean isFinished() {
        return (startingTime + elapsedTimeMilli) < elapsedTime;
    }
}
