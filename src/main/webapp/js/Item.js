function Item(language, country) {
	this.language = language;
	this.country = country;
	this.isEdit = false;
	this.item = {
		text: 'no name',
		countryCd: '',
		abv: '?'
	};
}

Item.prototype.list = function(listView, keyword) {
	var lang = this.language.getCurrentLanguage();
	var country = this.country;

	return $.ajax('/item/list/', {
		'type': 'POST',
		'data': {
			'lang': lang,
			'keyword': keyword
		},
		'success': function(data) {
			listView.empty();
			data.forEach(function(rec) {
				var li = $('<li></li>').attr('class', 'cell').attr('data-id', rec.id);
				var flag = country.getFlag(rec.countryCd);
				var name = $('<span></span>').addClass('name').append(flag).append(rec.text);
				var pict = $('<img/>').attr('src', rec.imgsrc);
				var href = 'detail.html?lang=' + lang + '&id=' + rec.id;
				var anchor = $('<a></a>').attr('data-ajax', 'false').attr('href', href)
						.append(pict).append(name);
				var abv = $('<span></span>').addClass('abv').text(rec.abv);
				var attribute = $('<span></span>').addClass('attribute').append(abv);
				var description = $('<span></span>').addClass('description').text('スクールモン修道院で醸造されるトラピストビール。\nフルーティーな香りがある。');

				['アビー', 'エール', 'トラピスト', 'ベルギービール'].forEach(function(txt) {
					attribute.append($('<abbr></abbr>').text(txt));
				});
				anchor.append(attribute);
				anchor.append(description);
				li.attr('data-filtertext', keyword);
				li.append(anchor);
				listView.append(li);
				//li.click(showItem);
			});
			listView.filterable('refresh');
console.log('success: Item.list()');
		}
	});
};

Item.prototype.read = function(id) {
	var item = this;
	var lang = this.language.getCurrentLanguage();

	return $.ajax('/item/read/', {
		'type': 'POST',
		'data': {
			'lang': lang,
			'id': id
		},
		'success': function(rec) {
			item.item = rec;
			item.show();
		}
	});
};

Item.prototype.show = function() {
	var commitButton = $('#commitButton');

	if (this.isEdit) {
		commitButton.show();
	} else {
		commitButton.hide();
	}
	this.showName();
	this.showPicture();
	this.showAttribute();
	this.showDescription();
};

Item.prototype.showName = function() {
	var rec = this.item;
	var flag = this.country.getFlag(rec.countryCd);
	var name = rec.text;

	if (this.isEdit) {
		flag = $('<a></a>').attr('href', '#countryPanel').append(flag);
		name = $('<a href="#namePopup"></a>').attr('data-rel', 'popup').text(name);
		name.click(function() {
			$('input[name=name]').val(rec.text);
		});
	}
	$('.name').empty().append(flag).append(name);
};

Item.prototype.showPicture = function() {
	var rec = this.item;
	var imageFile = $('#imageFile').parent('.ui-input-text');

	$('#picture > img').attr('src', rec.imgsrc);
	if (this.isEdit) {
		imageFile.show();
	} else {
		imageFile.hide();
	}
};

Item.prototype.showAttribute = function() {
	var rec = this.item;
	var attribute = $('.attribute').empty();
	var abv = $('<span></span>').addClass('abv').text(rec.abv);

	if (this.isEdit) {
		abv = $('<a></a>').attr('href', '#abvPopup').attr('data-rel', 'popup').append(abv);
		abv.click(function() {
			$('input[name=abv]').val(rec.abv);
		});
	}
	attribute.append(abv);
	['アビー', 'エール', 'トラピスト', 'ベルギービール'].forEach(function(txt) {
		attribute.append($('<abbr></abbr>').text(txt));
	});
	if (this.isEdit) {
		var plusButton = $('<a>+</a>').attr('href', '#tagsPanel').addClass('plusButton');

		attribute.append(plusButton);
	}
};

Item.prototype.showDescription = function() {
	var rec = this.item;
	var description = 'スクールモン修道院で醸造されるトラピストビール。\nフルーティーな香りがある。';

	$('textarea[name=description]').text(description);
	if (this.isEdit) {
		description = $('<a href="#descriptionPopup"></a>').attr('data-rel', 'popup').text(description);
	}
	$('.description').empty().append(description);
};

Item.prototype.setText = function(text) {
	this.item.text = text;
	this.showName();
};

Item.prototype.setCountryCd = function(countryCd) {
	this.item.countryCd = countryCd;
	this.showName();
};

Item.prototype.setAbv = function(abv) {
	this.item.abv = abv;
	this.showAttribute();
};

Item.prototype.save = function(fd) {
	var item = this;
	var rec = this.item;

	fd.append('lang', this.language.getCurrentLanguage());
	Object.getOwnPropertyNames(rec).forEach(function(key) {
		fd.append(key, rec[key]);
	});
	return $.ajax('/item/save/', {
		'type': 'POST',
		'data': fd,
		'processData': false,
		'contentType': false,
		'success': function(rec) {
			item.item = rec;
			item.isEdit = false;
			item.show();
		}
	});
};
