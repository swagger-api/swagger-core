package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        $id = "http://yourdomain.com/schemas/myschema.json",
        description = "this is model for testing OAS 3.1 resolving",
        $comment = "Random comment at schema level",
        types = {"object"}
)
public class ModelWithOAS31Stuff {

    private List<String> randomList;
    private Object status;
    private int intValue;
    private String text;
    private String encodedString;
    private Address address;
    private Client client;

    @ArraySchema(
            schema = @Schema(
                    types = { "string" },
                    description = "itemdescription",
                    title = "itemtitle"

            ),
            arraySchema = @Schema(description = "arraydescription", title = "arraytitle"),
            maxContains = 10,
            minContains = 1,
            contains = @Schema(
                    types = "string"
            ),
            unevaluatedItems = @Schema(
                    types = "number"
            ),
            prefixItems = {
                    @Schema(
                            types = "string"
                    )
            }
    )
    public List<String> getRandomList() {
        return randomList;
    }

    public void setRandomList(List<String> randomList) {
        this.randomList = randomList;
    }

    @Schema(types = {
            "string",
            "number"
    })
    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    @Schema(
            $anchor = "intValue",
            $comment = "comment at schema property level",
            exclusiveMaximumValue = 100,
            exclusiveMinimumValue = 1
    )
    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    @Schema(
            contentEncoding = "plan/text",
            contentMediaType = "base64"
    )
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Schema(
            contentMediaType = "application/jwt",
            contentSchema = MultipleBaseBean.class
    )
    public String getEncodedString() {
        return encodedString;
    }

    public void setEncodedString(String encodedString) {
        this.encodedString = encodedString;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Schema(
            dependentSchemas = {
                    @StringToClassMapItem(
                            key = "creditCard",
                            value = CreditCard.class
                    )
            },
            patternProperties = {
                    @StringToClassMapItem(
                            key = "creditCard",
                            value = CreditCard.class
                    )
            },
            properties = {
                    @StringToClassMapItem(
                            key = "extraObject",
                            value = Object.class
                    )
            }
    )
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
