# HybridMT-2014
This is the system published in SMT tool contest for ICON-2014 
keep all codes into the same directory.

javac SentenceClustering.java

java SentenceClustering train.source train.target

e.g. java SentenceClustering de-en/europarl-v7.de-en.en de-en/europarl-v7.de-en.de

* the o/p file will be saved as 

de-en/europarl-v7.de-en.en.cln

de-en/europarl-v7.de-en.de.cln

javac templatePhraseExtraction.java



java templatePhraseExtraction <source> <target> <no-of-iterations>

java templatePhraseExtraction de-en/europarl-v7.de-en.en.cln de-en/europarl-v7.de-en.de.cln 5

* the o/p file will be saved as *.ebmt

If you use any code cite this paper:

@InProceedings {Pal:2014,
	author			= {Pal, Santanu and Srivastava, Ankit and  Dandapat, Sandipan  and van Genabith, Josef and Liu, Qun and Way, Andy},
	title				= {{USAAR-DCU Hybrid Machine Translation System for ICON 2014}},
	booktitle			= {Proceedings of the 11th International Conference on Natural Language Processing},
	year					= {2014},
	address				= {Goa, India}
}

@InProceedings{pal-naskar-vangenabith:2015:WMT,
  author    = {Pal, Santanu  and  Naskar, Sudip  and  van Genabith, Josef},
  title     = {UdS-Sant: English--German Hybrid Machine Translation System},
  booktitle = {Proceedings of the Tenth Workshop on Statistical Machine Translation},
  month     = {September},
  year      = {2015},
  address   = {Lisbon, Portugal},
  publisher = {Association for Computational Linguistics},
  pages     = {152--157},
  url       = {http://aclweb.org/anthology/W15-3017}
}

