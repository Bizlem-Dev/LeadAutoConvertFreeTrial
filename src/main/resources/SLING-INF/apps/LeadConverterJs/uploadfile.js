<script>
$(document).ready(function() {
		var fileInput = document.getElementById('selectedFile2');
		var fileDisplayArea = document.getElementById('editor2');

		fileInput.addEventListener('change', function(e) {
			var file = fileInput.files[0];
			var textType = /html.*/;

			if (file.type.match(textType)) {
				var reader = new FileReader();

				reader.onload = function(e) {
					fileDisplayArea.innerHTML = reader.result;
				}

				reader.readAsText(file);	
			} else {
				fileDisplayArea.innerHTML = "File type not supported!"
			}
		});
})
</script>

<script>
$(document).ready(function() {
		var fileInput = document.getElementById('selectedFile3');
		var fileDisplayArea = document.getElementById('editor3');

		fileInput.addEventListener('change', function(e) {
			var file = fileInput.files[0];
			var textType = /html.*/;

			if (file.type.match(textType)) {
				var reader = new FileReader();

				reader.onload = function(e) {
					fileDisplayArea.innerHTML = reader.result;
				}

				reader.readAsText(file);	
			} else {
				fileDisplayArea.innerHTML = "File type not supported!"
			}
		});
})
</script>

<script>
$(document).ready(function() {
		var fileInput = document.getElementById('selectedFile4');
		var fileDisplayArea = document.getElementById('editor4');

		fileInput.addEventListener('change', function(e) {
			var file = fileInput.files[0];
			var textType = /html.*/;

			if (file.type.match(textType)) {
				var reader = new FileReader();

				reader.onload = function(e) {
					fileDisplayArea.innerHTML = reader.result;
				}

				reader.readAsText(file);	
			} else {
				fileDisplayArea.innerHTML = "File type not supported!"
			}
		});
})
</script>

<script>
$(document).ready(function() {
		var fileInput = document.getElementById('selectedFile5');
		var fileDisplayArea = document.getElementById('editor5');

		fileInput.addEventListener('change', function(e) {
			var file = fileInput.files[0];
			var textType = /html.*/;

			if (file.type.match(textType)) {
				var reader = new FileReader();

				reader.onload = function(e) {
					fileDisplayArea.innerHTML = reader.result;
				}

				reader.readAsText(file);	
			} else {
				fileDisplayArea.innerHTML = "File type not supported!"
			}
		});
})
</script>
