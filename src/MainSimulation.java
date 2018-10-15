import java.util.*;
import java.io.*;

//Denna klass �rver Global s� att man kan an�nda time och signalnamnen utan punktnotation


public class MainSimulation extends Global{
	
    public static void main(String[] args) throws IOException {

    	//Signallistan startas och actSignal deklareras. actSignal �r den senast utplockade signalen i huvudloopen nedan.

    	Signal actSignal;
    	new SignalList();
		int nextPackageTo = 1;
    	//H�r nedan skapas de processinstanser som beh�vs och parametrar i dem ges v�rden.
	
		QS Q1 = new QS();
		QS Q2 = new QS();
		QS Q3 = new QS();
		QS Q4 = new QS();
		QS Q5 = new QS();
		Q1.sendTo = null;
		Q2.sendTo = null;
		Q3.sendTo = null;
		Q4.sendTo = null;
		Q5.sendTo = null;
		


    	Gen Generator = new Gen();
    	Generator.lambda = 8; //Generator ska generera nio kunder per sekund
    	Generator.sendTo = Q1; //De genererade kunderna ska skickas till k�systemet QS

    	//H�r nedan skickas de f�rsta signalerna f�r att simuleringen ska komma ig�ng.

    	SignalList.SendSignal(READY, Generator, time);
    	SignalList.SendSignal(MEASURE, Q1, time);
		SignalList.SendSignal(MEASURE, Q1, time);
    	SignalList.SendSignal(MEASURE, Q2, time);
    	SignalList.SendSignal(MEASURE, Q3, time);
    	SignalList.SendSignal(MEASURE, Q4, time);
    	SignalList.SendSignal(MEASURE, Q5, time);

    	// Detta �r simuleringsloopen:

    	while (time < 100000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
			actSignal.destination.TreatSignal(actSignal);
			// om signalen är en arrival signal s� skall signalen skickas vidare till l�mpligt k�system beroende p� vilken algoritm som anv�nds
			if(actSignal.signalType == 1){
				//MainSimulation.randQueue(Generator,Q1,Q2,Q3,Q4,Q5);
				MainSimulation.sequenceQueue(Generator,Q1,Q2,Q3,Q4,Q5,nextPackageTo++);
				//MainSimulation.leastfloodedQueue(Generator,Q1,Q2,Q3,Q4,Q5);
			}
    	}

    	//Slutligen skrivs resultatet av simuleringen ut nedan:

    	System.out.println("Medelantal kunder i kösystem: " + 1.0*Q1.accumulated/Q1.noMeasurements);
    	System.out.println("Medelantal kunder i kösystem: " + 1.0*Q2.accumulated/Q2.noMeasurements);
    	System.out.println("Medelantal kunder i kösystem: " + 1.0*Q3.accumulated/Q3.noMeasurements);
    	System.out.println("Medelantal kunder i kösystem: " + 1.0*Q4.accumulated/Q4.noMeasurements);
    	System.out.println("Medelantal kunder i kösystem: " + 1.0*Q5.accumulated/Q5.noMeasurements);
    
	}
	// v�ljer k� slumpm�ssigt
	public static void randQueue(Gen Generator,QS Q1,QS Q2,QS Q3,QS Q4,QS Q5){
		Random rand = new Random();
		switch (rand.nextInt(5)) {
            case 0:  Generator.sendTo = Q1;
                     break;
            case 1:  Generator.sendTo = Q2;
                     break;
            case 2:  Generator.sendTo = Q3;
                     break;
            case 3:  Generator.sendTo = Q4;
                     break;
            case 4:  Generator.sendTo = Q5;
                     break;
          
        }
	   
	}
	//skickar packeten i en sekvens fr�n 1-5
	public static  void sequenceQueue(Gen Generator,QS Q1,QS Q2,QS Q3,QS Q4,QS Q5,int sendPackageTo){
		
	
		switch (sendPackageTo%5) {
            case 0:  Generator.sendTo = Q1;
            		 break;
            case 1:  Generator.sendTo = Q2;
            		 break;
            case 2:  Generator.sendTo = Q3;
            	     break; 
            case 3:  Generator.sendTo = Q4;
            		 break;      
            case 4:  Generator.sendTo = Q5;
                   	 break;
          
        }
	
	   
	} 
	//v�ljer den k� som �r minst belastad
	public static void leastfloodedQueue(Gen Generator,QS Q1,QS Q2,QS Q3,QS Q4,QS Q5){
		
		
		//unders�ker vilken k� som �r kortast
		
		if(Q1.numberInQueue <= Q2.numberInQueue && Q1.numberInQueue <= Q3.numberInQueue && Q1.numberInQueue <= Q4.numberInQueue && Q1.numberInQueue <= Q5.numberInQueue){
			Generator.sendTo = Q1;
		}
		else if(Q2.numberInQueue <= Q1.numberInQueue && Q2.numberInQueue <= Q3.numberInQueue && Q2.numberInQueue <= Q2.numberInQueue && Q2.numberInQueue <= Q5.numberInQueue){
			Generator.sendTo = Q2;
		}
		else if(Q3.numberInQueue <= Q2.numberInQueue && Q3.numberInQueue <= Q2.numberInQueue && Q3.numberInQueue <= Q4.numberInQueue && Q3.numberInQueue <= Q5.numberInQueue){
			Generator.sendTo = Q3;
		}
		else if(Q4.numberInQueue <= Q2.numberInQueue && Q4.numberInQueue <= Q3.numberInQueue && Q4.numberInQueue <= Q1.numberInQueue && Q4.numberInQueue <= Q5.numberInQueue){
			Generator.sendTo = Q4;
		}
		else if(Q5.numberInQueue <= Q2.numberInQueue && Q5.numberInQueue <= Q3.numberInQueue && Q5.numberInQueue <= Q4.numberInQueue && Q5.numberInQueue <= Q1.numberInQueue){
			Generator.sendTo = Q5;
		}
	
	}
	
	   
}


	
	