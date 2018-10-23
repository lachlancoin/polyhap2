/*     */ package lc1.dp.model;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.states.DotState;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.stats.SimpleDistribution;
/*     */ import lc1.stats.StateDistribution;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MarkovModel
/*     */   implements Serializable
/*     */ {
/*  25 */   public static final DotState MAGIC = new DotState(new DotState("!", SimpleDistribution.noOffset, SimpleDistribution.noOffset));
/*     */   protected String name;
/*  27 */   protected List<State> states = new ArrayList();
/*     */   
/*     */   public abstract boolean converged();
/*     */   
/*     */   public State getState(int j)
/*     */   {
/*  33 */     return (State)this.states.get(j);
/*     */   }
/*     */   
/*     */ 
/*  37 */   int length = 0;
/*     */   
/*     */   public abstract Object clone(boolean paramBoolean);
/*     */   
/*  41 */   public final double[][] getHittingProb(int length) { double[][] hittingProb = (double[][])null;
/*  42 */     if ((hittingProb == null) || (length != this.length)) {
/*  43 */       this.length = length;
/*  44 */       hittingProb = new double[length][modelLength()];
/*  45 */       hittingProb[0][0] = 0.0D;
/*  46 */       for (int j = 1; j < modelLength(); j++)
/*     */       {
/*  48 */         hittingProb[0][j] = getTransitionScore(0, j, 0);
/*     */       }
/*  50 */       for (int i = 1; i < length; i++) {
/*  51 */         hittingProb[i][0] = 0.0D;
/*  52 */         for (int j = 1; j < modelLength(); j++) {
/*  53 */           double sum = 0.0D;
/*  54 */           double[] trans = new double[modelLength()];
/*  55 */           double[] hp = new double[modelLength()];
/*  56 */           for (int k = 1; k < modelLength(); k++) {
/*  57 */             trans[k] = getTransitionScore(k, j, i);
/*  58 */             hp[k] = hittingProb[(i - 1)][k];
/*  59 */             sum += hp[k] * trans[k];
/*     */           }
/*  61 */           hittingProb[i][j] = sum;
/*     */         }
/*     */       }
/*     */     }
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
/*  76 */     return hittingProb;
/*     */   }
/*     */   
/*     */   public abstract void setPseudoCountWeights(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2);
/*     */   
/*     */   public MarkovModel(MarkovModel m) {
/*  82 */     this(m.getName(), m.noSnps.intValue(), m.emissionStateSpace);
/*     */     
/*     */     try
/*     */     {
/*  86 */       for (Iterator<State> it = m.states(); it.hasNext();) {
/*  87 */         State st = (State)it.next();
/*     */         
/*     */ 
/*  90 */         if (st != MAGIC) {
/*  91 */           State clone = (State)st.clone();
/*  92 */           clone.setIndex(this.states.size());
/*  93 */           addState(clone);
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/* 100 */       exc.printStackTrace();
/* 101 */       System.exit(0);
/*     */     }
/*     */   }
/*     */   
/*     */   public MarkovModel(String name, int noSnps, EmissionStateSpace emissionStateSpace) {
/* 106 */     this.name = name;
/* 107 */     this.states.add(MAGIC);
/* 108 */     MAGIC.setIndex(0);
/* 109 */     this.noSnps = Integer.valueOf(noSnps);
/* 110 */     this.emissionStateSpace = emissionStateSpace;
/*     */   }
/*     */   
/* 113 */   List<int[]> equivalenceClasses = new ArrayList();
/*     */   
/*     */   public abstract void addCounts(StateDistribution[] paramArrayOfStateDistribution, int paramInt1, int paramInt2);
/*     */   
/* 117 */   public State addState(State st) { if (this.states.contains(st)) { return st;
/*     */     }
/* 119 */     st.setIndex(this.states.size());
/* 120 */     if ((st instanceof EmissionState)) {
/* 121 */       this.equivalenceClasses.add(new int[] { st.getIndex() });
/*     */     }
/* 123 */     this.states.add(st);
/* 124 */     return st;
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
/* 140 */     List<State> l = new ArrayList();
/* 141 */     if (clazz.isInstance(startState)) l.add(startState);
/* 142 */     State state = startState;
/* 143 */     int cumLength = -1;
/* 144 */     while ((cumLength < 0) || (state != MAGIC)) {
/*     */       try {
/* 146 */         if ((state instanceof EmissionState)) cumLength++;
/* 147 */         if (clazz.isInstance(state)) l.add(state);
/* 148 */         state = sampleState(state, cumLength);
/*     */       } catch (Exception exc) {
/* 150 */         Exception exc1 = new Exception("problem with sampling from state " + state + " at " + state + " " + cumLength);
/* 151 */         exc1.initCause(exc);
/* 152 */         exc1.printStackTrace();
/* 153 */         System.exit(0);
/*     */       }
/*     */     }
/* 156 */     return (State[])l.toArray(new State[0]);
/*     */   }
/*     */   
/*     */ 
/* 160 */   public String getName() { return this.name; }
/*     */   
/*     */   public abstract double getTransitionScore(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   public void initialiseEmissionCounts() {
/* 165 */     for (Iterator<State> states = states(); states.hasNext();) {
/* 166 */       State st = (State)states.next();
/* 167 */       if ((st instanceof EmissionState))
/* 168 */         ((EmissionState)st).initialiseCounts();
/*     */     }
/*     */   }
/*     */   
/*     */   public void initialiseCounts() {
/* 173 */     initialiseTransitionCounts();
/* 174 */     initialiseEmissionCounts();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 184 */   public static double ps = 0.0D;
/*     */   
/*     */   public abstract void initialiseTransitionCounts();
/*     */   
/* 188 */   public int maxAdv() { int adv = 0;
/* 189 */     for (Iterator<State> it = this.states.iterator(); it.hasNext();) {
/* 190 */       State state = (State)it.next();
/* 191 */       if (state.adv > adv) adv = state.adv;
/*     */     }
/* 193 */     return adv;
/*     */   }
/*     */   
/*     */ 
/*     */   public int modelLength()
/*     */   {
/* 199 */     return this.states.size();
/*     */   }
/*     */   
/*     */   public State sampleState(State state, int i) {
/* 203 */     Iterator<State> it = statesOut(state, i).iterator();
/* 204 */     double pr = Math.random();
/* 205 */     double sum = 0.0D;
/* 206 */     while (it.hasNext()) {
/* 207 */       State entry = (State)it.next();
/* 208 */       sum += getTransitionScore(state.getIndex(), entry.getIndex(), i + entry.adv);
/* 209 */       if (sum >= pr) {
/* 210 */         return entry;
/*     */       }
/*     */     }
/* 213 */     throw new RuntimeException("did not find sample " + sum + " " + pr);
/*     */   }
/*     */   
/*     */   public static double sum(Map<Object, Double> m) {
/* 217 */     double sum = 0.0D;
/* 218 */     for (Iterator<Double> d = m.values().iterator(); d.hasNext();) {
/* 219 */       sum += ((Double)d.next()).doubleValue();
/*     */     }
/* 221 */     return sum;
/*     */   }
/*     */   
/* 224 */   protected static Map getMap(Iterator<Map.Entry<Object, Double>> it) { Map m = new HashMap();
/* 225 */     while (it.hasNext()) {
/* 226 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/* 227 */       m.put(entry.getKey(), entry.getValue());
/*     */     }
/* 229 */     return m;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Integer noSnps;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final EmissionStateSpace emissionStateSpace;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<State> states()
/*     */   {
/* 251 */     return this.states.iterator();
/*     */   }
/*     */   
/* 254 */   public List<State> statesIn(State to, int positionOfToEmission) { return this.states; }
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
/*     */   public List<State> statesOut(State from, int beforeToEmission)
/*     */   {
/* 275 */     return this.states;
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
/*     */   public void print(PrintWriter pw, List<Integer> cols, int popsize)
/*     */   {
/* 296 */     pw.println(this.name);
/* 297 */     for (Iterator<State> it = states(); it.hasNext();) {
/* 298 */       State st = (State)it.next();
/* 299 */       if ((st instanceof EmissionState)) {
/* 300 */         ((EmissionState)st).print(pw, "State_" + st.getName() + "    ", cols);
/* 301 */         pw.print("\n");
/*     */       }
/*     */     }
/* 304 */     pw.println("misclassification");
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
/*     */   public String toString()
/*     */   {
/* 317 */     StringWriter sb = new StringWriter();
/* 318 */     PrintWriter pw = new PrintWriter(sb);
/* 319 */     print(pw, null, 100);
/* 320 */     return sb.getBuffer().toString();
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
/*     */   public abstract void transferCountsToProbs(int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<int[]> equivalenceClasses()
/*     */   {
/* 343 */     return this.equivalenceClasses.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */   public void validate(int length)
/*     */     throws Exception
/*     */   {
/* 350 */     for (int j = 0; j < this.states.size(); j++) {
/* 351 */       State st = (State)this.states.get(j);
/* 352 */       if ((st instanceof EmissionState)) {
/* 353 */         ((EmissionState)st).validate();
/*     */       }
/*     */     }
/*     */     
/* 357 */     validateTrans(length);
/*     */   }
/*     */   
/*     */ 
/*     */   public void validateTrans(int length)
/*     */     throws Exception
/*     */   {
/* 364 */     int start = 0;int end = 1;
/* 365 */     for (int i = -1; i < length - 1; i++) {
/* 366 */       if (i >= 0) {
/* 367 */         start = 1;
/* 368 */         end = modelLength();
/*     */       }
/*     */       
/* 371 */       for (int k = start; k < end; k++) {
/* 372 */         double sum = 0.0D;
/* 373 */         Double[] d = new Double[modelLength()];
/* 374 */         for (int j = 0; j < modelLength(); j++) {
/* 375 */           int adv = ((State)this.states.get(j)).adv;
/* 376 */           d[j] = Double.valueOf(getTransitionScore(k, j, adv + i));
/* 377 */           sum += d[j].doubleValue();
/*     */         }
/* 379 */         if (Math.abs(1.0D - sum) > 0.01D) {
/* 380 */           validateTransAt(i);
/* 381 */           throw new Exception(sum + " at  " + i + " " + k + " " + getClass());
/*     */         }
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
/*     */   protected void validateTransAt(int i) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int[] statesIn(int paramInt1, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int[] statesOut(int paramInt1, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean trainEmissions();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String info()
/*     */   {
/* 429 */     return "";
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/MarkovModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */