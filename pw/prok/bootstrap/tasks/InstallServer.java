/*    */ package pw.prok.bootstrap.tasks;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import pw.prok.bootstrap.LibraryArtifact;
/*    */ import pw.prok.bootstrap.Sync.KCauldronInfo;
/*    */ 
/*    */ public class InstallServer extends DefaultTask
/*    */ {
/*    */   public void make() throws Exception
/*    */   {
/* 12 */     File serverDir = getServerDir();
/* 13 */     File binDir = getBinDir();
/* 14 */     File serverJar = new File(this.mMain.cli.getOptionValue(this.mMain.installServer.getOpt())).getCanonicalFile();
/* 15 */     if (!serverJar.exists()) {
/* 16 */       System.err.println("Server file not exists: " + serverJar);
/* 17 */       return;
/*    */     }
/* 19 */     make(serverDir, binDir, serverJar);
/*    */   }
/*    */   
/*    */   public static File make(File serverDir, File binDir, File serverJar) throws Exception {
/* 23 */     System.out.println("Server directory: " + serverDir.getAbsolutePath());
/* 24 */     File targetServerBin = serverDir;
/*    */     
/* 26 */     Sync.KCauldronInfo info = pw.prok.bootstrap.Sync.getInfo(serverJar);
/* 27 */     if (info == null)
/* 28 */       throw new RuntimeException("Couldn't resolve main jar dependencies. Are you sure this correct server jar?");
/* 29 */     targetServerBin = binDir;
/* 30 */     File targetServerJar = new LibraryArtifact(info.group, info.channel, info.version).getTarget(targetServerBin);
/* 31 */     if (!targetServerJar.getCanonicalPath().equals(serverJar.getCanonicalPath())) {
/* 32 */       pw.prok.bootstrap.Utils.copyFile(serverJar, targetServerJar);
/*    */     }
/* 34 */     if (!pw.prok.bootstrap.Sync.sync(targetServerJar, targetServerBin, true)) {
/* 35 */       throw new IllegalStateException("Could not install libraries");
/*    */     }
/* 37 */     DefaultTask.postInstall(serverDir, targetServerJar);
/* 38 */     return targetServerJar;
/*    */   }
/*    */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\pw\prok\bootstrap\tasks\InstallServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */