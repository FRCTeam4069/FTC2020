package org.firstinspires.ftc.teamcode.autonomous.commands;

public class TurnCommand extends Command {

    //IN DEGREES
    double desiredTurn;
    double currentTurn;

    double error;

    //Takes desired turn IN DEGREES
    public TurnCommand(double desiredTurn) {
        this.desiredTurn = desiredTurn;
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
        double errorSum = 0;
        error = desiredTurn - currentTurn;
        errorSum += error;
        double kP = 0.1;
        double kI = 0.0;
        double output = error * kP + errorSum * kI;

        if(error < 180) {
            robot.drivetrain.update(0, 0, output);
        }
        else {
            robot.drivetrain.update(0, 0, -output);
        }
        currentTurn = robot.drivetrain.getCurrentTurn();
    }

    //If less than 5 degrees off the action is complete
    @Override
    public boolean isFinished() {
        return Math.abs(error) < 2;
    }
}
