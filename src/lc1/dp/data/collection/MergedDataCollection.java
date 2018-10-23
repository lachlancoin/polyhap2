/*     */ package lc1.dp.data.collection;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.data.representation.SimpleScorableObject;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.HaplotypeEmissionState;
/*     */ import lc1.stats.IntegerDistribution;
/*     */ import lc1.stats.PseudoDistribution;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MergedDataCollection
/*     */   extends LikelihoodDataCollection
/*     */ {
/*     */   private final double[] weight;
/*     */   public final String[] dataTypes;
/*     */   
/*     */   public String[] getUnderlyingDataSets()
/*     */   {
/*  37 */     return this.dataTypes;
/*     */   }
/*     */   
/*  40 */   Map<String, Double> weights = new HashMap();
/*     */   public List<int[]> transfer;
/*     */   
/*  43 */   public double weight(String key) { Double res = (Double)this.weights.get(key);
/*  44 */     if (res != null) return res.doubleValue();
/*  45 */     return 1.0D;
/*     */   }
/*     */   
/*     */   protected int getNumberDataTypes()
/*     */   {
/*  50 */     return this.dataTypes.length;
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
/*     */   public static DataCollection getMergedDataCollection(DataCollection[] ldl1, double cn_ratio)
/*     */   {
/*  64 */     List<DataCollection> ldl = new ArrayList((Collection)Arrays.asList(ldl1));
/*  65 */     if (ldl.size() == 1) return (DataCollection)ldl.get(0);
/*  66 */     return new MergedDataCollection((DataCollection[])ldl.toArray(new DataCollection[0]), Constants.weights());
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
/*     */   private MergedDataCollection(DataCollection[] ldl, double[] weights)
/*     */   {
/*  81 */     this.pheno = new MergedPhenotypes(ldl);
/*     */     
/*  83 */     this.ldl = ldl;
/*  84 */     this.dataTypes = new String[ldl.length];
/*  85 */     this.stSp = ldl[0].stSp;
/*  86 */     this.stSp1 = ldl[0].stSp1;
/*  87 */     this.weight = weights;
/*  88 */     Set<String> keys = new HashSet();
/*  89 */     compareAlleles(ldl);
/*  90 */     for (int i = 0; i < ldl.length; i++) {
/*  91 */       keys.addAll(ldl[i].getKeys());
/*  92 */       this.dataTypes[i] = ldl[i].name;
/*     */     }
/*  94 */     List<String> keysL = new ArrayList(keys);
/*  95 */     Logger.global.info("creating merged data set ");
/*  96 */     List<Map<Integer, Info>> list = new ArrayList(this.map.values());
/*  97 */     this.length = Integer.valueOf(list.size());
/*  98 */     createDataStructure(keysL);
/*     */     
/* 100 */     Collections.sort(list, new Comparator() {
/*     */       public int compare(Map<Integer, MergedDataCollection.Info> o1, Map<Integer, MergedDataCollection.Info> o2) {
/* 102 */         Integer loc1 = ((MergedDataCollection.Info)o1.values().iterator().next()).loc;
/* 103 */         Integer loc2 = ((MergedDataCollection.Info)o2.values().iterator().next()).loc;
/* 104 */         return loc1.compareTo(loc2);
/*     */       }
/* 106 */     });
/* 107 */     this.snpid = new ArrayList(list.size());
/* 108 */     this.loc = new ArrayList(list.size());
/* 109 */     double[] wts = Constants.weights();
/* 110 */     for (int i = 0; i < list.size(); i++) {
/* 111 */       Map<Integer, Info> m_i = (Map)list.get(i);
/* 112 */       Info first = (Info)m_i.values().iterator().next();
/*     */       
/* 114 */       this.loc.add(first.loc);
/* 115 */       this.snpid.add(first.id);
/* 116 */       this.majorAllele.add(first.majorAllele);
/* 117 */       this.minorAllele.add(first.minorAllele);
/* 118 */       for (int k = 0; k < keysL.size(); k++) {
/* 119 */         String key = (String)keysL.get(k);
/* 120 */         HaplotypeEmissionState st = (HaplotypeEmissionState)dataL(key);
/* 121 */         HaplotypeEmissionState st1 = (HaplotypeEmissionState)this.data.get(key);
/* 122 */         for (Iterator<Integer> it = m_i.keySet().iterator(); it.hasNext();) {
/* 123 */           int dat_ind = ((Integer)it.next()).intValue();
/* 124 */           if (ldl[dat_ind].containsKey(key)) {
/* 125 */             st.setDataIndex(dat_ind);
/* 126 */             Double wt = (Double)this.weights.get(key);
/* 127 */             if (wt == null) this.weights.put(key, Double.valueOf(weights[dat_ind])); else
/* 128 */               this.weights.put(key, Double.valueOf(Math.max(wt.doubleValue(), weights[dat_ind])));
/* 129 */             Info inf = (Info)m_i.get(Integer.valueOf(dat_ind));
/* 130 */             int position_within = inf.relative_position;
/* 131 */             if (dat_ind != inf.data_index) throw new RuntimeException("!!");
/* 132 */             HaplotypeEmissionState within = (HaplotypeEmissionState)ldl[dat_ind].dataL(key);
/* 133 */             st.updateEmissions(i, within.emissions[position_within]);
/* 134 */             HaplotypeEmissionState emst = (HaplotypeEmissionState)ldl[dat_ind].data.get(key);
/* 135 */             if (st1.emissions[i] == null) {
/* 136 */               st1.updateEmissions(i, emst.emissions[position_within]);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 142 */         if (st.emissions[i] == null) {
/* 143 */           st.emissions[i] = new SimpleExtendedDistribution(st.getEmissionStateSpace().defaultList.size(), Double.POSITIVE_INFINITY);
/* 144 */           st.emissions[i].setDataIndex((short)-2);
/* 145 */           st1.emissions[i] = new IntegerDistribution(0);
/* 146 */           st1.emissions[i].setDataIndex((short)-2);
/* 147 */           if (st.getName().startsWith("NA")) {
/* 148 */             System.err.println("hap map not contained " + first);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 154 */     calculateMaf(false);
/* 155 */     Logger.global.info("h");
/*     */   }
/*     */   
/*     */ 
/*     */   class Comparison
/*     */   {
/*     */     int index1;
/*     */     int index2;
/*     */     String rsid;
/*     */     Set<String> keys;
/*     */     
/*     */     Comparison() {}
/*     */   }
/*     */   
/*     */   private Set<String>[][] getCommonKeys(DataCollection[] ldl2)
/*     */   {
/* 171 */     Set[][] res = new HashSet[ldl2.length][ldl2.length];
/* 172 */     for (int i = 0; i < ldl2.length; i++) {
/* 173 */       Set<String> keys1 = ldl2[i].getKeys();
/* 174 */       for (int j = i + 1; j < ldl2.length; j++) {
/* 175 */         res[i][j] = new HashSet(ldl2[j].getKeys());
/* 176 */         res[i][j].retainAll(keys1);
/*     */       }
/*     */     }
/*     */     
/* 180 */     return res;
/*     */   }
/*     */   
/*     */   public void switchAlleles(DataCollection[] ldl2) {
/* 184 */     for (int i = 0; i < this.switchedAlleles.size(); i++) {
/* 185 */       Info inf = (Info)this.switchedAlleles.get(i);
/* 186 */       ldl2[inf.data_index].switchAlleles(inf.relative_position);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void compareDataPoints(Set<String> ck, Info inf1, Info inf2, DataCollection ldl1, DataCollection ldl2)
/*     */   {
/* 194 */     for (Iterator<String> it = ck.iterator(); it.hasNext();) {
/* 195 */       String st = (String)it.next();
/* 196 */       int st1 = ((lc1.dp.states.PhasedDataState)ldl1.data.get(st)).emissions[inf1.relative_position].fixedInteger().intValue();
/* 197 */       int st2 = ((lc1.dp.states.PhasedDataState)ldl2.data.get(st)).emissions[inf2.relative_position].fixedInteger().intValue();
/* 198 */       if (st1 != st2) {
/* 199 */         try { throw new RuntimeException("inconsistent " + st);
/*     */         } catch (Exception exc) {
/* 201 */           exc.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 209 */   public List<Info> switchedAlleles = new ArrayList();
/*     */   
/*     */   class Info {
/*     */     public Info(String id, Character b_all, Character a_all, int data_index, int relative_position, Integer loc2, double afreq) {
/* 213 */       this.id = id;
/* 214 */       this.minorAllele = b_all;
/* 215 */       this.majorAllele = a_all;
/* 216 */       this.data_index = data_index;
/* 217 */       this.relative_position = relative_position;
/* 218 */       this.loc = loc2;
/* 219 */       this.Afreq = afreq; }
/*     */     
/*     */     String id;
/* 222 */     public String toString() { return this.id + "_" + this.loc + "_" + this.data_index + "_" + this.relative_position; }
/*     */     
/*     */     Character minorAllele;
/*     */     Character majorAllele;
/*     */     Integer loc;
/*     */     double Afreq;
/*     */     int data_index;
/*     */     int relative_position;
/*     */     public void compare(Info res) {
/* 231 */       if (Math.abs(res.loc.intValue() - this.loc.intValue()) > 100)
/*     */       {
/* 233 */         throw new RuntimeException("same id at different location" + this.id + " " + res.loc + " " + this.loc);
/*     */       }
/*     */       
/* 236 */       System.err.println("same id loc " + this.id + " " + res.loc + " " + this.loc);
/*     */       
/* 238 */       if ((res.minorAllele != null) && (res.majorAllele != null) && (this.minorAllele != null) && (this.majorAllele != null)) {
/* 239 */         boolean match = MergedDataCollection.check(res.minorAllele.charValue(), this.minorAllele.charValue());
/* 240 */         boolean match1 = MergedDataCollection.check(res.majorAllele.charValue(), this.majorAllele.charValue());
/* 241 */         if (match != match1) {
/* 242 */           throw new RuntimeException("!!");
/*     */         }
/* 244 */         if (!match)
/*     */         {
/* 246 */           MergedDataCollection.this.switchedAlleles.add(res);
/* 247 */           compare(this.Afreq, 1.0D - res.Afreq, res.data_index);
/* 248 */           throw new RuntimeException("should already be switched !!!");
/*     */         }
/*     */         
/* 251 */         compare(this.Afreq, this.Afreq, res.data_index);
/*     */       }
/*     */     }
/*     */     
/*     */     private void compare(double afreq2, double afreq3, int data_ind1) {
/* 256 */       Logger.global.info("freq difference " + Math.abs(afreq2 - afreq3) + " at " + this.id + " between " + MergedDataCollection.this.ldl[this.data_index].name + " " + MergedDataCollection.this.ldl[data_ind1]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 262 */   Map<String, Map<Integer, Info>> map = new HashMap();
/*     */   
/* 264 */   private static void compare(Map<Integer, Info> m) { Iterator<Integer> it = m.keySet().iterator();
/* 265 */     Integer firstKey = (Integer)it.next();
/* 266 */     Info first = (Info)m.get(firstKey);
/*     */     
/* 268 */     while (it.hasNext()) {
/* 269 */       Integer key = (Integer)it.next();
/* 270 */       Info inf = (Info)m.get(key);
/* 271 */       first.compare(inf);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void compareCommonStates(Map<Integer, Info> m, Set<String>[][] commonKeys, DataCollection[] ldl2) {
/* 276 */     Iterator<Integer> it = m.keySet().iterator();
/* 277 */     Integer firstKey = (Integer)it.next();
/* 278 */     Info first = (Info)m.get(firstKey);
/* 279 */     while (it.hasNext()) {
/* 280 */       Info info = (Info)m.get(it.next());
/* 281 */       compareDataPoints(commonKeys[first.data_index][info.data_index], first, info, ldl2[first.data_index], ldl2[info.data_index]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void compareAlleles(DataCollection[] ldl2)
/*     */   {
/* 289 */     for (int i = 0; i < ldl2.length; i++)
/*     */     {
/*     */ 
/* 292 */       for (int j = 0; j < ldl2[i].loc.size(); j++) {
/* 293 */         double[] maf = ldl2[i].maf.getEmiss(i);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 298 */         Character A_all = ldl2[i].majorAllele.size() > 0 ? (Character)ldl2[i].majorAllele.get(j) : null;
/* 299 */         Character B_all = ldl2[i].minorAllele.size() > 0 ? (Character)ldl2[i].minorAllele.get(j) : null;
/* 300 */         String id = (String)ldl2[i].snpid.get(j);
/* 301 */         Integer loc = (Integer)ldl2[i].loc.get(j);
/* 302 */         int a_ind = ldl2[i].maf.getEmissionStateSpace().getFromString("A").intValue();
/* 303 */         int b_ind = ldl2[i].maf.getEmissionStateSpace().getFromString("B").intValue();
/* 304 */         Info inf = new Info(id, B_all, A_all, i, j, loc, maf[a_ind] / (maf[a_ind] + maf[b_ind]));
/* 305 */         Map<Integer, Info> res = (Map)this.map.get(id);
/* 306 */         if (res == null) {
/* 307 */           this.map.put(id, res = new TreeMap());
/*     */         }
/* 309 */         res.put(Integer.valueOf(i), inf);
/*     */       }
/*     */     }
/* 312 */     for (Iterator<Map<Integer, Info>> it = this.map.values().iterator(); it.hasNext();) {
/* 313 */       Map<Integer, Info> map = (Map)it.next();
/* 314 */       compare(map);
/*     */     }
/* 316 */     switchAlleles(ldl2);
/*     */   }
/*     */   
/*     */ 
/*     */   final DataCollection[] ldl;
/*     */   
/*     */   HaplotypeEmissionState[] tmp;
/*     */   private void print(double[] probs1)
/*     */   {
/* 325 */     Double[] d = new Double[probs1.length];
/* 326 */     StringBuffer sb = new StringBuffer();
/* 327 */     for (int i = 0; i < probs1.length; i++) {
/* 328 */       d[i] = Double.valueOf(probs1[i]);
/* 329 */       sb.append("%5.3f \t");
/*     */     }
/* 331 */     System.err.println(Format.sprintf(sb.toString(), d));
/*     */   }
/*     */   
/*     */   private static char transform(char c) {
/* 335 */     char a = Character.toLowerCase(c);
/* 336 */     if ((a == 'a') || (a == 't')) return 'a';
/* 337 */     if ((a == 'g') || (a == 'c')) { return 'g';
/*     */     }
/* 339 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   private static boolean check(char c, char charValue)
/*     */   {
/* 344 */     if ((c == '-') || (charValue == '-')) return true;
/* 345 */     return transform(charValue) == transform(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HaplotypeEmissionState createEmissionState(String key)
/*     */   {
/* 354 */     if (this.tmp == null) {
/* 355 */       this.tmp = new HaplotypeEmissionState[this.ldl.length];
/*     */     }
/*     */     
/* 358 */     for (int i = 0; i < this.tmp.length; i++) {
/* 359 */       this.tmp[i] = ((HaplotypeEmissionState)this.ldl[i].dataL.get(key));
/*     */     }
/* 361 */     Double[] phenD = ((MergedPhenotypes)this.pheno).getRecodedValues(this.tmp);
/* 362 */     int no_copy = -1;
/*     */     
/* 364 */     for (int i = 0; i < this.ldl.length; i++) {
/* 365 */       if (this.ldl[i].containsKey(key)) {
/* 366 */         Double[] phenD_i = ((EmissionState)this.ldl[i].dataL.get(key)).phenValue();
/*     */         
/* 368 */         if (no_copy < 0) { no_copy = this.ldl[i].no_copies;
/* 369 */         } else if (no_copy != this.ldl[i].no_copies) { throw new RuntimeException("!!");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 377 */     HaplotypeEmissionState res = new HaplotypeEmissionState(key, this.length.intValue(), this.stSp[(no_copy - 1)], (short)-1);
/* 378 */     res.setPhenotype(phenD);
/* 379 */     return res;
/*     */   }
/*     */   
/*     */   public final void createDataStructure(List<String> indiv)
/*     */   {
/* 384 */     int i = 0;
/* 385 */     for (Iterator<String> it = indiv.iterator(); it.hasNext(); i++) {
/* 386 */       String key = (String)it.next();
/*     */       
/* 388 */       HaplotypeEmissionState value = createEmissionState(key);
/*     */       
/*     */ 
/* 391 */       this.dataL.put(key, value);
/* 392 */       this.data.put(key, SimpleScorableObject.make(key, this.length.intValue(), value.getEmissionStateSpace()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/collection/MergedDataCollection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */