var $j = jQuery;	

function addressFieldChange(element) {
		
	var searchString = '';

	// we need to iterate through all the address hiearchy levels from top to bottom
	// for all levels *above* the next element, we need to build a search string in the 
	// format "UNITED STATES|MASSACHUSETTS|SUFFOLK";
	// then the element and all levels below it need to be emptied;
	var reachedLevelToUpdate = false;
	
	$j.each(addressHierarchyLevels, function (i, entry) {		
		if (element.attr('class') == entry) { 
			reachedLevelToUpdate = true; 
		}
		if (reachedLevelToUpdate == false) {
			// build the search string	
			searchString = searchString + element.closest('.address').find('.' + entry).val() + "|";
		}
		else {
			// empty the other entries
			element.closest('.address').find('.' + entry).empty(); 
		}
	});

	// slice off the trailing "|"
	if (searchString != null) {
		searchString = searchString.slice(0,-1);
	}
		
	updateOptions(element, searchString);
}

function updateOptions(element, searchString) {	
	// do the JSON call and add the appropriate elements
	$j.getJSON(pageContext + '/module/addresshierarchy/ajax/getChildAddressHierarchyEntries.form',
			{ 'searchString': searchString },
			function (data) {
				element.append($j(document.createElement("option")).text("--"));
				$j.each(data, function(i, entry) {
					element.append($j(document.createElement("option")).attr("value", entry.name).text(entry.name));
				});
			}
		);
}

