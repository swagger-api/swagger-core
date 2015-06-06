package io.swagger.model;

import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;

/**
 * legacy support for swagger 1.2 deployments
 **/

@Deprecated
public class ApiInfo extends Info {
    public ApiInfo() {
    }

    public ApiInfo(
            String title,
            String description,
            String tosURL,
            String contactEmail,
            String licenseName,
            String licenseUrl) {

        setTitle(title);
        setDescription(description);
        setTermsOfService(tosURL);
        setContact(new Contact().name(contactEmail));
        setLicense(new License().name(licenseName).url(licenseUrl));
    }
}