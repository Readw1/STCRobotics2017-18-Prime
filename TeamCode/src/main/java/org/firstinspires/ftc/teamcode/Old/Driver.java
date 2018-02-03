/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode.Old;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;


/**
 *
 */



public class Driver extends OpMode {

    /**
     * Make some objects
     */
    NewHardwareRegister robot = new NewHardwareRegister();
    public ElapsedTime runtime = new ElapsedTime();
    //SharedUtils shared = new SharedUtils(robot);

    /**
     * Make some variables
     */
    private static int      COUNTS_PER_MOTOR_REV = 420; //blaze it
    private static double   DRIVE_SPEED = 1.0;
    private boolean         flipperReady = true; // used for button press 'y' so that launchMotor goes back and forth
    private boolean         sweeperOff = false; // used for button press 'x' so that sweeperMotor can toggle on and off
    private double            numLoops = 0;
    private double          left;
    private double          right;
    private double          powerVal; // used when button 'A' pressed
    private boolean         opModeIsActive = false;
    private static double   SPEED_MULTIPLIER = 0.5; // master control over speed adjustment
    ColorSensor colorSensor;

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
        robot.gyro.calibrate();

        // make sure the gyro is calibrated
        while (robot.gyro.isCalibrating())  {
            robot.sleep(50);
        }

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();
        colorSensor.enableLed(false);

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

        numLoops++;

        left = 0;
        right = 0;
        powerVal = 0;

        /**
         * DRIVING
         * Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
         * Holding button 'A' syncs the speed of both wheels so it can be
         * driven straight at slower speeds.
         * All values are cut in half so that the robot can only go half of its max speed.
         */
        if (gamepad1.a) {

            // if only the left stick is moved, use that value for both motors
            if (gamepad1.left_stick_y == 0 && gamepad1.right_stick_y != 0) {
                powerVal = -gamepad1.right_stick_y;

            }

            // if only the right stick is moved, use that value for both motors
            else if (gamepad1.right_stick_y == 0 && gamepad1.left_stick_y != 0) {
                powerVal = -gamepad1.left_stick_y;
            }

            // if both sticks are moved, use the average of those two values
            else { powerVal = ((-gamepad1.left_stick_y + -gamepad1.right_stick_y) / 2); }

            // check if trigger is pressed, if it is, the following reverts changes
            // about to happen to powerVal by SPEED_MULTIPLIER in the line after this one
            if (gamepad1.right_trigger != 0) { powerVal = 1; }

            // if the trigger is not pressed, adjust the speed based on SPEED_MULTIPLIER
            else { powerVal *= SPEED_MULTIPLIER; }

            // set power when button 'A' pressed
            robot.leftMotor.setPower(powerVal);
            robot.rightMotor.setPower(powerVal);
        }

        // if button 'a' is not pressed, let wheels spin independently (standard)
        else {

            left = -gamepad1.left_stick_y;
            right = -gamepad1.right_stick_y;

            // check if trigger is pressed, if so set speed to max in forward direction
            if (gamepad1.right_trigger != 0) { left = 1; right = 1; }

            // if trigger not pressed, adjust power based on SPEED_MULTIPLIER
            else { left *= SPEED_MULTIPLIER; right *= SPEED_MULTIPLIER; }

            // set power when button 'a' not pressed
            robot.leftMotor.setPower(left);
            robot.rightMotor.setPower(right);
            robot.sweeperMotor.setPower(right);
        }

        /**
         * Button mapping
         */

        // push the right button on the beacon
        if (gamepad1.left_bumper)
        {
            robot.beaconServo.setPosition(.8);
            robot.sleep(500);
            robot.beaconServo.setPosition(.5);
        }

        // push the left button on the beacon
        if (gamepad1.right_bumper)
        {
            robot.beaconServo.setPosition(.2);
            robot.sleep(500);
            robot.beaconServo.setPosition(.5);
        }

        // launch one particle
        if (gamepad1.b) {
            flipper(1,-1,30.0);
        }

        // rotate flipper half a rotation back and forth
        if (gamepad1.y) {
            if(flipperReady) {
                flipper(1, 0.5, 30.0);
                flipperReady = !flipperReady;
            } else if(!flipperReady) {
                flipper(1, -0.5, 30.0);
                flipperReady = !flipperReady;
            }
        }

        // toggle sweeper
        if (gamepad1.x) {
            if (sweeperOff) {
                robot.sweeperMotor.setPower(1);
                robot.sleep(500);
                sweeperOff = !sweeperOff;
            } else if (!sweeperOff) {
                robot.sweeperMotor.setPower(0);
                robot.sleep(500);
                sweeperOff = !sweeperOff;
            }
        }

        // Send telemetry message to signify robot running;
        telemetry.addData("left",  "%.2f", left);
        telemetry.addData("right", "%.2f", right);
        telemetry.addData("powerVal", "%.2f", powerVal);
        telemetry.addData("numLoops (in thousands)", "%.2f", numLoops / 1000);
        telemetry.update();

        // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
        robot.sleep(40);

        /**
         * Test code for gyro
         */

        curResetState = (gamepad1.a && gamepad1.b);
        if (curResetState && !lastResetState)  {
            robot.gyro.resetZAxisIntegrator();
        }
        lastResetState = curResetState;

        // get the x, y, and z values (rate of change of angle).
        xVal = robot.gyro.rawX();
        yVal = robot.gyro.rawY();
        zVal = robot.gyro.rawZ();

        // get the heading info.
        // the Modern Robotics' gyro sensor keeps
        // track of the current heading for the Z axis only.
        heading = robot.gyro.getHeading();
        angleZ  = robot.gyro.getIntegratedZValue();

        telemetry.addData(">", "Press A & B to reset Heading.");
        telemetry.addData("0", "Heading %03d", heading);
        telemetry.addData("1", "Int. Ang. %03d", angleZ);
        telemetry.addData("2", "X av. %03d", xVal);
        telemetry.addData("3", "Y av. %03d", yVal);
        telemetry.addData("4", "Z av. %03d", zVal);
        telemetry.update();

    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        opModeIsActive = false;

    }

    /**
     * Method "flipper" rotates the launch motor based on the number of rotations requested
     * Has no effect on the sweeper, that must be activated separately
     * @param speed speed motor turns
     * @param rotations number of full rotations motor will make (0.5 for half rotation)
     * @param timeoutS seconds before the method times out and cancels
     * Re-written by Gunther
     */
    public void flipper(double speed, double rotations, double timeoutS)
    {
        int newTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive)
        {
            // Determine new target position, and pass to motor controller
            //newLeftTarget = robot.launchMotor.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            //newRightTarget = robot.sweeperMotor.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newTarget = robot.launchMotor.getCurrentPosition() + (int)(4*rotations * COUNTS_PER_MOTOR_REV);
            robot.launchMotor.setTargetPosition(newTarget);

            // Turn On RUN_TO_POSITION
            robot.launchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.launchMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and the motor is running.
            while (opModeIsActive &&
                    (runtime.seconds() < timeoutS) &&
                    robot.launchMotor.isBusy())
            {

                // Display data on the driver station.
                telemetry.addData("newTarget", "Running to %7d",newTarget);
                telemetry.addData("getCurrentPosition", "Running at %7d", robot.launchMotor.getCurrentPosition());
                telemetry.update();

            }

            // Stop all motion;
            robot.launchMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.launchMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // optional pause after each move
            robot.sleep(250);
        } else {
            telemetry.addLine("Error: OpMode not active");
        }
    }
}
