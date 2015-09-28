package preprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class NeIdentification {

	List<String> fcorpus = new ArrayList<String>();
	List<String> ecorpus = new ArrayList<String>();
	List<String> nelist = new ArrayList<String>();

	public String neR(String line1, String line2, String sourceTag,
			String targetTag) {
		String l1[] = line1.split(" ");
		String l2[] = line2.split(" ");
		int sne = 0;
		int tne = 0;
		String sourceNE = "";
		String targetNE = "";
		for (int i = 0; i < l1.length; i++) {
			if (l1[i].contains(sourceTag)) {
				sourceNE += l1[i] + " ";
				++sne;
			}
		}
		for (int i = 0; i < l2.length; i++) {
			if (l2[i].contains(targetTag)) {
				targetNE += l2[i] + " ";
				++tne;
			}
		}
		if (sne == tne && sne > 0)
			return sne + "\t" + sourceNE + "\t" + targetNE;
		else
			return "";
	}

	public void loadCorpus(String source, String target) {
		try {
			Scanner scsource = null;
			Scanner sctarget = null;

			scsource = new Scanner(new FileReader(source));
			sctarget = new Scanner(new FileReader(target));

			int x = 0;
			while (scsource.hasNextLine() && sctarget.hasNextLine()) {

				String line1 = scsource.nextLine();
				String line2 = sctarget.nextLine();
				// fcorpus.add(line1);
				// ecorpus.add(line2);
				String tag[] = { "QT_QTC", "NNP", "DM", "QT_QTC" };
				for (int k = 0; k < tag.length; k++) {
					String ne = this.neR(line1, line2, tag[k], tag[k]);
					if (ne.trim().length() > 0) {
						// System.out.println(++x+"\t"+ne);
						String ss[] = ne.split("\t");
						int len = Integer.parseInt(ss[0]);
						if (len > 1 && len <= 4) {
							String sne[] = ss[1].split(" ");
							String tne[] = ss[2].split(" ");
							for (int i = 0; i < len; i++) {
								nelist.add(sne[i] + "\t" + tne[i]);
							}
						}
						nelist.add(ss[1] + "\t" + ss[2]);
					}

				}
			}
			Collections.sort(nelist);
			HashSet<String> sh = new HashSet<String>(nelist);
			nelist = new ArrayList<String>(sh);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void writeNE(String source, String target, boolean flag) {
		this.loadCorpus(source, target);
		try {
			File fout = new File(source + ".ne");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			File fout1 = new File(target + ".ne");
			FileOutputStream fos1 = new FileOutputStream(fout1);
			BufferedWriter bw1 = new BufferedWriter(
					new OutputStreamWriter(fos1));
			if (flag)
				for (int i = 0; i < nelist.size(); i++) {
					String ne[] = nelist.get(i).split("\t");

					bw.write(this.formatStringCausal(ne[0]) + "\n");
					bw1.write(this.formatStringCausal(ne[1]) + "\n");
					bw.flush();
					bw1.flush();
				}
			else
				for (int i = 0; i < nelist.size(); i++) {
					String ne[] = nelist.get(i).split("\t");

					bw.write(ne[0] + "\n");
					bw1.write(ne[1] + "\n");
					bw.flush();
					bw1.flush();
				}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String formatStringCausal(String str) {
		String finalResult = "";
		try {
			str = str.replace(" \\ ", " ");
			str =str.replaceAll("  *", " ");
			str = str.replace("\\", "/");
			str = str.replace(" / ", " ");
			str =str.replaceAll("  *", " ");
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String workDir = "./train_dev_bitext/";
		String domain[] = { "tourism/", "health/" };
		String languagePair[] = { "Hindi_Bangla/", "Hindi_English/",
				"Hindi_Marathi/", "Hindi_Tamil/", "Hindi_Telegu/" };
		String source = workDir + languagePair[0] + domain[0] + "train.bn";
		String target = workDir + languagePair[0] + domain[0] + "train.hi";
		NeIdentification ne = new NeIdentification();
		ne.writeNE(source, target,true);
	}

}
