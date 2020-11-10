package org.firstinspires.ftc.teamcode.tutorials.mecanumTutorials.MecanumThree;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MecanumThreeSubsystem {

    //Declare Motors
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;

    Telemetry telemetry;

    //Motor setup
    public MecanumThreeSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
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
    }

    public void update(double forward, double strafe, double turn) {

        //Calculate direction and magnitude
        double direction = Math.atan2(forward, strafe);
        double magnitude = Math.hypot(forward, strafe);

        //Calculate motor outputs
        double frontLeftOutput = (Math.sin(direction + (Math.PI / 4))) * magnitude;
        double frontRightOutput = (Math.sin(direction - (Math.PI / 4))) * magnitude;
        double backLeftOutput = (Math.sin(direction - (Math.PI / 4))) * magnitude;
        double backRightOutput = (Math.sin(direction + (Math.PI / 4))) * magnitude;

        frontLeftOutput += turn;
        backLeftOutput += turn;
        frontRightOutput -= turn;
        backRightOutput -= turn;

        //Scale back speeds if over 1
        double max1 = Math.max(Math.abs(frontLeftOutput), Math.abs(frontRightOutput));
        double max2 = Math.max(Math.abs(backLeftOutput), Math.abs(backRightOutput));
        double speedScale = Math.max(max1, max2);

        if(speedScale > 1) {
            frontLeftOutput /= speedScale;
            frontRightOutput /= speedScale;
            backLeftOutput /= speedScale;
            backRightOutput /= speedScale;
        }

        frontLeft.setPower(frontLeftOutput);
        frontRight.setPower(frontRightOutput);
        backLeft.setPower(backLeftOutput);
        backRight.setPower(backRightOutput);
    }
}
