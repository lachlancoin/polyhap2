/*     */ package lc1.dp.genotype.io;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import java.util.SortedSet;
/*     */ import lc1.dp.CompoundMarkovModel;
/*     */ import lc1.dp.CompoundState;
/*     */ import lc1.dp.DP;
/*     */ import lc1.dp.EmissionState;
/*     */ import lc1.dp.MarkovModel;
/*     */ import lc1.dp.PairEmissionState;
/*     */ import lc1.dp.StatePath;
/*     */ import lc1.dp.genotype.StateDistribution;
/*     */ import lc1.dp.genotype.io.scorable.CompoundScorableObject;
/*     */ import lc1.dp.genotype.io.scorable.DataCollection;
/*     */ import lc1.dp.genotype.io.scorable.PhasedIntegerGenotypeData;
/*     */ import lc1.dp.genotype.io.scorable.ScorableObject;
/*     */ import lc1.dp.genotype.io.scorable.StateIndices;
/*     */ import lc1.dp.genotype.io.scorable.StateScorableObject;
/*     */ import lc1.dp.genotype.io.scorable.StateScorableObjectDistribution;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Sampler
/*     */ {
/*     */   public DataCollection data;
/*     */   final String sb_i1;
/*     */   final double[][] uncertaintyL;
/*     */   PrintWriter[] pw;
/*     */   File[] files;
/*     */   List<CompoundScorableObject>[] spLists;
/*  47 */   boolean buffer = true;
/*     */   
/*     */   public Sampler(DataCollection data) {
/*  50 */     if (this.buffer) {
/*  51 */       this.spLists = new List[data.size()];
/*  52 */       for (int i = 0; i < data.size(); i++) {
/*  53 */         this.spLists[i] = new ArrayList();
/*     */       }
/*     */     }
/*     */     else {
/*  57 */       this.files = new File[data.size()];
/*  58 */       this.pw = new PrintWriter[data.size()];
/*  59 */       for (int i = 0; i < this.files.length; i++) {
/*  60 */         this.files[i] = new File("tmp_" + i);
/*     */         try {
/*  62 */           this.pw[i] = new PrintWriter(new BufferedWriter(new FileWriter(this.files[i], false)));
/*     */         } catch (Exception exc) {
/*  64 */           exc.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*  68 */     this.uncertaintyL = new double[data.size()][data.get(0).length()];
/*  69 */     this.data = data;
/*  70 */     StringBuffer sb_is1 = new StringBuffer();
/*  71 */     for (int i = 0; i < data.get(0).length(); i++) {
/*  72 */       sb_is1.append("%1i");
/*     */     }
/*  74 */     this.sb_i1 = sb_is1.toString(); }
/*     */   
/*  76 */   static Comparator entryComp = new Comparator()
/*     */   {
/*     */     public int compare(Map.Entry<List<Comparable>, Integer> o1, Map.Entry<List<Comparable>, Integer> o2) {
/*  79 */       return ((Integer)o1.getValue()).compareTo((Integer)o2.getValue());
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  86 */   public String getPrintString() { return this.sb_i1; }
/*     */   
/*     */   public Double[] uncertainty() {
/*  89 */     Double[] res = new Double[this.uncertaintyL[0].length];
/*  90 */     for (int i = 0; i < res.length; i++) {
/*  91 */       double res_i = 0.0D;
/*  92 */       for (int j = 0; j < this.uncertaintyL.length; j++) {
/*  93 */         res_i += this.uncertaintyL[j][i];
/*     */       }
/*  95 */       res[i] = Double.valueOf(res_i / this.uncertaintyL.length);
/*     */     }
/*  97 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CompoundScorableObject sample(StatePath sp, CompoundMarkovModel hmm, CompoundScorableObject orig)
/*     */   {
/* 108 */     CompoundScorableObject result_phased = (CompoundScorableObject)orig.clone();
/* 109 */     EmissionState[] prev = (EmissionState[])null;
/*     */     
/* 111 */     for (int i = 0; i < sp.size(); i++) {
/* 112 */       CompoundState state_i = (CompoundState)sp.getState(i);
/*     */       
/*     */ 
/* 115 */       EmissionState[] states_i = state_i.getMemberStates();
/* 116 */       ComparableArray emiss = (ComparableArray)sp.getEmission(i);
/* 117 */       ComparableArray selected = emiss;
/* 118 */       List<ComparableArray> possibilities = PairEmissionState.decompose(selected, orig.noCopies());
/*     */       
/* 120 */       if (possibilities.size() > 1) {
/* 121 */         double[] prob = new double[possibilities.size()];
/* 122 */         double sum = 0.0D;
/*     */         
/* 124 */         for (int k = 0; k < prob.length; k++) {
/* 125 */           Iterator<Comparable> poss_k = ((ComparableArray)possibilities.get(k)).iterator();
/* 126 */           prob[k] = states_i[0].score(hmm.getMarkovModel(0).getEmissionStateSpaceIndex(poss_k.next()), i);
/* 127 */           for (int k1 = 1; poss_k.hasNext(); k1++) {
/* 128 */             prob[k] *= states_i[k1].score(hmm.getMarkovModel(k1).getEmissionStateSpaceIndex(poss_k.next()), i);
/*     */           }
/* 130 */           sum += prob[k];
/*     */         }
/* 132 */         selected = (ComparableArray)possibilities.get(select(prob, sum));
/*     */       }
/*     */       else {
/* 135 */         selected = (ComparableArray)possibilities.get(0);
/*     */       }
/* 137 */       result_phased.set(i, selected);
/* 138 */       prev = states_i;
/*     */     }
/* 140 */     return result_phased;
/*     */   }
/*     */   
/*     */   public static int select(double[] prob, double sum) {
/* 144 */     double randn = Constants.rand.nextDouble() * sum;
/* 145 */     double cum = 0.0D;
/* 146 */     for (int k = 0; k < prob.length; k++) {
/* 147 */       cum += prob[k];
/* 148 */       if (cum >= randn) return k;
/*     */     }
/* 150 */     return prob.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sampleFromHMM(CompoundMarkovModel[] models)
/*     */   {
/* 158 */     int index = 0;
/*     */     try
/*     */     {
/* 161 */       for (Iterator<PhasedIntegerGenotypeData> it = this.data.iterator(); it.hasNext(); index++) {
/* 162 */         PhasedIntegerGenotypeData dat = (PhasedIntegerGenotypeData)it.next();
/*     */         
/* 164 */         CompoundMarkovModel hmm = models[(dat.noCopies() - 1)];
/*     */         
/*     */ 
/*     */ 
/* 168 */         StateIndices dat1 = StateIndices.get(dat, models[(dat.noCopies() - 1)]);
/* 169 */         DP dp = new DP(hmm, "", dat1, false);
/* 170 */         dp.reset();
/* 171 */         dp.fillComplexTrace();
/* 172 */         for (int k = 0; k < Constants.noSamples(); k++)
/*     */         {
/* 174 */           StatePath sp = dp.getStatePath();
/*     */           
/* 176 */           CompoundScorableObject obj = sample(sp, hmm, dat);
/* 177 */           if (this.buffer) {
/* 178 */             this.spLists[index].add(obj);
/*     */           }
/*     */           else {
/* 181 */             obj.print(this.pw[index], false);
/*     */           }
/*     */         }
/*     */         
/* 185 */         if (!this.buffer) this.pw[index].flush();
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/* 189 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void finalise()
/*     */   {
/* 195 */     if (!this.buffer) {
/* 196 */       for (int i = 0; i < this.files.length; i++) {
/* 197 */         this.files[i].delete();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void calcBestPathSampling()
/*     */     throws Exception
/*     */   {
/* 208 */     int index = 0;
/* 209 */     if (!this.buffer) {
/* 210 */       for (int i = 0; i < this.pw.length; i++) {
/* 211 */         this.pw[i].close();
/*     */       }
/*     */     }
/*     */     
/* 215 */     for (Iterator<PhasedIntegerGenotypeData> it = this.data.iterator(); it.hasNext(); index++) {
/* 216 */       Arrays.fill(this.uncertaintyL[index], 1.0D);
/*     */       try
/*     */       {
/* 219 */         PhasedIntegerGenotypeData dat = (PhasedIntegerGenotypeData)it.next();
/*     */         
/* 221 */         int datSize = dat.noCopies();
/* 222 */         Arrays.fill(this.uncertaintyL[index], 1.0D);
/*     */         List<CompoundScorableObject> spList;
/* 224 */         List<CompoundScorableObject> spList; if (this.buffer) {
/* 225 */           spList = this.spLists[index];
/*     */         }
/*     */         else {
/* 228 */           spList = new ArrayList();
/* 229 */           BufferedReader br = new BufferedReader(new FileReader(this.files[index]));
/* 230 */           String[] st = new String[datSize];
/* 231 */           while ((st[0] = br.readLine()) != null) {
/* 232 */             for (int i = 1; i < datSize; i++) {
/* 233 */               st[i] = br.readLine();
/*     */             }
/* 235 */             spList.add(new PhasedIntegerGenotypeData(dat.getName(), st));
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 240 */         dat.sampleGenotype(spList, this.uncertaintyL[index]);
/*     */         
/*     */ 
/* 243 */         dat.samplePhase(spList);
/*     */       }
/*     */       catch (Exception exc) {
/* 246 */         exc.printStackTrace();
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
/*     */   int validate(double[] d)
/*     */   {
/* 285 */     double sum = 0.0D;
/* 286 */     int maxIndex = 0;
/* 287 */     for (int i = 0; i < d.length; i++) {
/* 288 */       sum += d[i];
/* 289 */       if (d[i] > d[maxIndex]) { maxIndex = i;
/*     */       }
/*     */     }
/* 292 */     return maxIndex;
/*     */   }
/*     */   
/* 295 */   private double sum(SortedSet<Map.Entry<List<Comparable>, Integer>> set) { double sum = 0.0D;
/* 296 */     for (Iterator<Map.Entry<List<Comparable>, Integer>> it = set.iterator(); it.hasNext();) {
/* 297 */       sum += ((Integer)((Map.Entry)it.next()).getValue()).intValue();
/*     */     }
/* 299 */     return sum;
/*     */   }
/*     */   
/*     */   private static String toString(List<ComparableArray> poss)
/*     */   {
/* 304 */     StringBuffer sb = new StringBuffer();
/* 305 */     sb.append("{");
/* 306 */     for (int i = 0; i < poss.size(); i++) {
/* 307 */       sb.append(Arrays.asList(new List[] { poss }).toString());
/*     */     }
/* 309 */     sb.append("}");
/* 310 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<Integer[][]> calcBestPathViterbi(PrintWriter pw, CompoundMarkovModel hmm)
/*     */     throws Exception
/*     */   {
/* 319 */     ArrayList<Integer[][]> num = new ArrayList();
/*     */     
/* 321 */     MarkovModel hapHMM = hmm.getMarkovModel(0);
/* 322 */     Integer[][] num1 = new Integer[hapHMM.modelLength()][this.data.get(0).length()];
/* 323 */     for (int i = 0; i < num1.length; i++) {
/* 324 */       Arrays.fill(num1[i], Integer.valueOf(0));
/*     */     }
/* 326 */     for (int ij = 0; ij < this.data.size(); ij++) {
/*     */       try {
/* 328 */         PhasedIntegerGenotypeData dat = (PhasedIntegerGenotypeData)this.data.get(ij);
/* 329 */         StateIndices dat1 = new StateScorableObjectDistribution(dat, hmm);
/* 330 */         DP dp = new DP(hmm, "", dat1, true);
/* 331 */         dp.reset();
/* 332 */         dp.searchViterbi();
/* 333 */         StatePath sp = dp.getStatePath();
/* 334 */         if (sp.size() != dat.length()) {
/* 335 */           throw new RuntimeException("!!  " + dat.noCopies() + " / " + ij + " / " + sp.size() + " / " + dat.length());
/*     */         }
/* 337 */         EmissionState[] previous = (EmissionState[])null;
/* 338 */         Integer[][] num_k = new Integer[dat.noCopies()][dat.length()];
/* 339 */         num.add(num_k);
/* 340 */         for (int i = 0; i < sp.size(); i++) {
/* 341 */           CompoundState st = (CompoundState)sp.getState(i);
/* 342 */           EmissionState[] states = st.getMemberStates();
/* 343 */           for (int k1 = 0; k1 < states.length; k1++) {
/* 344 */             int ste = states[k1].index;
/*     */             
/* 346 */             num_k[k1][i] = Integer.valueOf(ste); int 
/* 347 */               tmp303_301 = i; Integer[] tmp303_300 = num1[ste];tmp303_300[tmp303_301] = Integer.valueOf(tmp303_300[tmp303_301].intValue() + 1);
/*     */           }
/*     */           
/* 350 */           previous = states;
/*     */         }
/*     */         
/* 353 */         if (pw != null) {
/* 354 */           for (int k1 = 0; k1 < num_k.length; k1++) {
/* 355 */             pw.println(Format.sprintf(this.sb_i1, num_k[k1]));
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception exc) {
/* 360 */         exc.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 365 */     pw.flush();
/* 366 */     return num;
/*     */   }
/*     */   
/*     */   public List<PhasedIntegerGenotypeData> filter(ArrayList<Integer[][]> vit_cases, Integer[] sigSites, Integer[] sigStates)
/*     */   {
/* 371 */     List<PhasedIntegerGenotypeData> list = new ArrayList();
/* 372 */     for (int k = 0; k < this.data.size(); k++) {
/* 373 */       Integer[][] vit = (Integer[][])vit_cases.get(k);
/* 374 */       boolean match = false;
/* 375 */       for (int k1 = 0; k1 < vit.length; k1++) {
/* 376 */         boolean allMatch = true;
/* 377 */         for (int i = 0; i < sigSites.length; i++) {
/* 378 */           int s = sigSites[i].intValue();
/* 379 */           int j = sigStates[i].intValue();
/* 380 */           int j1 = vit[k1][s].intValue();
/* 381 */           allMatch = (allMatch) && (j1 == j);
/*     */         }
/* 383 */         match = (match) || (allMatch);
/*     */       }
/* 385 */       if (match) list.add((PhasedIntegerGenotypeData)this.data.get(k));
/*     */     }
/* 387 */     for (Iterator<PhasedIntegerGenotypeData> it = list.iterator(); it.hasNext();) {
/* 388 */       ((PhasedIntegerGenotypeData)it.next()).restrictTo(sigSites[0], sigSites[(sigStates.length - 1)]);
/*     */     }
/* 390 */     return list;
/*     */   }
/*     */   
/*     */   public Double[][] calcBestPathForwardBackward(CompoundMarkovModel hmm) throws Exception {
/* 394 */     MarkovModel hapHMM = hmm.getMarkovModel(0);
/* 395 */     Double[][] num1 = new Double[hapHMM.modelLength()][this.data.get(0).length()];
/* 396 */     for (int i = 0; i < num1.length; i++) {
/* 397 */       Arrays.fill(num1[i], Double.valueOf(0.0D));
/*     */     }
/*     */     
/* 400 */     for (Iterator<PhasedIntegerGenotypeData> it = this.data.iterator(); it.hasNext();) {
/*     */       try {
/* 402 */         StateIndices dat = new StateScorableObject((ScorableObject)it.next(), hmm);
/* 403 */         DP dp = new DP(hmm, "", dat, false);
/* 404 */         dp.reset();
/* 405 */         dp.search(true);
/* 406 */         for (int i = 0; i < dat.length(); i++) {
/* 407 */           StateDistribution dist = new StateDistribution(hmm.modelLength());
/* 408 */           dp.getPosterior(i, dist);
/* 409 */           for (int j = 1; j < hmm.modelLength(); j++) {
/* 410 */             CompoundState st = (CompoundState)hmm.getState(j);
/* 411 */             EmissionState[] states = st.getMemberStates();
/* 412 */             for (int k1 = 0; k1 < states.length; k1++) {
/* 413 */               int ste = states[k1].index; int 
/* 414 */                 tmp188_186 = i; Double[] tmp188_185 = num1[ste];tmp188_185[tmp188_186] = Double.valueOf(tmp188_185[tmp188_186].doubleValue() + dist.get(j).doubleValue());
/*     */             }
/*     */             
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception exc)
/*     */       {
/* 422 */         exc.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 426 */     return num1;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/Sampler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */