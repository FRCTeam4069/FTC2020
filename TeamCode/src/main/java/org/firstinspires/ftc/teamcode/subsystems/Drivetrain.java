package org.firstinspires.ftc.teamcode.subsystems;

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
import org.firstinspires.ftc.teamcode.subsystems.robot.RobotHardware;

public class Drivetrain extends RobotHardware {

    Telemetry telemetry;

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

    double flSum;
    double frSum;
    double blSum;
    double brSum;

    double flError;
    double frError;
    double blError;
    double brError;

    private double currentAvgPos;
    double direction;

    double totalTimeElapsed = 0;

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

        if((state == DriveState.NOT_DRIVING || state == DriveState.HOLONOMIC) &&
                (forward > 0.05 || strafe > 0.05 || turn > 0.05)) {

            state = DriveState.HOLONOMIC;

            this.turn = turn;

            if (navx.isCalibrating()) {
                telemetry.log().add("NavX Calibrating");
                telemetry.update();
            }

            //Calculate direction and speed
            double direction = Math.atan2(forward, strafe);
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
            currentTurn = navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
            if (currentTurn < 0) {
                currentTurn = 180 + (180 - Math.abs(currentTurn));
            }
            if (lastTurn == 0) {
                turnChange = 0;
            } else {
                turnChange = currentTurn - lastTurn;
            }
            lastTurn = navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
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

            //Calculate actual velocity of each motor
            double flActualVelocity = flPosChange / elapsedTime;
            double blActualVelocity = blPosChange / elapsedTime;
            double frActualVelocity = frPosChange / elapsedTime;
            double brActualVelocity = brPosChange / elapsedTime;

            //Calculate desired motor outputs
            desiredFrontLeftSpeed = (Math.sin(direction + Math.PI / 4) * desiredSpeed) + turnOutput;
            desiredFrontRightSpeed = (Math.sin(direction - Math.PI / 4) * desiredSpeed) - turnOutput;
            desiredBackRightSpeed = (Math.sin(direction + Math.PI / 4) * desiredSpeed) - turnOutput;
            desiredBackLeftSpeed = (Math.sin(direction - Math.PI / 4) * desiredSpeed) + turnOutput;

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

            double kP = 0.1;
            double kI = 0.01;

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
        else state = DriveState.NOT_DRIVING;
    }

    public enum Direction {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
    }

    public void directDrive(Direction desiredDirection) {

        if((state == DriveState.DIRECT_DRIVE || state == DriveState.NOT_DRIVING) && direction != 0) {

            state = DriveState.DIRECT_DRIVE;

            direction = 0;
            if (desiredDirection == Direction.FORWARD) direction = Math.atan2(1, 0);
            else if (desiredDirection == Direction.BACKWARD) direction = Math.atan2(-1, 0);
            else if (desiredDirection == Direction.LEFT) direction = Math.atan2(0, -1);
            else if (desiredDirection == Direction.RIGHT) direction = Math.atan2(0, 1);
            else direction = 0;

            //Calculate change in time
            double currentTime = System.currentTimeMillis();
            double elapsedTime = 0;
            if (lastTime == 0) {
                elapsedTime = 0;
            } else {
                elapsedTime = currentTime - lastTime;
            }
            double totalTimeElapsed = 0;
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

            double flVel = flPosChange / elapsedTime;
            double frVel = frPosChange / elapsedTime;
            double blVel = blPosChange / elapsedTime;
            double brVel = brPosChange / elapsedTime;

            desiredFrontLeftSpeed = Math.sin(direction + Math.PI / 4);
            desiredFrontRightSpeed = Math.sin(direction - Math.PI / 4);
            desiredBackRightSpeed = Math.sin(direction + Math.PI / 4);
            desiredBackLeftSpeed = Math.sin(direction - Math.PI / 4);

            flError = desiredFrontLeftSpeed - flVel;
            frError = desiredFrontRightSpeed - frVel;
            blError = desiredBackLeftSpeed - blVel;
            brError = desiredBackRightSpeed - brVel;

            flSum += flError;
            frSum += frError;
            blSum += blError;
            brSum += brError;

            double kP = 0.1;
            double kI = 0.01;

            frontLeftOutput = (flError * kP) + (flSum * kI);
            frontRightOutput = (frError * kP) + (frSum * kI);
            backLeftOutput = (blError * kP) + (blSum * kI);
            backRightOutput = (brError * kP) + (brSum * kI);

            frontLeft.setPower(frontLeftOutput);
            frontRight.setPower(frontRightOutput);
            backLeft.setPower(backLeftOutput);
            backRight.setPower(backRightOutput);
        }
        else state = DriveState.NOT_DRIVING;
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
        telemetry.addData("Desired Speed", desiredSpeed);
        telemetry.addData("Desired Front Left Speed", desiredFrontLeftSpeed);
        telemetry.addData("Desired Front Right Speed", desiredFrontRightSpeed);
        telemetry.addData("Desired Back Left Speed", desiredBackLeftSpeed);
        telemetry.addData("Desired Back Right Speed", desiredBackRightSpeed);

        telemetry.addData("Front Left Output", frontLeftOutput);
        telemetry.addData("Front Right Output", frontRightOutput);
        telemetry.addData("Back Left Output", backLeftOutput);
        telemetry.addData("Back Right Output", backRightOutput);

        telemetry.addData("Turn Error", turnError);
        telemetry.addData("Turn Output", turnOutput);
        telemetry.addData("Current Turn", currentTurn);
        if(update) {
            telemetry.update();
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
