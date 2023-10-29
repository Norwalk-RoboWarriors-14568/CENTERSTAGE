package org.firstinspires.ftc.teamcode.opmodes;

import static com.acmerobotics.roadrunner.ftc.Actions.runBlocking;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;
@Autonomous(group = "Isreal or Palestine?? \uD83E\uDD14 \uD83E\uDD14 \uD83E\uDD14")
public class MyPocketsISrealite extends LinearOpMode{

        MecanumDrive drive;
        MONKERYSEEMONRYDOO openCv;
        ElapsedTime waitTimer = new ElapsedTime();
        ElapsedTime matchTimer = new ElapsedTime();
/*        enum State {
            START,
            FIRST_JUNCTION,
            WAIT_1,
            STACK,
            WAIT_2,
            POLE_RETURN,
            DROP_CONE,
            IDLE,
            SMALL_POLE,
            PARK
        }
        State currentState = State.START; */
        Pose2d startPose = new Pose2d(0, 0.5, 0);

        Pose2d Yellow = new Pose2d(50,-25, Math.toRadians(90));
        Pose2d Blue = new Pose2d(52,-3,Math.toRadians(90));
        Pose2d Red = new Pose2d(52,28,Math.toRadians(180));
        Pose2d park;

        @Override
        public void runOpMode() throws InterruptedException {

            drive = new MecanumDrive(hardwareMap, startPose);

            openCv = new MONKERYSEEMONRYDOO();
            openCv.OpenCv(hardwareMap, telemetry);

            while (!isStarted() && !isStopRequested())
            {
                telemetry.addData("Realtime analysis : ", openCv.pipeline.getAnalysis());
                telemetry.update();
                sleep(10);
            }
            int snapshotAnalysis = openCv.analysis();

            telemetry.addData("Snapshot post-START analysis : ", snapshotAnalysis);
            telemetry.update();

            if (isStopRequested()) return;

            switch (snapshotAnalysis)
            {
                case 1://BLUE
                {
                    park = Blue;
                    break;
                }
                case 2://YELLOW
                {
                    park = Yellow;
                    break;
                }
                default://RED
                {
                    park = Red;
                    break;
                }
            }

            Action startingStrafe = drive.actionBuilder((startPose))
                    .lineToXConstantHeading(8).lineToYConstantHeading(8)
                    .build();

            class Drive {
                public Action followTrajectory() {
                    return null;
                }

                public Action turn(double v) {
                    return null;
                }

                public Action moveToPoint() {
                    return null;
                }
            }
            class Shooter {
                public Action spinUp() {
                    return null;
                }
                public Action fireBall() {
                    return null;
                }
                public Action loadBall() {
                    return null;
                }
            }
/*
            TrajectoryActionBuilder toFirstJunction = drive.actionBuilder(new Pose2d(startingStrafe.))
                    .lineToLinearHeading(new Pose2d(48, -24, Math.toRadians(0)))
                    .lineToLinearHeading(new Pose2d(55.5, -10, Math.toRadians(0)))
                    .build();

            TrajectoryActionBuilder toStack = drive.actionBuilder((toFirstJunction.end()))
                    .lineToLinearHeading(new Pose2d(52.5, -11, Math.toRadians(0)))
                    .lineToLinearHeading(new Pose2d(53, 0, Math.toRadians(90)))
                    .lineToLinearHeading(new Pose2d(53, 27.5, Math.toRadians(90)))
                    .build();

            TrajectoryActionBuilder toBigPole = drive.actionBuilder((toStack.end()))
                    //.lineToLinearHeading(new Pose2d(52.5, 6, Math.toRadians(-90)))
                    //.lineToLinearHeading(new Pose2d(52, 10, Math.toRadians(0)))
                    //.lineToLinearHeading(new Pose2d(55.75, 10, Math.toRadians(0)))
                    .lineToLinearHeading(new Pose2d(50, 0, Math.toRadians(90)))
                    .lineToLinearHeading(new Pose2d(57.5, -9, Math.toRadians(0)))
                    .build();

            TrajectoryActionBuilder toStackTwo = drive.actionBuilder((toBigPole.end()))
                    .lineToLinearHeading(new Pose2d(52.5, -10.5, Math.toRadians(0)))
                    .lineToLinearHeading(new Pose2d(53, 0, Math.toRadians(90)))
                    .lineToLinearHeading(new Pose2d(53.75, 26.5, Math.toRadians(90)))
                    .build();

            TrajectoryActionBuilder toBigPoleTwo = drive.actionBuilder((toStackTwo.end()))
                    .lineToLinearHeading(new Pose2d(52.5, -6, Math.toRadians(90)))
                    .lineToLinearHeading(new Pose2d(52, -10, Math.toRadians(0)))
                    .lineToLinearHeading(new Pose2d(55.75, -10, Math.toRadians(0)))
                    .build();
            TrajectoryActionBuilder toSmallPole = drive.actionBuilder(toStack.end())
                    //.lineToLinearHeading(new Pose2d(54, 14.5, Math.toRadians(180)))
                    .lineToLinearHeading(new Pose2d(55, 25, Math.toRadians(90)))

                    .lineToLinearHeading(new Pose2d(49.5, 12.5, Math.toRadians(180)))
                    .build();
            TrajectoryActionBuilder Park = drive.actionBuilder(toSmallPole.end())
                    // .lineToLinearHeading(new Pose2d(54, 6, Math.toRadians(90)))

                    //.lineToLinearHeading(new Pose2d(52.5, -11, Math.toRadians(0)))
                    .lineToLinearHeading(park)
                    .build();

 */

            waitForStart();

            if (isStopRequested()) return;
            //drive.FollowTrajectoryAction(startingStrafe);

            Drive drive = new Drive();
            drive.moveToPoint(10, 20).runBlocking();

            while (opModeIsActive() && !isStopRequested()) {
                /*switch (currentState) {
                    case START:
                        drive.ConeGrabber.setPosition(0);
                        if (!drive.isBusy()) {
                            currentState = State.FIRST_JUNCTION;
                            drive.ParallelAction(toFirstJunction);
                        }
                        break;
                    case FIRST_JUNCTION:
                        armHeight(1, highPoleTicks);
                        if (!drive.isBusy()) {
                            // armHeight(1, highPoleTicks -(int)(84.5 *3.5))
                            //sleep(10000);
                            drive.ConeGrabber.setPosition(0.4);
                            if (stackConesGrabbed <=2) {
                                waitTimer.reset();
                                currentState = State.WAIT_1;
                                if (stackConesGrabbed==0) {
                                    drive.followTrajectorySequenceAsync(toStack);
                                } else {
                                    drive.followTrajectorySequenceAsync(toStackTwo);
                                }
                            }
                        }
                        break;
                    case WAIT_1:
                        if(waitTimer.seconds()>= waitTime) {
                            if (stackConesGrabbed == 3) {
                                currentState = State.PARK;
                            }else {
                                currentState = State.STACK;
                            }
                        }
                        break;
                    case STACK:
                        armHeight(-1,topOfStack);
                        if (!drive.isBusy()) {
                            drive.ConeGrabber.setPosition(0);
                            currentState = State.WAIT_2;
                            stackConesGrabbed++;
                            waitTimer.reset();
                        }
                        break;
                    case WAIT_2:
                        if(waitTimer.seconds()>= waitTime) {
                            if (stackConesGrabbed < 3) {
                                currentState = State.POLE_RETURN;
                            } else {
                                currentState = State.SMALL_POLE;
                            }
                            topOfStack-= (1.25 * ARMTPI);
                        }
                        break;
                    case SMALL_POLE:
                        armHeight(1, (int)84.5*16);
                        if(!drive.isBusy()){
                            drive.followTrajectorySequenceAsync(toSmallPole);
                            currentState = State.DROP_CONE;
                        }
                        break;
                    case POLE_RETURN:
                        armHeight(1, highPoleTicks);
                        if (!drive.isBusy()) {
                            drive.followTrajectorySequenceAsync(toBigPole);
                            currentState = State.FIRST_JUNCTION;
                        }
                        break;
                    case DROP_CONE:
                        if (!drive.isBusy()) {
                            // armHeight(-1, drive.motorLift.getCurrentPosition() -(int)(84.5 *3.5));
                            drive.ConeGrabber.setPosition(0.4);
                            waitTimer.reset();
                            currentState = State.WAIT_1;
                        }
                        break;
                    case PARK:
                        drive.followTrajectorySequenceAsync(Park);
                        currentState = State.IDLE;
                        break;
                    case IDLE:
                        if (!drive.isBusy()) {
                            armHeight(-1,0);
                        }
                        break;
                }

                 */
                /*
                runBlocking(new SequentialAction(
                        drive.turn(Math.PI / 2),
                        new ParallelAction(
                                drive.followTrajectory(shootingTraj),
                                new SequentialAction(
                                        shooter.spinUp(),
                                        shooter.fireBall(),
                                        ),
                                ),
                        ));
*/

               // Pose2d poseEstimate = drive.getPoseEstimate();

              //  PoseStorage.currentPose = poseEstimate;


            }
        }



}
