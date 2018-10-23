/*     */ package lc1.ensj;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SummaryFileReader
/*     */ {
/*  15 */   public static void main(String[] args) { SummaryFileReader sfr = new SummaryFileReader(new File(".")); }
/*     */   
/*     */   public SummaryFileReader(File dir) {
/*  18 */     File[] f = dir.listFiles(new java.io.FileFilter()
/*     */     {
/*     */       public boolean accept(File pathname) {
/*  21 */         return pathname.isDirectory();
/*     */       }
/*     */     });
/*     */     
/*  25 */     for (int i = 0; i < f.length; i++) {
/*     */       try {
/*  27 */         read(new File(f[i], "summary4.txt"));
/*     */       } catch (Exception exc) {
/*  29 */         exc.printStackTrace();
/*     */       }
/*     */     }
/*  32 */     this.siteRes1.normalise();
/*  33 */     this.siteRes2.normalise();
/*  34 */     System.err.println(this.siteRes1.toString());
/*  35 */     System.err.println(this.siteRes2.toString());
/*  36 */     double tot = ((OverallResults)this.m.get(Double.valueOf(0.5D))).res1[1];
/*  37 */     for (Iterator<OverallResults> it = this.m.values().iterator(); it.hasNext();) {
/*  38 */       OverallResults oe = (OverallResults)it.next();
/*  39 */       oe.res1[1] = tot;
/*  40 */       System.err.println(oe);
/*     */     }
/*     */   }
/*     */   
/*  44 */   Map<Double, OverallResults> m = new java.util.TreeMap();
/*  45 */   SiteResults siteRes1 = new SiteResults(1);
/*  46 */   SiteResults siteRes2 = new SiteResults(2);
/*  47 */   int cnt = 0;
/*     */   
/*     */   public void read(File f) throws Exception {
/*  50 */     BufferedReader br = new BufferedReader(new java.io.FileReader(f));
/*  51 */     String st = "";
/*  52 */     double tot = 0.0D;
/*  53 */     while ((st = br.readLine()) != null) {
/*  54 */       if (st.startsWith("thresh")) {
/*  55 */         double thresh = Double.parseDouble(st.split("\\s+")[2]);
/*     */         
/*  57 */         for (int i = 0; i < 6; i++) {
/*  58 */           st = br.readLine();
/*     */         }
/*  60 */         OverallResults oe = (OverallResults)this.m.get(Double.valueOf(thresh));
/*  61 */         if (oe == null) {
/*  62 */           this.m.put(Double.valueOf(thresh), oe = new OverallResults(thresh));
/*     */         }
/*  64 */         oe.append(st.split("\\s+"));
/*     */       }
/*  66 */       else if (st.startsWith("cnt thresh 1")) {
/*  67 */         this.siteRes1.append(br);
/*  68 */         br.readLine();
/*  69 */         this.siteRes2.append(br);
/*  70 */         this.cnt += 1;
/*     */       }
/*     */     }
/*  73 */     br.close();
/*     */   }
/*     */   
/*     */ 
/*     */   class OverallResults
/*     */   {
/*     */     final double threshold;
/*     */     
/*  81 */     OverallResults(double th) { this.threshold = th; }
/*     */     
/*  83 */     double[] res1 = { 0.0D, 0.0D };
/*  84 */     double[] res2 = { 0.0D, 0.0D };
/*  85 */     double[] res3 = { 0.0D, 0.0D };
/*     */     
/*  87 */     public void append(String[] st) { for (int i = 0; i < st.length; i++) {
/*  88 */         st[i] = st[i].replace(':', ' ');
/*     */       }
/*  90 */       double d1 = Double.parseDouble(st[2].trim());
/*  91 */       this.res1[0] += d1 - Double.parseDouble(st[1].trim());
/*  92 */       this.res1[1] += d1;
/*  93 */       this.res2[0] += Double.parseDouble(st[4].trim());
/*  94 */       this.res2[1] += Double.parseDouble(st[5].trim());
/*  95 */       this.res3[0] += Double.parseDouble(st[7].trim());
/*  96 */       this.res3[1] += Double.parseDouble(st[8].trim());
/*     */     }
/*     */     
/*     */     public String toString() {
/* 100 */       StringBuffer sb = new StringBuffer();
/* 101 */       sb.append(this.threshold + ": " + this.res1[0] / this.res1[1] + " // " + this.res2[0] / this.res2[1] + " //  " + this.res3[0] / this.res3[1]);
/* 102 */       return sb.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   class SiteResults { int cntThresh;
/* 107 */     double[] thresh = { 0.5D, 0.6D, 0.7D, 0.8D, 0.9D, 0.95D };
/* 108 */     double[][] res = new double[6][2];
/* 109 */     int[] count = new int[6];
/*     */     
/* 111 */     SiteResults(int cnt) { for (int i = 0; i < this.res.length; i++) {
/* 112 */         Arrays.fill(this.res[i], 0.0D);
/*     */       }
/* 114 */       Arrays.fill(this.count, 0);
/* 115 */       this.cntThresh = cnt;
/*     */     }
/*     */     
/* 118 */     public void normalise() { for (int i = 0; i < this.res.length; i++) {
/* 119 */         this.res[i][0] /= this.count[i];
/* 120 */         this.res[i][1] /= this.count[i];
/*     */       }
/*     */     }
/*     */     
/*     */     public void append(BufferedReader br) throws Exception {
/* 125 */       boolean failed = false;
/* 126 */       for (int i = 0; i < this.res.length; i++) {
/* 127 */         String[] str = br.readLine().split("\\s+");
/*     */         try {
/* 129 */           double d1 = Double.parseDouble(str[11]);
/* 130 */           double d2 = Double.parseDouble(str[12]);
/*     */           
/*     */ 
/* 133 */           if ((Double.isNaN(d1)) || (Double.isNaN(d2))) {
/* 134 */             Logger.global.warning("!! " + Arrays.asList(str));
/*     */           }
/*     */           else {
/* 137 */             this.res[i][0] += d1;
/* 138 */             this.res[i][1] += d2;
/* 139 */             this.count[i] += 1;
/*     */           }
/*     */         } catch (Exception exc) {
/* 142 */           exc.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 149 */       StringBuffer sb = new StringBuffer("cnt thresh " + this.cntThresh + "\n");
/* 150 */       for (int i = 0; i < this.res.length; i++) {
/* 151 */         sb.append(this.thresh[i] + ": " + this.res[i][0] + " " + this.res[i][1] + "\n");
/*     */       }
/* 153 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/ensj/SummaryFileReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */