package org.firstinspires.ftc.teamcode.autonomous.commands;

public class ShooterOn extends Command{

    double desiredSpeed;

    public ShooterOn(double desiredSpeed) {
        this.desiredSpeed = desiredSpeed;
    }

    @Override
    public void start() {}

    @Override
    public void loop() {
        robot.shooter.update(desiredSpeed);
    }

    @Override
    public boolean isFinished() {
        return (Math.abs(desiredSpeed - robot.shooter.speed)) < 0.2;
    }
}
