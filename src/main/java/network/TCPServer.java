package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import database.UserDatabase;

/**
 * Using TCP Server, allowing
 */
public class TCPServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(12345); // Choose a suitable port
        System.out.println("Server is running. Waiting for connections...");
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            System.out.println("IP Address: " + ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            Socket socket = serverSocket.accept();
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputData = input.readLine();

            if(inputData.startsWith("GPS")){
                String[] splits = inputData.split("~");
                String username = splits[1];
                char[] chars = username.toCharArray();
                if ((chars[0] >= 'a' && chars[0] <= 'z') || (chars[0] >= 'A' && chars[0] <= 'Z')){
                    String newGPS = splits[2];
                    System.out.println(newGPS);
                    String point = newGPS.split(",")[0];
                     if (!point.equals("")) {
                         System.out.println("GPS will be updated");
                         UserDatabase.updateGPS(username, newGPS);
                     }else{
                         System.out.println("the gps point is blank");
                     }
                }else{
                    System.out.println("the username is not allowed");
                }
            } else if (inputData.startsWith("UPDATE")) {
                String[] splits = inputData.split("~");
                String username = splits[1];
                char[] chars = username.toCharArray();
                if ((chars[0] >= 'a' && chars[0] <= 'z') || (chars[0] >= 'A' && chars[0] <= 'Z')) {
                    String newPassword = splits[2];
                    System.out.println(newPassword);
                    if (!newPassword.equals("")) {
                        System.out.println("update! "+username);
                        UserDatabase.updatePassword(username, newPassword);
                    } else {
                        System.out.println("The password is blank");
                    }
                } else {
                    System.out.println("The username is not allowed");
                }
            }else if (inputData.startsWith("BRUTE")) {
                System.out.println("Brute Force detected!");
                UserDatabase.addBrute();
            } else if (inputData.startsWith("LED")) {
                System.out.println("LED setting will be changed");
                String[] splits = inputData.split("~");
                int setting = Integer.parseInt(splits[1]);
                UserDatabase.updateLED(setting);
            } else if (inputData.startsWith("BUZZER")) {
                System.out.println("BUZZER setting will be changed");
                String[] splits = inputData.split("~");
                int setting = Integer.parseInt(splits[1]);
                UserDatabase.updateBuzzer(setting);
            } else {
                System.out.println("Received data: " + inputData);
            }
            socket.close();
        }
    }
}
