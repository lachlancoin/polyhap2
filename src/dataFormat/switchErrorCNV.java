/*      */ package dataFormat;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FileReader;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.zip.ZipFile;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class switchErrorCNV
/*      */ {
/*   28 */   static int[] mid = { 19502220, 40491848 };
/*      */   
/*      */   Map<String, List<Integer>> missingIndex;
/*      */   
/*      */   Map<String, List<Double>> uncertaintyScore;
/*      */   
/*      */   Map<String, List<Character[]>> rawData;
/*      */   Map<String, List<Integer>> rawDataMissing;
/*      */   Map<String, String[]> phasedData;
/*      */   List<String> phasedIndID;
/*      */   
/*      */   public List<String> getIndiv(ZipFile f, String entryName, Integer column)
/*      */     throws Exception
/*      */   {
/*   42 */     BufferedReader nxt = 
/*   43 */       new BufferedReader(new InputStreamReader(
/*   44 */       f.getInputStream(f.getEntry(entryName))));
/*   45 */     List<String> indiv = new ArrayList();
/*   46 */     String st = "";
/*   47 */     for (int k = 0; (st = nxt.readLine()) != null; k++) {
/*   48 */       indiv.add(st.split("\t")[column.intValue()]);
/*      */     }
/*   50 */     nxt.close();
/*   51 */     return indiv;
/*      */   }
/*      */   
/*      */   public static List<String> getSNP(ZipFile f, String entryName) throws Exception {
/*   55 */     BufferedReader nxt = 
/*   56 */       new BufferedReader(new InputStreamReader(
/*   57 */       f.getInputStream(f.getEntry(entryName))));
/*   58 */     List<String> snp = new ArrayList();
/*   59 */     String st = "";
/*   60 */     while ((st = nxt.readLine()) != null) {
/*   61 */       String[] str = st.split("\t");
/*   62 */       if ((Integer.parseInt(str[1]) >= mid[0]) && (Integer.parseInt(str[2]) <= mid[1]))
/*   63 */         snp.add(str[3]);
/*      */     }
/*   65 */     nxt.close();
/*   66 */     return snp;
/*      */   }
/*      */   
/*      */   public void getMissingIndex(File f)
/*      */     throws Exception
/*      */   {
/*   72 */     this.missingIndex = new HashMap();
/*   73 */     Map<Integer, Integer> loc = new HashMap();
/*   74 */     ZipFile zf = new ZipFile(f);
/*   75 */     String st = "";
/*   76 */     BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry("Snps"))));
/*   77 */     int count = 0;
/*   78 */     while ((st = nxt.readLine()) != null) {
/*   79 */       String[] str = st.split("\t");
/*   80 */       if ((Integer.parseInt(str[1]) >= mid[0]) && (Integer.parseInt(str[2]) <= mid[1])) {
/*   81 */         loc.put(Integer.valueOf(Integer.parseInt(str[1])), Integer.valueOf(count));
/*   82 */         count++;
/*      */       }
/*      */     }
/*      */     
/*   86 */     nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry("missingIndex"))));
/*   87 */     String[] str; int i; for (; (st = nxt.readLine()) != null; 
/*      */         
/*      */ 
/*   90 */         i < str.length)
/*      */     {
/*   88 */       str = st.split("\\s+");
/*   89 */       this.missingIndex.put(str[0], new ArrayList());
/*   90 */       i = 1; continue;
/*   91 */       int temp = Integer.parseInt(str[i]);
/*   92 */       if (loc.containsKey(Integer.valueOf(temp))) {
/*   93 */         ((List)this.missingIndex.get(str[0])).add((Integer)loc.get(Integer.valueOf(temp)));
/*      */       }
/*   90 */       i++;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Map<String, List<Double>> getUncertainty(File f)
/*      */     throws Exception
/*      */   {
/*  104 */     this.uncertaintyScore = new HashMap();
/*  105 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  106 */     String[] headline = br.readLine().split("\t");
/*  107 */     for (int i = 3; i < headline.length; i += 3) {
/*  108 */       this.uncertaintyScore.put(headline[i].replace(" ", ""), new ArrayList());
/*      */     }
/*  110 */     String st = "";
/*  111 */     int i; for (; (st = br.readLine()) != null; 
/*      */         
/*  113 */         i < headline.length)
/*      */     {
/*  112 */       String[] temp = st.split("\t");
/*  113 */       i = 4; continue;
/*  114 */       ((List)this.uncertaintyScore.get(headline[i].replace(" ", ""))).add(Double.valueOf(Double.parseDouble(temp[i])));i += 3;
/*      */     }
/*      */     
/*      */ 
/*  117 */     return this.uncertaintyScore;
/*      */   }
/*      */   
/*      */   public Map<String, List<Double>> getUncertaintyImpute(File f) throws Exception
/*      */   {
/*  122 */     this.uncertaintyScore = new HashMap();
/*  123 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  124 */     String[] headline = br.readLine().split("\t");
/*  125 */     for (int i = 3; i < headline.length; i += 3) {
/*  126 */       this.uncertaintyScore.put(headline[i], new ArrayList());
/*      */     }
/*  128 */     String st = "";
/*  129 */     int i; for (; (st = br.readLine()) != null; 
/*      */         
/*  131 */         i < headline.length)
/*      */     {
/*  130 */       String[] temp = st.split("\t");
/*  131 */       i = 5; continue;
/*  132 */       ((List)this.uncertaintyScore.get(headline[i])).add(Double.valueOf(Double.parseDouble(temp[i].split(":")[0].split("_")[1])));i += 3;
/*      */     }
/*      */     
/*      */ 
/*  135 */     return this.uncertaintyScore;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void getRawData(File f)
/*      */     throws Exception
/*      */   {
/*  144 */     ZipFile zf = new ZipFile(f);
/*  145 */     this.rawData = new HashMap();
/*      */     
/*  147 */     List<String> indiv = new ArrayList();
/*  148 */     indiv = getIndiv(zf, "Samples", Integer.valueOf(0));
/*  149 */     List<String> snp = new ArrayList();
/*  150 */     snp = getSNP(zf, "Snps");
/*  151 */     String st = "";
/*  152 */     int count = 0;
/*  153 */     for (int i = 0; i < snp.size(); i++) {
/*  154 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)snp.get(i)))));
/*  155 */       List<Integer> temp2; if (this.rawData.isEmpty()) {
/*  156 */         for (int s = 0; s < indiv.size(); s++) {
/*  157 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  158 */           Character[] temp = new Character[st.length()];
/*  159 */           for (int k = 0; k < temp.length; k++) {
/*  160 */             temp[k] = Character.valueOf(st.charAt(k));
/*      */           }
/*  162 */           List<Character[]> temp1 = new ArrayList();
/*  163 */           temp2 = new ArrayList();
/*  164 */           temp1.add(temp);
/*  165 */           this.rawData.put((String)indiv.get(s), temp1);
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*  174 */         for (int s = 0; s < indiv.size(); s++) {
/*  175 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  176 */           Character[] temp = new Character[st.length()];
/*  177 */           for (int k = 0; k < temp.length; k++) {
/*  178 */             temp[k] = Character.valueOf(st.charAt(k));
/*  179 */             if (st.charAt(k) == 'N') {
/*  180 */               temp2 = 0;
/*      */             }
/*      */           }
/*  183 */           ((List)this.rawData.get(indiv.get(s))).add(temp);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void getCountCN(File f, PrintStream ps)
/*      */     throws Exception
/*      */   {
/*  197 */     ZipFile zf = new ZipFile(f);
/*  198 */     List<String> indiv = new ArrayList();
/*  199 */     indiv = getIndiv(zf, "Samples", Integer.valueOf(0));
/*  200 */     List<String> snp = new ArrayList();
/*  201 */     snp = getSNP(zf, "Snps");
/*  202 */     String st = "";
/*  203 */     int[] count = new int[7];
/*  204 */     int[] countHeterozygous = new int[7];
/*  205 */     int countAAAB_BBBA = 0;
/*  206 */     for (int i = 0; i < snp.size(); i++) {
/*  207 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)snp.get(i)))));
/*  208 */       for (int s = 0; s < indiv.size(); s++) {
/*  209 */         if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  210 */         String str = st.replaceAll("_", "");
/*  211 */         count[str.length()] += 1;
/*  212 */         int[] countAB = new int[2];
/*  213 */         for (int k = 0; k < str.length(); k++) {
/*  214 */           if (str.charAt(k) == 'A') {
/*  215 */             countAB[0] += 1;
/*      */           }
/*  217 */           else if (str.charAt(k) == 'B') {
/*  218 */             countAB[1] += 1;
/*      */           } else
/*  220 */             throw new RuntimeException("!!");
/*      */         }
/*  222 */         if ((countAB[0] != 0) && (countAB[1] != 0)) {
/*  223 */           countHeterozygous[str.length()] += 1;
/*      */         }
/*  225 */         if ((countAB[0] == 3) && (countAB[1] == 1)) countAAAB_BBBA++;
/*  226 */         if ((countAB[0] == 1) && (countAB[1] == 3)) { countAAAB_BBBA++;
/*      */         }
/*      */       }
/*      */     }
/*  230 */     int c3 = 0;
/*  231 */     int c4 = 0;
/*      */     
/*      */ 
/*  234 */     for (int s = 0; s < indiv.size(); s++) {
/*  235 */       for (int i = 0; i < ((String[])this.phasedData.get(indiv.get(s)))[0].length(); i++) {
/*  236 */         List<Character> phase = new ArrayList();
/*  237 */         List<Character> raw = new ArrayList();
/*  238 */         for (int k = 0; k < 2; k++) {
/*  239 */           phase.add(Character.valueOf(((String[])this.phasedData.get(indiv.get(s)))[k].charAt(i)));
/*  240 */           raw.add(((Character[])((List)this.rawData.get(indiv.get(s))).get(i))[k]);
/*      */         }
/*  242 */         if ((!phase.containsAll(raw)) || (!raw.containsAll(phase))) {
/*  243 */           List<String> temp = new ArrayList();
/*  244 */           temp.add(raw.get(0) + phase.get(0));
/*  245 */           temp.add(raw.get(1) + phase.get(1));
/*  246 */           if (getFromToCopies(temp) == 33) { c3++;
/*  247 */           } else if (getFromToCopies(temp) == 44) { c4++;
/*      */           }
/*      */           else
/*  250 */             throw new RuntimeException("!!");
/*      */         }
/*      */       }
/*      */     }
/*  254 */     ps.println("CN: " + count[0] + "\t" + count[1] + "\t" + count[2] + "\t" + count[3] + "\t" + count[4]);
/*  255 */     ps.println("Heterozygous: " + countHeterozygous[0] + "\t" + countHeterozygous[1] + "\t" + 
/*  256 */       countHeterozygous[2] + "\t" + countHeterozygous[3] + "\t" + countHeterozygous[4]);
/*  257 */     ps.println("Prediction Error: c3: " + c3 + "\t+ c4: " + c4);
/*  258 */     ps.println("countAAAB_BBBA: " + countAAAB_BBBA);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void getPhasedData(File f)
/*      */     throws Exception
/*      */   {
/*  268 */     this.phasedData = new HashMap();
/*  269 */     this.phasedIndID = new ArrayList();
/*  270 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  271 */     String st = br.readLine();
/*  272 */     while (!st.startsWith("#")) {
/*  273 */       st = br.readLine();
/*      */     }
/*  275 */     while (st != null) {
/*  276 */       if (st.startsWith("#")) {
/*  277 */         String[] str = st.split("\\s+");
/*  278 */         List<String> te = new ArrayList();
/*  279 */         st = br.readLine();
/*  280 */         while (!st.startsWith("#")) {
/*  281 */           te.add(st);
/*  282 */           st = br.readLine();
/*  283 */           if (st == null) break;
/*      */         }
/*  285 */         String[] temp = new String[te.size()];
/*  286 */         for (int i = 0; i < temp.length; i++) {
/*  287 */           temp[i] = ((String)te.get(i));
/*      */         }
/*  289 */         this.phasedData.put(str[2].replace("|", ""), temp);
/*  290 */         this.phasedIndID.add(str[2].replace('|', ' '));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void getPhasedDataCNVhap(File f, int ploidy) throws Exception
/*      */   {
/*  297 */     this.phasedData = new HashMap();
/*  298 */     this.phasedIndID = new ArrayList();
/*  299 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  300 */     String st = br.readLine();
/*  301 */     while (!st.startsWith("#")) {
/*  302 */       st = br.readLine();
/*      */     }
/*  304 */     while (st != null) {
/*  305 */       if (st.startsWith("#")) {
/*  306 */         String[] temp = new String[ploidy];
/*  307 */         String[] str = st.split("\\s+");
/*  308 */         for (int cn = 0; cn < ploidy; cn++) {
/*  309 */           List<String> te = new ArrayList();
/*  310 */           st = br.readLine();
/*  311 */           while (!st.startsWith("#")) {
/*  312 */             te.add(st);
/*  313 */             st = br.readLine();
/*  314 */             if (st == null) break;
/*      */           }
/*  316 */           String afterCode = "";
/*  317 */           for (int m = 0; m < ((String)te.get(0)).length(); m++) {
/*  318 */             List<Character> geno = new ArrayList();
/*  319 */             for (int m1 = 0; m1 < te.size(); m1++) {
/*  320 */               geno.add(Character.valueOf(((String)te.get(m1)).charAt(m)));
/*      */             }
/*  322 */             afterCode = afterCode + codeDuplicate(geno);
/*      */           }
/*  324 */           if (afterCode == "") throw new RuntimeException("!!");
/*  325 */           temp[cn] = afterCode;
/*      */         }
/*  327 */         this.phasedData.put(str[2].split(":")[0].replace("|", ""), temp);
/*  328 */         this.phasedIndID.add(str[2].split(":")[0].replace('|', ' '));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  336 */   int errorPredictGenotype = 0;
/*      */   
/*  338 */   public List<Integer> getHeteroIndexCode(String indivID, int noCopy) { List<Integer> heteroIndex = new ArrayList();
/*  339 */     for (int i = 1501; i < ((String[])this.phasedData.get(indivID))[0].length(); i++) {
/*  340 */       List<Character> phase = new ArrayList();
/*  341 */       List<Character> raw = new ArrayList();
/*  342 */       if (noCopy == 2) {
/*  343 */         for (int k = 0; k < 2; k++) {
/*  344 */           phase.add(Character.valueOf(((String[])this.phasedData.get(indivID))[k].charAt(i)));
/*  345 */           raw.add(((Character[])((List)this.rawData.get(indivID)).get(i))[k]);
/*      */         }
/*  347 */         if ((phase.containsAll(raw)) && (raw.containsAll(phase)))
/*      */         {
/*      */ 
/*  350 */           if (phase.get(0) != phase.get(1)) {
/*  351 */             heteroIndex.add(Integer.valueOf(i));
/*      */           }
/*      */         }
/*      */       }
/*  355 */       if (noCopy == 3) {
/*  356 */         char a = ((String[])this.phasedData.get(indivID))[0].charAt(i);
/*  357 */         String heter = null;
/*  358 */         for (int k = 1; k < noCopy; k++) {
/*  359 */           if (((String[])this.phasedData.get(indivID))[k].charAt(i) != a) heter = "yes";
/*      */         }
/*  361 */         if (heter != null) heteroIndex.add(Integer.valueOf(i));
/*      */       }
/*      */     }
/*  364 */     return heteroIndex;
/*      */   }
/*      */   
/*      */   public List<Integer> getHeteroIndex(String indivID, int no_copies)
/*      */   {
/*  369 */     List<Integer> heteroIndex = new ArrayList();
/*  370 */     for (int i = 0; i < ((String[])this.phasedData.get(indivID))[0].length(); i++) {
/*  371 */       char a = ((String[])this.phasedData.get(indivID))[0].charAt(i);
/*  372 */       String heter = null;
/*  373 */       for (int k = 1; k < no_copies; k++) {
/*  374 */         if (((String[])this.phasedData.get(indivID))[k].charAt(i) != a) heter = "yes";
/*      */       }
/*  376 */       if (heter != null) heteroIndex.add(Integer.valueOf(i));
/*      */     }
/*  378 */     return heteroIndex;
/*      */   }
/*      */   
/*      */ 
/*  382 */   List<String> hapSp2 = new ArrayList();
/*      */   
/*  384 */   public void getHapSp2() { this.hapSp2.add("AA");
/*  385 */     this.hapSp2.add("AB");
/*  386 */     this.hapSp2.add("BA");
/*  387 */     this.hapSp2.add("BB");
/*      */   }
/*      */   
/*  390 */   List<String> hapSp3 = new ArrayList();
/*  391 */   Map<Integer, List<Integer>> group = new HashMap();
/*  392 */   List<Integer> confusedGroup1 = new ArrayList();
/*  393 */   List<Integer> confusedGroup2 = new ArrayList();
/*  394 */   List<Integer> confusedGroup3 = new ArrayList();
/*      */   
/*  396 */   public void getHapSp3() { this.hapSp3.add("AAA");
/*  397 */     this.hapSp3.add("AAB");this.hapSp3.add("ABA");this.hapSp3.add("BAA");
/*  398 */     this.hapSp3.add("ABB");this.hapSp3.add("BAB");this.hapSp3.add("BBA");
/*  399 */     this.hapSp3.add("BBB");
/*  400 */     this.hapSp3.add("AA_");this.hapSp3.add("A_A");this.hapSp3.add("_AA");
/*  401 */     this.hapSp3.add("AB_");this.hapSp3.add("A_B");this.hapSp3.add("_AB");
/*  402 */     this.hapSp3.add("BA_");this.hapSp3.add("B_A");this.hapSp3.add("_BA");
/*  403 */     this.hapSp3.add("BB_");this.hapSp3.add("B_B");this.hapSp3.add("_BB");
/*  404 */     this.hapSp3.add("A__");this.hapSp3.add("_A_");this.hapSp3.add("__A");
/*  405 */     this.hapSp3.add("B__");this.hapSp3.add("_B_");this.hapSp3.add("__B");
/*  406 */     this.hapSp3.add("___");
/*  407 */     List<Integer> temp1 = new ArrayList();
/*  408 */     List<Integer> temp2 = new ArrayList();
/*  409 */     List<Integer> temp3 = new ArrayList();
/*  410 */     List<Integer> temp4 = new ArrayList();
/*  411 */     List<Integer> temp5 = new ArrayList();
/*  412 */     List<Integer> temp6 = new ArrayList();
/*  413 */     List<Integer> temp7 = new ArrayList();
/*  414 */     for (int i = 1; i < 27; i++) {
/*  415 */       if (i < 4) {
/*  416 */         temp1.add(Integer.valueOf(i));
/*      */       }
/*  418 */       this.group.put(Integer.valueOf(1), temp1);
/*  419 */       if ((i > 3) && (i < 7)) {
/*  420 */         temp2.add(Integer.valueOf(i));
/*      */       }
/*  422 */       this.group.put(Integer.valueOf(2), temp2);
/*  423 */       if ((i > 7) && (i < 11)) {
/*  424 */         temp3.add(Integer.valueOf(i));
/*      */       }
/*  426 */       this.group.put(Integer.valueOf(3), temp3);
/*  427 */       if ((i > 10) && (i < 17)) {
/*  428 */         temp4.add(Integer.valueOf(i));
/*      */       }
/*  430 */       this.group.put(Integer.valueOf(4), temp4);
/*  431 */       if ((i > 16) && (i < 20)) {
/*  432 */         temp5.add(Integer.valueOf(i));
/*      */       }
/*  434 */       this.group.put(Integer.valueOf(5), temp5);
/*  435 */       if ((i > 19) && (i < 23)) {
/*  436 */         temp6.add(Integer.valueOf(i));
/*      */       }
/*  438 */       this.group.put(Integer.valueOf(6), temp6);
/*  439 */       if ((i > 22) && (i < 26)) {
/*  440 */         temp7.add(Integer.valueOf(i));
/*      */       }
/*  442 */       this.group.put(Integer.valueOf(7), temp7);
/*      */     }
/*  444 */     this.confusedGroup1.add(Integer.valueOf(2));this.confusedGroup1.add(Integer.valueOf(5));this.confusedGroup1.add(Integer.valueOf(9));this.confusedGroup1.add(Integer.valueOf(18));this.confusedGroup1.add(Integer.valueOf(21));this.confusedGroup1.add(Integer.valueOf(24));
/*  445 */     this.confusedGroup2.add(Integer.valueOf(1));this.confusedGroup2.add(Integer.valueOf(6));this.confusedGroup2.add(Integer.valueOf(8));this.confusedGroup2.add(Integer.valueOf(17));this.confusedGroup2.add(Integer.valueOf(22));this.confusedGroup2.add(Integer.valueOf(25));
/*  446 */     this.confusedGroup3.add(Integer.valueOf(3));this.confusedGroup3.add(Integer.valueOf(4));this.confusedGroup3.add(Integer.valueOf(10));this.confusedGroup3.add(Integer.valueOf(19));this.confusedGroup3.add(Integer.valueOf(23));this.confusedGroup3.add(Integer.valueOf(20));
/*      */   }
/*      */   
/*  449 */   List<String> hapSp4 = new ArrayList();
/*  450 */   List<Integer> group3 = new ArrayList();
/*  451 */   List<Integer> group4 = new ArrayList();
/*  452 */   List<Integer> group5 = new ArrayList();
/*      */   
/*  454 */   public void getHapSp4() { this.hapSp4.add("AAAA");
/*  455 */     this.hapSp4.add("AAAB");this.hapSp4.add("AABA");this.hapSp4.add("ABAA");this.hapSp4.add("BAAA");
/*  456 */     this.hapSp4.add("AABB");this.hapSp4.add("ABAB");this.hapSp4.add("ABBA");this.hapSp4.add("BAAB");this.hapSp4.add("BBAA");this.hapSp4.add("BABA");
/*  457 */     this.hapSp4.add("ABBB");this.hapSp4.add("BABB");this.hapSp4.add("BBAB");this.hapSp4.add("BBBA");
/*  458 */     this.hapSp4.add("BBBB");
/*  459 */     for (int i = 1; i < 15; i++) {
/*  460 */       if (i < 5) this.group3.add(Integer.valueOf(i));
/*  461 */       if ((i > 4) && (i < 11)) this.group4.add(Integer.valueOf(i));
/*  462 */       if (i > 10) this.group5.add(Integer.valueOf(i));
/*      */     }
/*      */   }
/*      */   
/*      */   public int getSp2Index(String aa) {
/*  467 */     int i = -1;
/*  468 */     while (i < this.hapSp2.size()) {
/*  469 */       i++;
/*  470 */       if (((String)this.hapSp2.get(i)).equals(aa)) break;
/*      */     }
/*  472 */     return i;
/*      */   }
/*      */   
/*      */   public int getSp2GenoIndex(String aa) {
/*  476 */     int i = -1;
/*  477 */     while (i < this.hapSp2.size()) {
/*  478 */       i++;
/*  479 */       if (((String)this.hapSp2.get(i)).equals(aa)) break;
/*      */     }
/*  481 */     if (i > 1) return i - 1;
/*  482 */     return i;
/*      */   }
/*      */   
/*      */   public int getSpIndex(List<String> hapSp, String aa) {
/*  486 */     int i = -1;
/*  487 */     while (i < hapSp.size()) {
/*  488 */       i++;
/*  489 */       if (((String)hapSp.get(i)).equals(aa)) break;
/*      */     }
/*  491 */     return i;
/*      */   }
/*      */   
/*      */   public List<Integer> getSp3PossibleIndex(int index)
/*      */   {
/*  496 */     List<Integer> temp = new ArrayList();
/*  497 */     for (Iterator<Integer> is = this.group.keySet().iterator(); is.hasNext();) {
/*  498 */       Integer groupIndex = (Integer)is.next();
/*  499 */       temp = (List)this.group.get(groupIndex);
/*  500 */       if (temp.contains(Integer.valueOf(index))) {
/*      */         break;
/*      */       }
/*      */     }
/*  504 */     if (temp.isEmpty()) {
/*  505 */       throw new RuntimeException("no machted possible Index");
/*      */     }
/*  507 */     return temp;
/*      */   }
/*      */   
/*      */   public List<Integer> getSp4PossibleIndex(int index) {
/*  511 */     if (this.group3.contains(Integer.valueOf(index))) {
/*  512 */       return this.group3;
/*      */     }
/*  514 */     if (this.group4.contains(Integer.valueOf(index))) {
/*  515 */       return this.group4;
/*      */     }
/*  517 */     if (this.group5.contains(Integer.valueOf(index))) {
/*  518 */       return this.group5;
/*      */     }
/*  520 */     throw new RuntimeException("no machted possible Index");
/*      */   }
/*      */   
/*      */ 
/*      */   public void getAllPossibleExIndex()
/*      */   {
/*  526 */     this.possibleExIndex = new HashMap();
/*  527 */     List<byte[]> temp = new ArrayList();
/*  528 */     temp.add(new byte[] { 1, 0, 3, 2 });temp.add(new byte[] { 1, 2, 3 });temp.add(new byte[] { 3, 0, 1, 2 });temp.add(new byte[] { 3, 2, 1 });
/*  529 */     this.possibleExIndex.put(Integer.valueOf(1), temp);
/*  530 */     temp = new ArrayList();
/*  531 */     temp.add(new byte[] { 2, 3, 0, 1 });temp.add(new byte[] { 2, 3, 1 });temp.add(new byte[] { 3, 2, 0, 1 });temp.add(new byte[] { 3, 2, 1 });
/*  532 */     this.possibleExIndex.put(Integer.valueOf(2), temp);
/*  533 */     temp = new ArrayList();
/*  534 */     temp.add(new byte[] { 1, 0, 3, 2 });temp.add(new byte[] { 1, 3, 0, 2 });temp.add(new byte[] { 2, 0, 3, 1 });temp.add(new byte[] { 2, 3, 0, 1 });
/*  535 */     this.possibleExIndex.put(Integer.valueOf(3), temp);
/*      */   }
/*      */   
/*      */   public int getAmbigousIndex(List<Integer> index) {
/*  539 */     int amIndex = 0;
/*  540 */     List<Integer> temp = new ArrayList();
/*  541 */     temp.add(Integer.valueOf(6));temp.add(Integer.valueOf(10));
/*  542 */     if (index.containsAll(temp)) {
/*  543 */       amIndex = 1;
/*      */     }
/*  545 */     temp = new ArrayList();
/*  546 */     temp.add(Integer.valueOf(5));temp.add(Integer.valueOf(9));
/*  547 */     if (index.containsAll(temp)) {
/*  548 */       amIndex = 2;
/*      */     }
/*  550 */     temp = new ArrayList();
/*  551 */     temp.add(Integer.valueOf(7));temp.add(Integer.valueOf(8));
/*  552 */     if (index.containsAll(temp)) {
/*  553 */       amIndex = 3;
/*      */     }
/*  555 */     return amIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<String> getHapFromPhasedData(String indivID, int i, List<Integer> heteroIndex, Integer hapSpIndex, int noIndex, List<String> hapSp)
/*      */   {
/*  562 */     List<String> inferHap = new ArrayList();
/*  563 */     for (int k = 0; k < ((String[])this.phasedData.get(indivID)).length; k++) {
/*  564 */       char[] temp = new char[noIndex];
/*  565 */       int count = noIndex - 1;
/*  566 */       for (int n = 0; n < noIndex - 1; n++) {
/*  567 */         temp[n] = ((String[])this.phasedData.get(indivID))[k].charAt(((Integer)heteroIndex.get(i - count)).intValue());
/*  568 */         count--;
/*      */       }
/*  570 */       temp[(noIndex - 1)] = ((String)hapSp.get(hapSpIndex.intValue())).charAt(k);
/*  571 */       String s = new String(temp);
/*  572 */       inferHap.add(s);
/*      */     }
/*  574 */     return inferHap;
/*      */   }
/*      */   
/*      */   public List<String> getHapFromIndex(int[] hapSpIndex, List<String> hapSp, int no_copies)
/*      */   {
/*  579 */     List<String> inferHap = new ArrayList();
/*  580 */     for (int i = 0; i < no_copies; i++) {
/*  581 */       char[] temp = new char[hapSpIndex.length];
/*  582 */       for (int k = 0; k < hapSpIndex.length; k++) {
/*  583 */         temp[k] = ((String)hapSp.get(hapSpIndex[k])).charAt(i);
/*      */       }
/*  585 */       String s = new String(temp);
/*  586 */       inferHap.add(s);
/*      */     }
/*  588 */     return inferHap;
/*      */   }
/*      */   
/*      */   public String getGenotypeString(String indivID, int i, List<Integer> heteroIndex)
/*      */   {
/*  593 */     char[] geno = new char[((String[])this.phasedData.get(indivID)).length];
/*  594 */     for (int k = 0; k < geno.length; k++) {
/*  595 */       geno[k] = ((String[])this.phasedData.get(indivID))[k].charAt(((Integer)heteroIndex.get(i)).intValue());
/*      */     }
/*  597 */     String s = new String(geno);
/*  598 */     return s;
/*      */   }
/*      */   
/*      */   public String getGenoStringSwiched(String indivID, int i, List<Integer> heteroIndex, List<byte[]> newIndexTrueHap)
/*      */   {
/*  603 */     char[] geno = new char[((String[])this.phasedData.get(indivID)).length];
/*  604 */     for (int k = 0; k < geno.length; k++) {
/*  605 */       geno[k] = ((String[])this.phasedData.get(indivID))[((byte[])newIndexTrueHap.get(i))[k]].charAt(((Integer)heteroIndex.get(i)).intValue());
/*      */     }
/*  607 */     String s = new String(geno);
/*  608 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */   public List<byte[]> getExchangeIndex(String a1, String a2, List<byte[]> newIndexTrueHapList, int no_copies)
/*      */   {
/*  614 */     byte[] newIndexTrueHap = new byte[3];
/*  615 */     byte[] dif = new byte[2];
/*  616 */     byte c = 0;
/*  617 */     if ((no_copies == 3) && (a1.charAt(0) != a1.charAt(1)) && (a1.charAt(0) != a1.charAt(2)) && (a1.charAt(1) != a1.charAt(2))) {
/*  618 */       for (byte i = 0; i < no_copies; i = (byte)(i + 1)) {
/*  619 */         for (byte j = 0; j < no_copies; j = (byte)(j + 1)) {
/*  620 */           if (a1.charAt(i) == a2.charAt(j)) {
/*  621 */             newIndexTrueHap[i] = j;
/*  622 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  628 */       for (byte i = 0; i < no_copies; i = (byte)(i + 1)) {
/*  629 */         if (a1.charAt(i) != a2.charAt(i)) {
/*  630 */           dif[c] = i;
/*  631 */           c = (byte)(c + 1);
/*      */         }
/*      */       }
/*  634 */       for (byte i = 0; i < no_copies; i = (byte)(i + 1)) {
/*  635 */         if (i == dif[0]) { newIndexTrueHap[i] = dif[1];
/*  636 */         } else if (i == dif[1]) newIndexTrueHap[i] = dif[0]; else {
/*  637 */           newIndexTrueHap[i] = i;
/*      */         }
/*      */       }
/*      */     }
/*  641 */     List<byte[]> newIndexTrueHapList1 = new ArrayList();
/*  642 */     if (!newIndexTrueHapList.isEmpty()) {
/*  643 */       for (int k = 0; k < newIndexTrueHapList.size(); k++) {
/*  644 */         byte[] newIndexTrueHap1 = new byte[3];
/*  645 */         for (int i = 0; i < no_copies; i++) {
/*  646 */           newIndexTrueHap1[i] = ((byte[])newIndexTrueHapList.get(k))[newIndexTrueHap[i]];
/*      */         }
/*  648 */         newIndexTrueHapList1.add(newIndexTrueHap1);
/*      */       }
/*      */     }
/*      */     
/*  652 */     newIndexTrueHapList1.add(newIndexTrueHap);
/*  653 */     return newIndexTrueHapList1;
/*      */   }
/*      */   
/*      */   public List<byte[]> getExchangeIndex4(String a1, String a2, List<byte[]> newIndexTrueHapList, int no_copies)
/*      */   {
/*  658 */     byte[] newIndexTrueHap = new byte[no_copies];
/*  659 */     byte[] dif = new byte[2];
/*  660 */     byte c = 0;
/*  661 */     for (byte i = 0; i < no_copies; i = (byte)(i + 1)) {
/*  662 */       if (a1.charAt(i) != a2.charAt(i)) {
/*  663 */         dif[c] = i;
/*  664 */         c = (byte)(c + 1);
/*      */       }
/*      */     }
/*  667 */     for (byte i = 0; i < no_copies; i = (byte)(i + 1)) {
/*  668 */       if (i == dif[0]) { newIndexTrueHap[i] = dif[1];
/*  669 */       } else if (i == dif[1]) newIndexTrueHap[i] = dif[0]; else {
/*  670 */         newIndexTrueHap[i] = i;
/*      */       }
/*      */     }
/*  673 */     List<byte[]> newIndexTrueHapList1 = new ArrayList();
/*  674 */     if (!newIndexTrueHapList.isEmpty()) {
/*  675 */       for (int k = 0; k < newIndexTrueHapList.size(); k++) {
/*  676 */         byte[] newIndexTrueHap1 = new byte[no_copies];
/*  677 */         for (int i = 0; i < no_copies; i++) {
/*  678 */           if (i == dif[0]) { newIndexTrueHap1[i] = ((byte[])newIndexTrueHapList.get(k))[dif[1]];
/*  679 */           } else if (i == dif[1]) newIndexTrueHap1[i] = ((byte[])newIndexTrueHapList.get(k))[dif[0]]; else
/*  680 */             newIndexTrueHap1[i] = ((byte[])newIndexTrueHapList.get(k))[i];
/*      */         }
/*  682 */         newIndexTrueHapList1.add(newIndexTrueHap1);
/*      */       }
/*      */     }
/*      */     
/*  686 */     newIndexTrueHapList1.add(newIndexTrueHap);
/*  687 */     return newIndexTrueHapList1;
/*      */   }
/*      */   
/*      */   public List<byte[]> getExchangeIndexAll(byte[] onePossible, List<byte[]> newIndexTrueHapList, int no_copies)
/*      */   {
/*  692 */     List<byte[]> newIndexTrueHapList1 = new ArrayList();
/*  693 */     for (int k = 0; k < newIndexTrueHapList.size(); k++) {
/*  694 */       byte[] newIndexTrueHap1 = new byte[no_copies];
/*  695 */       for (int i = 0; i < no_copies; i++) {
/*  696 */         newIndexTrueHap1[i] = ((byte[])newIndexTrueHapList.get(k))[onePossible[i]];
/*      */       }
/*  698 */       newIndexTrueHapList1.add(newIndexTrueHap1);
/*      */     }
/*  700 */     newIndexTrueHapList1.add(onePossible);
/*  701 */     return newIndexTrueHapList1;
/*      */   }
/*      */   
/*      */   public int getNoDiffHap(int[] hapSpIndex, List<String> hapSp, int no_copies)
/*      */   {
/*  706 */     List<String> hapList = getHapFromIndex(hapSpIndex, hapSp, no_copies);
/*  707 */     List<String> newHapList = new ArrayList();
/*  708 */     for (int i = 0; i < hapList.size(); i++) {
/*  709 */       if (!newHapList.contains(hapList.get(i))) newHapList.add((String)hapList.get(i));
/*      */     }
/*  711 */     int noDiffHap = newHapList.size();
/*  712 */     return noDiffHap;
/*      */   }
/*      */   
/*      */   public int[] getA2B2count(String indivID, List<String> hapSp, int no_copies) {
/*  716 */     int count = 0;
/*  717 */     List<Integer> heteroIndex = getHeteroIndex(indivID, no_copies);
/*  718 */     for (int i = 0; i < heteroIndex.size(); i++) {
/*  719 */       if (this.group4.contains(Integer.valueOf(getSpIndex(hapSp, getGenotypeString(indivID, i, heteroIndex))))) count++;
/*      */     }
/*  721 */     int[] ratio = { count, heteroIndex.size() };
/*  722 */     return ratio;
/*      */   }
/*      */   
/*      */   public void getMajorAllel(File f) throws Exception
/*      */   {
/*  727 */     this.majorAllele = new ArrayList();
/*  728 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*      */     String st;
/*  730 */     while ((st = br.readLine()) != null) { String st;
/*  731 */       String[] str = st.split("\t");
/*  732 */       if ((Integer.parseInt(str[1]) >= mid[0]) && (Integer.parseInt(str[2]) <= mid[1])) {
/*  733 */         this.majorAllele.add(str[4]);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public int getFromToCopies(List<String> inferHap)
/*      */   {
/*  740 */     int from = 0;
/*  741 */     int to = 0;
/*  742 */     for (int i = 0; i < inferHap.size(); i++) {
/*  743 */       if ((((String)inferHap.get(i)).charAt(0) == 'A') || (((String)inferHap.get(i)).charAt(0) == 'B')) from++;
/*  744 */       if ((((String)inferHap.get(i)).charAt(0) == 'X') || (((String)inferHap.get(i)).charAt(0) == 'Y') || (((String)inferHap.get(i)).charAt(0) == 'Z')) from += 2;
/*  745 */       if ((((String)inferHap.get(i)).charAt(0) == 'U') || (((String)inferHap.get(i)).charAt(0) == 'V') || (((String)inferHap.get(i)).charAt(0) == 'W') || (((String)inferHap.get(i)).charAt(0) == 'T')) from += 3;
/*      */     }
/*  747 */     for (int i = 0; i < inferHap.size(); i++) {
/*  748 */       if ((((String)inferHap.get(i)).charAt(1) == 'A') || (((String)inferHap.get(i)).charAt(1) == 'B')) to++;
/*  749 */       if ((((String)inferHap.get(i)).charAt(1) == 'X') || (((String)inferHap.get(i)).charAt(1) == 'Y') || (((String)inferHap.get(i)).charAt(1) == 'Z')) to += 2;
/*  750 */       if ((((String)inferHap.get(i)).charAt(1) == 'U') || (((String)inferHap.get(i)).charAt(1) == 'V') || (((String)inferHap.get(i)).charAt(1) == 'W') || (((String)inferHap.get(i)).charAt(1) == 'T')) to += 3;
/*      */     }
/*  752 */     if ((from == 0) || (to == 0)) {
/*  753 */       throw new RuntimeException("!!");
/*      */     }
/*  755 */     return from * 10 + to;
/*      */   }
/*      */   
/*      */   public int getFromToCopies1(List<String> inferHap) {
/*  759 */     int from = 0;
/*  760 */     int to = 0;
/*  761 */     for (int i = 0; i < inferHap.size(); i++) {
/*  762 */       if ((((String)inferHap.get(i)).charAt(1) == 'A') || (((String)inferHap.get(i)).charAt(1) == 'B')) from++;
/*  763 */       if ((((String)inferHap.get(i)).charAt(1) == 'X') || (((String)inferHap.get(i)).charAt(1) == 'Y') || (((String)inferHap.get(i)).charAt(0) == 'Z')) from += 2;
/*  764 */       if ((((String)inferHap.get(i)).charAt(1) == 'U') || (((String)inferHap.get(i)).charAt(1) == 'V') || (((String)inferHap.get(i)).charAt(0) == 'W') || (((String)inferHap.get(i)).charAt(0) == 'T')) from += 3;
/*      */     }
/*  766 */     for (int i = 0; i < inferHap.size(); i++) {
/*  767 */       if ((((String)inferHap.get(i)).charAt(2) == 'A') || (((String)inferHap.get(i)).charAt(2) == 'B')) to++;
/*  768 */       if ((((String)inferHap.get(i)).charAt(2) == 'X') || (((String)inferHap.get(i)).charAt(2) == 'Y') || (((String)inferHap.get(i)).charAt(1) == 'Z')) to += 2;
/*  769 */       if ((((String)inferHap.get(i)).charAt(2) == 'U') || (((String)inferHap.get(i)).charAt(2) == 'V') || (((String)inferHap.get(i)).charAt(1) == 'W') || (((String)inferHap.get(i)).charAt(1) == 'T')) to += 3;
/*      */     }
/*  771 */     if ((from == 0) || (to == 0)) {
/*  772 */       throw new RuntimeException("!!");
/*      */     }
/*  774 */     return from * 10 + to;
/*      */   }
/*      */   
/*      */   public int[] getSwitchError(String indivID) {
/*  778 */     if (((List)this.rawData.get(indivID)).size() != ((String[])this.phasedData.get(indivID))[0].length()) {
/*  779 */       throw new RuntimeException(((String[])this.phasedData.get(indivID))[0].length() + "# of snp on phasedHap !=" + ((List)this.rawData.get(indivID)).size() + "# of snp on rawData");
/*      */     }
/*  781 */     int no_copies = ((String[])this.phasedData.get(indivID)).length;
/*  782 */     List<Integer> heteroIndex = getHeteroIndexCode(indivID, no_copies);
/*      */     
/*  784 */     int switchError = 0;
/*  785 */     this.errorHeteroIndex.put(indivID, new Integer[3][heteroIndex.size()]);
/*  786 */     ((Integer[][])this.errorHeteroIndex.get(indivID))[0] = ((Integer[])heteroIndex.toArray(new Integer[0]));
/*  787 */     Arrays.fill(((Integer[][])this.errorHeteroIndex.get(indivID))[1], Integer.valueOf(0));
/*  788 */     Arrays.fill(((Integer[][])this.errorHeteroIndex.get(indivID))[2], Integer.valueOf(0));
/*  789 */     Map<String, List<byte[]>> newIndexTrueHapList = new HashMap();
/*  790 */     for (int i = 1; i < heteroIndex.size(); i++)
/*      */     {
/*  792 */       if (no_copies == 2) {
/*  793 */         List<String> trueHap = new ArrayList();
/*  794 */         List<String> inferHap = new ArrayList();
/*  795 */         for (int k = 0; k < no_copies; k++) {
/*  796 */           char[] temp = { ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i - 1)).intValue()))[k].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i)).intValue()))[k].charValue() };
/*  797 */           String s = new String(temp);
/*  798 */           trueHap.add(s);
/*      */         }
/*  800 */         for (int k = 0; k < no_copies; k++) {
/*  801 */           char[] temp = { ((String[])this.phasedData.get(indivID))[k].charAt(((Integer)heteroIndex.get(i - 1)).intValue()), ((String[])this.phasedData.get(indivID))[k].charAt(((Integer)heteroIndex.get(i)).intValue()) };
/*  802 */           String s = new String(temp);
/*  803 */           inferHap.add(s);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  809 */         if ((!trueHap.containsAll(inferHap)) || (!inferHap.containsAll(trueHap))) {
/*  810 */           switchError++;
/*  811 */           ((Integer[][])this.errorHeteroIndex.get(indivID))[1][i] = Integer.valueOf(1);
/*  812 */           ((Integer[][])this.errorHeteroIndex.get(indivID))[2][i] = Integer.valueOf(getFromToCopies(inferHap));
/*      */         } else {
/*  814 */           ((Integer[][])this.errorHeteroIndex.get(indivID))[2][i] = Integer.valueOf(getFromToCopies(inferHap));
/*      */         }
/*      */       }
/*      */       
/*  818 */       if (no_copies == 3) {
/*  819 */         if (i == 1) {
/*  820 */           List<String> trueHap = new ArrayList();
/*      */           
/*  822 */           for (int k = 0; k < no_copies; k++) {
/*  823 */             char[] temp = { ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i - 1)).intValue()))[k].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i)).intValue()))[k].charValue() };
/*  824 */             String s = new String(temp);
/*  825 */             trueHap.add(s);
/*      */           }
/*      */           
/*  828 */           Integer hapSpIndex = Integer.valueOf(getSpIndex(this.hapSp3, getGenotypeString(indivID, i, heteroIndex)));
/*      */           
/*  830 */           List<String> inferHap = getHapFromPhasedData(indivID, i, heteroIndex, hapSpIndex, 2, this.hapSp3);
/*      */           
/*  832 */           int p = 0;
/*  833 */           if ((!trueHap.containsAll(inferHap)) || (!inferHap.containsAll(trueHap))) {
/*  834 */             switchError++;
/*  835 */             ((Integer[][])this.errorHeteroIndex.get(indivID))[1][i] = Integer.valueOf(1);
/*  836 */             ((Integer[][])this.errorHeteroIndex.get(indivID))[2][i] = Integer.valueOf(getFromToCopies(inferHap));
/*  837 */             List<Integer> possibleIndex = getSp3PossibleIndex(hapSpIndex.intValue());
/*  838 */             for (int h = 0; h < possibleIndex.size(); h++) {
/*  839 */               if (possibleIndex.get(h) != hapSpIndex) {
/*  840 */                 inferHap = getHapFromPhasedData(indivID, i, heteroIndex, (Integer)possibleIndex.get(h), 2, this.hapSp3);
/*  841 */                 if (trueHap.containsAll(inferHap)) {
/*  842 */                   List<byte[]> newIndexTrueHapList1 = new ArrayList();
/*  843 */                   newIndexTrueHapList.put(i + "_" + p, newIndexTrueHapList1);
/*  844 */                   newIndexTrueHapList.put(i + "_" + p, getExchangeIndex((String)this.hapSp3.get(hapSpIndex.intValue()), (String)this.hapSp3.get(((Integer)possibleIndex.get(h)).intValue()), (List)newIndexTrueHapList.get(i + "_" + p), no_copies));
/*  845 */                   p++;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           else {
/*  851 */             List<byte[]> newIndexTrueHapList1 = new ArrayList();
/*  852 */             newIndexTrueHapList1.add(new byte[] { 0, 1, 2 });
/*  853 */             newIndexTrueHapList.put(i + "_" + p, newIndexTrueHapList1);
/*  854 */             ((Integer[][])this.errorHeteroIndex.get(indivID))[2][i] = Integer.valueOf(getFromToCopies(inferHap));
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  861 */           List<String> inferHap = null;
/*  862 */           List<String> trueHap = null;
/*      */           
/*  864 */           String check = null;
/*  865 */           int p = 0;
/*  866 */           String switchErrorSign = null;
/*  867 */           List<String> tempIterator = new ArrayList();
/*  868 */           for (Iterator<String> is = newIndexTrueHapList.keySet().iterator(); is.hasNext();) {
/*  869 */             String st = (String)is.next();
/*  870 */             tempIterator.add(st);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  875 */           for (int t = 0; t < tempIterator.size(); t++)
/*      */           {
/*  877 */             String st = (String)tempIterator.get(t);
/*      */             
/*  879 */             Integer hapSpIndex1 = Integer.valueOf(getSpIndex(this.hapSp3, getGenotypeString(indivID, i, heteroIndex)));
/*  880 */             Integer hapSpIndex2 = Integer.valueOf(getSpIndex(this.hapSp3, getGenotypeString(indivID, i - 1, heteroIndex)));
/*  881 */             Integer hapSpIndex3 = Integer.valueOf(getSpIndex(this.hapSp3, getGenoStringSwiched(indivID, i - 2, heteroIndex, (List)newIndexTrueHapList.get(st))));
/*      */             
/*  883 */             int newIndex = i - 2;
/*  884 */             for (int ji = i - 3; ji >= 0; ji--) {
/*  885 */               List<Integer> temp = new ArrayList();
/*  886 */               temp.add(hapSpIndex2);temp.add(hapSpIndex3);
/*  887 */               if ((!temp.containsAll(this.confusedGroup1)) && (!temp.containsAll(this.confusedGroup2)) && (!temp.containsAll(this.confusedGroup3)) && (hapSpIndex2 != hapSpIndex3)) break;
/*  888 */               hapSpIndex3 = Integer.valueOf(getSpIndex(this.hapSp3, getGenoStringSwiched(indivID, ji, heteroIndex, (List)newIndexTrueHapList.get(st))));
/*  889 */               newIndex = ji; continue;
/*      */               
/*  891 */               break;
/*      */             }
/*      */             
/*  894 */             trueHap = new ArrayList();
/*  895 */             for (int k = 0; k < no_copies; k++) {
/*  896 */               char[] temp = { ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(newIndex)).intValue()))[k].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i - 1)).intValue()))[k].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i)).intValue()))[k].charValue() };
/*  897 */               String s = new String(temp);
/*  898 */               trueHap.add(s);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  908 */             inferHap = getHapFromIndex(new int[] { hapSpIndex3.intValue(), hapSpIndex2.intValue(), hapSpIndex1.intValue() }, this.hapSp3, no_copies);
/*      */             
/*  910 */             if ((!trueHap.containsAll(inferHap)) || (!inferHap.containsAll(trueHap))) {
/*  911 */               List<Integer> possibleIndex = getSp3PossibleIndex(hapSpIndex1.intValue());
/*  912 */               for (int h = 0; h < possibleIndex.size(); h++) {
/*  913 */                 if (possibleIndex.get(h) != hapSpIndex1) {
/*  914 */                   inferHap = getHapFromIndex(new int[] { hapSpIndex3.intValue(), hapSpIndex2.intValue(), ((Integer)possibleIndex.get(h)).intValue() }, this.hapSp3, no_copies);
/*  915 */                   if (trueHap.containsAll(inferHap)) {
/*  916 */                     newIndexTrueHapList.put(i + "_" + p, getExchangeIndex((String)this.hapSp3.get(hapSpIndex1.intValue()), (String)this.hapSp3.get(((Integer)possibleIndex.get(h)).intValue()), (List)newIndexTrueHapList.get(st), no_copies));
/*  917 */                     check = "ok";
/*  918 */                     p++;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/*  925 */               ((List)newIndexTrueHapList.get(st)).add(new byte[] { 0, 1, 2 });
/*  926 */               newIndexTrueHapList.put(i + "_" + p, (List)newIndexTrueHapList.get(st));
/*  927 */               check = "ok";
/*  928 */               p++;
/*  929 */               switchErrorSign = "-";
/*  930 */               ((Integer[][])this.errorHeteroIndex.get(indivID))[2][i] = Integer.valueOf(getFromToCopies1(inferHap));
/*      */             }
/*  932 */             newIndexTrueHapList.remove(st);
/*      */           }
/*  934 */           if (check == null) {
/*  935 */             throw new RuntimeException(i + "th true haps and phase haps are not matched");
/*      */           }
/*  937 */           if (switchErrorSign == null) {
/*  938 */             switchError++;
/*  939 */             ((Integer[][])this.errorHeteroIndex.get(indivID))[1][i] = Integer.valueOf(1);
/*  940 */             ((Integer[][])this.errorHeteroIndex.get(indivID))[2][i] = Integer.valueOf(getFromToCopies1(inferHap));
/*  941 */             if (getFromToCopies1(inferHap) == 22) {
/*  942 */               t = 0;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  948 */     int[] switchCounts = { switchError, heteroIndex.size() };
/*  949 */     newIndexTrueHapList.clear();
/*  950 */     return switchCounts;
/*      */   }
/*      */   
/*      */   public double getIncorrectRate(String indivID)
/*      */   {
/*  955 */     int no_copies = ((String[])this.phasedData.get(indivID)).length;
/*  956 */     List<Integer> heteroIndex = getHeteroIndex(indivID, no_copies);
/*  957 */     double[] incorrectRate = new double[no_copies];
/*  958 */     int[][] countDif = new int[no_copies][no_copies];
/*  959 */     for (int i = 0; i < countDif.length; i++) {
/*  960 */       Arrays.fill(countDif[i], 0);
/*  961 */       Arrays.fill(countDif[i], 0);
/*      */     }
/*  963 */     for (int i = 0; i < no_copies; i++) {
/*  964 */       for (int t = 0; t < no_copies; t++) {
/*  965 */         for (int m = 0; m < heteroIndex.size(); m++) {
/*  966 */           if (((String[])this.phasedData.get(indivID))[i].charAt(((Integer)heteroIndex.get(m)).intValue()) != ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(m)).intValue()))[t].charValue()) {
/*  967 */             countDif[i][t] += 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  972 */     for (int i = 0; i < no_copies; i++) {
/*  973 */       int min = Integer.MAX_VALUE;
/*  974 */       for (int k = 0; k < countDif[i].length; k++) {
/*  975 */         if (countDif[i][k] < min) min = countDif[i][k];
/*      */       }
/*  977 */       incorrectRate[i] = (min / heteroIndex.size());
/*      */     }
/*  979 */     double temp = 0.0D;
/*  980 */     for (int i = 0; i < no_copies; i++) {
/*  981 */       temp += incorrectRate[i];
/*      */     }
/*  983 */     return temp / no_copies;
/*      */   }
/*      */   
/*      */   public Map<String, Integer> getNumDiff()
/*      */   {
/*  988 */     Map<String, Integer> numDiff = new HashMap();
/*  989 */     for (Iterator<String> is = this.phasedData.keySet().iterator(); is.hasNext();) {
/*  990 */       String indivID = (String)is.next();
/*  991 */       int[] count = new int[3];
/*  992 */       for (int i = 0; i < ((String[])this.phasedData.get(indivID))[0].length(); i++) {
/*  993 */         if (((String[])this.phasedData.get(indivID))[0].charAt(i) != ((String[])this.phasedData.get(indivID))[1].charAt(i)) count[0] += 1;
/*  994 */         if (((String[])this.phasedData.get(indivID))[0].charAt(i) != ((String[])this.phasedData.get(indivID))[2].charAt(i)) count[1] += 1;
/*  995 */         if (((String[])this.phasedData.get(indivID))[1].charAt(i) != ((String[])this.phasedData.get(indivID))[2].charAt(i)) count[2] += 1;
/*      */       }
/*  997 */       int min = count[0];
/*  998 */       for (int i = 1; i < count.length; i++) {
/*  999 */         if (count[i] < min) min = count[i];
/*      */       }
/* 1001 */       numDiff.put(indivID, Integer.valueOf(min));
/*      */     }
/* 1003 */     return numDiff;
/*      */   }
/*      */   
/*      */   public Map<String, Integer> getNumDiffRaw()
/*      */   {
/* 1008 */     Map<String, Integer> numDiff = new HashMap();
/* 1009 */     for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 1010 */       String indivID = (String)is.next();
/* 1011 */       int[] count = new int[3];
/* 1012 */       for (int i = 0; i < ((List)this.rawData.get(indivID)).size(); i++) {
/* 1013 */         if (((Character[])((List)this.rawData.get(indivID)).get(i))[0] != ((Character[])((List)this.rawData.get(indivID)).get(i))[1]) count[0] += 1;
/* 1014 */         if (((Character[])((List)this.rawData.get(indivID)).get(i))[0] != ((Character[])((List)this.rawData.get(indivID)).get(i))[2]) count[1] += 1;
/* 1015 */         if (((Character[])((List)this.rawData.get(indivID)).get(i))[1] != ((Character[])((List)this.rawData.get(indivID)).get(i))[2]) count[2] += 1;
/*      */       }
/* 1017 */       int min = count[0];
/* 1018 */       for (int i = 1; i < count.length; i++) {
/* 1019 */         if (count[i] < min) min = count[i];
/*      */       }
/* 1021 */       numDiff.put(indivID, Integer.valueOf(min));
/*      */     }
/* 1023 */     return numDiff;
/*      */   }
/*      */   
/*      */   public void printNumDiff(PrintStream ps) {
/* 1027 */     Map<String, Integer> numDiff = getNumDiffRaw();
/* 1028 */     String indivID; for (Iterator<String> is = numDiff.keySet().iterator(); is.hasNext(); 
/*      */         
/* 1030 */         ps.println(numDiff.get(indivID)))
/*      */     {
/* 1029 */       indivID = (String)is.next();
/* 1030 */       ps.print(indivID + "  ");
/*      */     }
/*      */   }
/*      */   
/*      */   public void printErrorRate(PrintStream ps, Map<String, Double> rate)
/*      */   {
/* 1036 */     double sum = 0.0D;
/* 1037 */     String idv; for (Iterator<String> id = rate.keySet().iterator(); id.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/* 1041 */         ps.println(rate.get(idv)))
/*      */     {
/* 1038 */       idv = (String)id.next();
/*      */       
/* 1040 */       sum += ((Double)rate.get(idv)).doubleValue();
/* 1041 */       ps.print(idv);ps.print("---");
/*      */     }
/*      */     
/* 1044 */     ps.print("averge:");ps.println(sum / this.switchErrortateAll.size());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Map<Integer, int[][]> compareUncerSwitch()
/*      */   {
/* 1051 */     Map<Integer, int[][]> fromToCertainty = new HashMap();
/* 1052 */     int sum = 0;
/* 1053 */     Integer[][] count; int i; for (Iterator<String> is = this.errorHeteroIndex.keySet().iterator(); is.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/* 1057 */         i < count[0].length)
/*      */     {
/* 1054 */       String indivID = (String)is.next();
/* 1055 */       count = (Integer[][])this.errorHeteroIndex.get(indivID);
/* 1056 */       sum += count[0].length;
/* 1057 */       i = 0; continue;
/* 1058 */       double certainty = ((Double)((List)this.uncertaintyScore.get(indivID)).get(count[0][i].intValue())).doubleValue();
/* 1059 */       Integer a = count[2][i];
/* 1060 */       if (!fromToCertainty.containsKey(a)) {
/* 1061 */         int[][] countError = new int[2][10];
/* 1062 */         Arrays.fill(countError[0], 0);
/* 1063 */         Arrays.fill(countError[1], 0);
/* 1064 */         fromToCertainty.put(a, countError);
/*      */       }
/* 1066 */       for (int k = 1; k < 11; k++) {
/* 1067 */         if (certainty * 100.0D <= k * 10.0D) {
/* 1068 */           ((int[][])fromToCertainty.get(a))[1][(k - 1)] += 1;
/* 1069 */           break;
/*      */         }
/*      */       }
/* 1072 */       if (count[1][i].intValue() == 1) {
/* 1073 */         for (int k = 1; k < 11; k++) {
/* 1074 */           if (certainty * 100.0D <= k * 10.0D) {
/* 1075 */             ((int[][])fromToCertainty.get(a))[0][(k - 1)] += 1;
/* 1076 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1057 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1082 */     int[][] countError = new int[1][1];
/* 1083 */     countError[0][0] = sum;
/* 1084 */     fromToCertainty.put(Integer.valueOf(0), countError);
/* 1085 */     return fromToCertainty;
/*      */   }
/*      */   
/*      */   public int comparePhaseRaw(String[] phase, List<Character[]> raw, int pos)
/*      */   {
/* 1090 */     int[] phasedCount = new int[2];
/* 1091 */     int[] rawCount = new int[2];
/* 1092 */     for (int c = 0; c < phase.length; c++) {
/* 1093 */       if (phase[c].charAt(pos) == 'A') phasedCount[0] += 1; else
/* 1094 */         phasedCount[1] += 1;
/* 1095 */       if (((Character[])raw.get(pos))[c].equals(Character.valueOf('A'))) rawCount[0] += 1; else
/* 1096 */         rawCount[1] += 1;
/*      */     }
/* 1098 */     if ((phasedCount[0] != rawCount[0]) || (phasedCount[1] != rawCount[1])) return 1;
/* 1099 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public int[][] compareUncerImpute()
/*      */   {
/* 1105 */     int[][] countError = new int[2][10];
/* 1106 */     Arrays.fill(countError[0], 0);
/* 1107 */     Arrays.fill(countError[1], 0);
/* 1108 */     String idv; int i; for (Iterator<String> id = this.rawDataMissing.keySet().iterator(); id.hasNext(); 
/*      */         
/* 1110 */         i < ((List)this.rawDataMissing.get(idv)).size())
/*      */     {
/* 1109 */       idv = (String)id.next();
/* 1110 */       i = 0; continue;
/* 1111 */       if (((Integer)((List)this.rawDataMissing.get(idv)).get(i)).intValue() == 1)
/*      */       {
/* 1113 */         double certainty = ((Double)((List)this.uncertaintyScore.get(idv)).get(i)).doubleValue();
/* 1114 */         for (int k = 1; k < 11; k++) {
/* 1115 */           if (certainty * 100.0D <= k * 10.0D) {
/* 1116 */             countError[1][(k - 1)] += 1;
/* 1117 */             break;
/*      */           }
/*      */         }
/* 1120 */         if (comparePhaseRaw((String[])this.phasedData.get(idv), (List)this.rawData.get(idv), i) == 1) {
/* 1121 */           for (int k = 1; k < 11; k++) {
/* 1122 */             if (certainty * 100.0D <= k * 10.0D) {
/* 1123 */               countError[0][(k - 1)] += 1;
/* 1124 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1110 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1132 */     return countError;
/*      */   }
/*      */   
/*      */ 
/*      */   public Map<Integer, Integer[]> getCountFromTo()
/*      */   {
/* 1138 */     Map<Integer, Integer[]> fromToCount = new HashMap();
/* 1139 */     int sum = 0;
/* 1140 */     Integer[][] count; int i; for (Iterator<String> is = this.errorHeteroIndex.keySet().iterator(); is.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/* 1144 */         i < count[0].length)
/*      */     {
/* 1141 */       String indivID = (String)is.next();
/* 1142 */       count = (Integer[][])this.errorHeteroIndex.get(indivID);
/* 1143 */       sum += count[0].length;
/* 1144 */       i = 0; continue;
/* 1145 */       Integer a = count[2][i];
/*      */       
/*      */ 
/* 1148 */       if (!fromToCount.containsKey(a)) {
/* 1149 */         if (count[1][i].intValue() == 1) {
/* 1150 */           fromToCount.put(a, new Integer[] { Integer.valueOf(1), Integer.valueOf(0) });
/* 1151 */           fromToCount.put(a, new Integer[] { Integer.valueOf(0), Integer.valueOf(1) });
/*      */         } else {
/* 1153 */           fromToCount.put(a, new Integer[] { Integer.valueOf(0), Integer.valueOf(1) });
/*      */         }
/*      */         
/*      */       }
/* 1157 */       else if (count[1][i].intValue() == 1) {
/* 1158 */         int tmp216_215 = 0; Integer[] tmp216_212 = ((Integer[])fromToCount.get(a));tmp216_212[tmp216_215] = Integer.valueOf(tmp216_212[tmp216_215].intValue() + 1); int 
/* 1159 */           tmp239_238 = 1; Integer[] tmp239_235 = ((Integer[])fromToCount.get(a));tmp239_235[tmp239_238] = Integer.valueOf(tmp239_235[tmp239_238].intValue() + 1);
/*      */       } else {
/* 1161 */         int tmp265_264 = 1; Integer[] tmp265_261 = ((Integer[])fromToCount.get(a));tmp265_261[tmp265_264] = Integer.valueOf(tmp265_261[tmp265_264].intValue() + 1);
/*      */       }
/* 1144 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1166 */     fromToCount.put(Integer.valueOf(0), new Integer[] { Integer.valueOf(sum), Integer.valueOf(sum) });
/* 1167 */     return fromToCount;
/*      */   }
/*      */   
/*      */   public Map<Integer, Integer> getCountFromToTotal()
/*      */   {
/* 1172 */     Map<Integer, Integer> fromToCount = new HashMap();
/* 1173 */     int sum = 0;
/* 1174 */     List<Integer> heteroIndex; int i; for (Iterator<String> is = this.phasedData.keySet().iterator(); is.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/* 1178 */         i < heteroIndex.size())
/*      */     {
/* 1175 */       String indivID = (String)is.next();
/* 1176 */       int no_copies = ((String[])this.phasedData.get(indivID)).length;
/* 1177 */       heteroIndex = getHeteroIndexCode(indivID, no_copies);
/* 1178 */       i = 1; continue;
/*      */       
/* 1180 */       sum++;
/* 1181 */       List<String> inferHap = new ArrayList();
/* 1182 */       for (int k = 0; k < no_copies; k++) {
/* 1183 */         char[] temp = { ((String[])this.phasedData.get(indivID))[k].charAt(((Integer)heteroIndex.get(i - 1)).intValue()), ((String[])this.phasedData.get(indivID))[k].charAt(((Integer)heteroIndex.get(i)).intValue()) };
/* 1184 */         String s = new String(temp);
/* 1185 */         inferHap.add(s);
/*      */       }
/* 1187 */       int a = getFromToCopies(inferHap);
/* 1188 */       if (a != 0) {
/* 1189 */         if (!fromToCount.containsKey(Integer.valueOf(a))) { fromToCount.put(Integer.valueOf(a), Integer.valueOf(1));
/*      */         } else {
/* 1191 */           Integer newCount = Integer.valueOf(((Integer)fromToCount.get(Integer.valueOf(a))).intValue() + 1);
/* 1192 */           fromToCount.put(Integer.valueOf(a), newCount);
/*      */         }
/*      */       }
/*      */       else {
/* 1196 */         System.out.println((String)inferHap.get(0) + ":" + (String)inferHap.get(1));
/*      */       }
/* 1178 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1200 */     fromToCount.put(Integer.valueOf(0), Integer.valueOf(sum));
/* 1201 */     return fromToCount;
/*      */   }
/*      */   
/*      */   public int[] getABcount(List<Character> geno)
/*      */   {
/* 1206 */     int[] count = new int[3];
/* 1207 */     for (int i = 0; i < geno.size(); i++) {
/* 1208 */       char a = ((Character)geno.get(i)).charValue();
/* 1209 */       if (a == '_') { count[0] += 1;
/* 1210 */       } else if (a == 'A') { count[1] += 1;
/* 1211 */       } else if (a == 'B') { count[2] += 1;
/* 1212 */       } else if (a == 'X') { count[1] += 2;
/* 1213 */       } else if (a == 'Z') { count[2] += 2;
/* 1214 */       } else if (a == 'T') { count[1] += 3;
/* 1215 */       } else if (a == 'W') { count[2] += 3;
/* 1216 */       } else if (a == 'Y') { count[1] += 1;count[2] += 1;
/* 1217 */       } else if (a == 'U') { count[1] += 2;count[2] += 1;
/* 1218 */       } else if (a == 'V') { count[1] += 1;count[2] += 2;
/* 1219 */       } else { throw new RuntimeException("!!");
/*      */       } }
/* 1221 */     return count;
/*      */   }
/*      */   
/*      */   public String codeDuplicate(List<Character> geno)
/*      */   {
/* 1226 */     int[] count = new int[3];
/* 1227 */     String code = null;
/* 1228 */     for (int i = 0; i < geno.size(); i++) {
/* 1229 */       char a = ((Character)geno.get(i)).charValue();
/* 1230 */       if (a == '_') { count[0] += 1;
/* 1231 */       } else if (a == 'A') { count[1] += 1;
/* 1232 */       } else if (a == 'B') { count[2] += 1;
/* 1233 */       } else if (a != ' ')
/* 1234 */         throw new RuntimeException("!!");
/*      */     }
/* 1236 */     if ((count[0] == 1) && (count[1] == 0) && (count[2] == 0)) code = "_";
/* 1237 */     if ((count[0] == 2) && (count[1] == 0) && (count[2] == 0)) code = "_";
/* 1238 */     if ((count[1] == 1) && (count[2] == 0)) code = "A";
/* 1239 */     if ((count[1] == 0) && (count[2] == 1)) code = "B";
/* 1240 */     if ((count[1] == 2) && (count[2] == 0)) code = "X";
/* 1241 */     if ((count[1] == 0) && (count[2] == 2)) code = "Z";
/* 1242 */     if ((count[1] == 1) && (count[2] == 1)) code = "Y";
/* 1243 */     if ((count[1] == 3) && (count[2] == 0)) code = "T";
/* 1244 */     if ((count[1] == 0) && (count[2] == 3)) code = "W";
/* 1245 */     if ((count[1] == 2) && (count[2] == 1)) code = "U";
/* 1246 */     if ((count[1] == 1) && (count[2] == 2)) code = "V";
/* 1247 */     if (code == null) {
/* 1248 */       throw new RuntimeException("!!");
/*      */     }
/* 1250 */     return code;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int[] calculateErrorRatePredictMissing()
/*      */   {
/* 1257 */     int sum = 0;
/* 1258 */     int error = 0;
/* 1259 */     for (Iterator<String> is = this.phasedData.keySet().iterator(); is.hasNext();) {
/* 1260 */       String indivID = (String)is.next();
/* 1261 */       if (this.missingIndex.containsKey(indivID)) {
/* 1262 */         List<Integer> missing = (List)this.missingIndex.get(indivID);
/* 1263 */         for (int i = 0; i < missing.size(); i++) {
/* 1264 */           sum++;
/* 1265 */           List<Character> phased = new ArrayList();
/* 1266 */           List<Character> rawed = new ArrayList();
/* 1267 */           phased.add(Character.valueOf(((String[])this.phasedData.get(indivID))[0].charAt(((Integer)missing.get(i)).intValue())));
/* 1268 */           phased.add(Character.valueOf(((String[])this.phasedData.get(indivID))[1].charAt(((Integer)missing.get(i)).intValue())));
/* 1269 */           rawed.add(((Character[])((List)this.rawData.get(indivID)).get(((Integer)missing.get(i)).intValue()))[0]);
/* 1270 */           rawed.add(((Character[])((List)this.rawData.get(indivID)).get(((Integer)missing.get(i)).intValue()))[1]);
/* 1271 */           int[] phasedcount = getABcount(phased);
/* 1272 */           int[] rawedcount = getABcount(rawed);
/* 1273 */           if ((phasedcount[0] != rawedcount[0]) || (phasedcount[1] != rawedcount[1]) || (phasedcount[2] != rawedcount[2])) {
/* 1274 */             error++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1279 */     return new int[] { error, sum };
/*      */   }
/*      */   
/*      */ 
/*      */   public void printOutCount(PrintStream ps)
/*      */   {
/* 1285 */     Map<Integer, Integer[]> fromToCount = getCountFromTo();
/* 1286 */     int sum = ((Integer[])fromToCount.get(Integer.valueOf(0)))[0].intValue();
/* 1287 */     double check = 0.0D;
/* 1288 */     for (Iterator<Integer> is = fromToCount.keySet().iterator(); is.hasNext();) {
/* 1289 */       Integer fromTo = (Integer)is.next();
/* 1290 */       ps.println(fromTo + "\t+count:" + ((Integer[])fromToCount.get(fromTo))[1] + 
/* 1291 */         "\t+rate:" + ((Integer[])fromToCount.get(fromTo))[0].intValue() / ((Integer[])fromToCount.get(fromTo))[1].intValue() + 
/* 1292 */         "\t+percentage:" + ((Integer[])fromToCount.get(fromTo))[1].intValue() / sum);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   Map<Integer, List<byte[]>> possibleExIndex;
/*      */   
/*      */ 
/*      */ 
/*      */   List<String> majorAllele;
/*      */   
/*      */ 
/*      */ 
/*      */   Map<String, Double> switchErrortateAll;
/*      */   
/*      */ 
/*      */   List<Double> incorrectRateAll;
/*      */   
/*      */ 
/*      */   Map<String, Integer[][]> errorHeteroIndex;
/*      */   
/*      */ 
/*      */   public void run()
/*      */   {
/* 1318 */     this.errorHeteroIndex = new HashMap();
/* 1319 */     this.switchErrortateAll = new HashMap();
/* 1320 */     this.incorrectRateAll = new ArrayList();
/* 1321 */     for (Iterator<String> is = this.phasedData.keySet().iterator(); is.hasNext();) {
/* 1322 */       String indivID = (String)is.next();
/*      */       
/* 1324 */       int[] count = getSwitchError(indivID);
/* 1325 */       this.switchErrortateAll.put(indivID, Double.valueOf(count[0] / count[1]));
/* 1326 */       this.incorrectRateAll.add(Double.valueOf(getIncorrectRate(indivID)));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/* 1331 */   Map<Integer, Integer> noSwitchAll = new HashMap();
/* 1332 */   Map<Integer, Integer> noHeteroSiteAll = new HashMap();
/*      */   
/* 1334 */   public void run1() { this.errorHeteroIndex = new HashMap();
/* 1335 */     for (Iterator<String> is = this.phasedData.keySet().iterator(); is.hasNext();) {
/* 1336 */       String indivID = (String)is.next();
/*      */       
/* 1338 */       int no_copies = ((String[])this.phasedData.get(indivID)).length;
/* 1339 */       if (no_copies != 1) {
/* 1340 */         int[] count = getSwitchError(indivID);
/* 1341 */         if (!this.noSwitchAll.containsKey(Integer.valueOf(no_copies))) {
/* 1342 */           this.noSwitchAll.put(Integer.valueOf(no_copies), Integer.valueOf(count[0]));
/* 1343 */           this.noHeteroSiteAll.put(Integer.valueOf(no_copies), Integer.valueOf(count[1]));
/*      */         }
/*      */         else {
/* 1346 */           this.noSwitchAll.put(Integer.valueOf(no_copies), Integer.valueOf(((Integer)this.noSwitchAll.get(Integer.valueOf(no_copies))).intValue() + count[0]));
/* 1347 */           this.noHeteroSiteAll.put(Integer.valueOf(no_copies), Integer.valueOf(((Integer)this.noHeteroSiteAll.get(Integer.valueOf(no_copies))).intValue() + count[1]));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void printErrorRate1(PrintStream ps) {
/* 1354 */     for (Iterator<Integer> in = this.noSwitchAll.keySet().iterator(); in.hasNext();) {
/* 1355 */       Integer no = (Integer)in.next();
/* 1356 */       double rate = ((Integer)this.noSwitchAll.get(no)).intValue() / ((Integer)this.noHeteroSiteAll.get(no)).intValue();
/* 1357 */       ps.println("no_copies: " + no);
/* 1358 */       ps.println(rate);
/* 1359 */       ps.println(Math.sqrt(rate * (1.0D - rate) / ((Integer)this.noHeteroSiteAll.get(no)).intValue()));
/*      */     }
/* 1361 */     ps.println("errorPredictGenotype:" + this.errorPredictGenotype);
/*      */   }
/*      */   
/*      */ 
/*      */   public void printCompareUncerSwi(PrintStream ps, int[][] count)
/*      */   {
/* 1367 */     int subSum = 0;
/* 1368 */     for (int i = 0; i < count[0].length; i++) {
/* 1369 */       subSum += count[1][i];
/*      */     }
/* 1371 */     for (int i = 0; i < count[0].length; i++) {
/* 1372 */       if ((count[1][i] != 0) && 
/* 1373 */         (count[1][i] / subSum > 0.02D)) {
/* 1374 */         double p = (i + 1) / 10.0D - 0.05D;
/* 1375 */         ps.print(p + "\t");
/* 1376 */         ps.print(count[0][i] / count[1][i] + "\t");
/* 1377 */         ps.print(count[1][i] / subSum + "\t");
/* 1378 */         ps.print(count[0][i] + "\t");
/* 1379 */         ps.println(count[1][i]);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/*      */     try
/*      */     {
/* 1389 */       switchErrorCNV se = new switchErrorCNV();
/* 1390 */       se.getHapSp2();
/* 1391 */       se.getHapSp3();
/* 1392 */       se.getHapSp4();
/* 1393 */       se.getAllPossibleExIndex();
/*      */       
/*      */ 
/* 1396 */       se.getRawData(new File("X_nfbc_internal.zip"));
/*      */       
/*      */ 
/* 1399 */       se.getPhasedData(new File("phased2_nfbc_quater_internal1"));
/*      */       
/* 1401 */       se.run1();
/* 1402 */       PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("switchErrorRate_nfbc_quater_internal3.txt"))));
/* 1403 */       se.printErrorRate1(ps);
/* 1404 */       ps.close();
/* 1405 */       PrintStream ps1 = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("switchErrorRateFromToTotal_nfbc_quater_internal3.txt"))));
/* 1406 */       se.printOutCount(ps1);
/* 1407 */       ps1.close();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (Exception exc)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1475 */       exc.printStackTrace();
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/switchErrorCNV.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */