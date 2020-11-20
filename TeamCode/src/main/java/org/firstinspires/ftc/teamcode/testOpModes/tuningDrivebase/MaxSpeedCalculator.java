package org.firstinspires.ftc.teamcode.testOpModes.tuningDrivebase;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

@Autonomous
public class MaxSpeedCalculator extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap, telemetry);
        double[] greatestSpeeds = {0, 0, 0, 0, 0};

        waitForStart();
        double startTime = getRuntime();
        while(opModeIsActive() && getRuntime() - startTime < 3) {
            robot.drivetrain.update(0.5,0,0);
            double currentVelocity = robot.drivetrain.getAvgVelocity();
            for(int i = 0; i < greatestSpeeds.length; i++) {
                if(greatestSpeeds[i] < currentVelocity) {
                    greatestSpeeds[i] = currentVelocity;
                    break;
                }
            }
            idle();
        }
        while(opModeIsActive()) {
            robot.disableMotors();
            double total = 0;
            for(double speed : greatestSpeeds) {
                total += speed;
            }
            double maxVel = total / 5;
            telemetry.addData("Max Velocity", maxVel);
            telemetry.update();
            idle();
        }
    }
}
