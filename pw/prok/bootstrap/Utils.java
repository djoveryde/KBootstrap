/*     */ package pw.prok.bootstrap;
/*     */ 
/*     */ import java.io.File;
/*     */ 
/*     */ public class Utils
/*     */ {
/*     */   public static String binToHex(byte[] bytes) {
/*   8 */     StringBuilder builder = new StringBuilder(bytes.length * 2);
/*   9 */     for (byte b : bytes)
/*  10 */       builder.append(String.format("%02X", new Object[] { Integer.valueOf(b & 0xFF) }));
/*  11 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static String readFile(File file) {
/*     */     try {
/*  16 */       java.io.InputStream is = new java.io.BufferedInputStream(new java.io.FileInputStream(file));
/*  17 */       java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is, "utf-8"));
/*  18 */       StringBuilder builder = new StringBuilder();
/*     */       String line;
/*  20 */       while ((line = reader.readLine()) != null) {
/*  21 */         builder.append(line).append('\n');
/*     */       }
/*  23 */       reader.close();
/*  24 */       return builder.toString();
/*     */     } catch (Exception e) {
/*  26 */       e.printStackTrace();
/*     */     }
/*  28 */     return null;
/*     */   }
/*     */   
/*     */   public static String sha1(File file) {
/*  32 */     return digest("SHA-1", file);
/*     */   }
/*     */   
/*     */   public static String md5(File file) {
/*  36 */     return digest("MD5", file);
/*     */   }
/*     */   
/*     */   public static String digest(String algorithm, File file) {
/*     */     try {
/*  41 */       java.security.MessageDigest md = java.security.MessageDigest.getInstance(algorithm.toUpperCase());
/*  42 */       java.io.InputStream is = new java.io.BufferedInputStream(new java.io.FileInputStream(file));
/*  43 */       byte[] buffer = new byte['က'];
/*     */       int c;
/*  45 */       while ((c = is.read(buffer)) > 0) {
/*  46 */         md.update(buffer, 0, c);
/*     */       }
/*  48 */       is.close();
/*  49 */       return binToHex(md.digest());
/*     */     } catch (Exception e) {
/*  51 */       e.printStackTrace(); }
/*  52 */     return null;
/*     */   }
/*     */   
/*     */   public static void removeDir(File dir)
/*     */   {
/*  57 */     if (dir == null) return;
/*  58 */     File[] files = dir.listFiles();
/*  59 */     if ((files != null) && (files.length != 0)) {
/*  60 */       for (File f : files) {
/*  61 */         if (f.isDirectory()) {
/*  62 */           removeDir(f);
/*     */         }
/*  64 */         f.delete();
/*     */       }
/*     */     }
/*  67 */     dir.delete();
/*     */   }
/*     */   
/*     */   public static void writeToFile(File file, String s) {
/*     */     try {
/*  72 */       file.getParentFile().mkdirs();
/*  73 */       java.io.OutputStream os = new java.io.FileOutputStream(file);
/*  74 */       java.io.Writer writer = new java.io.OutputStreamWriter(os, "utf-8");
/*  75 */       writer.write(s);
/*  76 */       writer.close();
/*  77 */       os.close();
/*     */     } catch (Exception e) {
/*  79 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static File checksumFile(String algo, File file) {
/*  84 */     return new File(file.getAbsolutePath() + "." + algo);
/*     */   }
/*     */   
/*     */   public static String readChecksum(String algo, File file) {
/*  88 */     File checksumFile = checksumFile(algo, file);
/*  89 */     if (!checksumFile.exists()) return null;
/*  90 */     String checksum = readFile(checksumFile);
/*  91 */     return checksum == null ? null : checksum.trim();
/*     */   }
/*     */   
/*     */   public static void writeChecksum(String algo, File file) {
/*  95 */     writeToFile(checksumFile(algo, file), digest(algo, file));
/*     */   }
/*     */   
/*     */   public static void copyFile(File in, File out) throws java.io.IOException {
/*  99 */     out.getParentFile().mkdirs();
/* 100 */     copyStream(new java.io.FileInputStream(in), new java.io.FileOutputStream(out));
/*     */   }
/*     */   
/*     */   private static void copyStream(java.io.InputStream inputStream, java.io.OutputStream outputStream) throws java.io.IOException {
/* 104 */     byte[] bytes = new byte['က'];
/*     */     int c;
/* 106 */     while ((c = inputStream.read(bytes)) > 0) {
/* 107 */       outputStream.write(bytes, 0, c);
/*     */     }
/* 109 */     outputStream.close();
/* 110 */     inputStream.close();
/*     */   }
/*     */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\pw\prok\bootstrap\Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */