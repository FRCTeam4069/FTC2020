package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class Drivetrain {

    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private  DcMotor backRight;

    private NavxMicroNavigationSensor navx;

    Telemetry telemetry;


    //Values for calculating changes over time
    double lastTime = 0;

    double flLastPos = 0;
    double blLastPos = 0;
    double frLastPos = 0;
    double brLastPos = 0;

    double lastTurn = 0;

    //Values to be read in telemetry
    double turnError;
    double turnOutput;

    double desiredSpeed;
    double desiredFrontLeftSpeed;
    double desiredFrontRightSpeed;
    double desiredBackRightSpeed;
    double desiredBackLeftSpeed;

    double frontLeftOutput;
    double frontRightOutput;
    double backLeftOutput;
    double backRightOutput;

    double currentTurn;

    private double currentAvgPos;

    //Setup such as initializing motors, setting directions and run modes
    public Drivetrain(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        navx = hardwareMap.get(NavxMicroNavigationSensor.class, "navx");

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

    public double getCurrentPos() {
        return currentAvgPos;
    }
    public double getCurrentTurn() {
        return currentTurn;
    }

    //To add or add and update important drivetrain vals to telemetry
    public void addTelemetry(boolean update) {
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

    public void update(double forward, double strafe, double turn) {

        while(navx.isCalibrating()) {
            telemetry.log().add("NavX Calibrating");
            telemetry.update();
        }

        //Calculate direction and speed
        double direction = Math.atan2(forward, strafe);
        desiredSpeed = Math.hypot(forward, strafe);

        //Calculate change in time
        double currentTime = System.currentTimeMillis();
        double elapsedTime = 0;
        if(lastTime == 0) {
            elapsedTime = 0;
        }
        else {
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

        if(flLastPos == 0) {
            flPosChange = flCurrentPos;
            blPosChange = blCurrentPos;
            frPosChange = frCurrentPos;
            brPosChange = brCurrentPos;
        }
        else {
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
        double turnChange;
        if(lastTurn == 0) {
            turnChange = 0;
        }
        else {
            turnChange = currentTurn - lastTurn;
        }
        lastTurn = navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;

        //PID turn
        double turnErrorSum = 0;
        turnError = ((turnChange) - (turn));
        turnErrorSum += turnError;

        double turnP = 0.015;
        double turnI = 0.0;
        double turnD = 0.0;

        turnOutput = turn + (turnError * turnP) + (turnErrorSum * turnI) + (turnChange * turnD);

        //Calculate actual velocity of each motor
        double flActualVelocity = flPosChange / elapsedTime;
        double blActualVelocity = blPosChange / elapsedTime;
        double frActualVelocity = frPosChange / elapsedTime;
        double brActualVelocity = brPosChange / elapsedTime;

        //Calculate desired motor outputs
        desiredFrontLeftSpeed = (Math.sin(direction + Math.PI / 4) * desiredSpeed) + turnOutput;
        desiredFrontRightSpeed = (Math.sin(direction - Math.PI / 4) * desiredSpeed) - turnOutput;
        desiredBackRightSpeed = (Math.sin(direction + Math.PI / 4) * desiredSpeed) - turnOutput;
        desiredBackLeftSpeed = (Math.sin(direction - Math.PI/4) * desiredSpeed) + turnOutput;

        //PID Outputs
        double flSum = 0;
        double frSum = 0;
        double blSum = 0;
        double brSum = 0;

        double flError = desiredFrontLeftSpeed - flActualVelocity;
        double frError = desiredFrontRightSpeed - frActualVelocity;
        double blError = desiredBackLeftSpeed - blActualVelocity;
        double brError = desiredBackRightSpeed - brActualVelocity;

        flSum += flError;
        frSum += frError;
        blSum += blError;
        brSum += brError;

        double kP = 0.2;
        double kI = 0.0;

        frontLeftOutput = desiredFrontLeftSpeed + (flError * kP) + (kI * flSum);
        frontRightOutput = desiredFrontRightSpeed + (frError * kP) + (kI * frSum);
        backLeftOutput = desiredBackLeftSpeed + (blError * kP) + (kI * blSum);
        backRightOutput = desiredBackRightSpeed + (brError * kP) + (kI * brSum);

        //Check for speed scaling factor to counteract greater than 1 outputs
        double max1 = Math.max((Math.abs(frontLeftOutput)), (Math.abs(frontRightOutput)));
        double max2 = Math.max((Math.abs(backLeftOutput)), (Math.abs(backRightOutput)));
        double scalar = Math.max(max1, max2);

        if(scalar > 1) {
            frontLeftOutput /= scalar;
            frontRightOutput /= scalar;
            backLeftOutput /= scalar;
            backRightOutput /= scalar;
        }

        //Calculate average position for driveForwards and backwards commands
        currentAvgPos = (((double)frontLeft.getCurrentPosition()) + ((double)frontRight.getCurrentPosition())
                + ((double)backLeft.getCurrentPosition()) + ((double)backRight.getCurrentPosition())) / 4;

        //Set motor speeds
        frontLeft.setPower(frontLeftOutput);
        frontRight.setPower(frontRightOutput);
        backLeft.setPower(backLeftOutput);
        backRight.setPower(backRightOutput);
    }
}
