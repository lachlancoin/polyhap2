/*     */ package lc1.dp.data.collection;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import lc1.CGH.Location;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.emissionspace.CompoundEmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpaceTranslation;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ public class LikelihoodDataCollection
/*     */   extends DataCollection
/*     */ {
/*  25 */   public void createDataStructure(String paramString, int paramInt) { throw new Error("Unresolved compilation problem: \n\tThe type LikelihoodDataCollection must implement the inherited abstract method DataCollection.createDataStructure(String, int)\n"); } public void process(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt, Character paramCharacter1, Character paramCharacter2, Double paramDouble, boolean paramBoolean) { throw new Error("Unresolved compilation problem: \n\tThe type LikelihoodDataCollection must implement the inherited abstract method DataCollection.process(String, String[], String[], int, Character, Character, Double, boolean)\n"); } public void process(String paramString, int paramInt) { throw new Error("Unresolved compilation problem: \n\tThe type LikelihoodDataCollection must implement the inherited abstract method DataCollection.process(String, int)\n"); } public void process(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt) { throw new Error("Unresolved compilation problem: \n\tThe type LikelihoodDataCollection must implement the inherited abstract method DataCollection.process(String, String[], String[], int)\n"); }
/*     */   
/*  27 */   public static String[] cat = {
/*  28 */     "", 
/*  29 */     "A", 
/*  30 */     "B", 
/*  31 */     "AA", 
/*  32 */     "AB", 
/*  33 */     "BB", 
/*  34 */     "AAA", 
/*  35 */     "AAB", 
/*  36 */     "ABB", 
/*  37 */     "BBB", 
/*  38 */     "AAAA", 
/*  39 */     "AAAB", 
/*  40 */     "AABB", 
/*  41 */     "ABBB", 
/*  42 */     "BBBB" };
/*     */   
/*     */ 
/*     */   public static DataCollection read(File f)
/*     */   {
/*     */     try
/*     */     {
/*  49 */       return new LikelihoodDataCollection(cat);
/*     */     } catch (Exception exc) {
/*  51 */       exc.printStackTrace();
/*  52 */       System.exit(0); }
/*  53 */     return null;
/*     */   }
/*     */   
/*     */   public double[] getR(Location loc, List<Double> l, List<Integer> l1, List<Integer> l2, List<Double> b, String name)
/*     */   {
/*  58 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public DataC clone()
/*     */   {
/*  64 */     return new LikelihoodDataCollection(this);
/*     */   }
/*     */   
/*     */   protected LikelihoodDataCollection(DataCollection dat)
/*     */   {
/*  69 */     super(dat);
/*     */   }
/*     */   
/*     */   public void maximisationStep(double[] pseudo, int i)
/*     */   {
/*  74 */     if (Constants.trainData())
/*  75 */       for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext();) {
/*  76 */         EmissionState dataLi = (EmissionState)it.next();
/*  77 */         dataLi.transferCountsToProbs(pseudo[4]);
/*     */       }
/*     */   }
/*     */   
/*     */   public void initialisationStep() {
/*  82 */     for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext();)
/*     */     {
/*  84 */       ((EmissionState)it.next()).initialiseCounts();
/*     */     }
/*     */   }
/*     */   
/*     */   static boolean readLine(BufferedReader[] br, List<String>[] str) throws Exception {
/*  89 */     int lengthRest = Constants.restrict()[0];
/*  90 */     for (int i = 0; i < str.length; i++) {
/*  91 */       if (br[i] != null) {
/*  92 */         String stri = br[i].readLine();
/*  93 */         if (stri == null) { return false;
/*     */         }
/*  95 */         List<String> l = Arrays.asList(stri.split("\\s+"));
/*  96 */         int min = Math.min(l.size(), lengthRest + 1);
/*  97 */         str[i] = l.subList(1, min);
/*     */       }
/*     */     }
/*     */     
/* 101 */     return true;
/*     */   }
/*     */   
/*     */   static List<String> readNames(File id_file) {
/* 105 */     if ((!id_file.exists()) || (id_file.length() == 0L)) return null;
/* 106 */     List<String> l = new ArrayList();
/*     */     try {
/* 108 */       if ((!id_file.exists()) || (id_file.length() == 0L)) throw new RuntimeException("!!!");
/* 109 */       BufferedReader br = new BufferedReader(new FileReader(id_file));
/* 110 */       String st = "";
/* 111 */       while ((st = br.readLine()) != null) {
/* 112 */         l.add(st);
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/* 116 */       exc.printStackTrace();
/*     */     }
/* 118 */     return l;
/*     */   }
/*     */   
/*     */   public void extractFromTrioData() {
/* 122 */     super.extractFromTrioData();
/* 123 */     Map<String, EmissionState> l = new java.util.HashMap();
/* 124 */     for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext();)
/*     */     {
/* 126 */       EmissionState dat_i = (EmissionState)it.next();
/* 127 */       if ((dat_i instanceof lc1.dp.states.CompoundState)) {
/* 128 */         EmissionState[] data_i = dat_i.split();
/* 129 */         for (int j = 0; j < data_i.length; j++) {
/* 130 */           l.put(data_i[j].getName(), data_i[j]);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 135 */         l.put(dat_i.getName(), dat_i);
/*     */       }
/*     */     }
/*     */     
/* 139 */     this.dataL = l;
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
/*     */   public LikelihoodDataCollection(String[] categories)
/*     */     throws Exception
/*     */   {
/* 159 */     if (Constants.onlyCopyNo()) {
/* 160 */       for (int i = 0; i < categories.length; i++) {
/* 161 */         char[] ch = categories[i].toCharArray();
/* 162 */         Arrays.fill(ch, 'A');
/* 163 */         categories[i] = new String(ch);
/*     */       }
/*     */     }
/* 166 */     File dir1 = new File(Constants.getDirFile());
/* 167 */     File fi = new File(dir1, "L.50K_Merged.CEPH.Affy.txt");
/* 168 */     String[] indiv = readIndiv(new File(dir1, "indiv.txt"));
/* 169 */     BufferedReader br = new BufferedReader(new FileReader(fi));
/* 170 */     String st = br.readLine();
/* 171 */     boolean header = st.startsWith("chrom");
/* 172 */     if (header) {
/* 173 */       st = br.readLine();
/*     */     }
/* 175 */     this.loc = readPosInfo(fi, 1, header);
/* 176 */     int read = IlluminaRDataCollection.firstGreaterThan(this.loc, Constants.offset());
/* 177 */     this.loc = this.loc.subList(read, this.loc.size());
/* 178 */     for (int i = 0; i < read; i++) {
/* 179 */       st = br.readLine();
/*     */     }
/* 181 */     CompoundEmissionStateSpace stSp = Emiss.getEmissionStateSpace(1, 2);
/* 182 */     EmissionStateSpace stSp1 = Emiss.getEmissionStateSpace(1);
/* 183 */     EmissionStateSpaceTranslation trans = new EmissionStateSpaceTranslation(stSp, stSp1, true);
/* 184 */     this.length = Integer.valueOf(Math.min(Constants.restrict()[0], this.loc.size()));
/* 185 */     int[] categoryToGenotypeIndex = new int[categories.length];
/* 186 */     Arrays.fill(categoryToGenotypeIndex, -1);
/* 187 */     for (int i = 0; i < categories.length; i++) {
/* 188 */       String str = categories[i];
/* 189 */       for (int k = 0; k < stSp.genoListSize().intValue(); k++) {
/* 190 */         String genString = stSp1.getGenotypeString(stSp.getGenotype(k));
/* 191 */         if (str.equals(genString)) {
/* 192 */           categoryToGenotypeIndex[i] = k;
/* 193 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 198 */     EmissionState[] ldata = (EmissionState[])null;
/* 199 */     String[][] str = (String[][])null;
/*     */     
/* 201 */     for (int k = 0; k < this.length.intValue(); k++)
/*     */     {
/* 203 */       boolean NA = st.indexOf("NA") >= 0;
/* 204 */       List<String> string = Arrays.asList(st.split("\\s+"));
/* 205 */       if (k == 0) {
/* 206 */         if (Math.abs(Math.IEEEremainder(string.size() - 2, categories.length)) > 0.001D) throw new RuntimeException("!!");
/* 207 */         double len = (string.size() - 2.0D) / categories.length;
/*     */         
/* 209 */         ldata = new EmissionState[(int)len];
/* 210 */         if (ldata.length != indiv.length) throw new RuntimeException("!!");
/* 211 */         for (int i = 0; i < ldata.length; i++) {
/* 212 */           ldata[i] = EmissionState.getEmissionState(indiv[i], stSp1, this.length.intValue());
/*     */         }
/* 214 */         str = new String[ldata.length][];
/*     */       }
/*     */       
/* 217 */       for (int i = 0; i < ldata.length; i++) {
/* 218 */         str[i] = ((String[])string.subList(2 + i * categories.length, 2 + (i + 1) * categories.length).toArray(new String[0]));
/*     */       }
/* 220 */       for (int i = 0; i < ldata.length; i++) {
/* 221 */         double[] d = new double[str[i].length];
/* 222 */         for (int k1 = 0; k1 < d.length; k1++) {
/* 223 */           d[k1] = (NA ? -1.0D : -1.0D * Double.parseDouble(str[i][k1]));
/*     */         }
/*     */       }
/*     */       
/* 227 */       st = br.readLine();
/*     */     }
/*     */     
/* 230 */     for (int i = 0; i < ldata.length; i++)
/*     */     {
/* 232 */       this.dataL.put(ldata[i].getName(), ldata[i]);
/*     */     }
/* 234 */     calculateMLGenotypeData(true);
/* 235 */     initialiseMaf();
/* 236 */     br.close();
/*     */   }
/*     */   
/*     */ 
/*     */   public LikelihoodDataCollection(List<Integer> locs)
/*     */   {
/* 242 */     this.length = Integer.valueOf(locs.size());
/* 243 */     this.loc = locs;
/*     */   }
/*     */   
/*     */   public LikelihoodDataCollection(File f, short index, int no_copies)
/*     */     throws Exception
/*     */   {
/* 249 */     super(f, index, no_copies);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialiseMaf()
/*     */   {
/* 259 */     CompoundEmissionStateSpace stSp = Emiss.getEmissionStateSpace(1);
/* 260 */     EmissionStateSpace stSp1 = stSp.getMembers()[0];
/* 261 */     this.maf = makeMafState(stSp1);
/* 262 */     for (int i = 0; i < this.length.intValue(); i++) { double[] probs;
/* 263 */       int k; for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext(); 
/*     */           
/*     */ 
/* 266 */           k < probs.length)
/*     */       {
/* 264 */         EmissionState nxt = (EmissionState)it.next();
/* 265 */         probs = nxt.getEmiss(i);
/* 266 */         k = 0; continue;
/* 267 */         int[] indices = stSp.getMemberIndices(k);
/* 268 */         for (int k1 = 0; k1 < indices.length; k1++) {
/* 269 */           this.maf.addCount(indices[k1], probs[k], i);
/*     */         }
/* 266 */         k++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 274 */     this.maf.transferCountsToProbs(0.0D);
/* 275 */     this.maf.initialiseCounts();
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
/*     */   public void print(PrintWriter pw)
/*     */   {
/* 296 */     List<Integer> posi = new ArrayList();
/* 297 */     StringBuffer sb2 = new StringBuffer();
/*     */     
/* 299 */     for (int i = 0; i < length(); i++) {
/* 300 */       posi.add(Integer.valueOf(i));
/* 301 */       sb2.append("%8i ");
/*     */     }
/* 303 */     pw.println(" " + com.braju.format.Format.sprintf(sb2.toString(), posi.toArray()));
/* 304 */     for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext();)
/*     */     {
/* 306 */       ((EmissionState)it.next()).print(pw, "", null);
/* 307 */       pw.println();
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
/*     */   public int size()
/*     */   {
/* 324 */     return this.dataL.size();
/*     */   }
/*     */   
/*     */   public LikelihoodDataCollection() {}
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/collection/LikelihoodDataCollection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */