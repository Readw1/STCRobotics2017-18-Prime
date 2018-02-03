
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;



@TeleOp(name="Template: Linear OpMode", group="Linear Opmode")  // @Autonomous(...) is the other common choice
@Disabled
public class Driver_Mechanum_TankMode extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    HardwareMap_Mechanum robot = new HardwareMap_Mechanum();
    // DcMotor leftMotor = null;
    // DcMotor rightMotor = null;

    private static double DRIVE_SPEED = 0.25; // Standard drive speed

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot.init(hardwareMap);


        // leftMotor  = hardwareMap.dcMotor.get("left motor");
        // rightMotor = hardwareMap.dcMotor.get("right motor");

        // eg: Set the drive motor directions:
        // "Reverse" the motor that runs backwards when connected directly to the battery
        // leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        // rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        double left = 0;
        double right = 0;
        double mult =1.0;

        //robot.theClaw.setPosition(1);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("left", left);
            telemetry.addData("right", right);

            telemetry.update();



            // Strafe left
            if (gamepad1.b) {
                robot.frontLeft.setPower(-1);
                robot.frontRight.setPower(1);
                robot.backLeft.setPower(1);
                robot.backRight.setPower(-1);
            }

            // Strafe right
            else if (gamepad1.x) {
                robot.frontLeft.setPower(1);
                robot.frontRight.setPower(-1);
                robot.backLeft.setPower(-1);
                robot.backRight.setPower(1);
            }

            // Standard tank driving
            else {

                right = (gamepad1.right_stick_y / 2)*mult;
                left = (gamepad1.left_stick_y / 2)*mult;

                robot.backLeft.setPower(-left);
                robot.frontLeft.setPower(-left);

                robot.backRight.setPower(-right);
                robot.frontRight.setPower(-right);
            }

            if (gamepad1.right_stick_button)
            {
                mult += .1;

            }
            if (gamepad1.left_stick_button)
            {
                mult -= .1;
            }

            /*

            if (gamepad1.a) {
                robot.armMotor.setPower(.25);

            }
            if (!gamepad1.a) {
                robot.armMotor.setPower(0);
            }
            if (gamepad1.y){

                robot.theClaw.setPosition(0);
            }
            else if (!gamepad1.y) {
                robot.theClaw.setPosition(1);
            }*/



            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop

        }
    }
}
