/* 

------- Student Name : Baabu Aravind Vellaian Selvarajan
------- Student No.  : 200339484

------- Compilation Code     : 1.  javac assign3.java       (creates class files)
                               2.  java assign3             (executes that class file)

------- Example method to give inputs : (n, p, a, r)

                               4,0.33,0.7,0.8               (comma is must inbetween each input)

                               BT (or) FC (or) FLC          (Type any one)

                               Y (or) N                     (Choice of Choosing Arc Consistency)

To check with any more inputs again need to execute " java assign3 " command, and then enter the inputs

*/


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class assign3   // main class file
{
    public static Map<String, ArrayList<String>> container = new HashMap<String, ArrayList<String>>();
    public static int size=0;
    public static Map<String, ArrayList<Integer>> domainValue =  new HashMap<String, ArrayList<Integer>>();
    public static Map<String, String> constraints = new HashMap<String, String>();
    public static String[] nodes;

    public static void main(String[] args) 
    {
        String statusContinue="";
        String[] tempStatus;
    	int n=0;
        double p=0;
        String tempReader="";
        double a=0;
        String statusAC="";
        long timeDifference;
        double r=0;
        String statusStrategy="";
        try  
        {            
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));     

		// Getting the user input parameters (n, p, a, r)
                System.out.println("\nEnter Binary CSP instance Input Parameters (n, p, a, r)"+"\nExample Input (separated by comma) : 4,0.33,0.7,0.8");
                tempReader = reader.readLine();
		// Selection area to choose anyone of the solving techniques 
                System.out.println("\nSelect anyone of the Solving Techniques to solve"+"\nEnter 'BT' (or) 'FC' (or) 'FLA'"+"\nBT  - Standard BackTracking" + "\nFC  - Forward Checking" +"\nFLA - Full Look Ahead");    
                statusStrategy=reader.readLine();
		// Asking user to use arc consistency or not before doing search
                System.out.println("\nApply Arc Consistency Before Search (Y/N) ?");    
                statusAC=reader.readLine();
                tempReader = tempReader.trim(); tempReader = tempReader.replace(" ",""); 
                tempStatus = tempReader.split(",");
                n = Integer.parseInt(tempStatus[0]); 
                p = Double.parseDouble(tempStatus[1]); 
                a = Double.parseDouble(tempStatus[2]); 
                r = Double.parseDouble(tempStatus[3]);                
                long start_time = System.nanoTime();
                BinaryCSPInstances obj = new BinaryCSPInstances(n,p,a,r);
                constraints = obj.contraintsList;
                nodes = obj.values;
                for(int i=0; i < obj.values.length; i++)
                    {  
                        domainValue.put(obj.values[i], obj.domainValues);
                    }
                if(statusAC.compareToIgnoreCase("y") == 0)
                    {
                        arcConsistency();
                    }
                    if(statusStrategy.compareToIgnoreCase("bt") == 0)
                    {
                        backTrack(0);
                    }
                    else
                      if(statusStrategy.compareToIgnoreCase("fc") == 0)
                    {
                        forwarcCheck(0);
                    }  
                    else
                          if(statusStrategy.compareToIgnoreCase("fla") == 0)
                    {
                        fullLookAhead(0);
                    }
                    long end_time = System.nanoTime();
                    timeDifference = (long) ((end_time - start_time)/1e6);
                    System.out.println("\nTime needed to solve the instance: "+(timeDifference)+" milli-seconds\n");                
        }
        catch(Exception io)
        {
          io.printStackTrace();
        }
    }
    
    public static void backTrack(int size)      // BT - Standard Backtracking Solving Technique
    {
	       Integer[] assignValsIndex = null;
	       Integer[] domainVal = SolvingTechniques.createListInteger(domainValue.get(nodes[size]));
	       Integer[] unassignedValsIndex = null;
	       int status = -1;
	       boolean statusConstraint = true;
	       if(!container.isEmpty() && container.containsKey(nodes[size]))
	       { 
	    	   assignValsIndex = SolvingTechniques.createIntegerList(container.get(nodes[size]));  
	       }
	       if(assignValsIndex != null && size == 0 && SolvingTechniques.checkArrays(domainVal, assignValsIndex))
	       {
	           System.out.println("--- No Solution ---");     // If there is no output solution it prints as 'no solution'
	       }
	       else
	       {
	           if(assignValsIndex == null || assignValsIndex.length < 1)
	           {
	               unassignedValsIndex = domainVal;
	           }
	           else
	           {
	               unassignedValsIndex = SolvingTechniques.checkItems(domainVal, assignValsIndex); 
	           }
	           for(int i=0; i < unassignedValsIndex.length; i++)
	            {
	                if(checkConstraint(size, unassignedValsIndex[i]) == false)
	                {
	                    status = unassignedValsIndex[i];
	                    i = unassignedValsIndex.length;
	                    statusConstraint = false; 
	                }
	            }
	            if(statusConstraint == true)
	            {
	                if(container.containsKey(nodes[size]))
	                {
	                    container.remove(nodes[size]);
	                }
	                
	                if(size > 0)
	                {   size --; }  
	                backTrack(size);  
	            }
	            else
	            {
	                if(container.containsKey(nodes[size]))
	                {
	                    ArrayList<String> sv = container.get(nodes[size]);
	                    sv.add(Integer.toString(status));
	                    container.put(nodes[size], sv);
	                }
	                else
	                {
	                    ArrayList<String> sv = new ArrayList();
	                    sv.add(Integer.toString(status));
	                    container.put(nodes[size], sv);
	                }
	                    if(size == (nodes.length-1))
	                    {
	                        System.out.println("\nDone Searching - Printing the Output Values:\n");  // Displays the found output   
	                        displayValues();
	                    }else
	                    {
	                        size++;
	                        backTrack(size);
	                    }
	                }
	       }
    }

    public static void forwarcCheck(int size)  // FC - Forward Checking Solving Technique
    {
       boolean checkConstraint = true;
       Integer[] unAssignedVaues = null;
       int temp=0;
       Integer[] domainVal = SolvingTechniques.createListInteger(domainValue.get(nodes[size]));
       int selectedValue=-1;
       Integer[] assignValues = null;
       if(!container.isEmpty() && container.containsKey(nodes[size]))
       	   { 
    	   		assignValues = SolvingTechniques.createIntegerList(container.get(nodes[size]));  
    	   }
       if(assignValues != null && size == 0 && SolvingTechniques.checkArrays(domainVal, assignValues))
       	   {
           		System.out.println("--- No Solution ---");       // If there is no output solution it prints as 'no solution'
           }
       else
       {
           if(assignValues == null || assignValues.length < 1)
           {
               unAssignedVaues = domainVal;
           }
           else
           {
                 unAssignedVaues = SolvingTechniques.checkItems(domainVal, assignValues); 
           }
           if(size==0)
           {
            temp = SolvingTechniques.randomWithRange(0, unAssignedVaues.length-1);
            selectedValue = unAssignedVaues[temp];
            checkConstraint = false;  
           }
           else
           {
                for(int i=0; i < unAssignedVaues.length; i++)
                {
                    if(checkConstraint(size, unAssignedVaues[i]) == false)
                    {
                        selectedValue = unAssignedVaues[i];
                        i = unAssignedVaues.length;
                        checkConstraint = false; 
                    }
                }
           }
            fcAC(size, selectedValue);
            if(checkConstraint == true)
            {
                if(container.containsKey(nodes[size]))
                {
                    container.remove(nodes[size]);
                }
                if(size > 0)
                {   size --; }  
                forwarcCheck(size);  
            }
            else
            {
                if(container.containsKey(nodes[size]))
                {
                    ArrayList<String> sv = container.get(nodes[size]);
                    sv.add(Integer.toString(selectedValue));
                    container.put(nodes[size], sv);
                }
                else
                {
                    ArrayList<String> sv = new ArrayList();
                    sv.add(Integer.toString(selectedValue));
                    container.put(nodes[size], sv);
                }
                    if(size == (nodes.length-1))
                    {
                        System.out.println("\nDone Searching - Printing the Output Values:\n");   // Displays the found output    
                        displayValues();
                    }
                    else
                    {
                        size++;
                        forwarcCheck(size);
                    }
                }
       }
    }

    public static void fullLookAhead(int size)  // FLA - Full Look Ahead Solving Technique
    {
       Integer[] unassignedValues = null;
       int temp=0;
       Integer[] domainVal = SolvingTechniques.createListInteger(domainValue.get(nodes[size]));
       Integer[] assignValues = null;
       int selectedValues=-1;
       boolean checkConstraints = true;
       if(!container.isEmpty() && container.containsKey(nodes[size]))
       	   { 
    	   assignValues = SolvingTechniques.createIntegerList(container.get(nodes[size]));  
    	   }
       if(assignValues != null && size == 0 && SolvingTechniques.checkArrays(domainVal, assignValues))
       {
           System.out.println("--- No Solution ---");  // If there is no output solution it prints as 'no solution'
       }
       else
       {
           if(assignValues == null || assignValues.length < 1)
           {
               unassignedValues = domainVal;
           }
           else
           {
                unassignedValues = SolvingTechniques.checkItems(domainVal, assignValues); 
           }
           if(size==0)
           {
            temp = SolvingTechniques.randomWithRange(0, unassignedValues.length-1);
            selectedValues = unassignedValues[temp];
            checkConstraints = false;
           }
           else
           {
                for(int i=0; i < unassignedValues.length; i++)
                {
                    if(checkConstraint(size, unassignedValues[i]) == false)
                    {
                        selectedValues = unassignedValues[i];
                        i = unassignedValues.length;
                        checkConstraints = false; 
                    }
                }
           }
            arcConsistency();
            if(checkConstraints == true)
            {
                if(container.containsKey(nodes[size]))
                {
                    container.remove(nodes[size]);
                }
                if(size > 0)
                {   size --; }  
                fullLookAhead(size);  
            }
            else
            {
                if(container.containsKey(nodes[size]))
                {
                    ArrayList<String> sv = container.get(nodes[size]);
                    sv.add(Integer.toString(selectedValues));
                    container.put(nodes[size], sv);
                }
                else
                {
                    ArrayList<String> sv = new ArrayList();
                    sv.add(Integer.toString(selectedValues));
                    container.put(nodes[size], sv);
                }
                    if(size == (nodes.length-1))
                    {
                        System.out.println("\nDone Searching - Printing the Output Values:\n");    // Displays the found output
                        displayValues();
                    }else
                    {
                        size++;
                        fullLookAhead(size);
                    }
                }
       }
    }

    public static void arcConsistency()     // Arc Consistency Process
    {
       Map<String, ArrayList<Integer>> domainList = new HashMap<String, ArrayList<Integer>>();
       Map<String, String> constraint = new HashMap<String, String>();
       constraint = constraints;
       Map<String, ArrayList<Integer>> temp = new HashMap<String, ArrayList<Integer>>();
       ArrayList<Integer> joinValues = new ArrayList();
       for(Map.Entry<String, String> entry : constraint.entrySet()) 
           {
           temp = acceptableValues(entry.getKey(), entry.getValue());
               if(!temp.isEmpty())
            {
                for (Map.Entry<String, ArrayList<Integer>> entryx : temp.entrySet()) 
                {
                       if(domainList.containsKey(entryx.getKey()))
                    {
                        domainList.put(entryx.getKey(), SolvingTechniques.joinList(entryx.getValue(), domainList.get(entryx.getKey())));
                    }
                    else
                    {
                        domainList.put(entryx.getKey(), entryx.getValue());
                    }
                }
                temp.clear();
            }
           }
          domainValue = domainList;
    }

    public static void fcAC(int size, int value)
    {
       String[] constraintsValues;
       String[] constraintsPair;       
       String tempCons="";
       String constraintValue = nodes[size];
       String checkedVariables="";
       for (Map.Entry<String, String> entry : constraints.entrySet()) 
       {
                if(entry.getKey().contains(constraintValue))
                {  
                    tempCons += entry.getValue(); tempCons += "-";  
                    checkedVariables = entry.getKey(); 
                    checkedVariables += "-";
                }	    
       }
       checkedVariables = checkedVariables.substring(0,checkedVariables.length()-1);
       tempCons = tempCons.substring(0,tempCons.length()-1);
       if(!checkedVariables.isEmpty() && !tempCons.isEmpty())
       {
           constraintsPair = tempCons.split("-");
           constraintsValues = checkedVariables.split("-");
       }
    }

    public static String checkNodes(String firstNode, String secondNode)
    {
       String temp3="";
       String temp="";
       String constraint="";
       String temp2="";
       String incompatible="none";
       int check=0;
        if(constraints.containsKey(firstNode+","+secondNode))
        	{ 
        	constraint = constraints.get(firstNode+","+secondNode); 
        	}
        temp = ""; 
        check=0;
        if(constraints.containsKey(secondNode+","+firstNode))
        	{ 
        	temp3 = constraints.get(secondNode+","+firstNode); 
        	}
        if(constraint.isEmpty())
        	{  
        	incompatible = temp3;  
        	}
        else
            if(!constraint.isEmpty() && !temp3.isEmpty())
            	{
            	incompatible = constraint+"~"+temp3;
            	}
        else
            {
                incompatible = constraint;
            }
        return incompatible;
    }

    public static boolean checkConstraint(int size, int value)
    {
       String assignedVals="";
       int rootNode=0;
       String reverse="";
       String temp="";
       boolean status = true;
       String var="";
       ArrayList<String> valueArraylist;
       if(size == 0)
       {
           rootNode=0;  
       }
       else
       {
            for(int i=size; i > 0; i-- )
            {
                reverse = getIncompatibleTuples(nodes[size], nodes[i-1]);
                if(!reverse.isEmpty())
                	{  
                	var = reverse+"~"+var;  
                	}
                if(container.containsKey(nodes[i-1]))
                	{
                    valueArraylist = container.get(nodes[i-1]);
                    if(valueArraylist.size() > 0)
                    	{  
                    	assignedVals += valueArraylist.get(valueArraylist.size()-1); assignedVals += "~";
                    	}
                    else
                    	{    
                    	assignedVals+= valueArraylist.get(0); assignedVals += "~";
                    	}
                	}
            }
                assignedVals = assignedVals.substring(0,assignedVals.length()-1); 
            if(assignedVals.isEmpty())
            {
                rootNode = 0;
            }
            else
            {
                String[] chkAs = assignedVals.split("~");
                String cs="";
                if(chkAs != null && chkAs.length > 0)
                {
                    for(int i=0; i < chkAs.length; i++)
                    {
                        cs = value+","+chkAs[i];
                        if(var.contains(cs))
                        {
                            rootNode++;
                        }
                    }
                }
                else
                {
                    rootNode = 0;
                }
            }
       }
       if(rootNode < 1)
       {
           status = false;
       }
       return status;
    }

    public static ArrayList<String> desertedDomain(String temp, String val)
    {
       ArrayList<Integer> domainVal = new ArrayList();
       int status = -1;
       ArrayList<String> used= new ArrayList();
       if(domainValue.containsKey(temp))
       {
            domainVal = domainValue.get(temp);
            for(int i=0; i < domainVal.size(); i++)
            {
                status = domainVal.get(i);
                if(Integer.parseInt(val) != status)
                {
                    used.add(Integer.toString(status));
                }
            }
       }
       return used;
    }

    public static ArrayList<String> notInDomain(String var, String val)
    {
	       ArrayList<String> res= new ArrayList();
	       ArrayList<Integer> dvals = new ArrayList();
	       int v=-1;
	       if(domainValue.containsKey(var))
	       {
	            dvals = domainValue.get(var);
	            for(int i=0; i < dvals.size(); i++)
	            {
	                v = dvals.get(i);
	                if(Integer.parseInt(val) != v)
	                {
	                    res.add(Integer.toString(v));
	                }
	            }
	       }
	       return res;
    }

    public static String getIncompatibleTuples(String firstNode, String secondNode)     // finding the incompatible tuples process area
    {
       String temp3="";
       String temp="";
       String constraint="";
       String temp2="";
       String incompatible="none";
       int check=0;
        if(constraints.containsKey(firstNode+","+secondNode))
        	{ 
        		constraint = constraints.get(firstNode+","+secondNode);
        	}
        temp = ""; 
        check=0;
        if(constraints.containsKey(secondNode+","+firstNode))
        	{ 
        	temp3 = constraints.get(secondNode+","+firstNode); 
        	}
        if(constraint.isEmpty())
        {  
        	incompatible = temp3;  
        }
        else
            if(!constraint.isEmpty() && !temp3.isEmpty())
        {
            incompatible = constraint+"~"+temp3;
        }
        else
            {
                incompatible = constraint;
            }
        return incompatible;
    }

    public static Map<String, ArrayList<Integer>> acceptableValues(String cons, String conIncompairs)
    {
        ArrayList<Integer> values1 = new ArrayList();
        String[] newString = conIncompairs.replace("~",",").split(",");
        Map<String, ArrayList<Integer>> reverse = new HashMap<String, ArrayList<Integer>>();
        String[] cs = cons.split(",");
        ArrayList<Integer> values2 = new ArrayList();
        ArrayList<String> temp = new ArrayList();
        if(newString.length > 0 && cs.length > 1)
        {
               String[] newString1 = new String[newString.length/2];
               String[] newString2 = new String[newString.length/2];
               for(int i=0; i < newString.length/2; i++)
               {
                   newString1[i] = newString[(i*2)];
                   newString2[i] = newString[(i*2)+1];          
               }
               for(int i=0; i < newString1.length; i++)
               {
                   temp = desertedDomain(cs[0],newString1[i]);
                    if(!temp.isEmpty())
                    {
                        for(int j=0; j < temp.size(); j++)
                        {
                           if(!values1.contains(Integer.parseInt(temp.get(j))))
                           {
                              values1.add(Integer.parseInt(temp.get(j))); 
                           }
                        }
                    }
                    temp.clear();
               }
               for(int i=0; i < newString2.length; i++)
               {
                   temp = desertedDomain(cs[1],newString2[i]);
                    if(!temp.isEmpty())
                    {
                        for(int j=0; j < temp.size(); j++)
                        {
                           if(!values2.contains(Integer.parseInt(temp.get(j))))
                           {
                              values2.add(Integer.parseInt(temp.get(j))); 
                           }
                        }
                    }
                    temp.clear();
               }
               reverse.put(cs[0], values1);
               reverse.put(cs[1], values2);
         }
        return reverse;
    }

    public static ArrayList<String> CountValues(String temp, String val)
    {
       ArrayList<Integer> domainVal = new ArrayList();
       int status = -2;
       ArrayList<String> used= new ArrayList();
       if(domainValue.containsKey(temp))
       {
            domainVal = domainValue.get(temp);
            for(int i=0; i < domainVal.size(); i++)
            {
                status = domainVal.get(i);
                if(Integer.parseInt(val) != status)
                {
                    used.add(Integer.toString(status));
                }
            }
       }
       return used;
    }

    public static void displayValues()
    {
       if(container.isEmpty())
       {
           System.out.println("--- Values are not assigned to Variables ---");
       }
       else
       {
           for (Map.Entry<String, ArrayList<String>> item : container.entrySet()) 
            {
           System.out.println("ID: "+item.getKey()+" Value: "+item.getValue().get(0));
            }
       }
    }
}

class BinaryCSPInstances    // sub-class where declared inputs getting from user
{
    public int variables;
    public double constraintTightness;
    public double alpha;
    public ArrayList<String> createIncompatibleList;
    public int compatibleConstraints;
    public double rConstant;
    public int domainSize;
    public Map<String, String> contraintsList;  
    public ArrayList<String> createConstraintsList;
    public String[] values;
    public ArrayList<Integer> domainValues;
    public int incompatibleConstraints;
    
    public static void main(String[] args) 
    {
    	BinaryCSPInstances obj = new BinaryCSPInstances(4,0.33,0.8,0.7);
    }
     
    public  BinaryCSPInstances(int n, double p, double a, double r)    //  Place where do the process of
    {                                                                  //  generating RB instance by math calculations 
      variables = n;
      constraintTightness = p;
      alpha = a;
      rConstant = r;
      values = new String[n];
        for(int i=0; i < n; i++)
        {
           values[i] = "X"+Integer.toString(i);
        }
       domainSize = (int)Math.round(Math.pow(n, a));                               // finding domain size   (n^alpha)
       compatibleConstraints = (int)Math.round(r * n * Math.log(n));               // finding number of constraints (rnlnn)  
       incompatibleConstraints = (int)Math.round(p * Math.pow(domainSize, 2));     // finding number of incompatible tuples (pd^2)
       createConstraintsList = new ArrayList();
       createIncompatibleList = new ArrayList();
       domainValues = new ArrayList();
       contraintsList = new HashMap<String, String>();
       createConstraints();
       createDomainValues();
       showCSPInstance();
    }

