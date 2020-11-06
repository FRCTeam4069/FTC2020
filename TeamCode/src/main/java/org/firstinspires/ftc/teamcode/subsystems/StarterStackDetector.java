package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class StarterStackDetector {

    Telemetry telemetry;

    //TF Detector assets
    private static final String TF_DECTECTOR_MODEL_ASSETS = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";

    //Vuforia Asset
    private static final String VUFORIA_KEY = "AUdbl2P/////AAABmWYe3kyuhU8cm/rBVoVMzVotL3p0vfzGnir9dscLSA2K03xgWC6VBG08gzHvNXA4ac6axF+EINaVBUMwEcoY6tNQX8bSAY778bfzT6WQzilY8UNOo+Pa0oBZ6Ut5B0piq7wcLLUSMjXv2iX0yjcioMJ9Lqoo5tXAbaL2x3sn+zkioNVm1zl1a1fgzXmtxilRTrK4MN7kYFoO0yfIlq5YDKiNU5Hzrbr1wzdhSp2+FoQekvlCfMZzi0YjICtfooi5m70RuTJcn7aRNHHMyuKSSmqzxe4W4j0cLR+Kw4yFVxue75GDxMIh1Ot7zi1blRojFBZ1oY5s84aQvCZ5Xtsf+COCA0MP1GQwcw9utlH9Ln2o";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfDetector;

    //Global list of returned recogn
    private List<Recognition> updatedRecognitions;

    // Function to initialize vuforia detector (called in init)
    private void initVuforia(HardwareMap hardwareMap) {

        // Create necessary params for vuforia init
        VuforiaLocalizer.Parameters vuforiaParam = new VuforiaLocalizer.Parameters();
        vuforiaParam.vuforiaLicenseKey = VUFORIA_KEY;
        vuforiaParam.cameraName = hardwareMap.get(WebcamName.class, "webcam");

        // Create vuforia
        vuforia = ClassFactory.getInstance().createVuforia(vuforiaParam);
        telemetry.addData("Vuforia", "Initialized");
        telemetry.update();
    }

    // Function to initialize tensorFlow lite detector
    private void initTFDetector(HardwareMap hardwareMap) {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        // Create necessary params for tf init
        TFObjectDetector.Parameters tfParam = new TFObjectDetector.Parameters();
        tfParam.minResultConfidence = 0.75f;

        // Create tf detector and add model assets
        tfDetector = ClassFactory.getInstance().createTFObjectDetector(tfParam, vuforia);
        tfDetector.loadModelFromAsset(TF_DECTECTOR_MODEL_ASSETS, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        telemetry.addData("TF Detector", "Initialized");
    }

    public StarterStackDetector(HardwareMap hardwareMap, Telemetry telemetry) {

        //Init vuforia and tfDetector, give access to telemetry
        this.telemetry = telemetry;
        initVuforia(hardwareMap);
        initTFDetector(hardwareMap);
        tfDetector.activate();
    }

    private void updateRecognitions() {

        //Update list of updated recognitions
        updatedRecognitions = tfDetector.getUpdatedRecognitions();
    }

    public void printCurrentVals() {

        updateRecognitions();

        //Check if list is null or empty
        if(updatedRecognitions != null) {

            //Add data on recognition
            if(updatedRecognitions.size() != 0) {
                telemetry.addData("Num Objects", updatedRecognitions.size());
                Recognition starterStack = updatedRecognitions.get(0);

                telemetry.addData("Confidence", starterStack.getConfidence());
                telemetry.addData("Top", starterStack.getTop());
                telemetry.addData("Bottom", starterStack.getBottom());
                telemetry.addData("Left", starterStack.getLeft());
                telemetry.addData("Right", starterStack.getRight());
                telemetry.addData("Height", starterStack.getHeight());
                telemetry.addData("Width", starterStack.getWidth());
            }
            else {

                //If the list is empty return the number of recognitions
                telemetry.addData("Num Objects Detected", updatedRecognitions.size());
            }
        }
    }

    private int lastReturn = 1000;
    public int getStarterStackSize(int fourStackHeight) {

        updateRecognitions();

        //Check if list is null or empty
        if (updatedRecognitions != null) {
            telemetry.addData("Num Objects", updatedRecognitions.size());
            if (updatedRecognitions.size() != 0) {
                Recognition starterStack = updatedRecognitions.get(0);

                //Adding telemetry recognition data
                telemetry.addData("Confidence", starterStack.getConfidence());
                telemetry.addData("Top", starterStack.getTop());
                telemetry.addData("Bottom", starterStack.getBottom());
                telemetry.addData("Left", starterStack.getLeft());
                telemetry.addData("Right", starterStack.getRight());
                telemetry.addData("Height", starterStack.getHeight());
                telemetry.addData("Width", starterStack.getWidth());

                //Returning starter stack based on hard-coded height values
                if ((starterStack.getHeight() > (fourStackHeight - 10)) && (starterStack.getHeight() < (fourStackHeight + 10))) {
                    lastReturn = 4;
                    telemetry.addData("Starter stack", 4);
                    return 4;
                }
                else {
                    lastReturn = 1;
                    telemetry.addData("Starter stack", 1);
                    return 1;
                }
            }
            else {
                lastReturn = 0;
                telemetry.addData("Starter stack", 0);
                return 0;
            }
        }
        else {
            return lastReturn;
        }
    }
}
