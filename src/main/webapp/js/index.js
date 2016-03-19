$(document).ready(function() {
	loadLanguage();
	loadCountry();
	listItem();
	initControls();
});

function initControls() {
	$('#searchList').on('filterablebeforefilter', filterItem);
//	$(document).bind("mobileinit", function(){  
//		$.mobile.page.prototype.options.addBackBtn = false;  
//		$.mobile.ajaxEnabled = false;  
//	});  
}

function getCurrentLanguage() {
	var languageList = $('#languageList').controlgroup('container');
	var lang = languageList.attr('data-lang');

	if (!lang) {
		lang = (navigator.browserLanguage || navigator.language || navigator.userLanguage).substr(0,2);
	}
	return lang;
}

function loadLanguage() {
	var languageList = $('#languageList').controlgroup('container');
	var lang = getCurrentLanguage();

	$.ajax('/list/language/', {
		'type': 'POST',
		'data': {'lang': lang},
		'success': function(data) {
			languageList.empty();
			data.forEach(function(rec) {
				var id = 'lang-' + rec.id;
				var input = $('<input type="radio"/>').attr('id', id).attr('name', 'lang').val(rec.id);
				var label = $('<label></label>').text(rec.text).attr('for', id);

				if (lang == rec.id) {
					input.prop('checked', true);
				}
				languageList.append(input);
				languageList.append(label);
				input.change(function() {
					languageList.attr('data-lang', $(this).val());
					loadLanguage();
					loadCountry();
				});
			});
			$('#preferencePanel').trigger('create');
			console.log('success: loadLanguage()');
		}
	});
}

function loadCountry() {
	var countryList = $('#countryList').controlgroup('container');
	var lang = getCurrentLanguage();

	$.ajax('/list/country/', {
		'type': 'POST',
		'data': {'lang': lang},
		'success': function(data) {
			countryList.empty();
			data.forEach(function(rec) {
				var id = 'country-' + rec.id;
				var img = $('<img/>').attr('src', 'data:image/png;base64,' + rec.flag).attr('data-id', rec.id);
				var input = $('<input type="checkbox"/>').attr('id', id).attr('name', 'lang').val(rec.id);
				var label = $('<label></label>').attr('for', id).append(img).append(' ' + rec.text);

				if (lang == rec.id) {
					input.prop('checked', true);
				}
				countryList.append(input);
				countryList.append(label);
				input.change(function() {
					countryList.attr('data-lang', $(this).val());
				});
			});
			$('#countryPanel').trigger('create');
			console.log('success: loadLanguage()');
		}
	});
}

function filterItem(e, data) {
	var keyword = data.input.val().trim();

	listItem();
}

function getFlag(countryCd) {
	var img = $('img[data-id=' + countryCd + ']');

	return img.clone();
}

function listItem(keyword) {
	var searchList = $('#searchList');
	var lang = getCurrentLanguage();

	$.ajax('/item/list/', {
		'type': 'POST',
		'data': {
			'lang': lang,
			'keyword': keyword
		},
		'success': function(data) {
			searchList.empty();
			data.forEach(function(rec) {
				var li = $('<li></li>').attr('class', 'cell').attr('data-id', rec.id);
				var flag = getFlag(rec.countryCd);
				var name = $('<span></span>').addClass('name').append(flag).append(rec.text);
				var pict = $('<img/>').attr('src', rec.imgsrc);
				var href = 'detail.html?lang=' + lang + '?id=' + rec.id;
				var anchor = $('<a></a>').attr('data-ajax', 'false').attr('href', href)
						.append(pict).append(name);
				var abv = $('<span></span>').addClass('abv').text('7.0');
				var attribute = $('<span></span>').addClass('attribute').append(abv);
				var comment = $('<span></span>').addClass('comment').text('スクールモン修道院で醸造されるトラピストビール。\nフルーティーな香りがある。');

				['アビー', 'エール', 'トラピスト', 'ベルギービール'].forEach(function(txt) {
					attribute.append($('<abbr></abbr>').text(txt));
				});
				anchor.append(attribute);
				anchor.append(comment);
				li.attr('data-filtertext', keyword);
				li.append(anchor);
				searchList.append(li);
				//li.click(showItem);
			});
			searchList.filterable('refresh');
console.log('success: list-item');
		}
	});
}

function showItem() {
	var li = $(this);
	var id = li.attr('data-id');
	var name = li.find('.name').clone();
	var attributes = li.find('.attribute *').clone();
	var plusButton = $('<span>+</span>').addClass('plusButton');
	var comment = li.find('.comment').text();

	$('#detailPopup .name').empty().append(name);
	$('#detailPopup .attribute').empty().append(attributes).append(plusButton);
	$('#detailPopup .comment').text(comment);
	name.dblclick(function() {
		console.log('dbl');
	});
	console.log('ID:' + id);
}
