package EBMT;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class makeEBMTfile {

	public makeEBMTfile(){
		
		
	}
	public void makefile(String file , int strt, int end){
		Scanner scsource = null;
		try {
			scsource = new Scanner(new FileReader(file));
			int x = strt;
			while (scsource.hasNextLine() ) {

				String line1 = scsource.nextLine();
				System.out.println(line1);
				x++;
				if(x==end)break;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String args[]){
		makeEBMTfile e = new makeEBMTfile();
		//e.makefile(args[1], strt, end);
	}
}
