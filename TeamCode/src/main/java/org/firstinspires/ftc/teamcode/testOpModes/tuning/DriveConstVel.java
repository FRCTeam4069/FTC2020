package org.firstinspires.ftc.teamcode.testOpModes.tuning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

@Autonomous
public class DriveConstVel extends LinearOpMode {

    Robot robot = new Robot(hardwareMap, telemetry);

    @Override
    public void runOpMode() throws InterruptedException {

        double desiredSpeed = 0.5;

        waitForStart();

        double startTime = getRuntime();

        boolean isStarted = false;

        while(opModeIsActive()) {

            double elapsedRunTime = getRuntime() - startTime;

            if(!isStarted) {
                robot.drivetrain.update(desiredSpeed, 0, 0);
                isStarted = true;
            }
            if(elapsedRunTime > 3000) robot.drivetrain.update(0,0,0);

            telemetry.addData("Velocity",robot.drivetrain.getAvgVelocity());
            telemetry.addData("Runtime", elapsedRunTime);
            robot.drivetrain.pidTelemetry(false);
            robot.drivetrain.displayPIDCoeffs(true);

            idle();
        }
    }
}
