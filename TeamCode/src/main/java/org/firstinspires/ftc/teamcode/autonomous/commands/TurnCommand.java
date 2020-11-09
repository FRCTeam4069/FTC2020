package org.firstinspires.ftc.teamcode.autonomous.commands;

public class TurnCommand extends Command {

    //IN DEGREES
    double desiredTurn;
    double currentTurn;

    double speed;
    double error;

    //Takes desired turn IN DEGREES
    public TurnCommand(double desiredTurn, double speed) {
        this.desiredTurn = desiredTurn;
        this.speed = speed;
    }

    //Get current drivetrain position
    @Override
    public void start() {
        drivetrain.resetEncoders();
        currentTurn = drivetrain.getCurrentTurn();
    }

    //Calculate error in turn, if greater than 180 turn counter clockwise
    @Override
    public void loop() {
        error = desiredTurn - currentTurn;

        if(error < 180) {
            drivetrain.update(0, 0, speed);
        }
        else {
            drivetrain.update(0, 0, -speed);
        }
        currentTurn = drivetrain.getCurrentTurn();
    }

    //If less than 5 degrees off the action is complete
    @Override
    public boolean isFinished() {
        return Math.abs(error) < 5;
    }
}
