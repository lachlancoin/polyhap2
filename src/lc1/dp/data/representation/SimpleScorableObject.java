/*     */ package lc1.dp.data.representation;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.states.PhasedDataState;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public abstract class SimpleScorableObject implements ScorableObject, SSOData
/*     */ {
/*     */   List<Comparable> l1;
/*     */   String id;
/*     */   final Class clazz;
/*     */   
/*     */   public static PhasedDataState make(String string, List<String> asList, EmissionStateSpace emStSp)
/*     */   {
/*  19 */     return new PhasedDataState(string, asList, emStSp, (short)-1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static PhasedDataState make(String string, int noSnps, EmissionStateSpace emStSp)
/*     */   {
/*  26 */     return new PhasedDataState(string, noSnps, emStSp, (short)-1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  35 */     return this.id;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/*  41 */     this.id = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ScorableObject clone();
/*     */   
/*     */ 
/*     */ 
/*     */   public final void remove(int i)
/*     */   {
/*  53 */     this.l1.remove(i);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getStringRep(int i)
/*     */   {
/*  59 */     return getElement(i).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void restrictTo(Integer integer, Integer integer2)
/*     */   {
/*  66 */     this.l1 = this.l1.subList(integer.intValue(), integer2.intValue() + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void restrictSites(int i)
/*     */   {
/*  74 */     if (i < this.l1.size()) {
/*  75 */       this.l1 = this.l1.subList(0, i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void restrictSites(int min, int max)
/*     */   {
/*  83 */     this.l1 = this.l1.subList(min, max);
/*     */   }
/*     */   
/*     */   public SimpleScorableObject(String id, int noSites, Class clazz)
/*     */   {
/*  88 */     this.l1 = new java.util.ArrayList(noSites);
/*  89 */     this.id = id;
/*  90 */     this.clazz = clazz;
/*     */   }
/*     */   
/*  93 */   public Class clazz() { return this.clazz; }
/*     */   
/*     */   public static void printIdLine(String idLine, PrintWriter pw, int len)
/*     */   {
/*  97 */     char[] ch = new char[Math.max(idLine.length(), len)];
/*  98 */     java.util.Arrays.fill(ch, ' ');
/*  99 */     System.arraycopy(idLine.toCharArray(), 0, ch, 0, idLine.length());
/* 100 */     int jmp = 10;
/* 101 */     int jmp2 = 100;
/* 102 */     for (int i = 0; i < ch.length; i += jmp) {
/* 103 */       if ((Math.IEEEremainder(i, jmp2) == 0.0D) && (idLine.length() + i < ch.length)) {
/* 104 */         System.arraycopy(idLine.toCharArray(), 0, ch, i, idLine.length());
/*     */       }
/* 106 */       if (i >= idLine.length()) {
/* 107 */         ch[i] = '|';
/*     */       }
/*     */     }
/* 110 */     pw.println(new String(ch));
/*     */   }
/*     */   
/* 113 */   public SimpleScorableObject(SSOData data) { this(data.getName(), data.length(), data.clazz());
/* 114 */     for (int i = 0; i < data.length(); i++) {
/* 115 */       this.l1.add(copyElement(data.getElement(i)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract Comparable copyElement(Comparable paramComparable);
/*     */   
/*     */ 
/*     */ 
/*     */   public int addMissingData(double perc)
/*     */   {
/* 127 */     int cnt = 0;
/* 128 */     for (int i = 0; i < length(); i++) {
/* 129 */       if (Constants.rand.nextDouble() < perc) {
/* 130 */         set(i, null);
/* 131 */         cnt++;
/*     */       }
/* 133 */       else if (Constants.rand.nextDouble() < perc) {
/* 134 */         throw new RuntimeException();
/*     */       }
/*     */     }
/* 137 */     return cnt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean allNull()
/*     */   {
/* 146 */     for (int i = 0; i < this.l1.size(); i++) {
/* 147 */       if (this.l1.get(i) != null) return false;
/*     */     }
/* 149 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int length()
/*     */   {
/* 159 */     return this.l1.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Comparable getElement(int i)
/*     */   {
/* 166 */     return (Comparable)this.l1.get(i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void printElement(PrintWriter pw, Object el, boolean expand)
/*     */   {
/* 174 */     if (el == null) { pw.print(" ");
/*     */     }
/*     */     else
/*     */     {
/* 178 */       ((Integer)el);pw.print((el instanceof Integer) ? Integer.toString(((Integer)el).intValue(), Constants.radix()) : (el instanceof Emiss) ? ((Emiss)el).toStringPrint() : expand ? ((Emiss)el).toStringShort() : el.toString());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void print(boolean idline, PrintWriter pw)
/*     */   {
/* 186 */     if (idline) pw.println("# id " + getName());
/* 187 */     for (int i = 0; i < length(); i++) {
/* 188 */       printElement(pw, this.l1.get(i), false);
/*     */     }
/* 190 */     pw.println();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addPoint(int i, Comparable i1)
/*     */   {
/* 197 */     this.l1.add(i1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void set(int i, Comparable obj)
/*     */   {
/* 205 */     this.l1.set(i, obj);
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
/*     */   public String toString()
/*     */   {
/* 220 */     if ((getElement(0) instanceof Emiss)) {
/* 221 */       StringBuffer sb = new StringBuffer();
/* 222 */       for (int i = 0; i < length(); i++) {
/* 223 */         sb.append(getStringRep(i));
/*     */       }
/* 225 */       return sb.toString();
/*     */     }
/* 227 */     StringWriter sw = new StringWriter();
/* 228 */     PrintWriter pw = new PrintWriter(sw);
/* 229 */     print(pw, true, false, null);
/* 230 */     return sw.getBuffer().toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static PIGData make(CSOData[] unit, boolean b, String join)
/*     */   {
/* 236 */     return new PhasedIntegerGenotypeData(unit, b, join);
/*     */   }
/*     */   
/*     */   public static PhasedDataState make(PIGData next)
/*     */   {
/* 241 */     return new PhasedDataState((PhasedDataState)next);
/*     */   }
/*     */   
/*     */ 
/*     */   public static PhasedDataState make(PIGData[] statesD, List<int[]> list)
/*     */   {
/* 247 */     return new PhasedDataState(statesD, list);
/*     */   }
/*     */   
/*     */   public static Comparable switchAlleles(Comparable comp)
/*     */   {
/* 252 */     if ((comp instanceof Emiss)) {
/* 253 */       return Emiss.switchElement((Emiss)comp);
/*     */     }
/*     */     
/* 256 */     ComparableArray comp1 = ((ComparableArray)comp).copy();
/* 257 */     for (int j = 0; j < comp1.size(); j++) {
/* 258 */       comp1.set(j, Emiss.switchElement((Emiss)comp1.get(j)));
/*     */     }
/* 260 */     return comp1;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/representation/SimpleScorableObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */