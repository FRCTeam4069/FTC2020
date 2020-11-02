package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    //Hardware
    Telemetry telemetry;
    DcMotor intakeMotor;
    Servo flop;

    //Initializer initializes hardware and telemetry
    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        flop = hardwareMap.get(Servo.class, "flop");
    }

    //Set flop servo closed
    public void lockIntake() {
        flop.setPosition(0);
    }

    //Set flop servo open
    public void releaseIntake() {
        flop.setPosition(1);
    }

    //Set intake full power out or in based on boolean (buttons)
    public void setIntake(boolean in, boolean out) {
        if(in && !out) {
            intakeMotor.setPower(1);
        }
        else if(out && !in) {
            intakeMotor.setPower(-1);
        }
        else {
            intakeMotor.setPower(0);
        }
    }
}
