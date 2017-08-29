/*     */ package pw.prok.bootstrap;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.commons.cli.CommandLine;
/*     */ import org.apache.commons.cli.CommandLineParser;
/*     */ import org.apache.commons.cli.HelpFormatter;
/*     */ import org.apache.commons.cli.Option;
/*     */ import org.apache.commons.cli.Options;
/*     */ import org.apache.commons.cli.ParseException;
/*     */ import pw.prok.bootstrap.tasks.DefaultTask;
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   public final Options options;
/*     */   public final Option serverDir;
/*     */   public final Option binDir;
/*     */   public final Option jvmArgs;
/*     */   public final Option serverSymlinks;
/*     */   public final Option pidFile;
/*     */   public final Option warmRoast;
/*     */   public final Option installKCauldron;
/*     */   public final Option runKCauldron;
/*     */   public final Option installServer;
/*     */   public final Option runServer;
/*     */   public final Option libraries;
/*     */   public final CommandLineParser parser;
/*     */   public final HelpFormatter helpFormatter;
/*     */   public CommandLine cli;
/*  29 */   private boolean wasExecuted = false;
/*     */   public static Main instance;
/*     */   
/*     */   public Main()
/*     */   {
/*  34 */     this.options = new Options();
/*     */     
/*  36 */     this.serverDir = new Option("d", "serverDir", true, "Server root directory");
/*  37 */     this.serverDir.setArgName("dir");
/*  38 */     this.options.addOption(this.serverDir);
/*     */     
/*  40 */     this.binDir = new Option("b", "binDir", true, "Server bin directory");
/*  41 */     this.binDir.setArgName("dir");
/*  42 */     this.options.addOption(this.binDir);
/*     */     
/*  44 */     this.jvmArgs = new Option("j", "jvmArg", true, "Server's JVM arguments");
/*  45 */     this.jvmArgs.setArgName("args");
/*  46 */     this.options.addOption(this.jvmArgs);
/*     */     
/*  48 */     this.serverSymlinks = new Option("s", "serverSymlinks", true, "Server's symlinks");
/*  49 */     this.serverSymlinks.setArgName("paths");
/*  50 */     this.serverSymlinks.setValueSeparator(File.pathSeparatorChar);
/*  51 */     this.options.addOption(this.serverSymlinks);
/*     */     
/*  53 */     this.pidFile = new Option("p", "pidFile", true, "PID file for server");
/*  54 */     this.pidFile.setArgName("file");
/*  55 */     this.options.addOption(this.pidFile);
/*     */     
/*  57 */     this.warmRoast = new Option("w", "warmRoast", false, "Run warmroast for this launch (useful with -r and -c)");
/*  58 */     this.options.addOption(this.warmRoast);
/*     */     
/*  60 */     this.installKCauldron = new Option("k", "installKCauldron", true, "Install specified or latest KCauldron");
/*  61 */     this.installKCauldron.setArgName("version");
/*  62 */     this.installKCauldron.setOptionalArg(true);
/*  63 */     this.options.addOption(this.installKCauldron);
/*     */     
/*  65 */     this.runKCauldron = new Option("r", "runKCauldron", true, "Install & run specified or latest KCauldron");
/*  66 */     this.runKCauldron.setArgName("version");
/*  67 */     this.runKCauldron.setOptionalArg(true);
/*  68 */     this.options.addOption(this.runKCauldron);
/*     */     
/*  70 */     this.installServer = new Option("i", "installServer", true, "Install custom server");
/*  71 */     this.installServer.setArgName("server file or url");
/*  72 */     this.options.addOption(this.installServer);
/*     */     
/*  74 */     this.runServer = new Option("c", "runServer", true, "Install & run custom server");
/*  75 */     this.runServer.setArgName("server file or url");
/*  76 */     this.options.addOption(this.runServer);
/*     */     
/*  78 */     this.libraries = new Option("l", "libraries", true, "Install specified libraries into server dir");
/*  79 */     this.libraries.setArgName("libraries");
/*  80 */     this.libraries.setValueSeparator(File.pathSeparatorChar);
/*  81 */     this.options.addOption(this.libraries);
/*     */     
/*  83 */     this.parser = new org.apache.commons.cli.DefaultParser();
/*  84 */     this.helpFormatter = new HelpFormatter();
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*  88 */     (instance = new Main()).start(args);
/*     */   }
/*     */   
/*     */   private void start(String[] args) {
/*     */     try {
/*  93 */       this.cli = this.parser.parse(this.options, args, true);
/*  94 */       if (this.cli.hasOption(this.libraries.getOpt())) {
/*  95 */         run(new pw.prok.bootstrap.tasks.Libraries());
/*     */       }
/*  97 */       if (this.cli.hasOption(this.installKCauldron.getOpt())) {
/*  98 */         run(new pw.prok.bootstrap.tasks.InstallKCauldron());
/*     */       }
/* 100 */       if (this.cli.hasOption(this.runKCauldron.getOpt())) {
/* 101 */         run(new pw.prok.bootstrap.tasks.RunKCauldron());
/*     */       }
/* 103 */       if (this.cli.hasOption(this.installServer.getOpt())) {
/* 104 */         run(new pw.prok.bootstrap.tasks.InstallServer());
/*     */       }
/* 106 */       if (this.cli.hasOption(this.runServer.getOpt())) {
/* 107 */         run(new pw.prok.bootstrap.tasks.RunServer());
/*     */       }
/* 109 */       if (!this.wasExecuted) {
/* 110 */         printHelp();
/*     */       }
/*     */     } catch (ParseException e) {
/* 113 */       e.printStackTrace();
/* 114 */       printHelp();
/*     */     }
/*     */   }
/*     */   
/*     */   public void run(DefaultTask task) {
/* 119 */     task.setMain(this);
/*     */     try {
/* 121 */       task.make();
/*     */     } catch (Exception e) {
/* 123 */       e.printStackTrace();
/*     */     }
/* 125 */     this.wasExecuted = true;
/*     */   }
/*     */   
/*     */   private void printHelp() {
/* 129 */     this.helpFormatter.printHelp("kbootstrap", this.options, true);
/*     */   }
/*     */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\pw\prok\bootstrap\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */