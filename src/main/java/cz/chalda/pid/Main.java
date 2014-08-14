package cz.chalda.pid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.chalda.pid.getter.GetPid;

/**
 * Starting new process via Process Builder and trying to find it's pid
 * specifically based on platform.
 *
 */
public class Main {
  public static void main(String[] args) throws IOException {

    ProcessStarter starter = new ProcessStarter();
    Process process;
    
    if (Platform.isUnixLike()) {
      String[] command = new String[] {"/bin/sh", "-c", "while(true); do echo \"Sleeping...\"; sleep 10; done"};
      process = starter.start(command);
      System.out.println("Pid of the underlined process is: " + GetPid.getUnixPid(process));
    } else {
      String[] command = new String[] {"echo", "\"A\""};
      process = starter.start(command);
      System.out.println("Pid of the underlined process is: " + GetPid.getWindowsPid(process));
    }

    System.out.println("Waiting for character 'q' to end the program");
    String inputString = "";
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while(!"q".equals(inputString.trim())) {
      System.out.println("Waiting for 'q'");
      inputString = br.readLine();
    }
    process.destroy();
  }
}
