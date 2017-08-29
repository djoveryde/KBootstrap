/*    */ package com.sk89q.warmroast;
/*    */ 
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StackTraceNode
/*    */   extends StackNode
/*    */ {
/*    */   private final String className;
/*    */   private final String methodName;
/*    */   
/*    */   public StackTraceNode(String className, String methodName)
/*    */   {
/* 29 */     super(className + "." + methodName + "()");
/* 30 */     this.className = className;
/* 31 */     this.methodName = methodName;
/*    */   }
/*    */   
/*    */   public String getClassName() {
/* 35 */     return this.className;
/*    */   }
/*    */   
/*    */   public String getMethodName() {
/* 39 */     return this.methodName;
/*    */   }
/*    */   
/*    */   public String getNameHtml(McpMapping mapping)
/*    */   {
/* 44 */     ClassMapping classMapping = mapping.mapClass(getClassName());
/* 45 */     if (classMapping != null)
/*    */     {
/*    */ 
/* 48 */       String className = "<span class=\"matched\" title=\"" + escapeHtml(getClassName()) + "\">" + escapeHtml(classMapping.getActual()) + "</span>";
/*    */       
/* 50 */       List<String> actualMethods = classMapping.mapMethod(getMethodName());
/* 51 */       if (actualMethods.size() == 0)
/* 52 */         return className + "." + escapeHtml(getMethodName()) + "()";
/* 53 */       if (actualMethods.size() == 1)
/*    */       {
/*    */ 
/*    */ 
/* 57 */         return className + ".<span class=\"matched\" title=\"" + escapeHtml(getMethodName()) + "\">" + escapeHtml((String)actualMethods.get(0)) + "</span>()";
/*    */       }
/* 59 */       StringBuilder builder = new StringBuilder();
/* 60 */       boolean first = true;
/* 61 */       for (String m : actualMethods) {
/* 62 */         if (!first) {
/* 63 */           builder.append(" ");
/*    */         }
/* 65 */         builder.append(m);
/* 66 */         first = false;
/*    */       }
/*    */       
/*    */ 
/* 70 */       return className + ".<span class=\"multiple-matches\" title=\"" + builder.toString() + "\">" + escapeHtml(getMethodName()) + "</span>()";
/*    */     }
/*    */     
/* 73 */     String actualMethod = mapping.mapMethodId(getMethodName());
/* 74 */     if (actualMethod == null) {
/* 75 */       return escapeHtml(getClassName()) + "." + escapeHtml(getMethodName()) + "()";
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 80 */     return this.className + ".<span class=\"matched\" title=\"" + escapeHtml(getMethodName()) + "\">" + escapeHtml(actualMethod) + "</span>()";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int compareTo(StackNode o)
/*    */   {
/* 87 */     if (getTotalTime() == o.getTotalTime())
/* 88 */       return 0;
/* 89 */     if (getTotalTime() > o.getTotalTime()) {
/* 90 */       return -1;
/*    */     }
/* 92 */     return 1;
/*    */   }
/*    */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\com\sk89q\warmroast\StackTraceNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */