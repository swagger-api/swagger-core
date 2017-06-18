package io.swagger.jaxrs2.annotations.info;

import io.swagger.oas.annotations.info.Contact;
import io.swagger.oas.annotations.info.Info;
import io.swagger.oas.annotations.info.License;

public class InfoTest {
    @Info(
            title = "the title",
            version = "0.0",
            description = "My API",
            license = @License(name = "Apache 2.0", url = "http://foo.bar"),
            contact = @Contact(url = "http://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
    )
    static class ClassWithInfoAnnotation {
        // do something here
    }
}
