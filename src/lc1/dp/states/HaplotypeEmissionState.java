/*     */ package lc1.dp.states;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import lc1.dp.data.collection.DataCollection;
/*     */ import lc1.dp.data.representation.ComparableArray;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.emissionspace.CompoundEmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.stats.CompoundDistribution;
/*     */ import lc1.stats.IlluminaDistribution;
/*     */ import lc1.stats.IntegerDistribution;
/*     */ import lc1.stats.ProbabilityDistribution;
/*     */ import lc1.stats.PseudoDistribution;
/*     */ import lc1.stats.SimpleDistribution;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.stats.SimpleExtendedDistribution1;
/*     */ import lc1.stats.SkewNormal;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class HaplotypeEmissionState
/*     */   extends EmissionState
/*     */ {
/*     */   SkewNormal[] probR;
/*  30 */   Integer noCop = null;
/*     */   
/*     */   public Integer noCop()
/*     */   {
/*  34 */     return this.noCop;
/*     */   }
/*     */   
/*     */   public void setProbR(SkewNormal[] probR1) {
/*  38 */     this.probR = probR1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  43 */   public SkewNormal[] probR() { return this.probR; }
/*     */   
/*  45 */   public final boolean train_j = true;
/*     */   final EmissionStateSpace emStSp;
/*     */   
/*  48 */   public void reverse() { List<PseudoDistribution> l = Arrays.asList(this.emissions);
/*  49 */     Collections.reverse(l);
/*  50 */     this.emissions = ((PseudoDistribution[])l.toArray(new PseudoDistribution[0]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PseudoDistribution[] emissions;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void restrictSites(int i)
/*     */   {
/*  69 */     PseudoDistribution[] em1 = new PseudoDistribution[this.emissions.length];
/*  70 */     System.arraycopy(this.emissions, 0, em1, 0, Math.min(i, this.emissions.length));
/*  71 */     this.emissions = em1;
/*     */   }
/*     */   
/*     */ 
/*     */   public void restrictSites(int min, int max)
/*     */   {
/*  77 */     PseudoDistribution[] em1 = new PseudoDistribution[max - min + 1];
/*  78 */     System.arraycopy(this.emissions, min, em1, 0, em1.length);
/*  79 */     this.emissions = em1;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getUnderlyingData(int i)
/*     */   {
/*  85 */     return this.emissions[i].getUnderlyingDataAll(this.emStSp);
/*     */   }
/*     */   
/*     */   public void setTheta(double[] prob, int i) {
/*  89 */     this.emissions[i].setProb(prob);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setTheta(int val, int i2)
/*     */   {
/*  95 */     this.emissions[i2] = new IntegerDistribution(val);
/*     */   }
/*     */   
/*     */   public boolean transferCountsToProbs(double pseudoC)
/*     */   {
/* 100 */     if (pseudoC > 1000.0D) { return super.transferCountsToProbs(pseudoC);
/*     */     }
/* 102 */     this.paramIndex += 1;
/* 103 */     for (int i = 0; i < this.emissions.length; i++) {
/* 104 */       PseudoDistribution em = this.emissions[i];
/* 105 */       if ((em != null) && 
/* 106 */         ((em instanceof SimpleExtendedDistribution))) {
/* 107 */         em.transfer(pseudoC);
/* 108 */         PseudoDistribution fixed = ((SimpleExtendedDistribution)em).makeFixed();
/* 109 */         if (fixed != null) {
/* 110 */           this.emissions[i] = fixed;
/*     */         }
/*     */       }
/*     */     }
/* 114 */     if (this.emissionsDatatype != null) {
/* 115 */       for (int k = 0; k < this.emissionsDatatype.length; k++) {
/* 116 */         for (int i = 0; i < this.emissions.length; i++) {
/* 117 */           ProbabilityDistribution em = this.emissionsDatatype[k][i];
/* 118 */           if (em != null) {
/* 119 */             em.transfer(pseudoC);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 124 */     return true;
/*     */   }
/*     */   
/*     */   public void switchAlleles(int i) {
/* 128 */     this.emissions[i].swtchAlleles(getEmissionStateSpace().getSwitchTranslation());
/*     */   }
/*     */   
/*     */ 
/*     */   public HaplotypeEmissionState(String name, int noSnps, EmissionStateSpace emStSp, short data_index)
/*     */   {
/* 134 */     super(name, 1);
/* 135 */     this.data_index = data_index;
/* 136 */     this.distribution = new double[emStSp.defaultList.size()];
/* 137 */     this.noCop = null;
/* 138 */     this.emStSp = emStSp;
/* 139 */     this.noSnps = noSnps;
/* 140 */     this.emissions = new PseudoDistribution[noSnps];
/* 141 */     this.emissionsDatatype = null;
/*     */   }
/*     */   
/*     */   public HaplotypeEmissionState(String name, int noSnps, int len, EmissionStateSpace emStSp, Integer noCop, double[][] meanvarskew, double r_prior) {
/* 145 */     this(name, noSnps, len, emStSp, noCop, meanvarskew, (short)-1, r_prior);
/*     */   }
/*     */   
/*     */   public HaplotypeEmissionState(String name, int noSnps, int len, EmissionStateSpace emStSp, Integer noCop, double[][] meanvarskew, short data_index, double r_prior)
/*     */   {
/* 150 */     this(name, noSnps, emStSp, data_index);
/*     */     
/* 152 */     this.probR = (noCop == null ? null : getProbRGroup(noCop.intValue(), 1.0D, meanvarskew, r_prior));
/*     */     
/* 154 */     for (int i = 0; i < noSnps; i++) {
/* 155 */       this.emissions[i] = new SimpleExtendedDistribution(len);
/* 156 */       this.emissions[i].setDataIndex(data_index);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPhenotype(Double[] phen)
/*     */   {
/* 167 */     this.phenValue = phen;
/*     */   }
/*     */   
/*     */   public HaplotypeEmissionState(String name, int noSnps, double u, double[] init, EmissionStateSpace emStSp, Integer noCop, double[][] meanvarskew, double prior_mod, ProbabilityDistribution[] numLevels) {
/* 171 */     super(name, 1);
/* 172 */     this.data_index = -1;
/* 173 */     int index = Constants.getMax(init);
/* 174 */     boolean fixed = init[index] > 0.9999D;
/* 175 */     this.noCop = noCop;
/* 176 */     this.probR = getProbRGroup(noCop.intValue(), 1.0D, meanvarskew, prior_mod);
/*     */     
/*     */ 
/* 179 */     this.emStSp = emStSp;
/* 180 */     this.noSnps = noSnps;
/* 181 */     this.emissions = new PseudoDistribution[noSnps];
/* 182 */     this.emissionsDatatype = new ProbabilityDistribution[numLevels.length][noSnps];
/* 183 */     for (int i = 0; i < noSnps; i++) {
/* 184 */       this.emissions[i] = 
/* 185 */         (fixed ? new IntegerDistribution(index) : 
/* 186 */         new SimpleExtendedDistribution1(init, u));
/* 187 */       for (int k = 0; k < this.emissionsDatatype.length; k++) {
/* 188 */         this.emissionsDatatype[k][i] = numLevels[k].clone(100.0D);
/*     */         
/* 190 */         this.emissionsDatatype[k][i].initialise();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ProbabilityDistribution[][] emissionsDatatype;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int noSnps;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   short data_index;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(PrintWriter pw, String prefix)
/*     */   {
/* 228 */     StringBuffer sb1 = new StringBuffer(prefix);
/* 229 */     StringBuffer sb2 = new StringBuffer(prefix);
/* 230 */     for (int i = 0; i < this.noSnps; i++) {
/* 231 */       sb1.append("%8.2g ");
/*     */     }
/* 233 */     Double[] em = new Double[this.noSnps];
/* 234 */     for (int i = 0; i < getEmissionStateSpace().size(); i++) {
/* 235 */       for (int j = 0; j < this.noSnps; j++) {
/* 236 */         em[j] = Double.valueOf(this.emissions[j] == null ? NaN.0D : this.emissions[j].probs(i));
/*     */       }
/* 238 */       pw.println(Format.sprintf(sb1.toString(), em));
/*     */     }
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, String prefix, List<Integer> columns)
/*     */   {
/* 244 */     for (int i = 0; i < this.probR.length; i++) {
/* 245 */       pw.println("prob R_" + i + this.probR[i].toString());
/*     */     }
/* 247 */     StringBuffer sb1 = new StringBuffer(prefix);
/* 248 */     StringBuffer sb2 = new StringBuffer(prefix);
/* 249 */     Object[] em = emissions(this, columns);
/* 250 */     for (int i = 0; i < ((Double[])em[0]).length; i++) {
/* 251 */       sb1.append("%8.2g ");
/* 252 */       sb2.append("%8s ");
/*     */     }
/* 254 */     pw.println(Format.sprintf(sb2.toString(), (String[])em[1]));
/* 255 */     pw.println(Format.sprintf(sb1.toString(), (Double[])em[0]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HaplotypeEmissionState(HaplotypeEmissionState st_to_init, String name)
/*     */   {
/* 262 */     super(name, 1);
/* 263 */     this.data_index = -1;
/* 264 */     this.probR = st_to_init.probR;
/* 265 */     this.noCop = st_to_init.noCop;
/*     */     
/* 267 */     this.emStSp = st_to_init.emStSp;
/* 268 */     if (st_to_init.phenValue != null) {
/* 269 */       this.phenValue = new Double[st_to_init.phenValue.length];
/* 270 */       System.arraycopy(st_to_init.phenValue, 0, this.phenValue, 0, this.phenValue.length);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 275 */     this.noSnps = st_to_init.noSnps;
/* 276 */     this.emissions = new PseudoDistribution[this.noSnps];
/* 277 */     this.emissionsDatatype = new ProbabilityDistribution[st_to_init.emissionsDatatype.length][this.noSnps];
/* 278 */     for (int i = 0; i < this.noSnps; i++) {
/* 279 */       this.emissions[i] = (st_to_init.emissions[i] == null ? null : st_to_init.emissions[i].clone());
/*     */       
/* 281 */       for (int k = 0; k < this.emissionsDatatype.length; k++) {
/* 282 */         this.emissionsDatatype[k][i] = st_to_init.emissionsDatatype[k][i].clone();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public HaplotypeEmissionState(EmissionState state_j) {
/* 288 */     super(state_j.getName(), 1);
/* 289 */     this.data_index = ((HaplotypeEmissionState)state_j).data_index;
/* 290 */     this.noCop = state_j.noCop();
/* 291 */     this.probR = state_j.probR();
/* 292 */     this.emStSp = state_j.getEmissionStateSpace();
/* 293 */     this.noSnps = state_j.noSnps();
/* 294 */     this.emissions = new PseudoDistribution[this.noSnps];
/* 295 */     if (((HaplotypeEmissionState)state_j).emissionsDatatype == null) {
/* 296 */       this.emissionsDatatype = null;
/*     */     }
/*     */     else {
/* 299 */       this.emissionsDatatype = 
/* 300 */         new ProbabilityDistribution[((HaplotypeEmissionState)state_j).emissionsDatatype.length][this.noSnps];
/* 301 */       for (int k = 0; k < this.emissionsDatatype.length; k++) {
/* 302 */         for (int i = 0; i < this.noSnps; i++) {
/* 303 */           this.emissionsDatatype[k][i] = ((HaplotypeEmissionState)state_j).emissionsDatatype[k][i].clone();
/*     */         }
/*     */       }
/*     */     }
/* 307 */     if (((HaplotypeEmissionState)state_j).phenValue != null) {
/* 308 */       this.phenValue = new Double[((HaplotypeEmissionState)state_j).phenValue.length];
/* 309 */       System.arraycopy(((HaplotypeEmissionState)state_j).phenValue, 0, this.phenValue, 0, this.phenValue.length);
/*     */     }
/* 311 */     for (int i = 0; i < this.noSnps; i++) {
/* 312 */       Integer fixed_i = state_j.getFixedInteger(i);
/* 313 */       if (fixed_i != null) {
/* 314 */         this.emissions[i] = new IntegerDistribution(fixed_i.intValue());
/*     */       }
/*     */       else {
/* 317 */         double[] probs = new double[this.emStSp.size()];
/* 318 */         for (int j = 0; j < probs.length; j++) {
/* 319 */           double sc = state_j.score(j, i);
/* 320 */           probs[j] = sc;
/*     */         }
/* 322 */         this.emissions[i] = new SimpleExtendedDistribution(probs, Constants.switchU());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object clone(State pseudo)
/*     */   {
/* 331 */     return new HaplotypeEmissionState(this, getName());
/*     */   }
/*     */   
/*     */   public Object clone() {
/* 335 */     return new HaplotypeEmissionState(this, getName());
/*     */   }
/*     */   
/*     */ 
/*     */   public void addCount(int obj_index, int data_index, double value, int i)
/*     */   {
/* 341 */     this.emissions[i].addCount(obj_index, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addCount(int obj_index, double value, int i)
/*     */   {
/* 348 */     this.emissions[i].addCount(obj_index, value);
/*     */   }
/*     */   
/*     */   public void addCountDT(double phen, int phen_index, double value, int i) {
/* 352 */     if (this.emissionsDatatype != null)
/*     */     {
/* 354 */       this.emissionsDatatype[phen_index][i].addCount(phen, value);
/*     */     }
/*     */   }
/*     */   
/* 358 */   Double[] phenValue = new Double[0];
/*     */   
/*     */   public Double[] phenValue() {
/* 361 */     return this.phenValue;
/*     */   }
/*     */   
/*     */ 
/* 365 */   public int dataIndex() { return this.data_index; }
/*     */   
/*     */   public void initialiseCounts() {
/* 368 */     for (int i = 0; i < this.noSnps; i++) {
/* 369 */       if (this.emissions[i] != null) this.emissions[i].initialise();
/*     */     }
/* 371 */     if (this.emissionsDatatype != null) {
/* 372 */       for (int k = 0; k < this.emissionsDatatype.length; k++) {
/* 373 */         for (int i = 0; i < this.emissions.length; i++) {
/* 374 */           this.emissionsDatatype[k][i].initialise();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public double KLDistance(EmissionState st) {
/* 381 */     double sum = 0.0D;
/* 382 */     HaplotypeEmissionState hes = (HaplotypeEmissionState)st;
/* 383 */     for (int i = 0; i < this.emissions.length; i++) {
/* 384 */       if (this.emissions[i].probs()[0] != 0.0D)
/* 385 */         sum += this.emissions[i].probs()[0] * Math.log(this.emissions[i].probs()[0] / hes.emissions[i].probs()[0]);
/*     */     }
/* 387 */     return sum / this.emissions.length;
/*     */   }
/*     */   
/*     */   public int sample(int i) {
/* 391 */     return (int)this.emissions[i].sample();
/*     */   }
/*     */   
/*     */   public double score(int obj_i, int i1)
/*     */   {
/* 396 */     return this.emissions[i1].probs(obj_i);
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
/*     */   public static Object[] emissions(EmissionState d, List<Integer> columns)
/*     */   {
/* 409 */     int len = d.noSnps();
/* 410 */     EmissionStateSpace emStSp = d.getEmissionStateSpace();
/* 411 */     Double[] res = new Double[columns == null ? len : columns.size()];
/* 412 */     String[] res1 = new String[columns == null ? len : columns.size()];
/* 413 */     for (int i = 0; i < res.length; i++) {
/* 414 */       int pos = columns == null ? i : ((Integer)columns.get(i)).intValue();
/* 415 */       if (len == 1) pos = 0;
/* 416 */       int k = d.getBestIndex(pos);
/* 417 */       res[i] = Double.valueOf(d.score(k, pos));
/* 418 */       if (res[i].doubleValue() < 0.01D) { res[i] = Double.valueOf(0.0D);
/* 419 */       } else if (res[i].doubleValue() > 0.99D) { res[i] = Double.valueOf(1.0D);
/*     */       }
/* 421 */       Comparable compa = emStSp.get(k);
/* 422 */       res1[i] = ((compa instanceof Emiss) ? ((Emiss)compa).toStringShort() : ((ComparableArray)compa).toStringShort());
/*     */     }
/*     */     
/* 425 */     return new Object[] { res, res1 };
/*     */   }
/*     */   
/* 428 */   public static Object[] emissions(SimpleExtendedDistribution[] d, List<Integer> columns) { int len = d.length;
/*     */     
/* 430 */     Double[] res = new Double[columns == null ? len : columns.size()];
/* 431 */     String[] res1 = new String[columns == null ? len : columns.size()];
/* 432 */     for (int i = 0; i < res.length; i++) {
/* 433 */       int pos = columns == null ? i : ((Integer)columns.get(i)).intValue();
/* 434 */       if (len == 1) pos = 0;
/* 435 */       double[] probs = d[i].probs;
/* 436 */       int k = Constants.getMax(probs);
/* 437 */       res[i] = Double.valueOf(probs[k]);
/* 438 */       if (res[i].doubleValue() < 0.01D) { res[i] = Double.valueOf(0.0D);
/* 439 */       } else if (res[i].doubleValue() > 0.99D) { res[i] = Double.valueOf(1.0D);
/*     */       }
/* 441 */       res1[i] = k;
/*     */     }
/*     */     
/* 444 */     return new Object[] { res, res1 };
/*     */   }
/*     */   
/*     */   public String toString(int i)
/*     */   {
/* 449 */     return getName();
/*     */   }
/*     */   
/*     */   public void validate()
/*     */     throws Exception
/*     */   {
/* 455 */     this.lengthDistrib.validate();
/* 456 */     for (int i = 0; i < this.noSnps; i++) {
/* 457 */       double sum = this.emissions[i].sum();
/* 458 */       if (Math.abs(1.0D - sum) > 0.001D) {
/* 459 */         throw new RuntimeException("invalid! " + this.emissions[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int mostLikely(int pos)
/*     */   {
/* 466 */     return this.emissions[pos].getMax();
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
/*     */   public void print(PrintWriter pw, List<Integer> cols)
/*     */   {
/* 501 */     print(pw, "", cols);
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
/*     */   public int noSnps()
/*     */   {
/* 517 */     return this.emissions.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EmissionStateSpace getEmissionStateSpace()
/*     */   {
/* 525 */     return this.emStSp;
/*     */   }
/*     */   
/*     */ 
/*     */   public Integer getFixedInteger(int i)
/*     */   {
/* 531 */     return this.emissions[i].fixedInteger();
/*     */   }
/*     */   
/*     */   public void setFixedIndex(int i, int k) {
/* 535 */     this.emissions[i].setFixedIndex(k);
/*     */   }
/*     */   
/*     */   public boolean hasIlluminaDist()
/*     */   {
/* 540 */     for (int i = 0; i < this.emissions.length; i++) {
/* 541 */       if ((this.emissions[i] instanceof IlluminaDistribution)) return true;
/*     */     }
/* 543 */     return false;
/*     */   }
/*     */   
/* 546 */   int paramIndex = 1;
/*     */   
/*     */   public int getParamIndex()
/*     */   {
/* 550 */     int res = this.paramIndex;
/* 551 */     if (this.probR != null) {
/* 552 */       for (int i = 0; i < this.probR.length; i++) {
/* 553 */         res += this.probR[i].getParamIndex();
/*     */       }
/*     */     }
/* 556 */     return res;
/*     */   }
/*     */   
/*     */   public void removeAll(List<Integer> toDrop) {
/* 560 */     PseudoDistribution[] newDist = new PseudoDistribution[length() - toDrop.size()];
/* 561 */     int k = 0;
/* 562 */     for (int i = 0; i < length(); i++) {
/* 563 */       if (!toDrop.contains(Integer.valueOf(i)))
/*     */       {
/* 565 */         newDist[k] = this.emissions[i];
/* 566 */         k++;
/*     */       }
/*     */     }
/* 569 */     this.emissions = newDist;
/* 570 */     this.noSnps = this.emissions.length;
/*     */   }
/*     */   
/*     */   public void fixLikelihood(boolean X, int i) {
/* 574 */     double[] em = ((SimpleExtendedDistribution)this.emissions[i]).probs;
/* 575 */     double max = em[Constants.getMax(em)];
/* 576 */     double[] probs = this.emissions[i].probs();
/* 577 */     for (int k = 0; k < probs.length; k++)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 582 */       if (this.emStSp.getCN(k) != ((CompoundEmissionStateSpace)getEmissionStateSpace()).noCopies()) { probs[k] = 0.0D;
/*     */       }
/*     */     }
/* 585 */     SimpleExtendedDistribution.normalise(probs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAsMissing(List<Integer> toDrop, double cn_ratio)
/*     */   {
/* 594 */     EmissionStateSpace subSp = getEmissionStateSpace();
/* 595 */     List<String> genoList = new ArrayList();
/* 596 */     for (int k = 0; k < subSp.getGenotypeList().size(); k++) {
/* 597 */       char[] ch = ((ComparableArray)subSp.getGenotype(k)).toStringShort().replaceAll("_", "").toCharArray();
/* 598 */       Arrays.sort(ch);
/* 599 */       genoList.add(new String(ch));
/*     */     }
/* 601 */     for (int i1 = 0; i1 < toDrop.size(); i1++) {
/* 602 */       int i = ((Integer)toDrop.get(i1)).intValue();
/* 603 */       if ((this.emissions[i] instanceof IntegerDistribution)) {
/* 604 */         this.emissions[i] = new SimpleExtendedDistribution(subSp.defaultList.size());
/*     */       }
/* 606 */       double[] prob = ((SimpleExtendedDistribution)this.emissions[i]).probs;
/* 607 */       Arrays.fill(prob, 0.0D);
/* 608 */       for (int k = 0; k < genoList.size(); k++) {
/* 609 */         String compa = (String)genoList.get(k);
/*     */         
/* 611 */         int genoIndex = this.emStSp.getFromString(compa).intValue();
/* 612 */         int cn = compa.length();
/* 613 */         int[] indices = this.emStSp.getGenotypeConsistent(genoIndex);
/* 614 */         double[] weights = this.emStSp.getWeights(genoIndex);
/* 615 */         double val = cn == 2 ? cn_ratio : 1.0D;
/*     */         
/* 617 */         for (int k1 = 0; k1 < indices.length; k1++) {
/* 618 */           prob[indices[k1]] = (weights[k1] * val);
/*     */         }
/*     */       }
/* 621 */       SimpleExtendedDistribution.normalise(prob);
/* 622 */       setTheta(prob, i);
/*     */     }
/*     */   }
/*     */   
/*     */   public void applyAlias(int[] alias) {
/* 627 */     DataCollection.reorder(alias, this.emissions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double calcDistribution(EmissionState hmm_state, int i)
/*     */   {
/* 634 */     double sum = 0.0D;
/* 635 */     if (this.distribution == null) {
/* 636 */       this.distribution = new double[getEmissionStateSpace().size()];
/*     */     }
/* 638 */     Arrays.fill(this.distribution, 0.0D);
/* 639 */     IlluminaProbB[] probBState = hmm_state.probB();
/*     */     
/*     */ 
/* 642 */     for (int j = 0; j < this.distribution.length; j++) {
/* 643 */       if (this.emStSp.getCN(j) == hmm_state.noCop().intValue()) {
/* 644 */         double prob_j = hmm_state.score(j, i);
/* 645 */         if (prob_j > 0.0D)
/*     */         {
/* 647 */           this.distribution[j] = 
/*     */           
/*     */ 
/* 650 */             (prob_j * this.emissions[i].score(j, probBState) * this.emStSp.getWeight(j));
/* 651 */           sum += this.distribution[j];
/*     */         }
/*     */       }
/*     */     }
/* 655 */     return sum;
/*     */   }
/*     */   
/*     */   public double calcDistribution(EmissionState hmm_state, int i, double[] distribution, double p) {
/* 659 */     double sum = 0.0D;
/* 660 */     IlluminaProbB[] probBState = hmm_state.probB();
/* 661 */     SkewNormal[] probRState = hmm_state.probR();
/* 662 */     for (int j = 0; j < distribution.length; j++) {
/* 663 */       if (this.emStSp.getCN(j) == hmm_state.noCop().intValue()) {
/* 664 */         double prob_j = hmm_state.score(j, i) * p;
/* 665 */         if (prob_j > 0.0D) {
/* 666 */           double v = 
/* 667 */             prob_j * 
/* 668 */             this.emissions[i].score(j, probBState) * 
/* 669 */             this.emissions[i].score(probRState) * 
/* 670 */             this.emStSp.getWeight(j);
/* 671 */           distribution[j] += v;
/* 672 */           sum += v;
/*     */         }
/*     */       }
/*     */     }
/* 676 */     return sum;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addCount(EmissionState hmm_state, Double double1, int i, double weight)
/*     */   {
/* 683 */     this.emissions[i].addCount(hmm_state.probR(), double1.doubleValue() * weight);
/* 684 */     Double[] phenv = this.phenValue;
/* 685 */     Integer index = hmm_state.getFixedInteger(i);
/* 686 */     hmm_state.addCountSynchDT(phenv, weight * double1.doubleValue(), i);
/* 687 */     if (index != null) {
/* 688 */       this.emissions[i].addCount(hmm_state.probB(), index, double1.doubleValue());
/* 689 */       hmm_state.addCountSynch(index.intValue(), weight * double1.doubleValue(), i);
/*     */     }
/*     */     else {
/* 692 */       double sum = calcDistribution(hmm_state, i);
/* 693 */       for (int j = 0; j < this.distribution.length; j++) {
/* 694 */         double prob_j = this.distribution[j] / sum;
/* 695 */         if (prob_j > 0.0D) {
/* 696 */           double val = prob_j * double1.doubleValue();
/* 697 */           hmm_state.addCountSynch(j, val * weight, i);
/* 698 */           IlluminaProbB[] pb = hmm_state.probB();
/* 699 */           this.emissions[i].addCount(pb, Integer.valueOf(j), val);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int getBestIndex(EmissionState hmm_state, int i, boolean sample)
/*     */   {
/* 707 */     double sum = calcDistribution(hmm_state, i);
/*     */     
/*     */ 
/* 710 */     int res = sample ? Constants.sample(this.distribution, sum) : 
/* 711 */       Constants.getMax(this.distribution);
/*     */     
/*     */ 
/*     */ 
/* 715 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */   public double scoreEmiss(Double[] object_index, int i1)
/*     */   {
/* 721 */     double sc = 1.0D;
/* 722 */     for (int k = 0; k < object_index.length; k++) {
/* 723 */       if (object_index[k] != null) {
/* 724 */         sc *= this.emissionsDatatype[k][i1].probability(object_index[k].doubleValue());
/*     */       }
/*     */     }
/* 727 */     return sc;
/*     */   }
/*     */   
/*     */   public double score(EmissionState hmm_state, int i_hmm, int stSpSize)
/*     */   {
/* 732 */     double sc = calcDistribution(hmm_state, i_hmm) * this.emissions[i_hmm].score(hmm_state.probR());
/*     */     
/* 734 */     if (Constants.scoreDT()) {
/* 735 */       sc *= hmm_state.scoreEmiss(this.phenValue, i_hmm);
/*     */     }
/* 737 */     return sc;
/*     */   }
/*     */   
/*     */   public double[] getEmiss(int i) {
/* 741 */     return this.emissions[i].probs();
/*     */   }
/*     */   
/*     */   public List<Short> getDataIndices() {
/* 745 */     List<Short> res = new ArrayList();
/* 746 */     for (int i = 0; i < this.emissions.length; i++) {
/* 747 */       res.add(Short.valueOf(this.emissions[i].getDataIndex()));
/*     */     }
/* 749 */     return res;
/*     */   }
/*     */   
/* 752 */   public void applyCorrection(double parseDouble) { for (int i = 0; i < this.emissions.length; i++) {
/* 753 */       this.emissions[i].applyCorrection(parseDouble);
/*     */     }
/*     */   }
/*     */   
/*     */   public void applyLoess(Double[] loess) {
/* 758 */     for (int i = 0; i < this.emissions.length; i++) {
/* 759 */       this.emissions[i].applyCorrection(-1.0D * loess[i].doubleValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateEmissions(int i, PseudoDistribution pseudoDistribution) {
/* 764 */     if (this.emissions[i] == null)
/*     */     {
/* 766 */       this.emissions[i] = pseudoDistribution;
/*     */ 
/*     */     }
/* 769 */     else if ((this.emissions[i] instanceof CompoundDistribution)) {
/* 770 */       ((CompoundDistribution)this.emissions[i]).addDist(pseudoDistribution);
/*     */     }
/* 772 */     else if (!(this.emissions[i] instanceof IntegerDistribution))
/*     */     {
/*     */ 
/* 775 */       if (!(this.emissions[i] instanceof IntegerDistribution)) {
/* 776 */         this.emissions[i] = new CompoundDistribution(this.emissions[i], pseudoDistribution);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDataIndex(int dat_ind) {
/* 782 */     if (this.data_index < 0) this.data_index = ((short)dat_ind);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/HaplotypeEmissionState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */