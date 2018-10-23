/*     */ package lc1.dp.emissionspace;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import lc1.dp.data.representation.ComparableArray;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.util.Constants;
/*     */ import lc1.util.CopyEnumerator;
/*     */ 
/*     */ public class CompoundEmissionStateSpace
/*     */   extends EmissionStateSpace
/*     */ {
/*     */   final EmissionStateSpace[] members;
/*  21 */   static final Comparator<int[]> comp1 = new Comparator()
/*     */   {
/*     */     public int compare(int[] o1, int[] o2) {
/*  24 */       for (int i = 0; i < o1.length; i++) {
/*  25 */         if (o1[i] != o2[i]) return o1[i] < o2[i] ? -1 : 1;
/*     */       }
/*  27 */       return 0;
/*     */     }
/*     */   };
/*     */   
/*     */   private int[][] haploToMember;
/*  32 */   public SortedMap<int[], Integer> membersIndexToIndex = new TreeMap(comp1);
/*     */   
/*  34 */   public EmissionStateSpace[] getMembers() { return this.members; }
/*     */   
/*     */ 
/*     */ 
/*     */   public CompoundEmissionStateSpace(EmissionStateSpace[] stateSpaces, int noCop)
/*     */   {
/*  40 */     super(noCop);
/*  41 */     this.members = stateSpaces;
/*  42 */     init(initStateSpace(stateSpaces));
/*     */     
/*  44 */     this.haploToMember = new int[this.haploList.size()][];
/*  45 */     for (int i = 0; i < this.haploList.size(); i++) {
/*  46 */       ComparableArray compa = backTranslate((Comparable)this.haploList.get(i));
/*  47 */       this.haploToMember[i] = new int[compa.size()];
/*  48 */       for (int ij = 0; ij < compa.size(); ij++) {
/*  49 */         this.haploToMember[i][ij] = this.members[ij].getHapl(this.members[ij].getHaploString(compa.get(ij))).intValue();
/*     */       }
/*     */     }
/*  52 */     for (int i = 0; i < this.haploToMember.length; i++) {
/*  53 */       int[] members = this.haploToMember[i];
/*  54 */       this.membersIndexToIndex.put(members, Integer.valueOf(i));
/*     */     }
/*  56 */     for (int i = 0; i < this.defaultList.size(); i++) {
/*  57 */       int[] hapL = getHaps(i);
/*  58 */       for (int i1 = 1; i1 < hapL.length; i1++) {
/*  59 */         if (((Comparable)this.haploList.get(i1)).toString().equals(((Comparable)this.haploList.get(0)).toString())) throw new RuntimeException("!!");
/*     */       }
/*     */     }
/*  62 */     SortedSet<Integer> s = new TreeSet();
/*  63 */     for (int i = 0; i < this.haploPairToHaplo.length; i++) {
/*  64 */       s.add(Integer.valueOf(this.haploPairToHaplo[i].length));
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
/*     */   protected ComparableArray backTranslate(Comparable comparable)
/*     */   {
/*  78 */     return (ComparableArray)comparable;
/*     */   }
/*     */   
/*     */ 
/*     */   public int[] getMemberIndices(int haploIndex)
/*     */   {
/*  84 */     return this.haploToMember[haploIndex];
/*     */   }
/*     */   
/*     */ 
/*  88 */   protected Comparable translate(ComparableArray array) { return array; }
/*     */   
/*     */   private List<Comparable> initStateSpace(final List<Comparable>[] stateSpaces) {
/*  91 */     final List<Comparable> set = new ArrayList();
/*  92 */     CopyEnumerator posm = new CopyEnumerator(stateSpaces.length) {
/*     */       public Iterator<Comparable> getPossibilities(int depth) {
/*  94 */         return stateSpaces[depth].iterator();
/*     */       }
/*     */       
/*     */       public void doInner(Comparable[] list)
/*     */       {
/*  99 */         set.add(
/* 100 */           CompoundEmissionStateSpace.this.translate(new ComparableArray(Arrays.asList(list))));
/*     */       }
/*     */       
/*     */ 
/*     */       public boolean exclude(Object obj, Object previous)
/*     */       {
/* 106 */         return false;
/*     */       }
/* 108 */     };
/* 109 */     posm.run();
/*     */     
/* 111 */     return set;
/*     */   }
/*     */   
/* 114 */   public int getIndex(int[] indices) { return this.haploToHaploPair[((Integer)this.membersIndexToIndex.get(indices)).intValue()]; }
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
/*     */   public String getGenotypeString(Comparable comp)
/*     */   {
/* 135 */     if ((comp instanceof ComparableArray)) return ((ComparableArray)comp).getGenotypeString();
/* 136 */     if ((comp instanceof Integer)) return Integer.toString(((Integer)comp).intValue(), Constants.radix());
/* 137 */     return ((Emiss)comp).toStringShort();
/*     */   }
/*     */   
/*     */   public String getHaploString(Comparable comp) {
/* 141 */     if ((comp instanceof ComparableArray)) return ((ComparableArray)comp).getHaplotypeString();
/* 142 */     if ((comp instanceof Integer)) return Integer.toString(((Integer)comp).intValue(), Constants.radix());
/* 143 */     return ((Emiss)comp).toStringPrint();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getHaploPairString(Comparable comp)
/*     */   {
/* 149 */     if ((comp instanceof ComparableArray)) return ((ComparableArray)comp).getHaploPairString();
/* 150 */     if ((comp instanceof Integer)) return Integer.toString(((Integer)comp).intValue(), Constants.radix());
/* 151 */     return ((Emiss)comp).toStringPrint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Integer translateToTarget(Integer comp, CompoundEmissionStateSpace target, int memInd)
/*     */   {
/* 159 */     if (target.size() == size()) return comp;
/* 160 */     int[] memberIndices = getMemberIndices(comp.intValue());
/* 161 */     int[] targetMemberIndices = new int[memberIndices.length];
/*     */     
/* 163 */     for (int ij = 0; ij < memberIndices.length; ij++) {
/* 164 */       targetMemberIndices[ij] = ((CompoundEmissionStateSpace)this.members[ij]).getMemberIndices(memberIndices[ij])[memInd];
/* 165 */       EmissionStateSpace targ1 = ((CompoundEmissionStateSpace)this.members[ij]).getMembers()[memInd];
/* 166 */       EmissionStateSpace targ2 = target.getMembers()[memInd];
/* 167 */       if (targ1 != targ2) throw new RuntimeException("!! " + targ1 + " " + targ2);
/*     */     }
/* 169 */     return target.getGenotype(target.get(target.getIndex(targetMemberIndices)));
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/emissionspace/CompoundEmissionStateSpace.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */