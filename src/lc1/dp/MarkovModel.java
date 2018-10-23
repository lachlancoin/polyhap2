/*     */ package lc1.dp;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import lc1.dp.genotype.StateDistribution;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MarkovModel
/*     */   implements Serializable
/*     */ {
/*     */   public DotState MAGIC;
/*     */   String name;
/*  23 */   protected List<State> states = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  29 */   public State getState(int j) { return (State)this.states.get(j); }
/*     */   
/*     */   public abstract Object clone();
/*     */   
/*  33 */   public MarkovModel(String name, int noSnps) { this(name, SimpleDistribution.noOffset, SimpleDistribution.noOffset, noSnps); }
/*     */   
/*     */ 
/*  36 */   public MarkovModel(String name, SimpleDistribution start, SimpleDistribution end, int noSnps) { this(name, new DotState("!", start, end), noSnps); }
/*     */   
/*  38 */   int length = 0;
/*     */   
/*     */   public abstract boolean transitionsChanged();
/*     */   
/*     */   public abstract void transitionsFixed();
/*     */   
/*  44 */   public Double[][] getHittingProb(int length) { if ((this.hittingProb == null) || (transitionsChanged()) || (length != this.length)) {
/*  45 */       this.length = length;
/*  46 */       this.hittingProb = new Double[modelLength()][];
/*  47 */       for (int j = 1; j < modelLength(); j++) {
/*  48 */         this.hittingProb[j] = new Double[length];
/*  49 */         this.hittingProb[j][0] = Double.valueOf(getTransitionScore(0, j, 0));
/*     */       }
/*  51 */       for (int i = 1; i < length; i++) {
/*  52 */         for (int j = 1; j < modelLength(); j++) {
/*  53 */           double sum = 0.0D;
/*  54 */           for (int k = 1; k < modelLength(); k++) {
/*  55 */             sum += this.hittingProb[k][(i - 1)].doubleValue() * getTransitionScore(k, j, i);
/*     */           }
/*  57 */           this.hittingProb[j][i] = Double.valueOf(sum);
/*     */         }
/*     */       }
/*  60 */       transitionsFixed();
/*     */     }
/*  62 */     return this.hittingProb;
/*     */   }
/*     */   
/*     */   public abstract void setPseudoCountWeights(double paramDouble);
/*     */   
/*     */   public MarkovModel(MarkovModel m, MarkovModel pseudo) {
/*  68 */     this(m.getName(), m.MAGIC, m.noSnps);
/*     */     try
/*     */     {
/*  71 */       Iterator<State> it_ps = pseudo == null ? null : pseudo.states();
/*  72 */       for (Iterator<State> it = m.states(); it.hasNext();) {
/*  73 */         State st = (State)it.next();
/*  74 */         State st_ps = it_ps == null ? null : (State)it_ps.next();
/*     */         
/*  76 */         if (st != m.MAGIC) {
/*  77 */           State clone = (State)st.clone(st_ps);
/*  78 */           clone.index = this.states.size();
/*  79 */           addState(clone);
/*     */         }
/*     */       }
/*  82 */     } catch (Exception exc) { exc.printStackTrace();
/*  83 */       System.exit(0);
/*     */     }
/*     */   }
/*     */   
/*     */   public MarkovModel(String name, DotState magic, int noSnps) {
/*  88 */     this.name = name;
/*  89 */     this.MAGIC = magic;
/*     */     
/*  91 */     this.states.add(magic);
/*  92 */     magic.index = 0;
/*  93 */     this.noSnps = noSnps;
/*     */   }
/*     */   
/*     */   protected Double[][] hittingProb;
/*  97 */   List<int[]> equivalenceClasses = new ArrayList();
/*     */   
/*     */   public abstract void addCounts(StateDistribution[] paramArrayOfStateDistribution, int paramInt);
/*     */   
/* 101 */   public State addState(State st) { if (this.states.contains(st)) { return st;
/*     */     }
/* 103 */     st.index = this.states.size();
/* 104 */     if ((st instanceof EmissionState)) {
/* 105 */       this.equivalenceClasses.add(new int[] { st.index });
/*     */     }
/* 107 */     this.states.add(st);
/* 108 */     return st;
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
/*     */   public State[] emitStatePath(State startState, Class clazz)
/*     */   {
/* 124 */     List<State> l = new ArrayList();
/* 125 */     if (clazz.isInstance(startState)) l.add(startState);
/* 126 */     State state = startState;
/* 127 */     int cumLength = -1;
/* 128 */     while ((cumLength < 0) || (state != this.MAGIC)) {
/*     */       try {
/* 130 */         if ((state instanceof EmissionState)) cumLength++;
/* 131 */         if (clazz.isInstance(state)) l.add(state);
/* 132 */         state = sampleState(state, cumLength);
/*     */       } catch (Exception exc) {
/* 134 */         Exception exc1 = new Exception("problem with sampling from state " + state + " at " + state + " " + cumLength);
/* 135 */         exc1.initCause(exc);
/* 136 */         exc1.printStackTrace();
/* 137 */         System.exit(0);
/*     */       }
/*     */     }
/* 140 */     return (State[])l.toArray(new State[0]);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 144 */     return this.name;
/*     */   }
/*     */   
/*     */   public abstract double getTransitionScore(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   public void initialiseCounts() {
/* 150 */     initialiseTransitionCounts();
/* 151 */     for (Iterator<State> states = states(); states.hasNext();) {
/* 152 */       State st = (State)states.next();
/* 153 */       if ((st instanceof EmissionState)) {
/* 154 */         ((EmissionState)st).initialiseCounts();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 160 */   public static double ps = 0.0D;
/*     */   
/*     */   public abstract void initialiseTransitionCounts();
/*     */   
/* 164 */   public int maxAdv() { int adv = 0;
/* 165 */     for (Iterator<State> it = this.states.iterator(); it.hasNext();) {
/* 166 */       State state = (State)it.next();
/* 167 */       if (state.adv > adv) adv = state.adv;
/*     */     }
/* 169 */     return adv;
/*     */   }
/*     */   
/*     */ 
/*     */   public int modelLength()
/*     */   {
/* 175 */     return this.states.size();
/*     */   }
/*     */   
/*     */   public State sampleState(State state, int i) {
/* 179 */     Iterator<State> it = statesOut(state, i).iterator();
/* 180 */     double pr = Math.random();
/* 181 */     double sum = 0.0D;
/* 182 */     while (it.hasNext()) {
/* 183 */       State entry = (State)it.next();
/* 184 */       sum += getTransitionScore(state.index, entry.index, i + entry.adv);
/* 185 */       if (sum >= pr) {
/* 186 */         return entry;
/*     */       }
/*     */     }
/* 189 */     throw new RuntimeException("did not find sample " + sum + " " + pr);
/*     */   }
/*     */   
/*     */   public static double sum(Map<Object, Double> m) {
/* 193 */     double sum = 0.0D;
/* 194 */     for (Iterator<Double> d = m.values().iterator(); d.hasNext();) {
/* 195 */       sum += ((Double)d.next()).doubleValue();
/*     */     }
/* 197 */     return sum;
/*     */   }
/*     */   
/* 200 */   protected static Map getMap(Iterator<Map.Entry<Object, Double>> it) { Map m = new HashMap();
/* 201 */     while (it.hasNext()) {
/* 202 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/* 203 */       m.put(entry.getKey(), entry.getValue());
/*     */     }
/* 205 */     return m;
/*     */   }
/*     */   
/*     */   public void setRandom(double trans, double emiss, boolean restart, boolean lastOnly) {
/* 209 */     if (emiss < Double.POSITIVE_INFINITY) {
/* 210 */       for (Iterator<State> states = states(); states.hasNext();)
/*     */       {
/* 212 */         State st = (State)states.next();
/* 213 */         if (((st instanceof EmissionState)) && ((!lastOnly) || (!states.hasNext()))) {
/* 214 */           ((EmissionState)st).setRandom(emiss, restart);
/*     */         }
/*     */       }
/*     */     }
/* 218 */     if (trans < Double.POSITIVE_INFINITY) { setRandomTransitions(trans, restart, lastOnly);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract void setRandomTransitions(double paramDouble, boolean paramBoolean1, boolean paramBoolean2);
/*     */   
/*     */   public Iterator<State> states()
/*     */   {
/* 227 */     return this.states.iterator();
/*     */   }
/*     */   
/* 230 */   public List<State> statesIn(State to, int positionOfToEmission) { return this.states; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int noSnps;
/*     */   
/*     */ 
/*     */ 
/*     */   protected List<Comparable> emissionStateSpace;
/*     */   
/*     */ 
/*     */ 
/*     */   protected double[][] emissionStateSpaceDist;
/*     */   
/*     */ 
/*     */   protected Map<Comparable, Integer> stateSpaceToIndex;
/*     */   
/*     */ 
/*     */   public List<State> statesOut(State from, int beforeToEmission)
/*     */   {
/* 251 */     return this.states;
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
/*     */   public void print(PrintWriter pw)
/*     */   {
/* 272 */     for (Iterator<State> it = states(); it.hasNext();) {
/* 273 */       State st = (State)it.next();
/* 274 */       if ((st instanceof EmissionState)) {
/* 275 */         ((EmissionState)st).print(pw, "State_" + st.getName() + " ");
/* 276 */         pw.print("\n");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 281 */   public String toString() { StringWriter sb = new StringWriter();
/* 282 */     PrintWriter pw = new PrintWriter(sb);
/* 283 */     print(pw);
/* 284 */     return sb.getBuffer().toString();
/*     */   }
/*     */   
/*     */   public abstract double totalTransitionDistance(MarkovModel paramMarkovModel);
/*     */   
/* 289 */   public final double totalTransitionEmissionDist(MarkovModel m1) { double sum = 0.0D;
/* 290 */     if (m1.modelLength() != modelLength()) throw new RuntimeException("cannot compare");
/* 291 */     for (int i = 0; i < this.states.size(); i++) {
/* 292 */       State st1 = (State)this.states.get(i);
/* 293 */       State st2 = (State)m1.states.get(i);
/* 294 */       if ((st1 instanceof EmissionState)) {
/* 295 */         sum += ((EmissionState)st1).KLDistance((EmissionState)st2);
/*     */       }
/*     */     }
/*     */     
/* 299 */     sum += totalTransitionDistance(m1);
/* 300 */     return sum;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract void transferCountsToProbs();
/*     */   
/*     */   public Iterator<int[]> equivalenceClasses()
/*     */   {
/* 308 */     return this.equivalenceClasses.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */   public void validate(int length)
/*     */     throws Exception
/*     */   {
/* 315 */     for (int j = 0; j < this.states.size(); j++) {
/* 316 */       State st = (State)this.states.get(j);
/* 317 */       if ((st instanceof EmissionState)) {
/* 318 */         ((EmissionState)st).validate();
/*     */       }
/*     */     }
/* 321 */     validateTrans(length);
/*     */   }
/*     */   
/*     */   private void validateTrans(int length)
/*     */   {
/* 326 */     int start = 0;int end = 1;
/* 327 */     for (int i = -1; i < length; i++) {
/* 328 */       if (i >= 0) {
/* 329 */         start = 1;
/* 330 */         end = modelLength();
/*     */       }
/* 332 */       for (int k = start; k < end; k++) {
/* 333 */         double sum = 0.0D;
/* 334 */         Double[] d = new Double[modelLength()];
/* 335 */         for (int j = 0; j < modelLength(); j++) {
/* 336 */           int adv = ((State)this.states.get(j)).adv;
/* 337 */           d[j] = Double.valueOf(getTransitionScore(k, j, adv + i));
/* 338 */           sum += d[j].doubleValue();
/*     */         }
/* 340 */         if (Math.abs(sum - 1.0D) > 0.001D) throw new RuntimeException("!! " + sum + " " + i + " " + getState(k).getName() + "\n" + Arrays.asList(d) + "\n");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 345 */   public void setStatesChanged(boolean b) { for (int j = 0; j < this.states.size(); j++) {
/* 346 */       State st = (State)this.states.get(j);
/* 347 */       if ((st instanceof EmissionState)) {
/* 348 */         ((EmissionState)st).setChangedParams(b);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract int[] statesIn(int paramInt1, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract int[] statesOut(int paramInt1, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract List<Comparable> initStateSpace();
/*     */   
/*     */ 
/*     */   protected void initialiseStateSpace()
/*     */   {
/* 368 */     this.emissionStateSpace = initStateSpace();
/* 369 */     if (this.emissionStateSpace.size() == 0) return;
/* 370 */     this.stateSpaceToIndex = new HashMap();
/* 371 */     for (int i = 0; i < this.emissionStateSpace.size(); i++) {
/* 372 */       this.stateSpaceToIndex.put((Comparable)this.emissionStateSpace.get(i), Integer.valueOf(i));
/* 373 */       double[] res = new double[this.emissionStateSpace.size()];
/* 374 */       Arrays.fill(res, 0.0D);
/* 375 */       res[i] = 1.0D;
/*     */     }
/* 377 */     updateEmissionStateSpaceDist();
/*     */   }
/*     */   
/*     */   public abstract void updateEmissionStateSpaceDist();
/*     */   
/*     */   public List<Comparable> getEmissionStateSpace() {
/* 383 */     if (this.emissionStateSpace == null) throw new RuntimeException("must initialise state space");
/* 384 */     return this.emissionStateSpace;
/*     */   }
/*     */   
/*     */   public int getEmissionStateSpaceIndex(Object element)
/*     */   {
/*     */     try {
/* 390 */       return ((Integer)this.stateSpaceToIndex.get(element)).intValue();
/*     */     } catch (Exception exc) {
/* 392 */       exc.printStackTrace(); }
/* 393 */     return -1;
/*     */   }
/*     */   
/*     */   public double[] getEmissionStateSpaceDistribution(int index) {
/* 397 */     return this.emissionStateSpaceDist[index];
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/MarkovModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */