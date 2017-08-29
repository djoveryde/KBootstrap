 package pw.prok.bootstrap;
 
 import java.io.File;
 import org.apache.commons.cli.CommandLine;
 import org.apache.commons.cli.CommandLineParser;
 import org.apache.commons.cli.HelpFormatter;
 import org.apache.commons.cli.Option;
 import org.apache.commons.cli.Options;
 import org.apache.commons.cli.ParseException;
 import pw.prok.bootstrap.tasks.DefaultTask;
 
 public class Main
 {
   public final Options options;
   public final Option serverDir;
   public final Option binDir;
   public final Option jvmArgs;
   public final Option serverSymlinks;
   public final Option pidFile;
   public final Option warmRoast;
   public final Option installKCauldron;
   public final Option runKCauldron;
   public final Option installServer;
   public final Option runServer;
   public final Option libraries;
   public final CommandLineParser parser;
   public final HelpFormatter helpFormatter;
   public CommandLine cli;
   private boolean wasExecuted = false;
   public static Main instance;
   
   public Main()
   {
     this.options = new Options();
     
     this.serverDir = new Option("d", "serverDir", true, "Server root directory");
     this.serverDir.setArgName("dir");
     this.options.addOption(this.serverDir);
     
     this.binDir = new Option("b", "binDir", true, "Server bin directory");
     this.binDir.setArgName("dir");
     this.options.addOption(this.binDir);
     
     this.jvmArgs = new Option("j", "jvmArg", true, "Server's JVM arguments");
     this.jvmArgs.setArgName("args");
     this.options.addOption(this.jvmArgs);
     
     this.serverSymlinks = new Option("s", "serverSymlinks", true, "Server's symlinks");
     this.serverSymlinks.setArgName("paths");
     this.serverSymlinks.setValueSeparator(File.pathSeparatorChar);
     this.options.addOption(this.serverSymlinks);
     
     this.pidFile = new Option("p", "pidFile", true, "PID file for server");
     this.pidFile.setArgName("file");
     this.options.addOption(this.pidFile);
     
     this.warmRoast = new Option("w", "warmRoast", false, "Run warmroast for this launch (useful with -r and -c)");
     this.options.addOption(this.warmRoast);
     
     this.installKCauldron = new Option("k", "installKCauldron", true, "Install specified or latest KCauldron");
     this.installKCauldron.setArgName("version");
     this.installKCauldron.setOptionalArg(true);
     this.options.addOption(this.installKCauldron);
     
     this.runKCauldron = new Option("r", "runKCauldron", true, "Install & run specified or latest KCauldron");
     this.runKCauldron.setArgName("version");
     this.runKCauldron.setOptionalArg(true);
     this.options.addOption(this.runKCauldron);
     
     this.installServer = new Option("i", "installServer", true, "Install custom server");
     this.installServer.setArgName("server file or url");
     this.options.addOption(this.installServer);
     
     this.runServer = new Option("c", "runServer", true, "Install & run custom server");
     this.runServer.setArgName("server file or url");
     this.options.addOption(this.runServer);
     
     this.libraries = new Option("l", "libraries", true, "Install specified libraries into server dir");
     this.libraries.setArgName("libraries");
     this.libraries.setValueSeparator(File.pathSeparatorChar);
     this.options.addOption(this.libraries);
     
     this.parser = new org.apache.commons.cli.DefaultParser();
     this.helpFormatter = new HelpFormatter();
   }
   
   public static void main(String[] args) {
     (instance = new Main()).start(args);
   }
   
   private void start(String[] args) {
     try {
       this.cli = this.parser.parse(this.options, args, true);
       if (this.cli.hasOption(this.libraries.getOpt())) {
         run(new pw.prok.bootstrap.tasks.Libraries());
       }
       if (this.cli.hasOption(this.installKCauldron.getOpt())) {
         run(new pw.prok.bootstrap.tasks.InstallKCauldron());
       }
       if (this.cli.hasOption(this.runKCauldron.getOpt())) {
         run(new pw.prok.bootstrap.tasks.RunKCauldron());
       }
       if (this.cli.hasOption(this.installServer.getOpt())) {
         run(new pw.prok.bootstrap.tasks.InstallServer());
       }
       if (this.cli.hasOption(this.runServer.getOpt())) {
         run(new pw.prok.bootstrap.tasks.RunServer());
       }
       if (!this.wasExecuted) {
         printHelp();
       }
     } catch (ParseException e) {
       e.printStackTrace();
       printHelp();
     }
   }
   
   public void run(DefaultTask task) {
     task.setMain(this);
     try {
       task.make();
     } catch (Exception e) {
       e.printStackTrace();
     }
     this.wasExecuted = true;
   }
   
   private void printHelp() {
     this.helpFormatter.printHelp("kbootstrap", this.options, true);
   }
}
