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
package org.firstinspires.ftc.teamcode.Crap;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.HardwareMap_Mechanum;

import java.util.ArrayList;

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

public class K9botTeleopTank_Linear extends LinearOpMode {
    private double[][] speedModes;
    private boolean[][] dirModes;
    private int currentDriveMode;
    private ArrayList<DcMotor> geg;


    /* Declare OpMode members. */
    HardwareMap_Mechanum robot           = new HardwareMap_Mechanum();              // Use a K9'shardware


    public int wrap(int numWraps, int wraPt, int topOut)
    {
        for(int j=0; j<numWraps; j++)
        {
            if(wraPt==topOut)
            {
                wraPt = 0;
                continue;
            }
            else
            {
                wraPt++;
            }
        }
        return wraPt;
    }

    public void runOpMode() throws InterruptedException {
        double left;
        double right;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);



        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        speedModes = new double[4][4];

        //initialize forward drive mode
        for (int j = 1; j<5; j++){
            speedModes[0][j] = 0.5;
        }
        //initialize right drive mode
        speedModes[1][0] = -0.5; speedModes[1][1] = 0.5; speedModes[1][2] = 0.5; speedModes[1][3] = -0.5;

        //initialize backwards drive mode
        for (int j = 1; j<5; j++){
            speedModes[2][j] = -0.5;
        }

        //initialize left drive mode
        speedModes[3][0] = 0.5; speedModes[3][1] = -0.5; speedModes[3][2] = -0.5; speedModes[3][3] = 0.5;
        //initialize motor array
        geg.add(robot.frontLeft);
        geg.add(robot.frontRight);
        geg.add(robot.backLeft);
        geg.add(robot.backRight);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if(gamepad1.a)
            {
                //turn robot 90 degrees to the right (some crap)
                currentDriveMode = wrap(1, currentDriveMode, 3);



            }
            if(gamepad1.dpad_up)
            {
                for(int j = 0; j<4; j++)
                {
                    geg.get(j).setPower(speedModes[currentDriveMode][j]);
                }
            }
            if (gamepad1.dpad_left)
            {
                int leftMode = wrap(1, currentDriveMode, 3);
                for(int j = 0; j<4; j++)
                {
                    geg.get(j).setPower(speedModes[leftMode][j]);
                }
            }
            if (gamepad1.dpad_down)
            {
                int downMode = wrap(2, currentDriveMode, 3);
                for(int j = 0; j<4; j++)
                {
                    geg.get(j).setPower(speedModes[downMode][j]);
                }
            }
            if (gamepad1.dpad_right)
            {
                int rightMode = wrap(3, currentDriveMode, 3);
                for(int j = 0; j<4; j++)
                {
                    geg.get(j).setPower(speedModes[rightMode][j]);
                }
            }










            }





            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }
    }


