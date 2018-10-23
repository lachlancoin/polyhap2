/*     */ package lc1.dp.data.representation;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import lc1.CGH.Aberation;
/*     */ import lc1.dp.data.collection.DataCollection;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ class PhasedIntegerGenotypeData
/*     */   extends CompoundScorableObject implements Serializable, Comparable, PIGData
/*     */ {
/*     */   public PhasedIntegerGenotypeData(String id, int noSites)
/*     */   {
/*  25 */     super(id, noSites);
/*     */   }
/*     */   
/*  28 */   public PhasedIntegerGenotypeData(PIGData data) { super(data); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean homozygous()
/*     */   {
/*  37 */     for (int i = 0; i < length(); i++) {
/*  38 */       ComparableArray comp = (ComparableArray)getElement(i);
/*  39 */       for (int j = 1; j < comp.size(); j++) {
/*  40 */         if (!comp.get(j).equals(comp.get(0))) {
/*  41 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*  45 */     return true;
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
/*     */   public PhasedIntegerGenotypeData(String id, List<String> st, Class type)
/*     */   {
/*  61 */     super(id, ((String)st.get(0)).length());
/*  62 */     for (int i = 0; i < ((String)st.get(0)).length(); i++)
/*  63 */       if (((String)st.get(0)).charAt(i) != ' ') {
/*  64 */         List<Comparable> point = new ArrayList();
/*  65 */         for (int j = 0; j < st.size(); j++) {
/*  66 */           char ch = ((String)st.get(j)).charAt(i);
/*  67 */           point.add(
/*  68 */             type == Emiss.class ? 
/*  69 */             Emiss.translate(ch) : 
/*  70 */             Integer.valueOf(Integer.parseInt(ch, Constants.radix())));
/*     */         }
/*     */         
/*  73 */         addPoint(i, new ComparableArray(point));
/*     */       }
/*     */   }
/*     */   
/*     */   public PhasedIntegerGenotypeData(CSOData[] unit, boolean merge, String sep) {
/*  78 */     super(unit, merge, sep);
/*     */   }
/*     */   
/*     */ 
/*     */   private static PhasedIntegerGenotypeData[] getList(String name, ComparableArray model, List<String> st)
/*     */   {
/*  84 */     List<PhasedIntegerGenotypeData> dat = new ArrayList();
/*  85 */     for (int i = 0; i < model.size(); i++) {
/*  86 */       Comparable compa = model.get(i);
/*  87 */       int start = 0;
/*  88 */       if ((compa instanceof ComparableArray)) {
/*  89 */         int end = start + ((ComparableArray)compa).size();
/*  90 */         if ((((ComparableArray)compa).get(0) instanceof ComparableArray)) {
/*  91 */           dat.add(new PhasedIntegerGenotypeData(name + i, (ComparableArray)compa, st.subList(start, end), ";"));
/*     */         }
/*     */         else {
/*  94 */           dat.add(new PhasedIntegerGenotypeData("", st.subList(start, end), Emiss.class));
/*     */         }
/*  96 */         start = end;
/*     */       }
/*     */       else {
/*  99 */         throw new RuntimeException("!!");
/*     */       }
/*     */     }
/* 102 */     return (PhasedIntegerGenotypeData[])dat.toArray(new PhasedIntegerGenotypeData[0]);
/*     */   }
/*     */   
/*     */   public PhasedIntegerGenotypeData(String name, ComparableArray model, List<String> st, String sep) {
/* 106 */     this(getList(name, model, st), false, sep);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ScorableObject clone()
/*     */   {
/* 117 */     return new PhasedIntegerGenotypeData(this);
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
/*     */   public void convertHemizygousHomozygous()
/*     */   {
/* 131 */     for (int i = 0; i < length(); i++) {
/* 132 */       ComparableArray comp = (ComparableArray)getElement(i);
/* 133 */       comp.convertHemizygousHomozygous();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 143 */     StringWriter sw = new StringWriter();
/* 144 */     PrintWriter pw = new PrintWriter(sw);
/* 145 */     print(true, pw);
/*     */     
/*     */ 
/* 148 */     return sw.getBuffer().toString();
/*     */   }
/*     */   
/* 151 */   private String conc(List<String> names) { StringBuffer sb = new StringBuffer();
/* 152 */     for (Iterator<String> it = names.iterator(); it.hasNext();) {
/* 153 */       sb.append((String)it.next());
/* 154 */       if (it.hasNext()) sb.append("_");
/*     */     }
/* 156 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public PhasedIntegerGenotypeData[] split(int[] is)
/*     */   {
/* 162 */     List<String> names = Arrays.asList(getName().split("_"));
/* 163 */     PhasedIntegerGenotypeData[] res = new PhasedIntegerGenotypeData[is.length];
/* 164 */     int[] from = new int[is.length];
/* 165 */     int[] to = new int[is.length];
/* 166 */     for (int j = 0; j < res.length; j++) {
/* 167 */       from[j] = (j == 0 ? 0 : to[(j - 1)]);
/* 168 */       from[j] += is[j];
/* 169 */       res[j] = new PhasedIntegerGenotypeData(conc(names.subList(from[j], to[j])), length());
/*     */     }
/* 171 */     for (int i = 0; i < length(); i++) {
/* 172 */       ComparableArray compa = (ComparableArray)getElement(i);
/* 173 */       for (int j = 0; j < is.length; j++) {
/* 174 */         ComparableArray compa_1 = new ComparableArray(compa.elements().subList(from[j], to[j]));
/* 175 */         res[j].addPoint(i, compa_1);
/*     */       }
/*     */     }
/* 178 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */   public PhasedIntegerGenotypeData recombine(Map<Integer, Integer> recSites, int starting_index)
/*     */   {
/* 184 */     if ((noCopies() != 2) && (noCopies() != 1)) throw new RuntimeException("!!");
/* 185 */     PhasedIntegerGenotypeData rec = new PhasedIntegerGenotypeData(getName() + "r", length());
/* 186 */     int index = starting_index;
/* 187 */     for (int i = 0; i < length(); i++) {
/* 188 */       if (recSites.containsKey(Integer.valueOf(i))) {
/* 189 */         index = 1 - index;
/*     */       }
/* 191 */       ComparableArray comp = (ComparableArray)getElement(i);
/*     */       
/* 193 */       rec.addPoint(i, ComparableArray.make(((ComparableArray)getElement(i)).get(index)));
/*     */     }
/* 195 */     return rec;
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<Integer>[] getSwitches()
/*     */   {
/* 201 */     Set[] switches = new TreeSet[noCopies()];
/*     */     
/* 203 */     Comparable[] prev = new Comparable[switches.length];
/* 204 */     for (int j = 0; j < prev.length; j++) {
/* 205 */       prev[j] = ((ComparableArray)getElement(0)).get(j);
/* 206 */       switches[j] = new TreeSet();
/*     */     }
/* 208 */     for (int i = 1; i < length(); i++) {
/* 209 */       for (int j = 0; j < prev.length; j++) {
/* 210 */         Comparable nxt = ((ComparableArray)getElement(i)).get(j);
/* 211 */         if (!prev[j].equals(nxt)) {
/* 212 */           switches[j].add(Integer.valueOf(i));
/*     */         }
/* 214 */         prev[j] = nxt;
/*     */       }
/*     */     }
/* 217 */     return switches;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<Aberation> getDeletedPositions(EmissionState st)
/*     */   {
/* 226 */     int[][] cnts = new int[2][length()];
/*     */     
/*     */ 
/* 229 */     for (int i = 0; i < length(); i++) {
/* 230 */       ComparableArray compa = (ComparableArray)getElement(i);
/* 231 */       for (int k = 0; k < compa.size(); k++) {
/* 232 */         Comparable em = compa.get(k);
/* 233 */         cnts[k][i] = ((em instanceof Emiss) ? ((Emiss)em).noCopies() : ((ComparableArray)em).getGenotypeString().length());
/*     */       }
/*     */     }
/*     */     
/* 237 */     List<Aberation> res = new ArrayList();
/* 238 */     for (int k = 0; k < cnts.length; k++) {
/* 239 */       res.addAll(Aberation.getAberation(getName() + "_" + k, cnts[k], getUncertainty(st)));
/*     */     }
/* 241 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getStringRep(int start, int end)
/*     */   {
/* 247 */     StringBuffer sb = new StringBuffer();
/* 248 */     for (int i = start; i <= end; i++) {
/* 249 */       sb.append(getStringRep(i));
/*     */     }
/* 251 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public PhasedIntegerGenotypeData(PIGData[] dat1, List<int[]> trans)
/*     */   {
/* 256 */     this(dat1[0].getName(), trans.size());
/* 257 */     for (int i = 0; i < trans.size(); i++) {
/* 258 */       int[] t = (int[])trans.get(i);
/* 259 */       addPoint(i, dat1[t[0]].getElement(t[1]));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void removeAll(List<Integer> toDrop)
/*     */   {
/* 266 */     for (int i = toDrop.size() - 1; i >= 0; i--) {
/* 267 */       remove(((Integer)toDrop.get(i)).intValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void reverse()
/*     */   {
/* 275 */     Collections.reverse(this.l1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int compareTo(Object arg0)
/*     */   {
/* 282 */     SimpleScorableObject o1 = (SimpleScorableObject)arg0;
/* 283 */     return getName().compareTo(o1.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void switchAlleles(int i)
/*     */   {
/* 291 */     set(i, switchAlleles(getElement(i)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAsMissing(List<Integer> toDrop, double cn_ratio)
/*     */   {
/* 299 */     int noCopies = noCopies();
/*     */     
/* 301 */     for (int i = 0; i < toDrop.size(); i++) {
/* 302 */       ComparableArray compa = (ComparableArray)getElement(((Integer)toDrop.get(i)).intValue());
/* 303 */       for (int j = 0; j < compa.size(); j++) {
/* 304 */         compa.set(j, Emiss.N);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void applyAlias(int[] alias)
/*     */   {
/* 313 */     DataCollection.reorder(alias, this.l1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int countNull()
/*     */   {
/* 321 */     int cnt = 0;
/* 322 */     for (int i = 0; i < length(); i++) {
/* 323 */       Comparable comp = getElement(i);
/* 324 */       if ((comp instanceof ComparableArray)) {
/* 325 */         if (((ComparableArray)comp).countNull() == ((ComparableArray)comp).size()) {
/* 326 */           cnt++;
/*     */         }
/*     */       }
/* 329 */       else if (comp == Emiss.N()) cnt++;
/*     */     }
/* 331 */     return cnt;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/representation/PhasedIntegerGenotypeData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */