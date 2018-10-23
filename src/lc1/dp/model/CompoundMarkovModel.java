/*     */ package lc1.dp.model;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.dp.states.CompoundState;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.stats.SkewNormal;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public abstract class CompoundMarkovModel extends MarkovModel
/*     */ {
/*     */   public abstract MarkovModel getMarkovModel(int paramInt);
/*     */   
/*     */   public abstract int noCopies();
/*     */   
/*     */   public CompoundMarkovModel(MarkovModel m)
/*     */   {
/*  21 */     super(m);
/*     */   }
/*     */   
/*     */   public String info() {
/*  25 */     StringBuffer sb = new StringBuffer();
/*  26 */     MarkovModel[] m = getMemberModels();
/*  27 */     for (int i = 0; i < m.length; i++) {
/*  28 */       sb.append(m[i].info() + "\n");
/*     */     }
/*  30 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public boolean converged() {
/*  34 */     MarkovModel[] m = getMemberModels();
/*  35 */     for (int i = 0; i < m.length; i++) {
/*  36 */       if (!m[i].converged()) return false;
/*     */     }
/*  38 */     return true;
/*     */   }
/*     */   
/*     */   public void validate(int length) throws Exception {
/*     */     try {
/*  43 */       super.validate(length);
/*     */     }
/*     */     catch (Exception exc) {
/*  46 */       exc.printStackTrace();
/*  47 */       for (int i = 0; i < noCopies(); i++) {
/*  48 */         getMarkovModel(i).validate(length);
/*     */       }
/*  50 */       System.exit(0);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean allOneLength() {
/*  55 */     boolean allOneLength = true;
/*  56 */     for (Iterator<int[]> it = this.equivalenceClasses.iterator(); it.hasNext();) {
/*  57 */       int[] equiv = (int[])it.next();
/*  58 */       if (equiv.length > 1) allOneLength = false;
/*     */     }
/*  60 */     return allOneLength;
/*     */   }
/*     */   
/*     */   public CompoundMarkovModel(String name, int noSnps, lc1.dp.emissionspace.EmissionStateSpace emissionStateSpace) {
/*  64 */     super(name, noSnps, emissionStateSpace);
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract CompoundState getCompoundState(EmissionState[] paramArrayOfEmissionState);
/*     */   
/*     */   public abstract void refresh();
/*     */   
/*     */   public abstract EmissionState[] disambiguate(EmissionState[] paramArrayOfEmissionState1, EmissionState[] paramArrayOfEmissionState2, int paramInt, boolean paramBoolean);
/*     */   
/*     */   public abstract MarkovModel[] getMemberModels();
/*     */   
/*     */   public void print(PrintWriter pw, List<Integer> columns, int popsize)
/*     */   {
/*     */     try
/*     */     {
/*  80 */       for (int i = 0; i < Constants.format.length; i++) {
/*  81 */         probB(i).print(pw);
/*     */       }
/*  83 */       Map<String, MarkovModel> m = new java.util.HashMap();
/*  84 */       for (int i = 0; i < noCopies(); i++) {
/*  85 */         MarkovModel mm = getMarkovModel(i);
/*  86 */         m.put(mm.getName(), mm);
/*     */       }
/*  88 */       for (Iterator<MarkovModel> it = m.values().iterator(); it.hasNext();) {
/*  89 */         MarkovModel mm = (MarkovModel)it.next();
/*  90 */         if ((mm instanceof HaplotypeHMM)) { Constants.writeHMM();
/*     */         }
/*     */         
/*  93 */         mm.print(pw, columns, popsize);
/*  94 */         pw.println("####################################################");
/*     */       }
/*     */     } catch (Exception exc) {
/*  97 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/* 101 */   public CompoundMarkovModel unwrapModel() { return this; }
/*     */   
/*     */   public abstract IlluminaProbB probB(int paramInt);
/*     */   
/*     */   public void probR(List<List<Integer>> stateIndices, List<Integer> cnvIndex, List<SkewNormal> s, List<SkewNormal[]> s1, int ind)
/*     */   {
/* 107 */     for (int i = 1; i < this.states.size(); i++) {
/* 108 */       EmissionState st = (EmissionState)this.states.get(i);
/* 109 */       SkewNormal probR = st.probR()[ind];
/* 110 */       int index = s.indexOf(probR);
/* 111 */       if (index < 0) {
/* 112 */         s.add(probR);
/* 113 */         List<Integer> l = new java.util.ArrayList();
/* 114 */         l.add(Integer.valueOf(i));
/* 115 */         if (stateIndices != null) {
/* 116 */           stateIndices.add(l);
/* 117 */           cnvIndex.add(((EmissionState)this.states.get(i)).noCop());
/*     */         }
/* 119 */         if ((s1 != null) && ((st instanceof CompoundState))) {
/* 120 */           s1.add(((CompoundState)st).getProbabilityDist(ind));
/*     */         }
/*     */         
/*     */       }
/* 124 */       else if (stateIndices != null) {
/* 125 */         ((List)stateIndices.get(index)).add(Integer.valueOf(i));
/* 126 */         ((EmissionState)this.states.get(i)).noCop().intValue();cnvIndex.get(index);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/CompoundMarkovModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */