package Decoder;

import EBMT.templatePhraseExtraction;

public class DecodingWithMoses {

	public DecodingWithMoses() {
		// TODO Auto-generated constructor stub
	}
	public void runEngine(){
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
				
			}
		}
	}

}
