package cz.chalda.pid;

import java.io.File;
import java.util.regex.Pattern;


public class Platform {
  public static final int PLATFORM_i386 = 32;
  public static final int PLATFORM_x64 = 64;
  public static final int PLATFORM_SPARC64 = 66;
  public static final String EXPLICIT_DATA_MODEL_THRESHOLD = "MANU_EXPLICIT_DATA_MODEL_THRESHOLD";

  private static String osName = System.getProperty("os.name");
  private static String osArch = System.getProperty("os.arch");
    private static String tmpDir = System.getProperty("java.io.tmpdir");

  static {
    if (isWindows()) {
      // on newer windows java.io.tmpdir will probably point to something like c:\Users\<userName>\...
      if (new File("C:\\temp").exists()) {
          tmpDir = "C:\\temp";
      } else if (new File("C:\\tmp").exists()) {
          tmpDir = "C:\\tmp";
      }
    }
  }

  public static boolean isWindows() {
      return isMatch(osName, ".*[Ww]indows.*");
  }

  public static boolean isLinux() {
      return isMatch(osName, "[Ll]inux.*");
  }

  public static boolean isSolaris() {
      return osName.equals("SunOS");
  }

  public static boolean isHP() {
      return osName.equals("HP-UX");
  }

  public static boolean isX64() {
      return osArch.equals("amd64");
  }

  public static boolean isX86() {
      return osArch.equals("x386") || osArch.equals("x86") || osArch.equals("i386");
  }

  public static boolean isSparc() {
      return osArch.equals("sparc") || osArch.equals("sparcv9");
  }

  public static boolean isPpc64() {
      return osArch.equals("ppc64");
  }
  
  public static String getTmpDir() {
    return tmpDir;
  }

  public static boolean isUnixLike() {
    return (isSolaris() || isHP() || isLinux());
 }
  
  /**
   * Does stringToCheck match the regexp?
   */
  private static boolean isMatch(String stringToCheck, String regexp) {
      return Pattern.compile(regexp).matcher(stringToCheck).find();
  }
}