    public void showCSPInstance()
    {
       System.out.println("\n------------- Input Parameters --------------\n");
       printParameters();
       System.out.println("\nConstraint : Incompatible Tuples");          // place finding the incompatible tuples 
       if(createConstraintsList.isEmpty() ==false)                        // and displaying in array list
        {
          for(int i=0; i < createConstraintsList.size(); i++)
          {
              contraintsList.put(createConstraintsList.get(i), createIncompatibleList.get(i));
              System.out.println("("+createConstraintsList.get(i)+"): "+createIncompatibleList.get(i).replace("~","  "));
          }
        }
    } 

    public void createConstraints()
    {
      ArrayList<Integer> value = new ArrayList();
      String tempVariables = "";
      ArrayList<String> possibleVariables = new ArrayList();
      ArrayList<String> possibleConstraints = new ArrayList();
      String tempString = "";
      String res = "";
      String s1="", s2="";
      String tempString1="", tempString2="";
      boolean chk = true;
      for(int i=0; i < compatibleConstraints; i++)
        {
          s1 = values[rangeDifference(0,variables-1)];
          s2 = values[rangeDifference(0,variables-1)];
          while(s1.equalsIgnoreCase(s2))
               {
                s1 = values[rangeDifference(0,variables-1)];
                s2 = values[rangeDifference(0,variables-1)];
               }
               tempString1 = s1+s2;
               chk = (subsistAvailable(tempString1,possibleVariables) || subsistAvailable(s2+s1,possibleVariables) );
       		   if(chk == true)
               {
                  while(chk == true)
                    {
                    s1 = values[rangeDifference(0,variables-1)];
                    s2 = values[rangeDifference(0,variables-1)];
                   		while(s1.equalsIgnoreCase(s2))
               		      {
                   		   s1 = values[rangeDifference(0,variables-1)];
                    	   s2 = values[rangeDifference(0,variables-1)];
                       	  }
               		tempString1 = s1+s2;
                    chk = (subsistAvailable(tempString1,possibleVariables) || subsistAvailable(s2+s1,possibleVariables) );
           			}
                 }
          possibleVariables.add(tempString1);
          createConstraintsList.add(s1+","+s2);
          for(int h=0; h < incompatibleConstraints; h++)
          {
            tempString = Integer.toString(rangeDifference(0,domainSize-1)) + ","+Integer.toString(rangeDifference(0,domainSize-1));
            chk = subsistAvailable(tempString,possibleConstraints);
            if(chk == true)
             {
                while(chk == true)
                   {
                	tempString = Integer.toString(rangeDifference(0,domainSize-1)) + ","+
                	Integer.toString(rangeDifference(0,domainSize-1));
                    chk = subsistAvailable(tempString,possibleConstraints);
                    }
             }
             possibleConstraints.add(tempString);
             tempString += "~";  
             res += tempString;
           }
           possibleConstraints.clear();
   		   res = res.substring(0,res.length()-1);
           createIncompatibleList.add(res);
           res = "";
        }
    }

