package preprocessing;

import java.io.*;
import java.util.*;

public class BigramCompositionality1 { /* class name */
	static List<String> sentence = new ArrayList<String>();

	static List<Double> unigramprob = new ArrayList<Double>();
	

	static List<String> unilist = new ArrayList<String>();
	List<String> bigramListOutput = new ArrayList<String>(); //bigram any combination
	List<String> bigramListOutput2 = new ArrayList<String>(); //bigram strict
	static List<Double> bigramprob = new ArrayList<Double>(); // any combination
	static List<Double> bigramprob2 = new ArrayList<Double>(); // strict
	static List<Integer> countunigram = new ArrayList<Integer>();
	// static
	

	public static void main(String args[]) {

		BigramCompositionality1 op = new BigramCompositionality1();
		/* new class */
		try {
			String workDir = "./train_dev_bitext/";
			String domain[] = { "tourism/", "health/" };
			String languagePair[] = { "Hindi_Bangla/", "Hindi_English/",
					"Hindi_Marathi/", "Hindi_Tamil/", "Hindi_Telegu/" };
			String ifile=workDir +languagePair[0]+domain[0]+"/train.hi.cln";
			BufferedReader r = null;
			FileInputStream i = new FileInputStream(ifile); /*
																	 * to take
																	 * the file
																	 * as input
																	 * stream
																	 */
			r = new BufferedReader(new InputStreamReader(i,"UTF-8"));

			String h = "";

			int p = 0;

			Set<String> set1 = new HashSet<String>();

			while ((h = r.readLine()) != null) { /*
												 * loop to count words & create
												 * a set with the words
												 */

				sentence.add(h);
				String[] s = h.split(" ");

				for (String split : s) {
					p++;
					set1.add(split);

				}
			}
			r.close();
			i.close();
			// System.out.println(sentence.size());
			// System.out.println(set1);
			// wlist.addAll(set1);
			// System.out.println("number of words:"+p);
			op.unigram(set1, p);/* function calling */

			System.out.println("********************************************");
			System.out.println("mean,variance and s.d. calculation:");
			System.out.println("********************************************");
			
			System.out.println("word\t\t\tcount\t\tprobability\t\t"
					+ "\tmean\t" + "\tvariance\t\t SD"
					+ "\tstandard deviation:");
			//op.bigramfuncAnyComb(set1);
			op.bigram(ifile+".mwe");

		} catch (Exception e) {
			System.out.println("error buddy!!");
			e.printStackTrace();
			return;

		}
	}

	public void unigram(Set<String> set1, int l) { /* unigram function */
		try {
			List<String> wlist = new ArrayList<String>();
			wlist.addAll(set1);
			int size = wlist.size();
			int y, j, a;
			for (y = 0; y < size; y++) { // loop to check the word
				int g = 0;
				String tmp = wlist.get(y);
				for (j = 0; j < sentence.size(); j++) { // loop to compare with
														// the sentence
					String[] f = sentence.get(j).split(" ");

					for (a = 0; a < f.length; a++)
						if (tmp.equals(f[a])) { // condition
							g++;
						}

				}
				double v = (double) g / l;
				countunigram.add(g);
				//System.out
				//		.println(wlist.get(y) + "\t|||\t" + g + "\t|||\t" + v);
				unigramprob.add(v);
				unilist.add(tmp);
			}
			/* printing the output */

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not open file");
		}
	}

	public double probX(String x) {
		int p = 0;
		for (int i = 0; i < unilist.size(); i++) {
			if (x.equals(unilist.get(i)))
				p = i;
			break;
		}
		double val = unigramprob.get(p);
		return val;
	}

	public static List<String> ngrams(int n, String str) { // ngrams function
		List<String> ngrams = new ArrayList<String>();
		String[] words = str.split(" ");
		for (int i = 0; i < words.length - n + 1; i++)
			ngrams.add(concat(words, i, i + n)); // calling of concat function
		return ngrams;
	}

