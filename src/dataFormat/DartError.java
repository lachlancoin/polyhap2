/*     */ package dataFormat;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ 
/*     */ public class DartError
/*     */ {
/*  22 */   static int[] mid = { 34135863, 40527829 };
/*     */   Map<String, String[]> phasedData;
/*     */   Map<String, List<String>> rawData;
/*     */   
/*     */   public void getPhasedData(File f) throws Exception {
/*  27 */     this.phasedData = new HashMap();
/*  28 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  29 */     String st = br.readLine();
/*  30 */     while (!st.startsWith("#")) {
/*  31 */       st = br.readLine();
/*     */     }
/*  33 */     while (st != null) {
/*  34 */       if (st.startsWith("#")) {
/*  35 */         String[] str = st.split("\\s+");
/*  36 */         List<String> te = new ArrayList();
/*  37 */         st = br.readLine();
/*  38 */         while (!st.startsWith("#")) {
/*  39 */           te.add(st);
/*  40 */           st = br.readLine();
/*  41 */           if (st == null) break;
/*     */         }
/*  43 */         String[] temp = new String[te.size()];
/*  44 */         for (int i = 0; i < temp.length; i++) {
/*  45 */           temp[i] = ((String)te.get(i));
/*     */         }
/*  47 */         this.phasedData.put(str[2], temp);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static List<String> getSNP(ZipFile f, String entryName) throws Exception {
/*  53 */     BufferedReader nxt = 
/*  54 */       new BufferedReader(new InputStreamReader(
/*  55 */       f.getInputStream(f.getEntry(entryName))));
/*  56 */     List<String> snp = new ArrayList();
/*  57 */     String st = "";
/*  58 */     while ((st = nxt.readLine()) != null) {
/*  59 */       String[] str = st.split("\t");
/*  60 */       if ((Integer.parseInt(str[1]) >= mid[0]) && (Integer.parseInt(str[2]) <= mid[1]))
/*  61 */         snp.add(str[3]);
/*     */     }
/*  63 */     nxt.close();
/*  64 */     return snp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  70 */   List<String> sampleID = new ArrayList();
/*     */   
/*  72 */   public void getRawData(File f, int maxInd) throws Exception { ZipFile zf = new ZipFile(f);
/*  73 */     this.rawData = new HashMap();
/*  74 */     this.sampleID = getIndiv(zf, "Samples", Integer.valueOf(0), maxInd);
/*  75 */     List<String> snp = new ArrayList();
/*  76 */     snp = getSNP(zf, "Snps");
/*  77 */     String st = "";
/*  78 */     for (int i = 0; i < this.sampleID.size(); i++) {
/*  79 */       this.rawData.put((String)this.sampleID.get(i), new ArrayList());
/*     */     }
/*  81 */     for (int i = 0; i < snp.size(); i++) {
/*  82 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)snp.get(i)))));
/*  83 */       for (int s = 0; s < this.sampleID.size(); s++) {
/*  84 */         if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  85 */         ((List)this.rawData.get(this.sampleID.get(s))).add(st.split("\\s+")[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   Map<String, List<String>> rawDataDart;
/*  92 */   List<String> sampleIDdart = new ArrayList();
/*     */   
/*  94 */   public void getRawDataDart(File f, int maxInd) throws Exception { ZipFile zf = new ZipFile(f);
/*  95 */     this.rawDataDart = new HashMap();
/*  96 */     this.sampleIDdart = getIndiv(zf, "Samples", Integer.valueOf(0), maxInd);
/*  97 */     List<String> snp = new ArrayList();
/*  98 */     snp = getSNP(zf, "Snps");
/*  99 */     String st = "";
/* 100 */     for (int i = 0; i < this.sampleIDdart.size(); i++) {
/* 101 */       this.rawDataDart.put((String)this.sampleIDdart.get(i), new ArrayList());
/*     */     }
/* 103 */     for (int i = 0; i < snp.size(); i++) {
/* 104 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)snp.get(i)))));
/* 105 */       for (int s = 0; s < this.sampleIDdart.size(); s++) {
/* 106 */         if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/* 107 */         ((List)this.rawDataDart.get(this.sampleIDdart.get(s))).add(st.split("\\s+")[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Map<String, List<Double>> getUncertaintyImpute(File f)
/*     */     throws Exception
/*     */   {
/* 115 */     this.uncertaintyScore = new HashMap();
/* 116 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 117 */     String[] headline = br.readLine().split("\t");
/* 118 */     for (int i = 3; i < headline.length; i += 3) {
/* 119 */       this.uncertaintyScore.put(headline[i], new ArrayList());
/*     */     }
/* 121 */     String st = "";
/* 122 */     int i; for (; (st = br.readLine()) != null; 
/*     */         
/* 124 */         i < headline.length)
/*     */     {
/* 123 */       String[] temp = st.split("\t");
/* 124 */       i = 5; continue;
/* 125 */       ((List)this.uncertaintyScore.get(headline[i])).add(Double.valueOf(Double.parseDouble(temp[i].split(":")[0].split("_")[1])));i += 3;
/*     */     }
/*     */     
/*     */ 
/* 128 */     return this.uncertaintyScore;
/*     */   }
/*     */   
/*     */   public int comparePhaseRaw(String[] phase, List<String> raw, int pos)
/*     */   {
/* 133 */     int[] phasedCount = new int[2];
/* 134 */     int[] rawCount = new int[2];
/* 135 */     for (int c = 0; c < phase.length; c++) {
/* 136 */       if (phase[c].charAt(pos) == 'A') phasedCount[0] += 1; else
/* 137 */         phasedCount[1] += 1;
/* 138 */       if (((String)raw.get(pos)).charAt(c) == 'A') rawCount[0] += 1; else
/* 139 */         rawCount[1] += 1;
/*     */     }
/* 141 */     if ((phasedCount[0] != rawCount[0]) || (phasedCount[1] != rawCount[1])) return 1;
/* 142 */     return 0;
/*     */   }
/*     */   
/*     */   public int heterzygousIndex(String[] phase, List<String> raw, int pos)
/*     */   {
/* 147 */     int[] phasedCount = new int[2];
/* 148 */     int[] rawCount = new int[2];
/* 149 */     for (int c = 0; c < phase.length; c++) {
/* 150 */       if (phase[c].charAt(pos) == 'A') phasedCount[0] += 1; else
/* 151 */         phasedCount[1] += 1;
/* 152 */       if (((String)raw.get(pos)).charAt(c) == 'A') rawCount[0] += 1; else
/* 153 */         rawCount[1] += 1;
/*     */     }
/* 155 */     if ((phasedCount[0] == rawCount[0]) && (phasedCount[1] == rawCount[1]) && (phasedCount[1] == 1) && (phasedCount[0] == 1)) return 1;
/* 156 */     return 0;
/*     */   }
/*     */   
/*     */   public int heterzygousRaw(int CN, List<String> raw, int pos)
/*     */   {
/* 161 */     int[] rawCount = new int[2];
/* 162 */     for (int c = 0; c < CN; c++) {
/* 163 */       if (((String)raw.get(pos)).charAt(c) == 'A') rawCount[0] += 1; else
/* 164 */         rawCount[1] += 1;
/*     */     }
/* 166 */     if ((rawCount[1] == 1) && (rawCount[0] == 1)) return 1;
/* 167 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   Map<String, List<Double>> uncertaintyScore;
/*     */   
/*     */   public int[][] compareUncerImpute()
/*     */   {
/* 175 */     int[][] countError = new int[2][10];
/* 176 */     Arrays.fill(countError[0], 0);
/* 177 */     Arrays.fill(countError[1], 0);
/* 178 */     if (((List)this.rawData.get(this.sampleID.get(0))).size() != ((String[])this.phasedData.get(this.sampleID.get(0)))[0].length()) throw new RuntimeException("no. of snps are different between phased and raw data");
/* 179 */     String idv; int i; for (Iterator<String> id = this.phasedData.keySet().iterator(); id.hasNext(); 
/*     */         
/* 181 */         i < ((List)this.rawDataDart.get(idv)).size())
/*     */     {
/* 180 */       idv = (String)id.next();
/* 181 */       i = 0; continue;
/* 182 */       if (((String)((List)this.rawDataDart.get(idv)).get(i)).equals("NN")) {
/* 183 */         double certainty = ((Double)((List)this.uncertaintyScore.get(idv)).get(i)).doubleValue();
/* 184 */         for (int k = 1; k < 11; k++) {
/* 185 */           if (certainty * 100.0D <= k * 10.0D) {
/* 186 */             countError[1][(k - 1)] += 1;
/* 187 */             break;
/*     */           }
/*     */         }
/* 190 */         if (comparePhaseRaw((String[])this.phasedData.get(idv), (List)this.rawData.get(idv), i) == 1) {
/* 191 */           for (int k = 1; k < 11; k++) {
/* 192 */             if (certainty * 100.0D <= k * 10.0D) {
/* 193 */               countError[0][(k - 1)] += 1;
/* 194 */               break;
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 201 */       else if (!((String)((List)this.rawDataDart.get(idv)).get(i)).equals(((List)this.rawData.get(idv)).get(i))) { throw new RuntimeException("rawData is not consistant");
/*     */       }
/* 181 */       i++;
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
/* 205 */     return countError;
/*     */   }
/*     */   
/*     */ 
/*     */   public double compareImputeHetero()
/*     */   {
/* 211 */     int dartSum = 0;
/* 212 */     int heterozygousSum = 0;
/*     */     String idv;
/* 214 */     int i; for (Iterator<String> id = this.rawDataDart.keySet().iterator(); id.hasNext(); 
/*     */         
/* 216 */         i < ((List)this.rawDataDart.get(idv)).size())
/*     */     {
/* 215 */       idv = (String)id.next();
/* 216 */       i = 0; continue;
/* 217 */       if (((String)((List)this.rawDataDart.get(idv)).get(i)).equals("NN")) {
/* 218 */         dartSum++;
/* 219 */         if (heterzygousRaw(2, (List)this.rawData.get(idv), i) == 1) {
/* 220 */           heterozygousSum++;
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 225 */       else if (!((String)((List)this.rawDataDart.get(idv)).get(i)).equals(((List)this.rawData.get(idv)).get(i))) { throw new RuntimeException("rawData is not consistant");
/*     */       }
/* 216 */       i++;
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
/* 229 */     return 1.0D - heterozygousSum / dartSum;
/*     */   }
/*     */   
/*     */ 
/*     */   public void printCompareUncerImpute(PrintStream ps, int[][] count)
/*     */   {
/* 235 */     int sum = 0;
/* 236 */     int sum0 = 0;
/* 237 */     for (int i = 0; i < count[1].length; i++) {
/* 238 */       sum += count[1][i];
/* 239 */       sum0 += count[0][i];
/*     */     }
/* 241 */     for (int i = 0; i < count[0].length; i++) {
/* 242 */       ps.print((i + 1) / 10.0D + "\t");
/* 243 */       ps.print(count[0][i] / count[1][i] + "\t");
/* 244 */       ps.println(count[1][i] / sum);
/*     */     }
/* 246 */     ps.println(sum0 / sum);
/*     */   }
/*     */   
/*     */   public List<String> getIndiv(ZipFile f, String entryName, Integer column, int maxInd) throws Exception {
/* 250 */     BufferedReader nxt = 
/* 251 */       new BufferedReader(new InputStreamReader(
/* 252 */       f.getInputStream(f.getEntry(entryName))));
/* 253 */     List<String> indiv = new ArrayList();
/* 254 */     String st = "";
/* 255 */     for (int k = 0; ((st = nxt.readLine()) != null) && (k < maxInd); k++) {
/* 256 */       indiv.add(st.split("\\s+")[column.intValue()]);
/*     */     }
/* 258 */     nxt.close();
/* 259 */     return indiv;
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 266 */       DartError de = new DartError();
/*     */       
/*     */ 
/* 269 */       de.getRawData(new File("Xtrue.zip"), 1000);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 276 */       PrintStream ps1 = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("errorRate_p_hetero"))));
/* 277 */       ps1.println("pi: 0.5 0.7 0.9 1.0");
/* 278 */       de.getRawDataDart(new File("Xpi0.5.zip"), 1000);
/* 279 */       ps1.print(de.compareImputeHetero() + "\t");
/* 280 */       de.getRawDataDart(new File("Xpi0.7.zip"), 1000);
/* 281 */       ps1.print(de.compareImputeHetero() + "\t");
/* 282 */       de.getRawDataDart(new File("Xpi0.9.zip"), 1000);
/* 283 */       ps1.print(de.compareImputeHetero() + "\t");
/* 284 */       de.getRawDataDart(new File("Xpi1.zip"), 1000);
/* 285 */       ps1.println(de.compareImputeHetero() + "\t");
/*     */       
/* 287 */       ps1.println("pm: 0.5 0.7 0.9 1.0");
/* 288 */       de.getRawDataDart(new File("Xpm0.5.zip"), 1000);
/* 289 */       ps1.print(de.compareImputeHetero() + "\t");
/* 290 */       de.getRawDataDart(new File("Xpm0.7.zip"), 1000);
/* 291 */       ps1.print(de.compareImputeHetero() + "\t");
/* 292 */       de.getRawDataDart(new File("Xpm0.9.zip"), 1000);
/* 293 */       ps1.print(de.compareImputeHetero() + "\t");
/* 294 */       de.getRawDataDart(new File("Xpm1.zip"), 1000);
/* 295 */       ps1.println(de.compareImputeHetero() + "\t");
/*     */       
/* 297 */       ps1.println("p: 0.5 0.7 0.9 1.0");
/* 298 */       de.getRawDataDart(new File("Xp0.5.zip"), 1000);
/* 299 */       ps1.print(de.compareImputeHetero() + "\t");
/* 300 */       de.getRawDataDart(new File("Xp0.7.zip"), 1000);
/* 301 */       ps1.print(de.compareImputeHetero() + "\t");
/* 302 */       de.getRawDataDart(new File("Xp0.9.zip"), 1000);
/* 303 */       ps1.print(de.compareImputeHetero() + "\t");
/* 304 */       de.getRawDataDart(new File("Xp1.zip"), 1000);
/* 305 */       ps1.println(de.compareImputeHetero() + "\t");
/* 306 */       ps1.close();
/*     */     }
/*     */     catch (Exception exc) {
/* 309 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/DartError.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */