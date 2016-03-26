$(document).ready(function() {
	var env = new Environment();
	var language = new Language(env);
	var lang = language.getCurrentLanguage();
	var country = new Country('#countryList');
	var tags = new Tags('#tagsList', language);
	var item = new Item(language, country, tags);
	var id = env.params['id'];
	var editBtn = $('#editBtn');

	env.showLoading();
	country.load(lang).then(function() {
		$('#countryList input').change(function() {
			item.setCountryCd($(this).val());
			console.log('country changed.');
		});
		tags.load().then(function() {
			env.hideLoading();
			if (id) {
				item.read(id).then(function() {
					country.select(item.item.countryCd);
				});
			} else {
				item.isEdit = true;
				item.show();
			}
		});
	});
	$('#namePopup a:last').click(function() {
		item.setText($('input[name=name]').val());
	});
	$('#abvPopup a:last').click(function() {
		item.setAbv($('input[name=abv]').val());
	});
	$('#commitButton').click(function() {
		var form = document.getElementById('detailForm');
		var fd = new FormData(form);

		env.showLoading();
		item.save(fd).then(function() {
			env.hideLoading();
			alert('saved.');
		});
	});
	editBtn.click(function() {
		item.isEdit = !item.isEdit;
		item.show();
	});
	if (!id) {
		editBtn.hide();
	}
	$('#decideTagsButton').click(function() {
		item.setTags(tags.listSelected());
	});
	$('#tagsPopup a:last').click(function() {
		tags.save($('input[name=tag]').val());
	});
	console.log('ready!!!');
});
