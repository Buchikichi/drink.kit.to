function Environment() {
	var expressions = window.location.search.substring(1).split('&');
	var params = [];

	expressions.forEach(function(exp) {
		var element = exp.split('=');

		params[element[0]] = element[1];
	});
	this.params = params;
	this.loading = false;
}

Environment.INSTANCE = new Environment();

Environment.prototype.showLoading = function() {
	this.loading = true;
	$.mobile.loading('show', {
		text: 'Loading...',
		textVisible: true,
		theme: 'b'
	});
};

Environment.prototype.hideLoading = function() {
	$.mobile.loading('hide');
	this.loading = false;
};
