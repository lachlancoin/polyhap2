/*    */ package lc1.ensj;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileFilter;
/*    */ import java.io.FileReader;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Analysis
/*    */ {
/* 15 */   static String key = "gaps";
/* 16 */   static int pos = 4;
/*    */   
/*    */   public static void main(String[] args) {
/* 19 */     try { File dir = new File(".");
/* 20 */       File[] f = dir.listFiles(new FileFilter() {
/*    */         public boolean accept(File pathname) {
/* 22 */           return pathname.isDirectory();
/*    */         }
/*    */         
/* 25 */       });
/* 26 */       List<Double> set = new ArrayList();
/*    */       
/* 28 */       for (int i = 0; i < f.length; i++) {
/* 29 */         File summ = new File(f[i], "logfile.txt");
/* 30 */         if (summ.exists()) {
/* 31 */           BufferedReader br = new BufferedReader(new FileReader(summ));
/* 32 */           String st = "";
/* 33 */           while ((st = br.readLine()) != null) {
/* 34 */             if (st.startsWith(key))
/*    */             {
/* 36 */               String[] str = st.split("\\s+");
/* 37 */               set.add(Double.valueOf(Double.parseDouble(str[pos])));
/*    */               
/*    */ 
/* 40 */               break;
/*    */             }
/*    */           }
/* 43 */           br.close();
/*    */         } }
/* 45 */       Collections.sort(set);
/* 46 */       for (Iterator<Double> it = set.iterator(); it.hasNext();) {
/* 47 */         System.err.println(it.next());
/*    */       }
/* 49 */       System.err.println("median " + set.get((int)Math.round(set.size() / 2.0D)));
/*    */     } catch (Exception exc) {
/* 51 */       exc.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/ensj/Analysis.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */