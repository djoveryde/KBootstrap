/*    */ package com.sk89q.warmroast;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServlet;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
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
/*    */ public class DataViewServlet
/*    */   extends HttpServlet
/*    */ {
/*    */   private static final long serialVersionUID = -2331397310804298286L;
/*    */   private final WarmRoast roast;
/*    */   
/*    */   public DataViewServlet(WarmRoast roast)
/*    */   {
/* 37 */     this.roast = roast;
/*    */   }
/*    */   
/*    */   protected void doGet(HttpServletRequest request, HttpServletResponse response)
/*    */     throws ServletException, IOException
/*    */   {
/* 43 */     response.setContentType("text/html; charset=utf-8");
/* 44 */     response.setStatus(200);
/*    */     
/* 46 */     PrintWriter w = response.getWriter();
/* 47 */     w.println("<!DOCTYPE html><html><head><title>WarmRoast</title>");
/* 48 */     w.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">");
/* 49 */     w.println("</head><body>");
/* 50 */     w.println("<h1>WarmRoast</h1>");
/* 51 */     w.println("<div class=\"loading\">Downloading snapshot; please wait...</div>");
/* 52 */     w.println("<div class=\"stack\" style=\"display: none\">");
/* 53 */     synchronized (this.roast) {
/* 54 */       Collection<StackNode> nodes = this.roast.getData().values();
/* 55 */       for (StackNode node : nodes) {
/* 56 */         w.println(node.toHtml(this.roast.getMapping()));
/*    */       }
/* 58 */       if (nodes.size() == 0) {
/* 59 */         w.println("<p class=\"no-results\">There are no results. (Thread filter does not match thread?)</p>");
/*    */       }
/*    */     }
/*    */     
/* 63 */     w.println("</div>");
/* 64 */     w.println("<p class=\"legend\">Legend: ");
/* 65 */     w.println("<span class=\"matched\">Mapped</span> ");
/* 66 */     w.println("<span class=\"multiple-matches\">Multiple Mappings</span> ");
/* 67 */     w.println("</p>");
/* 68 */     w.println("<div id=\"overlay\"></div>");
/* 69 */     w.println("<p class=\"footer\">");
/* 70 */     w.println("Icons from <a href=\"http://www.fatcow.com/\">FatCow</a> &mdash; ");
/* 71 */     w.println("<a href=\"http://github.com/sk89q/warmroast\">github.com/sk89q/warmroast</a></p>");
/* 72 */     w.println("<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js\"></script>");
/* 73 */     w.println("<script src=\"warmroast.js\"></script>");
/* 74 */     w.println("</body></html>");
/*    */   }
/*    */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\com\sk89q\warmroast\DataViewServlet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */