package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

@TeleOp
public class DrivetrainTeleOpTest extends OpMode {

    Drivetrain drivetrain;
    //Intake intake;

    @Override
    public void init() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        //intake = new Intake(hardwareMap, telemetry);

        //intake.lockIntake();
    }

    @Override
    public void start() {
        //intake.releaseIntake();
    }

    @Override
    public void loop() {
        drivetrain.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        drivetrain.addTelemetry(true);
        //intake.setIntake(gamepad1.a, gamepad1.b);
    }
}
