package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.robot.RobotHardware;

public class Shooter extends RobotHardware {

    Telemetry telemetry;
    private double lastTime = 0;
    private double lastPos = 0;
    private double lastSpeed = 0;

    double totalTimeElapsed;
    double deltaV;
    double kP;
    double kI;
    double kD;
    double errorSum;
    double error;
    double output;

    public double speed;

    boolean started;

    public Shooter(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap);

        this.telemetry = telemetry;

        shooterMaster.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        shooterMaster.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterSlave.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void update(double power) {

        //Calculate change in time
        double currentTime = System.currentTimeMillis();
        double elapsedTime = 0;
        if(lastTime == 0) {
            elapsedTime = 0;
        }
        else {
            elapsedTime = currentTime - lastTime;
        }
        totalTimeElapsed = 0;
        totalTimeElapsed += elapsedTime;
        lastTime = System.currentTimeMillis();

        double changePos;
        if(lastPos == 0) changePos = 0;
        else changePos = shooterMaster.getCurrentPosition() - lastPos;
        lastPos = shooterMaster.getCurrentPosition();

        double actualSpeed = changePos / totalTimeElapsed;
        if(lastSpeed == 0) deltaV = 0;
        else deltaV = lastSpeed - actualSpeed;
        lastSpeed = changePos / totalTimeElapsed;


        error = power - actualSpeed;
        errorSum += error;
        kP = 1.62;
        kD = 0.2;

        output = power + (error * kP) + (deltaV * kD);

        shooterMaster.setPower(output);
        shooterSlave.setPower(output);

        speed = shooterMaster.getVelocity();
    }

    @Override
    public void disable() {
        shooterMaster.setPower(0);
        shooterSlave.setPower(0);
    }

    public void getTelemetry(boolean update) {
        telemetry.addData("kP", kP);
        telemetry.addData("kI", kI);
        telemetry.addData("kD", kD);
        telemetry.addData("Error", error);
        telemetry.addData("Error Sum", errorSum);
        telemetry.addData("Change in V", deltaV);
        telemetry.addData("Output", output);
        if(Math.abs(error) < 0.05) telemetry.addData("Ready", true);
        if(update) telemetry.update();
    }
}
