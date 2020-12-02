package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.robot.RobotHardware;

public class Intake extends RobotHardware {

    //Hardware
    Telemetry telemetry;

    //Initializer initializes hardware and telemetry
    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap);
        this.telemetry = telemetry;
    }
    //Set intake full power out or in based on boolean (buttons)
    public void update(boolean in, boolean out) {
        if(in && !out) {
            intakeMotor.setPower(1);
            passthroughMotor.setPower(-1);
        }
        else if(out && !in) {
            intakeMotor.setPower(-1);
            passthroughMotor.setPower(1);
        }
        else {
            intakeMotor.setPower(0);
            passthroughMotor.setPower(0);
        }
    }

    @Override
    public void disable() {
        intakeMotor.setPower(0);
    }
}
