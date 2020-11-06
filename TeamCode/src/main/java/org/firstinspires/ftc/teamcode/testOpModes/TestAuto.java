package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveForward;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;


@Autonomous
public class TestAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Drivetrain drivetrain = new Drivetrain(hardwareMap, telemetry);
        StarterStackDetector detector = new StarterStackDetector(hardwareMap, telemetry);

        Scheduler scheduler = new Scheduler(telemetry, drivetrain, detector, null);
        scheduler.addCommand(new DriveForward(1000, 0.5));
        //scheduler.addCommand(new DriveForward(-500, -0.5));

        waitForStart();

        while(opModeIsActive()) {
            scheduler.run();
            telemetry.addData("position", drivetrain.getCurrentPos());
            if(scheduler.getQueueSize() == 0) {
                drivetrain.update(0,0,0);
            }
            idle();
        }
    }
}
