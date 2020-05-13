package test;

public class ThreadExplorer {
    public static void main(String[] args) {
        Thread t1 = new MyThread("t1");
        Thread t2 = new MyThread("t2");
        t1.start();

        t2.start();
    }

}

class MyThread extends Thread {
    private String name;

    public MyThread(String name) {
        this.name = name;
    }

    public void run() {
        for(;;) {
            System.out.println(name + ":hello world");
            yield();
        }

    }
}
