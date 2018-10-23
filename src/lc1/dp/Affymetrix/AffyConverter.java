/*    */ package lc1.dp.Affymetrix;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileWriter;
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Arrays;
/*    */ import lc1.dp.data.collection.DataCollection;
/*    */ import lc1.dp.data.representation.ComparableArray;
/*    */ import lc1.dp.data.representation.Emiss;
/*    */ import lc1.dp.data.representation.PIGData;
/*    */ import lc1.dp.data.representation.SimpleScorableObject;
/*    */ 
/*    */ public class AffyConverter
/*    */ {
/*    */   final DataCollection data;
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/*    */     try
/*    */     {
/* 23 */       AffyConverter conv = new AffyConverter(new File("calls.chr22.merged.txt"));
/* 24 */       conv.write();
/*    */     } catch (Exception exc) {
/* 26 */       exc.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/* 30 */   final int noIndiv = 62;
/* 31 */   final int noSnps = 761;
/* 32 */   final String[] names = { "A", "B", "N" };
/*    */   
/* 34 */   public AffyConverter(File f) throws Exception { BufferedReader br = new BufferedReader(new java.io.FileReader(f));
/* 35 */     String st = "";
/*    */     
/* 37 */     PIGData[] store = new PIGData[62];
/* 38 */     for (int j = 0; j < 62; j++) {
/* 39 */       store[j] = 
/* 40 */         SimpleScorableObject.make(j, 761, Emiss.getEmissionStateSpace(1));
/*    */     }
/* 42 */     for (int j = 0; j < 62; j++) {
/* 43 */       for (int i = 0; i < 761; i++) {
/* 44 */         String line = br.readLine().trim();
/* 45 */         System.err.println(line);
/*    */         Comparable ma2;
/* 47 */         if (line.equals("A")) {
/* 48 */           Comparable ma = Emiss.a();
/* 49 */           ma2 = Emiss.a();
/*    */         } else { Comparable ma2;
/* 51 */           if (line.equals("B")) {
/* 52 */             Comparable ma = Emiss.b();
/* 53 */             ma2 = Emiss.b();
/*    */           } else { Comparable ma2;
/* 55 */             if (line.equals("AB")) {
/* 56 */               Comparable ma = Emiss.a();
/* 57 */               ma2 = Emiss.b();
/*    */             } else { Comparable ma2;
/* 59 */               if (line.startsWith("N")) {
/* 60 */                 Comparable ma = Emiss.N();
/* 61 */                 ma2 = Emiss.N();
/*    */               } else {
/* 63 */                 throw new RuntimeException("!!"); } } } }
/* 64 */         Comparable ma2; Comparable ma; ComparableArray comp = ComparableArray.make(ma, ma2);
/*    */         
/* 66 */         store[j].addPoint(i, comp);
/*    */       }
/*    */     }
/* 69 */     if ((st = br.readLine()) != null) throw new RuntimeException(st);
/* 70 */     this.data = new lc1.dp.data.collection.SimpleDataCollection(Arrays.asList(store));
/* 71 */     br.close();
/*    */   }
/*    */   
/*    */   public void write() throws Exception {
/* 75 */     PrintWriter pw = new PrintWriter(new java.io.BufferedWriter(new FileWriter(new File("affy_calls.txt"))));
/*    */     
/* 77 */     this.data.writeFastphase(pw, false);
/* 78 */     pw.close();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/Affymetrix/AffyConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */