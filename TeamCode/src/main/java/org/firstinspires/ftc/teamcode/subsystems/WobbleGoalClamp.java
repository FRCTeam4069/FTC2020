package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class WobbleGoalClamp extends RobotHardware {

    Telemetry telemetry;

    public WobbleGoalClamp(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap);
        this.telemetry = telemetry;
    }

    public void update(boolean clamp, boolean open) {
        if(clamp && !open) {
            master.setPower(1);
        }
        else if(open && !clamp) {
            master.setPower(-1);
        }
        else {
            master.setPower(0);
        }
        slave.setPower(master.getPower());
    }

    @Override
    public void disable() {
        slave.setPower(0);
        master.setPower(0);
    }
}
