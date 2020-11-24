package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Passthrough extends RobotHardware {

    Telemetry telemetry;

    public Passthrough(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap);
        this.telemetry = telemetry;
    }

    public void update(boolean in, boolean out) {
        if(in && !out) {
            passthroughMotor.setPower(1);
        }
        else if(out && !in) {
            passthroughMotor.setPower(-1);
        }
        else {
            passthroughMotor.setPower(0);
        }
    }

    @Override
    public void disable() {
        passthroughMotor.setPower(0);
    }
}
