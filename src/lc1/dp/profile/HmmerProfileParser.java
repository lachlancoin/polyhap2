/*     */ package lc1.dp.profile;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.DotState;
/*     */ import lc1.dp.MarkovModel;
/*     */ import pal.datatype.AminoAcids;
/*     */ import pal.datatype.DataType;
/*     */ import pal.substmodel.RateMatrix;
/*     */ import pal.substmodel.WAG;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HmmerProfileParser
/*     */   implements Iterator<MarkovModel>
/*     */ {
/*  26 */   Logger logger = Logger.getLogger("lc1.dpprofile.HmmerProfileParser");
/*     */   
/*     */ 
/*     */ 
/*  30 */   static String hmmerString = "A      C      D      E      F      G      H      I      K      L      M      N      P      Q      R      S      T      V      W      Y";
/*     */   
/*  32 */   static String[] hmmerOrder = hmmerString.split("\\s+");
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
/*     */   static Integer[] alphList;
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
/*     */   RateMatrix nullM;
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
/*  77 */   private static final Double one = new Double(1.0D);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  82 */   static Double zero = new Double(0.0D);
/*     */   private DataType dt;
/*     */   BufferedReader in;
/*     */   
/*     */   public void remove() {}
/*     */   
/*     */   private Double convertToProb(String sc) {
/*  89 */     if (sc.equals("*")) return zero;
/*  90 */     int score = Integer.parseInt(sc);
/*  91 */     double result = 0.0D;
/*  92 */     if (score != Integer.MIN_VALUE) {
/*  93 */       result = 1.0D * Math.pow(2.0D, score / 1000.0D);
/*     */     }
/*  95 */     return new Double(result);
/*     */   }
/*     */   
/*     */   private double convertToProb(String sc, double nullprob) {
/*  99 */     if (sc.equals("*")) return 0.0D;
/* 100 */     double score = Integer.parseInt(sc);
/* 101 */     double result = 0.0D;
/* 102 */     if (score != -2.147483648E9D) {
/* 103 */       result = nullprob * Math.pow(2.0D, score / 1000.0D);
/*     */     }
/* 105 */     return result;
/*     */   }
/*     */   
/*     */   public boolean hasNext() {
/* 109 */     return this.currentString != null;
/*     */   }
/*     */   
/*     */   public HmmerProfileParser(BufferedReader in, DataType dt, boolean emitColumns)
/*     */     throws IOException
/*     */   {
/*  28 */     this.logger.setLevel(Level.WARNING);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  36 */     DataType amino = AminoAcids.DEFAULT_INSTANCE;
/*  37 */     alphList = new Integer[amino.getNumStates()];
/*  38 */     for (int i = 0; i < hmmerOrder.length; i++) {
/*  39 */       alphList[i] = Integer.valueOf(amino.getState(hmmerOrder[i].charAt(0)));
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
/* 125 */     this.dt = dt;
/* 126 */     this.in = in;
/* 127 */     this.emissionClass = ProfileEmissionState.class;
/* 128 */     this.nullM = (emitColumns ? new WAG(WAG.getOriginalFrequencies()) : null);
/* 129 */     this.currentString = in.readLine();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HmmerProfileHMM next()
/*     */   {
/* 141 */     HmmerProfileHMM hmm = null;
/*     */     try
/*     */     {
/* 144 */       String[] str = this.in.readLine().split("\\s+");
/* 145 */       setName(str[1]);
/* 146 */       HmmerModel model = new HmmerModel(null);
/*     */       
/*     */ 
/*     */ 
/* 150 */       hmm = model.hmm;
/*     */       
/* 152 */       this.currentString = this.in.readLine();
/* 153 */       if (this.currentString == null) this.in.close();
/*     */     } catch (Exception exc) {
/* 155 */       exc.printStackTrace();
/* 156 */       System.exit(0);
/*     */     }
/* 158 */     hmm.fix();
/* 159 */     return hmm;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setName(String domain)
/*     */   {
/* 168 */     this.name = domain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   String currentString;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   Class emissionClass;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String name;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private class HmmerModel
/*     */   {
/*     */     HmmerProfileHMM hmm;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 197 */     double[] null_prob = new double[HmmerProfileParser.this.dt.getNumStates()];
/*     */     
/*     */     private void setEmission(ProfileEmissionState state, String currentString) {
/* 200 */       String[] vals = currentString.trim().split("\\s+");
/* 201 */       double[] emission = new double[HmmerProfileParser.this.dt.getNumStates()];
/* 202 */       for (int j = 0; j < emission.length; j++) {
/* 203 */         int index = HmmerProfileParser.alphList[j].intValue();
/* 204 */         emission[index] = HmmerProfileParser.this.convertToProb(vals[(j + 1)], this.null_prob[index]);
/*     */       }
/* 206 */       state.setDistribution(emission);
/*     */     }
/*     */     
/*     */     private HmmerModel()
/*     */     {
/*     */       try {
/* 212 */         pfamId = "";
/* 213 */         while ((HmmerProfileParser.this.currentString = HmmerProfileParser.this.in.readLine()) != null) {
/* 214 */           HmmerProfileParser.this.logger.fine(HmmerProfileParser.this.currentString);
/* 215 */           if (HmmerProfileParser.this.currentString.startsWith("LENG")) {
/* 216 */             int length = Integer.parseInt(HmmerProfileParser.this.currentString.trim().split("\\s+")[1]);
/* 217 */             this.hmm = new HmmerProfileHMM(pfamId, length, HmmerProfileParser.this.emissionClass);
/* 218 */             this.hmm.setPfamId(HmmerProfileParser.this.currentString.trim().split("\\s+")[1]);
/* 219 */             this.hmm.initialiseTransitions();
/*     */           }
/* 221 */           else if (HmmerProfileParser.this.currentString.startsWith("ACC")) {
/* 222 */             pfamId = HmmerProfileParser.this.currentString.trim().split("\\s+")[1];
/* 223 */             int index = pfamId.indexOf(".");
/* 224 */             if (index >= 0) pfamId = pfamId.substring(0, index);
/*     */           }
/* 226 */           else if (HmmerProfileParser.this.currentString.startsWith("GA")) {
/* 227 */             String[] vals = HmmerProfileParser.this.currentString.trim().split("\\s+");
/* 228 */             this.hmm.setLsDomThresh(Float.valueOf(Float.parseFloat(vals[1])));
/* 229 */             this.hmm.setLsSeqThresh(Float.valueOf(Float.parseFloat(vals[2])));
/*     */           }
/* 231 */           else if (HmmerProfileParser.this.currentString.startsWith("NULE")) {
/* 232 */             String[] vals = HmmerProfileParser.this.currentString.trim().split("\\s+");
/* 233 */             for (int j = 0; j < HmmerProfileParser.this.dt.getNumStates(); j++) {
/* 234 */               this.null_prob[HmmerProfileParser.alphList[j].intValue()] = HmmerProfileParser.this.convertToProb(vals[(j + 1)], 0.05D);
/*     */             }
/* 236 */             this.hmm.cState().setDistribution(this.null_prob);
/* 237 */             this.hmm.jState().setDistribution(this.null_prob);
/* 238 */             this.hmm.nState().setDistribution(this.null_prob);
/* 239 */             this.hmm.getNullModel().gState().setDistribution(this.null_prob);
/*     */           }
/* 241 */           else if (HmmerProfileParser.this.currentString.startsWith("NULT")) {
/* 242 */             String[] vals = HmmerProfileParser.this.currentString.trim().split("\\s+");
/* 243 */             ProfileEmissionState g = this.hmm.getNullModel().gState();
/* 244 */             double gtog = HmmerProfileParser.this.convertToProb(vals[1]).doubleValue();
/* 245 */             this.hmm.getNullModel().setTransition(g, g, gtog);
/* 246 */             this.hmm.getNullModel().setTransition(g, this.hmm.getNullModel().MAGIC, HmmerProfileParser.this.convertToProb(vals[2]).doubleValue());
/* 247 */             this.hmm.getNullModel().setTransition(this.hmm.getNullModel().MAGIC, g, 1.0D);
/*     */           }
/* 249 */           else if (HmmerProfileParser.this.currentString.startsWith("XT")) {
/* 250 */             String[] vals = HmmerProfileParser.this.currentString.trim().split("\\s+");
/*     */             
/* 252 */             this.hmm.setTransition(this.hmm.nState(), this.hmm.begin, HmmerProfileParser.this.convertToProb(vals[1]).doubleValue());
/* 253 */             this.hmm.setTransition(this.hmm.nState(), this.hmm.nState(), HmmerProfileParser.this.convertToProb(vals[2]).doubleValue());
/*     */             
/* 255 */             this.hmm.setTransition(this.hmm.MAGIC, this.hmm.nState(), this.hmm.getTransitionScore(this.hmm.nState().index, this.hmm.nState().index, 1));
/* 256 */             this.hmm.setTransition(this.hmm.MAGIC, this.hmm.begin, this.hmm.getTransitionScore(this.hmm.nState().index, this.hmm.begin.index, 1));
/*     */             
/* 258 */             this.hmm.setTransition(this.hmm.end, this.hmm.cState(), 
/* 259 */               HmmerProfileParser.this.convertToProb(vals[3]).doubleValue());
/* 260 */             this.hmm.setTransition(this.hmm.end, this.hmm.jState(), 
/* 261 */               HmmerProfileParser.this.convertToProb(vals[4]).doubleValue());
/* 262 */             this.hmm.setTransition(this.hmm.cState(), this.hmm.MAGIC, 
/* 263 */               HmmerProfileParser.this.convertToProb(vals[5]).doubleValue());
/* 264 */             this.hmm.setTransition(this.hmm.cState(), this.hmm.cState(), 
/* 265 */               HmmerProfileParser.this.convertToProb(vals[6]).doubleValue());
/*     */             
/*     */ 
/* 268 */             double endToC = this.hmm.getTransitionScore(this.hmm.end.index, this.hmm.cState().index, 1);
/* 269 */             this.hmm.setTransition(this.hmm.end, this.hmm.cState(), 
/* 270 */               endToC * this.hmm.getTransitionScore(this.hmm.cState().index, this.hmm.cState().index, 1));
/* 271 */             this.hmm.setTransition(this.hmm.end, this.hmm.MAGIC, 
/* 272 */               endToC * this.hmm.getTransitionScore(this.hmm.cState().index, this.hmm.MAGIC.index, 1));
/*     */             
/*     */ 
/* 275 */             this.hmm.setTransition(this.hmm.jState(), this.hmm.begin, 
/* 276 */               HmmerProfileParser.this.convertToProb(vals[7]).doubleValue());
/* 277 */             this.hmm.setTransition(this.hmm.jState(), this.hmm.jState(), 
/* 278 */               HmmerProfileParser.this.convertToProb(vals[8]).doubleValue());
/*     */           }
/*     */           else {
/* 281 */             if (HmmerProfileParser.this.currentString.startsWith("HMM ")) {
/* 282 */               if (!HmmerProfileParser.this.currentString.trim().endsWith(HmmerProfileParser.hmmerString)) throw new RuntimeException("hmmer order changed!");
/* 283 */               HmmerProfileParser.this.in.readLine();
/* 284 */               HmmerProfileParser.this.currentString = HmmerProfileParser.this.in.readLine();
/* 285 */               String[] beginTransition = HmmerProfileParser.this.currentString.trim().split("\\s+");
/* 286 */               HmmerProfileParser.this.logger.info(HmmerProfileParser.this.currentString);
/* 287 */               this.hmm.setTransition(this.hmm.begin, this.hmm.getMatch(0), HmmerProfileParser.this.convertToProb(beginTransition[0]).doubleValue());
/* 288 */               if (beginTransition[1] != null) {
/* 289 */                 this.hmm.setTransition(this.hmm.begin, this.hmm.getInsert(0), HmmerProfileParser.this.convertToProb(beginTransition[1]).doubleValue());
/*     */               }
/* 291 */               this.hmm.setTransition(this.hmm.begin, this.hmm.getDelete(0), HmmerProfileParser.this.convertToProb(beginTransition[2]).doubleValue());
/* 292 */               HmmerProfileParser.this.currentString = HmmerProfileParser.this.in.readLine();
/* 293 */               for (int i = 0; !HmmerProfileParser.this.currentString.startsWith("//"); i++) {
/* 294 */                 parseState(HmmerProfileParser.this.in, i);
/*     */               }
/* 296 */               break;
/*     */             }
/* 298 */             if (HmmerProfileParser.this.currentString.startsWith("EVD")) {
/* 299 */               String[] st = HmmerProfileParser.this.currentString.split("\\s+");
/* 300 */               this.hmm.evdparams = new double[] { Double.parseDouble(st[1]), Double.parseDouble(st[2]) };
/*     */             }
/*     */           }
/*     */         }
/*     */       } catch (Throwable t) {
/*     */         String pfamId;
/* 306 */         t.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */     private void parseState(BufferedReader in, int index) throws Exception {
/* 311 */       ProfileEmissionState match = this.hmm.getMatch(index);
/*     */       
/* 313 */       DotState delete = this.hmm.getDelete(index);
/* 314 */       setEmission(match, HmmerProfileParser.this.currentString);
/* 315 */       HmmerProfileParser.this.currentString = in.readLine();
/* 316 */       ProfileEmissionState insert = null;
/* 317 */       if (index < this.hmm.columns() - 1) {
/* 318 */         insert = this.hmm.getInsert(index);
/* 319 */         setEmission(insert, HmmerProfileParser.this.currentString);
/*     */       }
/* 321 */       HmmerProfileParser.this.currentString = in.readLine();
/* 322 */       HmmerProfileParser.this.logger.info(HmmerProfileParser.this.currentString);
/* 323 */       String[] vals = HmmerProfileParser.this.currentString.trim().split("\\s+");
/* 324 */       if (index < this.hmm.columns() - 1) {
/* 325 */         this.hmm.setTransition(match, this.hmm.getMatch(index + 1), HmmerProfileParser.this.convertToProb(vals[1]).doubleValue());
/* 326 */         this.hmm.setTransition(match, this.hmm.getInsert(index), HmmerProfileParser.this.convertToProb(vals[2]).doubleValue());
/* 327 */         this.hmm.setTransition(match, this.hmm.getDelete(index + 1), HmmerProfileParser.this.convertToProb(vals[3]).doubleValue());
/* 328 */         this.hmm.setTransition(insert, this.hmm.getMatch(index + 1), HmmerProfileParser.this.convertToProb(vals[4]).doubleValue());
/* 329 */         this.hmm.setTransition(insert, insert, HmmerProfileParser.this.convertToProb(vals[5]).doubleValue());
/* 330 */         this.hmm.setTransition(delete, this.hmm.getMatch(index + 1), HmmerProfileParser.this.convertToProb(vals[6]).doubleValue());
/* 331 */         this.hmm.setTransition(delete, this.hmm.getDelete(index + 1), HmmerProfileParser.this.convertToProb(vals[7]).doubleValue());
/*     */       } else {
/* 333 */         this.hmm.setTransition(match, this.hmm.end, HmmerProfileParser.one.doubleValue());
/* 334 */         this.hmm.setTransition(delete, this.hmm.end, HmmerProfileParser.one.doubleValue());
/*     */       }
/* 336 */       HmmerProfileParser.this.currentString = in.readLine();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/profile/HmmerProfileParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */