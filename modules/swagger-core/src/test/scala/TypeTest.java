import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.*;

public class TypeTest {
	@Test
	public void testStringType() throws Exception {
		Class<?> cls = String.class;
		System.out.println(cls);
	}

	@Test
	public void testUserType() throws Exception {
		Class<?> cls = User.class;
		System.out.println(cls);
	}

	@Test
	public void testListType() throws Exception {
		Class<?> cls = List.class;
		System.out.println(cls);
	}

	@Test
	public void testArrayListType() throws Exception {
		List<User> userList = new ArrayList<User>();
		Class<?> cls = userList.getClass();
		System.out.println(cls);
	}

	@Test
	public void testTypeReference() throws Exception {
		TypeReference tr = new TypeReference<List<User>>(){};
		System.out.println(tr.getType());
		System.out.println(tr.getType().getClass());
	}
}
