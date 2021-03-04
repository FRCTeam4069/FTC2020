package org.firstinspires.ftc.teamcode.opModes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.DriveToPosition;
import org.firstinspires.ftc.teamcode.autonomous.commands.intake.IntakeFeed;
import org.firstinspires.ftc.teamcode.autonomous.commands.intake.IntakeOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.shooter.ShooterOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.shooter.ShooterOn;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Autonomous
public class LowGoalAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);
        Scheduler scheduler = new Scheduler(telemetry, robot);
        scheduler.addCommand(new DriveToPosition(18000, 170000));
        scheduler.addCommand(new ShooterOn(1));
        scheduler.addCommand(new IntakeFeed( 1));
        scheduler.addCommand(new IntakeOff());
        scheduler.addCommand(new ShooterOff());
        scheduler.addCommand(new DriveToPosition(0, 110000));

        waitForStart();

        while(opModeIsActive()) {
            scheduler.run();
        }
    }
}
