/*
 * These are convenient helper functions for Handlebars templates
 */
Handlebars.registerHelper('ifequal', function (conditional, options) {
	if (options.hash.value === conditional) {
		return options.fn(this)
	} else {
		return options.inverse(this);
	}
});