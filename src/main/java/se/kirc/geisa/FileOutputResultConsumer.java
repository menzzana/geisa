/*
 * FileOutputResultConsumer.java
 * Copyright (C) 2011-2012  KIRC
 * 
 * This file is part of GEISA. GEISA is an upgrade of Jeira by Daniel Uvehag
 * 
 * GEISA is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software 
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 * 
 * GEISA is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this software; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.kirc.geisa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Consumer implementation which outputs the results to individual files, one
 * for each permutation.
 * 
 * @author Daniel Uvehag <daniel.uvehag@ki.se>
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class FileOutputResultConsumer extends AbstractResultConsumer {
	/**
	 * The output directory where the files will be created.
	 */
	
	final static double MAX_P_VALUE=1;
	final static ResultColumn[] INDEX_RESULTCOLUMN={
		ResultColumn.ORII,ResultColumn.ORIIL,ResultColumn.ORIIH,
		ResultColumn.ORIO,ResultColumn.ORIOL,ResultColumn.ORIOH,
		ResultColumn.OROI,ResultColumn.OROIL,ResultColumn.OROIH,
		ResultColumn.APP,ResultColumn.STABLELRA,ResultColumn.MULT,
		ResultColumn.STABLELRM
	};
	
	final static String[] PermutationResultColumn={
		"Interaction_marker","Test_marker",
		"ORa_double_exposure_permutation_pvalue","ORa_double_exposure_lower_limit_permutation_pvalue", 
		"ORa_double_exposure_higher_limit_permutation_pvalue","ORa_test_marker_permutation_pvalue",
		"ORa_test_marker_lower_limit_permutation_pvalue","ORa_test_marker_higher_limit_permutation_pvalue", 
		"ORa_risk_factor_permutation_pvalue","ORa_risk_factor_lower_limit_permutation_pvalue", 
		"ORa_risk_factor_higher_limit_permutation_pvalue","APP_permutation_pvalue", 
		"NO_succeded_additive_logistic_regression","Multiplicative_interaction_term_permutation_pvalue", 
		"NO_succeded_multiplicative_logistic_regression"
	};
	
	final static String[] TotalPermutationResultColumn={
		"Significance Limit","APP_permutation_pvalue", 
		"Significance Limit","Multiplicative_interaction_term_permutation_pvalue"
	};	
	
	protected File directory;
	private int permutations;
	private List<Double> APpLimits;
	private List<Double> MULTLimits;
	private int noMarkers;
	private boolean permutationOutput;
	private boolean totalPermutationOutput;
	private boolean negativeapp;

	/**
	 * Constructs a new result consumer which saves each permutation in its own
	 * file in the provided output directory.
	 * 
	 * @param done
	 *            An atomic boolean used to signal that no more entries will be
	 *            put into the queue.
	 * @param columns
	 *            A list of columns to use as the output header.
	 * @param directory
	 *            The output directory where the files should be stored.
	 * @param permutations
	 * 			  Number of permutations
	 * @param APpLimits
	 * 			  List of significance cutoff values for permuted APP
	 * @param MULTLimits
	 * 			  List of significance cutoff values for permuted MULT           
	 * @param queue
	 *            The queue containing all future results.
	 * @throws FileNotFoundException
	 */
	public FileOutputResultConsumer(AtomicBoolean signal,
			List<ResultColumn> columns,File directory,int permutations,boolean permutationOutput,
			boolean totalPermutationOutput,boolean negativeapp,List<Double> APpLimits,List<Double> MULTLimits,int noMarkers,
			BlockingQueue<Future<Iterable<Map<ResultColumn, String>>>> queue) {
		// Call the parent's constructor.
		super(signal, columns, queue);

		// Store a reference to the output directory.
		this.directory = directory;
		this.permutations=permutations;
		this.APpLimits=APpLimits;
		this.MULTLimits=MULTLimits;
		this.noMarkers=noMarkers;
		this.permutationOutput=permutationOutput;
		this.totalPermutationOutput=totalPermutationOutput;
		this.negativeapp=negativeapp;
	}
		
	private double [] getTotalPermutationArray(List<Double> permutedResults, List<Double>cutoffLimits) {
		double[] result=new double[cutoffLimits.size()+1];
		for (int i1=1; i1<permutedResults.size(); i1++) {
			for (int i2=0; i2<cutoffLimits.size(); i2++)
				if (permutedResults.get(i1)<=cutoffLimits.get(i2))
					result[i2]++;
		}
		for (int i1=0; i1<result.length; i1++)
			result[i1]=result[i1]/((double)permutedResults.size()-1);
		return result;
	}
	
	private void printResults(PrintStream out,Map<ResultColumn, String> result) {
		// Print all columns, separated by a tab delimiter. Lock the
		// output stream while doing this, nobody should interfere.
		synchronized (out) {
			for (ResultColumn resultColumn : ResultColumn.values()) {
				if (resultColumn==ResultColumn.PERM || resultColumn==ResultColumn.THRESHOLD)
					continue;
				if (resultColumn.ordinal()>1)
					out.print("\t");
				out.print(result.get(resultColumn));	
			}
			out.println();
		}
	}
	
	private void printPermutationResults(PrintStream outp,List<String>originalMarkers,
			PermutationData[][] originalResults,String oldInteractionMarker) {
		synchronized(outp) {
			for (int i1=0; i1<originalMarkers.size(); i1++) {
				outp.print(oldInteractionMarker+"\t"+originalMarkers.get(i1));
				for (int i2=0; i2<INDEX_RESULTCOLUMN.length; i2++) {
					outp.print("\t"+originalResults[i1][i2].PermutationPValue());
					originalResults[i1][i2].clear();
				}
				outp.println();
			}
		}
	}

	/*
	 * The runnable method for this class.
	 */
	public void run() {
		PrintStream out=null,outp=null;
		StringBuilder header;
		int permutation;
		String s1,oldInteractionMarker,interactionMarker="";

		try {
			// Record Max AP or TMULT per permutation. permutation=0 is original data
			List<Double> permutedAPP=new LinkedList<Double>();
			List<Double> permutedMult=new LinkedList<Double>();
			for (int i1=0; i1<=permutations; i1++) {
				permutedAPP.add(MAX_P_VALUE);
				permutedMult.add(MAX_P_VALUE);
			}			
			// Set the original results and permuted results
			PermutationData[][] originalResults=new PermutationData[noMarkers][INDEX_RESULTCOLUMN.length];
			for (int i1=0; i1<noMarkers; i1++)
				for (int i2=0; i2<INDEX_RESULTCOLUMN.length; i2++)
					originalResults[i1][i2]=new PermutationData();
			List<String> originalMarkers=new LinkedList<String>();
			// Open file for results
			s1=directory.getPath()+File.separator+"results.txt";
			out=new PrintStream(new FileOutputStream(s1,false));
			header=new StringBuilder();
			for (ResultColumn resultColumn : ResultColumn.values()) {
				if (resultColumn==ResultColumn.PERM || resultColumn==ResultColumn.THRESHOLD)
					continue;
				if (resultColumn.ordinal()>1)
					header.append("\t");
				header.append(resultColumn.getKey());
			}
			out.println(header);
			// Open file for permutation results
			if (permutations>0) {
				s1=directory.getPath()+File.separator+"marker_permutation_results.txt";
				outp=new PrintStream(new FileOutputStream(s1,false));
				for (int i1=0; i1<PermutationResultColumn.length; i1++)
					outp.print((i1>0?"\t":"")+PermutationResultColumn[i1]);
				outp.println();				
			}
			// Loop for as long as there's work to be done.
			while (!queue.isEmpty() || !done.get()) {
				Future<Iterable<Map<ResultColumn, String>>> future = queue.take();
				Iterable<Map<ResultColumn, String>> results = future.get();
				for (Map<ResultColumn, String> result : results) {
					oldInteractionMarker=interactionMarker;
					interactionMarker=result.get(ResultColumn.INTERACTION);
					if (!oldInteractionMarker.equals(interactionMarker) && originalMarkers.size()>0) {
						// Writing marker specific permuted results
						printPermutationResults(outp,originalMarkers,originalResults,oldInteractionMarker);
						originalMarkers.clear();					
					}
					permutation = Integer.valueOf(result.get(ResultColumn.PERM));
					boolean stableAdditive=result.get(ResultColumn.STABLELRA).equals(Messages.getString("status.iteration.converging.yes"));
					boolean stableMultiplicative=result.get(ResultColumn.STABLELRM).equals(Messages.getString("status.iteration.converging.yes"));
					boolean noCalcNegativeAPP=!negativeapp && Double.parseDouble(result.get(ResultColumn.AP))<0;
					String marker=result.get(ResultColumn.SNP);
					if (stableAdditive && !noCalcNegativeAPP) {
						double d1=Double.parseDouble(result.get(ResultColumn.APP));
						permutedAPP.set(permutation,Math.min(d1, permutedAPP.get(permutation)));
					}
					if (stableMultiplicative) {
						double d1=Double.parseDouble(result.get(ResultColumn.MULT));
						permutedMult.set(permutation,Math.min(d1, permutedMult.get(permutation)));
					}
					if (permutation==0) {
						printResults(out,result);
						if (permutations==0)
							continue;
						originalMarkers.add(marker);
						for (int i1=0; i1<INDEX_RESULTCOLUMN.length; i1++) {
							if (INDEX_RESULTCOLUMN[i1]==ResultColumn.STABLELRA ||
									INDEX_RESULTCOLUMN[i1]==ResultColumn.STABLELRM)
								originalResults[originalMarkers.size()-1][i1].setDefaultConvergenceThreshold(Double.parseDouble(result.get(ResultColumn.THRESHOLD)));
							else
								originalResults[originalMarkers.size()-1][i1].setValue(result.get(INDEX_RESULTCOLUMN[i1]));
						}
						continue;
					}	
					if (permutationOutput) {
						s1=directory.getPath()+File.separator+"results_permutation_"+permutation+".txt";
						File f1=new File(s1);
						boolean fileExists=f1.exists();
						PrintStream outp2=new PrintStream(new FileOutputStream(s1,true));
						if (!fileExists)
							outp2.println(header);
						printResults(outp2,result);
						outp2.close();
					}
					int index=originalMarkers.indexOf(marker);
					for (int i1=0; i1<INDEX_RESULTCOLUMN.length; i1++)
						originalResults[index][i1].setPositivePermutation(result.get(INDEX_RESULTCOLUMN[i1]),
								stableAdditive,stableMultiplicative,INDEX_RESULTCOLUMN[i1],noCalcNegativeAPP);
					
				}
			}
			out.close();
			if (permutations>0) {
				printPermutationResults(outp,originalMarkers,originalResults,interactionMarker);
				originalMarkers.clear();	
				outp.close();		
				// Writing lowest permutation results for each permutation
				if (totalPermutationOutput) {
					outp=new PrintStream(new FileOutputStream(directory.getPath()+File.separator+"total_permutations.txt",false));
					synchronized(outp) {
						outp.print("APP\tMULT\n");
						for (int i1=0; i1<=permutations; i1++)
							outp.println(permutedAPP.get(i1)+"\t"+permutedMult.get(i1));
					}
					outp.close();
				}
				// Calculating Total permuted results
				APpLimits.add(permutedAPP.get(0));
				MULTLimits.add(permutedMult.get(0));
				double[] totalPermutedAPP=getTotalPermutationArray(permutedAPP,APpLimits);
				double[] totalPermutedMULT=getTotalPermutationArray(permutedMult,MULTLimits);
				outp=new PrintStream(new FileOutputStream(directory.getPath()+File.separator+"total_permutation_results.txt",false));
				synchronized(outp) {	
					for (int i1=0; i1<TotalPermutationResultColumn.length; i1++)
						outp.print((i1>0?"\t":"")+TotalPermutationResultColumn[i1]);
					outp.println();
					for (int i1=0; i1<Math.max(APpLimits.size(),MULTLimits.size()); i1++) {
						if (i1<APpLimits.size())
							outp.print(APpLimits.get(i1)+"\t"+totalPermutedAPP[i1]+"\t");
						else
							outp.print("\t\t");
						if (i1<MULTLimits.size())
							outp.print(MULTLimits.get(i1)+"\t"+totalPermutedMULT[i1]+"\t");
						outp.println();
					}
				}
				outp.close();
				permutedAPP.clear();
				permutedMult.clear();
				APpLimits.clear();
				MULTLimits.clear();
			}
			s1 = String.format(Messages.getString("status.output.finished"),Calendar.getInstance());
			System.err.println(s1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
