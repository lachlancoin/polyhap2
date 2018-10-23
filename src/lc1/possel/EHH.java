/*     */ package lc1.possel;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.zip.ZipFile;
/*     */ import lc1.dp.data.collection.DataCollection;
/*     */ import lc1.dp.data.collection.SimpleDataCollection;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ import lc1.dp.states.EmissionState;
/*     */ 
/*     */ public class EHH extends AbstractEHH
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/*  29 */       File user = new File(System.getProperty("user.dir"));
/*  30 */       File[] f = user.listFiles(new java.io.FilenameFilter() {
/*     */         public boolean accept(File arg0, String arg1) {
/*  32 */           return arg1.endsWith(".zip");
/*     */         }
/*     */       });
/*     */       
/*  36 */       for (int i = 0; i < f.length; i++) {
/*  37 */         ZipFile zf = new ZipFile(f[i]);
/*  38 */         BufferedReader br = new BufferedReader(new java.io.InputStreamReader(zf.getInputStream(zf.getEntry("phased2.txt_1"))));
/*  39 */         SimpleDataCollection sdt = 
/*  40 */           SimpleDataCollection.readFastPhaseOutput(br, Emiss.class, Emiss.getEmissionStateSpace(1));
/*  41 */         int st = 10;
/*  42 */         Map<String, List<PIGData>> m = sdt.getAllHaplotypes(st, st);
/*  43 */         String[] cores = (String[])m.keySet().toArray(new String[0]);
/*  44 */         double[] maf = new double[cores.length];
/*  45 */         double sum = 0.0D;
/*  46 */         for (int ik = 0; ik < maf.length; ik++) {
/*  47 */           maf[ik] = ((List)m.get(cores[ik])).size();
/*  48 */           sum += maf[ik];
/*     */         }
/*  50 */         for (int ik = 0; ik < maf.length; ik++) {
/*  51 */           maf[ik] /= sum;
/*     */         }
/*     */         
/*  54 */         EHH ehh = new EHH(sdt, f[i].getName());
/*  55 */         Double[][] res = new Double[2][cores.length];
/*  56 */         List[][] in1 = new List[2][cores.length];
/*  57 */         for (int k = 0; k < in1.length; k++) {
/*  58 */           for (int j = 0; j < in1.length; j++) {
/*  59 */             in1[k][j] = new ArrayList();
/*     */           }
/*     */         }
/*     */         
/*  63 */         ehh.setCore(st, st, cores, res, new boolean[] { true, true }, in1);
/*  64 */         GraphIHH.plot(in1, 0.0D, 0.0D, st, st, sdt.loc, f[i], Double.valueOf(maf[0]), true);
/*  65 */         System.err.println("h");
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/*  69 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*  73 */   static double thresh = 0.05D;
/*     */   
/*     */ 
/*  76 */   Map<String, EHHCalculator> map = new HashMap();
/*     */   
/*     */   public EHH(SimpleDataCollection coll, String f) throws Exception {
/*  79 */     super(coll, f);
/*     */   }
/*     */   
/*     */   public int noCharInCommon(String st, String st1, boolean left) {
/*  83 */     int len = st.length();
/*  84 */     if (len != st1.length()) { throw new RuntimeException("!!");
/*     */     }
/*  86 */     for (int i = 0; i < len; i++) {
/*  87 */       if (!left) {
/*  88 */         if (st.charAt(i) != st1.charAt(i)) return i;
/*     */       }
/*  90 */       else if ((left) && 
/*  91 */         (st.charAt(len - (i + 1)) != st1.charAt(len - (i + 1)))) {
/*  92 */         return i;
/*     */       }
/*     */     }
/*     */     
/*  96 */     return len;
/*     */   }
/*     */   
/*     */   class EHHCalculator
/*     */   {
/*     */     boolean left;
/* 102 */     List<Double> scores = new ArrayList();
/* 103 */     List<Integer> loc1 = new ArrayList();
/*     */     SortedMap<String, List<PIGData>> hapl_1;
/*     */     int originalNumber;
/*     */     String core;
/* 107 */     int pos = 0;
/* 108 */     StringBuffer ch = new StringBuffer();
/*     */     
/* 110 */     String mostCommon() { Iterator<Map.Entry<String, List<PIGData>>> it = this.hapl_1.entrySet().iterator();
/* 111 */       String st = "";int count = 0;
/* 112 */       while (it.hasNext()) {
/* 113 */         Map.Entry<String, List<PIGData>> ent = (Map.Entry)it.next();
/* 114 */         int cnt1 = ((List)ent.getValue()).size();
/* 115 */         if (cnt1 >= count) {
/* 116 */           st = (String)ent.getKey();
/* 117 */           count = cnt1;
/*     */         }
/*     */       }
/* 120 */       if (this.left) return st.substring(0, st.length() - (EHH.this.end - EHH.this.start + 1));
/* 121 */       return st.substring(EHH.this.end - EHH.this.start + 1, st.length());
/*     */     }
/*     */     
/*     */     EHHCalculator(String core, boolean left)
/*     */     {
/* 126 */       this.core = core;
/* 127 */       this.left = left;
/* 128 */       List<PIGData> dat = (List)EHH.this.sdt.getAllHaplotypes(EHH.this.start, EHH.this.end).get(core);
/* 129 */       this.originalNumber = dat.size();
/*     */       
/* 131 */       this.hapl_1 = new TreeMap();
/* 132 */       this.hapl_1.put(core, dat);
/*     */       
/* 134 */       this.pos += 1;
/* 135 */       this.scores.add(Double.valueOf(numEquiv(this.hapl_1)));
/*     */       
/* 137 */       this.loc1.add((Integer)EHH.this.sdt.loc.get(left ? EHH.this.start : EHH.this.end));
/* 138 */       this.ch.append(left ? core.charAt(0) : core.charAt(core.length() - 1));
/*     */     }
/*     */     
/*     */ 
/*     */     public void printSum(Logger log)
/*     */     {
/* 144 */       log.info(EHH.this.name + " " + EHH.this.start + " " + EHH.this.end + " " + this.core);
/* 145 */       log.info(this.scores + " ");
/*     */     }
/*     */     
/*     */     public double sum()
/*     */     {
/* 150 */       double sum = 0.0D;
/* 151 */       for (int i = 0; i < this.scores.size() - 1; i++)
/*     */       {
/*     */ 
/*     */ 
/* 155 */         double midpoint = (scores(i) + scores(i + 1)) / 2.0D;
/* 156 */         double width = Math.abs(((Integer)this.loc1.get(i + 1)).intValue() - ((Integer)this.loc1.get(i)).intValue());
/* 157 */         sum += width * midpoint;
/*     */       }
/* 159 */       return sum;
/*     */     }
/*     */     
/* 162 */     public double scores(int i) { return ((Double)this.scores.get(i)).doubleValue() == 0.0D ? 0.0D : ((Double)this.scores.get(i)).doubleValue() / ((Double)this.scores.get(0)).doubleValue(); }
/*     */     
/*     */     public int getNextStart() {
/* 165 */       if (this.left)
/*     */       {
/* 167 */         return EHH.this.start - this.pos;
/*     */       }
/* 169 */       return EHH.this.start;
/*     */     }
/*     */     
/* 172 */     public int getNextEnd() { if (this.left) { return EHH.this.end;
/*     */       }
/*     */       
/* 175 */       return EHH.this.end + this.pos;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean skip(int i)
/*     */     {
/* 181 */       if (EHH.this.sdt.maf.getFixedInteger(i) != null) { return true;
/*     */       }
/* 183 */       double[] d = EHH.this.sdt.maf.getEmiss(i);
/* 184 */       for (int k = 0; k < d.length; k++) {
/* 185 */         if ((d[k] > 0.0D) && (EHH.this.sdt.maf.getEmissionStateSpace().getCN(k) != 1)) {
/* 186 */           return true;
/*     */         }
/*     */       }
/*     */       
/* 190 */       return false;
/*     */     }
/*     */     
/*     */     public boolean extend() {
/* 194 */       int start_ = getNextStart();
/* 195 */       int end_ = getNextEnd();
/* 196 */       this.pos += 1;
/* 197 */       if (this.left) {
/* 198 */         if (skip(start_)) {
/* 199 */           return true;
/*     */         }
/*     */         
/*     */       }
/* 203 */       else if (skip(end_)) { return true;
/*     */       }
/* 205 */       SortedMap<String, List<PIGData>> map_1 = this.hapl_1;
/* 206 */       SortedMap<String, List<PIGData>> m_pos = new TreeMap();
/*     */       
/*     */ 
/*     */ 
/* 210 */       this.loc1.add(this.left ? (Integer)EHH.this.sdt.loc.get(start_) : (Integer)EHH.this.sdt.loc.get(end_));
/* 211 */       int max_size = 0;
/* 212 */       String commonHapl = "";
/* 213 */       Map.Entry<String, List<PIGData>> nxt; int i; for (Iterator<Map.Entry<String, List<PIGData>>> it = map_1.entrySet().iterator(); it.hasNext(); 
/*     */           
/* 215 */           i < ((List)nxt.getValue()).size())
/*     */       {
/* 214 */         nxt = (Map.Entry)it.next();
/* 215 */         i = 0; continue;
/* 216 */         PIGData dat_i = (PIGData)((List)nxt.getValue()).get(i);
/* 217 */         String hapl = dat_i.getStringRep(start_, end_);
/* 218 */         List<PIGData> l_i = (List)m_pos.get(hapl);
/* 219 */         if (l_i == null) {
/* 220 */           m_pos.put(hapl, l_i = new ArrayList());
/*     */         }
/* 222 */         l_i.add(dat_i);
/* 223 */         if (l_i.size() > max_size) {
/* 224 */           max_size = l_i.size();
/* 225 */           commonHapl = hapl;
/*     */         }
/* 215 */         i++;
/*     */       }
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
/* 229 */       this.scores.add(Double.valueOf(numEquiv(m_pos)));
/* 230 */       this.ch.append(this.left ? commonHapl.charAt(0) : commonHapl.charAt(commonHapl.length() - 1));
/* 231 */       this.hapl_1 = m_pos;
/* 232 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public double combinations(double numHapl)
/*     */     {
/* 244 */       return 
/*     */       
/*     */ 
/* 247 */         numHapl * (numHapl - 1.0D) / 2.0D;
/*     */     }
/*     */     
/* 250 */     public double numEquiv(SortedMap<String, List<PIGData>> map) { double sum = 0.0D;
/*     */       
/* 252 */       for (Iterator<List<PIGData>> it = map.values().iterator(); it.hasNext();)
/*     */       {
/* 254 */         sum += combinations(((List)it.next()).size());
/*     */       }
/* 256 */       return sum;
/*     */     }
/*     */     
/*     */     public double ehhMin() {
/* 260 */       return scores(this.scores.size() - 1);
/*     */     }
/*     */     
/*     */     public boolean canExtend()
/*     */     {
/* 265 */       if ((getNextStart() < 0) || (getNextEnd() >= EHH.this.sdt.length())) return false;
/* 266 */       return true;
/*     */     }
/*     */     
/* 269 */     public boolean hasNext() { if ((canExtend()) && (ehhMin() >= EHH.thresh)) {
/* 270 */         return true;
/*     */       }
/* 272 */       return false;
/*     */     }
/*     */     
/*     */     public char ch(int k)
/*     */     {
/* 277 */       return this.ch.charAt(k);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 284 */   public double minEHH = 0.0D;
/* 285 */   static boolean[] left = { true };
/*     */   
/*     */ 
/*     */   public Integer findAncestral(int start, int end, String derived, String[] ancestral, boolean[] doLR)
/*     */   {
/* 290 */     this.start = start;
/* 291 */     this.end = end;
/* 292 */     Integer[] anc_index = new Integer[2];
/* 293 */     for (int i = 0; i < left.length; i++) {
/* 294 */       if (doLR[i] != 0) {
/* 295 */         EHHCalculator coredDerived = new EHHCalculator(derived, left[i]);
/* 296 */         EHHCalculator[] coreAncestral = new EHHCalculator[ancestral.length];
/*     */         
/* 298 */         int[] st_anc = new int[coreAncestral.length];
/* 299 */         for (int j = 0; j < coreAncestral.length; j++) {
/* 300 */           coreAncestral[j] = new EHHCalculator(ancestral[j], left[i]);
/*     */         }
/* 302 */         while (coredDerived.canExtend()) {
/* 303 */           coredDerived.extend();
/* 304 */           String st_der = coredDerived.mostCommon();
/* 305 */           String[] anc_com = new String[coreAncestral.length];
/* 306 */           for (int j = 0; j < coreAncestral.length; j++) {
/* 307 */             coreAncestral[j].extend();
/* 308 */             anc_com[j] = coreAncestral[j].mostCommon();
/* 309 */             st_anc[j] = noCharInCommon(st_der, anc_com[j], left[i]);
/*     */           }
/* 311 */           int max_index = lc1.util.Constants.getMax(st_anc);
/* 312 */           boolean greaterThanAll = st_anc[max_index] > 0;
/* 313 */           for (int j = 0; (j < coreAncestral.length) && (greaterThanAll); j++) {
/* 314 */             if ((j != max_index) && 
/* 315 */               (st_anc[max_index] <= st_anc[j])) greaterThanAll = false;
/*     */           }
/* 317 */           if (greaterThanAll)
/* 318 */             anc_index[i] = Integer.valueOf(max_index);
/*     */         }
/*     */       }
/*     */     }
/* 322 */     if (anc_index[0] == null) return anc_index[1];
/* 323 */     if (anc_index[1] == null) return anc_index[0];
/* 324 */     if (anc_index[0] == anc_index[1]) return anc_index[0];
/* 325 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCore(int start, int end, String[] cores, Double[][] res, boolean[] doLR, List<Double>[][] in1)
/*     */   {
/* 336 */     super.setCore(start, end, cores, res, doLR, in1);
/*     */     
/* 338 */     EHHCalculator[] ehh = new EHHCalculator[cores.length];
/*     */     
/*     */ 
/* 341 */     for (int i = 0; i < left.length; i++) {
/* 342 */       if (doLR[i] != 0) {
/* 343 */         int numNonFalse = 0;
/* 344 */         for (int j = 0; j < cores.length; j++) {
/* 345 */           ehh[j] = new EHHCalculator(cores[j], left[i]);
/*     */           
/* 347 */           if (ehh[j].originalNumber < 2) ehh[j] = null; else
/* 348 */             numNonFalse++;
/*     */         }
/* 350 */         if (numNonFalse <= 1) {}
/* 351 */         while (hasNextCore(ehh)) {
/* 352 */           extendCore(ehh);
/*     */         }
/* 354 */         for (int j = 0; j < ehh.length; j++) {
/* 355 */           res[i][j] = Double.valueOf(ehh[j].sum());
/* 356 */           in1[i][0] = ehh[j].scores;
/*     */         }
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
/*     */   private void extendCore(EHHCalculator[] ehh)
/*     */   {
/* 373 */     for (int i = 0; i < ehh.length; i++) {
/* 374 */       ehh[i].extend();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasNextCore(EHHCalculator[] ehh)
/*     */   {
/* 380 */     for (int i = 0; i < ehh.length; i++) {
/* 381 */       if (ehh[i].hasNext()) return true;
/*     */     }
/* 383 */     return false;
/*     */   }
/*     */   
/* 386 */   static boolean plot = true;
/*     */   
/*     */   public double score(Double[][] scores) {
/* 389 */     return Math.log((scores[0][0].doubleValue() + scores[1][0].doubleValue()) / (scores[0][1].doubleValue() + scores[1][1].doubleValue()));
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/possel/EHH.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */