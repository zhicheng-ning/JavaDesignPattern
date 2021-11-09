# JavaDesignPattern

# 本项目为《Java设计模式及实践学习》的实例代码以及个人笔记

官方网站：https://www.oodesign.com/

## 面向对象的设计原则（Design Principles）

https://www.oodesign.com/design-principles.html

> 面向对象的设计原则也被称为 **SOLID** ，**SOLID** 原则包括单一职责原则、开闭原则、
> 里氏替换原则、接口隔离原则和依赖倒置原则。（ SOLID 分别是这五个设计原则的首字母 ）

### 单一职责原则（Single Responsibility Principle）

单一职责原则指出***软件模块应该只有一个被修改的理由***。

```
    这个原则指出，如果我们有两个理由改变一个类，我们必须将功能拆分为两个类。每个类将只处理一个职责，
    如果将来我们需要进行一项更改，我们将在处理它的类中进行更改。
    当我们需要对具有更多职责的类进行更改时，该更改可能会影响与该类的其他职责相关的其他功能。
    
    例如，我们将使用数据库来持久保存对象。假设对 Car 类添加方法来处理增、删、改、查的数据库操作。
    在这种情况下，Car 不仅会封装逻辑，还会封装数据库操作（两个职责是改变的两个原因）。
这将使得我们的类更难以维护和测试，因为代码是紧密耦合的。Car 类将取决于数据库，如果将来
想要更改和数据库系统，我们必须要更改 Car 代码，这可能会在 Car 逻辑中产生错误。
    相反，更改 Car 逻辑可能会在数据持久性中产生错误。
    解决方案是创建两个类：一个用于封装 Car 逻辑，另一个用于负责持久性。
```

```
例子：
假设我们需要一个对象来保存电子邮件消息。我们将使用下面示例中的IEmail接口。乍一看，一切都很好。仔细看一下，
我们可以看到IEmail接口和Email类有两个职责(更改的原因)。一个是在pop3或imap等电子邮件协议中使用该类。
如果必须支持其他协议，则应该以另一种方式序列化对象，并添加代码以支持新的协议。另一个是Content字段。即使
内容是字符串，我们也可能希望将来支持HTML或其他格式。
如果只保留一个类，每个责任的更改都可能影响另一个:
- 添加新协议将需要添加用于解析和序列化每种类型字段内容的代码。
- 添加新的内容类型(如html)使我们为实现的每个协议添加代码。
```

```java
// single responsibility principle - bad example

interface IEmail {
    public void setSender(String sender);

    public void setReceiver(String receiver);

    public void setContent(String content);
}

class Email implements IEmail {
    public void setSender(String sender) {
        // set sender; 
    }

    public void setReceiver(String receiver) {
        // set receiver; 
    }

    public void setContent(String content) {
        // set content; 
    }
}
```

```
我们可以创建一个名为 IContent 和 Content 的新接口和类来划分职责。对每个类只有一种责任可以让我们的设计更加灵活:
- 添加新协议只会在 Email 类中引起变化。
- 添加支持的新内容类型只会导致 Content 类中的更改
```

```java
// single responsibility principle - good example
interface IEmail {
    public void setSender(String sender);

    public void setReceiver(String receiver);
}

//interface Content {
//	public String getAsString(); // used for serialization
//}
interface IContent {
    public void setContent(IContent content);
}

class Email implements IEmail {
    public void setSender(String sender) {
        // set sender; 
    }

    public void setReceiver(String receiver) {
        // set receiver; 
    }
}

class Content implements IContent {
    public void setContent(IContent content) {
        // set content; 
    }
}
```

小结
> 单一责任原则是在应用程序的设计阶段识别类的一种好方法，它提醒您考虑类的所有发展方式。
> 只有当充分理解了应用程序应该如何工作的全貌时，才能实现良好的职责分离。

### 开闭原则（Open Close Principle）

开闭原则指出***模块、类和函数应该对扩展开放，对修改关闭***。

```
示例：
    我们必须想象：开发的软件正在构建一个复杂的结构，一旦我们完成了它的一部分，不应该再
修改它，而是应该在它的基础之上继续建设。软件开发也是一样的。一旦我们开发并测试了一个模块，
如果想要改变它，不仅要测试正在改变的功能，还要测试它负责的整个功能。这涉及许多额外的资源，
这些资源可能从一开始就没有估算过，也会带来额外风险。一个模块中的更改可能会影响其他模块或
整体上的功能。
    解决方案是：尝试在完成后保持模块不变，并通过继承和多态来添加新功能。开闭原则是最重要
的设计原则之一，是大多数设计模式的基础。  
```

```
例子：
下面是一个违反开闭原则的例子。它实现了一个图形编辑器，可以处理不同形状的绘制。很明显，它不遵循开放关闭原则，
因为必须为每个必须添加的新形状类修改 GraphicEditor 类。有几个缺点：
- 对于添加的每个新形状，应该重做 GraphicEditor 类的单元测试（因为他被修改了）
- 当添加一种新的形状时，添加他的时间会很长，因为添加它的开发人员应该了解 GraphicEditor 类的逻辑
- 添加新形状可能会以一种不希望的方式影响现有功能，即使新形状工作得很好

为了更易于理解，我们可以将 GraphicEditor 想象成一个大类，里面有很多功能，由许多开发人员编写和修改，
而 Shape 可能是一个只由一个开发人员实现的类。在这种情况下，允许在不改变 GraphicEditor 类的情况下
添加新形状将是一个很大的改进。
```

```java
// Open-Close Principle - Bad example
class GraphicEditor {

    public void drawShape(Shape s) {
        if (s.m_type == 1)
            drawRectangle(s);
        else if (s.m_type == 2)
            drawCircle(s);
    }

    public void drawCircle(Circle r) {
        //....
    }

    public void drawRectangle(Rectangle r) {
        //....
    }
}

class Shape {
    int m_type;
}

class Rectangle extends Shape {
    Rectangle() {
        super.m_type = 1;
    }
}

class Circle extends Shape {
    Circle() {
        super.m_type = 2;
    }
} 
```

```
下面是一个支持开闭原则的例子。在新的设计中，我们在 GraphicEditor 类中使用抽象的 draw() 方法绘制对象，同时在具体的形状对象中实现 draw() 方法。
使用开闭原则可以避免先前设计中的问题，因为 GraphicEditor 类在添加新形状类时不会被改变:
- 不需要单元测试。
- 不需要理解 GraphicEditor 类的源代码。
- 由于绘图代码被移到了具体的形状类中，因此在添加新功能时影响旧功能的风险降低了。
```

```java
// Open-Close Principle - Good example
class GraphicEditor {
    public void drawShape(Shape s) {
        s.draw();
    }
}

class Shape {
    abstract void draw();
}

class Rectangle extends Shape {
    public void draw() {
        // draw the rectangle
    }
}

class Circle extends Shape {
    public void draw() {
        // draw the circle
    }
}
```

小结
> 就像所有的原则一样，OCP 只是一个原则。灵活的设计需要花费额外的时间和精力，并且引入了新的抽象层次，增加了代码的复杂性。因此，这一原则应适用于那些最有可能改变的领域。
> 有许多设计模式可以帮助我们在不更改代码的情况下扩展代码。例如，Decorator 模式帮助我们遵循开闭原则。此外，工厂方法或 Observer 模式也可以用来设计易于更改的应用程序，
> 只需对现有代码进行最小的更改。

### 里氏替换原则（Liskov's Substitution Principle）

里氏替换原则指出我们***必须确保新的派生类只是扩展而不替换旧类的功能***。否则，新类在现有程序模块中使用时会产生不希望的效果。
即我们需要保证，***如果程序模块使用基类，则可以用派生类替换对基类的引用，而不会影响程序模块的功能***（或者说派生类型必须完全可替代其基类型）。

