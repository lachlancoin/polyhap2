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
/*     */ import java.util.Set;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SEtrisomy
/*     */ {
/*  23 */   static int[] mid = { 46351620, 46383462 };
/*     */   Map<String, List<Double>> uncertaintyScore;
/*     */   Map<String, List> rawData;
/*     */   
/*     */   public static List<String> getIndiv(ZipFile f, String entryName, Integer column) throws Exception {
/*  28 */     BufferedReader nxt = 
/*  29 */       new BufferedReader(new InputStreamReader(
/*  30 */       f.getInputStream(f.getEntry(entryName))));
/*  31 */     List<String> indiv = new ArrayList();
/*  32 */     String st = "";
/*  33 */     while ((st = nxt.readLine()) != null) {
/*  34 */       indiv.add(st.split("\t")[column.intValue()]);
/*     */     }
/*  36 */     nxt.close();
/*  37 */     return indiv;
/*     */   }
/*     */   
/*     */   public static List<String> getSNP(ZipFile f, String entryName) throws Exception {
/*  41 */     BufferedReader nxt = 
/*  42 */       new BufferedReader(new InputStreamReader(
/*  43 */       f.getInputStream(f.getEntry(entryName))));
/*  44 */     List<String> snp = new ArrayList();
/*  45 */     String st = "";
/*  46 */     while ((st = nxt.readLine()) != null) {
/*  47 */       String[] str = st.split("\t");
/*  48 */       if ((Integer.parseInt(str[1]) >= mid[0]) && (Integer.parseInt(str[2]) <= mid[1]))
/*  49 */         snp.add(str[3].trim());
/*     */     }
/*  51 */     nxt.close();
/*  52 */     return snp;
/*     */   }
/*     */   
/*     */   public Map<String, List<Double>> getUncertainty(File f)
/*     */     throws Exception
/*     */   {
/*  58 */     this.uncertaintyScore = new HashMap();
/*  59 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  60 */     String[] headline = br.readLine().split("\t");
/*  61 */     for (int i = 6; i < headline.length; i += 3) {
/*  62 */       this.uncertaintyScore.put(headline[i], new ArrayList());
/*     */     }
/*  64 */     String st = "";
/*  65 */     int i; for (; (st = br.readLine()) != null; 
/*     */         
/*  67 */         i < headline.length)
/*     */     {
/*  66 */       String[] temp = st.split("\t");
/*  67 */       i = 7; continue;
/*  68 */       ((List)this.uncertaintyScore.get(headline[i])).add(Double.valueOf(Double.parseDouble(temp[i])));i += 3;
/*     */     }
/*     */     
/*     */ 
/*  71 */     return this.uncertaintyScore;
/*     */   }
/*     */   
/*     */   List<String> indiv;
/*     */   Map<String[], String[]> phasedData;
/*     */   public void getRawData(File f) throws Exception
/*     */   {
/*  78 */     ZipFile zf = new ZipFile(f);
/*  79 */     this.rawData = new HashMap();
/*  80 */     this.indiv = new ArrayList();
/*  81 */     this.indiv = getIndiv(zf, "Samples", Integer.valueOf(0));
/*  82 */     List<String> snp = new ArrayList();
/*  83 */     snp = getSNP(zf, "Snps");
/*  84 */     String st = "";
/*  85 */     for (int i = 0; i < snp.size(); i++) {
/*  86 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)snp.get(i)))));
/*  87 */       if (this.rawData.isEmpty()) {
/*  88 */         for (int s = 0; s < this.indiv.size(); s++) {
/*  89 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  90 */           List<String> temp = new ArrayList();
/*  91 */           temp.add(st);
/*  92 */           this.rawData.put((String)this.indiv.get(s), temp);
/*     */         }
/*  94 */         if ((st = nxt.readLine()) != null) throw new RuntimeException("!!");
/*     */       }
/*     */       else {
/*  97 */         for (int s = 0; s < this.indiv.size(); s++) {
/*  98 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  99 */           List<String> temp = new ArrayList();
/* 100 */           temp.add(st);
/* 101 */           ((List)this.rawData.get(this.indiv.get(s))).add(temp);
/*     */         }
/* 103 */         if ((st = nxt.readLine()) != null) throw new RuntimeException("!!");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void checkConsisPC()
/*     */   {
/* 110 */     int count = 0;
/* 111 */     for (int i = 0; i < this.indiv.size(); i++) {
/* 112 */       String sts = (String)this.indiv.get(i);
/* 113 */       String[] st = sts.split("-");
/* 114 */       if ((st[1].equals("P")) || (st[1].equals("3"))) {
/* 115 */         for (int m = 0; m < ((List)this.rawData.get(sts)).size(); m++) {
/* 116 */           String oneMarC = null;
/* 117 */           String oneMarF = null;
/* 118 */           String oneMarM = null;
/* 119 */           for (Iterator<String> it = this.rawData.keySet().iterator(); it.hasNext();) {
/* 120 */             String nts = (String)it.next();
/* 121 */             String[] nt = nts.split("-");
/* 122 */             if (nt[0].equals(st[0])) {
/* 123 */               if ((nt[1].equals("P")) || (nt[1].equals("3"))) {
/* 124 */                 oneMarC = ((List)this.rawData.get(nts)).get(m).toString();
/*     */               }
/* 126 */               else if ((nt[1].equals("F")) || (nt[1].equals("2"))) {
/* 127 */                 oneMarF = ((List)this.rawData.get(nts)).get(m).toString();
/*     */               }
/* 129 */               else if ((nt[1].equals("M")) || (nt[1].equals("1"))) {
/* 130 */                 oneMarM = ((List)this.rawData.get(nts)).get(m).toString();
/*     */               } else
/* 132 */                 throw new RuntimeException("!!");
/*     */             }
/*     */           }
/* 135 */           if ((oneMarC != null) && (oneMarF != null) && (oneMarM != null)) {
/* 136 */             for (int k = 0; k < oneMarC.length(); k++) {
/* 137 */               String oneAllele = oneMarC.substring(k, k + 1);
/* 138 */               if ((!oneAllele.equals("N")) && 
/* 139 */                 (!oneMarF.contains(oneAllele)) && (!oneMarM.contains(oneAllele)) && 
/* 140 */                 (!oneMarF.contains("N")) && (!oneMarM.contains("N"))) {
/* 141 */                 System.out.println(st[0]);
/* 142 */                 System.out.println(m);
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 149 */           if ((oneMarC != null) && (oneMarF != null) && (oneMarM != null)) {
/* 150 */             if (oneMarC.contains("N")) count++;
/* 151 */             if (oneMarF.contains("N")) count++;
/* 152 */             if (oneMarM.contains("N")) { count++;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 158 */     System.out.println("missing:" + count);
/*     */   }
/*     */   
/*     */   List<String[]> indIdC;
/*     */   List<String[]> indIdP;
/*     */   Map<String[], String[]> phasedDataP;
/*     */   List<Integer> switchErrorCount;
/*     */   public void getPhasedData(File f) throws Exception {
/* 166 */     this.phasedData = new HashMap();
/* 167 */     this.indIdC = new ArrayList();
/* 168 */     this.indIdP = new ArrayList();
/* 169 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 170 */     String st = br.readLine();
/* 171 */     while (!st.startsWith("#")) {
/* 172 */       st = br.readLine();
/*     */     }
/* 174 */     while (st != null) {
/* 175 */       if (st.startsWith("#")) {
/* 176 */         String[] str = st.split("\\s+");
/* 177 */         List<String> te = new ArrayList();
/* 178 */         st = br.readLine();
/* 179 */         while (!st.startsWith("#")) {
/* 180 */           te.add(st);
/* 181 */           st = br.readLine();
/* 182 */           if (st == null) break;
/*     */         }
/* 184 */         String[] temp = new String[te.size()];
/* 185 */         for (int i = 0; i < temp.length; i++) {
/* 186 */           temp[i] = ((String)te.get(i));
/*     */         }
/* 188 */         String[] s = str[2].split("-");
/* 189 */         this.phasedData.put(s, temp);
/* 190 */         if ((s[1].charAt(0) == 'P') || (s[1].charAt(0) == '3')) this.indIdC.add(s); else {
/* 191 */           this.indIdP.add(s);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void getPhasedDataP(File f)
/*     */     throws Exception
/*     */   {
/* 200 */     this.phasedDataP = new HashMap();
/* 201 */     this.indIdP = new ArrayList();
/* 202 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 203 */     String st = br.readLine();
/* 204 */     while (!st.startsWith("#")) {
/* 205 */       st = br.readLine();
/*     */     }
/* 207 */     while (st != null) {
/* 208 */       if (st.startsWith("#")) {
/* 209 */         String[] str = st.split("\\s+");
/* 210 */         List<String> te = new ArrayList();
/* 211 */         st = br.readLine();
/* 212 */         while (!st.startsWith("#")) {
/* 213 */           te.add(st);
/* 214 */           st = br.readLine();
/* 215 */           if (st == null) break;
/*     */         }
/* 217 */         String[] temp = new String[te.size()];
/* 218 */         for (int i = 0; i < temp.length; i++) {
/* 219 */           temp[i] = ((String)te.get(i));
/*     */         }
/* 221 */         this.phasedDataP.put(str[2].split("-"), temp);
/* 222 */         this.indIdP.add(str[2].split("-"));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 228 */   Map<String[], List> indexMatched = new HashMap();
/* 229 */   List<String[]> indexUnmatch = new ArrayList();
/* 230 */   int count = 0;
/*     */   
/* 232 */   public void matchParentsChildren() { Map<Integer, String> hapParents = new HashMap();
/* 233 */     for (int i = 0; i < this.indIdC.size(); i++) {
/* 234 */       hapParents.clear();
/* 235 */       for (Iterator<String[]> it = this.phasedData.keySet().iterator(); it.hasNext();) {
/* 236 */         String[] nt = (String[])it.next();
/* 237 */         if ((nt[0].equals(((String[])this.indIdC.get(i))[0])) && (nt[1].charAt(0) != 'P') && (nt[1].charAt(0) != '3')) {
/* 238 */           if ((nt[1].charAt(0) == 'M') || (nt[1].charAt(0) == '1')) {
/* 239 */             for (int k = 0; k < ((String[])this.phasedData.get(nt)).length; k++) {
/* 240 */               hapParents.put(Integer.valueOf(k + 1), ((String[])this.phasedData.get(nt))[k]);
/*     */             }
/*     */             
/*     */           } else {
/* 244 */             for (int k = 0; k < ((String[])this.phasedData.get(nt)).length; k++) {
/* 245 */               hapParents.put(Integer.valueOf(k + 3), ((String[])this.phasedData.get(nt))[k]);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 250 */       if (hapParents.size() >= 4) {
/* 251 */         this.count += 1;
/* 252 */         List<Integer> indexFM = new ArrayList();
/* 253 */         for (int c = 0; c < ((String[])this.phasedData.get(this.indIdC.get(i))).length; c++) {
/* 254 */           String oneChr = ((String[])this.phasedData.get(this.indIdC.get(i)))[c];
/* 255 */           for (Iterator<Integer> it = hapParents.keySet().iterator(); it.hasNext();) {
/* 256 */             Integer nt = (Integer)it.next();
/* 257 */             String oneChrPa = (String)hapParents.get(nt);
/* 258 */             if (getNumDisMatch(oneChr, oneChrPa) == 0)
/*     */             {
/*     */ 
/*     */ 
/* 262 */               indexFM.add(nt);
/* 263 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 268 */         if (indexFM.size() == ((String[])this.phasedData.get(this.indIdC.get(i))).length) { this.indexMatched.put((String[])this.indIdC.get(i), indexFM);
/*     */         } else {
/* 270 */           Integer errorPos = null;
/* 271 */           int numPos = 0;
/* 272 */           for (int c = 0; c < ((String[])this.phasedData.get(this.indIdC.get(i))).length; c++) {
/* 273 */             String oneChr = ((String[])this.phasedData.get(this.indIdC.get(i)))[c];
/* 274 */             for (Iterator<Integer> it = hapParents.keySet().iterator(); it.hasNext();) {
/* 275 */               Integer nt = (Integer)it.next();
/* 276 */               String oneChrPa = (String)hapParents.get(nt);
/* 277 */               int[] count = getNumDisMatch1(oneChr, oneChrPa);
/* 278 */               if (count[0] == 1) {
/* 279 */                 if (errorPos == null) { errorPos = Integer.valueOf(count[1]); break;
/*     */                 }
/* 281 */                 if (count[1] == errorPos.intValue()) break; numPos++;
/*     */                 
/* 283 */                 break;
/*     */               }
/*     */             }
/*     */           }
/* 287 */           if (numPos == 0) { this.indexUnmatch.add((String[])this.indIdC.get(i));
/*     */           } else {
/* 289 */             System.out.println(((String[])this.indIdC.get(i))[0]);
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 294 */         System.out.println("imcompletely family:" + ((String[])this.indIdC.get(i))[0]);
/*     */       } }
/* 296 */     System.out.println("complete family:" + this.count);
/*     */   }
/*     */   
/*     */   public int[] countIndex(List<Integer> indexFM)
/*     */   {
/* 301 */     int[] count = new int[2];
/* 302 */     for (int i = 0; i < indexFM.size(); i++) {
/* 303 */       if ((((Integer)indexFM.get(i)).intValue() == 1) || (((Integer)indexFM.get(i)).intValue() == 2)) count[0] += 1;
/*     */     }
/* 305 */     for (int i = 0; i < indexFM.size(); i++) {
/* 306 */       if ((((Integer)indexFM.get(i)).intValue() == 3) || (((Integer)indexFM.get(i)).intValue() == 4)) count[1] += 1;
/*     */     }
/* 308 */     return count;
/*     */   }
/*     */   
/*     */   public int getNumDisMatch(String s1, String s2) {
/* 312 */     if (s1.length() != s2.length()) throw new RuntimeException("!!");
/* 313 */     int count = 0;
/* 314 */     for (int i = 0; i < s1.length(); i++) {
/* 315 */       if (s1.charAt(i) != s2.charAt(i)) count++;
/*     */     }
/* 317 */     return count;
/*     */   }
/*     */   
/*     */   public int[] getNumDisMatch1(String s1, String s2) {
/* 321 */     if (s1.length() != s2.length()) throw new RuntimeException("!!");
/* 322 */     int[] count = new int[2];
/* 323 */     for (int i = 0; i < s1.length(); i++) {
/* 324 */       if (s1.charAt(i) != s2.charAt(i)) {
/* 325 */         count[0] += 1;
/* 326 */         count[1] = i;
/*     */       }
/*     */     }
/* 329 */     return count;
/*     */   }
/*     */   
/*     */   public void printImputeErrorRate(PrintStream ps, double rate) {
/* 333 */     ps.println(rate);
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try {
/* 339 */       SEtrisomy se = new SEtrisomy();
/* 340 */       se.getPhasedData(new File(args[0] + args[1], "phased2.TXT_1"));
/*     */       
/* 342 */       se.getRawData(new File(args[0] + args[1], "21.zip"));
/* 343 */       se.checkConsisPC();
/* 344 */       se.matchParentsChildren();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 369 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/SEtrisomy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */