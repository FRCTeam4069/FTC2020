package org.firstinspires.ftc.teamcode.opModes.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.DriveToPosition;
import org.firstinspires.ftc.teamcode.autonomous.commands.intake.DropIntake;
import org.firstinspires.ftc.teamcode.autonomous.commands.intake.IntakeFeed;
import org.firstinspires.ftc.teamcode.autonomous.commands.intake.IntakeOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.RawDriveControl;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.ResetEncoders;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.TurnCommand;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.WaitCommand;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Autonomous
public class HighGoalAuto extends LinearOpMode {

    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();
        boolean isStack = false;

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
        secondScheduler.addCommand(new IntakeFeed(1));
        secondScheduler.addCommand(new WaitCommand(3000));
        secondScheduler.addCommand(new IntakeOff());

        waitForStart();

        //Drive to starter stack
        while(opModeIsActive() && initialScheduler.getQueueSize() != 0) {
            initialScheduler.run();
            telemetry.update();
            idle();
        }

        //Check for stack
        if(opModeIsActive()) {
            double startTime = System.currentTimeMillis();
            while(System.currentTimeMillis() < startTime + 1000 && opModeIsActive()) {
                robot.detector.updateRecognitions();
                if(robot.detector.getStarterStackSize() != 0) isStack = true;
                dashboardTelemetry.addData("IS stack?", isStack);
                dashboardTelemetry.update();
                sleep(50);
                idle();
            }
        }
        //Set third scheduler based on stack (pick it up or no)
        Scheduler thirdScheduler = setScheduler(isStack);

        //Drive to line and fire
        while(opModeIsActive() && secondScheduler.getQueueSize() != 0) {
            double shooterSetpoint;
            if(robot.shooter.isReady()) secondScheduler.run();
//            if(secondScheduler.getQueueSize() < 2) shooterSetpoint = 2550;
//            else shooterSetpoint = 0;
            robot.shooter.update(3000);
            dashboardTelemetry.addData("RPM", robot.shooter.speed);
//            dashboardTelemetry.addData("Shooter setpoint", shooterSetpoint);
            dashboardTelemetry.addData("Queue", secondScheduler.getQueueSize());
            dashboardTelemetry.update();
            idle();
        }

        while(!robot.shooter.isReady() && opModeIsActive()) {
            robot.shooter.update(0);
        }

        boolean indexStarted = false;
        //Pick up stack, fire and park on line or just park on line based on stack
        while(opModeIsActive() && thirdScheduler.getQueueSize() != 0) {
            double timeStartIndex = 0;
            double shooterSetpoint;
            if(robot.shooter.isReady() || thirdScheduler.getQueueSize() > 4) thirdScheduler.run();
            if(thirdScheduler.getQueueSize() <= 4) shooterSetpoint = 3000;
            else shooterSetpoint = 0;
            robot.shooter.update(shooterSetpoint);
            if(robot.odometry.colorSensor().red() > 300 && opModeIsActive() &&
                    robot.odometry.colorSensor().green() > 300 && shooterSetpoint == 0) {
                if(!indexStarted) {
                    timeStartIndex = System.currentTimeMillis();
                    indexStarted = true;
                }
                if(System.currentTimeMillis() < timeStartIndex + 1000) {
                    robot.intake.updatePassthrough(false, true);
                }
            }
            else {
                indexStarted = false;
            }
            dashboardTelemetry.addData("RPM", robot.shooter.speed);
            dashboardTelemetry.addData("Shooter setpoint", shooterSetpoint);
            dashboardTelemetry.addData("Queue", secondScheduler.getQueueSize());
            dashboardTelemetry.update();
            idle();
        }

        robot.deactivate();
    }

    //Set final scheduler based on stack
    private Scheduler setScheduler(boolean isStack) {
        Scheduler scheduler = new Scheduler(telemetry, robot);
        if(isStack) {
            scheduler.addCommand(new DropIntake());
            scheduler.addCommand(new TurnCommand(270));
            scheduler.addCommand(new IntakeFeed(0.5));
            scheduler.addCommand(new RawDriveControl(-0.5, 2200));
            scheduler.addCommand(new IntakeOff());
            scheduler.addCommand(new TurnCommand(270));
            scheduler.addCommand(new DriveToPosition(29000, 40000));
            scheduler.addCommand(new TurnCommand(270));
            //Run shooter here
            scheduler.addCommand(new IntakeFeed(1));
            scheduler.addCommand(new WaitCommand(3000));
            scheduler.addCommand(new DriveToPosition(29000, 60000));
        }
        else scheduler.addCommand(new DriveToPosition(29000, 60000));

        return scheduler;
    }
}
