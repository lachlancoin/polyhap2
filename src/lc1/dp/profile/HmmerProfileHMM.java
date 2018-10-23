/*     */ package lc1.dp.profile;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ public class HmmerProfileHMM
/*     */   extends ProfileHMM
/*     */ {
/*     */   static final long serialVersionUID = 654324L;
/*     */   
/*     */   public HmmerProfileHMM(String name, int columns, Class clazz)
/*     */     throws InstantiationException
/*     */   {
/*  43 */     super(name, columns, clazz);
/*  44 */     this.nullModel = new NullModel(); }
/*     */   
/*  46 */   public boolean useContext = false;
/*     */   public double[] evdparams;
/*     */   
/*  49 */   public double[] getEvdParams() { return this.evdparams; }
/*     */   
/*     */   double[] insertScore;
/*     */   boolean logspaceInsertScore;
/*     */   public void fix() {
/*  54 */     super.fix();
/*  55 */     this.nullModel.fix();
/*     */   }
/*     */   
/*     */   public void initialiseTransitions() {
/*  59 */     super.initialiseTransitions();
/*  60 */     this.nullModel.initialiseTransitions();
/*     */   }
/*     */   
/*  63 */   public double getEvdPosition() { return this.evdparams[0]; }
/*     */   
/*     */   public double getEvdScale()
/*     */   {
/*  67 */     return this.evdparams[1];
/*     */   }
/*     */   
/*     */   protected NullModel nullModel;
/*     */   String pfamId;
/*     */   Float ls_dom_thresh;
/*     */   Float ls_seq_thresh;
/*     */   public synchronized NullModel getNullModel() {
/*  75 */     return this.nullModel;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class NullModel
/*     */     extends FastMarkovModel
/*     */   {
/*     */     static final long serialVersionUID = 543623L;
/*     */     
/*     */     public ProfileEmissionState gState;
/*     */     
/*     */ 
/*     */     public ProfileEmissionState gState()
/*     */     {
/*  89 */       return this.gState;
/*     */     }
/*     */     
/*     */     NullModel() {
/*  93 */       super();
/*     */       try {
/*  95 */         this.gState = 
/*  96 */           HmmerProfileHMM.makeEmissionState("G", (short)0, 1, false);
/*     */         
/*  98 */         addState(this.gState);
/*     */       }
/*     */       catch (Throwable t) {
/* 101 */         t.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */     public Object clone()
/*     */     {
/* 107 */       return new NullModel();
/*     */     }
/*     */     
/*     */     protected List initStateSpace()
/*     */     {
/* 112 */       Integer[] res = new Integer[21];
/* 113 */       for (int i = 0; i < res.length; i++) {
/* 114 */         res[i] = Integer.valueOf(i);
/*     */       }
/*     */       
/* 117 */       return Arrays.asList(res);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPfamId(String pfamId)
/*     */   {
/* 126 */     this.pfamId = pfamId;
/*     */   }
/*     */   
/* 129 */   public Object getPfamId() { return this.pfamId; }
/*     */   
/*     */ 
/*     */   public Float getLsDomThresh()
/*     */   {
/* 134 */     return this.ls_dom_thresh;
/*     */   }
/*     */   
/* 137 */   public void setLsDomThresh(Float ls_dom_thresh) { this.ls_dom_thresh = ls_dom_thresh; }
/*     */   
/*     */   public void setLsSeqThresh(Float thresh) {
/* 140 */     this.ls_seq_thresh = thresh;
/*     */   }
/*     */   
/* 143 */   public Number getLsSeqThresh() { return this.ls_seq_thresh; }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/profile/HmmerProfileHMM.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */