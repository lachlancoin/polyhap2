/*     */ package lc1.dp.emissionspace;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.data.representation.ComparableArray;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.SimpleScorableObject;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class EmissionStateSpace
/*     */   implements List<Comparable>
/*     */ {
/*  31 */   public StringMethod gsm = new StringMethod()
/*     */   {
/*     */     public String getString(Comparable comp) {
/*  34 */       return EmissionStateSpace.this.getGenotypeString(comp);
/*     */     }
/*     */   };
/*     */   
/*  38 */   public StringMethod hsm = new StringMethod()
/*     */   {
/*     */     public String getString(Comparable comp) {
/*  41 */       return EmissionStateSpace.this.getHaploPairString(comp);
/*     */     }
/*     */   };
/*     */   
/*     */   private int noCopies;
/*     */   
/*     */   public List defaultList;
/*     */   
/*     */   public int getCNIndex(int i)
/*     */   {
/*  51 */     for (int j = 0; j < this.copyNumber.size(); j++) {
/*  52 */       if (((Integer)this.copyNumber.get(j)).intValue() == i) return j;
/*     */     }
/*  54 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*  57 */   private List<Comparable> genotypeList = new ArrayList();
/*  58 */   private List<Comparable> haplopairList = new ArrayList();
/*  59 */   protected List<Comparable> haploList = new ArrayList();
/*     */   
/*  61 */   public List<Integer> copyNumber = new ArrayList();
/*     */   
/*  63 */   private Map<Comparable, Integer> stateSpaceToCopyNumber = new TreeMap();
/*  64 */   private Map<Comparable, Integer> stateSpaceToGenotypeIndex = new TreeMap();
/*  65 */   private Map<Comparable, Integer> stateSpaceToHaplolistIndex = new HashMap();
/*  66 */   private Map<Comparable, Integer> stateSpaceToHaploPairIndex = new TreeMap();
/*     */   double[][] genoToHaploW;
/*     */   
/*  69 */   public List<Comparable> getGenotypeList() { return this.genotypeList; }
/*     */   
/*     */ 
/*     */   Comparable[][] genoToHaplos;
/*     */   
/*     */   Comparable[][] haploPairToHaplos;
/*     */   
/*     */   private int[][] copyNoIndexToGeno;
/*     */   
/*     */   private int[][] genoToHaploPair;
/*     */   
/*     */   private int[][] genoToHaplo;
/*     */   public int getHaploPairFromHaplo(int hapl)
/*     */   {
/*  83 */     return this.haploToHaploPair[hapl];
/*     */   }
/*     */   
/*  86 */   public int getCN(int index) { return this.haploPairToCN[index]; }
/*     */   
/*     */ 
/*     */   public int[] getGenoForCopyNo(int i)
/*     */   {
/*  91 */     int j = this.copyNumber.indexOf(Integer.valueOf(i));
/*  92 */     return this.copyNoIndexToGeno[j];
/*     */   }
/*     */   
/*  95 */   public int[] getHaplopairForGeno(int i) { return this.genoToHaploPair[i]; }
/*     */   
/*     */   public Comparable[] getHaploForHaploPair(Comparable comp)
/*     */   {
/*  99 */     return this.haploPairToHaplos[getHaploPair(comp).intValue()];
/*     */   }
/*     */   
/* 102 */   public Integer getHapl(String st) { return (Integer)this.stateSpaceToHaplolistIndex.get(st); }
/*     */   
/*     */   public Integer getHapl(Comparable omp) {
/* 105 */     Integer res = (Integer)this.stateSpaceToHaplolistIndex.get(getHaploString(omp));
/* 106 */     if (res == null) {
/* 107 */       throw new RuntimeException("nothing found for " + omp + " in \n" + this.stateSpaceToHaplolistIndex.keySet());
/*     */     }
/* 109 */     return res;
/*     */   }
/*     */   
/* 112 */   public Integer getHaploPair(Object omp) { Integer res = (Integer)this.stateSpaceToHaploPairIndex.get(getHaploPairString((Comparable)omp));
/* 113 */     return res;
/*     */   }
/*     */   
/*     */   public Comparable getHapl(Integer i) {
/* 117 */     return ((ComparableArray)this.haploList.get(i.intValue())).copy();
/*     */   }
/*     */   
/* 120 */   public Comparable getHaploPair(Integer i) { return (Comparable)this.haplopairList.get(i.intValue()); }
/*     */   
/*     */ 
/*     */ 
/*     */   public Integer get(Object comp)
/*     */   {
/* 126 */     return getHaploPair(comp);
/*     */   }
/*     */   
/*     */   public int[] getHaps(int i) {
/* 130 */     return this.haploPairToHaplo[i];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean exclude(ComparableArray list)
/*     */   {
/* 140 */     if (((list.get(0) instanceof Emiss)) && (list.size() > 1) && (
/* 141 */       ((((Emiss)list.get(0)).noCopies() == 0) && (((Emiss)list.get(1)).noCopies() > 1)) || (
/* 142 */       (((Emiss)list.get(1)).noCopies() == 0) && (((Emiss)list.get(0)).noCopies() > 1)))) {
/* 143 */       return true;
/*     */     }
/*     */     
/* 146 */     return false;
/*     */   }
/*     */   
/* 149 */   public Comparable getGenotype(int comp) { return (Comparable)this.genotypeList.get(comp); }
/*     */   
/*     */   public Integer getGenotype(Object comp) {
/* 152 */     return (Integer)this.stateSpaceToGenotypeIndex.get(getGenotypeString((Comparable)comp));
/*     */   }
/*     */   
/* 155 */   public int getRealCN(ComparableArray compA) { return getCN(compA); }
/*     */   
/*     */   public int getCN(ComparableArray compA) {
/* 158 */     return ((Integer)this.stateSpaceToCopyNumber.get(getGenotypeString(compA))).intValue();
/*     */   }
/*     */   
/*     */   public Comparable get(int i)
/*     */   {
/* 163 */     return (Comparable)this.haploList.get(i);
/*     */   }
/*     */   
/* 166 */   public int[] getGenotypeConsistent(int i) { return this.genoToHaploPair[i]; }
/*     */   
/*     */   public double[] getWeights(int stSpIndex) {
/* 169 */     return this.genoToHaploW[stSpIndex];
/*     */   }
/*     */   
/* 172 */   protected Comparator<Comparable> getHaploPairComparator() { new Comparator() {
/*     */       public int compare(Comparable o1, Comparable o2) {
/* 174 */         return EmissionStateSpace.this.getHaploPairString(o1).compareTo(EmissionStateSpace.this.getHaploPairString(o2));
/*     */       }
/*     */     }; }
/*     */   
/*     */ 
/*     */   protected Comparator<Comparable> getGenoComparator()
/*     */   {
/* 181 */     new Comparator() {
/*     */       public int compare(Comparable o1, Comparable o2) {
/* 183 */         String st2 = EmissionStateSpace.this.getGenotypeString(o2);
/* 184 */         String st1 = EmissionStateSpace.this.getGenotypeString(o1);
/* 185 */         return st1.compareTo(st2);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void map1(int[][] copyNumberToGeno, List<Comparable> genotypeList, Map<Comparable, Integer> stateSpaceToCNIndex, StringMethod sm, List<Integer> copyNumber)
/*     */   {
/* 194 */     for (int i = 0; i < copyNumberToGeno.length; i++) {
/* 195 */       int cn = ((Integer)copyNumber.get(i)).intValue();
/* 196 */       List<Integer> hapl = new ArrayList();
/* 197 */       for (int j = 0; j < genotypeList.size(); j++) {
/* 198 */         Comparable st = (Comparable)genotypeList.get(j);
/* 199 */         String str = sm.getString(st);
/* 200 */         int ind = ((Integer)stateSpaceToCNIndex.get(
/* 201 */           str)).intValue();
/*     */         
/* 202 */         if (ind == cn) {
/* 203 */           hapl.add(Integer.valueOf(j));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 209 */       copyNumberToGeno[i] = new int[hapl.size()];
/* 210 */       for (int j = 0; j < hapl.size(); j++) {
/* 211 */         copyNumberToGeno[i][j] = ((Integer)hapl.get(j)).intValue();
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
/*     */   private static void map(int[][] genoToHaplo, Comparable[][] genoToHaplos, List<Comparable> haploList, Map<Comparable, Integer> stateSpaceToGenotypeIndex, StringMethod sm)
/*     */   {
/* 225 */     for (int i = 0; i < genoToHaplo.length; i++)
/*     */     {
/* 227 */       List<Integer> hapl = new ArrayList();
/* 228 */       for (int j = 0; j < haploList.size(); j++) {
/* 229 */         Comparable st = (Comparable)haploList.get(j);
/* 230 */         String str = sm.getString(st);
/* 231 */         int ind = ((Integer)stateSpaceToGenotypeIndex.get(
/* 232 */           str)).intValue();
/*     */         
/* 233 */         if (ind == i) {
/* 234 */           hapl.add(Integer.valueOf(j));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 240 */       genoToHaplo[i] = new int[hapl.size()];
/* 241 */       if (genoToHaplos != null) genoToHaplos[i] = new Comparable[hapl.size()];
/* 242 */       for (int j = 0; j < hapl.size(); j++) {
/* 243 */         genoToHaplo[i][j] = ((Integer)hapl.get(j)).intValue();
/* 244 */         if (genoToHaplos != null) { genoToHaplos[i][j] = ((Comparable)haploList.get(((Integer)hapl.get(j)).intValue()));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 251 */   public EmissionStateSpace(int noCopies) { this.noCopies = noCopies; }
/*     */   
/*     */   public void init(List<Comparable> list) {
/* 254 */     this.defaultList = this.haplopairList;
/* 255 */     Set<Comparable> genotypeSet = new TreeSet(getGenoComparator());
/* 256 */     Set<Comparable> haplopairSet = new TreeSet(getHaploPairComparator());
/* 257 */     Set<Comparable> haplotypeSet = new HashSet();
/* 258 */     Set<Integer> cn = new TreeSet();
/* 259 */     for (int i = 0; i < list.size(); i++) {
/* 260 */       Comparable el = (Comparable)list.get(i);
/* 261 */       genotypeSet.add(el);
/* 262 */       haplopairSet.add(el);
/* 263 */       haplotypeSet.add(el);
/* 264 */       if ((el instanceof Emiss)) {
/* 265 */         cn.add(Integer.valueOf(((Emiss)el).noCopies()));
/*     */       }
/* 267 */       else if ((el instanceof Integer)) {
/* 268 */         cn.add(Integer.valueOf(1));
/*     */       }
/*     */       else {
/* 271 */         cn.add(Integer.valueOf(((ComparableArray)el).noCopies(true)));
/*     */       }
/*     */     }
/* 274 */     haplotypeSet.removeAll(haplopairSet);
/* 275 */     haplopairSet.removeAll(genotypeSet);
/* 276 */     this.copyNumber.addAll(cn);
/* 277 */     this.genotypeList.addAll(genotypeSet);
/* 278 */     this.haplopairList.addAll(genotypeSet);
/* 279 */     this.haploList.addAll(genotypeSet);
/* 280 */     this.haplopairList.addAll(haplopairSet);
/* 281 */     this.haploList.addAll(haplopairSet);
/*     */     
/* 283 */     this.haploList.addAll(haplotypeSet);
/*     */     
/* 285 */     this.noCopies = this.noCopies;
/* 286 */     initialise();
/* 287 */     this.invCNVSize = (1.0D / this.copyNumber.size());
/* 288 */     this.invSize = (1.0D / this.defaultList.size());
/* 289 */     this.genoToHaploW = new double[this.genotypeList.size()][];
/* 290 */     this.haploPairIndexToWeight = new double[this.haplopairList.size()];
/*     */     
/*     */ 
/* 293 */     this.copyNoIndexToGeno = new int[this.copyNumber.size()][];
/* 294 */     this.genoToHaplo = new int[genoListSize().intValue()][];
/* 295 */     this.genoToHaploPair = new int[genoListSize().intValue()][];
/*     */     
/* 297 */     this.genoToHaplos = new Comparable[genoListSize().intValue()][];
/* 298 */     this.haploPairToHaplo = new int[this.haplopairList.size()][];
/* 299 */     this.haploToHaploPair = new int[this.haploList.size()];
/* 300 */     this.haploPairToHaplos = new Comparable[this.haplopairList.size()][];
/* 301 */     this.haploPairToGeno = new int[this.haplopairList.size()];
/* 302 */     this.haploPairToCN = new int[this.haplopairList.size()];
/* 303 */     map(this.genoToHaplo, this.genoToHaplos, this.haploList, this.stateSpaceToGenotypeIndex, this.gsm);
/* 304 */     map1(this.copyNoIndexToGeno, this.genotypeList, this.stateSpaceToCopyNumber, this.gsm, this.copyNumber);
/*     */     
/*     */ 
/* 307 */     map(this.genoToHaploPair, null, this.haplopairList, this.stateSpaceToGenotypeIndex, this.gsm);
/* 308 */     map(this.haploPairToHaplo, this.haploPairToHaplos, this.haploList, this.stateSpaceToHaploPairIndex, this.hsm);
/* 309 */     for (int i = 0; i < this.haplopairList.size(); i++) {
/* 310 */       Comparable hpli = (Comparable)this.haplopairList.get(i);
/* 311 */       String hplis = getGenotypeString(hpli);
/* 312 */       this.haploPairToGeno[i] = ((Integer)this.stateSpaceToGenotypeIndex.get(hplis)).intValue();
/*     */     }
/*     */     
/* 315 */     for (int i = 0; i < this.haploPairToHaplo.length; i++) {
/* 316 */       int[] hPToHi = this.haploPairToHaplo[i];
/* 317 */       for (int j = 0; j < hPToHi.length; j++) {
/* 318 */         this.haploToHaploPair[hPToHi[j]] = i;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 323 */     for (int i = 0; i < this.genoToHaploPair.length; i++) {
/* 324 */       int[] haploPairIndices = this.genoToHaploPair[i];
/* 325 */       this.genoToHaploW[i] = new double[haploPairIndices.length];
/* 326 */       for (int j = 0; j < haploPairIndices.length; j++) {
/* 327 */         Comparable comp1 = (Comparable)this.haplopairList.get(haploPairIndices[j]);
/* 328 */         if (((comp1 instanceof Emiss)) || ((comp1 instanceof Integer))) {
/* 329 */           this.genoToHaploW[i][j] = 1.0D;
/*     */         }
/*     */         else {
/* 332 */           boolean exclude = exclude((ComparableArray)comp1);
/* 333 */           if (exclude) {
/* 334 */             this.genoToHaploW[i][j] = Constants.exclude();
/*     */           } else {
/* 336 */             this.genoToHaploW[i][j] = 1.0D;
/*     */           }
/*     */         }
/* 339 */         this.haploPairIndexToWeight[haploPairIndices[j]] = this.genoToHaploW[i][j];
/*     */       }
/* 341 */       SimpleExtendedDistribution.normalise(this.genoToHaploW[i]);
/*     */     }
/*     */     
/* 344 */     for (int i = 0; i < haplopairListSize(); i++) {
/* 345 */       Comparable comp = (Comparable)this.haplopairList.get(i);
/* 346 */       if ((comp instanceof ComparableArray)) {
/* 347 */         this.haploPairToCN[i] = ((ComparableArray)comp).noCopies(true);
/*     */       }
/* 349 */       else if ((comp instanceof Emiss)) {
/* 350 */         this.haploPairToCN[i] = ((Emiss)comp).noCopies();
/*     */       } else {
/* 352 */         this.haploPairToCN[i] = 2;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 357 */     StringBuffer sw = new StringBuffer();
/* 358 */     sw.append("emission state space");
/*     */     
/* 360 */     for (int i = 0; i < this.haploList.size(); i++) {
/* 361 */       Comparable comp = (Comparable)this.haploList.get(i);
/* 362 */       sw.append((comp instanceof ComparableArray) ? ((ComparableArray)comp).elements().toString() : comp.toString());
/* 363 */       sw.append("  ");
/* 364 */       if ((i == this.genotypeList.size() - 1) || (i == this.haplopairList.size() - 1)) sw.append("\n");
/*     */     }
/* 366 */     for (int i = 0; i < this.defaultList.size(); i++) {
/* 367 */       int[] hapL = getHaps(i);
/* 368 */       for (int i1 = 1; i1 < hapL.length; i1++) {
/* 369 */         if (((Comparable)this.haploList.get(i1)).toString().equals(((Comparable)this.haploList.get(0)).toString())) throw new RuntimeException("!!");
/*     */       }
/*     */     }
/* 372 */     Logger.global.info(sw.toString());
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
/*     */   public void initialise()
/*     */   {
/* 385 */     for (int i = 0; i < this.genotypeList.size(); i++) {
/* 386 */       Comparable com = (Comparable)this.genotypeList.get(i);
/* 387 */       String genString = getGenotypeString(com);
/* 388 */       this.stateSpaceToGenotypeIndex.put(genString, Integer.valueOf(i));
/* 389 */       if ((com instanceof Emiss)) {
/* 390 */         this.stateSpaceToCopyNumber.put(genString, Integer.valueOf(((Emiss)com).noCopies()));
/*     */       }
/* 392 */       else if ((com instanceof Integer)) {
/* 393 */         this.stateSpaceToCopyNumber.put(genString, Integer.valueOf(1));
/*     */       }
/*     */       else {
/* 396 */         this.stateSpaceToCopyNumber.put(genString, Integer.valueOf(((ComparableArray)com).noCopies(true)));
/*     */       }
/*     */     }
/*     */     
/* 400 */     for (int i = 0; i < this.haploList.size(); i++) {
/* 401 */       this.stateSpaceToHaplolistIndex.put(getHaploString((Comparable)this.haploList.get(i)), Integer.valueOf(i));
/*     */     }
/* 403 */     Comparator comp = getHaploPairComparator();
/* 404 */     for (int i = 0; i < this.haploList.size(); i++) {
/* 405 */       int j = 0;
/* 406 */       Comparable o2 = (Comparable)this.haploList.get(i);
/* 407 */       for (; j < this.haplopairList.size(); j++)
/* 408 */         if (comp.compare(this.haplopairList.get(j), o2) == 0)
/*     */           break;
/* 410 */       if (j == this.haplopairList.size()) throw new RuntimeException("!!");
/* 411 */       String haploPairString = getHaploPairString((Comparable)this.haploList.get(i));
/* 412 */       this.stateSpaceToHaploPairIndex.put(
/* 413 */         haploPairString, Integer.valueOf(j));
/*     */     }
/* 415 */     System.err.println(this.stateSpaceToGenotypeIndex.size() + " " + this.stateSpaceToHaplolistIndex.size());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 424 */     return this.defaultList.size();
/*     */   }
/*     */   
/* 427 */   public boolean isEmpty() { return this.defaultList.isEmpty(); }
/*     */   
/*     */   public boolean contains(Object o) {
/* 430 */     return this.defaultList.contains(o);
/*     */   }
/*     */   
/* 433 */   public Iterator<Comparable> iterator() { return this.defaultList.iterator(); }
/*     */   
/*     */   public Object[] toArray() {
/* 436 */     return this.defaultList.toArray();
/*     */   }
/*     */   
/* 439 */   public <T> T[] toArray(T[] a) { return this.defaultList.toArray(a); }
/*     */   
/*     */   public boolean add(Comparable o) {
/* 442 */     return this.defaultList.add(o);
/*     */   }
/*     */   
/* 445 */   public boolean remove(Object o) { return this.defaultList.remove(o); }
/*     */   
/*     */   public boolean containsAll(Collection<?> c) {
/* 448 */     return this.defaultList.containsAll(c);
/*     */   }
/*     */   
/* 451 */   public boolean addAll(Collection<? extends Comparable> c) { return this.defaultList.addAll(c); }
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends Comparable> c) {
/* 454 */     return this.defaultList.addAll(index, c);
/*     */   }
/*     */   
/* 457 */   public boolean removeAll(Collection<?> c) { return this.defaultList.removeAll(c); }
/*     */   
/*     */   public boolean retainAll(Collection<?> c) {
/* 460 */     return this.defaultList.retainAll(c);
/*     */   }
/*     */   
/* 463 */   public void clear() { this.defaultList.clear(); }
/*     */   
/*     */   public Comparable set(int index, Comparable element)
/*     */   {
/* 467 */     return (Comparable)this.defaultList.set(index, element);
/*     */   }
/*     */   
/* 470 */   public void add(int index, Comparable element) { this.defaultList.add(index, element); }
/*     */   
/*     */   public Comparable remove(int index)
/*     */   {
/* 474 */     return (Comparable)this.defaultList.remove(index);
/*     */   }
/*     */   
/* 477 */   public int indexOf(Object o) { return this.defaultList.indexOf(o); }
/*     */   
/*     */   public int lastIndexOf(Object o) {
/* 480 */     return this.defaultList.lastIndexOf(o);
/*     */   }
/*     */   
/* 483 */   public ListIterator<Comparable> listIterator() { return this.defaultList.listIterator(); }
/*     */   
/*     */   public ListIterator<Comparable> listIterator(int index) {
/* 486 */     return this.defaultList.listIterator(index);
/*     */   }
/*     */   
/* 489 */   public List<Comparable> subList(int fromIndex, int toIndex) { return this.defaultList.subList(fromIndex, toIndex); }
/*     */   
/*     */   public int haplopairListSize() {
/* 492 */     return this.haplopairList.size();
/*     */   }
/*     */   
/* 495 */   public int noCopies() { return this.noCopies; }
/*     */   
/*     */   public Integer genoListSize() {
/* 498 */     return Integer.valueOf(this.genotypeList.size());
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract String getHaploPairString(Comparable paramComparable);
/*     */   
/*     */ 
/*     */   public abstract String getGenotypeString(Comparable paramComparable);
/*     */   
/*     */   public abstract String getHaploString(Comparable paramComparable);
/*     */   
/* 509 */   public int getGenoForHaplopair(int obj_index) { return this.haploPairToGeno[obj_index]; }
/*     */   
/*     */   public Integer getFromString(String sti) {
/* 512 */     Integer res = (Integer)this.stateSpaceToGenotypeIndex.get(sti);
/*     */     
/*     */ 
/*     */ 
/* 516 */     return res;
/*     */   }
/*     */   
/* 519 */   public double[] getArray(String c) { double[] res = new double[this.defaultList.size()];
/* 520 */     Arrays.fill(res, 0.0D);
/* 521 */     if (c.equals("e")) {
/* 522 */       Arrays.fill(res, 1.0D / res.length);
/* 523 */       return res;
/*     */     }
/* 525 */     if (c.equals("f")) {
/* 526 */       char[] ch = { 'A', '_', 'X' };
/* 527 */       for (int j = 0; j < ch.length; j++) {
/* 528 */         res[getFromString(ch[j]).intValue()] = (1.0D / ch.length);
/*     */       }
/* 530 */       return res;
/*     */     }
/*     */     
/* 533 */     Integer i = getFromString(c);
/* 534 */     if (i != null) {
/* 535 */       res[i.intValue()] = 1.0D;
/* 536 */       return res;
/*     */     }
/*     */     
/*     */     char[] ch;
/* 540 */     if (c.equals("1")) { ch = new char[] { 'A', 'B' };
/*     */     } else { char[] ch;
/* 542 */       if (c.equals("0")) { ch = new char[] { '_' };
/*     */       } else { char[] ch;
/* 544 */         if (c.equals("2")) { ch = new char[] { 'X', 'Y', 'Z' };
/*     */         } else { char[] ch;
/* 546 */           if (c.equals("3")) { ch = new char[] { 'T', 'U', 'V', 'W' };
/*     */           } else
/* 548 */             throw new RuntimeException(c); } } }
/* 549 */     char[] ch; for (int j = 0; j < ch.length; j++) {
/* 550 */       res[getFromString(ch[j]).intValue()] = (1.0D / ch.length);
/*     */     }
/* 552 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int[][] haploPairToHaplo;
/*     */   
/*     */   protected int[] haploToHaploPair;
/*     */   
/*     */   private double[] haploPairIndexToWeight;
/*     */   
/*     */   private int[] haploPairToCN;
/*     */   
/*     */   private int[] haploPairToGeno;
/*     */   
/*     */   double invCNVSize;
/*     */   
/*     */   double invSize;
/*     */   
/*     */   public double getWeight(int haploPairIndex)
/*     */   {
/* 573 */     return this.haploPairIndexToWeight[haploPairIndex]; }
/*     */   
/*     */   public int[] getSwitchTranslation() {
/* 576 */     int[] res = new int[this.haplopairList.size()];
/* 577 */     for (int i = 0; i < res.length; i++) {
/* 578 */       res[i] = get(SimpleScorableObject.switchAlleles((Comparable)this.haplopairList.get(i))).intValue();
/*     */     }
/* 580 */     return res;
/*     */   }
/*     */   
/* 583 */   public double[] mix(double[] probs, double[] array, double[] ds) { double[] res = new double[probs.length];
/* 584 */     for (int i = 0; i < res.length; i++) {
/* 585 */       res[i] = (probs[i] * ds[0] + array[i] * ds[1]);
/*     */     }
/* 587 */     return res;
/*     */   }
/*     */   
/* 590 */   public int[] getHaploFromHaploPair(int hapl) { return this.haploPairToHaplo[hapl]; }
/*     */   
/*     */   public int flip(int hap1_j) {
/* 593 */     int[] res = getHaploFromHaploPair(getHaploPairFromHaplo(hap1_j));
/*     */     
/* 595 */     if (res[1] == hap1_j) return res[0];
/* 596 */     return res[1];
/*     */   }
/*     */   
/*     */ 
/*     */   public double invCNVSize()
/*     */   {
/* 602 */     return this.invCNVSize;
/*     */   }
/*     */   
/* 605 */   public double bSpaceSize(int j) { return getGenoForCopyNo(getCN(j)).length; }
/*     */   
/*     */   public double invSize() {
/* 608 */     return this.invSize;
/*     */   }
/*     */   
/*     */   static abstract interface StringMethod
/*     */   {
/*     */     public abstract String getString(Comparable paramComparable);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/emissionspace/EmissionStateSpace.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */