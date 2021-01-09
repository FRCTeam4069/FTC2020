package org.firstinspires.ftc.teamcode.autonomous.commands;

public class TurnCommand extends Command {

    //IN DEGREES
    double desiredTurn;
    double currentTurn;

    double error;

    double errorSum = 0;

    //Takes desired turn IN DEGREES
    public TurnCommand(double desiredTurn) {
        this.desiredTurn = desiredTurn;
    }

    //Get current drivetrain position
    @Override
    public void start() {
        robot.drivetrain.resetEncoders();
        currentTurn = robot.odometry.getCurrentHeading();
    }

    //Calculate error in turn, if greater than 180 turn counter clockwise
    @Override
    public void loop() {
        error = desiredTurn - currentTurn;
        errorSum += error;
        double kP = 0.01205;
        double kI = 0.000025;
        double output = error * kP + errorSum * kI;

        if(Math.abs(error) < 180) {
            robot.drivetrain.update(0, 0, -output);
        }
        else {
            robot.drivetrain.update(0, 0, output);
        }
        currentTurn = robot.odometry.getCurrentHeading();
    }

    //If less than 5 degrees off the action is complete
    @Override
    public boolean isFinished() {
        return Math.abs(error) < 3;
    }
}
