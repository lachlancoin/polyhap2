/*    */ package lc1.dp.appl;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JScrollPane;
/*    */ import javax.swing.JTabbedPane;
/*    */ import lc1.dp.data.collection.Phenotypes;
/*    */ import lc1.dp.model.FreeHaplotypeHMM;
/*    */ import lc1.dp.swing.HMMPanel;
/*    */ import lc1.dp.swing.HaplotypePanel;
/*    */ import lc1.dp.swing.IndividualPlot;
/*    */ import lc1.util.Constants;
/*    */ 
/*    */ public class CNVFrame
/*    */   extends JFrame
/*    */ {
/* 20 */   List<HMMPanel> hmm_tab = new ArrayList();
/* 21 */   JTabbedPane tabs = new JTabbedPane();
/*    */   
/*    */   public CNVFrame() {
/* 24 */     add(this.tabs);
/* 25 */     setSize(new Dimension(800, 700));
/* 26 */     setDefaultCloseOperation(2);
/*    */   }
/*    */   
/* 29 */   public HMMPanel addHMMTab(FreeHaplotypeHMM hmm, List<Integer> location, List<Character> maj, List<Character> min, int noIndiv, Phenotypes pheno) { HMMPanel hmmp = new HMMPanel(hmm, location, maj, min, noIndiv, pheno);
/* 30 */     JScrollPane jscp = new JScrollPane(hmmp, 20, 32);
/* 31 */     this.tabs.add("HMM", jscp);
/* 32 */     pack();
/* 33 */     this.hmm_tab.add(hmmp);
/* 34 */     if (Constants.plot() > 1) setVisible(true);
/* 35 */     return hmmp;
/*    */   }
/*    */   
/* 38 */   public void addTab(String name, JComponent ip) { this.tabs.add(name, ip);
/* 39 */     pack();
/* 40 */     if (Constants.plot() > 1) setVisible(true);
/*    */   }
/*    */   
/* 43 */   public void setToPlot(int level) { for (int i = 0; i < this.tabs.countComponents(); i++) {
/* 44 */       if ((this.tabs.getComponent(i) instanceof IndividualPlot)) {
/* 45 */         IndividualPlot ip = (IndividualPlot)this.tabs.getComponent(i);
/* 46 */         ip.setToPlot(level);
/*    */       }
/* 48 */       else if (((this.tabs.getComponent(i) instanceof HaplotypePanel)) && 
/* 49 */         (level == 2)) { ((HaplotypePanel)this.tabs.getComponent(i)).setToPlot();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void clearTabs()
/*    */   {
/* 56 */     this.tabs.removeAll();
/*    */   }
/*    */   
/*    */   public void update() {
/* 60 */     for (int i = 0; i < this.hmm_tab.size(); i++) {
/* 61 */       ((HMMPanel)this.hmm_tab.get(i)).update();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/appl/CNVFrame.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */