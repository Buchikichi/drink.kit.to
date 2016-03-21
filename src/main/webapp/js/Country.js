function Country(listId) {
	this.listView = $(listId);
}

Country.prototype.load = function(lang) {
	var listView = this.listView;
	var container = listView.controlgroup('container');
	var type = listView.data('type');

	return $.ajax('/list/country/', {
		'type': 'POST',
		'data': {'lang': lang},
		'success': function(data) {
			container.empty();
			data.forEach(function(rec) {
				var id = '[' + rec.id + ']';
				var img = $('<img/>').attr('src', 'data:image/png;base64,' + rec.flag).attr('alt', id);
				var input = $('<input/>').attr('type', type).attr('id', id).attr('name', 'lang').val(rec.id);
				var label = $('<label></label>').attr('for', id).append(img).append(' ' + rec.text);

				if (lang == rec.id) {
					input.prop('checked', true);
				}
				container.append(input);
				container.append(label);
				input.change(function() {
					container.attr('data-lang', $(this).val());
				});
			});
			listView.parent().trigger('create');
console.log('success: Country.load()');
		}
	});
};

Country.prototype.select = function(countryCd) {
	var input = $('input[id*=' + countryCd + ']');

	input.prop('checked', true).checkboxradio('refresh');
};

Country.prototype.getFlag = function(countryCd) {
	if (countryCd) {
		var img = this.listView.find('img[alt*=' + countryCd + ']').clone();
	} else {
		var img = $('<img/>').attr('alt', '[?]');
	}
	return img;
};
