//FileName -> Utilities.java
//Description -> Helper Class with utility functions to help in creating the KB and Resolution

public class Utilities {


    //Static method to Negate a given string with '~'
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
    public static String getPredicate(String s){
        if(s==null) return s;
        int j=0;
        while(s.charAt(j)!='('){
            j++;
        }
        return s.substring(0,j);
    }


    //Static method to check if 2 string arrays can be unified
    public static boolean isUnify(String[] fromQ, String[] fromKB)
    {

        int val=0;

        int len=fromQ.length;
        int z=0;

        while(z<len)
        {
            String x=fromQ[z];
            String y=fromKB[z];

            if(x.charAt(0)>=97 && x.charAt(0)<=122 && y.charAt(0)>=97 && y.charAt(0)<=122)
            {
                val+=1;

            }
            else if(x.charAt(0)>=97 && x.charAt(0)<=122 && y.charAt(0)>=65 && y.charAt(0)<=90) {
                val += 1;
            }
            else if(x.charAt(0)>=65 && x.charAt(0)<=90 && y.charAt(0)>=97 && y.charAt(0)<=122) {
                val += 1;
            }
            else if(x.equals(y)) {
                val += 1;
            }

            z=z+1;

        }
        return val == fromQ.length;
    }

}
