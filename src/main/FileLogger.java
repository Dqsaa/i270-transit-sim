package main;
import java.io.*;

public class FileLogger extends Logger {

    private FileWriter writer;
    public FileLogger( String fname ) throws java.io.IOException {
        File file = new File(fname);
        writer = new FileWriter( file );
    }

    public boolean endLogging() {
        try {
            writer.flush();
            writer.close();
            return false;
        } catch (java.io.IOException ex ) {
            return false;
        }
    }

    public boolean write( String str ) {
        try {
            writer.write( str + "\n" );
            return true;
        } catch (java.io.IOException ex ) {
            return false;
        }
    }
}
