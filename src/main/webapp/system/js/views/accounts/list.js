// Filename: views/project/list
define([
  'jquery',
  'underscore',
  'backbone',
  'collections/accounts',
  // Using the Require.js text! plugin, we are loaded raw text
  // which will be used as our views primary template
  'text!/webmenus/system/templates/accounts/list.html',
  'text!/webmenus/system/templates/accounts/list_row.html'
], function($, _, Backbone, AccountCollection, accountListTemplate, accountListRowTemplate){
	
	var AccountRowView = Backbone.View.extend({
		tagName: 'li', // name of (orphan) root tag in this.el
		template: '',
		initialize: function(){
			_.bindAll(this, 'render', 'unrender', 'remove');
			//this.template = _.template($('#account_row').html());
			this.template = _.template( accountListRowTemplate, {} );
		},
		render: function(){
			this.$el.html(this.template(this.model.attributes));
			return this; // for chainable calls, like .render().el
		},
		unrender: function(){
			$(this.el).remove();
		},
		remove: function(){
			this.model.destroy();
		}
	});
	
  	var AccountListView = Backbone.View.extend({
		el: $('#viewport'),
		template: '',
		initialize: function(){
			_.bindAll(this, 'render', 'appendItem');
			this.template = _.template( accountListTemplate, {} );
		},
		render: function(){
			var self = this;
			this.$el.html(this.template);
			this.collection = new AccountCollection();
			this.collection.fetch({
				success: function(collection, response) {
					_(collection.models).each(function(item){
						self.appendItem(item);
					}, this);
				}
			});
			return this;
		},
		appendItem: function(item){
			var accountRowView = new AccountRowView({
				model: item
			});
			$('ul', this.el).append(accountRowView.render().el);
		}
	});
  	
  	// Our module now returns our view
	return AccountListView;
});