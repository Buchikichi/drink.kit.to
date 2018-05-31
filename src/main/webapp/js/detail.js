$(document).ready(function() {
	var env = new Environment();
	var language = new Language(env);
	var lang = language.getCurrentLanguage();
	var country = new Country('#countryList');
	var tags = new Tags('#tagsList', language);
	var item = new Item(language, country, tags);
	var account = new Account();
	var id = env.params['id'];
	var editBtn = $('#editBtn').hide();

	env.showLoading();
	country.load(lang).then(function() {
		$('#countryList input').change(function() {
			item.setCountryCd($(this).val());
			console.log('country changed.');
		});
		tags.load().then(function() {
			if (id) {
				item.read(id).then(function() {
					$('#detail').show();
					env.hideLoading();
				});
			} else {
				$('#detail').show();
				item.isEdit = true;
				item.show();
				env.hideLoading();
			}
		});
	});
	$('#namePopup a:last').click(function() {
		item.setText($('input[name=name]').val());
	});
	$('#abvPopup a:last').click(function() {
		item.setAbv($('input[name=abv]').val());
	});
	$('#descriptionPopup a:last').click(function() {
		item.setDescription($('#descriptionPopup [name=description]').val());
	});
	$('#commitButton').click(function() {
		var btn = $(this);
		var form = document.getElementById('detailForm');
		var fd = new FormData(form);

		btn.attr('disabled', true);
		env.showLoading();
		item.save(fd).then(function() {
			$.mobile.silentScroll(0);
			setTimeout(function() {
				env.showMessage('Saved.');
			}, 300);
			btn.removeAttr('disabled');
			env.hideLoading();
		});
	});
	editBtn.click(function() {
		item.isEdit = !item.isEdit;
		item.show();
	});
	account.getUser().then(function() {
		if (id && account.user.email) {
			editBtn.show();
		}
	});
	$('#decideTagsButton').click(function() {
		item.setTags(tags.listChecked());
	});
	$('#tagsPopup a:last').click(function() {
		tags.save($('input[name=tag]').val());
	});
	console.log('ready!!!');
});
