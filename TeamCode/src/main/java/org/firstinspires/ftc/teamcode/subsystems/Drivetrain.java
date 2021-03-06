package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.subsystems.odometry.Odometry;
import org.firstinspires.ftc.teamcode.subsystems.robot.RobotHardware;

import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;

public class Drivetrain extends RobotHardware {

    Telemetry telemetry;
    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry DTelemetry = dashboard.getTelemetry();

    //Just in case
    public final double TICKS_PER_REV = 432;
    public final double WHEEL_CIRCUMFERENCE = 2 * Math.PI * 0.035; //METERS

    //Values for calculating changes over time
    double lastTime = 0;

    double flLastPos = 0;
    double blLastPos = 0;
    double frLastPos = 0;
    double brLastPos = 0;

    double lastTurn = 0;

    boolean turnIsZero;

    //Values to be read in telemetry
    double turnError;
    double turnOutput;
    double currentTurn;
    double turnChange;
    double turnErrorSum;
    double turn;

    double desiredSpeed;
    double desiredFrontLeftSpeed;
    double desiredFrontRightSpeed;
    double desiredBackRightSpeed;
    double desiredBackLeftSpeed;

    double frontLeftOutput;
    double frontRightOutput;
    double backLeftOutput;
    double backRightOutput;

    double flActualVelocity;
    double frActualVelocity;
    double blActualVelocity;
    double brActualVelocity;

    double flSum;
    double frSum;
    double blSum;
    double brSum;

    double flError;
    double frError;
    double blError;
    double brError;

    private double currentAvgPos;
    double totalTimeElapsed = 0;

    boolean directDriveStarted = false;
    double startingAngle;
    double lastTurnOutput = 0;

    double turnToAngleSum = 0;
    double desiredAngle;

    boolean turnZero = false;
    double turnRef = 0;
    boolean turnApplied = false;

    //Save drivetrain state
    Drivetrain.DriveState state = DriveState.NOT_DRIVING;

    //Setup such as initializing motors, setting directions and run modes
    public Drivetrain(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap);

