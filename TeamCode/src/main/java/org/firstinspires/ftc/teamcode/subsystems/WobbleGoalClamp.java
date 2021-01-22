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

    public void update(boolean clamp, boolean open, double position) {
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

        wobbleMaster.setPosition(position);
        wobbleSlave.setPosition(position);
    }

    public void positionTelemetry(boolean update) {
        telemetry.addData("L Pos", wobbleMaster.getPosition());
        telemetry.addData("R Pos", wobbleSlave.getPosition());
        if(update) telemetry.update();
    }

    public double position() {
        return (wobbleSlave.getPosition() + wobbleMaster.getPosition()) / 2;
    }

    @Override
    public void disable() {
        left.setPower(0);
        right.setPower(0);
    }
}
