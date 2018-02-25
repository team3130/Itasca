package org.usfirst.frc.team3130.robot.sensors;

import edu.wpi.first.wpilibj.I2C;

/**
 *
 */
public class Rangefinder {
	
    private I2C i2c;
	
	private final int LIDAR_ADDR = 0x52>>1;

	//Instance Handling
	private static Rangefinder m_pInstance = null;
	public static Rangefinder GetInstance()
	{
		if(m_pInstance == null) m_pInstance = new Rangefinder();
		return m_pInstance;
	}
	
	public Rangefinder() {
		i2c = new I2C(I2C.Port.kOnboard, LIDAR_ADDR);
		// Mandatory : private registers
		writeByte(0x0207, 0x01);
		
		int reset = readByte(0x016);
		if(reset == 1 || reset ==-1) {
			initialize();
		}
		else {
			System.out.println("LIDAR Already Initialized! Skipping I2C Writes!");
		}

		System.out.println("LIDAR Init complete");
	}

	public void initialize() {
		writeByte(0x0208, 0x01);
		writeByte(0x0096, 0x00);
		writeByte(0x0097, 0xfd);
		writeByte(0x00e3, 0x00);
		writeByte(0x00e4, 0x04);
		writeByte(0x00e5, 0x02);
		writeByte(0x00e6, 0x01);
		writeByte(0x00e7, 0x03);
		writeByte(0x00f5, 0x02);
		writeByte(0x00d9, 0x05);
		writeByte(0x00db, 0xce);
		writeByte(0x00dc, 0x03);
		writeByte(0x00dd, 0xf8);
		writeByte(0x009f, 0x00);
		writeByte(0x00a3, 0x3c);
		writeByte(0x00b7, 0x00);
		writeByte(0x00bb, 0x3c);
		writeByte(0x00b2, 0x09);
		writeByte(0x00ca, 0x09);
		writeByte(0x0198, 0x01);
		writeByte(0x01b0, 0x17);
		writeByte(0x01ad, 0x00);
		writeByte(0x00ff, 0x05);
		writeByte(0x0100, 0x05);
		writeByte(0x0199, 0x05);
		writeByte(0x01a6, 0x1b);
		writeByte(0x01ac, 0x3e);
		writeByte(0x01a7, 0x1f);
		writeByte(0x0030, 0x00);

		// Recommended : Public registers - See data sheet for more detail
		 // Enables polling for ‘New Sample ready’ when measurement completes
		writeByte (0x0011 , 0x10);
		 // Set the averaging sample period
		 // (compromise between lower noise and increased execution time)
		writeByte (0x010a , 0x30);
		 // Sets the light and dark gain (upper nibble). Dark gain should not be changed.
		writeByte (0x003f , 0x46);
		 // sets the # of range measurements after which auto calibration of system is performed   
		writeByte (0x0031 , 0xFF);
		 // Set ALS integration time to 100ms
		writeByte (0x0040 , 0x63);
		 // perform a single temperature calibration of the ranging sensor 
		writeByte (0x002e , 0x01);

		//Start Single Shot Ranging
		writeByte(0x0018, 0x01);
		writeByte(0x0016, 0x00);
	}

	// Distance in mm
	public int getDistance() {
		int reg = 0x0062;
		byte[] buffer = new byte[1];
		
		byte[] wbuffer = new byte[2];
		
		wbuffer[0] = (byte) ( (reg>>8) &0xFF);
		wbuffer[1] = (byte) (reg &0xFF);

		if(!i2c.writeBulk(wbuffer)) {				
			if( !i2c.readOnly(buffer, 1) ) {
				int num = buffer[0];
				if(num < 0) {
					num = num&127 + 128;
				}
				writeByte(0x0018, 0x01);
				
				return num;
			}
			else {
				return -1;
			}
		}
		else {
			return -1;
		}
	}
	
	// RESULT_RANGE_STATUS register
	public int getDistanceStatus() {
		byte[] buffer = new byte[1];
		if( !i2c.read(0x4D, 1, buffer) ) {
			int num = (buffer[0]>>4)&0x0F;
			return num;
		}
		else {
			return -1;
		}
	}
		
	// Range Ready register
	public boolean getDistanceReady() {
		int reg = 0x004F;
		byte[] buffer = new byte[1];
		
		byte[] wbuffer = new byte[2];
		wbuffer[0] = (byte) ( (reg>>8) &0xFF);
		wbuffer[1] = (byte) (reg &0xFF);

		if(!i2c.writeBulk(wbuffer)) {
			if( !i2c.readOnly(buffer, 1)) {
				int num = buffer[0]&0x04;
				return num > 0;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}	

	private boolean writeByte(int reg, int data) {
		byte[] buffer = new byte[3];
		
		buffer[0] = (byte) ( (reg>>8) & 0xFF);
		buffer[1] = (byte) (reg & 0xFF);
		buffer[2] = (byte) ((byte) data & 0xFF);
		
		byte count = 0;
		while(count<3) {
			if(!i2c.writeBulk(buffer)) {
				return true;
			}
			else {
				count ++;
			}
			System.out.println("LIDAR Write Byte Failed " + count + " times.");
		}
		return false;
	}
	
	private int readByte(int reg) {
		
		byte[] buffer = new byte[1];
		
		byte[] wbuffer = new byte[2];
		wbuffer[0] = (byte) ( (reg>>8) &0xFF);
		wbuffer[1] = (byte) (reg &0xFF);

		if(!i2c.writeBulk(wbuffer)) {
			if( !i2c.readOnly(buffer, 1)) {
				int num = buffer[0];
				if(num < 0) {
					num = num&127 + 128;
				}
				
				return num;
			}
			else {
				return -1;
			}
		}
		return -1;
	}
}

