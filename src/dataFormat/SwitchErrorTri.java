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
/*      */ public class SwitchErrorTri
/*      */ {
/*   28 */   static int[] mid = { 151801138, 154499338 };
/*      */   List<Integer> missingIndex;
/*      */   double noMissingSNP;
/*      */   
/*   32 */   public List<String> getIndiv(ZipFile f, String entryName, Integer column) throws Exception { BufferedReader nxt = 
/*   33 */       new BufferedReader(new InputStreamReader(
/*   34 */       f.getInputStream(f.getEntry(entryName))));
/*   35 */     List<String> indiv = new ArrayList();
/*   36 */     String st = "";
/*   37 */     for (int k = 0; (st = nxt.readLine()) != null; k++) {
/*   38 */       indiv.add(st.split("\t")[column.intValue()]);
/*      */     }
/*   40 */     nxt.close();
/*   41 */     return indiv; }
/*      */   
/*      */   Map<String, List<Double>> uncertaintyScore;
/*      */   Map<String, List<Character[]>> rawData;
/*   45 */   public static List<String> getSNP(ZipFile f, String entryName) throws Exception { BufferedReader nxt = 
/*   46 */       new BufferedReader(new InputStreamReader(
/*   47 */       f.getInputStream(f.getEntry(entryName))));
/*   48 */     List<String> snp = new ArrayList();
/*   49 */     String st = "";
/*   50 */     while ((st = nxt.readLine()) != null) {
/*   51 */       String[] str = st.split("\t");
/*   52 */       if ((Integer.parseInt(str[1]) >= mid[0]) && (Integer.parseInt(str[2]) <= mid[1]))
/*   53 */         snp.add(str[3]);
/*      */     }
/*   55 */     nxt.close();
/*   56 */     return snp;
/*      */   }
/*      */   
/*      */   public void getMissingIndex(File f)
/*      */     throws Exception
/*      */   {
/*   62 */     this.missingIndex = new ArrayList();
/*   63 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*   64 */     String st = "";
/*   65 */     br.readLine();br.readLine();
/*   66 */     while ((st = br.readLine()) != null) {
/*   67 */       this.missingIndex.add(Integer.valueOf(Integer.parseInt(st.split("\\s+")[0])));
/*      */     }
/*      */   }
/*      */   
/*      */   Map<String, List<Integer>> rawDataMissing;
/*      */   Map<String, List<Character[]>> rawDataDart;
/*      */   Map<String, String[]> phasedData;
/*      */   List<String> phasedIndID;
/*      */   public double getErrorImpute()
/*      */   {
/*   77 */     int error = 0;
/*   78 */     String indivID; int m; for (Iterator<String> is = this.phasedData.keySet().iterator(); is.hasNext(); 
/*      */         
/*      */ 
/*   81 */         m < ((String[])this.phasedData.get(indivID))[0].length())
/*      */     {
/*   79 */       indivID = (String)is.next();
/*   80 */       this.noMissingSNP = (this.phasedData.size() * ((String[])this.phasedData.get(indivID))[0].length() * 0.01D);
/*   81 */       m = 0; continue;
/*   82 */       int[] phasedCount = new int[2];
/*   83 */       int[] rawCount = new int[2];
/*   84 */       for (int c = 0; c < ((String[])this.phasedData.get(indivID)).length; c++) {
/*   85 */         if (((String[])this.phasedData.get(indivID))[c].charAt(m) == 'A') phasedCount[0] += 1; else
/*   86 */           phasedCount[1] += 1;
/*   87 */         if (((Character[])((List)this.rawData.get(indivID)).get(m))[c].equals(Character.valueOf('A'))) rawCount[0] += 1; else
/*   88 */           rawCount[1] += 1;
/*      */       }
/*   90 */       if ((phasedCount[0] != rawCount[0]) || (phasedCount[1] != rawCount[1])) {
/*   91 */         error++;
/*      */       }
/*   81 */       m++;
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
/*   95 */     double errorImputeRate = error / this.noMissingSNP;
/*   96 */     return errorImputeRate;
/*      */   }
/*      */   
/*      */   public Map<String, List<Double>> getUncertainty(File f)
/*      */     throws Exception
/*      */   {
/*  102 */     this.uncertaintyScore = new HashMap();
/*  103 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  104 */     String[] headline = br.readLine().split("\t");
/*  105 */     for (int i = 6; i < headline.length; i += 3) {
/*  106 */       this.uncertaintyScore.put(headline[i].replace(" ", ""), new ArrayList());
/*      */     }
/*  108 */     String st = "";
/*  109 */     int i; for (; (st = br.readLine()) != null; 
/*      */         
/*  111 */         i < headline.length)
/*      */     {
/*  110 */       String[] temp = st.split("\t");
/*  111 */       i = 7; continue;
/*  112 */       ((List)this.uncertaintyScore.get(headline[i].replace(" ", ""))).add(Double.valueOf(Double.parseDouble(temp[i])));i += 3;
/*      */     }
/*      */     
/*      */ 
/*  115 */     return this.uncertaintyScore;
/*      */   }
/*      */   
/*      */   public Map<String, List<Double>> getUncertaintyImpute(File f) throws Exception
/*      */   {
/*  120 */     this.uncertaintyScore = new HashMap();
/*  121 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  122 */     String[] headline = br.readLine().split("\t");
/*  123 */     for (int i = 6; i < headline.length; i += 3) {
/*  124 */       this.uncertaintyScore.put(headline[i], new ArrayList());
/*      */     }
/*  126 */     String st = "";
/*  127 */     int i; for (; (st = br.readLine()) != null; 
/*      */         
/*  129 */         i < headline.length)
/*      */     {
/*  128 */       String[] temp = st.split("\t");
/*  129 */       i = 8; continue;
/*  130 */       ((List)this.uncertaintyScore.get(headline[i])).add(Double.valueOf(Double.parseDouble(temp[i].split("_")[1])));i += 3;
/*      */     }
/*      */     
/*      */ 
/*  133 */     return this.uncertaintyScore;
/*      */   }
/*      */   
/*      */ 
/*      */   public void getRawData(File f)
/*      */     throws Exception
/*      */   {
/*  140 */     ZipFile zf = new ZipFile(f);
/*  141 */     this.rawData = new HashMap();
/*  142 */     this.rawDataMissing = new HashMap();
/*  143 */     List<String> indiv = new ArrayList();
/*  144 */     indiv = getIndiv(zf, "Samples", Integer.valueOf(0));
/*  145 */     List<String> snp = new ArrayList();
/*  146 */     snp = getSNP(zf, "Snps");
/*  147 */     String st = "";
/*  148 */     int count = 0;
/*  149 */     for (int i = 0; i < snp.size(); i++) {
/*  150 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)snp.get(i)))));
/*  151 */       if (this.rawData.isEmpty()) {
/*  152 */         for (int s = 0; s < indiv.size(); s++) {
/*  153 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  154 */           Character[] temp = new Character[st.length()];
/*  155 */           for (int k = 0; k < temp.length; k++) {
/*  156 */             temp[k] = Character.valueOf(st.charAt(k));
/*      */           }
/*  158 */           List<Character[]> temp1 = new ArrayList();
/*  159 */           List<Integer> temp2 = new ArrayList();
/*  160 */           temp1.add(temp);
/*  161 */           this.rawData.put((String)indiv.get(s), temp1);
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*  170 */         for (int s = 0; s < indiv.size(); s++) {
/*  171 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  172 */           Character[] temp = new Character[st.length()];
/*  173 */           for (int k = 0; k < temp.length; k++) {
/*  174 */             temp[k] = Character.valueOf(st.charAt(k));
/*      */           }
/*  176 */           ((List)this.rawData.get(indiv.get(s))).add(temp);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void getRawDataDart(File f)
/*      */     throws Exception
/*      */   {
/*  189 */     ZipFile zf = new ZipFile(f);
/*  190 */     this.rawDataDart = new HashMap();
/*  191 */     List<String> indiv = new ArrayList();
/*  192 */     indiv = getIndiv(zf, "Samples", Integer.valueOf(0));
/*  193 */     List<String> snp = new ArrayList();
/*  194 */     snp = getSNP(zf, "Snps");
/*  195 */     String st = "";
/*  196 */     for (int i = 0; i < snp.size(); i++) {
/*  197 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)snp.get(i)))));
/*  198 */       if (this.rawDataDart.isEmpty()) {
/*  199 */         for (int s = 0; s < indiv.size(); s++) {
/*  200 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  201 */           Character[] temp = new Character[st.length()];
/*  202 */           for (int k = 0; k < temp.length; k++) {
/*  203 */             temp[k] = Character.valueOf(st.charAt(k));
/*      */           }
/*  205 */           List<Character[]> temp1 = new ArrayList();
/*  206 */           temp1.add(temp);
/*  207 */           this.rawDataDart.put((String)indiv.get(s), temp1);
/*      */         }
/*  209 */         if ((st = nxt.readLine()) != null) throw new RuntimeException("!!");
/*      */       }
/*      */       else {
/*  212 */         for (int s = 0; s < indiv.size(); s++) {
/*  213 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  214 */           Character[] temp = new Character[st.length()];
/*  215 */           for (int k = 0; k < temp.length; k++) {
/*  216 */             temp[k] = Character.valueOf(st.charAt(k));
/*      */           }
/*  218 */           ((List)this.rawDataDart.get(indiv.get(s))).add(temp);
/*      */         }
/*  220 */         if ((st = nxt.readLine()) != null) { throw new RuntimeException("!!");
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void getPhasedData(File f)
/*      */     throws Exception
/*      */   {
/*  229 */     this.phasedData = new HashMap();
/*  230 */     this.phasedIndID = new ArrayList();
/*  231 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  232 */     String st = br.readLine();
/*  233 */     while (!st.startsWith("#")) {
/*  234 */       st = br.readLine();
/*      */     }
/*  236 */     while (st != null) {
/*  237 */       if (st.startsWith("#")) {
/*  238 */         String[] str = st.split("\\s+");
/*  239 */         List<String> te = new ArrayList();
/*  240 */         st = br.readLine();
/*  241 */         while (!st.startsWith("#")) {
/*  242 */           te.add(st);
/*  243 */           st = br.readLine();
/*  244 */           if (st == null) break;
/*      */         }
/*  246 */         String[] temp = new String[te.size()];
/*  247 */         for (int i = 0; i < temp.length; i++) {
/*  248 */           temp[i] = ((String)te.get(i));
/*      */         }
/*  250 */         this.phasedData.put(str[2].replace("|", ""), temp);
/*  251 */         this.phasedIndID.add(str[2].replace('|', ' '));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void getFastPhasedData(File f) throws Exception
/*      */   {
/*  258 */     this.phasedData = new HashMap();
/*      */     
/*  260 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  261 */     String st = br.readLine();
/*  262 */     while (!st.startsWith("#")) {
/*  263 */       st = br.readLine();
/*      */     }
/*  265 */     while (st != null) {
/*  266 */       if (st.startsWith("#")) {
/*  267 */         String[] str = st.split("\\s+");
/*  268 */         String[] temp = new String[2];
/*  269 */         for (int i = 0; i < temp.length; i++) {
/*  270 */           temp[i] = br.readLine().replaceAll(" ", "");
/*      */         }
/*  272 */         this.phasedData.put(str[1], temp);
/*  273 */         st = br.readLine();
/*      */       }
/*  275 */       if (st.startsWith("END")) {
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public List<Integer> getHeteroIndex(String indivID, int no_copies) {
/*  282 */     List<Integer> heteroIndex = new ArrayList();
/*  283 */     for (int i = 0; i < ((String[])this.phasedData.get(indivID))[0].length(); i++) {
/*  284 */       char a = ((String[])this.phasedData.get(indivID))[0].charAt(i);
/*  285 */       String heter = null;
/*  286 */       for (int k = 1; k < no_copies; k++) {
/*  287 */         if (((String[])this.phasedData.get(indivID))[k].charAt(i) != a) heter = "yes";
/*      */       }
/*  289 */       if (heter != null) heteroIndex.add(Integer.valueOf(i));
/*      */     }
/*  291 */     return heteroIndex;
/*      */   }
/*      */   
/*      */   public List<Integer> getBoundaryIndex(String indivID, List<Integer> heteroIndex)
/*      */   {
/*  296 */     List<Integer> boundaryIndex = new ArrayList();
/*  297 */     boundaryIndex.add((Integer)heteroIndex.get(0));
/*  298 */     for (int i = 1; i < heteroIndex.size(); i++) {
/*  299 */       if ((((String[])this.phasedData.get(indivID))[0].charAt(((Integer)heteroIndex.get(i - 1)).intValue()) != ((String[])this.phasedData.get(indivID))[0].charAt(((Integer)heteroIndex.get(i)).intValue())) || 
/*  300 */         (((String[])this.phasedData.get(indivID))[1].charAt(((Integer)heteroIndex.get(i - 1)).intValue()) != ((String[])this.phasedData.get(indivID))[1].charAt(((Integer)heteroIndex.get(i)).intValue())) || 
/*  301 */         (((String[])this.phasedData.get(indivID))[2].charAt(((Integer)heteroIndex.get(i - 1)).intValue()) != ((String[])this.phasedData.get(indivID))[2].charAt(((Integer)heteroIndex.get(i)).intValue()))) {
/*  302 */         boundaryIndex.add((Integer)heteroIndex.get(i));
/*      */       }
/*      */     }
/*  305 */     return boundaryIndex;
/*      */   }
/*      */   
/*      */ 
/*  309 */   List<String> hapSp2 = new ArrayList();
/*      */   
/*  311 */   public void getHapSp2() { this.hapSp2.add("AA");
/*  312 */     this.hapSp2.add("AB");
/*  313 */     this.hapSp2.add("BA");
/*  314 */     this.hapSp2.add("BB");
/*      */   }
/*      */   
/*  317 */   List<String> hapSp3 = new ArrayList();
/*  318 */   Map<Integer, List<Integer>> group = new HashMap();
/*  319 */   List<Integer> confusedGroup1 = new ArrayList();
/*  320 */   List<Integer> confusedGroup2 = new ArrayList();
/*  321 */   List<Integer> confusedGroup3 = new ArrayList();
/*      */   
/*  323 */   public void getHapSp3() { this.hapSp3.add("AAA");
/*  324 */     this.hapSp3.add("AAB");this.hapSp3.add("ABA");this.hapSp3.add("BAA");
/*  325 */     this.hapSp3.add("ABB");this.hapSp3.add("BAB");this.hapSp3.add("BBA");
/*  326 */     this.hapSp3.add("BBB");
/*  327 */     this.hapSp3.add("AA_");this.hapSp3.add("A_A");this.hapSp3.add("_AA");
/*  328 */     this.hapSp3.add("AB_");this.hapSp3.add("A_B");this.hapSp3.add("_AB");
/*  329 */     this.hapSp3.add("BA_");this.hapSp3.add("B_A");this.hapSp3.add("_BA");
/*  330 */     this.hapSp3.add("BB_");this.hapSp3.add("B_B");this.hapSp3.add("_BB");
/*  331 */     this.hapSp3.add("A__");this.hapSp3.add("_A_");this.hapSp3.add("__A");
/*  332 */     this.hapSp3.add("B__");this.hapSp3.add("_B_");this.hapSp3.add("__B");
/*  333 */     this.hapSp3.add("___");
/*  334 */     List<Integer> temp1 = new ArrayList();
/*  335 */     List<Integer> temp2 = new ArrayList();
/*  336 */     List<Integer> temp3 = new ArrayList();
/*  337 */     List<Integer> temp4 = new ArrayList();
/*  338 */     List<Integer> temp5 = new ArrayList();
/*  339 */     List<Integer> temp6 = new ArrayList();
/*  340 */     List<Integer> temp7 = new ArrayList();
/*  341 */     for (int i = 1; i < 27; i++) {
/*  342 */       if (i < 4) {
/*  343 */         temp1.add(Integer.valueOf(i));
/*      */       }
/*  345 */       this.group.put(Integer.valueOf(1), temp1);
/*  346 */       if ((i > 3) && (i < 7)) {
/*  347 */         temp2.add(Integer.valueOf(i));
/*      */       }
/*  349 */       this.group.put(Integer.valueOf(2), temp2);
/*  350 */       if ((i > 7) && (i < 11)) {
/*  351 */         temp3.add(Integer.valueOf(i));
/*      */       }
/*  353 */       this.group.put(Integer.valueOf(3), temp3);
/*  354 */       if ((i > 10) && (i < 17)) {
/*  355 */         temp4.add(Integer.valueOf(i));
/*      */       }
/*  357 */       this.group.put(Integer.valueOf(4), temp4);
/*  358 */       if ((i > 16) && (i < 20)) {
/*  359 */         temp5.add(Integer.valueOf(i));
/*      */       }
/*  361 */       this.group.put(Integer.valueOf(5), temp5);
/*  362 */       if ((i > 19) && (i < 23)) {
/*  363 */         temp6.add(Integer.valueOf(i));
/*      */       }
/*  365 */       this.group.put(Integer.valueOf(6), temp6);
/*  366 */       if ((i > 22) && (i < 26)) {
/*  367 */         temp7.add(Integer.valueOf(i));
/*      */       }
/*  369 */       this.group.put(Integer.valueOf(7), temp7);
/*      */     }
/*  371 */     this.confusedGroup1.add(Integer.valueOf(2));this.confusedGroup1.add(Integer.valueOf(5));this.confusedGroup1.add(Integer.valueOf(9));this.confusedGroup1.add(Integer.valueOf(18));this.confusedGroup1.add(Integer.valueOf(21));this.confusedGroup1.add(Integer.valueOf(24));
/*  372 */     this.confusedGroup2.add(Integer.valueOf(1));this.confusedGroup2.add(Integer.valueOf(6));this.confusedGroup2.add(Integer.valueOf(8));this.confusedGroup2.add(Integer.valueOf(17));this.confusedGroup2.add(Integer.valueOf(22));this.confusedGroup2.add(Integer.valueOf(25));
/*  373 */     this.confusedGroup3.add(Integer.valueOf(3));this.confusedGroup3.add(Integer.valueOf(4));this.confusedGroup3.add(Integer.valueOf(10));this.confusedGroup3.add(Integer.valueOf(19));this.confusedGroup3.add(Integer.valueOf(23));this.confusedGroup3.add(Integer.valueOf(20));
/*      */   }
/*      */   
/*      */ 
/*  377 */   List<String> hapSp4 = new ArrayList();
/*  378 */   List<Integer> group3 = new ArrayList();
/*  379 */   List<Integer> group4 = new ArrayList();
/*  380 */   List<Integer> group5 = new ArrayList();
/*      */   
/*  382 */   public void getHapSp4() { this.hapSp4.add("AAAA");
/*  383 */     this.hapSp4.add("AAAB");this.hapSp4.add("AABA");this.hapSp4.add("ABAA");this.hapSp4.add("BAAA");
/*  384 */     this.hapSp4.add("AABB");this.hapSp4.add("ABAB");this.hapSp4.add("ABBA");this.hapSp4.add("BAAB");this.hapSp4.add("BBAA");this.hapSp4.add("BABA");
/*  385 */     this.hapSp4.add("ABBB");this.hapSp4.add("BABB");this.hapSp4.add("BBAB");this.hapSp4.add("BBBA");
/*  386 */     this.hapSp4.add("BBBB");
/*  387 */     for (int i = 1; i < 15; i++) {
/*  388 */       if (i < 5) this.group3.add(Integer.valueOf(i));
/*  389 */       if ((i > 4) && (i < 11)) this.group4.add(Integer.valueOf(i));
/*  390 */       if (i > 10) this.group5.add(Integer.valueOf(i));
/*      */     }
/*      */   }
/*      */   
/*      */   Map<Integer, List<byte[]>> possibleExIndex;
/*  395 */   public int getSp2Index(String aa) { int i = -1;
/*  396 */     while (i < this.hapSp2.size()) {
/*  397 */       i++;
/*  398 */       if (((String)this.hapSp2.get(i)).equals(aa)) break;
/*      */     }
/*  400 */     return i;
/*      */   }
/*      */   
/*      */   public int getSp2GenoIndex(String aa) {
/*  404 */     int i = -1;
/*  405 */     while (i < this.hapSp2.size()) {
/*  406 */       i++;
/*  407 */       if (((String)this.hapSp2.get(i)).equals(aa)) break;
/*      */     }
/*  409 */     if (i > 1) return i - 1;
/*  410 */     return i;
/*      */   }
/*      */   
/*      */   public int getSpIndex(List<String> hapSp, String aa) {
/*  414 */     int i = -1;
/*  415 */     while (i < hapSp.size()) {
/*  416 */       i++;
/*  417 */       if (((String)hapSp.get(i)).equals(aa)) break;
/*      */     }
/*  419 */     return i;
/*      */   }
/*      */   
/*      */   public List<Integer> getSp3PossibleIndex(Integer index)
/*      */   {
/*  424 */     List<Integer> temp = new ArrayList();
/*  425 */     for (Iterator<Integer> is = this.group.keySet().iterator(); is.hasNext();) {
/*  426 */       Integer groupIndex = (Integer)is.next();
/*  427 */       temp = (List)this.group.get(groupIndex);
/*  428 */       if (temp.contains(index)) {
/*      */         break;
/*      */       }
/*      */     }
/*  432 */     if (temp.isEmpty()) {
/*  433 */       throw new RuntimeException("no machted possible Index");
/*      */     }
/*  435 */     return temp;
/*      */   }
/*      */   
/*      */   public List<Integer> getSp4PossibleIndex(int index) {
/*  439 */     if (this.group3.contains(Integer.valueOf(index))) {
/*  440 */       return this.group3;
/*      */     }
/*  442 */     if (this.group4.contains(Integer.valueOf(index))) {
/*  443 */       return this.group4;
/*      */     }
/*  445 */     if (this.group5.contains(Integer.valueOf(index))) {
/*  446 */       return this.group5;
/*      */     }
/*  448 */     throw new RuntimeException("no machted possible Index");
/*      */   }
/*      */   
/*      */   List<String> majorAllele;
/*      */   public void getAllPossibleExIndex()
/*      */   {
/*  454 */     this.possibleExIndex = new HashMap();
/*  455 */     List<byte[]> temp = new ArrayList();
/*  456 */     temp.add(new byte[] { 1, 0, 3, 2 });temp.add(new byte[] { 1, 2, 3 });temp.add(new byte[] { 3, 0, 1, 2 });temp.add(new byte[] { 3, 2, 1 });
/*  457 */     this.possibleExIndex.put(Integer.valueOf(1), temp);
/*  458 */     temp = new ArrayList();
/*  459 */     temp.add(new byte[] { 2, 3, 0, 1 });temp.add(new byte[] { 2, 3, 1 });temp.add(new byte[] { 3, 2, 0, 1 });temp.add(new byte[] { 3, 2, 1 });
/*  460 */     this.possibleExIndex.put(Integer.valueOf(2), temp);
/*  461 */     temp = new ArrayList();
/*  462 */     temp.add(new byte[] { 1, 0, 3, 2 });temp.add(new byte[] { 1, 3, 0, 2 });temp.add(new byte[] { 2, 0, 3, 1 });temp.add(new byte[] { 2, 3, 0, 1 });
/*  463 */     this.possibleExIndex.put(Integer.valueOf(3), temp);
/*      */   }
/*      */   
/*      */   public int getAmbigousIndex(List<Integer> index) {
/*  467 */     int amIndex = 0;
/*  468 */     List<Integer> temp = new ArrayList();
/*  469 */     temp.add(Integer.valueOf(6));temp.add(Integer.valueOf(10));
/*  470 */     if (index.containsAll(temp)) {
/*  471 */       amIndex = 1;
/*      */     }
/*  473 */     temp = new ArrayList();
/*  474 */     temp.add(Integer.valueOf(5));temp.add(Integer.valueOf(9));
/*  475 */     if (index.containsAll(temp)) {
/*  476 */       amIndex = 2;
/*      */     }
/*  478 */     temp = new ArrayList();
/*  479 */     temp.add(Integer.valueOf(7));temp.add(Integer.valueOf(8));
/*  480 */     if (index.containsAll(temp)) {
/*  481 */       amIndex = 3;
/*      */     }
/*  483 */     return amIndex;
/*      */   }
/*      */   
/*      */   Map<String, List<Integer>> errorPos;
/*      */   List<Double> dartErrorRate;
/*      */   public List<String> getHapFromPhasedData(String indivID, int i, List<Integer> heteroIndex, Integer hapSpIndex, int noIndex, List<String> hapSp)
/*      */   {
/*  490 */     List<String> inferHap = new ArrayList();
/*  491 */     for (int k = 0; k < ((String[])this.phasedData.get(indivID)).length; k++) {
/*  492 */       char[] temp = new char[noIndex];
/*  493 */       int count = noIndex - 1;
/*  494 */       for (int n = 0; n < noIndex - 1; n++) {
/*  495 */         temp[n] = ((String[])this.phasedData.get(indivID))[k].charAt(((Integer)heteroIndex.get(i - count)).intValue());
/*  496 */         count--;
/*      */       }
/*  498 */       temp[(noIndex - 1)] = ((String)hapSp.get(hapSpIndex.intValue())).charAt(k);
/*  499 */       String s = new String(temp);
/*  500 */       inferHap.add(s);
/*      */     }
/*  502 */     return inferHap;
/*      */   }
/*      */   
/*      */   public List<String> getHapFromIndex(int[] hapSpIndex, List<String> hapSp, int no_copies)
/*      */   {
/*  507 */     List<String> inferHap = new ArrayList();
/*  508 */     for (int i = 0; i < no_copies; i++) {
/*  509 */       char[] temp = new char[hapSpIndex.length];
/*  510 */       for (int k = 0; k < hapSpIndex.length; k++) {
/*  511 */         temp[k] = ((String)hapSp.get(hapSpIndex[k])).charAt(i);
/*      */       }
/*  513 */       String s = new String(temp);
/*  514 */       inferHap.add(s);
/*      */     }
/*  516 */     return inferHap;
/*      */   }
/*      */   
/*      */   public String getGenotypeString(String indivID, int i, List<Integer> heteroIndex)
/*      */   {
/*  521 */     char[] geno = new char[((String[])this.phasedData.get(indivID)).length];
/*  522 */     for (int k = 0; k < geno.length; k++) {
/*  523 */       geno[k] = ((String[])this.phasedData.get(indivID))[k].charAt(((Integer)heteroIndex.get(i)).intValue());
/*      */     }
/*  525 */     String s = new String(geno);
/*  526 */     return s;
/*      */   }
/*      */   
/*      */   public String getGenoStringSwiched(String indivID, int i, List<Integer> heteroIndex, List<byte[]> newIndexTrueHap)
/*      */   {
/*  531 */     char[] geno = new char[((String[])this.phasedData.get(indivID)).length];
/*  532 */     for (int k = 0; k < geno.length; k++) {
/*  533 */       geno[k] = ((String[])this.phasedData.get(indivID))[((byte[])newIndexTrueHap.get(i))[k]].charAt(((Integer)heteroIndex.get(i)).intValue());
/*      */     }
/*  535 */     String s = new String(geno);
/*  536 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */   public List<byte[]> getExchangeIndex(String a1, String a2, List<byte[]> newIndexTrueHapList, int no_copies)
/*      */   {
/*  542 */     byte[] newIndexTrueHap = new byte[3];
/*  543 */     byte[] dif = new byte[2];
/*  544 */     byte c = 0;
/*  545 */     for (byte i = 0; i < no_copies; i = (byte)(i + 1)) {
/*  546 */       if (a1.charAt(i) != a2.charAt(i)) {
/*  547 */         dif[c] = i;
/*  548 */         c = (byte)(c + 1);
/*      */       }
/*      */     }
/*  551 */     for (byte i = 0; i < no_copies; i = (byte)(i + 1)) {
/*  552 */       if (i == dif[0]) { newIndexTrueHap[i] = dif[1];
/*  553 */       } else if (i == dif[1]) newIndexTrueHap[i] = dif[0]; else {
/*  554 */         newIndexTrueHap[i] = i;
/*      */       }
/*      */     }
/*  557 */     List<byte[]> newIndexTrueHapList1 = new ArrayList();
/*  558 */     if (!newIndexTrueHapList.isEmpty()) {
/*  559 */       for (int k = 0; k < newIndexTrueHapList.size(); k++) {
/*  560 */         byte[] newIndexTrueHap1 = new byte[3];
/*  561 */         for (int i = 0; i < no_copies; i++) {
/*  562 */           if (i == dif[0]) { newIndexTrueHap1[i] = ((byte[])newIndexTrueHapList.get(k))[dif[1]];
/*  563 */           } else if (i == dif[1]) newIndexTrueHap1[i] = ((byte[])newIndexTrueHapList.get(k))[dif[0]]; else
/*  564 */             newIndexTrueHap1[i] = ((byte[])newIndexTrueHapList.get(k))[i];
/*      */         }
/*  566 */         newIndexTrueHapList1.add(newIndexTrueHap1);
/*      */       }
/*      */     }
/*      */     
/*  570 */     newIndexTrueHapList1.add(newIndexTrueHap);
/*  571 */     return newIndexTrueHapList1;
/*      */   }
/*      */   
/*      */   public List<byte[]> getExchangeIndex4(String a1, String a2, List<byte[]> newIndexTrueHapList, int no_copies)
/*      */   {
/*  576 */     byte[] newIndexTrueHap = new byte[no_copies];
/*  577 */     byte[] dif = new byte[2];
/*  578 */     byte c = 0;
/*  579 */     for (byte i = 0; i < no_copies; i = (byte)(i + 1)) {
/*  580 */       if (a1.charAt(i) != a2.charAt(i)) {
/*  581 */         dif[c] = i;
/*  582 */         c = (byte)(c + 1);
/*      */       }
/*      */     }
/*  585 */     for (byte i = 0; i < no_copies; i = (byte)(i + 1)) {
/*  586 */       if (i == dif[0]) { newIndexTrueHap[i] = dif[1];
/*  587 */       } else if (i == dif[1]) newIndexTrueHap[i] = dif[0]; else {
/*  588 */         newIndexTrueHap[i] = i;
/*      */       }
/*      */     }
/*  591 */     List<byte[]> newIndexTrueHapList1 = new ArrayList();
/*  592 */     if (!newIndexTrueHapList.isEmpty()) {
/*  593 */       for (int k = 0; k < newIndexTrueHapList.size(); k++) {
/*  594 */         byte[] newIndexTrueHap1 = new byte[no_copies];
/*  595 */         for (int i = 0; i < no_copies; i++) {
/*  596 */           if (i == dif[0]) { newIndexTrueHap1[i] = ((byte[])newIndexTrueHapList.get(k))[dif[1]];
/*  597 */           } else if (i == dif[1]) newIndexTrueHap1[i] = ((byte[])newIndexTrueHapList.get(k))[dif[0]]; else
/*  598 */             newIndexTrueHap1[i] = ((byte[])newIndexTrueHapList.get(k))[i];
/*      */         }
/*  600 */         newIndexTrueHapList1.add(newIndexTrueHap1);
/*      */       }
/*      */     }
/*      */     
/*  604 */     newIndexTrueHapList1.add(newIndexTrueHap);
/*  605 */     return newIndexTrueHapList1;
/*      */   }
/*      */   
/*      */   public List<byte[]> getExchangeIndexAll(byte[] onePossible, List<byte[]> newIndexTrueHapList, int no_copies)
/*      */   {
/*  610 */     List<byte[]> newIndexTrueHapList1 = new ArrayList();
/*  611 */     for (int k = 0; k < newIndexTrueHapList.size(); k++) {
/*  612 */       byte[] newIndexTrueHap1 = new byte[no_copies];
/*  613 */       for (int i = 0; i < no_copies; i++) {
/*  614 */         newIndexTrueHap1[i] = ((byte[])newIndexTrueHapList.get(k))[onePossible[i]];
/*      */       }
/*  616 */       newIndexTrueHapList1.add(newIndexTrueHap1);
/*      */     }
/*  618 */     newIndexTrueHapList1.add(onePossible);
/*  619 */     return newIndexTrueHapList1;
/*      */   }
/*      */   
/*      */   public int getNoDiffHap(int[] hapSpIndex, List<String> hapSp, int no_copies)
/*      */   {
/*  624 */     List<String> hapList = getHapFromIndex(hapSpIndex, hapSp, no_copies);
/*  625 */     List<String> newHapList = new ArrayList();
/*  626 */     for (int i = 0; i < hapList.size(); i++) {
/*  627 */       if (!newHapList.contains(hapList.get(i))) newHapList.add((String)hapList.get(i));
/*      */     }
/*  629 */     int noDiffHap = newHapList.size();
/*  630 */     return noDiffHap;
/*      */   }
/*      */   
/*      */   public int[] getA2B2count(String indivID, List<String> hapSp, int no_copies) {
/*  634 */     int count = 0;
/*  635 */     List<Integer> heteroIndex = getHeteroIndex(indivID, no_copies);
/*  636 */     for (int i = 0; i < heteroIndex.size(); i++) {
/*  637 */       if (this.group4.contains(Integer.valueOf(getSpIndex(hapSp, getGenotypeString(indivID, i, heteroIndex))))) count++;
/*      */     }
/*  639 */     int[] ratio = { count, heteroIndex.size() };
/*  640 */     return ratio;
/*      */   }
/*      */   
/*      */   public void getMajorAllel(File f) throws Exception
/*      */   {
/*  645 */     this.majorAllele = new ArrayList();
/*  646 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*      */     String st;
/*  648 */     while ((st = br.readLine()) != null) { String st;
/*  649 */       String[] str = st.split("\t");
/*  650 */       if ((Integer.parseInt(str[1]) >= mid[0]) && (Integer.parseInt(str[2]) <= mid[1])) {
/*  651 */         this.majorAllele.add(str[4]);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void getDartErrorRate()
/*      */   {
/*  660 */     this.dartErrorRate = new ArrayList();
/*  661 */     this.errorPos = new HashMap();
/*  662 */     this.counts = new ArrayList();
/*  663 */     for (int i = 0; i < this.majorAllele.size(); i++) {
/*  664 */       int noDart = 0;
/*  665 */       int noError = 0;
/*  666 */       int c1 = 0;int c2 = 0;int c3 = 0;int c4 = 0;
/*  667 */       int[] count = new int[5];
/*  668 */       for (Iterator<String> it = this.rawData.keySet().iterator(); it.hasNext();) {
/*  669 */         String index = (String)it.next();
/*  670 */         int no_copies = ((String[])this.phasedData.get(index)).length;
/*  671 */         if (this.majorAllele.size() != ((List)this.rawData.get(index)).size()) { throw new RuntimeException("majoralleled are notmatched with raw data");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  676 */         if (((Character[])((List)this.rawDataDart.get(index)).get(i))[0].charValue() == 'N') {
/*  677 */           noDart++;
/*  678 */           char[] temp = new char[no_copies];
/*  679 */           char[] temp1 = new char[no_copies];
/*  680 */           for (int k = 0; k < no_copies; k++) {
/*  681 */             temp[k] = ((String[])this.phasedData.get(index))[k].charAt(i);
/*  682 */             temp1[k] = ((Character[])((List)this.rawData.get(index)).get(i))[k].charValue();
/*      */           }
/*  684 */           String ph = new String(temp);
/*  685 */           String ra = new String(temp1);
/*  686 */           if (getSp2GenoIndex(ra) != getSp2GenoIndex(ph)) {
/*  687 */             noError++;
/*  688 */             if (this.errorPos.get(index) == null) {
/*  689 */               List<Integer> errorList = new ArrayList();
/*  690 */               errorList.add(Integer.valueOf(i));
/*  691 */               this.errorPos.put(index, errorList);
/*      */             }
/*      */             else {
/*  694 */               ((List)this.errorPos.get(index)).add(Integer.valueOf(i));
/*      */             }
/*  696 */             System.out.print(getSp2GenoIndex(ra));System.out.print(":");System.out.println(getSp2GenoIndex(ph));
/*      */           }
/*  698 */           if (getSp2GenoIndex(ph) == 1) c3++; else
/*  699 */             c4++;
/*  700 */           if (getSp2GenoIndex(ra) == 1) c1++; else {
/*  701 */             c2++;
/*      */           }
/*      */         }
/*      */       }
/*  705 */       count[0] = c1;count[1] = c2;count[2] = c3;count[3] = c4;count[4] = noDart;
/*  706 */       this.counts.add(count);
/*  707 */       this.dartErrorRate.add(Double.valueOf(noError / noDart));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public int getFromToCopies(List<String> inferHap)
/*      */   {
/*  714 */     int from = 0;
/*  715 */     int to = 0;
/*  716 */     for (int i = 0; i < inferHap.size(); i++) {
/*  717 */       if ((((String)inferHap.get(i)).charAt(0) == 'A') || (((String)inferHap.get(i)).charAt(0) == 'B')) from++;
/*  718 */       if ((((String)inferHap.get(i)).charAt(0) == 'X') || (((String)inferHap.get(i)).charAt(0) == 'Y') || (((String)inferHap.get(i)).charAt(0) == 'Z')) from += 2;
/*  719 */       if ((((String)inferHap.get(i)).charAt(0) == 'U') || (((String)inferHap.get(i)).charAt(0) == 'V') || (((String)inferHap.get(i)).charAt(0) == 'W') || (((String)inferHap.get(i)).charAt(0) == 'T')) from += 3;
/*      */     }
/*  721 */     for (int i = 0; i < inferHap.size(); i++) {
/*  722 */       if ((((String)inferHap.get(i)).charAt(1) == 'A') || (((String)inferHap.get(i)).charAt(1) == 'B')) to++;
/*  723 */       if ((((String)inferHap.get(i)).charAt(1) == 'X') || (((String)inferHap.get(i)).charAt(1) == 'Y') || (((String)inferHap.get(i)).charAt(1) == 'Z')) to += 2;
/*  724 */       if ((((String)inferHap.get(i)).charAt(1) == 'U') || (((String)inferHap.get(i)).charAt(1) == 'V') || (((String)inferHap.get(i)).charAt(1) == 'W') || (((String)inferHap.get(i)).charAt(1) == 'T')) to += 3;
/*      */     }
/*  726 */     if ((from == 0) || (to == 0)) throw new RuntimeException("!!");
/*  727 */     return from * 10 + to;
/*      */   }
/*      */   
/*      */   public int[] getSwitchError(String indivID) {
/*  731 */     if (((List)this.rawData.get(indivID)).size() != ((String[])this.phasedData.get(indivID))[0].length()) {
/*  732 */       throw new RuntimeException(((String[])this.phasedData.get(indivID))[0].length() + "# of snp on phasedHap !=" + ((List)this.rawData.get(indivID)).size() + "# of snp on rawData");
/*      */     }
/*  734 */     int no_copies = ((String[])this.phasedData.get(indivID)).length;
/*  735 */     List<Integer> heteroIndex = getHeteroIndex(indivID, no_copies);
/*      */     
/*  737 */     int switchError = 0;
/*  738 */     this.errorHeteroIndex.put(indivID, new Integer[3][heteroIndex.size()]);
/*  739 */     ((Integer[][])this.errorHeteroIndex.get(indivID))[0] = ((Integer[])heteroIndex.toArray(new Integer[0]));
/*  740 */     Arrays.fill(((Integer[][])this.errorHeteroIndex.get(indivID))[1], Integer.valueOf(0));
/*  741 */     Arrays.fill(((Integer[][])this.errorHeteroIndex.get(indivID))[2], Integer.valueOf(0));
/*  742 */     Map<String, List<byte[]>> newIndexTrueHapList = new HashMap();
/*  743 */     for (int i = 1; i < heteroIndex.size(); i++) {
/*  744 */       if (no_copies == 2) {
/*  745 */         List<String> trueHap = new ArrayList();
/*  746 */         List<String> inferHap = new ArrayList();
/*  747 */         for (int k = 0; k < no_copies; k++) {
/*  748 */           char[] temp = { ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i - 1)).intValue()))[k].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i)).intValue()))[k].charValue() };
/*  749 */           String s = new String(temp);
/*  750 */           if ((i == 379) && (indivID.equals("22075"))) {
/*  751 */             throw new RuntimeException("missing value in the rawData");
/*      */           }
/*  753 */           trueHap.add(s);
/*      */         }
/*  755 */         for (int k = 0; k < no_copies; k++) {
/*  756 */           char[] temp = { ((String[])this.phasedData.get(indivID))[k].charAt(((Integer)heteroIndex.get(i - 1)).intValue()), ((String[])this.phasedData.get(indivID))[k].charAt(((Integer)heteroIndex.get(i)).intValue()) };
/*  757 */           String s = new String(temp);
/*  758 */           inferHap.add(s);
/*      */         }
/*  760 */         if ((!trueHap.containsAll(inferHap)) || (!inferHap.containsAll(trueHap))) {
/*  761 */           switchError++;
/*  762 */           ((Integer[][])this.errorHeteroIndex.get(indivID))[1][i] = Integer.valueOf(1);
/*  763 */           ((Integer[][])this.errorHeteroIndex.get(indivID))[2][i] = Integer.valueOf(getFromToCopies(inferHap));
/*      */         }
/*      */       }
/*  766 */       if (no_copies == 3) {
/*  767 */         if (i == 1) {
/*  768 */           List<String> trueHap = new ArrayList();
/*      */           
/*  770 */           for (int k = 0; k < no_copies; k++) {
/*  771 */             char[] temp = { ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i - 1)).intValue()))[k].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i)).intValue()))[k].charValue() };
/*  772 */             String s = new String(temp);
/*  773 */             trueHap.add(s);
/*      */           }
/*      */           
/*  776 */           Integer hapSpIndex = Integer.valueOf(getSpIndex(this.hapSp3, getGenotypeString(indivID, i, heteroIndex)));
/*      */           
/*  778 */           List<String> inferHap = getHapFromPhasedData(indivID, i, heteroIndex, hapSpIndex, 2, this.hapSp3);
/*      */           
/*  780 */           int p = 0;
/*  781 */           if ((!trueHap.containsAll(inferHap)) || (!inferHap.containsAll(trueHap))) {
/*  782 */             switchError++;
/*  783 */             ((Integer[][])this.errorHeteroIndex.get(indivID))[1][i] = Integer.valueOf(1);
/*  784 */             List<Integer> possibleIndex = getSp3PossibleIndex(hapSpIndex);
/*  785 */             for (int h = 0; h < possibleIndex.size(); h++) {
/*  786 */               if (possibleIndex.get(h) != hapSpIndex) {
/*  787 */                 inferHap = getHapFromPhasedData(indivID, i, heteroIndex, (Integer)possibleIndex.get(h), 2, this.hapSp3);
/*  788 */                 if (trueHap.containsAll(inferHap)) {
/*  789 */                   List<byte[]> newIndexTrueHapList1 = new ArrayList();
/*  790 */                   newIndexTrueHapList.put(i + "_" + p, newIndexTrueHapList1);
/*  791 */                   newIndexTrueHapList.put(i + "_" + p, getExchangeIndex((String)this.hapSp3.get(hapSpIndex.intValue()), (String)this.hapSp3.get(((Integer)possibleIndex.get(h)).intValue()), (List)newIndexTrueHapList.get(i + "_" + p), no_copies));
/*  792 */                   p++;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           else {
/*  798 */             List<byte[]> newIndexTrueHapList1 = new ArrayList();
/*  799 */             newIndexTrueHapList1.add(new byte[] { 0, 1, 2 });
/*  800 */             newIndexTrueHapList.put(i + "_" + p, newIndexTrueHapList1);
/*      */           }
/*      */         }
/*      */         else {
/*  804 */           String check = null;
/*  805 */           int p = 0;
/*  806 */           String switchErrorSign = null;
/*  807 */           List<String> tempIterator = new ArrayList();
/*  808 */           for (Iterator<String> is = newIndexTrueHapList.keySet().iterator(); is.hasNext();) {
/*  809 */             String st = (String)is.next();
/*  810 */             tempIterator.add(st);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  815 */           for (int t = 0; t < tempIterator.size(); t++) {
/*  816 */             String st = (String)tempIterator.get(t);
/*      */             
/*  818 */             Integer hapSpIndex1 = Integer.valueOf(getSpIndex(this.hapSp3, getGenotypeString(indivID, i, heteroIndex)));
/*  819 */             Integer hapSpIndex2 = Integer.valueOf(getSpIndex(this.hapSp3, getGenotypeString(indivID, i - 1, heteroIndex)));
/*  820 */             Integer hapSpIndex3 = Integer.valueOf(getSpIndex(this.hapSp3, getGenoStringSwiched(indivID, i - 2, heteroIndex, (List)newIndexTrueHapList.get(st))));
/*      */             
/*  822 */             int newIndex = i - 2;
/*  823 */             for (int ji = i - 3; ji >= 0; ji--) {
/*  824 */               List<Integer> temp = new ArrayList();
/*  825 */               temp.add(hapSpIndex2);temp.add(hapSpIndex3);
/*  826 */               if ((!temp.containsAll(this.confusedGroup1)) && (!temp.containsAll(this.confusedGroup2)) && (!temp.containsAll(this.confusedGroup3)) && (hapSpIndex2 != hapSpIndex3)) break;
/*  827 */               hapSpIndex3 = Integer.valueOf(getSpIndex(this.hapSp3, getGenoStringSwiched(indivID, ji, heteroIndex, (List)newIndexTrueHapList.get(st))));
/*  828 */               newIndex = ji; continue;
/*      */               
/*  830 */               break;
/*      */             }
/*      */             
/*  833 */             List<String> trueHap = new ArrayList();
/*  834 */             for (int k = 0; k < no_copies; k++) {
/*  835 */               char[] temp = { ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(newIndex)).intValue()))[k].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i - 1)).intValue()))[k].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i)).intValue()))[k].charValue() };
/*  836 */               String s = new String(temp);
/*  837 */               trueHap.add(s);
/*      */             }
/*      */             
/*  840 */             List<String> inferHap = getHapFromIndex(new int[] { hapSpIndex3.intValue(), hapSpIndex2.intValue(), hapSpIndex1.intValue() }, this.hapSp3, no_copies);
/*      */             
/*  842 */             if ((!trueHap.containsAll(inferHap)) || (!inferHap.containsAll(trueHap))) {
/*  843 */               List<Integer> possibleIndex = getSp3PossibleIndex(hapSpIndex1);
/*  844 */               for (int h = 0; h < possibleIndex.size(); h++) {
/*  845 */                 if (possibleIndex.get(h) != hapSpIndex1) {
/*  846 */                   inferHap = getHapFromIndex(new int[] { hapSpIndex3.intValue(), hapSpIndex2.intValue(), ((Integer)possibleIndex.get(h)).intValue() }, this.hapSp3, no_copies);
/*  847 */                   if (trueHap.containsAll(inferHap)) {
/*  848 */                     newIndexTrueHapList.put(i + "_" + p, getExchangeIndex((String)this.hapSp3.get(hapSpIndex1.intValue()), (String)this.hapSp3.get(((Integer)possibleIndex.get(h)).intValue()), (List)newIndexTrueHapList.get(st), no_copies));
/*  849 */                     check = "ok";
/*  850 */                     p++;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/*  857 */               ((List)newIndexTrueHapList.get(st)).add(new byte[] { 0, 1, 2 });
/*  858 */               newIndexTrueHapList.put(i + "_" + p, (List)newIndexTrueHapList.get(st));
/*  859 */               check = "ok";
/*  860 */               p++;
/*  861 */               switchErrorSign = "-";
/*      */             }
/*  863 */             newIndexTrueHapList.remove(st);
/*      */           }
/*  865 */           if (check == null) {
/*  866 */             throw new RuntimeException(i + "th true haps and phase haps are not matched");
/*      */           }
/*  868 */           if (switchErrorSign == null) {
/*  869 */             switchError++;
/*  870 */             ((Integer[][])this.errorHeteroIndex.get(indivID))[1][i] = Integer.valueOf(1);
/*      */           }
/*      */         }
/*      */       }
/*  874 */       if (no_copies == 4) {
/*  875 */         if (i == 1) {
/*  876 */           String check = null;
/*  877 */           List<String> trueHap = new ArrayList();
/*      */           
/*  879 */           for (int k = 0; k < no_copies; k++) {
/*  880 */             char[] temp = { ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i - 1)).intValue()))[k].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i)).intValue()))[k].charValue() };
/*  881 */             String s = new String(temp);
/*  882 */             trueHap.add(s);
/*      */           }
/*      */           
/*  885 */           Integer hapSpIndex = Integer.valueOf(getSpIndex(this.hapSp4, getGenotypeString(indivID, i, heteroIndex)));
/*      */           
/*  887 */           List<String> inferHap = getHapFromPhasedData(indivID, i, heteroIndex, hapSpIndex, 2, this.hapSp4);
/*      */           
/*  889 */           int p = 0;
/*  890 */           if ((!trueHap.containsAll(inferHap)) || (!inferHap.containsAll(trueHap))) {
/*  891 */             switchError++;
/*  892 */             ((Integer[][])this.errorHeteroIndex.get(indivID))[1][i] = Integer.valueOf(1);
/*  893 */             List<Integer> possibleIndex = getSp4PossibleIndex(hapSpIndex.intValue());
/*  894 */             for (int h = 0; h < possibleIndex.size(); h++) {
/*  895 */               if (possibleIndex.get(h) != hapSpIndex) {
/*  896 */                 inferHap = getHapFromPhasedData(indivID, i, heteroIndex, (Integer)possibleIndex.get(h), 2, this.hapSp4);
/*  897 */                 if (trueHap.containsAll(inferHap)) {
/*  898 */                   List<Integer> index2 = new ArrayList();
/*  899 */                   index2.add(hapSpIndex);index2.add((Integer)possibleIndex.get(h));
/*  900 */                   int amIndex = getAmbigousIndex(index2);
/*  901 */                   if (this.possibleExIndex.containsKey(Integer.valueOf(amIndex))) {
/*  902 */                     for (int k = 0; k < ((List)this.possibleExIndex.get(Integer.valueOf(amIndex))).size(); k++) {
/*  903 */                       List<byte[]> newIndexTrueHapList1 = new ArrayList();
/*  904 */                       newIndexTrueHapList1.add((byte[])((List)this.possibleExIndex.get(Integer.valueOf(amIndex))).get(k));
/*  905 */                       newIndexTrueHapList.put(i + "_" + p, newIndexTrueHapList1);
/*  906 */                       p++;
/*  907 */                       check = "ok";
/*      */                     }
/*      */                   }
/*      */                   else
/*      */                   {
/*  912 */                     List<byte[]> newIndexTrueHapList1 = new ArrayList();
/*  913 */                     newIndexTrueHapList.put(i + "_" + p, newIndexTrueHapList1);
/*  914 */                     newIndexTrueHapList.put(i + "_" + p, getExchangeIndex4((String)this.hapSp4.get(hapSpIndex.intValue()), (String)this.hapSp4.get(((Integer)possibleIndex.get(h)).intValue()), (List)newIndexTrueHapList.get(i + "_" + p), no_copies));
/*  915 */                     p++;
/*  916 */                     check = "ok";
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           else {
/*  923 */             List<byte[]> newIndexTrueHapList1 = new ArrayList();
/*  924 */             newIndexTrueHapList1.add(new byte[] { 0, 1, 2, 3 });
/*  925 */             newIndexTrueHapList.put(i + "_" + p, newIndexTrueHapList1);
/*  926 */             check = "ok";
/*      */           }
/*  928 */           if (check == null) throw new RuntimeException(i + "the true haps and phase haps are not matched");
/*      */         }
/*  930 */         else if (i == 2) {
/*  931 */           String check = null;
/*  932 */           int p = 0;
/*  933 */           String switchErrorSign = null;
/*  934 */           List<String> tempIterator = new ArrayList();
/*  935 */           for (Iterator<String> is = newIndexTrueHapList.keySet().iterator(); is.hasNext();) {
/*  936 */             String st = (String)is.next();
/*  937 */             tempIterator.add(st);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  942 */           for (int t = 0; t < tempIterator.size(); t++) {
/*  943 */             String st = (String)tempIterator.get(t);
/*      */             
/*  945 */             Integer hapSpIndex1 = Integer.valueOf(getSpIndex(this.hapSp4, getGenotypeString(indivID, i, heteroIndex)));
/*  946 */             Integer hapSpIndex2 = Integer.valueOf(getSpIndex(this.hapSp4, getGenotypeString(indivID, i - 1, heteroIndex)));
/*  947 */             Integer hapSpIndex3 = Integer.valueOf(getSpIndex(this.hapSp4, getGenoStringSwiched(indivID, i - 2, heteroIndex, (List)newIndexTrueHapList.get(st))));
/*      */             
/*  949 */             List<String> trueHap = new ArrayList();
/*  950 */             for (int k = 0; k < no_copies; k++) {
/*  951 */               char[] temp = { ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i - 2)).intValue()))[k].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i - 1)).intValue()))[k].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i)).intValue()))[k].charValue() };
/*  952 */               String s = new String(temp);
/*  953 */               trueHap.add(s);
/*      */             }
/*      */             
/*  956 */             List<String> inferHap = getHapFromIndex(new int[] { hapSpIndex3.intValue(), hapSpIndex2.intValue(), hapSpIndex1.intValue() }, this.hapSp4, no_copies);
/*  957 */             if ((!trueHap.containsAll(inferHap)) || (!inferHap.containsAll(trueHap))) {
/*  958 */               List<Integer> possibleIndex = getSp4PossibleIndex(hapSpIndex1.intValue());
/*  959 */               for (int h = 0; h < possibleIndex.size(); h++) {
/*  960 */                 if (possibleIndex.get(h) != hapSpIndex1) {
/*  961 */                   inferHap = getHapFromIndex(new int[] { hapSpIndex3.intValue(), hapSpIndex2.intValue(), ((Integer)possibleIndex.get(h)).intValue() }, this.hapSp4, no_copies);
/*  962 */                   if (trueHap.containsAll(inferHap)) {
/*  963 */                     List<Integer> index2 = new ArrayList();
/*  964 */                     index2.add(hapSpIndex1);index2.add((Integer)possibleIndex.get(h));
/*  965 */                     int amIndex = getAmbigousIndex(index2);
/*  966 */                     if (this.possibleExIndex.containsKey(Integer.valueOf(amIndex))) {
/*  967 */                       for (int k = 0; k < ((List)this.possibleExIndex.get(Integer.valueOf(amIndex))).size(); k++) {
/*  968 */                         newIndexTrueHapList.put(i + "_" + p, getExchangeIndexAll((byte[])((List)this.possibleExIndex.get(Integer.valueOf(amIndex))).get(k), (List)newIndexTrueHapList.get(st), no_copies));
/*  969 */                         check = "ok";
/*  970 */                         p++;
/*      */                       }
/*      */                     }
/*      */                     else
/*      */                     {
/*  975 */                       newIndexTrueHapList.put(i + "_" + p, getExchangeIndex4((String)this.hapSp4.get(hapSpIndex1.intValue()), (String)this.hapSp4.get(((Integer)possibleIndex.get(h)).intValue()), (List)newIndexTrueHapList.get(st), no_copies));
/*  976 */                       check = "ok";
/*  977 */                       p++;
/*      */                     }
/*      */                     
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/*  986 */               ((List)newIndexTrueHapList.get(st)).add(new byte[] { 0, 1, 2, 3 });
/*  987 */               newIndexTrueHapList.put(i + "_" + p, (List)newIndexTrueHapList.get(st));
/*  988 */               check = "ok";
/*  989 */               p++;
/*  990 */               switchErrorSign = "-";
/*      */             }
/*  992 */             newIndexTrueHapList.remove(st);
/*      */           }
/*  994 */           if (check == null) {
/*  995 */             throw new RuntimeException(i + "th true haps and phase haps are not matched");
/*      */           }
/*  997 */           if (switchErrorSign == null) {
/*  998 */             switchError++;
/*  999 */             ((Integer[][])this.errorHeteroIndex.get(indivID))[1][i] = Integer.valueOf(1);
/*      */           }
/*      */         }
/*      */         else {
/* 1003 */           String check = null;
/* 1004 */           int p = 0;
/* 1005 */           String switchErrorSign = null;
/* 1006 */           List<String> tempIterator = new ArrayList();
/* 1007 */           for (Iterator<String> is = newIndexTrueHapList.keySet().iterator(); is.hasNext();) {
/* 1008 */             String st = (String)is.next();
/* 1009 */             tempIterator.add(st);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1014 */           for (int t = 0; t < tempIterator.size(); t++) {
/* 1015 */             String st = (String)tempIterator.get(t);
/*      */             
/* 1017 */             Integer hapSpIndex1 = Integer.valueOf(getSpIndex(this.hapSp4, getGenotypeString(indivID, i, heteroIndex)));
/* 1018 */             Integer hapSpIndex2 = Integer.valueOf(getSpIndex(this.hapSp4, getGenotypeString(indivID, i - 1, heteroIndex)));
/* 1019 */             Integer hapSpIndex3 = Integer.valueOf(getSpIndex(this.hapSp4, getGenoStringSwiched(indivID, i - 2, heteroIndex, (List)newIndexTrueHapList.get(st))));
/*      */             
/* 1021 */             int newIndex3 = i - 2;
/* 1022 */             for (int ji = i - 3; ji > 0; ji--) {
/* 1023 */               int noDiffHap = getNoDiffHap(new int[] { hapSpIndex3.intValue(), hapSpIndex2.intValue() }, this.hapSp4, no_copies);
/* 1024 */               if (noDiffHap >= 3) break;
/* 1025 */               hapSpIndex3 = Integer.valueOf(getSpIndex(this.hapSp4, getGenoStringSwiched(indivID, ji, heteroIndex, (List)newIndexTrueHapList.get(st))));
/* 1026 */               newIndex3 = ji; continue;
/*      */               
/* 1028 */               break;
/*      */             }
/*      */             
/* 1031 */             int newIndex4 = newIndex3 - 1;
/* 1032 */             Integer hapSpIndex4 = Integer.valueOf(getSpIndex(this.hapSp4, getGenoStringSwiched(indivID, newIndex4, heteroIndex, (List)newIndexTrueHapList.get(st))));
/* 1033 */             for (int ji = newIndex3 - 2; ji >= 0; ji--) {
/* 1034 */               int noDiffHap = getNoDiffHap(new int[] { hapSpIndex4.intValue(), hapSpIndex3.intValue(), hapSpIndex2.intValue() }, this.hapSp4, no_copies);
/* 1035 */               if (noDiffHap >= 4) break;
/* 1036 */               hapSpIndex4 = Integer.valueOf(getSpIndex(this.hapSp4, getGenoStringSwiched(indivID, ji, heteroIndex, (List)newIndexTrueHapList.get(st))));
/* 1037 */               newIndex4 = ji; continue;
/*      */               
/* 1039 */               break;
/*      */             }
/*      */             
/* 1042 */             List<String> trueHap = new ArrayList();
/* 1043 */             for (int k = 0; k < no_copies; k++) {
/* 1044 */               char[] temp = { ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(newIndex4)).intValue()))[k].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(newIndex3)).intValue()))[k].charValue(), 
/* 1045 */                 ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i - 1)).intValue()))[k].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(i)).intValue()))[k].charValue() };
/* 1046 */               String s = new String(temp);
/* 1047 */               trueHap.add(s);
/*      */             }
/*      */             
/* 1050 */             List<String> inferHap = getHapFromIndex(new int[] { hapSpIndex4.intValue(), hapSpIndex3.intValue(), hapSpIndex2.intValue(), hapSpIndex1.intValue() }, this.hapSp4, no_copies);
/*      */             
/* 1052 */             if ((!trueHap.containsAll(inferHap)) || (!inferHap.containsAll(trueHap))) {
/* 1053 */               List<Integer> possibleIndex = getSp4PossibleIndex(hapSpIndex1.intValue());
/* 1054 */               for (int h = 0; h < possibleIndex.size(); h++) {
/* 1055 */                 if (possibleIndex.get(h) != hapSpIndex1) {
/* 1056 */                   inferHap = getHapFromIndex(new int[] { hapSpIndex4.intValue(), hapSpIndex3.intValue(), hapSpIndex2.intValue(), ((Integer)possibleIndex.get(h)).intValue() }, this.hapSp4, no_copies);
/* 1057 */                   if (trueHap.containsAll(inferHap)) {
/* 1058 */                     List<Integer> index2 = new ArrayList();
/* 1059 */                     index2.add(hapSpIndex1);index2.add((Integer)possibleIndex.get(h));
/* 1060 */                     int amIndex = getAmbigousIndex(index2);
/* 1061 */                     if (this.possibleExIndex.containsKey(Integer.valueOf(amIndex))) {
/* 1062 */                       for (int k = 0; k < ((List)this.possibleExIndex.get(Integer.valueOf(amIndex))).size(); k++) {
/* 1063 */                         newIndexTrueHapList.put(i + "_" + p, getExchangeIndexAll((byte[])((List)this.possibleExIndex.get(Integer.valueOf(amIndex))).get(k), (List)newIndexTrueHapList.get(st), no_copies));
/* 1064 */                         check = "ok";
/* 1065 */                         p++;
/*      */                       }
/*      */                     }
/*      */                     else
/*      */                     {
/* 1070 */                       newIndexTrueHapList.put(i + "_" + p, getExchangeIndex4((String)this.hapSp4.get(hapSpIndex1.intValue()), (String)this.hapSp4.get(((Integer)possibleIndex.get(h)).intValue()), (List)newIndexTrueHapList.get(st), no_copies));
/* 1071 */                       check = "ok";
/* 1072 */                       p++;
/*      */                     }
/*      */                     
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/* 1081 */               ((List)newIndexTrueHapList.get(st)).add(new byte[] { 0, 1, 2, 3 });
/* 1082 */               newIndexTrueHapList.put(i + "_" + p, (List)newIndexTrueHapList.get(st));
/* 1083 */               check = "ok";
/* 1084 */               p++;
/* 1085 */               switchErrorSign = "-";
/*      */             }
/* 1087 */             newIndexTrueHapList.remove(st);
/*      */           }
/* 1089 */           if (check == null) {
/* 1090 */             throw new RuntimeException(i + "th true haps and phase haps are not matched");
/*      */           }
/* 1092 */           if (switchErrorSign == null) {
/* 1093 */             switchError++;
/* 1094 */             ((Integer[][])this.errorHeteroIndex.get(indivID))[1][i] = Integer.valueOf(1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1099 */     int[] switchCounts = { switchError, heteroIndex.size() };
/* 1100 */     newIndexTrueHapList.clear();
/* 1101 */     return switchCounts;
/*      */   }
/*      */   
/*      */   public double getIncorrectRate(String indivID)
/*      */   {
/* 1106 */     int no_copies = ((String[])this.phasedData.get(indivID)).length;
/* 1107 */     List<Integer> heteroIndex = getHeteroIndex(indivID, no_copies);
/* 1108 */     double[] incorrectRate = new double[no_copies];
/* 1109 */     int[][] countDif = new int[no_copies][no_copies];
/* 1110 */     for (int i = 0; i < countDif.length; i++) {
/* 1111 */       Arrays.fill(countDif[i], 0);
/* 1112 */       Arrays.fill(countDif[i], 0);
/*      */     }
/* 1114 */     for (int i = 0; i < no_copies; i++) {
/* 1115 */       for (int t = 0; t < no_copies; t++) {
/* 1116 */         for (int m = 0; m < heteroIndex.size(); m++) {
/* 1117 */           if (((String[])this.phasedData.get(indivID))[i].charAt(((Integer)heteroIndex.get(m)).intValue()) != ((Character[])((List)this.rawData.get(indivID)).get(((Integer)heteroIndex.get(m)).intValue()))[t].charValue()) {
/* 1118 */             countDif[i][t] += 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1123 */     for (int i = 0; i < no_copies; i++) {
/* 1124 */       int min = Integer.MAX_VALUE;
/* 1125 */       for (int k = 0; k < countDif[i].length; k++) {
/* 1126 */         if (countDif[i][k] < min) min = countDif[i][k];
/*      */       }
/* 1128 */       incorrectRate[i] = (min / heteroIndex.size());
/*      */     }
/* 1130 */     double temp = 0.0D;
/* 1131 */     for (int i = 0; i < no_copies; i++) {
/* 1132 */       temp += incorrectRate[i];
/*      */     }
/* 1134 */     return temp / no_copies;
/*      */   }
/*      */   
/*      */   public Map<String, Integer> getNumDiff()
/*      */   {
/* 1139 */     Map<String, Integer> numDiff = new HashMap();
/* 1140 */     for (Iterator<String> is = this.phasedData.keySet().iterator(); is.hasNext();) {
/* 1141 */       String indivID = (String)is.next();
/* 1142 */       int[] count = new int[3];
/* 1143 */       for (int i = 0; i < ((String[])this.phasedData.get(indivID))[0].length(); i++) {
/* 1144 */         if (((String[])this.phasedData.get(indivID))[0].charAt(i) != ((String[])this.phasedData.get(indivID))[1].charAt(i)) count[0] += 1;
/* 1145 */         if (((String[])this.phasedData.get(indivID))[0].charAt(i) != ((String[])this.phasedData.get(indivID))[2].charAt(i)) count[1] += 1;
/* 1146 */         if (((String[])this.phasedData.get(indivID))[1].charAt(i) != ((String[])this.phasedData.get(indivID))[2].charAt(i)) count[2] += 1;
/*      */       }
/* 1148 */       int min = count[0];
/* 1149 */       for (int i = 1; i < count.length; i++) {
/* 1150 */         if (count[i] < min) min = count[i];
/*      */       }
/* 1152 */       numDiff.put(indivID, Integer.valueOf(min));
/*      */     }
/* 1154 */     return numDiff;
/*      */   }
/*      */   
/*      */   public Map<String, Integer> getNumDiffRaw()
/*      */   {
/* 1159 */     Map<String, Integer> numDiff = new HashMap();
/* 1160 */     for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 1161 */       String indivID = (String)is.next();
/* 1162 */       int[] count = new int[3];
/* 1163 */       for (int i = 0; i < ((List)this.rawData.get(indivID)).size(); i++) {
/* 1164 */         if (((Character[])((List)this.rawData.get(indivID)).get(i))[0] != ((Character[])((List)this.rawData.get(indivID)).get(i))[1]) count[0] += 1;
/* 1165 */         if (((Character[])((List)this.rawData.get(indivID)).get(i))[0] != ((Character[])((List)this.rawData.get(indivID)).get(i))[2]) count[1] += 1;
/* 1166 */         if (((Character[])((List)this.rawData.get(indivID)).get(i))[1] != ((Character[])((List)this.rawData.get(indivID)).get(i))[2]) count[2] += 1;
/*      */       }
/* 1168 */       int min = count[0];
/* 1169 */       for (int i = 1; i < count.length; i++) {
/* 1170 */         if (count[i] < min) min = count[i];
/*      */       }
/* 1172 */       numDiff.put(indivID, Integer.valueOf(min));
/*      */     }
/* 1174 */     return numDiff;
/*      */   }
/*      */   
/*      */   public void printNumDiff(PrintStream ps) {
/* 1178 */     Map<String, Integer> numDiff = getNumDiffRaw();
/* 1179 */     String indivID; for (Iterator<String> is = numDiff.keySet().iterator(); is.hasNext(); 
/*      */         
/* 1181 */         ps.println(numDiff.get(indivID)))
/*      */     {
/* 1180 */       indivID = (String)is.next();
/* 1181 */       ps.print(indivID + "  ");
/*      */     }
/*      */   }
/*      */   
/*      */   public void printErrorRate(PrintStream ps, Map<String, Double> rate)
/*      */   {
/* 1187 */     double sum = 0.0D;
/* 1188 */     String idv; for (Iterator<String> id = rate.keySet().iterator(); id.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/* 1192 */         ps.println(rate.get(idv)))
/*      */     {
/* 1189 */       idv = (String)id.next();
/*      */       
/* 1191 */       sum += ((Double)rate.get(idv)).doubleValue();
/* 1192 */       ps.print(idv);ps.print("---");
/*      */     }
/*      */     
/* 1195 */     ps.print("averge:");ps.println(sum / this.switchErrortateAll.size());
/*      */   }
/*      */   
/*      */   public void printDartErrorRate(PrintStream ps) {
/* 1199 */     for (int i = 0; i < this.dartErrorRate.size(); i++) {
/* 1200 */       for (int c = 0; c < 5; c++) {
/* 1201 */         ps.print(((int[])this.counts.get(i))[c]);ps.print("  ");
/*      */       }
/* 1203 */       ps.println(this.dartErrorRate.get(i));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public int[][] compareUncerSwitch()
/*      */   {
/* 1210 */     int[][] countError = new int[2][10];
/* 1211 */     Arrays.fill(countError[0], 0);
/* 1212 */     Arrays.fill(countError[1], 0);
/* 1213 */     int test = 0;
/* 1214 */     String idv; int i; for (Iterator<String> id = this.uncertaintyScore.keySet().iterator(); id.hasNext(); 
/*      */         
/* 1216 */         i < ((Integer[][])this.errorHeteroIndex.get(idv))[0].length)
/*      */     {
/* 1215 */       idv = (String)id.next();
/* 1216 */       i = 0; continue;
/* 1217 */       double certainty = ((Double)((List)this.uncertaintyScore.get(idv)).get(((Integer[][])this.errorHeteroIndex.get(idv))[0][i].intValue())).doubleValue();
/* 1218 */       for (int k = 1; k < 11; k++) {
/* 1219 */         if (certainty * 100.0D <= k * 10.0D) {
/* 1220 */           countError[1][(k - 1)] += 1;
/* 1221 */           break;
/*      */         }
/*      */       }
/* 1224 */       if (((Integer[][])this.errorHeteroIndex.get(idv))[1][i].intValue() == 1) {
/* 1225 */         for (int k = 1; k < 11; k++) {
/* 1226 */           if (certainty * 100.0D <= k * 10.0D) {
/* 1227 */             countError[0][(k - 1)] += 1;
/* 1228 */             break;
/*      */           }
/*      */         }
/*      */       } else {
/* 1232 */         test++;
/*      */       }
/* 1216 */       i++;
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
/* 1235 */     return countError;
/*      */   }
/*      */   
/*      */   public int comparePhaseRaw(String[] phase, List<Character[]> raw, int pos)
/*      */   {
/* 1240 */     int[] phasedCount = new int[2];
/* 1241 */     int[] rawCount = new int[2];
/* 1242 */     for (int c = 0; c < phase.length; c++) {
/* 1243 */       if (phase[c].charAt(pos) == 'A') phasedCount[0] += 1; else
/* 1244 */         phasedCount[1] += 1;
/* 1245 */       if (((Character[])raw.get(pos))[c].equals(Character.valueOf('A'))) rawCount[0] += 1; else
/* 1246 */         rawCount[1] += 1;
/*      */     }
/* 1248 */     if ((phasedCount[0] != rawCount[0]) || (phasedCount[1] != rawCount[1])) return 1;
/* 1249 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public int[][] compareUncerImpute()
/*      */   {
/* 1255 */     int[][] countError = new int[2][10];
/* 1256 */     Arrays.fill(countError[0], 0);
/* 1257 */     Arrays.fill(countError[1], 0);
/* 1258 */     String idv; int i; for (Iterator<String> id = this.rawDataMissing.keySet().iterator(); id.hasNext(); 
/*      */         
/* 1260 */         i < ((List)this.rawDataMissing.get(idv)).size())
/*      */     {
/* 1259 */       idv = (String)id.next();
/* 1260 */       i = 0; continue;
/* 1261 */       if (((Integer)((List)this.rawDataMissing.get(idv)).get(i)).intValue() == 1)
/*      */       {
/* 1263 */         double certainty = ((Double)((List)this.uncertaintyScore.get(idv)).get(i)).doubleValue();
/* 1264 */         for (int k = 1; k < 11; k++) {
/* 1265 */           if (certainty * 100.0D <= k * 10.0D) {
/* 1266 */             countError[1][(k - 1)] += 1;
/* 1267 */             break;
/*      */           }
/*      */         }
/* 1270 */         if (comparePhaseRaw((String[])this.phasedData.get(idv), (List)this.rawData.get(idv), i) == 1) {
/* 1271 */           for (int k = 1; k < 11; k++) {
/* 1272 */             if (certainty * 100.0D <= k * 10.0D) {
/* 1273 */               countError[0][(k - 1)] += 1;
/* 1274 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1260 */       i++;
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
/* 1282 */     return countError;
/*      */   }
/*      */   
/*      */ 
/*      */   public Map<Integer, Integer> getCountFromTo()
/*      */   {
/* 1288 */     Map<Integer, Integer> fromToCount = new HashMap();
/* 1289 */     int sum = 0;
/* 1290 */     Integer[][] count; int i; for (Iterator<String> is = this.errorHeteroIndex.keySet().iterator(); is.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/* 1294 */         i < count[0].length)
/*      */     {
/* 1291 */       String indivID = (String)is.next();
/* 1292 */       count = (Integer[][])this.errorHeteroIndex.get(indivID);
/* 1293 */       sum += count[0].length;
/* 1294 */       i = 0; continue;
/* 1295 */       Integer a = count[2][i];
/* 1296 */       if (a.intValue() != 0) {
/* 1297 */         if (count[1][i].intValue() == 0) throw new RuntimeException("!!");
/* 1298 */         if (!fromToCount.containsKey(a)) { fromToCount.put(a, Integer.valueOf(1));
/*      */         } else {
/* 1300 */           Integer newCount = Integer.valueOf(((Integer)fromToCount.get(a)).intValue() + 1);
/* 1301 */           fromToCount.put(a, newCount);
/*      */         }
/*      */       }
/* 1294 */       i++;
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
/* 1306 */     fromToCount.put(Integer.valueOf(0), Integer.valueOf(sum));
/* 1307 */     return fromToCount;
/*      */   }
/*      */   
/*      */   public void printOutCount(PrintStream ps) {
/* 1311 */     Map<Integer, Integer> fromToCount = getCountFromTo();
/* 1312 */     int sum = ((Integer)fromToCount.get(Integer.valueOf(0))).intValue();
/* 1313 */     double check = 0.0D;
/* 1314 */     for (Iterator<Integer> is = fromToCount.keySet().iterator(); is.hasNext();) {
/* 1315 */       Integer fromTo = (Integer)is.next();
/* 1316 */       ps.println(fromTo + ":" + ((Integer)fromToCount.get(fromTo)).intValue() / sum);
/* 1317 */       check += ((Integer)fromToCount.get(fromTo)).intValue() / sum;
/*      */     }
/* 1319 */     System.out.print(check);
/*      */   }
/*      */   
/*      */ 
/*      */   List<int[]> counts;
/*      */   Map<String, Double> switchErrortateAll;
/*      */   List<Double> incorrectRateAll;
/*      */   Map<String, Integer[][]> errorHeteroIndex;
/*      */   public void run()
/*      */   {
/* 1329 */     this.errorHeteroIndex = new HashMap();
/* 1330 */     this.switchErrortateAll = new HashMap();
/* 1331 */     this.incorrectRateAll = new ArrayList();
/* 1332 */     for (Iterator<String> is = this.phasedData.keySet().iterator(); is.hasNext();) {
/* 1333 */       String indivID = (String)is.next();
/*      */       
/* 1335 */       int[] count = getSwitchError(indivID);
/* 1336 */       this.switchErrortateAll.put(indivID, Double.valueOf(count[0] / count[1]));
/* 1337 */       this.incorrectRateAll.add(Double.valueOf(getIncorrectRate(indivID)));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/* 1342 */   Map<Integer, Integer> noSwitchAll = new HashMap();
/* 1343 */   Map<Integer, Integer> noHeteroSiteAll = new HashMap();
/*      */   
/* 1345 */   public void run1() { this.errorHeteroIndex = new HashMap();
/* 1346 */     for (Iterator<String> is = this.phasedData.keySet().iterator(); is.hasNext();) {
/* 1347 */       String indivID = (String)is.next();
/* 1348 */       int no_copies = ((String[])this.phasedData.get(indivID)).length;
/* 1349 */       if (no_copies != 1) {
/* 1350 */         int[] count = getSwitchError(indivID);
/* 1351 */         if (!this.noSwitchAll.containsKey(Integer.valueOf(no_copies))) {
/* 1352 */           this.noSwitchAll.put(Integer.valueOf(no_copies), Integer.valueOf(count[0]));
/* 1353 */           this.noHeteroSiteAll.put(Integer.valueOf(no_copies), Integer.valueOf(count[1]));
/*      */         }
/*      */         else {
/* 1356 */           this.noSwitchAll.put(Integer.valueOf(no_copies), Integer.valueOf(((Integer)this.noSwitchAll.get(Integer.valueOf(no_copies))).intValue() + count[0]));
/* 1357 */           this.noHeteroSiteAll.put(Integer.valueOf(no_copies), Integer.valueOf(((Integer)this.noHeteroSiteAll.get(Integer.valueOf(no_copies))).intValue() + count[1]));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void printErrorRate1(PrintStream ps) {
/* 1364 */     for (Iterator<Integer> in = this.noSwitchAll.keySet().iterator(); in.hasNext();) {
/* 1365 */       Integer no = (Integer)in.next();
/* 1366 */       double rate = ((Integer)this.noSwitchAll.get(no)).intValue() / ((Integer)this.noHeteroSiteAll.get(no)).intValue();
/* 1367 */       ps.println("no_copies: " + no);
/* 1368 */       ps.println(rate);
/* 1369 */       ps.println(Math.sqrt(rate * (1.0D - rate) / ((Integer)this.noHeteroSiteAll.get(no)).intValue()));
/*      */     }
/*      */   }
/*      */   
/*      */   public void printImputeErrorRate(PrintStream ps, double rate) {
/* 1374 */     ps.println(rate);
/* 1375 */     ps.println(Math.sqrt(rate * (1.0D - rate) / this.noMissingSNP)); }
/*      */   
/*      */   public void printCompareUncerSwi(PrintStream ps) { int[][] count;
/*      */     int i;
/* 1379 */     for (Iterator<Integer> in = this.noHeteroSiteAll.keySet().iterator(); in.hasNext(); 
/*      */         
/*      */ 
/* 1382 */         i < count[0].length)
/*      */     {
/* 1380 */       Integer no = (Integer)in.next();
/* 1381 */       count = compareUncerSwitch();
/* 1382 */       i = 0; continue;
/* 1383 */       ps.print((i + 1) / 10.0D - 0.05D + "\t");
/* 1384 */       ps.print(count[0][i] / count[1][i] + "\t");
/* 1385 */       ps.println(count[1][i] / ((Integer)this.noHeteroSiteAll.get(no)).intValue());i++;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void printCompareUncerImpute(PrintStream ps, int[][] count)
/*      */   {
/* 1391 */     for (int i = 0; i < count[0].length; i++) {
/* 1392 */       ps.print((i + 1) / 10.0D + "\t");
/* 1393 */       ps.print(count[0][i] / count[1][i] + "\t");
/* 1394 */       ps.println(count[1][i] / (this.missingIndex.size() - 2));
/*      */     }
/*      */   }
/*      */   
/*      */   public static void main(String[] args) {
/*      */     try {
/* 1400 */       SwitchErrorTri se = new SwitchErrorTri();
/* 1401 */       se.getHapSp2();
/* 1402 */       se.getHapSp3();
/* 1403 */       se.getHapSp4();
/* 1404 */       se.getAllPossibleExIndex();
/*      */       
/*      */ 
/* 1407 */       se.getRawData(new File("Xinternal.zip"));
/* 1408 */       se.getPhasedData(new File("phased2_10_internal"));
/* 1409 */       se.getUncertainty(new File("phased1_10_internal"));
/* 1410 */       se.run1();
/* 1411 */       PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("switchErrorRateCNVinternal.txt"))));
/* 1412 */       se.printErrorRate1(ps);
/* 1413 */       ps.close();
/*      */       
/* 1415 */       se.compareUncerSwitch();
/* 1416 */       PrintStream ps2 = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("compareUncerSwitchCNVinternal.txt"))));
/* 1417 */       se.printCompareUncerSwi(ps2);
/* 1418 */       ps2.close();
/*      */       
/*      */ 
/*      */ 
/* 1422 */       int j = 8;
/* 1423 */       String k = "K0";
/* 1424 */       String t = "T10";
/* 1425 */       String r = "r20";
/* 1426 */       String s = "s8";
/*      */       
/* 1428 */       double rate = 0.0D;
/*      */       
/* 1430 */       i = 1;
/*      */     }
/*      */     catch (Exception exc)
/*      */     {
/*      */       int i;
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
/* 1490 */       exc.printStackTrace();
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/SwitchErrorTri.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */