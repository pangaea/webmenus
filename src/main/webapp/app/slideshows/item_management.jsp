<ol id="help_slideshow" style="display:none;">
    <li data-text="Next" data-options="width:400px;">
      <h2>Item Management</h2>
      <p>Items (Locations, Themes, etc) can be managed in the same way. Click 'next' to see a brief overview.</p>
    </li>
	<li data-class="ui-jqgrid-bdiv" data-text="Next">
      <h2>Item List</h2>
      <p>List of all items, or items in the current search results.</p>
	</li>
	<li data-id="Button_New" data-text="Next">
      <h2>Create New</h2>
      <p>Click here to create a new item. Fill in the properties and click 'save'.</p>
	</li>
	<li data-class="ui-tabs-nav" data-text="Next">
      <h2>Tabs</h2>
      <p>Tabs allow you to view items related to the current selection.</p>
	</li>
	<li data-id="Button_Edit" data-text="Next">
      <h2>Edit</h2>
      <p>You can edit an items' properties and save the changes.</p>
	</li>
	<li data-text="Done" data-options="width:400px;">
      <h2>Enjoy chowMagic</h2>
      <p>Hope you've got the basic understanding of item management now. You can start this tour again from the menu above.</p>
	</li>
</ol>
<script type="text/javascript">
$(function(){
	$("#help_slideshow").joyride({
		'autoStart' : true,
		'scrollSpeed' : 0,
		'localStorage' : true,
	    'localStorageKey' : 'joyride_view_obj_<%=info.m_UserName%>'
	});
});
</script>