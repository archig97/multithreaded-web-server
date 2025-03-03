package multithreaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public Runnable getRunnable(){
        return new Runnable() {//anonymous class definition of Runnable instead of lambda function
        @Override
            public void run() {
            int port = 8010;
            try {
                InetAddress address = InetAddress.getByName("localhost");
                Socket socket = new Socket(address, port);
                try (
                        PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                ) {
                    toSocket.println("Hello from Client " + socket.getLocalSocketAddress());
                    String line = fromSocket.readLine();
                    System.out.println("Response from Server " + line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // The socket will be closed automatically when leaving the try-with-resources block
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        };
    }
    public static void main(String[] args) {
        //We need to send 100 requests from the client
        Client client = new Client();
        for(int i=0;i<100;i++){
            try{
                Thread thread = new Thread(client.getRunnable());
                thread.start();
            }
            catch(Exception e){
                return;
            }
        }
    }
}