```
例子：
下面是违反 Likov 替换原则的经典示例。在示例中使用了 2 个类：矩形和方形。让我们假设在应用程序的某个地方使用了 Rectangle 对象。
我们扩展应用程序并添加 Square 类。square 类由工厂模式返回，基于某些条件，我们不知道将返回的确切对象类型。但我们知道它是一个矩形。
我们得到矩形对象，将宽度设置为 5，高度设置为 10 并获得面积。对于宽度为 5 和高度为 10 的矩形，面积应为 50。结果却是 100。
```

```java
// Violation of Likov's Substitution Principle
class Rectangle {
    protected int m_width;
    protected int m_height;

    public void setWidth(int width) {
        m_width = width;
    }

    public void setHeight(int height) {
        m_height = height;
    }


    public int getWidth() {
        return m_width;
    }

    public int getHeight() {
        return m_height;
    }

    public int getArea() {
        return m_width * m_height;
    }
}

class Square extends Rectangle {
    public void setWidth(int width) {
        m_width = width;
        m_height = width;
    }

    public void setHeight(int height) {
        m_width = height;
        m_height = height;
    }

}

class LspTest {
    private static Rectangle getNewRectangle() {
        // it can be an object returned by some factory ... 
        return new Square();
    }

    public static void main(String args[]) {
        Rectangle r = LspTest.getNewRectangle();

        r.setWidth(5);
        r.setHeight(10);
        // user knows that r it's a rectangle. 
        // It assumes that he's able to set the width and height as for the base class

        System.out.println(r.getArea());
        // now he's surprised to see that the area is 100 instead of 50.
    }
}
```

小结
> 这个原则只是开闭原则的扩展，它意味着我们必须确保新的派生类在扩展基类的同时不改变基类的行为。

### 接口隔离原则（Interface Segregation Principle）

接口隔离原则指出***不应该强迫客户机实现它们不使用的接口***。 不是一个胖接口，而是基于一组方法的许多小接口，每个方法服务于一个子模块。

```
例子：
下面是一个违反接口隔离原则的例子。我们有一个 Manager 类，代表管理工人的人。我们有两种类型的工人，有些是普通工人，有些是非常高效的工人。
这两种类型的工人都在工作，他们每天都需要休息时间来吃饭。但是现在一些机器人也进入了他们工作的公司，但他们不吃东西，所以他们不需要休息时间。
方面，新的 Robot 类需要实现 IWorker 接口，因为机器人可以工作。另一方面，他们不必实现 IWorker 接口，因为他们不吃东西。
这就是为什么在这种情况下 IWorker 被认为是受污染的接口。如果我们保持目前的设计，新的 Robot 类将被迫实现 eat 方法。
根据接口隔离原则，灵活的设计不会有污染的接口。在我们的例子中，IWorker 接口应该分成 2 个不同的接口。
```

```java
// interface segregation principle - bad example
interface IWorker {
    public void work();

    public void eat();
}

class Worker implements IWorker {
    public void work() {
        // ....working
    }

    public void eat() {
        // ...... eating in launch break
    }
}

class SuperWorker implements IWorker {
    public void work() {
        //.... working much more
    }

    public void eat() {
        //.... eating in launch break
    }
}

class Manager {
    IWorker worker;

    public void setWorker(IWorker w) {
        worker = w;
    }

    public void manage() {
        worker.work();
    }
}
```

```
下面是支持接口隔离原则的代码。通过将 IWorker 接口拆分为 2 个不同的接口，新的 Robot 类不再被迫实现 eat 方法。
此外，如果我们需要机器人的另一个功能，比如充电，我们可以创建另一个接口 IRechargeble 与方法 recharge 。
```

```java
// interface segregation principle - good example
interface IWorker extends Feedable, Workable {
}

interface IWorkable {
    public void work();
}

interface IFeedable {
    public void eat();
}

class Worker implements IWorkable, IFeedable {
    public void work() {
        // ....working
    }

    public void eat() {
        //.... eating in launch break
    }
}

class Robot implements IWorkable {
    public void work() {
        // ....working
    }
}

class SuperWorker implements IWorkable, IFeedable {
    public void work() {
        //.... working much more
    }

    public void eat() {
        //.... eating in launch break
    }
}

class Manager {
    Workable worker;

    public void setWorker(Workable w) {
        worker = w;
    }

    public void manage() {
        worker.work();
    }
}
```

