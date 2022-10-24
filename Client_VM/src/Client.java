import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.nio.channels.FileChannel;

public class Client {
  public static void main(String[] args) throws Exception {
    try {
      Socket socket = new Socket("192.168.56.1", 7999);
      DataInputStream inStream = new DataInputStream(socket.getInputStream());
      DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
      Scanner sc = new Scanner(System.in);
      String clientMessage = "", serverMessage = "", fn = "";
      while (!clientMessage.equals("bye")) {
        System.out.println("[ ================================================================ ]");
        serverMessage = inStream.readUTF();
        System.out.println(serverMessage);
        System.out.println("[ ================================================================ ]");

        /* start 2 client get message from server */
        clientMessage = sc.nextLine();
        outStream.writeUTF(clientMessage);
        /* start 2 client get message from server */

        /* start 3 client get message from server */
        System.out.println(inStream.readUTF());
        boolean check = inStream.readBoolean();
        String file_name = inStream.readUTF();
        boolean check_zero = inStream.readBoolean();
        /* start 3 client get message from server */

        /* get file normal Send */
        if (check == true && check_zero==false) {
          int bytes = 0;
          FileOutputStream fileOutputStream = new FileOutputStream("/home/aum/Desktop/VM/Keepfile_client/"  + file_name);
          long size = inStream.readLong(); // read file size
          byte[] buffer = new byte[10 * 1024];
          double sum = 0;
          double size_exis = size;
          // System.out.print(size);
          long start = System.currentTimeMillis();
          while (size > 0 && (bytes = inStream.read(buffer, 0,
              (int) Math.min(buffer.length, size))) != -1) {
            sum += bytes;
            System.out.println("Client get file:" + String.format("%.2f", (sum) / size_exis * 100) + " %");
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes; // read upto file size
          }
          System.out.println("successfully get size file:" + String.format("%.2f", size_exis / (1024 * 1024)) + " MB");
          fileOutputStream.close();
          long end = System.currentTimeMillis();
          long time = end - start;
          System.out.println("Time normal send  : " + time / 1000 + " second");
        }
        /* get file zoro copy */
        if (check == true &&check_zero == true) {
          String path = "/media/sf_Server/"+ file_name;
          System.out.println("Path check : "+path);
          long start = System.currentTimeMillis();
          zeroCopy(path, file_name);
          System.out.println("Time normal send : " + (System.currentTimeMillis()-start)/ 1000 + " second");
        }

      }
    } catch (IOException e) {
      System.out.println(e);
    } finally {
      System.out.println("is closed");
    }
  }

  public static void zeroCopy(String path, String file_name) throws IOException {
    FileChannel source = null;
    FileChannel destination = null;
    try {
      source = new FileInputStream(path).getChannel();
      destination = new FileOutputStream("/home/aum/Desktop/VM/Keepfile_client/"+ file_name).getChannel();
      source.transferTo(0, source.size(), destination);
    } finally {
      if (source != null) {
        source.close();
      }
      if (destination != null) {
        destination.close();
      }
    }
  }

}