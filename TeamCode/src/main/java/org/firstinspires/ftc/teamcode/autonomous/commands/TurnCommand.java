package org.firstinspires.ftc.teamcode.autonomous.commands;

public class TurnCommand extends Command {

    //IN DEGREES
    double desiredTurn;
    double currentTurn = drivetrain.getCurrentTurn();

    double speed;
    double error;

    //Takes desired turn IN DEGREES
    public TurnCommand(double desiredTurn, double speed) {
        this.desiredTurn = desiredTurn;
        this.speed = speed;
    }

    @Override
    public void start() {
        drivetrain.resetEncoders();
    }

    @Override
    public void loop() {
        error = desiredTurn - currentTurn;

        if(error > 0) {
            drivetrain.update(0, 0, speed);
        }
        else {
            drivetrain.update(0, 0, -speed);
        }
    }

    @Override
    public boolean isFinished() {
        return Math.abs(error) < 5;
    }
}
