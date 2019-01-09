
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

//Driver program for skip list implementation.

public class SkipListDriver {
    public static void main(String[] args) throws FileNotFoundException {
	Scanner sc;
	  File file = new File("\\lp2-t13.txt");
	    sc = new Scanner(file);
	    
/*	if (args.length > 0) {
	    File file = new File("F:\\UT Dallas\\Courses\\Sem1-Fall18\\Implementation of Data Structures\\LP\\LP2\\Starter\\Input files\\lp2-in02.txt");
	    sc = new Scanner(file);
	    
	}
	else {
	    sc = new Scanner(System.in);
	}*/
	String operation = "";
	long operand = 0;
	int modValue = 999983;
	long result = 0;
	Long returnValue = null;
	SkipList<Long> skipList = new SkipList<>();
	// Initialize the timer
	Timer timer = new Timer();
	int count=0;
	while (!((operation = sc.next()).equals("End"))) {
	    switch (operation) {
	    case "Add": {
		
	    	operand = sc.nextLong();
	    	count++;
			
		if(skipList.add(operand)) {
		    result = (result + 1) % modValue;
		    System.out.println(count +"Add: 1" );
			
		}
		else {

		    System.out.println(count +"Add: 0" );

		}
			break;
	    }
	    case "Ceiling": {
		operand = sc.nextLong();
		returnValue = skipList.ceiling(operand);
		count++;
		if (returnValue != null) {
		    result = (result + returnValue) % modValue;
		    System.out.println(count +"Ceiling: "+returnValue );
		}
		else {
			System.out.println(count +"Ceiling: NULL" );
		}
		break;
	    }
	    case "First": {
		returnValue = skipList.first();
		count++;
		if (returnValue != null) {
		    result = (result + returnValue) % modValue;
		    System.out.println(count +"First: "+returnValue );
		}
		else {
			System.out.println(count +"First: NULL" );
		}
		break;
	    }
	    case "Get": {
		int intOperand = sc.nextInt();
		returnValue = skipList.get(intOperand);
		count++;
		if (returnValue != null) {
		    result = (result + returnValue) % modValue;
		    System.out.println(count +"Get: "+returnValue );
		}
		else {
			System.out.println(count +"Get: NULL" );
		}
		break;
	    }
	    case "Last": {
		returnValue = skipList.last();
		count++;
		if (returnValue != null) {
		    result = (result + returnValue) % modValue;
		    System.out.println(count +"Last: "+returnValue );
		}
		else {
			System.out.println(count +"Last: NULL" );
		}
		
		break;
	    }
	    case "Floor": {
		operand = sc.nextLong();
		count++;
		returnValue = skipList.floor(operand);
		if (returnValue != null) {
		    result = (result + returnValue) % modValue;
		    System.out.println(count +"Floor: "+returnValue );
		}
		else {
			System.out.println(count +"Floor: NULL" );
		}
		break;
	    }
	    case "Remove": {
		operand = sc.nextLong();
		count++;
		if (skipList.remove(operand) != null) {
		    result = (result + 1) % modValue;
		    System.out.println(count +"Remove: "+1 );
		}
		else {
			System.out.println(count +"Remove: 0" );
		}
		break;
	    }
	    case "Contains":{
		operand = sc.nextLong();
		count++;
		if (skipList.contains(operand)) {
		    result = (result + 1) % modValue;
		    System.out.println(count +"Contains: 1" );
		}
		else {
			System.out.println(count +"Contains: 0" );
		}
		break;
	    }
		
	    }
	}
	sc.close();
	// End Time
	timer.end();

	System.out.println(result);
	System.out.println(timer);
    }
}
