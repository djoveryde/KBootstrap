/*    */ package pw.prok.bootstrap.tasks;
/*    */ 
/*    */ import com.sk89q.warmroast.WarmRoast;
/*    */ import com.sun.tools.attach.VirtualMachine;
/*    */ import java.io.File;
/*    */ 
/*    */ public class WarmRoastExecutor implements Runnable
/*    */ {
/*    */   private final String address;
/*    */   private final int port;
/*    */   private final int pid;
/*    */   private final File mappingsDir;
/*    */   
/*    */   public WarmRoastExecutor(String... args)
/*    */   {
/* 16 */     this.address = args[0];
/* 17 */     this.port = Integer.parseInt(args[1]);
/* 18 */     this.pid = Integer.parseInt(args[2]);
/* 19 */     this.mappingsDir = new File(args[3]);
/*    */   }
/*    */   
/*    */   public static void main(String... args) {
/* 23 */     new Thread(new WarmRoastExecutor(args)).start();
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try {
/* 29 */       java.net.InetSocketAddress socketAddress = new java.net.InetSocketAddress(this.address, this.port);
/* 30 */       VirtualMachine vm = VirtualMachine.attach(String.valueOf(this.pid));
/* 31 */       WarmRoast warmRoast = new WarmRoast(vm, 100);
/* 32 */       warmRoast.getMapping().read(new File(this.mappingsDir, "joined.srg"), new File(this.mappingsDir, "methods.csv"));
/* 33 */       warmRoast.connect();
/* 34 */       warmRoast.start(socketAddress);
/*    */     } catch (Exception e) {
/* 36 */       new RuntimeException("Failed to run warmroast", e).printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\pw\prok\bootstrap\tasks\WarmRoastExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */