package org.firstinspires.ftc.teamcode.Crap;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardwareMap_Mechanum;

import java.util.ArrayList;
/**
 * Created by gta on 12/20/16.
 */

public class Driver_Mechanum_CrazyArrayJon2 extends OpMode {

    /**
     * Make some objects
     */
    HardwareMap_Mechanum robot = new HardwareMap_Mechanum();
    public ElapsedTime runtime = new ElapsedTime();
    //SharedUtils shared = new SharedUtils(robot);

    /**
     * Make some variables
     */
    private static int COUNTS_PER_MOTOR_REV = 420; //blaze it
    private static final int COUNTS_PER_INCH = 420;//everyday
    private static double DRIVE_SPEED = 1.0;
    private boolean flipperReady = true; // used for button press 'y' so that launchMotor goes back and forth
    private boolean sweeperOff = false; // used for button press 'x' so that sweeperMotor can toggle on and off
    private double numLoops = 0;
    private double left;
    private double right;
    private double powerVal; // used when button 'A' pressed
    private boolean opModeIsActive = false;
    private static double SPEED_MULTIPLIER = 0.5; // master control over speed adjustment
    ColorSensor colorSensor;
    private double[][] speedModes;
    private boolean[][] dirModes;
    private int currentDriveMode;
    private

    /**
     * Variables for gyro
     */

    boolean curResetState = false;
    boolean lastResetState = false;
    int xVal, yVal, zVal;
    int heading; // Gyro integrated heading
    int angleZ;

    /**
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // calibrate gyro
        telemetry.addData(">", "Gyro Calibrating. Do Not move!");
        telemetry.update();


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();
        speedModes = new double[3][3];
        dirModes = new boolean[3][3];

        //initialize forward drive mode
        for (int j = 1; j<5; j++){
            speedModes[0][j] = 0.5;
        }
        //initialize right drive mode
        speedModes[1][0] = -0.5; speedModes[1][0] = 0.5; speedModes[1][0] = 0.5; speedModes[1][0] = -0.5;

        //initialize backwards drive mode
        for (int j = 1; j<5; j++){
            speedModes[2][j] = -0.5;
        }

        //initialize left drive mode
        speedModes[3][0] = 0.5; speedModes[1][0] = -0.5; speedModes[1][0] = -0.5; speedModes[1][0] = 0.5;







    }

    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {

    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        opModeIsActive = true;


    }


    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
    //turn 90 degrees to the right
        if (gamepad1.a)
        {

        }





    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        opModeIsActive = false;

    }


    public void encoderDrive(double speed,
                             double leftInches, double rightInches, double bLeftInches, double bRightInches,
                             double timeoutS) throws InterruptedException {
        int newLeftTarget;
        int newRightTarget;
        int newbLeftTarget;
        int newbRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.frontLeft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.frontRight.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newbLeftTarget = robot.backLeft.getCurrentPosition() + (int)(bLeftInches * COUNTS_PER_INCH);
            newbRightTarget = robot.backRight.getCurrentPosition() + (int)(bRightInches * COUNTS_PER_INCH);


            robot.frontRight.setTargetPosition(newRightTarget);
            robot.frontLeft.setTargetPosition(newLeftTarget);
            robot.backRight.setTargetPosition(newbRightTarget);
            robot.backLeft.setTargetPosition(newbLeftTarget);

            // Turn On RUN_TO_POSITION
            robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            runtime.reset();
            robot.frontLeft.setPower(Math.abs(speed));
            robot.frontRight.setPower(Math.abs(speed));
            robot.backLeft.setPower(Math.abs(speed));
            robot.backRight.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.backRight.isBusy() && robot.backLeft.isBusy() && robot.frontLeft.isBusy() && robot.frontRight.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.frontLeft.getCurrentPosition(),
                        robot.frontRight.getCurrentPosition());
                telemetry.update();

                // Allow time for other processes to run.
               // idle();
            }

            // Stop all motion;
            robot.frontRight.setPower(0);
            robot.frontLeft.setPower(0);
            robot.backRight.setPower(0);
            robot.backLeft.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // Added by Gunther, should reset encoder values
            robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            //sleep(250);   // optional pause after each move
        }



    }

}


