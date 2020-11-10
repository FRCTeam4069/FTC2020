package org.firstinspires.ftc.teamcode.tutorials.mecanumTutorials.MecanumThree;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class MecanumThreeTeleOp extends OpMode {

    //Driver controlled full mecanum control

    //Declare new variable, object of type drivetrain subsystem
    MecanumThreeSubsystem drivetrain;

    @Override
    public void init() {

        //Initialize drivetrain
        drivetrain = new MecanumThreeSubsystem(hardwareMap, telemetry);
    }

    @Override
    public void loop() {

        //Update behaviour based on driver input
        drivetrain.update(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
    }
}