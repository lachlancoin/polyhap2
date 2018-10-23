/*      */ package lc1.dp.data.collection;
/*      */ 
/*      */ import calc.DPrimeCalculator;
/*      */ import calc.LDCalculator;
/*      */ import calc.R2Calculator;
/*      */ import com.braju.format.Format;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FileReader;
/*      */ import java.io.FileWriter;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.StringWriter;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeMap;
/*      */ import java.util.TreeSet;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.zip.GZIPInputStream;
/*      */ import java.util.zip.ZipFile;
/*      */ import lc1.CGH.Aberation;
/*      */ import lc1.CGH.Location;
/*      */ import lc1.CGH.Locreader;
/*      */ import lc1.dp.data.representation.CSOData;
/*      */ import lc1.dp.data.representation.ComparableArray;
/*      */ import lc1.dp.data.representation.Emiss;
/*      */ import lc1.dp.data.representation.PIGData;
/*      */ import lc1.dp.data.representation.SimpleScorableObject;
/*      */ import lc1.dp.emissionspace.CompoundEmissionStateSpace;
/*      */ import lc1.dp.emissionspace.EmissionStateSpace;
/*      */ import lc1.dp.emissionspace.EmissionStateSpaceTranslation;
/*      */ import lc1.dp.states.AlleleCopyPairEmissionState;
/*      */ import lc1.dp.states.EmissionState;
/*      */ import lc1.dp.states.HaplotypeEmissionState;
/*      */ import lc1.dp.states.PhasedDataState;
/*      */ import lc1.ensj.PedigreeDataCollection;
/*      */ import lc1.stats.ArmitageTrendTest;
/*      */ import lc1.stats.ChiSq;
/*      */ import lc1.stats.Contigency;
/*      */ import lc1.stats.IntegerDistribution;
/*      */ import lc1.stats.ProbabilityDistribution;
/*      */ import lc1.stats.PseudoDistribution;
/*      */ import lc1.stats.SimpleExtendedDistribution;
/*      */ import lc1.stats.SimpleExtendedDistribution1;
/*      */ import lc1.stats.SkewNormal;
/*      */ import lc1.stats.TrainableNormal;
/*      */ import lc1.util.Compressor;
/*      */ import lc1.util.Constants;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class DataCollection
/*      */   extends DataC
/*      */ {
/*   78 */   public Map<String, PIGData> data = new TreeMap();
/*   79 */   public Map<String, EmissionState> dataL = new TreeMap();
/*   80 */   public Map<String, EmissionState> dataLSt = new TreeMap();
/*      */   
/*      */   EmissionStateSpace[] stSp;
/*      */   
/*      */   EmissionStateSpace[] stSp1;
/*      */   
/*      */   EmissionStateSpaceTranslation[] trans;
/*   87 */   short index = -1;
/*      */   List<Double>[] hwe;
/*      */   
/*   90 */   public void calcHWE(boolean state, boolean exclMissing) { this.hwe = new List[getNumberDataTypes()];
/*   91 */     for (int i = 0; i < getNumberDataTypes(); i++) {
/*   92 */       this.hwe[i] = new ArrayList();
/*   93 */       calcHWE(state, exclMissing, this.hwe[i], null, i);
/*      */     }
/*      */   }
/*      */   
/*      */   public void applyMedianCorrection(ZipFile zf) throws Exception
/*      */   {
/*   99 */     List<String> indiv = Compressor.getIndiv(zf, "Samples", Integer.valueOf(this.header_sample.indexOf("id")), this.index);
/*  100 */     List<String> mc = Compressor.getIndiv(zf, "Samples", Integer.valueOf(this.header_sample.indexOf("medianCorrection")), this.index);
/*  101 */     for (int i = 0; i < indiv.size(); i++) {
/*  102 */       String indiv_i = ((String)indiv.get(i)).split("#")[0];
/*  103 */       HaplotypeEmissionState state = (HaplotypeEmissionState)this.dataL.get(indiv_i);
/*  104 */       if (state == null) {
/*  105 */         throw new RuntimeException("no state for " + indiv_i);
/*      */       }
/*  107 */       state.applyCorrection(Double.parseDouble((String)mc.get(i)));
/*      */     }
/*      */   }
/*      */   
/*      */   public void applyLoess(ZipFile zf) throws Exception
/*      */   {
/*  113 */     BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry("SNPS"))));
/*  114 */     String st = "";
/*  115 */     int id_index = this.header_snp.indexOf("id");
/*  116 */     int loess_index = this.header_snp.indexOf("loess");
/*  117 */     Double[] loess = new Double[this.snpid.size()];
/*  118 */     while ((st = br.readLine()) != null) {
/*  119 */       String[] str = st.split("\\s+");
/*  120 */       int ind = this.snpid.indexOf(str[id_index]);
/*  121 */       if (ind >= 0) {
/*  122 */         loess[ind] = 
/*  123 */           Double.valueOf(str[loess_index].equals("NA") ? NaN.0D : 
/*  124 */           Double.parseDouble(str[loess_index]));
/*      */       }
/*      */     }
/*      */     
/*  128 */     for (int i = 0; i < loess.length; i++) {
/*  129 */       if (loess[i].doubleValue() == 0.0D) throw new RuntimeException("no loess for " + (String)this.snpid.get(i));
/*  130 */       if (Double.isNaN(loess[i].doubleValue())) {
/*  131 */         loess[i] = average(loess, i);
/*      */       }
/*      */     }
/*  134 */     for (Iterator<EmissionState> it = dataLvalues(); it.hasNext();) {
/*  135 */       ((HaplotypeEmissionState)it.next()).applyLoess(loess);
/*      */     }
/*      */   }
/*      */   
/*      */   private Double average(Double[] loess, int i) {
/*  140 */     int before_index = i - 1;
/*  141 */     int after_index = i + 1;
/*      */     do
/*      */     {
/*  144 */       before_index--;
/*  143 */       if (before_index < 0) break; } while (Double.isNaN(loess[before_index].doubleValue()));
/*      */     
/*      */ 
/*  146 */     while ((after_index < loess.length) && (Double.isNaN(loess[after_index].doubleValue()))) {
/*  147 */       after_index++;
/*      */     }
/*  149 */     if (before_index < 0) return loess[after_index];
/*  150 */     if (after_index >= loess.length) return loess[before_index];
/*  151 */     return Double.valueOf((loess[after_index].doubleValue() + loess[before_index].doubleValue()) / 2.0D);
/*      */   }
/*      */   
/*      */   protected int getNumberDataTypes() {
/*  155 */     return 1;
/*      */   }
/*      */   
/*      */   public void calcHWE(boolean state, boolean exclMissing, List<Double> hwe, List<Double> hwe_Exp, int data_index) {
/*  159 */     hwe.clear();
/*  160 */     if (hwe_Exp != null) hwe_Exp.clear();
/*  161 */     for (int i = 0; i < length(); i++) {
/*  162 */       hwe.add(Double.valueOf(getHWE(i, state, exclMissing, data_index)));
/*  163 */       if (hwe_Exp != null) calculatedExpectedHWE(hwe, hwe_Exp, i);
/*      */     }
/*      */   }
/*      */   
/*      */   public void calculatedExpectedHWE(List<Double> hwe, List<Double> hwe_Exp, int data_index) {
/*  168 */     List<Double[]> l = new ArrayList();
/*  169 */     for (int i = 0; i < hwe.size(); i++) {
/*  170 */       l.add(new Double[] { (Double)hwe.get(i), Double.valueOf(0.0D), Double.valueOf(i) });
/*      */     }
/*  172 */     Collections.sort(l, new Comparator()
/*      */     {
/*      */       public int compare(Double[] arg0, Double[] arg1) {
/*  175 */         return arg0[0].compareTo(arg1[0]);
/*      */       }
/*      */     });
/*      */     
/*  179 */     for (int i = 0; i < l.size(); i++) {
/*  180 */       Double[] d = (Double[])l.get(i);
/*  181 */       d[1] = Double.valueOf((i + 0.5D) / l.size());
/*      */     }
/*  183 */     Collections.sort(l, new Comparator()
/*      */     {
/*      */       public int compare(Double[] arg0, Double[] arg1) {
/*  186 */         return arg0[2].compareTo(arg1[2]);
/*      */       }
/*      */       
/*  189 */     });
/*  190 */     hwe_Exp.clear();
/*  191 */     for (int i = 0; i < l.size(); i++) {
/*  192 */       Double[] d = (Double[])l.get(i);
/*  193 */       hwe_Exp.add(d[1]);
/*      */     }
/*      */   }
/*      */   
/*  197 */   public EmissionState getState(String key, EmissionStateSpace stSp) { if (stSp == null) return null;
/*  198 */     EmissionState st = (EmissionState)this.dataLSt.get(key);
/*  199 */     if (st != null) {
/*  200 */       if (stSp != st.getEmissionStateSpace()) throw new RuntimeException("!!");
/*  201 */       return st;
/*      */     }
/*      */     
/*  204 */     this.dataLSt.put(key, st = new HaplotypeEmissionState(key, this.length.intValue(), stSp.size(), stSp, null, null, 1.0D));
/*      */     
/*  206 */     return st;
/*      */   }
/*      */   
/*      */   public SortedMap<String, List<PIGData>> getAllHaplotypes(int start, int end)
/*      */   {
/*  211 */     return getHaplotypes(start, end, new HashSet(this.data.values()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public SortedSet<Map.Entry<String, List<PIGData>>> getHaplotypes(Map<String, List<PIGData>> left)
/*      */   {
/*  218 */     SortedSet<Map.Entry<String, List<PIGData>>> s = new TreeSet(
/*  219 */       new Comparator()
/*      */       {
/*      */         public int compare(Map.Entry<String, List<PIGData>> e1, Map.Entry<String, List<PIGData>> e2)
/*      */         {
/*  223 */           int s1 = ((List)e1.getValue()).size();
/*  224 */           int s2 = ((List)e2.getValue()).size();
/*  225 */           if (s1 != s2) {
/*  226 */             return s1 > s2 ? -1 : 1;
/*      */           }
/*  228 */           return ((String)e1.getKey()).compareTo((String)e2.getKey());
/*      */         }
/*  230 */       });
/*  231 */     s.addAll((Collection)left.entrySet());
/*  232 */     return s;
/*      */   }
/*      */   
/*      */   public SortedMap<String, List<PIGData>> getHaplotypes(int st, int end, Collection<PIGData> set)
/*      */   {
/*  237 */     SortedMap<String, List<PIGData>> left = new TreeMap(
/*  238 */       new Comparator()
/*      */       {
/*      */         public int compare(String o1, String o2) {
/*  241 */           return -1 * o1.compareTo(o2);
/*      */         }
/*      */       });
/*      */     
/*      */ 
/*      */ 
/*  247 */     for (Iterator<PIGData> it = set.iterator(); it.hasNext();) {
/*  248 */       PIGData nxt = (PIGData)it.next();
/*  249 */       String stL = nxt.getStringRep(st, end);
/*  250 */       List<PIGData> cntL = (List)left.get(stL);
/*  251 */       if (cntL == null) {
/*  252 */         left.put(stL, cntL = new ArrayList());
/*      */       }
/*  254 */       cntL.add(nxt);
/*      */     }
/*      */     
/*  257 */     return left;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void drop(List<Integer> toDrop, boolean setAsMissing)
/*      */   {
/*  264 */     Collections.sort(toDrop);
/*  265 */     for (int i = toDrop.size() - 1; i >= 0; i--) {
/*  266 */       this.loc.remove(((Integer)toDrop.get(i)).intValue());
/*  267 */       if ((this.snpid != null) && (this.snpid.size() > 0)) this.snpid.remove(((Integer)toDrop.get(i)).intValue());
/*  268 */       if ((this.majorAllele != null) && (this.majorAllele.size() > 0)) this.majorAllele.remove(((Integer)toDrop.get(i)).intValue());
/*  269 */       if ((this.minorAllele != null) && (this.minorAllele.size() > 0)) { this.minorAllele.remove(((Integer)toDrop.get(i)).intValue());
/*      */       }
/*      */     }
/*  272 */     for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/*  273 */       PIGData data = (PIGData)it.next();
/*  274 */       EmissionState emst = getL(data.getName());
/*  275 */       if (setAsMissing)
/*      */       {
/*  277 */         if (emst != null) {
/*  278 */           emst.setAsMissing(toDrop, Constants.cn_ratio());
/*      */         }
/*      */       }
/*      */       else {
/*  282 */         data.removeAll(toDrop);
/*  283 */         if (emst != null) {
/*  284 */           emst.removeAll(toDrop);
/*      */         }
/*      */       }
/*      */     }
/*  288 */     if (!setAsMissing) {
/*  289 */       if (this.maf != null) this.maf.removeAll(toDrop);
/*  290 */       this.length = Integer.valueOf(this.loc.size());
/*      */     }
/*      */   }
/*      */   
/*      */   public void append(DataCollection idc)
/*      */   {
/*  296 */     for (int i = 0; i < idc.loc.size(); i++) {
/*  297 */       if (!((String)idc.snpid.get(i)).equals(this.snpid.get(i))) throw new RuntimeException("!!");
/*  298 */       if (!((Integer)idc.loc.get(i)).equals(this.loc.get(i))) throw new RuntimeException("!!");
/*  299 */       if ((this.majorAllele != null) && (this.majorAllele.size() > i)) {
/*  300 */         if (!((Character)idc.majorAllele.get(i)).equals(this.majorAllele.get(i))) throw new RuntimeException("!!");
/*  301 */         if (!((Character)idc.minorAllele.get(i)).equals(this.minorAllele.get(i))) throw new RuntimeException("!!");
/*      */       }
/*      */     }
/*  304 */     if (!idc.loc.equals(this.loc)) throw new RuntimeException("!! not equal");
/*  305 */     for (Iterator<PIGData> it = idc.data.values().iterator(); it.hasNext();) {
/*  306 */       PIGData nxt = (PIGData)it.next();
/*  307 */       this.data.put(nxt.getName(), nxt);
/*      */     }
/*  309 */     for (Iterator<EmissionState> it = idc.dataL.values().iterator(); it.hasNext();) {
/*  310 */       EmissionState nxt = (EmissionState)it.next();
/*  311 */       this.dataL.put(nxt.getName(), nxt);
/*      */     }
/*      */   }
/*      */   
/*  315 */   public void reorder() { int[] alias = getAlias(this.loc);
/*  316 */     apply(alias);
/*      */   }
/*      */   
/*  319 */   private void apply(int[] alias) { reorder(alias, this.loc);
/*  320 */     reorder(alias, this.snpid);
/*  321 */     reorder(alias, this.majorAllele);
/*  322 */     reorder(alias, this.minorAllele);
/*  323 */     for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/*  324 */       PIGData data = (PIGData)it.next();
/*  325 */       EmissionState emst = getL(data.getName());
/*  326 */       data.applyAlias(alias);
/*  327 */       emst.applyAlias(alias);
/*      */     }
/*  329 */     if (this.maf != null) this.maf.applyAlias(alias);
/*      */   }
/*      */   
/*      */   public static void reorder(int[] alias, List loc) {
/*  333 */     List clone = new ArrayList(loc);
/*  334 */     for (int i = 0; i < clone.size(); i++) {
/*  335 */       loc.set(alias[i], clone.get(i));
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reorder(int[] alias, Object[] loc)
/*      */   {
/*  341 */     Object[] clone = new Object[loc.length];
/*  342 */     System.arraycopy(loc, 0, clone, 0, loc.length);
/*  343 */     for (int i = 0; i < loc.length; i++) {
/*  344 */       loc[alias[i]] = clone[i];
/*      */     }
/*      */   }
/*      */   
/*      */   private int[] getAlias(List<Integer> loc)
/*      */   {
/*  350 */     List<Integer> loc1 = new ArrayList(loc);
/*  351 */     Collections.sort(loc1);
/*  352 */     int[] res = new int[loc.size()];
/*  353 */     for (int i = 0; i < res.length; i++) {
/*  354 */       res[i] = loc1.indexOf(loc.get(i));
/*      */     }
/*  356 */     return res;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public PedigreeDataCollection ped;
/*      */   
/*      */ 
/*      */   Contigency ct;
/*      */   
/*      */ 
/*      */   public int noCopies(String i)
/*      */   {
/*  370 */     if (this.dataL.size() == 0) throw new RuntimeException("no data");
/*  371 */     return ((EmissionState)this.dataL.get(i)).noCopies();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void fillLikelihoodData(double pseudo, int[] indices)
/*      */   {
/*  378 */     System.err.println("filling in likelihood data");
/*  379 */     int[] se = (int[])null;
/*  380 */     if ((indices != null) && (indices.length > 1)) {
/*  381 */       se = new int[] {
/*  382 */         firstGreaterThan(this.loc, indices[0]), 
/*  383 */         firstGreaterThan(this.loc, indices[1]) };
/*      */       
/*  385 */       if (this.loc.contains(Integer.valueOf(indices[1]))) {
/*  386 */         se[1] -= 1;
/*      */       }
/*      */     }
/*      */     else {
/*  390 */       se = new int[] { 0, this.loc.size() };
/*      */     }
/*      */     
/*  393 */     for (Iterator<Map.Entry<String, PIGData>> it = this.data.entrySet().iterator(); it.hasNext();) {
/*  394 */       Map.Entry<String, PIGData> key = (Map.Entry)it.next();
/*  395 */       EmissionState emSt = EmissionState.getEmissionState((CSOData)key.getValue(), false, pseudo, se);
/*  396 */       this.dataL.put((String)key.getKey(), emSt);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeSNPFile(File file, String chr, boolean header, Collection<Integer> toD)
/*      */     throws Exception
/*      */   {
/*  407 */     PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
/*  408 */     if (header) pw.println("snpid\tchr\tHG17");
/*  409 */     for (int i = 0; i < this.loc.size(); i++)
/*  410 */       if ((toD == null) || (!toD.contains(Integer.valueOf(i)))) {
/*  411 */         String snp_id = this.snpid == null ? i : (String)this.snpid.get(i);
/*  412 */         pw.print(snp_id + "\t");
/*  413 */         if (header) pw.print(chr + "\t");
/*  414 */         pw.println(this.loc.get(i));
/*      */       }
/*  416 */     pw.close();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int count(int[] indices, int index)
/*      */   {
/*  424 */     int cnt = 0;
/*  425 */     for (int i = 0; i < indices.length; i++) {
/*  426 */       if (indices[i] == index) cnt++;
/*      */     }
/*  428 */     return cnt;
/*      */   }
/*      */   
/*  431 */   protected EmissionStateSpace getEmStSpace() { return ((EmissionState)this.dataL.values().iterator().next()).getEmissionStateSpace(); }
/*      */   
/*      */ 
/*      */ 
/*      */   public Double[] scoreChi(double[][] ns, double[][] nc)
/*      */   {
/*  437 */     Double[] result = new Double[ns[0].length];
/*  438 */     for (int i = 0; i < result.length; i++) {
/*  439 */       Double R = Double.valueOf(ns[0][i] + ns[1][i] + ns[2][i]);
/*  440 */       Double N = Double.valueOf(nc[0][i] + nc[1][i] + nc[2][i] + ns[0][i] + ns[1][i] + ns[2][i]);
/*  441 */       Double r1 = Double.valueOf(ns[1][i]);Double r2 = Double.valueOf(ns[2][i]);Double n1 = Double.valueOf(ns[1][i] + nc[1][i]);Double n2 = Double.valueOf(ns[2][i] + nc[2][i]);
/*  442 */       Double chiSqaureValues1 = Double.valueOf(N.doubleValue() * Math.pow(N.doubleValue() * (r1.doubleValue() + 2.0D * r2.doubleValue()) - R.doubleValue() * (n1.doubleValue() + 2.0D * n2.doubleValue()), 2.0D));
/*  443 */       Double chiSqaureValues2 = Double.valueOf(R.doubleValue() * (N.doubleValue() - R.doubleValue()) * (N.doubleValue() * (n1.doubleValue() + 4.0D * n2.doubleValue()) - Math.pow(n1.doubleValue() + 2.0D * n2.doubleValue(), 2.0D)));
/*  444 */       if (chiSqaureValues2.doubleValue() == 0.0D) result[i] = Double.valueOf(0.0D); else
/*  445 */         result[i] = Double.valueOf(chiSqaureValues1.doubleValue() / chiSqaureValues2.doubleValue());
/*      */     }
/*  447 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   ArmitageTrendTest att;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   double[][] ns;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   double[][] nc;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   Double[] res;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getInfo(String tag, String key, int i, boolean style)
/*      */     throws Exception
/*      */   {
/*  477 */     Object o = ((Map)getClass().getField(tag).get(this)).get(key);
/*  478 */     if (o == null) {
/*  479 */       return null;
/*      */     }
/*  481 */     if ((o instanceof EmissionState)) {
/*  482 */       return ((EmissionState)o).getUnderlyingData(i);
/*      */     }
/*  484 */     if ((o instanceof PIGData)) {
/*  485 */       ComparableArray arr = (ComparableArray)((PIGData)o).getElement(i);
/*  486 */       StringBuffer sb = new StringBuffer();
/*  487 */       for (int i1 = 0; i1 < arr.size(); i1++) {
/*  488 */         sb.append(arr.get(i1));
/*      */       }
/*  490 */       return sb.toString();
/*      */     }
/*  492 */     if ((o instanceof double[])) {
/*  493 */       return Format.sprintf("%7.3f ", new Double[] { Double.valueOf(((double[])o)[i]) });
/*      */     }
/*      */     
/*  496 */     return "-";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reverse()
/*      */   {
/*  504 */     this.maf.reverse();
/*  505 */     Collections.reverse(this.loc);
/*  506 */     for (int i = 0; i < this.loc.size(); i++) {
/*  507 */       this.loc.set(i, Integer.valueOf(-1 * ((Integer)this.loc.get(i)).intValue()));
/*      */     }
/*  509 */     Collections.reverse(this.snpid);
/*  510 */     for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/*  511 */       PIGData nxt = (PIGData)it.next();
/*  512 */       nxt.reverse();
/*      */     }
/*  514 */     for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext();) {
/*  515 */       EmissionState nxt = (EmissionState)it.next();
/*  516 */       nxt.reverse();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[][] getSourcePositions()
/*      */   {
/*  525 */     int[] res = new int[this.loc.size()];
/*  526 */     for (int i = 0; i < res.length; i++) {
/*  527 */       res[i] = i;
/*      */     }
/*  529 */     return new int[][] { res };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void mix()
/*      */   {
/*  537 */     for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/*  538 */       ((PIGData)it.next()).mix();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] readIndiv(File f)
/*      */     throws Exception
/*      */   {
/*  557 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  558 */     List<String> l = new ArrayList();
/*  559 */     String st = "";
/*  560 */     while ((st = br.readLine()) != null) {
/*  561 */       l.add(st);
/*      */     }
/*  563 */     return (String[])l.toArray(new String[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public EmissionState makeMafState(EmissionStateSpace emStSp1)
/*      */   {
/*  572 */     if ((emStSp1 instanceof CompoundEmissionStateSpace)) {
/*  573 */       CompoundEmissionStateSpace emStSp = (CompoundEmissionStateSpace)emStSp1;
/*  574 */       EmissionStateSpace[] ems = emStSp.getMembers();
/*  575 */       EmissionState[] st = new EmissionState[ems.length];
/*  576 */       for (int i = 0; i < st.length; i++) {
/*  577 */         st[i] = new HaplotypeEmissionState("maf_" + i, this.length.intValue(), ems[i].size(), ems[i], null, null, 1.0D);
/*      */       }
/*      */       
/*  580 */       this.maf = new AlleleCopyPairEmissionState(Arrays.asList(st), emStSp, false, null);
/*      */     }
/*      */     else {
/*  583 */       return new HaplotypeEmissionState("maf_", this.length.intValue(), emStSp1.size(), emStSp1, null, null, 1.0D);
/*      */     }
/*  585 */     this.maf.initialiseCounts();
/*  586 */     return this.maf;
/*      */   }
/*      */   
/*      */   public static void main(String[] args) {
/*  590 */     String input_f = args[0];
/*  591 */     String output_f = args[1];
/*  592 */     String input_file = args[2];
/*  593 */     String output_file = args[3];
/*      */   }
/*      */   
/*      */   public abstract DataC clone();
/*      */   
/*      */   public static List<Integer> readPosInfo(File f, int index, boolean header)
/*      */     throws Exception
/*      */   {
/*  601 */     List<Integer> res = new ArrayList();
/*  602 */     readPosInfo(f, new int[] { index }, header, new List[] { res }, new Class[] { Integer.class });
/*  603 */     return res;
/*      */   }
/*      */   
/*  606 */   public static BufferedReader getBufferedReader(File f) throws Exception { if ((f.exists()) && (f.length() > 0L)) {
/*  607 */       return new BufferedReader(new FileReader(f));
/*      */     }
/*      */     
/*  610 */     File f1 = new File(f.getAbsolutePath() + ".gz");
/*  611 */     if (f1.exists())
/*  612 */       return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(f1))));
/*  613 */     return null;
/*      */   }
/*      */   
/*      */   public static void readPosInfo(File f, int[] index, boolean header, List[] res, Class[] cl)
/*      */     throws Exception
/*      */   {
/*  619 */     readPosInfo(getBufferedReader(f), index, header, res, cl);
/*      */   }
/*      */   
/*      */ 
/*  623 */   public static void readPosInfo(BufferedReader br, int[] index, boolean header, List[] res, Class[] cl) throws Exception { readPosInfo(br, index, header, res, cl, "\\s+"); }
/*      */   
/*      */   public static void readPosInfo(BufferedReader br, int[] index, boolean header, List[] res, Class[] cl, String spl) throws Exception {
/*  626 */     if (header) br.readLine();
/*  627 */     String st = "";
/*      */     int i;
/*  629 */     for (; (st = br.readLine()) != null; 
/*      */         
/*      */ 
/*      */ 
/*  633 */         i < index.length)
/*      */     {
/*  630 */       String st1 = st.trim();
/*  631 */       String[] str = st1.split(spl);
/*      */       
/*  633 */       i = 0; continue;
/*  634 */       if (str.length > index[i]) {
/*      */         try {
/*  636 */           if (cl[i].equals(String.class)) {
/*  637 */             res[i].add(str[index[i]]);
/*      */           }
/*      */           else {
/*  640 */             res[i].add(
/*  641 */               cl[i].getConstructor(new Class[] { String.class }).newInstance(new Object[] { str[index[i]] }));
/*      */           }
/*      */         } catch (Exception exc) {
/*  644 */           System.err.println(Arrays.asList(str));
/*  645 */           exc.printStackTrace();
/*  646 */           System.exit(0);
/*      */         }
/*      */       }
/*  633 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  651 */     br.close();
/*      */   }
/*      */   
/*      */   public static SimpleDataCollection getMarchiniFile(File parent, int i, boolean trio, boolean answer) { try { File[] f;
/*      */       File[] f;
/*  656 */       if (answer) {
/*  657 */         f = new File[] { new File(parent, "ans.haps." + i) };
/*      */       } else { File[] f;
/*  659 */         if (trio) {
/*  660 */           f = new File[] { new File(parent, "pgenos.haps." + i), new File(parent, "cgenos.haps." + i) };
/*      */         }
/*      */         else
/*  663 */           f = new File[] { new File(parent, "pgenos.haps." + i) };
/*      */       }
/*  665 */       SimpleDataCollection coll = readMarchiniFormat(f, new File(parent, "posinfo." + i));
/*  666 */       coll.calculateMaf(false);
/*  667 */       if (answer) coll.setPedigree(null);
/*  668 */       return coll;
/*      */     } catch (Exception exc) {
/*  670 */       exc.printStackTrace(); }
/*  671 */     return null;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public static void download(String address, String localFileName)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_2
/*      */     //   2: aconst_null
/*      */     //   3: astore_3
/*      */     //   4: aconst_null
/*      */     //   5: astore 4
/*      */     //   7: new 852	java/net/URL
/*      */     //   10: dup
/*      */     //   11: aload_0
/*      */     //   12: invokespecial 854	java/net/URL:<init>	(Ljava/lang/String;)V
/*      */     //   15: astore 5
/*      */     //   17: new 855	java/io/BufferedOutputStream
/*      */     //   20: dup
/*      */     //   21: new 857	java/io/FileOutputStream
/*      */     //   24: dup
/*      */     //   25: aload_1
/*      */     //   26: invokespecial 859	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
/*      */     //   29: invokespecial 860	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
/*      */     //   32: astore_2
/*      */     //   33: aload 5
/*      */     //   35: invokevirtual 863	java/net/URL:openConnection	()Ljava/net/URLConnection;
/*      */     //   38: astore_3
/*      */     //   39: aload_3
/*      */     //   40: invokevirtual 867	java/net/URLConnection:getInputStream	()Ljava/io/InputStream;
/*      */     //   43: astore 4
/*      */     //   45: sipush 1024
/*      */     //   48: newarray <illegal type>
/*      */     //   50: astore 6
/*      */     //   52: lconst_0
/*      */     //   53: lstore 8
/*      */     //   55: goto +20 -> 75
/*      */     //   58: aload_2
/*      */     //   59: aload 6
/*      */     //   61: iconst_0
/*      */     //   62: iload 7
/*      */     //   64: invokevirtual 872	java/io/OutputStream:write	([BII)V
/*      */     //   67: lload 8
/*      */     //   69: iload 7
/*      */     //   71: i2l
/*      */     //   72: ladd
/*      */     //   73: lstore 8
/*      */     //   75: aload 4
/*      */     //   77: aload 6
/*      */     //   79: invokevirtual 878	java/io/InputStream:read	([B)I
/*      */     //   82: dup
/*      */     //   83: istore 7
/*      */     //   85: iconst_m1
/*      */     //   86: if_icmpne -28 -> 58
/*      */     //   89: getstatic 884	java/lang/System:out	Ljava/io/PrintStream;
/*      */     //   92: new 144	java/lang/StringBuilder
/*      */     //   95: dup
/*      */     //   96: aload_1
/*      */     //   97: invokestatic 588	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   100: invokespecial 148	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   103: ldc_w 591
/*      */     //   106: invokevirtual 151	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   109: lload 8
/*      */     //   111: invokevirtual 887	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
/*      */     //   114: invokevirtual 155	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   117: invokevirtual 527	java/io/PrintStream:println	(Ljava/lang/String;)V
/*      */     //   120: goto +41 -> 161
/*      */     //   123: astore 5
/*      */     //   125: aload 5
/*      */     //   127: invokevirtual 805	java/lang/Exception:printStackTrace	()V
/*      */     //   130: goto +57 -> 187
/*      */     //   133: astore 10
/*      */     //   135: aload 4
/*      */     //   137: ifnull +8 -> 145
/*      */     //   140: aload 4
/*      */     //   142: invokevirtual 890	java/io/InputStream:close	()V
/*      */     //   145: aload_2
/*      */     //   146: ifnull +12 -> 158
/*      */     //   149: aload_2
/*      */     //   150: invokevirtual 891	java/io/OutputStream:close	()V
/*      */     //   153: goto +5 -> 158
/*      */     //   156: astore 11
/*      */     //   158: aload 10
/*      */     //   160: athrow
/*      */     //   161: aload 4
/*      */     //   163: ifnull +8 -> 171
/*      */     //   166: aload 4
/*      */     //   168: invokevirtual 890	java/io/InputStream:close	()V
/*      */     //   171: aload_2
/*      */     //   172: ifnull +38 -> 210
/*      */     //   175: aload_2
/*      */     //   176: invokevirtual 891	java/io/OutputStream:close	()V
/*      */     //   179: goto +31 -> 210
/*      */     //   182: astore 11
/*      */     //   184: goto +26 -> 210
/*      */     //   187: aload 4
/*      */     //   189: ifnull +8 -> 197
/*      */     //   192: aload 4
/*      */     //   194: invokevirtual 890	java/io/InputStream:close	()V
/*      */     //   197: aload_2
/*      */     //   198: ifnull +12 -> 210
/*      */     //   201: aload_2
/*      */     //   202: invokevirtual 891	java/io/OutputStream:close	()V
/*      */     //   205: goto +5 -> 210
/*      */     //   208: astore 11
/*      */     //   210: return
/*      */     // Line number table:
/*      */     //   Java source line #676	-> byte code offset #0
/*      */     //   Java source line #677	-> byte code offset #2
/*      */     //   Java source line #678	-> byte code offset #4
/*      */     //   Java source line #680	-> byte code offset #7
/*      */     //   Java source line #681	-> byte code offset #17
/*      */     //   Java source line #682	-> byte code offset #21
/*      */     //   Java source line #681	-> byte code offset #29
/*      */     //   Java source line #683	-> byte code offset #33
/*      */     //   Java source line #684	-> byte code offset #39
/*      */     //   Java source line #685	-> byte code offset #45
/*      */     //   Java source line #687	-> byte code offset #52
/*      */     //   Java source line #688	-> byte code offset #55
/*      */     //   Java source line #689	-> byte code offset #58
/*      */     //   Java source line #690	-> byte code offset #67
/*      */     //   Java source line #688	-> byte code offset #75
/*      */     //   Java source line #692	-> byte code offset #89
/*      */     //   Java source line #693	-> byte code offset #123
/*      */     //   Java source line #694	-> byte code offset #125
/*      */     //   Java source line #695	-> byte code offset #133
/*      */     //   Java source line #697	-> byte code offset #135
/*      */     //   Java source line #698	-> byte code offset #140
/*      */     //   Java source line #700	-> byte code offset #145
/*      */     //   Java source line #701	-> byte code offset #149
/*      */     //   Java source line #703	-> byte code offset #156
/*      */     //   Java source line #705	-> byte code offset #158
/*      */     //   Java source line #697	-> byte code offset #161
/*      */     //   Java source line #698	-> byte code offset #166
/*      */     //   Java source line #700	-> byte code offset #171
/*      */     //   Java source line #701	-> byte code offset #175
/*      */     //   Java source line #703	-> byte code offset #182
/*      */     //   Java source line #705	-> byte code offset #184
/*      */     //   Java source line #697	-> byte code offset #187
/*      */     //   Java source line #698	-> byte code offset #192
/*      */     //   Java source line #700	-> byte code offset #197
/*      */     //   Java source line #701	-> byte code offset #201
/*      */     //   Java source line #703	-> byte code offset #208
/*      */     //   Java source line #706	-> byte code offset #210
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	211	0	address	String
/*      */     //   0	211	1	localFileName	String
/*      */     //   1	201	2	out	java.io.OutputStream
/*      */     //   3	37	3	conn	java.net.URLConnection
/*      */     //   5	188	4	in	InputStream
/*      */     //   15	19	5	url	java.net.URL
/*      */     //   123	3	5	exception	Exception
/*      */     //   50	28	6	buffer	byte[]
/*      */     //   58	12	7	numRead	int
/*      */     //   83	3	7	numRead	int
/*      */     //   53	57	8	numWritten	long
/*      */     //   133	26	10	localObject	Object
/*      */     //   156	1	11	localIOException	java.io.IOException
/*      */     //   182	1	11	localIOException1	java.io.IOException
/*      */     //   208	1	11	localIOException2	java.io.IOException
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	120	123	java/lang/Exception
/*      */     //   7	133	133	finally
/*      */     //   135	153	156	java/io/IOException
/*      */     //   161	179	182	java/io/IOException
/*      */     //   187	205	208	java/io/IOException
/*      */   }
/*      */   
/*      */   public boolean indetermined(int i)
/*      */   {
/*  708 */     char allA = ((Character)this.majorAllele.get(i)).charValue();
/*  709 */     char allB = ((Character)this.minorAllele.get(i)).charValue();
/*  710 */     boolean indetermined = false;
/*  711 */     if ((allA == 'A') || (allA == 'T')) {
/*  712 */       if ((allB != 'C') && (allB != 'G'))
/*      */       {
/*      */ 
/*  715 */         indetermined = true;
/*      */       }
/*  717 */     } else if ((allB == 'A') || (allB == 'T')) {
/*  718 */       if ((allA == 'C') || (allA == 'G')) {
/*  719 */         throw new RuntimeException("should be switched");
/*      */       }
/*      */       
/*  722 */       indetermined = true;
/*      */     }
/*  724 */     else if ((allA == 'C') || (allA == 'G')) {
/*  725 */       if ((allB == 'A') || (allB == 'T')) {
/*  726 */         throw new RuntimeException("should be switched");
/*      */       }
/*      */       
/*  729 */       indetermined = true;
/*      */     }
/*  731 */     else if ((allB == 'C') || (allB == 'G')) {
/*  732 */       if ((allA != 'A') && (allA != 'T'))
/*      */       {
/*      */ 
/*  735 */         indetermined = true; }
/*      */     } else {
/*  737 */       throw new RuntimeException("!! unknown " + allA + " " + allB); }
/*  738 */     return indetermined;
/*      */   }
/*      */   
/*      */   public List<Integer> convertToTopBottom() {
/*  742 */     List<Integer> l = new ArrayList();
/*  743 */     for (int i = 0; i < this.loc.size(); i++) {
/*  744 */       char allA = ((Character)this.majorAllele.get(i)).charValue();
/*  745 */       char allB = ((Character)this.minorAllele.get(i)).charValue();
/*  746 */       boolean indetermined = false;
/*  747 */       if ((allA == 'A') || (allA == 'T')) {
/*  748 */         if ((allB != 'C') && (allB != 'G'))
/*      */         {
/*      */ 
/*  751 */           indetermined = true;
/*      */         }
/*  753 */       } else if ((allB == 'A') || (allB == 'T')) {
/*  754 */         if ((allA == 'C') || (allA == 'G')) {
/*  755 */           switchAlleles(i);
/*      */         } else {
/*  757 */           indetermined = true;
/*      */         }
/*  759 */       } else if ((allA == 'C') || (allA == 'G')) {
/*  760 */         if ((allB == 'A') || (allB == 'T')) {
/*  761 */           switchAlleles(i);
/*      */         } else {
/*  763 */           indetermined = true;
/*      */         }
/*  765 */       } else if ((allB == 'C') || (allB == 'G')) {
/*  766 */         if ((allA != 'A') && (allA != 'T'))
/*      */         {
/*      */ 
/*  769 */           indetermined = true; }
/*      */       } else
/*  771 */         throw new RuntimeException("unknown");
/*  772 */       if (indetermined) {
/*  773 */         l.add(Integer.valueOf(i));
/*  774 */         System.err.println("indetermined at " + (String)this.snpid.get(i) + " " + allA + " " + allB + " " + this.name);
/*      */       }
/*      */     }
/*  777 */     return l;
/*      */   }
/*      */   
/*      */   public static DataCollection readHapMapPhasedFormat(File dir, String prefix, double soften, double probPair, int[] mid, int[] kb) throws Exception
/*      */   {
/*  782 */     boolean trio = true;
/*  783 */     File[] f = {
/*  784 */       new File(dir, prefix + "legend.txt"), 
/*  785 */       new File(dir, prefix + "phased"), 
/*  786 */       new File(dir, prefix + "sample.txt") };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  802 */     List<Integer> loc1 = new ArrayList();
/*  803 */     List<String> snps = new ArrayList();
/*  804 */     List<String> major = new ArrayList();
/*  805 */     List<String> minor = new ArrayList();
/*  806 */     SimpleDataCollection.readPosInfo(f[0], new int[] { 0, 1, 2, 3 }, true, new List[] { snps, loc1, major, minor }, new Class[] { String.class, Integer.class, String.class, String.class });
/*      */     
/*  808 */     List<String> ids = new ArrayList();
/*  809 */     SimpleDataCollection.readPosInfo(new File(dir, prefix + "sample.txt"), new int[1], false, new List[] { ids }, new Class[] { String.class });
/*  810 */     if (trio) {
/*  811 */       int noFam = ids.size();
/*  812 */       int noParents = (int)Math.round(noFam * 0.6666666666666666D);
/*  813 */       if (noParents * 1.5D != noFam) throw new RuntimeException("!!");
/*  814 */       ids = ids.subList(0, noParents);
/*      */     }
/*      */     int end;
/*  817 */     if ((Constants.mid()[0] > 0) || (Constants.mid().length > 1)) { int mid_index1;
/*      */       int mid_index;
/*      */       int mid_index1;
/*  820 */       if (kb.length > 1) {
/*  821 */         int start_index = mid[0] - kb[0] * 1000;
/*  822 */         int mid_index = firstGreaterThan(loc1, start_index);
/*  823 */         mid_index1 = firstGreaterThan(loc1, mid[1] + kb[1] * 1000);
/*      */       }
/*      */       else {
/*  826 */         mid_index = firstGreaterThan(loc1, mid[0]) - Constants.restrict()[0];
/*  827 */         mid_index1 = firstGreaterThan(loc1, mid[1]) + Constants.restrict()[1];
/*      */       }
/*  829 */       int read = mid_index;
/*  830 */       end = mid_index1;
/*      */     }
/*      */     else {
/*  833 */       read = IlluminaRDataCollection.firstGreaterThan(loc1, Constants.offset());
/*  834 */       end = Math.min(getEndIndex(loc1), read + Constants.restrict()[0]);
/*      */     }
/*      */     
/*  837 */     int read = Math.max(0, read);
/*  838 */     int end = Math.min(end, loc1.size());
/*  839 */     DataCollection sdc = new LikelihoodDataCollection();
/*  840 */     sdc.name = "hapmap";
/*  841 */     sdc.loc = new ArrayList((Collection)loc1.subList(read, end));
/*  842 */     sdc.snpid = new ArrayList((Collection)snps.subList(read, end));
/*  843 */     sdc.majorAllele = new ArrayList();
/*  844 */     sdc.minorAllele = new ArrayList();
/*  845 */     for (int i = read; i < end; i++) {
/*  846 */       if (((Integer)loc1.get(i)).intValue() != ((Integer)sdc.loc.get(i - read)).intValue()) throw new RuntimeException("!!");
/*  847 */       char allA = ((String)major.get(i)).charAt(0);
/*  848 */       char allB = ((String)minor.get(i)).charAt(0);
/*  849 */       sdc.majorAllele.add(Character.valueOf(allA));
/*  850 */       sdc.minorAllele.add(Character.valueOf(allB));
/*      */     }
/*  852 */     System.err.println(sdc.snpid);
/*  853 */     System.err.println(sdc.loc);
/*  854 */     System.err.println(sdc.majorAllele);
/*  855 */     System.err.println(sdc.minorAllele);
/*  856 */     BufferedReader br = getBufferedReader(f[1]);
/*  857 */     String st = "";String st1 = "";
/*  858 */     sdc.length = Integer.valueOf(sdc.loc.size());
/*  859 */     int i = 0;
/*      */     
/*  861 */     for (i = 0; (st = br.readLine()) != null; i++) {
/*  862 */       String subst = st.replaceAll("\\s+", "").substring(read, end).replace('0', 'A').replace('1', 'B');
/*  863 */       String subst1 = br.readLine().replaceAll("\\s+", "").substring(read, end).replace('0', 'A').replace('1', 'B');
/*  864 */       EmissionStateSpace emStSp = Emiss.getEmissionStateSpace(1);
/*  865 */       if ((subst.length() != sdc.length()) || (subst1.length() != sdc.length())) throw new RuntimeException("!!");
/*  866 */       if (probPair > 0.0D)
/*      */       {
/*  868 */         PIGData dat = SimpleScorableObject.make((String)ids.get(i), Arrays.asList(new String[] { subst, subst1 }), emStSp);
/*  869 */         if (dat.length() != sdc.length()) throw new RuntimeException("!!");
/*  870 */         sdc.data.put(dat.getName(), dat);
/*      */       }
/*      */       else {
/*  873 */         PIGData dat = SimpleScorableObject.make((String)ids.get(i) + "_" + 0, Arrays.asList(new String[] { subst }), emStSp);
/*  874 */         if (dat.length() != sdc.length()) throw new RuntimeException("!!");
/*  875 */         sdc.data.put(dat.getName(), dat);
/*  876 */         PIGData dat1 = SimpleScorableObject.make((String)ids.get(i) + "_" + 1, Arrays.asList(new String[] { subst1 }), emStSp);
/*  877 */         if (dat1.length() != sdc.length()) throw new RuntimeException("!!");
/*  878 */         sdc.data.put(dat1.getName(), dat1);
/*      */       }
/*      */     }
/*  881 */     if (probPair < 1.0D) { sdc.split(probPair, true);
/*      */     }
/*  883 */     sdc.calculateMaf(false);
/*  884 */     if (Constants.topBottom()) sdc.convertToTopBottom();
/*  885 */     return sdc;
/*      */   }
/*      */   
/*  888 */   public DataCollection[] splitOnSize() { EmissionStateSpace[] m = getNoCopies();
/*  889 */     DataCollection[] coll = new DataCollection[m.length];
/*  890 */     for (int i = 0; i < coll.length; i++) {
/*  891 */       coll[i] = new LikelihoodDataCollection();
/*  892 */       coll[i].loc = new ArrayList(this.loc);
/*  893 */       coll[i].snpid = new ArrayList(this.snpid);
/*  894 */       coll[i].majorAllele = new ArrayList(this.majorAllele);
/*  895 */       coll[i].minorAllele = new ArrayList(this.minorAllele);
/*  896 */       coll[i].length = this.length;
/*      */     }
/*  898 */     for (Iterator<String> it = getKeyIterator(); it.hasNext();) {
/*  899 */       String key = (String)it.next();
/*  900 */       PIGData dat = get(key);
/*  901 */       EmissionState emst = getL(key);
/*  902 */       int no_cop = dat.noCopies();
/*  903 */       coll[(no_cop - 1)].data.put(key, dat);
/*  904 */       coll[(no_cop - 1)].dataL.put(key, emst);
/*      */     }
/*  906 */     for (int i = 0; i < coll.length; i++) {
/*  907 */       coll[i].calculateMaf(false);
/*      */     }
/*  909 */     return coll;
/*      */   }
/*      */   
/*      */ 
/*      */   public void restricToAlias(Collection<String> alias)
/*      */   {
/*  915 */     List<String> keys = new ArrayList(getKeys());
/*  916 */     for (Iterator<String> it = keys.iterator(); it.hasNext();) {
/*  917 */       String key = (String)it.next();
/*  918 */       if (!alias.contains(key)) {
/*  919 */         this.dataL.remove(key);
/*  920 */         this.recSites.remove(key);
/*  921 */         this.viterbi.remove(key);
/*  922 */         this.data.remove(key);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public DataCollection[] splitRandom() {
/*  928 */     DataCollection[] coll = new DataCollection[2];
/*  929 */     for (int i = 0; i < coll.length; i++) {
/*  930 */       coll[i] = new LikelihoodDataCollection();
/*  931 */       coll[i].loc = new ArrayList(this.loc);
/*  932 */       coll[i].snpid = new ArrayList(this.snpid);
/*  933 */       coll[i].majorAllele = new ArrayList(this.majorAllele);
/*  934 */       coll[i].minorAllele = new ArrayList(this.minorAllele);
/*  935 */       coll[i].length = this.length;
/*      */     }
/*  937 */     for (Iterator<String> it = getKeyIterator(); it.hasNext();) {
/*  938 */       String key = (String)it.next();
/*  939 */       PIGData dat = get(key);
/*  940 */       EmissionState emst = getL(key);
/*  941 */       int no_cop = Constants.rand.nextBoolean() ? 1 : 2;
/*  942 */       coll[(no_cop - 1)].data.put(key, dat);
/*  943 */       coll[(no_cop - 1)].dataL.put(key, emst);
/*      */     }
/*  945 */     for (int i = 0; i < coll.length; i++) {
/*  946 */       coll[i].calculateMaf(false);
/*      */     }
/*  948 */     return coll;
/*      */   }
/*      */   
/*      */   public static SimpleDataCollection readMarchiniFormat(File[] f, File posF) throws Exception
/*      */   {
/*  953 */     List<Integer> locs = readPosInfo(posF, 2, false);
/*  954 */     int noSites = Math.min(Constants.restrict()[0], locs.size());
/*  955 */     SimpleDataCollection coll = new SimpleDataCollection(noSites);
/*  956 */     coll.loc = locs.subList(0, noSites);
/*      */     
/*  958 */     for (int ik = 0; ik < f.length; ik++) {
/*  959 */       BufferedReader br = new BufferedReader(new FileReader(f[ik]));
/*  960 */       String st = "";
/*      */       
/*  962 */       EmissionStateSpace emStSp = Emiss.getEmissionStateSpace(1);
/*  963 */       while ((st = br.readLine()) != null) {
/*  964 */         if (st.length() == 0) break;
/*  965 */         String[] str1 = st.split("\\s+");
/*  966 */         String[] str2 = br.readLine().split("\\s+");
/*  967 */         PIGData dat1 = SimpleScorableObject.make(str1[3].replace(':', '_'), noSites, emStSp);
/*  968 */         for (int j = 0; j < noSites; j++) {
/*  969 */           Comparable c1 = translate(str1[0].charAt(j), true);
/*  970 */           Comparable c2 = translate(str2[0].charAt(j), false);
/*      */           
/*  972 */           dat1.addPoint(j, ComparableArray.make(c1, c2));
/*      */         }
/*      */         
/*  975 */         coll.data.put(dat1.getName(), dat1);
/*      */       }
/*  977 */       br.close();
/*      */     }
/*  979 */     Map<String, String> mother = new TreeMap();
/*  980 */     Map<String, String> father = new TreeMap();
/*  981 */     for (int i = 1;; i++) {
/*  982 */       String child_name = "FAM" + i + "_CHILD";
/*  983 */       if (!coll.data.containsKey(child_name)) {
/*      */         break;
/*      */       }
/*  986 */       String moth_name = "FAM" + i + "_MOTH";
/*  987 */       String fath_name = "FAM" + i + "_FATH";
/*  988 */       mother.put(child_name, moth_name);
/*  989 */       father.put(child_name, fath_name);
/*      */     }
/*  991 */     coll.setPedigree(new PedigreeDataCollection(mother, father));
/*  992 */     return coll;
/*      */   }
/*      */   
/*      */   private static Comparable translate(char c, boolean first) {
/*  996 */     if (c == '0') return Emiss.N();
/*  997 */     if (c == '1') return Emiss.b();
/*  998 */     if (c == '2') return Emiss.a();
/*  999 */     if (c == '9') {
/* 1000 */       if (first) return Emiss.a();
/* 1001 */       return Emiss.b();
/*      */     }
/* 1003 */     throw new RuntimeException("!!");
/*      */   }
/*      */   
/*      */   public static SimpleDataCollection readDickFormat(File f) throws Exception {
/* 1007 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 1008 */     String[] st = br.readLine().split("\\s+");
/* 1009 */     int noIndiv = Integer.parseInt(st[1]);
/* 1010 */     int noSites = Math.min(Constants.restrict()[0], Integer.parseInt(st[2]));
/* 1011 */     SimpleDataCollection coll = new SimpleDataCollection(noSites);
/* 1012 */     Character[] majorAllelle = new Character[noSites];
/* 1013 */     Character[] minorAllelle = new Character[noSites];
/* 1014 */     st = br.readLine().split("\\s+");
/* 1015 */     for (int i = 1; i <= noSites; i++) {
/* 1016 */       coll.loc.add(Integer.valueOf(Integer.parseInt(st[i])));
/*      */     }
/* 1018 */     st = br.readLine().split("\\s+");
/* 1019 */     for (int i = 1; i <= noSites; i++) {
/* 1020 */       coll.snpid.add(st[i]);
/*      */     }
/* 1022 */     for (int i = st.length; i < noSites; i++) {
/* 1023 */       coll.snpid.add("-");
/*      */     }
/* 1025 */     for (int i = 0; i < noIndiv; i++) {
/* 1026 */       String[] str = br.readLine().split("\\s+");
/* 1027 */       EmissionStateSpace emStSp = Emiss.getEmissionStateSpace(1);
/*      */       
/* 1029 */       PIGData dat = SimpleScorableObject.make(str[0], noSites, emStSp);
/* 1030 */       for (int j = 0; j < noSites; j++) {
/* 1031 */         char c = str[(j + 1)].charAt(0);
/* 1032 */         if ((c == '?') || (c == '-')) { dat.addPoint(j, ComparableArray.make(Emiss.N()));
/*      */         }
/* 1034 */         else if (majorAllelle[j] == null) {
/* 1035 */           majorAllelle[j] = new Character(c);
/* 1036 */           dat.addPoint(j, ComparableArray.make(Emiss.a()));
/*      */         }
/* 1038 */         else if ((minorAllelle[j] == null) && (c != majorAllelle[j].charValue())) {
/* 1039 */           minorAllelle[j] = Character.valueOf(c);
/* 1040 */           dat.addPoint(j, ComparableArray.make(Emiss.b()));
/*      */         }
/*      */         else {
/* 1043 */           dat.addPoint(j, ComparableArray.make(c == majorAllelle[j].charValue() ? Emiss.a() : Emiss.b()));
/*      */         }
/*      */       }
/*      */       
/* 1047 */       coll.data.put(dat.getName(), dat);
/*      */     }
/*      */     
/* 1050 */     return coll;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int noAllelles()
/*      */   {
/* 1057 */     int res = 0;
/* 1058 */     for (Iterator<String> it = this.data.keySet().iterator(); it.hasNext();) {
/* 1059 */       res += noCopies((String)it.next());
/*      */     }
/* 1061 */     return res;
/*      */   }
/*      */   
/* 1064 */   public void switchAlleles(int i) { Character maj = (Character)this.majorAllele.get(i);
/* 1065 */     Character min = (Character)this.minorAllele.get(i);
/* 1066 */     this.majorAllele.set(i, min);
/* 1067 */     this.minorAllele.set(i, maj);
/* 1068 */     this.maf.switchAlleles(i);
/* 1069 */     for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/* 1070 */       ((PIGData)it.next()).switchAlleles(i);
/*      */     }
/* 1072 */     for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext();) {
/* 1073 */       ((EmissionState)it.next()).switchAlleles(i);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void extractFromTrioData()
/*      */   {
/* 1082 */     Map<String, PIGData> l = new HashMap();
/* 1083 */     Map<String, EmissionState> data_l = new HashMap();
/* 1084 */     Map<String, double[]> unc = new HashMap();
/* 1085 */     for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/* 1086 */       PIGData dat_i = (PIGData)it.next();
/* 1087 */       EmissionState dat_i1 = (EmissionState)this.dataL.get(dat_i.getName());
/* 1088 */       if (((ComparableArray)dat_i.getElement(0)).isNested()) {
/* 1089 */         PIGData[] data_i = dat_i.split();
/* 1090 */         EmissionState[] data_il = dat_i1 == null ? null : dat_i1.split();
/* 1091 */         for (int j = 0; j < data_i.length; j++) {
/* 1092 */           l.put(data_i[j].getName(), data_i[j]);
/* 1093 */           if (dat_i1 != null) { data_l.put(data_il[j].getName(), data_il[j]);
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1100 */         l.put(dat_i.getName(), dat_i);
/* 1101 */         if (dat_i1 != null) { data_l.put(dat_i1.getName(), dat_i1);
/*      */         }
/*      */       }
/*      */     }
/* 1105 */     this.data = l;
/* 1106 */     this.dataL = data_l;
/*      */   }
/*      */   
/*      */ 
/*      */   public void calculateMaf(boolean state)
/*      */   {
/* 1112 */     if (!state) calculateMaf(state, true); else
/* 1113 */       calculateMaf(state, false);
/*      */   }
/*      */   
/* 1116 */   public Phenotypes pheno() { return this.pheno; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Double scoreChi(int i)
/*      */   {
/* 1123 */     EmissionStateSpace emStSp = this.maf.getEmissionStateSpace();
/* 1124 */     CompoundEmissionStateSpace emStSp1 = (CompoundEmissionStateSpace)getEmStSpace();
/* 1125 */     double[][] ns = new double[3][emStSp.size()];
/* 1126 */     double[][] nc = new double[3][ns[0].length];
/* 1127 */     for (int k = 0; k < ns.length; k++) {
/* 1128 */       Arrays.fill(ns[i], 0.0D);
/* 1129 */       Arrays.fill(nc[i], 0.0D);
/*      */     }
/* 1131 */     for (Iterator<String> it = getKeys().iterator(); it.hasNext();) {
/* 1132 */       String key = (String)it.next();
/* 1133 */       PhasedDataState dat = (PhasedDataState)this.data.get(key);
/* 1134 */       throw new RuntimeException("!!");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1144 */     Double[] res = scoreChi(ns, nc);
/* 1145 */     ChiSq ch = new ChiSq();
/* 1146 */     return Double.valueOf(ChiSq.chi2prob(1, res[Constants.getMax(res)].doubleValue()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void add(double[][] counts, ComparableArray comp, double prob)
/*      */   {
/* 1155 */     int noCop = comp.noCopies(true);
/* 1156 */     int noB = (int)comp.noB();
/* 1157 */     int noA = noCop - noB;
/* 1158 */     counts[0][noCop] += prob;
/* 1159 */     counts[1][noA] += prob;
/* 1160 */     counts[2][noB] += prob;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Double[] scoreChi1(int i, boolean armitage, boolean useEmSt, int phenIndex)
/*      */   {
/* 1167 */     CompoundEmissionStateSpace emStSp1 = (CompoundEmissionStateSpace)getEmStSpace();
/*      */     
/* 1169 */     if (this.ct == null) {
/* 1170 */       this.ct = new Contigency();
/* 1171 */       this.att = new ArmitageTrendTest();
/* 1172 */       this.ns = new double[3][emStSp1.copyNumber.size()];
/* 1173 */       this.nc = new double[3][emStSp1.copyNumber.size()];
/* 1174 */       this.res = new Double[3];
/*      */     }
/* 1176 */     for (int ik = 0; ik < this.ns.length; ik++) {
/* 1177 */       Arrays.fill(this.ns[ik], 0.0D);Arrays.fill(this.nc[ik], 0.0D);
/*      */     }
/* 1179 */     for (Iterator<String> it = getKeys().iterator(); it.hasNext();) {
/* 1180 */       String key = (String)it.next();
/* 1181 */       PhasedDataState dat = (PhasedDataState)this.data.get(key);
/* 1182 */       int di = dat.emissions[i].getDataIndex();
/* 1183 */       if (di >= 0)
/*      */       {
/* 1185 */         Boolean cse = null;
/* 1186 */         Double phenV = ((EmissionState)this.dataL.get(key)).phenValue()[phenIndex];
/* 1187 */         if (phenV != null) cse = Boolean.valueOf(Math.abs(phenV.doubleValue() - 0.0D) > 0.01D);
/* 1188 */         if (cse != null)
/*      */         {
/* 1190 */           double[][] counts = cse.booleanValue() ? this.ns : this.nc;
/*      */           
/* 1192 */           if (useEmSt) {
/* 1193 */             throw new RuntimeException("!!");
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1211 */           ComparableArray comp = (ComparableArray)dat.getElement(i);
/* 1212 */           add(counts, comp, 1.0D);
/*      */         }
/*      */       }
/*      */     }
/* 1216 */     for (int j = 0; j < this.ns.length; j++) {
/* 1217 */       if (armitage) {
/* 1218 */         this.att.set(this.ns[j], this.nc[j]);
/* 1219 */         this.res[j] = Double.valueOf(this.att.getSig());
/*      */       }
/*      */       else {
/* 1222 */         this.ct.setMatrix(new double[][] { this.ns[j], this.nc[j] });
/* 1223 */         this.res[j] = Double.valueOf(this.ct.getSig());
/*      */       }
/*      */     }
/*      */     
/* 1227 */     return this.res;
/*      */   }
/*      */   
/*      */ 
/*      */   public void calculateMaf(boolean state, boolean exclMissing)
/*      */   {
/* 1233 */     CompoundEmissionStateSpace emStSp = null;
/* 1234 */     if (this.dataL.values().size() > 0) emStSp = (CompoundEmissionStateSpace)((EmissionState)this.dataL.values().iterator().next()).getEmissionStateSpace();
/* 1235 */     if (emStSp == null) {
/* 1236 */       PIGData nxt = (PIGData)this.data.values().iterator().next();
/* 1237 */       if (nxt != null) {
/* 1238 */         emStSp = Emiss.getEmissionStateSpace(nxt.noCopies() - 1);
/*      */       }
/*      */     }
/* 1241 */     EmissionStateSpace emStSp_1 = emStSp.getMembers()[0];
/*      */     
/* 1243 */     this.maf = makeMafState(emStSp_1);
/* 1244 */     for (int i = 0; i < length(); i++) {
/* 1245 */       if (!state) { int[] indices;
/* 1246 */         int k; for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext(); 
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1266 */             k < indices.length)
/*      */         {
/* 1247 */           PhasedDataState nxt = (PhasedDataState)it.next();
/* 1248 */           CompoundEmissionStateSpace emstsp = Emiss.getEmissionStateSpace(nxt.noCopies() - 1);
/* 1249 */           Comparable compa = nxt.getElement(i);
/*      */           
/* 1251 */           if (exclMissing)
/* 1252 */             if ((compa instanceof ComparableArray)) {
/* 1253 */               int cnt = ((ComparableArray)compa).size();
/* 1254 */               int cn = ((ComparableArray)compa).copyNumber();
/* 1255 */               if (cn != cnt)
/*      */                 break;
/* 1257 */               int cntNull = ((ComparableArray)compa).countNull();
/* 1258 */               if (cnt == cntNull)
/*      */                 break;
/*      */             } else {
/* 1261 */               if ((Emiss)compa == Emiss.N())
/*      */                 break;
/*      */             }
/* 1264 */           Integer ind = emstsp.get(compa);
/* 1265 */           indices = emstsp.getMemberIndices(ind.intValue());
/* 1266 */           k = 0; continue;
/* 1267 */           if ((indices[k] != 0) && (indices[k] != 1)) {
/* 1268 */             System.err.println(compa);
/*      */           }
/* 1270 */           this.maf.addCount(indices[k], 1.0D, i);k++;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1275 */         for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext();) {
/* 1276 */           EmissionState nxt = (EmissionState)it.next();
/* 1277 */           CompoundEmissionStateSpace emStsp = (CompoundEmissionStateSpace)nxt.getEmissionStateSpace();
/* 1278 */           Integer fixed = nxt.getFixedInteger(i);
/* 1279 */           if (fixed != null) {
/* 1280 */             int ind = nxt.getFixedInteger(i).intValue();
/*      */             
/* 1282 */             int[] indices = emStsp.getMemberIndices(ind);
/* 1283 */             for (int k = 0; k < indices.length; k++) {
/* 1284 */               this.maf.addCount(indices[k], 1.0D, i);
/*      */             }
/*      */           }
/*      */           else {
/* 1288 */             double[] prob = nxt.getEmiss(i);
/* 1289 */             double sum = Constants.sum(prob);
/* 1290 */             if (Math.abs(1.0D - sum) > 0.001D) {
/* 1291 */               throw new RuntimeException("!! " + i + prob[0] + ":" + prob[1] + ":" + prob[2]);
/*      */             }
/* 1293 */             for (int ind = 0; ind < prob.length; ind++) {
/* 1294 */               int[] indices = ((CompoundEmissionStateSpace)nxt.getEmissionStateSpace()).getMemberIndices(ind);
/* 1295 */               for (int k = 0; k < indices.length; k++)
/*      */               {
/* 1297 */                 this.maf.addCount(indices[k], prob[ind], i);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1306 */     this.maf.transferCountsToProbs(0.001D);
/* 1307 */     this.maf.initialiseCounts();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EmissionState calculateMaf1()
/*      */   {
/* 1315 */     EmissionStateSpace[] m = getNoCopies();
/* 1316 */     EmissionStateSpace emStSp_1 = null;
/* 1317 */     for (int i = 0; i < m.length; i++) {
/* 1318 */       if (m[i] != null) {
/* 1319 */         emStSp_1 = ((CompoundEmissionStateSpace)m[i]).getMembers()[0];
/*      */       }
/*      */     }
/*      */     
/* 1323 */     EmissionState maf = makeMafState(emStSp_1);
/* 1324 */     for (int i = 0; i < length(); i++) {
/* 1325 */       for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/* 1326 */         PhasedDataState nxt = (PhasedDataState)it.next();
/* 1327 */         Comparable compa = nxt.getElement(i);
/* 1328 */         int no_cop = nxt.noCopies() - 1;
/* 1329 */         CompoundEmissionStateSpace emStsp = (CompoundEmissionStateSpace)m[no_cop];
/* 1330 */         Integer ind = emStsp.get(compa);
/* 1331 */         if (ind != null)
/*      */         {
/* 1333 */           int[] indices = emStsp.getMemberIndices(ind.intValue());
/* 1334 */           for (int k = 0; k < indices.length; k++) {
/* 1335 */             maf.addCount(indices[k], 1.0D, i);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1341 */     maf.transferCountsToProbs(0.0D);
/* 1342 */     maf.initialiseCounts();
/* 1343 */     return maf;
/*      */   }
/*      */   
/*      */   static class FastphaseFormatReader {
/*      */     String currentString;
/*      */     BufferedReader br;
/*      */     Class clazz;
/*      */     String st;
/*      */     EmissionStateSpace emStSp;
/* 1352 */     SimpleDataCollection coll = new SimpleDataCollection();
/* 1353 */     int i = 0;
/*      */     
/* 1355 */     FastphaseFormatReader(BufferedReader br, Class clazz, EmissionStateSpace emStSp) throws Exception { this.br = br;
/* 1356 */       this.clazz = clazz;
/* 1357 */       this.emStSp = emStSp;
/*      */     }
/*      */     
/*      */     public PIGData readSingleFastPhaseLine(String name, EmissionStateSpace em) throws Exception {
/* 1361 */       int noCopies = em.noCopies();
/* 1362 */       List<String> dat1 = new ArrayList();
/* 1363 */       EmissionStateSpace emStSp = Emiss.getEmissionStateSpace(noCopies - 1, 2);
/* 1364 */       EmissionStateSpace emStSp1 = Emiss.getEmissionStateSpace(noCopies - 1);
/* 1365 */       EmissionStateSpaceTranslation trans = new EmissionStateSpaceTranslation(emStSp, emStSp1, true);
/* 1366 */       while ((this.st != null) && (!this.st.startsWith("#")) && (!this.st.startsWith("END")) && (!this.st.startsWith("certainty")) && (!this.st.startsWith(">")))
/*      */       {
/* 1368 */         dat1.add(new String(this.st));
/* 1369 */         this.st = this.br.readLine();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1377 */       int len = ((String)dat1.get(0)).length();
/* 1378 */       PIGData dat = new PhasedDataState(name, ((String)dat1.get(0)).length(), em, (short)-1);
/*      */       
/* 1380 */       for (int i = 0; i < len; i++) {
/* 1381 */         StringBuffer sb = new StringBuffer();
/* 1382 */         for (int j = 0; j < dat1.size(); j++) {
/* 1383 */           char ch = ((String)dat1.get(j)).charAt(i);
/* 1384 */           if ((ch != ' ') && (ch != '-') && (ch != '_')) sb.append(ch);
/* 1385 */           if (j < dat1.size() - 1) sb.append(",");
/*      */         }
/* 1387 */         char[] ch = sb.toString().toCharArray();
/*      */         
/* 1389 */         Integer comp = em.getHapl(new String(ch));
/* 1390 */         ((PhasedDataState)dat).emissions[i] = new IntegerDistribution(comp.intValue());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1397 */       return dat;
/*      */     }
/*      */     
/*      */     public void readLocation() throws Exception {
/* 1401 */       this.coll.loc = new ArrayList();
/* 1402 */       String[] str; int i; for (; ((this.st = this.br.readLine()) != null) && (!this.st.startsWith("#")); 
/*      */           
/* 1404 */           i < str.length)
/*      */       {
/* 1403 */         str = this.st.trim().split("\\s+");
/* 1404 */         i = 0; continue;
/* 1405 */         if (str[i].length() != 0) {
/* 1406 */           this.coll.loc.add(Integer.valueOf(Integer.parseInt(str[i])));
/*      */         }
/* 1404 */         i++;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1409 */       Collections.sort(this.coll.loc);
/* 1410 */       this.st = this.br.readLine();
/*      */     }
/*      */     
/* 1413 */     public void readMultiLine() throws Exception { PIGData dat = null;
/* 1414 */       String[] str = this.st.split("\\s+");
/* 1415 */       String name = "";
/* 1416 */       if (str.length > 2) {
/* 1417 */         name = this.st.split("\\s+")[2];
/*      */       }
/* 1419 */       if (name.indexOf('|') >= 0) name = name.substring(0, name.indexOf('|'));
/* 1420 */       this.st = this.br.readLine();
/* 1421 */       while (this.st.startsWith("#")) {
/* 1422 */         this.st = this.br.readLine();
/*      */       }
/* 1424 */       if (this.st.startsWith(">")) {
/* 1425 */         boolean trCompArray = this.st.endsWith("true");
/* 1426 */         List<PIGData> l = new ArrayList();
/* 1427 */         for (int i = 0; (this.st != null) && (this.st.startsWith(">")); i++) {
/* 1428 */           this.st = this.br.readLine();
/* 1429 */           l.add(readSingleFastPhaseLine(name + i, this.emStSp));
/*      */         }
/* 1431 */         dat = SimpleScorableObject.make((CSOData[])l.toArray(new PIGData[0]), false, "_");
/* 1432 */         if (trCompArray) {
/* 1433 */           dat.mkTrCompArray();
/*      */         }
/*      */       }
/*      */       else {
/* 1437 */         dat = readSingleFastPhaseLine(name, this.emStSp);
/*      */       }
/* 1439 */       this.coll.data.put(name, dat);
/* 1440 */       if ((this.st != null) && (this.st.startsWith("certainty"))) {
/* 1441 */         String st1 = this.br.readLine();
/* 1442 */         String st2 = this.br.readLine();
/* 1443 */         double[] cer = new double[st1.length()];
/* 1444 */         for (int ij = 0; ij < cer.length; ij++) {
/* 1445 */           String string = new String(new char[] { st1.charAt(ij), st2.charAt(ij) });
/* 1446 */           if (string.equals("**")) cer[ij] = 1.0D; else
/* 1447 */             cer[ij] = (Double.parseDouble(string) / 100.0D);
/*      */         }
/* 1449 */         this.st = this.br.readLine();
/*      */       }
/* 1451 */       if ((this.st != null) && (this.st.startsWith("certainty"))) {
/* 1452 */         String st1 = this.br.readLine();
/* 1453 */         String st2 = this.br.readLine();
/* 1454 */         double[] cer = new double[st1.length()];
/* 1455 */         for (int ij = 0; ij < cer.length; ij++) {
/* 1456 */           String string = new String(new char[] { st1.charAt(ij), st2.charAt(ij) });
/* 1457 */           if (string.equals("**")) cer[ij] = 1.0D; else
/* 1458 */             cer[ij] = (Double.parseDouble(string) / 100.0D);
/*      */         }
/* 1460 */         this.coll.uncertaintyPhase.put(name, cer);
/* 1461 */         this.st = this.br.readLine();
/*      */       }
/* 1463 */       this.i += 1;
/*      */     }
/*      */     
/*      */     public void read() throws Exception {
/* 1467 */       while (!(this.st = this.br.readLine()).startsWith("#")) {}
/* 1468 */       if ((this.st != null) && (this.st.startsWith("# locs"))) {
/* 1469 */         readLocation();
/*      */       }
/*      */       do {
/* 1472 */         this.st = this.br.readLine();
/* 1471 */         if (this.st == null) break; } while (!this.st.startsWith("#"));
/*      */       
/*      */ 
/* 1474 */       while ((this.st != null) && (this.st.startsWith("#"))) {
/* 1475 */         readMultiLine();
/*      */       }
/* 1477 */       this.coll.length = Integer.valueOf(((PIGData)this.coll.data.values().iterator().next()).length());
/*      */     }
/*      */   }
/*      */   
/*      */   public static SimpleDataCollection readFastPhaseOutput(BufferedReader br, Class clazz, EmissionStateSpace emStSp) throws Exception
/*      */   {
/* 1483 */     FastphaseFormatReader r = new FastphaseFormatReader(br, clazz, emStSp);
/* 1484 */     r.read();
/* 1485 */     br.close();
/* 1486 */     return r.coll;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   SortedMap<Integer, Integer> getRecSites(double mult, String pos)
/*      */   {
/* 1493 */     SortedMap<Integer, Integer> res = new TreeMap();
/*      */     
/* 1495 */     for (int i = 1; i < length(); i++) {
/* 1496 */       double d = 1.0D - Math.exp(-1 * (((Integer)this.loc.get(i)).intValue() - ((Integer)this.loc.get(i - 1)).intValue()) * mult * Constants.probCrossOverBetweenBP);
/* 1497 */       if (Constants.rand.nextDouble() < d) {
/* 1498 */         res.put(Integer.valueOf(i), Integer.valueOf(1));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1503 */     return res;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void printTrioData(PrintWriter pw)
/*      */   {
/* 1511 */     int i = 0;
/* 1512 */     for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext(); i++) {
/* 1513 */       PIGData dat_i = (PIGData)it.next();
/* 1514 */       if (((ComparableArray)dat_i.getElement(0)).isNested()) {
/* 1515 */         PIGData[] data_i = dat_i.split();
/* 1516 */         int childs = data_i[2].noCopies() == 1 ? 1 : 2;
/* 1517 */         pw.println(i + "\t 1 \t 0 \t 0 \t 1 \t urn \t urn:lsid:dcc.hapmap.org:Sample:" + data_i[1].getName() + ":1");
/* 1518 */         pw.println(i + "\t 2 \t 0 \t 0 \t 2 \t urn \t urn:lsid:dcc.hapmap.org:Sample:" + data_i[0].getName() + ":1");
/* 1519 */         pw.println(i + "\t 3 \t 1 \t 2 \t " + childs + "\t urn \t urn:lsid:dcc.hapmap.org:Sample:" + data_i[2].getName() + ":1");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PIGData get(String st)
/*      */   {
/* 1530 */     PIGData dat = (PIGData)this.data.get(st);
/*      */     
/*      */ 
/* 1533 */     return dat;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1542 */   public Map<String, double[]> uncertaintyVitPhase = new HashMap();
/* 1543 */   public Map<String, double[]> uncertaintyPhase = new HashMap();
/* 1544 */   public Map<String, double[]> uncertaintyState = new HashMap();
/* 1545 */   public Map<String, SortedMap<Integer, Integer>[]> recSites = new HashMap();
/* 1546 */   public Map<String, double[]> noSwitches = new HashMap();
/* 1547 */   public Map<String, PIGData> viterbi = new HashMap();
/*      */   
/*      */   DataCollection(int length)
/*      */   {
/* 1551 */     this.length = Integer.valueOf(length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   DataCollection() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int countSwitches()
/*      */   {
/* 1572 */     int sum = 0;
/* 1573 */     PIGData dat; int i; for (Iterator<PIGData> keys = this.viterbi.values().iterator(); keys.hasNext(); 
/*      */         
/*      */ 
/* 1576 */         i < dat.length())
/*      */     {
/* 1574 */       dat = (PIGData)keys.next();
/* 1575 */       ComparableArray prev = (ComparableArray)dat.getElement(0);
/* 1576 */       i = 1; continue;
/* 1577 */       ComparableArray comp = (ComparableArray)dat.getElement(i);
/* 1578 */       for (int j = 0; j < comp.size(); j++) {
/* 1579 */         if (!comp.get(j).equals(prev.get(j))) {
/* 1580 */           sum++;
/*      */         }
/*      */       }
/* 1583 */       prev = comp;i++;
/*      */     }
/*      */     
/* 1586 */     return sum;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Integer> getBlockBoundaries()
/*      */   {
/* 1615 */     SortedSet<Integer> res = new TreeSet();
/* 1616 */     res.add(Integer.valueOf(0));
/* 1617 */     for (Iterator<String> keys = this.viterbi.keySet().iterator(); keys.hasNext();) {
/* 1618 */       getBlockBoundaries((String)keys.next(), res);
/*      */     }
/* 1620 */     return new ArrayList(res);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void getBlockBoundaries(String key, Set<Integer> res)
/*      */   {
/* 1628 */     PIGData dat = (PIGData)this.viterbi.get(key);
/* 1629 */     Comparable prev = dat.getElement(0);
/* 1630 */     for (int i = 1; i < dat.length(); i++) {
/* 1631 */       Comparable comp = dat.getElement(i);
/* 1632 */       if (!comp.equals(prev)) {
/* 1633 */         res.add(Integer.valueOf(i));
/*      */       }
/* 1635 */       prev = comp;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public EmissionState getL(String st)
/*      */   {
/* 1668 */     EmissionState dat = (EmissionState)this.dataL.get(st);
/* 1669 */     if (dat == null) return null;
/* 1670 */     if (!dat.name.equals(st)) throw new RuntimeException("!!");
/* 1671 */     return dat;
/*      */   }
/*      */   
/*      */   public Map<String, boolean[]> getUncertainPositions() {
/* 1675 */     Map<String, boolean[]> m = new HashMap();
/* 1676 */     for (Iterator<String> it = getKeyIterator(); it.hasNext();) {
/* 1677 */       String key = (String)it.next();
/* 1678 */       m.put(key, getUncertainGenotypePositions(key));
/*      */     }
/* 1680 */     return m;
/*      */   }
/*      */   
/*      */   public boolean[] getUncertainGenotypePositions(String key) {
/* 1684 */     EmissionState emst = dataL(key);
/* 1685 */     boolean[] res = new boolean[emst.length()];
/* 1686 */     for (int i = 0; i < emst.length(); i++) {
/* 1687 */       if (emst.getFixedInteger(i) != null) {
/* 1688 */         res[i] = false;
/*      */       }
/*      */       else
/*      */       {
/* 1692 */         double[] emiss = emst.getEmiss(i);
/* 1693 */         double sum = Constants.sum(emiss);
/* 1694 */         int max = Constants.getMax(emiss);
/* 1695 */         if (emiss[max] > 0.999999D) {
/* 1696 */           res[i] = false;
/*      */         }
/*      */         else {
/* 1699 */           res[i] = true;
/*      */         }
/*      */       }
/*      */     }
/* 1703 */     return res;
/*      */   }
/*      */   
/* 1706 */   protected DataCollection(DataCollection dat) { this.index = dat.index;
/* 1707 */     this.pheno = dat.pheno;
/*      */     
/* 1709 */     this.loc = new ArrayList(dat.loc);
/* 1710 */     this.length = dat.length;
/*      */     
/* 1712 */     this.dataL = new TreeMap();
/* 1713 */     for (Iterator<Map.Entry<String, EmissionState>> it = dat.dataL.entrySet().iterator(); it.hasNext();) {
/* 1714 */       Map.Entry<String, EmissionState> nxt = (Map.Entry)it.next();
/* 1715 */       this.dataL.put((String)nxt.getKey(), (EmissionState)((EmissionState)nxt.getValue()).clone());
/*      */     }
/*      */     
/* 1718 */     this.length = dat.length;
/* 1719 */     this.maf = (dat.maf != null ? (EmissionState)dat.maf.clone() : null);
/*      */     
/*      */ 
/* 1722 */     this.ped = dat.ped;
/* 1723 */     this.snpid = new ArrayList(dat.snpid);
/*      */     
/* 1725 */     this.data = new HashMap();
/* 1726 */     for (Iterator<PIGData> it = dat.data.values().iterator(); it.hasNext();) {
/* 1727 */       PIGData data_i = SimpleScorableObject.make((PIGData)it.next());
/* 1728 */       this.data.put(data_i.getName(), data_i);
/*      */     }
/* 1730 */     this.majorAllele = (dat.majorAllele != null ? new ArrayList(dat.majorAllele) : null);
/* 1731 */     this.minorAllele = (dat.minorAllele != null ? new ArrayList(dat.minorAllele) : null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getFirstIndexAbove(long pos)
/*      */   {
/* 1742 */     for (int i = 0; i < this.loc.size(); i++) {
/* 1743 */       if (((Integer)this.loc.get(i)).intValue() > pos) return i;
/*      */     }
/* 1745 */     return this.loc.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getIndex(Integer name)
/*      */   {
/* 1752 */     return this.loc.indexOf(name);
/*      */   }
/*      */   
/*      */   String getPrintString(int no, String st)
/*      */   {
/* 1757 */     StringBuffer sb = new StringBuffer();
/* 1758 */     for (int i = 0; i < no; i++) {
/* 1759 */       sb.append(st);
/*      */     }
/* 1761 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final int length()
/*      */   {
/* 1770 */     return this.length.intValue();
/*      */   }
/*      */   
/* 1773 */   public Comparator<PIGData> comp = new Comparator()
/*      */   {
/*      */     public int compare(PIGData arg0, PIGData arg1)
/*      */     {
/* 1777 */       String name1 = arg0.getName();
/* 1778 */       String name2 = arg1.getName();
/*      */       
/*      */ 
/* 1781 */       return name1.compareTo(name2);
/*      */     }
/*      */   };
/*      */   String[] header;
/*      */   List<String> header_snp;
/*      */   List<String> header_sample;
/*      */   private static final String REAL_NUMBER = "^[-+]?\\d+(\\.\\d+)?$";
/*      */   private static final String NONINTEGER = "\\D";
/*      */   Phenotypes pheno;
/*      */   
/* 1791 */   public void printHapMapFormat(File f, String chr) throws Exception { String len = "%" + ((String)getKeys().iterator().next()).length() + "s";
/* 1792 */     PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
/* 1793 */     StringBuffer header = new StringBuffer("%8s\t%8s");
/* 1794 */     String[] toPrint = new String[2 + this.data.size()];
/* 1795 */     toPrint[0] = "chrom";toPrint[1] = "pos";
/* 1796 */     List<PIGData> list = new ArrayList(this.data.values());
/* 1797 */     Collections.sort(list, this.comp);
/* 1798 */     for (int i = 2; i < toPrint.length; i++) {
/* 1799 */       toPrint[i] = ((PIGData)list.get(i - 2)).getName();
/* 1800 */       header.append("\t" + len);
/*      */     }
/* 1802 */     String headerSt = header.toString();
/*      */     
/* 1804 */     pw.println(Format.sprintf(headerSt, toPrint));
/*      */     
/* 1806 */     for (int pos_index = 0; pos_index < this.loc.size(); pos_index++) {
/* 1807 */       toPrint[0] = chr;toPrint[1] = ((Integer)this.loc.get(pos_index)).toString();
/*      */       
/* 1809 */       for (int i = 2; i < toPrint.length; i++) {
/* 1810 */         ComparableArray comp = (ComparableArray)((PIGData)list.get(i - 2)).getElement(pos_index);
/* 1811 */         toPrint[i] = comp.toStringPrint();
/*      */       }
/*      */       
/*      */ 
/* 1815 */       pw.println(Format.sprintf(headerSt, toPrint));
/*      */     }
/* 1817 */     pw.close();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void printHapMapFormat(PrintWriter pw, Collection<Integer> toD, boolean style)
/*      */     throws Exception
/*      */   {
/* 1826 */     printHapMapFormat(pw, toD, style, new String[] { "snpid", "loc" }, new String[0], new String[] { "data" }, "%7s");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void printHapMapFormat(PrintWriter pw, Collection<Integer> toD, boolean style, String[] initTags, String[] phenoTags, String[] tags, String len)
/*      */     throws Exception
/*      */   {
/* 1844 */     int phenoSize = this.pheno == null ? 0 : this.pheno.size();
/*      */     
/*      */ 
/* 1847 */     StringBuffer header = new StringBuffer("");
/* 1848 */     int multiple = tags.length;
/* 1849 */     int stLen = initTags.length + phenoTags.length * phenoSize;
/* 1850 */     String[] toPrint = new String[stLen + multiple * this.data.size()];
/* 1851 */     String[] toPrintH = new String[stLen + multiple * this.data.size()];
/*      */     
/* 1853 */     for (int i = 0; i < initTags.length; i++)
/*      */     {
/*      */ 
/* 1856 */       header.append("%20s\t");
/*      */       
/* 1858 */       toPrintH[i] = 
/* 1859 */         ((initTags[i].equals("maf")) && (this.maf != null) ? this.maf.getEmissionStateSpace().defaultList : 
/* 1860 */         initTags[i]);
/* 1861 */       if (initTags[i].equals("maf")) {
/* 1862 */         this.maf = calculateMaf1();
/*      */       }
/*      */     }
/* 1865 */     for (int i1 = 0; i1 < phenoSize; i1++) {
/* 1866 */       for (int i = 0; i < phenoTags.length; i++)
/*      */       {
/* 1868 */         header.append("%20s\t");
/*      */         
/* 1870 */         toPrintH[(i + initTags.length)] = ((String)this.pheno.phen.get(i1) + "_" + phenoTags[i]);
/*      */       }
/*      */     }
/*      */     
/* 1874 */     List<String> list = new ArrayList((Collection)this.data.keySet());
/* 1875 */     int ki = 0;
/* 1876 */     for (int i = 0; i < list.size(); i++) {
/* 1877 */       for (int k = 0; k < multiple; k++) {
/* 1878 */         toPrintH[(i * multiple + stLen + k)] = ((String)list.get(i));
/* 1879 */         header.append("\t" + len);
/*      */       }
/*      */     }
/* 1882 */     String headerSt = header.toString();
/*      */     
/* 1884 */     pw.println(Format.sprintf(headerSt, toPrintH));
/*      */     
/* 1886 */     for (int pos_index = 0; pos_index < this.loc.size(); pos_index++)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1892 */       if ((toD == null) || (!toD.contains(Integer.valueOf(pos_index))))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1897 */         for (int ik = 0; ik < initTags.length; ik++) {
/* 1898 */           toPrint[ik] = getInfo(initTags[ik], pos_index);
/*      */         }
/* 1900 */         for (int i1 = 0; i1 < phenoSize; i1++) {
/* 1901 */           for (int i = 0; i < phenoTags.length; i++) {
/* 1902 */             toPrint[(initTags.length + i1 * phenoTags.length + i)] = getPhenInfo(phenoTags[i], pos_index, i1);
/*      */           }
/*      */         }
/* 1905 */         for (int i = 0; i < list.size(); i++) {
/* 1906 */           for (int k = 0; k < multiple; k++) {
/* 1907 */             String st = getInfo(tags[k], (String)list.get(i), pos_index, style);
/* 1908 */             toPrint[(i * multiple + stLen + k)] = (st == null ? "null" : st);
/*      */           }
/*      */         }
/* 1911 */         pw.println(Format.sprintf(headerSt, toPrint));
/*      */       } }
/*      */   }
/*      */   
/*      */   public int numClasses(int phenIndex) {
/* 1916 */     if ((this.pheno.phenotypeDistribution[phenIndex] instanceof SkewNormal)) { return 0;
/*      */     }
/* 1918 */     return ((SimpleExtendedDistribution)this.pheno.phenotypeDistribution[phenIndex]).probs.length;
/*      */   }
/*      */   
/*      */   public String getPhenInfo(String string, int pos_index, int phenIndex)
/*      */   {
/* 1923 */     int numCl = numClasses(phenIndex);
/*      */     
/* 1925 */     if (string.startsWith("chisq")) {
/* 1926 */       if (numCl == 2) return Format.sprintf("%5.3g/%5.3g/%5.3g", scoreChi1(pos_index, false, string.endsWith("state"), phenIndex));
/* 1927 */       return "";
/*      */     }
/* 1929 */     if (string.startsWith("armitage")) {
/* 1930 */       if (numCl == 2) return Format.sprintf("%5.3g/%5.3g/%5.3g", scoreChi1(pos_index, true, string.endsWith("state"), phenIndex));
/* 1931 */       return "";
/*      */     }
/* 1933 */     throw new RuntimeException("!!");
/*      */   }
/*      */   
/*      */   public String getInfo(String string, int pos_index)
/*      */   {
/* 1938 */     if (string.startsWith("chisq")) {
/* 1939 */       throw new RuntimeException("!!");
/*      */     }
/* 1941 */     if (string.startsWith("armitage")) {
/* 1942 */       throw new RuntimeException("!!");
/*      */     }
/* 1944 */     if (string.equals("maf")) {
/* 1945 */       double[] res = this.maf.getEmiss(pos_index);
/* 1946 */       Double[] r = new Double[res.length];
/* 1947 */       StringBuffer sb = new StringBuffer();
/* 1948 */       for (int i = 0; i < res.length; i++) {
/* 1949 */         r[i] = Double.valueOf(res[i]);
/* 1950 */         sb.append("%5.3g/");
/*      */       }
/*      */       
/* 1953 */       return Format.sprintf(sb.toString(), r);
/*      */     }
/* 1955 */     if (string.equals("snpid")) {
/* 1956 */       return this.snpid == null ? "" : (String)this.snpid.get(pos_index);
/*      */     }
/* 1958 */     if (string.equals("loc")) {
/* 1959 */       return (String)this.loc.get(pos_index);
/*      */     }
/* 1961 */     if (string.equals("index")) {
/* 1962 */       return pos_index;
/*      */     }
/* 1964 */     if (string.startsWith("hwe")) {
/* 1965 */       Double[] r = new Double[this.hwe.length];
/* 1966 */       for (int i = 0; i < r.length; i++) {
/* 1967 */         r[i] = ((Double)this.hwe[i].get(pos_index));
/*      */       }
/* 1969 */       StringBuffer sb = new StringBuffer();
/* 1970 */       for (int i = 0; i < r.length; i++)
/*      */       {
/* 1972 */         sb.append("%5.3g/");
/*      */       }
/* 1974 */       return Format.sprintf(sb.toString(), r);
/*      */     }
/*      */     try
/*      */     {
/* 1978 */       List l = (List)getClass().getField(string).get(this);
/* 1979 */       if (l.size() == 0) return null;
/* 1980 */       if (l.get(pos_index) == null) return "null";
/* 1981 */       return l.get(pos_index).toString();
/*      */     } catch (Exception exc) {
/* 1983 */       exc.printStackTrace(); }
/* 1984 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private double getHWE(int pos_index, boolean state, boolean ignoreMissing, int data_index)
/*      */   {
/* 1994 */     CompoundEmissionStateSpace emstsp = Emiss.getEmissionStateSpace(1);
/* 1995 */     double[] count1 = new double[emstsp.getMembers()[0].size()];
/* 1996 */     double[] count = new double[emstsp.size()];
/* 1997 */     int cnt = 0;
/* 1998 */     for (Iterator<String> it = getKeyIterator(); it.hasNext();) {
/* 1999 */       String key = (String)it.next();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2009 */       PhasedDataState dat = (PhasedDataState)this.data.get(key);
/* 2010 */       if (dat.noCopies() == 2) {
/* 2011 */         int d_index = dat.emissions[pos_index].getDataIndex();
/*      */         
/* 2013 */         if (d_index == data_index)
/*      */         {
/* 2015 */           cnt++;
/* 2016 */           if (!state) {
/* 2017 */             ComparableArray compa = (ComparableArray)dat.getElement(pos_index);
/* 2018 */             if ((!ignoreMissing) || (compa.countNull() != 2)) {
/* 2019 */               int ind = emstsp.get(compa).intValue();
/*      */               
/* 2021 */               int[] indices = emstsp.getMemberIndices(ind);
/* 2022 */               for (int k = 0; k < indices.length; k++) {
/* 2023 */                 count1[indices[k]] += 1.0D;
/*      */               }
/* 2025 */               count[ind] += 1.0D;
/*      */             }
/*      */           } else {
/* 2028 */             EmissionState nxt = (EmissionState)this.dataL.get(key);
/* 2029 */             Integer fixed = nxt.getFixedInteger(pos_index);
/* 2030 */             if (fixed != null) {
/* 2031 */               int ind = nxt.getFixedInteger(pos_index).intValue();
/* 2032 */               if ((!ignoreMissing) || (((ComparableArray)emstsp.get(ind)).countNull() != 2))
/*      */               {
/* 2034 */                 int[] indices = emstsp.getMemberIndices(ind);
/* 2035 */                 for (int k = 0; k < indices.length; k++) {
/* 2036 */                   count1[indices[k]] += 1.0D;
/*      */                 }
/* 2038 */                 count[ind] += 1.0D;
/*      */               }
/*      */             } else {
/* 2041 */               double[] prob = nxt.getEmiss(pos_index);
/* 2042 */               double sum = Constants.sum(prob);
/*      */               
/* 2044 */               for (int ind = 0; ind < prob.length; ind++) {
/* 2045 */                 if ((!ignoreMissing) || (((ComparableArray)emstsp.get(ind)).countNull() != 2)) {
/* 2046 */                   int[] indices = ((CompoundEmissionStateSpace)nxt.getEmissionStateSpace()).getMemberIndices(ind);
/* 2047 */                   for (int k = 0; k < indices.length; k++) {
/* 2048 */                     count1[indices[k]] += prob[ind];
/*      */                   }
/* 2050 */                   count[ind] += prob[ind];
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2058 */     if ((cnt == 0) || (Constants.sum(count1) < 0.001D)) { return 1.0D;
/*      */     }
/* 2060 */     SimpleExtendedDistribution.normalise(count1);
/* 2061 */     double sum = Constants.sum(count);
/* 2062 */     double statistic = 0.0D;
/* 2063 */     double sum1 = 0.0D;
/* 2064 */     int cntNonZero = 0;
/* 2065 */     for (int i = 0; i < count1.length; i++) {
/* 2066 */       if (count1[i] > 0.001D) cntNonZero++;
/*      */     }
/* 2068 */     for (int i = 0; i < emstsp.size(); i++) {
/* 2069 */       int[] memb = emstsp.getMemberIndices(i);
/* 2070 */       double exp_count = 
/* 2071 */         count1[memb[0]] * count1[memb[1]] * sum;
/* 2072 */       if (memb[0] != memb[1]) exp_count *= 2.0D;
/* 2073 */       sum1 += exp_count;
/* 2074 */       double diff = exp_count - count[i];
/* 2075 */       if ((exp_count < 1.0E-10D) && (count[i] > 0.01D)) throw new RuntimeException("!! " + exp_count + " " + count[i] + " " + pos_index);
/* 2076 */       if ((Math.abs(diff) > 0.001D) && (exp_count > 0.001D)) {
/* 2077 */         statistic += Math.pow(diff, 2.0D) / exp_count;
/*      */       }
/*      */     }
/*      */     
/* 2081 */     if (Math.abs(sum - sum1) > 0.001D) throw new RuntimeException("!! " + sum + " " + sum1);
/* 2082 */     return ChiSq.chi2prob(degF(cntNonZero), statistic);
/*      */   }
/*      */   
/*      */   private int degF(int cntNonZero)
/*      */   {
/* 2087 */     return cntNonZero * (cntNonZero - 1) / 2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract int size();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeDickFormat(File f, boolean header, Collection<String> idv, Collection<Integer> toD)
/*      */     throws Exception
/*      */   {
/* 2107 */     if ((idv == null) || (idv.size() == 0)) idv = getKeys();
/* 2108 */     List<Integer> loc1 = new ArrayList();
/* 2109 */     for (int i = 0; i < this.loc.size(); i++) {
/* 2110 */       if ((toD == null) || (!toD.contains(Integer.valueOf(i)))) {
/* 2111 */         loc1.add((Integer)this.loc.get(i));
/*      */       }
/*      */     }
/* 2114 */     PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
/* 2115 */     if (header) {
/* 2116 */       pw.println("\t" + size() + "\t" + (length() - (toD == null ? 0 : toD.size())));
/* 2117 */       pw.print("\t");
/* 2118 */       pw.println(Format.sprintf(getPrintString(loc1.size(), "%5i "), loc1.toArray(new Integer[0])));
/* 2119 */       pw.print("\t"); }
/*      */     PIGData[] nxt;
/*      */     int j;
/* 2122 */     for (Iterator<String> it = idv.iterator(); it.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/* 2126 */         j < nxt.length)
/*      */     {
/* 2123 */       String key = (String)it.next();
/* 2124 */       PIGData nt = (PIGData)this.data.get(key);
/* 2125 */       nxt = nt.split();
/* 2126 */       j = 0; continue;
/* 2127 */       pw.print(nt.getName() + "\t");
/* 2128 */       pw.print((j == 0 ? 'U' : 'T') + "\t");
/* 2129 */       for (int i = 0; i < nxt[j].length(); i++) {
/* 2130 */         if ((toD == null) || (!toD.contains(Integer.valueOf(i)))) {
/* 2131 */           Emiss c = (Emiss)nxt[j].getElement(i);
/*      */           
/*      */ 
/*      */ 
/* 2135 */           if (c == Emiss.A) { pw.print("1");
/* 2136 */           } else if (c == Emiss.B) { pw.print("2");
/* 2137 */           } else if (c == Emiss.N) pw.print("3"); else {
/* 2138 */             pw.print("4");
/*      */           }
/* 2140 */           if (i < nxt[j].length() - 1) pw.print("\t");
/*      */         }
/*      */       }
/* 2143 */       pw.println();j++;
/*      */     }
/*      */     
/* 2146 */     pw.close();
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeDickFormat1(File f, boolean header)
/*      */     throws Exception
/*      */   {
/* 2153 */     Collection<String> idv = getKeys();
/* 2154 */     List<Integer> loc1 = this.loc;
/* 2155 */     String intStr = getPrintString(loc1.size(), "%9i\t");
/* 2156 */     String doubStr = getPrintString(loc1.size(), "%9.3g\t");
/* 2157 */     PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
/* 2158 */     if (header)
/*      */     {
/* 2160 */       pw.print("indiv \t type\t");
/* 2161 */       pw.println(Format.sprintf(intStr, loc1.toArray(new Integer[0])));
/*      */     }
/*      */     
/*      */ 
/* 2165 */     Double[] cert = new Double[length()];
/* 2166 */     Integer[] val = new Integer[length()];
/* 2167 */     for (Iterator<String> it = idv.iterator(); it.hasNext();) {
/* 2168 */       String key = (String)it.next();
/* 2169 */       EmissionState nt = (EmissionState)this.dataL.get(key);
/*      */       
/*      */ 
/* 2172 */       for (int i = 0; i < nt.length(); i++) {
/* 2173 */         val[i] = Integer.valueOf(nt.getBestIndex(i));
/* 2174 */         cert[i] = Double.valueOf(nt.score(val[i].intValue(), i));
/*      */       }
/* 2176 */       pw.print(nt.getName() + "\t false\t");
/* 2177 */       pw.println(Format.sprintf(intStr, val));
/* 2178 */       pw.print(nt.getName() + "\t true\t");
/* 2179 */       pw.println(Format.sprintf(doubStr, cert));
/*      */     }
/*      */     
/* 2182 */     pw.close();
/*      */   }
/*      */   
/*      */ 
/*      */   public void getDeletedPositions(double mafThresh, SortedSet<Integer> ampl, SortedSet<Integer> del)
/*      */   {
/* 2188 */     int[] count = new int[this.loc.size()];
/* 2189 */     int[] countAmpl = new int[this.loc.size()];
/* 2190 */     double cntThresh = mafThresh * this.data.size() * 2.0D;
/* 2191 */     System.err.println("count thresh is " + cntThresh + " " + this.data.size() + " " + mafThresh);
/* 2192 */     Arrays.fill(count, 0);
/* 2193 */     PIGData dat; int i; for (Iterator<PIGData> it = iterator(); it.hasNext(); 
/*      */         
/* 2195 */         i < dat.length())
/*      */     {
/* 2194 */       dat = (PIGData)it.next();
/* 2195 */       i = 0; continue;
/* 2196 */       ComparableArray compa = (ComparableArray)dat.getElement(i);
/* 2197 */       for (int k = 0; k < compa.size(); k++) {
/* 2198 */         Emiss em = (Emiss)compa.get(k);
/* 2199 */         if (em.noCopies() == 0) { count[i] += 1;
/* 2200 */         } else if (em.noCopies() == 2) { countAmpl[i] += 1;
/*      */         }
/*      */       }
/* 2195 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2204 */     for (int i = 0; i < count.length; i++) {
/* 2205 */       if (count[i] > cntThresh) del.add(Integer.valueOf(i));
/* 2206 */       if (countAmpl[i] > cntThresh) { ampl.add(Integer.valueOf(i));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void printLocations(PrintWriter pw)
/*      */   {
/* 2213 */     for (int i = 0; i < this.loc.size(); i++) {
/* 2214 */       pw.println(this.loc.get(i));
/*      */     }
/* 2216 */     pw.close();
/*      */   }
/*      */   
/*      */ 
/*      */   public List<Aberation> getDeletedPositions()
/*      */   {
/* 2222 */     List<Aberation> obj = new ArrayList();
/* 2223 */     for (Iterator<PIGData> it = iterator(); it.hasNext();) {
/* 2224 */       PhasedDataState dat = (PhasedDataState)it.next();
/* 2225 */       obj.addAll(dat.getDeletedPositions((EmissionState)this.dataL.get(dat.getName())));
/*      */     }
/*      */     
/* 2228 */     Collections.sort(obj);
/* 2229 */     return obj;
/*      */   }
/*      */   
/*      */   public void fix(Locreader loc, int thresh) {
/* 2233 */     List<Integer> fixed = new ArrayList();
/* 2234 */     List<Integer> nonFixed = new ArrayList();
/* 2235 */     for (int i = 0; i < this.loc.size(); i++) {
/* 2236 */       int pos = ((Integer)this.loc.get(i)).intValue();
/* 2237 */       Location overl = loc.contains(pos, thresh);
/* 2238 */       if (overl == null) {
/* 2239 */         fixed.add((Integer)this.loc.get(i));
/* 2240 */         for (Iterator<EmissionState> it = dataLvalues(); it.hasNext();) {
/* 2241 */           HaplotypeEmissionState nxt = (HaplotypeEmissionState)it.next();
/* 2242 */           EmissionStateSpace emstsp = nxt.getEmissionStateSpace();
/* 2243 */           double[] emiss = nxt.getEmiss(i);
/* 2244 */           double[] newd = new double[emiss.length];
/* 2245 */           for (int k = 0; k < newd.length; k++) {
/* 2246 */             int cn = emstsp.getCN(k);
/* 2247 */             if (cn != 2) newd[k] = 0.0D; else
/* 2248 */               newd[k] = emiss[k];
/*      */           }
/* 2250 */           SimpleExtendedDistribution.normalise(newd);
/* 2251 */           int max = Constants.getMax(newd);
/* 2252 */           if (newd[max] > 0.999D) {
/* 2253 */             nxt.emissions[i] = new IntegerDistribution(max);
/*      */           } else {
/* 2255 */             nxt.emissions[i] = new SimpleExtendedDistribution(newd, Double.POSITIVE_INFINITY);
/*      */           }
/*      */         }
/*      */       } else {
/* 2259 */         nonFixed.add((Integer)this.loc.get(i));
/*      */       }
/*      */     }
/* 2262 */     calculateMaf(true);
/* 2263 */     System.err.println("finished fixing \nfixed:" + fixed + "\n not fixed" + nonFixed);
/*      */   }
/*      */   
/* 2266 */   public Locreader getMergedDeletions(boolean extend) { List<Aberation> l = getDeletedPositions();
/*      */     
/* 2268 */     Locreader locr = new Locreader(9223372036854775806L, "");
/* 2269 */     for (int i = 0; i < l.size(); i++) {
/* 2270 */       Aberation ab = (Aberation)l.get(i);
/* 2271 */       Integer start = (Integer)this.loc.get(ab.start);
/* 2272 */       Integer finish = (Integer)this.loc.get(ab.end);
/* 2273 */       if (extend) {
/* 2274 */         if (ab.start > 0) {
/* 2275 */           start = Integer.valueOf(((Integer)this.loc.get(ab.start - 1)).intValue() + 1);
/*      */         }
/* 2277 */         if (ab.end < this.loc.size() - 1) {
/* 2278 */           finish = Integer.valueOf(((Integer)this.loc.get(ab.end + 1)).intValue() - 1);
/*      */         }
/*      */       }
/* 2281 */       Location loc = new Location(start.intValue(), finish.intValue(), "");
/* 2282 */       loc.setName("");
/* 2283 */       loc.setNoCop(0);
/* 2284 */       locr.add(loc);
/*      */     }
/* 2286 */     locr.merge(0.0D);
/* 2287 */     return locr;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void printDeletedPositions(PrintWriter pw)
/*      */   {
/* 2295 */     String pst = new String("%-7s %7s %7s %7s %7s %7s %7s %7s %7s");
/* 2296 */     String pst1 = new String("%-7s %7s %7s %7i %7i %7i %7s %7i %5.3g");
/* 2297 */     pw.println(Format.sprintf(pst, new Object[] { "File", "Sample", "Chr", "Start", "End", "NbSnp", "Intensity", "Type", "Avg_certainty" }));
/* 2298 */     List<Aberation> obj = getDeletedPositions();
/* 2299 */     for (int i = 0; i < obj.size(); i++) {
/* 2300 */       Aberation ab = (Aberation)obj.get(i);
/* 2301 */       Object[] obj1 = new Object[9];
/* 2302 */       obj1[0] = "french";
/* 2303 */       obj1[1] = ab.name;
/*      */       
/* 2305 */       File dir = new File(Constants.getDirFile());
/* 2306 */       obj1[2] = dir.getName();
/* 2307 */       obj1[3] = this.loc.get(ab.start);
/* 2308 */       obj1[4] = this.loc.get(ab.end);
/* 2309 */       obj1[5] = Integer.valueOf(ab.end - ab.start + 1);
/* 2310 */       obj1[6] = "0";
/* 2311 */       obj1[7] = Integer.valueOf(ab.copy);
/* 2312 */       obj1[8] = Double.valueOf(ab.certainty);
/* 2313 */       String st = Format.sprintf(pst1, obj1);
/*      */       
/* 2315 */       pw.println(st);
/*      */     }
/*      */     
/* 2318 */     pw.close();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeLocation(PrintWriter pw, Collection<Integer> toDrop)
/*      */   {
/* 2331 */     List<Integer> loc1 = new ArrayList(this.loc.size());
/* 2332 */     for (int i = 0; i < this.loc.size(); i++) {
/* 2333 */       if ((toDrop == null) || (!toDrop.contains(Integer.valueOf(i)))) {
/* 2334 */         loc1.add((Integer)this.loc.get(i));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2339 */     SimpleScorableObject.printIdLine("", pw, loc1.size());
/* 2340 */     pw.println("# locs");
/* 2341 */     Integer maxLoc = (Integer)loc1.get(loc1.size() - 1);
/* 2342 */     int numberPos = maxLoc.toString().length();
/* 2343 */     char[][] sb = new char[numberPos + 1][loc1.size() + numberPos + 10];
/* 2344 */     char sp = ' ';
/* 2345 */     for (int i = 0; i < sb.length; i++) {
/* 2346 */       Arrays.fill(sb[i], sp);
/*      */     }
/* 2348 */     int j = 0;
/*      */     
/* 2350 */     for (int i = 0; i < loc1.size(); i++) {
/* 2351 */       char[] st = ((Integer)loc1.get(i)).toString().toCharArray();
/* 2352 */       System.arraycopy(st, 0, sb[j], i, st.length);
/* 2353 */       j++;
/* 2354 */       if (j == sb.length) j = 0;
/*      */     }
/* 2356 */     for (j = 0; j < sb.length; j++) {
/* 2357 */       pw.println(new String(sb[j]));
/*      */     }
/* 2359 */     pw.println("# end locs");
/*      */   }
/*      */   
/* 2362 */   public static void printUncertainty(double[] unc, PrintWriter pw, Collection<Integer> toDrop) { StringBuffer[] sb = { new StringBuffer(), new StringBuffer() };
/* 2363 */     sb[0] = new StringBuffer();
/* 2364 */     sb[1] = new StringBuffer();
/* 2365 */     for (int j = 0; j < unc.length; j++) {
/* 2366 */       if ((toDrop == null) || (!toDrop.contains(Integer.valueOf(j)))) {
/* 2367 */         double d = Math.round(100.0D * unc[j]);
/* 2368 */         if (d >= 100.0D) {
/* 2369 */           sb[0].append("*");sb[1].append("*");
/*      */         }
/*      */         else {
/* 2372 */           char[] st = d.toCharArray();
/* 2373 */           sb[0].append(st[0]);sb[1].append(st[1]);
/*      */         }
/*      */       }
/*      */     }
/* 2377 */     pw.println(sb[0].toString());
/* 2378 */     pw.println(sb[1].toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeFastphase(Map<String, PIGData> data1, Map<String, double[]> uncertaintyPhase, PrintWriter pw, boolean printUncertainty, boolean mark, Collection<Integer> toDrop)
/*      */     throws Exception
/*      */   {
/* 2388 */     writeFastphase(data1, uncertaintyPhase, pw, printUncertainty, mark, true, toDrop);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeFastphase(Map<String, PIGData> data1, Map<String, double[]> uncertaintyPhase, PrintWriter pw, boolean printUncertainty, boolean mark, boolean expand, Collection<Integer> toDrop)
/*      */     throws Exception
/*      */   {
/* 2399 */     pw.println(size());
/*      */     
/* 2401 */     pw.println(length());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2407 */     for (Iterator<String> it = getKeyIterator(); it.hasNext();)
/*      */     {
/* 2409 */       String key = (String)it.next();
/*      */       
/*      */ 
/*      */ 
/* 2413 */       PIGData dat = (PIGData)data1.get(key);
/* 2414 */       dat.print(pw, expand, mark, toDrop);
/* 2415 */       double[] unc = (double[])null;
/*      */       
/* 2417 */       if (unc != null) {
/* 2418 */         double[] uncP = (double[])uncertaintyPhase.get(dat.getName());
/* 2419 */         pw.println("certainty");
/* 2420 */         printUncertainty(unc, pw, toDrop);
/* 2421 */         pw.println("certainty phase");
/* 2422 */         if (uncP != null) printUncertainty(uncP, pw, toDrop);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeFastphase1(PrintWriter pw, Map<String, double[]> uncertaintyState)
/*      */     throws Exception
/*      */   {
/* 2430 */     for (Iterator<String> is = uncertaintyState.keySet().iterator(); is.hasNext();) {
/* 2431 */       String s = (String)is.next();
/* 2432 */       pw.println("# id " + s);
/* 2433 */       for (int i = 0; i < ((double[])uncertaintyState.get(s)).length - 1; i++) {
/* 2434 */         pw.print(Format.sprintf("%.4f", new Double[] { Double.valueOf(((double[])uncertaintyState.get(s))[i]) }) + "\t");
/*      */       }
/* 2436 */       pw.println(Format.sprintf("%.4f", new Double[] { Double.valueOf(((double[])uncertaintyState.get(s))[(((double[])uncertaintyState.get(s)).length - 1)]) }));
/*      */     }
/*      */   }
/*      */   
/*      */   public double weight(String key)
/*      */   {
/* 2442 */     return 1.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   Iterator<String> getKeyIterator()
/*      */   {
/* 2480 */     return getKeys().iterator();
/*      */   }
/*      */   
/*      */ 
/*      */   public List<String> getNames()
/*      */   {
/* 2486 */     return new ArrayList(getKeys());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void trim(int i)
/*      */   {
/* 2493 */     if (size() <= i) return;
/* 2494 */     List<String> names = getNames();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2501 */     List<String> toKeep = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/* 2505 */     Collections.shuffle(names);
/* 2506 */     toKeep.addAll((Collection)names.subList(0, i - toKeep.size()));
/*      */     
/* 2508 */     restricToAlias(toKeep);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Integer> getLocations()
/*      */   {
/* 2532 */     return this.loc;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void replace(Map<String, PIGData> newData)
/*      */   {
/* 2539 */     this.data = newData;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void arrangeDataAccordingToPedigree()
/*      */   {
/* 2547 */     System.err.println("arranging according to pedigree");
/* 2548 */     this.data = arrangeDataAccordingToPedigree(this.data);
/* 2549 */     this.viterbi = arrangeDataAccordingToPedigree(this.viterbi);
/* 2550 */     if (this.dataL.size() > 0) {
/* 2551 */       Map<String, String> mother = this.ped.mother;
/* 2552 */       Map<String, String> father = this.ped.father;
/* 2553 */       Map<String, EmissionState> toAdd = new HashMap();
/* 2554 */       for (Iterator<String> it = mother.keySet().iterator(); it.hasNext();) {
/* 2555 */         String chi = (String)it.next();
/*      */         
/* 2557 */         EmissionState moth = getL((String)mother.get(chi));
/* 2558 */         EmissionState child = getL(chi);
/* 2559 */         String fathKey = (String)father.get(chi);
/*      */         EmissionState trio;
/* 2561 */         EmissionState trio; if (fathKey != null) {
/* 2562 */           EmissionState fath = getL(fathKey);
/* 2563 */           trio = EmissionState.getEmissionState(moth, fath, child);
/*      */         }
/*      */         else {
/* 2566 */           trio = EmissionState.getEmissionState(moth, child);
/*      */         }
/* 2568 */         toAdd.put(trio.getName(), trio);
/*      */       }
/*      */       
/* 2571 */       replaceL(toAdd);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void replaceL(Map<String, EmissionState> newData)
/*      */   {
/* 2578 */     this.dataL = newData;
/*      */   }
/*      */   
/*      */ 
/*      */   public final Map<String, PIGData> arrangeDataAccordingToPedigree(Map<String, PIGData> datai)
/*      */   {
/* 2584 */     Map<String, PIGData> toAdd = new HashMap();
/* 2585 */     for (Iterator<String> it = this.ped.mother.keySet().iterator(); it.hasNext();) {
/* 2586 */       String chi = (String)it.next();
/* 2587 */       PIGData fath = (PIGData)datai.get(this.ped.father.get(chi));
/* 2588 */       PIGData moth = (PIGData)datai.get(this.ped.mother.get(chi));
/* 2589 */       PIGData child = (PIGData)datai.get(chi);
/* 2590 */       if ((moth != null) && (child != null))
/*      */       {
/*      */ 
/*      */ 
/* 2594 */         if (fath == null) {
/* 2595 */           PIGData moth1 = SimpleScorableObject.make(new CSOData[] { moth, child }, false, ";");
/* 2596 */           toAdd.put(moth1.getName(), moth1);
/*      */         }
/*      */         else {
/* 2599 */           PIGData moth1 = SimpleScorableObject.make(new CSOData[] { moth, fath, child }, false, ";");
/* 2600 */           toAdd.put(moth1.getName(), moth1);
/*      */         }
/*      */       }
/*      */     }
/* 2604 */     return toAdd;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Iterator<PIGData> iterator()
/*      */   {
/* 2613 */     return this.data.values().iterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void set(int ij, PIGData newDat)
/*      */   {
/* 2620 */     this.data.put(newDat.getName(), newDat);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPedigree(PedigreeDataCollection pedData)
/*      */   {
/* 2628 */     this.ped = pedData;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<String> getKeys()
/*      */   {
/* 2636 */     if (this.dataL.size() != 0)
/* 2637 */       return this.dataL.keySet();
/* 2638 */     return this.data.keySet();
/*      */   }
/*      */   
/*      */ 
/*      */   public final double[][] checkConsistency()
/*      */   {
/* 2644 */     double[] cons1 = checkConsistency(this.data);
/* 2645 */     double[] cons2 = checkConsistency(this.viterbi);
/* 2646 */     return new double[][] { cons1, cons2 };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final double[] checkConsistency(Map<String, PIGData> datai)
/*      */   {
/* 2653 */     Map<String, PIGData> toAdd = new HashMap();
/* 2654 */     int incons = 0;
/* 2655 */     int cons = 0;
/* 2656 */     int total = 0;
/* 2657 */     PIGData fath; int i; label335: for (Iterator<String> it = this.ped.mother.keySet().iterator(); it.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2664 */         i < fath.length())
/*      */     {
/* 2658 */       String chi = (String)it.next();
/* 2659 */       fath = (PIGData)datai.get(this.ped.father.get(chi));
/* 2660 */       PIGData moth = (PIGData)datai.get(this.ped.mother.get(chi));
/* 2661 */       PIGData child = (PIGData)datai.get(chi);
/* 2662 */       if ((fath == null) || (moth == null) || (child == null)) break label335;
/* 2663 */       total += fath.length();
/* 2664 */       i = 0; continue;
/* 2665 */       ComparableArray c = (ComparableArray)child.getElement(i);
/* 2666 */       ComparableArray m = (ComparableArray)moth.getElement(i);
/* 2667 */       ComparableArray f = (ComparableArray)fath.getElement(i);
/* 2668 */       if (c.size() == 1) {
/* 2669 */         if (m.contains(c.get(0))) cons++; else {
/* 2670 */           incons++;
/*      */         }
/* 2672 */       } else if (c.size() == 2) {
/* 2673 */         if ((m.contains(c.get(0))) && (f.contains(c.get(1)))) { cons++;
/* 2674 */         } else if ((m.contains(c.get(1))) && (f.contains(c.get(0)))) cons++; else
/* 2675 */           incons++;
/*      */       } else {
/* 2677 */         throw new RuntimeException("!!");
/*      */       }
/* 2664 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2680 */     if (incons + cons != total) throw new RuntimeException("!!");
/* 2681 */     return new double[] { incons, cons };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean containsKey(String name2)
/*      */   {
/* 2688 */     return this.data.containsKey(name2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void summarise()
/*      */   {
/* 2695 */     split();
/* 2696 */     double[] ld_av = calcLDAverage();
/* 2697 */     System.err.println("ld_av is " + ld_av[0] + " " + ld_av[1]);
/* 2698 */     int no = this.loc.size();
/* 2699 */     int length = ((Integer)this.loc.get(no - 1)).intValue() - ((Integer)this.loc.get(0)).intValue();
/* 2700 */     List<Integer> l = new ArrayList();
/* 2701 */     double desn1 = 0.0D;
/* 2702 */     for (int i = 1; i < this.loc.size(); i++) {
/* 2703 */       l.add(Integer.valueOf(((Integer)this.loc.get(i)).intValue() - ((Integer)this.loc.get(i - 1)).intValue()));
/* 2704 */       desn1 += ((Integer)this.loc.get(i)).intValue() - ((Integer)this.loc.get(i - 1)).intValue();
/*      */     }
/* 2706 */     Collections.sort(l);
/* 2707 */     int mid = (int)Math.round(l.size() / 2.0D);
/* 2708 */     double desn = ((Integer)l.get(mid)).intValue();
/*      */     
/*      */ 
/* 2711 */     System.err.println("length is " + length);
/* 2712 */     System.err.println("density is " + desn + " " + desn1 / this.loc.size());
/* 2713 */     System.err.println("num is " + no);
/* 2714 */     System.exit(0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public double[] calcLDAverage()
/*      */   {
/* 2722 */     double[][] res = calcLD();
/* 2723 */     double[] sum = { 0.0D, 0.0D };
/* 2724 */     for (int i = 0; i < res[0].length; i++) {
/* 2725 */       sum[0] += res[0][i];
/* 2726 */       sum[1] += res[1][i];
/*      */     }
/* 2728 */     sum[0] /= res[0].length;
/* 2729 */     sum[1] /= res[0].length;
/* 2730 */     return sum;
/*      */   }
/*      */   
/*      */ 
/*      */   public double[][] calcLD()
/*      */   {
/* 2736 */     return calcLD(Emiss.a().toString(), Emiss.b().toString());
/*      */   }
/*      */   
/*      */ 
/*      */   public double[][] calcLD(String st1, String st2)
/*      */   {
/* 2742 */     LDCalculator lower = new R2Calculator();
/* 2743 */     LDCalculator upper = new DPrimeCalculator();
/* 2744 */     double[][] res = new double[2][length() - 1];
/* 2745 */     for (int i = 1; i < length(); i++) {
/* 2746 */       int j = i - 1;
/* 2747 */       String[] st_i = { st1, st2 };
/* 2748 */       String[] st_j = { st1, st2 };
/* 2749 */       double[][] d = getLD(i, j, st_i, st_j);
/* 2750 */       res[0][(i - 1)] = upper.calculate(d);
/* 2751 */       res[1][(i - 1)] = lower.calculate(d);
/* 2752 */       if (Double.isNaN(res[0][(i - 1)]))
/* 2753 */         throw new RuntimeException("!! " + i + " " + d[0][0] + " " + d[0][1] + " " + d[1][0] + " " + d[1][1]);
/*      */     }
/* 2755 */     return res;
/*      */   }
/*      */   
/*      */   public void split() {
/* 2759 */     split(1.0D, false);
/*      */   }
/*      */   
/*      */ 
/*      */   public void split(double prob, boolean repackage)
/*      */   {
/* 2765 */     Set<PIGData> set = new HashSet();
/* 2766 */     for (Iterator<PIGData> it = iterator(); it.hasNext();) {
/* 2767 */       PIGData da = (PIGData)it.next();
/* 2768 */       if (Constants.rand.nextDouble() <= prob)
/*      */       {
/* 2770 */         PIGData[] dat = da.split();
/* 2771 */         for (int j = 0; j < dat.length; j++) {
/* 2772 */           dat[j].setName(da.getName() + "_" + j);
/* 2773 */           set.add(
/* 2774 */             repackage ? 
/* 2775 */             SimpleScorableObject.make(new PIGData[] { dat[j] }, false, "") : 
/* 2776 */             dat[j]);
/*      */         }
/*      */       }
/*      */       else {
/* 2780 */         set.add(da);
/*      */       }
/*      */     }
/* 2783 */     this.data.clear();
/* 2784 */     for (Iterator<PIGData> it = set.iterator(); it.hasNext();) {
/* 2785 */       PIGData da = (PIGData)it.next();
/* 2786 */       this.data.put(da.getName(), da);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void calcLD(LDCalculator upper, LDCalculator lower, PIGData poss, double[][] res)
/*      */   {
/* 2798 */     for (int i = 0; i < length(); i++) {
/* 2799 */       res[i][i] = 1.0D;
/* 2800 */       for (int j = i + 1; j < length(); j++) {
/* 2801 */         ComparableArray c_i = (ComparableArray)poss.getElement(i);
/* 2802 */         ComparableArray c_j = (ComparableArray)poss.getElement(j);
/* 2803 */         String[] st_i = { c_i.get(0).toString(), c_i.get(1).toString() };
/* 2804 */         String[] st_j = { c_j.get(0).toString(), c_j.get(1).toString() };
/* 2805 */         double[][] d = getLD(i, j, st_i, st_j);
/* 2806 */         res[i][j] = lower.calculate(d);
/* 2807 */         res[j][i] = upper.calculate(d);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Double> calcLD(LDCalculator upper, PIGData poss, boolean left, int pos, int lenthresh)
/*      */   {
/* 2821 */     List<Double> res = new ArrayList();
/*      */     
/* 2823 */     res.add(Double.valueOf(0.0D));
/* 2824 */     for (int ik = 1;; ik++) {
/* 2825 */       int i = left ? pos - ik : pos + ik;
/* 2826 */       if ((i < 0) || (i >= this.loc.size()) || 
/* 2827 */         (Math.abs(((Integer)this.loc.get(i)).intValue() - ((Integer)this.loc.get(pos)).intValue()) > lenthresh)) break;
/* 2828 */       ComparableArray c_i = (ComparableArray)poss.getElement(i);
/* 2829 */       ComparableArray c_j = (ComparableArray)poss.getElement(pos);
/* 2830 */       String[] st_i = { c_i.get(0).toString(), c_i.get(1).toString() };
/* 2831 */       String[] st_j = { c_j.get(0).toString(), c_j.get(1).toString() };
/* 2832 */       double[][] d = getLD(i, pos, st_i, st_j);
/*      */       try {
/* 2834 */         res.add(Double.valueOf(upper.calculate(d)));
/*      */       } catch (Exception exc) {
/* 2836 */         System.err.println(d[0][0] + " " + d[0][1] + " " + d[1][0] + " " + d[1][1]);
/* 2837 */         exc.printStackTrace();
/*      */       }
/*      */     }
/* 2840 */     return res;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double[][] getLD(int pos1, int pos2, String[] st1, String[] st2)
/*      */   {
/* 2851 */     double[][] res = { { 0.0D, 0.0D }, { 0.0D, 0.0D } };
/* 2852 */     int[] start = { pos1, pos2 };
/* 2853 */     String[][] hapl = getHaplotypes(pos1, pos2);
/* 2854 */     for (int i = 0; i < st1.length; i++) {
/* 2855 */       for (int j = 0; j < st2.length; j++)
/*      */       {
/* 2857 */         res[i][j] = countHaplotypes(hapl, new String[] { st1[i], st2[j] });
/*      */       }
/*      */     }
/*      */     
/* 2861 */     if ((allzero(res[0])) && (allzero(res[1]))) throw new RuntimeException("!!");
/* 2862 */     return res;
/*      */   }
/*      */   
/*      */   private boolean allzero(double[] ds) {
/* 2866 */     for (int i = 0; i < ds.length; i++) {
/* 2867 */       if (ds[i] != 0.0D) return false;
/*      */     }
/* 2869 */     return true;
/*      */   }
/*      */   
/*      */   private double countHaplotypes(String[][] hapl, String[] str) {
/* 2873 */     int cont = 0;
/* 2874 */     for (int i = 0; i < hapl.length; i++) {
/* 2875 */       int k = 0;
/* 2876 */       while (hapl[i][k].equals(str[k]))
/*      */       {
/* 2875 */         k++; if (k >= str.length)
/*      */         {
/*      */ 
/* 2878 */           cont++; }
/*      */       } }
/* 2880 */     return cont;
/*      */   }
/*      */   
/*      */   public String[][] getHaplotypes(int st, int end) {
/* 2884 */     String[][] str = new String[this.data.values().size()][];
/* 2885 */     Iterator<PIGData> it = this.data.values().iterator();
/* 2886 */     for (int i = 0; it.hasNext(); i++) {
/* 2887 */       PIGData nxt = (PIGData)it.next();
/* 2888 */       str[i] = { nxt.getStringRep(st), nxt.getStringRep(end) };
/*      */     }
/*      */     
/* 2891 */     return str;
/*      */   }
/*      */   
/*      */ 
/*      */   public int countHaplotypes(int[] st, int[] en, String[] str)
/*      */   {
/* 2897 */     int count = 0;
/* 2898 */     for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/* 2899 */       PIGData nxt = (PIGData)it.next();
/* 2900 */       for (int i = 0; i < st.length; i++) {
/* 2901 */         int start = st[i];
/* 2902 */         int end = en[i];
/* 2903 */         String st1 = nxt.getStringRep(start, end);
/*      */         
/* 2905 */         if (!st1.equals(str[i])) {
/*      */           break;
/*      */         }
/*      */       }
/* 2909 */       count++;
/*      */     }
/*      */     
/* 2912 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */   public void printIndiv(PrintWriter pw_indiv)
/*      */   {
/* 2918 */     for (Iterator<String> it = getKeys().iterator(); it.hasNext();) {
/* 2919 */       pw_indiv.println((String)it.next());
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeFastphase(PrintWriter pw, boolean states) throws Exception {
/* 2924 */     if (!states)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 2929 */       writeFastphase(this.data, this.uncertaintyPhase, pw, false, true, false, null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public double[] get(Map<String, double[]> uncertainty1, String key)
/*      */   {
/* 2936 */     double[] res = (double[])uncertainty1.get(key);
/* 2937 */     if (res == null) {
/* 2938 */       uncertainty1.put(key, res = new double[this.length.intValue()]);
/*      */     }
/* 2940 */     return res;
/*      */   }
/*      */   
/*      */   public double[] get1(Map<String, double[]> uncertainty1, String key) {
/* 2944 */     double[] res = (double[])uncertainty1.get(key);
/* 2945 */     if (res == null) {
/* 2946 */       uncertainty1.put(key, res = new double[this.length.intValue() * Constants.modify(0).length]);
/*      */     }
/* 2948 */     return res;
/*      */   }
/*      */   
/*      */   public final double[] uncertaintyPhase(String key)
/*      */   {
/* 2953 */     return get(this.uncertaintyPhase, key);
/*      */   }
/*      */   
/*      */   public final double[] uncertaintyState(String key)
/*      */   {
/* 2958 */     return get1(this.uncertaintyState, key);
/*      */   }
/*      */   
/*      */   public double[] uncertaintyVitPhase(String key)
/*      */   {
/* 2963 */     return get(this.uncertaintyVitPhase, key);
/*      */   }
/*      */   
/*      */   public void setData(String key, PIGData dat) {
/* 2967 */     this.data.put(key, dat);
/*      */   }
/*      */   
/*      */   public void setViterbi(String key, PIGData datvit) {
/* 2971 */     this.viterbi.put(key, datvit);
/*      */   }
/*      */   
/*      */   public void setRecSites(String name, SortedMap<Integer, Integer>[] sampleRecSites)
/*      */   {
/* 2976 */     this.recSites.put(name, sampleRecSites);
/*      */   }
/*      */   
/*      */   public void clearViterbi() {
/* 2980 */     this.viterbi.clear();
/*      */   }
/*      */   
/*      */   public Iterator<EmissionState> dataLvalues() {
/* 2984 */     return this.dataL.values().iterator();
/*      */   }
/*      */   
/*      */   public void clearData() {
/* 2988 */     this.data.clear();
/*      */   }
/*      */   
/*      */   public SortedMap<Integer, Integer>[] recSites(String j) {
/* 2992 */     return (SortedMap[])this.recSites.get(j);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public EmissionState maf()
/*      */   {
/* 2999 */     return this.maf;
/*      */   }
/*      */   
/*      */   public Object ped() {
/* 3003 */     return this.ped;
/*      */   }
/*      */   
/*      */   public EmissionState dataL(String string) {
/* 3007 */     EmissionState res = (EmissionState)this.dataL.get(string);
/* 3008 */     return res;
/*      */   }
/*      */   
/*      */   public void makeDistributions() {}
/*      */   
/*      */   public Object headerObject(String st, Object prev) {
/* 3014 */     String str = st.toLowerCase();
/* 3015 */     if ((str.startsWith("index")) || (str.startsWith("sample"))) return null;
/* 3016 */     if ((str.startsWith("name")) || (str.startsWith("snp"))) return this.snpid;
/* 3017 */     if ((str.startsWith("pos")) || (str.startsWith("loc"))) return this.loc;
/* 3018 */     if (str.startsWith("chr")) return null;
/* 3019 */     if (str.startsWith("address")) return null;
/*      */     try
/*      */     {
/* 3022 */       return getClass().getField(st).get(this);
/*      */     }
/*      */     catch (Exception localException) {}
/*      */     
/* 3026 */     return null;
/*      */   }
/*      */   
/*      */   public Integer trans(String st)
/*      */   {
/* 3031 */     int cn_index = st.length() - 1;
/*      */     
/* 3033 */     if (st.indexOf('N') >= 0) {
/* 3034 */       st = st.replace('A', 'N');
/* 3035 */       st = st.replace('B', 'N');
/* 3036 */       return null;
/*      */     }
/* 3038 */     StringBuffer sb = new StringBuffer();
/* 3039 */     Comparable[] comp = new Comparable[st.length()];
/* 3040 */     for (int ik = 0; ik < st.length(); ik++) {
/* 3041 */       comp[ik] = Emiss.translate(st.charAt(ik));
/*      */     }
/* 3043 */     CompoundEmissionStateSpace stSpa = (CompoundEmissionStateSpace)this.stSp[cn_index];
/* 3044 */     return stSpa.get(new ComparableArray(comp));
/*      */   }
/*      */   
/*      */ 
/*      */   public Integer trans(String st, PhasedDataState data)
/*      */   {
/* 3050 */     if (st.indexOf('N') >= 0)
/*      */     {
/*      */ 
/* 3053 */       return null;
/*      */     }
/*      */     
/* 3056 */     Comparable[] comp = new Comparable[st.length()];
/* 3057 */     for (int ik = 0; ik < st.length(); ik++) {
/* 3058 */       comp[ik] = Emiss.translate(st.charAt(ik));
/*      */     }
/* 3060 */     CompoundEmissionStateSpace stSpa = (CompoundEmissionStateSpace)data.getEmissionStateSpace();
/* 3061 */     return stSpa.get(new ComparableArray(comp));
/*      */   }
/*      */   
/*      */   public void process(String indiv, String[] header, String[] geno, int i, Character majorAllele, Double recessiveFreq, boolean missing)
/*      */   {
/*      */     try {
/* 3067 */       PhasedDataState data = (PhasedDataState)this.data.get(indiv);
/* 3068 */       for (int k = 0; k < header.length; k++) {
/* 3069 */         if ((header[k].toLowerCase().indexOf("geno") >= 0) && 
/* 3070 */           (data.emissions[i] == null)) {
/* 3071 */           int ind = trans(geno[k]).intValue();
/* 3072 */           data.emissions[i] = new IntegerDistribution(ind);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 3077 */       if (data.emissions[i] == null) {
/* 3078 */         EmissionState sta = dataL(indiv);
/* 3079 */         data.emissions[i] = new IntegerDistribution(sta.getBestIndex(i));
/*      */       }
/* 3081 */       data.emissions[i].setDataIndex(this.index);
/*      */     } catch (Exception exc) {
/* 3083 */       exc.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public abstract void process(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt, Character paramCharacter1, Character paramCharacter2, Double paramDouble, boolean paramBoolean);
/*      */   
/*      */   public abstract void process(String paramString, int paramInt);
/*      */   
/*      */   public abstract void process(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt);
/*      */   
/*      */   public abstract void createDataStructure(String paramString, int paramInt);
/*      */   
/* 3095 */   public void createDataStructure(List<String> indiv) { for (int i = 0; i < indiv.size(); i++) {
/* 3096 */       this.dataL.put((String)indiv.get(i), null);
/* 3097 */       this.data.put((String)indiv.get(i), SimpleScorableObject.make((String)indiv.get(i), this.loc.size(), this.stSp[1]));
/*      */     }
/*      */   }
/*      */   
/*      */   public void readSNPInfo(InputStream is, String chr) throws Exception {
/* 3102 */     BufferedReader br = new BufferedReader(new InputStreamReader(is));
/* 3103 */     String st = "";
/*      */     
/* 3105 */     while ((st = br.readLine()) != null) {
/* 3106 */       String[] str = st.split("\t");
/* 3107 */       if (str[1].equals(chr)) {
/* 3108 */         int id = this.snpid.indexOf(str[0]);
/* 3109 */         if (id >= 0) {
/* 3110 */           this.majorAllele.set(id, Character.valueOf(str[3].charAt(0)));
/* 3111 */           this.minorAllele.set(id, Character.valueOf(str[4].charAt(0)));
/*      */         }
/*      */       }
/* 3114 */       System.err.println(st);
/*      */     }
/*      */   }
/*      */   
/*      */   public static int firstGreaterThanOrEqual(List<Integer> loc, int readPos)
/*      */   {
/* 3120 */     int read = 0;
/* 3121 */     for (read = 0; read < loc.size(); read++)
/* 3122 */       if (((Integer)loc.get(read)).intValue() > readPos)
/*      */         break;
/* 3124 */     return read;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ProbabilityDistribution[] numLevels()
/*      */   {
/* 3132 */     if (this.pheno == null) return new ProbabilityDistribution[0];
/* 3133 */     return this.pheno.phenotypeDistribution;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Set<String> readPhenotypes(File phenF, File incl, List<String> indiv)
/*      */     throws Exception
/*      */   {
/* 3141 */     if ((incl.exists()) && (incl.length() > 0L)) {
/* 3142 */       this.pheno = new Phenotypes(incl);
/*      */       
/*      */ 
/* 3145 */       Set<String> todo = new HashSet(indiv);
/*      */       
/* 3147 */       String[][] res = new String[indiv.size()][this.pheno.phen.size()];
/* 3148 */       BufferedReader br = new BufferedReader(new FileReader(phenF));
/* 3149 */       List<String> header = Arrays.asList(br.readLine().split("\t"));
/* 3150 */       int[] alias = new int[this.pheno.size()];
/* 3151 */       for (int i = 0; i < this.pheno.size(); i++) {
/* 3152 */         alias[i] = header.indexOf(this.pheno.phen.get(i));
/* 3153 */         if (alias[i] < 0) {
/* 3154 */           throw new RuntimeException("!!");
/*      */         }
/*      */       }
/* 3157 */       String str = "";
/* 3158 */       while ((todo.size() > 0) && ((str = br.readLine()) != null))
/*      */       {
/* 3160 */         String[] st = str.split("\t");
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3165 */         String nme = st[0].split("#")[0];
/* 3166 */         if (todo.contains(nme)) {
/* 3167 */           int index = indiv.indexOf(nme);
/*      */           
/*      */ 
/* 3170 */           for (int j = 0; j < alias.length; j++) {
/* 3171 */             res[index][j] = st[alias[j]];
/*      */           }
/* 3173 */           todo.remove(nme);
/*      */         }
/*      */       }
/* 3176 */       Logger.global.info("did not find pheno for " + todo.size() / indiv.size() + "\n" + todo);
/* 3177 */       int[] numLevels = new int[alias.length];
/* 3178 */       Double[][] res1 = new Double[indiv.size()][this.pheno.size()];
/* 3179 */       double[] min = new double[alias.length];
/* 3180 */       double[] max = new double[alias.length];
/* 3181 */       Arrays.fill(max, Double.NEGATIVE_INFINITY);
/* 3182 */       Arrays.fill(min, Double.POSITIVE_INFINITY);
/*      */       
/* 3184 */       for (int i = 0; i < alias.length; i++) {
/* 3185 */         int nonNull = 0;
/* 3186 */         while ((res[nonNull][i] == null) || (res[nonNull][i].equals("NA")) || (res[nonNull][i].length() == 0)) {
/* 3187 */           nonNull++;
/*      */         }
/*      */         
/* 3190 */         if (res[nonNull][i].matches("^[-+]?\\d+(\\.\\d+)?$")) {
/* 3191 */           if (res[nonNull][i].matches("\\D")) {
/* 3192 */             numLevels[i] = 0;
/* 3193 */             for (int k = 0; k < res.length; k++) {
/* 3194 */               if ((res[k][i] != null) && (!res[k][i].equals("NA")) && (res[k][i].length() != 0)) {
/* 3195 */                 res1[k][i] = Double.valueOf(Double.parseDouble(res[k][i]));
/* 3196 */                 if (res1[k][i].doubleValue() < min[i]) min[i] = res1[k][i].doubleValue();
/* 3197 */                 if (res1[k][i].doubleValue() < max[i]) max[i] = res1[k][i].doubleValue();
/*      */               }
/*      */             }
/*      */           }
/*      */           else {
/* 3202 */             this.pheno.phenVals[i] = new HashMap();
/* 3203 */             for (int k = 0; k < res.length; k++) {
/* 3204 */               if ((res[k][i] != null) && (!res[k][i].equals("NA")) && (res[k][i].length() != 0)) {
/* 3205 */                 int val = Integer.parseInt(res[k][i]);
/* 3206 */                 res1[k][i] = Double.valueOf(val);
/* 3207 */                 this.pheno.phenVals[i].put(val, Integer.valueOf(val));
/*      */               }
/*      */             }
/* 3210 */             numLevels[i] = this.pheno.phenVals[i].size();
/* 3211 */             if (numLevels[i] > 10) numLevels[i] = 0;
/* 3212 */             this.pheno.phenVals[i] = null;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 3217 */           this.pheno.phenVals[i] = new HashMap();
/* 3218 */           int curr_lev = 0;
/* 3219 */           for (int k = 0; k < res.length; k++) {
/* 3220 */             if ((res[k][i] != null) && (!res[k][i].equals("NA")) && (res[k][i].length() != 0)) {
/* 3221 */               Integer lev = (Integer)this.pheno.phenVals[i].get(res[k][i]);
/* 3222 */               if (lev == null) {
/* 3223 */                 this.pheno.phenVals[i].put(res[k][i], lev = Integer.valueOf(curr_lev));
/* 3224 */                 curr_lev++;
/*      */               }
/* 3226 */               res1[k][i] = Double.valueOf(lev.doubleValue());
/*      */               
/* 3228 */               numLevels[i] = this.pheno.phenVals[i].size();
/*      */             }
/*      */           }
/* 3231 */           numLevels[i] = this.pheno.phenVals[i].size();
/* 3232 */           if (numLevels[i] > 10) numLevels[i] = 0;
/*      */         }
/*      */       }
/* 3235 */       this.pheno.phenotypeDistribution = new ProbabilityDistribution[this.pheno.size()];
/* 3236 */       for (int k = 0; k < this.pheno.phen.size(); k++) {
/* 3237 */         this.pheno.phenotypeDistribution[k] = (numLevels[k] > 0 ? 
/* 3238 */           new SimpleExtendedDistribution1(numLevels[k]) : 
/* 3239 */           new TrainableNormal(0.0D, 1.0D, 1000.0D, 1.0D));
/*      */       }
/*      */       
/*      */ 
/* 3243 */       for (int i = 0; i < alias.length; i++) {
/* 3244 */         for (int k = 0; k < res1.length; k++) {
/* 3245 */           Double v = res1[k][i];
/* 3246 */           if (v != null) {
/* 3247 */             this.pheno.phenotypeDistribution[i].addCount(v.doubleValue(), 1.0D);
/*      */           }
/*      */         }
/* 3250 */         this.pheno.phenotypeDistribution[i].transfer(0.0D);
/*      */       }
/* 3252 */       for (int k = 0; k < indiv.size(); k++) {
/* 3253 */         ((HaplotypeEmissionState)this.dataL.get(indiv.get(k))).setPhenotype(res1[k]);
/*      */       }
/* 3255 */       return todo;
/*      */     }
/* 3257 */     return new HashSet();
/*      */   }
/*      */   
/*      */   public List<Integer> getRandomIndex(int totalSNPs, double missingRate)
/*      */   {
/* 3262 */     List<Integer> randomIndex = new ArrayList();
/* 3263 */     SortedMap<Double, Integer> temp = new TreeMap();
/* 3264 */     Random ran = new Random();
/* 3265 */     int k = 0;
/* 3266 */     while (k < totalSNPs) {
/* 3267 */       Double tp = Double.valueOf(ran.nextDouble());
/* 3268 */       if (!temp.containsKey(tp)) {
/* 3269 */         temp.put(Double.valueOf(ran.nextDouble()), Integer.valueOf(k));
/* 3270 */         k++;
/*      */       }
/*      */     }
/* 3273 */     int numMissingGenotype = (int)Math.round(missingRate * totalSNPs);
/* 3274 */     int c = 0;
/* 3275 */     if (numMissingGenotype != 0)
/* 3276 */       for (Iterator<Double> it = temp.keySet().iterator(); it.hasNext();) {
/* 3277 */         Double nt = (Double)it.next();
/* 3278 */         randomIndex.add((Integer)temp.get(nt));
/* 3279 */         c++;
/* 3280 */         if (c == numMissingGenotype)
/*      */           break;
/*      */       }
/* 3283 */     return randomIndex;
/*      */   }
/*      */   
/*      */   public void printMissingIndex(PrintStream ps, List<Integer> index, int noSNP, int noInd) {
/* 3287 */     ps.println(noSNP);
/* 3288 */     ps.println(noInd);
/* 3289 */     for (int i = 0; i < index.size(); i++) {
/* 3290 */       ps.println(index.get(i));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3544 */   int no_copies = Constants.noCopies[0];
/*      */   
/*      */   public DataCollection(File f, short index)
/*      */     throws Exception
/*      */   {
/* 3549 */     this.index = index;
/* 3550 */     this.name = f.getParentFile().getName();
/* 3551 */     int[] mid = Constants.mid();
/* 3552 */     File bf = new File(f.getParentFile(), Constants.build(index));
/* 3553 */     int read = 0;
/* 3554 */     List<String> indiv = new ArrayList();
/* 3555 */     ZipFile zf = new ZipFile(f);
/* 3556 */     List<Integer> loc1 = new ArrayList();
/*      */     
/* 3558 */     List<String> headers = Compressor.getIndiv(zf, "Name", null, index);
/* 3559 */     this.header = ((String)headers.get(0)).split("\t");
/* 3560 */     this.header_snp = Arrays.asList(((String)headers.get(1)).split("\t"));
/* 3561 */     this.header_sample = Arrays.asList(((String)headers.get(2)).split("\t"));
/* 3562 */     indiv = Compressor.getIndiv(zf, "Samples", Integer.valueOf(this.header_sample.indexOf("sampleID")), index);
/* 3563 */     for (int i = 0; i < indiv.size(); i++) {
/* 3564 */       indiv.set(i, ((String)indiv.get(i)).split("#")[0]);
/*      */     }
/*      */     
/* 3567 */     List<String> chr = new ArrayList();
/* 3568 */     if ((Constants.mid()[0] > 0) || (Constants.mid().length > 1)) {
/* 3569 */       int[] kb = Constants.restrictKb();
/* 3570 */       Compressor.readBuildFile1(bf, "chr" + Constants.chrom0(), mid[0] - kb[0] * 1000, mid[1] + kb[1] * 1000, 
/* 3571 */         this.loc, chr, this.snpid, 1, 0, 3);
/*      */     }
/*      */     else {
/* 3574 */       throw new RuntimeException("!!");
/*      */     }
/*      */     
/* 3577 */     if (this.loc.size() == 0) throw new RuntimeException("no snps!");
/* 3578 */     this.length = Integer.valueOf(this.loc.size());
/*      */     
/* 3580 */     makeDistributions();
/* 3581 */     String stri = "";
/* 3582 */     this.length = Integer.valueOf(this.loc.size());
/*      */     
/* 3584 */     Logger.global.info("reading pheno");
/*      */     
/* 3586 */     readPhenotypes(new File(f.getParentFile(), "Samples.txt"), new File(f.getParentFile(), "include.txt"), indiv);
/* 3587 */     Logger.global.info("done");
/*      */     
/* 3589 */     List<Integer> randomIndex = getRandomIndex(this.loc.size() * indiv.size(), Constants.missingRate);
/*      */     
/*      */ 
/* 3592 */     if (Constants.missingRate != 0.0D) {
/* 3593 */       PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("missingIndex"))));
/* 3594 */       printMissingIndex(ps, randomIndex, this.loc.size(), indiv.size());
/* 3595 */       ps.close();
/*      */     }
/*      */     
/*      */ 
/* 3599 */     List<String> ls = Compressor.getIndiv(zf, (String)this.snpid.get(0), null, index);
/* 3600 */     if (ls.size() != indiv.size()) throw new RuntimeException("!!");
/* 3601 */     for (int j = 0; j < indiv.size(); j++) {
/* 3602 */       stri = (String)ls.get(j);
/* 3603 */       String[] st = stri.trim().split("\\s+");
/*      */       
/*      */ 
/*      */ 
/* 3607 */       this.stSp = new EmissionStateSpace[this.no_copies];
/* 3608 */       this.stSp1 = new EmissionStateSpace[this.no_copies];
/* 3609 */       for (int k = 0; k < this.stSp.length; k++) {
/* 3610 */         this.stSp[k] = Emiss.getEmissionStateSpace(k, 2);
/* 3611 */         this.stSp1[k] = Emiss.getEmissionStateSpace(k);
/*      */       }
/* 3613 */       this.trans = new EmissionStateSpaceTranslation[this.stSp.length];
/* 3614 */       for (int k = 0; k < this.stSp.length; k++) {
/* 3615 */         this.trans[k] = new EmissionStateSpaceTranslation(this.stSp[k], this.stSp1[k], true);
/*      */       }
/*      */       
/* 3618 */       createDataStructure((String)indiv.get(j), this.no_copies);
/*      */     }
/*      */     
/*      */ 
/* 3622 */     for (int i = 0; i < this.length.intValue(); i++) {
/* 3623 */       String snp_id = (String)this.snpid.get(i);
/* 3624 */       Character majAllele = this.majorAllele.isEmpty() ? null : (Character)this.majorAllele.get(i);
/* 3625 */       Character minAllele = this.minorAllele.isEmpty() ? null : (Character)this.minorAllele.get(i);
/* 3626 */       Double recessiveFreq = this.recesiveGenoFreq.isEmpty() ? null : (Double)this.recesiveGenoFreq.get(i);
/*      */       
/* 3628 */       List<String> l = null;
/*      */       try {
/* 3630 */         l = Compressor.getIndiv(zf, snp_id, null, index);
/*      */       } catch (Exception exc) {
/* 3632 */         System.err.println("prob with " + snp_id);
/* 3633 */         exc.printStackTrace();
/* 3634 */         System.exit(0);
/*      */       }
/* 3636 */       if (l.size() != indiv.size()) throw new RuntimeException("!!");
/* 3637 */       for (int j = 0; j < indiv.size(); j++) {
/* 3638 */         stri = (String)l.get(j);
/* 3639 */         String[] st = stri.trim().split("\\s+");
/*      */         boolean missing;
/* 3641 */         boolean missing; if (randomIndex.contains(Integer.valueOf(j + i * indiv.size()))) missing = true; else
/* 3642 */           missing = false;
/* 3643 */         if (Constants.phasingType == 1) {
/* 3644 */           process((String)indiv.get(j), this.header, st, i, majAllele, minAllele, recessiveFreq, missing);
/*      */         }
/*      */         else
/*      */         {
/* 3648 */           process((String)indiv.get(j), this.header, st, i);
/*      */         }
/*      */       }
/*      */     }
/* 3652 */     if (Constants.loess(index)) {
/* 3653 */       applyLoess(zf);
/*      */     }
/* 3655 */     if (Constants.median_correction(index)) {
/* 3656 */       applyMedianCorrection(zf);
/*      */     }
/* 3658 */     if (this.length.intValue() != this.loc.size()) {
/* 3659 */       throw new RuntimeException("!!");
/*      */     }
/*      */     
/*      */ 
/* 3663 */     calculateMaf(true);
/*      */     
/* 3665 */     zf.close();
/* 3666 */     if (Constants.topBottom()) {
/* 3667 */       i = convertToTopBottom();
/*      */     }
/*      */   }
/*      */   
/*      */   public DataCollection(File f, short index, int no_copies) throws Exception
/*      */   {
/* 3673 */     this.index = index;
/* 3674 */     this.no_copies = no_copies;
/* 3675 */     this.name = f.getParentFile().getName();
/*      */     
/*      */ 
/* 3678 */     int[] mid = Constants.mid();
/* 3679 */     File bf = new File(f.getParentFile(), Constants.build[index]);
/*      */     
/* 3681 */     int read = 0;
/* 3682 */     List<String> indiv = new ArrayList();
/*      */     
/* 3684 */     ZipFile zf = new ZipFile(f);
/* 3685 */     List<Integer> loc1 = new ArrayList();
/*      */     
/* 3687 */     List<String> headers = Compressor.getIndiv(zf, "Name", null, index);
/* 3688 */     this.header = ((String)headers.get(0)).split("\t");
/* 3689 */     this.header_snp = Arrays.asList(((String)headers.get(1)).split("\t"));
/* 3690 */     this.header_sample = Arrays.asList(((String)headers.get(2)).split("\t"));
/*      */     
/* 3692 */     indiv = Compressor.getIndiv(zf, "Samples", Integer.valueOf(this.header_sample.indexOf("sampleID")), index);
/* 3693 */     for (int i = 0; i < indiv.size(); i++) {
/* 3694 */       indiv.set(i, ((String)indiv.get(i)).split("#")[0]);
/*      */     }
/*      */     
/* 3697 */     List<String> chr = new ArrayList();
/* 3698 */     if ((Constants.mid()[0] > 0) || (Constants.mid().length > 1)) {
/* 3699 */       int[] kb = Constants.restrictKb();
/* 3700 */       Compressor.readBuildFile(bf, "chr" + Constants.chrom0(), mid[0] - kb[0] * 1000, mid[1] + kb[1] * 1000, 
/* 3701 */         this.loc, chr, this.snpid, this.majorAllele, this.minorAllele, this.recesiveGenoFreq, 
/* 3702 */         1, 0, 
/* 3703 */         3, 
/* 3704 */         new int[] { 4, 5 }, 6);
/*      */     }
/*      */     else {
/* 3707 */       throw new RuntimeException("!!");
/*      */     }
/*      */     
/*      */ 
/* 3711 */     if (this.loc.size() == 0) throw new RuntimeException("no snps!");
/* 3712 */     this.length = Integer.valueOf(this.loc.size());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3717 */     this.stSp = new EmissionStateSpace[no_copies];
/*      */     
/* 3719 */     this.stSp1 = new EmissionStateSpace[no_copies];
/* 3720 */     for (int i = 0; i < this.stSp.length; i++) {
/* 3721 */       this.stSp[i] = Emiss.getEmissionStateSpace(i, 2);
/* 3722 */       this.stSp1[i] = Emiss.getEmissionStateSpace(i);
/*      */     }
/* 3724 */     this.trans = new EmissionStateSpaceTranslation[this.stSp.length];
/* 3725 */     for (int i = 0; i < this.stSp.length; i++) {
/* 3726 */       this.trans[i] = new EmissionStateSpaceTranslation(this.stSp[i], this.stSp1[i], true);
/*      */     }
/*      */     
/*      */ 
/* 3730 */     makeDistributions();
/* 3731 */     String stri = "";
/* 3732 */     this.length = Integer.valueOf(this.loc.size());
/*      */     
/* 3734 */     createDataStructure(indiv);
/* 3735 */     Logger.global.info("reading pheno");
/*      */     
/* 3737 */     readPhenotypes(new File(f.getParentFile(), "Samples.txt"), new File(f.getParentFile(), "include.txt"), indiv);
/* 3738 */     Logger.global.info("done");
/*      */     
/* 3740 */     List<Integer> randomIndex = getRandomIndex(this.loc.size() * indiv.size(), Constants.missingRate);
/*      */     
/*      */ 
/* 3743 */     PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("missingIndex"))));
/* 3744 */     printMissingIndex(ps, randomIndex, this.loc.size(), indiv.size());
/* 3745 */     ps.close();
/*      */     
/* 3747 */     for (int i = 0; i < this.length.intValue(); i++) {
/* 3748 */       String snp_id = (String)this.snpid.get(i);
/* 3749 */       Character majAllele = this.majorAllele.isEmpty() ? null : (Character)this.majorAllele.get(i);
/* 3750 */       Double recessiveFreq = this.recesiveGenoFreq.isEmpty() ? null : (Double)this.recesiveGenoFreq.get(i);
/* 3751 */       List<String> l = null;
/*      */       try {
/* 3753 */         l = Compressor.getIndiv(zf, snp_id, null, index);
/*      */       } catch (Exception exc) {
/* 3755 */         System.err.println("prob with " + snp_id);
/* 3756 */         exc.printStackTrace();
/* 3757 */         System.exit(0);
/*      */       }
/* 3759 */       if (l.size() != indiv.size()) throw new RuntimeException("!!");
/* 3760 */       for (int j = 0; j < indiv.size(); j++) {
/* 3761 */         stri = (String)l.get(j);
/* 3762 */         String[] st = stri.trim().split("\\s+");
/*      */         boolean missing;
/* 3764 */         boolean missing; if (randomIndex.contains(Integer.valueOf(j + i * indiv.size()))) missing = true; else {
/* 3765 */           missing = false;
/*      */         }
/*      */         
/*      */ 
/* 3769 */         process((String)indiv.get(j), this.header, st, i, majAllele, recessiveFreq, missing);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3775 */     if (Constants.loess(index)) {
/* 3776 */       applyLoess(zf);
/*      */     }
/* 3778 */     if (Constants.median_correction(index)) {
/* 3779 */       applyMedianCorrection(zf);
/*      */     }
/* 3781 */     if (this.length.intValue() != this.loc.size()) {
/* 3782 */       throw new RuntimeException("!!");
/*      */     }
/*      */     
/*      */ 
/* 3786 */     calculateMaf(true);
/*      */     
/* 3788 */     zf.close();
/* 3789 */     if (Constants.topBottom())
/* 3790 */       i = convertToTopBottom();
/*      */   }
/*      */   
/*      */   public void calculateMLGenotypeData(boolean change) {
/* 3794 */     if (change) this.data.clear();
/* 3795 */     for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext();) {
/* 3796 */       EmissionState ld = (EmissionState)it.next();
/*      */       
/* 3798 */       PIGData res = ld.getGenotypeData();
/* 3799 */       if (change) this.data.put(res.getName(), res);
/*      */     }
/*      */   }
/*      */   
/* 3803 */   private Integer whichIndex(String[] strings, String string) { String st1 = string.toLowerCase();
/* 3804 */     for (int i = 0; i < strings.length; i++) {
/* 3805 */       if (strings[i].toLowerCase().indexOf(st1) >= 0) return Integer.valueOf(i);
/*      */     }
/* 3807 */     return null;
/*      */   }
/*      */   
/*      */   public static int firstGreaterThan(List<Integer> loc, int readPos) {
/* 3811 */     int read = 0;
/* 3812 */     for (read = 0; read < loc.size(); read++)
/* 3813 */       if (((Integer)loc.get(read)).intValue() > readPos)
/*      */         break;
/* 3815 */     return read;
/*      */   }
/*      */   
/* 3818 */   public static int getEndIndex(List<Integer> loc) { int readPos = Constants.end().intValue();
/* 3819 */     int read = 0;
/* 3820 */     for (read = loc.size() - 1; read >= 0; read--)
/* 3821 */       if (((Integer)loc.get(read)).intValue() < readPos)
/*      */         break;
/* 3823 */     return read + 1;
/*      */   }
/*      */   
/* 3826 */   public List<Integer> randPos(double d) { List<Integer> l = new ArrayList();
/*      */     
/* 3828 */     for (int i = 0; i < length(); i++) {
/* 3829 */       double nxt = Constants.rand.nextDouble();
/* 3830 */       if (nxt < d) {
/* 3831 */         l.add(Integer.valueOf(i));
/*      */       }
/*      */     }
/* 3834 */     return l;
/*      */   }
/*      */   
/*      */ 
/*      */   public void dropRandom(double d, boolean setAsMissing)
/*      */   {
/* 3840 */     drop(randPos(d), setAsMissing);
/*      */   }
/*      */   
/*      */   public final String toString()
/*      */   {
/* 3845 */     StringWriter sw = new StringWriter();
/* 3846 */     PrintWriter pw_hap2 = new PrintWriter(new BufferedWriter(sw));
/*      */     try {
/* 3848 */       writeFastphase(pw_hap2, false);
/*      */     } catch (Exception exc) {
/* 3850 */       exc.printStackTrace();
/*      */     }
/* 3852 */     return sw.getBuffer().toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void dropIndiv(String[] toDel)
/*      */   {
/* 3870 */     for (int i = 0; i < toDel.length; i++) {
/* 3871 */       this.dataL.remove(toDel[i]);
/* 3872 */       this.data.remove(toDel[i]);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/collection/DataCollection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */