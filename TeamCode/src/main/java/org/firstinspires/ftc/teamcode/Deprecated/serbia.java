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

package org.firstinspires.ftc.teamcode.Deprecated;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Old.HardwareRegister;

/**
 * This OpMode uses the common HardwareK9bot class to define the devices on the robot.
 * All device access is managed through the HardwareK9bot class. (See this class for device names)
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a basic Tank Drive Teleop for the K9 bot
 * It raises and lowers the arm using the Gampad Y and A buttons respectively.
 * It also opens and closes the claw slowly using the X and B buttons.
 *
 * Note: the configuration of the servos is such that
 * as the arm servo approaches 0, the arm position moves up (away from the floor).
 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */


public class serbia extends LinearOpMode {

    // Declare OpMode members
    HardwareRegister robot = new HardwareRegister();

    /**
     * Initialize encoder variables
     */
    private ElapsedTime runtime = new ElapsedTime();
    static final double     COUNTS_PER_MOTOR_REV    = 420 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;    // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;    // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159);
    static final double     DRIVE_SPEED             = 0.3;
    static final double     TURN_SPEED              = 0.2;

    /**
     * Misc variables for toggle buttons
     */
    private boolean flipperReady = true; // used for button press 'y' so that launchMotor goes back and forth
    private boolean sweeperOff = false; // used for button press 'x' so that sweeperMotor can toggle on and off
    private boolean ballServoRetracted = true;

    /**
     * Initialize servo variables
     */
    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position
    double  position = (MAX_POS - MIN_POS) / 2; // Start at halfway position
    boolean rampUp = true;



    @Override
    public void runOpMode() throws InterruptedException
    {
        double left;
        double right;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {

            // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
            left = -gamepad1.left_stick_y; // change this
            right = -gamepad1.right_stick_y;
            robot.leftMotor.setPower(left);
            robot.rightMotor.setPower(right);
            // Send telemetry message to signify robot running;

            telemetry.addData("left",  "%.2f", left);
            telemetry.addData("right", "%.2f", right);
            telemetry.update();
            //Running REJII check... :VALID:

            // launch_motor goes 1 full rotation, launch one ball
            if (gamepad1.b) {
                flipper(DRIVE_SPEED,-1,30.0);
            }

            // launch_motor rotates a half rotation in positive direction the first time,
            // then goes half rotation in negative direction second time
            if (gamepad1.y) {
                if(flipperReady) {
                    flipper(DRIVE_SPEED, 0.5, 30.0);
                    flipperReady = !flipperReady;
                } else if(!flipperReady) {
                    flipper(DRIVE_SPEED, -0.5, 30.0);
                    flipperReady = !flipperReady;
                }
            }

            // toggle sweeper on or off
            if (gamepad1.x) {
                if (sweeperOff) {
                    robot.sweeperMotor.setPower(1);
                    sweeperOff = !sweeperOff;
                } else if (!sweeperOff) {
                    robot.sweeperMotor.setPower(0);
                    sweeperOff = !sweeperOff;
                }
            }

            // toggle servo back and forth
            if (gamepad1.left_bumper) {
                if (robot.beaconServo.getPosition() == 0.75) {
                    robot.beaconServo.setPosition(0.25);
                } else {
                    robot.beaconServo.setPosition(0.75);
                }
            }

            /*
            if (gamepad1.a)
            {
                if(ballServoRetracted) {
                    robot.leftBallServo.setPosition(100);
                    ballServoRetracted = !ballServoRetracted;
                }
                else if(!ballServoRetracted) {
                    robot.leftBallServo.setPosition(0);
                    ballServoRetracted = !ballServoRetracted;

                }


            }
            */

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop

        }
    }




    /**
     * Method "flipper" rotates the launch motor based on the number of rotations requested
     * Has no effect on the sweeper, that must be activated separately
     * @param speed speed motor turns
     * @param rotations number of full rotations motor will make (0.5 for half rotation)
     * @param timeoutS seconds before the method times out and cancels
     * @throws InterruptedException
     * Re-written by Gunther
     */
    public void flipper(double speed, double rotations, double timeoutS)
            throws InterruptedException
    {
        int newTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive())
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
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    robot.launchMotor.isBusy())
            {

                // Display data on the driver station.
                telemetry.addData("newTarget", "Running to %7d",newTarget);
                telemetry.addData("getCurrentPosition", "Running at %7d", robot.launchMotor.getCurrentPosition());
                telemetry.update();

                // Allow time for other processes to run.
                idle();
            }

            // Stop all motion;
            robot.launchMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.launchMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // optional pause after each move
            sleep(250);
        }
    }
}