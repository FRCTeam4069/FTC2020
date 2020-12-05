package org.firstinspires.ftc.teamcode.autonomous.commands;

public class DriveToPosition extends Command {

    //Initial values to be read/collected
    double xSetPoint;
    double ySetPoint;
    public double startingTurn;

    //Values needed for PID control
    double yError = 0;
    double xError = 0;

    double yErrorSum = 0;
    double xErrorSum = 0;

    double xLastError = 0;
    double yLastError = 0;

    double turnErrorSum = 0;
    double turnError = 0;

    //Get desired location
    public DriveToPosition(double xSetPoint, double ySetPoint) {
        this.xSetPoint = xSetPoint;
        this.ySetPoint = ySetPoint;
    }

    //Get starting turn, used to keep contanst position
    @Override
    public void start() {
        startingTurn = robot.odometry.getCurrentHeading();
    }

    //Run code
    @Override
    public void loop() {

        //Calculate error, integral of error and derivative of error for forward and strafe
        xError = xSetPoint - robot.odometry.x.getPosition();
        yError = ySetPoint - robot.odometry.getYAvgPos();
        xErrorSum += xError;
        yErrorSum += yError;
        double xChangeError;
        double yChangeError;

        if(xLastError == 0) {
            xChangeError = 0;
        }
        else {
            xChangeError = xError - xLastError;
        }

        if(yLastError == 0) {
            yChangeError = 0;
        }
        else {
            yChangeError = yError - yLastError;
        }
        xLastError = xError;
        yLastError = yError;

        //PID gains
        double xKP = 0.000033;
        double yKP = 0.000026;


        double xKI = 0.0000001;
        double yKI = 0.00000015;

        double xKD = 0.0;
        double yKD = 0.0;

        //Outputs for directional movements
        double yOutput = (yError * yKP) + (yErrorSum * yKI) + (yChangeError * yKD);
        double xOutput = (xError * xKP) + (xErrorSum * xKI) + (xChangeError * xKD);

        //Calculate error and integral of error for turn
        double currentTurn = robot.odometry.getCurrentHeading();

        turnError = startingTurn - currentTurn;
        turnErrorSum += turnError;

        //Turning PID gains
        double turnKP = 0.05;
        double turnKI = 0.000015;

        //Turn output
        double turnOutput = (turnError * turnKP) + (turnErrorSum * turnKI);

        if(Math.abs(turnError) < 180) turnOutput *= -1;

        //Setting outputs to be executed by the drivetrain
        robot.drivetrain.update(yOutput, -xOutput, turnOutput);
    }


    @Override
    public boolean isFinished() {
        //If close enough to accurate return isFinished as true
        if((Math.abs(yError) < 1500) && (Math.abs(xError) < 1500) && (Math.abs(turnError) < 3)) {
            return true;
        }
        else return false;
    }
}
