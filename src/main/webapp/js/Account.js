function Account() {
	this.user = null;
}

Account.prototype.getUser = function() {
	var account = this;

	return $.ajax('/account/', {
		'type': 'POST',
		'data': {},
		'success': function(data) {
			account.user = data;
		}
	});
}
