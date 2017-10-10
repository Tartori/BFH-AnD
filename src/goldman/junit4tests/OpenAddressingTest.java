package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.set.*;

public class OpenAddressingTest extends SetTest {

	public Collection<Comparable> createCollection() {
		return new OpenAddressing<Comparable>();
	}
	
	public Collection<Comparable> createCollection(int size) {
		return new OpenAddressing<Comparable>(size);
	}

	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}
}

