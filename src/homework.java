import java.io.*;
import java.util.*;

public class homework {


     private static int numberOfQueries;
     private static int numberOfStmtInkb;

     private static ArrayList<String> queries=new ArrayList<>();

     //KB's that are created during the intermediate step
     private static ArrayList<String> kb=new ArrayList<>();
     private static ArrayList<String> kbNew = new ArrayList<>();
     private static ArrayList<String> kbStd=new ArrayList<>();


     //Final KB
     private static HashMap<String,ArrayList<String>> hmFinalKB = new HashMap<>();



    private static void readFile() throws IOException{
        BufferedReader br = null;
        try{

            String line = null;
            List<String> contents = new ArrayList<String>();

            //Code to delete the file if exists!
            File out = new File("./output.txt");
            if(out.exists()){
                out.delete();
            }

            File input = new File("./src/input.txt");
            //Read all lines and insert into a array-list
            br = new BufferedReader(new FileReader(input));

            while ((line = br.readLine()) != null) {
                contents.add(line);
            }

            numberOfQueries=Integer.parseInt(contents.get(0));

            for(int i=0;i<numberOfQueries;i++){
                String s = contents.get(1+i).trim();
                s=s.replaceAll("\\s+","");
                queries.add(s);
            }

            numberOfStmtInkb=Integer.parseInt(contents.get(numberOfQueries+1));

            for(int i=0;i<numberOfStmtInkb;i++){
                String s=contents.get(2+numberOfQueries+i).trim();
                s=s.replaceAll("\\s+","");
                kb.add(s);
            }

        }
        catch (IOException ex){
            ex.printStackTrace();
            throw new IOException();
        }
        finally {
            try{
                if(br!=null) br.close();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private static void processKB(){

        for (String kbStmt : kb) {
            String[] kbStmtarr1 = null;
            String[] kbStmtarr2 = null;

            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();


            //If it contains implication
            if (kbStmt.contains("=>")) {
                kbStmtarr1 = kbStmt.split("=>");
                sb1.append("|").append(kbStmtarr1[1]);

                String s1 = kbStmtarr1[0];

                if (s1.contains("&")) {
                    kbStmtarr2 = s1.split("&");

                    for (int j = 0; j < kbStmtarr2.length; j++) {

                        String s2 = "~" + kbStmtarr2[j];
                        sb2.append(s2);
                        if (j != kbStmtarr2.length - 1) {
                            sb2.append("|");
                        }

                    }
                    s1 = sb2.toString();

                    //Free up sb2
                    sb2.setLength(0);

                } else {
                    s1 = "~" + s1;
                }
                kbStmt = s1 + sb1.toString();
                kbNew.add(kbStmt);

            } else {
                kbNew.add(kbStmt);
            }


        }


    }
    private static void standardizeKB(){

        HashMap<String, String> hmStd=null;
        char[] s1arr=null;
        String s2="";

        int hmCounter=0;

        for(String s1: kbNew) {
            s1arr = s1.toCharArray();

            hmStd = new HashMap<>();


            for (int i = 0; i < s1arr.length; i++) {

                String args = "";

                if (s1arr[i] == '(') {
                    i++;

                    while (s1arr[i] != ')') {
                        args = args + s1arr[i];
                        i++;
                    }

                    if (args.contains(",")) {

                        String[] arglist = args.split(",");

                        for (String par1 : arglist) {
                            if (par1.charAt(0) >= 'a' && par1.charAt(0) <= 'z') {
                                if (!hmStd.containsKey(par1)) {
                                    hmCounter++;
                                    hmStd.put(par1, ("p" + hmCounter));
                                }
                            }
                        }
                    }
                    else {
                        if (args.charAt(0) >= 'a' && args.charAt(0) <= 'z') {
                            if (!hmStd.containsKey(args)) {
                                hmCounter++;
                                hmStd.put(args,("p"+hmCounter));
                            }
                        }
                    }

                }

            }

            for (Map.Entry<String, String> entry : hmStd.entrySet()) {

                StringBuilder sb1= new StringBuilder();


                for(int j=0;j<s1arr.length;j++){

                    if(s1arr[j]=='(')
                    {
                        sb1.append(s1arr[j]);
                        j=j+1;
                        String argsToBeReplaced="";

                        while(s1arr[j]!=')')
                        {
                            argsToBeReplaced=argsToBeReplaced+s1arr[j];
                            j=j+1;
                        }


                        sb1.append(argsToBeReplaced.replace(entry.getKey(), entry.getValue()));
                        sb1.append(')');
                    }
                    else {
                        sb1.append(s1arr[j]);
                    }
                }
                s2= sb1.toString();

                s1arr=s2.toCharArray();
            }
            if(!s2.equals("")){
                kbStd.add(s2);
                s2="";
            }
            else{
                kbStd.add(s1);
            }
            //s2="";

        }

        //Call to make the Knowledge base!
        //TODO

    }
    private static String getPredicate(String s){
        if(s==null) return s;
        int j=0;
        while(s.charAt(j)!='('){
            j++;
        }
        return s.substring(0,j);
    }


    private static void makeFinalKB(){


        for(String s1: kbStd){

            //Get predicates. Put it in hmFinalKB(if not present), with key as the entire sentence where it appears. Diff K-V for ~ and not ~
            //Key = Take | value= sentence where Take comes.
            //Key = ~Take | value= sentence where ~Take comes

            if(s1.contains("|")){
                String[] strarr1 = s1.split("\\|");

                for(String s3 : strarr1){
                    ArrayList<String> al1= new ArrayList<>();
                    String s4=getPredicate(s3);
                    al1.add(s1);

                    if(hmFinalKB.containsKey(s4)){
                        al1=hmFinalKB.get(s4);
                        al1.add(s1);
                        hmFinalKB.put(s4,al1);
                    }
                    else{
                        hmFinalKB.put(s4,al1);
                    }

                }
            }
            //Single sentence
            else{
                //Get predicate
                String s2=getPredicate(s1);

                ArrayList<String> al1 = new ArrayList<>();
                al1.add(s1);

                if(hmFinalKB.containsKey(s2)){
                    al1=hmFinalKB.get(s2);
                    al1.add(s1);
                    hmFinalKB.put(s2,al1);
                }
                else{
                    hmFinalKB.put(s2,al1);
                }

            }



        }

    }

    public static String negate(String query)
    {
        if(query.contains("~"))
            return query.substring(1);
        else
            return "~"+query;
    }


    //Method to resolve and prove!
    //TODO
    private static boolean resolve(int cutOffDepth, Stack<String> stackQ){

        while(!stackQ.empty()){

            String query=stackQ.pop();

            query=negate(query);

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

                for(int j=0;j<al1.size();j++){

                    if(cutOffDepth>700){
                        return false;
                    }

                    String s1=al1.get(j);

                    String[] splitKbSent = s1.split("\\|");

                    ArrayList<String> al2= new ArrayList<>();

                    String matchStr="";

                    for(String s2:splitKbSent) {
                        al2.add(s2);
                        if(s2.contains(predicate))
                            matchStr=s2;
                    }

                    String argInMatchedPred="";

                    for(int k=0;k<matchStr.length();k++)
                    {
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
                    }


                    //To be unified and matched
                    String[] argsToBeUnified= argInMatchedPred.split(",");


                    boolean unificationRes=unification(queryArgsArr,argsToBeUnified);




                    if(unificationRes){
                        HashMap<String,String> hmUnification=new HashMap<String,String>();

                        for(int z=0;z<queryArgsArr.length;z++)
                        {
                            String queryArgs=queryArgsArr[z];
                            String kbValArgs=argsToBeUnified[z];

                            if(!hmUnification.containsKey(kbValArgs))
                                hmUnification.put(kbValArgs,queryArgs);

                        }

                        Stack<String> copystack=new Stack<String>();
                        String[] stackarray = stackQ.toArray(new String[stackQ.size()]);
                        ArrayList<String> stackarraylist = new ArrayList<String>(Arrays.asList(stackarray));

                        for(int si=0;si<stackQ.size();si++)
                        {
                            copystack.push(stackarray[si]);
                            // System.out.println("copystack ="+copystack);
                        }

                        for (String currentkbelement : al2) {
                            // System.out.println("current "+currentkbelement);
                            for (Map.Entry<String, String> stringStringEntry : hmUnification.entrySet()) {
                                Map.Entry pair = stringStringEntry;
                                if (currentkbelement.contains((String) pair.getKey()))
                                    currentkbelement = currentkbelement.replace((String) pair.getKey(), (String) pair.getValue());
                            }


                            //to avoid adding resolved predicates in stack eg.A(Alice)
                            String checking = "";
                            for (int f = 0; f < currentkbelement.length(); f++) {

                                while (currentkbelement.charAt(f) != '(') {
                                    checking += currentkbelement.charAt(f) + "";
                                    f++;
                                }
                                break;
                            }


                            if (!checking.equals(predicate))  //Whtever remain after resolution
                            {


                                String original = currentkbelement;
                                String temp = "";
                                if (original.contains("~"))
                                    temp = original.substring(1);
                                else
                                    temp = "~" + original;
                                int count = 0;


                                for (Iterator<String> iterator = stackarraylist.iterator(); iterator.hasNext(); ) {
                                    String string = iterator.next();
                                    if (string.equals(temp)) {
                                        // Remove the current element from the iterator and the list.
                                        iterator.remove();
                                        count = 1;
                                    }
                                }
                                if (count != 1)
                                    stackarraylist.add(original);

                            }

                        }
                        //  System.out.println("Im innide dfs with query stack "+querystack);
                        Stack<String> finalstack=new Stack<String>();
                        for(String z:stackarraylist)
                            finalstack.push(z);

                        boolean printing=resolve(++cutOffDepth,finalstack);

                        if(printing)
                        {
                            return true;
                        }

                    }

                }
                return false;
            }
            else{
                return false;
            }

        }
        return true;
    }

    public static boolean unification(String a[],String b[])
    {

        int counter=0;
        for(int i=0;i<a.length;i++)
        {
            String x=a[i]; //stack
            String y=b[i];   //kbstring

            if(x.charAt(0)>='a'&& x.charAt(0)<='z' && y.charAt(0)>='a' && y.charAt(0)<='z')
            {
                counter++;

            }
            else if(x.charAt(0)>='a'&& x.charAt(0)<='z' && y.charAt(0)>='A' && y.charAt(0)<='Z')
                counter++;
            else if(x.charAt(0)>='A'&& x.charAt(0)<='Z' && y.charAt(0)>='a' && y.charAt(0)<='z')
                counter++;
            else if(x.equals(y))
                counter++;


        }
        if(counter==a.length)
            return true;
        else
            return false;
    }




    //Function to write to a file!
    private static void writeFile(ArrayList<String> resultList){
        BufferedWriter bfwr=null;
        try {
            //Append = true as second argument
            bfwr=new BufferedWriter(new FileWriter(new File("output.txt")));

            //TODO check working!
            String listString = String.join("\n", resultList);

            bfwr.write(listString);
            bfwr.flush();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            if(bfwr!=null){
                try {
                    bfwr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args){
        try {
            readFile();
            processKB();
            standardizeKB();
            makeFinalKB();


            System.out.println("KB Created!");


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
                boolean result=resolve(0,st1);

                if(result){
                    resultList.add("TRUE");
                }
                else{
                    resultList.add("FALSE");
                }

            }
            //Write file
            writeFile(resultList);

        }
        catch (Throwable t){
            t.printStackTrace();
        }

    }




}
