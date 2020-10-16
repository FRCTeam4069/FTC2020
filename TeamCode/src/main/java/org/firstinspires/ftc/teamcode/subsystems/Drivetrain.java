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

    double desiredFrontLeftSpeed;
    double desiredFrontRightSpeed;
    double desiredBackRightSpeed;
    double desiredBackLeftSpeed;

    double frontLeftOutput;
    double frontRightOutput;
    double backLeftOutput;
    double backRightOutput;

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

    //To add or add and update important drivetrain vals to telemetry
    public void addTelemetry(boolean update) {
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

        if(update) {
            telemetry.update();
        }
    }

    public void update(double forward, double strafe, double turn) {

        //Calculate direction and speed
        double direction = Math.atan2(forward, strafe);
        double desiredSpeed = Math.hypot(forward, strafe);

        //Calculate change in time
        double currentTime = System.currentTimeMillis();
        double elapsedTime = 0;
        if(lastTime == 0) {
            elapsedTime = 0;
        }
        else {
            elapsedTime = currentTime - lastTime;
        }
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
        double currentTurn = navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle;
        double turnChange = 0;
        if(lastTurn == 0) {
            turnChange = 0;
        }
        else {
            turnChange = currentTurn - lastTurn;
        }
        lastTurn = navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle;

        //PID turn
        turnError = (turnChange / elapsedTime) - (turn);
        double turnP = 0.1;
        double turnD = 0.0;

        turnOutput = (turnError * turnP) + (turnChange * turnD);

        //Calculate actual velocity of each motor
        double flActualVelocity = flPosChange / elapsedTime;
        double blActualVelocity = blPosChange / elapsedTime;
        double frActualVelocity = frPosChange / elapsedTime;
        double brActualVelocity = brPosChange / elapsedTime;

        //Calculate desired motor outputs
        desiredFrontLeftSpeed = (Math.sin(direction + Math.PI / 4) * desiredSpeed) + turnOutput;
        desiredFrontRightSpeed = (Math.sin(direction - Math.PI / 4) * desiredSpeed) - turnOutput;
        desiredBackRightSpeed = (Math.sin(direction + Math.PI / 4) * desiredSpeed) + turnOutput;
        desiredBackLeftSpeed = (Math.sin(direction - Math.PI/4) * desiredSpeed) - turnOutput;

        //PID Outputs
        double flError = desiredFrontLeftSpeed - flActualVelocity;
        double frError = desiredFrontRightSpeed - frActualVelocity;
        double blError = desiredBackLeftSpeed - blActualVelocity;
        double brError = desiredBackRightSpeed - brActualVelocity;

        double P = 0.1;

        frontLeftOutput = desiredFrontLeftSpeed + (flError * P);
        frontRightOutput = desiredFrontRightSpeed + (frError * P);
        backLeftOutput = desiredBackLeftSpeed + (blError * P);
        backRightOutput = desiredBackRightSpeed + (brError * P);

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

        //Set motor speeds
        frontLeft.setPower(frontLeftOutput);
        frontRight.setPower(frontRightOutput);
        backLeft.setPower(backLeftOutput);
        backRight.setPower(backRightOutput);
    }
}
