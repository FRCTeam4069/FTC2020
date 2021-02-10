package org.firstinspires.ftc.teamcode.subsystems.robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class RobotHardware {

    //Drivetrain
    protected DcMotorEx frontLeft;
    protected DcMotorEx backLeft;
    protected DcMotorEx frontRight;
    protected DcMotorEx backRight;
    protected NavxMicroNavigationSensor navx;
    protected BNO055IMU imu;

    //Intake
    protected DcMotorEx intakeMotor;
    protected DcMotorEx passthroughMotor;

    //WobbleGoalClamp
    protected CRServo wobbleMaster;
    protected CRServo wobbleSlave;
    protected CRServo left;
    protected CRServo right;
    protected TouchSensor top;
    protected TouchSensor bottom;


    //Shooter
    protected DcMotorEx shooterMaster;
    protected DcMotorEx shooterSlave;
    protected ColorSensor colourSensor;

    protected RevBlinkinLedDriver blinkin;

    public RobotHardware(HardwareMap hardwareMap) {

        //Drivetrain
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        navx = hardwareMap.get(NavxMicroNavigationSensor.class, "navx");

        //Intake
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        passthroughMotor = hardwareMap.get(DcMotorEx.class, "passthroughMotor");

        //WobbleGoalClamp
        wobbleMaster = hardwareMap.get(CRServo.class, "master");
        wobbleSlave = hardwareMap.get(CRServo.class, "slave");
        wobbleSlave.setDirection(DcMotorSimple.Direction.REVERSE);
        left = hardwareMap.get(CRServo.class, "left");
        right = hardwareMap.get(CRServo.class, "right");
        top = hardwareMap.get(TouchSensor.class, "top");
        bottom = hardwareMap.get(TouchSensor.class, "bottom");

        //Shooter
        shooterMaster = hardwareMap.get(DcMotorEx.class, "shooterMaster");
        shooterSlave = hardwareMap.get(DcMotorEx.class, "shooterSlave");
        shooterMaster.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterSlave.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        colourSensor = hardwareMap.get(ColorSensor.class, "colourSensor");

        blinkin = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

        imu = hardwareMap.get(BNO055IMU.class, "imu");
    }


    public abstract void disable();
}
