package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * To handle logic which interacts with the operating system. The runCommand
 * method sets up a StringBuffer to store standard out and another StringBuffer
 * to store standard error. It then calls the Runtime.exec() to create a
 * process by method passing to the exec() method the command. It then gets
 * the standard out stream from the process as an InputStream, and spawns a
 * new thread passing the new thread the StringBuffer and the InputStream.
 *
 * The new thread processes the InputStream. It creates an InputStreamReader
 * and pass it the InputStream. It then creates a BufferedReader passing it
 * the InputStreamReader. Now loop in the BufferedReader getting a
 * line at a time and appending the line to the StringBuffer. Because
 * the StringBuffer came from the client, the client now has standard
 * out in a StringBuffer.
 *
 * This process is repeated for standard out and the runCommand does a join
 * on the two threads so that it does not return until both stdout and stderr
 * have been processed (appended to a StringBuffer)
 *
 * @author  yan, rox, tom
 */
public class ProcessHandler {

    private static Long pid = null;
    private static Logger logger = null;

    static {
        logger = Logger.getLogger(new CurrentClassGetter().getClassName());
    }



    /**
     * Execute a runtime command. This handles standard out and standard error
     * from the operating system process smartly. That is, it uses two threads,
     * one for each of stdout and stderr and reads these buffers concurrently
     * thus avoiding possible buffer overflow.
     * @param command The command to run.
     * @return The process id (pid) if the running process.
     * @throws IOException
     * @throws InterruptedException
     */
    public static int runCommand(String command)
            throws IOException, InterruptedException {
        return runCommand(command, null, null);
    }

    /**
     * Execute a runtime command. This handles standard out and standard error
     * from the operating system process smartly. That is, it uses two threads,
     * one for each of stdout and stderr and reads these buffers concurrently
     * thus avoiding possible buffer overflow.
     * @param command The command to run.
     * @param stdOut A buffer to put the stdout string into.
     * @return The process id (pid) of the running process.
     * @throws IOException
     * @throws InterruptedException
     */
    public static int runCommand(String command, StringBuffer stdOut)
            throws IOException, InterruptedException {
        return runCommand(command, stdOut, null);
    }

    /**
     * Execute a runtime command. This handles standard out and standard error
     * from the operating system process smartly. That is, it uses two threads,
     * one for each of stdout and stderr and reads these buffers concurrently
     * thus avoiding possible buffer overflow.
     * @param command The command to run.
     * @param stdOut A buffer to put the stdout string into.
     * @param stdErr A buffer to put the stderr string into.
     * @return The process id (pid) of the running process.
     * @throws IOException
     * @throws InterruptedException
     */
    public static int runCommand(String command, StringBuffer stdOut,
            StringBuffer stdErr) throws IOException, InterruptedException {
        return runCommand((Object) command, stdOut, stdErr);
    }

    /**
     * Execute a runtime command. This handles standard out and standard error
     * from the operating system process smartly. That is, it uses two threads,
     * one for each of stdout and stderr and reads these buffers concurrently
     * thus avoiding possible buffer overflow
     * @param command The command to run.
     * @return The process id (pid) of the running process.
     * @throws IOException
     * @throws InterruptedException
     */
    public static int runCommand(String[] command)
            throws IOException, InterruptedException {
        return runCommand(command, null, null);
    }

    /**
     * Execute a runtime command. This handles standard out and standard error
     * from the operating system process smartly. That is, it uses two threads,
     * one for each of stdout and stderr and reads these buffers concurrently
     * thus avoiding possible buffer overflow.
     * @param command The command to execute.
     * @param stdOut A buffer to put the stdout string into.
     * @return The process id (pid) of the running process
     * @throws IOException
     * @throws InterruptedException
     */
    public static int runCommand(String[] command, StringBuffer stdOut)
            throws IOException, InterruptedException {
        return runCommand(command, stdOut, null);
    }

    /**
     * Execute a runtime command. This handles standard out and standard error
     * from the operating system process smartly. That is, it uses two threads,
     * one for each of stdout and stderr and reads these buffers concurrently
     * thus avoiding possible buffer overflow
     * @param command The command to execute
     * @param stdOut A buffer to put the stdout string into.
     * @param stdErr A buffer to put the std error string into.
     * @return The pid of the running process.
     * @throws IOException
     * @throws InterruptedException
     */
    public static int runCommand(String[] command, StringBuffer stdOut,
            StringBuffer stdErr) throws IOException, InterruptedException {
        return runCommand((Object) command, stdOut, stdErr);
    }

