/*     */ package lc1.dp.data.collection;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ import lc1.dp.emissionspace.CompoundEmissionStateSpace;
/*     */ import lc1.dp.states.CompoundState;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.HaplotypeEmissionState;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ public class ChiamoDataCollection
/*     */   extends DataCollection
/*     */ {
/*  26 */   public void createDataStructure(String paramString, int paramInt) { throw new Error("Unresolved compilation problem: \n\tThe type ChiamoDataCollection must implement the inherited abstract method DataCollection.createDataStructure(String, int)\n"); } public void process(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt, Character paramCharacter1, Character paramCharacter2, Double paramDouble, boolean paramBoolean) { throw new Error("Unresolved compilation problem: \n\tThe type ChiamoDataCollection must implement the inherited abstract method DataCollection.process(String, String[], String[], int, Character, Character, Double, boolean)\n"); } public void process(String paramString, int paramInt) { throw new Error("Unresolved compilation problem: \n\tThe type ChiamoDataCollection must implement the inherited abstract method DataCollection.process(String, int)\n"); } public void process(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt) { throw new Error("Unresolved compilation problem: \n\tThe type ChiamoDataCollection must implement the inherited abstract method DataCollection.process(String, String[], String[], int)\n"); }
/*     */   
/*     */   public static DataC read(File f)
/*     */   {
/*     */     try
/*     */     {
/*  32 */       return new ChiamoDataCollection(f);
/*     */     } catch (Exception exc) {
/*  34 */       exc.printStackTrace();
/*  35 */       System.exit(0); }
/*  36 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DataC clone()
/*     */   {
/*  45 */     return new ChiamoDataCollection(this);
/*     */   }
/*     */   
/*     */   protected ChiamoDataCollection(DataCollection dat)
/*     */   {
/*  50 */     super(dat);
/*  51 */     this.dataL = new HashMap(dat.dataL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialisationStep()
/*     */   {
/*  61 */     for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext();)
/*     */     {
/*  63 */       ((EmissionState)it.next()).initialiseCounts();
/*     */     }
/*     */   }
/*     */   
/*     */   static boolean readLine(BufferedReader[] br, List<String>[] str) throws Exception {
/*  68 */     int lengthRest = Constants.restrict()[0];
/*  69 */     for (int i = 0; i < str.length; i++) {
/*  70 */       if (br[i] != null) {
/*  71 */         String stri = br[i].readLine();
/*  72 */         if (stri == null) { return false;
/*     */         }
/*  74 */         List<String> l = Arrays.asList(stri.split("\\s+"));
/*  75 */         int min = Math.min(l.size(), lengthRest + 1);
/*  76 */         str[i] = l.subList(1, min);
/*     */       }
/*     */     }
/*     */     
/*  80 */     return true;
/*     */   }
/*     */   
/*     */   static List<String> readNames(File id_file) {
/*  84 */     if ((!id_file.exists()) || (id_file.length() == 0L)) return null;
/*  85 */     List<String> l = new ArrayList();
/*     */     try {
/*  87 */       if ((!id_file.exists()) || (id_file.length() == 0L)) throw new RuntimeException("!!!");
/*  88 */       BufferedReader br = new BufferedReader(new FileReader(id_file));
/*  89 */       String st = "";
/*  90 */       while ((st = br.readLine()) != null) {
/*  91 */         l.add(st);
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/*  95 */       exc.printStackTrace();
/*     */     }
/*  97 */     return l;
/*     */   }
/*     */   
/*     */   public void extractFromTrioData() {
/* 101 */     super.extractFromTrioData();
/* 102 */     Map<String, EmissionState> l = new HashMap();
/* 103 */     for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext();)
/*     */     {
/* 105 */       EmissionState dat_i = (EmissionState)it.next();
/* 106 */       if ((dat_i instanceof CompoundState)) {
/* 107 */         EmissionState[] data_i = dat_i.split();
/* 108 */         for (int j = 0; j < data_i.length; j++) {
/* 109 */           l.put(data_i[j].getName(), data_i[j]);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 114 */         l.put(dat_i.getName(), dat_i);
/*     */       }
/*     */     }
/*     */     
/* 118 */     this.dataL = l;
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
/*     */   static String[] getIndiv(File f)
/*     */     throws Exception
/*     */   {
/* 135 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/* 137 */     String start = null;
/* 138 */     List<String> l = new ArrayList();
/* 139 */     String st; while ((st = br.readLine()) != null) { String st;
/* 140 */       String[] str = st.split("\\s+");
/* 141 */       if (start == null) {
/* 142 */         start = str[0];
/*     */       } else
/* 144 */         if (!st.startsWith(start)) break;
/* 145 */       l.add(str[1]);
/*     */     }
/* 147 */     br.close();
/* 148 */     return (String[])l.toArray(new String[0]);
/*     */   }
/*     */   
/* 151 */   public ChiamoDataCollection(File fi) throws Exception { File dir1 = new File(Constants.getDirFile());
/*     */     
/* 153 */     File snpFile = new File(dir1, Constants.chrom0() + "_snp.txt");
/* 154 */     String[] indiv = getIndiv(fi);
/* 155 */     BufferedReader br = new BufferedReader(new FileReader(fi));
/* 156 */     String st = "";
/* 157 */     List<String> A_allele1 = new ArrayList();
/* 158 */     ArrayList<Integer> loc1 = new ArrayList();
/* 159 */     ArrayList<String> snp1 = new ArrayList();
/* 160 */     readPosInfo(snpFile, new int[] { 0, 2, 4 }, false, new List[] { snp1, loc1, A_allele1 }, new Class[] { String.class, Integer.class, String.class });
/* 161 */     int start = IlluminaRDataCollection.firstGreaterThan(loc1, Constants.offset());
/* 162 */     int end = start + Constants.restrict()[0];
/* 163 */     start = Math.max(0, start);
/* 164 */     end = Math.min(end, loc1.size());
/* 165 */     this.loc = new ArrayList((Collection)loc1.subList(start, end));
/* 166 */     this.snpid = new ArrayList((Collection)snp1.subList(start, end));
/* 167 */     List<String> A_allele = A_allele1.subList(start, end);
/* 168 */     this.length = Integer.valueOf(this.loc.size());
/* 169 */     for (int i = 0; i < start; i++) {
/* 170 */       st = br.readLine();
/*     */     }
/* 172 */     CompoundEmissionStateSpace stSp = Emiss.getEmissionStateSpace(1, 2);
/* 173 */     this.length = Integer.valueOf(Math.min(Constants.restrict()[0], this.loc.size()));
/* 174 */     EmissionState[] ldata = new EmissionState[indiv.length];
/* 175 */     String[][] str = (String[][])null;
/* 176 */     for (int k = 0; k < indiv.length; k++) {
/* 177 */       ldata[k] = new HaplotypeEmissionState(indiv[k], this.length.intValue(), stSp.size(), stSp, Integer.valueOf(-1), null, 1.0D);
/*     */     }
/* 179 */     for (int i = 0; i < this.length.intValue(); i++) {
/* 180 */       String al = (String)A_allele.get(i);
/* 181 */       if (al.length() != 1) throw new RuntimeException("!!" + al);
/* 182 */       char a = al.charAt(0);
/* 183 */       for (int k = 0; k < indiv.length; k++) {
/* 184 */         String[] str1 = br.readLine().split("\\s+");
/* 185 */         if (!str1[1].equals(indiv[k])) throw new RuntimeException("!!");
/* 186 */         if (!str1[0].equals(this.snpid.get(i))) throw new RuntimeException("!!");
/* 187 */         char[] geno = str1[2].toCharArray();
/* 188 */         for (int ik = 0; ik < geno.length; ik++) {
/* 189 */           if (geno[ik] == a) geno[ik] = 'A'; else
/* 190 */             geno[ik] = 'B';
/*     */         }
/* 192 */         Arrays.sort(geno);
/* 193 */         Double prob = Double.valueOf(Double.parseDouble(str1[3]));
/* 194 */         int index = stSp.getFromString(new String(geno)).intValue();
/* 195 */         double[] dist = new double[stSp.size()];
/* 196 */         double leftover = (1.0D - prob.doubleValue()) / (dist.length - 1);
/* 197 */         for (int j = 0; j < stSp.size(); j++) {
/* 198 */           if (j == index) dist[j] = prob.doubleValue(); else
/* 199 */             dist[j] = leftover;
/*     */         }
/* 201 */         ((HaplotypeEmissionState)ldata[k]).setTheta(dist, i);
/*     */       }
/*     */     }
/* 204 */     for (int i = 0; i < ldata.length; i++)
/*     */     {
/* 206 */       this.dataL.put(ldata[i].getName(), ldata[i]);
/*     */     }
/* 208 */     calculateMLGenotypeData();
/* 209 */     initialiseMaf();
/* 210 */     br.close();
/*     */   }
/*     */   
/*     */ 
/*     */   public ChiamoDataCollection(List<Integer> locs)
/*     */   {
/* 216 */     this.length = Integer.valueOf(locs.size());
/* 217 */     this.loc = locs;
/*     */   }
/*     */   
/*     */ 
/*     */   public void calculateMLGenotypeData()
/*     */   {
/* 223 */     this.data.clear();
/* 224 */     for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext();) {
/* 225 */       EmissionState ld = (EmissionState)it.next();
/*     */       
/* 227 */       PIGData res = ld.getGenotypeData();
/* 228 */       this.data.put(res.getName(), res);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void initialiseMaf()
/*     */   {
/* 235 */     CompoundEmissionStateSpace stSp = Emiss.getEmissionStateSpace(1);
/* 236 */     lc1.dp.emissionspace.EmissionStateSpace stSp1 = stSp.getMembers()[0];
/* 237 */     this.maf = makeMafState(stSp1);
/* 238 */     for (int i = 0; i < this.length.intValue(); i++) { double[] probs;
/* 239 */       int k; for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext(); 
/*     */           
/*     */ 
/* 242 */           k < probs.length)
/*     */       {
/* 240 */         EmissionState nxt = (EmissionState)it.next();
/* 241 */         probs = nxt.getEmiss(i);
/* 242 */         k = 0; continue;
/* 243 */         int[] indices = stSp.getMemberIndices(k);
/* 244 */         for (int k1 = 0; k1 < indices.length; k1++) {
/* 245 */           this.maf.addCount(indices[k1], probs[k], i);
/*     */         }
/* 242 */         k++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 250 */     this.maf.transferCountsToProbs(0.0D);
/* 251 */     this.maf.initialiseCounts();
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
/* 272 */     List<Integer> posi = new ArrayList();
/* 273 */     StringBuffer sb2 = new StringBuffer();
/*     */     
/* 275 */     for (int i = 0; i < length(); i++) {
/* 276 */       posi.add(Integer.valueOf(i));
/* 277 */       sb2.append("%8i ");
/*     */     }
/* 279 */     pw.println(" " + com.braju.format.Format.sprintf(sb2.toString(), posi.toArray()));
/* 280 */     for (Iterator<EmissionState> it = this.dataL.values().iterator(); it.hasNext();)
/*     */     {
/* 282 */       ((EmissionState)it.next()).print(pw, "", null);
/* 283 */       pw.println();
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
/*     */   public void restricToAlias(Collection<String> alias)
/*     */   {
/* 298 */     List<String> keys = new ArrayList((Collection)this.data.keySet());
/* 299 */     for (Iterator<String> it = keys.iterator(); it.hasNext();) {
/* 300 */       String key = (String)it.next();
/* 301 */       if (!alias.contains(key)) {
/* 302 */         this.dataL.remove(key);
/* 303 */         this.recSites.remove(key);
/* 304 */         this.viterbi.remove(key);
/* 305 */         this.data.remove(key);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int size()
/*     */   {
/* 313 */     return this.dataL.size();
/*     */   }
/*     */   
/*     */   public void maximisationStep(double[] pseudo, int i) {}
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/collection/ChiamoDataCollection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */