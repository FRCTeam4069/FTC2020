package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
                turnVal = gamepad1.right_trigger / 2;
            }
            else if(Math.abs(gamepad1.right_trigger / 2) < 0.025) {
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
