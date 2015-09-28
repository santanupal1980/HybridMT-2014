/**
 * 
 */
package preprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 * @author santanu
 *
 */
public class cleanDevTestset {

	/**
	 * 
	 */
	public cleanDevTestset() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public void cleanfile(String source, String target){
		int len = 0;
		SentenceClustering sc  = new SentenceClustering();
		try {
			Scanner scsource = null;
			Scanner sctarget = null;

			scsource = new Scanner(new FileReader(source));
			sctarget = new Scanner(new FileReader(target));
			File fout = new File(source + ".cln");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					fos));
			
			File fout1 = new File(target + ".cln");
			FileOutputStream fos1 = new FileOutputStream(fout1);
			BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(
					fos1));

			int x = 0;
			while (scsource.hasNextLine() && sctarget.hasNextLine()) {

				String line1 = scsource.nextLine();
				String line2 = sctarget.nextLine();
				line1= sc.formatStringCausal(line1);
				line1=sc.linecleaning(line1);
				line2= sc.formatStringCausal(line2);
				line2=sc.linecleaning(line2);
				if(line1.trim().length()>1&&line2.trim().length()>1){
					bw.write(line1+"\n");
					bw1.write(line2+"\n");
					bw.flush();
					bw1.flush();
					System.out.println(line1+"\t"+line2);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		cleanDevTestset cl = new cleanDevTestset();
		String workDir = "./train_NEA_EBMT/";
		String domain[] = { "health/"/*, "health/" */};
		String languagePair[] = { "Hindi_Bangla/", "Hindi_English/",
				"Hindi_Marathi/", "Hindi_Tamil/", "Hindi_Telegu/" };
		for(int i=0;i<1/*languagePair.length*/;i++){
			for(int j=0;j<domain.length;j++){
				String ext = "";
				if(languagePair[i].contains("Bangla")){
					ext = "bn";
				}
				if(languagePair[i].contains("English")){
					ext = "en";
				}
				if(languagePair[i].contains("Marathi")){
					ext = "mr";
				}
				if(languagePair[i].contains("Tamil")){
					ext = "ta";
				}
				if(languagePair[i].contains("Telegu")){
					ext = "te";
				}
				String source=workDir +languagePair[i]+domain[j]+"/train."+ext+".ebmt";
				String target= workDir+languagePair[i]+domain[j]+"/train.hi.ebmt";
				cl.cleanfile(source, target);
			}
		}

	}

}
