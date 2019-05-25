package client;

import lpi.server.rmi.IServer;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.currentTimeMillis;


public class ConnectionHandlerRmi implements Closeable{
    private Registry rgs;
    private IServer server;
    private int Port;
    private String Host;
    private ProtocolManager protocolManager = new ProtocolManager();

    public ConnectionHandlerRmi(String Host, int Port) {
        this.Port = Port;
        this.Host = Host;
    }
    /////////////////////////

    public void perform(String textFromLabel) throws IOException {
        CommPars_1(protocolManager.TextPars(textFromLabel));
    }
    public static long start, end, time;
    private String pathFile = "C:\\Users\\Admin\\Idea\\";
    private void CommPars_1(String text_from_client) throws IOException {
        Matcher matcher;
        /////////////////
        for (final Rmi_commands comm : Rmi_commands.values()) {
            /////////////pattern match
            matcher = Pattern.compile(comm.getReg()).matcher(text_from_client);
            if (matcher.find()) {
                try {
                    switch (comm) {
                        case CMD_PING:
                            server.ping();
                            bool = true;
                            break;
                        case CMD_ECHO:
                            //resp
                            string = new String(server.echo(new String(protocolManager.parsComm1(text_from_client))));
                            bool = true;
                            break;

                        case CMD_PROCESS:
                            String[] path = protocolManager.parsComm2(text_from_client);
                            String[] path1 = protocolManager.parsComm3(path[1].toString());
                            System.out.println(path[1].toString()+"1");
                            System.out.println(path[2].toString()+"2");
                            System.out.println(path1[0].toString()+"3");
                            stuff(path1[0],path[2]);
                            bool = true;
                            break;

                        case EXIT:
                            break;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } bool = false;
    }
   private void proc(String fromFile, String toFile) throws IOException {
        IServer.Algoritm alg;
        //=null;
       alg = new IServer.Algoritm(new File(fromFile));
       Path pth=Paths.get(toFile);
       byte[] b_1;
       // b_1= new byte[0];
       start= currentTimeMillis();
       b_1 = server.executeTask(alg);
       byte[] h=new byte[0];
       Files.write(pth, b_1);
       end= currentTimeMillis();
       time=end-start;
       string = new String("Ok! File is sorted with time-"+time/1000+" sec");
    }

    private void stuff(String fromFile, String toFile) throws IOException {
        String to_path=toFile;
        String from_path=fromFile;
        int[] Size;
        Size= new int[100000];
        Path p_1 = Paths.get(fromFile);
        StringBuilder st_2 = new StringBuilder();
        for (int i = 0; i < Size.length-1; i++) {
            //Size[i] = new Random().nextInt(500)%100;
            Size[i] = new Random().nextInt(5000);
            st_2.append(Size[i]);
            if(i!=Size.length-1){
                st_2.append(" ");
            }
        }
        ////////////////////////////
        byte[] byte_array=st_2.toString().getBytes();
        Files.write(p_1, byte_array);
        proc(from_path,to_path);
    }
    ////////////////////////////
    ///////////////////////////////
    public boolean regClient() {
        try {
            //create object registry
            this.rgs= LocateRegistry.getRegistry(Host, Port);
            ///get object
            this.server = (IServer) rgs.lookup("lpi.server.rmi");
            return true;
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
            this.rgs = null;
            this.server = null;
            ident = null;
            return false; }
    }
    //////////////////////////

    private String string;
    String ident;
    @Override

    public void close() throws IOException {
        if (this.rgs != null) {
            try {
                if (ident != null) {
                    server.exit(ident);
                    ident = null;
                } } catch (AccessException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void Host(String text) {
    }

    public void Port(int parseInt) {
    }

    public  Registry rregistry() {
        return rgs;
    }

    public Object ident() {
        return null;
    }

    public String inText() { return string; }

    public void outText(String string) {
        this.string=string;
    }
    private boolean bool = false;

}
