package myOS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Phase1 {
	
	static char M [][] = new char [100][4];	//Memory 100*4
	static char R [][] = new char [1][4];		//General Purpose register
	static char IR [][] = new char [1][4];		//fetches instruction from memory one by one
	static int IC  ; 	//loads starting address of first instruction
	static boolean C;							//store True or False result of operations
	static int SI;
	static String data = new String();
	static int TTL,TLL;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		String current_card;
		String instruction;
		int job_number = 0;
		
		
		
		File file = new File("C:\\Users\\naikw\\workspace\\MultiprogrammingOS\\src\\myOS\\input3.txt");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int flag = 0;

		String line;
		try {
			while((line = br.readLine()) != null){
			     //process the line
			     System.out.println(line);
			     
			     current_card = line;
			     

			     if(flag == 2){
			    	 if(current_card.startsWith("$")){
			    		 flag = 0;
			    	 }
			    	 else{
			    		data += current_card;
			    		data += "!";
			    	 }
			    	}
			     
			     for(int i=0;i<line.length() && flag != 2 ;i+=4){
			    	instruction = line.substring(i, i+4);
			    	System.out.println("instrction:"+instruction);
			    	
			    	if(instruction.equalsIgnoreCase("$AMJ")){
			    		init();
			    		job_number = Integer.parseInt(current_card.substring(i+4, i+8));
			    		TTL = Integer.parseInt(current_card.substring(i+8, i+12));
			    		TLL = Integer.parseInt(current_card.substring(i+12,i+16));
			    		i = 16;
			    		flag = 1;
			    		
			    	}
			    	
			    	else if(instruction.equalsIgnoreCase("$DTA")){
			    		flag = 2;
			    		System.out.println("break");
			    		break;
			    	}
			    	
			    	else if(instruction.equalsIgnoreCase("$END")){
			    		flag = 0;
			    		if(job_number == Integer.parseInt(current_card.substring(i+4,i+8))){
			    				System.out.println("Success end of "+job_number+" job");
			    				user_program();
			    				fileWriterNewLine();
			    				
			    		}
			    		else
			    			System.out.println("Error in Job number");
			    		i = 8;
			    	}
			    	
			    	else if(flag == 1){
			    		System.out.println("asdf");
			    		addToMemory(instruction);
			    	}
			     }
			     
			     
			}
			
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("data:"+data);
		
		
		
		
		
		int i,j;
		System.out.println("Memory\n");
		for(i=0;i<100;i++){
			System.out.print(i+": ");
			for(j=0;j<4;j++)
				System.out.print(M[i][j]+" ");
			
			System.out.println("\n");
		}
		
		

	}

	private static void fileWriterNewLine() {
		// TODO Auto-generated method stub
		File file = new File("C:\\Users\\naikw\\workspace\\MultiprogrammingOS\\src\\myOS\\output1.txt");
        FileWriter fr = null;
        

        try {
        	
        	fr = new FileWriter(file,true);
        	
        	//System.out.println("TLL"+TLL);
        		fr.write(System.lineSeparator());
        		fr.write(System.lineSeparator());
        		fr.write(System.lineSeparator());

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


	private static void user_program() {
		// TODO Auto-generated method stub
		
		System.out.println();
		while(TTL>0){
			for(int i=0;i<4;i++){
				//IR[0][i] = M[Integer.parseInt(""+IC[0][0]+IC[0][1])][i];
				IR[0][i] = M[IC][i];
			}
		
			IC++;
		
			switch(IR[0][0]+IR[0][1]){
				case 'G'+'D': SI = 1;
								MOS();
								break;
						
				case 'P'+'D': SI = 2;
								MOS();
								break;
						
				case 'H'+'A': SI = 3;
								MOS();
								break;
						
				case 'L'+'R': 
			
						System.out.println("IR:"+IR[0][2]+IR[0][3]);
						for(int i=0;i<4;i++)
							R[0][i] = M[Integer.parseInt(""+IR[0][2]+IR[0][3])][i];
		
						for(int i=0;i<4;i++)
							System.out.print("R:"+R[0][i]);
			
							break;
			
				case 'S'+'R':
					for(int i=0;i<4;i++){
						M[Integer.parseInt(""+IR[0][2]+IR[0][3])][i] = R[0][i];
					}
		
					break;
			
				case 'C'+'R':
					for(int i=0;i<4;i++){
							if(M[Integer.parseInt(""+IR[0][2]+IR[0][3])][i] == R[0][i])
								C = true;
							else{
								C = false;
								break;
							}
					}
		
					break;
			
				case 'B'+'T':
					if(C == true)
						IC = Integer.parseInt(""+IR[0][2]+IR[0][3]);
		
					break;
		
			}
			
			TTL--;
			
		}
		
	}

	private static void MOS() {
		// TODO Auto-generated method stub
		
		switch(SI){
		case 1:
				read();
				break;
		case 2:
				write();
				break;
		case 3:
				terminate();
				break;
		}
		
	}

	private static void terminate() {
		// TODO Auto-generated method stub
		TTL=1;
		System.out.println("terminated");
	}

	private static void write() {
		// TODO Auto-generated method stub
		int row_number = Integer.parseInt(""+IR[0][2]+IR[0][3]);
		int column_number = 0;
		String output = new String();
		System.out.println("start");
		for(int i=0;true;i++,column_number++){
			if(M[row_number][column_number] == '-')
				break;
			else{
				output += M[row_number][column_number];
				System.out.print(M[row_number][column_number]);
			}
			if(column_number==3){
				row_number++;
				column_number = -1;
			}
		}
		
		writeUsingFileWriter(output);
		
		System.out.println();
	}

	private static void read() {
		// TODO Auto-generated method stub
		String current_data = new String();
		int i=0;
		while(data.charAt(i) != '!'){
			current_data += data.charAt(i);
			i++;
		}
		
		data = data.substring(i+1, data.length());
		//System.out.println("READ DATA:"+data);
		
		System.out.println("current_data:"+current_data);
		int row_number = Integer.parseInt(""+IR[0][2]+IR[0][3]);
		int column_number = 0;
		for(i=0;i<current_data.length();i++,column_number++){
			M[row_number][column_number] = current_data.charAt(i);
		
			if(column_number==3){
				row_number++;
				column_number = -1;
			}
				
		}
			
		
	}

	private static void addToMemory(String instruction) {
		// TODO Auto-generated method stub
		int i,j = 0;
		for(i=0;i<100;i++){
			for(j=0;j<4;j++)
				if(M[i][j] == '-')
					break;
			
			if(j==4){
				continue;	
			}
			
			break;
		}
		
		System.out.println("i="+i+"j="+j);
		for(int k=0;k<instruction.length();++k,j++){
			M[i][j]= instruction.charAt(k);
			System.out.println("M["+i+"]["+j+"] = "+M[i][j]);
		}
		
		
	}

	private static void init() {
		// TODO Auto-generated method stub
		for(int i=0;i<100;i++)
			for(int j=0;j<4;j++)
				M[i][j] = '-';
		
		/*for(int i=0;i<2;i++)
			IC[i] = 0;*/
		
		IC = 0;
		SI = 3;
		
		
		
	}
	
	private static void writeUsingFileWriter(String data) {
        File file = new File("C:\\Users\\naikw\\workspace\\MultiprogrammingOS\\src\\myOS\\output1.txt");
        FileWriter fr = null;
        

        try {
        	
        	fr = new FileWriter(file,true);
        	
        	//System.out.println("TLL"+TLL);
        	if(TLL>1){
        		fr.write(data+System.lineSeparator());
        		TLL--;
        	}
        	else
                fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
