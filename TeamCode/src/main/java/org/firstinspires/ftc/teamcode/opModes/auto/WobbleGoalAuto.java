package org.firstinspires.ftc.teamcode.opModes.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.ParallelCommand;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.DriveToPosition;
import org.firstinspires.ftc.teamcode.autonomous.commands.intake.DropIntake;
import org.firstinspires.ftc.teamcode.autonomous.commands.intake.IntakeFeed;
import org.firstinspires.ftc.teamcode.autonomous.commands.intake.IntakeOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.ResetEncoders;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.TurnCommand;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.WaitCommand;
import org.firstinspires.ftc.teamcode.autonomous.commands.wobble.WobbleDown;
import org.firstinspires.ftc.teamcode.autonomous.commands.wobble.WobbleIntake;
import org.firstinspires.ftc.teamcode.autonomous.commands.wobble.WobbleIntakeOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.wobble.WobbleUp;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;


//A = ZERO RINGS - CLOSEST TO LAUNCH LINE
//C = FOUR RINGS - CLOSEST TO GOAL

@Autonomous
public class WobbleGoalAuto extends LinearOpMode {

    Robot robot;
    StarterStackDetector.DropZone dropZone;

    public Scheduler setScheduler(StarterStackDetector.DropZone dropZone) {
        Scheduler scheduler = new Scheduler(telemetry, robot);
        if(dropZone == StarterStackDetector.DropZone.A) {
            scheduler.addCommand(new DriveToPosition(-10000, 102000));
            scheduler.addCommand(new WobbleDown());
            scheduler.addCommand(new WobbleIntake(false));
            scheduler.addCommand(new ParallelCommand(new WaitCommand(500),
                    new TurnCommand(270)));
            scheduler.addCommand(new ParallelCommand(new WobbleUp(), new WobbleIntakeOff()));
            scheduler.addCommand(new ParallelCommand(new WobbleUp(), new WobbleIntakeOff()));
            scheduler.addCommand(new DriveToPosition(36000, 125000));
        }
        else if(dropZone == StarterStackDetector.DropZone.B) {
            scheduler.addCommand(new DriveToPosition(35000, 130000));
            scheduler.addCommand(new WobbleDown());
            scheduler.addCommand(new WobbleIntake(false));
            scheduler.addCommand(new ParallelCommand(new WaitCommand(500), new TurnCommand(270)));
            scheduler.addCommand(new ParallelCommand(new WobbleIntakeOff(), new WobbleUp()));
            scheduler.addCommand(new DriveToPosition(35000, 115000));
            scheduler.addCommand(new DropIntake());
            scheduler.addCommand(new TurnCommand(270));
            scheduler.addCommand(new IntakeFeed(0.5));
            scheduler.addCommand(new DriveToPosition(35000, 10000, 0.6));
            scheduler.addCommand(new IntakeOff());
            scheduler.addCommand(new TurnCommand(270));
            scheduler.addCommand(new DriveToPosition(35000, 110000));
            scheduler.addCommand(new TurnCommand(270));
            //Run shooter here
            scheduler.addCommand(new IntakeFeed(1));
            scheduler.addCommand(new WaitCommand(3000));
            scheduler.addCommand(new DriveToPosition(35000, 125000));
        }
        else {
            scheduler.addCommand(new DriveToPosition(-10000, 165000));
            scheduler.addCommand(new WobbleDown());
            scheduler.addCommand(new WobbleIntake(false));
            scheduler.addCommand(new ParallelCommand(new WaitCommand(500), new TurnCommand(270)));
            scheduler.addCommand(new ParallelCommand(new WobbleIntakeOff(), new WobbleUp()));
            scheduler.addCommand(new DriveToPosition(35000, 115000));
            scheduler.addCommand(new DropIntake());
            scheduler.addCommand(new TurnCommand(270));
            scheduler.addCommand(new IntakeFeed(0.5));
            scheduler.addCommand(new DriveToPosition(35000, 10000, 0.6));
            scheduler.addCommand(new IntakeOff());
            scheduler.addCommand(new TurnCommand(270));
            scheduler.addCommand(new DriveToPosition(35000, 110000));
            scheduler.addCommand(new TurnCommand(270));
            //Run shooter here
            scheduler.addCommand(new IntakeFeed(1));
            scheduler.addCommand(new WaitCommand(3000));
            scheduler.addCommand(new DriveToPosition(35000, 125000));
        }
        return scheduler;
    }

