//FileName -> homework.java
//Created By -> Karthik Anand Bhat
//Date -> 11/25/2019
//Description -> Given a Knowledge Base and a query, the program will say if the KB entails the Query

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



            //Resolution starts!
             ArrayList<String> resultList = new ArrayList<>();


            for(String query: queries){

                query=Utilities.negate(query);

                Stack<String> st1= new Stack<>();
                st1.push(query);

                //Object of class Resolver
                Resolver robj = new Resolver(KBHelperClass.makeFinalKB(kbStd), kbStd);

                //Result of Resolution
                boolean result=robj.resolve(0,st1);

                if(result){
                    resultList.add("TRUE");
                }
                else{
                    resultList.add("FALSE");
                }

            }
            //Write to file
            FileUtilitiesClass.writeFile(resultList);


        }
        catch (Throwable t){
            t.printStackTrace();
        }

    }

}
