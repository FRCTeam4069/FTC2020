package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@TeleOp
public class MasterTeleDouble extends OpMode {

    Robot robot;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        robot.drivetrain.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        if(gamepad2.right_trigger > 0.5) {
            robot.shooter.update(2500);
        }
        else if(gamepad2.left_trigger > 0.5) {
            robot.shooter.update(-1000);
        }
        else if(gamepad2.x) robot.shooter.update(3000);
        else if(gamepad2.y) robot.shooter.update(2750);
        else {
            robot.shooter.rawControl(0);
        }
        robot.intake.update(gamepad2.a, gamepad2.b);
        robot.clamp.update(gamepad2.left_bumper, gamepad2.right_bumper, gamepad2.dpad_up, gamepad2.dpad_down);
    }
}
