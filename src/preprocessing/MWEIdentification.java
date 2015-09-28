package preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MWEIdentification {

	static List<String> sentence = new ArrayList<String>();
	List<String> list2 = new ArrayList<String>();
	static List<Double> unigramprob = new ArrayList<Double>();
	static List<Double> bigramprob = new ArrayList<Double>();
	List<Double> trigramprob = new ArrayList<Double>();
	static List<String> wlist = new ArrayList<String>();
	List<String> bigramlist = new ArrayList<String>();
	List<String> trigramlist = new ArrayList<String>();
	static List<Integer> countunigram = new ArrayList<Integer>();
	static List<Integer> countbigram = new ArrayList<Integer>();
	static List<Integer> counttrigram = new ArrayList<Integer>();
	List<Double> cooccurance = new ArrayList<Double>();
	List<Double> pmi = new ArrayList<Double>();
	double value, powval;
	int totalBigram=0;
	public MWEIdentification() {
		// TODO Auto-generated constructor stub
	}

	public void unigram(Set<String> set1, int l) { /* unigram function */
		try {

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
				//System.out.println("word:" + wlist.get(y) + "\t"
					//	+ "occurrence:" + g + "\t probability:" + v);
				unigramprob.add(v);
			}
			/* printing the output */

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not open file");
		}
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

    
    
    public void bigramfunc(Set<String> set1, int l) { /* bigram function */
		try {

			List<String> wlist = new ArrayList<String>();
			wlist.addAll(set1);
			int size = wlist.size();
			int y, j, a, u, total = 0;
			for (y = 0; y < size; y++) { // loop to check the first word

				String tmp = wlist.get(y);
				for (u = 0; u <= size - 1; u++) { // loop to check the second
													// word
					int g = 0;
					if (u != y) {
						String tmp1 = wlist.get(u);

						for (j = 0; j < sentence.size(); j++) { // loop to
																// compare with
																// the sentence
							String[] f = sentence.get(j).split(" ");

							for (a = 0; a < f.length - 1; a++) {
								if ((tmp.equals(f[a]) && (tmp1.equals(f[a + 1])))) { // condition
									g++;
								}
							}

						}

						if (g != 0) {

							total = total + g;
							countbigram.add(g);
							bigramlist.add(tmp + " " + tmp1);
						}
					}
				}

			}
			totalBigram=total;

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not open file");
		}
	}

	public void bigramProbability(){
		for (int id = 0; id < bigramlist.size(); id++) {
			double val = (double) countbigram.get(id) / totalBigram;
			
			bigramprob.add(val);
		}
	}
	
	public void cooccurance(List<Double> p1, List<Double> p2) { // equation
																				// 1:
		// P(x,y)*(P(x)*p(y)/sum(P(x)*P(y)))
		int q, c;
		double sum;
		int p1size = p1.size();

		sum = 0;

		for (q = 0; q < p1size; q++) { // loop to check unigram words

			for (c = 0; c < p1size; c++) { // loop to check the combination in
											// bigram
				if (c != q) {
					sum = sum + (p1.get(q) * p1.get(c)); // calculation of the
															// sum part

				}

			}

		}
		
		for (q = 0; q < p1size; q++) { // loop to check unigram words
			for (c = 0; c < p1size; c++) { // loop to check the combination in
											// bigram
				if (c != q) {
					if (bigramlist.contains(wlist.get(q) + " " + wlist.get(c))) {
						double val = p2.get(bigramlist.indexOf((wlist.get(q)
								+ " " + wlist.get(c))))
								* (p1.get(q) * p1.get(c)) / sum; // main
																	// calculation
																	// part
						//System.out.println(val);
						cooccurance.add(val);
					}

				}

			}
		}

	}

	public void PMI(List<Double> p1, List<Double> p2) { // equation
																		// 3:
																		// log2[P(x,y)/P(x)*P(y)]
		int q, c;
		
		for (q = 0; q < p1.size(); q++) { // loop to check unigram word
			for (c = 0; c < p1.size(); c++) { // loop for bigram combination
				if (c != q) {
					if (bigramlist.contains(wlist.get(q) + " " + wlist.get(c))) {
						double val = Math.log(p2.get(bigramlist.indexOf((wlist
								.get(q) + " " + wlist.get(c))))
								/ (p1.get(q) * p1.get(c)))
								/ Math.log(2);// main calculation part
						pmi.add(val);
					}

				}

			}
		}

	}

	

	public void scorefunc(List<Double> p1, List<Double> p2, String ofile) {// function of
																// score
		try{														// computation5
		File fout = new File(ofile);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		bw.write("words\tbigram prob\t co-occ \t PMI\n");
		//System.out.println(bigramprob.size()+"\t"+bigramlist.size());
		for (int q1 = 0; q1 < bigramlist.size(); q1++) {
			bw.write(bigramlist.get(q1) + "\t" + bigramprob.get(q1) + "\t" +cooccurance.get(q1)
					+ "\t"+ pmi.get(q1)+"\n");
			bw.flush();
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
	public static void main(String args[]) {

		MWEIdentification op = new MWEIdentification();
		/* new class */
		try {
			CorpusProcessing cor = new CorpusProcessing();
			String workDir = "./train_dev_bitext/";
			String domain[] = { "tourism/", "health/" };
			String languagePair[] = { "Hindi_Bangla/", "Hindi_English/",
					"Hindi_Marathi/", "Hindi_Tamil/", "Hindi_Telegu/" };
			String ifile=workDir +languagePair[0]+domain[0]+"/train.hi";
			//String ifile = "EN-HI/train.en";
			BufferedReader r = null;
			FileInputStream i = new FileInputStream(ifile); /*
																 * to take the
																 * file as input
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

			//System.out.println(set1);
			wlist.addAll(set1);
			//System.out.println("number of words:" + p);
			System.out.println("Unigram");
			long startTime   = System.currentTimeMillis();
			op.unigram(set1, p);/* function calling */
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			startTime = endTime;
			System.out.println(totalTime);
			System.out.println("bigram");
			op.bigramfunc(set1, p);/* function calling */
			totalTime = endTime - startTime;
			startTime = endTime;
			System.out.println(totalTime);
			System.out.println("bigram probability");
			op.bigramProbability();
			totalTime = endTime - startTime;
			startTime = endTime;
			System.out.println(totalTime);
			System.out.println("cooccurance");
			op.cooccurance(unigramprob, bigramprob);
			totalTime = endTime - startTime;
			startTime = endTime;
			System.out.println(totalTime);
			System.out.println("PMI");
			op.PMI(unigramprob, bigramprob);
			totalTime = endTime - startTime;
			startTime = endTime;
			System.out.println(totalTime);
			
			op.scorefunc(unigramprob, bigramprob, ifile+"mwe");// to show the scores
			
			
		} catch (Exception e) {
			System.out.println("error buddy!!");
			e.printStackTrace();
			return;

		}
	}

}
