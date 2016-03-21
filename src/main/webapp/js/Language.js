function Language(env, listId) {
	this.env = env;
	this.listView = $(listId);
	this.setupDefaultLanguage();
}

Language.prototype.setupDefaultLanguage = function() {
	var params = this.env.params;

	if (params['lang']) {
		this.lang = params['lang'];
	} else {
		this.lang = (navigator.browserLanguage || navigator.language || navigator.userLanguage).substr(0,2);
	}
};

Language.prototype.getCurrentLanguage = function() {
	var radio = this.listView.find(':checked');
	var lang = radio.val();

	if (lang) {
		this.lang = lang;
	}
	return this.lang;
};

Language.prototype.load = function() {
	var listView = this.listView;
	var container = listView.controlgroup('container');
	var lang = this.getCurrentLanguage();

	return $.ajax('/list/language/', {
		'type': 'POST',
		'data': {'lang': lang},
		'success': function(data) {
			container.empty();
			data.forEach(function(rec) {
				var id = 'lang-' + rec.id;
				var input = $('<input type="radio"/>').attr('id', id).attr('name', 'lang').val(rec.id);
				var label = $('<label></label>').text(rec.text).attr('for', id);

				if (lang == rec.id) {
					input.prop('checked', true);
				}
				container.append(input);
				container.append(label);
			});
			listView.parent().trigger('create');
		}
	});
};
