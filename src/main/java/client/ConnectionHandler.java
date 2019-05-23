package client;


import java.io.IOException;
import java.net.*;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;


public class ConnectionHandler implements Closeable{

    private String Host;
    private int Port;
    private Socket Socket;
    private DataInputStream DataInputStream;
    private DataOutputStream DataOutputStream;
    private ProtocolManager ProtocolManager;
    private volatile boolean isClose = false;
    private Thread InText;
    private Thread showResponse;
    private volatile String responseText;

    private static final byte CMD_PING = 1;
    private static final byte CMD_ECHO = 3;

    //constructor
    public ConnectionHandler(String Host, int Port) {
        this.Host = Host;
        this.Port = Port;
    }

    public void SocketConnect() {
        if(Socket != null && Socket.isConnected()){
            //connect to socket
            close(true);
        }
        try {
            Socket = new Socket();
            Socket.connect(new InetSocketAddress(this.Host, this.Port), 500);
            //input/output
            DataInputStream = new DataInputStream(Socket.getInputStream());
            DataOutputStream = new DataOutputStream(Socket.getOutputStream());
            ProtocolManager = new ProtocolManager();
            if (Socket.isConnected()) {
                System.out.println("Connect to socket!");
                this.isClose = false;
            }

        }  catch (SocketTimeoutException te){

            System.out.println("Socket timeout connection");
        } catch (UnknownHostException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    @Override
    //close
    public void close() throws IOException {
        close(false);
    }


    public void close(boolean Close) {
        if (Socket == null) return;
        try {
            DataInputStream.close();
            DataOutputStream.close();
            Socket.close();
            this.isClose = true;
            System.out.println("Close socket");
        } catch (IOException e) {
            System.out.println(e);
        }
        Socket = null;
    }

    public void OutText(String in_text) throws IOException {
        byte[] comand = ProtocolManager.perform(in_text);
        try {
            DataOutputStream.writeInt(comand.length);
            DataOutputStream.write(comand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String InText(){
        InText = new Thread(new Runnable() {
            @Override
            public void run() {
                while (Socket.isConnected()) {
                    try {
                        int contentSize = DataInputStream.readInt();
                        byte[] message = new byte[contentSize];
                        DataInputStream.readFully(message);
                        responseText = new String(ProtocolManager.Response(message));
                    } catch (IOException e) {
                        InText.interrupt();
                        ConnectionHandler.this.close(true);
                        e.printStackTrace();
                    }
                }
            }
        });
        InText.start();
        return responseText;
    }


    public void InHost(String host) {
        this.Host = host;
    }

    public String ReturnHost() {
        return Host;
    }

    public void InPort(int port) {
        this.Port = port;
    }

    public int ReturnPort() {
        return Port;
    }

    public Socket getSocket() {
        return Socket;
    }

    public String getText() {
        return responseText;
    }

    public void setText(String responseText) {
        this.responseText = responseText;
    }
}
