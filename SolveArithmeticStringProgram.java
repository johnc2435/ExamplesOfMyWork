import java.util.*;

/*the goal of this program is ultimately to be able to carry out the operations contained in a string representing a mathematical expression with
parentheses, +'s, -'s, *'s, /'s and single digit integers, currently it works for most inputs. Redundant parenthesis may cause inaccurate answers such
as ((5+4))

Methods reiterate through arrays more times than necessary, iterations could be trimmed to produce a faster program

*/

public class SolveArithmeticStringProgram{
  
  public static void main(String[] args){
  
  System.out.println("Please enter a mathematical expression:");
  
  Scanner sc = new Scanner(System.in);
  String inputExpression = sc.next(); //cannot use spaces between characters in input, no testing to make sure expression is valid
  
  System.out.println("Answer = " + solveArithmeticString(inputExpression));
  
  
    
  }
  
  public static int solveArithmeticString (String str){
 
    
    StringBuffer simplifiedExpression = new StringBuffer(str); //this represents the expression as it's being solved
   
    int[] parenthesisDepthList; //this represents how deep in parenthesis each character of the expression is
    int i,j;
    String tempString;
    boolean isAnInt = false;
    
    //keep simplifying the expression by solving the subexpressions in the innermost loops
    //stop when the expression is an integer
    while(isAnInt == false){
      
      parenthesisDepthList = generateParenthesisDepthList(simplifiedExpression);
     int maxDepth = 0; //depth of deepest character(s)
     for(i = 0; i < simplifiedExpression.length(); i++){
      if(parenthesisDepthList[i] > maxDepth){
        maxDepth = parenthesisDepthList[i];     
      }
    }
    
    //look for sequences of repeating maxdepth entries in depthList, the corresponding characters  in the expression
    //are in the innermost parenthesis and constitute a complete expression
    i = 0;
    j = 0;
    while(i < simplifiedExpression.length() && j == 0){
      if(parenthesisDepthList[i] == maxDepth){
        j = i;
        while(j < simplifiedExpression.length() && parenthesisDepthList[j] == maxDepth){
          j++;
        }
        
        tempString = Integer.toString(solveArithmeticSubstring(simplifiedExpression.substring(i,j)));
        simplifiedExpression.delete(i, j);
        simplifiedExpression.insert(i,tempString);
        
        if((i + tempString.length()) < simplifiedExpression.length() && simplifiedExpression.charAt(i + tempString.length()) == ')'){
          simplifiedExpression.deleteCharAt(i+tempString.length());
          simplifiedExpression.deleteCharAt(i-1);
          
        }
      }
      
      else{
        i++;
      }
      
    }
    
      isAnInt = true;
      
      for(i = 1; i < simplifiedExpression.length(); i++){
      if(simplifiedExpression.charAt(i) == '(' | simplifiedExpression.charAt(i) == ')' | simplifiedExpression.charAt(i) == '+' |
        simplifiedExpression.charAt(i) == '-' | simplifiedExpression.charAt(i) == '*' | simplifiedExpression.charAt(i) == '/'){
        isAnInt = false;
      }
    }
      //if the simplified expression is now a single integer isAnInt will be true and the while loop will stop
    
    }
    
    return Integer.parseInt(simplifiedExpression.toString());
    
  }
  
  
  //this method is used to generate an array of integers which correspond to how deep in parenthesis
  //the corresponding element is in the string which represents the arithmetic expression
  public static int[] generateParenthesisDepthList(StringBuffer strbuff){
     
    Stack<Character> parenthesisDepth = new Stack<Character>();
    int[] parenthesisDepthList; // represents how deep in the parenthesis each character is
                                // parenthesis are represented by 0's    
    
    parenthesisDepthList = new int[strbuff.length()];
    
    for(int i = 0; i < strbuff.length(); i++){
      if(strbuff.charAt(i) == '('){
        parenthesisDepth.push('(');
        parenthesisDepthList[i] = -1;
      }
      else if(strbuff.charAt(i) == ')'){
        parenthesisDepth.pop();
        parenthesisDepthList[i] = -1;
      }
      
      else{
        parenthesisDepthList[i] = parenthesisDepth.size();
      }
   
    }
    return parenthesisDepthList;
    
  }
  
  
  //this method will solve a substring with no parenthesis
  public static int solveArithmeticSubstring(String subStr){
    int i = 0; //index
    int beginIntegerIndex; // beginning of an integer to parsed
    int endIntegerIndex; // beginning of an integer to parsed
    ArrayList<Integer> integerList = new ArrayList<Integer>(); //list of integers to be operated on
    Integer tempInteger; //placeholder
    ArrayList<Character> operatorList = new ArrayList<Character>(); //list of operators to be used
    int operatorIndex; 
    Character tempOperator; //placeholder
    
    //first, parse ints and operators into seperate arraylists to carry out operations with
    //i'm using wrapper classes for ints and characters because i don't think you can use arraylists
    //with primative types but I could be mistaken
    while(i < subStr.length()){
      beginIntegerIndex = i;
      while((i+1) < subStr.length() && subStr.charAt(i+1) != '+' && subStr.charAt(i+1) != '-' &&
            subStr.charAt(i+1) != '*' && subStr.charAt(i+1) != '/'){
        i++;
      }
      endIntegerIndex = i;
      operatorIndex = i + 1;
      i = i + 2;
      
      tempInteger = new Integer(subStr.substring(beginIntegerIndex, endIntegerIndex + 1));
      integerList.add(tempInteger);
      
      if(operatorIndex < subStr.length()){
      tempOperator = new Character(subStr.charAt(operatorIndex));
      operatorList.add(tempOperator);
      }
      
    }
      
    // find multiplication and division signs and carry them out
    
    i = 0;
    
    while(i < operatorList.size()){
      if(operatorList.get(i).charValue() == '*'){
        tempInteger = new Integer(integerList.get(i).intValue()*integerList.get(i+1).intValue());
        integerList.add(i,tempInteger);
        integerList.remove(i+1);
        integerList.remove(i+1);
        operatorList.remove(i);
      }
      else if(operatorList.get(i).charValue() == '/'){
        tempInteger = new Integer(integerList.get(i).intValue()/integerList.get(i+1).intValue());
        integerList.add(i,tempInteger);
        integerList.remove(i+1);
        integerList.remove(i+1);
        operatorList.remove(i);
      }
      else{
        i++;
      }
    }
    
    
    // only addition and subtraction signs remain, carry them out
    i = 0;
    
    
     while(i < operatorList.size()){
      if(operatorList.get(i).charValue() == '+'){
        tempInteger = new Integer(integerList.get(i).intValue()+integerList.get(i+1).intValue());
        integerList.add(i,tempInteger);
        integerList.remove(i+1);
        integerList.remove(i+1);
        operatorList.remove(i);
      }
      else if(operatorList.get(i).charValue() == '-'){
        tempInteger = new Integer(integerList.get(i).intValue()-integerList.get(i+1).intValue());
        integerList.add(i,tempInteger);
        integerList.remove(i+1);
        integerList.remove(i+1);
        operatorList.remove(i);
      }
    }   
      //the integerList has been reduced to one integer which is the result of the arithmetic expression
      //that is the substring
    return integerList.get(0).intValue();
    
  }
    
   
  
}