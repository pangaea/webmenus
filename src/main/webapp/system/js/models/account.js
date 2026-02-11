// Filename: models/account
define([
  'underscore',
  'backbone'
], function(_, Backbone){
	
	var AccountModel = Backbone.Model.extend({
		defaults: {
			"id": "",
			"name": "",
			"created": null
		},
		idAttribute: "id",
		urlRoot: '/webmenus/api/accounts'
	});
	
	// Return the model for the module
	return AccountModel;
});