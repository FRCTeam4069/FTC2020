package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.robot.RobotHardware;

public class WobbleGoalClamp extends RobotHardware {

    Telemetry telemetry;

    public WobbleGoalClamp(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap);
        this.telemetry = telemetry;
    }

    public void update(boolean clamp, boolean open) {
        if(clamp && !open) {
            wobbleMaster.setPower(1);
        }
        else if(open && !clamp) {
            wobbleMaster.setPower(-1);
        }
        else {
            wobbleMaster.setPower(0);
        }
        wobbleSlave.setPower(wobbleMaster.getPower());
    }

    @Override
    public void disable() {
        wobbleSlave.setPower(0);
        wobbleMaster.setPower(0);
    }
}
