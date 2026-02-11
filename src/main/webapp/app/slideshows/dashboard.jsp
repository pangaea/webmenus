<ol id="help_slideshow" style="display:none;">
    <li data-text="Next" data-options="width:400px;">
      <h2>Welcome to chowMagic</h2>
      <em><strong>Here's the basics, to get you up and running quickly...</strong></em>
      <ol style="font-weight:400;">
      	<li>Open the <b>Menu Designer</b> from the sample restaurant location and edit it to match your own menus (you can do this any time you want to make a change).</li>
      	<li>View the menus for your location by clicking <b>View Live Menus</b>.</li>
      	<li><b>Menus Link</b> displays the code you can place on your web site to make your menus public.</li>
      	<li>Use <b>Themes</b> to change how your menus look. Click on the location's name to change it's theme.</li>
      	<li>Use <b>Schedules</b> to make your menus open to take orders (make sure you set a valid email address for the location first). Open the <b>Menu Designer</b>, click on a menu, set <b>Accept Orders</b> to <b>Schedule</b> and choose a schedule for that menu.</li>
      </ol>
      <p>Click 'Next' for a brief tour.</p>
    </li>
	<li data-id="location-title" data-text="Next">
      <h2>Locations</h2>
      <p>Locations represent a restaurants' location. All menus and orders are associated with a location.</p>
	</li>
	<li data-id="location-designer-0" data-text="Next">
      <h2>Menu Designer</h2>
      <p>Use this tool to create and edit this locations' menus.</p>
	</li>
	<li data-id="location-view-0" data-text="Next">
      <h2>View Live Menus</h2>
      <p>Click here to see the live menus for this location.</p>
	</li>
	<li data-id="location-link-0" data-text="Next">
      <h2>View Menus Link</h2>
      <p>View the link to this locations' menus. Copy this link and paste it someplace on your site.</p>
	</li>
	<li data-id="location-orders-0" data-text="Next">
      <h2>View Orders</h2>
      <p>View the orders for this location.</p>
	</li>
	<li data-id="themes" data-text="Next" data-options="tipLocation:top;">
      <h2>Themes</h2>
      <p>You can change the look and feel of a locations' menus using themes. Themes allow you to change colors, fonts, layout, etc.</p>
	</li>
	<li data-id="schedules" data-text="Next" data-options="tipLocation:top;">
      <h2>Schedules</h2>
      <p>A schedule defines the time in which a menu is available to take orders. Use a schedule to automatically open and close selected menus.</p>
	</li>
	<li data-id="accordion" data-text="Next">
      <h2>Navigation</h2>
      <p>Navigate directly to information using convenient links.</p>
	</li>
	<li data-id="imageLibrary" data-text="Next">
      <h2>Image Library</h2>
      <p>Upload and manage custom images using the image library tool.</p>
	</li>
	<li data-text="Done" data-options="width:400px;">
      <h2>Enjoy chowMagic</h2>
      <p>Hope you've got the basic understanding now. You can start this tour again from the menu above.</p>
	</li>
</ol>
<script type="text/javascript">
$(function(){
	$("#help_slideshow").joyride({
		'autoStart' : true,
		'scrollSpeed' : 0,
		'localStorage' : true,
	    'localStorageKey' : 'joyride_summary_<%=info.m_UserName%>'
	});
});
</script>