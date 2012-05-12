package controllers;

import java.util.*;

import play.mvc.*;
import play.data.*;
import play.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.StringWriter;

import models.*;

/**
 * Manage a database of computers
 */
public class BaseApiController extends Controller {
	static ObjectMapper mapper;
	static JavaRestResourceUtil ru = new JavaRestResourceUtil();
	
	static {
		mapper = new ObjectMapper();
		mapper.setSerializationConfig(mapper.getSerializationConfig().without(SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS));
	}
	
	public static Result JsonResponse(Object obj) {
		return JsonResponse(obj, 200);
	}

	public static Result JsonResponse(Object obj, int code) {
		StringWriter w = new StringWriter();
		try{
		mapper.writeValue(w, obj);
		}
		catch(Exception e){
			// TODO: handle proper return code
			e.printStackTrace();
		}

		response().setContentType("application/json");
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(w.toString());
	}
}