/*     */ package lc1.dp.illumina;
/*     */ 
/*     */ import JSci.maths.statistics.ProbabilityDistribution;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import lc1.stats.Interpolation;
/*     */ import lc1.stats.SkewNormal;
/*     */ import lc1.stats.TrainableNormal;
/*     */ import lc1.stats.UniformDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ public class IlluminaProbR
/*     */ {
/*     */   static final double round = 100.0D;
/*     */   protected ProbabilityDistribution[] r;
/*     */   protected int[][] r_alias;
/*     */   protected boolean[] r_train;
/*     */   Interpolation meanI;
/*     */   Interpolation skewI;
/*     */   Interpolation varI;
/*  27 */   SkewNormal roaming = new SkewNormal(0.0D, 0.0D, 0.0D, -5.0D, 5.0D, 100.0D, 1.0D);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IlluminaProbR(List<Integer> cn, List<Integer> bg_cn, double[] x, double[] mean, double[] var, double[] skew, boolean train)
/*     */   {
/*  44 */     this.meanI = new Interpolation(x, mean);
/*  45 */     this.skewI = new Interpolation(x, skew);
/*  46 */     this.varI = new Interpolation(x, var);
/*     */     
/*     */ 
/*  49 */     this.r_alias = new int[cn.size()][cn.size()];
/*  50 */     SortedMap<Double, ProbabilityDistribution> r_dist = new TreeMap();
/*  51 */     for (int i = 0; i < cn.size(); i++) {
/*  52 */       double cn_i = ((Integer)cn.get(i)).intValue();
/*  53 */       if (cn_i == 0.0D)
/*     */       {
/*  55 */         r_dist.put(Double.valueOf(Double.POSITIVE_INFINITY), 
/*  56 */           bg_cn.contains(new Integer((int)cn_i)) ? 
/*  57 */           new UniformDistribution(-7.0D, 7.0D) : null);
/*     */       }
/*     */       else
/*  60 */         for (int j = 0; j < cn.size(); j++) {
/*  61 */           double ratio = ((Integer)cn.get(j)).intValue() / cn_i;
/*  62 */           if (r_dist.get(Double.valueOf(ratio)) == null) {
/*  63 */             double mean_i = this.meanI.getScore(ratio);
/*  64 */             double var_i = this.varI.getScore(ratio);
/*  65 */             double skew_i = this.skewI.getScore(ratio);
/*  66 */             double[] lower = { -5.0D, 0.001D, -1.0E10D };
/*  67 */             double[] upper = { 5.0D, 1000.0D, 1.0E10D };
/*     */             
/*  69 */             if (bg_cn.contains(new Integer((int)cn_i)))
/*     */             {
/*  71 */               if (Math.abs(mean_i - 0.175D) < 0.01D) {
/*  72 */                 throw new RuntimeException("!!");
/*     */               }
/*  74 */               r_dist.put(Double.valueOf(ratio), 
/*  75 */                 skew_i == 0.0D ? new TrainableNormal(mean_i, var_i, 100.0D, 1.0D) : 
/*  76 */                 new SkewNormal(mean_i, var_i, 
/*  77 */                 skew_i, lower, upper, 100.0D, 1.0D));
/*     */             }
/*     */             else {
/*  80 */               r_dist.put(Double.valueOf(ratio), null);
/*     */             }
/*     */           }
/*     */         }
/*     */     }
/*  85 */     this.r = ((ProbabilityDistribution[])r_dist.values().toArray(new ProbabilityDistribution[0]));
/*  86 */     this.r_train = new boolean[this.r.length];
/*  87 */     Arrays.fill(this.r_train, train);
/*  88 */     for (int i = 0; i < this.r_alias.length; i++) {
/*  89 */       double noCop_i = ((Integer)cn.get(i)).intValue();
/*  90 */       if (noCop_i == 0.0D) {
/*  91 */         double ratio = Double.POSITIVE_INFINITY;
/*  92 */         Arrays.fill(this.r_alias[i], r_dist.headMap(Double.valueOf(ratio)).size());
/*  93 */         this.r_train[this.r_alias[i][0]] = false;
/*     */       }
/*     */       else {
/*  96 */         for (int j = 0; j < this.r_alias.length; j++) {
/*  97 */           double noCop_j = ((Integer)cn.get(j)).intValue();
/*  98 */           double ratio = noCop_j / noCop_i;
/*  99 */           this.r_alias[i][j] = r_dist.headMap(Double.valueOf(ratio)).size();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double sampleR(int obj_i, int obj_j)
/*     */   {
/* 110 */     int r_al = this.r_alias[obj_i][obj_j];
/* 111 */     return ((SkewNormal)this.r[r_al]).rsn();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 120 */   double[] meanvarskew = Constants.meanvarskewprior();
/*     */   
/* 122 */   public void rMaximistation(double pseudo, int i1) { boolean chnged = false;
/* 123 */     for (int i = 0; i < this.r.length; i++) {
/* 124 */       ProbabilityDistribution dist_r = this.r[i];
/* 125 */       if ((this.r_train[i] != 0) && ((dist_r instanceof SkewNormal)))
/*     */       {
/* 127 */         ((SkewNormal)dist_r).maximise(pseudo * this.meanvarskew[0], pseudo * this.meanvarskew[1], pseudo * this.meanvarskew[2]);
/* 128 */         chnged = true;
/*     */       }
/*     */     }
/*     */     
/* 132 */     if (chnged) this.paramIndex += 1;
/*     */   }
/*     */   
/* 135 */   public void initialiseRCounts() { for (int i = 0; i < this.r.length; i++) {
/* 136 */       ProbabilityDistribution dist_r = this.r[i];
/* 137 */       if ((dist_r instanceof SkewNormal)) ((SkewNormal)dist_r).initialiseCounts();
/*     */     }
/*     */   }
/*     */   
/*     */   public void addRCount(int obj_i, int obj_j, double weight, double r_i, int i) {
/* 142 */     int r_al = this.r_alias[obj_i][obj_j];
/* 143 */     ProbabilityDistribution dist_r = this.r[r_al];
/* 144 */     if ((dist_r instanceof SkewNormal)) ((SkewNormal)dist_r).addCount(r_i, weight);
/*     */   }
/*     */   
/* 147 */   public void addRCount(double obj_i, int obj_j, double weight, double r_i, int i) { throw new RuntimeException("!!"); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(PrintWriter pw)
/*     */   {
/* 154 */     List<ProbabilityDistribution> r1 = new ArrayList();
/* 155 */     for (int i = 0; i < this.r.length; i++) {
/* 156 */       if (this.r[i] != null) {
/* 157 */         r1.add(this.r[i]);
/*     */       }
/*     */     }
/* 160 */     pw.println("Illumina dist");
/* 161 */     pw.println("intensity: ");
/* 162 */     pw.println("x = seq(-2,2, 0.001)");
/* 163 */     for (int i = 0; i < r1.size(); i++) {
/* 164 */       pw.println("param" + i + "=" + ((ProbabilityDistribution)r1.get(i)).toString());
/*     */     }
/* 166 */     pw.print("param = rbind(param0");
/* 167 */     for (int i = 1; i < r1.size(); i++) {
/* 168 */       pw.print(", param" + i);
/*     */     }
/* 170 */     pw.println("); \n");
/* 171 */     pw.println("i=4");
/* 172 */     pw.println("plot(x, dsn(x,param[i,1], param[i,2], param[i,3]), col=i, type = \"l\")");
/* 173 */     pw.println("for(i in 1:" + r1.size() + "){");
/* 174 */     pw.println("lines(x, dsn(x,param[i,1], param[i,2], param[i,3]), col=i, type = \"l\")");
/* 175 */     pw.println("}\n");
/*     */   }
/*     */   
/*     */   public double calcR(int obj_i, int obj_j, double r_i)
/*     */   {
/* 180 */     int r_al = this.r_alias[obj_i][obj_j];
/* 181 */     double res = this.r[r_al].probability(r_i);
/*     */     
/* 183 */     return res;
/*     */   }
/*     */   
/*     */   public double calcR(double obj_i, int obj_j, double r_i, int i)
/*     */   {
/* 188 */     double ratio = obj_j / obj_i;
/* 189 */     this.roaming.setParams(this.meanI.getScore2(ratio), 
/* 190 */       this.varI.getScore2(ratio), 
/* 191 */       this.skewI.getScore2(ratio));
/* 192 */     return this.roaming.probability(r_i);
/*     */   }
/*     */   
/* 195 */   int paramIndex = 1;
/*     */   
/* 197 */   public int getParamIndex() { return this.paramIndex; }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/illumina/IlluminaProbR.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */