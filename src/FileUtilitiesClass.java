//FileName -> FileUtilitiesClass.java
//Description -> Helper Class to manage file I/O

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtilitiesClass {

    //Static method to take Input
    public static ArrayList<ArrayList<String>> readFile() throws IOException{

        ArrayList<String> queries= new ArrayList<>();
        ArrayList<String> kb = new ArrayList<>();

        ArrayList<ArrayList<String>> retArr= new ArrayList<>();


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

            int numberOfQueries = Integer.parseInt(contents.get(0));

            for(int i = 0; i< numberOfQueries; i++){
                String s = contents.get(1+i).trim();
                s=s.replaceAll("\\s+","");
                queries.add(s);
            }

            int numberOfStmtInkb = Integer.parseInt(contents.get(numberOfQueries + 1));

            for(int i = 0; i< numberOfStmtInkb; i++){
                String s=contents.get(2+ numberOfQueries +i).trim();
                s=s.replaceAll("\\s+","");
                kb.add(s);
            }

            retArr.add(queries);
            retArr.add(kb);

            return retArr;

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



    //Static method to write to file named 'output.txt'
    public static void writeFile(ArrayList<String> resultList){
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
}
