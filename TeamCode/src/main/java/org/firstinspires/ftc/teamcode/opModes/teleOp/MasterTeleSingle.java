package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

import java.util.HashMap;

@TeleOp
public class MasterTeleSingle extends OpMode {

    Robot robot;
    double shooterSetpoint;
    boolean in;
    boolean out;
    double startingTime = 0;
    boolean isIndexing;

    boolean shooterRunning = false;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        shooterSetpoint = 0.0;
    }

    @Override
    public void loop() {

        //Control Drivetrain
        double turnVal;
        turnVal = gamepad1.right_stick_x;
        if(Math.abs(turnVal) < 0.05) {
            if(Math.abs(gamepad1.left_trigger / 2) < 0.025) {
                turnVal = gamepad1.right_trigger / 2;
            }
            if(Math.abs(gamepad1.right_trigger / 2) < 0.025) {
                turnVal = -(gamepad1.left_trigger / 2);
            }
        }

        if(Math.abs(gamepad1.left_stick_y) > 0.025 || Math.abs(gamepad1.left_stick_x) > 0.025 ||
                Math.abs(turnVal) > 0.025) {
            robot.drivetrain.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, turnVal);
        }
        else {
            Drivetrain.Direction direction = Drivetrain.Direction.NO_DIRECTION;
            if(gamepad1.dpad_up) direction = Drivetrain.Direction.FORWARD;
            else if(gamepad1.dpad_down) direction = Drivetrain.Direction.BACKWARD;
            else if(gamepad1.dpad_left) direction = Drivetrain.Direction.LEFT;
            else if(gamepad1.dpad_right) direction = Drivetrain.Direction.RIGHT;

            robot.drivetrain.directDrive(direction);
        }
        //Indexing Routine (NO JAMS!!!)
        if(!isIndexing) {
            if (robot.odometry.colorSensor().red() > 300 && robot.odometry.colorSensor().green() > 300) {
                telemetry.addData("Indexing", true);

                startingTime = System.currentTimeMillis();

                isIndexing = true;
            }
        }
        else {
            if((System.currentTimeMillis() < startingTime + 2000) && (robot.odometry.colorSensor().red() > 300 &&
                    robot.odometry.colorSensor().green() > 300) && !gamepad1.left_stick_button && !shooterRunning) {
                robot.intake.updatePassthrough(false, true);
                robot.shooter.rawControl(-0.25);
                robot.intake.updateIntake(false, false);
            }
            else {
                isIndexing = false;
                telemetry.addData("Indexing", false);
            }
        }

        //Control Intake
        if(gamepad1.a) {
            in = true;
            out = false;
        }
        else if(gamepad1.b) {
            out = true;
            in = false;
        }
        else {
            in = false;
            out = false;
        }
        if(!isIndexing) robot.intake.update(in, out);

        //Control shooter
        if (gamepad1.y) {
            shooterSetpoint = 2000;
            shooterRunning = true;
        }
        else if (gamepad1.x) {
            shooterSetpoint = 2250;
            shooterRunning = true;
        }
        else if (gamepad1.left_bumper) {
            shooterSetpoint = -500;
            in = false;
            out = true;
        }
        else {
            shooterSetpoint = 0;
            shooterRunning = false;
        }

        telemetry.addData("Shooter setpoint", shooterSetpoint);
        robot.shooter.getTelemetry(false);
        if(!isIndexing) robot.shooter.update(shooterSetpoint);

        //robot.clamp.update(gamepad1.left_bumper, gamepad1.right_bumper, gamepad1.dpad_up, gamepad1.dpad_down);

        telemetry.update();
    }
}
