
import java.net.*;
import java.io.*;

public class MultithreadedSocketServer {
    public static void main(String[] args) throws Exception {
        try {
            ServerSocket server = new ServerSocket(9999);
            int counter = 0;
            System.out.println("Server Started ....");
            while (true) {
                counter++;
                Socket serverClient = server.accept(); // server accept the client connection request
                System.out.println(" >> " + "Client No:" + counter + " started!");
                new Thread(new ServerClientThread(serverClient, counter)).start(); // thread get client

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class ServerClientThread implements Runnable {
    Socket serverClient;
    int clientNo;
    DataInputStream inStream;
    DataOutputStream outStream;
    ServerClientThread(Socket inSocket, int counter) {
        serverClient = inSocket;
        clientNo = counter;

    }

    public void run() {
        try {
            String clientMessage = "", serverMessage = "", fn = "";
            boolean check = false; // HERE EDIT
            inStream = new DataInputStream(serverClient.getInputStream());
            outStream = new DataOutputStream(serverClient.getOutputStream());
            while (!clientMessage.equals("bye")) {
                /* start 1 server send from client */
                serverMessage = "          Hello Client we have 3 files for download  \n--->        1.BLACKPINK\n--->        2.GAME\n--->        3.JAPAN\n";
                serverMessage += "         PRESS 1 2 3 OR type name file";
                outStream.writeUTF(serverMessage);
                /* end 1 server send from client */

                /* start 2 server get message from client */
                clientMessage=inStream.readUTF();
                System.out.println("Client -" + clientNo +" Message: "+clientMessage);
                /* start 2 server get message from client */

                /*Send file */
                if (clientMessage.equals("1")||clientMessage.equalsIgnoreCase("BLACKPINK")){
                    outStream.writeUTF("Sending file BLACKPINK.mp4 to Clinet . . .");
                    outStream.writeBoolean(true);
                    outStream.writeUTF("BLACKPINK.mp4");
                    sendFile("C:/Users/aumra/OneDrive/เดสก์ท็อป/psc/Server/BLACKPINK.mp4");
                }else if(clientMessage.equals("2")||clientMessage.equalsIgnoreCase("GAME")){
                    outStream.writeUTF("Sending file GAME.mp4 to Clinet . . .");
                    outStream.writeBoolean(true);
                    outStream.writeUTF("GAME.mp4");
                    sendFile("C:/Users/aumra/OneDrive/เดสก์ท็อป/psc/Server/GAME.mp4");
                }else if(clientMessage.equals("3")||clientMessage.equalsIgnoreCase("JAPAN")){
                    outStream.writeUTF("Sending file JAPAN.mp4 to Clinet . . .");
                    outStream.writeBoolean(true);
                    outStream.writeUTF("JAPAN.mp4");
                    sendFile("C:/Users/aumra/OneDrive/เดสก์ท็อป/psc/Server/JAPAN.mp4");
                }else {
                    outStream.writeUTF("not found file what you want");
                    outStream.writeBoolean(false);
                    outStream.writeUTF("Noflie");
                    continue;
                }

            }
            inStream.close();
            outStream.close();
            serverClient.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            System.out.println("Client -" + clientNo + " exit!! ");
        }
    }

    public void sendFile(String path) throws Exception{
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        
        // send file size
        outStream.writeLong(file.length());  
        // break file into chunks
        double sum=0;
        byte[] buffer = new byte[10*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            sum+=bytes;
            System.out.println("Server send file :"+String.format("%.2f",(sum)/file.length()*100)+" %");
            outStream.write(buffer,0,bytes);
            outStream.flush();
        }
        fileInputStream.close();
    }
}
