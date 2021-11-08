package cn.nzcer;

import cn.nzcer.singleton.Singleton;
import org.junit.Test;

/**
 * @project: JavaDesignPattern
 * @ClassName: SingletonTest
 * @author: nzcer
 * @creat: 2021/11/8 9:25
 */
public class SingletonTest {
    @Test
    public void test() {
        Singleton myInstance = Singleton.getInstance();
        myInstance.doSomething();
        myInstance.doSomething();
    }
}
