/*    */ package com.sk89q.warmroast;
/*    */ 
/*    */ import com.beust.jcommander.Parameter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RoastOptions
/*    */ {
/*    */   @Parameter(names={"-h", "--help"}, help=true)
/*    */   public boolean help;
/*    */   @Parameter(names={"--bind"}, description="The address to bind the HTTP server to")
/*    */   public String bindAddress;
/*    */   @Parameter(names={"-p", "--port"}, description="The port to bind the HTTP server to")
/*    */   public Integer port;
/*    */   @Parameter(names={"--pid"}, description="The PID of the VM to attach to")
/*    */   public Integer pid;
/*    */   @Parameter(names={"--name"}, description="The name of the VM to attach to")
/*    */   public String vmName;
/*    */   @Parameter(names={"-t", "--thread"}, description="Optionally specify a thread to log only")
/*    */   public String threadName;
/*    */   @Parameter(names={"-m", "--mappings"}, description="A directory with joined.srg and methods.csv")
/*    */   public String mappingsDir;
/*    */   
/*    */   public RoastOptions()
/*    */   {
/* 28 */     this.bindAddress = "0.0.0.0";
/*    */     
/*    */ 
/*    */ 
/* 32 */     this.port = Integer.valueOf(23000);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Parameter(names={"--interval"}, description="The sample rate, in milliseconds")
/* 47 */   public Integer interval = Integer.valueOf(100);
/*    */   @Parameter(names={"--timeout"}, description="The number of seconds before ceasing sampling (optional)")
/*    */   public Integer timeout;
/*    */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\com\sk89q\warmroast\RoastOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */