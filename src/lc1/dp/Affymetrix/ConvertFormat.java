/*     */ package lc1.dp.Affymetrix;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.List;
/*     */ import lc1.dp.data.collection.SimpleDataCollection;
/*     */ import lc1.dp.data.representation.ComparableArray;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConvertFormat
/*     */ {
/*     */   double[][][] l;
/*  28 */   int numGen = 15;
/*     */   final int numIndiv;
/*     */   final int numSnp;
/*     */   Integer[] pos;
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*  35 */     Constants.modelCNP = 6;
/*  36 */     Constants.offset = 0;
/*  37 */     String f = "L.unphased.txt";
/*     */     
/*  39 */     String f1 = "sim.unphased.txt";
/*     */     try {
/*  41 */       conv = new ConvertFormat(f, f1);
/*     */     } catch (Exception exc) { ConvertFormat conv;
/*  43 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   ConvertFormat(String file, String posFSt) throws Exception {
/*  48 */     BufferedReader br = new BufferedReader(new FileReader(new File(file)));
/*  49 */     PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File("L.50K_Merged.CEPH.Affy.txt"))));
/*  50 */     String st = "";
/*  51 */     File posF = new File(posFSt);
/*  52 */     SimpleDataCollection data = new SimpleDataCollection();
/*  53 */     String[] names = SimpleDataCollection.readAffy(posF.getParentFile(), posF.getName(), false, data, 2);
/*  54 */     PrintWriter delPos = new PrintWriter(new BufferedWriter(new FileWriter(new File("cnv_orig.txt"))));
/*  55 */     data.printDeletedPositions(delPos);
/*  56 */     delPos.close();
/*  57 */     List<Integer> pos1 = data.loc;
/*  58 */     this.pos = ((Integer[])pos1.toArray(new Integer[0]));
/*  59 */     this.numSnp = this.pos.length;
/*     */     
/*  61 */     this.numIndiv = names.length;
/*  62 */     System.err.println("num indiv " + this.numIndiv + " " + this.numSnp + " " + this.numGen + " " + this.numIndiv * this.numSnp * this.numGen);
/*  63 */     EmissionStateSpace emiss = Emiss.getEmissionStateSpace(1);
/*  64 */     this.l = new double[this.numIndiv][this.numSnp][this.numGen];
/*     */     
/*  66 */     for (int i = 0; i < this.numIndiv; i++) {
/*  67 */       PIGData dat = data.get(names[i]);
/*     */       
/*  69 */       for (int j = 0; j < this.numSnp; j++) {
/*  70 */         int rightIndex = emiss.getGenotype(dat.getElement(j)).intValue();
/*  71 */         for (int k = 0; k < this.numGen; k++) {
/*  72 */           this.l[i][j][k] = Double.parseDouble(br.readLine());
/*     */         }
/*  74 */         k = Constants.getMin(this.l[i][j]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */     String str1 = br.readLine();
/*  83 */     if (str1 != null) throw new RuntimeException("not null " + str1);
/*  84 */     br.close();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */     out.print("chrom  pos  ");
/*  93 */     for (int i = 0; i < this.numIndiv; i++) {
/*  94 */       String nme = names[i];
/*  95 */       for (int k = 0; k < this.numGen; k++) {
/*  96 */         out.print(nme + "_" + k + " ");
/*     */       }
/*     */     }
/*  99 */     out.println();
/* 100 */     for (int j = 0; j < this.numSnp; j++) {
/* 101 */       out.print("T\t" + this.pos[j] + "\t");
/* 102 */       for (int i = 0; i < this.numIndiv; i++) {
/* 103 */         for (int k = 0; k < this.numGen; k++) {
/* 104 */           out.print(this.l[i][j][k] + " ");
/*     */         }
/*     */       }
/* 107 */       out.println();
/*     */     }
/*     */     
/* 110 */     out.close();
/*     */     
/* 112 */     PrintWriter out1 = new PrintWriter(new BufferedWriter(new FileWriter(new File("calls.50K_Merged.CEPH.Affy.txt"))));
/* 113 */     PrintWriter indiv = new PrintWriter(new BufferedWriter(new FileWriter(new File("indiv.txt"))));
/* 114 */     for (int i = 0; i < this.numIndiv; i++) {
/* 115 */       indiv.println(names[i]);
/*     */     }
/* 117 */     indiv.close();
/*     */     
/* 119 */     out1.print("chrom  pos  ");
/* 120 */     for (int i = 0; i < this.numIndiv; i++) {
/* 121 */       String nme = names[i];
/* 122 */       out1.print(nme + " ");
/*     */     }
/* 124 */     out1.println();
/* 125 */     for (int j = 0; j < this.numSnp; j++) {
/* 126 */       out1.print("T\t" + this.pos[j] + "\t");
/* 127 */       for (int i = 0; i < this.numIndiv; i++) {
/* 128 */         String key = names[i];
/* 129 */         PIGData d = data.get(key);
/* 130 */         ComparableArray comp = (ComparableArray)d.getElement(j);
/* 131 */         out1.print("\"");
/* 132 */         out1.print(((Emiss)comp.get(0)).toStringShort() + ((Emiss)comp.get(1)).toStringShort());
/* 133 */         out1.print("\" ");
/*     */       }
/* 135 */       out1.println();
/*     */     }
/* 137 */     out1.close();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/Affymetrix/ConvertFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */