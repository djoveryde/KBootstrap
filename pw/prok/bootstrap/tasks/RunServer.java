/*    */ package pw.prok.bootstrap.tasks;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ public class RunServer extends DefaultTask
/*    */ {
/*    */   public void make() throws Exception {
/*  8 */     File serverDir = getServerDir();
/*  9 */     File binDir = getBinDir();
/* 10 */     File serverJar = new File(this.mMain.cli.getOptionValue(this.mMain.runServer.getLongOpt()));
/* 11 */     runServer(InstallServer.make(serverDir, binDir, serverJar), serverDir);
/*    */   }
/*    */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\pw\prok\bootstrap\tasks\RunServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */