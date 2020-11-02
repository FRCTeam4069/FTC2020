package org.firstinspires.ftc.teamcode.tutorials;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@Autonomous
public class BasicAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        DcMotor rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        int desiredPosition = 1000;
        int avgPosition = (leftMotor.getCurrentPosition() + rightMotor.getCurrentPosition()) / 2;

        waitForStart();

        while(desiredPosition > avgPosition) {
            leftMotor.setPower(0.75);
            rightMotor.setPower(0.75);
            avgPosition = (leftMotor.getCurrentPosition() + rightMotor.getCurrentPosition()) / 2;
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }
}
