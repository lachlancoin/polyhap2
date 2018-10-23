/*     */ package dataFormat;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ public class ImputationError
/*     */ {
/*     */   List<String> maskSNP;
/*     */   List<String> allSNP;
/*     */   Map<String, String[]> phasedData;
/*     */   Map<String, List<String>> rawData;
/*     */   
/*     */   public void maskSNP(File f) throws Exception
/*     */   {
/*  25 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/*  27 */     this.maskSNP = new ArrayList();
/*  28 */     String st; while ((st = br.readLine()) != null) { String st;
/*  29 */       this.maskSNP.add(st.split("\\s+")[3]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void getAllSNP(File f, File fhapmap, int from, int to)
/*     */     throws Exception
/*     */   {
/*  37 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  38 */     BufferedReader br1 = new BufferedReader(new FileReader(fhapmap));
/*     */     
/*  40 */     SortedMap<Integer, String> allSNPmap = new java.util.TreeMap();
/*  41 */     this.allSNP = new ArrayList();
/*  42 */     String st; while ((st = br1.readLine()) != null) { String st;
/*  43 */       String[] str = st.split("\\s+");
/*  44 */       Integer position = Integer.valueOf(Integer.parseInt(str[1]));
/*  45 */       if ((position.intValue() >= from) && (position.intValue() <= to)) {
/*  46 */         allSNPmap.put(position, str[3]);
/*     */       }
/*     */     }
/*  49 */     while ((st = br.readLine()) != null) {
/*  50 */       String[] str = st.split("\\s+");
/*  51 */       if (!allSNPmap.containsValue(str[3])) {
/*  52 */         Integer position = Integer.valueOf(Integer.parseInt(str[1]));
/*  53 */         if ((position.intValue() >= from) && (position.intValue() <= to) && (str[0].equals("chrX"))) {
/*  54 */           allSNPmap.put(position, str[3]);
/*     */         }
/*     */       }
/*     */     }
/*  58 */     for (Iterator<Integer> is = allSNPmap.keySet().iterator(); is.hasNext();) {
/*  59 */       Integer s = (Integer)is.next();
/*  60 */       this.allSNP.add((String)allSNPmap.get(s));
/*     */     }
/*     */   }
/*     */   
/*     */   public void getPhasedData(File f)
/*     */     throws Exception
/*     */   {
/*  67 */     this.phasedData = new HashMap();
/*  68 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  69 */     String st = br.readLine();
/*  70 */     while (!st.startsWith("#")) {
/*  71 */       st = br.readLine();
/*     */     }
/*  73 */     while (st != null) {
/*  74 */       if (st.startsWith("#")) {
/*  75 */         String[] str = st.split("\\s+");
/*  76 */         List<String> te = new ArrayList();
/*  77 */         st = br.readLine();
/*  78 */         while (!st.startsWith("#")) {
/*  79 */           te.add(st);
/*  80 */           st = br.readLine();
/*  81 */           if (st == null) break;
/*     */         }
/*  83 */         String[] temp = new String[te.size()];
/*  84 */         for (int i = 0; i < temp.length; i++) {
/*  85 */           temp[i] = ((String)te.get(i));
/*     */         }
/*  87 */         this.phasedData.put(str[2], temp);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  94 */   List<String> sampleID = new ArrayList();
/*     */   
/*  96 */   public void getRawData(File f, int maxInd) throws Exception { ZipFile zf = new ZipFile(f);
/*  97 */     this.rawData = new HashMap();
/*  98 */     this.sampleID = getIndiv1(zf, "Samples", Integer.valueOf(0), maxInd);
/*  99 */     String st = "";
/* 100 */     for (int i = 0; i < this.maskSNP.size(); i++)
/*     */     {
/* 102 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)this.maskSNP.get(i)))));
/* 103 */       List<String> temp = new ArrayList();
/* 104 */       for (int s = 0; s < this.sampleID.size(); s++) {
/* 105 */         if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/* 106 */         temp.add(st.split("\\s+")[0]);
/*     */       }
/* 108 */       this.rawData.put((String)this.maskSNP.get(i), temp);
/*     */     }
/*     */   }
/*     */   
/*     */   public void getRawData1(File f, int maxInd) throws Exception {
/* 113 */     ZipFile zf = new ZipFile(f);
/* 114 */     this.rawData = new HashMap();
/* 115 */     this.sampleID = getIndiv1(zf, "Samples", Integer.valueOf(0), maxInd);
/* 116 */     String st = "";
/* 117 */     for (int i = 0; i < this.allSNP.size(); i++) {
/* 118 */       if (zf.getEntry((String)this.allSNP.get(i)) != null) {
/* 119 */         BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)this.allSNP.get(i)))));
/* 120 */         List<String> temp = new ArrayList();
/* 121 */         for (int s = 0; s < this.sampleID.size(); s++) {
/* 122 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/* 123 */           temp.add(st.split("\\s+")[0]);
/*     */         }
/* 125 */         this.rawData.put((String)this.allSNP.get(i), temp);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String checkImputatioResult(int numChr)
/*     */   {
/* 132 */     if (this.allSNP.size() != ((String[])this.phasedData.get(this.sampleID.get(0)))[0].length()) throw new RuntimeException("!!");
/* 133 */     int errorCount = 0;
/* 134 */     int numMask = 0;
/* 135 */     for (int m = 0; m < this.allSNP.size(); m++) {
/* 136 */       String snpName = (String)this.allSNP.get(m);
/* 137 */       if (this.maskSNP.contains(snpName)) {
/* 138 */         numMask++;
/* 139 */         System.out.println(snpName);
/* 140 */         for (int i = 0; i < this.sampleID.size(); i++) {
/* 141 */           String trueGeno = (String)((List)this.rawData.get(snpName)).get(i);
/* 142 */           String imputeGeno = "";
/* 143 */           for (int n = 0; n < numChr; n++) {
/* 144 */             imputeGeno = imputeGeno + ((String[])this.phasedData.get(this.sampleID.get(i)))[n].charAt(m);
/*     */           }
/* 146 */           if (compareGeno(trueGeno, imputeGeno, numChr)) errorCount++;
/*     */         }
/*     */       }
/* 149 */       else if (this.rawData.containsKey(snpName)) {
/* 150 */         for (int i = 0; i < this.sampleID.size(); i++) {
/* 151 */           String trueGeno = (String)((List)this.rawData.get(snpName)).get(i);
/* 152 */           String imputeGeno = "";
/* 153 */           for (int n = 0; n < numChr; n++) {
/* 154 */             imputeGeno = imputeGeno + ((String[])this.phasedData.get(this.sampleID.get(i)))[n].charAt(m);
/*     */           }
/* 156 */           System.out.println(trueGeno + imputeGeno);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 162 */     double rate = errorCount / (numMask * this.sampleID.size());
/* 163 */     return rate + "\t" + numMask + "\t" + this.sampleID.size();
/*     */   }
/*     */   
/*     */   public boolean compareGeno(String geno1, String geno2, int numChr)
/*     */   {
/* 168 */     if (!geno1.equals("NC")) {
/* 169 */       if (numChr == 1) {
/* 170 */         if (!geno1.equals(geno2)) return true;
/* 171 */         return false;
/*     */       }
/*     */       
/* 174 */       if (getSp2GenoIndex(geno1) != getSp2GenoIndex(geno2)) return true;
/* 175 */       return false;
/*     */     }
/*     */     
/* 178 */     return false;
/*     */   }
/*     */   
/* 181 */   List<String> hapSp2 = new ArrayList();
/*     */   
/* 183 */   public void getHapSp2() { this.hapSp2.add("AA");
/* 184 */     this.hapSp2.add("AB");
/* 185 */     this.hapSp2.add("BA");
/* 186 */     this.hapSp2.add("BB");
/*     */   }
/*     */   
/*     */   public int getSp2GenoIndex(String aa) {
/* 190 */     int i = -1;
/* 191 */     while (i < this.hapSp2.size()) {
/* 192 */       i++;
/* 193 */       if (((String)this.hapSp2.get(i)).equals(aa)) break;
/*     */     }
/* 195 */     if (i > 1) return i - 1;
/* 196 */     return i;
/*     */   }
/*     */   
/*     */   public List<String> getIndiv(ZipFile f, String entryName, Integer column, int maxInd) throws Exception
/*     */   {
/* 201 */     BufferedReader nxt = 
/* 202 */       new BufferedReader(new InputStreamReader(
/* 203 */       f.getInputStream(f.getEntry(entryName))));
/* 204 */     List<String> indiv = new ArrayList();
/* 205 */     String st = "";
/* 206 */     for (int k = 0; ((st = nxt.readLine()) != null) && (k < maxInd); k++) {
/* 207 */       indiv.add(st.split("\\s+")[column.intValue()]);
/*     */     }
/* 209 */     nxt.close();
/* 210 */     return indiv;
/*     */   }
/*     */   
/*     */   public List<String> getIndiv1(ZipFile f, String entryName, Integer column, int maxInd) throws Exception {
/* 214 */     BufferedReader nxt = 
/* 215 */       new BufferedReader(new InputStreamReader(
/* 216 */       f.getInputStream(f.getEntry(entryName))));
/* 217 */     List<String> indiv = new ArrayList();
/* 218 */     String st = "";
/* 219 */     for (int k = 0; ((st = nxt.readLine()) != null) && (k < maxInd); k++) {
/* 220 */       indiv.add(st.split("#")[column.intValue()] + "|");
/*     */     }
/* 222 */     nxt.close();
/* 223 */     return indiv;
/*     */   }
/*     */   
/*     */   public void printImputeErrorRate(PrintStream ps, int numChr)
/*     */   {
/* 228 */     ps.println(checkImputatioResult(numChr));
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 233 */       ImputationError ie = new ImputationError();
/* 234 */       ie.getHapSp2();
/* 235 */       ie.maskSNP(new File("marked_1M_" + args[0]));
/* 236 */       ie.getAllSNP(new File("build36_1M_" + args[0]), new File("build36hapmap.txt"), 87680935, 92861863);
/* 237 */       ie.getRawData1(new File(args[1] + ".zip"), Integer.parseInt(args[3]));
/* 238 */       ie.getPhasedData(new File("phased2.p" + args[0]));
/* 239 */       PrintStream ps = new PrintStream(new java.io.BufferedOutputStream(new java.io.FileOutputStream(new File("errorRate_" + args[0]))));
/* 240 */       ie.printImputeErrorRate(ps, Integer.parseInt(args[2]));
/* 241 */       ps.close();
/*     */ 
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/* 246 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/ImputationError.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */