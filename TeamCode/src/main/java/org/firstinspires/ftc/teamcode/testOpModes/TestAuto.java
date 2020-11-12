package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveForward;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Robot;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;


@Autonomous
public class TestAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);

        Scheduler scheduler = new Scheduler(telemetry, robot);
        scheduler.addCommand(new DriveForward(1000, 0.75));

        waitForStart();

        while(opModeIsActive()) {
            scheduler.run();
            telemetry.addData("position", robot.drivetrain.getCurrentPos());
            if(scheduler.getQueueSize() == 0) {
                scheduler.disableSubsystems();
            }
            idle();
        }
    }
}
