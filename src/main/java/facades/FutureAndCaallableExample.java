package facades;

import java.util.Random;
import java.util.concurrent.*;


public class FutureAndCaallableExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService service = Executors.newCachedThreadPool();


            Future<Integer> future = service.submit(new Task());
            while(!future.isDone()){
                Thread.sleep(1000);
                System.out.println("Still working on it!");
            }
        System.out.println(future.get());
        service.shutdownNow();
        System.out.println("Thread name: " + Thread.currentThread().getName());
    }


    public static class Task implements Callable {

        @Override
        public Object call() throws Exception {
            Thread.sleep(1000);
            return new Random().nextInt();
        }
    }
}
