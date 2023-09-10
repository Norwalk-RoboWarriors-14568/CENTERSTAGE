package org.firstinspires.ftc.teamcode.opmodesCurrent;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import static java.lang.Math.abs;

@TeleOp(name = "TeleOp-4")

public class CenterStageTeleOp extends OpMode {
    private DcMotor fl, bl, fr, br;
    private boolean mecanumDriveMode = true;
    private float mecanumStrafe = 0 dominantXJoystick = 0;

    @Override
    public void init() {
        fl = hardwareMap.dcMotor.get("frontLeft");
        bl = hardwareMap.dcMotor.get("backLeft");
        fr = hardwareMap.dcMotor.get("frontRight");
        br = hardwareMap.dcMotor.get("backRight");

        fr.setDirection(REVERSE);
        br.setDirection(REVERSE);

        telemetry.addLine("Init Opmode");
    }

    @Override
    public void loop(){
        if (abs(gamepad1.left_stick_x)> 0.20 || abs(gamepad1.right_stick_x) > 0.2) {
            dominantXJoystick = (abs(gamepad1.left_stick_x) - abs(gamepad1.right_stick_x));
            mecanumDriveMode = true;
        } else {
            mecanumDriveMode = false;
        }

        if (mecanumDriveMode) {
                fl.setPower((gamepad1.left_stick_y + -mecanumStrafe) / 1.5);
                bl.setPower((gamepad1.left_stick_y + mecanumStrafe) / 1.5);
                fr.setPower((gamepad1.right_stick_y + mecanumStrafe) / 1.5);
                br.setPower((gamepad1.right_stick_y + -mecanumStrafe) / 1.5);
        } else {
                drive(gamepad1.left_stick_y * 0.8, gamepad1.right_stick_y * 0.8 );
        }

    }
    @Override
    public void stop() {
        telemetry.clearAll();
        telemetry.addLine("Stopped");
    }

    public void drive(double left, double right) {
        motorLeft.setPower(left);
        motorLeft2.setPower(left);
        motorRight.setPower(right);
        motorRight2.setPower(right);

    }

}
