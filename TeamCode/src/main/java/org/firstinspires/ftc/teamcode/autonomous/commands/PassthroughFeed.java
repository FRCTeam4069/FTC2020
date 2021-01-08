package org.firstinspires.ftc.teamcode.autonomous.commands;

public class PassthroughFeed extends Command {

    double runTimeMillis;
    boolean forward;
    double startTime;

    public PassthroughFeed(double runTimeMillis, boolean forward) {
        this.runTimeMillis = runTimeMillis;
        this.forward = forward;
    }

    @Override
    public void start() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        robot.intake.updatePassthrough(forward, !forward);
    }

    @Override
    public boolean isFinished() {
        return (startTime + runTimeMillis) > System.currentTimeMillis();
    }
}
