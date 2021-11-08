package cn.nzcer.singleton;

/**
 * @project: JavaDesignPattern
 * @ClassName: Singleton
 * @author: nzcer
 * @creat: 2021/11/8 9:22
 */
public class Singleton {
    /**
     * 私有静态
     */
    private static Singleton instance;

    /**
     * 私有构造方法
     */
    private Singleton() {
        System.out.println("实例初始化");
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void doSomething() {
        System.out.println("do something...");
    }

}
