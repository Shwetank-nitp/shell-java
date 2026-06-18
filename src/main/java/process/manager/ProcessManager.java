package process.manager;

import data.CommandContext;
import data.ShellProcess;
import utils.CommandResult;

import java.util.List;

public interface ProcessManager {
    List<CommandResult> run(List<CommandContext> contexts);
    List<ShellProcess> getRunningProcess();
}
