package org.firstinspires.ftc.teamcode.autonomous.commands;

public class ShooterOff extends Command {

    @Override
    public void start() {
        robot.shooter.update(0);
    }

    @Override
    public void loop() {
        robot.shooter.update(0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
