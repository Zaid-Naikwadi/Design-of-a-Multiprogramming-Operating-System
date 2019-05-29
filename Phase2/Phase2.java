package myOS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


class PCB{
	public int job_id;
	public int TTL;
	public int TLL;
	public int TTC;
	public int LLC;
	
	PCB(int job_id,int TTL,int TLL,int TTC,int LLC){
		this.job_id = job_id;
		this.TTL = TTL;
		this.TLL = TLL;
		this.TTC = TTC;
		this.LLC = LLC;
		
	}
}

public class Phase2{
	static char M [][] = new char [300][4];	//Memory 100*4
	static char R [] = new char [4];		//General Purpose register
	static char IR [] = new char [4];		//fetches instruction from memory one by one
	static int IC  ; 	//loads starting address of first instruction
	static boolean C;							//store True or False result of operations
	static int SI;
	static String data = new String();
	static int TTL,TLL;
	static int PTE;	//Page Table Entry
	static int RA;	//Real Address
	static int valid;	//Page fault valid or not
	String EM;
	static int PTR;	//store randomly generated address of Page Table
	BufferedReader br = null;
	static int ProgramEnd;
	static int PI;
	static int TI;
	
	static int RandomNumbersGenerated [] = new int[100];	//to store already generated random numbers
	
	PCB pcb;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("start");
		Phase2 phase2 = new Phase2();
		phase2.Load();
		
