/*     */ package lc1.dp.core;
/*     */ 
/*     */ import lc1.dp.states.State;
/*     */ 
/*     */ public class StatePath implements Comparable
/*     */ {
/*     */   public final java.util.List<StatePosEmission> spe;
/*     */   final String name;
/*     */   public double sc;
/*     */   
/*     */   public class StatePosEmission {
/*     */     public State state;
/*     */     public Integer pos;
/*     */     public Object emission;
/*     */     
/*     */     public StatePosEmission(State st, Object em, int pos2) {
/*  17 */       this.state = st;
/*  18 */       this.emission = em;
/*  19 */       this.pos = Integer.valueOf(pos2);
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/*  25 */       String str = this.state.getName();
/*  26 */       String[] split = str.split("_");
/*  27 */       if (split.length == 3) return split[1] + split[2];
/*  28 */       return str;
/*     */     }
/*     */   }
/*     */   
/*     */   public java.util.List<State> getStates() {
/*  33 */     java.util.List<State> st = new java.util.ArrayList(size());
/*  34 */     for (int i = 0; i < size(); i++) {
/*  35 */       st.add(getState(i));
/*     */     }
/*  37 */     return st;
/*     */   }
/*     */   
/*  40 */   public StatePosEmission[] getSubList(java.util.Set<State> include) { java.util.List<StatePosEmission> subl = new java.util.ArrayList();
/*  41 */     for (int i = 0; i < len(); i++) {
/*  42 */       StatePosEmission spe_i = getSPE(i);
/*  43 */       if (include.contains(spe_i.state)) subl.add(spe_i);
/*     */     }
/*  45 */     return (StatePosEmission[])subl.toArray(new StatePosEmission[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StatePath(StatePath sp)
/*     */   {
/*  53 */     this.name = sp.name;
/*  54 */     this.sc = sp.sc;
/*  55 */     this.spe = new java.util.ArrayList();
/*     */   }
/*     */   
/*  58 */   public StatePath(String name, double sc) { this.name = name;
/*  59 */     this.sc = sc;
/*  60 */     this.spe = new java.util.ArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(State st, Object em, int pos)
/*     */   {
/*  69 */     this.spe.add(new StatePosEmission(st, em, pos));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int len()
/*     */   {
/*  76 */     return this.spe.size();
/*     */   }
/*     */   
/*  79 */   public StatePosEmission getSPE(int i) { return (StatePosEmission)this.spe.get(len() - i - 1); }
/*     */   
/*     */   public Object getEmission(int i) {
/*  82 */     return getSPE(i).emission;
/*     */   }
/*     */   
/*  85 */   public Integer getIndex(int i) { return getSPE(i).pos; }
/*     */   
/*     */   public State getState(int i) {
/*  88 */     return getSPE(i).state;
/*     */   }
/*     */   
/*  91 */   public void setState(int k, State st) { getSPE(k).state = st; }
/*     */   
/*     */ 
/*     */   public java.util.List<State> getSublist(java.util.Set<State> states)
/*     */   {
/*  96 */     java.util.List<State> list = new java.util.ArrayList();
/*  97 */     for (int i = 0; i < len(); i++) {
/*  98 */       State st = getState(i);
/*  99 */       if (states.contains(st)) {
/* 100 */         list.add(st);
/*     */       }
/*     */     }
/* 103 */     return list;
/*     */   }
/*     */   
/*     */   public int[] find(lc1.dp.states.EmissionState match1, lc1.dp.states.EmissionState match2) {
/* 107 */     int[] res = { -1, -1 };
/* 108 */     State match = match2;
/* 109 */     for (int i = 0; i < this.spe.size(); i++) {
/* 110 */       if (((StatePosEmission)this.spe.get(i)).state == match) {
/* 111 */         if (match == match2) {
/* 112 */           res[1] = i;
/* 113 */           match = match1;
/*     */         }
/*     */         else {
/* 116 */           res[0] = i;
/* 117 */           return res;
/*     */         }
/*     */       }
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */   
/*     */ 
/* 125 */   public int getEnd() { return getIndex(len() - 1).intValue(); }
/*     */   
/*     */   public Object getFirstEmission() {
/* 128 */     if (size() == 0) return null;
/* 129 */     return getEmission(0);
/*     */   }
/*     */   
/* 132 */   public Integer getFirstEmissionPos() { if (this.spe.size() == 0) return null;
/* 133 */     return getIndex(0);
/*     */   }
/*     */   
/*     */   public State getFirstState() {
/* 137 */     if (this.spe.size() == 0) return null;
/* 138 */     return getState(0);
/*     */   }
/*     */   
/*     */   public Object getLastState() {
/* 142 */     if (this.spe.size() == 0) return null;
/* 143 */     return getState(len() - 1);
/*     */   }
/*     */   
/* 146 */   public String getName() { return this.name; }
/*     */   
/*     */   public int getStart()
/*     */   {
/* 150 */     return getIndex(0).intValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] getWord()
/*     */   {
/* 156 */     String[] sb = new String[this.spe.size()];
/* 157 */     for (int i = 0; i < this.spe.size(); i++) {
/* 158 */       int i1 = this.spe.size() - i - 1;
/* 159 */       sb[i1] = ((StatePosEmission)this.spe.get(i)).state.getName();
/*     */     }
/* 161 */     return sb;
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
/* 173 */     double no_correct = 0.0D;
/* 174 */     double no_wrong = 0.0D;
/* 175 */     int k = 0;
/* 176 */     for (int i = this.spe.size() - 1; i >= 0; i--) {
/* 177 */       State st0 = ((StatePosEmission)this.spe.get(i)).state;
/* 178 */       State st1 = st[k];
/* 179 */       if ((st0 instanceof lc1.dp.states.EmissionState)) {
/* 180 */         if (st0.equals(st1)) no_correct += 1.0D; else
/* 181 */           no_wrong += 1.0D;
/* 182 */         k++;
/*     */       }
/*     */     }
/* 185 */     return no_correct / (no_wrong + no_correct);
/*     */   }
/*     */   
/*     */ 
/* 189 */   public int size() { return this.spe.size(); }
/*     */   
/*     */   public String toString() {
/* 192 */     StringBuffer sb1 = new StringBuffer();
/*     */     
/*     */ 
/* 195 */     for (int i = 0; i < len(); i++) {
/* 196 */       sb1.append(this.spe.get(len() - i - 1));
/*     */     }
/*     */     
/*     */ 
/* 200 */     return 
/* 201 */       sb1.toString() + "\n";
/*     */   }
/*     */   
/*     */   public int count(State state) {
/* 205 */     int count = 0;
/* 206 */     for (int i = 0; i < size(); i++) {
/* 207 */       if (getState(i).equals(state)) count++;
/*     */     }
/* 209 */     return count;
/*     */   }
/*     */   
/* 212 */   public int compareTo(Object o) { StatePath sp1 = (StatePath)o;
/* 213 */     if (this.sc == sp1.sc) return 0;
/* 214 */     if (this.sc < sp1.sc) return 1;
/* 215 */     return -1;
/*     */   }
/*     */   
/*     */   public int getIdenticalStretch(int start)
/*     */   {
/* 220 */     State obj = getState(start);
/* 221 */     for (int i1 = start + 1; i1 < size(); i1++) {
/* 222 */       if (!getState(i1).equals(obj)) return i1;
/*     */     }
/* 224 */     return size();
/*     */   }
/*     */   
/* 227 */   public int getRecombineIndex() { int max_start = 0;
/* 228 */     int max_end = 0;
/* 229 */     for (int start = 0; start < size() - 1; start++) {
/* 230 */       int end = getIdenticalStretch(start);
/* 231 */       if (end - start > max_end - max_start) {
/* 232 */         max_end = end;
/* 233 */         max_start = start;
/*     */       }
/*     */     }
/* 236 */     int max_index = max_start + (int)Math.floor((max_end - max_start) / 2.0D);
/* 237 */     return max_index;
/*     */   }
/*     */   
/*     */   public void merge(StatePath spi, lc1.dp.states.DotState begin, lc1.dp.states.DotState end) {
/* 241 */     throw new RuntimeException("need to reimplement this");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/core/StatePath.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */