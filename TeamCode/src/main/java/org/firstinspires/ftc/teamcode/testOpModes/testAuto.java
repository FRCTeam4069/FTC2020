package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveForward;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;

@Autonomous
public class testAuto extends OpMode {

    Scheduler scheduler;

    @Override
    public void init() {
        Drivetrain drivetrain = new Drivetrain(hardwareMap, telemetry);
        StarterStackDetector stackDetector = new StarterStackDetector(hardwareMap, telemetry);
        scheduler = new Scheduler(telemetry, drivetrain, stackDetector);
        scheduler.addCommand(new DriveForward(2000));
    }

    @Override
    public void loop() {
        scheduler.run();
    }
}
