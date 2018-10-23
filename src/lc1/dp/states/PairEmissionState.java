/*     */ package lc1.dp.states;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import lc1.dp.emissionspace.CompoundEmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.stats.SkewNormal;
/*     */ 
/*     */ public class PairEmissionState extends CompoundState
/*     */ {
/*     */   private final EmissionState[] dist;
/*     */   protected final CompoundEmissionStateSpace emStSp;
/*     */   final boolean decomp;
/*     */   final int[] indices_tmp;
/*     */   String name1;
/*     */   final int noCopies;
/*     */   final Integer noCop;
/*     */   final IlluminaProbB[] probB;
/*     */   SkewNormal[] probR;
/*     */   
/*     */   public Object clone()
/*     */   {
/*  26 */     PairEmissionState res = new PairEmissionState(copy(this.dist), this.emStSp, this.decomp, this.probB, this.probR);
/*     */     
/*  28 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */   public Integer getFixedInteger(int i)
/*     */   {
/*  34 */     return null;
/*     */   }
/*     */   
/*     */   public Integer calculateIndex(int i)
/*     */   {
/*  39 */     EmissionState[] states = getMemberStates(false);
/*     */     
/*     */ 
/*     */ 
/*  43 */     for (int j = 0; j < states.length; j++) {
/*  44 */       if ((states[j] instanceof CompoundState)) {
/*  45 */         Integer ind = ((CompoundState)states[j]).calculateIndex(i);
/*  46 */         if (ind == null) return null;
/*  47 */         this.indices_tmp[j] = ind.intValue();
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*  54 */         Integer ind = states[j].getFixedInteger(i);
/*  55 */         if (ind == null) return null;
/*  56 */         this.indices_tmp[j] = ind.intValue();
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
/*  70 */     Integer result = Integer.valueOf(this.emStSp.getIndex(this.indices_tmp));
/*  71 */     if (result == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */       throw new RuntimeException("is null " + this.indices_tmp[0] + " " + this.indices_tmp[1] + " " + this.indices_tmp.length + " ");
/*     */     }
/*     */     
/*     */ 
/*  81 */     if (result.intValue() > 30) {
/*  82 */       throw new RuntimeException("!!");
/*     */     }
/*  84 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public double[] getEmiss(int i)
/*     */   {
/*  90 */     throw new RuntimeException("should not call this!");
/*     */   }
/*     */   
/*     */   public String toString(int i) {
/*  94 */     StringBuffer sb = new StringBuffer(this.dist[0].toString(i));
/*  95 */     for (int j = 1; j < this.dist.length; j++) {
/*  96 */       sb.append(this.dist[j].toString(i));
/*     */     }
/*  98 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected static List<EmissionState> copy(EmissionState[] em) {
/* 102 */     EmissionState[] res = new EmissionState[em.length];
/* 103 */     for (int k = 0; k < res.length; k++) {
/* 104 */       res[k] = ((EmissionState)em[k].clone());
/*     */     }
/* 106 */     return java.util.Arrays.asList(res);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int mostLikely(int pos)
/*     */   {
/* 113 */     int[] mostLikely = new int[this.dist.length];
/* 114 */     for (int j = 0; j < mostLikely.length; j++) {
/* 115 */       mostLikely[j] = getInnerState(j, false).mostLikely(pos);
/*     */     }
/* 117 */     return this.emStSp.getIndex(mostLikely);
/*     */   }
/*     */   
/*     */   static String getConcatenation(Iterator<EmissionState> states) {
/* 121 */     StringBuffer sb = new StringBuffer();
/* 122 */     while (states.hasNext()) {
/* 123 */       EmissionState nxt = (EmissionState)states.next();
/*     */       
/* 125 */       sb.append(nxt.getName());
/* 126 */       if (states.hasNext()) sb.append("_");
/*     */     }
/* 128 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static int getNoCop(List<EmissionState> dist)
/*     */   {
/* 133 */     int noCop = 0;
/* 134 */     for (int i = 0; i < dist.size(); i++) {
/* 135 */       noCop += ((EmissionState)dist.get(i)).noCop().intValue();
/*     */     }
/* 137 */     return noCop;
/*     */   }
/*     */   
/* 140 */   public String getEmissionName() { return this.name1; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SkewNormal[] probR()
/*     */   {
/* 149 */     return this.probR;
/*     */   }
/*     */   
/* 152 */   public IlluminaProbB[] probB() { return this.probB; }
/*     */   
/*     */   public IlluminaProbB probB(int i) {
/* 155 */     return this.probB[i];
/*     */   }
/*     */   
/* 158 */   public Integer noCop() { return this.noCop; }
/*     */   
/*     */ 
/* 161 */   static ProbRManager pm = new ProbRManager();
/*     */   
/*     */ 
/*     */   public PairEmissionState(List<EmissionState> dist, EmissionStateSpace emStSp, boolean decompose, IlluminaProbB[] probB, SkewNormal[] probR2)
/*     */   {
/* 166 */     super(getConcatenation(dist.iterator()), 1);
/* 167 */     this.noCop = Integer.valueOf(getNoCop(dist));
/* 168 */     this.probB = probB;
/* 169 */     if (probR2 != null) {
/* 170 */       this.probR = probR2;
/*     */     }
/*     */     else {
/* 173 */       this.probR = pm.getR(dist);
/*     */     }
/*     */     
/* 176 */     this.emStSp = ((CompoundEmissionStateSpace)emStSp);
/* 177 */     this.decomp = decompose;
/*     */     
/* 179 */     this.noCopies = dist.size();
/* 180 */     this.dist = ((EmissionState[])dist.toArray(new EmissionState[0]));
/* 181 */     SortedSet<EmissionState> distSet = new java.util.TreeSet(dist);
/* 182 */     this.name1 = getConcatenation(distSet.iterator());
/* 183 */     this.indices_tmp = new int[this.noCopies];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EmissionState getInnerState(int j, boolean real)
/*     */   {
/* 191 */     return this.dist[j];
/*     */   }
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
/*     */ 
/*     */   public double score(int key, int i, boolean recursive, boolean decompose)
/*     */   {
/* 231 */     if (decompose) {
/* 232 */       double score = 0.0D;
/* 233 */       int[] l = this.emStSp.getHaps(key);
/*     */       
/* 235 */       for (int k = 0; k < l.length; k++)
/*     */       {
/* 237 */         int li = l[k];
/* 238 */         score += score(li, i, recursive, false);
/*     */       }
/* 240 */       return score;
/*     */     }
/*     */     
/* 243 */     double sc = 1.0D;
/* 244 */     int[] indices = this.emStSp.getMemberIndices(key);
/* 245 */     for (int j = 0; j < indices.length; j++) {
/* 246 */       EmissionState innerSt = getInnerState(j, false);
/* 247 */       if ((recursive) && ((innerSt instanceof CompoundState))) {
/* 248 */         sc *= ((CompoundState)innerSt).score(indices[j], i, recursive, decompose);
/*     */       }
/*     */       else {
/* 251 */         sc *= innerSt.score(indices[j], i);
/*     */       }
/*     */     }
/*     */     
/* 255 */     return sc;
/*     */   }
/*     */   
/*     */   public void addCount(int key, double value, int i, boolean decompose) {
/* 259 */     if (decompose) {
/* 260 */       int[] l = this.emStSp.getHaps(key);
/* 261 */       if (l.length > 1) {
/* 262 */         double[] prob = 
/* 263 */           new double[l.length];
/* 264 */         double sum = 0.0D;
/* 265 */         for (int j = 0; j < prob.length; j++) {
/* 266 */           prob[j] = score(l[j], i, true, false);
/* 267 */           sum += prob[j];
/*     */         }
/* 269 */         if (sum == 0.0D) {
/* 270 */           java.util.Arrays.fill(prob, 1.0D / prob.length);
/* 271 */           sum = 1.0D;
/*     */         }
/* 273 */         for (int j = 0; j < prob.length; j++) {
/* 274 */           double prob_j = value * (prob[j] / sum);
/* 275 */           addCount(l[j], prob_j, i, false);
/*     */         }
/*     */       }
/*     */       else {
/* 279 */         addCount(l[0], value, i, false);
/*     */       }
/*     */     }
/*     */     else {
/* 283 */       int[] indices = this.emStSp.getMemberIndices(key);
/* 284 */       for (int j = 0; j < indices.length; j++)
/* 285 */         getInnerState(j, false).addCount(indices[j], value, i);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addCountDT(double key, int phen_index, double value, int i) {
/* 290 */     for (int k = 0; k < this.dist.length; k++) {
/* 291 */       this.dist[k].addCountDT(key, phen_index, value / this.dist.length, i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public double scoreEmiss(Double[] object_index, int i1)
/*     */   {
/* 298 */     double sc = 1.0D;
/* 299 */     for (int k = 0; k < object_index.length; k++) {
/* 300 */       if (object_index[k] != null) {
/* 301 */         throw new RuntimeException("!! ");
/*     */       }
/*     */     }
/*     */     
/* 305 */     return sc;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addCount(int key1, double value, int i)
/*     */   {
/* 311 */     if (value == 0.0D) { return;
/*     */     }
/* 313 */     addCount(key1, value, i, this.decomp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double score(int key1, int i)
/*     */   {
/* 320 */     double sc = score(key1, i, false, this.decomp);
/*     */     
/* 322 */     return sc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean transferCountsToProbs(double pseudo)
/*     */   {
/* 332 */     throw new RuntimeException("!!! " + getClass());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initialiseCounts()
/*     */   {
/* 339 */     for (int j = 0; j < this.dist.length; j++) {
/* 340 */       this.dist[j].initialiseCounts();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int sample(int i)
/*     */   {
/* 347 */     int[] sample = new int[this.dist.length];
/* 348 */     for (int j = 0; j < this.dist.length; j++) {
/* 349 */       sample[j] = getInnerState(j, false).sample(i);
/*     */     }
/* 351 */     return this.emStSp.getIndex(sample);
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(PrintWriter pw, String prefix, List<Integer> cols)
/*     */   {
/* 357 */     for (int i = 0; i < this.probB.length; i++) {
/* 358 */       this.probB[i].print(pw);
/*     */     }
/* 360 */     for (int i = 0; i < this.dist.length; i++) {
/* 361 */       this.dist[i].print(pw, prefix + " " + this.dist[i].getName(), cols);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void validate()
/*     */     throws Exception
/*     */   {
/* 370 */     for (int k = 0; k < this.dist.length; k++) {
/* 371 */       this.dist[k].validate();
/*     */     }
/* 373 */     this.lengthDistrib.validate();
/* 374 */     for (int i = 0; i < noSnps(); i++) {
/* 375 */       double sum = 0.0D;
/*     */       
/* 377 */       for (int j = 0; j < this.emStSp.size(); j++) {
/* 378 */         sum += score(j, i);
/*     */       }
/* 380 */       if (Math.abs(sum - 1.0D) > 0.1D) {
/* 381 */         throw new RuntimeException("!! " + sum);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public EmissionState[] getMemberStates(boolean real)
/*     */   {
/* 388 */     return this.dist;
/*     */   }
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
/*     */   public EmissionStateSpace getEmissionStateSpace()
/*     */   {
/* 426 */     return this.emStSp;
/*     */   }
/*     */   
/*     */   public int getParamIndex()
/*     */   {
/* 431 */     int max = 0;
/* 432 */     for (int i = 0; i < this.dist.length; i++) {
/* 433 */       int in = this.dist[i].getParamIndex();
/* 434 */       if (in > max) max = in;
/*     */     }
/* 436 */     return max;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/PairEmissionState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */