$(document).ready(function() {
	loadItem();
	initControls();
	console.log('ready!!!');
});

function initControls() {
	$('#imageFile').change(function() {
		var file = this.files[0];
		var type = file.type;

		if (type.indexOf('image') != 0) {
			alert('画像を選択してください。');
			return;
		}
		var form = $('form').get(0);
		var fd = new FormData(form);

		fd.append('name', file.name);
		fd.append('type', type);
		$.ajax('/picture/create/', {
			'type': 'POST',
			'data': fd,
			'processData': false,
			'contentType': false,
		}).then(function(data) {
			console.log('uploaded');
		});
	});
}

function loadItem() {
	var lang = 'ja';
	var id = '59262e6fbafceb2b66a03bd2ec1d9f98';

console.log('lang:' + lang);
	$.ajax('/item/read/', {
		'type': 'POST',
		'data': {
			'lang': lang,
			'id': id
		},
		'success': function(rec) {
			var flag = getFlag(rec.countryCd);
			var name = $('<span></span>').addClass('name').append(flag).append(rec.text);
			var abv = $('<span></span>').addClass('abv').text('7.0');
			var attribute =$('.attribute').addClass('attribute').append(abv);
			var plusButton = $('<span>+</span>').addClass('plusButton');
			var comment = 'スクールモン修道院で醸造されるトラピストビール。\nフルーティーな香りがある。';

			['アビー', 'エール', 'トラピスト', 'ベルギービール'].forEach(function(txt) {
				attribute.append($('<abbr></abbr>').text(txt));
			});
			attribute.append(plusButton);
			$('img:first').attr('src', rec.imgsrc);
			$('.name').append(name);
			$('.comment').text(comment);
console.log('success: item/read/');
		}
	});
}

function getFlag() {
	return $('<img/>');
}
