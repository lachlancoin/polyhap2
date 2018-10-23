/*     */ package lc1.dp.profile;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import lc1.dp.DotState;
/*     */ import lc1.dp.FastMarkovModel;
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
/*     */ public class ProfileHMM
/*     */   extends FastMarkovModel
/*     */ {
/*     */   static final long serialVersionUID = 654298764L;
/*     */   private final int columns;
/*     */   public ProfileEmissionState j;
/*     */   public ProfileEmissionState c;
/*     */   public ProfileEmissionState n;
/*     */   public DotState begin;
/*     */   public DotState end;
/*     */   private final ProfileEmissionState[] matchStates;
/*     */   private final ProfileEmissionState[] insertStates;
/*     */   private final DotState[] deleteStates;
/*     */   
/*     */   public ProfileEmissionState[] getMatchStates()
/*     */   {
/*  39 */     return this.matchStates;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int columns()
/*     */   {
/*  66 */     return this.columns;
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
/*     */   public ProfileEmissionState getMatch(int indx)
/*     */     throws IndexOutOfBoundsException
/*     */   {
/*  83 */     if ((indx < 0) || (indx >= this.columns)) {
/*  84 */       throw new IndexOutOfBoundsException(
/*  85 */         "Match-state index must be within (0.." + this.columns + "), not " + indx);
/*     */     }
/*     */     
/*     */ 
/*  89 */     return this.matchStates[indx];
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
/*     */   public ProfileEmissionState getInsert(int indx)
/*     */     throws IndexOutOfBoundsException
/*     */   {
/* 110 */     if ((indx < 0) || (indx >= this.columns - 1)) {
/* 111 */       throw new IndexOutOfBoundsException(
/* 112 */         "Insert-state index must be within (0.." + (this.columns - 1) + "), not " + indx);
/*     */     }
/*     */     
/*     */ 
/* 116 */     return this.insertStates[indx];
/*     */   }
/*     */   
/*     */   public DotState getDelete(int indx)
/*     */     throws IndexOutOfBoundsException
/*     */   {
/* 122 */     if ((indx < 0) || (indx >= this.columns)) {
/* 123 */       throw new IndexOutOfBoundsException(
/* 124 */         "delete-state index must be within (1.." + this.columns + "), not " + indx);
/*     */     }
/*     */     
/*     */ 
/* 128 */     return this.deleteStates[indx];
/*     */   }
/*     */   
/*     */   public static ProfileEmissionState makeEmissionState(String st, short sh, int adv, boolean MATCH)
/*     */   {
/* 133 */     return new ProfileEmissionState(st, sh, adv, MATCH);
/*     */   }
/*     */   
/*     */   public ProfileHMM(String name, int columns, Class emissionClass)
/*     */     throws InstantiationException
/*     */   {
/* 139 */     super(name);
/*     */     try {
/* 141 */       this.columns = columns;
/* 142 */       this.matchStates = new ProfileEmissionState[columns];
/* 143 */       this.insertStates = new ProfileEmissionState[columns - 1];
/* 144 */       this.deleteStates = new DotState[columns];
/* 145 */       this.begin = new DotState(name + "_B");
/* 146 */       ProfileEmissionState iO = makeEmissionState("i", (short)0, 1, false);
/* 147 */       DotState dO = new DotState(name + "_d_0");
/* 148 */       ProfileEmissionState mO = makeEmissionState(name + "_m", (short)0, 1, true);
/* 149 */       this.j = makeEmissionState("j", (short)0, 1, false);
/*     */       
/* 151 */       this.c = makeEmissionState("c", (short)0, 1, false);
/* 152 */       this.n = makeEmissionState("n", (short)0, 1, false);
/* 153 */       this.insertStates[0] = iO;this.matchStates[0] = mO;this.deleteStates[0] = dO;
/* 154 */       addState(this.n);
/* 155 */       addState(this.j);
/* 156 */       addState(this.begin);
/* 157 */       addState(mO);
/* 158 */       addState(iO);
/* 159 */       addState(dO);
/*     */       
/* 161 */       for (int i = 1; i < columns; i++) {
/* 162 */         ProfileEmissionState mN = makeEmissionState(name + "_m", (short)i, 1, true);
/* 163 */         DotState dN = new DotState(name + "_d_" + i);
/* 164 */         addState(mN);
/* 165 */         this.matchStates[i] = mN;
/* 166 */         if (i < columns - 1) {
/* 167 */           ProfileEmissionState iN = makeEmissionState("i", (short)i, 1, false);
/* 168 */           addState(iN);
/* 169 */           this.insertStates[i] = iN;
/* 170 */           iO = iN;
/*     */         }
/* 172 */         addState(dN);
/* 173 */         this.deleteStates[i] = dN;
/* 174 */         mO = mN;
/* 175 */         dO = dN;
/*     */       }
/* 177 */       this.end = new DotState(name + "_E");
/* 178 */       addState(this.end);
/* 179 */       addState(this.c);
/*     */     } catch (Exception exc) {
/* 181 */       InstantiationException excNew = new InstantiationException("");
/* 182 */       excNew.initCause(exc);
/* 183 */       throw excNew;
/*     */     }
/*     */   }
/*     */   
/*     */   public ProfileEmissionState jState()
/*     */   {
/* 189 */     return this.j;
/*     */   }
/*     */   
/*     */   public ProfileEmissionState cState()
/*     */   {
/* 194 */     return this.c;
/*     */   }
/*     */   
/*     */   public ProfileEmissionState nState()
/*     */   {
/* 199 */     return this.n;
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/* 204 */     throw new RuntimeException("clone not supported");
/*     */   }
/*     */   
/*     */   protected List initStateSpace()
/*     */   {
/* 209 */     Integer[] res = new Integer[21];
/* 210 */     for (int i = 0; i < res.length; i++) {
/* 211 */       res[i] = Integer.valueOf(i);
/*     */     }
/*     */     
/* 214 */     return Arrays.asList(res);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/profile/ProfileHMM.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */