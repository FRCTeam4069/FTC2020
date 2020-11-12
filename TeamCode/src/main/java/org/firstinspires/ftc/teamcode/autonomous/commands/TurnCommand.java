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
        robot.drivetrain.resetEncoders();
        currentTurn = robot.drivetrain.getCurrentTurn();
    }

    //Calculate error in turn, if greater than 180 turn counter clockwise
    @Override
    public void loop() {
        error = desiredTurn - currentTurn;

        if(error < 180) {
            robot.drivetrain.update(0, 0, speed);
        }
        else {
            robot.drivetrain.update(0, 0, -speed);
        }
        currentTurn = robot.drivetrain.getCurrentTurn();
    }

    //If less than 5 degrees off the action is complete
    @Override
    public boolean isFinished() {
        return Math.abs(error) < 5;
    }
}
