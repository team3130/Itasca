package org.usfirst.frc.team3130.robot.sensors;

import edu.wpi.first.wpilibj.I2C;

/**
 *
 */
public class Rangefinder {
	
    private I2C i2c;
	
	private final int LIDAR_ADDR = 0x52>>1;

	//Instance Handling
	private static Rangefinder m_pInstance;
	public static Rangefinder GetInstance()
	{
		if(m_pInstance == null) m_pInstance = new Rangefinder();
		return m_pInstance;
	}
	
	public Rangefinder() {
		i2c = new I2C(I2C.Port.kOnboard, LIDAR_ADDR);
		// Mandatory : private registers
		i2c.write(0x0207, 0x01);
		i2c.write(0x0208, 0x01);
		i2c.write(0x0096, 0x00);
		i2c.write(0x0097, 0xfd);
		i2c.write(0x00e3, 0x00);
		i2c.write(0x00e4, 0x04);
		i2c.write(0x00e5, 0x02);
		i2c.write(0x00e6, 0x01);
		i2c.write(0x00e7, 0x03);
		i2c.write(0x00f5, 0x02);
		i2c.write(0x00d9, 0x05);
		i2c.write(0x00db, 0xce);
		i2c.write(0x00dc, 0x03);
		i2c.write(0x00dd, 0xf8);
		i2c.write(0x009f, 0x00);
		i2c.write(0x00a3, 0x3c);
		i2c.write(0x00b7, 0x00);
		i2c.write(0x00bb, 0x3c);
		i2c.write(0x00b2, 0x09);
		i2c.write(0x00ca, 0x09);
		i2c.write(0x0198, 0x01);
		i2c.write(0x01b0, 0x17);
		i2c.write(0x01ad, 0x00);
		i2c.write(0x00ff, 0x05);
		i2c.write(0x0100, 0x05);
		i2c.write(0x0199, 0x05);
		i2c.write(0x01a6, 0x1b);
		i2c.write(0x01ac, 0x3e);
		i2c.write(0x01a7, 0x1f);
		i2c.write(0x0030, 0x00);

		// Recommended : Public registers - See data sheet for more detail
		 // Enables polling for ‘New Sample ready’ when measurement completes
		i2c.write (0x0011 , 0x10);
		 // Set the averaging sample period
		 // (compromise between lower noise and increased execution time)
		i2c.write (0x010a , 0x30);
		 // Sets the light and dark gain (upper nibble). Dark gain should not be changed.
		i2c.write (0x003f , 0x46);
		 // sets the # of range measurements after which auto calibration of system is performed   
		i2c.write (0x0031 , 0xFF);
		 // Set ALS integration time to 100ms
		i2c.write (0x0040 , 0x63);
		 // perform a single temperature calibration of the ranging sensor 
		i2c.write (0x002e , 0x01);

		 //Start Continuous Ranging
		i2c.write(0x0018, 0x03);
	}

	// Distance in mm
	public static int getDistance() {
		Rangefinder instance = GetInstance();
		byte[] buffer = new byte[1];
		if( !instance.i2c.read(0x62, 1, buffer) ) {
			return buffer[0];
		}
		else {
			return -1;
		}
	}
	
}

