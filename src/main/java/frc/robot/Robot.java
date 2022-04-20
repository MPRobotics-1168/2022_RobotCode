// RobotBuilder Version: 4.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

// ROBOTBUILDER TYPE: Robot.

package frc.robot;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.MotorCage;
import frc.robot.subsystems.Pneumatics;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.TimedRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in
 * the project.
 */
public class Robot extends TimedRobot {

    private Command m_autonomousCommand;

    private RobotContainer m_robotContainer;

    public static Pneumatics pneumatics;

    public static MotorCage motorcage;

    public static Object drivetrain;

    public boolean actuated;

    public double actuatedmaxSpeed;

    public double speedFactor = .6;

    boolean toggleOn = false;
    boolean togglePressed = false;

    UsbCamera shootCamera;
    UsbCamera intakeCamera;
    NetworkTableEntry cameraSelection;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        // Instantiate our RobotContainer. This will perform all our button bindings,
        // and put our
        // autonomous chooser on the dashboard.

        pneumatics = new Pneumatics();
        motorcage = new MotorCage();
        m_robotContainer = RobotContainer.getInstance();
        HAL.report(tResourceType.kResourceType_Framework, tInstances.kFramework_RobotBuilder);
        intakeCamera = CameraServer.startAutomaticCapture(0);
        shootCamera = CameraServer.startAutomaticCapture(1);
        cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");

        actuated = false;
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for
     * items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and
     * test.
     *
     * <p>
     * This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler. This is responsible for polling buttons, adding
        // newly-scheduled
        // commands, running already-scheduled commands, removing finished or
        // interrupted commands,
        // and running subsystem periodic() methods. This must be called from the
        // robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    /**
     * This autonomous runs the autonomous command selected by your
     * {@link RobotContainer} class.
     */
    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();

        }
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {

        XboxController jstick = RobotContainer.getInstance().getXboxController1();
        double ySpeed = -jstick.getRawAxis(1) * Math.abs(jstick.getRawAxis(1)) * speedFactor;
        double xSpeed = jstick.getRawAxis(0) * Math.abs(jstick.getRawAxis(0)) * speedFactor;
        double zRot = jstick.getRawAxis(4) * Math.abs(jstick.getRawAxis(4)) * speedFactor;
        double gyro = 0;

        if (actuated == true) {
            xSpeed = xSpeed * .6;
            ySpeed = ySpeed * .6;
            zRot = zRot * .4;
        }

        DriveTrain.mecanumDrive1.driveCartesian(ySpeed * Math.abs(ySpeed), xSpeed * Math.abs(xSpeed), zRot, gyro);

        // two button config - X retract, B fire
        /*
         * if(jstick.getRawButton(2)){
         * Robot.pneumatics.actuateClimbing();
         * }
         * 
         * if(jstick.getRawButton(3)){
         * Robot.pneumatics.retractClimbing();
         * }
         */

        // one button config - X hold fires, X release unfires
        /*
         * if(jstick.getRawButtonPressed(3)){
         * Robot.pneumatics.actuateClimbing();
         * }
         * if(jstick.getRawButtonReleased(3)){
         * Robot.pneumatics.retractClimbing();
         * }
         */

        MotorThings();
        // toggle
        updateToggle();

        if (toggleOn) {
            Robot.pneumatics.actuateClimbing();
            actuated = true;
        } else {
            Robot.pneumatics.retractClimbing();
            actuated = false;
        }

        if (jstick.getPOV() == 0) {
            speedFactor = .8;
        } else if (jstick.getPOV() == 180) {
            speedFactor = .6;
        }

    }

    public void updateToggle() {
        XboxController jstick = RobotContainer.getInstance().getXboxController1();
        {
            if (jstick.getRawButton(4)) {
                if (!togglePressed) {
                    toggleOn = !toggleOn;
                    togglePressed = true;
                }
            } else {
                togglePressed = false;
            }
        }
    }

    public void MotorThings() {
        XboxController jstick = RobotContainer.getInstance().getXboxController1();
        // if intake AND reverse
        if ((jstick.getRawAxis(2) > .2 || jstick.getRawButton(5)) && jstick.getRawButton(2)) {
            Robot.motorcage.Intake_reverse();
            Robot.motorcage.BottomConveyor_reverse();
        }
        // OTherwise, if Intake
        else if ((jstick.getRawAxis(2) > .2) || jstick.getRawButton(5)) {

            Robot.motorcage.Intake_start();
            Robot.motorcage.BottomConveyor_start();
        } else {
            Robot.motorcage.Intake_stop();
            Robot.motorcage.BottomConveyor_stop();
        }
        /*
         * if(jstick.getRawButton(5) && jstick.getRawButton(2)){
         * Robot.motorcage.BottomConveyor_reverse();
         * }else if(jstick.getRawButton(5)){
         * Robot.motorcage.BottomConveyor_start();
         * }else{
         * Robot.motorcage.BottomConveyor_stop();
         * }
         */

        //If top conveyor and revers
        if (jstick.getRawButton(6) && jstick.getRawButton(2)) {
            Robot.motorcage.TopConveyor_reverse();
        } 
        //otherwise if top conveyor
        else if (jstick.getRawButton(6)) {
            Robot.motorcage.TopConveyor_start();
        }
        //otherwise
        else {
            Robot.motorcage.TopConveyor_stop();
        }

        //If shooter and reverse
        if (jstick.getRawAxis(3) > .2 && jstick.getRawButton(2)) {
            Robot.motorcage.shoot_reverse();
        }
        //otherwise if shooter
        else if (jstick.getRawAxis(3) > .2) {
            Robot.motorcage.shoot_start();
        }
        //otherwise
        else {
            Robot.motorcage.shoot_stop();
        }

        // Camera Switch
        if (jstick.getRawButtonPressed(7)) {
            System.out.println("Setting camera 2");
            cameraSelection.setString(shootCamera.getName());
        } else if (jstick.getRawButtonPressed(8)) {
            System.out.println("Setting camera 1");
            cameraSelection.setString(intakeCamera.getName());
        }

    }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }

}