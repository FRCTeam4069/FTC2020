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
        if(clamp) {
            master.setPower(0.5);
        }
        else if(open) {
            master.setPower(-0.5);
        }
        else {
            master.setPower(0);
        }
        slave.setPower(master.getPower());
    }
}
