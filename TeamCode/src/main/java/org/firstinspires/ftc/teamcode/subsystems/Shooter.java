package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.robot.RobotHardware;

import java.util.HashMap;
import java.util.Map;

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
    double errorSum1 = 0;
    double error1;
    double output1;

    double deltaV2;
    double kP2;
    double kI2;
    double kD2;
    double errorSum2 = 0;
    double error2;
    double output2;

    public double speed;

    public Shooter(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap);

        this.telemetry = telemetry;

        shooterMaster.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterSlave.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        shooterSlave.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterMaster.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterSlave.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }
//////////////Shooter
    public void update(double rpm) {

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

        //Change in position on first wheel
        double changePos1;
        if(lastPos1 == 0) changePos1 = 0;
        else changePos1 = shooterMaster.getCurrentPosition() - lastPos1;
        lastPos1 = shooterMaster.getCurrentPosition();

        //Calculate velocity, and change in velocity for first wheel
        //Velocity in ticks/second
        double actualSpeed1 = (changePos1 / elapsedTime) * 1000;
        //Velocity in rpm
        actualSpeed1 *= (60.0 / 28.0);
        if(lastSpeed1 == 0) deltaV1 = 0;
        else deltaV1 = lastSpeed1 - actualSpeed1;
        lastSpeed1 = changePos1 / totalTimeElapsed;

        //Change in position for second wheel
        double changePos2;
        if(lastPos2 == 0) changePos2 = 0;
        else changePos2 = shooterSlave.getCurrentPosition() - lastPos2;
        lastPos2 = shooterSlave.getCurrentPosition();

        //Calculate velocity, and change in velocity for second wheel
        //Velocity in ticks/second
        double actualSpeed2 = (changePos2 / elapsedTime) * 1000;
        //Velocity in rpm
        actualSpeed2 *= (60.0 / 28.0);
        if(lastSpeed2 == 0) deltaV2 = 0;
        else deltaV2 = lastSpeed2 - actualSpeed2;
        lastSpeed2 = changePos1 / totalTimeElapsed;

        error1 = rpm - actualSpeed1;
        if(Double.isNaN(errorSum1)) errorSum1 = error1;
        else errorSum1 += error1;
        kP1 = 0.0032;
        kD1 = 0;

        error2 = rpm - actualSpeed2;
        if(Double.isNaN(errorSum2)) errorSum2 = error2;
        else errorSum2 += error2;
        kP2 = 0.0032;
        kD2 = 0;

        output1 = (error1 * kP1) + (deltaV1 * kD1);
        output2 = (error2 * kP2) + (deltaV2 * kD2);

        shooterMaster.setPower(output1);
        shooterSlave.setPower(output2);

        speed = (actualSpeed1 + actualSpeed2) / 2;

        telemetry.addData("RPM", speed);
        telemetry.addData("Sum1", errorSum1);
        telemetry.addData("Sum 2", errorSum2);
    }

    public void rawControl(double power) {
        shooterMaster.setPower(power);
        shooterSlave.setPower(power);

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

        //Change in position on first wheel
        double changePos1;
        if(lastPos1 == 0) changePos1 = 0;
        else changePos1 = shooterMaster.getCurrentPosition() - lastPos1;
        lastPos1 = shooterMaster.getCurrentPosition();

        //Calculate velocity, and change in velocity for first wheel
        //Velocity in ticks/second
        double actualSpeed1 = (changePos1 / elapsedTime) * 1000;
        //Velocity in rpm
        actualSpeed1 *= (60.0 / 28.0);
        if(lastSpeed1 == 0) deltaV1 = 0;
        else deltaV1 = lastSpeed1 - actualSpeed1;
        lastSpeed1 = changePos1 / totalTimeElapsed;

        //Change in position for second wheel
        double changePos2;
        if(lastPos2 == 0) changePos2 = 0;
        else changePos2 = shooterSlave.getCurrentPosition() - lastPos2;
        lastPos2 = shooterSlave.getCurrentPosition();

        //Calculate velocity, and change in velocity for second wheel
        //Velocity in ticks/second
        double actualSpeed2 = (changePos2 / elapsedTime) * 1000;
        //Velocity in rpm
        actualSpeed2 *= (60.0 / 28.0);
        if(lastSpeed2 == 0) deltaV2 = 0;
        else deltaV2 = lastSpeed2 - actualSpeed2;
        lastSpeed2 = changePos1 / totalTimeElapsed;

        speed = (actualSpeed1 + actualSpeed2) / 2;
    }

    @Override
    public void disable() {
        shooterMaster.setPower(0);
        shooterSlave.setPower(0);
    }

    public void getTelemetry(boolean update) {
        telemetry.addData("Error", error1);
        telemetry.addData("Error Sum", errorSum1);
        telemetry.addData("Change in V", deltaV1);
        telemetry.addData("Output", output1);
        telemetry.addData("vel", speed);
        if(Math.abs(error1) < 100) telemetry.addData("Ready", true);

        telemetry.addData("Error", error2);
        telemetry.addData("Error Sum", errorSum2);
        telemetry.addData("Change in V", deltaV2);
        telemetry.addData("Output", output2);
        if(Math.abs(error2) < 100) telemetry.addData("Ready", true);

        if(update) telemetry.update();
    }
}
