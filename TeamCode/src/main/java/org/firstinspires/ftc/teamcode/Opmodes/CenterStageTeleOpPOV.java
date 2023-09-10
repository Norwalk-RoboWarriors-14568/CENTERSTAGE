package org.firstinspires.ftc.teamcode.Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math;
@TeleOp(name = "POV Drive")

public class CenterStageTeleOpPOV extends OpMode {
    private DcMotor fl, bl, fr, br;
    private boolean mecanumDriveMode = true;
    private float mecanumStrafe = 0;

    @Override
    public void init() {
        fl = hardwareMap.dcMotor.get("leftFront");
        bl = hardwareMap.dcMotor.get("leftRear");
        fr = hardwareMap.dcMotor.get("rightFront");
        br = hardwareMap.dcMotor.get("rightRear");

        fr.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        telemetry.addLine("Init Opmode");
    }

    @Override
    public void loop(){
        mecanumStrafe = gamepad1.left_stick_x;


        if ((gamepad1.left_stick_x) > 0.15){
            fl.setPower((-gamepad1.left_stick_x) / 2);
            bl.setPower((gamepad1.left_stick_x) / 2);
            fr.setPower((gamepad1.left_stick_x) / 2);
            br.setPower((-gamepad1.left_stick_x) / 2);
        } else {
            fl.setPower((gamepad1.left_stick_y + -mecanumStrafe) / 2);
            bl.setPower((gamepad1.left_stick_y + mecanumStrafe) / 2);
            fr.setPower((gamepad1.left_stick_y + mecanumStrafe) / 2);
            br.setPower((gamepad1.left_stick_y + -mecanumStrafe) / 2);
        }

        if ((gamepad1.right_stick_x) > 0.2){
            fl.setPower(-gamepad1.right_stick_x *2);
            bl.setPower(-gamepad1.right_stick_x  *2);
            fr.setPower(gamepad1.right_stick_x * 2);
            br.setPower(gamepad1.right_stick_x  *2);
        }

    }
    @Override
    public void stop() {
        telemetry.clearAll();
        telemetry.addLine("Stopped");
    }

    public void drive(double left, double right) {
        fl.setPower(left);
        bl.setPower(left);
        fr.setPower(right);
        br.setPower(right);

    }

}
