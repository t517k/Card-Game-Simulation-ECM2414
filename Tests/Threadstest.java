public class Threadstest extends Threads {
    public void run(){
        System.out.println("1");
    }
}
public class Test{
    public static void main(String[] args){
    Threadstest t1 = new Threadstest();
    t1.start();
    }
}

