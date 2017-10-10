package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;

import goldman.collection.set.OpenAddressing;
import goldman.collection.tagged.set.Mapping;
import goldman.collection.tagged.set.OpenAddressingMapping;

public class OpenAddressingMappingTest extends MappingTest {


	public Mapping<Integer,String> createCollection() {
		return new OpenAddressingMapping<Integer,String>(OpenAddressing.DEFAULT_CAPACITY);
	}
	
	public Mapping<Integer,String> createCollection(int size) {
		return new OpenAddressingMapping<Integer,String>(size);
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}

}
