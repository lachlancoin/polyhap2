/*     */ package lc1.ensj;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PedigreeDataCollection
/*     */ {
/*     */   public final Map<String, String> mother;
/*     */   public final Map<String, String> father;
/*     */   
/*     */   public PedigreeDataCollection(File pedigree)
/*     */   {
/*  25 */     this.mother = new TreeMap();
/*  26 */     this.father = new TreeMap();
/*     */     try {
/*  28 */       readPedigreeFamilies(pedigree);
/*     */     } catch (Exception exc) {
/*  30 */       exc.printStackTrace();
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
/*     */   public PedigreeDataCollection()
/*     */   {
/*  45 */     this.mother = new TreeMap();
/*  46 */     this.father = new TreeMap();
/*     */   }
/*     */   
/*     */   public PedigreeDataCollection(Map<String, String> mother2, Map<String, String> father2) {
/*  50 */     this.mother = mother2;
/*  51 */     this.father = father2;
/*     */   }
/*     */   
/*     */   private void readPedigreeFamilies(File f) throws Exception {
/*  55 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  57 */     while ((st = br.readLine()) != null) { String st;
/*  58 */       String[] str = st.split("\\s+");
/*  59 */       String moth = str[1];
/*  60 */       String fath = str[2];
/*  61 */       String chi = str[0];
/*     */       
/*  63 */       this.mother.put(chi, moth);
/*  64 */       this.father.put(chi, fath);
/*     */     }
/*  66 */     br.close();
/*     */   }
/*     */   
/*  69 */   private void readPedigreeFamiliesHapMap(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  71 */     while ((st = br.readLine()) != null) { String st;
/*  72 */       String[] father = st.split("\\s+");
/*  73 */       String id_f = father[6].split(":")[4];
/*  74 */       String str1 = father[4];
/*     */     }
/*     */     
/*  77 */     br.close();
/*     */     
/*  79 */     br = new BufferedReader(new FileReader(f));
/*  80 */     while ((st = br.readLine()) != null) {
/*  81 */       Map<Integer, String[]> m = new HashMap();
/*  82 */       Integer child_id = null;
/*  83 */       Integer mother_id = null;
/*  84 */       Integer father_id = null;
/*  85 */       for (int i = 0; i < 3; i++) {
/*  86 */         String[] str = st.split("\\s+");
/*  87 */         m.put(Integer.valueOf(Integer.parseInt(str[1])), str);
/*  88 */         if ((Integer.parseInt(str[2]) != 0) && (Integer.parseInt(str[3]) != 0)) {
/*  89 */           child_id = Integer.valueOf(Integer.parseInt(str[1]));
/*     */         }
/*  91 */         else if (str[4].equals("2")) {
/*  92 */           mother_id = Integer.valueOf(Integer.parseInt(str[1]));
/*     */         }
/*  94 */         else if (str[4].equals("1")) {
/*  95 */           father_id = Integer.valueOf(Integer.parseInt(str[1]));
/*     */         }
/*  97 */         if (i < 2) st = br.readLine();
/*     */       }
/*  99 */       String id_m = ((String[])m.get(mother_id))[6].split(":")[4];
/* 100 */       String id_c = ((String[])m.get(child_id))[6].split(":")[4];
/* 101 */       String id_f = ((String[])m.get(father_id))[6].split(":")[4];
/* 102 */       this.mother.put(id_c, id_m);
/* 103 */       this.father.put(id_c, id_f);
/*     */     }
/* 105 */     br.close();
/*     */   }
/*     */   
/*     */   public void setTrio(String mother, String father, String child) {
/* 109 */     if (this.mother.containsKey(mother)) throw new RuntimeException("!!");
/* 110 */     if (this.father.containsKey(father)) throw new RuntimeException("!!");
/* 111 */     this.mother.put(child, mother);
/* 112 */     this.father.put(child, father);
/*     */   }
/*     */   
/*     */   public void print(PrintWriter ped_pw) {
/* 116 */     for (Iterator<String> it = this.mother.keySet().iterator(); it.hasNext();) {
/* 117 */       String chi = (String)it.next();
/* 118 */       String moth = (String)this.mother.get(chi);
/* 119 */       String fath = (String)this.father.get(chi);
/* 120 */       ped_pw.println(chi + "\t" + moth + "\t" + fath);
/*     */     }
/* 122 */     ped_pw.close();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/ensj/PedigreeDataCollection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */