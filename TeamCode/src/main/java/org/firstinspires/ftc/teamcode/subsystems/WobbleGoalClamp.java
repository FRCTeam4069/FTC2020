package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class WobbleGoalClamp {

    Telemetry telemetry;
    private CRServo master;
    private CRServo slave;

    public WobbleGoalClamp(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        master = hardwareMap.get(CRServo.class, "master");
        slave = hardwareMap.get(CRServo.class, "slave");
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
}
