package org.firstinspires.ftc.teamcode.tutorials.juniorTest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp
public class JuniorWobble extends OpMode {

    CRServo left;
    CRServo right;

    @Override
    public void init() {
        left = hardwareMap.get(CRServo.class, "left");
        right = hardwareMap.get(CRServo.class, "right");
    }

    @Override
    public void loop() {
        if(gamepad1.a) {
            left.setPower(1);
            right.setPower(-1);
        }
        else if(gamepad1.b) {
            left.setPower(-1);
            right.setPower(1);
        }
        else {
            left.setPower(0);
            right.setPower(0);
        }
    }
}