    public void createDomainValues()
    {
        for(int i=0; i < domainSize; i++)
        {
         domainValues.add(i);
        }
    }

    public void printArray(String[] var)
    {
 	     if(var.length > 0)
  	     {
 	       for(int i=0; i< var.length; i++)
 	          {
 	 	         System.out.println("Key: "+i+" Value :"+var[i]);
 	          }
	     }
 	     else
   	     {
           System.out.println("--- Empty Array ---"); 
	     }
    }

    public boolean subsistAvailable(String temp,ArrayList<String> values)
    {
        boolean status = false;
        if( values.isEmpty()==false)
         { 
 	     for(int i=0; i < values.size(); i++)
           {
 	       if(values.get(i).equalsIgnoreCase(temp))
             {  
 	    	   status = true;  
 	         }
           }
        }
        return status;
    }
  
    public void printParameters()      // Displays the input value given by the user
    {
       System.out.println("Number of variables (n)    : "+variables);
       System.out.println("Constraint Tightness (p)   : "+constraintTightness);
       System.out.println("Constant alpha             : "+alpha);
       System.out.println("Constant r                 : "+rConstant);
       System.out.println("\n----------- Generated RB Instance -----------\n");  // Displays the value of RB instance from user input
       System.out.println("Domain size (n^alpha)      : "+domainSize);
       System.out.println("No. of Constraints (rnlnn) : "+compatibleConstraints);
       System.out.println("No. of Incompatible tuples : "+incompatibleConstraints+"\n");
 	
       String tempVar="Variables  : {";          // Displays the variables { }
        if(values[0] != null)
        {
	        for(int i=0; i < values.length; i++)
	           {
		         tempVar += values[i];
		         tempVar += ", ";
          	   }
        }
         tempVar = tempVar.substring(0, tempVar.length() -2);
         tempVar += "}";
         System.out.println(tempVar);
         tempVar = "Domain     : {";            // Display the domain { }
         if(domainValues.isEmpty()==false)
         {
   	       for(int i=0; i < domainValues.size(); i++)
   	       	   {
               tempVar += domainValues.get(i);
               tempVar += ", ";
               }
         }
         tempVar = tempVar.substring(0, tempVar.length() -2);
         tempVar += "}";
         System.out.println(tempVar);
    }
   
