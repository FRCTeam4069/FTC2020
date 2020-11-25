package org.firstinspires.ftc.teamcode.tutorials.mecanumTutorials.MecanumThree;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.tutorials.mecanumTutorials.MecanumThree.MecanumSubsystem;

@Disabled
@TeleOp
public class MecanumSubsystemTeleOp extends OpMode {

    MecanumSubsystem drivetrain;

    @Override
    public void init() {
        drivetrain = new MecanumSubsystem(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        drivetrain.update(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
    }
}
