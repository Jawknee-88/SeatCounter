package event;


import venue.Block;

/**
 * Created by Michael on 16/11/2014.
 */
//public class TestClass {

    public class TestClass {

    static class B extends TestClass {

        public void doSomething() {
            int alarms[] = new int[2];
            System.out.println("blah");
        }
    }

    private static double ANGLE;

    public void doSomething() {
        System.out.println("super");
    }
        public static void main(String[] args) {
            TestClass s = new GoodSpeak();
            ((Tone)s).up();

            TestClass tc = new B();
            tc.doSomething();

            String s1 = null;
            s1.concat("hhh");
            System.out.println(s1);

            if(true || false) {

            }
        }


}
class GoodSpeak extends TestClass implements Tone{
    public void up(){
        System.out.println("UP UP UP");
    } } interface Tone{
    void up(); }



