package org.firstinspires.ftc.teamcode.tutorials.mecanumTutorials;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class MecanumTwo extends OpMode {

    //Controlling mecanum using joystick


    //Declare motors
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    @Override
    public void init() {

        //Initialize motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        //Set motor directions and runmodes
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

    @Override
    public void loop() {

        //Calculating direction and magnitude from game controller
        double direction = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x);
        double magnitude = Math.hypot(gamepad1.left_stick_y, gamepad1.left_stick_x);

        //Calculate motor outputs
        double frontLeftOutput = (Math.sin(direction + (Math.PI / 4))) * magnitude;
        double frontRightOutput = (Math.sin(direction - (Math.PI / 4))) * magnitude;
        double backLeftOutput = (Math.sin(direction - (Math.PI / 4))) * magnitude;
        double backRightOutput = (Math.sin(direction + (Math.PI / 4))) * magnitude;

        //Set motor outputs
        frontLeft.setPower(frontLeftOutput);
        frontRight.setPower(frontRightOutput);
        backLeft.setPower(backLeftOutput);
        backRight.setPower(backRightOutput);

        //Display telemetry
        telemetry.addData("Left Front Output", frontLeftOutput);
        telemetry.addData("Right Front Output", frontRightOutput);
        telemetry.addData("Left Back Output", backLeftOutput);
        telemetry.addData("Right Back Output", backRightOutput);

        telemetry.addData("Speed", magnitude);

        telemetry.update();
    }
}
