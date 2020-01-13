//FileName -> Resolver.java
//Description -> Class to resolve the query
import java.util.*;

public class Resolver {

    private HashMap<String, ArrayList<String>> hmFinalKB = null;
    private ArrayList<String> kbStd=null;
    private ArrayList<String> itemsToRemove=new ArrayList<>();
    private HashMap<String, String> hmUnificationReversed=new HashMap<>();

    public Resolver(HashMap<String,ArrayList<String>> hmFinalKB, ArrayList<String> kbStd){
        this.hmFinalKB=hmFinalKB;
        this.kbStd=kbStd;
    }


    //Instance method to resolve the given query
    public boolean resolve(int cutOffDepth, Stack<String> stackQ){

        while(!stackQ.empty()){


            String query=stackQ.pop();

            query=Utilities.negate(query);

            String predicate="";
            int index=-1;
            int i=0;

            while(query.charAt(i)!='(')
            {
                predicate= predicate + query.charAt(i);
                i++;
            }
            index=i;


            //Arguments in the query! To be matched during unification
            String[] queryArgsArr=query.substring(index+1,query.length()-1).split(",");


            if(hmFinalKB.containsKey(predicate)){

                //Get all values for the given predicate

                ArrayList<String> al1= hmFinalKB.get(predicate);

                int j=0;
                while (j<al1.size()){

                    if(cutOffDepth>8000){
                        return false;
                    }

                    String s1=al1.get(j);

                    String[] splitKbSent = s1.split("\\|");

                    int lenSplitKbSent=splitKbSent.length;

                    ArrayList<String> al2= new ArrayList<>();

                    String matchStr="";

                    int kk=0;

                    while(kk<lenSplitKbSent){
                        String s2=splitKbSent[kk];
                        al2.add(s2);
                        if(s2.contains(predicate)) {
                            matchStr = s2;
                        }
                        kk+=1;
                    }

                    String argInMatchedPred="";

                    int k=0;
                    while(k<matchStr.length()){
                        if(matchStr.charAt(k)=='(')
                        {
                            k++;
                            while(matchStr.charAt(k)!=')')
                            {
                                argInMatchedPred= argInMatchedPred + matchStr.charAt(k);
                                k++;
                            }
                            break;
                        }

                        k++;
                    }

                    String[] argsToBeUnified= argInMatchedPred.split(",");


                    boolean unificationRes=Utilities.isUnify(queryArgsArr,argsToBeUnified);




                    if(unificationRes){
                        HashMap<String,String> hmUnification=new HashMap<String,String>();

                        int z=0;
                        while(z<queryArgsArr.length){
                            String queryArgs=queryArgsArr[z];
                            String kbValArgs=argsToBeUnified[z];

                            if(!hmUnification.containsKey(kbValArgs)) {
                                hmUnification.put(kbValArgs, queryArgs);
                            }
                            if(!hmUnificationReversed.containsKey(queryArgs)){
                                hmUnificationReversed.put(queryArgs,kbValArgs);
                            }

                            z+=1;
                        }

                        Stack<String> copystack=new Stack<String>();
                        String[] stackarray = stackQ.toArray(new String[stackQ.size()]);
                        ArrayList<String> stackarraylist = new ArrayList<String>(Arrays.asList(stackarray));


                        int v=0;
                        while (v<stackQ.size()){
                            copystack.push(stackarray[v]);
                            v+=1;
                        }


                        for (String currentkbele : al2) {


                            for (Map.Entry<String, String> stringStringEntry : hmUnification.entrySet()) {

                                if (currentkbele.contains((String) ((Map.Entry) stringStringEntry).getKey()))
                                    //Replace argument inside the () with value!!!
                                {
                                    String args=Utilities.getArguments(currentkbele);

                                    String pred=Utilities.getPredicate(currentkbele);
                                    //get arguments
                                    //replace args

                                    //append to currentkbele with ()
                                    args = args.replace((String) ((Map.Entry) stringStringEntry).getKey(), (String) ((Map.Entry) stringStringEntry).getValue());

                                    currentkbele=pred+"("+args+")";
                                }

                            }


                            String checking = "";

                            int f=0;
                            while (currentkbele.charAt(f) != '(') {
                                checking = checking + currentkbele.charAt(f) + "";
                                f++;
                            }




                            if(!checking.equals(predicate))
                            {


                                String temp = "";

                                temp=Utilities.negate(currentkbele);



                                int counter = 0;



                                //Use Entry Set

                                for (Iterator<String> itr = stackarraylist.iterator(); itr.hasNext(); ) {
                                    String str = itr.next();
                                    if (str.equals(temp)) {
                                        itr.remove();
                                        counter = 1;
                                    }
                                }
                                if (counter != 1) {
                                    stackarraylist.add(currentkbele);
                                }
                            }

                            //Iterate through the HashMap and if value contains predicate with arg, delete it!
                            else{
                                itemsToRemove.add(predicate+"("+argInMatchedPred+")");
                                if(cutOffDepth!=0) {
                                    //For query args, find
                                    StringBuilder sb=new StringBuilder();

                                    for(int s=0;s<queryArgsArr.length;s++)
                                    {
                                        if(hmUnificationReversed.containsKey(queryArgsArr[s])){
                                            sb.append(hmUnificationReversed.get(queryArgsArr[s]));
                                            if(s!=queryArgsArr.length-1) {
                                                sb.append(",");
                                            }
                                        }
                                    }
                                    String queryItemToRemove=Utilities.negate(predicate+"("+sb.toString()+")");
                                    itemsToRemove.add(queryItemToRemove);
                                }
                                hmFinalKB=KBHelperClass.makeFinalKBDuringResolution(kbStd,itemsToRemove);
                            }

                        }

                        Stack<String> fstk=new Stack<>();

                        for(int zz=0;zz<stackarraylist.size();zz++){
                            fstk.push(stackarraylist.get(zz));
                        }

                        boolean printing=false;

                        try {
                        printing = resolve(++cutOffDepth, fstk);

                        }
                        catch (StackOverflowError ex){
                            return false;
                        }
                        if(printing)
                        {
                            return true;
                        }

                    }
                    j+=1;
                }
                return false;
            }
            else{
                return false;
            }

        }
        //Contradiction, proves true!
        return true;
    }


}
