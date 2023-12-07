public class Main {
    public int a = 10;
    public static void main(String[] args) {
        // create object
        Main obj = new Main();

        // call instance method
        obj.instanceMethod();

        // call static method
        staticMethod();

        // call local variable
        obj.localVariable();
    }

    // local variable
    public void localVariable() {
        int a = 10;
        System.out.println(a);
    }

    // instance method
    public void instanceMethod() {
        System.out.println("Instance method");
    }

    // static method
    public static void staticMethod() {
        System.out.println("Static method");
    }
}
