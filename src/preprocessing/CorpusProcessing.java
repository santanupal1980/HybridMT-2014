/**
 * 
 */
package preprocessing;

import java.io.File;

import EBMT.templatePhraseExtraction;

/**
 * @author santanu
 *step1: This interface make clean and cluster the corpus according to its sentence 
 *length 
 *step 2: multi-word identification on the source and target side
 *
 *Step 3: mwe level parallel corpus should generate according to high PMI and low PMI
 */
public class CorpusProcessing {

	/**
	 * 
	 */File dir2;
	public CorpusProcessing() {
		this.dir2 = new File (".");
	}

	/**
	 * @param args
	 */
	public void combineMultipleFile(String file1, String file2, String ofile){
		Runtime rt = java.lang.Runtime.getRuntime(); 
		try{
			String command="sh combine.sh ";
			command+=file1+" "+file2+" "+ofile;
		
			System.out.println("\n"+command+":\n....");
			String[] cmd = {"/bin/sh", "-c", command};
			Process P = rt.exec(cmd,null,dir2);
			P.waitFor();
			System.out.println("translation done...op->\n");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CorpusProcessing cor = new CorpusProcessing();
		String workDir = "./train_dev_bitext/";
		String domain[] = { "tourism/", "health/" };
		String languagePair[] = { "Hindi_Bangla/", "Hindi_English/",
				"Hindi_Marathi/", "Hindi_Tamil/", "Hindi_Telegu/" };
		for(int i=0;i<languagePair.length;i++){
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
				String source=workDir +languagePair[i]+domain[j]+"/train."+ext;
				String target= workDir+languagePair[i]+domain[j]+"/train.hi";
				/*SentenceClustering sc  =  new SentenceClustering();
				sc.readCorpus(source, target,true); // false = not clean; true = clean with length based clustering sentences
				NeIdentification ne = new NeIdentification();
				ne.writeNE(source, target,true);*/
				cor.combineMultipleFile(source+".cln", source+".ne", source+".nea");
				cor.combineMultipleFile(target+".cln", target+".ne", target+".nea");
				
				templatePhraseExtraction te = new templatePhraseExtraction();
				te.runCicekliTemplate(source+".cln",target+".cln", 5);
			}	
		}
		/* Multiword identification
		 * Step 1 run tri-gram language model on both source and target
		 * step 2 design trigram, bigram mwe PMI code
		 * consider less composition words and highest composition words 
		 */
		
		/*rename the clean file and run then train GIZA++, Berkeley aligner, fast aligner etc. following */
		
		//sc.corpusWithaddClassId(source, target);
		
		/* =====================================================
		 * OFFLINE PROCESS
		 * =====================================================
		 * run class based factored alignment */
		/* run source context model */
		/* run class based model */
		
		

	}

}
