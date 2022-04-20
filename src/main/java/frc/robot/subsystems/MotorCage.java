package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class MotorCage extends SubsystemBase {
    private WPI_TalonSRX talonSRXIntake1;
    private WPI_TalonSRX talonSRXIntake2;
    private WPI_VictorSPX talonFXBottomConveyor;
    private WPI_VictorSPX talonFXTopConveyor;
    private WPI_TalonFX talonFXFrontShooter;
    private WPI_TalonFX talonFXBackShooter;
    

    private double shootSpeed = 1;
    private boolean shooterForward=true;
    

    public MotorCage() {
        talonSRXIntake1 = new WPI_TalonSRX(11);
        talonSRXIntake2 = new WPI_TalonSRX(12);
        talonFXBottomConveyor = new WPI_VictorSPX(6);
        talonFXTopConveyor = new WPI_VictorSPX(7);
        talonFXBackShooter = new WPI_TalonFX(8);
        talonFXFrontShooter = new WPI_TalonFX(9);
    }

    public void Intake_start(){
     talonSRXIntake1.set(-.5);
     talonSRXIntake2.set(-.5);
     
    }

    public void Intake_stop(){
        talonSRXIntake1.set(0);
        talonSRXIntake2.set(0);
        
    }

    public void Intake_reverse(){
        talonSRXIntake1.set(.5);
        talonSRXIntake2.set(.5);
    }

    public void BottomConveyor_start(){
        talonFXBottomConveyor.set(.3);
    }

    public void BottomConveyor_stop(){
        talonFXBottomConveyor.set(0);
    }

    public void BottomConveyor_reverse(){
        talonFXBottomConveyor.set(-.5);
    }

    public void TopConveyor_start(){
        talonFXTopConveyor.set(.5);
    }

    public void TopConveyor_stop(){
        talonFXTopConveyor.set(0);
    }
    public void TopConveyor_reverse(){
        talonFXTopConveyor.set(-.5);
    }


    public void revertTopConveyor_revert(){
        talonFXTopConveyor.set(1);

    }

    /**
     * Checks if
     */
    public void shoot_start(){
     /*   if (!shooterForward){
            talonFXFrontShooter.set(0);
            talonFXBackShooter.set(0);
        }*/
        //shooterForward=true;
        talonFXFrontShooter.set(shootSpeed);
        talonFXBackShooter.set(shootSpeed);
        
      }

      public void shoot_stop(){
        talonFXFrontShooter.set(0);
        talonFXBackShooter.set(0);
        
      }
      public void shoot_reverse(){
       /* if (shooterForward){
            talonFXFrontShooter.set(0);
            talonFXBackShooter.set(0);
          }
        shooterForward=false;*/
        talonFXFrontShooter.set(-shootSpeed*.1);
        talonFXBackShooter.set(-shootSpeed*.1);
        
      }
}

