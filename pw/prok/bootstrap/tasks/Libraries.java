/*    */ package pw.prok.bootstrap.tasks;
/*    */ 
/*    */ import java.io.File;
/*    */ import org.apache.commons.cli.Option;
/*    */ import pw.prok.bootstrap.Main;
/*    */ import pw.prok.damask.dsl.Builder;
/*    */ 
/*    */ public class Libraries extends DefaultTask
/*    */ {
/*    */   public void make()
/*    */   {
/* 12 */     File serverDir = getBinDir();
/* 13 */     File libraries = new File(serverDir, "libraries");
/* 14 */     for (String library : this.mMain.cli.getOptionValues(this.mMain.libraries.getOpt())) {
/* 15 */       pw.prok.bootstrap.Sync.syncArtifact(new pw.prok.bootstrap.LibraryArtifact(Builder.create().parse(library).asArtifact()), libraries, true);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\pw\prok\bootstrap\tasks\Libraries.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */