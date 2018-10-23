/*    */ package lc1.dp.Affymetrix;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.FileWriter;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Map;
/*    */ import lc1.ensj.PedigreeDataCollection;
/*    */ 
/*    */ public class MakePedigreeFile
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/*    */     try
/*    */     {
/* 16 */       java.io.File fi = new java.io.File("L.50K_Merged.CEPH.Affy.txt");
/*    */       
/* 18 */       BufferedReader br = new BufferedReader(new java.io.FileReader(fi));
/* 19 */       String[] st = br.readLine().trim().split("\\s+");
/* 20 */       String[] cat = lc1.dp.data.collection.LikelihoodDataCollection.cat;
/* 21 */       System.err.println(st.length - 2 + " " + cat.length);
/*    */       
/* 23 */       if (Math.IEEEremainder(st.length - 2, cat.length) != 0.0D) throw new RuntimeException("!!");
/* 24 */       int noIndiv = (int)Math.floor((st.length - 2) / cat.length);
/* 25 */       if (noIndiv * cat.length != st.length - 2) throw new RuntimeException("!!" + noIndiv);
/* 26 */       PrintWriter indiv = new PrintWriter(new BufferedWriter(new FileWriter("indiv.txt")));
/* 27 */       PrintWriter ped_pw = new PrintWriter(new BufferedWriter(new FileWriter("ped.txt")));
/* 28 */       String[] ldata = new String[noIndiv];
/* 29 */       for (int i = 0; i < noIndiv; i++) {
/* 30 */         ldata[i] = ("INDIV" + i);
/* 31 */         indiv.println(ldata[i]);
/*    */       }
/* 33 */       PedigreeDataCollection ped = new PedigreeDataCollection();
/* 34 */       for (int i = 0; i < ldata.length; i += 3) {
/* 35 */         String father = ldata[i];
/* 36 */         String mother = ldata[(i + 1)];
/* 37 */         String child = ldata[(i + 2)];
/*    */         
/* 39 */         ped.mother.put(child, mother);
/* 40 */         ped.father.put(child, father);
/*    */       }
/*    */       
/* 43 */       ped.print(ped_pw);
/* 44 */       ped_pw.close();
/* 45 */       indiv.close();
/*    */     } catch (Exception exc) {
/* 47 */       exc.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/Affymetrix/MakePedigreeFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */