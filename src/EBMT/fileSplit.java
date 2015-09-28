/**
 * 
 */
package EBMT;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * @author santanu
 *
 */
public class fileSplit {

	/**
	 * 
	 */
	File dir2;
	List<String> ebmt = new ArrayList<String>();
	public fileSplit() {
		this.dir2 = new File (".");
	}
	 
	public void runCicekliTemplate(String source, String target, int iteration){
		Runtime rt = java.lang.Runtime.getRuntime(); 
		try{
			String command="perl test_final.pl ";
			command+=source+" "+target+" "+iteration+" >"+source+".ebmt";
		
			System.out.println("\n"+command+":\n....");
			String[] cmd = {"/bin/sh", "-c", command};
			Process P = rt.exec(cmd,null,dir2);
			P.waitFor();
			System.out.println("translation done...op->\n");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void splitFile(String src, String tgt, String corpDir){
		Runtime rt = java.lang.Runtime.getRuntime(); 
		try{
			String command="sh splitMultipleFile.sh ";
			command+=src+" "+tgt+" "+corpDir;
		
			System.out.println("\n"+command+":\n....");
			String[] cmd = {"/bin/sh", "-c", command};
			Process P = rt.exec(cmd,null,dir2);
			P.waitFor();
			System.out.println("translation done...op->\n");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void read_directory(String dirname1,String dirname2){
		try{
			File file1 = new File(dirname1);
			File file2 = new File(dirname2);
	        // Reading directory contents
	        File[] files1 = file1.listFiles();
	        File[] files2 = file2.listFiles();

	        for (int i = 0; i < files1.length; i++) {
	            System.out.println(files1[i].getName()+"\t"+files2[i].getName());
	            String source=files1[i].getAbsolutePath();
	            String target=files2[i].getAbsolutePath();
	            System.out.println(source+"\t"+target);
	            this.runCicekliTemplate(source, target, 3);
	        }
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void combineFile(String corpDir){
		Runtime rt = java.lang.Runtime.getRuntime(); 
		try{
			String command="sh MkMerge.sh ";
			command+=corpDir;
		
			System.out.println("\n"+command+":\n....");
			String[] cmd = {"/bin/sh", "-c", command};
			Process P = rt.exec(cmd,null,dir2);
			P.waitFor();
			System.out.println("translation done...op->\n");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void processEbmtFile(String file, String src, String tgt){
		try{
			Scanner scsource = null;
			
			scsource = new Scanner(new FileReader(file));
			File fout = new File(file + "."+src+".ebmt");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					fos));
			
			File fout1 = new File(file + "."+tgt+".ebmt");
			FileOutputStream fos1 = new FileOutputStream(fout1);
			BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(
					fos1));

			int x = 0,y=0;
			while (scsource.hasNextLine()) {
				++y;
				String line1 = scsource.nextLine();
				//if(y>=179360)System.out.println(line1);
				if(line1.contains("\t")){
				String ss[]=line1.split("#");
				String srcTxt = ss[0].trim();
				String tmp[]=ss[1].split("\t");
				String tgtTxt = tmp[0].trim();
				//System.out.println(tmp[1]);
				int count = Integer.parseInt(tmp[1]);
				srcTxt = srcTxt.replaceAll("|", "@@").replace("@@", "");
				tgtTxt = tgtTxt.replaceAll("|", "@@").replace("@@", "");
				String srcSS[]=srcTxt.split(" ");
				String tgtSS[]=tgtTxt.split(" ");
				int l1=srcSS.length;
				int l2=tgtSS.length;
				double ratio = l1/l2;
				if(count>2&& srcTxt.length()>1&& tgtTxt.length()>1&& ((l1>1&&l1<10)||(l2>1&&l2<10))&& ratio==1){
					
					System.out.println(++x+"\t"+srcTxt+"\t"+tgtTxt+"\t"+count);
					bw.write(srcTxt+"\n");
					bw1.write(tgtTxt+"\n");
					bw.flush();
					bw1.flush();
				}
				}
			}
			Collections.sort(ebmt);
			
			for(int i=0;i<ebmt.size();i++){
				System.out.println(ebmt.size()+"\t"+ebmt.get(i));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String workDir = "./train_NEA_EBMT/";
		String domain[] = { "tourism", "health" };
		String languagePair[] = { "Hindi_Bangla", "Hindi_English",
				"Hindi_Marathi", "Hindi_Tamil", "Hindi_Telegu" };
		String corpDir = workDir +languagePair[0]+"/"+domain[1]+"/";
		String src="train.hi.cln";
		String tgt="train.bn.cln";
		String ipfile="EBMT/combined.op";
		
		fileSplit fs = new fileSplit();
		//fs.splitFile(src, tgt,corpDir);
		//fs.read_directory(corpDir+"/EBMT/src",corpDir+"/EBMT/tgt");
		//fs.combineFile(corpDir);
		fs.processEbmtFile(corpDir+ipfile, "hi", "bn");
	}

}
