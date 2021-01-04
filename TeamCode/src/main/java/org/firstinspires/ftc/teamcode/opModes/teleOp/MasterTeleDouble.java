package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

import java.util.HashMap;

@TeleOp
public class MasterTeleDouble extends OpMode {

    Robot robot;
    double shooterSetpoint;
    boolean in;
    boolean out;
    HashMap<String, Integer> colourVals;
    double startingTime = 0;

    boolean isIndexing;
    boolean shooterRunning = false;
    boolean turningToAngle = false;
    boolean active = false;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        colourVals = robot.odometry.sensorValues();

        //Control Drivetrain
        double turnVal;
        turnVal = gamepad1.right_stick_x;
        if(Math.abs(turnVal) < 0.05) {
            if(Math.abs(gamepad1.left_trigger / 2) < 0.025) {
                telemetry.addData("Mini turn", true);
                turnVal = gamepad1.right_trigger / 2;
            }
            else if(Math.abs(gamepad1.right_trigger / 2) < 0.025) {
                telemetry.addData("Mini turn", true);
                turnVal = -(gamepad1.left_trigger / 2);
            }
        }

        double[] outputs = new double[3];
        if((Math.abs(gamepad1.left_stick_y) > 0.025 || Math.abs(gamepad1.left_stick_x) > 0.025 ||
                Math.abs(turnVal) > 0.025) && !turningToAngle) {
            outputs[0] = -gamepad1.left_stick_y;
            outputs[1] = gamepad1.left_stick_x;
        }
        else if(gamepad1.left_bumper || gamepad1.right_bumper || gamepad1.y || gamepad1.a) {
            turningToAngle = true;
            if(!robot.drivetrain.atCorrectAngle() || !active) {
                telemetry.addData("Turning to angle", true);
                telemetry.addData("Heading", robot.odometry.getCurrentHeading());
                double angle = 0;
                if (gamepad1.left_bumper) angle = 90;
                else if (gamepad1.right_bumper) angle = 270;
                else if (gamepad1.a) angle = 180;
                turnVal = robot.drivetrain.turnToAngle(angle);
                active = true;
            }
            else {
                turningToAngle = false;
                turnVal = 0;
                active = false;
            }
        }
        else {
            turningToAngle = false;
            telemetry.addData("Direct", true);
            Drivetrain.Direction direction;
            if(gamepad1.dpad_up) direction = Drivetrain.Direction.FORWARD;
            else if(gamepad1.dpad_down) direction = Drivetrain.Direction.BACKWARD;
            else if(gamepad1.dpad_left) direction = Drivetrain.Direction.LEFT;
            else if(gamepad1.dpad_right) direction = Drivetrain.Direction.RIGHT;
            else direction = Drivetrain.Direction.NO_DIRECTION;

            outputs = robot.drivetrain.directDrive(direction);
            for(double output : outputs) {
                telemetry.addData("Output", output);
            }
        }
        outputs[2] = turnVal;

        robot.drivetrain.update(outputs[0], outputs[1], outputs[2]);

        //Indexing Routine (NO JAMS!!!)
        if(!isIndexing) {
            if (colourVals.get("Red") > 300 && colourVals.get("Green") > 300) {
                telemetry.addData("Indexing", true);

                startingTime = System.currentTimeMillis();

                isIndexing = true;
            }
        }
        else {
            if((System.currentTimeMillis() < startingTime + 2000) && (colourVals.get("Red") > 300 &&
                    colourVals.get("Green") > 300) && !gamepad2.left_stick_button && !shooterRunning) {
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
        if(gamepad2.a && !gamepad2.b) {
            in = true;
            out = false;
        }
        else if(gamepad2.b && gamepad2.a) {
            out = true;
            in = false;
        }
        else {
            in = false;
            out = false;
        }
        if(!isIndexing) robot.intake.update(in, out);

        if(!in && !out && !isIndexing) {
            robot.intake.updateIntake(gamepad2.x, gamepad2.y);
        }

        //Control shooter
        //High Goal Line
        if (gamepad2.right_bumper) {
            shooterSetpoint = 2000;
            shooterRunning = true;
        }

        //High Goal Wall
        else if (gamepad2.left_bumper) {
            shooterSetpoint = 2250;
            shooterRunning = true;
        }

        //Power Shot line
        else if (gamepad2.left_trigger > 0.25) {
            shooterSetpoint = 1750;
            shooterRunning = true;
        }

        //Power Shot wall
        else if(gamepad2.right_trigger > 0.25) {
            shooterSetpoint = 2000;
            shooterRunning = true;
        }

        //Unjam
        else if(gamepad2.right_stick_button) {
            shooterSetpoint = -500;
        }
        else {
            shooterSetpoint = 0;
            shooterRunning = false;
        }

        telemetry.addData("Shooter setpoint", shooterSetpoint);
        robot.shooter.getTelemetry(false);
        if(!isIndexing) robot.shooter.update(shooterSetpoint);
        if(shooterRunning && !isIndexing && robot.shooter.isReady())
            robot.intake.updatePassthrough(true, false);

        telemetry.update();
    }
}
