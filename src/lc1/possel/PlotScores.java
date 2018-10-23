/*     */ package lc1.possel;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintStream;
/*     */ import javax.swing.JFrame;
/*     */ import org.jfree.chart.ChartFactory;
/*     */ import org.jfree.chart.ChartPanel;
/*     */ import org.jfree.chart.JFreeChart;
/*     */ import org.jfree.chart.axis.NumberAxis;
/*     */ import org.jfree.chart.plot.PlotOrientation;
/*     */ import org.jfree.chart.plot.XYPlot;
/*     */ import org.jfree.data.xy.XYSeries;
/*     */ import org.jfree.data.xy.XYSeriesCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlotScores
/*     */   extends JFrame
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  27 */     File user = new File(System.getProperty("user.dir"));
/*  28 */     File norm = user.listFiles(new FilenameFilter1("score_1"))[0];
/*  29 */     File dels = user.listFiles(new FilenameFilter1("score_0"))[0];
/*  30 */     File ampl = user.listFiles(new FilenameFilter1("score_2"))[0];
/*     */     
/*  32 */     String[] str = { "1" };
/*  33 */     for (int i = 0; i < str.length; i++) {
/*  34 */       File pritch = new File(user.getParent(), "ihs/ceu.ch" + str[i]);
/*  35 */       PlotScores localPlotScores = new PlotScores(norm, dels, ampl, pritch, str[i], 158, 159);
/*     */     } }
/*     */   
/*  38 */   static Dimension maxDim = new Dimension(500, 500);
/*     */   
/*  40 */   XYSeries dels = new XYSeries("deletion allele");
/*  41 */   XYSeries pritch = new XYSeries("pritchard");
/*  42 */   XYSeries ampl = new XYSeries("amplified allele");
/*  43 */   XYSeries norm = new XYSeries("normal allele");
/*     */   String chr;
/*     */   ChartPanel chartPanel;
/*     */   final int start;
/*     */   final int end;
/*     */   
/*  49 */   PlotScores(File norm, File del, File ampl, File pritch, String chr, int start, int end) { super(chr);
/*  50 */     this.chr = chr;
/*  51 */     this.start = (start * 1000000);
/*  52 */     this.end = (end * 1000000);
/*  53 */     int cnt0 = read(del, this.dels, chr);
/*  54 */     int cnt1 = read(norm, this.norm, chr);
/*  55 */     int cnt2 = read(ampl, this.ampl, chr);
/*  56 */     System.err.println("cnts are " + cnt0 + " " + cnt1 + " " + cnt2);
/*  57 */     readPritch(pritch, this.pritch);
/*  58 */     this.chartPanel = new ChartPanel(getChart());
/*     */     
/*  60 */     this.chartPanel.setPreferredSize(maxDim);
/*  61 */     getContentPane().add(this.chartPanel);
/*  62 */     pack();
/*  63 */     setVisible(true);
/*     */   }
/*     */   
/*     */   private int read(File del, XYSeries dels2, String chr) {
/*     */     try {
/*  68 */       BufferedReader br = new BufferedReader(new FileReader(del));
/*  69 */       String st = "";
/*  70 */       br.readLine();
/*  71 */       double prev_sc = -100.0D;
/*  72 */       int prev_loc = 0;
/*  73 */       int cnt = 0;
/*  74 */       while ((st = br.readLine()) != null) {
/*  75 */         String[] str = st.split("\\s+");
/*     */         
/*  77 */         int loc = Integer.parseInt(str[3]);
/*     */         
/*  79 */         double ihs = Double.parseDouble(str[7]);
/*  80 */         if ((loc >= this.start) && (loc < this.end) && (str[0].equals(chr))) {
/*  81 */           dels2.add(loc, ihs);
/*     */         }
/*     */         
/*  84 */         if ((Math.abs(ihs - prev_sc) > 0.1D) || (loc - prev_loc > 100000)) cnt++;
/*  85 */         prev_loc = loc;
/*  86 */         prev_sc = ihs;
/*     */       }
/*     */       
/*  89 */       return cnt;
/*     */     } catch (Exception exc) {
/*  91 */       exc.printStackTrace();
/*     */     }
/*  93 */     return 0;
/*     */   }
/*     */   
/*     */   private void readPritch(File del, XYSeries dels2) {
/*  97 */     try { BufferedReader br = new BufferedReader(new FileReader(del));
/*  98 */       String st = "";
/*  99 */       br.readLine();
/* 100 */       while ((st = br.readLine()) != null) {
/* 101 */         String[] str = st.split("\\s+");
/* 102 */         if (!str[3].equals("-")) {
/* 103 */           int loc = Integer.parseInt(str[1]);
/* 104 */           if ((loc >= this.start) && (loc < this.end)) {
/* 105 */             double ihs = Double.parseDouble(str[3]);
/* 106 */             dels2.add(loc, ihs);
/*     */           }
/*     */         }
/*     */       }
/* 110 */     } catch (Exception exc) { exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   JFreeChart getChart()
/*     */   {
/* 116 */     XYSeriesCollection coll_l = new XYSeriesCollection();
/* 117 */     coll_l.addSeries(this.dels);
/* 118 */     coll_l.addSeries(this.ampl);
/* 119 */     coll_l.addSeries(this.pritch);
/* 120 */     coll_l.addSeries(this.norm);
/*     */     
/* 122 */     JFreeChart chart = ChartFactory.createXYLineChart(
/* 123 */       "IHH for  " + this.chr, 
/* 124 */       "Location ", 
/* 125 */       "IHH", 
/* 126 */       coll_l, 
/* 127 */       PlotOrientation.VERTICAL, true, 
/* 128 */       true, 
/* 129 */       false);
/*     */     
/*     */ 
/* 132 */     ((NumberAxis)((XYPlot)chart.getPlot()).getRangeAxis())
/* 133 */       .setAutoRangeIncludesZero(false);
/* 134 */     chart.setBackgroundPaint(Color.white);
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
/* 152 */     return chart;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/possel/PlotScores.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */