package org.firstinspires.ftc.teamcode.opModes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveToPosition;
import org.firstinspires.ftc.teamcode.autonomous.commands.IntakeFeed;
import org.firstinspires.ftc.teamcode.autonomous.commands.IntakeOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.ShooterOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.ShooterOn;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Autonomous
public class LowGoalAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);
        Scheduler scheduler = new Scheduler(telemetry, robot);
        scheduler.addCommand(new DriveToPosition(18000, 190000));
        scheduler.addCommand(new ShooterOn(1));
        scheduler.addCommand(new IntakeFeed(5000));
        scheduler.addCommand(new IntakeOff());
        scheduler.addCommand(new ShooterOff());
        scheduler.addCommand(new DriveToPosition(0, 120000));

        waitForStart();

        while(opModeIsActive()) {
            scheduler.run();
        }
    }
}
