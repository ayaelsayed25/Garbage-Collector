import java.util.LinkedList;

public class heapObject implements Comparable<heapObject>{
    int id;
    int memoryStart;
    int memoryEnd;
    int size;
    boolean marked;
    boolean copied = false;
    LinkedList<heapObject> children;
    public heapObject(int id, int memoryStart, int memoryEnd){
        this.id = id;
        this.memoryStart = memoryStart;
        this.memoryEnd = memoryEnd;
        this.children = new LinkedList<>();
        this.marked = false;
        this.size = memoryEnd - memoryStart;
    }
    public void addChild(heapObject child){//add pointer to the current object
        children.add(child);
    }
    public void markChildren(){
        this.marked = true;
        for(int i = 0; i < children.size(); i++){
            if(!children.get(i).marked)
                children.get(i).markChildren();
        }
    }

    public Integer getMemoryStart() {
        return memoryStart;
    }

    @Override
    public int compareTo(heapObject o) {
        return this.getMemoryStart().compareTo(o.getMemoryStart());
    }

}
