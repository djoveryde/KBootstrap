/*    */ package pw.prok.bootstrap.tasks;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileFilter;
/*    */ import java.io.PrintStream;
/*    */ import pw.prok.bootstrap.LibraryArtifact;
/*    */ import pw.prok.bootstrap.Main;
/*    */ import pw.prok.damask.dsl.Builder;
/*    */ import pw.prok.damask.dsl.IArtifact;
/*    */ import pw.prok.damask.dsl.Version;
/*    */ 
/*    */ public class InstallKCauldron extends DefaultTask
/*    */ {
/* 14 */   private static final FileFilter VERSION_FILTER = new FileFilter()
/*    */   {
/*    */     public boolean accept(File file) {
/* 17 */       return file.isDirectory();
/*    */     }
/*    */   };
/*    */   
/*    */   public void make() throws Exception
/*    */   {
/* 23 */     File serverDir = getServerDir();
/* 24 */     File binDir = getBinDir();
/* 25 */     String artifactNotation = this.mMain.cli.getOptionValue(this.mMain.installKCauldron.getLongOpt());
/* 26 */     make(serverDir, binDir, artifactNotation);
/*    */   }
/*    */   
/*    */   public static File make(File serverDir, File binDir, String artifactNotation) throws Exception {
/* 30 */     artifactNotation = shorthand(artifactNotation);
/* 31 */     IArtifact artifact = Builder.create().parse(artifactNotation).asArtifact();
/* 32 */     LibraryArtifact jar = null;
/* 33 */     System.out.print("Resolve KCauldron version... ");
/*    */     try {
/* 35 */       artifact = Builder.create().fromArtifact(artifact).fromModuleVersion(pw.prok.damask.Damask.get().versionList(artifact).getLatestVersion()).asArtifact();
/* 36 */       jar = findJar(binDir, artifact);
/* 37 */       System.out.println("SUCCESS: " + artifact.getVersion());
/*    */     } catch (Exception ignored) {
/* 39 */       System.out.print("FAILED\nTrying to find latest local version... ");
/* 40 */       jar = findJar(binDir, artifact);
/* 41 */       if (jar != null) {
/* 42 */         System.out.println("FOUND: " + jar.getArtifact().getVersion().toRawString() + "\nSo we're found something, attempting to launch...");
/* 43 */         artifact = jar.getArtifact();
/*    */       } else {
/* 45 */         System.out.println("FAILED\nNothing to launch ;( Goodbye!");
/*    */       }
/*    */     }
/* 48 */     System.out.println("Server directory: " + serverDir.getAbsolutePath());
/* 49 */     if (jar == null) {
/* 50 */       jar = new LibraryArtifact(artifact, new File(binDir, Builder.asPath(artifact, true, true)));
/*    */     }
/* 52 */     File file = pw.prok.bootstrap.Sync.syncArtifact(jar, binDir, true);
/* 53 */     if (file == null) {
/* 54 */       throw new IllegalStateException("Could not install libraries");
/*    */     }
/* 56 */     DefaultTask.postInstall(serverDir, file);
/* 57 */     return file;
/*    */   }
/*    */   
/*    */   private static LibraryArtifact findJar(File binDir, IArtifact artifact) {
/* 61 */     if (artifact.getVersion().isSpecified()) {
/* 62 */       File f = new File(binDir, Builder.asPath(artifact, true, true));
/* 63 */       return f.exists() ? new LibraryArtifact(artifact, f) : null;
/*    */     }
/* 65 */     File dir = new File(binDir, Builder.asPath(artifact, false, false));
/* 66 */     if (!dir.exists()) return null;
/* 67 */     File[] versionDirs = dir.listFiles(VERSION_FILTER);
/* 68 */     if ((versionDirs == null) || (versionDirs.length == 0)) return null;
/* 69 */     Version maxVersion = null;
/* 70 */     for (File file : versionDirs) {
/* 71 */       Version version = new Version(file.getName());
/* 72 */       if ((maxVersion == null) || (version.compareTo(maxVersion) > 0)) {
/* 73 */         maxVersion = version;
/*    */       }
/*    */     }
/* 76 */     if (maxVersion != null) {
/* 77 */       artifact = Builder.create().fromArtifact(artifact).version(maxVersion).asArtifact();
/* 78 */       File f = new File(binDir, Builder.asPath(artifact, true, true));
/* 79 */       return f.exists() ? new LibraryArtifact(artifact, f) : null;
/*    */     }
/* 81 */     return null;
/*    */   }
/*    */   
/*    */   private static String shorthand(String s) {
/* 85 */     if ((s == null) || ("latest".equals(s))) {
/* 86 */       return "pw.prok:KCauldron:0+";
/*    */     }
/* 88 */     if (s.startsWith("backport-")) {
/* 89 */       return String.format("pw.prok:KCauldron-Backport-%s:0+", new Object[] { s.substring("backport-".length()) });
/*    */     }
/* 91 */     return s;
/*    */   }
/*    */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\pw\prok\bootstrap\tasks\InstallKCauldron.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */