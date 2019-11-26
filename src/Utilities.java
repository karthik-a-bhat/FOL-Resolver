//FileName -> Utilities.java
//Created By -> Karthik Anand Bhat
//Date -> 11/25/2019
//Description -> Helper Class with utility functions to help in creating the KB and Resolution

import java.util.HashMap;
import java.util.Map;

public class Utilities {


    //Static method to Negate a given string
    // A -> ~A
    //~A -> A
    //Returns negated string that is negated
    public static String negate(String s)
    {
        StringBuilder sb= new StringBuilder();

        if(s.charAt(0)=='~') {
            return s.substring(1);
        }
        else{
            sb.append("~");
            sb.append(s);
            return sb.toString();
        }

    }

    //Static method to get predicates
    //Returns predicate
    public static String getPredicate(String s){
        if(s==null) return s;
        int j=0;
        while(s.charAt(j)!='('){
            j++;
        }
        return s.substring(0,j);
    }


    //Static method to check if 2 string arrays can be unified
    //Returns true if the arrays can be unified, else false
    public static boolean isUnify(String[] fromQ, String[] fromKB)
    {
        if(fromKB.length!=fromQ.length) return false;

        int val=0;

        Map<String, String> map = new HashMap<>();

        int len=fromQ.length;
        int z=0;

        while(z<len)
        {
            String x=fromQ[z];
            String y=fromKB[z];

            if(x.charAt(0)>=97 && x.charAt(0)<=122 && y.charAt(0)>=97 && y.charAt(0)<=122)
            {
                val+=1;
                if(map.containsKey(x) && !map.get(x).equals(y))return false;
                map.put(x, y);

            }
            else if(x.charAt(0)>=97 && x.charAt(0)<=122 && y.charAt(0)>=65 && y.charAt(0)<=90) {
                val += 1;
                if(map.containsKey(x) && !map.get(x).equals(y))return false;
                map.put(x, y);
            }
            else if(x.charAt(0)>=65 && x.charAt(0)<=90 && y.charAt(0)>=97 && y.charAt(0)<=122) {
                val += 1;
                if(map.containsKey(y) && !map.get(y).equals(x))return false;
                map.put(y, x);
            }
            else if(x.equals(y)) {
                val += 1;
            }

            z=z+1;

        }
        return val == fromQ.length;
    }

    //Gets arguments of a given string.
    //Returns arguments as a String
    //A(x,y) -> "x,y"
    public static String getArguments(String s){

        String args="";

        int k=0;
        while(k<s.length()){
            if(s.charAt(k)=='(')
            {
                k++;
                while(s.charAt(k)!=')')
                {
                    args= args + s.charAt(k);
                    k++;
                }
                break;
            }

            k++;
        }

        return args;
    }


}
