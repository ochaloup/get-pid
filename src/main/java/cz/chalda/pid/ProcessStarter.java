package cz.chalda.pid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;

public class ProcessStarter {
  private static final Logger log = Logger.getLogger(ProcessStarter.class.getName());

  public Process start(List<String> commandToRun) {
    return start(commandToRun.toArray(new String[0]));
  }
  
  public Process start(String[] commandToRun) {   
    // running the ProcessBuilder has some pitfalls
    // but for simple usecase this seems to be fine
    ProcessBuilder builder = new ProcessBuilder(commandToRun);
    // joining std out and std err to one output
    builder.redirectErrorStream(true);
    
    Process process;
    try {
      log.info("Starting new process with command: " + commandToRun);
      process = builder.start();
    } catch (IOException ioe) {
      log.severe("Not possible to start process " + ioe + " : " + ioe.getMessage());
      throw new RuntimeException(ioe);
    }
    
    // input stream means that it will read from command output
    readProcessOutput(process.getInputStream());
    
    return process;
  }
  
  /**
   * Starting thread which will read stream by buffered reader line and putting
   * data to queue.
   * The input stream has to be closed by somebody else.
   *
   * @param inputStream  stream to read
   * @return created started thread
   */
  private Thread readProcessOutput(final InputStream inputStream) {
      Thread thread = new Thread(new Runnable() {
          public void run() { 
              InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
              final BufferedReader br = new BufferedReader(inputStreamReader);
              
              String line;
              int lineNumber = 0;
              try {
                  while ((line = br.readLine()) != null) {
                    lineNumber++;
                    log.info("[" + inputStream + "]" + lineNumber + ": " + line);
                  }
              } catch(IOException ioe) {
                  // just log and go. Let user do something. 
                  log.severe("Not able to read from the buffered reader " + br + " Exception: " + ioe);
              } finally {
                  try {
                      inputStream.close();
                  } catch (IOException ioe) {
                      log.fine("Not able to close input stream " + ioe);
                  }
              }
          }
      }, "internal-process-output-reader");
      thread.start();
      return thread;
  }
}
