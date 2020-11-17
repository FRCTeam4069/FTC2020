package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

@Autonomous
public class DriveToPosition extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);
        boolean started = false;
        double lastPos = 0;
        double changeInError;
        double lastError = 0;
        double desiredPosition = 1500;

        waitForStart();

        while(opModeIsActive()) {
            double kP = 0.1;
            double kI = 0.0;
            double kD = 0.0;

            double error = desiredPosition - robot.drivetrain.getCurrentPos();

            if(!started) {
                changeInError = 0;
                started = true;
            }
            else {
                changeInError = error - lastError;
            }
            lastError = error;
            double errorSum = 0;
            errorSum += error;


            double output = kP * error + kI * errorSum + kD * changeInError;
            robot.drivetrain.update(output, 0, 0);
            idle();
        }
    }
}