    @Override
    public void runOpMode() throws InterruptedException {

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        robot = new Robot(hardwareMap, telemetry);
        robot.odometry.imu().initialize(robot.odometry.parameters);
        Scheduler initialScheduler = new Scheduler(telemetry, robot);
        Scheduler secondScheduler = new Scheduler(telemetry, robot);
        Scheduler thirdScheduler;

        //Scheduler to drive to starter stack
        initialScheduler.addCommand(new DriveToPosition(-60000, 0));
        initialScheduler.addCommand(new TurnCommand(0));

        //Drives to line, turns and fires
        int firstStrafePositionX = robot.odometry.x.getPosition();
        int firstStrafePositionY = robot.odometry.yRight.getPosition();
        secondScheduler.addCommand(new TurnCommand(270));
        secondScheduler.addCommand(new ResetEncoders());
        robot.odometry.yRight.setCurrentPositionTo(-firstStrafePositionX);
        robot.odometry.x.setCurrentPositionTo(firstStrafePositionY);
        secondScheduler.addCommand(new DriveToPosition(35000, 102000));
        secondScheduler.addCommand(new TurnCommand(270));
        //Start running shooter
        secondScheduler.addCommand(new IntakeFeed( 0.9));
        secondScheduler.addCommand(new WaitCommand(3000));
        secondScheduler.addCommand(new IntakeOff());

        boolean isStack = true;

        waitForStart();

        //Drive to starter stack
        while(opModeIsActive() && initialScheduler.getQueueSize() != 0) {
            initialScheduler.run();
            telemetry.update();
            idle();
        }
        int stackSize = 0;
        //Check for stack
        if(opModeIsActive()) {
            double startTime = System.currentTimeMillis();
            while(System.currentTimeMillis() < startTime + 1000 && opModeIsActive()) {
                robot.detector.updateRecognitions();
                stackSize = robot.detector.getStarterStackSize();
              //  dashboardTelemetry.addData("Stack size?", stackSize);
              //  dashboardTelemetry.update();
                sleep(50);
                idle();
            }
        }
        if(stackSize == 4) dropZone = StarterStackDetector.DropZone.C;
        else if (stackSize == 1) dropZone = StarterStackDetector.DropZone.B;
        else {
            dropZone = StarterStackDetector.DropZone.A;
            isStack = false;
        }
        telemetry.addLine("Drop Zone" + dropZone);

        thirdScheduler = setScheduler(dropZone);

        //Drive to line and fire
        while(opModeIsActive() && secondScheduler.getQueueSize() != 0) {
            secondScheduler.run();
            robot.shooter.update(3000);
           // dashboardTelemetry.addData("RPM", robot.shooter.speed);
           // dashboardTelemetry.addData("Queue", secondScheduler.getQueueSize());
            telemetry.addData("Y Position", robot.odometry.yRight.getPosition());
            dashboardTelemetry.update();
            idle();
        }

        //Position after this motion is (29k, 40k) relative to starter stack detection position
        boolean indexOnce = false;
        boolean indexStarted = false;
        while(opModeIsActive() && thirdScheduler.getQueueSize() != 0 && isStack) {
            double timeStartIndex = 0;
            double shooterSetpoint;
            thirdScheduler.run();
            if (thirdScheduler.getQueueSize() <= 5) {
                robot.shooter.update(3000);
                shooterSetpoint = 3000;
            }
            else {
                robot.shooter.rawControl(0);
                shooterSetpoint = 0;
            }

            if (robot.odometry.colorSensor().red() > 300 && opModeIsActive() &&
                    robot.odometry.colorSensor().green() > 300 && shooterSetpoint == 0 && !indexOnce) {
                if (!indexStarted) {
                    timeStartIndex = System.currentTimeMillis();
                    indexStarted = true;

                }
                if (System.currentTimeMillis() < timeStartIndex + 1000) {
                    robot.intake.updatePassthrough(false, true);
                }
                else {
                    robot.intake.updatePassthrough(false, false);
                    indexOnce = true;
                }
            }
            else {
                indexStarted = false;
            }
        }
        
        while(opModeIsActive() && thirdScheduler.getQueueSize() != 0 && !isStack) {
            thirdScheduler.run();
            idle();
        }
        robot.deactivate();
    }
}