	public static String concat(String[] words, int start, int end) { // concat
																		// function
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++)
			sb.append((i > start ? " " : "") + words[i]);
		return sb.toString();
	}

	public void bigram(String ofile) {
		
		// reset all list 
		List<String> bigramlist = new ArrayList<String>(); // bigram list w1
		// followed by w2
		bigramlist.clear();
		bigramListOutput.clear();
		bigramprob.clear();
		
		List<String> bigramlistUniq = new ArrayList<String>(); // bigram list w1
																// followed by
																// w2
		List<Integer> distance = new ArrayList<Integer>();
		for (int j = 0; j < sentence.size(); j++) { //
			String str = sentence.get(j);
			List<String> tmp = new ArrayList<String>();
			tmp = this.ngrams(2, str);
			bigramlist.addAll(tmp);

		}
		for (int i = 0; i < bigramlist.size(); i++) {
			distance.add(1);// because it is pair of words occurred as side by
							// side
		}
		HashSet<String> set = new HashSet<String>(bigramlist);
		bigramlistUniq.addAll(set);
		int size = bigramlistUniq.size();
		try{
		File fout = new File(ofile);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));
		for (int i = 0; i < size; i++) {
			String bigram = bigramlistUniq.get(i);
			String bigramProbVal = this
					.bigramProbfunc(bigram, size, bigramlist,1);// flag==1 means strict bigram
			String statistics = this.statisticCalculation(bigram, bigramlist,
					distance);
			bw.write(bigram + "\t" + bigramProbVal
					+ "\t" 
					+ this.equationfunc1(bigram,1)+ "\t"
							+ this.PMI(bigram,1)+"\n");
			bw.flush();

		}
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}

	public String bigramProbfunc(String bigram, int size,
			List<String> combination, int flag) { /* bigram function */
		String op = "";

		List<Integer> countbigram = new ArrayList<Integer>();
		try {

			int y, j, a, u, total = 0, g = 0;
			// loop to check the bigram word

			String tmp1 = bigram;

			for (j = 0; j < combination.size(); j++) { // loop to
														// compare with
														// the sentence
				String f = combination.get(j);

				if (tmp1.equals(f)) { // condition
					g++;
				}

			}

			countbigram.add(g);
			/*
			 * if (g != 0) {
			 * 
			 * //total = total + g; countbigram.add(g); bigramlist.add(list); }
			 */

			total = size;

			double val = (double) countbigram.get(0) / total;
			op = (countbigram.get(0) + "\t|||\t" + val);
			if(flag==0)bigramprob.add(val);
			else bigramprob2.add(val);
			countbigram.add(g);
			if(flag==0)bigramListOutput.add(bigram);
			else bigramListOutput2.add(bigram);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not open file");
		}
		return op;
	}

	public double probXY(String x,int flag) { // P(X,Y) = to compute P(i,j), which is the
										// probability that at any position in
										// the text you will find the word i
										// followed immediately by the word j
		if(flag==1){
			bigramListOutput.clear();
			bigramListOutput.addAll(bigramListOutput2);
			bigramprob.clear();
			bigramprob.addAll(bigramprob2);
		}
		int p = 0;
		for (int i = 0; i < bigramListOutput.size(); i++) {
			if (x.equals(bigramListOutput.get(i)))
				p = i;
			break;
		}
		double val = bigramprob.get(p);
		return val;
	}

	public double probXGivenY(String x) { // P(X|Y) = to compute P(i|j), which
											// is the
		// probability that at any position in
		// the text you will find the word i
		// followed by the word j
		int p = 0;
		for (int i = 0; i < bigramListOutput2.size(); i++) {
			if (x.equals(bigramListOutput2.get(i)))
				p = i;
			break;
		}
		double val = bigramprob2.get(p);
		return val;
	}

	public void bigramfuncAnyComb(Set<String> set1) { /* bigram function */
		try {
			// wlist.addAll(set1);
			//
			List<String> bigramAnyCombination = new ArrayList<String>();
			List<String> check = new ArrayList<String>();
			List<Integer> distance = new ArrayList<Integer>();
			int dist = 0;

			for (int j = 0; j < sentence.size(); j++) {// to read every sentence
				// System.out.println(sentence.size());
				// System.out.println("for the sentence:\t"+sentence.get(j));
				String[] sen = sentence.get(j).split(" "); // to split the
															// sentence
				for (String sent : sen) {
					check.add(sent); // adding all the tokens into a list
				}
				// to check the distance of two words in a line
				for (int t = 0; t < check.size(); t++) { // loop for the first
															// word
					for (int d = t + 1; d < check.size(); d++) {// to check the
																// second word

						dist = d - t;
						if (dist < 0)
							dist = -dist;

						// System.out.println("words:\t"+check.get(t)+" "+check.get(d)+"\tdistance:"+dist);//
						// to print the distance between two words
						bigramAnyCombination.add(check.get(t) + " "
								+ check.get(d));// adding
						// the
						// combinations
						// into
						// a
						// list
						distance.add(dist);// adding the corresponding distances
											// into a list
					}
				}
				check.clear();// reset the check list
			}
			/*
			 * System.out.println(combination); System.out.println(distlist);
			 */
			Set<String> set_combo = new HashSet<String>();
			List<String> combo_list = new ArrayList<String>();

			set_combo.addAll(bigramAnyCombination);
			combo_list.addAll(set_combo);
			int size = combo_list.size();
			for (int i = 0; i < size; i++) {
				String bigram = combo_list.get(i);
				String bigramProbVal = this.bigramProbfunc(bigram, size,
						bigramAnyCombination,0); // flag==0 means any combination
				String statistics = this.statisticCalculation(bigram,
						bigramAnyCombination, distance);
				System.out.println(bigram + "\t|||\t\t" + bigramProbVal
						+ "\t\t|||\t" + statistics + "\t\t|||\t"
						+ this.equationfunc2(bigram,0));

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not open file");
		}
	}

	public String statisticCalculation(String bigram, List<String> combination,
			List<Integer> distance) { // standard deviation
		// calculation function
		String op = "";
		List<Integer> used_list = new ArrayList<Integer>();
		{ // to check the word of
			// combo_list
			int sum = 0;
			double sum2 = 0;
			for (int d = 0; d < combination.size(); d++) { // to match it with
															// the combinations
				if (combination.get(d).equals(bigram)) { // condition
					used_list.add(distance.get(d)); // list of distances for a
													// particular word in the
													// whole document
					sum = sum + distance.get(d); // sum part to calculate the
													// mean
				}
			}

			// System.out.println();

			double mean = (double) sum / sentence.size(); // calculation of the
															// mean
			/*
			 * System.out.println(used_list); System.out.println();
			 */
			for (int l = 0; l < used_list.size(); l++) { // to read every
															// distance of a
															// word in the whole
															// document
				sum2 = sum2 + (double) Math.pow((used_list.get(l) - mean), 2); // sum
																				// part
																				// of
																				// the
																				// variance
																				// calculation

			}
			double variance = sum2 / sentence.size();
			// System.out.println();// variance calculation
			double sd = Math.pow(variance, 0.5); // standard deviation
													// calculation
			op = mean + "\t|||\t" + variance + "\t|||\t" + sd;
			used_list.clear(); // to reset the used list
		}
		return op;
	}

	public double equationfunc1(String bigram, int flag) { // equation 1:
													// P(x,y)*(P(x)*p(y)/sum(P(x)*P(y)))
		String uni[] = bigram.split(" ");
		double sum;

		sum = 0.0;
		double u1 = probX(uni[0]), u2 = probX(uni[1]);
		sum = u1 + u2; // calculation of the

		double val = probXY(bigram,flag) * u1 * u2 / (u1 + u2);
		return val;
	}

	public double equationfunc2(String bigram, int flag) { // equation 2:
													// P(x,Y)/(P(x)*P(y))
		String uniWord[] = bigram.split(" ");

		double val = this.probXY(bigram,flag) / probX(uniWord[0])
				* probX(uniWord[1]);

		return val;
	}

	public double PMI(String bigram, int flag) { // equation 3:
										// log2[P(x,y)/P(x)*P(y)]

		double val = Math.log(this.equationfunc2(bigram,flag)) / Math.log(2);// main
																		// calculation
																		// part

		return val;

	}

	public double unigramEntropy(String unigram) { // equation 4:P(x)*log2[P(x)]

		double val = probX(unigram) * (Math.log(probX(unigram)) / Math.log(2));// calculation
																				// part
		return val;

	}

	public double DocumentUnigramEntropy() { // equation
												// 5:H(x)=sum[P(x)*log2[P(x)]]
		int q;
		double value = 0;

		for (q = 0; q < unilist.size(); q++) { // loop to check the unigram
												// words

			value = value + unigramEntropy(unilist.get(q)); // calculation
															// part

		}

		return (-value);

	}

	public double documentUnigramPerplexity() { // equation 6: 2^[-H(x)]
		int q;
		double value = 0;

		value = Math.pow(2, DocumentUnigramEntropy());
		// System.out.println("the required value:" + powval); // calculation of
		// the equation
		return value;
	}

	public void bigramCount(){//c(w1w2)/[c(w1)+c(w2))
		
	}
	/*
	 * public void equationfunc7(List<Integer> c1, List<Integer> c2,
	 * List<Integer> c3) { // equation 7: c(w1w2w3)/[c(w1)+c(w2)+c(w1w2)] for
	 * (int q = 0; q < trigramlist.size(); q++) { // loop for the trigram //
	 * words String[] s = trigramlist.get(q).split(" "); // to split the trigram
	 * // word
	 * 
	 * double value1 = (double) counttrigram.get(q) /
	 * (countbigram.get(bigramlist.indexOf(s[0] + " " + s[1])) +
	 * countunigram.get(wlist.indexOf(s[0])) + countunigram
	 * .get(wlist.indexOf(s[0])));// calculation part
	 * System.out.println("for the words:" + trigramlist.get(q) + "\t\t value:"
	 * + value1); // print output } }
	 */

}