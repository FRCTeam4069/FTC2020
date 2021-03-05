package org.firstinspires.ftc.teamcode.subsystems.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Encoder {

    DcMotorEx motor;
    private int positionalDifference;

    //Initializer takes in the motor associated with the encoder port, and direction
    public Encoder(Telemetry telemetry, DcMotorEx motor, State state) {

        positionalDifference = 0;
        //Ensure motor is not null
        if(motor != null) {
            this.motor = motor;

            if(state == State.POSITIVE) {
                motor.setDirection(DcMotorSimple.Direction.FORWARD);
            }
            else {
                motor.setDirection(DcMotorSimple.Direction.REVERSE);
            }

            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        else {
            telemetry.addData("Motor passed to encoder", null);
            telemetry.update();
        }
    }

    public int getPosition() {
        return motor.getCurrentPosition() + positionalDifference;
    }

    public double getCurrentVel() {
        return motor.getVelocity();
    }

    //Reset encoder
    public void reset() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setCurrentPositionTo(int position) {
        reset();
        positionalDifference = position;
    }

    public void addPosition(double position) {
        positionalDifference += position;
    }

    //Directional state
    enum State {
        POSITIVE,
        NEGATIVE
    }
}
