/*     */ package com.sk89q.warmroast;
/*     */ 
/*     */ import au.com.bytecode.opencsv.CSVReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.io.FileUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class McpMapping
/*     */ {
/*  37 */   private static final Pattern clPattern = Pattern.compile("CL: (?<obfuscated>[^ ]+) (?<actual>[^ ]+)");
/*     */   
/*  39 */   private static final Pattern mdPattern = Pattern.compile("MD: (?<obfuscatedClass>[^ /]+)/(?<obfuscatedMethod>[^ ]+) [^ ]+ (?<method>[^ ]+) [^ ]+");
/*     */   
/*     */ 
/*  42 */   private final Map<String, ClassMapping> classes = new HashMap();
/*  43 */   private final Map<String, String> methods = new HashMap();
/*     */   
/*     */   public ClassMapping mapClass(String obfuscated) {
/*  46 */     return (ClassMapping)this.classes.get(obfuscated);
/*     */   }
/*     */   
/*     */   public void read(File joinedFile, File methodsFile) throws IOException {
/*  50 */     FileReader r = new FileReader(methodsFile);Throwable localThrowable6 = null;
/*  51 */     try { CSVReader reader = new CSVReader(r);Throwable localThrowable7 = null;
/*  52 */       try { List<String[]> entries = reader.readAll();
/*  53 */         processMethodNames(entries);
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/*  51 */         localThrowable7 = localThrowable1;throw localThrowable1;
/*     */       }
/*     */       finally {}
/*     */     }
/*     */     catch (Throwable localThrowable4)
/*     */     {
/*  50 */       localThrowable6 = localThrowable4;throw localThrowable4;
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*  55 */       if (r != null) if (localThrowable6 != null) try { r.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else r.close();
/*     */     }
/*  57 */     List<String> lines = FileUtils.readLines(joinedFile, "UTF-8");
/*  58 */     processClasses(lines);
/*  59 */     processMethods(lines);
/*     */   }
/*     */   
/*     */   public String mapMethodId(String id) {
/*  63 */     return (String)this.methods.get(id);
/*     */   }
/*     */   
/*     */   public String fromMethodId(String id) {
/*  67 */     String method = (String)this.methods.get(id);
/*  68 */     if (method == null) {
/*  69 */       return id;
/*     */     }
/*  71 */     return method;
/*     */   }
/*     */   
/*     */   private void processMethodNames(List<String[]> entries) {
/*  75 */     boolean first = true;
/*  76 */     for (String[] entry : entries) {
/*  77 */       if (entry.length >= 2)
/*     */       {
/*     */ 
/*  80 */         if (first) {
/*  81 */           first = false;
/*     */         }
/*     */         else
/*  84 */           this.methods.put(entry[0], entry[1]); }
/*     */     }
/*     */   }
/*     */   
/*     */   private void processClasses(List<String> lines) {
/*  89 */     for (String line : lines) {
/*  90 */       Matcher m = clPattern.matcher(line);
/*  91 */       if (m.matches()) {
/*  92 */         String obfuscated = m.group("obfuscated");
/*  93 */         String actual = m.group("actual").replace("/", ".");
/*  94 */         this.classes.put(obfuscated, new ClassMapping(obfuscated, actual));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void processMethods(List<String> lines) {
/* 100 */     for (String line : lines) {
/* 101 */       Matcher m = mdPattern.matcher(line);
/* 102 */       if (m.matches()) {
/* 103 */         String obfuscatedClass = m.group("obfuscatedClass");
/* 104 */         String obfuscatedMethod = m.group("obfuscatedMethod");
/* 105 */         String method = m.group("method");
/* 106 */         String methodId = method.substring(method.lastIndexOf('/') + 1);
/* 107 */         ClassMapping mapping = mapClass(obfuscatedClass);
/* 108 */         if (mapping != null) {
/* 109 */           mapping.addMethod(obfuscatedMethod, 
/* 110 */             fromMethodId(methodId));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\com\sk89q\warmroast\McpMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */