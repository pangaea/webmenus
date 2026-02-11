// Filename: collections/accounts
define([
  'underscore',
  'backbone',
  'models/account'
], function(_, Backbone, AccountModel){
	
	var AccountCollection = Backbone.Collection.extend({
		model: AccountModel,
		parse: function(response){
			return response.items;
		},
		url: "/webmenus/api/accounts"
	});
	
	// Return the collection for the module
	return AccountCollection;
});