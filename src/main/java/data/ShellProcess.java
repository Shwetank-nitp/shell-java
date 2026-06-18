package data;

public class ShellProcess {
    private final CommandContext context;
    private final long processId;
    private boolean isCompleted;

    private boolean REPLflag;

    private String error;
    private String output;

    public ShellProcess(CommandContext context, long processId, boolean isCompleted, boolean REPLflag) {
        this.context = context;
        this.processId = processId;
        this.isCompleted = isCompleted;
        this.REPLflag = REPLflag;
    }

    public CommandContext getContext() {
        return context;
    }

    public long getProcessId() {
        return processId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public boolean isREPLflag() {
        return REPLflag;
    }

    public void setREPLflag(boolean REPLflag) {
        this.REPLflag = REPLflag;
    }
}