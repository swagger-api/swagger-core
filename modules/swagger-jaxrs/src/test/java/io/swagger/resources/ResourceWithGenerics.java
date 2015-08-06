package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.GenericListWrapper;
import io.swagger.models.GenericType;
import io.swagger.models.GenericTypeWithApiModel;
import io.swagger.models.TestEnum;
import io.swagger.models.duplicated.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Api
@Path("/generics")
public class ResourceWithGenerics {

    @POST
    @Path("/testIntegerContainers")
    @ApiOperation("Tests integer containers")
    public void testIntegerContainers(@QueryParam("set") @ApiParam(allowableValues = "1, 2, 3") Set<Integer> set,
                                      @QueryParam("list") List<Integer> list, @QueryParam("list2D") List<List<Integer>> list2D,
                                      @QueryParam("array") Integer[] array, @QueryParam("arrayP") int[] arrayP, @QueryParam("scalar") Integer scalar,
                                      @QueryParam("scalarP") int scalarP, @QueryParam("forced") @ApiParam(allowMultiple = true) int forced) {
    }

    @POST
    @Path("/testStringContainers")
    @ApiOperation("Tests string containers")
    public void testStringContainers(@QueryParam("set") @ApiParam(allowableValues = "1, 2, 3") Set<String> set,
                                     @QueryParam("list") List<String> list, @QueryParam("list2D") List<List<String>> list2D,
                                     @QueryParam("array") String[] array, @QueryParam("scalar") String scalar) {
    }

    @POST
    @Path("/testObjectContainers")
    @ApiOperation("Tests object containers")
    public void testObjectContainers(@QueryParam("set") Set<Tag> set, @QueryParam("list") List<Tag> list,
                                     @QueryParam("list2D") List<List<Tag>> list2D, @QueryParam("array") Tag[] array, @QueryParam("scalar") Tag scalar) {
    }

    @POST
    @Path("/testEnumContainers")
    @ApiOperation("Tests enumeration containers")
    public void testEnumContainers(@QueryParam("set") Set<TestEnum> set, @QueryParam("list") List<TestEnum> list,
                                   @QueryParam("list2D") List<List<TestEnum>> list2D, @QueryParam("array") TestEnum[] array,
                                   @QueryParam("scalar") TestEnum scalar) {
    }

    @POST
    @Path("/testStringsInBody")
    @ApiOperation("Tests string container as body parameter")
    public void testStringsInBody(List<String> list) {
    }

    @POST
    @Path("/testObjectsInBody")
    @ApiOperation("Tests object container as body parameter")
    public void testObjectsInBody(List<Tag> list) {
    }

    @POST
    @Path("/testEnumsInBody")
    @ApiOperation("Tests enumeration container as body parameter")
    public void testEnumsInBody(List<TestEnum> list) {
    }

    @POST
    @Path("/test2DInBody")
    @ApiOperation("Tests object container as body parameter")
    public void test2DInBody(List<List<Tag>> list) {
    }

    @ApiOperation(value = "Tests generic type")
    @POST
    @Path("/testGenericType")
    public void testGenericType(GenericType<String> type) {
    }

    @ApiOperation(value = "Tests generic type")
    @POST
    @Path("/testStringBasedGenericType")
    public void testStringBasedGenericType(GenericType<UUID> type) {
    }

    @ApiOperation(value = "Tests complex generic type")
    @POST
    @Path("/testComplexGenericType")
    public void testComplexGenericType(GenericType<GenericType<String>> type) {
    }

    @ApiOperation(value = "Tests renamed generic type")
    @POST
    @Path("/testRenamedGenericType")
    public void testRenamedGenericType(GenericTypeWithApiModel<GenericTypeWithApiModel<String>> type) {
    }

    @ApiOperation(value = "Tests generic result")
    @GET
    @Path("testGenericResult")
    public GenericListWrapper<Tag> testGenericResult() {
        return null;
    }
}
