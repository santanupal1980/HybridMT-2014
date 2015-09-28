//package preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * This is length based sentence clustering method
 */
public class SentenceClustering {
	List<String> fcorpus = new ArrayList<String>();
	List<String> ecorpus = new ArrayList<String>();
	ArrayList<String> fecorpus = new ArrayList<String>();
	
	public String formatStringCausal(String str) {
		String finalResult = "";
		try {
			str = str.replace(" \\ ", " ");
			str = str.replaceAll("  *", " ");
			str = str.replace("\\", "/");
			str = str.replace(" / ", " ");
			str = str.replaceAll("  *", " ");
			String str1[] = str.replace(" ?/./O", "").split(" ");
			for (int i = 0; i < str1.length; i++) {
				String tok[] = str1[i].replace("/", "@").split("@");

				finalResult += tok[0] + " ";

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(str);
		}
		return finalResult;

	}

	public String linecleaning(String line) {
		line = this.formatStringCausal(line);
		line = line.replace("-LRB-", "(");
		line = line.replace("-RRB-", ")");

		// cleaning

		String specialChar[] = { "???", ".", "'", "\"", ",", ";", "'", "(",
				")", "!", "@", "#", "$", "%", "&", "*", "-", "_", "???" };

		for (int i = 0; i < specialChar.length; i++) {
			line = line.replace(specialChar[i], " " + specialChar[i] + " ");

		}

		line = line.replaceAll("  *", " ");
		return line;
	}

	public boolean lengthBasedCleaning(int l1, int l2) {

		if (l1 == 0)
			return false;
		else if (l2 == 0)
			return false;
		else if (l1 > 100 || l2 > 100) {
			return false;
		} else if (l2 / l1 >= 2 || l1 / l2 >= 2) {
			return false;
		} else
			return true;
	}

	public void corpusCleaning() {
		for (int i = 0; i < fcorpus.size(); i++) {
			String line1 = fcorpus.get(i);
			String line2 = ecorpus.get(i);
			// cleaning
			line1 = this.linecleaning(line1);
			line2 = this.linecleaning(line2);
			// cleaning end

			// line1 = line1.toLowerCase();
			// line2 = line2.toLowerCase();

			line1 = line1.trim();
			line2 = line2.trim();
			if (line1.contains(" ") && line2.contains(" ")) {
				String st[] = line1.split(" ");
				String tt[] = line2.split(" ");
				int l1 = st.length;
				int l2 = tt.length;
				if (this.lengthBasedCleaning(l1, l2)) {
					String length = "";
					if (l1 < 10)
						length = "0" + l1;
					else
						length = "" + l1;// ???
					fecorpus.add((length + "\t" /*+ (i) + "\t" */+ line1 + "\t" + line2));
				}
			}
		}
		Collections.sort(fecorpus);
		HashSet<String> sh = new HashSet<String>(fecorpus); 
		fecorpus.clear();
		fecorpus = new ArrayList<String>(sh);
		fcorpus.clear();
		ecorpus.clear();
		Collections.sort(fecorpus);
		System.out.println(fecorpus.size() + "\t" + fcorpus.size() + "\t"
				+ ecorpus.size());
		for (int i = 0; i < fecorpus.size(); i++) {
			String corp = (fecorpus.get(i));
			String line[] = corp.split("\t");
			fcorpus.add(line[1]);
			ecorpus.add(line[2]);
		}

	}

	public void readCorpus(String source, String target, boolean clean) {
		int len = 0;
		try {

			FileInputStream sf = new FileInputStream(source);
			BufferedReader sbr = new BufferedReader(new InputStreamReader(sf,
					"UTF-8"));
			FileInputStream tf = new FileInputStream(target);
			BufferedReader tbr = new BufferedReader(new InputStreamReader(tf,
					"UTF-8"));

			String line1 = "";
			String line2 = "";
			int x = 0;
			while ((line1 = sbr.readLine()) != null
					&& (line2 = tbr.readLine()) != null) {

				fcorpus.add(line1);
				ecorpus.add(line2);
				
				
			}
			if (clean) {
				this.corpusCleaning();
				File fout = new File(source + ".cln");
				FileOutputStream fos = new FileOutputStream(fout);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
						fos, "UTF-8"));
				for (int i = 0; i < fcorpus.size(); i++) {
					String line = fcorpus.get(i);

					bw.write(line + "\n");
					bw.flush();
				}
				File fout1 = new File(target + ".cln");
				FileOutputStream fos1 = new FileOutputStream(fout1);
				BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(
						fos1, "UTF-8"));
				for (int i = 0; i < ecorpus.size(); i++) {
					String line = ecorpus.get(i);

					bw1.write(line + "\n");
					bw1.flush();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void corpusWithaddClassId(String source, String target) {
		int len = 0;
		try {

			Scanner scsource_class = null;
			Scanner sctarget_class = null;
			scsource_class = new Scanner(new FileReader(source + ".cls"));
			sctarget_class = new Scanner(new FileReader(target + ".cls"));
			int x = 0;
			while (scsource_class.hasNextLine() && sctarget_class.hasNextLine()) {

				String line1 = fcorpus.get(x);
				String line2 = ecorpus.get(x);

				String line1_class = scsource_class.nextLine();
				String line2_class = sctarget_class.nextLine();
				String l1[] = line1.split(" ");
				String l2[] = line2.split(" ");
				String lc1[] = line1_class.split(" ");
				String lc2[] = line2_class.split(" ");
				String fline = "";
				for (int i = 0; i < l1.length; i++) {
					fline += l1[i] + "|" + lc1[i] + " ";
				}
				x++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public SentenceClustering() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		String source = args[0];//"de-en/europarl-v7.de-en.de";
		String target = args[1];//"de-en/europarl-v7.de-en.en";
		SentenceClustering sc = new SentenceClustering();
		sc.readCorpus(source, target, true); // false = not clean; true = clean
												// with length based clustering
												// sentences
	}

}