    public int rangeDifference(int min, int max)
    {
        int length = (max - min) + 1;  
        return (int)(Math.random() * length) + min;
    }   
}


class SolvingTechniques    // another sub-class 
{
    public static Integer[] checkItems(Integer[] tempArray1, Integer[] tempArray2)
    {
       Integer[] var1;
       ArrayList<Integer> newArray = new ArrayList();
       for(int i=0; i < tempArray1.length; i++)
       {
           if(verifyEquality(tempArray1[i], tempArray2) == false)
           {
               newArray.add(tempArray1[i]);
           }
       }
       var1 = new Integer[newArray.size()];
       if(!newArray.isEmpty())
       {
        for(int i=0; i < newArray.size(); i++)
        {  
        	var1[i] = newArray.get(i);   
        }
       }
       return var1;
    }

    public static Integer[] createListInteger(ArrayList<Integer> var)
    {
        int length = var.size();
        Integer[] arrayList = new Integer[length];
        for(int i=0; i < var.size(); i++)
        {
            arrayList[i] = var.get(i);
        }
        return arrayList;
    }        

    public static Integer[] createIntegerList(ArrayList<String> var)
    {
       int length = var.size();
       Integer[] newList = new Integer[length];
       for(int i=0; i < var.size(); i++)
       {
           newList[i] = Integer.parseInt(var.get(i));
       }
       return newList;
    }    

