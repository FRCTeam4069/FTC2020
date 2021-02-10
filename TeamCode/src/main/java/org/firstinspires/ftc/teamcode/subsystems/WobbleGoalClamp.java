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

    public void update(boolean clamp, boolean open, double power) {
        if(clamp && !open) {
            left.setPower(1);
            right.setPower(-1);
        }
        else if(open && !clamp) {
            left.setPower(-1);
            right.setPower(1);
        }
        else {
            left.setPower(0);
            right.setPower(0);
        }

        wobbleMaster.setPower(power);
        wobbleSlave.setPower(power);
    }

    public boolean topSensor() {return top.isPressed();}
    public boolean bottomSensor() {return bottom.isPressed();}

    @Override
    public void disable() {
        left.setPower(0);
        right.setPower(0);
    }
}
