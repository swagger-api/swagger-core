package com.wordnik.swagger.jaxrs.apt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.sun.mirror.apt.*;
import com.sun.mirror.declaration.*;
import com.sun.mirror.type.AnnotationType;
import com.sun.mirror.type.TypeMirror;
import com.wordnik.swagger.annotations.ApiError;
import com.wordnik.swagger.annotations.ApiErrors;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Processor for JAX-RS classes
 * @author Heiko W. Rupp
 */
class ClassLevelProcessor implements AnnotationProcessor {

    private static final String JAVAX_WS_RS = "javax.ws.rs.";
    private static final String[] HTTP_METHODS = {"GET","PUT","POST","HEAD","DELETE","OPTIONS"};
    private static final String[] PARAM_SKIP_ANNOTATIONS = {"javax.ws.rs.core.UriInfo","javax.ws.rs.core.HttpHeaders","javax.servlet.http.HttpServletRequest","javax.ws.rs.core.Request"};
    private static final String API_OUT_XML = "rest-api-out.xml";
    private static final String AT_PATH = "javax.ws.rs.Path";

    private AnnotationProcessorEnvironment env;
    private String targetDir;

    public ClassLevelProcessor(AnnotationProcessorEnvironment env, String targetDir) {
        this.env = env;
        this.targetDir = targetDir;
    }

    @Override
    public void process() {

        Document doc;
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = documentBuilder.newDocument();
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Element root = doc.createElement("api");
        doc.appendChild(root);

        // Loop over all classes and find class level annotations we are interested in
        processClass(doc, root);

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2); // xml indent 2 spaces
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // do xml indent

