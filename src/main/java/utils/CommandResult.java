package utils;

public record CommandResult(String message, boolean REPLFlag) {

    public static CommandResult exit() {
        return new CommandResult(null, false);
    }

    public static CommandResult of(String message) {
        return new CommandResult(message, true);
    }
}