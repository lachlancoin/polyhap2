/*     */ package lc1.dp.data.classification;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import lc1.dp.data.representation.CSOData;
/*     */ import lc1.dp.data.representation.ComparableArray;
/*     */ import lc1.dp.states.EmissionState;
/*     */ 
/*     */ 
/*     */ public abstract class ErrorClassificationAbstract
/*     */ {
/*  15 */   static boolean exclCopyMisMatch = true;
/*     */   protected final List<Comparable> emSt;
/*  17 */   protected StringBuffer formatStr = new StringBuffer();
/*  18 */   protected StringBuffer formatStr1 = new StringBuffer();
/*     */   
/*     */   private int[][][] errorcase;
/*     */   
/*  22 */   static int no_bins = 10;
/*  23 */   private static double incr = 1.0D / no_bins;
/*     */   
/*     */   private int[][][] error;
/*     */   
/*     */   protected final double thresh;
/*     */   
/*     */   protected final ErrorClassificationAbstract parent;
/*     */   public final PrintWriter log;
/*     */   
/*     */   public ErrorClassificationAbstract(List<Comparable> ss, double threshold, ErrorClassificationAbstract parent, Integer len1, int no_sources, PrintWriter log)
/*     */   {
/*  34 */     this.log = log;
/*  35 */     int len = len1 == null ? ss.size() : len1.intValue();
/*  36 */     this.emSt = ss;
/*  37 */     this.parent = parent;
/*  38 */     this.thresh = threshold;
/*     */     
/*  40 */     for (int i = 0; i < len; i++)
/*     */     {
/*  42 */       this.formatStr.append("%10i ");
/*  43 */       this.formatStr1.append("%10s ");
/*     */     }
/*     */     
/*  46 */     this.formatStr.append("%7i ");
/*  47 */     this.formatStr1.append("%7s ");
/*  48 */     this.errorcase = new int[no_sources][][];
/*  49 */     this.error = new int[no_sources][no_bins + 1][2];
/*  50 */     for (int k = 0; k < no_sources; k++) {
/*  51 */       this.errorcase[k] = init(len);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected int[][] init(int i)
/*     */   {
/*  58 */     int[][] res = new int[i][i];
/*  59 */     for (int j = 0; j < i; j++) {
/*  60 */       Arrays.fill(res[j], 0);
/*     */     }
/*  62 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void compare(CSOData orig, CSOData pred, EmissionState predState, int[][] res, boolean[] unc)
/*     */   {
/*  70 */     int[] fromTo = new int[2];
/*  71 */     for (int k = 0; k < res.length; k++) {
/*  72 */       for (int i1 = 0; i1 < res[k].length; i1++) {
/*  73 */         int i = res[k][i1];
/*  74 */         double cert = orig.getUncertainty(predState, i);
/*  75 */         if ((cert >= this.thresh) && (
/*  76 */           (unc == null) || (unc[i] != 0))) {
/*     */           try {
/*  78 */             ComparableArray compA = (ComparableArray)orig.getElement(i);
/*  79 */             ComparableArray compB = (ComparableArray)pred.getElement(i);
/*  80 */             if ((matchAtHigherLevel(compA, compB)) && 
/*  81 */               (wobbleRoom(compA) > 1))
/*     */             {
/*  83 */               compare(compA, compB, fromTo);
/*  84 */               this.errorcase[k][fromTo[0]][fromTo[1]] += 1;
/*  85 */               int bin = (int)Math.floor(cert / incr);
/*  86 */               this.error[k][bin][(fromTo[0] == fromTo[1] ? 0 : 1)] += 1;
/*  87 */               if ((this.log != null) && (fromTo[0] != fromTo[1])) {
/*  88 */                 this.log.println(k + " mismatch  " + orig.getName() + " " + i1 + " " + compA + " " + compB);
/*     */               }
/*     */             }
/*     */           }
/*     */           catch (Exception exc)
/*     */           {
/*  94 */             exc.printStackTrace();
/*  95 */             System.err.println(getClass());
/*  96 */             System.err.println(fromTo[0] + " " + fromTo[1]);
/*  97 */             System.err.println(this.errorcase.length + " " + this.errorcase[0].length);
/*  98 */             System.err.println("problem at " + i);
/*  99 */             System.err.println(orig);
/* 100 */             System.err.println(pred);
/* 101 */             System.exit(0);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract int wobbleRoom(ComparableArray paramComparableArray);
/*     */   
/*     */   protected final void conditionalCompare(ComparableArray orig, ComparableArray pred, int[] fromTo) {
/* 111 */     if (this.parent != null) { this.parent.compare(orig, pred, fromTo);
/*     */     } else {
/* 113 */       fromTo[0] = 0;
/* 114 */       fromTo[1] = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean matchAtHigherLevel(ComparableArray orig, ComparableArray pred) {
/* 119 */     int[] fromTo = new int[2];
/* 120 */     conditionalCompare(orig, pred, fromTo);
/* 121 */     return fromTo[0] == fromTo[1];
/*     */   }
/*     */   
/*     */   protected abstract void compare(ComparableArray paramComparableArray1, ComparableArray paramComparableArray2, int[] paramArrayOfInt);
/*     */   
/*     */   public abstract String getString(int paramInt);
/*     */   
/* 128 */   public void print(PrintWriter pw) { for (int kk = 0; kk < this.errorcase.length; kk++) {
/* 129 */       pw.println("Classifiction for " + getClass().getName() + " " + kk);
/*     */       
/* 131 */       double[] origEqMod = { 0.0D, 0.0D };
/*     */       
/* 133 */       int len = this.errorcase[kk].length + 1;
/* 134 */       String[] header = new String[len];
/* 135 */       for (int j = 0; j < this.errorcase[kk].length; j++) {
/* 136 */         header[j] = getString(j);
/*     */       }
/* 138 */       header[(len - 1)] = "sum";
/* 139 */       pw.println(Format.sprintf("%-10s ", new String[] { "" }) + Format.sprintf(this.formatStr1.toString(), header));
/* 140 */       Integer[] sum = new Integer[len];
/* 141 */       Arrays.fill(sum, Integer.valueOf(0));
/* 142 */       for (int j = 0; j < this.errorcase[kk].length; j++)
/*     */       {
/* 144 */         Double[] d = new Double[len];
/* 145 */         Arrays.fill(d, Double.valueOf(0.0D));
/* 146 */         for (int k = 0; k < this.errorcase[kk][j].length; k++) {
/* 147 */           d[k] = Double.valueOf(this.errorcase[kk][j][k]); int 
/* 148 */             tmp226_224 = k; Integer[] tmp226_222 = sum;tmp226_222[tmp226_224] = Integer.valueOf((int)(tmp226_222[tmp226_224].intValue() + d[k].doubleValue())); int 
/* 149 */             tmp252_251 = (len - 1); Double[] tmp252_246 = d;tmp252_246[tmp252_251] = Double.valueOf(tmp252_246[tmp252_251].doubleValue() + d[k].doubleValue());
/* 150 */           origEqMod[(k == j ? 0 : 1)] += d[k].doubleValue();
/*     */         }
/* 152 */         int tmp319_318 = (len - 1); Integer[] tmp319_313 = sum;tmp319_313[tmp319_318] = Integer.valueOf((int)(tmp319_313[tmp319_318].intValue() + d[(len - 1)].doubleValue()));
/*     */         
/*     */ 
/* 155 */         if (d[(len - 1)].doubleValue() > 0.0D) {
/* 156 */           pw.println(Format.sprintf("%-10s ", new String[] { getString(j) }) + Format.sprintf(this.formatStr.toString(), d));
/*     */         }
/*     */       }
/*     */       
/* 160 */       pw.println(Format.sprintf("%-10s ", new String[] { "sum" }) + Format.sprintf(this.formatStr.toString(), sum));
/* 161 */       pw.println(origEqMod[1] + " " + (origEqMod[1] + origEqMod[0]) + " " + origEqMod[1] / (origEqMod[1] + origEqMod[0]));
/* 162 */       for (int i = 0; i < this.error[kk].length; i++) {
/* 163 */         int[] origEqMo = this.error[kk][i];
/* 164 */         double bot = i * incr;
/* 165 */         double top = (i + 1) * incr;
/* 166 */         Object[] res = { Double.valueOf(bot), Double.valueOf(top), Integer.valueOf(origEqMo[1]), Integer.valueOf(origEqMo[1] + origEqMo[0]), Double.valueOf(origEqMo[1] / (origEqMo[1] + origEqMo[0])) };
/* 167 */         if (((Number)res[3]).doubleValue() > 0.0D) {
/* 168 */           pw.println(Format.sprintf("%5.1f - %5.1f  %5.3f %5.3f %5.3f", res));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/classification/ErrorClassificationAbstract.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */