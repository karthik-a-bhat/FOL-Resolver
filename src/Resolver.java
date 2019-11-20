//FileName -> Resolver.java

import java.util.*;

public class Resolver {

    private HashMap<String, ArrayList<String>> hmFinalKB = null;

    public Resolver(HashMap<String,ArrayList<String>> hmFinalKB){
        this.hmFinalKB=hmFinalKB;
    }


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

                    if(cutOffDepth>700){
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

                            if(!hmUnification.containsKey(kbValArgs))
                                hmUnification.put(kbValArgs,queryArgs);


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


                        for (String currentkbelement : al2) {


                            for (Map.Entry<String, String> stringStringEntry : hmUnification.entrySet()) {
                                Map.Entry pair = stringStringEntry;

                                if (currentkbelement.contains((String) pair.getKey()))
                                    currentkbelement = currentkbelement.replace((String) pair.getKey(), (String) pair.getValue());


                            }


                            String checking = "";

                            int f=0;
                            while (currentkbelement.charAt(f) != '(') {
                                checking += currentkbelement.charAt(f) + "";
                                f++;
                            }




                            if (!checking.equals(predicate))
                            {


                                String strOri = currentkbelement;
                                String temp = "";
                                if (strOri.contains("~"))
                                    temp = strOri.substring(1);
                                else
                                    temp = "~" + strOri;
                                int count = 0;



                                //Use Entry Set
                                for (Iterator<String> iterator = stackarraylist.iterator(); iterator.hasNext(); ) {
                                    String string = iterator.next();
                                    if (string.equals(temp)) {

                                        iterator.remove();
                                        count = 1;
                                    }
                                }
                                if (count != 1)
                                    stackarraylist.add(strOri);

                            }

                        }

                        Stack<String> finalstack=new Stack<String>();
                        for(String zz:stackarraylist)
                            finalstack.push(zz);

                        boolean printing=resolve(++cutOffDepth,finalstack);

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
        return true;
    }


}
