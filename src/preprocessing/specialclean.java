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
public class specialclean {

	/**
	 * 
	 */
	public specialclean() {
		// TODO Auto-generated constructor stub
	}

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
					fos,"UTF-8"));
			
			File fout1 = new File(target + ".cln");
			FileOutputStream fos1 = new FileOutputStream(fout1);
			BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(
					fos1,"UTF-8"));

			int x = 0;
			while (scsource.hasNextLine() && sctarget.hasNextLine()) {

				String line1 = scsource.nextLine();
				String line2 = sctarget.nextLine();
				
				//line1= sc.formatStringCausal(line1);
				//line1=sc.linecleaning(line1);
				//line2= sc.formatStringCausal(line2);
				//line2=sc.linecleaning(line2);
				if(line1.trim().length()>1&&line2.trim().length()>1)
				{
					bw.write(line1+"\n");
					bw1.write(line2+"\n");
					bw.flush();
					bw1.flush();
				//System.out.println(line1+"\t"+line2);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		specialclean sc = new specialclean();
		String workDir = "./train_NEA/";
		String domain[] = { "tourism/", "health/", "combined/" };
		String languagePair[] = { "Hindi_Bangla/", "Hindi_English/",
				"Hindi_Marathi/", "Hindi_Tamil/", "Hindi_Telegu/" };
		String source=workDir +languagePair[1]+domain[1]+"/train.en";
		String target= workDir+languagePair[1]+domain[1]+"/train.hi";
		
		sc.cleanfile(source, target);
	}

}
