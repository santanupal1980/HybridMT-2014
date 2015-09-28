package preprocessing;

import java.io.*;
import java.util.*;

public class BigramCompositionality { /* class name */
	static List<String> sentence = new ArrayList<String>();
	List<String> list2 = new ArrayList<String>();
	static List<Double> unigramprob = new ArrayList<Double>();
	static List<Double> bigramprob = new ArrayList<Double>();
	List<Double> trigramprob = new ArrayList<Double>();
	static List<String> unilist = new ArrayList<String>();
	List<String> bigramlistAnyComb = new ArrayList<String>();
	List<String> bigramlist = new ArrayList<String>(); // bigram list w1 followed by w2
	List<String> trigramlist = new ArrayList<String>();
	static List<Integer> countunigram = new ArrayList<Integer>();
	static List<Integer> countbigram = new ArrayList<Integer>();
	static List<Integer> counttrigram = new ArrayList<Integer>();
	List<String> combination = new ArrayList<String>();
	List<Integer> distlist = new ArrayList<Integer>();

	public static void main(String args[]) {

		BigramCompositionality op = new BigramCompositionality();
		/* new class */
		try {

			BufferedReader r = null;
			FileInputStream i = new FileInputStream("./test.txt"); /*
																	 * to take
																	 * the file
																	 * as input
																	 * stream
																	 */
			r = new BufferedReader(new InputStreamReader(i));

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
			System.out.println("********************************************");
			System.out.println("the unigram operation result:");
			System.out.println("********************************************");
			System.out.println("word\t|||\toccurrence\t|||\tprobability");
			op.unigram(set1, p);/* function calling */

			System.out.println("********************************************");
			System.out.println("the triigram operation result:");
			System.out.println("********************************************");
			System.out.println("word\t|||\toccurrence\t|||\tprobability");
			System.out.println("********************************************");
			op.trigramfunc(set1, p); /* function calling */

			System.out.println("********************************************");
			System.out.println("the bigram operation result:");
			System.out.println("********************************************");
			System.out.println("word\t|||\toccurrence\t|||\tprobability");
			// op.bigramfunc(set1, p);/* function calling */

			System.out.println("********************************************");
			System.out.println("mean,variance and s.d. calculation:");
			System.out.println("********************************************");
			System.out.println("word\t\t\tcount\t\tprobability\t\t"
					+ "\tmean\t" + "\tvariance\t\t SD"
					+ "\tstandard deviation:");
			op.bigramfuncAnyComb(set1);

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
				System.out
						.println(wlist.get(y) + "\t|||\t" + g + "\t|||\t" + v);
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

	public static List<String> ngrams(int n, String str) {   //ngrams function
        List<String> ngrams = new ArrayList<String>();
        String[] words = str.split(" ");
        for (int i = 0; i < words.length - n + 1; i++)
            ngrams.add(concat(words, i, i+n));     // calling of concat function
        return ngrams;
    }

    public static String concat(String[] words, int start, int end) {   //concat function
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append((i > start ? " " : "") + words[i]);
        return sb.toString();
    }
    
    public void bigram(){
    	for (int j = 0; j < sentence.size(); j++) {
    		String str=sentence.get(j);
    		List<String> tmp = new ArrayList<String>();
    		tmp=this.ngrams(2, str);
    		bigramlist.addAll(tmp);
    	}
    }
    
    
    
 	public String bigramProbfunc(String bigram, int size) { /* bigram function */
		String op = "";
		try {

			int y, j, a, u, total = 0, g = 0;
			{ // loop to check the bigram word

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
				 * //total = total + g; countbigram.add(g);
				 * bigramlist.add(list); }
				 */

			}

			total = size;

			double val = (double) countbigram.get(0) / total;
			op = (countbigram.get(0) + "\t|||\t" + val);
			bigramprob.add(val);
			countbigram.add(g);
			bigramlistAnyComb.add(bigram);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not open file");
		}
		return op;
	}

	public double probXY(String x) { // P(X,Y) = to compute P(i,j), which is the
										// probability that at any position in
										// the text you will find the word i
										// followed immediately by the word j
		int p = 0;
		for (int i = 0; i < bigramlistAnyComb.size(); i++) {
			if (x.equals(bigramlistAnyComb.get(i)))
				p = i;
			break;
		}
		double val = bigramprob.get(p);
		return val;
	}

	public void bigramfuncAnyComb(Set<String> set1) { /* bigram function */
		try {
			// wlist.addAll(set1);
			List<String> check = new ArrayList<String>();
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
						combination.add(check.get(t) + " " + check.get(d));// adding
																			// the
																			// combinations
																			// into
																			// a
																			// list
						distlist.add(dist);// adding the corresponding distances
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

			set_combo.addAll(combination);
			combo_list.addAll(set_combo);
			int size = combo_list.size();
			for (int i = 0; i < size; i++) {
				String bigram = combo_list.get(i);
				String bigramProbVal = this.bigramProbfunc(bigram, size);
				String statistics = this.statisticCalculation(bigram);
				System.out.println(bigram + "\t|||\t\t" + bigramProbVal
						+ "\t\t|||\t" + statistics + "\t\t|||\t"
						+ this.equationfunc2(bigram));

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not open file");
		}
	}

	public void trigramfunc(Set<String> set1, int l) { /* trigram function */
		/* trigram function */
		try {

			List<String> wlist = new ArrayList<String>();
			wlist.addAll(set1);
			int size = wlist.size();
			int y, j, a, u, total = 0;
			for (y = 0; y < size; y++) { /* loop to check the first word */

				String tmp = wlist.get(y);
				for (u = 0; u <= size - 1; u++) { // loop to check the second
													// word

					if (u != y) {
						String tmp1 = wlist.get(u);
						for (int z = 0; z <= size - 1; z++) { // loop to check
																// the third
																// word
							int g = 0;
							if (z != y && z != u) {
								String tmp2 = wlist.get(z);
								for (j = 0; j < sentence.size(); j++) { // loop
																		// to
																		// compare
																		// with
																		// the
																		// sentence
									String[] f = sentence.get(j).split(" ");

									for (a = 0; a < f.length - 2; a++) {
										if ((tmp.equals(f[a])
												&& (tmp1.equals(f[a + 1])) && (tmp2
													.equals(f[a + 2])))) { // condition
											g++; // count increment
										}
									}

								}

								if (g != 0) {
									total = total + g;
									counttrigram.add(g);
									trigramlist.add(tmp + " " + tmp1 + " "
											+ tmp2);

								}
							}
						}
					}
				}

			}
			System.out.println(total);
			for (int id = 0; id < trigramlist.size(); id++) {
				double val = (double) counttrigram.get(id) / total;
				System.out.println(trigramlist.get(id) + "\t|||\t"
						+ counttrigram.get(id) + "\t|||\t" + val);
				trigramprob.add(val);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not open file");
		}

	}

	public String statisticCalculation(String bigram) { // standard deviation
														// calcuation function
		String op = "";
		List<Integer> used_list = new ArrayList<Integer>();
		{ // to check the word of
			// combo_list
			int sum = 0;
			double sum2 = 0;
			for (int d = 0; d < combination.size(); d++) { // to match it with
															// the combinations
				if (combination.get(d).equals(bigram)) { // condition
					used_list.add(distlist.get(d)); // list of distances for a
													// particular word in the
													// whole document
					sum = sum + distlist.get(d); // sum part to calculate the
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

	public double equationfunc1(String bigram) { // equation 1:
													// P(x,y)*(P(x)*p(y)/sum(P(x)*P(y)))
		String uni[] = bigram.split(" ");
		double sum;

		sum = 0.0;
		double u1 = probX(uni[0]), u2 = probX(uni[1]);
		sum = u1 + u2; // calculation of the

		double val = probXY(bigram) * u1 * u2 / (u1 + u2);
		return val;
	}

	public double equationfunc2(String bigram) { // equation 2:
													// P(x,Y)/(P(x)*P(y))
		String uniWord[] = bigram.split(" ");

		double val = this.probXY(bigram) / probX(uniWord[0])
				* probX(uniWord[1]);

		return val;
	}

	public double PMI(String bigram) { // equation 3:
										// log2[P(x,y)/P(x)*P(y)]

		double val = Math.log(this.equationfunc2(bigram)) / Math.log(2);// main
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