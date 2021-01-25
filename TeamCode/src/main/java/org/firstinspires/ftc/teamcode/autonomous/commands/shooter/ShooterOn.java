package org.firstinspires.ftc.teamcode.autonomous.commands.shooter;

import org.firstinspires.ftc.teamcode.autonomous.commands.Command;

public class ShooterOn extends Command {

    double rpm;

    public ShooterOn(double rpm) {
        this.rpm = rpm;
    }

    @Override
    public void start() {}

    @Override
    public void loop() {
        robot.shooter.update(rpm);
    }

    @Override
    public boolean isFinished() {
        return (Math.abs(rpm - robot.shooter.speed)) < 200;
    }
}
