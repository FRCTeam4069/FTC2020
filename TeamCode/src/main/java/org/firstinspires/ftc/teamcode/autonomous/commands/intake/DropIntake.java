package org.firstinspires.ftc.teamcode.autonomous.commands.intake;

import org.firstinspires.ftc.teamcode.autonomous.commands.Command;

public class DropIntake extends Command {

    double startingY;
    double startingTime;
    boolean isDone = false;
    boolean backDone = false;
    double startingTurn;
    double turnError = 0;
    double turnErrorSum = 0;
    double lastTurnOutput = 0;

    @Override
    public void start() {
        startingY = robot.odometry.getYAvgPos();
        startingTime = System.currentTimeMillis();
        startingTurn = robot.odometry.getCurrentHeading();
    }

    @Override
    public void loop() {
        //Calculate error and integral of error for turn
        double currentTurn = robot.odometry.getCurrentHeading();

        if(currentTurn > 180 + startingTurn) turnError = -currentTurn + (360 + startingTurn);
        else turnError = (0 + startingTurn) - currentTurn;
        turnErrorSum += turnError;

        //Turning PID gains
        double turnKP = 0;
        double turnKI = 0;

        if(Math.abs(turnError) < 5) {
            turnKP = 0.0205;
            turnKI = 0.001;
        }
        else {
            turnKP = 0.012;
            turnKI = 0.0;
        }

        //Turn output
        double turnOutput = (turnError * turnKP) + (turnErrorSum * turnKI);

        if(Math.abs(turnError) < 180) turnOutput *= -1;

        lastTurnOutput = turnOutput;
        if(!backDone) {
            if (robot.odometry.getYAvgPos() > startingY - 1500)
                robot.drivetrain.update(-1, 0, turnOutput);
            else {
                backDone = true;
            }
        }
        else {
            if(robot.odometry.getYAvgPos() < startingY)
                robot.drivetrain.update(1, 0,turnOutput);
            else isDone = true;
        }
    }

    @Override
    public boolean isFinished() {
        return isDone;
    }
}
