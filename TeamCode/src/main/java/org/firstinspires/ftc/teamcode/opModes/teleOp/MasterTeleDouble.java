package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@TeleOp
public class MasterTeleDouble extends OpMode {


    Robot robot;
    double shooterSetpoint;
    boolean in;
    boolean out;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        shooterSetpoint = 0;
    }

    @Override
    public void loop() {

        //Control Drivetrain
        robot.drivetrain.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        //Control Intake
        if(gamepad2.a) {
            in = true;
            out = false;
        }
        else if(gamepad2.b) {
            out = true;
            in = false;
        }
        else {
            in = false;
            out = false;
        }

        //Control shooter
        if(gamepad2.y) shooterSetpoint = 2000;
        else if(gamepad2.x) shooterSetpoint = 2500;
        else if(gamepad2.left_bumper) {
            shooterSetpoint = -500;
            in = false;
            out = true;
        }
        else {
            shooterSetpoint = 0;
            robot.shooter.rawControl(0);
        }
        robot.shooter.update(shooterSetpoint);
        robot.intake.update(in, out);
        //robot.clamp.update(gamepad1.left_bumper, gamepad1.right_bumper, gamepad1.dpad_up, gamepad1.dpad_down);
    }
}
