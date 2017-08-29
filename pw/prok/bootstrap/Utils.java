 package pw.prok.bootstrap;
 
 import java.io.File;
 
 public class Utils
 {
   public static String binToHex(byte[] bytes) {
     StringBuilder builder = new StringBuilder(bytes.length * 2);
     for (byte b : bytes)
       builder.append(String.format("%02X", new Object[] { Integer.valueOf(b & 0xFF) }));
     return builder.toString();
   }
   
   public static String readFile(File file) {
     try {
       java.io.InputStream is = new java.io.BufferedInputStream(new java.io.FileInputStream(file));
       java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is, "utf-8"));
       StringBuilder builder = new StringBuilder();
       String line;
       while ((line = reader.readLine()) != null) {
         builder.append(line).append('\n');
       }
       reader.close();
       return builder.toString();
     } catch (Exception e) {
       e.printStackTrace();
     }
     return null;
   }
   
   public static String sha1(File file) {
     return digest("SHA-1", file);
   }
   
   public static String md5(File file) {
     return digest("MD5", file);
   }
   
   public static String digest(String algorithm, File file) {
     try {
       java.security.MessageDigest md = java.security.MessageDigest.getInstance(algorithm.toUpperCase());
       java.io.InputStream is = new java.io.BufferedInputStream(new java.io.FileInputStream(file));
       byte[] buffer = new byte['က'];
       int c;
       while ((c = is.read(buffer)) > 0) {
         md.update(buffer, 0, c);
       }
       is.close();
       return binToHex(md.digest());
     } catch (Exception e) {
       e.printStackTrace(); }
     return null;
   }
   
   public static void removeDir(File dir)
   {
     if (dir == null) return;
     File[] files = dir.listFiles();
     if ((files != null) && (files.length != 0)) {
       for (File f : files) {
         if (f.isDirectory()) {
           removeDir(f);
         }
         f.delete();
       }
     }
     dir.delete();
   }
   
   public static void writeToFile(File file, String s) {
     try {
       file.getParentFile().mkdirs();
       java.io.OutputStream os = new java.io.FileOutputStream(file);
       java.io.Writer writer = new java.io.OutputStreamWriter(os, "utf-8");
       writer.write(s);
       writer.close();
       os.close();
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
   
   public static File checksumFile(String algo, File file) {
     return new File(file.getAbsolutePath() + "." + algo);
   }
   
   public static String readChecksum(String algo, File file) {
     File checksumFile = checksumFile(algo, file);
     if (!checksumFile.exists()) return null;
     String checksum = readFile(checksumFile);
     return checksum == null ? null : checksum.trim();
   }
   
   public static void writeChecksum(String algo, File file) {
     writeToFile(checksumFile(algo, file), digest(algo, file));
   }
   
   public static void copyFile(File in, File out) throws java.io.IOException {
     out.getParentFile().mkdirs();
     copyStream(new java.io.FileInputStream(in), new java.io.FileOutputStream(out));
   }
   
   private static void copyStream(java.io.InputStream inputStream, java.io.OutputStream outputStream) throws java.io.IOException {
     byte[] bytes = new byte['က'];
     int c;
     while ((c = inputStream.read(bytes)) > 0) {
       outputStream.write(bytes, 0, c);
     }
     outputStream.close();
     inputStream.close();
   }
 }
