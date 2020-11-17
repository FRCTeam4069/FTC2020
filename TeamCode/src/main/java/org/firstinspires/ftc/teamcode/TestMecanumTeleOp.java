package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TestMecanumTeleOp extends OpMode {

    MecanumSubsystem drivebase;

    @Override
    public void init() {
        drivebase = new MecanumSubsystem(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        drivebase.update(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
    }
}
