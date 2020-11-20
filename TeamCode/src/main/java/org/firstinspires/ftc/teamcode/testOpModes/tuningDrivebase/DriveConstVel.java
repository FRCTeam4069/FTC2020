package org.firstinspires.ftc.teamcode.testOpModes.tuningDrivebase;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

@Autonomous
public class DriveConstVel extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap, telemetry);
        double desiredSpeed = 0.5;

        boolean isStarted = false;

        waitForStart();

        double startTime = getRuntime();
        double totalVelocity = 0;
        int itertations = 0;

        while(opModeIsActive()) {

            double elapsedRunTime = getRuntime() - startTime;

            if(!isStarted) {
                while(elapsedRunTime < 3 && opModeIsActive()) {
                    totalVelocity += robot.drivetrain.getAvgVelocity();
                    itertations += 1;
                    elapsedRunTime = getRuntime() - startTime;
                    robot.drivetrain.update(desiredSpeed, 0, 0);
                    isStarted = true;
                }
            }

            if(elapsedRunTime > 3) {
                robot.drivetrain.update(0,0,0);
                telemetry.addData("Time up", true);
            }

            telemetry.addData("Velocity", totalVelocity / itertations);
            telemetry.addData("Runtime", elapsedRunTime);
            robot.drivetrain.pidTelemetry(false);
            robot.drivetrain.displayPIDCoeffs(true);

            idle();
        }
    }
}
