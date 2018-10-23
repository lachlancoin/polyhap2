/*     */ package lc1.association.io;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class IntSNP
/*     */   extends SNP
/*     */   implements Serializable
/*     */ {
/*     */   float maf_freq;
/*     */   int location;
/*     */   Number[] snp;
/*     */   
/*     */   public Number getVal(int i)
/*     */   {
/*  19 */     return this.snp[i];
/*     */   }
/*     */   
/*  22 */   public int length() { return this.snp.length; }
/*     */   
/*     */   public String toString()
/*     */   {
/*  26 */     return this.name;
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw)
/*     */   {
/*  31 */     pw.print(this.name);pw.print("\t");
/*  32 */     int i = 0;
/*  33 */     for (i = 0; i < this.snp.length - 1; i++) {
/*  34 */       pw.print(this.snp[i]);pw.print("\t");
/*     */     }
/*  36 */     pw.println(this.snp[i]); }
/*     */   
/*  38 */   public double getAverage() { return this.mean; }
/*     */   
/*     */   IntSNP(String name, List<String> l, short offset) throws ArithmeticException {
/*  41 */     float freq1 = 0.0F;
/*  42 */     float freq2 = 0.0F;
/*  43 */     this.name = name;
/*  44 */     this.snp = new Number[l.size() - 1];
/*  45 */     for (int i = 0; i < l.size(); i++) {
/*  46 */       if ((!((String)l.get(i)).equals("null")) && (!((String)l.get(i)).equals("NA"))) {
/*  47 */         short val = (short)(Short.parseShort((String)l.get(i)) - offset);
/*  48 */         this.snp[i] = Short.valueOf(val);
/*     */         
/*  50 */         if ((val < -1) || (val > 1)) throw new ArithmeticException("val not in right bounds");
/*  51 */         if (val == -1) { freq1 += 1.0F;
/*  52 */         } else if (val == 1) { freq2 += 1.0F;
/*     */         } else {
/*  54 */           freq1 += 1.0F;
/*  55 */           freq2 += 1.0F;
/*     */         }
/*  57 */         this.mean += val;
/*     */       }
/*     */       else
/*     */       {
/*  61 */         NA_count += 1.0D;
/*     */       }
/*     */     }
/*  64 */     this.mean /= (this.snp.length - NA_count);
/*  65 */     if (freq1 < freq2) {
/*  66 */       this.maf_freq = (freq1 / (freq1 + freq2));
/*  67 */       this.mode = -1.0D;
/*     */     }
/*     */     else {
/*  70 */       this.maf_freq = (freq2 / (freq1 + freq2));
/*  71 */       this.mode = 1.0D;
/*     */     }
/*     */   }
/*     */   
/*  75 */   static double NA_count = 0.0D;
/*  76 */   static double totalCount = 0.0D;
/*     */   
/*  78 */   static double max_na_fraction = Double.NEGATIVE_INFINITY;
/*     */   
/*  80 */   private double mean = 0.0D;
/*  81 */   private double mode = 0.0D;
/*     */   
/*     */ 
/*     */   IntSNP(List<String> split, String name, char minorAllelle, float maf_freq)
/*     */   {
/*  86 */     this.name = name;
/*  87 */     double na = 0.0D;
/*  88 */     double total = 0.0D;
/*  89 */     this.maf_freq = maf_freq;
/*  90 */     this.snp = new Integer[split.size()];
/*     */     
/*  92 */     Arrays.fill(this.snp, null);
/*  93 */     for (int j = 0; j < split.size(); j++) {
/*  94 */       total += 1.0D;
/*  95 */       String st = (String)split.get(j);
/*  96 */       if ((st.indexOf('N') < 0) && (st.indexOf('*') < 0)) {
/*  97 */         int val = 0;
/*  98 */         for (int k = 0; k < st.length(); k++) {
/*  99 */           if (st.charAt(k) == minorAllelle) val++;
/*     */         }
/* 101 */         this.mean += val;
/* 102 */         this.snp[j] = Integer.valueOf(val);
/*     */       }
/*     */       else {
/* 105 */         na += 1.0D;
/*     */       }
/*     */     }
/*     */     
/* 109 */     double na_frac = na / total;
/* 110 */     this.mean /= (total - na);
/* 111 */     if (na_frac > max_na_fraction) max_na_fraction = na_frac;
/* 112 */     NA_count += na;
/* 113 */     totalCount += total;
/* 114 */     if (na > 0.0D) {
/* 115 */       Logger.global.fine("na " + NA_count / totalCount + " " + max_na_fraction);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 122 */   static int[] count = new int[4];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static SNP makeSNP(String name, String location, List<String> split)
/*     */     throws Exception
/*     */   {
/* 130 */     throw new Error("Unresolved compilation problems: \n\tdna cannot be resolved\n\tdna cannot be resolved\n\tdna cannot be resolved\n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/association/io/IntSNP.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */