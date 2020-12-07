package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.robot.RobotHardware;

public class Shooter extends RobotHardware {

    Telemetry telemetry;
    private double lastTime = 0;
    private double lastPos1 = 0;
    private double lastSpeed1 = 0;

    private double lastPos2 = 0;
    private double lastSpeed2 = 0;

    double totalTimeElapsed;
    double deltaV1;
    double kP1;
    double kI1;
    double kD1;
    double errorSum1;
    double error1;
    double output1;

    double deltaV2;
    double kP2;
    double kI2;
    double kD2;
    double errorSum2;
    double error2;
    double output2;

    public double speed;

    boolean started;

    public Shooter(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap);

        this.telemetry = telemetry;

        shooterMaster.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterSlave.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        shooterSlave.setDirection(DcMotorSimple.Direction.REVERSE);

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

        double changePos1;
        if(lastPos1 == 0) changePos1 = 0;
        else changePos1 = shooterMaster.getCurrentPosition() - lastPos1;
        lastPos1 = shooterMaster.getCurrentPosition();

        double actualSpeed1 = changePos1 / totalTimeElapsed;
        if(lastSpeed1 == 0) deltaV1 = 0;
        else deltaV1 = lastSpeed1 - actualSpeed1;
        lastSpeed1 = changePos1 / totalTimeElapsed;

        double changePos2;
        if(lastPos2 == 0) changePos2 = 0;
        else changePos2 = shooterSlave.getCurrentPosition() - lastPos2;
        lastPos2 = shooterSlave.getCurrentPosition();

        double actualSpeed2 = changePos2 / totalTimeElapsed;
        if(lastSpeed2 == 0) deltaV2 = 0;
        else deltaV2 = lastSpeed2 - actualSpeed2;
        lastSpeed2 = changePos1 / totalTimeElapsed;

        error1 = power - actualSpeed1;
        errorSum1 += error1;
        kP1 = 0.4;
        kD1 = 0;

        error2 = power - actualSpeed2;
        errorSum2 += error2;
        kP2 = 0.4;
        kD2 = 0;

        output1 = power + (error1 * kP1) + (deltaV1 * kD1);
        output2 = power + (error2 * kP2) + (deltaV2 * kD2);

        shooterMaster.setPower(output1);
        shooterSlave.setPower(output2);

        speed = shooterMaster.getVelocity();
    }

    @Override
    public void disable() {
        shooterMaster.setPower(0);
        shooterSlave.setPower(0);
    }

    public void getTelemetry(boolean update) {
        telemetry.addData("kP", kP1);
        telemetry.addData("kI", kI1);
        telemetry.addData("kD", kD1);
        telemetry.addData("Error", error1);
        telemetry.addData("Error Sum", errorSum1);
        telemetry.addData("Change in V", deltaV1);
        telemetry.addData("Output", output1);
        if(Math.abs(error1) < 0.05) telemetry.addData("Ready", true);

        telemetry.addData("kP", kP2);
        telemetry.addData("kI", kI2);
        telemetry.addData("kD", kD2);
        telemetry.addData("Error", error2);
        telemetry.addData("Error Sum", errorSum2);
        telemetry.addData("Change in V", deltaV2);
        telemetry.addData("Output", output2);
        if(Math.abs(error2) < 0.05) telemetry.addData("Ready", true);

        if(update) telemetry.update();
    }
}