    public static String[] createList(ArrayList<Integer> var)
    {
        int length = var.size();
        String[] newList = new String[length];
        for(int i=0; i < var.size(); i++)
        {
            newList[i] = Integer.toString(var.get(i));
        }
        return newList;
    }

    public static String[] loadArray(ArrayList<String> var)
    {
        int status = var.size();
        String[] newList = new String[status];
        for(int i=0; i < var.size(); i++)
        {
            newList[i] = var.get(i);
        }
        return newList;
    }

    public static boolean checkArrays(Integer[] array1, Integer[] array2)
    {
        int counter=0;
        boolean status = false;        
        if(array1.length == array2.length)
        {
            for(int i=0; i < array1.length; i++)
            {
                if(verifyEquality(array1[i], array2) == false)
                {
                    counter++;
                }
            }
        }
        else
        {
            counter = 1;
        }
        if(counter < 1)
        {  status = true;  }
        
        return status;
    }

    public static boolean sameArray(String[] array1, String[] array2)
    {
    	int status=0;
        boolean check = false;
        if(array1.length == array2.length)
        {
            for(int i=0; i < array1.length; i++)
            {
                if(verifyStatus(array1[i], array2,0) == false)
                {
                    status++;
                }
            }
        }
        else
        {
            status = 1;
        }
        if(status < 1)
        {  check = true;  }
        return check;
    }    

