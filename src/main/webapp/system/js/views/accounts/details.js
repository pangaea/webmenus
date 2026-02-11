// Filename: views/project/list
define([
  'jquery',
  'underscore',
  'backbone',
  'models/account',
  // Using the Require.js text! plugin, we are loaded raw text
  // which will be used as our views primary template
  'text!/webmenus/system/templates/accounts/details.html'
], function($, _, Backbone, AccountModel, accountDetailsTemplate){
	var AccountDetailsView = Backbone.View.extend({
		el: $('#viewport'),
		accountId: null,
		template: '',
		initialize: function(){
			var self = this;
			_.bindAll(this, 'render');
			this.template = _.template( accountDetailsTemplate, {} );
		},
		render: function(){
			var self = this;
			this.model = new AccountModel({"id": this.accountId});
			this.model.fetch({
				success: function(){
					self.$el.html(self.template(self.model.attributes));
				}
			});
			return this; // for chainable calls, like .render().el
		}
	});
	return AccountDetailsView;
});