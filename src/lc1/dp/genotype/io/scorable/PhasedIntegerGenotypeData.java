/*     */ package lc1.dp.genotype.io.scorable;
/*     */ 
/*     */ import java.util.List;
/*     */ import lc1.dp.genotype.io.ComparableArray;
/*     */ 
/*     */ 
/*     */ public class PhasedIntegerGenotypeData
/*     */   extends CompoundScorableObject
/*     */ {
/*     */   public PhasedIntegerGenotypeData(String id, int noSites)
/*     */   {
/*  12 */     super(id, noSites);
/*     */   }
/*     */   
/*  15 */   public PhasedIntegerGenotypeData(PhasedIntegerGenotypeData data) { super(data); }
/*     */   
/*     */ 
/*     */   public boolean homozygous()
/*     */   {
/*  20 */     for (int i = 0; i < length(); i++) {
/*  21 */       ComparableArray comp = (ComparableArray)getElement(i);
/*  22 */       for (int j = 1; j < comp.size(); j++) {
/*  23 */         if (comp.get(j) == null) {
/*  24 */           if (comp.get(0) != null) return false;
/*     */         }
/*  26 */         else if (!((Comparable)comp.get(j)).equals(comp.get(0))) {
/*  27 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*  31 */     return true;
/*     */   }
/*     */   
/*  34 */   public void collapse() { for (int i = 0; i < length(); i++) {
/*  35 */       ComparableArray comp = (ComparableArray)getElement(i);
/*  36 */       ComparableArray comp_n = new ComparableArray();
/*  37 */       comp_n.add((Comparable)comp.get(0));
/*  38 */       this.l1.set(i, comp_n);
/*     */     }
/*     */   }
/*     */   
/*  42 */   public char toString(Object obj) { if (obj == null) return '?';
/*  43 */     if ((obj instanceof Boolean)) {
/*  44 */       if (((Boolean)obj).booleanValue()) return '1';
/*  45 */       return '0';
/*     */     }
/*  47 */     return obj.toString().charAt(0);
/*     */   }
/*     */   
/*  50 */   public Comparable translate(char c, String gaps) { if (gaps.indexOf(c) >= 0) return null;
/*  51 */     if (c == '0') return Boolean.FALSE;
/*  52 */     if (c == '1') { return Boolean.TRUE;
/*     */     }
/*  54 */     throw new RuntimeException(new String(new char[] { c }) + c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PhasedIntegerGenotypeData(String id, String[] st)
/*     */   {
/*  61 */     super(id, st[0].length());
/*  62 */     for (int i = 0; i < st[0].length(); i++) {
/*  63 */       ComparableArray point = new ComparableArray();
/*  64 */       for (int j = 0; j < st.length; j++) {
/*  65 */         point.add(translate(st[j].charAt(i), new String(new char[] { '?', '-' })));
/*     */       }
/*  67 */       addPoint(point);
/*     */     }
/*     */   }
/*     */   
/*     */   public PhasedIntegerGenotypeData(CompoundScorableObject[] unit) {
/*  72 */     super(unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */   static Integer zero = new Integer(0);
/*  81 */   static Integer one = new Integer(1);
/*  82 */   static Integer two = new Integer(2);
/*     */   
/*     */   public static Integer get(Boolean b1, Boolean b2) {
/*  85 */     if ((b1 == null) || (b2 == null)) return null;
/*  86 */     if ((b1.booleanValue()) && (b2.booleanValue())) return two;
/*  87 */     if ((b1.booleanValue()) || (b2.booleanValue())) return one;
/*  88 */     return zero;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ComparableArray exampleSplit(Object obj, int noCopies)
/*     */   {
/* 100 */     if (obj == null) return null;
/* 101 */     int i = ((Number)obj).intValue();
/* 102 */     ComparableArray res = new ComparableArray();
/* 103 */     for (int k = 0; k < i; k++) {
/* 104 */       res.add(Boolean.TRUE);
/*     */     }
/* 106 */     for (int k = i; k < noCopies; k++) {
/* 107 */       res.add(Boolean.FALSE);
/*     */     }
/* 109 */     return res;
/*     */   }
/*     */   
/*     */   public ScorableObject clone() {
/* 113 */     return new PhasedIntegerGenotypeData(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void convertHemizygousHomozygous()
/*     */   {
/* 124 */     for (int i = 0; i < length(); i++) {
/* 125 */       ComparableArray comp = (ComparableArray)getElement(i);
/* 126 */       int countFalse = 0;
/* 127 */       int countTrue = 0;
/* 128 */       int countNull = countNull(comp);
/* 129 */       if ((countNull > 0) && (countNull < comp.size())) {
/* 130 */         for (int j = 0; j < comp.size(); j++) {
/* 131 */           if (comp.get(j) == Boolean.TRUE) { countTrue++;
/* 132 */           } else if (comp.get(j) == Boolean.FALSE) { countFalse++;
/* 133 */           } else if (comp.get(j) != null) throw new RuntimeException("!!");
/*     */         }
/* 135 */         Boolean val = countTrue >= countFalse ? Boolean.TRUE : Boolean.FALSE;
/* 136 */         for (int j = 0; j < comp.size(); j++) {
/* 137 */           if (comp.get(j) == null) comp.set(j, val);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/PhasedIntegerGenotypeData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */