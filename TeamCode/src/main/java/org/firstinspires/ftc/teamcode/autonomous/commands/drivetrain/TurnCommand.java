package org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain;

import org.firstinspires.ftc.teamcode.autonomous.commands.Command;

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
        currentTurn = robot.odometry.getCurrentHeading();
        error = desiredTurn - currentTurn;
        if(error > 180) error = -error + 360;
        else error = 0 - error;
        errorSum += error;
        double kP = 0.012;
        double kI = 0.0;
        double output = error * kP + errorSum * kI;

        if(Math.abs(error) < 9) {
            if(error < 0) robot.drivetrain.update(0, 0, -0.2);
            else robot.drivetrain.update(0, 0, 0.2);
        }
        else {
            if (Math.abs(error) < 180) {
                robot.drivetrain.update(0, 0, output);
            } else {
                robot.drivetrain.update(0, 0, -output);
            }
        }
        telemetry.addData("Turn error", error);
    }

    //If less than 2.5 degrees off the action is complete
    @Override
    public boolean isFinished() {
        return Math.abs(error) < 2.5;
    }
}
