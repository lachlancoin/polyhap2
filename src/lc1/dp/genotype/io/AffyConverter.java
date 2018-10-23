/*    */ package lc1.dp.genotype.io;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileReader;
/*    */ import java.io.PrintStream;
/*    */ import java.util.Arrays;
/*    */ import lc1.dp.genotype.io.scorable.DataCollection;
/*    */ import lc1.dp.genotype.io.scorable.PhasedIntegerGenotypeData;
/*    */ import lc1.dp.genotype.io.scorable.SimpleDataCollection;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AffyConverter
/*    */ {
/*    */   final DataCollection data;
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/*    */     try
/*    */     {
/* 22 */       AffyConverter conv = new AffyConverter(new File("calls.chr22.merged.txt"));
/* 23 */       conv.write();
/*    */     } catch (Exception exc) {
/* 25 */       exc.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/* 29 */   final int noIndiv = 62;
/* 30 */   final int noSnps = 761;
/* 31 */   final String[] names = { "A", "B", "N" };
/*    */   
/* 33 */   public AffyConverter(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/* 34 */     String st = "";
/*    */     
/* 36 */     PhasedIntegerGenotypeData[] store = new PhasedIntegerGenotypeData[62];
/* 37 */     for (int j = 0; j < 62; j++) {
/* 38 */       store[j] = new PhasedIntegerGenotypeData(j, 761);
/*    */     }
/* 40 */     for (int j = 0; j < 62; j++) {
/* 41 */       for (int i = 0; i < 761; i++) {
/* 42 */         String line = br.readLine().trim();
/* 43 */         System.err.println(line);
/*    */         Comparable ma2;
/* 45 */         if (line.equals("A")) {
/* 46 */           Comparable ma = Emiss.A();
/* 47 */           ma2 = Emiss.A();
/*    */         } else { Comparable ma2;
/* 49 */           if (line.equals("B")) {
/* 50 */             Comparable ma = Emiss.B();
/* 51 */             ma2 = Emiss.B();
/*    */           } else { Comparable ma2;
/* 53 */             if (line.equals("AB")) {
/* 54 */               Comparable ma = Emiss.A();
/* 55 */               ma2 = Emiss.B();
/*    */             } else { Comparable ma2;
/* 57 */               if (line.startsWith("N")) {
/* 58 */                 Comparable ma = Emiss.N();
/* 59 */                 ma2 = Emiss.N();
/*    */               } else {
/* 61 */                 throw new RuntimeException("!!"); } } } }
/* 62 */         Comparable ma2; Comparable ma; ComparableArray comp = ComparableArray.make(ma, ma2);
/*    */         
/* 64 */         store[j].addPoint(comp);
/*    */       }
/*    */     }
/* 67 */     if ((st = br.readLine()) != null) throw new RuntimeException(st);
/* 68 */     this.data = new SimpleDataCollection(Arrays.asList(store));
/* 69 */     br.close();
/*    */   }
/*    */   
/*    */   public void write()
/*    */     throws Exception
/*    */   {
/* 75 */     throw new Error("Unresolved compilation problem: \n\tThe field DataCollection.data is not visible\n");
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/AffyConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */