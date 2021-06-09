import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class mainGC {
    public Hashtable<Integer,heapObject> heap = new Hashtable<>();
    public ArrayList<Integer> stack = new ArrayList<>();
    public ArrayList<heapObject> IDs = new ArrayList<>();
    void readHeap(String path){//read the heap objects and store them in hashtable called heap key = id and value = object itself
        try
        {
            String line = "";
            String splitBy = ",";
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                line = removeUnwantedChars(line);
                String[] temp = line.split(splitBy);    // use comma as separator
                heapObject myObject = new heapObject(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
                IDs.add(myObject);
                heap.put(myObject.id,myObject);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    void readPointers(String path){//read the pointers file and update the linked list of every object in the heap
        try
        {
            String line = "";
            String splitBy = ",";
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                line = removeUnwantedChars(line);
                String[] temp = line.split(splitBy);    // use comma as separator
                heapObject parent = heap.get(Integer.parseInt(temp[0]));
                heapObject child = heap.get(Integer.parseInt(temp[1]));
                parent.addChild(child);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    void readRoot(String path){
        try
        {
            String line = "";
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                line = removeUnwantedChars(line);
                stack.add(Integer.parseInt(line));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    void markAndCompact(String path) throws IOException {//mark objects ,update addresses and write in given file
        for(int i = 0; i < stack.size(); i++){//loop to mark all used objects
            int id = stack.get(i);
            heapObject myObject = heap.get(id);
            if(!myObject.marked){//if not marked before then mark it and all children
                myObject.markChildren();
            }
        }
        int memoryPtr = 0;
        FileWriter csvWriter = new FileWriter(path);
        Collections.sort(IDs);
        System.out.println("");
        for (int i = 0; i < IDs.size(); i++){//loop to update addresses in memory and write in the given file
            heapObject temp = heap.get(IDs.get(i).id);
            if(temp.marked){
                temp.memoryStart = memoryPtr;
                memoryPtr = memoryPtr + temp.size;
                temp.memoryEnd = memoryPtr;
                memoryPtr++;
                csvWriter.append(Integer.toString(temp.id));
                csvWriter.append(",");
                csvWriter.append(Integer.toString(temp.memoryStart));
                csvWriter.append(",");
                csvWriter.append(Integer.toString(temp.memoryEnd));
                csvWriter.append("\n");
            }
        }
        csvWriter.flush();
        csvWriter.close();
    }
    public LinkedList<heapObject> copy()
    {
        LinkedList<heapObject> heapCopy = new LinkedList<heapObject>();
        int p=0 , start=0, end=0; //define pointers
        for(int i=0; i<stack.size(); i++) //get elements from root
        {
            int element = stack.get(i);
            heapObject obj = heap.get(element);
            if(i==0) start = 0;
            else start = end +1;
            end = start + obj.size;
            obj.copied = true; //if copied mark true so it is not copied again
            obj.memoryStart = start;
            obj.memoryEnd = end;
            heapCopy.add(obj);
        }
        for(p=0; p<heapCopy.size(); p++)
        {
            heapObject obj = heapCopy.get(p);
            LinkedList<heapObject> children = obj.children; //get every element children and copy them
            for(int j=0; j<children.size(); j++)
            {
                heapObject child = children.get(j);
                if(!child.copied)
                {
                    start = end +1;
                    end
                            = start + child.size;
                    child.copied = true;
                    child.memoryStart = start;
                    child.memoryEnd = end;
                    heapCopy.add(child);
                }
            }
        }
        return heapCopy;
    }
    public void writeInFile(String path, LinkedList<heapObject> heap) //write the new heap in a csv file
    {
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        for(int i=0; i<heap.size(); i++)
        {
            heapObject obj = heap.get(i);
            ArrayList<String> mylist = new ArrayList<String>();
            mylist.add(Integer.toString(obj.id));
            mylist.add(Integer.toString(obj.memoryStart));
            mylist.add(Integer.toString(obj.memoryEnd));
            list.add(mylist);
        }
        try {
            FileWriter csvWriter = new FileWriter(path);
            for(ArrayList<String> rowData : list)
            {
                csvWriter.append(String.join(",", rowData));
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void copyGC(String path)
    {
        LinkedList list = copy();
        writeInFile(path, list);
    }
    public String removeUnwantedChars(String str)
    {
        String newStr = "";
        for(int i=0; i<str.length(); i++)
        {
            char ch = str.charAt(i);
            if(Character.isDigit(ch) || ch == ',')
            {
                newStr = newStr + ch;
            }
        }
        return newStr;
    }
}
