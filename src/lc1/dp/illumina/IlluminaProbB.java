/*     */ package lc1.dp.illumina;
/*     */ 
/*     */ import JSci.maths.statistics.ProbabilityDistribution;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import lc1.dp.data.representation.ComparableArray;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.emissionspace.CompoundEmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.stats.SkewNormal;
/*     */ import lc1.stats.TrainableNormal;
/*     */ import lc1.stats.UniformDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class IlluminaProbB
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  25 */     CompoundEmissionStateSpace stSp = Emiss.getEmissionStateSpace(1, 2);
/*  26 */     IlluminaProbB probB = new IlluminaProbB(stSp, true);
/*  27 */     List<Double> l = new ArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  32 */   static final double round = ;
/*     */   
/*     */   public ProbabilityDistribution[] b;
/*     */   public final int[] b_alias;
/*     */   private double[] bfrac;
/*     */   final boolean[] b_train;
/*  38 */   double SIGMA_B = 0.05D;
/*     */   
/*     */   public IlluminaProbB(EmissionStateSpace emStSp, boolean train)
/*     */   {
/*  42 */     SortedMap<Double, ProbabilityDistribution> b_dist = new TreeMap();
/*  43 */     this.b_alias = new int[emStSp.size()];
/*  44 */     double[] b_mean = Constants.b_mean;
/*  45 */     double[] b_var = Constants.b_var;
/*  46 */     double[] b_skew = Constants.b_skew;
/*  47 */     double[] lower = { 0.0D, 0.001D, -1.0E10D };
/*  48 */     double[] upper = { 1.0D, 1000.0D, 1.0E10D };
/*  49 */     b_dist.put(Double.valueOf(NaN.0D), new UniformDistribution(0.0D, 1.0D));
/*  50 */     for (int i = 0; i < b_mean.length; i++) {
/*  51 */       double skew_i = b_skew[i];
/*  52 */       double mean_i = b_mean[i];
/*  53 */       double var_i = b_var[i];
/*  54 */       b_dist.put(Double.valueOf(b_mean[i]), skew_i == 0.0D ? new TrainableNormal(mean_i, var_i, round, 1.0D) : 
/*  55 */         new SkewNormal(mean_i, var_i, 
/*  56 */         skew_i, lower, upper, round, 1.0D));
/*     */     }
/*     */     
/*  59 */     for (int i = 0; i < this.b_alias.length; i++) {
/*  60 */       ComparableArray comp = (ComparableArray)emStSp.get(i);
/*  61 */       double noCop = comp.noCopies(true);
/*  62 */       double fracB = comp.noB() / noCop;
/*     */       
/*  64 */       if (!b_dist.containsKey(Double.valueOf(fracB))) {
/*  65 */         throw new RuntimeException("!! not enough " + fracB);
/*     */       }
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */     this.b = ((ProbabilityDistribution[])b_dist.values().toArray(new ProbabilityDistribution[0]));
/* 104 */     this.b_train = new boolean[this.b.length];
/* 105 */     this.bfrac = new double[this.b.length];
/* 106 */     Arrays.fill(this.b_train, train);
/* 107 */     for (int i = 0; i < this.b_alias.length; i++) {
/* 108 */       ComparableArray comp = (ComparableArray)emStSp.get(i);
/* 109 */       double noCop = comp.noCopies(true);
/* 110 */       double fracB = comp.noB() / noCop;
/* 111 */       this.b_alias[i] = b_dist.headMap(Double.valueOf(fracB)).size();
/* 112 */       this.bfrac[this.b_alias[i]] = fracB;
/*     */     }
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw) {
/* 117 */     pw.println("Illumina dist b allele");
/* 118 */     pw.println("param = rbind(param0, param1, param2, param3, param4); \n");
/* 119 */     pw.println("i=4");
/* 120 */     pw.println("plot(x, dsn(x,param[i,1], param[i,2], param[i,3]), col=i, type = \"l\")");
/* 121 */     pw.println("for(i in 1:5){");
/* 122 */     pw.println("lines(x, dsn(x,param[i,1], param[i,2], param[i,3]), col=i, type = \"l\")");
/* 123 */     pw.println("}\n");
/* 124 */     pw.println("b allele: ");
/* 125 */     pw.println("x = seq(0,1, 0.001)");
/* 126 */     for (int i = 0; i < this.b.length; i++) {
/* 127 */       if (!(this.b[i] instanceof UniformDistribution)) {
/* 128 */         pw.println("param" + i + "=" + this.b[i].toString());
/*     */       }
/*     */     }
/* 131 */     pw.println("param = rbind(param0, param1, param2, param3, param4, param5, param6); \n");
/* 132 */     pw.println("i=1");
/* 133 */     pw.println("plot(x, dsn(x,param[i,1], param[i,2], param[i,3]), col=i, type = \"l\")");
/* 134 */     pw.println("for(i in 1:7){");
/* 135 */     pw.println("lines(x, dsn(x,param[i,1], param[i,2], param[i,3]), col=i, type = \"l\")");
/* 136 */     pw.println("}\n");
/*     */   }
/*     */   
/*     */   public double calcB(int obj_index, double b_i)
/*     */   {
/* 141 */     int b_al = this.b_alias[obj_index];
/*     */     
/* 143 */     return this.b[b_al].probability(b_i);
/*     */   }
/*     */   
/*     */   public void initialiseBCounts() {
/* 147 */     this.totalCount = 0.0D;
/* 148 */     for (int i = 0; i < this.b.length; i++) {
/* 149 */       ProbabilityDistribution dist_b = this.b[i];
/* 150 */       if ((dist_b instanceof SkewNormal)) { ((SkewNormal)dist_b).initialiseCounts();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 155 */   double[] meanvarskew = Constants.meanvarskewprior();
/*     */   
/* 157 */   public void bMaximistation(double pseudo, int i1) { boolean cged = false;
/* 158 */     for (int i = 0; i < this.b.length; i++) {
/* 159 */       ProbabilityDistribution dist_b = this.b[i];
/* 160 */       if ((this.b_train[i] != 0) && ((dist_b instanceof SkewNormal))) {
/* 161 */         cged = true;
/* 162 */         ((SkewNormal)dist_b)
/* 163 */           .maximise(pseudo * this.meanvarskew[0], pseudo * this.meanvarskew[1], pseudo * this.meanvarskew[2]);
/*     */       }
/*     */     }
/* 166 */     if (cged) this.paramIndex += 1;
/*     */   }
/*     */   
/* 169 */   public double sampleB(int obj_index) { int b_al = this.b_alias[obj_index];
/* 170 */     if ((this.b[b_al] instanceof UniformDistribution)) return Constants.rand.nextDouble();
/* 171 */     return ((SkewNormal)this.b[b_al]).rsn();
/*     */   }
/*     */   
/*     */   public void addBCount(int obj_index, double weight, double b_i)
/*     */   {
/* 176 */     int b_al = this.b_alias[obj_index];
/* 177 */     ProbabilityDistribution dist_b = this.b[b_al];
/* 178 */     if ((dist_b instanceof SkewNormal)) {
/* 179 */       ((SkewNormal)dist_b).addCount(b_i, weight);
/* 180 */       this.totalCount += weight;
/*     */     }
/*     */   }
/*     */   
/* 184 */   int paramIndex = 1;
/*     */   
/* 186 */   public int getParamIndex() { return this.paramIndex; }
/*     */   
/* 188 */   double totalCount = 0.0D;
/*     */   
/*     */   public double totalCount()
/*     */   {
/* 192 */     return this.totalCount;
/*     */   }
/*     */   
/*     */   public void probDists(List<SkewNormal> res, List<Integer> b_frac) {
/* 196 */     for (int i = 0; i < this.b.length; i++) {
/* 197 */       if ((this.b[i] instanceof SkewNormal)) {
/* 198 */         res.add((SkewNormal)this.b[i]);
/* 199 */         b_frac.add(Integer.valueOf(i));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/illumina/IlluminaProbB.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */