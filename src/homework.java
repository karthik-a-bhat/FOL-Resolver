//FileName -> homework.java
//Description -> Java File for USC CSCI-561 Fall 2019 HW-3
//FOL Resolution for a given KB

import java.util.*;

public class homework {

    public static void main(String[] args){
        try {
            ArrayList<ArrayList<String>> retArr=null;
            retArr= FileUtilitiesClass.readFile();

            ArrayList<String> queries = retArr.get(0);
            //KB's that are created during the intermediate step
            ArrayList<String> kb = retArr.get(1);

            ArrayList<String> kbNew = KBHelperClass.processKB(kb);
            ArrayList<String> kbStd = KBHelperClass.standardizeKB(kbNew);

            HashMap<String,ArrayList<String>> hmFinalKB=KBHelperClass.makeFinalKB(kbStd);


            //Resolution starts!
            ArrayList<String> resultList = new ArrayList<>();


            for(String query: queries){

                if(query.contains("~")) {
                    query = query.substring(1);
                }
                else{
                    query="~"+query;
                }

                Stack<String> st1= new Stack<>();
                st1.push(query);

                //Object of Resolver
                Resolver robj = new Resolver(hmFinalKB);

                boolean result=robj.resolve(0,st1);

                if(result){
                    resultList.add("TRUE");
                }
                else{
                    resultList.add("FALSE");
                }

            }
            //Write file
            FileUtilitiesClass.writeFile(resultList);

        }
        catch (Throwable t){
            t.printStackTrace();
        }

    }

}
