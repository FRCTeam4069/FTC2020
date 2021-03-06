package org.firstinspires.ftc.teamcode.subsystems.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;
import org.firstinspires.ftc.teamcode.subsystems.WobbleGoalClamp;
import org.firstinspires.ftc.teamcode.subsystems.odometry.Odometry;

public class Robot {

    public Drivetrain drivetrain;
    public StarterStackDetector detector;
    public WobbleGoalClamp clamp;
    public Intake intake;
    public Shooter shooter;
    public Odometry odometry;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        detector = new StarterStackDetector(hardwareMap, telemetry);
        clamp = new WobbleGoalClamp(hardwareMap, telemetry);
        intake = new Intake(hardwareMap, telemetry);
        shooter = new Shooter(hardwareMap, telemetry);
        odometry = new Odometry(hardwareMap, telemetry);
    }
    public void disableMotors() {
        drivetrain.update(0, 0, 0);
        shooter.update(0);
        intake.update(false, false);
    }
    public void deactivate() {
        drivetrain.disable();
        detector.disable();
        clamp.disable();
        intake.disable();
        shooter.disable();
        odometry.disable();
    }
}
