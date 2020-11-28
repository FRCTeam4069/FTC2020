package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.TurnCommand;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;
import org.firstinspires.ftc.teamcode.testOpModes.tuningDrivebase.TurnToAngle;

@TeleOp
public class DrivetrainTeleOpTest extends OpMode {

    Robot robot;
    Scheduler scheduler;
    boolean turnIsZero = false;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        scheduler= new Scheduler(telemetry, robot);
    }

    @Override
    public void loop() {
        robot.drivetrain.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        robot.odometry.addTelemetry(true);
        robot.intake.update(gamepad1.a, gamepad1.b);
    }
}
