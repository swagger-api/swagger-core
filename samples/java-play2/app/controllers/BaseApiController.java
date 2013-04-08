package controllers;


import play.mvc.*;
import play.data.*;
import play.*;

import com.wordnik.swagger.core.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.io.StringWriter;

import models.*;

public class BaseApiController extends Controller {
	static JavaRestResourceUtil ru = new JavaRestResourceUtil();

	protected static ObjectMapper mapper = JsonUtil.getJsonMapper();
	
	public static Result JsonResponse(Object obj) {
		return JsonResponse(obj, 200);
	}

	public static Result JsonResponse(Object obj, int code) {
		StringWriter w = new StringWriter();
		try {
			mapper.writeValue(w, obj);
		} catch (Exception e) {
			// TODO: handle proper return code
			e.printStackTrace();
		}

		response().setContentType("application/json");
		response().setHeader("Access-Control-Allow-Origin", "*");
    response().setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
    response().setHeader("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization");

		return ok(w.toString());
	}
}