        this.telemetry = telemetry;

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        startingAngle = navx.getAngularOrientation(AxesReference.EXTRINSIC, XYZ, AngleUnit.DEGREES).thirdAngle;
    }

    //Reset encoders (for use mainly in auto)
    public void resetEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //Update motors after calculating proper outputs
    public void update(double forward, double strafe, double turn) {

        if(forward == 0 && strafe == 0 && turn == 0) {
            state = DriveState.NOT_DRIVING;
        }

            if (turn != 0) turnApplied = true;
            this.turn = turn;

            if (navx.isCalibrating()) {
                telemetry.addLine("NavX Calibrating");
                telemetry.update();
            }

            //Calculate direction and speed
            double direction = Math.atan2(forward, strafe);
            if(direction < 0) direction = (Math.PI * 2) + direction;
            desiredSpeed = Math.hypot(forward, strafe);

            //Calculate change in time
            double currentTime = System.currentTimeMillis();
            double elapsedTime = 0;
            if (lastTime == 0) {
                elapsedTime = 0;
            } else {
                elapsedTime = currentTime - lastTime;
            }

            totalTimeElapsed += elapsedTime;
            lastTime = System.currentTimeMillis();


            //Calculate change in position for each motor
            double flCurrentPos = frontLeft.getCurrentPosition();
            double blCurrentPos = backLeft.getCurrentPosition();
            double frCurrentPos = frontRight.getCurrentPosition();
            double brCurrentPos = backRight.getCurrentPosition();
            double flPosChange;
            double frPosChange;
            double blPosChange;
            double brPosChange;

            if (flLastPos == 0) {
                flPosChange = flCurrentPos;
                blPosChange = blCurrentPos;
                frPosChange = frCurrentPos;
                brPosChange = brCurrentPos;
            } else {
                flPosChange = flCurrentPos - flLastPos;
                blPosChange = blCurrentPos - blLastPos;
                brPosChange = brCurrentPos - brLastPos;
                frPosChange = frCurrentPos - frLastPos;
            }

            flLastPos = frontLeft.getCurrentPosition();
            blLastPos = backLeft.getCurrentPosition();
            frLastPos = frontRight.getCurrentPosition();
            brLastPos = backRight.getCurrentPosition();

            //calculate change in direction (turn)
            if(!navx.isCalibrating()) currentTurn = navx.getAngularOrientation(AxesReference.EXTRINSIC,
                    XYZ, AngleUnit.DEGREES).thirdAngle;
            else {
                currentTurn = imu.getAngularOrientation(AxesReference.EXTRINSIC, XYZ,
                        AngleUnit.DEGREES).thirdAngle;
            }
            if (currentTurn < 0) {
                currentTurn = 180 + (180 - Math.abs(currentTurn));
            }
            if (lastTurn == 0) {
                turnChange = 0;
            } else {
                turnChange = currentTurn - lastTurn;
            }
            if(!navx.isCalibrating()) lastTurn = navx.getAngularOrientation(AxesReference.EXTRINSIC,
                    XYZ, AngleUnit.DEGREES).thirdAngle;
            else {
                lastTurn = imu.getAngularOrientation(AxesReference.EXTRINSIC, XYZ,
                        AngleUnit.DEGREES).thirdAngle;
            }
            if (lastTurn < 0) {
                lastTurn = 180 + (180 - Math.abs(lastTurn));
            }

            if (!(turnChange > 180)) {
                //PID turn
                turnErrorSum = 0;
                turnError = ((turnChange) - (turn));
                turnErrorSum += turnError;

                double turnP = 0.0015;
                double turnI = 0.0;
                double turnD = 0.00001;

                turnOutput = turn + (turnError * turnP) + (turnErrorSum * turnI) + (turnChange * turnD);
            } else turnOutput = turn;

            //Maintain heading if no turn applied
//            if(Math.abs(turn) < 0.015 && turnApplied) {
//
//                if(!turnZero) {
//                    turnZero = true;
//                    turnRef = imu.getAngularOrientation(AxesReference.EXTRINSIC, XYZ,
//                            AngleUnit.DEGREES).thirdAngle;
//                    if (turnRef < 0) {
//                        turnRef = 180 + (180 - Math.abs(turnRef));
//                    }
//                }
//                turnError = turnRef - currentTurn;
//                if(turnError > 180) turnError = -turnError + 360;
//                else turnError = 0 - turnError;
//                double turnP = 0.0042;
//
//                turnOutput = turnError * turnP;
//                if(Math.abs(turnError) < 9 && Math.abs(turnError) > 2.5) {
//                    if(turnError < 0) turnOutput = 0.2;
//                    else turnOutput = -0.2;
//                }
//                else {
//                    if (Math.abs(turnError) > 180) {
//                        turnOutput *= -1;
//                    }
//                }
//
//                DTelemetry.addData("Turn Ref", turnRef);
//                DTelemetry.addData("Current Turn", currentTurn);
//                DTelemetry.addData("Turn Error", turnError);
//                DTelemetry.addData("Turn Output", turnOutput);
//                DTelemetry.update();
//            }
//            else turnZero = false;

            //Calculate actual velocity of each motor
            flActualVelocity = flPosChange / elapsedTime;
            blActualVelocity = blPosChange / elapsedTime;
            frActualVelocity = frPosChange / elapsedTime;
            brActualVelocity = brPosChange / elapsedTime;

            //Calculate desired motor outputs
            desiredFrontLeftSpeed = (Math.sin(direction + Math.PI / 4) * desiredSpeed)
                    * Math.sqrt(2) + turnOutput;
            desiredFrontRightSpeed = (Math.sin(direction - Math.PI / 4) * desiredSpeed)
                    * Math.sqrt(2) - turnOutput;
            desiredBackRightSpeed = (Math.sin(direction + Math.PI / 4) * desiredSpeed)
                    * Math.sqrt(2)- turnOutput;
            desiredBackLeftSpeed = (Math.sin(direction - Math.PI / 4) * desiredSpeed)
                    * Math.sqrt(2) + turnOutput;

            //PID Outputs
            flSum = 0;
            frSum = 0;
            blSum = 0;
            brSum = 0;

            flError = desiredFrontLeftSpeed - flActualVelocity;
            frError = desiredFrontRightSpeed - frActualVelocity;
            blError = desiredBackLeftSpeed - blActualVelocity;
            brError = desiredBackRightSpeed - brActualVelocity;

            flSum += flError;
            frSum += frError;
            blSum += blError;
            brSum += brError;

            double kP = 0.0001;
            double kI = 0.001;

            frontLeftOutput = desiredFrontLeftSpeed + (flError * kP) + (kI * flSum);
            frontRightOutput = desiredFrontRightSpeed + (frError * kP) + (kI * frSum);
            backLeftOutput = desiredBackLeftSpeed + (blError * kP) + (kI * blSum);
            backRightOutput = desiredBackRightSpeed + (brError * kP) + (kI * brSum);

            //Check for speed scaling factor to counteract greater than 1 outputs
            double max1 = Math.max((Math.abs(frontLeftOutput)), (Math.abs(frontRightOutput)));
            double max2 = Math.max((Math.abs(backLeftOutput)), (Math.abs(backRightOutput)));
            double scalar = Math.max(max1, max2);

            if (scalar > 1) {
                frontLeftOutput /= scalar;
                frontRightOutput /= scalar;
                backLeftOutput /= scalar;
                backRightOutput /= scalar;
            }

            //Calculate average position for driveForwards and backwards commands
            currentAvgPos = (((double) frontLeft.getCurrentPosition()) + ((double) frontRight.getCurrentPosition())
                    + ((double) backLeft.getCurrentPosition()) + ((double) backRight.getCurrentPosition())) / 4;

            //Set motor speeds
            frontLeft.setPower(frontLeftOutput);
            frontRight.setPower(frontRightOutput);
            backLeft.setPower(backLeftOutput);
            backRight.setPower(backRightOutput);

    }

    public enum Direction {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        NO_DIRECTION
    }

    public double[] directDrive(Direction desiredDirection) {

        double forward = 0;
        double strafe = 0;
        double startingAngleDD = 0;

        if((state == DriveState.DIRECT_DRIVE || state == DriveState.NOT_DRIVING)
                && desiredDirection != Direction.NO_DIRECTION) {

            if(!directDriveStarted) {
                if(!navx.isCalibrating()) startingAngleDD = navx.getAngularOrientation(AxesReference.EXTRINSIC,
                        XYZ, AngleUnit.DEGREES).thirdAngle;
                else startingAngleDD = imu.getAngularOrientation(AxesReference.EXTRINSIC, XYZ,
                        AngleUnit.DEGREES).thirdAngle;
                directDriveStarted = true;
            }

            state = DriveState.DIRECT_DRIVE;

            if(desiredDirection == Direction.FORWARD) {
                forward = 1;
                strafe = 0;
            }
            else if(desiredDirection == Direction.BACKWARD) {
                forward = -1;
                strafe = 0;
            }
            else if(desiredDirection == Direction.LEFT) {
                forward = 0;
                strafe = -1;
            }
            else if(desiredDirection == Direction.RIGHT) {
                forward = 0;
                strafe = 1;
            }

            //Calculate error and integral of error for turn
            double currentTurn;
            if(!navx.isCalibrating()) currentTurn = navx.getAngularOrientation(AxesReference.EXTRINSIC,
                    XYZ, AngleUnit.DEGREES).thirdAngle;
            else currentTurn = imu.getAngularOrientation(AxesReference.EXTRINSIC, XYZ,
                    AngleUnit.DEGREES).thirdAngle;

            if(currentTurn > 180 + startingAngleDD) turnError = -currentTurn + (360 + startingAngleDD);
            else turnError = (0 + startingAngleDD) - currentTurn;
            turnErrorSum += turnError;

            //Turning PID gains
            double turnKP = 0.012;
            double turnKI = 0.0003;

            //Turn output
            double turnOutput = (turnError * turnKP) + (turnErrorSum * turnKI);

            double turnOutputChange = turnOutput - lastTurnOutput;
            if(Math.abs(turnOutputChange) > 0.3) turnOutput = 0;

            if(Math.abs(turnError) < 180) turnOutput *= -1;

            lastTurnOutput = turnOutput;

            return new double[]{forward, strafe, turnOutput};
        }

        else {
            state = DriveState.NOT_DRIVING;
            directDriveStarted = false;
            return new double[]{0, 0, 0};
        }
    }

    public double turnToAngle(double desiredAngle) {

        this.desiredAngle = desiredAngle;
        if(!navx.isCalibrating()) currentTurn = navx.getAngularOrientation(AxesReference.EXTRINSIC,
                AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
        else {
            currentTurn = imu.getAngularOrientation(AxesReference.EXTRINSIC, XYZ,
                    AngleUnit.DEGREES).thirdAngle;
        }
        if(currentTurn < 0) {
            currentTurn = 180 + (180 + currentTurn);
        }
        double error = desiredAngle - currentTurn;
        turnToAngleSum += error;
        double kP = 0.019;
        double kI = 0.000012;
        double output = error * kP + turnToAngleSum * kI;

        if(Math.abs(error) < 180) {
            return -output;
        }
        else {
            return output;
        }
    }

    public boolean atCorrectAngle() {
        double currentAngle;
        if(!navx.isCalibrating()) currentAngle = navx.getAngularOrientation(AxesReference.EXTRINSIC,
                AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
        else currentAngle = imu.getAngularOrientation(AxesReference.EXTRINSIC, XYZ,
                AngleUnit.DEGREES).thirdAngle;
        if(currentAngle < 0) {
            currentAngle = 180 + (180 + currentAngle);
        }
        if(Double.isNaN(desiredAngle)) desiredAngle = 0;
        return(Math.abs(desiredAngle - currentAngle)) < 5;
    }

    public enum DriveState {
        DIRECT_DRIVE,
        HOLONOMIC,
        NOT_DRIVING
    }

    @Override
    public void disable() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    //GETTERS AND TELEMETRY/////////////////////////////////////////////////////////////////////////

    public double getCurrentPos() {
        return currentAvgPos;
    }
    public double getCurrentTurn() {
        return currentTurn;
    }

    //To add or add and update important drivetrain vals to telemetry
    public void addBaseTelemetry(boolean update) {
        telemetry.addData("Turn Ref", turnRef);

        telemetry.addData("Desired Speed", desiredSpeed);
        telemetry.addData("Desired Front Left Speed", desiredFrontLeftSpeed);
        telemetry.addData("Desired Front Right Speed", desiredFrontRightSpeed);
        telemetry.addData("Desired Back Left Speed", desiredBackLeftSpeed);
        telemetry.addData("Desired Back Right Speed", desiredBackRightSpeed);

        telemetry.addData("Front Left Speed", flActualVelocity);
        telemetry.addData("Front Right Speed", frActualVelocity);
        telemetry.addData("Back Left Speed", blActualVelocity);
        telemetry.addData("Back Right Speed", brActualVelocity);

        telemetry.addData("Front Left Output", frontLeft);
        telemetry.addData("Front Right Output", frontRightOutput);
        telemetry.addData("Back Left Output", backLeftOutput);
        telemetry.addData("Back Right Output", backRightOutput);

        telemetry.addData("Turn Error", turnError);
        telemetry.addData("Turn Output", turnOutput);
        telemetry.addData("Current Turn", currentTurn);
        if(update) {
            telemetry.update();
        }
        DTelemetry.addData("Desired Speed", desiredSpeed);
        DTelemetry.addData("Desired Front Left Speed", desiredFrontLeftSpeed);
        DTelemetry.addData("Desired Front Right Speed", desiredFrontRightSpeed);
        DTelemetry.addData("Desired Back Left Speed", desiredBackLeftSpeed);
        DTelemetry.addData("Desired Back Right Speed", desiredBackRightSpeed);

        DTelemetry.addData("Front Left Speed", flActualVelocity);
        DTelemetry.addData("Front Right Speed", frActualVelocity);
        DTelemetry.addData("Back Left Speed", blActualVelocity);
        DTelemetry.addData("Back Right Speed", brActualVelocity);

        DTelemetry.addData("Front Left Output", frontLeft);
        DTelemetry.addData("Front Right Output", frontRightOutput);
        DTelemetry.addData("Back Left Output", backLeftOutput);
        DTelemetry.addData("Back Right Output", backRightOutput);

        DTelemetry.addData("Turn Error", turnError);
        DTelemetry.addData("Turn Output", turnOutput);
        DTelemetry.addData("Current Turn", currentTurn);
        if(update) {
            DTelemetry.update();
        }

    }

    public void addTurnTelemetry(boolean update) {
        telemetry.addData("Desired turn", turn);
        telemetry.addData("Current turn", currentTurn);
        telemetry.addData("Turn error", turnError);
        telemetry.addData("Turn change", turnChange);
        telemetry.addData("Turn integral", turnErrorSum);
        telemetry.addData("Turn output", turnOutput);
        if(update) {
            telemetry.update();
        }
    }

    public void pidTelemetry(boolean update) {
        telemetry.addData("Desired Speed", desiredSpeed);
        telemetry.addData("Desired Front Left Speed", desiredFrontLeftSpeed);
        telemetry.addData("Desired Front Right Speed", desiredFrontRightSpeed);
        telemetry.addData("Desired Back Left Speed", desiredBackLeftSpeed);
        telemetry.addData("Desired Back Right Speed", desiredBackRightSpeed);

        telemetry.addData("Front Left Output", frontLeftOutput);
        telemetry.addData("Front Right Output", frontRightOutput);
        telemetry.addData("Back Left Output", backLeftOutput);
        telemetry.addData("Back Right Output", backRightOutput);

        telemetry.addData("Front Left Error", flError);
        telemetry.addData("Front Right Error", frError);
        telemetry.addData("Back Left Error", blError);
        telemetry.addData("Back Right Error", brError);

        telemetry.addData("Front Left Sum", flSum);
        telemetry.addData("Back Left Sum", blSum);
        telemetry.addData("Front Right Sum", frSum);
        telemetry.addData("Back Right Sum", brSum);
        if(update) {
            telemetry.update();
        }
    }

    public double getAvgVelocity() {
        return (frontLeft.getVelocity() + backLeft.getVelocity() +  backRight.getVelocity()
                + frontRight.getVelocity()) / 4;
    }

    public void displayPIDCoeffs(boolean update) {
        telemetry.addData("Pid Coefficients w/ encoder",
                frontLeft.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));

        PIDFCoefficients pidCoeffs = frontLeft.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
        MotorControlAlgorithm algorithm = pidCoeffs.algorithm;
        telemetry.addData("Algorithm", algorithm);

        if(update) telemetry.update();
    }

}
