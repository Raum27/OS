import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {

  public static void main(String[] args) throws Exception {
    try {
      Socket socket = new Socket("localhost", 9999);
      DataInputStream inStream = new DataInputStream(socket.getInputStream());
      DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
      Scanner sc = new Scanner(System.in);
      String clientMessage = "", serverMessage = "", fn = "";
      while (!clientMessage.equals("bye")) {
        System.out.println("[ ================================================================ ]");
        serverMessage = inStream.readUTF(); 
        System.out.println(serverMessage);
        System.out.println("[ ================================================================ ]");

        /*start 2 client get message from server */
        outStream.writeUTF(sc.nextLine());
        /*start 2 client get message from server */

        /*start 3 client get message from server */
        System.out.println(inStream.readUTF());
        boolean check = inStream.readBoolean();
        String file_name =inStream.readUTF();
        /*start 3 client get message from server */

        /*get file normal Send*/
        if (check==true){
          int bytes = 0;
          FileOutputStream fileOutputStream = new FileOutputStream("C:/Users/aumra/OneDrive/เดสก์ท็อป/psc/keep_client_file/"+file_name);
          long size = inStream.readLong();     // read file size
          byte[] buffer = new byte[10*1024];
          double sum=0;
          double size_exis = size;
          // System.out.print(size);
          while (size > 0 && (bytes = inStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
              sum+=bytes;
              System.out.println("Client get file :"+String.format("%.2f",(sum)/size_exis*100)+" %");
              fileOutputStream.write(buffer,0,bytes);
              size -= bytes;      // read upto file size
          }
          System.out.println("successfully get size file :"+String.format("%.2f",size_exis/(1024*1024))+" MB");
          fileOutputStream.close();
        }

        /*get file zoro copy*/

      }
    } catch (IOException e) {
      System.out.println(e);
    } finally {
      System.out.println("is closed");
    }
  }

}


