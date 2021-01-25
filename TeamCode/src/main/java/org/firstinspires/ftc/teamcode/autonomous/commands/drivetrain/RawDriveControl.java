package org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain;

import org.firstinspires.ftc.teamcode.autonomous.commands.Command;

public class RawDriveControl extends Command {

    double speed;
    double timeMillis;
    double startingTime;

    public RawDriveControl(double speed, double timeMillis) {
        this.speed = speed;
        this.timeMillis = timeMillis;
    }

    @Override
    public void start() {
        startingTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        robot.drivetrain.update(speed, 0, 0);
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() > startingTime + timeMillis;
    }
}
