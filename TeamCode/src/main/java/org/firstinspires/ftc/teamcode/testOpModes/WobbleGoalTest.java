package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.WobbleGoalClamp;

@TeleOp
public class WobbleGoalTest extends OpMode {

    WobbleGoalClamp wobble;

    @Override
    public void init() {
        wobble = new WobbleGoalClamp(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        wobble.update(gamepad1.left_bumper, gamepad1.right_bumper);
    }
}
