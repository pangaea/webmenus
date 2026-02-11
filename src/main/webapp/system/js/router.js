// Filename: router.js
define([
  'jquery',
  'underscore',
  'backbone',
  'views/accounts/list',
  'views/accounts/details'
], function($, _, Backbone, AccountListView, AccountDetailsView){
  var AppRouter = Backbone.Router.extend({
    routes: {
    	// Define some URL routes
    	"": "showAccounts",
		"account/:accountid": "showAccountDetails",

      // Default
      "*actions": "defaultAction"
    },
    showAccounts: function() {
		var homeView = new AccountListView();
		homeView.render();
	},
	showAccountDetails: function(accountid) {
      var detailsView = new AccountDetailsView();
      detailsView.accountId = accountid;
      detailsView.render();
	},
	defaultAction: function(actions){
		// We have no matching route, lets just log what the URL was
		console.log('No route:', actions);
	}
  });

  var initialize = function(){
    var app_router = new AppRouter;
    Backbone.history.start();
  };
  return {
    initialize: initialize
  };
});
