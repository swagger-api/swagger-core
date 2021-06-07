package io.swagger.v3.plugin.annotator.swagger3.resource;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类上的描述-swagger3
 *
 * @author zhangzicheng
 * @version 1.0.0
 * @date 2021/03/01
 * @exception Exception
 * @throws Exception
 * @link Exception
 * @see Exception
 * @since 1.0.0
 */
@RestController
public class TestController implements BeanNameAware {

    /**
     * beanName
     */
    private String name;

    /**
     * 方法上的描述
     *
     * @param param 参数
     * @return 返回值
     */
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(@RequestParam String param) {
        return name + " say hello, " + param;
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }
}
