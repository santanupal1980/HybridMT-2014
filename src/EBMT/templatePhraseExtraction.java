/**
 * 
 */
package EBMT;

import java.io.File;

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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
