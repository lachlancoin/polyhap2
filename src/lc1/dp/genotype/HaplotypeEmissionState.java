/*     */ package lc1.dp.genotype;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.PrintWriter;
/*     */ import lc1.dp.Dirichlet;
/*     */ import lc1.dp.EmissionState;
/*     */ import lc1.dp.SimpleDistribution;
/*     */ import lc1.dp.State;
/*     */ import lc1.dp.genotype.io.Constants;
/*     */ import lc1.dp.genotype.io.scorable.ScorableObject;
/*     */ 
/*     */ public class HaplotypeEmissionState
/*     */   extends EmissionState
/*     */   implements ScorableObject
/*     */ {
/*  16 */   public boolean train_j = true;
/*     */   
/*     */ 
/*     */   public SimpleExtendedDistribution[] emissions;
/*     */   
/*     */ 
/*     */   final int noSnps;
/*     */   
/*     */   final NullEmitter nullM;
/*     */   
/*  26 */   private boolean trainGaps_j = true;
/*     */   
/*     */   public void setTrainGaps(boolean b)
/*     */   {
/*  30 */     this.trainGaps_j = b;
/*     */   }
/*     */   
/*     */   public void setTheta(double[] prob, int i) {
/*  34 */     System.arraycopy(prob, 0, this.emissions[i].probs, 0, prob.length);
/*  35 */     System.arraycopy(prob, 0, this.emissions[i].pseudo, 0, prob.length); }
/*     */   
/*  37 */   public static Boolean[] stateSpace = { Boolean.valueOf(true), Boolean.valueOf(false) };
/*     */   
/*     */ 
/*     */   public void transferCountsToProbs(double pseudo)
/*     */   {
/*  42 */     if (Constants.trainEmissions()) {
/*  43 */       for (int i = 0; i < this.emissions.length; i++) {
/*  44 */         double[] counts = this.emissions[i].counts;
/*     */         
/*  46 */         this.emissions[i].transfer(pseudo);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   HaplotypeEmissionState(String name, int noSnps, double u, NullEmitter nullEm)
/*     */   {
/*  54 */     super(name, 1);
/*  55 */     this.nullM = nullEm;
/*  56 */     this.noSnps = noSnps;
/*  57 */     double stateProbOfNull = Constants.stateProbOfNull();
/*  58 */     double stateProbNotNull = (1.0D - stateProbOfNull) / 2.0D;
/*     */     
/*  60 */     this.emissions = new SimpleExtendedDistribution[noSnps];
/*  61 */     for (int i = 0; i < noSnps; i++)
/*  62 */       this.emissions[i] = new SimpleExtendedDistribution(new double[] { stateProbNotNull, stateProbNotNull, stateProbOfNull }, u);
/*     */   }
/*     */   
/*     */   HaplotypeEmissionState(String name, int noSnps, double[] d) {
/*  66 */     super(name, 1);
/*  67 */     this.nullM = null;
/*  68 */     this.noSnps = noSnps;
/*  69 */     this.emissions = new SimpleExtendedDistribution[noSnps];
/*  70 */     for (int i = 0; i < noSnps; i++) {
/*  71 */       this.emissions[i] = new SimpleExtendedDistribution(d, Double.POSITIVE_INFINITY);
/*     */     }
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, String prefix) {
/*  76 */     StringBuffer sb1 = new StringBuffer(prefix);
/*  77 */     for (int i = 0; i < this.noSnps; i++) {
/*  78 */       sb1.append("%8.2g ");
/*     */     }
/*  80 */     pw.println(Format.sprintf(sb1.toString(), emissions(this.emissions, 0)));
/*  81 */     pw.println(Format.sprintf(sb1.toString(), emissions(this.emissions, 2)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HaplotypeEmissionState(HaplotypeEmissionState st_to_init, HaplotypeEmissionState st_to_pseudo, String name)
/*     */   {
/*  88 */     super(name, 1);
/*  89 */     this.nullM = st_to_init.nullM;
/*  90 */     if ((st_to_pseudo != null) && (!st_to_init.getName().equals(st_to_pseudo.getName()))) throw new RuntimeException("!!");
/*  91 */     this.noSnps = st_to_init.noSnps;
/*  92 */     this.emissions = new SimpleExtendedDistribution[this.noSnps];
/*  93 */     for (int i = 0; i < this.noSnps; i++)
/*  94 */       this.emissions[i] = (st_to_pseudo == null ? 
/*  95 */         new SimpleExtendedDistribution(st_to_init.emissions[i]) : 
/*  96 */         new SimpleExtendedDistribution(st_to_init.emissions[i], 
/*  97 */         st_to_pseudo.emissions[i]));
/*     */   }
/*     */   
/*     */   public Object clone(State pseudo) {
/* 101 */     return new HaplotypeEmissionState(this, (HaplotypeEmissionState)pseudo, getName());
/*     */   }
/*     */   
/*     */   public Object clone() {
/* 105 */     return new HaplotypeEmissionState(this, this, getName());
/*     */   }
/*     */   
/*     */ 
/*     */   public void addCount(int obj_index, Double value, int i)
/*     */   {
/* 111 */     if ((this.train_j) && (this.nullM != null)) {
/* 112 */       double p_avg_null = this.nullM.getProb(i, 1);
/* 113 */       double p_st_null = this.emissions[i].probs[2];
/* 114 */       double p_x_null = p_avg_null * p_st_null + 
/* 115 */         p_avg_null * (1.0D - p_st_null) + 
/* 116 */         (1.0D - p_avg_null) * p_st_null;
/*     */       double prob_avg_nonnull_cond;
/* 118 */       double prob_st_null_cond; double prob_avg_null_cond; double prob_st_nonnull_cond; double prob_avg_nonnull_cond; if (obj_index == 2)
/*     */       {
/* 120 */         double prob_st_null_cond = p_st_null / p_x_null;
/* 121 */         double prob_avg_null_cond = p_avg_null / p_x_null;
/* 122 */         double prob_st_nonnull_cond = p_avg_null * (1.0D - p_st_null) / p_x_null;
/* 123 */         prob_avg_nonnull_cond = p_st_null * (1.0D - p_avg_null) / p_x_null;
/*     */       }
/*     */       else {
/* 126 */         prob_st_null_cond = 0.0D;
/* 127 */         prob_avg_null_cond = 0.0D;
/* 128 */         prob_st_nonnull_cond = (1.0D - p_st_null) * (1.0D - p_avg_null) / (1.0D - p_x_null);
/* 129 */         prob_avg_nonnull_cond = (1.0D - p_st_null) * (1.0D - p_avg_null) / (1.0D - p_x_null);
/*     */       }
/*     */       
/*     */ 
/* 133 */       this.nullM.addCounts(i, 1, prob_avg_null_cond * value.doubleValue());
/*     */       
/* 135 */       this.nullM.addCounts(i, 0, prob_avg_nonnull_cond * value.doubleValue());
/*     */       
/* 137 */       this.emissions[i].counts[2] += prob_st_null_cond * value.doubleValue();
/* 138 */       if (obj_index != 2) { this.emissions[i].counts[obj_index] += value.doubleValue();
/*     */       } else {
/* 140 */         double nonnullprob = 1.0D - p_st_null;
/* 141 */         this.emissions[i].counts[0] += prob_st_nonnull_cond * value.doubleValue() * (this.emissions[i].probs[0] / nonnullprob);
/* 142 */         this.emissions[i].counts[1] += prob_st_nonnull_cond * value.doubleValue() * (this.emissions[i].probs[1] / nonnullprob);
/*     */       }
/*     */     }
/*     */     else {
/* 146 */       this.emissions[i].counts[obj_index] += value.doubleValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public void initialiseCounts()
/*     */   {
/* 152 */     for (int i = 0; i < this.noSnps; i++) {
/* 153 */       this.emissions[i].initialise();
/*     */     }
/*     */   }
/*     */   
/*     */   public double KLDistance(EmissionState st) {
/* 158 */     double sum = 0.0D;
/* 159 */     HaplotypeEmissionState hes = (HaplotypeEmissionState)st;
/* 160 */     for (int i = 0; i < this.emissions.length; i++) {
/* 161 */       if (this.emissions[i].probs[0] != 0.0D)
/* 162 */         sum += this.emissions[i].probs[0] * Math.log(this.emissions[i].probs[0] / hes.emissions[i].probs[0]);
/*     */     }
/* 164 */     return sum / this.emissions.length;
/*     */   }
/*     */   
/*     */   public Object sample(int i) {
/* 168 */     int k = this.emissions[i].sample();
/* 169 */     int k1 = this.nullM.sample(i);
/* 170 */     if (k1 == 1) return null;
/* 171 */     return stateSpace[k];
/*     */   }
/*     */   
/*     */   public double score(int obj_i, int i) {
/* 175 */     if (this.nullM != null) {
/* 176 */       double p_avg_null = this.nullM.getProb(i, 1);
/* 177 */       double p_st_null = this.emissions[i].probs[2];
/* 178 */       double p_x_null = p_avg_null * p_st_null + 
/* 179 */         p_avg_null * (1.0D - p_st_null) + 
/* 180 */         (1.0D - p_avg_null) * p_st_null;
/*     */       
/*     */ 
/* 183 */       return obj_i == 2 ? p_x_null : 
/* 184 */         (1.0D - p_x_null) * (this.emissions[i].probs[obj_i] / (1.0D - p_st_null));
/*     */     }
/*     */     
/* 187 */     return this.emissions[i].probs[obj_i];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRandom(double emiss, boolean restart)
/*     */   {
/* 195 */     for (int i = 0; i < this.noSnps; i++) {
/* 196 */       this.emissions[i].setRandom(emiss, restart);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Double[] emissions(SimpleExtendedDistribution[] d, int k)
/*     */   {
/* 202 */     Double[] res = new Double[d.length];
/* 203 */     for (int i = 0; i < res.length; i++) {
/* 204 */       res[i] = Double.valueOf(d[i].probs[k]);
/* 205 */       if (res[i].doubleValue() < 0.01D) { res[i] = Double.valueOf(0.0D);
/* 206 */       } else if (res[i].doubleValue() > 0.99D) { res[i] = Double.valueOf(1.0D);
/*     */       }
/*     */     }
/* 209 */     return res;
/*     */   }
/*     */   
/*     */   public String toString(int i)
/*     */   {
/* 214 */     return getName();
/*     */   }
/*     */   
/*     */   public void validate()
/*     */     throws Exception
/*     */   {
/* 220 */     this.lengthDistrib.validate();
/* 221 */     for (int i = 0; i < this.noSnps; i++) {
/* 222 */       double sum = this.emissions[i].sum();
/* 223 */       if (Math.abs(1.0D - sum) > 0.001D) {
/* 224 */         throw new RuntimeException("invalid! " + this.emissions[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Object mostLikely(int pos)
/*     */   {
/* 231 */     int k1 = this.nullM.mostLikely(pos);
/* 232 */     if (this.emissions[pos].probs[0] > 0.5D) return Boolean.TRUE;
/* 233 */     return Boolean.FALSE;
/*     */   }
/*     */   
/*     */   public void fix() {
/* 237 */     setChangedParams(true);
/* 238 */     for (int i = 0; i < this.emissions.length; i++) {
/* 239 */       this.emissions[i].fix();
/* 240 */       this.train_j = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public void initValues(HaplotypeEmissionState state) {}
/*     */   
/*     */   public void reinitialise(Double[] maf_cases, double u)
/*     */   {
/* 248 */     for (int i = 0; i < this.emissions.length; i++) {
/* 249 */       if (u == Double.POSITIVE_INFINITY) {
/* 250 */         this.emissions[i].probs[0] = maf_cases[i].doubleValue();
/* 251 */         this.emissions[i].probs[1] = (1.0D - maf_cases[i].doubleValue());
/*     */       }
/*     */       else {
/* 254 */         Dirichlet dir = new Dirichlet(new Double[] { maf_cases[i], Double.valueOf(1.0D - maf_cases[i].doubleValue()) }, u);
/* 255 */         Double[] d = dir.sample();
/* 256 */         this.emissions[i].probs[0] = d[0].doubleValue();
/* 257 */         this.emissions[i].probs[1] = d[1].doubleValue();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Object getElement(int i) {
/* 263 */     return this;
/*     */   }
/*     */   
/* 266 */   public int length() { return this.emissions.length; }
/*     */   
/*     */   public void print(PrintWriter pw) {
/* 269 */     print(pw, "");
/*     */   }
/*     */   
/*     */   public void recombine(HaplotypeEmissionState to, int i) {
/* 273 */     SimpleExtendedDistribution[] fromEm = this.emissions;
/* 274 */     SimpleExtendedDistribution[] toEm = to.emissions;
/* 275 */     SimpleExtendedDistribution[] fromEm1 = new SimpleExtendedDistribution[fromEm.length];
/* 276 */     int len = fromEm.length;
/* 277 */     System.arraycopy(fromEm, 0, fromEm1, 0, i);
/* 278 */     System.arraycopy(toEm, i, fromEm1, i, len - i);
/* 279 */     System.arraycopy(fromEm, i, toEm, i, len - i);
/* 280 */     this.emissions = fromEm1;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/HaplotypeEmissionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */