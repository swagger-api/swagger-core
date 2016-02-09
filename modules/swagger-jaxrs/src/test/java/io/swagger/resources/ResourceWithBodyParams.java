package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.duplicated.Tag;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Api
@Path("/")
public class ResourceWithBodyParams {

    @POST
    @Path("/testString")
    @ApiOperation("Tests string parameter without annotation")
    public void testString(String input) {
    }

    @POST
    @Path("/testApiString")
    @ApiOperation("Tests string parameter with annotation")
    public void testApiString(@ApiParam(name = "input", value = "String parameter") String input) {
    }

    @POST
    @Path("/testObject")
    @ApiOperation("Tests object parameter without annotation")
    public void testObject(Tag input) {
    }

    @POST
    @Path("/testApiObject")
    @ApiOperation("Tests object parameter with annotation")
    public void testApiObject(@ApiParam(name = "input", value = "Object parameter") Tag input) {
    }

    @POST
    @Path("/testBoolean")
    @ApiOperation("Tests parameter of the boolean type")
    public void testPrimitiveBoolean(boolean input) {
    }

    @POST
    @Path("/testBooleanArray")
    @ApiOperation("Tests parameter of the boolean type")
    public void testPrimitiveBooleans(boolean[] input) {
    }

    @POST
    @Path("/testChar")
    @ApiOperation("Tests parameter of the char type")
    public void testPrimitiveChar(char input) {
    }

    @POST
    @Path("/testCharArray")
    @ApiOperation("Tests parameter of the char type")
    public void testPrimitiveChars(char[] input) {
    }

    @POST
    @Path("/testByte")
    @ApiOperation("Tests parameter of the byte type")
    public void testPrimitiveByte(byte input) {
    }

    @POST
    @Path("/testByteArray")
    @ApiOperation("Tests parameter of the byte type")
    public void testPrimitiveBytes(byte[] input) {
    }

    @POST
    @Path("/testShort")
    @ApiOperation("Tests parameter of the short type")
    public void testPrimitiveShort(@ApiParam(value = "a short input") short input) {
    }

    @POST
    @Path("/testShortArray")
    @ApiOperation("Tests parameter of the short type")
    public void testPrimitiveShorts(short[] input) {
    }

    @POST
    @Path("/testInt")
    @ApiOperation("Tests parameter of the int type")
    public void testPrimitiveInt(int input) {
    }

    @POST
    @Path("/testIntArray")
    @ApiOperation("Tests parameter of the int type")
    public void testPrimitiveInts(int[] input) {
    }

    @POST
    @Path("/testLong")
    @ApiOperation("Tests parameter of the long type")
    public void testPrimitiveLong(long input) {
    }

    @POST
    @Path("/testLongArray")
    @ApiOperation("Tests parameter of the long type")
    public void testPrimitiveLongs(long[] input) {
    }

    @POST
    @Path("/testFloat")
    @ApiOperation("Tests parameter of the float type")
    public void testPrimitiveFloat(float input) {
    }

    @POST
    @Path("/testFloatArray")
    @ApiOperation("Tests parameter of the float type")
    public void testPrimitiveFloats(float[] input) {
    }

    @POST
    @Path("/testDouble")
    @ApiOperation("Tests parameter of the double type")
    public void testPrimitiveDouble(double input) {
    }

    @POST
    @Path("/testDoubleArray")
    @ApiOperation("Tests parameter of the double type")
    public void testPrimitiveDoubles(double[] input) {
    }
}
