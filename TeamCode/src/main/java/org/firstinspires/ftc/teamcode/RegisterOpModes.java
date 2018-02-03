
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

import org.firstinspires.ftc.teamcode.Crap.K9botTeleopTank_Linear;
import org.firstinspires.ftc.teamcode.Crap.SensorMRColor;


public class RegisterOpModes {

    @OpModeRegistrar
    public static void registerMyOpModes(OpModeManager manager) {
        //manager.register("TestWheels",TestWheels.class);
        manager.register("Autonomous Red", Auto_Mechanum_Red.class);
        manager.register("Autonomous Blue", Auto_Mechanum_Blue.class);
        manager.register("TestColor", TestColor.class);
        //manager.register("Driver Controlled", Driver_Mechanum_TankMode.class);
        //manager.register("Single Driver", Single.class);
        manager.register("Dual Driver", Dual.class);


        //manager.register("color sensor", SensorMRColor.class);


    }
}
