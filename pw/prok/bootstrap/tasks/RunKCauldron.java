/*    */ package pw.prok.bootstrap.tasks;
/*    */ 
/*    */ import pw.prok.bootstrap.Main;
/*    */ 
/*    */ public class RunKCauldron extends DefaultTask
/*    */ {
/*    */   public void make() throws Exception {
/*  8 */     java.io.File serverDir = getServerDir();
/*  9 */     java.io.File binDir = getBinDir();
/* 10 */     String artifactNotation = this.mMain.cli.getOptionValue(this.mMain.runKCauldron.getLongOpt());
/* 11 */     runServer(InstallKCauldron.make(serverDir, binDir, artifactNotation), serverDir);
/*    */   }
/*    */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\pw\prok\bootstrap\tasks\RunKCauldron.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */