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

import org.firstinspires.ftc.teamcode.Old.NewHardwareRegister;

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


public class newdrivercontrolled_mod extends LinearOpMode {

    /* Declare OpMode members. */
    NewHardwareRegister robot = new NewHardwareRegister();
    //SharedUtils shared = new SharedUtils(robot);

    /**
     * Make some variables
     */
    final static int COUNTS_PER_MOTOR_REV = 420; //blaze it
    final static double DRIVE_SPEED = 1.0;
    protected ElapsedTime runtime = new ElapsedTime();
    private boolean flipperReady = true; // used for button press 'y' so that launchMotor goes back and forth
    private boolean sweeperOff = false; // used for button press 'x' so that sweeperMotor can toggle on and off

    public newdrivercontrolled_mod() {

    }

    @Override
    public void runOpMode() throws InterruptedException {
        double left;
        double right;
        double powerVal; // used when button 'A' pressed

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
        // previously was just "while (opModeIsActive())"
        for (double numLoops = 0; opModeIsActive(); numLoops++) {

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
                if (gamepad1.left_stick_y == 0) {
                    powerVal = -gamepad1.right_stick_y / 2; // cuts value in half

                }
                
                // if only the right stick is moved, use that value for both motors
                else if (gamepad1.right_stick_y == 0) {
                    powerVal = -gamepad1.left_stick_y / 2; // cuts value in half
                }
                
                // if both sticks are moved, use the average of those two values
                else {
                    powerVal = ((-gamepad1.left_stick_y + -gamepad1.right_stick_y) / 2) / 2; // cuts value in half after averaging
                }

                // set power when button 'A' pressed
                if (gamepad1.right_trigger != 0) { powerVal *= 2; } // check if trigger is pressed to double speed
                robot.leftMotor.setPower(powerVal);
                robot.rightMotor.setPower(powerVal);
            }
            
            // if button 'a' is not pressed, let wheels spin independently (standard)
            else {
                left = -gamepad1.left_stick_y;
                right = -gamepad1.right_stick_y;

                if (gamepad1.right_trigger != 0) { left *= 2; right *= 2; } // check if trigger is pressed to double speed

                robot.leftMotor.setPower(left / 2);
                robot.rightMotor.setPower(right / 2);
            }

            /**
             * Button mapping
             */

            // push the right button on the beacon
            if (gamepad1.left_bumper)
            {
                robot.beaconServo.setPosition(.8);
                robot.waitForTick(500);
                robot.beaconServo.setPosition(.5);
            }

            // push the left button on the beacon
            if (gamepad1.right_bumper)
            {
                robot.beaconServo.setPosition(.2);
                robot.waitForTick(500);
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
                    sleep(500);
                    sweeperOff = !sweeperOff;
                } else if (!sweeperOff) {
                    robot.sweeperMotor.setPower(0);
                    sleep(500);
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
