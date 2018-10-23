/*     */ package lc1.dp.data.collection;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import lc1.CGH.Location;
/*     */ import lc1.CGH.Locreader;
/*     */ import lc1.dp.data.representation.ComparableArray;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ import lc1.dp.data.representation.SimpleScorableObject;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.dp.illumina.IlluminaProbR;
/*     */ import lc1.dp.states.HaplotypeEmissionState;
/*     */ import lc1.dp.states.IlluminaNoBg;
/*     */ import lc1.stats.IlluminaDistribution;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class IlluminaRDataCollection extends LikelihoodDataCollection
/*     */ {
/*     */   IlluminaProbB distB;
/*     */   IlluminaProbR distR;
/*     */   
/*     */   public IlluminaProbB getB(String key)
/*     */   {
/*  36 */     return this.distB;
/*     */   }
/*     */   
/*     */   public IlluminaProbR getR(String key)
/*     */   {
/*  41 */     return this.distR;
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
/*     */   public HaplotypeEmissionState createEmissionState(String key)
/*     */   {
/*  82 */     return new IlluminaNoBg(key, this.stSp[1], getR(key), getB(key), this.length.intValue(), this.index);
/*     */   }
/*     */   
/*     */ 
/*     */   public final void createDataStructure(List<String> indiv)
/*     */   {
/*  88 */     int i = 0;
/*  89 */     for (Iterator<String> it = indiv.iterator(); it.hasNext(); i++) {
/*  90 */       String key = (String)it.next();
/*     */       
/*  92 */       HaplotypeEmissionState value = createEmissionState(key);
/*     */       
/*  94 */       this.dataL.put(key, value);
/*  95 */       this.data.put(key, SimpleScorableObject.make(key, this.loc.size(), value.getEmissionStateSpace()));
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
/*     */   public final void process(String indiv, String[] header, String[] geno, int i)
/*     */   {
/* 149 */     throw new Error("Unresolved compilation problem: \n\tCannot directly invoke the abstract method process(String, String[], String[], int) for the type DataCollection\n");
/*     */   }
/*     */   
/*     */   public void fix(Locreader loc, int thresh) {
/* 153 */     List<Integer> fixed = new ArrayList();
/* 154 */     List<Integer> nonFixed = new ArrayList();
/* 155 */     EmissionStateSpace emstsp = getEmStSpace();
/* 156 */     double[] d = new double[emstsp.size()];
/* 157 */     for (int i = 0; i < this.loc.size(); i++) {
/* 158 */       int pos = ((Integer)this.loc.get(i)).intValue();
/* 159 */       Location overl = loc.contains(pos, thresh);
/* 160 */       if (overl == null) {
/* 161 */         fixed.add((Integer)this.loc.get(i));
/* 162 */         for (Iterator<lc1.dp.states.EmissionState> it = dataLvalues(); it.hasNext();) {
/* 163 */           HaplotypeEmissionState nxt = (HaplotypeEmissionState)it.next();
/* 164 */           nxt.emissions[i].calcDistribution(getB(nxt.getName()), d, emstsp, 2);
/*     */           
/* 166 */           SimpleExtendedDistribution.normalise(d);
/* 167 */           int max = Constants.getMax(d);
/* 168 */           if (d[max] > 0.999D) {
/* 169 */             nxt.emissions[i] = new lc1.stats.IntegerDistribution(max);
/*     */           } else {
/* 171 */             nxt.emissions[i] = new SimpleExtendedDistribution(d, Double.POSITIVE_INFINITY);
/*     */           }
/*     */         }
/*     */       } else {
/* 175 */         nonFixed.add((Integer)this.loc.get(i));
/*     */       }
/*     */     }
/* 178 */     calculateMaf(true);
/* 179 */     System.err.println("finished fixing \nfixed:" + fixed + "\n not fixed" + nonFixed);
/*     */   }
/*     */   
/*     */   public IlluminaRDataCollection(File f, short index, int no_copies) throws Exception {
/* 183 */     super(f, index, no_copies);
/*     */   }
/*     */   
/*     */   public IlluminaRDataCollection(IlluminaRDataCollection collection)
/*     */   {
/* 188 */     super(collection);
/* 189 */     this.distR = collection.distR;
/* 190 */     this.distB = collection.distB;
/*     */   }
/*     */   
/* 193 */   public IlluminaRDataCollection(DataCollection obj) { super(obj);
/*     */     
/* 195 */     this.stSp = obj.stSp;
/* 196 */     this.stSp1 = obj.stSp1;
/* 197 */     this.trans = obj.trans;
/*     */     
/*     */ 
/*     */ 
/* 201 */     makeDistributions();
/*     */     
/* 203 */     StringBuffer sb = new StringBuffer();
/* 204 */     for (int i = 0; i < this.loc.size(); i++) {
/* 205 */       sb.append("%5.3f ");
/*     */     }
/*     */     
/* 208 */     for (Iterator<String> it = this.data.keySet().iterator(); it.hasNext();) {
/* 209 */       String key = (String)it.next();
/* 210 */       PIGData d = (PIGData)this.data.get(key);
/* 211 */       IlluminaNoBg ldc = 
/*     */       
/* 213 */         new IlluminaNoBg(d.getName(), this.stSp[1], getR(key), getB(key), this.loc.size(), this.index);
/*     */       
/*     */ 
/* 216 */       for (int i = 0; i < d.length(); i++) {
/* 217 */         ComparableArray arr = (ComparableArray)d.getElement(i);
/* 218 */         int obj_index = this.stSp[1].getGenotype(arr).intValue();
/*     */         
/*     */ 
/*     */ 
/* 222 */         int cn = this.stSp[1].getCN(obj_index);
/* 223 */         ldc.set(i, getR(key).sampleR(2, cn), getB(key).sampleB(obj_index));
/*     */       }
/* 225 */       this.dataL.put(key, ldc);
/*     */     }
/*     */   }
/*     */   
/* 229 */   public List<Integer> getBackgroundCN() { return Arrays.asList(new Integer[] { Integer.valueOf(2) }); }
/*     */   
/*     */   public void makeDistributions() {
/* 232 */     this.distR = new IlluminaProbR(this.stSp[1].copyNumber, getBackgroundCN(), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 238 */       Constants.r_x(), 
/* 239 */       Constants.r_mean(), 
/* 240 */       Constants.r_var(), 
/* 241 */       Constants.r_skew(), 
/* 242 */       false);
/*     */     
/* 244 */     Double[] d1 = new Double[Constants.b_mean.length];
/* 245 */     for (int i = 0; i < d1.length; i++) {
/* 246 */       d1[i] = Double.valueOf(Constants.b_mean[i]);
/*     */     }
/* 248 */     this.distB = new IlluminaProbB(this.stSp[1], false);
/*     */   }
/*     */   
/*     */   public void initialisationStep()
/*     */   {
/* 253 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public void maximisationStep(double[] pseudo, int i)
/*     */   {
/* 259 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DataC clone()
/*     */   {
/* 268 */     return new IlluminaRDataCollection(this);
/*     */   }
/*     */   
/*     */   public void printDist(PrintWriter pw) {
/* 272 */     this.distR.print(pw);
/* 273 */     this.distB.print(pw);
/*     */   }
/*     */   
/*     */   public void print(String[] print, PrintWriter pw)
/*     */   {
/* 278 */     for (int i = 0; i < print.length; i++) {
/* 279 */       pw.print(print[i]);
/* 280 */       pw.print(i < print.length - 1 ? "\t" : "\n");
/*     */     }
/*     */   }
/*     */   
/* 284 */   public void printHapMapFormat(File f, String chr) throws Exception { String len = "%7s";
/* 285 */     PrintWriter pw = new PrintWriter(new java.io.BufferedWriter(new FileWriter(f)));
/* 286 */     StringBuffer header = new StringBuffer("%-7s");
/* 287 */     List<String> list = new ArrayList((Collection)this.data.keySet());
/* 288 */     String[] toPrint = new String[5 + 3 * list.size()];
/* 289 */     toPrint[0] = "Index";
/* 290 */     toPrint[1] = "Name";
/* 291 */     toPrint[2] = "Address";
/* 292 */     toPrint[3] = "Chr";
/* 293 */     toPrint[4] = "Position";
/* 294 */     for (int i = 1; i < 5; i++) {
/* 295 */       header.append(" " + len);
/*     */     }
/* 297 */     for (int i = 0; i < list.size(); i++) {
/* 298 */       toPrint[(i * 3 + 5)] = ((String)list.get(i) + ".GType");
/* 299 */       toPrint[(i * 3 + 6)] = ((String)list.get(i) + ".B Allele Freq");
/* 300 */       toPrint[(i * 3 + 7)] = ((String)list.get(i) + ".Log R Ratio");
/* 301 */       header.append(" " + len);
/*     */     }
/* 303 */     String headerSt = header.toString();
/*     */     
/*     */ 
/* 306 */     print(toPrint, pw);
/* 307 */     for (int pos_index = 0; pos_index < this.loc.size(); pos_index++) {
/* 308 */       toPrint[0] = "-";toPrint[1] = "-";toPrint[2] = "-";
/* 309 */       toPrint[3] = chr;toPrint[4] = ((Integer)this.loc.get(pos_index)).toString();
/*     */       
/* 311 */       for (int i = 0; i < list.size(); i++) {
/* 312 */         String key = (String)list.get(i);
/* 313 */         ComparableArray comp = (ComparableArray)((PIGData)this.data.get(key)).getElement(pos_index);
/* 314 */         IlluminaNoBg ill = (IlluminaNoBg)this.dataL.get(list.get(i));
/* 315 */         toPrint[(i * 3 + 5)] = comp.toStringPrint();
/* 316 */         IlluminaDistribution dist = (IlluminaDistribution)ill.emissions[i];
/* 317 */         toPrint[(i * 3 + 6)] = Format.sprintf("%5.3f", new Object[] { dist.b() });
/* 318 */         toPrint[(i * 3 + 7)] = Format.sprintf("%5.3f", new Object[] { dist.r() });
/*     */       }
/* 320 */       print(toPrint, pw);
/*     */     }
/*     */     
/* 323 */     pw.close();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/collection/IlluminaRDataCollection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */