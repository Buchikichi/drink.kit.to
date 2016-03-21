$(document).ready(function() {
	var env = new Environment();
	var language = new Language(env);
	var lang = language.getCurrentLanguage();
	var country = new Country('#countryList');
	var item = new Item(language, country);
	var id = env.params['id'];
	var editBtn = $('#editBtn');

	env.showLoading();
	country.load(lang).then(function() {
		$('#countryList input').change(function() {
			item.setCountryCd($(this).val());
			console.log('country changed.');
		});
		if (id) {
			item.read(id).then(function() {
				country.select(item.item.countryCd);
			});
		} else {
			item.isEdit = true;
			item.show();
		}
		env.hideLoading();
	});

//	initControls();
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
	console.log('ready!!!');
});

/*
function initControls() {
	$('#imageFile').change(function() {
		var file = this.files[0];
		var type = file.type;

		if (type.indexOf('image') != 0) {
			alert('画像を選択してください。');
			return;
		}
//		var form = $('form').get(0);
//		var fd = new FormData(form);
//
//		fd.append('name', file.name);
//		fd.append('type', type);
//		$.ajax('/picture/create/', {
//			'type': 'POST',
//			'data': fd,
//			'processData': false,
//			'contentType': false,
//		}).then(function(data) {
//			console.log('uploaded');
//		});
	});
}
//*/
