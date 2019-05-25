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
    private static final byte SERVER_ERROR = 100;
    private static final byte SERIALIZATION_ERROR = 102;
    private static final byte INCORRECT_COMMAND = 103;
    private static final byte WRONG_PARAMS = 104;
    private boolean isCommand = false;
    private String comm;
    private Object command;
    private IServer server;

    private String pathFile = "C:\\Users\\Admin\\Idea\\";
    private boolean WEcho = false;
    //////////////////////////////////////////////////////////////////////////////////////
    public byte[] perform(String text_from_client) {

        return CommPars(TextPars(text_from_client));
    }
    //////////////////////////////////////////////////////////////////////////////////////////

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
            case SERVER_ERROR:
                return "SERVER_ERROR";
            default: {
                return new String(message);
            }}
    }
    //////////////////////////////////////////////////////////////////////////////File send

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
        return string2;
    }
    public String[] parsComm3(String str) {
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