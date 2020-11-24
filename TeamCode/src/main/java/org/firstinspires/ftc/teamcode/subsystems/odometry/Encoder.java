package org.firstinspires.ftc.teamcode.subsystems.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Encoder {

    DcMotorEx motor;

    public Encoder(Telemetry telemetry, DcMotorEx motor) {
        if(motor != null) {
            this.motor = motor;
        }
        else {
            telemetry.addData("Motor passed to encoder", null);
            telemetry.update();
        }
    }

    public int getPosition() {
        return motor.getCurrentPosition();
    }

    public double getCurrentVel() {
        return motor.getVelocity();
    }

    public void reset() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
