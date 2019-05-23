package client;

public enum Rmi_commands {
    CMD_PING("^%(ping):?"),
    CMD_ECHO("^%(echo):?"),
    CMD_LOGIN("^%(log)?:((log)(-)(.)+)(\\s)((pass)+(-)+(.)+)+;"),
    CMD_LIST("^%list:?"),
    CMD_MSG("^%(msg):?\\s((log)(-)(.)+)\\s((m)(-)(.)+)+;"),
    CMD_RECIVE_MSG("^%(rec):?"),
    CMD_FILE("^%(file):?\\s((l)(-|\\s)(.)+)\\s(f)(-|\\s)(.)+;"),
    CMD_RECIVE_FILE("^%(fileres):?"),
    CMD_PROCESS("^%(process):?(\\s)((f)(-)(.)+)(\\s)((to)(-)(.)+)+;"),
    EXIT("^%(ex):?");


    private final String reg;

    //construtor
    Rmi_commands(final String reg) {
        this.reg = reg;
    }

    public String getReg() {
        return reg;
    }
}

