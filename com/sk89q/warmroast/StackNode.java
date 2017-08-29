/*     */ package com.sk89q.warmroast;
/*     */ 
/*     */ import java.text.NumberFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
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
/*     */ public class StackNode
/*     */   implements Comparable<StackNode>
/*     */ {
/*  32 */   private static final NumberFormat cssDec = NumberFormat.getPercentInstance(Locale.US);
/*     */   private final String name;
/*  34 */   private final Map<String, StackNode> children = new HashMap();
/*     */   private long totalTime;
/*     */   
/*     */   static {
/*  38 */     cssDec.setGroupingUsed(false);
/*  39 */     cssDec.setMaximumFractionDigits(2);
/*     */   }
/*     */   
/*     */   public StackNode(String name) {
/*  43 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  47 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getNameHtml(McpMapping mapping) {
/*  51 */     return escapeHtml(getName());
/*     */   }
/*     */   
/*     */   public Collection<StackNode> getChildren() {
/*  55 */     List<StackNode> list = new ArrayList(this.children.values());
/*  56 */     Collections.sort(list);
/*  57 */     return list;
/*     */   }
/*     */   
/*     */   public StackNode getChild(String name) {
/*  61 */     StackNode child = (StackNode)this.children.get(name);
/*  62 */     if (child == null) {
/*  63 */       child = new StackNode(name);
/*  64 */       this.children.put(name, child);
/*     */     }
/*  66 */     return child;
/*     */   }
/*     */   
/*     */   public StackNode getChild(String className, String methodName) {
/*  70 */     StackTraceNode node = new StackTraceNode(className, methodName);
/*  71 */     StackNode child = (StackNode)this.children.get(node.getName());
/*  72 */     if (child == null) {
/*  73 */       child = node;
/*  74 */       this.children.put(node.getName(), node);
/*     */     }
/*  76 */     return child;
/*     */   }
/*     */   
/*     */   public long getTotalTime() {
/*  80 */     return this.totalTime;
/*     */   }
/*     */   
/*     */   public void log(long time) {
/*  84 */     this.totalTime += time;
/*     */   }
/*     */   
/*     */   private void log(StackTraceElement[] elements, int skip, long time) {
/*  88 */     log(time);
/*     */     
/*  90 */     if (elements.length - skip == 0) {
/*  91 */       return;
/*     */     }
/*     */     
/*  94 */     StackTraceElement bottom = elements[(elements.length - (skip + 1))];
/*  95 */     getChild(bottom.getClassName(), bottom.getMethodName())
/*  96 */       .log(elements, skip + 1, time);
/*     */   }
/*     */   
/*     */   public void log(StackTraceElement[] elements, long time) {
/* 100 */     log(elements, 0, time);
/*     */   }
/*     */   
/*     */   public int compareTo(StackNode o)
/*     */   {
/* 105 */     return getName().compareTo(o.getName());
/*     */   }
/*     */   
/*     */   private void writeHtml(StringBuilder builder, McpMapping mapping, long totalTime) {
/* 109 */     builder.append("<div class=\"node collapsed\">");
/* 110 */     builder.append("<div class=\"name\">");
/* 111 */     builder.append(getNameHtml(mapping));
/* 112 */     builder.append("<span class=\"percent\">");
/* 113 */     builder
/* 114 */       .append(String.format("%.2f", new Object[] {Double.valueOf(getTotalTime() / totalTime * 100.0D) }))
/* 115 */       .append("%");
/* 116 */     builder.append("</span>");
/* 117 */     builder.append("<span class=\"time\">");
/* 118 */     builder.append(getTotalTime()).append("ms");
/* 119 */     builder.append("</span>");
/* 120 */     builder.append("<span class=\"bar\">");
/* 121 */     builder.append("<span class=\"bar-inner\" style=\"width:")
/* 122 */       .append(formatCssPct(getTotalTime() / totalTime))
/* 123 */       .append("\">");
/* 124 */     builder.append("</span>");
/* 125 */     builder.append("</span>");
/* 126 */     builder.append("</div>");
/* 127 */     builder.append("<ul class=\"children\">");
/* 128 */     for (StackNode child : getChildren()) {
/* 129 */       builder.append("<li>");
/* 130 */       child.writeHtml(builder, mapping, totalTime);
/* 131 */       builder.append("</li>");
/*     */     }
/* 133 */     builder.append("</ul>");
/* 134 */     builder.append("</div>");
/*     */   }
/*     */   
/*     */   public String toHtml(McpMapping mapping) {
/* 138 */     StringBuilder builder = new StringBuilder();
/* 139 */     writeHtml(builder, mapping, getTotalTime());
/* 140 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private void writeString(StringBuilder builder, int indent) {
/* 144 */     StringBuilder b = new StringBuilder();
/* 145 */     for (int i = 0; i < indent; i++) {
/* 146 */       b.append(" ");
/*     */     }
/* 148 */     String padding = b.toString();
/*     */     
/* 150 */     for (StackNode child : getChildren()) {
/* 151 */       builder.append(padding).append(child.getName());
/* 152 */       builder.append(" ");
/* 153 */       builder.append(getTotalTime()).append("ms");
/* 154 */       builder.append("\n");
/* 155 */       child.writeString(builder, indent + 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 161 */     StringBuilder builder = new StringBuilder();
/* 162 */     writeString(builder, 0);
/* 163 */     return builder.toString();
/*     */   }
/*     */   
/*     */   protected static String formatCssPct(double pct) {
/* 167 */     return cssDec.format(pct);
/*     */   }
/*     */   
/*     */   protected static String escapeHtml(String str) {
/* 171 */     return str.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
/*     */   }
/*     */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\com\sk89q\warmroast\StackNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */