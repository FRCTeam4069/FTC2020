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
    public double turnError = 0;

    double turnOutputChange = 0;
    double lastTurnOutput = 0;

    double rampOutput = 0;
    double lastTime = 0;
    boolean rampComplete = false;

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

        if(Math.abs(xSetPoint) > ySetPoint) {
            if (Math.abs(xOutput) > 0.5) {
                double currentTime = System.currentTimeMillis();
                //Ramp Strafing speed
                if (Math.abs(robot.odometry.x.getCurrentVel()) < 27500 && !rampComplete) {
                    if (currentTime - lastTime > 25) {
                        if (xError > 0) rampOutput += 0.025;
                        else rampOutput -= 0.025;
                    }
                    xOutput = rampOutput;
                    telemetry.addData("Ramping", true);
                } else {
                    rampOutput = 0;
                    rampComplete = true;
                }

                lastTime = System.currentTimeMillis();
            }
        }
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

        //Setting outputs to be executed by the drivetrain
        robot.drivetrain.update(yOutput, -xOutput, turnOutput);



        /////TELEMETRY

        telemetry.addData("Starting turn", startingTurn);
        telemetry.addData("Heading", robot.odometry.getCurrentHeading());
        telemetry.addData("XPos", robot.odometry.x.getPosition());
        telemetry.addData("YPos", robot.odometry.yRight.getPosition());
        telemetry.addData("turn output", turnOutput);
        telemetry.addData("x vel", robot.odometry.x.getCurrentVel());
    }


    @Override
    public boolean isFinished() {
        //If close enough to accurate return isFinished as true
        if((Math.abs(yError) < 3000) && (Math.abs(xError) < 3000) && (Math.abs(turnError) < 5)) {
            rampComplete = false;
            return true;
        }
        else return false;
    }
}