    /**
     * Execute a runtime command. This handles standard out and standard error
     * from the operating system process smartly. That is, it uses two threads,
     * one for each of stdout and stderr and reads these buffers concurrently
     * thus avoiding possible buffer overflow
     * @param command The command to execute.
     * @param stdOut A buffer to put the stdout string into.
     * @param stdErr A buffer to put the stederr string into.
     * @return The process id (pid) of the running process
     * @throws IOException
     * @throws InterruptedException
     */
    private static int runCommand(Object command, StringBuffer stdOut,
            StringBuffer stdErr) throws IOException, InterruptedException {
        Process proc = null;

        if (stdOut == null) {
            stdOut = new StringBuffer();
        }

        if (stdErr == null) {
            stdErr = new StringBuffer();
        }

        try {
            if (command instanceof String) {
                proc = Runtime.getRuntime().exec((String) command);
            } else if (command instanceof String[]) {
                proc = Runtime.getRuntime().exec((String[]) command);
            }

            InputStream is = proc.getInputStream();
            StreamGobbler stdOutThread = new StreamGobbler(is, stdOut);
            stdOutThread.start();

            InputStream es = proc.getErrorStream();
            StreamGobbler stdErrThread = new StreamGobbler(es, stdErr);
            stdErrThread.start();

            stdOutThread.join();
            stdErrThread.join();
            int retVal = proc.waitFor();

            return retVal;
        } catch (RuntimeException re) {
            logger.log(Level.SEVERE,
                    "runCommand failed on command " + command,
                    re);
            throw re;
        } catch (IOException ioe) {
            logger.log(Level.SEVERE,
                    "runCommand failed on command " + command,
                    ioe);
            throw ioe;
        } catch (InterruptedException ie) {
            logger.log(Level.SEVERE,
                    "runCommand failed on command " + command,
                    ie);
            throw ie;
        } finally {
            if (proc != null) {
                try {
                    proc.getOutputStream().close();
                    proc.getInputStream().close();
                    proc.getErrorStream().close();
                    proc.destroy();
                } catch (IOException ioe) {
                    logger.log(Level.SEVERE, "Error closing process streams", ioe);
                }
            }
        }
    }

    /**
     * Get the process id for the current process.
     * @return Long - the process id of this program's process
     */
    public static Long getPid() {
        if (pid == null) {
            Process p = null;
            try {
                String[] cmd = {"/bin/bash", "-c", "echo $PPID"};
                p = Runtime.getRuntime().exec(cmd);

                InputStreamReader isr = new InputStreamReader(p.getInputStream());
                BufferedReader br = new BufferedReader(isr);

                pid = new Long(br.readLine());
            } catch (Exception ire) {
                // Error executing getPid(). Using random number...
            } finally {
                if (p != null) {
                    try {
                        p.getOutputStream().close();
                        p.getInputStream().close();
                        p.getErrorStream().close();
                        p.destroy();
                    } catch (IOException ioe) {
                        logger.log(Level.SEVERE, "Error closing process streams.", ioe);
                    }
                }
            }

            if (pid == null) {
                Random generator = new Random();
                pid = new Long(generator.nextLong());
                logger.log(Level.SEVERE, "Error trying to get PID. " +
                        "Using random number (" + pid + ") instead.");
            }
        }

        return pid;
    }

    /**
     * Thread used to read an input stream Outer class launches one thread for each thread
     * that might be written by a launched process - stdErr and stdOut.
     */
    private static class StreamGobbler extends Thread {

        InputStream is;
        StringBuffer buffer;

        /**
         * Create a new StreamGobbler object to read an input stream.
         * @param is InputStream containing a reference to the InputStream to be read
         * buffer
         //* @param StringBuffer containing a String buffer which the contents of
         * the input stream should be appended to.
         */
        public StreamGobbler(InputStream is, StringBuffer buffer) {
            this.is = is;
            this.buffer = buffer;
        }

        /**
         * Process the input stream in this thead. Create an InputStreamReader
         * and pass it the InputStream. Create a BufferedReader passing it
         * the InputStreamReader. Now loop in the BufferedReader getting a
         * line at a time and appending the line to the StringBuffer. Because
         * the StringBuffer came from the client, the client now has standard
         * out in a StringBuffer.
         */
        public void run() {

            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;

                while ((line = br.readLine()) != null) {
                    this.buffer.append(line + "\n");
                }

                br.close();
                br = null;
            } catch (IOException ioe) {
                logger.log(Level.SEVERE,
                        "Problem reading stdout or stdin from process.",
                        ioe);
            }
        }
    }

    /**
     * basic utility class to help empty the buffer as a stream.
     * @param sb
     * @return
     */
    public static String readline(StringBuffer sb){
        if(sb.length() < 1){
            return null;
        }else {
            int pos = sb.indexOf("\n");
            if(pos <= 0) return null;
            else{
                String line = sb.substring(0, pos);
                //System.err.println(pos);
                //System.err.println(line);
                sb.delete(0,pos+1);
                return line;
            }
        }
    }

    public static class CurrentClassGetter extends SecurityManager {

        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }
}
