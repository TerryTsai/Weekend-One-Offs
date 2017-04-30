// General
var size = 128;
var audio = document.querySelectorAll("video")[0];
var buffer = new Uint8Array(size);

// Components
var context = new window.AudioContext();
var sourced = context.createMediaElementSource(audio);
var analyze = context.createAnalyser();

// Setup
analyze.fftSize = size;
sourced.connect(analyze);
analyze.connect(context.destination);

// UI
var bars = new Array(size).fill(0).map((v,i) => {
	var bar = document.createElement("div");
	bar.style.position = "fixed";
	bar.style.bottom = 0;
	bar.style.left = (100 / size * i) + "%";
	bar.style.width = (100 / size) + "%";
	bar.style.zIndex = 2000000000;
	document.body.appendChild(bar);
	return bar;
});

var caps = new Array(size).fill(0).map((v,i) => {
	var cap = document.createElement("div");
	cap.style.background = "red";
	cap.style.position = "fixed";
	cap.style.height = "4px";
	cap.style.left = (100 / size * i) + "%";
	cap.style.width = (100 / size) + "%";
	cap.style.zIndex = 2000000000;
	document.body.appendChild(cap);
	return cap;
});

// Action
function action() {
	analyze.getByteFrequencyData(buffer);
	for (var i = 0; i < size; i++) {
		var val = Math.floor(buffer[i] * 3);
		var max = Math.max(0, val, caps[i].last ? caps[i].last - 1 : 0);

		bars[i].style.background = getcolor(buffer[i] / 255);
		bars[i].style.height = val + "px";
		caps[i].style.bottom = max + "px";
		caps[i].last = max;
	}
};

function getcolor(unit) {
	var r, g, b;
	var x = unit * 255;
	var nor = (x % 85) / 85;

	if (x < 85) {
		r = (1 - nor) * 255;
		g = nor * 255;
		b = 0;
	} else if (x < 171) {
		r = 0;
		g = (1 - nor) * 255;
		b = nor * 255;
	} else {
		r = nor * 255;
		g = 0;
		b = (1 - nor) * 255;
	}

	r = Math.floor(r);
	g = Math.floor(g);
	b = Math.floor(b);

	return "rgb(" + r + "," + g + "," + b + ")";
};

// Start
setInterval(action, 1);