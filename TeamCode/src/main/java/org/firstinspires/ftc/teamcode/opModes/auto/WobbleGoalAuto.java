package org.firstinspires.ftc.teamcode.opModes.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.DriveToPosition;
import org.firstinspires.ftc.teamcode.autonomous.commands.intake.IntakeFeed;
import org.firstinspires.ftc.teamcode.autonomous.commands.intake.IntakeOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.ResetEncoders;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.TurnCommand;
import org.firstinspires.ftc.teamcode.autonomous.commands.WaitCommand;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Autonomous
public class WobbleGoalAuto extends LinearOpMode {

    Robot robot;
    Scheduler scheduler;


    public void setScheduler(StarterStackDetector.DropZone dropZone) {
        if(dropZone == StarterStackDetector.DropZone.A)
            scheduler.addCommand(new DriveToPosition(50000, 75000));
        else if(dropZone == StarterStackDetector.DropZone.B)
            scheduler.addCommand(new DriveToPosition(25000, 100000));
        else scheduler.addCommand(new DriveToPosition(50000, 125000));
    }

    @Override
    public void runOpMode() throws InterruptedException {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        robot = new Robot(hardwareMap, telemetry);
        Scheduler initialScheduler = new Scheduler(telemetry, robot);
        Scheduler secondScheduler = new Scheduler(telemetry, robot);

        //Scheduler to drive to starter stack
        initialScheduler.addCommand(new DriveToPosition(-62000, 0));

        //Drives to line, turns and fires
        secondScheduler.addCommand(new ResetEncoders());
        secondScheduler.addCommand(new TurnCommand(270));
        secondScheduler.addCommand(new ResetEncoders());
        secondScheduler.addCommand(new DriveToPosition(29000, 40000));
        secondScheduler.addCommand(new TurnCommand(270));
        //Start running shooter
        secondScheduler.addCommand(new IntakeFeed(false, 1));
        secondScheduler.addCommand(new WaitCommand(3000));
        secondScheduler.addCommand(new IntakeOff());

        waitForStart();

        //Drive to starter stack
        while(opModeIsActive() && initialScheduler.getQueueSize() != 0) {
            initialScheduler.run();
            telemetry.update();
            idle();
        }
        int stackSize;
        //Check for stack
        if(opModeIsActive()) {
            double startTime = System.currentTimeMillis();
            while(System.currentTimeMillis() < startTime + 1000 && opModeIsActive()) {
                robot.detector.updateRecognitions();
                stackSize = robot.detector.getStarterStackSize(100);
                dashboardTelemetry.addData("Stack size?", stackSize);
                dashboardTelemetry.update();
                sleep(50);
                idle();
            }
        }

    }
}
