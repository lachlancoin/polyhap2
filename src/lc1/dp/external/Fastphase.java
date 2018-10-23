/*      */ package lc1.dp.external;
/*      */ 
/*      */ import com.braju.format.Format;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileWriter;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeMap;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.logging.ConsoleHandler;
/*      */ import java.util.logging.Formatter;
/*      */ import java.util.logging.LogRecord;
/*      */ import java.util.logging.Logger;
/*      */ import lc1.dp.appl.CNVFrame;
/*      */ import lc1.dp.appl.DickFormat;
/*      */ import lc1.dp.core.BaumWelchTrainer;
/*      */ import lc1.dp.core.Sampler;
/*      */ import lc1.dp.data.collection.DataC;
/*      */ import lc1.dp.data.collection.DataCollection;
/*      */ import lc1.dp.data.collection.IlluminaRDataCollection;
/*      */ import lc1.dp.data.collection.LikelihoodDataCollection;
/*      */ import lc1.dp.data.representation.ComparableArray;
/*      */ import lc1.dp.data.representation.Emiss;
/*      */ import lc1.dp.emissionspace.EmissionStateSpace;
/*      */ import lc1.dp.model.CachedHMM;
/*      */ import lc1.dp.model.CollapsedHMM;
/*      */ import lc1.dp.model.CompoundMarkovModel;
/*      */ import lc1.dp.model.FreeHaplotypeHMM;
/*      */ import lc1.dp.model.HaplotypeHMM;
/*      */ import lc1.dp.model.MarkovModel;
/*      */ import lc1.dp.model.PairMarkovModel;
/*      */ import lc1.dp.states.EmissionState;
/*      */ import lc1.dp.states.HaplotypeEmissionState;
/*      */ import lc1.dp.states.PairEmissionState;
/*      */ import lc1.dp.swing.HMMPanel;
/*      */ import lc1.dp.swing.HaplotypePanel;
/*      */ import lc1.dp.swing.IndividualPlot;
/*      */ import lc1.dp.transition.ExponentialTransitionProbs;
/*      */ import lc1.stats.Dirichlet;
/*      */ import lc1.stats.SimpleExtendedDistribution;
/*      */ import lc1.util.Constants;
/*      */ 
/*      */ 
/*      */ public class Fastphase
/*      */ {
/*   59 */   public static final Logger logger = ;
/*   60 */   static long time = System.currentTimeMillis();
/*   61 */   List<CompoundMarkovModel> best = null;
/*   62 */   Double bestSc = null;
/*      */   final int noSites;
/*      */   
/*      */   static {
/*   66 */     try { ConsoleHandler handler = new ConsoleHandler();
/*      */       
/*      */ 
/*   69 */       Formatter formatter = 
/*   70 */         new Formatter() {
/*      */           public String format(LogRecord record) {
/*   72 */             return record.getSourceClassName() + ":\n" + record.getMessage() + "\n";
/*      */           }
/*   74 */         };
/*   75 */         handler.setFormatter(formatter);
/*      */       }
/*      */       catch (Exception exc) {
/*   78 */         exc.printStackTrace();
/*   79 */         System.exit(0);
/*      */       }
/*      */     }
/*      */     
/*      */     private static void change(Comparable[] comp) {
/*   84 */       Comparable tmp = comp[0];
/*   85 */       comp[0] = comp[1];
/*   86 */       comp[1] = tmp;
/*      */     }
/*      */     
/*      */ 
/*      */     private static Double[] geom(Double[] d)
/*      */     {
/*   92 */       double[] prod = { 1.0D, 0.0D, 1.0D };
/*      */       
/*   94 */       for (int i = 0; i < d.length; i++) {
/*   95 */         prod[0] += Math.log(d[i].doubleValue());
/*   96 */         prod[1] += d[i].doubleValue();
/*   97 */         prod[2] = Math.min(prod[2], d[i].doubleValue());
/*      */       }
/*      */       
/*  100 */       double len = d.length;
/*  101 */       return new Double[] { Double.valueOf(Math.exp(prod[0] / len)), Double.valueOf(prod[1] / len), Double.valueOf(prod[2]) };
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     static boolean isSame(Comparable[] emiss)
/*      */     {
/*  108 */       return emiss[0] == emiss[1];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     final DataC dataC;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     final int modelLength;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private double getMax(Double[] doubles)
/*      */     {
/*  132 */       double max = Double.NEGATIVE_INFINITY;
/*  133 */       for (int i = 0; i < doubles.length; i++) {
/*  134 */         if (doubles[i].doubleValue() > max) max = doubles[i].doubleValue();
/*      */       }
/*  136 */       return max;
/*      */     }
/*      */     
/*      */     public void trainCases(int numIt1, PairMarkovModel hmm_union, PairMarkovModel hmm_cases, int rep, boolean emiss, boolean trans) throws Exception
/*      */     {
/*  141 */       logger.info("training cases");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public TrainingElement getTrainingElement(FreeHaplotypeHMM hmm)
/*      */     {
/*  149 */       return new TrainingElement(hmm);
/*      */     }
/*      */     
/*      */     public Iterator<TrainingElement> getTrainingElements(final Iterator<MarkovModel> hmm) throws Exception {
/*  153 */       final TrainingElement first = new TrainingElement((MarkovModel)hmm.next());
/*  154 */       new Iterator() {
/*  155 */         boolean isfirst = true;
/*      */         
/*      */ 
/*  158 */         public boolean hasNext() { return (this.isfirst) || (hmm.hasNext()); }
/*      */         
/*      */         public Fastphase.TrainingElement next() {
/*  161 */           if (this.isfirst) {
/*  162 */             this.isfirst = false;
/*  163 */             return first;
/*      */           }
/*  165 */           return new Fastphase.TrainingElement(Fastphase.this, (MarkovModel)hmm.next());
/*      */         }
/*      */         
/*      */         public void remove() {}
/*      */       };
/*      */     }
/*      */     
/*  172 */     public void prime(HaplotypeHMM hmm, int prime) { List<EmissionState> l = new ArrayList();
/*  173 */       for (Iterator<EmissionState> it = this.dataC.dataLvalues(); it.hasNext();) {
/*  174 */         l.add((EmissionState)it.next());
/*      */       }
/*  176 */       EmissionStateSpace emStSp2 = Emiss.getEmissionStateSpace(1);
/*  177 */       EmissionStateSpace emStSp1 = ((lc1.dp.emissionspace.CompoundEmissionStateSpace)emStSp2).getMembers()[0];
/*  178 */       int[][] map = new int[emStSp2.size()][];
/*  179 */       for (int k = 0; k < map.length; k++) {
/*  180 */         ComparableArray comp = (ComparableArray)emStSp2.get(k);
/*  181 */         map[k] = new int[comp.size()];
/*  182 */         for (int i = 0; i < map[k].length; i++) {
/*  183 */           map[k][i] = emStSp1.get(comp.get(i)).intValue();
/*      */         }
/*      */       }
/*  186 */       int start = Constants.nextInt(hmm.noSnps.intValue() - prime);
/*  187 */       for (int j = 1; j < 2; 
/*      */           
/*  189 */           j++) {
/*  190 */         EmissionState ld = (EmissionState)l.get(j);
/*  191 */         HaplotypeEmissionState hes = (HaplotypeEmissionState)hmm.getState(j);
/*      */         
/*      */ 
/*  194 */         for (int i = start; (i < ld.length()) && (i < start + prime); i++) {
/*  195 */           double[] probs2 = new double[emStSp1.size()];
/*  196 */           Arrays.fill(probs2, 0.0D);
/*  197 */           double[] probs1 = ld.getEmiss(i);
/*  198 */           double sum = 0.0D;
/*  199 */           for (int k = 0; k < probs1.length; k++)
/*      */           {
/*  201 */             if (probs1[k] != 0.0D) {
/*  202 */               int[] mapping = map[k];
/*  203 */               for (int k1 = 0; k1 < mapping.length; k1++) {
/*  204 */                 probs2[mapping[k1]] += probs1[k];
/*  205 */                 sum += probs1[k];
/*      */               }
/*      */             }
/*      */           }
/*  209 */           for (int k = 0; k < probs2.length; k++) {
/*  210 */             probs2[k] /= sum;
/*      */           }
/*  212 */           hes.emissions[i] = new SimpleExtendedDistribution(new Dirichlet(probs2, 10000.0D));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  218 */     int overallIndex = 0;
/*      */     
/*  220 */     public void train(TrainingElement train, boolean swtch) throws Exception { if (swtch) {
/*  221 */         this.dataC.extractFromTrioData();
/*  222 */         updateData();
/*      */       }
/*      */       
/*  225 */       if (Constants.prime() > 0) {
/*  226 */         if ((train.hapHMM instanceof FreeHaplotypeHMM)) { prime((FreeHaplotypeHMM)train.hapHMM, Constants.prime());
/*  227 */         } else if ((train.hapHMM instanceof CompoundMarkovModel)) {
/*  228 */           MarkovModel mm = ((CompoundMarkovModel)train.hapHMM).getMemberModels()[1];
/*  229 */           if (!mm.getName().startsWith("allele")) throw new RuntimeException("!! " + mm.getName());
/*  230 */           prime((FreeHaplotypeHMM)mm, Constants.prime());
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  235 */       logger.info("before simple emissions");
/*      */       
/*  237 */       int[] numIt = Constants.numIt();
/*      */       
/*  239 */       if (Constants.writeHMM() >= 1)
/*  240 */         writeHMM(train.hmm, new File(this.hmmFile.getAbsolutePath() + "_bef_" + this.overallIndex + Constants.column()), this.dataC);
/*  241 */       for (int i = 0; i < numIt.length; i++) {
/*  242 */         train.initialise(numIt[i], Constants.precision());
/*  243 */         for (int i1 = 0;; i1++) {
/*  244 */           if ((i1 == train.maxIt + 1) && (Constants.plot >= 1)) {
/*  245 */             DickFormat.cnvf.setToPlot(2);
/*      */           }
/*  247 */           else if ((i1 == train.maxIt) && (Constants.plot >= 1)) {
/*  248 */             DickFormat.cnvf.setToPlot(1);
/*      */           }
/*  250 */           boolean isLast = 
/*      */           
/*  252 */             train.train(i1);
/*  253 */           Logger.global.info("writing");
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  260 */           if (isLast)
/*      */             break;
/*      */         }
/*  263 */         if (i < numIt.length - 1) {
/*  264 */           logger.info("switching hmm ");
/*      */           
/*  266 */           train.switchHMM();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  274 */       if (swtch) {
/*  275 */         logger.info("arranging according to pedigree");
/*  276 */         this.dataC.arrangeDataAccordingToPedigree();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public Callable train(final Iterator<TrainingElement> hmm)
/*      */       throws Exception
/*      */     {
/*  284 */       new Callable() {
/*      */         public Object call() {
/*      */           try {
/*  287 */             boolean swtch = (Constants.sampleWithPedigree()) && (!Constants.trainWithPedigree());
/*  288 */             for (int j = 0; hmm.hasNext(); j++) {
/*  289 */               Fastphase.TrainingElement train = (Fastphase.TrainingElement)hmm.next();
/*  290 */               Fastphase.this.train(train, swtch);
/*  291 */               if (Constants.writeHMM() >= 1) {
/*  292 */                 Fastphase.writeHMM(train.hmm, new File(Fastphase.this.hmmFile.getAbsolutePath() + Constants.column()), Fastphase.this.dataC);
/*      */               }
/*  294 */               if ((Constants.sample()) && (!Constants.keepBest())) {
/*  295 */                 Fastphase.logger.info("sampling from HMM");
/*  296 */                 Fastphase.this.monitor.sampleFromHMM(train.hmm, Constants.noSamples(), j, train.data1);
/*      */               }
/*  298 */               if ((Constants.sample()) && (Constants.keepBest()) && (
/*  299 */                 (Fastphase.this.bestSc == null) || (train.logProb > Fastphase.this.bestSc.doubleValue()))) {
/*  300 */                 Fastphase.this.best = train.hmm;
/*  301 */                 Fastphase.this.bestSc = Double.valueOf(train.logProb);
/*      */               }
/*      */             }
/*      */             
/*  305 */             if ((Constants.keepBest()) && (Constants.sample())) {
/*  306 */               Fastphase.logger.info("sampling from HMM");
/*  307 */               Fastphase.this.monitor.sampleFromHMM(Fastphase.this.best, Constants.noSamples(), 0, null);
/*      */             }
/*      */           } catch (Exception exc) {
/*  310 */             exc.printStackTrace();
/*      */           }
/*  312 */           return null;
/*      */         }
/*      */       };
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
/*  351 */     static int pseudoCount = 0;
/*      */     private List<String>[] data;
/*      */     final File hmmFile;
/*      */     File hmmOutputFile;
/*      */     File phasedFile;
/*      */     File initialHMMFile;
/*      */     final boolean[] modelLengths;
/*      */     final File dir;
/*      */     
/*      */     public class TrainingElement {
/*      */       public MarkovModel hapHMM;
/*      */       public CompoundMarkovModel swHMM;
/*      */       public CompoundMarkovModel swHMM2;
/*      */       public HaplotypeHMM countHMM;
/*      */       public List<CompoundMarkovModel> hmm;
/*      */       List<EmissionState>[] data1;
/*      */       double[][] weights;
/*      */       
/*      */       public void makeBWT() {
/*  370 */         String[] dc = Fastphase.this.dataC.getUnderlyingDataSets();
/*      */         
/*      */ 
/*  373 */         if (Constants.plot() > 0) DickFormat.cnvf.clearTabs();
/*  374 */         if (Constants.plot() > 2)
/*      */         {
/*  376 */           HMMPanel pan = DickFormat.cnvf.addHMMTab((FreeHaplotypeHMM)this.hapHMM, Fastphase.this.dataC.getLocations(), Fastphase.this.dataC.majorAllele, Fastphase.this.dataC.minorAllele, Fastphase.this.dataC.size(), Fastphase.this.dataC.pheno());
/*  377 */           addPropertyChangeListener(pan);
/*      */         }
/*  379 */         for (int i = 0; i < this.bwt.length; i++) {
/*  380 */           if ((Fastphase.this.modelLengths[i] != 0) && (this.data1[i] != null) && (this.data1[i].size() > 0)) {
/*  381 */             this.bwt[i] = new BaumWelchTrainer((MarkovModel)this.hmm.get(i), (EmissionState[])this.data1[i].toArray(new EmissionState[0]), this.weights[i]);
/*  382 */             if (Constants.plot() > 0) {
/*  383 */               List<Integer> loc = Fastphase.this.dataC.loc;
/*  384 */               for (int data_index = 0; data_index < dc.length; data_index++) {
/*  385 */                 String name = dc[data_index] + " " + (i + 1);
/*  386 */                 if (this.bwt[i].trainDists) {
/*  387 */                   IndividualPlot plot = new IndividualPlot(this.bwt[i], loc, data_index, name);
/*  388 */                   this.bwt[i].addPropertyChangeListener(plot);
/*  389 */                   addPropertyChangeListener(plot);
/*  390 */                   DickFormat.cnvf.addTab(name, plot);
/*      */                 }
/*      */                 
/*  393 */                 Set<Integer> s = this.bwt[i].getDataIndices();
/*  394 */                 if (s.contains(Integer.valueOf(data_index))) {
/*  395 */                   HaplotypePanel panel = new HaplotypePanel(this.bwt[i], loc, Fastphase.this.dataC.majorAllele, Fastphase.this.dataC.minorAllele, "haplotypes " + name, i + 1, data_index);
/*  396 */                   this.bwt[i].addPropertyChangeListener(panel);
/*  397 */                   addPropertyChangeListener(panel);
/*  398 */                   DickFormat.cnvf.addTab(panel.getName(), panel);
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  406 */             if (this.data1[i].size() == 0) throw new RuntimeException("!!!");
/*      */           }
/*      */         }
/*  409 */         if (Constants.plot() > 1) {
/*  410 */           DickFormat.cnvf.pack();
/*  411 */           DickFormat.cnvf.setVisible(true);
/*      */         }
/*      */       }
/*      */       
/*      */       TrainingElement(MarkovModel haphmm) {
/*  416 */         this.pcs = new PropertyChangeSupport(this);
/*  417 */         Class[] clazz = { ExponentialTransitionProbs.class };
/*  418 */         HaplotypeHMM hmm_i = null;
/*  419 */         if (Fastphase.this.modelSwitch) {
/*  420 */           this.swHMM = new PairMarkovModel(new MarkovModel[] {
/*  421 */             hmm_i }, 
/*  422 */             new int[1], PairEmissionState.class, Emiss.getEmissionStateSpace(0), true);
/*  423 */           this.swHMM2 = new PairMarkovModel(new MarkovModel[] {
/*  424 */             hmm_i }, 
/*  425 */             new int[2], PairEmissionState.class, Emiss.getEmissionStateSpace(1), true);
/*      */         }
/*  427 */         setHMM(haphmm, 0);
/*  428 */         this.bwt = new BaumWelchTrainer[Fastphase.this.modelLengths.length];
/*  429 */         makeBWT();
/*      */       }
/*      */       
/*      */       Iterator<double[]> pseudoIterator;
/*      */       Iterator<double[]> pseudoIterator1;
/*      */       int num;
/*      */       int trainingRounds;
/*      */       int maxIt;
/*      */       double prec;
/*      */       BaumWelchTrainer[] bwt;
/*  439 */       public boolean switchHMM() { try { Fastphase.logger.info("SWITCHING!!!!");
/*  440 */           if ((this.hapHMM instanceof FreeHaplotypeHMM)) {
/*  441 */             this.hapHMM = ((MarkovModel)this.hapHMM.clone(true));
/*      */ 
/*      */           }
/*  444 */           else if ((this.hapHMM instanceof PairMarkovModel)) {
/*  445 */             this.hapHMM.validate(this.hapHMM.noSnps.intValue());
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
/*  463 */             this.hapHMM = new FreeHaplotypeHMM(this.hapHMM);
/*      */           }
/*      */           
/*  466 */           this.hapHMM.validate(this.hapHMM.noSnps.intValue());
/*      */           
/*      */ 
/*  469 */           makeGenotypeHMM(1);
/*  470 */           makeBWT();
/*      */         }
/*      */         catch (Exception exc) {
/*  473 */           exc.printStackTrace();
/*      */         }
/*  475 */         return true;
/*      */       }
/*      */       
/*  478 */       private void setHMM(MarkovModel hmm, int phase) { this.hapHMM = hmm;
/*      */         
/*      */ 
/*      */ 
/*  482 */         if (((hmm instanceof FreeHaplotypeHMM)) && (((FreeHaplotypeHMM)hmm).modifyWithData())) {
/*      */           try {
/*  484 */             modify((FreeHaplotypeHMM)hmm, Fastphase.this.dataC, null);
/*      */           } catch (Exception exc) {
/*  486 */             exc.printStackTrace();
/*  487 */             System.exit(0);
/*      */           }
/*      */           
/*      */         }
/*  491 */         else if ((hmm instanceof CompoundMarkovModel)) {
/*  492 */           MarkovModel mm = ((CompoundMarkovModel)hmm).getMemberModels()[1];
/*  493 */           if (!mm.getName().startsWith("allele")) throw new RuntimeException("!! " + mm.getName());
/*  494 */           modify((FreeHaplotypeHMM)mm, Fastphase.this.dataC, Integer.valueOf(1));
/*      */         }
/*  496 */         makeGenotypeHMM(phase);
/*      */       }
/*      */       
/*      */       public void modify(FreeHaplotypeHMM hmm, DataC data, Integer index) {
/*  500 */         if (!hmm.modifyWithData()) return;
/*  501 */         Logger.global.info("modifying!!");
/*  502 */         EmissionState maf = data.calculateMaf1();
/*  503 */         EmissionStateSpace emStSp = maf.getEmissionStateSpace();
/*  504 */         if (index != null) {
/*  505 */           maf = ((PairEmissionState)maf).getMemberStates(true)[index.intValue()];
/*      */         }
/*      */         
/*  508 */         for (int kj = 0; kj < emStSp.copyNumber.size(); kj++) {
/*  509 */           double numF = 0.0D;
/*  510 */           List<EmissionState> emStates = new ArrayList();
/*  511 */           for (int j = 1; j < hmm.modelLength(); j++) {
/*  512 */             EmissionState st = (EmissionState)hmm.getState(j);
/*  513 */             Integer fixed = st.getFixedInteger(0);
/*  514 */             if (fixed == null) fixed = Integer.valueOf(st.getBestIndex(0));
/*  515 */             Integer cn = Integer.valueOf(emStSp.getCN(fixed.intValue()));
/*  516 */             if (cn == emStSp.copyNumber.get(kj)) {
/*  517 */               numF += 1.0D;
/*  518 */               emStates.add(st);
/*      */             }
/*      */           }
/*  521 */           int[] set = emStSp.getGenoForCopyNo(((Integer)emStSp.copyNumber.get(kj)).intValue());
/*  522 */           if ((set.length != 1) && (((Integer)emStSp.copyNumber.get(kj)).intValue() == 1))
/*      */           {
/*  524 */             double avg = 1.0D / set.length;
/*      */             
/*  526 */             for (int i = 0; i < hmm.noSnps.intValue(); i++) {
/*  527 */               Integer fixed = maf.getFixedInteger(i);
/*  528 */               if (fixed != null) {
/*  529 */                 for (int j = 1; j < hmm.modelLength(); j++) {
/*  530 */                   EmissionState st = (EmissionState)hmm.getState(j);
/*  531 */                   st.setFixedIndex(i, fixed.intValue());
/*      */                 }
/*      */               }
/*      */               else {
/*  535 */                 double[] probs = maf.getEmiss(i);
/*      */                 
/*  537 */                 int[] count = new int[probs.length];
/*  538 */                 int sum = 0;
/*  539 */                 SortedMap<Double, Integer> lessThan = new TreeMap();
/*  540 */                 double summ = 0.0D;
/*  541 */                 for (int kk = 0; kk < set.length; kk++) {
/*  542 */                   summ += probs[set[kk]];
/*      */                 }
/*  544 */                 if (summ != 0.0D) {
/*  545 */                   for (int kk = 0; kk < set.length; kk++) {
/*  546 */                     int k = set[kk];
/*  547 */                     double prob = probs[k] / summ;
/*  548 */                     double raw = prob * numF;
/*      */                     
/*  550 */                     int cnt = 
/*  551 */                       (int)Math.max(Constants.modifyWithData(), 
/*  552 */                       prob < avg ? Math.ceil(prob * numF) : Math.floor(prob * numF));
/*  553 */                     if (cnt < raw) {
/*  554 */                       lessThan.put(Double.valueOf(raw - cnt), Integer.valueOf(k));
/*      */                     }
/*  556 */                     count[k] = cnt;
/*  557 */                     sum += cnt;
/*      */                   }
/*  559 */                   while (sum < numF) {
/*      */                     try {
/*  561 */                       int k = ((Integer)lessThan.remove(lessThan.lastKey())).intValue();
/*  562 */                       count[k] += 1;
/*  563 */                       sum++;
/*      */                     } catch (Exception exc) {
/*  565 */                       System.err.println(index);
/*  566 */                       exc.printStackTrace();
/*  567 */                       System.exit(0);
/*      */                     }
/*      */                   }
/*  570 */                   while (sum > numF) {
/*  571 */                     int maxIndex = Constants.getMax(count);
/*  572 */                     count[maxIndex] -= 1;
/*  573 */                     sum--;
/*      */                   }
/*  575 */                   int j = 0;
/*  576 */                   for (int kk = 0; kk < set.length; kk++) {
/*  577 */                     int k = set[kk];
/*  578 */                     for (int kk1 = 0; kk1 < count[k]; kk1++) {
/*  579 */                       EmissionState st = (EmissionState)emStates.get(j);
/*  580 */                       st.setFixedIndex(i, k);
/*  581 */                       j++;
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  591 */       private CompoundMarkovModel makeBasicModel(int no_cop, MarkovModel toCopy) { int[] no_copies = new int[no_cop];
/*  592 */         MarkovModel[] m = { toCopy };
/*  593 */         for (int i = 0; i < no_copies.length; i++)
/*      */         {
/*  595 */           no_copies[i] = 0;
/*      */         }
/*  597 */         return new PairMarkovModel(m, no_copies, PairEmissionState.class, Emiss.getEmissionStateSpace(no_cop - 1), true);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       private MarkovModel makeHMM(int no_cop, MarkovModel hapHMM)
/*      */       {
/*  605 */         CompoundMarkovModel result = null;
/*  606 */         if ((no_cop == 1) || (no_cop == 2) || (no_cop == 3) || (no_cop == 4)) {
/*  607 */           result = makeBasicModel(no_cop, hapHMM);
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
/*      */         }
/*      */         else
/*      */         {
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
/*  633 */           throw new RuntimeException("!! " + no_cop);
/*      */         }
/*      */         
/*  636 */         if ((Constants.fast()) && (result.noCopies() > 1) && (hapHMM.modelLength() > 2) && (!result.allOneLength())) { result = new CollapsedHMM(result);
/*      */         }
/*  638 */         if (Constants.cache()) return new CachedHMM(result);
/*  639 */         return result;
/*      */       }
/*      */       
/*      */       private void makeGenotypeHMM(int phase) {
/*  643 */         this.hmm = new ArrayList()
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           public int size() {
/*  650 */             return Fastphase.this.modelLengths.length; }
/*      */           
/*      */           public CompoundMarkovModel get(int i) {
/*  653 */             if (i >= Fastphase.this.modelLengths.length) return null;
/*  654 */             if (Fastphase.this.modelLengths[i] == 0) return null;
/*  655 */             if (super.get(i) == null) {
/*  656 */               set(i, (CompoundMarkovModel)Fastphase.TrainingElement.this.makeHMM(i + 1, Fastphase.TrainingElement.this.hapHMM));
/*      */             }
/*  658 */             return (CompoundMarkovModel)super.get(i);
/*      */           }
/*  660 */         };
/*  661 */         this.data1 = new List[Fastphase.this.modelLengths.length];
/*  662 */         this.weights = new double[Fastphase.this.modelLengths.length][];
/*  663 */         for (int i = 0; i < this.hmm.size(); i++) {
/*  664 */           if (Fastphase.this.modelLengths[i] != 0)
/*      */           {
/*  666 */             if (Fastphase.this.data[i] != null) {
/*  667 */               this.data1[i] = new ArrayList();
/*  668 */               this.weights[i] = new double[Fastphase.this.data[i].size()];
/*  669 */               for (int ik = 0; ik < Fastphase.this.data[i].size(); ik++) {
/*  670 */                 String key = (String)Fastphase.this.data[i].get(ik);
/*  671 */                 this.data1[i].add(Fastphase.this.dataC.dataL(key));
/*  672 */                 this.weights[i][ik] = Fastphase.this.dataC.weight(key);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */       public void initialise(double[] pseudo, double[] pseudo1, int round)
/*      */       {
/*  682 */         if ((this.hapHMM instanceof PairMarkovModel)) {
/*  683 */           boolean max = true;
/*      */           
/*  685 */           MarkovModel[] mm = ((PairMarkovModel)this.hapHMM).getMemberModels();
/*  686 */           mm[0].setPseudoCountWeights(pseudo, pseudo1);
/*  687 */           mm[1].setPseudoCountWeights(pseudo1, pseudo1);
/*      */         }
/*      */         else {
/*  690 */           this.hapHMM.setPseudoCountWeights(pseudo, pseudo1);
/*      */         }
/*  692 */         this.hapHMM.initialiseCounts();
/*  693 */         for (int ik = 0; ik < this.hmm.size(); ik++) {
/*  694 */           if (this.hmm.get(ik) != null) {
/*  695 */             ((CompoundMarkovModel)this.hmm.get(ik)).initialiseCounts();
/*      */           }
/*      */         }
/*  698 */         if (this.swHMM != null) {
/*  699 */           double[] ps = { 1.0E10D, 0.001D, Constants.u_exp(), 1.0E10D };
/*  700 */           this.swHMM.setPseudoCountWeights(ps, ps);
/*  701 */           this.swHMM.initialiseCounts();
/*      */         }
/*  703 */         (Fastphase.this.dataC instanceof LikelihoodDataCollection);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  708 */       public double logProb = Double.NEGATIVE_INFINITY;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       final PropertyChangeSupport pcs;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public boolean train(int i)
/*      */         throws Exception
/*      */       {
/*  726 */         firePropertyChange("pre_exp", null, null);
/*  727 */         double[] pseudo = (double[])this.pseudoIterator.next();
/*  728 */         double[] pseudo1 = (double[])this.pseudoIterator1.next();
/*  729 */         double lp = 0.0D;
/*  730 */         initialise(pseudo, pseudo1, i);
/*  731 */         List tasks = new ArrayList();
/*  732 */         boolean isLast = i > this.maxIt;
/*  733 */         if (i < Constants.numIteForHap) {
/*  734 */           for (int ik = 0; ik >= 0; ik--) {
/*  735 */             if (this.bwt[ik] != null) {
/*  736 */               if ((this.bwt[ik].hmm instanceof CachedHMM)) ((CachedHMM)this.bwt[ik].hmm).refresh();
/*  737 */               lp += this.bwt[ik].expectationStep(pseudo, isLast);
/*      */               
/*  739 */               if (pseudo[3] < 1000.0D) {
/*  740 */                 this.bwt[ik].maximisationStep(i, pseudo[3], tasks);
/*      */               }
/*      */               
/*      */             }
/*      */             
/*      */           }
/*      */         } else {
/*  747 */           for (int ik = this.bwt.length - 1; ik >= 0; ik--) {
/*  748 */             if (this.bwt[ik] != null) {
/*  749 */               if ((this.bwt[ik].hmm instanceof CachedHMM)) ((CachedHMM)this.bwt[ik].hmm).refresh();
/*  750 */               lp += this.bwt[ik].expectationStep(pseudo, isLast);
/*      */               
/*  752 */               if (pseudo[3] < 1000.0D) {
/*  753 */                 this.bwt[ik].maximisationStep(i, pseudo[3], tasks);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  759 */         isLast = (isLast) || ((this.num >= 1) && (Math.abs(lp - this.logProb) < this.prec));
/*      */         try
/*      */         {
/*  762 */           BaumWelchTrainer.involeTasks(tasks, true);
/*  763 */           firePropertyChange("dist_maximisation", null, null);
/*      */         } catch (Exception exc) {
/*  765 */           exc.printStackTrace();
/*      */         }
/*  767 */         tasks.clear();
/*  768 */         BaumWelchTrainer.maximisationStep(this.hapHMM, i, tasks);
/*      */         
/*      */ 
/*  771 */         Logger.global.info("START ########################################");
/*  772 */         if ((this.swHMM != null) && (i > Constants.indexToTrainSWHMM())) {
/*  773 */           BaumWelchTrainer.maximisationStep(this.swHMM, i, tasks);
/*      */         }
/*      */         try {
/*  776 */           BaumWelchTrainer.involeTasks(tasks, false);
/*  777 */           firePropertyChange("hmm_maximisation", null, null);
/*      */         }
/*      */         catch (Exception exc) {
/*  780 */           exc.printStackTrace();
/*      */         }
/*      */         
/*  783 */         if (Constants.writeHMM() > 1)
/*  784 */           Fastphase.writeHMM(this.hmm, new File(Fastphase.this.hmmFile.getAbsolutePath() + "." + Fastphase.this.overallIndex + Constants.column()), Fastphase.this.dataC);
/*  785 */         Fastphase.this.overallIndex += 1;
/*  786 */         for (int ik = 0; ik < this.bwt.length; ik++) {
/*  787 */           if (this.bwt[ik] != null) {
/*  788 */             ((CompoundMarkovModel)this.hmm.get(ik)).refresh();
/*      */           }
/*      */         }
/*  791 */         long t1 = System.currentTimeMillis();
/*      */         
/*  793 */         Fastphase.logger.info("log prob is " + lp + " at " + i + " with " + (this.hapHMM.modelLength() - 1) + " states " + (t1 - Fastphase.time) + " at " + pseudo[0] + " " + pseudo[1] + " " + pseudo[2] + " " + pseudo[3] + " " + pseudo[4]);
/*  794 */         Fastphase.time = t1;
/*  795 */         if (lp + 1.0D < this.logProb) {
/*  796 */           Fastphase.logger.info("warning: log prob should always be increasing ".toUpperCase() + lp + " should be > " + this.logProb);
/*      */         }
/*  798 */         if (((this.swHMM == null) || (this.swHMM.converged())) && (Math.abs(lp - this.logProb) < this.prec)) this.num += 1; else
/*  799 */           this.num = 0;
/*  800 */         this.logProb = lp;
/*  801 */         if ((this.num >= 2) || (i > this.maxIt))
/*      */         {
/*  803 */           return true;
/*      */         }
/*  805 */         this.trainingRounds += 1;
/*  806 */         return false;
/*      */       }
/*      */       
/*      */ 
/*      */       void addPropertyChangeListener(PropertyChangeListener arg0)
/*      */       {
/*  812 */         this.pcs.addPropertyChangeListener(arg0);
/*      */       }
/*      */       
/*      */       void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
/*  816 */         this.pcs.firePropertyChange(propertyName, oldValue, newValue);
/*      */       }
/*      */       
/*      */       public void initialise(int maxIt, double prec) throws Exception
/*      */       {
/*  821 */         this.pseudoIterator = Constants.pseudoIterator(Fastphase.this.noIndiv, 0);
/*  822 */         this.maxIt = maxIt;
/*  823 */         this.prec = prec;
/*  824 */         this.pseudoIterator1 = Constants.pseudoIterator(Fastphase.this.noIndiv, 1);
/*  825 */         if (Constants.writeHMM() > 1)
/*  826 */           Fastphase.writeHMM(this.hmm, new File(Fastphase.this.hmmFile.getAbsolutePath() + "_-1_" + Fastphase.this.overallIndex + Constants.column()), Fastphase.this.dataC);
/*  827 */         Fastphase.logger.info("training for  " + maxIt + " iterations");
/*  828 */         if (maxIt == 0) return;
/*  829 */         this.logProb = Double.NEGATIVE_INFINITY;
/*  830 */         this.trainingRounds = 0;
/*  831 */         this.num = 0;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void updateData()
/*      */     {
/*  842 */       for (int i = 0; i < this.data.length; i++) {
/*  843 */         if (this.data[i] != null) {
/*  844 */           this.data[i].clear();
/*      */         }
/*      */       }
/*  847 */       for (Iterator<String> it = this.dataC.getKeys().iterator(); it.hasNext();) {
/*  848 */         String key = (String)it.next();
/*  849 */         EmissionState dat = this.dataC.dataL(key);
/*      */         
/*  851 */         this.data[(dat.noCopies() - 1)].add(key);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  861 */     boolean modelSwitch = false;
/*  862 */     int noIndiv = 0;
/*      */     final String sb_i;
/*      */     
/*      */     public Fastphase(DataCollection data, Sampler monitor, int numRep, File dir) throws Exception {
/*  866 */       this.modelLength = numRep;
/*  867 */       this.dir = dir;
/*  868 */       this.dataC = data;
/*  869 */       this.hmmFile = new File(dir, "results_hmm.txt");
/*      */       
/*  871 */       SortedSet<Integer> len = new TreeSet();
/*  872 */       for (Iterator<EmissionState> it = this.dataC.dataLvalues(); it.hasNext();) {
/*  873 */         EmissionState dat = (EmissionState)it.next();
/*  874 */         int noCopies = dat.getEmissionStateSpace().noCopies();
/*  875 */         len.add(Integer.valueOf(noCopies));
/*  876 */         this.noIndiv += noCopies;
/*      */       }
/*  878 */       this.modelLengths = new boolean[((Integer)len.last()).intValue()];
/*  879 */       this.data = new List[((Integer)len.last()).intValue()];
/*      */       
/*  881 */       for (int i = 0; i < this.modelLengths.length; i++) {
/*  882 */         this.modelLengths[i] = len.contains(Integer.valueOf(i + 1));
/*      */       }
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
/*  896 */       for (int i = 0; i < this.modelLengths.length; i++) {
/*  897 */         if (this.modelLengths[i] != 0) {
/*  898 */           this.data[i] = new ArrayList();
/*      */         }
/*      */       }
/*  901 */       updateData();
/*  902 */       StringBuffer sb_is = new StringBuffer();StringBuffer sb_ds = new StringBuffer();
/*  903 */       StringBuffer sb_is1 = new StringBuffer();
/*  904 */       for (int i = 0; i < data.length(); i++) {
/*  905 */         sb_is.append("%8i ");
/*  906 */         sb_ds.append("%8.2g ");
/*      */       }
/*  908 */       this.sb_i = sb_is.toString();
/*  909 */       this.sb_d = sb_ds.toString();
/*  910 */       this.hmmOutputFile = new File(dir, "hmm_output");
/*  911 */       this.noSites = data.length();
/*  912 */       this.phasedFile = new File(dir, "phased.txt");
/*  913 */       this.monitor = monitor;
/*  914 */       this.initialHMMFile = new File(dir, "results_initial.txt");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private static boolean noNull(Comparable[] c)
/*      */     {
/*  925 */       for (int i = 0; i < c.length; i++) {
/*  926 */         if (c[i] == null) return false;
/*      */       }
/*  928 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     final String sb_d;
/*      */     
/*      */ 
/*      */     final Sampler monitor;
/*      */     
/*      */ 
/*      */     public boolean equal(Comparable[] o1, Comparable[] o2)
/*      */     {
/*  941 */       return Arrays.asList(o1).equals(Arrays.asList(o2));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  946 */     static Calendar cal = new GregorianCalendar();
/*      */     
/*      */     public static void writeHMM(List phmms, File textHMMOutput, DataC data) throws Exception
/*      */     {
/*  950 */       MarkovModel phmm = null;
/*      */       
/*  952 */       for (int i = phmms.size() - 1; i >= 0; i--)
/*      */       {
/*  954 */         if (phmms.get(i) != null)
/*      */         {
/*  956 */           phmm = (CompoundMarkovModel)phmms.get(i);
/*  957 */           break;
/*      */         }
/*      */       }
/*  960 */       logger.info("WRITING HMM " + textHMMOutput.getAbsolutePath());
/*  961 */       PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(textHMMOutput)));
/*  962 */       pw.println(cal.getTime());
/*  963 */       if ((data instanceof IlluminaRDataCollection)) {
/*  964 */         ((IlluminaRDataCollection)data).printDist(pw);
/*      */       }
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
/*  985 */       StringBuffer sb = new StringBuffer();
/*  986 */       List<Integer> posi = new ArrayList();
/*  987 */       List<Integer> cols = null;
/*  988 */       for (int i = 0; i < data.loc.size(); i++) {
/*  989 */         posi.add(Integer.valueOf(i));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  999 */       Object[] num = subList(data.getLocations(), cols).toArray();
/* 1000 */       Object[] name = data.snpid != null ? subList(data.snpid, cols).toArray() : null;
/*      */       
/* 1002 */       EmissionState maf_i = data.maf;
/* 1003 */       if ((maf_i instanceof PairEmissionState)) maf_i = ((PairEmissionState)maf_i).getMemberStates(true)[1];
/* 1004 */       ((HaplotypeEmissionState)maf_i).print(pw, "MAF            ");
/*      */       
/* 1006 */       Object[] mafs = HaplotypeEmissionState.emissions(maf_i, cols);
/* 1007 */       Double[] maf = (Double[])mafs[0];
/* 1008 */       String[] maf_st = (String[])mafs[1];
/* 1009 */       Object[] posi_i = subList(posi, cols).toArray();
/*      */       
/* 1011 */       StringBuffer sb1 = new StringBuffer();
/* 1012 */       StringBuffer sb2 = new StringBuffer();
/* 1013 */       for (int i = 0; i < maf.length; i++) {
/* 1014 */         sb1.append("%8i ");
/* 1015 */         sb.append("%8s ");
/* 1016 */         sb2.append("%8.2g ");
/*      */       }
/*      */       
/* 1019 */       if ((name != null) && (name.length > 0)) pw.println("name     " + Format.sprintf(sb.toString(), name));
/* 1020 */       if (posi_i.length > 0) pw.println("posi     " + Format.sprintf(sb.toString(), posi_i));
/* 1021 */       if (num.length > 0) pw.println("position " + Format.sprintf(sb1.toString(), num));
/* 1022 */       if (maf.length > 0) pw.println("m_allele " + Format.sprintf(sb.toString(), maf_st));
/* 1023 */       if (maf.length > 0) { pw.println("m_allele " + Format.sprintf(sb2.toString(), maf));
/*      */       }
/* 1025 */       pw.println(phmm.info());
/*      */       
/* 1027 */       phmm.print(pw, cols, data.noAllelles());
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
/* 1038 */       pw.close();
/* 1039 */       logger.info("finishing wrinting hmm");
/*      */     }
/*      */     
/*      */ 
/*      */     public static List subList(List l, List<Integer> cols)
/*      */     {
/* 1045 */       if (cols == null) return l;
/* 1046 */       List res = new ArrayList();
/* 1047 */       for (int i = 0; i < cols.size(); i++) {
/* 1048 */         res.add(l.get(((Integer)cols.get(i)).intValue()));
/*      */       }
/* 1050 */       return res;
/*      */     }
/*      */     
/*      */     public void sample() {}
/*      */   }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/external/Fastphase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */