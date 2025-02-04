import com.fazecast.jSerialComm.SerialPort;

public class Main {
    private static final double VAL_MAX_CAN = 5.0; // Example value, replace with actual
    private static final int CAN_BYTES = 12; // Example value, replace with actual

    public static void main(String[] args) {
        SerialPort serialPort = SerialPort.getCommPort("/dev/ttyS0");
        serialPort.setBaudRate(9600);
        serialPort.openPort();

        if (serialPort.isOpen()) {
            System.out.println("Port is open.");
            while (true) {
                byte[] buffer = new byte[1];
                int bytesRead1 = serialPort.readBytes(buffer, 1);
                int bytesRead2 = serialPort.readBytes(buffer, 1);

                if (bytesRead1 > 0 && bytesRead2 > 0) {
                    int int_value1 = buffer[0] & 0xFF;
                    int int_value2 = buffer[0] & 0xFF;

                    if (int_value1 == 255 && int_value2 == 255) {
                        bytesRead1 = serialPort.readBytes(buffer, 1);
                        bytesRead2 = serialPort.readBytes(buffer, 1);

                        if (bytesRead1 > 0 && bytesRead2 > 0) {
                            int_value1 = buffer[0] & 0xFF;
                            int_value2 = buffer[0] & 0xFF;

                            int combined_value = (int_value1 << 8) | int_value2;
                            double real_value = (combined_value * VAL_MAX_CAN) / (Math.pow(2, CAN_BYTES));

                            System.out.println("Real value: " + real_value);
                        }
                    }
                }
            }
        } else {
            System.out.println("Failed to open port.");
        }
    }
}