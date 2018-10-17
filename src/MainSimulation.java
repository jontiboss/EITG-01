import java.util.*;
import java.io.*;

//Denna klass ärver Global så att man kan anända time och signalnamnen utan punktnotation


public class MainSimulation extends Global{
	
    public static void main(String[] args) throws IOException {

    	//Signallistan startas och actSignal deklareras. actSignal är den senast utplockade signalen i huvudloopen nedan.

    	Signal actSignal;
    	new SignalList();
		int nextPackageTo = 1;
    	//Här nedan skapas de processinstanser som behövs och parametrar i dem ges värden.
	
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
    	Generator.sendTo = Q1; //De genererade kunderna ska skickas till kösystemet QS

    	//Här nedan skickas de första signalerna för att simuleringen ska komma igång.

    	SignalList.SendSignal(READY, Generator, time);
    	SignalList.SendSignal(MEASURE, Q1, time);
	SignalList.SendSignal(MEASURE, Q1, time);
    	SignalList.SendSignal(MEASURE, Q2, time);
    	SignalList.SendSignal(MEASURE, Q3, time);
    	SignalList.SendSignal(MEASURE, Q4, time);
    	SignalList.SendSignal(MEASURE, Q5, time);

    	// Detta är simuleringsloopen:

    	while (time < 100000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
			actSignal.destination.TreatSignal(actSignal);
			// om signalen Ã¤r en arrival signal så skall signalen skickas vidare till lämpligt kösystem beroende på vilken algoritm som används
			if(actSignal.signalType == 1){
				//MainSimulation.randQueue(Generator,Q1,Q2,Q3,Q4,Q5);
				MainSimulation.sequenceQueue(Generator,Q1,Q2,Q3,Q4,Q5,nextPackageTo++);
				//MainSimulation.leastfloodedQueue(Generator,Q1,Q2,Q3,Q4,Q5);
			}
    	}

    	//Slutligen skrivs resultatet av simuleringen ut nedan:

    	System.out.println("Medelantal kunder i kÃ¶system: " + 1.0*Q1.accumulated/Q1.noMeasurements);
    	System.out.println("Medelantal kunder i kÃ¶system: " + 1.0*Q2.accumulated/Q2.noMeasurements);
    	System.out.println("Medelantal kunder i kÃ¶system: " + 1.0*Q3.accumulated/Q3.noMeasurements);
    	System.out.println("Medelantal kunder i kÃ¶system: " + 1.0*Q4.accumulated/Q4.noMeasurements);
    	System.out.println("Medelantal kunder i kÃ¶system: " + 1.0*Q5.accumulated/Q5.noMeasurements);
    
	}
	// väljer kö slumpmässigt
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
	//skickar packeten i en sekvens från 1-5
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
	//väljer den kö som är minst belastad
	public static void leastfloodedQueue(Gen Generator,QS Q1,QS Q2,QS Q3,QS Q4,QS Q5){
		QS smalestQueue = Q1;
		QS[] queues = {Q1,Q2, Q3, Q4, Q5};
		for(int i=0;i<queues.length;i++) {

			if(smalestQueue.numberInQueue>queues[i].numberInQueue) {
				
				smalestQueue=queues[i];
				
			}
			Generator.sendTo = smalestQueue;
		}
		
    }
	
	
	   
}




	
	
