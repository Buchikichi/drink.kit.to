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

		$('#createButton').attr('href', '/item/create?lang=' + lang);
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
