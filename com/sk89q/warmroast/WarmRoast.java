/*     */ package com.sk89q.warmroast;
/*     */ 
/*     */ import com.beust.jcommander.JCommander;
/*     */ import com.sun.tools.attach.AgentInitializationException;
/*     */ import com.sun.tools.attach.AgentLoadException;
/*     */ import com.sun.tools.attach.AttachNotSupportedException;
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import com.sun.tools.attach.VirtualMachineDescriptor;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import java.lang.management.ThreadMXBean;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URL;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.TreeMap;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.remote.JMXConnector;
/*     */ import javax.management.remote.JMXConnectorFactory;
/*     */ import javax.management.remote.JMXServiceURL;
/*     */ import org.eclipse.jetty.server.Server;
/*     */ import org.eclipse.jetty.server.handler.HandlerList;
/*     */ import org.eclipse.jetty.server.handler.ResourceHandler;
/*     */ import org.eclipse.jetty.servlet.ServletContextHandler;
/*     */ import org.eclipse.jetty.servlet.ServletHolder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WarmRoast
/*     */   extends TimerTask
/*     */ {
/*     */   private static final String SEPARATOR = "------------------------------------------------------------------------";
/*     */   private final int interval;
/*     */   private final VirtualMachine vm;
/*  66 */   private final Timer timer = new Timer("Roast Pan", true);
/*  67 */   private final McpMapping mapping = new McpMapping();
/*  68 */   private final SortedMap<String, StackNode> nodes = new TreeMap();
/*     */   private JMXConnector connector;
/*     */   private MBeanServerConnection mbsc;
/*     */   private ThreadMXBean threadBean;
/*     */   private String filterThread;
/*  73 */   private long endTime = -1L;
/*     */   
/*     */   public WarmRoast(VirtualMachine vm, int interval) {
/*  76 */     this.vm = vm;
/*  77 */     this.interval = interval;
/*     */   }
/*     */   
/*     */   public Map<String, StackNode> getData() {
/*  81 */     return this.nodes;
/*     */   }
/*     */   
/*     */   private StackNode getNode(String name) {
/*  85 */     StackNode node = (StackNode)this.nodes.get(name);
/*  86 */     if (node == null) {
/*  87 */       node = new StackNode(name);
/*  88 */       this.nodes.put(name, node);
/*     */     }
/*  90 */     return node;
/*     */   }
/*     */   
/*     */   public McpMapping getMapping() {
/*  94 */     return this.mapping;
/*     */   }
/*     */   
/*     */   public String getFilterThread() {
/*  98 */     return this.filterThread;
/*     */   }
/*     */   
/*     */   public void setFilterThread(String filterThread) {
/* 102 */     this.filterThread = filterThread;
/*     */   }
/*     */   
/*     */   public long getEndTime() {
/* 106 */     return this.endTime;
/*     */   }
/*     */   
/*     */   public void setEndTime(long l) {
/* 110 */     this.endTime = l;
/*     */   }
/*     */   
/*     */   public void connect()
/*     */     throws IOException, AgentLoadException, AgentInitializationException
/*     */   {
/* 116 */     String connectorAddr = this.vm.getAgentProperties().getProperty("com.sun.management.jmxremote.localConnectorAddress");
/*     */     
/* 118 */     if (connectorAddr == null) {
/* 119 */       String agent = this.vm.getSystemProperties().getProperty("java.home") + File.separator + "lib" + File.separator + "management-agent.jar";
/*     */       
/*     */ 
/* 122 */       this.vm.loadAgent(agent);
/* 123 */       connectorAddr = this.vm.getAgentProperties().getProperty("com.sun.management.jmxremote.localConnectorAddress");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 128 */     JMXServiceURL serviceURL = new JMXServiceURL(connectorAddr);
/* 129 */     this.connector = JMXConnectorFactory.connect(serviceURL);
/* 130 */     this.mbsc = this.connector.getMBeanServerConnection();
/*     */     try {
/* 132 */       this.threadBean = getThreadMXBean();
/*     */     } catch (MalformedObjectNameException e) {
/* 134 */       throw new IOException("Bad MX bean name", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private ThreadMXBean getThreadMXBean() throws IOException, MalformedObjectNameException
/*     */   {
/* 140 */     ObjectName objName = new ObjectName("java.lang:type=Threading");
/* 141 */     Set<ObjectName> mbeans = this.mbsc.queryNames(objName, null);
/* 142 */     Iterator localIterator = mbeans.iterator(); if (localIterator.hasNext()) { ObjectName name = (ObjectName)localIterator.next();
/* 143 */       return (ThreadMXBean)ManagementFactory.newPlatformMXBeanProxy(this.mbsc, name
/* 144 */         .toString(), ThreadMXBean.class);
/*     */     }
/* 146 */     throw new IOException("No thread MX bean found");
/*     */   }
/*     */   
/*     */   public synchronized void run()
/*     */   {
/* 151 */     if ((this.endTime >= 0L) && 
/* 152 */       (this.endTime <= System.currentTimeMillis())) {
/* 153 */       cancel();
/* 154 */       System.err.println("Sampling has stopped.");
/* 155 */       return;
/*     */     }
/*     */     
/*     */ 
/* 159 */     ThreadInfo[] threadDumps = this.threadBean.dumpAllThreads(false, false);
/* 160 */     for (ThreadInfo threadInfo : threadDumps) {
/* 161 */       String threadName = threadInfo.getThreadName();
/* 162 */       StackTraceElement[] stack = threadInfo.getStackTrace();
/*     */       
/* 164 */       if ((threadName != null) && (stack != null))
/*     */       {
/*     */ 
/*     */ 
/* 168 */         if ((this.filterThread == null) || (this.filterThread.equals(threadName)))
/*     */         {
/*     */ 
/*     */ 
/* 172 */           StackNode node = getNode(threadName);
/* 173 */           node.log(stack, this.interval);
/*     */         } }
/*     */     }
/*     */   }
/*     */   
/* 178 */   public void start(InetSocketAddress address) throws Exception { this.timer.scheduleAtFixedRate(this, this.interval, this.interval);
/*     */     
/* 180 */     Server server = new Server(address);
/*     */     
/* 182 */     ServletContextHandler context = new ServletContextHandler();
/* 183 */     context.setContextPath("/");
/* 184 */     context.addServlet(new ServletHolder(new DataViewServlet(this)), "/stack");
/*     */     
/* 186 */     ResourceHandler resources = new ResourceHandler();
/* 187 */     String filesDir = WarmRoast.class.getResource("/www").toExternalForm();
/* 188 */     resources.setResourceBase(filesDir);
/* 189 */     resources.setDirectoriesListed(true);
/* 190 */     resources.setWelcomeFiles(new String[] { "index.html" });
/*     */     
/* 192 */     HandlerList handlers = new HandlerList();
/* 193 */     handlers.addHandler(context);
/* 194 */     handlers.addHandler(resources);
/* 195 */     server.setHandler(handlers);
/*     */     
/* 197 */     server.start();
/* 198 */     server.join();
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws AgentLoadException {
/* 202 */     RoastOptions opt = new RoastOptions();
/* 203 */     JCommander jc = new JCommander(opt, args);
/* 204 */     jc.setProgramName("warmroast");
/*     */     
/* 206 */     if (opt.help) {
/* 207 */       jc.usage();
/* 208 */       System.exit(0);
/*     */     }
/*     */     
/* 211 */     System.err.println("------------------------------------------------------------------------");
/* 212 */     System.err.println("WarmRoast");
/* 213 */     System.err.println("http://github.com/sk89q/warmroast");
/* 214 */     System.err.println("------------------------------------------------------------------------");
/* 215 */     System.err.println("");
/*     */     
/* 217 */     VirtualMachine vm = null;
/*     */     
/* 219 */     if (opt.pid != null) {
/*     */       try {
/* 221 */         vm = VirtualMachine.attach(String.valueOf(opt.pid));
/* 222 */         System.err.println("Attaching to PID " + opt.pid + "...");
/*     */       } catch (AttachNotSupportedException|IOException e) {
/* 224 */         System.err.println("Failed to attach VM by PID " + opt.pid);
/* 225 */         e.printStackTrace();
/* 226 */         System.exit(1);
/*     */       }
/* 228 */     } else if (opt.vmName != null) {
/* 229 */       for (VirtualMachineDescriptor desc : VirtualMachine.list()) {
/* 230 */         if (desc.displayName().contains(opt.vmName)) {
/*     */           try {
/* 232 */             vm = VirtualMachine.attach(desc);
/* 233 */             System.err.println("Attaching to '" + desc.displayName() + "'...");
/*     */           }
/*     */           catch (AttachNotSupportedException|IOException e)
/*     */           {
/* 237 */             System.err.println("Failed to attach VM by name '" + opt.vmName + "'");
/* 238 */             e.printStackTrace();
/* 239 */             System.exit(1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 245 */     if (vm == null) {
/* 246 */       List<VirtualMachineDescriptor> descriptors = VirtualMachine.list();
/* 247 */       System.err.println("Choose a VM:");
/*     */       
/* 249 */       Collections.sort(descriptors, new Comparator()
/*     */       {
/*     */         public int compare(VirtualMachineDescriptor o1, VirtualMachineDescriptor o2)
/*     */         {
/* 253 */           return o1.displayName().compareTo(o2.displayName());
/*     */         }
/*     */         
/*     */ 
/* 257 */       });
/* 258 */       int i = 1;
/* 259 */       for (VirtualMachineDescriptor desc : descriptors) {
/* 260 */         System.err.println("[" + i++ + "] " + desc.displayName());
/*     */       }
/*     */       
/*     */ 
/* 264 */       System.err.println("");
/* 265 */       System.err.print("Enter choice #: ");
/* 266 */       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
/*     */       try
/*     */       {
/* 269 */         s = reader.readLine();
/*     */       } catch (IOException e) { String s;
/* 271 */         return;
/*     */       }
/*     */       try
/*     */       {
/*     */         String s;
/* 276 */         int choice = Integer.parseInt(s) - 1;
/* 277 */         if ((choice < 0) || (choice >= descriptors.size())) {
/* 278 */           System.err.println("");
/* 279 */           System.err.println("Given choice is out of range.");
/* 280 */           System.exit(1);
/*     */         }
/* 282 */         vm = VirtualMachine.attach((VirtualMachineDescriptor)descriptors.get(choice));
/*     */       } catch (NumberFormatException e) {
/* 284 */         System.err.println("");
/* 285 */         System.err.println("That's not a number. Bye.");
/* 286 */         System.exit(1);
/*     */       } catch (AttachNotSupportedException|IOException e) {
/* 288 */         System.err.println("");
/* 289 */         System.err.println("Failed to attach VM");
/* 290 */         e.printStackTrace();
/* 291 */         System.exit(1);
/*     */       }
/*     */     }
/*     */     
/* 295 */     InetSocketAddress address = new InetSocketAddress(opt.bindAddress, opt.port.intValue());
/*     */     
/* 297 */     WarmRoast roast = new WarmRoast(vm, opt.interval.intValue());
/* 298 */     if (opt.mappingsDir != null) {
/* 299 */       File dir = new File(opt.mappingsDir);
/* 300 */       File joined = new File(dir, "joined.srg");
/* 301 */       File methods = new File(dir, "methods.csv");
/*     */       try {
/* 303 */         roast.getMapping().read(joined, methods);
/*     */       } catch (IOException e) {
/* 305 */         System.err.println("Failed to read the mappings files (joined.srg, methods.csv) from " + dir
/*     */         
/* 307 */           .getAbsolutePath() + ": " + e.getMessage());
/* 308 */         System.exit(2);
/*     */       }
/*     */     }
/*     */     
/* 312 */     System.err.println("------------------------------------------------------------------------");
/*     */     
/* 314 */     roast.setFilterThread(opt.threadName);
/*     */     
/* 316 */     if ((opt.timeout != null) && (opt.timeout.intValue() > 0)) {
/* 317 */       roast.setEndTime(System.currentTimeMillis() + opt.timeout.intValue() * 1000);
/* 318 */       System.err.println("Sampling set to stop in " + opt.timeout + " seconds.");
/*     */     }
/*     */     
/* 321 */     System.err.println("Starting a server on " + address.toString() + "...");
/* 322 */     System.err.println("Once the server starts (shortly), visit the URL in your browser.");
/* 323 */     System.err.println("Note: The longer you wait before using the output of that webpage, the more accurate the results will be.");
/*     */     
/*     */     try
/*     */     {
/* 327 */       roast.connect();
/* 328 */       roast.start(address);
/*     */     } catch (Throwable t) {
/* 330 */       t.printStackTrace();
/* 331 */       System.exit(3);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\com\sk89q\warmroast\WarmRoast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */