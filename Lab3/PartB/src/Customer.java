public class Customer {

    public void enterBarbershop() {
        System.out.println("Customer entered the barbershop " + Thread.currentThread().getName());
    }

    public void getHaircut() {
        System.out.println("Customer is getting the haircut " + Thread.currentThread().getName());
    }

    public void leave() {
        System.out.println("Customer leaves the shop " + Thread.currentThread().getName());
    }
}
