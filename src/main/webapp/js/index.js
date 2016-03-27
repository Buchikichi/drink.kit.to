$(document).ready(function() {
	var env = Environment.INSTANCE;
	var language = new Language(env, '#languageList');
	var country = new Country('#countryList');
	var tags = new Tags('#tagsList', language);
	var item = new Item(language, country, tags);
	var keyword = $('input[data-type=search]');
	var listView = $('#listView');
	var onLanguageChanged = function() {
		var lang = language.getCurrentLanguage();

		$('#createButton').attr('href', 'detail.html?lang=' + lang);
		$('#languageList input').change(function() {
			language.load().then(onLanguageChanged);
		});
		country.load(lang).then(function() {
			$('#countryList :checkbox').change(function() {
				keyword.change();
			});
			tags.load().then(function() {
				keyword.change();
			});
		});
	};

	language.load().then(onLanguageChanged);
	listView.on('filterablebeforefilter', function(e, data) {
		item.list(listView, data.input.val().trim());
	});
	$('a[href="#tagsPanel"]').click(function() {
		tags.atOpen();
	});
	$('#commitTagsButton').click(function() {
		tags.atCommit();
		keyword.change();
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
