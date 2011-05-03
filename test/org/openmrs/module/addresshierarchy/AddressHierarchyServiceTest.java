package org.openmrs.module.addresshierarchy;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.addresshierarchy.service.AddressHierarchyService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;

public class AddressHierarchyServiceTest extends BaseModuleContextSensitiveTest {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	protected static final String XML_DATASET_PACKAGE_PATH = "org/openmrs/module/addresshierarchy/include/addressHierarchy-dataset.xml";
	
	@Before
	public void setupDatabase() throws Exception {
		initializeInMemoryDatabase();
		authenticate();
		executeDataSet(XML_DATASET_PACKAGE_PATH);
	}
	
	@Test
	@Verifies(value = "should get hierarchy level by id", method = "getAddressHierarchyLevel(int id)")
	public void getAddressHierarchyLevel_shouldGetAddressHierarchyLevelById() throws Exception {
		AddressHierarchyLevel level = Context.getService(AddressHierarchyService.class).getAddressHierarchyLevel(1);
		
		Assert.assertEquals("Country", level.getName());
		Assert.assertEquals("country", level.getAddressField().getName());
		
	}
	
	@Test
	@Verifies(value = "should get top level hierarchy", method = "getTopAddressHierarchyLevel(int id)")
	public void getTopAddressHierarchyLevel_shouldGetTopAddressHierarchyLevel() throws Exception {
		AddressHierarchyLevel level = Context.getService(AddressHierarchyService.class).getTopAddressHierarchyLevel();
		
		Assert.assertEquals("Country", level.getName());
		Assert.assertEquals("country", level.getAddressField().getName());
		
	}
	
	@Test
	@Verifies(value = "should get bottom level hierarchy", method = "getBottomAddressHierarchyLevel(int id)")
	public void getBottomAddressHierarchyLevel_shouldGetBottomAddressHierarchyLevel() throws Exception {
		AddressHierarchyLevel level = Context.getService(AddressHierarchyService.class).getBottomAddressHierarchyLevel();
		
		Assert.assertEquals("Neighborhood", level.getName());
		Assert.assertEquals("region", level.getAddressField().getName());
		
	}
	
	@Test
	@Verifies(value = "should get all address hierarchy levels", method = "getAddressHierarchyLevels()")
	public void getAddressHierarchyLevels_shouldGetAllAddressHierarchyLevels() throws Exception {
		
		AddressHierarchyService ahService = Context.getService(AddressHierarchyService.class);
		
		List<AddressHierarchyLevel> levels = ahService.getAddressHierarchyLevels();
		
		Assert.assertEquals(5, levels.size());
			
		// make sure that the list returned contains all the levels
		Assert.assertTrue(levels.contains(ahService.getAddressHierarchyLevel(1)));
		Assert.assertTrue(levels.contains(ahService.getAddressHierarchyLevel(4)));
		Assert.assertTrue(levels.contains(ahService.getAddressHierarchyLevel(2)));
		Assert.assertTrue(levels.contains(ahService.getAddressHierarchyLevel(5)));
		Assert.assertTrue(levels.contains(ahService.getAddressHierarchyLevel(3)));
	}
	
	@Test
	@Verifies(value = "should get all address hierarchy levels in order", method = "getOrderedAddressHierarchyLevels()")
	public void getOrderedAddressHierarchyLevels_shouldGetAllAddressHierarchyLevelsInOrder() throws Exception {
		
		AddressHierarchyService ahService = Context.getService(AddressHierarchyService.class);
		
		List<AddressHierarchyLevel> levels = ahService.getOrderedAddressHierarchyLevels();
		
		Assert.assertEquals(5, levels.size());
			
		// make sure that the list returns the levels in the proper order
		Assert.assertTrue(levels.get(0) == (ahService.getAddressHierarchyLevel(1)));
		Assert.assertTrue(levels.get(1) == (ahService.getAddressHierarchyLevel(4)));
		Assert.assertTrue(levels.get(2) == (ahService.getAddressHierarchyLevel(2)));
		Assert.assertTrue(levels.get(3) == (ahService.getAddressHierarchyLevel(5)));
		Assert.assertTrue(levels.get(4) == (ahService.getAddressHierarchyLevel(3)));
	}
	
	
	@Test
	@Verifies(value = "should fetch children address hierarchy entries", method = "getChildAddressHierarchyEntries(AddressHierarchyEngy entry)")
	public void getChildAddressHierarchyEntries_shouldGetChildAddressHierarchyEntries() throws Exception {
		AddressHierarchyService ahService = Context.getService(AddressHierarchyService.class);
		
		// fetch the children of "United States"
		List<AddressHierarchyEntry> entries = ahService.getChildAddressHierarchyEntries(ahService.getAddressHierarchyEntry(1));
		
		// make sure the result set has 2 entries, Maine and Massachusetts
		Assert.assertEquals(2, entries.size());
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(2)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(3)));
		
		// fetch the children of "Plymouth (Count)"
		entries = ahService.getChildAddressHierarchyEntries(ahService.getAddressHierarchyEntry(4));
		
		// make sure the result set has 2 entries, Maine and Massachusetts
		Assert.assertEquals(4, entries.size());
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(6)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(7)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(8)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(9)));
		
	}
	
	@Test
	@Verifies(value = "should fetch children address hierarchy entries by id", method = "getChildAddressHierarchyEntries(AddressHierarchyEngy entry)")
	public void getChildAddressHierarchyEntries_shouldGetChildAddressHierarchyEntriesById() throws Exception {
		AddressHierarchyService ahService = Context.getService(AddressHierarchyService.class);
		
		// fetch the children of "United States"
		List<AddressHierarchyEntry> entries = ahService.getChildAddressHierarchyEntries(ahService.getAddressHierarchyEntry(1).getId());
		
		// make sure the result set has 2 entries, Maine and Massachusetts
		Assert.assertEquals(2, entries.size());
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(2)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(3)));
		
		// fetch the children of "Plymouth (County)"
		entries = ahService.getChildAddressHierarchyEntries(ahService.getAddressHierarchyEntry(4).getId());
		
		Assert.assertEquals(4, entries.size());
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(6)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(7)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(8)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(9)));
		
	}
	
	@Test
	@Verifies(value = "should find all address hierarchy entries by level", method = "getAddressHierarchyEntriesByLevel(AddressHierarchyLevel)")
	public void getAddressHierarchyEntriesByLevel_shouldFindAllAddressHierarchyEntriesByLevel() throws Exception {
		AddressHierarchyService ahService = Context.getService(AddressHierarchyService.class);
		
		List<AddressHierarchyEntry> entries = ahService.getAddressHierarchyEntriesByLevel(ahService.getAddressHierarchyLevel(5));
		
		Assert.assertEquals(6, entries.size());
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(6)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(7)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(8)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(9)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(10)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(11)));
	}
	
	@Test
	@Verifies(value = "should find all address hierarchy entries by level id", method = "getAddressHierarchyEntriesByLevel(Integer)")
	public void getAddressHierarchyEntriesByLevel_shouldFindAllAddressHierarchyEntriesByLevelId() throws Exception {
		AddressHierarchyService ahService = Context.getService(AddressHierarchyService.class);
		
		List<AddressHierarchyEntry> entries = ahService.getAddressHierarchyEntriesByLevel(ahService.getAddressHierarchyLevel(5).getId());
		
		Assert.assertEquals(6, entries.size());
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(6)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(7)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(8)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(9)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(10)));
		Assert.assertTrue(entries.contains(ahService.getAddressHierarchyEntry(11)));
	}
	
	@Test
	@Verifies(value = "should find address hierarchy entry by id", method = "getAddressHierarchyEntry(int)")
	public void searchHierarchy_shouldFindAddressHierarchyEntryById() throws Exception {	
		AddressHierarchyService ahService = Context.getService(AddressHierarchyService.class);
		
		Assert.assertTrue(ahService.getAddressHierarchyEntry(3).getName().equals("Maine"));
		Assert.assertTrue(ahService.getAddressHierarchyEntry(5).getName().equals("Middlesex"));
		
	}
	
	@Test
	@Verifies(value = "should find appropriate address hierarchy entries", method = "searchHierarchy(String, int)")
	public void searchHierarchy_shouldFindAppropriateHierarchyEntries() throws Exception {
		AddressHierarchyService ahService = Context.getService(AddressHierarchyService.class);
		
		// first try a few basic searches
		Assert.assertTrue(ahService.searchHierarchy("Boston", -1).get(0).getName().equals("Boston"));
		Assert.assertTrue(ahService.searchHierarchy("Scituate", -1).get(0).getName().equals("Scituate"));
		
		// now make sure there is no match if the address hierarchy level id is wrong
		Assert.assertTrue(ahService.searchHierarchy("Boston", 4).size() == 0);
		
		// but make sure there is a match if the address hierarchy level id correct
		Assert.assertTrue(ahService.searchHierarchy("Scituate", 5).get(0).getName().equals("Scituate"));
		
		// test that exact/non-exact flag works properly 
		Assert.assertTrue(ahService.searchHierarchy("Bosto", -1, false).get(0).getName().equals("Boston"));
		Assert.assertTrue(ahService.searchHierarchy("Bosto", -1, true).size() == 0);
		
	}
	
}
