/*    */ package dataFormat;
/*    */ 
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.FileReader;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Illumina
/*    */ {
/* 14 */   List<String> data = new ArrayList();
/*    */   
/* 16 */   public void getData(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*    */     String st;
/* 18 */     while ((st = br.readLine()) != null) { String st;
/* 19 */       String[] str = st.split("\\s+");
/* 20 */       if (str[1].equals("1")) {
/* 21 */         this.data.add(str[0]);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void print(PrintStream ps) {
/* 27 */     ps.println("");
/*    */   }
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/*    */     try {
/* 33 */       Illumina il = new Illumina();
/* 34 */       il.getData(new File(""));
/*    */       
/* 36 */       PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File(""))));
/* 37 */       il.print(ps);
/* 38 */       ps.close();
/*    */     }
/*    */     catch (Exception exc) {
/* 41 */       exc.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/Illumina.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */