/*      */ package lc1.dp.states;
/*      */ 
/*      */ import java.io.PrintWriter;
/*      */ import java.io.StringWriter;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.TreeMap;
/*      */ import java.util.TreeSet;
/*      */ import lc1.CGH.Aberation;
/*      */ import lc1.dp.data.representation.CSOData;
/*      */ import lc1.dp.data.representation.ComparableArray;
/*      */ import lc1.dp.data.representation.Emiss;
/*      */ import lc1.dp.data.representation.PIGData;
/*      */ import lc1.dp.emissionspace.EmissionStateSpace;
/*      */ import lc1.stats.IntegerDistribution;
/*      */ import lc1.stats.PseudoDistribution;
/*      */ import lc1.stats.SimpleExtendedDistribution;
/*      */ import lc1.util.Constants;
/*      */ 
/*      */ 
/*      */ public class PhasedDataState
/*      */   extends HaplotypeEmissionState
/*      */   implements PIGData
/*      */ {
/*      */   public PhasedDataState(PhasedDataState st_to_init, String name)
/*      */   {
/*   36 */     super(st_to_init, name);
/*      */   }
/*      */   
/*      */   public PhasedDataState(PhasedDataState state_j)
/*      */   {
/*   41 */     super(state_j);
/*      */   }
/*      */   
/*      */   public PhasedDataState(String name, int noSnps, EmissionStateSpace emStSp, short data_index)
/*      */   {
/*   46 */     super(name, noSnps, emStSp, data_index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PhasedDataState(PIGData[] statesD, List<int[]> list)
/*      */   {
/*   56 */     this(statesD[0].getName(), list.size(), ((PhasedDataState)statesD[0]).getEmissionStateSpace(), (short)-1);
/*   57 */     for (int i = 0; i < list.size(); i++) {
/*   58 */       int[] ind = (int[])list.get(i);
/*   59 */       this.emissions[i] = ((PhasedDataState)statesD[ind[0]]).emissions[ind[1]];
/*      */     }
/*      */   }
/*      */   
/*   63 */   public PhasedDataState clone() { return new PhasedDataState(this); }
/*      */   
/*      */   public PhasedDataState(String string, List<String> st, EmissionStateSpace emStSp, short s)
/*      */   {
/*   67 */     this(string, ((String)st.get(0)).length(), emStSp, s);
/*   68 */     for (int i = 0; i < ((String)st.get(0)).length(); i++) {
/*   69 */       if (((String)st.get(0)).charAt(i) != ' ') {
/*   70 */         char[] point = new char[2 * st.size() - 1];
/*   71 */         for (int j = 0; j < st.size(); j++) {
/*   72 */           point[(2 * j)] = ((String)st.get(j)).charAt(i);
/*   73 */           if (j < st.size() - 1) point[(2 * j + 1)] = ',';
/*      */         }
/*   75 */         String str = new String(point).replaceAll("_", "");
/*      */         
/*   77 */         int ind = emStSp.getHapl(str).intValue();
/*   78 */         this.emissions[i] = new IntegerDistribution(ind);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public double[] getUncertainty(EmissionState st)
/*      */   {
/*   86 */     double[] cert = new double[length()];
/*   87 */     for (int i = 0; i < length(); i++) {
/*   88 */       ComparableArray compa = (ComparableArray)getElement(i);
/*   89 */       cert[i] = st
/*      */       
/*   91 */         .score(this.emStSp.getHaploPairFromHaplo(((IntegerDistribution)this.emissions[i]).fixedInteger().intValue()), i);
/*      */     }
/*   93 */     return cert;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Collection<Aberation> getDeletedPositions(EmissionState st)
/*      */   {
/*  100 */     int[][] cnts = new int[2][length()];
/*  101 */     double[] uncertainty = getUncertainty(st);
/*  102 */     for (int i = 0; i < length(); i++) {
/*  103 */       ComparableArray compa = (ComparableArray)getElement(i);
/*  104 */       for (int k = 0; k < compa.size(); k++) {
/*  105 */         Comparable em = compa.get(k);
/*  106 */         cnts[k][i] = ((em instanceof Emiss) ? ((Emiss)em).noCopies() : ((ComparableArray)em).getGenotypeString().length());
/*      */       }
/*      */     }
/*      */     
/*  110 */     List<Aberation> res = new ArrayList();
/*  111 */     for (int k = 0; k < cnts.length; k++) {
/*  112 */       res.addAll(Aberation.getAberation(getName() + "_" + k, cnts[k], uncertainty));
/*      */     }
/*  114 */     return res;
/*      */   }
/*      */   
/*      */   public String getStringRep(int start, int end)
/*      */   {
/*  119 */     StringBuffer sb = new StringBuffer();
/*  120 */     for (int i = start; i <= end; i++) {
/*  121 */       sb.append(getStringRep(i));
/*      */     }
/*  123 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Set<Integer>[] getSwitches()
/*      */   {
/*  130 */     Set[] switches = new TreeSet[noCopies()];
/*      */     
/*  132 */     Comparable[] prev = new Comparable[switches.length];
/*  133 */     for (int j = 0; j < prev.length; j++) {
/*  134 */       prev[j] = ((ComparableArray)getElement(0)).get(j);
/*  135 */       switches[j] = new TreeSet();
/*      */     }
/*  137 */     for (int i = 1; i < length(); i++) {
/*  138 */       for (int j = 0; j < prev.length; j++) {
/*  139 */         Comparable nxt = ((ComparableArray)getElement(i)).get(j);
/*  140 */         if (!prev[j].equals(nxt)) {
/*  141 */           switches[j].add(Integer.valueOf(i));
/*      */         }
/*  143 */         prev[j] = nxt;
/*      */       }
/*      */     }
/*  146 */     return switches;
/*      */   }
/*      */   
/*      */   public PIGData recombine(Map<Integer, Integer> recSites, int starting_index) {
/*  150 */     if ((noCopies() != 2) && (noCopies() != 1)) { throw new RuntimeException("!!");
/*      */     }
/*  152 */     PhasedDataState rec = new PhasedDataState(getName() + "r", length(), ((lc1.dp.emissionspace.CompoundEmissionStateSpace)this.emStSp).getMembers()[0], this.data_index);
/*  153 */     int index = starting_index;
/*  154 */     for (int i = 0; i < length(); i++) {
/*  155 */       if (recSites.containsKey(Integer.valueOf(i))) {
/*  156 */         index = 1 - index;
/*      */       }
/*  158 */       ComparableArray comp = (ComparableArray)getElement(i);
/*      */       
/*  160 */       rec.addPoint(i, ComparableArray.make(((ComparableArray)getElement(i)).get(index)));
/*      */     }
/*  162 */     return rec;
/*      */   }
/*      */   
/*      */   private String conc(List<String> names) {
/*  166 */     StringBuffer sb = new StringBuffer();
/*  167 */     for (Iterator<String> it = names.iterator(); it.hasNext();) {
/*  168 */       sb.append((String)it.next());
/*  169 */       if (it.hasNext()) sb.append("_");
/*      */     }
/*  171 */     return sb.toString();
/*      */   }
/*      */   
/*      */   public PhasedDataState[] split(int[] is) {
/*  175 */     List<String> names = Arrays.asList(getName().split("_"));
/*  176 */     PhasedDataState[] res = new PhasedDataState[is.length];
/*  177 */     int[] from = new int[is.length];
/*  178 */     int[] to = new int[is.length];
/*  179 */     for (int j = 0; j < res.length; j++) {
/*  180 */       from[j] = (j == 0 ? 0 : to[(j - 1)]);
/*  181 */       from[j] += is[j];
/*  182 */       res[j] = new PhasedDataState(conc(names.subList(from[j], to[j])), length(), this.emStSp, this.data_index);
/*      */     }
/*  184 */     for (int i = 0; i < length(); i++) {
/*  185 */       ComparableArray compa = (ComparableArray)getElement(i);
/*  186 */       for (int j = 0; j < is.length; j++) {
/*  187 */         ComparableArray compa_1 = new ComparableArray(compa.elements().subList(from[j], to[j]));
/*  188 */         res[j].addPoint(i, compa_1);
/*      */       }
/*      */     }
/*  191 */     return res;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PhasedDataState[] split()
/*      */   {
/*  200 */     List<String> names = Arrays.asList(getName().split(";"));
/*      */     
/*  202 */     PhasedDataState[] res = new PhasedDataState[((ComparableArray)getElement(0)).size()];
/*  203 */     int[] from = new int[res.length];
/*  204 */     int[] to = new int[res.length];
/*  205 */     for (int j = 0; j < res.length; j++) {
/*  206 */       from[j] = (j == 0 ? 0 : to[(j - 1)]);
/*  207 */       Comparable compa = ((ComparableArray)getElement(0)).get(j);
/*  208 */       from[j] += 
/*  209 */         ((compa instanceof ComparableArray) ? 
/*  210 */         ((ComparableArray)compa).size() : 1);
/*  211 */       String name = j < names.size() ? (String)names.get(j) : "";
/*  212 */       res[j] = new PhasedDataState(name, length(), this.emStSp, this.data_index);
/*      */     }
/*  214 */     for (int i = 0; i < length(); i++) {
/*  215 */       ComparableArray compa = (ComparableArray)getElement(i);
/*  216 */       for (int j = 0; j < compa.size(); j++) {
/*  217 */         res[j].addPoint(i, compa.getReal(j));
/*      */       }
/*      */     }
/*  220 */     return res;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[] phaseCorrect(CSOData ph, Collection<Integer> noCopiesL, Collection<Integer> noCopiesR)
/*      */   {
/*  232 */     PhasedDataState original = (PhasedDataState)ph;
/*  233 */     Set<Integer> poss = new HashSet();
/*  234 */     int correct = 0;
/*  235 */     int wrong = 0;
/*  236 */     Integer[] phasePos = updatePhasePositions();
/*  237 */     for (int jk = 0; jk < phasePos.length; jk++) {
/*  238 */       int i = phasePos[jk].intValue();
/*  239 */       int hapl = getFixedInteger(i).intValue();
/*  240 */       int haploPair = this.emStSp.getHaploPairFromHaplo(hapl);
/*  241 */       if ((haploPair == this.emStSp.getHaploPairFromHaplo(original.getFixedInteger(i).intValue())) && 
/*  242 */         (noCopiesR.contains(Integer.valueOf(this.emStSp.getCN(haploPair)))))
/*      */       {
/*      */ 
/*      */ 
/*  246 */         poss.clear();
/*  247 */         int[] possib = this.emStSp.getHaploFromHaploPair(haploPair);
/*  248 */         addAll(poss, possib);
/*  249 */         for (int jk1 = jk - 1; jk1 >= 0; jk1--) {
/*  250 */           int i1 = phasePos[jk1].intValue();
/*  251 */           int hapl_1 = getFixedInteger(i1).intValue();
/*  252 */           int haploPair_1 = this.emStSp.getHaploPairFromHaplo(hapl_1);
/*  253 */           if ((haploPair_1 != this.emStSp.getHaploPairFromHaplo(original.getFixedInteger(i1).intValue())) || 
/*  254 */             (!noCopiesL.contains(Integer.valueOf(this.emStSp.getCN(haploPair_1))))) {
/*      */             break;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  262 */           phaseRelative(original, i, i1, poss);
/*  263 */           if (!poss.contains(Integer.valueOf(hapl)))
/*      */           {
/*  265 */             wrong++;
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  270 */             correct++;
/*      */             
/*      */ 
/*      */ 
/*  274 */             if (poss.size() == 1) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  281 */     return new int[] { wrong, correct + wrong };
/*      */   }
/*      */   
/*      */   public static int countDiff(Comparable o1, Comparable o2) {
/*  285 */     ComparableArray obj1 = (ComparableArray)o1;
/*  286 */     ComparableArray obj2 = (ComparableArray)o2;
/*  287 */     int cnt = 0;
/*  288 */     for (int i = 0; i < obj1.size(); i++) {
/*  289 */       if (!obj1.get(i).equals(obj2.get(i))) cnt++;
/*      */     }
/*  291 */     return cnt;
/*      */   }
/*      */   
/*      */   public static void printIdLine(String idLine, PrintWriter pw, int len)
/*      */   {
/*  296 */     char[] ch = new char[Math.max(idLine.length(), len)];
/*  297 */     Arrays.fill(ch, ' ');
/*  298 */     System.arraycopy(idLine.toCharArray(), 0, ch, 0, idLine.length());
/*  299 */     int jmp = 10;
/*  300 */     int jmp2 = 100;
/*  301 */     for (int i = 0; i < ch.length; i += jmp) {
/*  302 */       if ((Math.IEEEremainder(i, jmp2) == 0.0D) && (idLine.length() + i < ch.length)) {
/*  303 */         System.arraycopy(idLine.toCharArray(), 0, ch, i, idLine.length());
/*      */       }
/*  305 */       if (i >= idLine.length()) {
/*  306 */         ch[i] = '|';
/*      */       }
/*      */     }
/*  309 */     pw.println(new String(ch));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isNested()
/*      */   {
/*  316 */     if ((getElement(0) instanceof ComparableArray)) {
/*  317 */       return ((ComparableArray)getElement(0)).get(0) instanceof ComparableArray;
/*      */     }
/*  319 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public void print(PrintWriter pw, boolean idline, boolean expand, boolean mark, Collection<Integer> toDrop)
/*      */   {
/*  325 */     if (idline) {
/*  326 */       if (mark) printIdLine("# id " + getName(), pw, length()); else
/*  327 */         pw.println("# id " + getName());
/*      */     }
/*  329 */     if (toDrop != null) throw new RuntimeException("!!");
/*  330 */     if (isNested()) {
/*  331 */       PhasedDataState[] data = split();
/*  332 */       for (int i = 0; i < data.length; i++)
/*      */       {
/*  334 */         data[i].print(pw, false, expand, mark, toDrop);
/*      */       }
/*      */     }
/*      */     else {
/*  338 */       Object[][] l = new Object[noCopies()][];
/*  339 */       for (int i = 0; i < l.length; i++) {
/*  340 */         l[i] = new Object[length()];
/*      */       }
/*  342 */       for (int i = 0; i < length(); i++) {
/*  343 */         List<Comparable> list = new ArrayList();
/*  344 */         ((ComparableArray)getElement(i)).addObjectsRecursive(list);
/*  345 */         int k = 0;
/*  346 */         for (Iterator<Comparable> it = list.iterator(); it.hasNext(); k++) {
/*  347 */           Object nxt = it.next();
/*  348 */           l[k][i] = nxt;
/*      */         }
/*      */       }
/*  351 */       for (int j = 0; j < l.length; j++) {
/*  352 */         Object[][] res = { expand ? expand(l[j]) : l[j] };
/*  353 */         for (int k = 0; k < res.length; k++) {
/*  354 */           for (int i = 0; i < res[k].length; i++) {
/*  355 */             if ((toDrop == null) || (!toDrop.contains(Integer.valueOf(i)))) {
/*  356 */               printElement(pw, res[k][i], expand);
/*      */             }
/*      */           }
/*  359 */           pw.println();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*  365 */   private static Object[][] transpose(Object[][] obj) { int max = 0;
/*  366 */     for (int i = 0; i < obj.length; i++) {
/*  367 */       if (obj[i].length > max) {
/*  368 */         max = obj[i].length;
/*      */       }
/*      */     }
/*  371 */     Object[][] res = new Object[max][obj.length];
/*  372 */     for (int i = 0; i < obj.length; i++) {
/*  373 */       for (int j = 0; j < obj[i].length; j++) {
/*  374 */         res[j][i] = obj[i][j];
/*      */       }
/*      */     }
/*  377 */     return res;
/*      */   }
/*      */   
/*      */   static Object[][] expand(Object[] comp) {
/*  381 */     List<Object[]> l = new ArrayList();
/*  382 */     for (int i = 0; i < comp.length; i++)
/*  383 */       if ((comp[i] instanceof Emiss)) {
/*  384 */         ComparableArray compa = ((Emiss)comp[i]).expand();
/*  385 */         l.add(compa.elements().toArray());
/*      */       }
/*      */       else
/*      */       {
/*  389 */         l.add(new Object[] { comp[i] });
/*      */       }
/*  391 */     return transpose((Object[][])l.toArray(new Object[0][]));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void print(PrintWriter pw, boolean expand, boolean mark, Collection<Integer> toDrop)
/*      */   {
/*  398 */     print(pw, true, expand, mark, toDrop);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sampleGenotype(HaplotypeEmissionState emst, List<CSOData> spList)
/*      */   {
/*  406 */     EmissionStateSpace emStSp = emst.getEmissionStateSpace();
/*      */     
/*      */ 
/*  409 */     for (int i = 0; i < length(); i++) {
/*  410 */       short dat_index = emst.data_index;
/*  411 */       emst.emissions[i] = new SimpleExtendedDistribution(emStSp.defaultList.size());
/*  412 */       emst.emissions[i].setDataIndex(dat_index);
/*      */       
/*  414 */       for (int j = 0; j < spList.size(); j++) {
/*  415 */         PhasedDataState obj = (PhasedDataState)spList.get(j);
/*  416 */         Integer compa_i = emStSp.getHaploPair(obj.getElement(i));
/*  417 */         if (compa_i == null) {
/*      */           try {
/*  419 */             throw new RuntimeException("no element " + obj.getElement(i) + " in state space \n" + emStSp.defaultList);
/*      */           } catch (Exception exc) {
/*  421 */             exc.printStackTrace();
/*      */           }
/*      */           
/*      */         }
/*      */         else {
/*  426 */           emst.addCount(compa_i.intValue(), obj.dataIndex(i), 1.0D, i);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  433 */     emst.transferCountsToProbs(0.0D);
/*  434 */     for (int i = 0; i < length(); i++) {
/*  435 */       set(i, emStSp.getHaploPair(Integer.valueOf(emst.getBestIndex(i))));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double samplePhase1(EmissionStateSpace emStSp, List<CSOData> spList, int i1, int i2, Set<Integer> poss)
/*      */   {
/*  447 */     int len = length();
/*      */     
/*  449 */     int hap1 = getFixedInteger(i1).intValue();
/*  450 */     int hap2 = getFixedInteger(i2).intValue();
/*  451 */     int haplopairIndex1 = emStSp.getHaploPairFromHaplo(hap1);
/*  452 */     int haplopairIndex2 = emStSp.getHaploPairFromHaplo(hap2);
/*  453 */     Map<Integer, Integer> counts = new HashMap();
/*  454 */     for (Iterator<Integer> it = poss.iterator(); it.hasNext();) {
/*  455 */       counts.put((Integer)it.next(), Integer.valueOf(0));
/*      */     }
/*  457 */     int sumMatch = 0;
/*  458 */     for (int j = 0; j < spList.size(); j++) {
/*  459 */       PhasedDataState data = (PhasedDataState)spList.get(j);
/*  460 */       int hap1_j = data.getFixedInteger(i1).intValue();
/*  461 */       int haplopair_j = emStSp.getHaploPairFromHaplo(hap1_j);
/*  462 */       if (haplopair_j == haplopairIndex1) {
/*  463 */         int hap2_j = data.getFixedInteger(i2).intValue();
/*  464 */         int haplopair2_j = emStSp.getHaploPairFromHaplo(hap2_j);
/*  465 */         if (haplopair2_j == haplopairIndex2) {
/*  466 */           if (hap2_j != hap2) {
/*  467 */             hap1_j = emStSp.flip(hap1_j);
/*      */           }
/*  469 */           counts.put(Integer.valueOf(hap1_j), Integer.valueOf(((Integer)counts.get(Integer.valueOf(hap1_j))).intValue() + 1));
/*  470 */           sumMatch++;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  476 */     int max_index = getMax(counts);
/*  477 */     double max = ((Integer)counts.get(Integer.valueOf(max_index))).intValue();
/*  478 */     double certainty = sumMatch == 0 ? 0.0D : max / sumMatch;
/*  479 */     List<Integer> l1 = new ArrayList();
/*  480 */     for (Iterator<Integer> it = poss.iterator(); it.hasNext();) {
/*  481 */       int next = ((Integer)it.next()).intValue();
/*  482 */       if (((Integer)counts.get(Integer.valueOf(next))).intValue() < max) it.remove();
/*      */     }
/*  484 */     if (poss.size() == 0) {
/*  485 */       poss.addAll(l1);
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
/*  499 */     return certainty;
/*      */   }
/*      */   
/*      */   public int getRandomIndext(int size)
/*      */   {
/*  504 */     List<Integer> randomIndex = new ArrayList();
/*  505 */     SortedMap<Double, Integer> temp = new TreeMap();
/*  506 */     Random ran = new Random();
/*  507 */     for (int i = 0; i < size; i++) {
/*  508 */       temp.put(Double.valueOf(ran.nextDouble()), Integer.valueOf(i));
/*      */     }
/*  510 */     for (Iterator<Double> it = temp.keySet().iterator(); it.hasNext();) {
/*  511 */       Double nt = (Double)it.next();
/*  512 */       randomIndex.add((Integer)temp.get(nt));
/*      */     }
/*  514 */     return ((Integer)randomIndex.get(0)).intValue();
/*      */   }
/*      */   
/*      */   private void phaseRelative(CSOData orig, int i, int i1, Collection<Integer> poss)
/*      */   {
/*  519 */     PhasedDataState original = (PhasedDataState)orig;
/*      */     
/*  521 */     int hapl = getFixedInteger(i).intValue();
/*  522 */     int hapl_1 = getFixedInteger(i1).intValue();
/*  523 */     int o_hapl = original.getFixedInteger(i).intValue();
/*  524 */     int o_hapl_1 = original.getFixedInteger(i1).intValue();
/*  525 */     if (o_hapl_1 != hapl_1) {
/*  526 */       o_hapl = this.emStSp.flip(o_hapl);
/*      */     }
/*      */     
/*  529 */     for (Iterator<Integer> it = poss.iterator(); it.hasNext();) {
/*  530 */       if (((Integer)it.next()).intValue() != o_hapl) it.remove();
/*      */     }
/*      */   }
/*      */   
/*      */   private int getMax(Map<Integer, Integer> counts) {
/*  535 */     Iterator<Integer> it = counts.keySet().iterator();
/*  536 */     Integer key = (Integer)it.next();
/*  537 */     while (it.hasNext()) {
/*  538 */       Integer key1 = (Integer)it.next();
/*  539 */       if (((Integer)counts.get(key1)).intValue() > ((Integer)counts.get(key)).intValue()) key = key1;
/*      */     }
/*  541 */     return key.intValue();
/*      */   }
/*      */   
/*      */   private int getMax(Integer[][] diff, int i) {
/*  545 */     int max = 0;
/*  546 */     for (int k = 1; k < diff.length; k++) {
/*  547 */       if (diff[k][i].intValue() > diff[max][i].intValue()) {
/*  548 */         max = k;
/*      */       }
/*      */     }
/*  551 */     return max;
/*      */   }
/*      */   
/*      */   public Integer trans(String st, EmissionStateSpace emStSp) {
/*  555 */     if (st.indexOf('N') >= 0) {
/*  556 */       st = st.replace('A', 'N');
/*  557 */       st = st.replace('B', 'N');
/*  558 */       return null;
/*      */     }
/*  560 */     Comparable[] comp = new Comparable[st.length()];
/*  561 */     for (int ik = 0; ik < st.length(); ik++) {
/*  562 */       comp[ik] = Emiss.translate(st.charAt(ik));
/*      */     }
/*  564 */     return emStSp.getHapl(new ComparableArray(comp));
/*      */   }
/*      */   
/*      */ 
/*      */   public List<String> getHaplotype(int[] hap, EmissionStateSpace emStSp)
/*      */   {
/*  570 */     List<String> haplotypes = new ArrayList();
/*  571 */     for (int i = 1; i < 3 * emStSp.noCopies() - 1; i += 3) {
/*  572 */       char[] st = new char[hap.length];
/*  573 */       for (int j = 0; j < hap.length; j++) {
/*  574 */         st[j] = emStSp.get(hap[j]).toString().charAt(i);
/*      */       }
/*  576 */       haplotypes.add(new String(st));
/*      */     }
/*  578 */     return haplotypes;
/*      */   }
/*      */   
/*      */   public int getNoHaplotype(List<String> haplotypes) {
/*  582 */     List<String> temp = new ArrayList();
/*  583 */     for (int i = 0; i < haplotypes.size(); i++) {
/*  584 */       if (!temp.contains(haplotypes.get(i))) temp.add((String)haplotypes.get(i));
/*      */     }
/*  586 */     return temp.size();
/*      */   }
/*      */   
/*      */   public int getPossibHapSets(int[] possib, Map<Integer, List<String>> possHap, int nextIndex, Integer[] phasePos, List<Integer> seletedHap)
/*      */   {
/*  591 */     int hap1 = getFixedInteger(phasePos[nextIndex].intValue()).intValue();
/*  592 */     int next2 = -1;
/*  593 */     for (int nex = nextIndex - 1; nex >= 0; nex--) {
/*  594 */       next2 = phasePos[nex].intValue();
/*  595 */       int hap0 = getFixedInteger(next2).intValue();
/*  596 */       List<String> haplotypes = getHaplotype(new int[] { hap0, hap1 }, this.emStSp);
/*  597 */       int noDiffHap = getNoHaplotype(haplotypes);
/*  598 */       if (noDiffHap == 3) break;
/*      */     }
/*  600 */     if (seletedHap.isEmpty()) {
/*  601 */       for (int k = 0; k < possib.length; k++) {
/*  602 */         int hap0 = 0;
/*  603 */         if (next2 != -1) hap0 = getFixedInteger(next2).intValue();
/*  604 */         int[] hap = { hap0, hap1, possib[k] };
/*  605 */         List<String> haplotypes = getHaplotype(hap, this.emStSp);
/*  606 */         possHap.put(Integer.valueOf(possib[k]), haplotypes);
/*      */       }
/*      */       
/*      */     } else {
/*  610 */       for (int k = 0; k < seletedHap.size(); k++) {
/*  611 */         int hap0 = 0;
/*  612 */         if (next2 != -1) hap0 = getFixedInteger(next2).intValue();
/*  613 */         int[] hap = { hap0, hap1, ((Integer)seletedHap.get(k)).intValue() };
/*  614 */         List<String> haplotypes = getHaplotype(hap, this.emStSp);
/*  615 */         possHap.put((Integer)seletedHap.get(k), haplotypes);
/*      */       }
/*      */     }
/*  618 */     return next2;
/*      */   }
/*      */   
/*      */   public int[] getPossibHapSets4(int[] possib, Map<Integer, List<String>> possHap, int nextIndex, Integer[] phasePos, List<Integer> seletedHap)
/*      */   {
/*  623 */     int hap1 = getFixedInteger(phasePos[nextIndex].intValue()).intValue();
/*  624 */     int[] next2 = { -1, -1 };
/*  625 */     int nextIndex1 = 0;
/*  626 */     if (nextIndex == 1) {
/*  627 */       next2[0] = phasePos[0].intValue();
/*      */     }
/*      */     else {
/*  630 */       for (int nex = nextIndex - 1; nex >= 1; nex--) {
/*  631 */         next2[0] = phasePos[nex].intValue();
/*  632 */         nextIndex1 = nex;
/*  633 */         int hap0 = getFixedInteger(next2[0]).intValue();
/*  634 */         List<String> haplotypes = getHaplotype(new int[] { hap0, hap1 }, this.emStSp);
/*  635 */         int noDiffHap = getNoHaplotype(haplotypes);
/*  636 */         if (noDiffHap == 3) break;
/*      */       }
/*  638 */       for (int nex = nextIndex1 - 1; nex >= 0; nex--) {
/*  639 */         next2[1] = phasePos[nex].intValue();
/*  640 */         int hap00 = getFixedInteger(next2[1]).intValue();
/*  641 */         int hap0 = getFixedInteger(next2[0]).intValue();
/*  642 */         List<String> haplotypes = getHaplotype(new int[] { hap00, hap0, hap1 }, this.emStSp);
/*  643 */         int noDiffHap = getNoHaplotype(haplotypes);
/*  644 */         if (noDiffHap == 4)
/*      */           break;
/*      */       }
/*      */     }
/*  648 */     if (seletedHap.isEmpty()) {
/*  649 */       for (int k = 0; k < possib.length; k++) {
/*  650 */         int hap00 = 0;
/*  651 */         int hap0 = 0;
/*  652 */         if (next2[0] != -1) hap0 = getFixedInteger(next2[0]).intValue();
/*  653 */         if (next2[1] != -1) hap00 = getFixedInteger(next2[1]).intValue();
/*  654 */         int[] hap = { hap00, hap0, hap1, possib[k] };
/*  655 */         List<String> haplotypes = getHaplotype(hap, this.emStSp);
/*  656 */         possHap.put(Integer.valueOf(possib[k]), haplotypes);
/*      */       }
/*      */       
/*      */     } else {
/*  660 */       for (int k = 0; k < seletedHap.size(); k++) {
/*  661 */         int hap00 = 0;
/*  662 */         int hap0 = 0;
/*  663 */         if (next2[0] != -1) hap0 = getFixedInteger(next2[0]).intValue();
/*  664 */         if (next2[1] != -1) hap00 = getFixedInteger(next2[1]).intValue();
/*  665 */         int[] hap = { hap00, hap0, hap1, ((Integer)seletedHap.get(k)).intValue() };
/*  666 */         List<String> haplotypes = getHaplotype(hap, this.emStSp);
/*  667 */         possHap.put((Integer)seletedHap.get(k), haplotypes);
/*      */       }
/*      */     }
/*  670 */     return next2;
/*      */   }
/*      */   
/*      */ 
/*      */   public double samplePhase(EmissionStateSpace emStSp, List<CSOData> spList, int i1, int i2, Map<Integer, List<String>> possHap, int next2, List<Integer> seletedHap)
/*      */   {
/*  676 */     int hap1 = getFixedInteger(i1).intValue();
/*  677 */     int hap2 = getFixedInteger(i2).intValue();
/*  678 */     int hap3 = 0;
/*  679 */     if (next2 != -1) hap3 = getFixedInteger(next2).intValue();
/*  680 */     int haplopairIndex1 = emStSp.getHaploPairFromHaplo(hap1);
/*  681 */     int haplopairIndex2 = emStSp.getHaploPairFromHaplo(hap2);
/*  682 */     int haplopairIndex3 = emStSp.getHaploPairFromHaplo(hap3);
/*  683 */     Map<Integer, Integer> counts = new HashMap();
/*  684 */     for (Iterator<Integer> it = possHap.keySet().iterator(); it.hasNext();) {
/*  685 */       counts.put((Integer)it.next(), Integer.valueOf(0));
/*      */     }
/*  687 */     int sumMatch = 0;
/*  688 */     for (int j = 0; j < spList.size(); j++) {
/*  689 */       PhasedDataState data = (PhasedDataState)spList.get(j);
/*  690 */       int hap1_j = data.getFixedInteger(i1).intValue();
/*  691 */       int haplopair_j = emStSp.getHaploPairFromHaplo(hap1_j);
/*  692 */       if (haplopair_j == haplopairIndex1) {
/*  693 */         int hap2_j = data.getFixedInteger(i2).intValue();
/*  694 */         int haplopair2_j = emStSp.getHaploPairFromHaplo(hap2_j);
/*  695 */         if (haplopair2_j == haplopairIndex2) {
/*  696 */           int hap3_j = 0;
/*  697 */           if (next2 != -1) hap3_j = data.getFixedInteger(next2).intValue();
/*  698 */           int haplopair3_j = emStSp.getHaploPairFromHaplo(hap3_j);
/*  699 */           if (haplopair3_j == haplopairIndex3) {
/*  700 */             List<String> samplHaplotypes = getHaplotype(new int[] { hap3_j, hap2_j, hap1_j }, emStSp);
/*      */             
/*  702 */             for (Iterator<Integer> it = possHap.keySet().iterator(); it.hasNext();) {
/*  703 */               int pos = ((Integer)it.next()).intValue();
/*  704 */               if (((List)possHap.get(Integer.valueOf(pos))).containsAll(samplHaplotypes)) {
/*  705 */                 counts.put(Integer.valueOf(pos), Integer.valueOf(((Integer)counts.get(Integer.valueOf(pos))).intValue() + 1));
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  712 */             sumMatch++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  720 */     int max_index = getMax(counts);
/*  721 */     double max = ((Integer)counts.get(Integer.valueOf(max_index))).intValue();
/*  722 */     double certainty = sumMatch == 0 ? 0.0D : max / sumMatch;
/*  723 */     if ((seletedHap.isEmpty()) || (seletedHap.size() > 1)) {
/*  724 */       seletedHap.clear();
/*  725 */       for (Iterator<Integer> it = possHap.keySet().iterator(); it.hasNext();) {
/*  726 */         int index = ((Integer)it.next()).intValue();
/*  727 */         if (((Integer)counts.get(Integer.valueOf(index))).intValue() == max) { seletedHap.add(Integer.valueOf(index));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  739 */     return certainty;
/*      */   }
/*      */   
/*      */   public double samplePhase4(EmissionStateSpace emStSp, List<CSOData> spList, int i1, int i2, Map<Integer, List<String>> possHap, int[] next2, List<Integer> seletedHap) {
/*  743 */     int hap1 = getFixedInteger(i1).intValue();
/*  744 */     int hap2 = getFixedInteger(i2).intValue();
/*  745 */     int hap3 = 0;int hap4 = 0;
/*  746 */     if (next2[0] != -1) hap3 = getFixedInteger(next2[0]).intValue();
/*  747 */     if (next2[1] != -1) hap4 = getFixedInteger(next2[1]).intValue();
/*  748 */     int haplopairIndex1 = emStSp.getHaploPairFromHaplo(hap1);
/*  749 */     int haplopairIndex2 = emStSp.getHaploPairFromHaplo(hap2);
/*  750 */     int haplopairIndex3 = emStSp.getHaploPairFromHaplo(hap3);
/*  751 */     int haplopairIndex4 = emStSp.getHaploPairFromHaplo(hap4);
/*  752 */     Map<Integer, Integer> counts = new HashMap();
/*  753 */     for (Iterator<Integer> it = possHap.keySet().iterator(); it.hasNext();) {
/*  754 */       counts.put((Integer)it.next(), Integer.valueOf(0));
/*      */     }
/*  756 */     int sumMatch = 0;
/*  757 */     for (int j = 0; j < spList.size(); j++) {
/*  758 */       PhasedDataState data = (PhasedDataState)spList.get(j);
/*  759 */       int hap1_j = data.getFixedInteger(i1).intValue();
/*  760 */       int haplopair_j = emStSp.getHaploPairFromHaplo(hap1_j);
/*  761 */       if (haplopair_j == haplopairIndex1) {
/*  762 */         int hap2_j = data.getFixedInteger(i2).intValue();
/*  763 */         int haplopair2_j = emStSp.getHaploPairFromHaplo(hap2_j);
/*  764 */         if (haplopair2_j == haplopairIndex2) {
/*  765 */           int hap3_j = 0;
/*  766 */           if (next2[0] != -1) hap3_j = data.getFixedInteger(next2[0]).intValue();
/*  767 */           int haplopair3_j = emStSp.getHaploPairFromHaplo(hap3_j);
/*  768 */           if (haplopair3_j == haplopairIndex3) {
/*  769 */             int hap4_j = 0;
/*  770 */             if (next2[1] != -1) hap4_j = data.getFixedInteger(next2[1]).intValue();
/*  771 */             int haplopair4_j = emStSp.getHaploPairFromHaplo(hap4_j);
/*  772 */             if (haplopair4_j == haplopairIndex4) {
/*  773 */               List<String> samplHaplotypes = getHaplotype(new int[] { hap4_j, hap3_j, hap2_j, hap1_j }, emStSp);
/*  774 */               for (Iterator<Integer> it = possHap.keySet().iterator(); it.hasNext();) {
/*  775 */                 int pos = ((Integer)it.next()).intValue();
/*  776 */                 if (((List)possHap.get(Integer.valueOf(pos))).containsAll(samplHaplotypes)) {
/*  777 */                   counts.put(Integer.valueOf(pos), Integer.valueOf(((Integer)counts.get(Integer.valueOf(pos))).intValue() + 1));
/*      */                 }
/*      */               }
/*  780 */               sumMatch++;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  790 */     int max_index = getMax(counts);
/*  791 */     double max = ((Integer)counts.get(Integer.valueOf(max_index))).intValue();
/*  792 */     double certainty = sumMatch == 0 ? 0.0D : max / sumMatch;
/*  793 */     if ((seletedHap.isEmpty()) || (seletedHap.size() > 1)) {
/*  794 */       seletedHap.clear();
/*  795 */       for (Iterator<Integer> it = possHap.keySet().iterator(); it.hasNext();) {
/*  796 */         int index = ((Integer)it.next()).intValue();
/*  797 */         if (((Integer)counts.get(Integer.valueOf(index))).intValue() == max) { seletedHap.add(Integer.valueOf(index));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  806 */     return certainty;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void samplePhase(List<CSOData> spList, double[] uncertainty, EmissionStateSpace emStSp)
/*      */   {
/*  813 */     if (((ComparableArray)getElement(0)).isNested()) {
/*  814 */       throw new RuntimeException("!!");
/*      */     }
/*      */     
/*      */ 
/*  818 */     Map<Integer, List<String>> possHap = new HashMap();
/*  819 */     if (emStSp.noCopies() == 1) {
/*  820 */       for (int i = 0; i < length(); i++) {
/*  821 */         Map<Integer, Integer> counts = new HashMap();
/*  822 */         for (int j = 0; j < spList.size(); j++) {
/*  823 */           PhasedDataState data = (PhasedDataState)spList.get(j);
/*  824 */           int hap1_j = data.getFixedInteger(i).intValue();
/*  825 */           if (!counts.containsKey(Integer.valueOf(hap1_j))) counts.put(Integer.valueOf(hap1_j), Integer.valueOf(1)); else {
/*  826 */             counts.put(Integer.valueOf(hap1_j), Integer.valueOf(((Integer)counts.get(Integer.valueOf(hap1_j))).intValue() + 1));
/*      */           }
/*      */         }
/*  829 */         int max_index = getMax(counts);
/*  830 */         double max = ((Integer)counts.get(Integer.valueOf(max_index))).intValue();
/*  831 */         uncertainty[i] = (max / spList.size());
/*  832 */         ((IntegerDistribution)this.emissions[i]).setFixedIndex(max_index);
/*      */       }
/*      */     }
/*  835 */     if (emStSp.noCopies() == 2) {
/*  836 */       Integer[] phasePos = updatePhasePositions();
/*  837 */       Set<Integer> poss = new HashSet();
/*  838 */       for (int jk = 1; jk < phasePos.length; jk++) {
/*  839 */         poss.clear();
/*  840 */         int i = phasePos[jk].intValue();
/*  841 */         int hapl = getFixedInteger(i).intValue();
/*  842 */         int haplPair = emStSp.getHaploPairFromHaplo(hapl);
/*  843 */         int[] possib = emStSp.getHaploFromHaploPair(haplPair);
/*  844 */         addAll(poss, possib);
/*  845 */         for (int jk1 = jk - 1; (jk1 >= 0) && (poss.size() > 1); jk1--)
/*      */         {
/*  847 */           int i1 = phasePos[jk1].intValue();
/*  848 */           uncertainty[i] *= samplePhase1(emStSp, spList, i, i1, poss);
/*      */         }
/*  850 */         if (poss.size() > 0) {
/*  851 */           Integer res = (Integer)poss.iterator().next();
/*  852 */           ((IntegerDistribution)this.emissions[i]).setFixedIndex(res.intValue());
/*      */         }
/*      */       }
/*      */     }
/*  856 */     if (emStSp.noCopies() == 3) {
/*  857 */       Integer[] phasePos = updatePhasePositions();
/*  858 */       for (int jk = 1; jk < phasePos.length; jk++) {
/*  859 */         int i = phasePos[jk].intValue();
/*  860 */         int hap1 = getFixedInteger(i).intValue();
/*  861 */         int haplPair = emStSp.getHaploPairFromHaplo(hap1);
/*  862 */         int[] possib = emStSp.getHaploFromHaploPair(haplPair);
/*  863 */         List<Integer> seletedHap = new ArrayList();
/*  864 */         for (int jk1 = jk - 1; (jk1 >= 0) && ((seletedHap.size() > 1) || (seletedHap.isEmpty())); jk1--)
/*      */         {
/*  866 */           possHap.clear();
/*  867 */           int i1 = phasePos[jk1].intValue();
/*  868 */           int next2 = getPossibHapSets(possib, possHap, jk1, phasePos, seletedHap);
/*  869 */           uncertainty[i] *= samplePhase(emStSp, spList, i, i1, possHap, next2, seletedHap);
/*      */         }
/*  871 */         if (seletedHap.size() > 0) {
/*  872 */           ((IntegerDistribution)this.emissions[i]).setFixedIndex(((Integer)seletedHap.get(0)).intValue());
/*      */         } else
/*  874 */           throw new RuntimeException("!!");
/*      */       }
/*      */     }
/*  877 */     if (emStSp.noCopies() == 4) {
/*  878 */       Integer[] phasePos = updatePhasePositions();
/*  879 */       for (int jk = 1; jk < phasePos.length; jk++) {
/*  880 */         int i = phasePos[jk].intValue();
/*  881 */         int hap1 = getFixedInteger(i).intValue();
/*  882 */         int haplPair = emStSp.getHaploPairFromHaplo(hap1);
/*  883 */         int[] possib = emStSp.getHaploFromHaploPair(haplPair);
/*  884 */         List<Integer> seletedHap = new ArrayList();
/*  885 */         for (int jk1 = jk - 1; (jk1 >= 0) && ((seletedHap.size() > 1) || (seletedHap.isEmpty())); jk1--)
/*      */         {
/*  887 */           possHap.clear();
/*  888 */           int i1 = phasePos[jk1].intValue();
/*  889 */           int[] next2 = getPossibHapSets4(possib, possHap, jk1, phasePos, seletedHap);
/*  890 */           uncertainty[i] *= samplePhase4(emStSp, spList, i, i1, possHap, next2, seletedHap);
/*      */         }
/*  892 */         if (seletedHap.size() > 0) {
/*  893 */           ((IntegerDistribution)this.emissions[i]).setFixedIndex(((Integer)seletedHap.get(0)).intValue());
/*      */         } else {
/*  895 */           throw new RuntimeException("!!");
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void samplePhase1(List<CSOData> spList, double[] uncertainty, EmissionStateSpace emStSp)
/*      */   {
/*  905 */     if (((ComparableArray)getElement(0)).isNested())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  927 */       throw new RuntimeException("!!");
/*      */     }
/*      */     
/*      */ 
/*  931 */     Set<Integer> poss = new HashSet();
/*  932 */     Integer[] phasePos = updatePhasePositions();
/*  933 */     for (int jk = 1; jk < phasePos.length; jk++) {
/*  934 */       poss.clear();
/*  935 */       int i = phasePos[jk].intValue();
/*  936 */       int hapl = getFixedInteger(i).intValue();
/*  937 */       int haplPair = emStSp.getHaploPairFromHaplo(hapl);
/*  938 */       int[] possib = emStSp.getHaploFromHaploPair(haplPair);
/*  939 */       addAll(poss, possib);
/*  940 */       for (int jk1 = jk - 1; (jk1 >= 0) && (poss.size() > 1); jk1--) {
/*  941 */         int i1 = phasePos[jk1].intValue();
/*      */         
/*  943 */         uncertainty[i] *= samplePhase1(emStSp, spList, i, i1, poss);
/*      */       }
/*      */       
/*  946 */       if (poss.size() > 0) {
/*  947 */         Integer res = (Integer)poss.iterator().next();
/*  948 */         ((IntegerDistribution)this.emissions[i]).setFixedIndex(res.intValue());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void addAll(Set<Integer> poss, int[] possib)
/*      */   {
/*  957 */     for (int i = 0; i < possib.length; i++) {
/*  958 */       poss.add(Integer.valueOf(possib[i]));
/*      */     }
/*      */   }
/*      */   
/*      */   public void set(int i, Comparable obj)
/*      */   {
/*  964 */     addPoint(i, obj);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Integer[] updatePhasePositions()
/*      */   {
/*  971 */     List<Integer> l = new ArrayList();
/*  972 */     for (int i = 0; i < length(); i++) {
/*  973 */       int hapl = getFixedInteger(i).intValue();
/*  974 */       int haploPair = this.emStSp.getHaploPairFromHaplo(hapl);
/*  975 */       int[] poss = this.emStSp.getHaploFromHaploPair(haploPair);
/*  976 */       if (poss.length > 1) { l.add(Integer.valueOf(i));
/*      */       }
/*      */     }
/*  979 */     return (Integer[])l.toArray(new Integer[0]);
/*      */   }
/*      */   
/*      */   public void addPoint(int i, Comparable i1) {
/*  983 */     int res = this.emStSp.getHapl(i1).intValue();
/*  984 */     short d_i = this.emissions[i] != null ? this.emissions[i].getDataIndex() : -1;
/*  985 */     this.emissions[i] = new IntegerDistribution(res);
/*  986 */     this.emissions[i].setDataIndex(d_i);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class clazz()
/*      */   {
/*  995 */     throw new RuntimeException("!!");
/*      */   }
/*      */   
/*  998 */   public Comparable getElement(int i) { return this.emStSp.get(this.emissions[i].fixedInteger().intValue()); }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getStringRep(int i)
/*      */   {
/* 1004 */     return getElement(i).toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void printElement(PrintWriter pw, Object el, boolean expand)
/*      */   {
/* 1011 */     if (el == null) { pw.print(" ");
/*      */     }
/*      */     else
/*      */     {
/* 1015 */       ((Integer)el);pw.print((el instanceof Integer) ? Integer.toString(((Integer)el).intValue(), Constants.radix()) : (el instanceof Emiss) ? ((Emiss)el).toStringPrint() : expand ? ((Emiss)el).toStringShort() : el.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setName(String name)
/*      */   {
/* 1024 */     this.name = name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public double getUncertainty(EmissionState st, int i)
/*      */   {
/* 1031 */     EmissionStateSpace emstsp = st.getEmissionStateSpace();
/*      */     
/* 1033 */     return st.score(
/* 1034 */       emstsp.getHaploPairFromHaplo(
/* 1035 */       ((IntegerDistribution)this.emissions[i]).fixedInteger().intValue()), i);
/*      */   }
/*      */   
/*      */   public void mix()
/*      */   {
/* 1040 */     throw new RuntimeException("!!");
/*      */   }
/*      */   
/*      */   public void mkTrCompArray() {
/* 1044 */     throw new RuntimeException("!!");
/*      */   }
/*      */   
/*      */   public void print(PrintWriter pw, String prefix) {
/* 1048 */     StringBuffer sb1 = new StringBuffer(prefix);
/*      */     
/* 1050 */     for (int i = 0; i < getEmissionStateSpace().size(); i++) {
/* 1051 */       sb1.append(getElement(i));
/*      */     }
/* 1053 */     pw.println(prefix + " " + sb1.toString());
/*      */   }
/*      */   
/*      */   public String toString() {
/* 1057 */     StringWriter sw = new StringWriter();
/* 1058 */     PrintWriter pw = new PrintWriter(sw);
/* 1059 */     print(pw, getName());
/*      */     
/*      */ 
/* 1062 */     return sw.getBuffer().toString();
/*      */   }
/*      */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/PhasedDataState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */