function Tags(listId, language) {
	this.listView = $(listId);
	this.language = language;
	this.id = null;
}

Tags.prototype.load = function() {
	var env = Environment.INSTANCE;
	var tags = this;
	var listView = this.listView;
	var container = listView.controlgroup('container');
	var lang = this.language.getCurrentLanguage();

	env.showLoading();
	return $.ajax('/tags/list/', {
		'type': 'POST',
		'data': {'lang': lang},
		'success': function(data) {
			container.empty();
			data.forEach(function(rec) {
				var id = 'tag-' + rec.id;
				var div = $('<div></div>').addClass('ui-checkbox');
				var input = $('<input type="checkbox"/>').attr('id', id).val(rec.id);
				var label = $('<label></label>').attr('for', id).append(rec.text);

				div.attr('data-filtertext', rec.filtertext);
				div.append(input);
				div.append(label);
				container.append(div);
				div.dblclick(function() {
					tags.id = rec.id;
					$('#tagsPopup input[type=text]').val(rec.text);
					$('#tagsPopup').popup('open', {});
				});
			});
			listView.parent().trigger('create');
			env.hideLoading();
console.log('success: Tags.load()');
		}
	});
};

Tags.prototype.save = function(tag) {
	var env = Environment.INSTANCE;
	var tags = this;
	var listView = this.listView;
	var container = listView.controlgroup('container');
	var data = {
		'lang': this.language.getCurrentLanguage(),
		'text': tag
	};

	env.showLoading();
	if (this.id) {
		data['id'] = this.id;
	}
	return $.ajax('/tags/save/', {
		'type': 'POST',
		'data': data,
		'success': function(data) {
console.log('success: Tags.save()');
			$('#tagsPopup input[type=text]').val('');
			tags.id = null;
			tags.load();
		}
	});
};

Tags.prototype.listSelected = function() {
	var list = [];

	this.listView.find(':checked').each(function() {
console.log($(this).val());
		list.push($(this).val());
	});
	return list;
};

Tags.prototype.makeTags = function(tagList) {
	var tags = [];
	var listView = this.listView;

	tagList.forEach(function(id) {
		var tagId = 'tag-' + id;
		var text = listView.find('label[for=' + tagId + ']').text();

		tags.push($('<abbr></abbr>').data('id', id).text(text));
	});
	return tags;
};
