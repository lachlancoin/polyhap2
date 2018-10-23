/*     */ package dataFormat;
/*     */ 
/*     */ import cern.colt.matrix.DoubleMatrix2D;
/*     */ import cern.colt.matrix.doublealgo.Statistic;
/*     */ import cern.colt.matrix.impl.DenseDoubleMatrix2D;
/*     */ import cern.colt.matrix.linalg.Algebra;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OrderingMarkers
/*     */ {
/*  26 */   Algebra lg = new Algebra();
/*     */   DoubleMatrix2D X;
/*     */   DoubleMatrix2D XT;
/*     */   DoubleMatrix2D Y;
/*     */   List<String[]> valsCorr;
/*     */   
/*  32 */   public void getCorr(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*  33 */     this.valsCorr = new ArrayList();
/*  34 */     String st = "";
/*  35 */     while ((st = br.readLine()) != null) {
/*  36 */       this.valsCorr.add(st.split("\\s+"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  41 */   SortedMap<Double, List<Integer>> distanceSorted = new TreeMap();
/*  42 */   Map<List<Integer>, Double> distanceSorted1 = new HashMap();
/*     */   
/*  44 */   public void setCorr() { for (int i = 0; i < this.valsCorr.size(); i++) {
/*  45 */       for (int j = i + 1; j < ((String[])this.valsCorr.get(i)).length; j++) {
/*  46 */         Double dis = Double.valueOf(1.0D - Math.abs(Double.parseDouble(((String[])this.valsCorr.get(i))[j])));
/*  47 */         List<Integer> positionIndex = new ArrayList();
/*  48 */         positionIndex.add(Integer.valueOf(i));positionIndex.add(Integer.valueOf(j));
/*  49 */         if (this.distanceSorted.containsKey(dis)) {
/*  50 */           dis = Double.valueOf(dis.doubleValue() + 1.0E-8D);
/*  51 */           while (this.distanceSorted.containsKey(dis)) {
/*  52 */             dis = Double.valueOf(dis.doubleValue() + 1.0E-8D);
/*     */           }
/*  54 */           this.distanceSorted.put(dis, positionIndex);
/*  55 */           this.distanceSorted1.put(positionIndex, dis);
/*     */         }
/*     */         else {
/*  58 */           this.distanceSorted.put(dis, positionIndex);
/*  59 */           this.distanceSorted1.put(positionIndex, dis);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   Integer nextIndex;
/*     */   public List<Integer> getOrdering() {
/*  67 */     List<Integer> orderPre = new ArrayList();
/*  68 */     for (Iterator<Double> is = this.distanceSorted.keySet().iterator(); is.hasNext();) {
/*  69 */       Double s = (Double)is.next();
/*  70 */       List<Integer> index = (List)this.distanceSorted.get(s);
/*  71 */       orderPre.add((Integer)index.get(0));orderPre.add((Integer)index.get(1));
/*  72 */       break;
/*     */     }
/*  74 */     getNext(orderPre);
/*  75 */     while (this.nextIndex != null) {
/*  76 */       orderPre = getNewOrder(orderPre);
/*  77 */       getNext(orderPre);
/*     */     }
/*  79 */     return orderPre;
/*     */   }
/*     */   
/*     */   public List<Integer> getNewOrder(List<Integer> orderPre)
/*     */   {
/*  84 */     int maxIndex = 0;
/*  85 */     double min = Double.MAX_VALUE;
/*  86 */     for (int i = 0; i < orderPre.size() + 1; i++) {
/*  87 */       double sum = getTotalDistance(this.nextIndex.intValue(), i, orderPre);
/*  88 */       if (sum < min) {
/*  89 */         min = sum;
/*  90 */         maxIndex = i;
/*     */       }
/*     */     }
/*  93 */     List<Integer> newOrder = new ArrayList();
/*  94 */     for (int i = 0; i < orderPre.size(); i++)
/*  95 */       if (i == maxIndex) {
/*  96 */         newOrder.add(this.nextIndex);
/*  97 */         newOrder.add((Integer)orderPre.get(i));
/*     */       } else {
/*  99 */         newOrder.add((Integer)orderPre.get(i));
/*     */       }
/* 101 */     if (maxIndex == orderPre.size()) newOrder.add(this.nextIndex);
/* 102 */     return newOrder;
/*     */   }
/*     */   
/*     */   public List<Integer> getTemp(List<Integer> temp, int in1, int in2) {
/* 106 */     if (in1 < in2) {
/* 107 */       temp.add(Integer.valueOf(in1));temp.add(Integer.valueOf(in2));
/*     */     }
/*     */     else {
/* 110 */       temp.add(Integer.valueOf(in2));temp.add(Integer.valueOf(in1));
/*     */     }
/* 112 */     return temp;
/*     */   }
/*     */   
/*     */   public double getTotalDistance(int nextIndex, int position, List<Integer> orderPre) {
/* 116 */     double sum = 0.0D;
/* 117 */     List<Integer> temp = new ArrayList();
/* 118 */     if (position == 0) {
/* 119 */       temp = getTemp(temp, nextIndex, ((Integer)orderPre.get(0)).intValue());
/* 120 */       sum += ((Double)this.distanceSorted1.get(temp)).doubleValue();
/* 121 */       temp.clear();
/* 122 */       for (int i = 0; i < orderPre.size() - 1; i++) {
/* 123 */         temp = getTemp(temp, ((Integer)orderPre.get(i)).intValue(), ((Integer)orderPre.get(i + 1)).intValue());
/* 124 */         sum += ((Double)this.distanceSorted1.get(temp)).doubleValue();
/* 125 */         temp.clear();
/*     */       }
/*     */     }
/* 128 */     else if (position == orderPre.size()) {
/* 129 */       temp = getTemp(temp, nextIndex, ((Integer)orderPre.get(orderPre.size() - 1)).intValue());
/* 130 */       sum += ((Double)this.distanceSorted1.get(temp)).doubleValue();
/* 131 */       temp.clear();
/* 132 */       for (int i = 0; i < orderPre.size() - 1; i++) {
/* 133 */         temp = getTemp(temp, ((Integer)orderPre.get(i)).intValue(), ((Integer)orderPre.get(i + 1)).intValue());
/* 134 */         sum += ((Double)this.distanceSorted1.get(temp)).doubleValue();
/* 135 */         temp.clear();
/*     */       }
/*     */     }
/*     */     else {
/* 139 */       for (int i = 1; i < orderPre.size(); i++) {
/* 140 */         if (i == position) {
/* 141 */           temp = getTemp(temp, nextIndex, ((Integer)orderPre.get(i - 1)).intValue());
/* 142 */           sum += ((Double)this.distanceSorted1.get(temp)).doubleValue();
/* 143 */           temp.clear();
/* 144 */           temp = getTemp(temp, nextIndex, ((Integer)orderPre.get(i)).intValue());
/* 145 */           sum += ((Double)this.distanceSorted1.get(temp)).doubleValue();
/* 146 */           temp.clear();
/*     */         }
/*     */         else {
/* 149 */           temp = getTemp(temp, ((Integer)orderPre.get(i)).intValue(), ((Integer)orderPre.get(i - 1)).intValue());
/* 150 */           sum += ((Double)this.distanceSorted1.get(temp)).doubleValue();
/* 151 */           temp.clear();
/*     */         }
/*     */       }
/*     */     }
/* 155 */     return sum;
/*     */   }
/*     */   
/*     */   List<String[]> valsInten;
/*     */   double meanPheno;
/*     */   public void getNext(List<Integer> orderPre) {
/* 161 */     this.nextIndex = null;
/* 162 */     for (Iterator<Double> is = this.distanceSorted.keySet().iterator(); is.hasNext();) {
/* 163 */       Double s = (Double)is.next();
/* 164 */       List<Integer> index = (List)this.distanceSorted.get(s);
/* 165 */       if ((!orderPre.containsAll(index)) && (
/* 166 */         (orderPre.contains(index.get(0))) || (orderPre.contains(index.get(1))))) {
/* 167 */         if (orderPre.contains(index.get(0))) {
/* 168 */           this.nextIndex = ((Integer)index.get(1)); break;
/*     */         }
/* 170 */         if (orderPre.contains(index.get(1))) {
/* 171 */           this.nextIndex = ((Integer)index.get(0)); break;
/*     */         }
/* 173 */         throw new RuntimeException("!!");
/* 174 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void printOrder(PrintStream ps, List<Integer> order)
/*     */   {
/* 181 */     for (int i = 0; i < order.size(); i++) {
/* 182 */       ps.println(order.get(i));
/*     */     }
/*     */   }
/*     */   
/*     */   public void getPhenotype(File f)
/*     */     throws Exception
/*     */   {
/* 189 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 190 */     this.valsInten = new ArrayList();
/* 191 */     String st = "";
/* 192 */     br.readLine();br.readLine();
/* 193 */     while ((st = br.readLine()) != null) {
/* 194 */       this.valsInten.add(st.split("\\s+"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setIntensity()
/*     */   {
/* 201 */     DoubleMatrix2D intensityMatrix = new DenseDoubleMatrix2D(((String[])this.valsInten.get(0)).length - 4, this.valsInten.size());
/* 202 */     List<Integer> missingCount = new ArrayList();
/* 203 */     for (int m = 0; m < this.valsInten.size(); m++)
/*     */     {
/* 205 */       int count = 0;
/* 206 */       for (int i = 4; i < ((String[])this.valsInten.get(m)).length; i++) {
/* 207 */         if (((String[])this.valsInten.get(m))[i].charAt(0) == 'N') {
/* 208 */           count++;
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 213 */           intensityMatrix.setQuick(i - 4, m, Double.parseDouble(((String[])this.valsInten.get(m))[i]));
/*     */         }
/*     */       }
/* 216 */       missingCount.add(Integer.valueOf(count));
/*     */     }
/*     */     
/* 219 */     DoubleMatrix2D covarianceMat = Statistic.covariance(intensityMatrix);
/* 220 */     DoubleMatrix2D corrMat = Statistic.correlation(covarianceMat);
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 225 */       OrderingMarkers om = new OrderingMarkers();
/* 226 */       om.getCorr(new File("correlationMatrix.txt"));
/* 227 */       om.setCorr();
/* 228 */       PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("orderedMarker.txt"))));
/* 229 */       om.printOrder(ps, om.getOrdering());
/* 230 */       ps.close();
/*     */     } catch (Exception exc) {
/* 232 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/OrderingMarkers.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */