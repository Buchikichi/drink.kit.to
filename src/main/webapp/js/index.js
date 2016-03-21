$(document).ready(function() {
	var env = new Environment();
	var language = new Language(env, '#languageList');
	var country = new Country('#countryList');
	var item = new Item(language, country);
	var listView = $('#listView');
	var onLanguageChanged = function() {
		var keyword = $('input[data-type=search]').val();

		$('#languageList input').change(function() {
			console.log('change.');
			language.load().then(onLanguageChanged);
		});
		country.load(language.getCurrentLanguage()).then(function() {
			item.list(listView, keyword).then(function() {
				env.hideLoading();
			});
		});
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
