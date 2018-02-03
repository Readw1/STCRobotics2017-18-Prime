package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwareMap_Mechanum
{
    /* Public OpMode members. */
    public DcMotor  frontLeft       = null;
    public DcMotor  frontRight      = null;
    public DcMotor  backLeft        = null;
    public DcMotor  backRight       = null;
    public DcMotor  armMotor        = null;
    public DcMotor  extender        = null;
    public DcMotor moveArm          = null;


    public ColorSensor colorSensor  = null; //this is the ball sensor
    public ColorSensor botSense     = null; //this is the ground sensor
    //public ColorSensor color2     = null;

    public Servo leftExtend         = null;
    public Servo rightExtend        = null;
    public Servo theClaw            = null;
    public CRServo ben              = null;
    public Servo flicker            = null;
    //public OpticalDistanceSensor odsSensor = null;



    /* Local OpMode members. */
    com.qualcomm.robotcore.hardware.HardwareMap hwMap  = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareMap_Mechanum() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(com.qualcomm.robotcore.hardware.HardwareMap ahwMap) {
        // save reference to HW Map
        hwMap = ahwMap;

        // Define and Initialize Motors
        frontLeft = hwMap.dcMotor.get("frontLeft");
        frontRight = hwMap.dcMotor.get("frontRight");
        backLeft = hwMap.dcMotor.get("backLeft");
        backRight = hwMap.dcMotor.get("backRight");
        armMotor = hwMap.dcMotor.get("armMotor");
        extender = hwMap.dcMotor.get("extender");
        moveArm = hwMap.dcMotor.get("moveArm");

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);



        // Set all motors to zero power
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        armMotor.setPower(0);
        extender.setPower(0);
        moveArm.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        //initialize color sensors
        colorSensor = hwMap.colorSensor.get("colorSensor");
        colorSensor.enableLed(false);

        /*botSense = hwMap.colorSensor.get("botSense");
        botSense.enableLed(false);*/

        // servos boy
        theClaw = hwMap.servo.get("theClaw");
        theClaw.setPosition(0);

        flicker = hwMap.servo.get("flicker");
        flicker.setPosition(0);

        leftExtend = hwMap.servo.get("leftExtend");
        rightExtend = hwMap.servo.get("rightExtend");
        ben = hwMap.crservo.get("ben");
        leftExtend.setPosition(.25);
        rightExtend.setPosition(.25);
        ben.setPower(0);


        /*
        com.qualcomm.robotcore.hardware.I2cAddr color1Addr = new com.qualcomm.robotcore.hardware.I2cAddr(0x3c);
        colorSensor.setI2cAddress(color1Addr);

        com.qualcomm.robotcore.hardware.I2cAddr color2Addr = new com.qualcomm.robotcore.hardware.I2cAddr(0x3a);
        botSense.setI2cAddress(color2Addr);*/



    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs)  throws InterruptedException {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }

}