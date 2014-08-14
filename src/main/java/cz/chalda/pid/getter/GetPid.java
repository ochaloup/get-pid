package cz.chalda.pid.getter;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import com.sun.jna.Pointer;

public class GetPid {
  private static final Logger log = Logger.getLogger(GetPid.class.getName());

  public static int getWindowsPid(Process process) {
    int pid = -1;

    if (process.getClass().getName().equals("java.lang.Win32Process")
        || process.getClass().getName().equals("java.lang.ProcessImpl")) {
      /* determine the pid on windows plattforms */
      try {
        Field f = process.getClass().getDeclaredField("handle");
        f.setAccessible(true);
        long handl = f.getLong(process);

        Kernel32 kernel = Kernel32.INSTANCE;
        W32API.HANDLE handle = new W32API.HANDLE();
        handle.setPointer(Pointer.createConstant(handl));
        pid = kernel.GetProcessId(handle);
      } catch (Throwable e) {
        log.severe("Troubles to get pid on windows " + e + ":" + e.getMessage());
      }
    }
    return pid;
  }
  
  public static int getUnixPid(Process process) {
    int pid = -1;
    
    if(process.getClass().getName().equals("java.lang.UNIXProcess")) {
      /* get the PID on unix/linux systems */
      try {
        Field f = process.getClass().getDeclaredField("pid");
        f.setAccessible(true);
        pid = f.getInt(process);
      } catch (Throwable e) {
        log.severe("Troubles to get pid on windows " + e + ":" + e.getMessage());
      }
    }
    
    return pid;
  }
}
