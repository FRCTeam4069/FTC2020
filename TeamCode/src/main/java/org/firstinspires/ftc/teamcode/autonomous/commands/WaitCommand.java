package org.firstinspires.ftc.teamcode.autonomous.commands;

public class WaitCommand extends Command {

    double lengthMilli;
    double startTime;

    public WaitCommand(double lengthMilli) {
        this.lengthMilli = lengthMilli;
    }

    @Override
    public void start() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        telemetry.addData("Wait", true);
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() > startTime + lengthMilli;
    }
}