小结：
> 如果已经完成了设计，则可以使用 Adapter 模式分隔胖接口。 与所有原则一样，接口隔离原则也需要在设计期间花费额外的时间和精力来应用它，
> 并增加代码的复杂性。但它的设计很灵活。如果我们过多地应用它，就会导致代码包含许多接口和单个方法 ,因此，应用应该基于经验和常识来确定
> 哪些领域在未来更有可能发生代码扩展

### 依赖倒置原则（Dependency Inversion Principle）

依赖倒置原则指出***高级模块不应该依赖于低级模块。两者都应该依赖于抽象。***
***抽象不应该依赖于细节。细节应该依赖于抽象***。即应该将高级模块和低级模块 分开，让他们都依赖于抽象进而减少二者之间的依赖关系，如此就可以替换或扩展其中 任何一个模块而不影响其他模块。

```
例子：
下面是一个违背依赖倒置原则的例子。我们有一个 Manager 类，它是一个高级类，还有一个低级类，叫做 Worker。
我们需要向应用程序添加一个新模块，以模拟公司结构的变化，这些变化是由雇用新的专业工人决定的。为此我们创建
了一个新的类 SuperWorker 。
让我们假设 Manager 类相当复杂，包含非常复杂的逻辑。现在我们必须改变它，以引入新的 SuperWorker 。让我们
看看缺点:
- 我们必须更改 Manager 类（记住，这是一个复杂的类，需要花费时间和精力进行更改）
- Manager 类中的某些方法可能会受到影响
- 重新进行单元测试
所有这些问题都需要花费大量的时间来解决，并且可能会在旧的函数中引发新的错误。如果应用程序是按照依赖倒置原则设计的，
那么情况就不同了。这意味着我们设计了 Manager 类、IWorker 接口和实现 IWorker 接口的 Worker 类。当我们需要
添加 SuperWorker 类时，我们所要做的就是为它实现 IWorker 接口。在现有类中没有额外的更改。
```

```java
// Dependency Inversion Principle - Bad example

class Worker {

    public void work() {

        // ....working

    }

}


class Manager {

    Worker worker;


    public void setWorker(Worker w) {
        worker = w;
    }

    public void manage() {
        worker.work();
    }
}

class SuperWorker {
    public void work() {
        //.... working much more
    }
}
```

```
下面是支持依赖倒置原则的代码。在这个新的设计中，通过IWorker接口添加了一个新的抽象层。
现在上面代码的问题已经解决了(考虑到高级逻辑没有变化):
- 当添加 SuperWorker 类时，Manager 类不需要更改
- 因为我们不需要更改 Manager 类中的旧功能，所以降低了影响旧功能的风险
- 不需要为 Manager 类重新做单元测试
```

```java
// Dependency Inversion Principle - Good example
interface IWorker {
    public void work();
}

class Worker implements IWorker {
    public void work() {
        // ....working
    }
}

class SuperWorker implements IWorker {
    public void work() {
        //.... working much more
    }
}

class Manager {
    IWorker worker;

    public void setWorker(IWorker w) {
        worker = w;
    }

    public void manage() {
        worker.work();
    }
}
```

小结
> 当应用这一原则时，意味着高级类不会直接与低级类一起工作，它们使用接口作为抽象层。在这种情况下，
> 不能使用 new 操作符在高级类中实例化新的低级对象(如果需要的话)。相反，可以使用一些创造性设计
> 模式，如工厂方法、抽象工厂、原型。模板设计模式是应用 DIP 原则的一个例子。
>
> 当然，使用这个原则意味着更多的工作，将导致维护更多的类和接口，简单地说，代码更复杂，但更灵活。
> 这个原则不应该盲目地应用于每个类或每个模块。如果我们有一个类功能在将来更有可能保持不变，那么就不需要应用这个原则。
