package io.swagger.v3.plugin.annotator.swagger3.resource;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类上的描述2-swagger3
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
@Api(tags = {"类上的描述2-swagger3"}, protocols = "http,https")
@RestController
public class TargetController implements BeanNameAware {

    /**
     * beanName
     */
    private String name;

    /**
     * 方法上的描述2
     *
     * @param param2 参数2
     * @return 返回值2
     */
    @Operation(summary = "方法上的描述2")
    @ApiResponse(description = "返回值2")
    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public String test(@Parameter(name = "param2", description = "参数2") @RequestParam String param2) {
        return name + " say hello, " + param2;
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }
}
