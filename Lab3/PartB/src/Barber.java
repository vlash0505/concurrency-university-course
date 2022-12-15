public class Barber {

    public void cutHair() {
        System.out.println("Barber is cutting hair " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
