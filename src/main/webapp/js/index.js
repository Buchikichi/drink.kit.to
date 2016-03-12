$(document).ready(function() {
	loadLanguage();
	loadCountry();
});
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
				var img = $('<img/>').attr('src', 'data:image/png;base64,' + rec.flag);
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
