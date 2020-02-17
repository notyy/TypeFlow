# 类型流（TypeFlow）

Serverless(function as a service)正在蓬勃发展。由于其更高的资源利用效率和更低的运维成本，我认为Serverless会成为将来软件开发的默认部署平台。

Serverless的应用是有一个个输入输出非常简单明确的函数组成。以阿里云函数计算为例,类似如下代码：
```java
import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.PojoRequestHandler;

public class Multi2 implements PojoRequestHandler<Integer, Integer> {
    @Override
    public Integer handleRequest(Integer input, Context context) {
        return input * 2;
    }
}
```
整个应用都必须由这样的函数组成。每个函数可以独立部署、独立发布、互相调用或通过某些机制触发。

这种统一简单的接口带来了一个问题：没人知道怎么把一个复杂的业务应用拆解成这样的函数。而且天然和面向对象是不匹配的。因此目前大部分的Serverless平台上的代码示例都是比较简单的数据处理、简单数据流等。

因此我设计了**类型流**方法论及配套工具来填补这个空档。我的思路和当前alpha版本的视频介绍见[这里](https://zhuanlan.zhihu.com/p/94522501)

当前alpha版本的示例展示见[这里](https://github.com/notyy/scala_template_typeflow)

当前版本还在较早期阶段，目前的计划是尽快达到开源标准，开源出来请更多开发人员参与。也欢迎大家看了上面的两个介绍后尽量给与反馈。

