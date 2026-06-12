package error;

public class InvalidCommand extends RuntimeException {
    public InvalidCommand(String message) {
        super(message);
    }

    public static InvalidCommand notFound(String command) {
        return new InvalidCommand(command+": command not found");
    }
}
