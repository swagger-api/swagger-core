package io.swagger.v3.core.oas.models.composition;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(allOf = Ticket3030Parent.class)
public class Ticket3030Child {
	public String childProperty;
}
