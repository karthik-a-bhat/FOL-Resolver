//FileName -> KBHelperClass.java
//Description -> Helper class with methods to process and store the Knowledge Base



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KBHelperClass {


    //Static Method to standardize the KB
    public static ArrayList<String> standardizeKB(ArrayList<String> kbNew){
        HashMap<String, String> hmStd=null;

        ArrayList<String> kbStd = new ArrayList<>();

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


                    //Standardize the KB

                    if (args.contains(",")) {

                        String[] arglist = args.split(",");

                        for (String par1 : arglist) {

                            char[] par1Arr=par1.toCharArray();

                            if (par1Arr[0]>= 97 && par1Arr[0]<= 122) {
                                if (!hmStd.containsKey(par1)) {
                                    hmCounter=hmCounter+1;
                                    hmStd.put(par1, ("p" + hmCounter));
                                }
                            }
                        }
                    }
                    else {
                        if (args.charAt(0) >= 97 && args.charAt(0) <= 122) {
                            if (!hmStd.containsKey(args)) {
                                hmCounter=hmCounter+1;
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

        }

        return kbStd;

    }


    //Static method to process the KB by removing implication
    public static ArrayList<String> processKB(ArrayList<String> kb){

        ArrayList<String> kbNew=new ArrayList<>();


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

                        String s2=Utilities.negate(kbStmtarr2[j]);
                        sb2.append(s2);
                        if (j != kbStmtarr2.length - 1) {
                            sb2.append("|");
                        }

                    }
                    s1 = sb2.toString();

                    //Free up sb2
                    sb2.setLength(0);

                } else {
                    s1=Utilities.negate(s1);
                }
                kbStmt = s1 + sb1.toString();
                kbNew.add(kbStmt);

            } else {
                kbNew.add(kbStmt);
            }


        }
        return kbNew;
    }


    //Static method to make the final Hashed Knowledge Base
    public static HashMap<String,ArrayList<String>> makeFinalKB(ArrayList<String> kbStd){

        HashMap<String,ArrayList<String>> hmFinalKB = new HashMap<>();

        for(String s1: kbStd){

            //Get predicates. Put it in hmFinalKB(if not present), with key as the entire sentence where it appears. Diff K-V for ~ and not ~
            //Key = Take | value= sentence where Take comes.
            //Key = ~Take | value= sentence where ~Take comes

            if(s1.contains("|")){
                String[] strarr1 = s1.split("\\|");

                for(String s3 : strarr1){
                    ArrayList<String> al1= new ArrayList<>();
                    String s4=Utilities.getPredicate(s3);
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
                String s2=Utilities.getPredicate(s1);

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
        return hmFinalKB;

    }



}
