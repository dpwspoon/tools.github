package org.kaazing.github;

public abstract class SubCommand {

    private String[] args = new String[]{};

    /**
     * Name of the command as ran from the command line
     * @return
     */
    public abstract String getName();

    /**
     * Description of what the command does
     * @return
     */
    public abstract String getDescription();

    public String[] getArgs() {
        return args;
    }

    /**
     * Args passed into the command
     * @param args
     */
    public void setArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim();
        }
        this.args = args;
    }

    /**
     * List args that are required
     * @return
     */
    public abstract String listArgs();
}
