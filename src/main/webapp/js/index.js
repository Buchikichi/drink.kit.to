$(document).ready(function() {
	var env = new Environment();
	var language = new Language(env, '#languageList');
	var country = new Country('#countryList');
	var tags = new Tags('#tagsList', language);
	var item = new Item(language, country, tags);
	var listView = $('#listView');
	var onLanguageChanged = function() {
		var lang = language.getCurrentLanguage();

		$('#languageList input').change(function() {
			language.load().then(onLanguageChanged);
		});
		country.load(lang).then(function() {
			var keyword = $('input[data-type=search]').val();

			item.list(listView, keyword).then(function() {
				env.hideLoading();
			});
		});
		tags.load();
		env.showLoading();
	};

	language.load().then(onLanguageChanged);
	listView.on('filterablebeforefilter', function(e, data) {
		var keyword = data.input.val().trim();

		env.showLoading();
		item.list(listView, keyword).then(function() {
			env.hideLoading();
		});
	});
});
/*
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
//*/