		int i,j;
		System.out.println("Memory\n");
		for(i=0;i<300;i++){
			System.out.print(i+": ");
			for(j=0;j<4;j++)
				System.out.print(M[i][j]+" ");
			
			System.out.println("\n");
		}
		
		

	}
	
	
	private static void writeTwoBlankLines() {
		// TODO Auto-generated method stub
		
		 File file = new File("C:\\Users\\naikw\\workspace\\MultiprogrammingOS\\src\\myOS\\output13.txt");
	     FileWriter fr = null;
	        

	        try {
	        	fr = new FileWriter(file,true);
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


	void Load(){
		
		System.out.println("inload");
			File file = new File("C:\\Users\\naikw\\workspace\\MultiprogrammingOS\\src\\myOS\\input5.txt");
			
			try {
				br = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String current_card;	//buffer
			int job_id = 0;
			String line;
			try {
				while((line = br.readLine()) != null){
				     //process the line
				     System.out.println(line);
				     
				     current_card = line;
				     
				     if(current_card.startsWith("$AMJ")){ 
				    	 writeTwoBlankLines();
				    	 init();
				    	job_id = Integer.parseInt(current_card.substring(4, 8));
					    TTL = Integer.parseInt(current_card.substring(8, 12));
					    TLL = Integer.parseInt(current_card.substring(12,16));
				    	System.out.println("TTL:"+TTL);
				    	System.out.println("TLL:"+TLL);
					    pcb = new PCB(job_id,TTL,TLL,0,0);
					    allocate();
				    	 
				     }
				     
				     else if(current_card.startsWith("$END")){
				    //	 terminate(0);
				    	 //break;
				     }
				     
				     else if(current_card.startsWith("$DTA")){
				    	 startExecution();
				    	 
				     }
				     
				     
				     else{
				    	 allocateForProgram(current_card);
				     }
				     
				}
					

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	private void init() {
		// TODO Auto-generated method stub
		for(int i=0;i<300;i++)
			for(int j=0;j<4;j++)
				M[i][j] = '-';
		
		IC=0;
		TI=0;
		valid = 0;
		ProgramEnd = 0;
		SI = 0;
		
		for(int i=0;i<100;i++){
			RandomNumbersGenerated[i] = 100;	//ini to 100 as 100 will never be generated
		}
	}


	private void allocateForProgram(String current_card) {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int FrameNumber;
		System.out.println("current_card"+current_card);
		//do{
	//	FrameNumber = rand.nextInt(30);
		//}while(FrameNumber==(PTR/10));
		int j=0,flag=0;
		while(true){
			FrameNumber = rand.nextInt(30);
			for(int i=0;i<100;i++){
				if(RandomNumbersGenerated[i] == 100){
					RandomNumbersGenerated[i] = FrameNumber;
					flag=0;
					break;
				}
				else if(RandomNumbersGenerated[i] == FrameNumber){
					flag =1;
					break;
				}
			}
			
			if(flag==0)
				break;
		}
		
		flag=0;
		for(int i=PTR;i<(PTR+10);i++){
			for(j=0;j<4;j++){
				if(M[i][j]=='%'){
					M[i][j] = 0+'0';
					M[i][j+1] = 0+'0';
					M[i][j+2] = (char)((FrameNumber/10)+'0');
					M[i][j+3] = (char)((FrameNumber%10)+'0');
					flag=1;
					break;
					
				}
				
			}
			
			if(flag==1)
				break;
		}	
		
		FrameNumber *= 10;
		
		int row_number=0;
		
		for(int i=0;i<current_card.length();i+=4,row_number++)
			for(j=0;j<4;j++)
				M[FrameNumber+row_number][j] = current_card.charAt(i+j); 
		
		
	}
	
	private void allocate2() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int FrameNumber;
	//	System.out.println("current_card"+current_card);
		//do{
	//	FrameNumber = rand.nextInt(30);
		//}while(FrameNumber==(PTR/10));
		int j=0,flag=0;
		while(true){
			FrameNumber = rand.nextInt(30);
			for(int i=0;i<100;i++){
				if(RandomNumbersGenerated[i] == 100){
					RandomNumbersGenerated[i] = FrameNumber;
					flag=0;
					break;
				}
				else if(RandomNumbersGenerated[i] == FrameNumber){
					flag =1;
					break;
				}
			}
			
			if(flag==0)
				break;
		}
		
		flag=0;
		
		j=0;
		M[PTE][j] = 0+'0';
		M[PTE][j+1] = 0+'0';
		M[PTE][j+2] = (char)((FrameNumber/10)+'0');
		M[PTE][j+3] = (char)((FrameNumber%10)+'0');
		
		/*FrameNumber *= 10;
		
		int row_number=0;
		
		for(int i=0;i<current_card.length();i+=4,row_number++)
			for(j=0;j<4;j++)
				M[FrameNumber+row_number][j] = current_card.charAt(i+j); 
				*/
		
		
	}


	private void startExecution() {
		// TODO Auto-generated method stub
		
	/*	String line;
		try {
			while((line=br.readLine()) != null)
				System.out.println("line"+line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		while(ProgramEnd==0){
			System.out.println("IC:"+IC);
			RA = addressMap(IC);
			System.out.println("RA:"+RA);
			for(int i=0;i<4;i++){
				IR[i] = M[RA][i];
			}
			
			IC++;
			System.out.println("After Increment IC:"+IC);
			
			try{
			RA = addressMap2(IR[2],IR[3]);
			}catch(Exception e){
				if(IR[2]=='L' && IR[3]=='T')
				System.out.println("HALTING");
				else
					PI=2;
					
			}
			System.out.println("After 2 IC: "+IC);
			if(PI!=0){
				if(PI==3 && (IR[0]=='G' && IR[1]=='D') || (IR[0] == 'S' && IR[1]=='R') )
					valid =1;
			}
			else{
				System.out.println("IR:"+IR[0]+IR[1]);
				switch(IR[0]+IR[1]){
					case 'G'+'D': SI = 1;
									//MOS();
									break;
						
					case 'P'+'D': SI = 2;
									//MOS();
									break;
						
					case 'H'+'A': SI = 3;
									//MOS();
									break;
								
					case 'L'+'R': 
			
						System.out.println("IR:"+IR[2]+IR[3]);
						for(int i=0;i<4;i++)
							R[i] = M[RA][i];
		
						for(int i=0;i<4;i++)
							System.out.print("R:"+R[i]);
			
						break;
			
					case 'S'+'R':
						for(int i=0;i<4;i++){
							M[RA][i] = R[i];
						}
		
						break;
			
					case 'C'+'R':
						for(int i=0;i<4;i++){
							if(M[RA][i] == R[i])
								C = true;
							else{
								C = false;
								break;
							}
						}
		
						break;
			
					case 'B'+'T':
						if(C == true)
							IC = Integer.parseInt(""+IR[2]+IR[3]);
		
						break;
						
					default: PI =1;	//opcode error
		
				}
			}
			
			//if(SI!=0 || PI!=0)
			//MOS();
			
			simulation();
			
			
		}
		
		
	}


	private void simulation() {
		// TODO Auto-generated method stub
		pcb.TTC++;
		System.out.println("PCB.TTC:"+pcb.TTC);
		System.out.println("PCB.TTL:"+pcb.TTL);
		if(pcb.TTC > pcb.TTL)
			TI = 2;	//Time Limit Exceeded
		System.out.println("Mos IC:"+IC);
		System.out.println("SI:"+SI);
		System.out.println("TI:"+TI);
		System.out.println("PI:"+PI);
		if(SI!=0 || TI!=0 || PI!=0)
			MOS();
		
	//	if(TI!=0)
		//	MOS();
		
		System.out.println("Sim IC: "+IC);
		
	}
	


	private void MOS() {
		// TODO Auto-generated method stub
		switch(""+TI+SI){
			case "01":read();
						break;
			case "02":write();
						break;
			case "03":terminate(0);
						break;
			case "21":terminate(3);
						break;
			case "22":write();
					terminate(3);
						break;
			case "23":terminate(0);
						break;
		
		}
		
		switch(""+TI+PI){
			case "01":terminate(4);
						break;
			case "02":terminate(5);
						break;
			case "03":
				if(valid==1){
				//Valid Page Fault
				//allocate2(br.readLine());
					valid =0;
				allocate2();
				IC--;
				System.out.println("NOW IC:"+IC);
				PI=0;
			}
				else
					terminate(6);
				break;
			
			case "21":
				terminate(3+4);
				break;
				
			case "22":
				terminate(3+5);
				break;
			
			case "23":
				terminate(3);
				break;
		}
		
	}


	private void terminate(int em) {
		// TODO Auto-generated method stub
		if(em==0)
			writeUsingFileWriter("Normal Termination");
		else
			writeUsingFileWriter("Abnormal Termination!");
		
		writeUsingFileWriter(getErrorMessage(em)+"  IR:"+IR[0]+IR[1]+IR[2]+IR[3]+"  IC:"+IC+"  R:"+R[0]+R[1]+R[2]+R[3]+"  Toggle:"+C+"  TTL:"+pcb.TTL+"  TLL:"+pcb.TLL+"  TTC:"+pcb.TTC+"  LLC:"+pcb.LLC);
		ProgramEnd = 1;
	}


	private String getErrorMessage(int em) {
		// TODO Auto-generated method stub
		String errorMessage = new String();
		switch(em){
		case 0:
			errorMessage = "No Error";
			break;
		case 1:
			errorMessage = "Out of Data";
			break;
		case 2:
			errorMessage  = "Line Limit Exceeded";
			break;
		case 3:
			errorMessage = "Time Limit Exceeded";
			break;
		case 4:
			errorMessage = "Operation Code Error";
			break;
		case 5:
			errorMessage = "Operand Error";
			try {
				br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 6:
			errorMessage = "Invalid Page Fault";
			break;
		case 7:
			errorMessage = "Time Limit Exceeded and Operation Code Error";
			break;
		case 8:
			errorMessage = "Time Limit Exceeded and Operand Error";
			break;
		}
		return errorMessage;
	}


	private void write() {
		// TODO Auto-generated method stub
		pcb.LLC++;
		if(pcb.LLC>pcb.TLL){
			terminate(2);
		}
		else{
		String address = ""+IR[2];
		address += ""+IR[3];
		
		int addr = Integer.parseInt(address);
		addr = addressMap(addr);
		
		int row_number=0;		
		int column_number = 0;
		String output = new String();
		System.out.println("start");
		for(int i=0;true;i++,column_number++){
			if(M[addr+row_number][column_number] == '-')
				break;
			else{
				output += M[addr+row_number][column_number];
				System.out.print(M[addr+row_number][column_number]);
			}
			if(column_number==3){
				row_number++;
				column_number = -1;
			}
		}
		
		writeUsingFileWriter(output);
		
		System.out.println();
		SI = 0;
		}
	}


	private void writeUsingFileWriter(String data) {
		// TODO Auto-generated method stub
		 File file = new File("C:\\Users\\naikw\\workspace\\MultiprogrammingOS\\src\\myOS\\output13.txt");
	     FileWriter fr = null;
	        

	        try {
	        	fr = new FileWriter(file,true);
	        	fr.write(data+System.lineSeparator());
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


	private void read() {
		// TODO Auto-generated method stub\
		String current_card = new String();
		try {
			current_card = br.readLine();
			System.out.println("IN READ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(current_card.contains("$END")){
			terminate(1);
		}
		else{
		String address = ""+IR[2];
		address += ""+IR[3];
		
		int addr = Integer.parseInt(address);
		addr = addressMap(addr);
		
		int row_number=0;		
		int column_number = 0;
		for(int i=0;i<current_card.length();i++,column_number++){
			M[addr+row_number][column_number] = current_card.charAt(i);
		
			if(column_number==3){
				row_number++;
				column_number = -1;
			}
				
		}
	}
		
		SI = 0;
}


	private int addressMap2(char d, char e) {
		// TODO Auto-generated method stub
		
		String address = ""+d;
		address += ""+e;
		
		int addr = Integer.parseInt(address);
		
		if(addr>99){	//operand error
			PI = 2;
			return 0;
		}
		
		
		PTE = PTR+addr/10;
		if(M[PTE][0] == '%'){
			PI = 3;	 //Page Fault
			return 0;
		}
		
		return addressMap(addr);
		
		
	}


	private int addressMap(int VA) {
		// TODO Auto-generated method stub
		int PTE;	//Page Table Entry
		
		PTE = PTR+VA/10;
		String frame = new String();
		for(int i=0;i<4;i++)
			frame += M[PTE][i];
		
		System.out.println("Frame:"+frame);
		int frame_number = Integer.parseInt(frame);
		
		System.out.println("Still IC"+IC);
		
		return 10*frame_number+VA%10;
		
	}


	private void allocate() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int PageTableAddress = rand.nextInt(30);
		RandomNumbersGenerated[0] = PageTableAddress;
		PTR = PageTableAddress*10;
		System.out.println("PTR="+PTR);
		
		for(int i=PTR;i<(PTR+10);i++)
			for(int j=0;j<4;j++)
				M[i][j] = '%';
	}
	
}	