            // We initialize here for String writing to also see the result on stdout TODO change later
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);

            String xmlString = result.getWriter().toString();
            System.out.println(xmlString);

            File f;
            if (targetDir!=null) {
                File td = new File(targetDir);
                if (!td.exists())
                    td.mkdirs();

                f = new File(td, API_OUT_XML);
            }
            else
                f = new File(API_OUT_XML);

            String path = f.getAbsolutePath();
            System.out.println("... writing to " + path);
            try {
                FileWriter fw = new FileWriter(f);
                fw.write(xmlString);
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();  // TODO: Customise this generated block
            }

        } catch (TransformerException e) {
            e.printStackTrace();  // TODO: Customise this generated block
        }

    }

    private void processClass(Document doc, Element root) {
        for (TypeDeclaration td : env.getSpecifiedTypeDeclarations()) {
            String basePath = getPathValue(td);
            if (basePath==null || basePath.isEmpty()) {
                System.out.println("No @Path found on " + td.getQualifiedName() + " - skipping");
                continue;
            }

            Element classElement = doc.createElement("class");
            String className = td.toString();
            classElement.setAttribute("name",className);
            classElement.setAttribute("path",basePath);
            String shortDescription = getValue(td, "com.wordnik.swagger.annotations.Api", "value");
            setOptionalAttribute(classElement, "shortDesc", shortDescription);
            String longDescription = getValue(td, "com.wordnik.swagger.annotations.Api", "description");
            setOptionalAttribute(classElement, "description", longDescription);

            root.appendChild(classElement);

            // Loop over the methods on this class
            processMethods(doc, td, classElement);
        }
    }


    private void processMethods(Document doc, TypeDeclaration td, Element classElement) {
        for (MethodDeclaration m : td.getMethods()) {
            String path = getPathValue(m);

            Element methodElement = doc.createElement("method");
            methodElement.setAttribute("path",path);
            classElement.appendChild(methodElement);
            methodElement.setAttribute("name", m.getSimpleName());
            String httpMethod = getHttpMethod(m.getAnnotationMirrors());
            methodElement.setAttribute("method",httpMethod);
            String description = getValue(m, ApiOperation.class.getName(), "value");
            setOptionalAttribute(methodElement,"description",description);
            String responseClass = getValue(m,ApiOperation.class.getName(), "responseClass");
            setOptionalAttribute(methodElement,"returnType",responseClass,m.getReturnType().toString());

            // Loop over the parameters
            processParams(doc, m, methodElement);

            processErrors(doc,m, methodElement);

        }
    }

    /**
     * Process the parameters of a method.
     * @param doc Xml Document to add the output to
     * @param m Method to look for parameters
     * @param parent The parent xml element to tack the results on
     */
    private void processParams(Document doc, MethodDeclaration m, Element parent) {
        for (ParameterDeclaration p : m.getParameters()) {
            TypeMirror t = p.getType();

            if (skipParamType(t))
                continue;
            Element element = doc.createElement("param");
            parent.appendChild(element);
            element.setAttribute("name",p.getSimpleName());
            String description = getValue(p, ApiParam.class.getName(), "value");
            setOptionalAttribute(element, "description", description);
            String required = getValue(p, ApiParam.class.getName(), "required");
            if (isPathParam(p)) // PathParams are always required
                required="true";

            setOptionalAttribute(element,"required",required,"false");
            String allowedValues = getValue(p, ApiParam.class.getName(), "allowableValues");
            setOptionalAttribute(element,"allowableValues",allowedValues,"all");

            element.setAttribute("type", t.toString());
        }
    }

    /**
     * Look at the ApiError(s) annotations and populate the output
     * @param doc XML Document to add
     * @param m MethodDeclaration to look at
     * @param parent The parent xml element to attach the result to
     */
    private void processErrors(Document doc, MethodDeclaration m, Element parent) {
        ApiError ae = m.getAnnotation(ApiError.class);
        processError(doc,ae,parent);
        ApiErrors aes = m.getAnnotation(ApiErrors.class);
        if (aes != null) {
            for (ApiError ae2 : aes.value()) {
                processError(doc,ae2,parent);
            }
        }
    }

    /**
     * Process a single @ApiError
     * @param doc XML Document to add
     * @param ae ApiError annotation to evaluate
     * @param parent Parent XML element to tack the ApiError data on
     */
    private void processError(Document doc, ApiError ae, Element parent) {
        if (ae==null)
            return;

        Element element = doc.createElement("error");
        parent.appendChild(element);
        element.setAttribute("code", String.valueOf(ae.code()));
        element.setAttribute("reason",ae.reason());
    }

    /**
     * Determine if the passed mirror belongs to an annotation that denotes a parameter to be skipped
     * @param t Type to analyze
     * @return True if the type matches the blacklist
     */
    private boolean skipParamType(TypeMirror t) {
        String name = t.toString();
        boolean skip=false;
        for (String toSkip : PARAM_SKIP_ANNOTATIONS) {
            if (toSkip.equals(name)) {
                skip=true;
                break;
            }
        }
        return skip;
    }

    /**
     * Get the value of the @Path annotation. This is stripped from leading and trailing slashed
     * @param m Declaration to look at
     * @return Path with leading and trailing slashes stripped or null on error
     */
    private String getPathValue(Declaration m) {
        String path = getValue(m,AT_PATH,"value");
        if (path==null)
            return null;

        if (path.startsWith("/"))
            path = path.substring(1);
        if (path.endsWith("/"))
            path = path.substring(0,path.length()-1);

        return path;
    }



    /**
     * Get the value of a certain annotation on a declaration.
     * Annotation is e.g. of the form <pre>@com.acme.Annot(value"xyz")</pre>.
     * @param declaration The declaration of class/method/param....
     * @param annotationName Fully qualified name of the annotation - com.acme.Annot in above example
     * @param param The param of the annotation to query - value in above example. The parameter name may end in ().
     * If parens are not provided, they will be attached internally.
     * @return The value of the param - or null it not found
     */
    private String getValue(Declaration declaration, String annotationName, String param) {

        if (!param.endsWith("()")) {
            param = param+"()";
        }

        Collection<AnnotationMirror> mirrors = declaration.getAnnotationMirrors();

        for (AnnotationMirror am : mirrors) {
            AnnotationType annotationType = am.getAnnotationType();
            AnnotationTypeDeclaration annotationTypeDeclaration = annotationType.getDeclaration();
            String qName = annotationTypeDeclaration.getQualifiedName();
            if (qName.equals(annotationName)) {
                // found the annotation, now get the parameter value
                for (AnnotationTypeElementDeclaration decl : am.getElementValues().keySet()) {
                    if (decl.toString().equals(param)) {
                        String s = am.getElementValues().get(decl).toString();
                        // remove quotes we get passed in
                        if (s.startsWith("\""))
                            s = s.substring(1);
                        if (s.endsWith("\""))
                            s = s.substring(0,s.length()-1);
                        return s;
                    }
                }
            }
        }
        return null;
    }

    private boolean isPathParam(Declaration decl) {
        return hasAnnotation(decl,"javax.ws.rs.PathParam");
    }

    /**
     * Check if a certain annoation is present or not
     * @param declaration Declaration to look at
     * @param annotationName Name of the annotation
     * @return true if found, false otherwise
     */
    private boolean hasAnnotation(Declaration declaration, String annotationName) {

        boolean found = false;

        Collection<AnnotationMirror> mirrors = declaration.getAnnotationMirrors();

        for (AnnotationMirror am : mirrors) {
            AnnotationType annotationType = am.getAnnotationType();
            AnnotationTypeDeclaration annotationTypeDeclaration = annotationType.getDeclaration();
            String qName = annotationTypeDeclaration.getQualifiedName();
            if (qName.equals(annotationName)) {
                found = true;
                break;
            }
        }
        return found;

    }


    /**
     * Determine the http method (@GET, @PUT etc.) from the list of annotations on the method
     * @param annotationMirrors mirrors for the method
     * @return The http method string or null if it can not be determined
     */
    private String getHttpMethod(Collection<AnnotationMirror> annotationMirrors) {
        for (AnnotationMirror am : annotationMirrors) {
            String qName = am.getAnnotationType().getDeclaration().getQualifiedName();
            if (qName.startsWith(JAVAX_WS_RS)) {
                qName = qName.substring(JAVAX_WS_RS.length());
//                System.out.println(" Checking qname " + qName);
                for (String name : HTTP_METHODS) {
                    if (qName.equals(name))
                        return name;
                }
            }
        }
        return null;
    }

    /**
     * Set the passed text as attribute name on the passed xmlElement if the text is not empty
     * @param xmlElement Element to set the attribute on
     * @param name The name of the attribute
     * @param text The text to set
     */
    private void setOptionalAttribute(Element xmlElement, String name, String text) {
        if (text!=null && !text.isEmpty()) {
            xmlElement.setAttribute(name, text);
        }
    }

    /**
     * Set the passed text as attribute name on the passed xmlElement if the text is not empty
     * @param xmlElement Element to set the attribute on
     * @param name The name of the attribute
     * @param text The text to set
     * @param defaultValue Value to set if text is null or empty
     */
    private void setOptionalAttribute(Element xmlElement, String name, String text,String defaultValue) {
        if (text!=null && !text.isEmpty()) {
            xmlElement.setAttribute(name, text);
        }
        else {
            xmlElement.setAttribute(name,defaultValue);
        }
    }

}
