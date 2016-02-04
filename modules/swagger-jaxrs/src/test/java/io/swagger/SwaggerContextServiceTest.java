package io.swagger;

import io.swagger.config.Scanner;
import io.swagger.config.ScannerFactory;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.DefaultJaxrsScanner;
import io.swagger.jaxrs.config.SwaggerConfigLocator;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.jaxrs.config.SwaggerScannerLocator;
import io.swagger.jaxrs.config.WebXMLReader;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import static io.swagger.jaxrs.config.SwaggerContextService.CONFIG_ID_KEY;
import static io.swagger.jaxrs.config.SwaggerContextService.CONFIG_ID_PREFIX;
import static io.swagger.jaxrs.config.SwaggerContextService.CONTEXT_ID_KEY;
import static io.swagger.jaxrs.config.SwaggerContextService.SCANNER_ID_DEFAULT;
import static io.swagger.jaxrs.config.SwaggerContextService.SCANNER_ID_KEY;
import static io.swagger.jaxrs.config.SwaggerContextService.SCANNER_ID_PREFIX;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class SwaggerContextServiceTest {

    ServletContext servletContext1;
    ServletConfig servletConfig1;
    ServletContext servletContext2;
    ServletConfig servletConfig2;
    Scanner beanConfigScanner1;
    Scanner beanConfigScanner2;
    Scanner jaxrsScanner;

    @BeforeMethod
    void initMock() {
        servletContext1 = mock(ServletContext.class);
        servletConfig1 = mock(ServletConfig.class);
        servletContext2 = mock(ServletContext.class);
        servletConfig2 = mock(ServletConfig.class);

        jaxrsScanner = new DefaultJaxrsScanner();

        BeanConfig bc = new BeanConfig();
        bc.setDescription("Bean Config test 1");
        beanConfigScanner1 = (Scanner) bc;

        bc = new BeanConfig();
        bc.setDescription("Bean Config test 2");
        beanConfigScanner2 = (Scanner) bc;
    }

    void stubWithInitParam() {

        when(servletContext1.getAttribute(SCANNER_ID_PREFIX + "test.1")).thenReturn(beanConfigScanner1);
        when(servletContext2.getAttribute(SCANNER_ID_PREFIX + "test.2")).thenReturn(beanConfigScanner2);

        when(servletConfig1.getServletContext()).thenReturn(servletContext1);
        when(servletConfig2.getServletContext()).thenReturn(servletContext2);

        when(servletConfig1.getInitParameter(SCANNER_ID_KEY)).thenReturn("test.1");
        when(servletConfig2.getInitParameter(SCANNER_ID_KEY)).thenReturn("test.2");

        when(servletConfig1.getInitParameter(CONFIG_ID_KEY)).thenReturn("test.1");
        when(servletConfig2.getInitParameter(CONFIG_ID_KEY)).thenReturn("test.2");
    }

    void stubWithContextInitParam() {

        when(servletContext1.getAttribute(SCANNER_ID_PREFIX + "test.1")).thenReturn(beanConfigScanner1);
        when(servletContext2.getAttribute(SCANNER_ID_PREFIX + "test.2")).thenReturn(beanConfigScanner2);

        when(servletConfig1.getServletContext()).thenReturn(servletContext1);
        when(servletConfig2.getServletContext()).thenReturn(servletContext2);

        when(servletConfig1.getInitParameter(CONTEXT_ID_KEY)).thenReturn("test.1");
        when(servletConfig2.getInitParameter(CONTEXT_ID_KEY)).thenReturn("test.2");

    }

    void stubWithoutInitParam() {

        when(servletContext1.getAttribute(SCANNER_ID_DEFAULT)).thenReturn(jaxrsScanner);
        when(servletContext2.getAttribute(SCANNER_ID_DEFAULT)).thenReturn(jaxrsScanner);

        when(servletConfig1.getServletContext()).thenReturn(servletContext1);
        when(servletConfig2.getServletContext()).thenReturn(servletContext2);

    }

    @Test(description = "should add servletContext attributes SCANNER_ID_PREFIX + \"test.1/2")
    public void initializeScannerWithInitParam() {

        stubWithInitParam();

        new SwaggerContextService().withServletConfig(servletConfig1).initScanner();
        new SwaggerContextService().withServletConfig(servletConfig2).initScanner();

        verify(servletConfig1, times(2)).getInitParameter(eq(SCANNER_ID_KEY));
        verify(servletContext1, times(1)).setAttribute(eq(SCANNER_ID_PREFIX + "test.1"), any(Scanner.class));
        verify(servletContext2, never()).setAttribute(eq(SCANNER_ID_PREFIX + "test.1"), any(Scanner.class));

        verify(servletConfig2, times(2)).getInitParameter(eq(SCANNER_ID_KEY));
        verify(servletContext2, times(1)).setAttribute(eq(SCANNER_ID_PREFIX + "test.2"), any(Scanner.class));
        verify(servletContext1, never()).setAttribute(eq(SCANNER_ID_PREFIX + "test.2"), any(Scanner.class));

    }

    @Test(description = "should call servletContext getAttribute with param SCANNER_ID_PREFIX + \"test.1/2\"")
    public void getScannerWithInitParam() {

        stubWithInitParam();

        Scanner scanner1 = new SwaggerContextService().withServletConfig(servletConfig1).getScanner();
        Scanner scanner2 = new SwaggerContextService().withServletConfig(servletConfig2).getScanner();

        assertEquals(beanConfigScanner1, scanner1);
        assertEquals(beanConfigScanner2, scanner2);
        ;

        verify(servletConfig1, times(2)).getInitParameter(eq(SCANNER_ID_KEY));
        verify(servletContext1, times(1)).getAttribute(eq(SCANNER_ID_PREFIX + "test.1"));
        verify(servletContext2, never()).getAttribute(eq(SCANNER_ID_PREFIX + "test.1"));

        verify(servletConfig2, times(2)).getInitParameter(eq(SCANNER_ID_KEY));
        verify(servletContext2, times(1)).getAttribute(eq(SCANNER_ID_PREFIX + "test.2"));
        verify(servletContext1, never()).getAttribute(eq(SCANNER_ID_PREFIX + "test.2"));

    }

    @Test(description = "should add servletContext attributes SCANNER_ID_PREFIX + \"test.1/2")
    public void initializeScannerWithContextInitParam() {

        stubWithContextInitParam();

        new SwaggerContextService().withServletConfig(servletConfig1).initScanner();
        new SwaggerContextService().withServletConfig(servletConfig2).initScanner();

        verify(servletConfig1, times(1)).getInitParameter(eq(SCANNER_ID_KEY));
        verify(servletConfig1, times(2)).getInitParameter(eq(CONTEXT_ID_KEY));
        verify(servletContext1, times(1)).setAttribute(eq(SCANNER_ID_PREFIX + "test.1"), any(Scanner.class));
        verify(servletContext2, never()).setAttribute(eq(SCANNER_ID_PREFIX + "test.1"), any(Scanner.class));

        verify(servletConfig2, times(1)).getInitParameter(eq(SCANNER_ID_KEY));
        verify(servletConfig2, times(2)).getInitParameter(eq(CONTEXT_ID_KEY));
        verify(servletContext2, times(1)).setAttribute(eq(SCANNER_ID_PREFIX + "test.2"), any(Scanner.class));
        verify(servletContext1, never()).setAttribute(eq(SCANNER_ID_PREFIX + "test.2"), any(Scanner.class));

    }

    @Test(description = "should call servletContext getAttribute with param SCANNER_ID_PREFIX + \"test.1/2\"")
    public void getScannerWithContextInitParam() {

        stubWithContextInitParam();

        Scanner scanner1 = new SwaggerContextService().withServletConfig(servletConfig1).getScanner();
        Scanner scanner2 = new SwaggerContextService().withServletConfig(servletConfig2).getScanner();

        assertEquals(beanConfigScanner1, scanner1);
        assertEquals(beanConfigScanner2, scanner2);
        ;

        verify(servletConfig1, times(1)).getInitParameter(eq(SCANNER_ID_KEY));
        verify(servletConfig1, times(2)).getInitParameter(eq(CONTEXT_ID_KEY));
        verify(servletContext1, times(1)).getAttribute(eq(SCANNER_ID_PREFIX + "test.1"));
        verify(servletContext2, never()).getAttribute(eq(SCANNER_ID_PREFIX + "test.1"));

        verify(servletConfig2, times(1)).getInitParameter(eq(SCANNER_ID_KEY));
        verify(servletConfig2, times(2)).getInitParameter(eq(CONTEXT_ID_KEY));
        verify(servletContext2, times(1)).getAttribute(eq(SCANNER_ID_PREFIX + "test.2"));
        verify(servletContext1, never()).getAttribute(eq(SCANNER_ID_PREFIX + "test.2"));

    }

    @Test(description = "should add servletContext attributes SCANNER_ID_DEFAULT")
    public void initializeAndGetScannerWithoutInitParam() {

        stubWithoutInitParam();
        new SwaggerContextService().withServletConfig(servletConfig1).initScanner();
        new SwaggerContextService().withServletConfig(servletConfig2).initScanner();

        verify(servletConfig1, times(1)).getInitParameter(eq(SCANNER_ID_KEY));
        verify(servletContext1, times(1)).setAttribute(eq(SCANNER_ID_DEFAULT), any(Scanner.class));
        verify(servletContext1, never()).setAttribute(eq(SCANNER_ID_PREFIX + "test.1"), any(Scanner.class));
        verify(servletContext2, never()).setAttribute(eq(SCANNER_ID_PREFIX + "test.2"), any(Scanner.class));

        verify(servletConfig2, times(1)).getInitParameter(eq(SCANNER_ID_KEY));
        verify(servletContext2, times(1)).setAttribute(eq(SCANNER_ID_DEFAULT), any(Scanner.class));
        verify(servletContext2, never()).setAttribute(eq(SCANNER_ID_PREFIX + "test.2"), any(Scanner.class));
        verify(servletContext1, never()).setAttribute(eq(SCANNER_ID_PREFIX + "test.1"), any(Scanner.class));

    }

    @Test(description = "should add scanner to ScannerFactory")
    public void initializeAndGetScannerWithoutServletConfig() {

        new SwaggerContextService().initScanner();
        new SwaggerContextService().initScanner();

        verify(servletConfig1, never()).getInitParameter(eq(SCANNER_ID_KEY));
        verify(servletContext1, never()).setAttribute(eq(SCANNER_ID_DEFAULT), any(Scanner.class));
        verify(servletContext1, never()).setAttribute(eq(SCANNER_ID_PREFIX + "test.1"), any(Scanner.class));
        verify(servletContext2, never()).setAttribute(eq(SCANNER_ID_PREFIX + "test.2"), any(Scanner.class));

        verify(servletConfig2, never()).getInitParameter(eq(SCANNER_ID_KEY));
        verify(servletContext2, never()).setAttribute(eq(SCANNER_ID_DEFAULT), any(Scanner.class));
        verify(servletContext2, never()).setAttribute(eq(SCANNER_ID_PREFIX + "test.2"), any(Scanner.class));
        verify(servletContext1, never()).setAttribute(eq(SCANNER_ID_PREFIX + "test.1"), any(Scanner.class));

        assertNotNull(ScannerFactory.getScanner());
        assertEquals(new SwaggerContextService().getScanner(), SwaggerScannerLocator.getInstance().getScanner(SCANNER_ID_DEFAULT));

    }

    @Test(description = "should add SwaggerConfig to SwaggerConfigLocator map with key CONFIG_ID_PREFIX + \"test.1/2\"")
    public void initializeAndGetConfigWithInitParam() {

        stubWithInitParam();

        new SwaggerContextService().withServletConfig(servletConfig1).initConfig();
        new SwaggerContextService().withServletConfig(servletConfig2).initConfig();

        assertTrue(SwaggerConfigLocator.getInstance().getConfig(CONFIG_ID_PREFIX + "test.1") instanceof WebXMLReader);
        assertTrue(SwaggerConfigLocator.getInstance().getConfig(CONFIG_ID_PREFIX + "test.2") instanceof WebXMLReader);

        verify(servletConfig1, times(2)).getInitParameter(eq(CONFIG_ID_KEY));
        verify(servletConfig2, times(2)).getInitParameter(eq(CONFIG_ID_KEY));

    }

    @Test(description = "should add SwaggerConfig to SwaggerConfigLocator map with key CONFIG_ID_PREFIX + \"test.1/2\"")
    public void initializeAndGetConfigWithContextInitParam() {

        stubWithContextInitParam();

        new SwaggerContextService().withServletConfig(servletConfig1).initConfig();
        new SwaggerContextService().withServletConfig(servletConfig2).initConfig();

        assertTrue(SwaggerConfigLocator.getInstance().getConfig(CONFIG_ID_PREFIX + "test.1") instanceof WebXMLReader);
        assertTrue(SwaggerConfigLocator.getInstance().getConfig(CONFIG_ID_PREFIX + "test.2") instanceof WebXMLReader);

        verify(servletConfig1, times(2)).getInitParameter(eq(CONTEXT_ID_KEY));
        verify(servletConfig1, times(1)).getInitParameter(eq(CONFIG_ID_KEY));
        verify(servletConfig2, times(2)).getInitParameter(eq(CONTEXT_ID_KEY));
        verify(servletConfig2, times(1)).getInitParameter(eq(CONFIG_ID_KEY));

    }

    @Test(description = "should add SwaggerConfig to SwaggerConfigLocator map with key CONFIG_ID_DEFAULT")
    public void initializeAndGetConfigWithoutInitParam() {

        stubWithoutInitParam();

        new SwaggerContextService().withServletConfig(servletConfig1).initConfig();
        new SwaggerContextService().withServletConfig(servletConfig2).initConfig();

        verify(servletConfig1, times(1)).getInitParameter(eq(CONFIG_ID_KEY));
        verify(servletConfig2, times(1)).getInitParameter(eq(CONFIG_ID_KEY));

    }

    @Test(description = "should add SwaggerConfig to SwaggerConfigLocator map with key CONFIG_ID_DEFAULT")
    public void initializeAndGetConfigWithoutServletConfig() {

        new SwaggerContextService().initConfig();
        new SwaggerContextService().initConfig();

        verify(servletConfig1, never()).getInitParameter(eq(CONFIG_ID_KEY));
        verify(servletConfig2, never()).getInitParameter(eq(CONFIG_ID_KEY));

    }
}
