// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.LED;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LEDConstants;
import frc.robot.Constants.PWMConstants;

public class AddressableLEDSubsystem extends SubsystemBase {
  private AddressableLED m_led;
  private AddressableLEDBuffer m_ledBuffer;
  private int m_rainbowFirstPixelHue;
  private int m_ledMode;
  private int m_noteStatus = 0;
  private int m_ShootingStatus = 0;
  private int m_CopperHawkStatus = 0;
  private int m_CopperHawkDelay = 0;
  

  public AddressableLEDSubsystem() {
    m_led = new AddressableLED(PWMConstants.LEDStringID);
    m_ledBuffer = new AddressableLEDBuffer(LEDConstants.LEDStringLength);
    
    m_led.setLength(m_ledBuffer.getLength());

    // Set the data
    m_led.setData(m_ledBuffer);
    m_led.start();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(m_ledMode == LEDConstants.LEDModeRainbow)
      rainbowMode();
    else if(m_ledMode == LEDConstants.LEDModeAllianceBlue)
      blueMode();
    else if(m_ledMode == LEDConstants.LEDModeAllianceRed)
      redMode();
    else if(m_ledMode == LEDConstants.LEDModeCopperHawks)
      copperHawksMode();
    else if(m_ledMode == LEDConstants.LEDModeNoteEaten)
      noteMode();
    else if(m_ledMode == LEDConstants.LEDModeShooting)
      shootingMode();
    m_led.setData(m_ledBuffer);
   }

   public int getLEDMode()
   {
     return m_ledMode;  
   }
   
   public void setLEDMode(int mode)
  {
    m_ledMode = mode;  
  }
  private void blueMode() {
    // For every pixel
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setLED( i, Color.kBlue);
    }
  }
  private void redMode() {
    // For every pixel
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setLED( i, Color.kRed);
    }
  }

  private void copperHawksMode() {
    // For every pixel
    int index;

    if( m_CopperHawkDelay == 0 )
    {
      for (var i = 0; i < m_ledBuffer.getLength(); i++) {
        index = (i+m_CopperHawkStatus) % 14;
        switch( index )
        {
          case 13:
          case 12:
          case 11:
          case 10:
          case 9:
            m_ledBuffer.setRGB( i, 0, 255, 0);
            break;
          case 8:
          case 7:
          case 1:
          case 0:
            m_ledBuffer.setRGB( i, 0, 0, 0);
            break;
          case 6:
          case 5:
          case 4:
          case 3:
          case 2:
          m_ledBuffer.setRGB( i, 255, 68, 0);      
          break;

        }
      }
    }
    
    // for (var i = 0; i < m_ledBuffer.getLength() /2; i++) {
    //     m_ledBuffer.setRGB( i*2+1, 0, 0, 0);
    // }
    m_CopperHawkDelay++;
    if( m_CopperHawkDelay >= 2)
    {

      m_CopperHawkDelay = 0;  
      m_CopperHawkStatus--;
      if( m_CopperHawkStatus <= 0)
        m_CopperHawkStatus = 14;
    }
  }

  private void rainbowMode() {
    // For every pixel
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_ledBuffer.getLength())) % 180;
      // Set the value
      m_ledBuffer.setHSV(i, hue, 255, 128);
    }
    // Increase by to make the rainbow "move"
    m_rainbowFirstPixelHue += 3;
    // Check bounds
    m_rainbowFirstPixelHue %= 180;
  }

  private void noteMode() {
    // For every pixel
    if( m_noteStatus < 3 )
    {
      for (var i = 0; i < m_ledBuffer.getLength(); i++) {
          m_ledBuffer.setRGB( i, 255, 68, 0);      
      }
    }
    else 
    {
      for (var i = 0; i < m_ledBuffer.getLength(); i++) {
          m_ledBuffer.setRGB( i, 0, 0, 0);      
      }
    }
    m_noteStatus++;
    if( m_noteStatus == 6)
      m_noteStatus = 0;
  }

  private void shootingMode() {
    // For every pixel
    int index;
    for (var i = 0; i < m_ledBuffer.getLength() /2; i++) {
      index = (i+m_ShootingStatus) % 20;
      switch( index )
      {
        case 19:
        case 18:
        case 17:
          m_ledBuffer.setRGB( i*2, 255, 0, 0);
          m_ledBuffer.setRGB( i*2+1, 255, 0, 0);
          break;
        case 16:
        case 15:
          m_ledBuffer.setRGB( i*2, 100, 0, 0);
          m_ledBuffer.setRGB( i*2+1, 100, 0, 0);
          
          break;
        case 14:
        case 13:
        case 12:
        case 11:
        case 10:
         m_ledBuffer.setRGB( i*2, 35, 0, 0);
         m_ledBuffer.setRGB( i*2+1, 35, 0, 0);
          break;
        case 9:
        case 8:
        case 7:
        case 6:
          m_ledBuffer.setRGB( i*2, 5, 0, 0);
          m_ledBuffer.setRGB( i*2+1, 5, 0, 0);
          break;
        default:
          m_ledBuffer.setRGB( i*2, 0, 0, 0);
          m_ledBuffer.setRGB( i*2+1, 0, 0, 0);
      }
    }
    
    // for (var i = 0; i < m_ledBuffer.getLength() /2; i++) {
    //     m_ledBuffer.setRGB( i*2+1, 0, 0, 0);
    // }

    m_ShootingStatus--;
    if( m_ShootingStatus <= 0)
      m_ShootingStatus = 20;
  }
}
