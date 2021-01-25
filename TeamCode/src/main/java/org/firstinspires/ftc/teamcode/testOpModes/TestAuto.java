package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.DriveForward;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Disabled
@Autonomous
public class TestAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);

        Scheduler scheduler = new Scheduler(telemetry, robot);
        scheduler.addCommand(new DriveForward(1000));

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
