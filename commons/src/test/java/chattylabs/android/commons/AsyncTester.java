package chattylabs.android.commons;

public class AsyncTester {
    private Thread thread;
    private Throwable exc;

    public AsyncTester(final Runnable runnable){
        thread = new Thread(() -> {
            try{
                runnable.run();
            }catch(Throwable e){
                exc = e;
            }
        });
    }

    public void start(){
        thread.start();
    }

    public void test() throws InterruptedException{
        thread.join();
        if (exc != null) {
            if (exc instanceof RuntimeException) {
                throw new AssertionError(exc.getMessage());
            } else throw ((AssertionError) exc);
        }
    }
}
