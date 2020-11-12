package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {

    public Drivetrain drivetrain;
    public StarterStackDetector detector;
    public WobbleGoalClamp clamp;
    public Intake intake;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        detector = new StarterStackDetector(hardwareMap, telemetry);
        clamp = new WobbleGoalClamp(hardwareMap, telemetry);
        intake = new Intake(hardwareMap, telemetry);
    }

    public void deactivate() {
        drivetrain.disable();
        detector.disable();
        clamp.disable();
        intake.disable();
    }
}
