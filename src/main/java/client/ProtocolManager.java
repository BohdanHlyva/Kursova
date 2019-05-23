package client;

import lpi.server.rmi.IServer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtocolManager {
    // Commands
    private static final byte CMD_PING = 1;
    private static final byte CMD_PING_RESPONSE = 2;
    private static final byte CMD_ECHO = 3;
    private static final byte CMD_LOGIN = 5;
    private static final byte CMD_LIST = 10;
    private static final byte CMD_MSG = 15;
    private static final byte CMD_FILE = 20;
    private static final byte CMD_RECEIVE_MSG = 25;
    private static final byte CMD_RECEIVE_FILE = 30;
    private static final byte CMD_MSG_SENT = 16;
    private static final byte CMD_FILE_SENT = 21;
    private static final byte CMD_LOGIN_OK_NEW = 6;
    private static final byte CMD_LOGIN_OK = 7;
    private static final byte CMD_RECEIVE_MSG_EMPTY = 26;
    private static final byte CMD_RECEIVE_FILE_EMPTY = 31;
    private static final byte SERVER_ERROR = 100;
    private static final byte SERIALIZATION_ERROR = 102;
    private static final byte INCORRECT_COMMAND = 103;
    private static final byte WRONG_PARAMS = 104;
    private static final byte LOGIN_WRONG_PASSWORD = 110;
    private static final byte LOGIN_FIRST = 112;

    private boolean isCommand = false;
    private String comm;
    private Object command;
    private IServer server;

    // private Message message;
    //"C:\\Users\\Admin\\Idea\\Idea.txt";;
    private String pathFile = "C:\\Users\\Admin\\Idea\\";
    private boolean WEcho = false;
    private boolean WList = false;
    private boolean WMsg = false;
    private boolean WFile = false;
    //////////////////////////////////////////////////////////////////////////////////////
    public byte[] perform(String text_from_client) {

        return CommPars(TextPars(text_from_client));
    }
    //////////////////////////////////////////////////////////////////////////////////////////
    /*
    *  File CP_file = new File("C:/Users");
    int count = fr.fileSizeInLines(CP_file);
    System.out.println("Total number of lines in the file are: "+count);

    List<String> lines = fr.strReader(CP_file);
    File FileList = new File("C:/Users");
Scanner scan = new Scanner(FileList);
String TheName = idToFile(Integer.toString(result));

File myImageFile = new File("C:/Users" + TheName);
sendResponse("#OK");
sendResponse(TheName);
sendResponse(Long.toString(myImageFile.length()));
    * */
    /////////////////////////////////
    public String TextPars(String text_from_client) {
        StringBuffer stringBuffer = new StringBuffer("");
        String string = null;

        if (text_from_client.charAt(0) == ' ') {
            for (int i = 1; i < text_from_client.length(); i++) {
                if (text_from_client.charAt(i) == ' ' && text_from_client.charAt(i + 1) == '%') {
                    for (int j = i + 1; j < text_from_client.length(); j++) {
                        stringBuffer.append(text_from_client.charAt(j));
                    }
                    string= stringBuffer.toString();
                    isCommand = true;
                    return string;
                }
            }
            if (string == null) {
                return text_from_client;
            }
        } else if (text_from_client.charAt(0) == '%') {
            isCommand = true;
            return text_from_client;
        } else {
            return text_from_client;
        }
        return null;
    }
    Message message;
    ///////////////////////////////////////////////////////////////////////////////////
    private byte[] CommPars(String text_from_client) {
        Matcher matcher;
        /////////////////
        for (final Rmi_commands comm : Rmi_commands.values()) {
            /////////////pattern match
            matcher = Pattern.compile(comm.getReg()).matcher(text_from_client);
            if (matcher.find()) {
                switch (comm) {
                    case CMD_PING:
                        return new byte[]{CMD_PING};
                    case CMD_ECHO:
                        WEcho = true;
                        return serial(CMD_ECHO, new String(parsComm1(text_from_client)));
                    case CMD_LOGIN:
                        String[] item = parsComm2(text_from_client);
                        message = new Message();
                        message.setLogin(item[1]);
                        return serial(CMD_LOGIN, new String[]{item[1], item[2]});
                    case CMD_LIST:
                        WList = true;
                        return new byte[]{CMD_LIST};
                    case CMD_MSG:
                        ////////////  System.out.println(message.getLogin());
                        return serial(CMD_MSG, new String[]{
                                message.getLogin(), parsComm1(text_from_client)});
                    case CMD_FILE:
                        return sendFile(text_from_client);


                }}
        }
        return null;
    }
    ///////////////////////////////////////////////////////////////////response from server
    public String Response(byte[] message) {
        switch (message[0]) {
            case CMD_PING_RESPONSE:
                return "PING";
            case SERIALIZATION_ERROR:
                return "SERIALIZATION_ERROR";
            case INCORRECT_COMMAND:
                return "INCORRECT_COMMAND";
            case CMD_LOGIN_OK_NEW:
                return "LOGIN_OK_NEW";
            case SERVER_ERROR:
                return "SERVER_ERROR";
            case CMD_RECEIVE_FILE_EMPTY:
                return "RECEIVE_FILE_EMPTY";
            case WRONG_PARAMS:
                return "WRONG_PARAMS";
            case LOGIN_WRONG_PASSWORD:
                return "WRONG_PASSWORD";
            case LOGIN_FIRST:
                return "LOGIN_FIRST";
            case CMD_LOGIN_OK:
                return "LOGIN_OK";
            case CMD_MSG_SENT:
                return "Message sent.";
            case CMD_FILE_SENT:
                return "File_SENT.";
            case CMD_RECEIVE_MSG_EMPTY:
                return "RECEIVE_MSG_EMPTY";
            default: {
                return new String(message);
            }}
    }
    //////////////////////////////////////////////////////////////////////////////File send
    private byte[] sendFile(String comm){
        //pars command
        String[] str = parsComm2(comm);
        //////
        //path[1]-pathFile
        Path path = Paths.get(pathFile, str[1]);
        //file to byte
        byte[] filebyte = null;
        try {
            filebyte = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            filebyte = null;
        }
        //CMD_FILE, login, path, byte array file
        return serial(CMD_FILE, new  Object[]{message.getLogin(), str[1], filebyte});
    }
    //////////////////////////: ;
    public String parsComm1(String command) {
        if (isCommand) {
            String arg = command.substring(command.indexOf(":") + 1, command.indexOf(";"));
            return arg;
        } else return command;
    }
    /////////////////////////////////////////////pars command
    public String[] parsComm2(String command) {
        String arguments = parsComm1(command);
        String argument = parsComm1(command);
        String[] string=arguments.split(" ");
        String[] string2=arguments.split("(\\w)-");
        // String g=string[0].toString();
        //String[] strin1=g.split("-");
        //string[0]=strin1[1];
        //String d=string[1].toString();
        //String[] strin=d.split("-");
        //string[1]=strin[1];
        //string[2]=string[1];
        //string[1]=string[0];
        System.out.println(string.length);
        System.out.println(string[0]);
        System.out.println(string[1]);
        //System.out.println(string[2]);
        return string2;
    }
    public String[] parsComm3(String str) {
        //byte[] string=new byte[3];
        //string[0]= command;
        //string[1]= String1[0];
        //string[2]= String1[1];
        String[] str1=str.split("(\\s)");
        return str1;
    }

    private byte[] rebuild(byte firstByte, byte[] byteArray) {
        byte[] rebuild = new byte[byteArray.length + 1];
        rebuild[0] = firstByte;
        for (int i = 1; i < rebuild.length; i++) {
            rebuild[i] = byteArray[i - 1];
        }
        return rebuild;
    }

    private byte[] serial(byte command, Object object) {
        try {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                 ObjectOutputStream os = new ObjectOutputStream(out)) {
                os.writeObject(object);
                byte[] bytes = out.toByteArray();
                return rebuild(command, bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T deserialize(byte[] data, int offset, Class<T> clazz){
        try {
            try (ByteArrayInputStream stream = new ByteArrayInputStream(data, offset, data.length - offset);
                 ObjectInputStream objectStream = new ObjectInputStream(stream)) {
                return (T) objectStream.readObject();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    ////////////////////////////////////



}