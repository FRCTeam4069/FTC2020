package org.firstinspires.ftc.teamcode.tutorials;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@TeleOp
public class BasicTankDrive extends OpMode {

    DcMotor leftMotor;
    DcMotor rightMotor;

    @Override
    public void init() {
//        int wholeNum = 123; // 654, 1, 0, -87541
//        double pi = 3.1415926;
//        boolean isRoboticsGreat = true;
        String initMessage = "Initialized";
        telemetry.addData("Status", initMessage); // Status: Initialized

        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");

        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        double leftPower = -gamepad1.left_stick_y;
        double rightPower = -gamepad1.right_stick_y;

        double avgPower = (leftPower + rightPower) / 2;
        boolean isFast = avgPower > 0.75;

        if(isFast) {
            telemetry.addData("Speed of Robot", avgPower);
        }
        else {
            telemetry.addData("Robot is fast?", false);
        }

        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
    }
}
