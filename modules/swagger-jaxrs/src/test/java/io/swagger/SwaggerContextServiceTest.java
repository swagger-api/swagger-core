package io.swagger;

import io.swagger.config.Scanner;
import io.swagger.jaxrs.config.AbstractScanner;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.DefaultJaxrsScanner;
import io.swagger.jaxrs.config.SwaggerContextService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.testng.Assert.assertEquals;

public class SwaggerContextServiceTest {

    ServletContext servletContext;
    ServletConfig servletConfig;
    Scanner beanConfigScanner1;
    Scanner beanConfigScanner2;
    Scanner jaxrsScanner;

    @BeforeMethod
    public void stubContext() {

        servletContext = mock(ServletContext.class);
        servletConfig = mock(ServletConfig.class);

        jaxrsScanner = new DefaultJaxrsScanner();

        BeanConfig bc = new BeanConfig();
        bc.setDescription("Bean Config test 1");
        beanConfigScanner1 = (Scanner) bc;

        bc = new BeanConfig();
        bc.setDescription("Bean Config test 2");
        beanConfigScanner2 = (Scanner) bc;

        when(servletContext.getAttribute(AbstractScanner.SCANNER_ID_DEFAULT)).thenReturn(jaxrsScanner);
        when(servletContext.getAttribute(AbstractScanner.SCANNER_ID_PREFIX + "test.1")).thenReturn(beanConfigScanner1);
        when(servletContext.getAttribute(AbstractScanner.SCANNER_ID_PREFIX + "test.2")).thenReturn(beanConfigScanner2);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletConfig.getInitParameter(AbstractScanner.SCANNER_ID_KEY)).thenReturn("test.1");

    }

    @Test(description = "should add servletContext attribute AbstractScanner.SCANNER_ID_PREFIX + \"test.1\"")
    public void initializeScannerWithInitParam() {

        new SwaggerContextService().withServletConfig(servletConfig).initScanner();

        verify(servletConfig, times(2)).getInitParameter(eq(AbstractScanner.SCANNER_ID_KEY));
        verify(servletContext, times(1)).setAttribute(eq(AbstractScanner.SCANNER_ID_PREFIX + "test.1"), any(Scanner.class));

    }

    @Test(description = "should call servletContext getAttribute (AbstractScanner.SCANNER_ID_PREFIX + \"test.1\") and read")
    public void getScannerWithInitParam() {

        Scanner scanner = new SwaggerContextService().withServletConfig(servletConfig).getScanner();
        assertEquals (beanConfigScanner1, scanner);

        verify(servletConfig, times(2)).getInitParameter(eq(AbstractScanner.SCANNER_ID_KEY));
        verify(servletContext, times(1)).getAttribute(eq(AbstractScanner.SCANNER_ID_PREFIX + "test.1"));
    }

}
