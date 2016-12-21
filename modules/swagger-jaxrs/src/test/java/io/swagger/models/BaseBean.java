package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;

/**
 * <p>Provides ...</p>
 * <p>
 * <p>Created on 25/08/2016 by willows_s</p>
 *
 * @author <a href="mailto:willows_s@iblocks.co.uk">willows_s</a>
 */
@JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({@JsonSubTypes.Type(value = Sub1Bean.class, name = "sub1")})
@ApiModel(description = "BaseBean", discriminator = "type", subTypes = {Sub1Bean.class})
public class BaseBean {
    public String type;
    public int a;
    public String b;
}