    public static ArrayList joinList(ArrayList<Integer> firstList, ArrayList<Integer> secondList)
    {
        int tempValue = -1, nodeValue = -1;
        ArrayList<Integer> tempVar = new ArrayList();
        boolean status=false;
        if(firstList != null && firstList.size() > 0)
        {
            for(int i=0; i< firstList.size(); i++)
            {  
                    tempVar.add(firstList.get(i));
            }
        }
        if(secondList != null && secondList.size() > 0)
        {
            if(tempVar.size() > 0)
            {
            for(int i=0; i < secondList.size(); i++)
            {
                    nodeValue = secondList.get(i);
                     for(int y=0; y < tempVar.size(); y++)
                     {
                            if(nodeValue == tempVar.get(y))
                            {   
                                status = true; 
                            }
                     }
                     if(status == false)
                     { 
                         tempVar.add(nodeValue);   
                     }
                   status = false; 
                }
            }
            else
            {   tempVar = secondList; }
        }
        return tempVar;
    }

    public static int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;     
        return (int)(Math.random() * range) + min;
    }

    public static boolean verifyStatus(String item,String[] values,  int temp)
    {
        boolean status = false;
        if( values[0] != null )
        { 
            for(int i=0; i < values.length; i++)
            {
                switch(temp)
                {
                    case 0:
                    if(values[i].equalsIgnoreCase(item))
                    {  status = true;  }
                     break;
                    case 1:
                         if(values[i].compareTo(item) == 0)
                         {  status = true;  }
                     break;
                 }
            }
        }
        return status;
    }    

    public static boolean verifyEquality(Integer item, Integer[] vals)
    {
        boolean status = false;
        if( vals[0] != null )
        { 
            for(int i=0; i < vals.length; i++)
            {
                    if(vals[i].equals(item))
                    {  status = true;  }     
            }
        }
        return status;
    }
}

