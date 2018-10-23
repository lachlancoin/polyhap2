/*     */ package lc1.dp.data.collection;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import lc1.dp.data.representation.CSOData;
/*     */ import lc1.dp.data.representation.ComparableArray;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ import lc1.dp.data.representation.SimpleScorableObject;
/*     */ import lc1.dp.emissionspace.CompoundEmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpaceTranslation;
/*     */ import lc1.dp.states.HaplotypeEmissionState;
/*     */ import lc1.dp.states.PhasedDataState;
/*     */ import lc1.ensj.PedigreeDataCollection;
/*     */ import lc1.stats.IntegerDistribution;
/*     */ import lc1.stats.PseudoDistribution;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class SimpleDataCollection extends DataCollection
/*     */ {
/*     */   public SimpleDataCollection() {}
/*     */   
/*     */   protected SimpleDataCollection(DataCollection dat)
/*     */   {
/*  37 */     super(dat);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void readIllumina(File f, SimpleDataCollection sdt)
/*     */     throws Exception
/*     */   {
/*  44 */     List<Integer> loc1 = readPosInfo(f, 4, true);
/*     */     int end;
/*  46 */     if (Constants.mid()[0] > 0) {
/*  47 */       int[] mid = Constants.mid();
/*  48 */       int mid_index = IlluminaRDataCollection.firstGreaterThan(loc1, mid[0]);
/*  49 */       int mid_index1 = IlluminaRDataCollection.firstGreaterThan(loc1, mid[1]);
/*  50 */       int read = mid_index - (int)Math.round(Constants.restrict()[0] / 2.0D);
/*  51 */       end = mid_index1 + (int)Math.round(Constants.restrict()[0] / 2.0D);
/*     */     }
/*     */     else {
/*  54 */       read = IlluminaRDataCollection.firstGreaterThan(loc1, Constants.offset());
/*  55 */       end = Math.min(IlluminaRDataCollection.getEndIndex(loc1), read + Constants.restrict()[0]);
/*     */     }
/*     */     
/*  58 */     int read = Math.max(0, read);
/*  59 */     int end = Math.min(end, loc1.size());
/*  60 */     sdt.loc = new ArrayList(loc1.subList(read, end));
/*  61 */     sdt.snpid = new ArrayList();
/*  62 */     sdt.length = Integer.valueOf(sdt.loc.size());
/*     */     
/*     */ 
/*  65 */     File dir1 = new File(Constants.getDirFile());
/*  66 */     BufferedReader br = new BufferedReader(new java.io.FileReader(f));
/*  67 */     CompoundEmissionStateSpace stSp = Emiss.getEmissionStateSpace(1, 2);
/*  68 */     CompoundEmissionStateSpace stSp1 = Emiss.getEmissionStateSpace(1);
/*  69 */     EmissionStateSpaceTranslation trans = new EmissionStateSpaceTranslation(stSp, stSp1, true);
/*     */     
/*  71 */     String[] str = br.readLine().split("\\t");
/*     */     
/*     */ 
/*  74 */     int len = str.length - 5;
/*  75 */     String[] indv = new String[len / 3];
/*  76 */     PIGData[] data = new PIGData[indv.length];
/*  77 */     int i1 = 0;
/*     */     
/*  79 */     StringBuffer sb = new StringBuffer();
/*  80 */     for (int i = 0; i < sdt.loc.size(); i++) {
/*  81 */       sb.append("%5.3f ");
/*     */     }
/*     */     
/*  84 */     for (int i = 5; i < str.length; i += 3) {
/*  85 */       indv[i1] = str[i].substring(0, str[i].indexOf(".GType"));
/*     */       
/*  87 */       data[i1] = SimpleScorableObject.make(indv[i1], sdt.loc.size(), stSp);
/*  88 */       i1++;
/*     */     }
/*  90 */     for (int i = 0; i < read; i++) {
/*  91 */       br.readLine();
/*     */     }
/*  93 */     double min = Double.POSITIVE_INFINITY;
/*  94 */     double max = Double.NEGATIVE_INFINITY;
/*  95 */     for (int i = 0; i < sdt.length.intValue(); i++) {
/*  96 */       String[] st = br.readLine().split("\\t");
/*  97 */       int loc = Integer.parseInt(st[4]);
/*  98 */       sdt.snpid.add(st[1]);
/*  99 */       if (loc != ((Integer)sdt.loc.get(i)).intValue()) throw new RuntimeException("!!");
/* 100 */       for (int j = 0; j < indv.length; j++)
/*     */       {
/* 102 */         String geno = st[(5 + j * 3)];
/* 103 */         if (geno.equals("NC")) geno = "NN";
/* 104 */         double b = Double.parseDouble(st[(6 + j * 3)]);
/* 105 */         double r = Double.parseDouble(st[(7 + j * 3)]);
/* 106 */         if (r < min) min = r;
/* 107 */         if (r > max) max = r;
/* 108 */         geno = geno.replaceAll("N", "").replaceAll("_", "");
/* 109 */         int comp = trans.bigToSmall(stSp.getFromString(geno).intValue()).intValue();
/* 110 */         data[j].addPoint(i, stSp1.get(comp));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 115 */     sdt.length = Integer.valueOf(sdt.size());
/* 116 */     for (int i = 0; i < data.length; i++) {
/* 117 */       sdt.data.put(data[i].getName(), data[i]);
/*     */     }
/* 119 */     sdt.calculateMaf(false);
/* 120 */     br.close();
/*     */   }
/*     */   
/*     */   public static String[] readAffy(File dir1, String name, boolean quotes, SimpleDataCollection res, int noCopies)
/*     */     throws Exception
/*     */   {
/* 126 */     EmissionStateSpace emStSp = Emiss.getEmissionStateSpace(noCopies - 1, 2);
/* 127 */     EmissionStateSpace emStSp1 = Emiss.getEmissionStateSpace(noCopies - 1);
/* 128 */     EmissionStateSpaceTranslation trans = new EmissionStateSpaceTranslation(emStSp, emStSp1, true);
/* 129 */     File f = new File(dir1, name);
/* 130 */     res.loc = DataCollection.readPosInfo(f, 4, true);
/* 131 */     int read = IlluminaRDataCollection.firstGreaterThan(res.loc, Constants.offset());
/* 132 */     res.loc = res.loc.subList(read, Math.min(read + Constants.restrict()[0], res.loc.size()));
/* 133 */     BufferedReader br = DataCollection.getBufferedReader(f);
/* 134 */     String st = br.readLine().trim();
/* 135 */     boolean header = st.toUpperCase().indexOf("CHR") >= 0;
/* 136 */     PIGData[] dat = (PIGData[])null;
/*     */     String[] indiv;
/* 138 */     if (header) {
/* 139 */       String[] str = st.trim().split("\\s+");
/* 140 */       String[] indiv = new String[str.length - 2];
/* 141 */       System.arraycopy(str, 2, indiv, 0, indiv.length);
/* 142 */       st = br.readLine();
/*     */     } else {
/* 144 */       indiv = readIndiv(new File(dir1, "indiv.txt")); }
/* 145 */     for (int i = 0; i < read; i++) {
/* 146 */       st = br.readLine();
/*     */     }
/*     */     
/* 149 */     for (int ik1 = 0; (st != null) && (ik1 < res.loc.size()); ik1++) {
/* 150 */       String[] str = st.trim().replaceAll("No Call", "NN").split("\\s+");
/* 151 */       if (dat == null) {
/* 152 */         dat = new PIGData[str.length - 2];
/* 153 */         for (int i = 0; i < dat.length; i++) {
/* 154 */           dat[i] = SimpleScorableObject.make(indiv[i], 10, emStSp);
/*     */         }
/*     */       }
/* 157 */       for (int i = 2; i < str.length; i++) {
/* 158 */         String sti = str[i];
/*     */         
/* 160 */         if (quotes) sti = sti.substring(1, sti.length() - 1);
/* 161 */         char[] c = sti.toCharArray();
/* 162 */         Arrays.sort(c);
/*     */         
/* 164 */         StringBuffer sb = new StringBuffer();
/* 165 */         for (int ik = 0; ik < c.length; ik++) {
/* 166 */           if ((c[ik] != '_') && (c[ik] != '-')) {
/* 167 */             sb.append(c[ik]);
/*     */           }
/*     */         }
/* 170 */         sti = sb.toString();
/*     */         
/* 172 */         int comp = trans.bigToSmall(emStSp.getFromString(sti).intValue()).intValue();
/* 173 */         dat[(i - 2)].addPoint(ik1, emStSp1.get(comp));
/*     */       }
/* 175 */       st = br.readLine();
/*     */     }
/* 177 */     for (int i = 0; i < dat.length; i++) {
/* 178 */       res.data.put(dat[i].getName(), dat[i]);
/*     */     }
/* 180 */     res.length = Integer.valueOf(dat[0].length());
/* 181 */     return indiv;
/*     */   }
/*     */   
/*     */   private static ComparableArray getArray(String sti, int noC) throws Exception {
/* 185 */     if (noC == 2) {
/* 186 */       if (sti.length() == 0) sti = "NN";
/* 187 */       if (sti.length() == 1) sti = new String(sti + "N");
/* 188 */       int half = (int)Math.floor(sti.length() / 2.0D);
/* 189 */       return ComparableArray.make(Emiss.get(sti.substring(0, half)), Emiss.get(sti.substring(half)));
/*     */     }
/* 191 */     if (noC == 1) {
/* 192 */       if (sti.length() == 0) { sti = "N";
/*     */       }
/* 194 */       return ComparableArray.make(Emiss.get(sti));
/*     */     }
/*     */     
/*     */ 
/* 198 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void selectMale(boolean male) {
/* 202 */     Set<String> alias = new HashSet();
/* 203 */     for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/* 204 */       PIGData nxt = (PIGData)it.next();
/* 205 */       if ((nxt.noCopies() == 1) || (nxt.noCopies() == 3)) {
/* 206 */         if (male) { alias.add(nxt.getName());
/*     */         }
/*     */       }
/* 209 */       else if (!male) { alias.add(nxt.getName());
/*     */       }
/*     */     }
/* 212 */     restricToAlias(alias);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void selectMaleTrios()
/*     */   {
/* 219 */     Set<String> alias = new HashSet();
/* 220 */     for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/* 221 */       PIGData nxt = (PIGData)it.next();
/* 222 */       ComparableArray arr = (ComparableArray)((ComparableArray)nxt.getElement(0)).get(2);
/* 223 */       if (arr.size() == 1) {
/* 224 */         alias.add(nxt.getName());
/*     */       }
/*     */     }
/* 227 */     restricToAlias(alias);
/*     */   }
/*     */   
/*     */   public int size() {
/* 231 */     return this.data.size();
/*     */   }
/*     */   
/*     */   public List<String[]> getPairings(double[] fraction, List<String> keys) {
/* 235 */     int no_copies = Constants.sample(fraction) + 1;
/* 236 */     List<String[]> res = new ArrayList();
/* 237 */     while (keys.size() >= no_copies) {
/* 238 */       String[] keys1 = new String[no_copies];
/* 239 */       res.add(keys1);
/* 240 */       for (int i = 0; i < no_copies; i++) {
/* 241 */         int i1 = 
/* 242 */           no_copies == 1 ? 0 : 
/* 243 */           Constants.nextInt(keys.size());
/* 244 */         keys1[i] = ((String)keys.remove(i1));
/*     */       }
/* 246 */       no_copies = Constants.sample(fraction) + 1;
/*     */     }
/*     */     
/* 249 */     return res;
/*     */   }
/*     */   
/* 252 */   public void transform(double[] fraction, List<String> keys, String join) { transform(getPairings(fraction, keys).iterator(), join); }
/*     */   
/*     */   public void transform(List<String> names) {
/* 255 */     List<String[]> res = new ArrayList();
/* 256 */     for (Iterator<String> it = names.iterator(); it.hasNext();) {
/* 257 */       res.add(((String)it.next()).split("\\."));
/*     */     }
/* 259 */     transform(res.iterator(), ".");
/*     */   }
/*     */   
/*     */   public void transform(Iterator<String[]> pairs, String join)
/*     */   {
/* 264 */     while (pairs.hasNext())
/*     */     {
/* 266 */       String[] keys1 = (String[])pairs.next();
/* 267 */       CSOData[] unit = new CSOData[keys1.length];
/* 268 */       SortedMap[] recSites = new SortedMap[keys1.length];
/* 269 */       String[] parents = new String[keys1.length];
/* 270 */       for (int i = 0; i < unit.length; i++)
/*     */       {
/* 272 */         String str = keys1[i];
/* 273 */         parents[i] = ((this.ped != null) && (this.ped.mother != null) ? (String)this.ped.mother.remove(str) : null);
/* 274 */         unit[i] = ((CSOData)this.data.remove(str));
/* 275 */         if ((recSites != null) && (this.recSites != null)) {
/* 276 */           SortedMap[] tmp = (SortedMap[])this.recSites.remove(str);
/* 277 */           if (tmp != null) recSites[i] = tmp[0];
/*     */         }
/*     */       }
/* 280 */       PIGData pid = SimpleScorableObject.make(unit, unit[0].noCopies() == 1, join);
/* 281 */       if (parents[0] != null) {
/* 282 */         this.ped.mother.put(pid.getName(), parents[0]);
/*     */       }
/* 284 */       if ((parents.length > 1) && (parents[1] != null)) {
/* 285 */         this.ped.father.put(pid.getName(), parents[1]);
/*     */       }
/* 287 */       if (recSites[0] != null) {
/* 288 */         this.recSites.put(pid.getName(), recSites);
/*     */       }
/* 290 */       this.data.put(pid.getName(), pid);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void rename()
/*     */   {
/* 297 */     int ik = 0;
/* 298 */     Map<String, PIGData> newL = new HashMap();
/* 299 */     for (Iterator<String> it = this.data.keySet().iterator(); it.hasNext(); ik++) {
/* 300 */       String key = (String)it.next();
/* 301 */       PIGData nxt = (PIGData)this.data.get(key);
/* 302 */       nxt.setName("pair" + ik);
/* 303 */       newL.put(nxt.getName(), nxt);
/*     */     }
/*     */     
/* 306 */     this.data = newL;
/*     */   }
/*     */   
/*     */   public void makeTrioData() {
/* 310 */     this.ped = new PedigreeDataCollection();
/* 311 */     List<String> l = new ArrayList(this.data.keySet());
/* 312 */     int ik = 0;
/* 313 */     for (Iterator<String> it = l.iterator(); it.hasNext(); ik++) {
/* 314 */       String key = (String)it.next();
/* 315 */       PIGData dat_i = (PIGData)this.data.get(key);
/* 316 */       SortedMap<Integer, Integer> rec_sites = getRecSites(Constants.hotspot(2), dat_i.getName() + "r");
/* 317 */       int startingIndex = Constants.nextInt(2);
/* 318 */       System.err.println("sw " + ik + " " + rec_sites);
/* 319 */       PIGData child = dat_i.recombine(rec_sites, startingIndex);
/* 320 */       this.ped.mother.put(child.getName(), dat_i.getName());
/* 321 */       this.data.put(child.getName(), child);
/* 322 */       this.recSites.put(child.getName(), new SortedMap[] { rec_sites });
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<PIGData> iterator()
/*     */   {
/* 363 */     return this.data.values().iterator();
/*     */   }
/*     */   
/* 366 */   public SimpleDataCollection(int length) { super(length); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleDataCollection(List<PIGData> name)
/*     */   {
/* 377 */     this(((PIGData)name.get(0)).length());
/* 378 */     for (Iterator<PIGData> it = name.iterator(); it.hasNext();) {
/* 379 */       PIGData nxt = (PIGData)it.next();
/* 380 */       this.data.put(nxt.getName(), nxt);
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
/*     */   public SimpleDataCollection(File file, short index, int no_copies)
/*     */     throws Exception
/*     */   {
/* 395 */     super(file, index, no_copies);
/*     */   }
/*     */   
/*     */   public SimpleDataCollection(File file, short index)
/*     */     throws Exception
/*     */   {
/* 401 */     super(file, index);
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
/*     */   public SimpleDataCollection clone()
/*     */   {
/* 415 */     return new SimpleDataCollection(this);
/*     */   }
/*     */   
/*     */   public int getNumMissAllele(String st) {
/* 419 */     int count = 0;
/* 420 */     if (st.equals("NA")) {
/* 421 */       count = 9;
/*     */     }
/*     */     else {
/* 424 */       for (int i = 0; i < st.length(); i++) {
/* 425 */         if (st.charAt(i) == 'N') count++;
/*     */       }
/*     */     }
/* 428 */     return count;
/*     */   }
/*     */   
/*     */   public int[] getNumAllele(String st) {
/* 432 */     int[] count = new int[3];
/* 433 */     for (int i = 0; i < st.length(); i++) {
/* 434 */       if (st.charAt(i) == 'A') count[0] += 1;
/* 435 */       if (st.charAt(i) == 'B') count[1] += 1;
/* 436 */       if (st.charAt(i) == '_') count[2] += 1;
/*     */     }
/* 438 */     return count;
/*     */   }
/*     */   
/*     */   public void process(String indiv, int i)
/*     */   {
/*     */     try {
/* 444 */       PhasedDataState data = (PhasedDataState)this.data.get(indiv);
/* 445 */       HaplotypeEmissionState dataSt = (HaplotypeEmissionState)this.dataL.get(indiv);
/* 446 */       int no_copies = data.noCopies();
/* 447 */       EmissionStateSpace stsp = data.getEmissionStateSpace();
/* 448 */       double[] dist = new double[stsp.size()];
/* 449 */       double sum = 0.0D;
/* 450 */       for (int ij = 0; ij < dist.length; ij++) {
/* 451 */         if (stsp.getCN(ij) == no_copies) {
/* 452 */           dist[ij] = 1.0D;
/* 453 */           sum += 1.0D;
/*     */         }
/*     */       }
/* 456 */       for (int ij = 0; ij < dist.length; ij++) {
/* 457 */         dist[ij] /= sum;
/*     */       }
/* 459 */       if (data.emissions[i] == null) {
/* 460 */         data.emissions[i] = new IntegerDistribution(0);
/* 461 */         dataSt.emissions[i] = new SimpleExtendedDistribution(dist, Double.POSITIVE_INFINITY);
/*     */       }
/* 463 */       data.emissions[i].setDataIndex(this.index);
/*     */     } catch (Exception exc) {
/* 465 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int[] getRealGeno(ComparableArray codedGeno)
/*     */   {
/* 472 */     int[] count = new int[3];
/* 473 */     List<Emiss> temp = codedGeno.elements();
/* 474 */     for (int i = 0; i < codedGeno.elements().size(); i++) {
/* 475 */       String a = codedGeno.elements().get(i).toString();
/* 476 */       if (a.equals("_")) { count[0] += 1;
/* 477 */       } else if (a.equals("A")) { count[1] += 1;
/* 478 */       } else if (a.equals("B")) { count[2] += 1;
/* 479 */       } else if (a.equals("X")) { count[1] += 2;
/* 480 */       } else if (a.equals("Z")) { count[2] += 2;
/* 481 */       } else if (a.equals("T")) { count[1] += 3;
/* 482 */       } else if (a.equals("W")) { count[2] += 3;
/* 483 */       } else if (a.equals("Y")) { count[1] += 1;count[2] += 1;
/* 484 */       } else if (a.equals("U")) { count[1] += 2;count[2] += 1;
/* 485 */       } else if (a.equals("V")) { count[1] += 1;count[2] += 2;
/* 486 */       } else { throw new RuntimeException("!!");
/*     */       } }
/* 488 */     return count;
/*     */   }
/*     */   
/*     */   public void process(String indiv, String[] header, String[] geno, int i)
/*     */   {
/*     */     try {
/* 494 */       PhasedDataState data = (PhasedDataState)this.data.get(indiv);
/* 495 */       HaplotypeEmissionState dataSt = (HaplotypeEmissionState)this.dataL.get(indiv);
/* 496 */       int no_copies = data.noCopies();
/* 497 */       EmissionStateSpace stsp = data.getEmissionStateSpace();
/* 498 */       List<Integer> callingScoreIndex = new ArrayList();
/*     */       
/* 500 */       for (int c = 1; c < 4; c++) { int index;
/* 501 */         if ((index = getIndex(header, "call" + c)) != -1) {
/* 502 */           callingScoreIndex.add(Integer.valueOf(index));
/*     */         }
/*     */       }
/* 505 */       double[] callingScore = new double[callingScoreIndex.size()];
/* 506 */       for (int c = 0; c < callingScoreIndex.size(); c++) {
/* 507 */         callingScore[c] = Double.parseDouble(geno[((Integer)callingScoreIndex.get(c)).intValue()]);
/*     */       }
/* 509 */       double[] dist = new double[stsp.size()];
/* 510 */       double sum = 0.0D;
/*     */       
/* 512 */       Comparable[] comp = new Comparable[geno[0].length()];
/* 513 */       for (int ik = 0; ik < geno[0].length(); ik++) {
/* 514 */         comp[ik] = Emiss.translate(geno[0].charAt(ik));
/*     */       }
/* 516 */       Integer missingAllele = Integer.valueOf(getNumMissAllele(geno[0]));
/* 517 */       List<ComparableArray> genoList = stsp.defaultList;
/* 518 */       if (dist.length != genoList.size()) throw new RuntimeException("!!");
/* 519 */       if (missingAllele.intValue() == 0) {
/* 520 */         Arrays.fill(dist, 0.0D);
/* 521 */         int[] genoCount = getRealGeno(new ComparableArray(comp));
/* 522 */         for (int ij = 0; ij < dist.length; ij++) {
/* 523 */           int[] tempCount = getRealGeno((ComparableArray)genoList.get(ij));
/* 524 */           if ((genoCount[0] == tempCount[0]) && (genoCount[1] == tempCount[1]) && (genoCount[2] == tempCount[2])) {
/* 525 */             dist[ij] = 1.0D;
/* 526 */             sum += 1.0D;
/*     */           }
/*     */         }
/*     */         
/* 530 */         for (int ij = 0; ij < dist.length; ij++) {
/* 531 */           dist[ij] /= sum;
/*     */         }
/*     */       }
/* 534 */       else if (missingAllele.intValue() == 9) {
/* 535 */         Arrays.fill(dist, 1.0D);
/* 536 */         for (int ij = 0; ij < dist.length; ij++) {
/* 537 */           dist[ij] /= dist.length;
/*     */         }
/*     */       }
/* 540 */       else if (missingAllele.intValue() == 1) {
/* 541 */         Arrays.fill(dist, 0.0D);
/* 542 */         for (int ij = 0; ij < dist.length; ij++) {
/* 543 */           int[] tempCount = getRealGeno((ComparableArray)genoList.get(ij));
/* 544 */           int noCN = tempCount[1] + tempCount[2];
/* 545 */           if ((tempCount[0] == 1) && (noCN == 1)) {
/* 546 */             dist[ij] = 1.0D;
/* 547 */             sum += 1.0D;
/*     */           }
/*     */         }
/* 550 */         for (int ij = 0; ij < dist.length; ij++) {
/* 551 */           dist[ij] /= sum;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 556 */         Arrays.fill(dist, 0.0D);
/* 557 */         for (int ij = 0; ij < dist.length; ij++) {
/* 558 */           int[] tempCount = getRealGeno((ComparableArray)genoList.get(ij));
/* 559 */           int noCN = tempCount[1] + tempCount[2];
/* 560 */           if ((tempCount[0] == 0) && (noCN == missingAllele.intValue())) {
/* 561 */             dist[ij] = 1.0D;
/* 562 */             sum += 1.0D;
/*     */           }
/*     */         }
/* 565 */         for (int ij = 0; ij < dist.length; ij++) {
/* 566 */           dist[ij] /= sum;
/*     */         }
/*     */       }
/*     */       
/* 570 */       if (data.emissions[i] == null) {
/* 571 */         data.emissions[i] = new IntegerDistribution(0);
/*     */         
/*     */ 
/* 574 */         dataSt.emissions[i] = new SimpleExtendedDistribution(dist, Double.POSITIVE_INFINITY);
/*     */       }
/*     */       
/* 577 */       data.emissions[i].setDataIndex(this.index);
/*     */     } catch (Exception exc) {
/* 579 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void process(String indiv, String[] header, String[] geno, int i, Character majorAllele, Character minorAllele, Double recessiveFreq, boolean missing)
/*     */   {
/*     */     try
/*     */     {
/* 589 */       PhasedDataState data = (PhasedDataState)this.data.get(indiv);
/* 590 */       HaplotypeEmissionState dataSt = (HaplotypeEmissionState)this.dataL.get(indiv);
/* 591 */       int no_copies = data.noCopies();
/* 592 */       EmissionStateSpace stsp = data.getEmissionStateSpace();
/* 593 */       List<Integer> callingScoreIndex = new ArrayList();
/*     */       
/* 595 */       for (int c = 1; c < 4; c++) { int index;
/* 596 */         if ((index = getIndex(header, "call" + c)) != -1) {
/* 597 */           callingScoreIndex.add(Integer.valueOf(index));
/*     */         }
/*     */       }
/* 600 */       double[] callingScore = new double[callingScoreIndex.size()];
/* 601 */       for (int c = 0; c < callingScoreIndex.size(); c++) {
/* 602 */         callingScore[c] = Double.parseDouble(geno[((Integer)callingScoreIndex.get(c)).intValue()]);
/*     */       }
/* 604 */       double[] dist = new double[stsp.size()];
/* 605 */       double sum = 0.0D;
/* 606 */       Integer ind = trans(geno[0], data);
/* 607 */       int numMissAllele = getNumMissAllele(geno[0]);
/* 608 */       Integer majIndex = null;
/* 609 */       if ((majorAllele != null) && (!Constants.topBottom)) {
/* 610 */         majIndex = trans(majorAllele.toString() + majorAllele.toString());
/*     */       }
/* 612 */       if ((numMissAllele == no_copies) || (numMissAllele == 0))
/*     */       {
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
/* 635 */         for (int ij = 1; ij < dist.length; ij++)
/*     */         {
/* 637 */           dist[ij] = 1.0D;
/* 638 */           sum += 1.0D;
/*     */         }
/*     */         
/* 641 */         for (int ij = 0; ij < dist.length; ij++) {
/* 642 */           dist[ij] /= sum;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 647 */         List<Comparable> genoList = stsp.defaultList;
/* 648 */         if (dist.length != genoList.size()) throw new RuntimeException("!!");
/* 649 */         for (int ij = 1; ij < dist.length; ij++) {
/* 650 */           int[] numAlleleData = getNumAllele(geno[0]);
/*     */           
/* 652 */           String ge = ((Comparable)genoList.get(ij)).toString();
/* 653 */           int[] numAllele = getNumAllele(ge);
/* 654 */           if ((numAllele[0] >= numAlleleData[0]) && (numAllele[1] >= numAlleleData[1]) && (numAllele[2] >= numAlleleData[2])) {
/* 655 */             dist[ij] = 1.0D;
/* 656 */             sum += 1.0D;
/*     */           } else {
/* 658 */             dist[ij] = 0.0D;
/*     */           }
/*     */         }
/* 661 */         for (int ij = 0; ij < dist.length; ij++) {
/* 662 */           dist[ij] /= sum;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 667 */       if (data.emissions[i] == null) {
/* 668 */         data.emissions[i] = (ind == null ? new IntegerDistribution(0) : new IntegerDistribution(ind.intValue()));
/* 669 */         if ((ind == null) || (missing)) {
/* 670 */           dataSt.emissions[i] = new SimpleExtendedDistribution(dist, Double.POSITIVE_INFINITY);
/*     */         }
/* 672 */         else if ((stsp.getCN(ind.intValue()) != 0) && (callingScore.length == 1) && (callingScore[0] < 0.95D)) {
/* 673 */           dist = new double[stsp.size()];
/* 674 */           int genoIndex = stsp.getHaploPairFromHaplo(ind.intValue());
/* 675 */           for (int s = 0; s < dist.length; s++) {
/* 676 */             if (stsp.getCN(s) == no_copies) {
/* 677 */               dist[s] = ((1.0D - callingScore[0]) / (dist.length - 1));
/*     */             }
/*     */           }
/* 680 */           dist[genoIndex] = callingScore[0];
/* 681 */           dataSt.emissions[i] = new SimpleExtendedDistribution(dist, Double.POSITIVE_INFINITY);
/*     */         }
/* 683 */         else if ((stsp.getCN(ind.intValue()) != 0) && (callingScore.length == 3)) {
/* 684 */           dist = callingScore;
/* 685 */           int max = Constants.getMax(dist);
/* 686 */           if (dist.length != callingScore.length) throw new RuntimeException("!!");
/* 687 */           if (dist[max] > 0.95D) {
/* 688 */             dataSt.emissions[i] = new IntegerDistribution(max);
/*     */           }
/*     */           else {
/* 691 */             dataSt.emissions[i] = new SimpleExtendedDistribution(dist, Double.POSITIVE_INFINITY);
/*     */           }
/*     */         }
/*     */         else {
/* 695 */           dataSt.emissions[i] = new IntegerDistribution(ind.intValue());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 702 */       data.emissions[i].setDataIndex(this.index);
/*     */     } catch (Exception exc) {
/* 704 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void process(String indiv, String[] header, String[] geno, int i, Character majorAllele, Double recessiveFreq, boolean missing)
/*     */   {
/*     */     try
/*     */     {
/* 713 */       EmissionStateSpace stsp = this.stSp[(this.no_copies - 1)];
/* 714 */       List<Integer> callingScoreIndex = new ArrayList();
/*     */       
/* 716 */       for (int c = 1; c < 4; c++) { int index;
/* 717 */         if ((index = getIndex(header, "call" + c)) != -1) {
/* 718 */           callingScoreIndex.add(Integer.valueOf(index));
/*     */         }
/*     */       }
/* 721 */       double[] callingScore = new double[callingScoreIndex.size()];
/* 722 */       for (int c = 0; c < callingScoreIndex.size(); c++) {
/* 723 */         callingScore[c] = Double.parseDouble(geno[((Integer)callingScoreIndex.get(c)).intValue()]);
/*     */       }
/* 725 */       double[] dist = new double[stsp.size()];
/* 726 */       double sum = 0.0D;
/* 727 */       Integer majIndex = Integer.valueOf(majorAllele == null ? -1 : trans(majorAllele.toString() + majorAllele.toString()).intValue());
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
/* 748 */       for (int ij = 0; ij < dist.length; ij++) {
/* 749 */         if (stsp.getCN(ij) == this.no_copies) {
/* 750 */           if (majIndex.intValue() == ij) {
/* 751 */             dist[ij] = 0.0D;
/*     */           }
/*     */           else {
/* 754 */             dist[ij] = 1.0D;
/* 755 */             sum += 1.0D;
/*     */           }
/*     */         }
/*     */       }
/* 759 */       for (int ij = 0; ij < dist.length; ij++) {
/* 760 */         dist[ij] /= sum;
/*     */       }
/*     */       
/*     */ 
/* 764 */       PhasedDataState data = (PhasedDataState)this.data.get(indiv);
/* 765 */       HaplotypeEmissionState dataSt = (HaplotypeEmissionState)this.dataL.get(indiv);
/* 766 */       for (int k = 0; k < header.length; k++) {
/* 767 */         if ((header[k].toLowerCase().indexOf("geno") >= 0) && 
/* 768 */           (data.emissions[i] == null)) {
/* 769 */           Integer ind = trans(geno[k]);
/* 770 */           data.emissions[i] = (ind == null ? new IntegerDistribution(0) : new IntegerDistribution(ind.intValue()));
/* 771 */           if ((ind == null) || (missing)) {
/* 772 */             dataSt.emissions[i] = new SimpleExtendedDistribution(dist, Double.POSITIVE_INFINITY);
/*     */           }
/* 774 */           else if ((stsp.getCN(ind.intValue()) != 0) && (callingScore.length == 1)) {
/* 775 */             dist = new double[stsp.size()];
/* 776 */             int genoIndex = stsp.getHaploPairFromHaplo(ind.intValue());
/* 777 */             for (int s = 0; s < dist.length; s++) {
/* 778 */               if (stsp.getCN(s) == this.no_copies) {
/* 779 */                 dist[s] = ((1.0D - callingScore[0]) / (dist.length - 1));
/*     */               }
/*     */             }
/* 782 */             dist[genoIndex] = callingScore[0];
/* 783 */             dataSt.emissions[i] = new SimpleExtendedDistribution(dist, Double.POSITIVE_INFINITY);
/*     */           }
/* 785 */           else if ((stsp.getCN(ind.intValue()) != 0) && (callingScore.length == 3)) {
/* 786 */             dist = callingScore;
/* 787 */             if (dist.length != callingScore.length) throw new RuntimeException("!!");
/* 788 */             dataSt.emissions[i] = new SimpleExtendedDistribution(dist, Double.POSITIVE_INFINITY);
/*     */           }
/*     */           else {
/* 791 */             dataSt.emissions[i] = new IntegerDistribution(ind.intValue());
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 798 */       data.emissions[i].setDataIndex(this.index);
/*     */     } catch (Exception exc) {
/* 800 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected int getIndex(String[] header, String string)
/*     */   {
/* 807 */     String[] header1 = header[0].split("\\s+");
/* 808 */     for (int i = 0; i < header1.length; i++) {
/* 809 */       if (header1[i].startsWith(string)) return i;
/*     */     }
/* 811 */     return -1;
/*     */   }
/*     */   
/* 814 */   public void removeKeyIfStartsWith(String st) { for (Iterator<String> it = this.data.keySet().iterator(); it.hasNext();) {
/* 815 */       String key = (String)it.next();
/* 816 */       if ((key.startsWith(st)) && (!st.equals("NA12239"))) {
/* 817 */         this.recSites.remove(key);
/* 818 */         this.viterbi.remove(key);
/* 819 */         it.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void createDataStructure(List<String> indiv)
/*     */   {
/* 826 */     for (int i = 0; i < indiv.size(); i++) {
/* 827 */       PhasedDataState emst = SimpleScorableObject.make((String)indiv.get(i), this.loc.size(), this.stSp[(this.no_copies - 1)]);
/*     */       
/* 829 */       this.dataL.put((String)indiv.get(i), emst);
/* 830 */       this.data.put((String)indiv.get(i), SimpleScorableObject.make((String)indiv.get(i), this.loc.size(), this.stSp[(this.no_copies - 1)]));
/*     */     }
/*     */   }
/*     */   
/*     */   public void createDataStructure(String indiv, int no_copies) {
/* 835 */     PhasedDataState emst = SimpleScorableObject.make(indiv, this.loc.size(), this.stSp[(no_copies - 1)]);
/* 836 */     this.dataL.put(indiv, emst);
/* 837 */     this.data.put(indiv, SimpleScorableObject.make(indiv, this.loc.size(), this.stSp[(no_copies - 1)]));
/*     */   }
/*     */   
/*     */   public void restrictDataSites(int i) {
/* 841 */     for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/* 842 */       ((PIGData)it.next()).restrictSites(i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(List<PIGData> toAdd)
/*     */   {
/* 851 */     for (Iterator<PIGData> it = toAdd.iterator(); it.hasNext();) {
/* 852 */       PIGData nxt = (PIGData)it.next();
/* 853 */       this.data.put(nxt.getName(), nxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(List<String> toRemove)
/*     */   {
/* 864 */     for (int i = toRemove.size() - 1; i >= 0; i--) {
/* 865 */       this.data.remove(toRemove.get(i));
/*     */     }
/*     */   }
/*     */   
/*     */   public void split() {
/* 870 */     Set<PIGData> set = new HashSet();
/* 871 */     PIGData[] dat; int j; for (Iterator<PIGData> it = iterator(); it.hasNext(); 
/*     */         
/*     */ 
/* 874 */         j < dat.length)
/*     */     {
/* 872 */       PIGData da = (PIGData)it.next();
/* 873 */       dat = da.split();
/* 874 */       j = 0; continue;
/* 875 */       dat[j].setName(da.getName() + "_" + j);
/* 876 */       set.add(dat[j]);j++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 879 */     this.data.clear();
/* 880 */     for (Iterator<PIGData> it = set.iterator(); it.hasNext();) {
/* 881 */       PIGData da = (PIGData)it.next();
/* 882 */       this.data.put(da.getName(), da);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drop(List<Integer> toDrop) {
/* 887 */     java.util.Collections.sort(toDrop);
/* 888 */     this.dataL.clear();
/* 889 */     for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/* 890 */       PIGData data = (PIGData)it.next();
/* 891 */       data.removeAll(toDrop);
/*     */     }
/* 893 */     for (int i = toDrop.size() - 1; i >= 0; i--) {
/* 894 */       this.loc.remove(((Integer)toDrop.get(i)).intValue());
/* 895 */       if ((this.snpid != null) && (this.snpid.size() > 0)) this.snpid.remove(((Integer)toDrop.get(i)).intValue());
/*     */     }
/* 897 */     this.maf = null;
/* 898 */     this.length = Integer.valueOf(this.loc.size());
/*     */   }
/*     */   
/*     */   public int restrict(int st1, int end1, int lkb, int rkb, boolean kb) {
/* 902 */     int il = st1;
/* 903 */     int ir = end1;
/* 904 */     if (kb) {
/* 905 */       for (; il >= 0; il--) {
/* 906 */         if (((Integer)this.loc.get(st1)).intValue() - ((Integer)this.loc.get(il)).intValue() > lkb * 1000) {
/* 907 */           il++;
/* 908 */           break;
/*     */         }
/*     */       }
/* 912 */       for (; 
/* 912 */           ir < this.loc.size(); ir++) {
/* 913 */         if (((Integer)this.loc.get(ir)).intValue() - ((Integer)this.loc.get(end1)).intValue() > rkb * 1000) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 920 */       il = st1 - lkb;
/* 921 */       ir = end1 + rkb;
/*     */     }
/* 923 */     il = Math.max(0, il);
/* 924 */     ir = Math.min(this.loc.size() - 1, ir);
/*     */     
/* 926 */     this.loc = this.loc.subList(il, ir);
/* 927 */     this.snpid = this.snpid.subList(il, ir);
/* 928 */     for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/* 929 */       ((PIGData)it.next()).restrictSites(il, ir);
/*     */     }
/* 931 */     this.length = Integer.valueOf(this.loc.size());
/* 932 */     return il;
/*     */   }
/*     */   
/* 935 */   public boolean cnvBiggerThan(int i, int lenThresh, EmissionStateSpace stSp) { for (Iterator<PIGData> it = this.data.values().iterator(); it.hasNext();) {
/* 936 */       PIGData dat = (PIGData)it.next();
/* 937 */       Comparable comp = dat.getElement(i);
/* 938 */       int cnv = stSp.getCN(stSp.get(comp).intValue());
/* 939 */       if (cnv != 1) {
/* 940 */         int cnt = 1;
/*     */         
/* 942 */         for (int i1 = i + 1; (i1 - i < lenThresh) && (i1 < dat.length()); i1++) {
/* 943 */           int cnv1 = stSp.getCN(stSp.get(dat.getElement(i1)).intValue());
/* 944 */           if (cnv1 != cnv) break; cnt++;
/*     */         }
/*     */         
/* 947 */         for (int i1 = i - 1; (i - i1 < lenThresh) && (i1 >= 0); i1--) {
/* 948 */           int cnv1 = stSp.getCN(stSp.get(dat.getElement(i1)).intValue());
/* 949 */           if (cnv1 != cnv) break; cnt++;
/*     */         }
/*     */         
/* 952 */         if (cnt >= lenThresh) return true;
/*     */       } }
/* 954 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/collection/SimpleDataCollection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */