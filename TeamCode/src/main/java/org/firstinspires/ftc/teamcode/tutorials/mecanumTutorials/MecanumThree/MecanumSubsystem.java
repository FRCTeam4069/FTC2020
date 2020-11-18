package org.firstinspires.ftc.teamcode.tutorials.mecanumTutorials.MecanumThree;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MecanumSubsystem {

    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;

    Telemetry telemetry;

    public MecanumSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

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

    public void update(double forward, double strafe, double turn) {

        double direction = Math.atan2(forward, strafe);
        double speed = Math.hypot(forward, strafe);

        double frontLeftOutput = (Math.sin(direction + Math.PI / 4) * speed) + turn;
        double frontRightOutput = (Math.sin(direction - Math.PI / 4) * speed) - turn;
        double backLeftOutput = (Math.sin(direction - Math.PI / 4) * speed) + turn;
        double backRightOutput = (Math.sin(direction + Math.PI / 4) * speed) - turn;

        double max1 = Math.max(Math.abs(frontLeftOutput), Math.abs(backLeftOutput));
        double max2 = Math.max(Math.abs(frontRightOutput), Math.abs(backRightOutput));
        double speedScalar = Math.max(max1, max2);

        if(speedScalar > 1) {
            frontLeftOutput /= speedScalar;
            backLeftOutput /= speedScalar;
            frontRightOutput /= speedScalar;
            backRightOutput /= speedScalar;
        }

        frontLeft.setPower(frontLeftOutput);
        frontRight.setPower(frontRightOutput);
        backLeft.setPower(backLeftOutput);
        backRight.setPower(backRightOutput);
    }
}
