package org.firstinspires.ftc.teamcode.autonomous.commands.intake;

import org.firstinspires.ftc.teamcode.autonomous.commands.Command;

public class IntakeFeed extends Command {

    double speed;

    public IntakeFeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        robot.intake.update(true, false, speed);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
