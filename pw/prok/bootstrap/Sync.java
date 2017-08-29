/*     */ package pw.prok.bootstrap;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ public class Sync
/*     */ {
/*     */   public static class KCauldronInfo
/*     */   {
/*     */     public final boolean kcauldron;
/*     */     public final String group;
/*     */     public final String channel;
/*     */     public final String version;
/*     */     public final String[] classpath;
/*     */     
/*     */     public KCauldronInfo(boolean kcauldron, String group, String channel, String version, String[] classpath)
/*     */     {
/*  23 */       this.kcauldron = kcauldron;
/*  24 */       this.group = group;
/*  25 */       this.channel = channel;
/*  26 */       this.version = version;
/*  27 */       this.classpath = classpath;
/*     */     }
/*     */   }
/*     */   
/*     */   public static KCauldronInfo getInfo(File jar) {
/*  32 */     boolean kcauldron = false;
/*  33 */     String group = null;
/*  34 */     String channel = null;
/*  35 */     String version = null;
/*  36 */     String[] classpath = null;
/*     */     try {
/*  38 */       ZipFile serverZip = new ZipFile(jar);
/*  39 */       java.util.zip.ZipEntry entry = serverZip.getEntry("META-INF/MANIFEST.MF");
/*  40 */       if (entry == null) return null;
/*  41 */       java.io.InputStream is = serverZip.getInputStream(entry);
/*  42 */       Manifest manifest = new Manifest(is);
/*  43 */       is.close();
/*  44 */       serverZip.close();
/*  45 */       Attributes attributes = manifest.getMainAttributes();
/*  46 */       String prefix = "KCauldron";
/*  47 */       if (attributes.getValue("KBootstrap-Implementation") != null)
/*  48 */         prefix = attributes.getValue("KBootstrap-Implementation");
/*  49 */       if (attributes.getValue(prefix + "-Version") != null) {
/*  50 */         kcauldron = true;
/*  51 */         version = attributes.getValue(prefix + "-Version");
/*  52 */         channel = attributes.getValue(prefix + "-Channel");
/*  53 */         group = attributes.getValue(prefix + "-Group");
/*  54 */         if (group == null) group = "pw.prok";
/*     */       } else {
/*  56 */         return null;
/*     */       }
/*  58 */       String cp = attributes.getValue("Class-Path");
/*  59 */       classpath = cp == null ? new String[0] : cp.split(" ");
/*     */     } catch (Exception e) {
/*  61 */       e.printStackTrace();
/*     */     }
/*  63 */     return new KCauldronInfo(kcauldron, group, channel, version, classpath);
/*     */   }
/*     */   
/*     */   public static void parseLibraries(File jar, List<LibraryArtifact> artifacts) {
/*  67 */     KCauldronInfo info = getInfo(jar);
/*  68 */     if (info == null) return;
/*  69 */     String[] cp = info.classpath;
/*  70 */     if (cp == null) return;
/*  71 */     for (String s : cp)
/*  72 */       if ("minecraft_server.1.7.10.jar".equals(s)) {
/*  73 */         artifacts.add(new LibraryArtifact("net.minecraft", "server", "1.7.10", ".", "minecraft_server.1.7.10.jar"));
/*     */       }
/*     */       else {
/*  76 */         boolean legacy = s.startsWith("libraries/");
/*  77 */         if (legacy) {
/*  78 */           s = s.substring("libraries/".length());
/*     */         }
/*  80 */         int c = s.lastIndexOf('/');
/*  81 */         String filename = s.substring(c + 1);
/*  82 */         s = s.substring(0, c);
/*  83 */         String version = s.substring((c = s.lastIndexOf('/')) + 1).trim();
/*  84 */         s = s.substring(0, c);
/*  85 */         String artifact = s.substring((c = s.lastIndexOf('/')) + 1).trim();
/*  86 */         s = s.substring(0, c);
/*  87 */         String group = s.replace("../", "").replace('/', '.');
/*  88 */         artifacts.add(new LibraryArtifact(group, artifact, version, legacy ? "libraries/<group>/<artifact>/<version>" : null, legacy ? filename : null));
/*     */       }
/*     */   }
/*     */   
/*     */   public static boolean sync(File serverFile, File rootDir, boolean recursive) {
/*  93 */     List<LibraryArtifact> artifacts = new ArrayList();
/*  94 */     parseLibraries(serverFile, artifacts);
/*  95 */     return sync(artifacts, rootDir, recursive);
/*     */   }
/*     */   
/*     */   public static boolean sync(List<LibraryArtifact> artifacts, File rootDir, boolean recursive) {
/*     */     try {
/* 100 */       for (LibraryArtifact artifact : artifacts) {
/* 101 */         if (syncArtifact(artifact, rootDir, recursive) == null) {
/* 102 */           return false;
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 106 */       e.printStackTrace();
/* 107 */       return false;
/*     */     }
/* 109 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean checksum(File artifactFile) {
/* 113 */     for (String algorithm : ALGORITHMS) {
/*     */       try {
/* 115 */         String digest = Utils.digest(algorithm, artifactFile);
/* 116 */         String checksum = Utils.readChecksum(algorithm, artifactFile);
/* 117 */         if ((digest == null) || (checksum == null) || (!digest.equalsIgnoreCase(checksum))) {
/* 118 */           return false;
/*     */         }
/*     */       } catch (Exception e) {
/* 121 */         e.printStackTrace();
/* 122 */         return false;
/*     */       }
/*     */     }
/* 125 */     return true;
/*     */   }
/*     */   
/*     */   public static File syncArtifact(LibraryArtifact artifact, File rootDir, boolean recursive) {
/* 129 */     File artifactFile = artifact.getTarget(rootDir);
/* 130 */     if ((!artifactFile.exists()) || (!checksum(artifactFile))) {
/* 131 */       System.out.print("Downloading " + artifact + "... ");
/*     */       try {
/* 133 */         artifactFile.getParentFile().mkdirs();
/* 134 */         pw.prok.damask.Damask.get().artifactResolve(artifact.getArtifact(), artifactFile, false);
/* 135 */         for (String algorithm : ALGORITHMS) {
/* 136 */           Utils.writeChecksum(algorithm, artifactFile);
/*     */         }
/* 138 */         System.out.println("DONE!");
/*     */       } catch (Exception e) {
/* 140 */         System.out.println("ERROR!");
/* 141 */         e.printStackTrace();
/* 142 */         return null;
/*     */       }
/*     */     }
/* 145 */     if (recursive) {
/* 146 */       Object artifacts = new ArrayList();
/* 147 */       parseLibraries(artifactFile, (List)artifacts);
/* 148 */       if (!sync((List)artifacts, rootDir, true)) {
/* 149 */         return null;
/*     */       }
/*     */     }
/* 152 */     return artifactFile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */   private static final String[] ALGORITHMS = { "sha-1", "md5" };
/*     */   
/*     */   public static void resolveLatestVersion(File basedir, LibraryArtifact lib) {}
/*     */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\pw\prok\bootstrap\Sync.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */