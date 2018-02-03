
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.Math;



@TeleOp(name="Template: Linear OpMode", group="Linear Opmode")  // @Autonomous(...) is the other common choice
@Disabled
public class Single extends LinearOpMode {

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

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();


        double right = 0;
        double left  = 0;
        double mult = 1.0;

        //change modes on the fly
        boolean clawArm = false;
        boolean grabArm = false;
        boolean wheels  = true;


        //sets initial position for servos
        robot.theClaw.setPosition(.25);
        robot.rightExtend.setPosition(0);
        robot.leftExtend.setPosition(0);
        robot.ben.setPower(0);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("left", left);
            telemetry.addData("right", right);
            telemetry.update();


            /**
             * A - Control the claw
             * X - Control the grabber arm
             * B - Driving
             * Y - Precision
             */

            //on the fly
            if (gamepad1.a) {
                clawArm = true;
                grabArm = false;
                wheels = false;
            } else if (gamepad1.x) {
                grabArm = true;
                clawArm = false;
                wheels = false;
            } else if (gamepad1.b) {
                wheels = true;
                clawArm = false;
                grabArm = false;
            }else if (gamepad1.y){
                clawArm = false;
                grabArm = false;
                wheels = false;
            }

            //Aidan's Death Arm
            if (clawArm) {
                if (gamepad1.right_stick_y > 0) {
                    robot.moveArm.setPower(-.6);

                } else if(gamepad1.right_stick_y < 0) {
                    robot.moveArm.setPower(.6);
                }else{
                    robot.moveArm.setPower(0);
                    robot.moveArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                }

                if (gamepad1.left_stick_y > 0 || gamepad1.left_stick_y < 0) {
                    robot.armMotor.setPower(gamepad1.left_stick_y);
                } else {
                    robot.armMotor.setPower(0);
                }

                if (gamepad1.right_bumper) {
                    robot.theClaw.setPosition(.944444);
                } else {
                    robot.theClaw.setPosition(0);
                }
            } else {
                robot.moveArm.setPower(0);
                robot.armMotor.setPower(0);
            }

            //Ben's shitty Arm
            if (grabArm) {
                if (gamepad1.right_stick_y > 0) {
                    robot.extender.setPower(gamepad1.right_stick_y*10);
                } else if(gamepad1.right_stick_y < 0) {
                    robot.extender.setPower(gamepad1.right_stick_y*10);
                }else{
                    robot.extender.setPower(0);
                }

                if (gamepad1.right_bumper) {
                    robot.leftExtend.setPosition(1);
                    robot.rightExtend.setPosition(1);
                } else {
                    robot.leftExtend.setPosition(0);
                    robot.rightExtend.setPosition(0);
                }

                if (gamepad1.dpad_up) {
                    robot.ben.setPower(1);
                } else if (gamepad1.dpad_down) {
                    robot.ben.setPower(-1);
                }else {
                    robot.ben.setPower(0);
                }
            } else {
                robot.extender.setPower(0);
            }


            if (wheels) {
                if (gamepad1.right_stick_y > 0 || gamepad1.right_stick_y < 0 || gamepad1.left_stick_y < 0
                        || gamepad1.left_stick_y > 0) {
                    right = -(gamepad1.right_stick_y / 2) * mult;
                    left = -(gamepad1.left_stick_y / 2) * mult;
                    if (right > 0) {
                        robot.backRight.setPower(right);
                        robot.frontRight.setPower(Math.exp(right));
                        if (left > 0) {
                            robot.backLeft.setPower(left);
                            robot.frontLeft.setPower(Math.exp(left));
                        } else if (left < 0) {
                            robot.backLeft.setPower(left);
                            robot.frontLeft.setPower(-Math.exp(left));
                        } else {
                            robot.backLeft.setPower(0);
                            robot.frontLeft.setPower(0);
                        }
                    } else if (right < 0) {
                        robot.backRight.setPower(right*2);
                        robot.frontRight.setPower(-Math.exp(right)*2);
                    if (left > 0) {
                        robot.backLeft.setPower(left);
                        robot.frontLeft.setPower(Math.exp(left));
                    } else if (left < 0) {
                        robot.backLeft.setPower(left*2);
                        robot.frontLeft.setPower(-Math.exp(left)*2);
                    } else {
                        robot.backLeft.setPower(0);
                        robot.frontLeft.setPower(0);
                    }
                    } else if (left > 0) {
                        robot.backLeft.setPower(left);
                        robot.frontLeft.setPower(Math.exp(left));
                        if (right > 0) {
                            robot.backRight.setPower(right);
                            robot.frontRight.setPower(Math.exp(right));
                        } else if (right < 0) {
                            robot.backRight.setPower(right);
                            robot.frontRight.setPower(-Math.exp(right));
                        } else {
                            robot.backRight.setPower(0);
                            robot.frontRight.setPower(0);
                        }
                    } else if (left < 0) {
                        robot.backLeft.setPower(left);
                        robot.frontLeft.setPower(-Math.exp(left));
                        if (right > 0) {
                            robot.backRight.setPower(right);
                            robot.frontRight.setPower(Math.exp(right));
                        } else if (right < 0) {
                            robot.backRight.setPower(right);
                            robot.frontRight.setPower(-Math.exp(right));
                        } else {
                            robot.backRight.setPower(0);
                            robot.frontRight.setPower(0);
                        }
                    } else {
                        robot.backLeft.setPower(0);
                        robot.backRight.setPower(0);
                        robot.frontLeft.setPower(0);
                        robot.frontRight.setPower(0);
                    }
                    if (gamepad1.right_stick_button) {
                        mult += .1;
                    }
                    if (gamepad1.left_stick_button) {
                        mult -= .1;
                    }
                }else if(gamepad1.right_bumper) {
                    robot.backLeft.setPower(-1);
                    robot.backRight.setPower(1);
                    robot.frontLeft.setPower(4);
                    robot.frontRight.setPower(-4);
                }else if(gamepad1.left_bumper){
                    robot.backLeft.setPower(1);
                    robot.backRight.setPower(-1);
                    robot.frontLeft.setPower(-4);
                    robot.frontRight.setPower(4);
                } else {
                    robot.backLeft.setPower(0);
                    robot.backRight.setPower(0);
                    robot.frontLeft.setPower(0);
                    robot.frontRight.setPower(0);
                }

                idle();

                }
            }
        }
    }
