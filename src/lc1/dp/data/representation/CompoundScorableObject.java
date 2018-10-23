/*     */ package lc1.dp.data.representation;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.HaplotypeEmissionState;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.util.ComparableArrayComparator;
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
/*     */ abstract class CompoundScorableObject
/*     */   extends SimpleScorableObject
/*     */   implements CSOData
/*     */ {
/*  41 */   static transient Comparator comp = new ComparableArrayComparator();
/*  42 */   static transient Comparator countComp = new Comparator()
/*     */   {
/*     */     public int compare(Map.Entry<ComparableArray, Integer> o1, Map.Entry<ComparableArray, Integer> o2) {
/*  45 */       return ((Integer)o1.getValue()).compareTo((Integer)o2.getValue());
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addError(double error)
/*     */   {
/*  54 */     for (int i = 0; i < length(); i++) {
/*  55 */       ComparableArray missing = (ComparableArray)getElement(i);
/*  56 */       missing.addError(error);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int noCopies()
/*     */   {
/*  65 */     Comparable comp = getElement(0);
/*  66 */     if ((comp instanceof Emiss)) return 0;
/*  67 */     return ((ComparableArray)comp).noCopies(false);
/*     */   }
/*     */   
/*     */   protected CompoundScorableObject(String name, int noSites) {
/*  71 */     super(name, noSites, ComparableArray.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Comparable copyElement(Comparable element)
/*     */   {
/*  78 */     if ((element instanceof Emiss)) {
/*  79 */       return element;
/*     */     }
/*  81 */     return ((ComparableArray)element).copy();
/*     */   }
/*     */   
/*     */   public CompoundScorableObject(CSOData data) {
/*  85 */     super(data);
/*     */   }
/*     */   
/*     */   public static String getName(ScorableObject[] unit, String sep)
/*     */   {
/*  90 */     StringBuffer buf = new StringBuffer(unit[0].getName());
/*  91 */     for (int i = 1; i < unit.length; i++) {
/*  92 */       buf.append(((unit[0].getElement(0) instanceof ComparableArray)) && (((ComparableArray)unit[0].getElement(0)).size() > 1) ? sep : sep);
/*  93 */       buf.append(unit[i].getName());
/*     */     }
/*  95 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public CompoundScorableObject(CSOData[] unit, boolean merge, String sep) {
/*  99 */     this(getName(unit, sep), unit, merge);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void mkTrCompArray()
/*     */   {
/* 106 */     boolean half = ((ComparableArray)getElement(0)).size() == 2;
/* 107 */     for (int i = 0; i < length(); i++) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CompoundScorableObject(String name, CSOData[] unit, boolean merge)
/*     */   {
/* 118 */     this(name, unit[0].length());
/* 119 */     int noCopies = 0;
/*     */     
/* 121 */     for (int i = 0; i < unit.length; i++) {
/* 122 */       noCopies += unit[i].noCopies();
/*     */     }
/*     */     
/* 125 */     for (int i = 0; i < unit[0].length(); i++) {
/* 126 */       List<Comparable> l = new ArrayList();
/* 127 */       for (int j = 0; j < unit.length; j++) {
/* 128 */         Comparable comp = unit[j].getElement(i);
/* 129 */         if ((comp instanceof ComparableArray)) {
/* 130 */           ComparableArray comp1 = (ComparableArray)comp;
/* 131 */           if (merge) l.addAll(comp1.elements()); else {
/* 132 */             l.add(comp);
/*     */           }
/*     */         } else {
/* 135 */           l.add(comp);
/*     */         }
/*     */       }
/* 138 */       addPoint(i, new ComparableArray(l));
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
/*     */   public void mix()
/*     */   {
/* 151 */     for (int i = 0; i < length(); i++) {
/* 152 */       ((ComparableArray)getElement(i)).shuffle();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void set(int i, Comparable obj)
/*     */   {
/* 161 */     super.set(i, obj);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int countDiff(ComparableArray obj1, ComparableArray obj2)
/*     */   {
/* 168 */     int cnt = 0;
/* 169 */     for (int i = 0; i < obj1.size(); i++) {
/* 170 */       if (!obj1.get(i).equals(obj2.get(i))) cnt++;
/*     */     }
/* 172 */     return cnt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PhasedIntegerGenotypeData[] split()
/*     */   {
/* 180 */     List<String> names = Arrays.asList(getName().split(";"));
/*     */     
/* 182 */     PhasedIntegerGenotypeData[] res = new PhasedIntegerGenotypeData[((ComparableArray)getElement(0)).size()];
/* 183 */     int[] from = new int[res.length];
/* 184 */     int[] to = new int[res.length];
/* 185 */     for (int j = 0; j < res.length; j++) {
/* 186 */       from[j] = (j == 0 ? 0 : to[(j - 1)]);
/* 187 */       Comparable compa = ((ComparableArray)getElement(0)).get(j);
/* 188 */       from[j] += 
/* 189 */         ((compa instanceof ComparableArray) ? 
/* 190 */         ((ComparableArray)compa).size() : 1);
/* 191 */       String name = j < names.size() ? (String)names.get(j) : "";
/* 192 */       res[j] = new PhasedIntegerGenotypeData(name, length());
/*     */     }
/* 194 */     for (int i = 0; i < length(); i++) {
/* 195 */       ComparableArray compa = (ComparableArray)getElement(i);
/* 196 */       for (int j = 0; j < compa.size(); j++) {
/* 197 */         res[j].addPoint(i, compa.getReal(j));
/*     */       }
/*     */     }
/* 200 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double samplePhase(EmissionStateSpace emStSp, List<CSOData> spList, int i1, int i2, Set<Comparable> poss)
/*     */   {
/* 211 */     int len = length();
/* 212 */     ComparableArray em1 = (ComparableArray)getElement(i1);
/* 213 */     ComparableArray em2 = (ComparableArray)getElement(i2);
/* 214 */     int haplopairIndex1 = emStSp.getHaploPair(em1).intValue();
/* 215 */     int haplopairIndex2 = emStSp.getHaploPair(em2).intValue();
/* 216 */     Integer[][] diff = new Integer[noCopies() + 1][];
/*     */     
/* 218 */     for (int k = 0; k < noCopies() + 1; k++) {
/* 219 */       diff[k] = { Integer.valueOf(0), Integer.valueOf(k) };
/*     */     }
/* 221 */     int sumMatch = 0;
/* 222 */     for (int j = 0; j < spList.size(); j++) {
/* 223 */       CSOData data = (CSOData)spList.get(j);
/* 224 */       ComparableArray c1 = (ComparableArray)data.getElement(i1);
/* 225 */       if (emStSp.getHaploPair(c1).intValue() == haplopairIndex1) {
/* 226 */         ComparableArray c2 = (ComparableArray)data.getElement(i2);
/* 227 */         if (emStSp.getHaploPair(c2).intValue() == haplopairIndex2) {
/* 228 */           int tmp193_192 = 0; Integer[] tmp193_191 = diff[countDiff(c1, c2)];tmp193_191[tmp193_192] = Integer.valueOf(tmp193_191[tmp193_192].intValue() + 1);
/* 229 */           sumMatch++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 237 */     Arrays.sort(diff, comp);
/*     */     
/* 239 */     double certainty = sumMatch == 0 ? 0.0D : diff[(diff.length - 1)][0].intValue() / sumMatch;
/*     */     
/*     */ 
/* 242 */     int numDiff = diff[(diff.length - 1)][1].intValue();
/*     */     
/*     */ 
/* 245 */     List<ComparableArray> l1 = new ArrayList();
/* 246 */     for (Iterator<Comparable> it = poss.iterator(); it.hasNext();) {
/* 247 */       ComparableArray next = (ComparableArray)it.next();
/* 248 */       l1.add(next);
/* 249 */       if (countDiff(em2, next) != numDiff) it.remove();
/*     */     }
/* 251 */     if (poss.size() == 0) {
/* 252 */       poss.addAll(l1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 262 */     return certainty;
/*     */   }
/*     */   
/*     */   private void phaseRelative(CSOData original, int i, int i1, Collection<Comparable> poss)
/*     */   {
/* 267 */     ComparableArray emission = (ComparableArray)getElement(i);
/* 268 */     ComparableArray emission1 = (ComparableArray)getElement(i1);
/* 269 */     ComparableArray c1 = (ComparableArray)original.getElement(i);
/* 270 */     ComparableArray c2 = (ComparableArray)original.getElement(i1);
/* 271 */     int noDiff = countDiff(c1, c2);
/* 272 */     for (Iterator<Comparable> it = poss.iterator(); it.hasNext();) {
/* 273 */       if (countDiff((ComparableArray)it.next(), emission1) != noDiff) { it.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 282 */   Comparator<Comparable> compar1 = new Comparator()
/*     */   {
/*     */     public int compare(Comparable o1, Comparable o2) {
/* 285 */       ComparableArray a1 = (ComparableArray)o1;
/* 286 */       ComparableArray a2 = (ComparableArray)o2;
/* 287 */       for (int i = 0; i < a1.size(); i++) {
/* 288 */         if (a1.get(i).compareTo(a2.get(i)) != 0) return a1.get(i).compareTo(a2.get(i));
/*     */       }
/* 290 */       return 0;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] phaseCorrect(CSOData original, Collection<Integer> noCopiesL, Collection<Integer> noCopiesR)
/*     */   {
/* 300 */     Set<Comparable> poss = new HashSet();
/* 301 */     EmissionStateSpace emStSp = Emiss.getEmissionStateSpace(original.noCopies() - 1);
/* 302 */     int correct = 0;
/* 303 */     int wrong = 0;
/* 304 */     Integer[] phasePos = updatePhasePositions(emStSp);
/*     */     
/* 306 */     for (int jk = 0; jk < phasePos.length; jk++) {
/* 307 */       int i = phasePos[jk].intValue();
/* 308 */       ComparableArray emiss = (ComparableArray)getElement(i);
/* 309 */       if ((emStSp.getHaploPair(emiss) == emStSp.getHaploPair((ComparableArray)original.getElement(i))) && 
/* 310 */         (noCopiesR.contains(Integer.valueOf(emStSp.getRealCN(emiss)))))
/*     */       {
/*     */ 
/*     */ 
/* 314 */         poss.clear();
/* 315 */         poss.addAll((Collection)Arrays.asList(emStSp.getHaploForHaploPair(emiss)));
/* 316 */         for (int jk1 = jk - 1; jk1 >= 0; jk1--) {
/* 317 */           int i1 = phasePos[jk1].intValue();
/* 318 */           ComparableArray emiss1 = (ComparableArray)getElement(i1);
/* 319 */           if ((emStSp.getHaploPair(emiss1) != emStSp.getHaploPair((ComparableArray)original.getElement(i1))) || 
/* 320 */             (!noCopiesL.contains(Integer.valueOf(emStSp.getRealCN(emiss1))))) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 328 */           phaseRelative(original, i, i1, poss);
/* 329 */           if (!poss.contains(emiss))
/*     */           {
/* 331 */             wrong++;
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 336 */             correct++;
/*     */             
/*     */ 
/*     */ 
/* 340 */             if (poss.size() == 1) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 347 */     return new int[] { wrong, correct + wrong };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void samplePhase(List<CSOData> spList, double[] uncertainty, EmissionStateSpace emStSp)
/*     */   {
/* 356 */     if (((ComparableArray)getElement(0)).isNested())
/*     */     {
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
/* 378 */       throw new RuntimeException("!!");
/*     */     }
/*     */     
/*     */ 
/* 382 */     Set<Comparable> poss = new HashSet();
/* 383 */     Integer[] phasePos = updatePhasePositions(emStSp);
/* 384 */     for (int jk = 1; jk < phasePos.length; jk++) {
/* 385 */       int i = phasePos[jk].intValue();
/* 386 */       ComparableArray emiss = (ComparableArray)getElement(i);
/* 387 */       poss.clear();
/* 388 */       Comparable[] possib = emStSp.getHaploForHaploPair(emiss);
/* 389 */       poss.addAll((Collection)Arrays.asList(possib));
/* 390 */       for (int jk1 = jk - 1; (jk1 >= 0) && (poss.size() > 1); jk1--) {
/* 391 */         int i1 = phasePos[jk1].intValue();
/*     */         
/* 393 */         uncertainty[i] *= samplePhase(emStSp, spList, i, i1, poss);
/*     */       }
/*     */       
/* 396 */       if (poss.size() > 0) {
/* 397 */         set(i, (Comparable)poss.iterator().next());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer[] updatePhasePositions(EmissionStateSpace emStSp)
/*     */   {
/* 409 */     List<Integer> l = new ArrayList();
/* 410 */     for (int i = 0; i < length(); i++) {
/* 411 */       ComparableArray comp = (ComparableArray)getElement(i);
/*     */       
/* 413 */       if (emStSp.getHaploForHaploPair(comp).length > 1) { l.add(Integer.valueOf(i));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 423 */     return (Integer[])l.toArray(new Integer[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ComparableArray makeGapType(ComparableArray dat)
/*     */   {
/* 432 */     List<Comparable> dat1 = new ArrayList();
/* 433 */     for (int i = 0; i < dat.size(); i++) {
/* 434 */       if (dat.get(i).equals(Emiss.N())) {
/* 435 */         dat1.add(Emiss.b());
/*     */       } else
/* 437 */         dat1.add(Emiss.a());
/*     */     }
/* 439 */     return new ComparableArray(dat1);
/*     */   }
/*     */   
/*     */ 
/*     */   public double[] getUncertainty(EmissionState st)
/*     */   {
/* 445 */     EmissionStateSpace emstsp = st.getEmissionStateSpace();
/* 446 */     double[] cert = new double[length()];
/* 447 */     for (int i = 0; i < length(); i++) {
/* 448 */       ComparableArray compa = (ComparableArray)getElement(i);
/* 449 */       cert[i] = st.score(emstsp.get(compa).intValue(), i);
/*     */     }
/* 451 */     return cert;
/*     */   }
/*     */   
/*     */ 
/*     */   public double getUncertainty(EmissionState st, int i)
/*     */   {
/* 457 */     EmissionStateSpace emstsp = st.getEmissionStateSpace();
/* 458 */     ComparableArray compa = (ComparableArray)getElement(i);
/* 459 */     return st.score(emstsp.get(compa).intValue(), i);
/*     */   }
/*     */   
/*     */ 
/*     */   public void sampleGenotype(HaplotypeEmissionState emst, List<CSOData> spList)
/*     */   {
/* 465 */     EmissionStateSpace emStSp = emst.getEmissionStateSpace();
/*     */     
/*     */ 
/* 468 */     for (int i = 0; i < length(); i++) {
/* 469 */       emst.emissions[i] = new SimpleExtendedDistribution(emStSp.defaultList.size());
/*     */       
/* 471 */       for (int j = 0; j < spList.size(); j++) {
/* 472 */         CSOData obj = (CSOData)spList.get(j);
/* 473 */         Integer compa_i = emStSp.getHaploPair(obj.getElement(i));
/* 474 */         if (compa_i == null) {
/*     */           try {
/* 476 */             throw new RuntimeException("no element " + obj.getElement(i) + " in state space \n" + emStSp.defaultList);
/*     */           } catch (Exception exc) {
/* 478 */             exc.printStackTrace();
/*     */           }
/*     */           
/*     */         } else {
/* 482 */           emst.addCount(compa_i.intValue(), -1, 1.0D, i);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 489 */     emst.transferCountsToProbs(0.0D);
/* 490 */     for (int i = 0; i < length(); i++) {
/* 491 */       set(i, emStSp.getHaploPair(Integer.valueOf(emst.getBestIndex(i))));
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
/*     */   public void print(PrintWriter pw, boolean expand, boolean mark, Collection<Integer> toDrop)
/*     */   {
/* 504 */     print(pw, true, expand, mark, toDrop);
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(PrintWriter pw, boolean idline, boolean expand, boolean mark, Collection<Integer> toDrop)
/*     */   {
/* 510 */     if (idline) {
/* 511 */       if (mark) printIdLine("# id " + getName(), pw, length()); else
/* 512 */         pw.println("# id " + getName());
/*     */     }
/* 514 */     if (toDrop != null) throw new RuntimeException("!!");
/* 515 */     if (isNested()) {
/* 516 */       PhasedIntegerGenotypeData[] data = split();
/* 517 */       for (int i = 0; i < data.length; i++)
/*     */       {
/* 519 */         data[i].print(pw, false, expand, mark, toDrop);
/*     */       }
/*     */     }
/*     */     else {
/* 523 */       Object[][] l = new Object[noCopies()][];
/* 524 */       for (int i = 0; i < l.length; i++) {
/* 525 */         l[i] = new Object[length()];
/*     */       }
/* 527 */       for (int i = 0; i < length(); i++) {
/* 528 */         List<Comparable> list = new ArrayList();
/* 529 */         ((ComparableArray)getElement(i)).addObjectsRecursive(list);
/* 530 */         int k = 0;
/* 531 */         for (Iterator<Comparable> it = list.iterator(); it.hasNext(); k++) {
/* 532 */           Object nxt = it.next();
/* 533 */           l[k][i] = nxt;
/*     */         }
/*     */       }
/* 536 */       for (int j = 0; j < l.length; j++) {
/* 537 */         Object[][] res = { expand ? expand(l[j]) : l[j] };
/* 538 */         for (int k = 0; k < res.length; k++) {
/* 539 */           for (int i = 0; i < res[k].length; i++) {
/* 540 */             if ((toDrop == null) || (!toDrop.contains(Integer.valueOf(i)))) {
/* 541 */               printElement(pw, res[k][i], expand);
/*     */             }
/*     */           }
/* 544 */           pw.println();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 550 */   private static Object[][] transpose(Object[][] obj) { int max = 0;
/* 551 */     for (int i = 0; i < obj.length; i++) {
/* 552 */       if (obj[i].length > max) {
/* 553 */         max = obj[i].length;
/*     */       }
/*     */     }
/* 556 */     Object[][] res = new Object[max][obj.length];
/* 557 */     for (int i = 0; i < obj.length; i++) {
/* 558 */       for (int j = 0; j < obj[i].length; j++) {
/* 559 */         res[j][i] = obj[i][j];
/*     */       }
/*     */     }
/* 562 */     return res;
/*     */   }
/*     */   
/*     */   static Object[][] expand(Object[] comp) {
/* 566 */     List<Object[]> l = new ArrayList();
/* 567 */     for (int i = 0; i < comp.length; i++)
/* 568 */       if ((comp[i] instanceof Emiss)) {
/* 569 */         ComparableArray compa = ((Emiss)comp[i]).expand();
/* 570 */         l.add(compa.elements().toArray());
/*     */       }
/*     */       else
/*     */       {
/* 574 */         l.add(new Object[] { comp[i] });
/*     */       }
/* 576 */     return transpose((Object[][])l.toArray(new Object[0][]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(CSOData dat, int i)
/*     */   {
/* 585 */     for (int j = 0; j < length(); j++) {
/* 586 */       ComparableArray comp1 = (ComparableArray)getElement(j);
/* 587 */       ComparableArray comp2 = (ComparableArray)dat.getElement(j);
/* 588 */       if (!comp1.get(i).equals(comp2.get(i))) {
/* 589 */         System.err.println("false at " + j);
/* 590 */         System.err.println(comp1 + " " + comp2);
/* 591 */         return false;
/*     */       }
/*     */     }
/* 594 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isNested()
/*     */   {
/* 602 */     if ((getElement(0) instanceof ComparableArray)) {
/* 603 */       return ((ComparableArray)getElement(0)).get(0) instanceof ComparableArray;
/*     */     }
/* 605 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/representation/CompoundScorableObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */