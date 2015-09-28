/**
 * 
 */
//package preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author root
 *
 */
public class templatePhraseExtraction {

	/**
	 * 
	 */
	File dir2;
	public templatePhraseExtraction() {
		this.dir2 = new File (".");
	}
	
	public void runCicekliTemplate(String source, String target, int iteration){
		Runtime rt = java.lang.Runtime.getRuntime(); 
		try{
			String command="perl test_final_sant.pl ";
			command+=source+" "+target+" "+iteration+" >"+source+".ebmt";
		
			System.out.println("\n"+command+":\n....");
			String[] cmd = {"/bin/sh", "-c", command};
			Process P = rt.exec(cmd,null,dir2);
			P.waitFor();
			System.out.println("translation done...op->\n");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		try{
			FileInputStream sf = new FileInputStream(source+".ebmt");
			BufferedReader sbr = new BufferedReader(new InputStreamReader(sf,
					"UTF-8"));
			File fout1 = new File(source + ".ebmt.cln");
			FileOutputStream fos1 = new FileOutputStream(fout1);
			BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(
					fos1, "UTF-8"));
			String line="";
			while ((line = sbr.readLine()) != null){
				line= line.trim();
				line = line.replace("#", "\t");
				line= line.replace("|", "");
				String ss[]=line.split("\t");
				if(ss[0].trim().length()>0&&ss[1].trim().length()>0){
					int flag=1;
					if(ss[0].contains("X")&&!ss[0].trim().contains(" ")){
						flag=0;
					}
					if(ss[1].contains("X")&&!ss[1].trim().contains(" ")){
						flag=0;
					}
					if(flag==1){
						bw1.write(ss[0]+"|||"+ss[1]+"\t"+ss[2]+"\n");
						bw1.flush();
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		templatePhraseExtraction te = new templatePhraseExtraction();
		te.runCicekliTemplate(args[0], args[1], Integer.parseInt(args[2]));
	}

}
