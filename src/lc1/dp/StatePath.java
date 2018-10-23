/*     */ package lc1.dp;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ public class StatePath implements Comparable { public final List<StatePosEmission> spe;
/*     */   final String name;
/*     */   public double sc;
/*     */   
/*     */   public class StatePosEmission { public State state;
/*     */     public Integer pos;
/*     */     public Object emission;
/*     */     
/*  13 */     public StatePosEmission(State st, Object em, int pos2) { this.state = st;
/*  14 */       this.emission = em;
/*  15 */       this.pos = Integer.valueOf(pos2);
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/*  21 */       String str = this.state.getName();
/*  22 */       String[] split = str.split("_");
/*  23 */       if (split.length == 3) return split[1] + split[2];
/*  24 */       return str;
/*     */     }
/*     */   }
/*     */   
/*     */   public StatePosEmission[] getSubList(java.util.Set<State> include)
/*     */   {
/*  30 */     List<StatePosEmission> subl = new java.util.ArrayList();
/*  31 */     for (int i = 0; i < len(); i++) {
/*  32 */       StatePosEmission spe_i = getSPE(i);
/*  33 */       if (include.contains(spe_i.state)) subl.add(spe_i);
/*     */     }
/*  35 */     return (StatePosEmission[])subl.toArray(new StatePosEmission[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StatePath(StatePath sp)
/*     */   {
/*  43 */     this.name = sp.name;
/*  44 */     this.sc = sp.sc;
/*  45 */     this.spe = new java.util.ArrayList();
/*     */   }
/*     */   
/*  48 */   public StatePath(String name, double sc) { this.name = name;
/*  49 */     this.sc = sc;
/*  50 */     this.spe = new java.util.ArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(State st, Object em, int pos)
/*     */   {
/*  59 */     this.spe.add(new StatePosEmission(st, em, pos));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int len()
/*     */   {
/*  66 */     return this.spe.size();
/*     */   }
/*     */   
/*  69 */   public StatePosEmission getSPE(int i) { return (StatePosEmission)this.spe.get(len() - i - 1); }
/*     */   
/*     */   public Object getEmission(int i) {
/*  72 */     return getSPE(i).emission;
/*     */   }
/*     */   
/*  75 */   public Integer getIndex(int i) { return getSPE(i).pos; }
/*     */   
/*     */   public State getState(int i) {
/*  78 */     return getSPE(i).state;
/*     */   }
/*     */   
/*  81 */   public void setState(int k, State st) { getSPE(k).state = st; }
/*     */   
/*     */ 
/*     */   public List<State> getSublist(java.util.Set<State> states)
/*     */   {
/*  86 */     List<State> list = new java.util.ArrayList();
/*  87 */     for (int i = 0; i < len(); i++) {
/*  88 */       State st = getState(i);
/*  89 */       if (states.contains(st)) {
/*  90 */         list.add(st);
/*     */       }
/*     */     }
/*  93 */     return list;
/*     */   }
/*     */   
/*     */   public int[] find(EmissionState match1, EmissionState match2) {
/*  97 */     int[] res = { -1, -1 };
/*  98 */     State match = match2;
/*  99 */     for (int i = 0; i < this.spe.size(); i++) {
/* 100 */       if (((StatePosEmission)this.spe.get(i)).state == match) {
/* 101 */         if (match == match2) {
/* 102 */           res[1] = i;
/* 103 */           match = match1;
/*     */         }
/*     */         else {
/* 106 */           res[0] = i;
/* 107 */           return res;
/*     */         }
/*     */       }
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */   
/*     */ 
/* 115 */   public int getEnd() { return getIndex(len() - 1).intValue(); }
/*     */   
/*     */   public Object getFirstEmission() {
/* 118 */     if (size() == 0) return null;
/* 119 */     return getEmission(0);
/*     */   }
/*     */   
/* 122 */   public Integer getFirstEmissionPos() { if (this.spe.size() == 0) return null;
/* 123 */     return getIndex(0);
/*     */   }
/*     */   
/*     */   public State getFirstState() {
/* 127 */     if (this.spe.size() == 0) return null;
/* 128 */     return getState(0);
/*     */   }
/*     */   
/*     */   public Object getLastState() {
/* 132 */     if (this.spe.size() == 0) return null;
/* 133 */     return getState(len() - 1);
/*     */   }
/*     */   
/* 136 */   public String getName() { return this.name; }
/*     */   
/*     */   public int getStart()
/*     */   {
/* 140 */     return getIndex(0).intValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] getWord()
/*     */   {
/* 146 */     String[] sb = new String[this.spe.size()];
/* 147 */     for (int i = 0; i < this.spe.size(); i++) {
/* 148 */       int i1 = this.spe.size() - i - 1;
/* 149 */       sb[i1] = ((StatePosEmission)this.spe.get(i)).state.getName();
/*     */     }
/* 151 */     return sb;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double percCorrect(State[] st)
/*     */   {
/* 163 */     double no_correct = 0.0D;
/* 164 */     double no_wrong = 0.0D;
/* 165 */     int k = 0;
/* 166 */     for (int i = this.spe.size() - 1; i >= 0; i--) {
/* 167 */       State st0 = ((StatePosEmission)this.spe.get(i)).state;
/* 168 */       State st1 = st[k];
/* 169 */       if ((st0 instanceof EmissionState)) {
/* 170 */         if (st0.equals(st1)) no_correct += 1.0D; else
/* 171 */           no_wrong += 1.0D;
/* 172 */         k++;
/*     */       }
/*     */     }
/* 175 */     return no_correct / (no_wrong + no_correct);
/*     */   }
/*     */   
/*     */ 
/* 179 */   public int size() { return this.spe.size(); }
/*     */   
/*     */   public String toString() {
/* 182 */     StringBuffer sb1 = new StringBuffer();
/*     */     
/*     */ 
/* 185 */     for (int i = 0; i < len(); i++) {
/* 186 */       sb1.append(this.spe.get(len() - i - 1));
/*     */     }
/*     */     
/*     */ 
/* 190 */     return 
/* 191 */       sb1.toString() + "\n";
/*     */   }
/*     */   
/*     */   public int count(State state) {
/* 195 */     int count = 0;
/* 196 */     for (int i = 0; i < size(); i++) {
/* 197 */       if (getState(i).equals(state)) count++;
/*     */     }
/* 199 */     return count;
/*     */   }
/*     */   
/* 202 */   public int compareTo(Object o) { StatePath sp1 = (StatePath)o;
/* 203 */     if (this.sc == sp1.sc) return 0;
/* 204 */     if (this.sc < sp1.sc) return 1;
/* 205 */     return -1;
/*     */   }
/*     */   
/*     */   public int getIdenticalStretch(int start)
/*     */   {
/* 210 */     State obj = getState(start);
/* 211 */     for (int i1 = start + 1; i1 < size(); i1++) {
/* 212 */       if (!getState(i1).equals(obj)) return i1;
/*     */     }
/* 214 */     return size();
/*     */   }
/*     */   
/* 217 */   public int getRecombineIndex() { int max_start = 0;
/* 218 */     int max_end = 0;
/* 219 */     for (int start = 0; start < size() - 1; start++) {
/* 220 */       int end = getIdenticalStretch(start);
/* 221 */       if (end - start > max_end - max_start) {
/* 222 */         max_end = end;
/* 223 */         max_start = start;
/*     */       }
/*     */     }
/* 226 */     int max_index = max_start + (int)Math.floor((max_end - max_start) / 2.0D);
/* 227 */     return max_index;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/StatePath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */