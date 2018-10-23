/*     */ package lc1.dp.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.dp.transition.BetweenWithinTransitionProbs3;
/*     */ import lc1.stats.Dirichlet;
/*     */ import lc1.stats.Sampler;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class FreeExpSiteTrans extends SiteTransitions
/*     */ {
/*     */   final List<State> states;
/*     */   final Object[] transProbType;
/*     */   
/*     */   public FreeExpSiteTrans(List<Integer> loc, List<State> states, Double[] exp_p1, Double[] r, int length, Object[] transProbtype)
/*     */   {
/*  26 */     super(loc, states.size() - 1, exp_p1, r, length);
/*  27 */     this.states = states;
/*     */     
/*  29 */     this.transProbType = transProbtype;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FreeExpSiteTrans(FreeExpSiteTrans trans_init, boolean swtch)
/*     */   {
/*  37 */     super(trans_init, swtch);
/*  38 */     this.states = trans_init.states;
/*  39 */     this.transProbType = trans_init.transProbType;
/*     */   }
/*     */   
/*  42 */   double[] u = null;
/*     */   
/*     */   public SiteTransitions clone(boolean swtch) {
/*  45 */     return new FreeExpSiteTrans(this, swtch);
/*     */   }
/*     */   
/*     */   private static int[] group(List<State> states, int[] stateToGroup, int[] stateToIndexWithinGroup)
/*     */   {
/*  50 */     Map<String, List<Integer>> map = new java.util.TreeMap();
/*  51 */     map.put("-1", Arrays.asList(new Integer[] { Integer.valueOf(0) }));
/*  52 */     Set<Integer> commonClass = new HashSet();
/*  53 */     for (int i = 0; i < Constants.cnStatesInCommonClass().length; i++) {
/*  54 */       commonClass.add(Integer.valueOf(Constants.cnStatesInCommonClass()[i]));
/*     */     }
/*  56 */     for (int i = 1; i < states.size(); i++) {
/*  57 */       EmissionState st = (EmissionState)states.get(i);
/*  58 */       int best_index = st.getBestIndex(0);
/*  59 */       Emiss em = (Emiss)st.getEmissionStateSpace().get(best_index);
/*     */       
/*  61 */       int num = em.noCopies();
/*     */       
/*  63 */       for (int j = 1; j < st.noSnps(); j++) {
/*  64 */         int num1 = ((Emiss)st.getEmissionStateSpace().get(st.getBestIndex(j))).noCopies();
/*  65 */         if (num1 != num) { throw new RuntimeException("!!");
/*     */         }
/*     */       }
/*  68 */       String key = num;
/*     */       
/*     */ 
/*     */ 
/*  72 */       List<Integer> l = (List)map.get(key);
/*  73 */       if (l == null) {
/*  74 */         map.put(key, l = new ArrayList());
/*     */       }
/*  76 */       l.add(Integer.valueOf(i));
/*     */     }
/*  78 */     List[] membs = (List[])map.values().toArray(new List[0]);
/*  79 */     for (int j = 0; j < membs.length; j++) {
/*  80 */       for (int i = 0; i < membs[j].size(); i++) {
/*  81 */         int k = ((Integer)membs[j].get(i)).intValue();
/*  82 */         stateToGroup[k] = j;
/*  83 */         stateToIndexWithinGroup[k] = i;
/*     */       }
/*     */     }
/*  86 */     int[] res = new int[membs.length];
/*  87 */     for (int i = 0; i < res.length; i++) {
/*  88 */       res[i] = membs[i].size();
/*     */     }
/*  90 */     return res;
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
/*     */   public void initialise(double[] dist, double permute)
/*     */     throws Exception
/*     */   {
/* 108 */     int[] stateToGroup = new int[this.states.size()];
/* 109 */     int[] stateToIndexWithinGroup = new int[this.states.size()];
/* 110 */     int[] groupSizes = group(this.states, stateToGroup, stateToIndexWithinGroup);
/* 111 */     Double[] d = new Double[groupSizes.length];
/* 112 */     Arrays.fill(d, Double.valueOf(0.0D));
/* 113 */     for (int i = 0; i < this.states.size(); i++) {
/* 114 */       int g = stateToGroup[i]; int 
/* 115 */         tmp73_71 = g; Double[] tmp73_69 = d;tmp73_69[tmp73_71] = Double.valueOf(tmp73_69[tmp73_71].doubleValue() + dist[i]);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 121 */     double[] u0 = Constants.u_global(0);
/* 122 */     Dirichlet dir = new Dirichlet(d, Constants.u_global(0)[1]);
/* 123 */     Sampler[] samplers = new Sampler[groupSizes.length];
/* 124 */     for (int k = 0; k < groupSizes.length; k++) {
/* 125 */       Double[] d1 = new Double[groupSizes[k]];
/* 126 */       double incr = 1.0D / groupSizes[k];
/* 127 */       if (Constants.samplePermute() > 0.0D) {
/* 128 */         int j = 0;
/* 129 */         for (j = 0; j < d1.length; j++) {
/* 130 */           d1[j] = 
/* 131 */             Double.valueOf(Math.pow(Constants.samplePermute(), j));
/*     */         }
/*     */         
/* 134 */         lc1.stats.SimpleExtendedDistribution.normalise(d1);
/* 135 */         samplers[k] = new lc1.stats.PermutationSampler(d1, Constants.u_global(1)[1]);
/*     */       }
/*     */       else {
/* 138 */         Arrays.fill(d1, Double.valueOf(incr));
/* 139 */         samplers[k] = new Dirichlet(d1, Constants.u_global(1)[1]);
/*     */       }
/*     */     }
/*     */     
/* 143 */     for (int i = 1; i < this.transProbs.length; i++)
/*     */     {
/* 145 */       double[] expp = 
/*     */       
/*     */ 
/* 148 */         {
/* 149 */         (this.loc == null) || (this.loc.size() == 0) ? this.exp_p1[0].doubleValue() : Math.exp(-1.0D * this.r[0].doubleValue() * (((Integer)this.loc.get(i)).intValue() - ((Integer)this.loc.get(i - 1)).intValue())), 
/* 150 */         (this.loc == null) || (this.loc.size() == 0) ? this.exp_p1[1].doubleValue() : Math.exp(-1.0D * this.r[1].doubleValue() * (((Integer)this.loc.get(i)).intValue() - ((Integer)this.loc.get(i - 1)).intValue())) };
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 156 */       Dirichlet[] exp_p = {
/* 157 */         new Dirichlet(new double[] { expp[0], 1.0D - expp[0] }, Constants.u_global(0)[2]), 
/* 158 */         new Dirichlet(new double[] { expp[1], 1.0D - expp[1] }, Constants.u_global(1)[2]) };
/*     */       
/* 160 */       this.transProbs[i] = 
/* 161 */         (Constants.trans1() ? 
/* 162 */         new BetweenWithinTransitionProbs3(dir, samplers, stateToGroup, stateToIndexWithinGroup, exp_p, this.transProbType) : 
/* 163 */         new lc1.dp.transition.BetweenWithinTransitionProbs1(dir, samplers, stateToGroup, stateToIndexWithinGroup, exp_p, this.transProbType));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/FreeExpSiteTrans.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */