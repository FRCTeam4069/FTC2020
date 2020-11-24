package org.firstinspires.ftc.teamcode.subsystems.robot;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class RobotHardware {

    //Drivetrain
    protected DcMotorEx frontLeft;
    protected DcMotorEx backLeft;
    protected DcMotorEx frontRight;
    protected DcMotorEx backRight;
    protected NavxMicroNavigationSensor navx;

    //Intake
    protected DcMotorEx intakeMotor;

    //WobbleGoalClamp
    protected CRServo wobbleMaster;
    protected CRServo wobbleSlave;

    //Shooter
    protected DcMotorEx shooterMaster;
    protected DcMotorEx shooterSlave;

    //Passthrough
    protected CRServo passthroughMotor;

    //Odometry
    protected DcMotorEx yEncoderRight;

    public RobotHardware(HardwareMap hardwareMap) {

        //Drivetrain
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        navx = hardwareMap.get(NavxMicroNavigationSensor.class, "navx");

        //Intake
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");

        //Passthrough
        passthroughMotor = hardwareMap.get(CRServo.class, "passthroughMotor");

        //WobbleGoalClamp
        //master = hardwareMap.get(CRServo.class, "master");
        //slave = hardwareMap.get(CRServo.class, "slave");

        //Shooter
        shooterMaster = hardwareMap.get(DcMotorEx.class, "shooterMaster");
        shooterSlave = hardwareMap.get(DcMotorEx.class, "shooterSlave");

        //Odometry
        yEncoderRight = hardwareMap.get(DcMotorEx.class, "yEncoderRight");
    }


    public abstract void disable();
}
