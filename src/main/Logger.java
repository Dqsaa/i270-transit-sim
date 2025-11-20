package main;
// The Logger class hierarchy allows the construction of a logging object
// which can then be made available through a static method.
// An instantiable class is provided to log to a file.
abstract public class Logger {

    private static Logger defaultLogger = null;
    public static void setDefaultLogger( Logger logger ) {
        defaultLogger = logger;
    }
    public static Logger getDefaultLogger() {
        return defaultLogger;
    }
    public static void log( String str ) {
        if (getDefaultLogger() != null) {
            getDefaultLogger().write( str );
        }
    }

    abstract public boolean write( String str );

    abstract public boolean endLogging();

}
