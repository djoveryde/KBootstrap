/*    */ package com.sk89q.warmroast;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ public class ClassMapping
/*    */ {
/*    */   private final String obfuscated;
/*    */   private final String actual;
/* 30 */   private final Map<String, List<String>> methods = new HashMap();
/*    */   
/*    */   public ClassMapping(String obfuscated, String actual) {
/* 33 */     this.obfuscated = obfuscated;
/* 34 */     this.actual = actual;
/*    */   }
/*    */   
/*    */   public String getObfuscated() {
/* 38 */     return this.obfuscated;
/*    */   }
/*    */   
/*    */   public String getActual() {
/* 42 */     return this.actual;
/*    */   }
/*    */   
/*    */   public void addMethod(String obfuscated, String actual) {
/* 46 */     List<String> m = (List)this.methods.get(obfuscated);
/* 47 */     if (m == null) {
/* 48 */       m = new ArrayList();
/* 49 */       this.methods.put(obfuscated, m);
/*    */     }
/* 51 */     m.add(actual);
/*    */   }
/*    */   
/*    */   public List<String> mapMethod(String obfuscated) {
/* 55 */     List<String> m = (List)this.methods.get(obfuscated);
/* 56 */     if (m == null) {
/* 57 */       return new ArrayList();
/*    */     }
/* 59 */     return m;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 64 */     return getObfuscated() + "->" + getActual();
/*    */   }
/*    */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\com\sk89q\warmroast\ClassMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */